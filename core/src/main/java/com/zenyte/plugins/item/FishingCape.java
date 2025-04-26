package com.zenyte.plugins.item;

import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

import static com.zenyte.game.content.skills.magic.spells.teleports.ItemTeleport.MAX_CAPE_FISHING_GUILD;
import static com.zenyte.game.content.skills.magic.spells.teleports.ItemTeleport.MAX_CAPE_OTTOS_GROTTO;

/**
 * @author Kris | 16/03/2019 02:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FishingCape extends ItemPlugin {
    @Override
    public void handle() {
        bind("Fishing Guild", (player, item, slotId) -> MAX_CAPE_FISHING_GUILD.teleport(player));
        bind("Otto's Grotto", (player, item, slotId) -> MAX_CAPE_OTTOS_GROTTO.teleport(player));
    }

    @Override
    public int[] getItems() {
        return SkillcapePerk.FISHING.getSkillCapes();
    }
}
