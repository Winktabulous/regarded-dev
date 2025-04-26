package com.zenyte.game.content.achievementdiary.plugins.item;

import com.zenyte.game.content.achievementdiary.plugins.DiaryItem;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;

/**
 * @author Tommeh | 22-4-2019 | 19:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class WesternBanner extends DiaryItem {
    @Override
    public void handle() {
        bind("Teleport", (player, item, slotId) -> {
            final int limit = item.getId() == 13141 ? 1 : item.getId() == 13142 ? 2 : item.getId() == 13143 ? 3 : -1;
            final int usedTeleports = player.getVariables().getFishingColonyTeleports();
            if (limit != -1 && usedTeleports >= limit) {
                player.sendMessage("You have used up your daily teleports for today.");
                return;
            }
            if (limit != -1) {
                player.getVariables().setFishingColonyTeleports(usedTeleports + 1);
                player.getTemporaryAttributes().put("fishing colony restricted teleport", true);
                player.getTemporaryAttributes().put("fishing colony teleport limit", limit);
            }
            TeleportCollection.WESTERN_BANNER_FISHING_COLONY.teleport(player);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {13141, 13142, 13143, 13144};
    }
}
