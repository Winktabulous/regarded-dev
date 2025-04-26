package com.zenyte.game.content.tombsofamascut;

import com.google.common.collect.ImmutableList;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.tombsofamascut.encounter.ApmekenEncounter;
import com.zenyte.game.content.tombsofamascut.encounter.CrondisPuzzleEncounter;
import com.zenyte.game.content.tombsofamascut.encounter.TOACommands;
import com.zenyte.game.content.tombsofamascut.lobby.TOALobbyArea;
import com.zenyte.game.content.tombsofamascut.lobby.TOALobbyParty;
import com.zenyte.game.content.tombsofamascut.object.MirrorObjectAction;
import com.zenyte.game.content.tombsofamascut.raid.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.plugins.dialogue.OptionDialogue;
import com.zenyte.plugins.dialogue.PlainChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zenyte.game.content.tombsofamascut.lobby.TOALobbyParty.RAID_PARTIES;

/**
 * @author Savions.
 */
public class TOAManager extends AbstractTOAManager {

	static {
		new TOACommands();
	}
	public static final Logger log = LoggerFactory.getLogger(TOAManager.class);

	public static final int[] TOA_REGULAR_ITEM_IDS = {CrondisPuzzleEncounter.CONTAINER_ITEM_ID, MirrorObjectAction.MIRROR_ITEM_ID, ItemId.NEUTRALISING_POTION, ItemId.SUPPLIES, ItemId.HONEY_LOCUST};
	public static final int[] TOA_SUPPLIES_ITEM_IDS = {ItemId.NECTAR_4, ItemId.NECTAR_3, ItemId.NECTAR_2, ItemId.NECTAR_1, ItemId.TEARS_OF_ELIDINIS_4, ItemId.TEARS_OF_ELIDINIS_3, ItemId.TEARS_OF_ELIDINIS_2, ItemId.TEARS_OF_ELIDINIS_1, ItemId.SMELLING_SALTS_2, ItemId.SMELLING_SALTS_1, ItemId.LIQUID_ADRENALINE_2, ItemId.LIQUID_ADRENALINE_1, ItemId.SILK_DRESSING_2, ItemId.SILK_DRESSING_1, ItemId.BLESSED_CRYSTAL_SCARAB_2, ItemId.BLESSED_CRYSTAL_SCARAB_1, ItemId.AMBROSIA_2, ItemId.AMBROSIA_1};
	public static final Location OUTSIDE_LOCATION = new Location(3358, 9113);
	public static final int MAX_PARTY_MEMBERS = 8;
	private static final int POINT_VARBIT = 3586;
	private static final int PRESET_BASE_VAR = 3680;
	public static final int HUD_PATH_VARBIT = 14381;
	private static final int HUD_PLAYER_LIST_BASE_VARBIT = 14346;
	private static final int HUD_PLAYER_ME_VARBIT = 14354;
	private static final int HUD_PLAYER_SIGHT_VARBIT = 14362;
	private static final int HUD_RAID_LEVEL_VARBIT = 14380;
	private static final int HUD_PATH_LEVEL_BASE_VARBIT = 14376;
	private transient final Player player;
	private transient TOARaidParty raidParty;
	private transient TOALobbyParty viewingParty;
	private transient TOALobbyParty currentParty;
	private transient TOALobbyParty appliedParty;

	private transient EncounterType currentEncounter;
	private int individualDeaths;
	private boolean canClaimSupplies;
	private Container suppliesContainer;
	private TOAPartySettingData partySettingData = new TOAPartySettingData();
	private transient TOAPartySettings partySettings;
	private TOAPlayerLogoutState toaPlayerLogoutState;
	private Container rewardContainer;

	static {
		for (int i = 0; i < 15; i++) {
			VarManager.appendPersistentVarp(PRESET_BASE_VAR + i);
		}
		VarManager.appendPersistentVarbit(POINT_VARBIT);
	}

	public TOAManager(final Player player) {
        super(player);
        this.player = player;
		loadData(player.getToaPlayerData());
	}

	private void loadData(TOAPlayerData data) {
		this.individualDeaths = data.individualDeaths;
		this.canClaimSupplies = data.canClaimSupplies;
		this.suppliesContainer = data.suppliesContainer;
		this.partySettingData = data.partySettingData;
		this.toaPlayerLogoutState = data.toaPlayerLogoutState;
		this.rewardContainer = data.rewardContainer;
		this.points = data.points;
		this.damageDone = data.damageDone;
		this.damageTaken = data.damageTaken;
		if(data.partySettingData != null)
			partySettings = new TOAPartySettings(partySettingData);
		else partySettings = new TOAPartySettings();
	}



	@Override public Integer getCurrentInterfaceTab() {
		return this.player.currentTOAPartyManagementTab;
	}

	@Override public void setCurrentInterfaceTab(int currentInterfaceTab) {
		this.player.currentTOAPartyManagementTab = currentInterfaceTab;
	}

	@Override public int getViewingValue() {
		return this.player.currentTOAPartyViewingValue;
	}

	@Override public void setViewingValue(int viewingValue) {
		this.player.currentTOAPartyViewingValue = viewingValue;
	}

	@Override public void sendEmptyPartyList() {
		player.getPacketDispatcher().sendComponentText(GameInterface.TOA_PARTY.getId(), 5, "-<br>-<br>-<br>-<br>-<br>-<br>-<br>-");
		player.getVarManager().sendBit(TOALobbyParty.PARTY_STATUS_VAR, 0);
	}

	@Override public boolean viewingManagementInterface(String ownerUsername) {
		TOALobbyParty viewingParty = TOALobbyParty.getCurrentParty(player);
		return player.getInterfaceHandler().getInterface(player.getInterfaceHandler().getPane(),
				GameInterface.TOA_PARTY_MANAGEMENT.getPosition().getComponent(player.getInterfaceHandler().getPane())) == GameInterface.TOA_PARTY_MANAGEMENT.getId()
					&& viewingParty != null && ownerUsername.equals(viewingParty.getLeaderDisplayName());
	}

	@Override public void toggleInvocation(final Object gen, final Player player) {
		InvocationType invocation = (InvocationType) gen;
		if (partySettings.isActive(invocation)) {
			if (InvocationType.OVERCLOCKED.equals(invocation)) {
				if (partySettings.isActive(InvocationType.OVERCLOCKED_2)) {
					partySettings.unFlagInvocation(InvocationType.OVERCLOCKED_2);
				}
				if (partySettings.isActive(InvocationType.INSANITY)) {
					partySettings.unFlagInvocation(InvocationType.INSANITY);
				}
			} else if (InvocationType.OVERCLOCKED_2.equals(invocation) && partySettings.isActive(InvocationType.INSANITY)) {
				partySettings.unFlagInvocation(InvocationType.INSANITY);
			} else if (InvocationType.NOT_JUST_A_HEAD.equals(invocation) && partySettings.isActive(InvocationType.ARTERIAL_SPRAY)) {
				partySettings.unFlagInvocation(InvocationType.ARTERIAL_SPRAY);
			} else if (InvocationType.NOT_JUST_A_HEAD.equals(invocation) && partySettings.isActive(InvocationType.BLOOD_THINNERS)) {
				partySettings.unFlagInvocation(InvocationType.BLOOD_THINNERS);
			}
			partySettings.unFlagInvocation(invocation);
			player.sendSound(6588);
		} else {
			if (InvocationCategoryType.ATTEMPTS.equals(invocation.getCategory())) {
				partySettings.unFlagCategory(InvocationCategoryType.ATTEMPTS);
			}
			if (InvocationCategoryType.TIME_LIMIT.equals(invocation.getCategory())) {
				partySettings.unFlagCategory(InvocationCategoryType.TIME_LIMIT);
			}
			if (InvocationCategoryType.HELPFUL_SPIRIT.equals(invocation.getCategory())) {
				partySettings.unFlagCategory(InvocationCategoryType.HELPFUL_SPIRIT);
			}
			if (InvocationCategoryType.PATH_LEVEL.equals(invocation.getCategory())) {
				partySettings.unFlagCategory(InvocationCategoryType.PATH_LEVEL);
			}
			if (InvocationType.INSANITY.equals(invocation) && !partySettings.isActive(InvocationType.OVERCLOCKED_2)) {
				player.sendMessage("You cannot activate this invocation without first enabling <col=ff0000>Overclocked 2</col>.");
				return;
			} else if (InvocationType.OVERCLOCKED_2.equals(invocation) && !partySettings.isActive(InvocationType.OVERCLOCKED)) {
				player.sendMessage("You cannot activate this invocation without first enabling <col=ff0000>Overclocked</col>.");
				return;
			} else if (InvocationType.ARTERIAL_SPRAY.equals(invocation) && !partySettings.isActive(InvocationType.NOT_JUST_A_HEAD)) {
				player.sendMessage("You cannot activate this invocation without first enabling <col=ff0000>Not Just a Head</col>.");
				return;
			} else if (InvocationType.BLOOD_THINNERS.equals(invocation) && !partySettings.isActive(InvocationType.NOT_JUST_A_HEAD)) {
				player.sendMessage("You cannot activate this invocation without first enabling <col=ff0000>Not Just a Head</col>.");
				return;
			}
			partySettings.flagInvocation(invocation);
			player.sendSound(6589);
		}
	}

	@Override public void saveInvocationPreset(int index) {
		updatePreset(partySettings.getInvocationBitmaps(), index);
	}

	@Override public void clearInvocationPreset(int index) { updatePreset(new int[] {0, 0, 0}, index); }

	@Override public boolean isPresetEmpty(int index) {
		final int[] preset = getInvocationPreset(index);
		return preset[0] == 0 && preset[1] == 0 && preset[2] == 0;
	}

	@Override protected void updatePreset(int[] preset, int index) {
		for (int i = 0; i < 3; i++) {
			player.getVarManager().sendVarInstant(getPresetBaseVarId(index) + i, preset[i]);
		}
	}

	@Override public int[] getInvocationPreset(int index) {
		final int baseVarId = getPresetBaseVarId(index);
		return new int[] { player.getVarManager().getValue(baseVarId), player.getVarManager().getValue(baseVarId + 1), player.getVarManager().getValue(baseVarId + 1) };
	}

	@Override
	protected int getPresetBaseVarId(int index) { return PRESET_BASE_VAR + index * 3; }

	@Override public void enterRaid() {
		TOALobbyParty currentLobbyParty = TOALobbyParty.getCurrentParty(player);
		if (raidParty == null && currentLobbyParty != null) {
			if (!currentLobbyParty.insideRaid()) {
				if (!currentLobbyParty.isLeader(player)) {
					player.getDialogueManager().start(new PlainChat(player, "Your leader, " + currentLobbyParty.getLeader().getName() + ", must enter first."));
					return;
				} else {
					currentLobbyParty.setRaidParty(new TOARaidParty(currentLobbyParty, (TOAPartySettings) getPartySettings(), player));
					RAID_PARTIES.add(currentLobbyParty.getRaidParty());
					currentLobbyParty.removeFromList();
				}
			}
			raidParty = currentLobbyParty.getRaidParty();
		}
		enter(true, EncounterType.MAIN_HALL);
	}

	@Override public boolean enter(boolean checkLeader, final Object encounterType) {
		final EncounterType currentEncounter = raidParty.getCurrentEncounterType();
		if (!encounterType.equals(currentEncounter) || (raidParty.getCurrentRaidArea() != null && raidParty.getCurrentRaidArea().isDestroyed())) {
			if (checkLeader && !raidParty.isLeader(player)) {
				player.getDialogueManager().start(new PlainChat(player, "Your leader, " + raidParty.getLeader().getName() + ", must enter first."));
				return false;
			} else {
				if (currentEncounter == null) {
					raidParty.getPlayers().forEach(p -> {
						if (!player.getUsername().equals(p.getUsername())) {
							p.sendMessage(player.getName() + " has entered the Tombs of Amascut. Step inside to join " + (player.getAppearance().isMale() ? "him" : "her") + "...");
							p.getVarManager().sendBit(TOALobbyParty.PARTY_STATUS_VAR, 2);
						}
					});
				}
				raidParty.constructEncounter((EncounterType) encounterType);
			}
		}
		final TOARaidArea area = raidParty.getCurrentRaidArea();
		if (currentEncounter == null) {
			setIndividualDeaths(0);
			setCurrentPoints(0);
			setDamageDone(0);
			setDamageTaken(0);
			player.sendMessage("You enter the Tombs of Amascut (" + partySettings.getMode() + " Mode)...");
		}
		if (EncounterType.MAIN_HALL.equals(encounterType)) {
			player.getVarManager().sendBitInstant(HUD_PATH_VARBIT, 0);
		}
		final FadeScreen fadeScreen = new FadeScreen(player);
		WorldTasksManager.schedule(() -> {
				if (area.isDestroyed()) {
					raidParty.constructEncounter((EncounterType) encounterType);
				}
				if (raidParty.getTimeLimitMinutes() != -1 && currentEncounter == null) {
					player.sendMessage("Overall time to beat: <col=ef1020>" + raidParty.getTimeLimitMinutes() + ":00</col>. The timer starts upon choosing your first path.");
				}
				if (raidParty.getStartTime() == 0 && !EncounterType.MAIN_HALL.equals(encounterType)) {
					raidParty.setStartTime(WorldThread.getCurrentCycle());
					if (raidParty.getTimeLimitMinutes() != -1) {
						raidParty.getPlayers().forEach(p -> p.sendMessage("Overall time to beat: <col=ef1020>" + raidParty.getTimeLimitMinutes() + ":00</col>. The timer has started!"));
					}
				}
				refreshTimer();
				if (EncounterType.MAIN_HALL.equals(encounterType) && !raidParty.getPathsCompleted().isEmpty() && raidParty.getPathType() != null) {
					player.faceDirection(raidParty.getPathType().getFaceDirection());
				}
				TOALobbyParty.setCurrentParty(player, null);
		}, 0);

		fadeScreen.fade();
		WorldTasksManager.schedule(() -> fadeScreen.unfade(true, () -> {
			Location destination;
			if (EncounterType.MAIN_HALL.equals(encounterType) && !raidParty.getPathsCompleted().isEmpty() && raidParty.getPathType() != null) {
				destination = raidParty.getCurrentRaidArea().getLocation(raidParty.getPathType().getRandomizedSpawnTile());
			} else {
				destination = raidParty.getCurrentRaidArea().getLocation(((EncounterType)encounterType).getRandomizedSpawnTile());
			}
			player.setLocation(destination);
		}, 0), 2);
		setCurrentEncounter(encounterType);
		raidParty.getPlayers().forEach(p -> p.getTOAManager().refreshHudStates());
		return true;
	}

	@Override public void sendHud() {
		player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 481);
		if (raidParty != null) {
			sendRaidLevel();
			raidParty.getPlayers().forEach(p -> p.getTOAManager().refreshHudStates());
			refreshHudPlayers();
			refreshTimer();
			for (int i = 0; i < raidParty.getBossLevels().length; i++) {
				refreshPathLevel(i);
			}
		} else
			player.sendDeveloperMessage("Not sending HUD because raid party is null");
	}

	@Override public void sendRaidLevel() {
		player.getVarManager().sendBit(HUD_RAID_LEVEL_VARBIT, raidParty.getPartySettings().getRaidLevel());
	}

	@Override public void refreshHudPlayers() {
		if (raidParty != null) {
			player.getPacketDispatcher().sendClientScript(6585, raidParty.getHudPlayerList());
		}
	}

	@Override public void refreshHudStates() {
		if (raidParty != null) {
			final EncounterType currentEncounterType = raidParty.getCurrentEncounterType();
			for (int i = 0; i < TOAManager.MAX_PARTY_MEMBERS; i++) {
				if (i >= raidParty.getPlayers().size() || raidParty.getTotalTime() != -1) {
					player.getVarManager().sendBit(HUD_PLAYER_LIST_BASE_VARBIT + i, 0);
				} else {
					final Player p = raidParty.getPlayers().get(i);
					if (player.equals(p)) {
						player.getVarManager().sendBit(HUD_PLAYER_ME_VARBIT, i + 1);
					}
					player.getVarManager().sendBit(HUD_PLAYER_SIGHT_VARBIT + i, p.getTemporaryAttributes().containsKey(ApmekenEncounter.SIGHT_PLAYER_ATTRIBUTE) ? 1 : 0);
					if (currentEncounterType != null && !currentEncounterType.equals(p.getTOAManager().getCurrentEncounter())) {
						player.getVarManager().sendBit(HUD_PLAYER_LIST_BASE_VARBIT + i, 31);
					} else if (p.getAppearance().isTransformedIntoNpc()) {
						player.getVarManager().sendBit(HUD_PLAYER_LIST_BASE_VARBIT + i, 30);
					} else {
						player.getVarManager().sendBit(HUD_PLAYER_LIST_BASE_VARBIT + i, 1 + Math.min(28, (int) Math.floor(((double) p.getHitpoints() / (double) p.getMaxHitpoints()) * 28)));
					}
				}
			}
		}
	}

	@Override public void refreshPathLevel(int index) {
		if (raidParty != null) {
			player.getVarManager().sendBit(HUD_PATH_LEVEL_BASE_VARBIT + index, raidParty.getBossLevels()[index]);
		}
	}

	@Override public void refreshTimer() {
		if (raidParty != null && raidParty.getStartTime() > 0) {
			if (raidParty.getTotalTime() != -1) {
				player.getPacketDispatcher().sendClientScript(6580, (int) raidParty.getTotalTime(), 1);
			} else {
				player.getPacketDispatcher().sendClientScript(6580, (int) (WorldThread.getCurrentCycle() - raidParty.getStartTime()), 0);
			}
		}
	}

	@Override public void startLeaveDialogue() {
		if (player.getAppearance().isTransformedIntoNpc()) {
			player.getDialogueManager().start(new PlainChat(player, "A mysterious force prevents you from doing that."));
			return;
		}
		player.getDialogueManager().start(new Dialogue(player) {
			@Override public void buildDialogue() {
				plain("You are about to <col=ad2800>abandon the raid</col>. If you do this, you <col=ad2800>will not</col> be able to return to your current run.");
				options("Abandon the raid?", "Yes, abandon the raid.", "No, I want to stay.").onOptionOne(() -> leaveTombs("You abandon the raid and leave the Tombs of Amascut."));
			}
		});
	}

	@Override public void leaveTombs(String message) {
		player.sendMessage(message);
		final FadeScreen fadeScreen = new FadeScreen(player, () -> {
			removeTOAItems();
			player.reset();
			if (raidParty != null) {
				raidParty.leave(player, false);
			}
			player.setLocation(getRandomizedOutsideLocation());
		});
		fadeScreen.fade();
		WorldTasksManager.schedule(fadeScreen::unfade, 2);
	}

	@Override
	public Object getRaidParty() {
		return this.raidParty;
	}

	@Override
	public void setRaidParty(Object toaRaidParty) {
		this.raidParty = (TOARaidParty) toaRaidParty;
	}

	@Override
	public Object getViewingParty() {
		return this.viewingParty;
	}

	@Override
	public void setViewingParty(Object toaRaidParty) {
		this.viewingParty = (TOALobbyParty) toaRaidParty;
	}

	@Override
	public Object getCurrentParty() {
		return this.currentParty;
	}

	@Override
	public void setCurrentParty(Object toaRaidParty) {
		this.currentParty = (TOALobbyParty) toaRaidParty;
	}

	@Override
	public Object getAppliedParty() {
		return this.appliedParty;
	}

	@Override
	public void setAppliedParty(Object toaRaidParty) {
		this.appliedParty = (TOALobbyParty) toaRaidParty;
	}


	@Override public boolean needsAbandonRequest() {
		if (raidParty.getCurrentRaidArea() == null) {
			return false;
		}
		final EncounterType currentEncounterType = raidParty.getCurrentEncounterType();
		return raidParty.getPlayers().stream().anyMatch(p -> !currentEncounterType.equals(p.getTOAManager().getCurrentEncounter()));
	}

	@Override public void startAbandonDialogue(final String action, final Runnable runnable) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override public void buildDialogue() {
				plain("Some of your party don't seem to have arrived yet.<br>If you proceed, they will be abandoned.");
				options(action + "?", "No, wait for any stragglers.", "Yes, abandon any stragglers.").onOptionTwo(() -> {
					ImmutableList.copyOf(raidParty.getPlayers()).stream().filter(p -> !raidParty.getCurrentRaidArea().getPlayers().contains(p)).forEach(p -> {
						p.sendMessage("Your party moved on without you.");
						raidParty.leave(p, false);
						if (p.getArea() != null && p.getArea() instanceof TOARaidArea) {
							final FadeScreen fadeScreen = new FadeScreen(p, () -> {
								p.reset();
								p.setLocation(getRandomizedOutsideLocation());
							});
							fadeScreen.fade();
							WorldTasksManager.schedule(fadeScreen::unfade, 2);
						} else {
							TOALobbyParty.setCurrentParty(player, null);
							raidParty.leave(p, false);
							p.getTOAManager().sendEmptyPartyList();
						}
					});
					runnable.run();
				});
			}
		});
	}

	@Override public void initialize(AbstractTOAManager manager) {
		setToaPlayerLogoutState(this.getToaPlayerLogoutState());
		setIndividualDeaths(this.getIndividualDeaths());
		setSuppliesContainer(this.getSuppliesContainer());
		setPartySettings(this.getPartySettings());
		setRewardContainer(this.getRewardContainer());
		setCurrentPoints(this.getCurrentPoints());
		setDamageDone(this.getDamageDone());
		setDamageTaken(this.getDamageTaken());
	}

	@Override public int getCurrentPoints() { return points; }

	@Override public void setCurrentPoints(int currentPoints) {
		this.points = currentPoints;
		player.getVarManager().sendBit(POINT_VARBIT, currentPoints);
	}

	@Override public void onLogin() {
		if (toaPlayerLogoutState != null) {
			final RegionArea regionArea = GlobalAreaManager.getArea(toaPlayerLogoutState.getLogoutLocation());
			if (regionArea instanceof final TOARaidArea area && area.getParty().getOriginalPlayers().contains(player.getUsername())) {
				if (area.hasFailedTombs()) {
					player.getTOAManager().triggerTOAFailure(true);
				} else {
					player.sendMessage("You have rejoined your party");
					raidParty = area.getParty();
					refreshTimer();
					area.getParty().add(player);
					player.setLocation(area.getLocation(EncounterType.fromBase(toaPlayerLogoutState.getEncounterType()).getRandomizedSpawnTile()));
					setCurrentEncounter(area.getParty().getCurrentEncounterType());
					if (toaPlayerLogoutState.isDuringChallenge()) {
						player.sendMessage("You logged out during the challenge. Total deaths: <col=ff0000>" + area.getParty().getTotalDeaths() + "</col>.");
						if (EncounterStage.STARTED.equals(area.getStage())) {
							area.turnIntoDeadGhost(player);
						}
					}
					area.checkRoomReset();
				}
			} else {
				if (toaPlayerLogoutState.isDuringChallenge() && !toaPlayerLogoutState.isSafeDeath()) {
					player.getTOAManager().triggerTOAFailure(true);
				} else {
					removeTOAItems();
					player.sendMessage("You were unable to rejoin your party.");
					if (!(regionArea instanceof TOALobbyArea)) {
						player.setLocation(OUTSIDE_LOCATION);
					}
				}
			}
			player.getPacketDispatcher().sendClientScript(948, 0, 0, 0, 255, 50);
			setToaPlayerLogoutState(null);
		}
	}

	@Override public void advanceRaid(boolean quickUse) {
		if (player.getArea() instanceof TOARaidArea raidArea) {
			final EncounterType current = raidParty.getCurrentEncounterType();
			final Runnable runnable = () -> {
				enter(false, EncounterType.VALUES[raidArea.getEncounterType().ordinal() + 1]);
				if (!raidParty.getCurrentEncounterType().equals(current)) {
					raidParty.getPlayers().stream().filter(p -> !player.getUsername().equals(p.getUsername())).forEach(
							p -> p.sendMessage(player.getUsername() + " has proceeded to the next challenge. Join " + (player.getAppearance().isMale() ? "him" : "her") + "..."));
				}
			};
			if (!quickUse) {
				player.getDialogueManager().start(new OptionDialogue(player, "Are you ready to proceed?", new String[] {"Yes.", "No."}, new Runnable[] {runnable, null}));
			} else {
				runnable.run();
			}
		}
	}

	public void triggerTOAFailure(boolean playJingle) {
		removeTOAItems();
		player.setLocation(getRandomizedOutsideLocation());
		player.sendMessage("You failed to survive the Tombs of Amascut.");
		player.getDeathMechanics().service(ItemRetrievalService.RetrievalServiceType.TOMBS_OF_AMASCUT, null, false);
		ItemRetrievalService.updateVarps(player);
		if (!player.getRetrievalService().getContainer().isEmpty()) {
			player.sendMessage("A magical chest has retrieved some of your items. You can collect them from it in the Tombs of Amascut lobby.");
		}
		if (playJingle) {
			player.getMusic().playJingle(90);
		}
	}

	@Override public void resetSessionAttributes() {
		currentEncounter = null;
		individualDeaths = 0;
		toaPlayerLogoutState = null;
		canClaimSupplies = false;
		suppliesContainer = null;
		points = 0;
		damageDone = 0;
		damageTaken = 0;
	}

	public static Location getRandomizedOutsideLocation() {
		return OUTSIDE_LOCATION.transform(Utils.random(2), Utils.random(1));
	}

	@Override public void removeTOAItems() {
		for (int id : TOA_REGULAR_ITEM_IDS) {
			player.getInventory().deleteItem(id, Integer.MAX_VALUE);
		}
		for (int id : TOA_SUPPLIES_ITEM_IDS) {
			player.getInventory().deleteItem(id, Integer.MAX_VALUE);
		}
	}

	@Override public void withdrawSupplies(int amount) {
		if (suppliesContainer == null) {
			return;
		}
		amount = Math.min(amount, suppliesContainer.getSize());
		final int freeSlots = player.getInventory().getFreeSlots();
		final int withdrawAmount = Math.min(amount, player.getInventory().getFreeSlots());
		if (withdrawAmount > freeSlots) {
			player.sendMessage("You do not have enough space in your inventory to withdraw your supplies.");
		}
		if (withdrawAmount > 0) {
			for (int i = 0; i < withdrawAmount; i++) {
				player.getInventory().addItem(suppliesContainer.get(i));
				suppliesContainer.remove(i, 1);
			}
			suppliesContainer.shift();
			suppliesContainer.refresh(player);
			suppliesContainer.setFullUpdate(true);
			if (suppliesContainer.getSize() < 1) {
				player.getInventory().deleteItem(ItemId.SUPPLIES, 1);
				player.getInterfaceHandler().closeInterface(GameInterface.TOA_SUPPLIES_INV);
			}
		}
	}

	@Override public void reSupply() {
		if (suppliesContainer == null) {
			return;
		}
		int availableSpaces = player.getInventory().getContainer().getFreeSlotsSize();
		itemLoop : for (Item item : suppliesContainer.getItemsAsList()) {
			if (item == null) {
				continue;
			}
			for (int id : TOA_SUPPLIES_ITEM_IDS) {
				if (item.getId() == id) {
					if (availableSpaces < 1) {
						player.sendMessage("Your inventory is full.");
						break itemLoop;
					} else {
						player.getInventory().getContainer().add(item);
						suppliesContainer.remove(item);
						availableSpaces--;
					}
				}
			}
		}
	}

	@Override public boolean storeSupply(int slot, Item item) {
		final int freeSlots = suppliesContainer.getFreeSlotsSize();
		for (int id : TOA_SUPPLIES_ITEM_IDS) {
			if (item.getId() == id) {
				if (freeSlots < 0) {
					player.sendMessage("Your supply bag is full.");
				} else {
					player.getInventory().deleteItem(slot, item);
					suppliesContainer.add(item);
				}
				return true;
			}
		}
		return false;
	}

	@Override public void withdrawSpecificSupplies(int slotId, int option) {
		if (suppliesContainer == null || slotId >= suppliesContainer.getSize()) {
			return;
		}
		final Item item = suppliesContainer.get(slotId);
		if (item == null) {
			return;
		}
		final int freeSlots = player.getInventory().getFreeSlots();
		if (option == 4) {
			World.spawnFloorItem(item, player);
			suppliesContainer.set(slotId, null);
			suppliesContainer.shift();
		} else {
			int amount = 1;
			if (option == 2) {
				amount = 5;
			} else if (option == 3) {
				amount = 28;
			}
			final int removeAmount = Math.min(amount, suppliesContainer.getAmountOf(item.getId()));
			if (removeAmount > freeSlots) {
				player.sendMessage("You do not have enough space in your inventory to withdraw your supplies.");
			}
			int withdrawAmount = Math.min(removeAmount, freeSlots);
			suppliesContainer.set(slotId, null);
			player.getInventory().addItem(item);
			withdrawAmount--;
			if (withdrawAmount > 0) {
				for (int i = 0; i < suppliesContainer.getContainerSize() && withdrawAmount > 0; i++) {
					final Item slotItem = suppliesContainer.get(i);
					if (slotItem != null && slotItem.getId() == item.getId()) {
						suppliesContainer.set(i, null);
						player.getInventory().addItem(slotItem);
						withdrawAmount--;
					}
				}
			}
			suppliesContainer.shift();
		}
		suppliesContainer.refresh(player);
		suppliesContainer.setFullUpdate(true);
		if (suppliesContainer.getSize() < 1) {
			player.getInventory().deleteItem(ItemId.SUPPLIES, 1);
			player.getInterfaceHandler().closeInterface(GameInterface.TOA_SUPPLIES_INV);
		}
	}

	@Override public Object getPartySettings() {
		if(partySettings == null) {
			partySettings = new TOAPartySettings();
		}
		return partySettings;
	}

	@Override public void setPartySettings(Object partySettings) { this.partySettings = (TOAPartySettings) partySettings; }

	@Override public Object getToaPlayerLogoutState() { return toaPlayerLogoutState; }

	@Override public void setToaPlayerLogoutState(Object toaPlayerLogoutState) { this.toaPlayerLogoutState = (TOAPlayerLogoutState) toaPlayerLogoutState; }



	@Override public EncounterType getCurrentEncounter() { return currentEncounter; }

	@Override public void setCurrentEncounter(Object currentEncounter) {
		this.currentEncounter = (EncounterType) currentEncounter;
	}

	@Override public int getIndividualDeaths() { return individualDeaths; }

	@Override public void setIndividualDeaths(int individualDeaths) { this.individualDeaths = individualDeaths; }

	@Override public void setCanClaimSupplies(boolean canClaimSupplies) {
		this.canClaimSupplies = canClaimSupplies;
	}

	@Override public boolean isCanClaimSupplies() { return canClaimSupplies; }

	@Override public void setSuppliesContainer(Container suppliesContainer) { this.suppliesContainer = suppliesContainer; }

	@Override public Container getSuppliesContainer() { return suppliesContainer; }

	@Override public Container getRewardContainer() { return rewardContainer; }

	@Override public void setRewardContainer(Container rewardContainer) { this.rewardContainer = rewardContainer; }

}
