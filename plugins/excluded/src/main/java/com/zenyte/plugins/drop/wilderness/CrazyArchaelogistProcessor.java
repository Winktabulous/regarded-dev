package com.zenyte.plugins.drop.wilderness;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CrazyArchaelogistProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Fedora
        appendDrop(new DisplayedDrop(11990, 1, 1, 96));
        //Odium shard 2
        appendDrop(new DisplayedDrop(11929, 1, 1, 102));
        //Malediction shard 2
        appendDrop(new DisplayedDrop(11932, 1, 1, 102));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random;
            if ((random = random(102)) < 2) {
                return new Item(11929 + (random * 3));
            }
            if (random(96) == 0) {
                return new Item(11990);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 6618 };
    }
}
