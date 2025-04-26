package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.GameMode;

/**
 * @author Kris | 16/03/2019 17:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HCIronmanArmour implements EquipPlugin {
    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        if (slotId != -1 && !player.getGameMode().equals(GameMode.HARDCORE_IRON_MAN)) {
            player.sendMessage("You cannot wear the hardcore ironman armour without being a hardcore ironman.");
            return false;
        }
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {
                20792, 20794, 20796
        };
    }
}
