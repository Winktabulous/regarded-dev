package com.zenyte.game.content.boss.nightmare.area;

import com.near_reality.game.world.entity.player.PlayerDeathHandlerKt;
import com.near_reality.game.world.entity.player.TempIntefaceHandlerKt;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.boss.nightmare.*;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.model.MinimapState;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

import java.util.Set;

public class NightmareBossArea extends PolygonRegionArea implements DeathPlugin, CannonRestrictionPlugin, LootBroadcastPlugin {

	public static final Animation ENTER_ANIMATION = new Animation(8584);
	public static final Animation ENTER_ANIMATION_END = new Animation(8583);

	public static final int PROTECT_FROM_MAGIC_BUTTON = Interface.encode(17, -1);
	public static final int PROTECT_FROM_MISSILES_BUTTON = Interface.encode(18, -1);
	public static final int PROTECT_FROM_MELEE_BUTTON = Interface.encode(19, -1);

	public static NightmareNPC boss;
	public static NightmareTotemNE totemNE;
	public static NightmareTotemNW totemNW;
	public static NightmareTotemSE totemSE;
	public static NightmareTotemSW totemSW;
	private static WorldTask wakeUpPulse;

	public static String buildFirstInspectMessage() {
		int size = getAreaPlayers().size();
		if (size == 0) {
			return "There are no adventurers in the dream.";
		}

		if (size == 1) {
			return "There is 1 adventurer in the dream.";
		}

		return "There are " + size + " adventurers in the dream.";
	}

	public static String buildSecondInspectMessage() {
		if (boss.getId() != NightmareNPC.SLEEPING) {
			return "The fight has started and the Nightmare has taken some damage.";
		}

		return "The fight has not yet started.";
	}

	@Override
	protected RSPolygon[] polygons() {
		return new RSPolygon[]{new RSPolygon(15515)};
	}

	@Override
	public void enter(Player player) {
		enterStatic(player);
	}


	public static void enterStatic(Player player) {
		player.setViewDistance(Player.LARGE_VIEWPORT_RADIUS);
		GameInterface.DEATHS_OFFICE_ENTER_OVERLAY.open(player);

		TempIntefaceHandlerKt.tempInterfaceBind(player, GameInterface.PRAYER_TAB_INTERFACE, PROTECT_FROM_MAGIC_BUTTON, player1 -> togglePrayer(player1, Prayer.PROTECT_FROM_MAGIC));
		TempIntefaceHandlerKt.tempInterfaceBind(player, GameInterface.PRAYER_TAB_INTERFACE, PROTECT_FROM_MISSILES_BUTTON, player1 -> togglePrayer(player1, Prayer.PROTECT_FROM_MISSILES));
		TempIntefaceHandlerKt.tempInterfaceBind(player, GameInterface.PRAYER_TAB_INTERFACE, PROTECT_FROM_MELEE_BUTTON, player1 -> togglePrayer(player1, Prayer.PROTECT_FROM_MELEE));
	}

	private static void togglePrayer(Player player, Prayer prayer) {
		final int id;
		if (player.getBooleanTemporaryAttribute("nightmare_curse")) {
			id = BaseNightmareNPC.cursePrayerType(prayer).ordinal();
		} else {
			id = prayer.ordinal();
		}

		player.getPrayerManager().togglePrayer(id, false);
	}

	@Override
	public void leave(Player player, boolean logout) {
		if (!logout) {
			leaveStatic(player);
		} else {
			player.setLocation(new Location(3808, 9750, 1));
		}

		checkEmpty();
	}

	public static void leaveStatic(Player player) {
		player.getVarManager().sendBit(10151, 0);
		player.resetViewDistance();
		TempIntefaceHandlerKt.removeTempInterfaceBind(player, GameInterface.PRAYER_TAB_INTERFACE, PROTECT_FROM_MAGIC_BUTTON);
		TempIntefaceHandlerKt.removeTempInterfaceBind(player, GameInterface.PRAYER_TAB_INTERFACE, PROTECT_FROM_MISSILES_BUTTON);
		TempIntefaceHandlerKt.removeTempInterfaceBind(player, GameInterface.PRAYER_TAB_INTERFACE, PROTECT_FROM_MELEE_BUTTON);

		player.getHpHud().close();
		if (player.getInterfaceHandler().containsInterface(InterfacePosition.OVERLAY)) {
			WorldTasksManager.schedule(() -> player.getInterfaceHandler().closeInterface(GameInterface.DEATHS_OFFICE_ENTER_OVERLAY));
		}
		if (player.getInterfaceHandler().containsInterface(InterfacePosition.NIGHTMARE_TOTEMS_POS)) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.NIGHTMARE_TOTEMS_POS);
		}
	}

	private void checkEmpty() {
		if (!players.isEmpty()) {
			return;
		}

		NightmareLobbyArea.lobbyNPC.sleep();
		boss.death(false);
		if (wakeUpPulse != null) {
			wakeUpPulse.stop();
			wakeUpPulse = null;
		}
	}

	@Override
	public String name() {
		return "Nightmare combat";
	}

	public static void enterFight(Player player) {
		int lobbyNpcId = NightmareLobbyArea.lobbyNPC.getId();
		boolean needsReset = false;
		if(getAreaPlayers() != null && getAreaPlayers().size() == 0) {
			needsReset = true;
			NightmareLobbyArea.lobbyNPC.sleep();
			boss.death(false);
			if (wakeUpPulse != null) {
				wakeUpPulse.stop();
				wakeUpPulse = null;
			}
		}
		if (!needsReset && lobbyNpcId == NightmareLobbyNPC.FIRST_PHASE || lobbyNpcId == NightmareLobbyNPC.SECOND_PHASE || lobbyNpcId == NightmareLobbyNPC.THIRD_PHASE) {
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					plain("A group is already fighting the Nightmare. You'll have to wait until they are done.");
				}
			});
			return;
		}

		player.setAnimation(ENTER_ANIMATION);
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				plain("The Nightmare pulls you into her dream as you approach her.", false);
			}
		});
		player.getPacketDispatcher().sendMinimapState(MinimapState.MAP_DISABLED);
		new FadeScreen(player, () -> {
			player.teleport(new Location(3872, 9948, 3));
			player.setAnimation(ENTER_ANIMATION_END);
			player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED);
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
						plain("The Nightmare pulls you into her dream as you approach her.");
					}
			});
			checkWakePulse(50);
		}).fade(3);
	}

	private static void alertPlayers(String message) {
		NightmareLobbyArea.forEachPlayer(player -> player.sendMessage(message));
		for (Player player : getAreaPlayers()) {
			if (player == null) {
				continue;
			}

			player.sendMessage(message);
		}
	}

	public static void checkWakePulse(int initialTicks) {
		if (wakeUpPulse != null) {
			return;
		}

		NightmareLobbyArea.lobbyNPC.startAwaken();

		wakeUpPulse = new WorldTask() {
			int ticks = initialTicks;
			@Override
			public void run() {
				switch (ticks) {
					case 67 -> alertPlayers("The Nightmare will awaken in 40 seconds!");
					case 50 -> alertPlayers("The Nightmare will awaken in 30 seconds!");
					case 34 -> alertPlayers("The Nightmare will awaken in 20 seconds!");
					case 17 -> alertPlayers("The Nightmare will awaken in 10 seconds!");
					case 0 -> {
						alertPlayers("<col=E00A19>The Nightmare has awakened!</col>");
						boss.awaken();
						NightmareLobbyArea.lobbyNPC.awaken();
					}
					case -10 -> {
						wakeUpPulse = null;
						stop();
						return;
					}
				}

				ticks--;
			}
		};

		WorldTasksManager.schedule(wakeUpPulse, 0, 0);
	}

	public static Set<Player> getAreaPlayers() {
		return GlobalAreaManager.getArea(NightmareBossArea.class).getPlayers();
	}

	@Override
	public boolean isSafe() {
		return false;
	}

	@Override
	public String getDeathInformation() {
		return null;
	}

	@Override
	public Location getRespawnLocation() {
		return null;
	}

	@Override
	public boolean sendDeath(Player player, Entity source) {
		PlayerDeathHandlerKt.sendDeath(player, source, () -> {
			player.getDeathMechanics().service(ItemRetrievalService.RetrievalServiceType.NIGHTMARE, source, true);
			player.sendMessage("Shura has retrieved some of your items. You can collect them from her in the Sisterhood Sanctuary.");
			ItemRetrievalService.updateVarps(player);
			NightmareGlobal.updateDeathStatistics();
			player.incrementNumericAttribute("nightmare_death", 1);
			return null;
		});
		return true;
	}

}
