package com.zenyte.plugins.drop.godwars;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 16:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KreeArraProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Armour
        appendDrop(new DisplayedDrop(11826, 1, 1, 100));
        appendDrop(new DisplayedDrop(11828, 1, 1, 100));
        appendDrop(new DisplayedDrop(11830, 1, 1, 100));
        //Armadyl hilt
        appendDrop(new DisplayedDrop(11810, 1, 1, 135));
        //Godsword shards
        appendDrop(new DisplayedDrop(11818, 1, 1, 50));
        appendDrop(new DisplayedDrop(11820, 1, 1, 50));
        appendDrop(new DisplayedDrop(11822, 1, 1, 50));
        //Soul Crystal
        appendDrop(new DisplayedDrop(32006, 1, 1, 500));
        put(11826, new PredicatedDrop("There is a 1/75 chance to roll the armour table, with a 1/225 chance for a specific piece."));
        put(11828, new PredicatedDrop("There is a 1/75 chance to roll the armour table, with a 1/225 chance for a specific piece."));
        put(11830, new PredicatedDrop("There is a 1/75 chance to roll the armour table, with a 1/225 chance for a specific piece."));
        put(11818, new PredicatedDrop("There is a 1/50 chance to roll the shard table, with a 1/150 chance for a specific shard."));
        put(11820, new PredicatedDrop("There is a 1/50 chance to roll the shard table, with a 1/150 chance for a specific shard."));
        put(11822, new PredicatedDrop("There is a 1/50 chance to roll the shard table, with a 1/150 chance for a specific shard."));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random;
            if(randomDrop(killer, 500) == 0) {
                return new Item(32006);
            }
            if ((random = randomDrop(killer, 150)) < 3) {
                return new Item(11818 + (random * 2));
            }
            if (randomDrop(killer, 135) == 0) {
                return new Item(11810);
            }
            if ((random = randomDrop(killer, 300)) < 3) {
                return new Item(11826 + (random * 2));
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 3162, 6492 };
    }
}
