package com.zenyte.game.content.achievementdiary.plugins.item;

import com.zenyte.game.content.achievementdiary.plugins.DiaryItem;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.util.Colour;

/**
 * @author Tommeh | 18-11-2018 | 15:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FremennikSeaBoots extends DiaryItem {
    @Override
    public void handle() {
        bind("Teleport", (player, item, slotId) -> {
            final int limit = item.getId() == 13132 ? -1 : 1;
            final int usedTeleports = player.getVariables().getRellekkaTeleports();
            if (limit != -1 && usedTeleports >= limit) {
                player.sendMessage(Colour.RED.wrap("You have used up your daily Rellekka teleports for today."));
                return;
            }
            if (limit != -1) {
                player.getVariables().setRellekkaTeleports(usedTeleports + 1);
                player.getTemporaryAttributes().put("rellekka restricted teleport", true);
            }
            TeleportCollection.FREMENNIK_SEA_BOOTS_RELLEKKA_TELEPORT.teleport(player);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {13129, 13130, 13131, 13132};
    }
}
