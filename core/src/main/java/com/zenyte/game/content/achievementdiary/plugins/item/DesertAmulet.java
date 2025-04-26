package com.zenyte.game.content.achievementdiary.plugins.item;

import com.zenyte.game.content.achievementdiary.plugins.DiaryItem;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 21-11-2018 | 17:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DesertAmulet extends DiaryItem {
    @Override
    public void handle() {
        bind("Teleport", (player, item, slotId) -> nardahTeleport(player, item));
        bind("Nardah", (player, item, slotId) -> nardahTeleport(player, item));
        bind("Kalphite cave", (player, item, slotId) -> TeleportCollection.DESERT_AMULET_KALPHITE_HIVE.teleport(player));
    }

    private void nardahTeleport(final Player player, final Item item) {
        if (item.getId() == 13133) {
            //has teleport option but it doesn't mention the teleport feature on the wikia (drunk)
            player.sendMessage("Nothing interesting happens.");
            return;
        }
        final int limit = item.getId() == 13133 ? 0 : item.getId() == 13136 ? -1 : 1;
        final int usedTeleports = player.getVariables().getNardahTeleports();
        if (limit != -1 && usedTeleports >= limit) {
            player.sendMessage(Colour.RED.wrap("You have used up your daily Nardah teleports for today."));
            return;
        }
        if (limit != -1) {
            player.getVariables().setNardahTeleports(usedTeleports + 1);
            player.getTemporaryAttributes().put("nardah restricted teleport", true);
        }
        TeleportCollection.DESERT_AMULET_NARDAH.teleport(player);
    }

    @Override
    public int[] getItems() {
        return new int[] {13133, 13134, 13135, 13136};
    }
}
