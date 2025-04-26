package com.zenyte.game.content.achievementdiary.plugins.drop;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 19-11-2018 | 15:00
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BrimhavenDungeonRedDragonhideProcessor extends DropProcessor {

    private static final int RED_DRAGONHIDE = 1749;
    private static final int NOTED_RED_DRAGONHIDE = 1750;

    @Override
    public void attach() {
        put(RED_DRAGONHIDE, new PredicatedDrop("This drop will be noted in the Brimhaven Dungeon if you have the elite Karamja diary completed."));
    }

    @Override
    public Item drop(NPC npc, Player killer, Drop drop, Item item) {
        if (item.getId() == RED_DRAGONHIDE && killer.inArea("Brimhaven Dungeon") && DiaryUtil.eligibleFor(DiaryReward.KARAMJA_GLOVES4, killer)) {
            item.setId(NOTED_RED_DRAGONHIDE);
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 247, 248, 249, 250, 251};
    }
}
