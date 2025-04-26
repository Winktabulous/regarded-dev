package com.zenyte.game.content.achievementdiary.plugins.item;

import com.zenyte.game.content.achievementdiary.plugins.DiaryItem;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 26-4-2019 | 19:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MorytaniaLegs extends DiaryItem {
    @Override
    public void handle() {
        bind("Ecto Teleport", (player, item, slotId) -> slimePitTeleport(player, item));
        bind("Ectofuntus Pit", (player, item, slotId) -> slimePitTeleport(player, item));
        bind("Burgh Teleport", (player, item, slotId) -> TeleportCollection.MORYTANIA_LEGS_BURGH_DE_ROTT.teleport(player));
        bind("Burgh de Rott", (player, item, slotId) -> TeleportCollection.MORYTANIA_LEGS_BURGH_DE_ROTT.teleport(player));
    }

    private void slimePitTeleport(final Player player, final Item item) {
        final int limit = item.getId() == 13112 ? 2 : item.getId() == 13113 ? 5 : item.getId() == 13114 ? 5 : -1;
        final int usedTeleports = player.getVariables().getSlimePitTeleports();
        if (limit != -1 && usedTeleports >= limit) {
            player.sendMessage(Colour.RED.wrap("You have used up your daily Slime Pit teleports for today."));
            return;
        }
        if (limit != -1) {
            player.getVariables().setSlimePitTeleports(usedTeleports + 1);
            player.getTemporaryAttributes().put("slime pit restricted teleport", true);
            player.getTemporaryAttributes().put("slime pit teleport limit", limit);
        }
        TeleportCollection.MORYTANIA_LEGS_SLIME_PIT.teleport(player);
    }

    @Override
    public int[] getItems() {
        return new int[] {13112, 13113, 13114, 13115};
    }
}
