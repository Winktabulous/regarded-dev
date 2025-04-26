package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;

public class VigourOfInquisition extends Boon {
    private static final Item helm = new Item(ItemId.INQUISITORS_GREAT_HELM);
    private static final Item body = new Item(ItemId.INQUISITORS_HAUBERK);
    private static final Item legs = new Item(ItemId.INQUISITORS_PLATESKIRT);

    public static boolean applies(Player t) {
        return t.getEquipment().containsItems(helm, body, legs);
    }

    @Override
    public String name() {
        return "Vigour of Inquisition";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_VigourOfInquisition;
    }

    @Override
    public String description() {
        return "Provides a 20% damage and accuracy boost to any crush attack when wearing a full set of Inquisitor armor";
    }

    @Override
    public int item() {
        return ItemId.INQUISITORS_GREAT_HELM;
    }
}
