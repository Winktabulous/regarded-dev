package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 05/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FireCapeEquipPlugin implements EquipPlugin {
    @Override
    public boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot) {
        if (slotId != -1) {
            player.getAchievementDiaries().update(KaramjaDiary.EQUIP_FIRE_CAPE);
        }
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.FIRE_CAPE, ItemId.FIRE_CAPE_10566, ItemId.FIRE_CAPE_10637, ItemId.FIRE_MAX_CAPE, ItemId.FIRE_MAX_CAPE_21186,
                ItemId.INFERNAL_CAPE, ItemId.INFERNAL_MAX_CAPE, ItemId.INFERNAL_MAX_CAPE_21285
        };
    }
}
