package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.consumables.ConsumableEffects;
import com.zenyte.game.content.consumables.Drinkable;
import com.zenyte.game.content.consumables.Edible;
import com.zenyte.game.content.consumables.drinks.Potion;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.tombsofamascut.AbstractTOARaidArea;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.TOAManager;
import com.zenyte.game.content.tombsofamascut.encounter.BabaEncounter;
import com.zenyte.game.content.tombsofamascut.encounter.SecondWardenEncounter;
import com.zenyte.game.content.tombsofamascut.lobby.TOALobbyArea;
import com.zenyte.game.content.tombsofamascut.npc.Baba;
import com.zenyte.game.content.tombsofamascut.npc.Osmumten;
import com.zenyte.game.content.tombsofamascut.npc.TOANPC;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.music.Music;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.*;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.plugins.dialogue.OptionDialogue;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.utils.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static com.zenyte.game.content.tombsofamascut.TOAManager.OUTSIDE_LOCATION;

/**
 * @author Savions.
 */
public abstract class TOARaidArea extends AbstractTOARaidArea implements HitProcessPlugin, CannonRestrictionPlugin, LogoutPlugin, LayableObjectPlugin, MusicPlugin, DeathPlugin, TeleportPlugin, EdiblePlugin, DrinkablePlugin, DropPlugin {

	private static final int TELEPORT_NPC_ID = 11689;
	private static final int GHOST_PLAYER_NPC_ID = 11695;
	private static final Graphics TELEPORT_GFX = new Graphics(409);
	private static final SoundEffect TELEPORT_SOUND = new SoundEffect(198);
	protected final TOARaidParty party;
	protected final EncounterType encounterType;
	private final boolean onDiet;
	private final boolean deHydration;
	private final boolean overlyDraining;
	private final boolean quietPrayers;
	private final boolean deadlyPrayers;
	protected EncounterStage stage = EncounterStage.NOT_STARTED;
	private long challengeTime;
	protected int teamSize;
	private final ArrayList<WorldTask> runningTasks = new ArrayList<>();
	private String challengeName = "";
	private boolean tombsFailure;

	public TOARaidArea(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
		super(allocatedArea, copiedChunkX, copiedChunkY);
		this.party = party;
		this.encounterType = encounterType;
		onDiet = party.getPartySettings().isActive(InvocationType.ON_A_DIET);
		deHydration = party.getPartySettings().isActive(InvocationType.DEHYDRATION);
		overlyDraining = party.getPartySettings().isActive(InvocationType.OVERLY_DRAINING);
		quietPrayers = party.getPartySettings().isActive(InvocationType.QUIET_PRAYERS);
		deadlyPrayers = party.getPartySettings().isActive(InvocationType.DEADLY_PRAYERS);
	}

	@Override
	public boolean isRaidArea() {
		return true;
	}

	@Override public void enter(Player player) {
		player.getTOAManager().sendHud();
		party.getPlayers().forEach(p -> p.getTOAManager().refreshHudStates());
		player.setViewDistance(Player.SCENE_DIAMETER);
	}

	@Override public void leave(Player player, boolean logout) {
		final RegionArea nextArea = GlobalAreaManager.getArea(player.getLocation());
		if (logout) {
			party.leave(player, true);
			player.forceLocation(new Location(OUTSIDE_LOCATION));
		}
		if (!(nextArea instanceof TOARaidArea)) {
			if (!(nextArea instanceof TOALobbyArea)) {
				player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
			}
			if (!player.isFinished()) {
				player.getTOAManager().removeTOAItems();
			}
			party.leave(player, false);
			player.getAppearance().resetRenderAnimation();
			player.setRunSilent(false);
			player.setViewDistance(Player.SMALL_VIEWPORT_RADIUS);
			player.getPacketDispatcher().resetCamera();
			player.getHpHud().close();
			if (player.getVariables().getTime(TickVariable.SALT) > 0) {
				ConsumableEffects.resetSalt(player);
			}
			player.getVariables().resetScheduled();
			player.getAppearance().transform(-1);
			GameInterface.EQUIPMENT_TAB.open(player);
			GameInterface.INVENTORY_TAB.open(player);
		} else {
			player.getTOAManager().sendHud();
		}
		player.blockIncomingHits(5);
		party.getPlayers().forEach(p -> {
			p.getTOAManager().refreshHudStates();
			if (!(nextArea instanceof SecondWardenEncounter)) {
				p.getHpHud().close();
			}
		});
	}

	public void handleBarrier(final Player player, final WorldObject object, boolean quickUse) {
		if (player.getAppearance().isTransformedIntoNpc()) {
			player.getDialogueManager().start(new PlainChat(player, "A mysterious force prevents you from doing that."));
			return;
		}
		if (EncounterStage.COMPLETED.equals(stage)) {
			walkBarrier(player, object);
			return;
		}
		final boolean insideChallengeArea = insideChallengeArea(player);
		if (insideChallengeArea && EncounterStage.STARTED.equals(stage)) {
			player.getDialogueManager().start(new PlainChat(player, "You can't leave until the challenge is over."));
			return;
		}
		if (!insideChallengeArea) {
			final Runnable enterRunnable = () -> {
				if (EncounterStage.NOT_STARTED.equals(stage)) {
					startRoom();
					party.getPlayers().forEach(p -> p.sendMessage("Challenge started: " + getCurrentChallengeName(true)));
				}
				walkBarrier(player, object);
				player.getMusic().unlock(Music.get(encounterType.getSoundTrack()));
			};
			if (EncounterStage.STARTED.equals(stage)) {
				enterRunnable.run();
			} else {
				if (player.getTOAManager().needsAbandonRequest()) {
					player.getTOAManager().startAbandonDialogue("Begin the challenge", enterRunnable);
				} else if (!quickUse) {
					player.getDialogueManager().start(new OptionDialogue(player, "Begin the challenge?", new String[] {"Yes.", "No."}, new Runnable[] {enterRunnable, null}));
				} else {
					enterRunnable.run();
				}
			}
		}
	}

	public void handleTeleportCrystal(final Player player, boolean quickUse) {
		if (player.getAppearance().isTransformedIntoNpc()) {
			player.getDialogueManager().start(new PlainChat(player, "A mysterious force prevents you from doing that."));
			return;
		}
		final boolean isWardens = EncounterType.WARDENS_FIRST_ROOM.equals(encounterType);
		final Runnable enterRunnable = () -> {
			if (!isWardens && EncounterStage.NOT_STARTED.equals(stage)) {
				startRoom();
				party.getPlayers().forEach(p -> p.sendMessage("Challenge started: " + getCurrentChallengeName(false)));
			}
			final Direction faceDirection = Direction.getDirection(player.getLocation(), encounterType.getChallengeSpawnLocation());
			if (faceDirection != null) {
				player.setDirection(faceDirection.getDirection());
			}
			player.sendSound(TELEPORT_SOUND);
			player.setGraphics(TELEPORT_GFX);
			player.setLocation(getLocation(encounterType.getChallengeSpawnLocation()));
			player.getVariables().setRunEnergy(100);
			if (!isWardens) {
				player.getMusic().unlock(Music.get(encounterType.getSoundTrack()));
			}
		};
		if (!isWardens && EncounterStage.NOT_STARTED.equals(stage)) {
			if (player.getTOAManager().needsAbandonRequest()) {
				player.getTOAManager().startAbandonDialogue("Begin the challenge", enterRunnable);
			} else if (!quickUse) {
				player.getDialogueManager().start(new OptionDialogue(player, "Begin the challenge?", new String[] {"Yes.", "No."}, new Runnable[] {enterRunnable, null}));
			} else {
				enterRunnable.run();
			}
		} else {
			enterRunnable.run();
		}
	}

	public final String getCurrentChallengeName(boolean path) {
		if (challengeName != null && challengeName.length() > 0) {
			return challengeName;
		}
		if (path) {
			challengeName = "Path of " + party.getPathType().getPathName();
		} else if (EncounterType.APMEKEN_BOSS.equals(encounterType)) {
			challengeName = "Ba-Ba";
		} else if (EncounterType.CRONDIS_BOSS.equals(encounterType)) {
			challengeName = "Zebak";
		} else if (EncounterType.HET_BOSS.equals(encounterType)) {
			challengeName = "Akkha";
		} else if (EncounterType.SCABARIS_BOSS.equals(encounterType)) {
			challengeName = "Kephri";
		} else {
			challengeName = "The Wardens";
		}
		return challengeName;
	}

	protected final boolean isPuzzleEncounter() {
		return EncounterType.APMEKEN_PUZZLE.equals(encounterType) || EncounterType.CRONDIS_PUZZLE.equals(encounterType) ||
				EncounterType.HET_PUZZLE.equals(encounterType) || EncounterType.SCABARIS_PUZZLE.equals(encounterType);
	};

	private void walkBarrier(Player player, WorldObject object) {
		player.resetWalkSteps();
		player.addWalkSteps(object.getX() < player.getX() ? player.getX() - 2 : player.getX() + 2, player.getY(), 2, false);
		player.lock(1);
		player.setRunSilent(true);
		player.setFaceLocation(object.getLocation());
		WorldTasksManager.schedule(() -> player.setRunSilent(false));
	}

	public boolean insideChallengeArea(final Entity entity) {
		if (encounterType.getMinChallengeLocation() == null || encounterType.getMaxChallengeLocation() == null) {
			return false;
		}
		return encounterType.insideChallengeArea(this, entity);
	}

	@Override public void constructed() {

	}

	@Override public void destroyRegion() {
		super.destroyRegion();
		party.updateOriginalPlayers();
		stopRunningTasks();
		stage = EncounterStage.NOT_STARTED;
	}

	@Override public Location onLoginLocation() {
		return OUTSIDE_LOCATION;
	}

	@Override public void onLogout(@NotNull Player player) {
		final boolean die = EncounterStage.STARTED.equals(stage) && insideChallengeArea(player) && party.getCurrentRaidArea() != null;
		player.getTOAManager().setToaPlayerLogoutState(new TOAPlayerLogoutState(encounterType.toBase(), EncounterStage.STARTED.equals(stage) && insideChallengeArea(player),
				die ? party.getCurrentRaidArea().getLocation(encounterType.getRandomizedSpawnTile()) : player.getLocation(), party.getPermittedTeamDeaths() == -1));
		ConsumableEffects.resetSalt(player);
		if (die) {
			player.getTOAManager().setIndividualDeaths(player.getTOAManager().getIndividualDeaths() + 1);
			party.increaseTotalDeaths();
			party.getCurrentRaidArea().getPlayers().stream().filter(p -> p != null && !p.getUsername().equals(player.getUsername())).forEach(
					p -> p.sendMessage("<col=ff0000>" + player.getName() + "</col> has logged out. Total deaths: <col=ff0000>" + party.getTotalDeaths() + "</col>."));
		}
		player.forceLocation(TOAManager.getRandomizedOutsideLocation());
	}

	public abstract void onRoomStart();

	public abstract void onRoomEnd();

	public abstract void onRoomReset();

	public void startRoom() {
		challengeTime = WorldThread.getCurrentCycle();
		stage = EncounterStage.STARTED;
		teamSize = party.getOriginalPlayers().size();
		onRoomStart();
	}

	public void completeRoom() {
		stage = EncounterStage.COMPLETED;
		final ChallengeResult result = new ChallengeResult(WorldThread.getCurrentCycle() - challengeTime, null, encounterType);
		party.getChallengeResults().add(result);
		final String challengeDuration = formatTime(result.getTime());
		final String totalChallengeDuration = formatTime(party.computeTotalChallengeTime());
		final boolean isPuzzleRoom = isPuzzleEncounter();
		final boolean isEnd = EncounterType.WARDENS_SECOND_ROOM.equals(encounterType);
		if (isEnd) {
			party.setCompletion();
		}
		players.forEach(p -> {
			p.getHpHud().close();
			p.getReceivedHits().clear();
			p.getPacketDispatcher().resetCamera();
			p.blockIncomingHits(3);
			p.getMusic().unlock(Music.get(!EncounterType.WARDENS_SECOND_ROOM.equals(encounterType) ?
					EncounterType.MAIN_HALL.getSoundTrack() : EncounterType.REWARD_ROOM.getSoundTrack()));
			if (isPuzzleRoom) {
				p.getMusic().playJingle(295);
			} else {
				p.reset();
			}
			if (p.getAppearance().isTransformedIntoNpc()) {
				p.getAppearance().transform(-1);
				GameInterface.EQUIPMENT_TAB.open(p);
				GameInterface.INVENTORY_TAB.open(p);
			}
			if (!insideChallengeArea(p)) {
				p.setLocation(party.getCurrentRaidArea().getLocation(encounterType.getChallengeSpawnLocation()));
			}
			if (!isEnd) {
				p.sendMessage("Challenge complete: " + challengeName + ". Duration: <col=ef1020>"
						+ challengeDuration + "</col>. Total: <col=ef1020>" + totalChallengeDuration + "</col>");
			} else {
				p.getTOAManager().removeTOAItems();
				p.sendMessage("Challenge complete: " + challengeName + ". Duration: <col=ef1020>" + challengeDuration + "</col>");
				final String mode = party.getPartySettings().getMode();
				p.sendMessage("Tombs of Amascut: " + mode + " Mode challenge completion time: <col=ef1020>" + totalChallengeDuration + "</col>");
				p.sendMessage("Tombs of Amascut: " + mode + " Mode total completion time: <col=ef1020>" + formatTime(party.getTotalTime()) + "</col>");
				p.getNotificationSettings().increaseKill("tombs of amascut: " + mode.toLowerCase() + " mode");
				p.getNotificationSettings().sendBossKillCountNotification("tombs of amascut: " + mode.toLowerCase() + " mode");
				if (party.getTimeLimitMinutes() != -1) {
					if (party.isFailedTimeChallenge()) {
						p.sendMessage("<col=FF0000>Your party failed to beat the overall target time of " +
								party.getTimeLimitMinutes() + ":00</col");
					} else {
						p.sendMessage("<col=00FF00>Your party beat the overall target time of " +
								party.getTimeLimitMinutes() + ":00!</col");
					}
				}
			}
		});
		stopRunningTasks();
		onRoomEnd();
	}

	public void resetRoom() {
		stage = EncounterStage.NOT_STARTED;
		players.forEach(p -> {
			p.getHpHud().close();
			p.getMusic().unlock(Music.get(EncounterType.MAIN_HALL.getSoundTrack()));
			if (!onDiet && !tombsFailure) {
				p.getInventory().addOrDrop(new Item(ItemId.HONEY_LOCUST, Utils.random(4, 6)));
			}
		});
		onRoomReset();
	}

	public void spawnTeleportNPC() {
		players.forEach(p -> p.getMusic().playJingle(296));
		final Direction direction = encounterType.getChallengeSpawnLocation().getX() > encounterType.getNpcLocation().getX() ? Direction.EAST : Direction.WEST;
		final Osmumten osmumten = new Osmumten(TELEPORT_NPC_ID, getLocation(encounterType.getNpcLocation()), direction);
		osmumten.spawn();
	}

	public static String formatTime(long time) {
		if (time < 0) {
			return "0:00";
		}
		String duration = "";
		final long hours = TimeUnit.TICKS.toHours(time);
		if (hours > 0) {
			duration += hours + ":";
		}
		final long minutes = TimeUnit.TICKS.toMinutes(time) % 60;
		if (hours > 0 && minutes < 10) {
			duration += "0";
		}
		duration += minutes + ":";
		final long seconds = TimeUnit.TICKS.toSeconds(time) % 60;
		if (seconds > 0) {
			duration += seconds >= 10 ? seconds : ("0" + seconds);
		}
		return duration;
	}

	@Override
	public boolean isMultiwayArea(Position position) {
		return true;
	}

	@Override public String name() {
		return "TOA raid";
	}

	@Override public boolean hit(Player source, Entity target, Hit hit, float modifier) {
		if (target instanceof final TOANPC toaNpc && toaNpc.getPointMultiplier() > 0) {
			final int damageDone = Math.min(hit.getDamage(), target.getHitpoints());
			source.getTOAManager().setDamageDone(source.getTOAManager().getDamageDone() + damageDone);
			final int points = (int) Math.floor(damageDone * toaNpc.getPointMultiplier());
			source.getTOAManager().setCurrentPoints(source.getTOAManager().getCurrentPoints() + points);
		}
		return true;
	}

	@Override public boolean canLay(@NotNull Player player, @NotNull LayableObjectType type) {
		player.sendMessage("You can't do this right now.");
		return false;
	}

	@Override
	public Set<Music> getMusics(Player player) {
		final Set<Music> list = new HashSet<>();
		if (encounterType != null && EncounterStage.STARTED.equals(stage) && insideChallengeArea(player) && encounterType.getSoundTrack() != null) {
			list.add(Music.get(encounterType.getSoundTrack()));
		}
		return list;
	}

	@Override
	public boolean canTeleport(final Player player, final Teleport teleport) {
		if (player.getAppearance().isTransformedIntoNpc()) {
			player.getDialogueManager().start(new PlainChat(player, "A mysterious force prevents you from doing that."));
			return false;
		}
		return true;
	}

	@Override public boolean eat(Player player, Edible food) {
		if (onDiet) {
			player.sendMessage("You've been prevented from consuming food within the Tombs of Amascut");
			return false;
		}
		return true;
	}

	@Override public boolean drink(final Player player, final Drinkable potion) {
		if (deHydration && potion instanceof final Potion pot) {
			for (Consumable.Boost boost : pot.boosts()) {
				if (!(boost instanceof Consumable.Debuff) && boost.getSkill() == SkillConstants.HITPOINTS) {
					player.sendMessage("You've been prevented from drinking this potion within the Tombs of Amascut");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public int visibleTicks(final Player player, final Item item) {
		if(!item.isTradable())
			return 0;
		return -1;
	}

	@Override
	public int invisibleTicks(final Player player, final Item item) {
		if(!item.isTradable())
			return 300;
		return -1;
	}

	@Override public boolean isSafe() { return false; }

	@Override public String getDeathInformation() { return null; }

	@Override public Location getRespawnLocation() { return null; }

	@Override public boolean sendDeath(Player player, Entity source) {
		player.getPacketDispatcher().resetCamera();
		player.setAnimation(Animation.STOP);
		player.lock();
		player.stopAll();
		final boolean intoGap = player.getTemporaryAttributes().remove(BabaEncounter.APMEKEN_BABA_DEATH_ATTRIBUTE, Boolean.TRUE);
		if (!intoGap && player.getPrayerManager().isActive(Prayer.RETRIBUTION)) {
			player.getPrayerManager().applyRetributionEffect(source);
		}
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;

			@Override
			public void run() {
				if (player.isFinished() || player.isNulled()) {
					stop();
					return;
				}
				if (ticks == 1) {
					if (intoGap) {
						player.setAnimation(Baba.PIT_FALL_ANIM);
						player.sendSound(Baba.ROLLING_BOULDER_SOUND);
					} else {
						player.setAnimation(Player.DEATH_ANIMATION);
					}
				} else if (ticks == (intoGap ? 4 : 5)) {
					party.increaseTotalDeaths();
					if (intoGap) {
						player.sendMessage("<col=ff3045>You're pushed straight into a deep pit! Ouch!</col>");
					} else {
						player.sendMessage("You have died. Total deaths: <col=ff0000>" + party.getTotalDeaths() + "</col>.");
					}
					if (EncounterStage.STARTED.equals(stage) && getChallengePlayers().length > 0) {
						if (party.getPermittedTeamDeaths() == -1 || party.getPermittedTeamDeaths() > party.getTeamDeaths() + 1) {
							player.sendMessage("You will respawn when your party completes or fails the challenge.");
						} else {
							player.sendMessage("You will respawn when your party completes the challenge.");
						}
					}
					final int currentPoints = player.getTOAManager().getCurrentPoints();
					player.getTOAManager().setIndividualDeaths(player.getTOAManager().getIndividualDeaths() + 1);
					player.getTOAManager().setCurrentPoints(Math.max(0, currentPoints - Math.max(1000, (int) (currentPoints * .2F))));
					player.reset();
					player.setAnimation(Animation.STOP);
					player.getVariables().setSkull(false);
					player.unlock();
					player.blockIncomingHits();
					if (EncounterType.MAIN_HALL.equals(encounterType) || EncounterStage.STARTED.equals(stage) || EncounterStage.NOT_STARTED.equals(stage)) {
						player.setLocation(party.getCurrentRaidArea().getLocation(encounterType.getRandomizedSpawnTile()));
						if (!EncounterType.MAIN_HALL.equals(encounterType) && EncounterStage.STARTED.equals(stage)) {
							turnIntoDeadGhost(player);
						}
					} else {
						player.setLocation(party.getCurrentRaidArea().getLocation(encounterType.getChallengeSpawnLocation()));
					}

					getPlayers().stream().filter(p -> p != null && !p.getUsername().equals(player.getUsername())).forEach(
							p -> p.sendMessage("<col=ff0000>" + p.getName()+ "</col> has died. Total deaths: <col=ff0000>" + party.getTotalDeaths() + "</col>."));
				} else if (ticks == (intoGap ? 6 : 7)) {
					checkRoomReset();
					stop();
				}
				ticks++;
			}
		}, 0, 0);
		return true;
	}

	public void turnIntoDeadGhost(Player player) {
		player.getAppearance().transform(GHOST_PLAYER_NPC_ID);
		player.getInterfaceHandler().closeInterface(InterfacePosition.INVENTORY_TAB);
		player.getInterfaceHandler().closeInterface(InterfacePosition.EQUIPMENT_TAB);
		player.setRun(false);
		getPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getTOAManager().refreshHudStates());
	}

	public void checkRoomReset() {
		if (getPlayers().stream().anyMatch(p -> p != null && !p.isFinished() && (insideChallengeArea(p) || !p.getAppearance().isTransformedIntoNpc())) || !EncounterStage.STARTED.equals(stage)) {
			return;
		}
		party.increaseTeamDeaths();
		final boolean resetRoom = party.getPermittedTeamDeaths() == -1 || party.getTeamDeaths() < party.getPermittedTeamDeaths();
		for (Player p : getPlayers()) {
			if (p != null) {
				p.lock();
				final FadeScreen fadeScreen = new FadeScreen(p, () -> {
					p.reset();
					p.getAppearance().transform(-1);
					GameInterface.EQUIPMENT_TAB.open(p);
					GameInterface.INVENTORY_TAB.open(p);
					p.unlock();
					p.getMusic().playJingle(90);
					if (resetRoom) {
						p.sendMessage("Your party failed to complete the challenge. " + (party.getPermittedTeamDeaths() == -1 ? "You may try again..." : ("You have <col=ff0000>" + (party.getPermittedTeamDeaths() - party.getTeamDeaths()) + "</col> attempts remaining...")));
					} else {
						p.getTOAManager().triggerTOAFailure(false);
					}
				});
				fadeScreen.fade();
				WorldTasksManager.schedule(() -> fadeScreen.unfade(true, () -> p.getTOAManager().sendHud()), 2);
			}
		}
		if (resetRoom) {
			stopRunningTasks();
		} else {
			tombsFailure = true;
		}
		resetRoom();
	}

	protected void stopRunningTasks() {
		runningTasks.removeIf(task -> {
			task.stop();
			return true;
		});
	}

	public TOARaidParty getParty() { return party; }

	public WorldTask addRunningTask(WorldTask worldTask) {
		runningTasks.add(worldTask);
		return worldTask;
	}

	public EncounterStage getStage() { return stage; }

	public Player[] getChallengePlayers() {
		return getChallengePlayers(p -> true);
	}

	public Player[] getChallengePlayers(Predicate<Player> condition) {
		return players.stream().filter(p -> p != null && !p.isDying() && insideChallengeArea(p)).filter(condition).toArray(Player[]::new);
	}

	public EncounterType getEncounterType() { return encounterType; }

	public int getStartTeamSize() { return teamSize; }

	public boolean hasFailedTombs() { return tombsFailure; }

	@Override public boolean isDeHydration() { return deHydration; }

	@Override public boolean isOverlyDraining() { return overlyDraining; }

	@Override public boolean isQuietPrayers() { return quietPrayers; }

	@Override public boolean isDeadlyPrayers() { return deadlyPrayers; }

	public long getChallengeTime() { return challengeTime; }

	public void setChallengeTime(long challengeTime) { this.challengeTime = challengeTime; }

	public int getTeamSize() { return teamSize; }

	public void setTeamSize(int teamSize) { this.teamSize = teamSize; }

	public String getChallengeName() { return challengeName; }

	public void setChallengeName(String challengeName) { this.challengeName = challengeName; }


}
