package com.zenyte.plugins.drop.wilderness;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ScorpiaProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Odium shard 3
        appendDrop(new DisplayedDrop(11930, 1, 1, 102));
        //Malediction shard 3
        appendDrop(new DisplayedDrop(11933, 1, 1, 102));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random;
            if ((random = random(102)) < 2) {
                return new Item(11930 + (random * 3));
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 6615 };
    }
}
