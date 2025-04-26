package com.zenyte.game.content.boss.nightmare.object;

import com.zenyte.game.content.boss.nightmare.area.NightmareBossArea;
import com.zenyte.game.content.boss.nightmare.area.PhosaniInstance;
import com.zenyte.game.model.MinimapState;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;

public class PoolOfNightmares implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getAttributes().containsKey("Ignore phosani nightmare warning")) {
			enterFight(player);
			return;
		}

		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				plain("You are about to begin an encounter with Phosani's Nightmare. Dying during this encounter will not be considered as safe death. Are you sure you wish to begin?");
				options("Are you sure you wish to begin?", new DialogueOption("Yes.", () -> enterFight(player)), new DialogueOption("Yes, and don't ask again.", () -> {
					player.getAttributes().put("Ignore phosani nightmare warning", true);
					enterFight(player);
				}), new DialogueOption("No."));
			}
		});
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {29710};
	}

	public static void enterFight(Player player) {
		player.setAnimation(NightmareBossArea.ENTER_ANIMATION);
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				plain("The Nightmare pulls you into Phosani's dream as you drink from the pool.", false);
			}
		});
		player.getPacketDispatcher().sendMinimapState(MinimapState.MAP_DISABLED);
		new FadeScreen(player, () -> {
			try {
				final AllocatedArea area = MapBuilder.findEmptyChunk(64, 64);
				final PhosaniInstance instance = new PhosaniInstance(area, player);
				instance.constructRegion();
				player.teleport(instance.getLocation(3872, 9948, 3));
				player.setAnimation(NightmareBossArea.ENTER_ANIMATION_END);
				player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED);
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("The Nightmare pulls you into Phosani's dream as you drink from the pool.");
					}
				});
			} catch (OutOfSpaceException e) {
				throw new RuntimeException(e);
			}
		}).fade(3);
	}

}
