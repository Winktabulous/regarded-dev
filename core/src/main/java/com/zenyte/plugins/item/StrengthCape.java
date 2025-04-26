package com.zenyte.plugins.item;

import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

import static com.zenyte.game.content.skills.magic.spells.teleports.ItemTeleport.MAX_CAPE_WARRIORS_GUILD;

/**
 * @author Kris | 16/03/2019 02:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StrengthCape extends ItemPlugin {
    @Override
    public void handle() {
        bind("Teleport", (player, item, slotId) -> MAX_CAPE_WARRIORS_GUILD.teleport(player));
        bind("Warriors' Guild", (player, item, slotId) -> MAX_CAPE_WARRIORS_GUILD.teleport(player));
    }

    @Override
    public int[] getItems() {
        return SkillcapePerk.STRENGTH.getSkillCapes();
    }
}
