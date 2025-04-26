package com.zenyte.game.content.achievementdiary.plugins.drop;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;

/**
 * @author Tommeh | 22-4-2019 | 16:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class WildernessDragonBonesProcessor extends DropProcessor {

    private static final int DRAGON_BONES = 536;
    private static final int LAVA_DRAGON_BONES = 11943;

    @Override
    public void attach() {
        put(DRAGON_BONES, new PredicatedDrop("This drop will be noted in the Wilderness if you have the elite Wilderness diary completed."));
        put(LAVA_DRAGON_BONES, new PredicatedDrop("This drop will be noted in the Wilderness if you have the elite Wilderness diary completed."));
    }

    @Override
    public Item drop(NPC npc, Player killer, Drop drop, Item item) {
        if ((item.getId() == DRAGON_BONES || item.getId() == LAVA_DRAGON_BONES) && WildernessArea.isWithinWilderness(npc) && DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD4, killer)) {
            item.setId(item.getDefinitions().getNotedId());
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 260, 261, 262, 263, 264, 7868, 7869, 7870, 6593,
                NpcId.BLACK_DRAGON_255, NpcId.BLACK_DRAGON_256, NpcId.BLACK_DRAGON_7861, NpcId.BLACK_DRAGON_7862, NpcId.BLACK_DRAGON_7863

        };
    }
}
