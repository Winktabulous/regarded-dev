package com.zenyte.game.content.achievementdiary.plugins.item;

import com.zenyte.game.content.achievementdiary.plugins.DiaryItem;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;

/**
 * @author Tommeh | 22-4-2019 | 20:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KandarinHeadgear extends DiaryItem {
    @Override
    public void handle() {
        bind("Teleport", (player, item, slotId) -> {
            final int limit = item.getId() == 13139 ? 1 : -1;
            final int teleports = player.getVariables().getSherlockTeleports();
            if (limit != -1 && teleports >= limit) {
                player.sendMessage("You have used up your daily teleports for today.");
                return;
            }
            player.getVariables().setSherlockTeleports(teleports + 1);
            TeleportCollection.KANDARIN_HEADGEAR_SHERLOCK.teleport(player);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {13137, 13138, 13139, 13140};
    }
}
