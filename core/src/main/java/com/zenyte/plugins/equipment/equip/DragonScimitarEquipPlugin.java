package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 10/04/2019 20:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DragonScimitarEquipPlugin implements EquipPlugin {
    @Override
    public boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot) {
        if (slotId != -1) {
            SherlockTask.WIELD_DRAGON_SCIM.progress(player);
        }
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {
            4587, 20000, 20406
        };
    }
}
