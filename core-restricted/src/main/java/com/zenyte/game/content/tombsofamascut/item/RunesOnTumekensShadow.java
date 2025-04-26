package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;

@SuppressWarnings("unused")
public class RunesOnTumekensShadow implements PairedItemOnItemPlugin {

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final int fromId = from.getId();
		final Item staff;
		final int staffSlot;
		if (fromId == ItemId.TUMEKENS_SHADOW || fromId == ItemId.TUMEKENS_SHADOW_UNCHARGED) {
			staff = from;
			staffSlot = fromSlot;
		} else {
			staff = to;
			staffSlot = toSlot;
		}

		TumekensShadowPlugin.charge(player, staff, staffSlot);
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[] {
				ItemPair.of(ItemId.SOUL_RUNE, ItemId.TUMEKENS_SHADOW),
				ItemPair.of(ItemId.CHAOS_RUNE, ItemId.TUMEKENS_SHADOW),
				ItemPair.of(ItemId.SOUL_RUNE, ItemId.TUMEKENS_SHADOW_UNCHARGED),
				ItemPair.of(ItemId.CHAOS_RUNE, ItemId.TUMEKENS_SHADOW_UNCHARGED),
		};
	}

}
