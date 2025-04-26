package com.zenyte.game.content.achievementdiary.plugins.item;

import com.zenyte.game.content.achievementdiary.plugins.DiaryItem;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;

/**
 * @author Tommeh | 18-11-2018 | 16:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KaramjaGloves extends DiaryItem {

    @Override
    public void handle() {
        bind("Gem Mine", (player, item, slotId) -> TeleportCollection.KARAMJA_GLOVES_GEM_MINE.teleport(player));
        bind("Duradel", (player, item, slotId) -> TeleportCollection.KARAMJA_GLOVES_DURADEL.teleport(player));
    }

    @Override
    public int[] getItems() {
        return new int[] { 11136, 11138, 11140, 13103 };
    }
}
