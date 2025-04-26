package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;

public class BrawnOfJustice extends Boon {
    private static final Item helm = new Item(ItemId.JUSTICIAR_FACEGUARD);
    private static final Item body = new Item(ItemId.JUSTICIAR_CHESTGUARD);
    private static final Item legs = new Item(ItemId.JUSTICIAR_LEGGUARDS);

    public static boolean applies(Player t) {
        return t.getEquipment().containsItems(helm, body, legs);
    }

    @Override
    public String name() {
        return "Brawn of Justice";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_BrawnOfJustice;
    }

    @Override
    public String description() {
        return "Wearing full Justiciar in ToB grants a 20% damage, accuracy and defense boost.";
    }

    @Override
    public int item() {
        return ItemId.JUSTICIAR_FACEGUARD;
    }
}
