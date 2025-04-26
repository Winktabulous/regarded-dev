package com.zenyte.plugins.drop.slayer;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 21/04/2019 17:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LizardmanShamanProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(13576, 1, 1, 2000));
        appendDrop(new DisplayedDrop(13392, 1, 1) {
            public double getRate(final Player player, final int id) {
                return DiaryUtil.eligibleFor(DiaryReward.RADAS_BLESSING1, player) ? 125 : 250;
            }
        });
        put(13392, new PredicatedDrop("This drop becomes 1/125 if you have the easy Kourend & Kebos diary completed."));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (randomDrop(killer, 2000) == 0) {
                return new Item(13576);
            } else if (random(DiaryUtil.eligibleFor(DiaryReward.RADAS_BLESSING1, killer) ? 125 : 250) == 0) {
                return new Item(13392);
            }
        }
        return super.drop(npc, killer, drop, item);
    }

    @Override
    public int[] ids() {
        return new int[] {
                6766, 6767, 8565, 7744, 7745
        };
    }
}
