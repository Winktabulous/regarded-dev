package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 05/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EquipTeamCapePlugin implements EquipPlugin {
    @Override
    public boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot) {
        if (slotId != -1) {
            player.getAchievementDiaries().update(WildernessDiary.EQUIP_TEAM_CAPE);
        }
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.TEAM1_CAPE, ItemId.TEAM2_CAPE, ItemId.TEAM3_CAPE, ItemId.TEAM4_CAPE, ItemId.TEAM5_CAPE, ItemId.TEAM6_CAPE, ItemId.TEAM7_CAPE, ItemId.TEAM8_CAPE, ItemId.TEAM9_CAPE,
                ItemId.TEAM10_CAPE, ItemId.TEAM11_CAPE, ItemId.TEAM12_CAPE, ItemId.TEAM13_CAPE, ItemId.TEAM14_CAPE, ItemId.TEAM15_CAPE, ItemId.TEAM16_CAPE, ItemId.TEAM17_CAPE, ItemId.TEAM18_CAPE,
                ItemId.TEAM19_CAPE, ItemId.TEAM20_CAPE, ItemId.TEAM21_CAPE, ItemId.TEAM22_CAPE, ItemId.TEAM23_CAPE, ItemId.TEAM24_CAPE, ItemId.TEAM25_CAPE, ItemId.TEAM26_CAPE, ItemId.TEAM27_CAPE,
                ItemId.TEAM28_CAPE, ItemId.TEAM29_CAPE, ItemId.TEAM30_CAPE, ItemId.TEAM31_CAPE, ItemId.TEAM32_CAPE, ItemId.TEAM33_CAPE, ItemId.TEAM34_CAPE, ItemId.TEAM35_CAPE, ItemId.TEAM36_CAPE,
                ItemId.TEAM37_CAPE, ItemId.TEAM38_CAPE, ItemId.TEAM39_CAPE, ItemId.TEAM40_CAPE, ItemId.TEAM41_CAPE, ItemId.TEAM42_CAPE, ItemId.TEAM43_CAPE, ItemId.TEAM44_CAPE, ItemId.TEAM45_CAPE,
                ItemId.TEAM46_CAPE, ItemId.TEAM47_CAPE, ItemId.TEAM48_CAPE, ItemId.TEAM49_CAPE, ItemId.TEAM50_CAPE, ItemId.TEAM1_CAPE_10638
        };
    }
}
