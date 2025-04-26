package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.content.tombsofamascut.encounter.CrondisPuzzleEncounter;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class PalmTreeAction extends NPCPlugin implements ItemOnNPCAction {

	@Override public void handle() {
		bind("Water", (player, npc) -> waterPalm(player));
	}

	@Override public int[] getNPCs() {
		return new int[] {CrondisPuzzleEncounter.PALM_NPC_ID, CrondisPuzzleEncounter.PALM_NPC_ID + 1,
				CrondisPuzzleEncounter.PALM_NPC_ID + 2, CrondisPuzzleEncounter.PALM_NPC_ID + 3};
	}

	@Override public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
		waterPalm(player);
	}

	private void waterPalm(Player player) {
		if (player.getArea() instanceof CrondisPuzzleEncounter crondisPuzzleEncounter) {
			crondisPuzzleEncounter.waterPalm(player);
		}
	}

	@Override public Object[] getItems() {
		return new Object[] {CrondisPuzzleEncounter.CONTAINER_ITEM_ID};
	}

	@Override public Object[] getObjects() {
		return new Object[] {CrondisPuzzleEncounter.PALM_NPC_ID, CrondisPuzzleEncounter.PALM_NPC_ID + 1,
				CrondisPuzzleEncounter.PALM_NPC_ID + 2, CrondisPuzzleEncounter.PALM_NPC_ID + 3};
	}
}
