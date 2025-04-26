package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.utils.TextUtils;

@SuppressWarnings("unused")
public class AmuletOfBloodFury extends ItemPlugin implements ChargeExtension {

	@Override
	public void handle() {
		bind("revert", (player, item, container, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				item(item, "Reverting your <col=00080>" + item.getName() + "</col> will not grant you back your blood shard.");
				options("Are you sure you want to revert your <col=00080>" + item.getName() + "</col>?", "Yes, I'm " +
						"sure.", "No.").onOptionOne(() -> {
					if (container.get(slotId) == item) {
						container.set(slotId, new Item(ItemId.AMULET_OF_FURY));
						container.refresh(player);
					}
					setKey(5);
				});
				item(5, item, "Your <col=00080>" + item.getName() + "</col> was successfully reverted.");
			}
		}));
	}

	@Override
	public int[] getItems() {
		return new int[] {ItemId.AMULET_OF_BLOOD_FURY};
	}

	@Override
	public void removeCharges(final Player player, final Item item, final ContainerWrapper wrapper, int slotId, final int amount) {
		if (player.getCombatAchievements().hasTierCompleted(CATierType.MASTER) && Utils.random(19) == 0) {
			player.sendFilteredMessage("As you've completed the master combat achievements, you have been prevented from losing a charge.");
			return;
		}
		final int charges = item.getCharges();
		item.setCharges((charges - amount) <= 0 ? 10_000 : (charges - amount));
		if ((charges - amount) <= 0) {
			player.getEquipment().set(EquipmentSlot.AMULET.getSlot(), new Item(ItemId.AMULET_OF_FURY));
			player.sendMessage(Colour.RS_PURPLE.wrap("Your Amulet of Blood Fury has shattered."));
		}
	}

	@Override
	public void checkCharges(final Player player, final Item item) {
		int charges = item.getCharges();
		if(charges == 0) {
			item.setCharges(10_000);
			charges = 10_000;
		}
		player.sendMessage("Your " + item.getName() + " has " + TextUtils.formatCurrency(charges) + " charge" + (charges == 1 ? "" : "s") + " remaining.");
	}

}
