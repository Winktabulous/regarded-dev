package com.zenyte.game.content.achievementdiary.plugins.drop;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 21-11-2018 | 17:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DesertGoatHornProcessor extends DropProcessor {

    private static final int DESERT_GOAT_HORN = 9735;
    private static final int DESERT_GOAT_HORN_NOTED = 9738;

    @Override
    public void attach() {
        put(DESERT_GOAT_HORN, new PredicatedDrop("This drop will be noted if you have the easy Desert diary completed."));
    }

    @Override
    public Item drop(NPC npc, Player killer, Drop drop, Item item) {
        if (item.getId() == DESERT_GOAT_HORN && DiaryUtil.eligibleFor(DiaryReward.DESERT_AMULET1, killer)) {
            item.setId(DESERT_GOAT_HORN_NOTED);
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 1795, 1796, 1797 };
    }
}
