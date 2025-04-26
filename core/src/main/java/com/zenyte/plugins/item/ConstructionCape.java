package com.zenyte.plugins.item;

import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.item.capes.NewMaxCapes;

/**
 * @author Kris | 16/03/2019 02:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ConstructionCape extends ItemPlugin {
    @Override
    public void handle() {
        bind("Tele to POH", (player, item, slotId) -> player.sendMessage("Construction is currently disabled."
        ));
        bind("Teleport", (player, item, slotId) -> NewMaxCapes.sendPOHPortalTeleports(player));
    }

    @Override
    public int[] getItems() {
        return SkillcapePerk.CONSTRUCTION.getSkillCapes();
    }
}
