package com.zenyte.game.content.achievementdiary.plugins.drop;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 18-11-2018 | 22:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AviansieAdamantiteBarProcessor extends DropProcessor {

    private static final int ADAMANTITE_BAR = 2361;
    private static final int NOTED_ADAMANTITE_BAR = 2362;

    @Override
    public void attach() {
        put(ADAMANTITE_BAR, new PredicatedDrop("This drop will be noted once you have the hard Fremennik diary completed."));
    }

    @Override
    public Item drop(NPC npc, Player killer, Drop drop, Item item) {
        if (item.getId() == ADAMANTITE_BAR && DiaryUtil.eligibleFor(DiaryReward.FREMENNIK_SEA_BOOTS3, killer)) {
            item.setId(NOTED_ADAMANTITE_BAR);
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 3169, 3170, 3171, 3172, 3173, 3174, 3175, 3176, 3177, 3178, 3179, 3180, 3181, 3182, 3183 };
    }
}
