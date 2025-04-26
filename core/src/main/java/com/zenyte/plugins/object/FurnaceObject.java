package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.smithing.CannonballSmithing;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.DialogueManager;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.skills.CannonballSmithingD;
import com.zenyte.plugins.dialogue.skills.MoltenGlassD;
import com.zenyte.plugins.dialogue.skills.SmeltingD;

public final class FurnaceObject implements ObjectAction {
	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		final DialogueManager dialogueManager = player.getDialogueManager();
		final Inventory inventory = player.getInventory();
		if (inventory.containsItem(ItemId.SODA_ASH) && inventory.containsItem(ItemId.BUCKET_OF_SAND)) {
			player.getDialogueManager().start(new MoltenGlassD(player));
		} else if (inventory.containsItem(CannonballSmithing.MOULD) && inventory.containsItem(ItemId.STEEL_BAR)) {
			dialogueManager.start(new CannonballSmithingD(player));
		} else {
			dialogueManager.start(new SmeltingD(player, object));
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Furnace", "Clay forge", "Lava forge", "Small furnace"};
	}
}
