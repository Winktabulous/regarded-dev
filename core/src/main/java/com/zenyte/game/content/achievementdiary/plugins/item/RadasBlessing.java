package com.zenyte.game.content.achievementdiary.plugins.item;

import com.zenyte.game.content.achievementdiary.plugins.DiaryItem;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.item.ItemId;

/**
 * @author Tommeh | 17/11/2019 | 14:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class RadasBlessing extends DiaryItem {
    @Override
    public void handle() {
        bind("Kourend Woodland", (player, item, slotId) -> {
            final int limit = item.getId() == ItemId.RADAS_BLESSING_1 ? 3 : item.getId() == ItemId.RADAS_BLESSING_2 ? 5 : -1;
            final int usedTeleports = player.getVariables().getKourendWoodlandTeleports();
            if (limit != -1 && usedTeleports >= limit) {
                player.sendMessage("You have used up your daily teleports for today.");
                return;
            }
            if (limit != -1) {
                player.getVariables().setKourendWoodlandTeleports(usedTeleports + 1);
                player.getTemporaryAttributes().put("kourend woodland restricted teleport", true);
                player.getTemporaryAttributes().put("kourend woodland teleport limit", limit);
            }
            TeleportCollection.RADAS_BLESSING_KOUREND_WOODLAND.teleport(player);
        });
        bind("Mount Karuulm", (player, item, slotId) -> {
            final int limit = item.getId() == ItemId.RADAS_BLESSING_3 ? 3 : -1;
            final int usedTeleports = player.getVariables().getMountKaruulmTeleports();
            if (limit != -1 && usedTeleports >= limit) {
                player.sendMessage("You have used up your daily teleports for today.");
                return;
            }
            if (limit != -1) {
                player.getVariables().setMountKaruulmTeleports(usedTeleports + 1);
                player.getTemporaryAttributes().put("mount karuulm restricted teleport", true);
                player.getTemporaryAttributes().put("mount karuulm teleport limit", limit);
            }
            TeleportCollection.RADAS_BLESSING_MOUNT_KARUULM.teleport(player);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.RADAS_BLESSING_1, ItemId.RADAS_BLESSING_2, ItemId.RADAS_BLESSING_3, ItemId.RADAS_BLESSING_4};
    }
}
