package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.GameMode;

/**
 * @author Kris | 16/03/2019 17:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class IronmanArmour implements EquipPlugin {
    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        if (slotId != -1 && !player.getGameMode().equals(GameMode.STANDARD_IRON_MAN)) {
            player.sendMessage("You cannot wear the ironman armour without being an ironman.");
            return false;
        }
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {
                12810, 12811, 12812
        };
    }
}
