package com.zenyte.plugins.dialogue;

import com.near_reality.game.model.item.degrading.Degradeable;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.model.item.degradableitems.RepairableItem;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 18 jan. 2018 : 18:01:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class RepairItemD extends Dialogue {
	private final Item item;
	private final boolean armourstand;

	public RepairItemD(final Player player, final Item item, final boolean armourstand) {
		super(player);
		this.item = item;
		this.armourstand = armourstand;
	}

	@Override
	public void buildDialogue() {
		final RepairableItem repairable = RepairableItem.getItem(item);
		if (repairable == null || item.getId() == repairable.getIds()[0]) {
			player.sendMessage("Nothing interesting happens.");
			return;
		}
		final int cost = (int) repairable.getCost(player, item, armourstand);
		final Degradeable degradable = DegradableItem.ITEMS.get(item.getId());
		if (degradable != null && item.getCharges() >= degradable.getMaximumCharges()) {
			item(item, "This item already is fully repaired.");
			return;
		}
		options("That will cost " + cost + " to repair.", "Okay.", "Cancel.").onOptionOne(() -> {
			if (player.getInventory().containsItem(995, cost)) {
				Item repaired = new Item(repairable.getIds()[0], 1);//Do not add charges cus the default item has 0 for tradability and stackability reasons.
				player.getInventory().deleteItem(995, cost);
				player.getInventory().deleteItem(item);
				player.getInventory().addItem(repaired);
				finish();
			} else setKey(2);
		});
		plain("You don't have enough coins to repair this item.");
	}
}
