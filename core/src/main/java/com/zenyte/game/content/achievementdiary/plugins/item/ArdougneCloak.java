package com.zenyte.game.content.achievementdiary.plugins.item;

import com.zenyte.game.content.achievementdiary.plugins.DiaryItem;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 10-11-2018 | 18:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ArdougneCloak extends DiaryItem {
    @Override
    public void handle() {
        bind("Monastery Teleport", (player, item, slotId) -> TeleportCollection.ARDOUGNE_CLOAK_MONASTERY_TELEPORT.teleport(player));
        bind("Kandarin Monastery", (player, item, slotId) -> TeleportCollection.ARDOUGNE_CLOAK_MONASTERY_TELEPORT.teleport(player));
        bind("Farm Teleport", (player, item, slotId) -> ardougneFarmTeleport(player, item));
        bind("Ardougne Farm", (player, item, slotId) -> ardougneFarmTeleport(player, item));
    }

    public static void ardougneFarmTeleport(final Player player, final Item item) {
        final int limit = item.getId() == 13122 ? 3 : item.getId() == 13123 ? 5 : -1;
        final int usedTeleports = player.getVariables().getArdougneFarmTeleports();
        if (limit != -1 && usedTeleports >= limit) {
            player.sendMessage(Colour.RED.wrap("You have used up your daily Ardougne Farm teleports for today."));
            return;
        }
        if (limit != -1) {
            player.getVariables().setArdougneFarmTeleports(usedTeleports + 1);
            player.getTemporaryAttributes().put("ardougne farm restricted teleport", true);
            player.getTemporaryAttributes().put("ardougne farm teleport limit", limit);
        }
        TeleportCollection.ARDOUGNE_CLOAK_FARMING_TELEPORT.teleport(player);
    }

    @Override
    public int[] getItems() {
        return new int[] { 13121, 13122, 13123, 13124 };
    }
}
