package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.content.boons.DisabledBoon;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.plugins.equipment.equip.EquipPlugin;
import org.jetbrains.annotations.NotNull;

@DisabledBoon
public class TorvasGluttony extends Boon implements EquipPlugin {
    @Override
    public String name() {
        return "Torva's Gluttony";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_TorvasGluttony;
    }

    @Override
    public String description() {
        return "Wearing a full set of Torva increases your maximum HP by 21 (max: 120).";
    }

    @Override
    public int item() {
        return ItemId.TORVA_FULLHELM;
    }

    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        return true;
    }

    @Override
    public void onEquip(Player player, Container container, Item equippedItem) {
        EquipPlugin.super.onEquip(player, container, equippedItem);
    }

    @Override
    public void onLogin(@NotNull Player player, @NotNull Item item, int slot) {
        if(player.hasBoon(this.getClass())) {
            EquipPlugin.super.onLogin(player, item, slot);
            if (player.getEquipment().wearingFullTorva()) {
                player.setTorvaBoosted(true);
            }
        }
    }

    @Override
    public int[] getItems() {
        return new int[]{ItemId.TORVA_FULLHELM, ItemId.TORVA_PLATEBODY, ItemId.TORVA_PLATELEGS};
    }
}
