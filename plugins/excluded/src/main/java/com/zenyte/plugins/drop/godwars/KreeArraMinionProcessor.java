package com.zenyte.plugins.drop.godwars;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 17:03
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KreeArraMinionProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Armour
        appendDrop(new DisplayedDrop(11826, 1, 1, 12150));
        appendDrop(new DisplayedDrop(11828, 1, 1, 12150));
        appendDrop(new DisplayedDrop(11830, 1, 1, 12150));
        //Godsword shards
        appendDrop(new DisplayedDrop(11818, 1, 1, 1143));
        appendDrop(new DisplayedDrop(11820, 1, 1, 1143));
        appendDrop(new DisplayedDrop(11822, 1, 1, 1143));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random;
            if ((random = random(12150)) < 3) {
                return new Item(11826 + (random * 2));
            }
            if ((random = random(1143)) < 3) {
                return new Item(11818 + (random * 2));
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 3163, 3164, 3165 };
    }
}
