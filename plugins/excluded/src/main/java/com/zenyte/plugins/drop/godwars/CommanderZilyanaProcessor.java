package com.zenyte.plugins.drop.godwars;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 17:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CommanderZilyanaProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Saradomin sword
        appendDrop(new DisplayedDrop(11838, 1, 1, 44));
        //Saradomin's light
        appendDrop(new DisplayedDrop(13256, 1, 1, 70));
        //Armadyl crossbow
        appendDrop(new DisplayedDrop(11785, 1, 1, 150));
        //Saradomin hilt
        appendDrop(new DisplayedDrop(11814, 1, 1, 150));
        //Godsword shards
        appendDrop(new DisplayedDrop(11818, 1, 1, 50));
        appendDrop(new DisplayedDrop(11820, 1, 1, 50));
        appendDrop(new DisplayedDrop(11822, 1, 1, 50));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random;
            if ((random = randomDrop(killer,150)) < 3) {
                return new Item(11818 + (random * 2));
            }
            if ((random = randomDrop(killer, 300)) < 2) {
                return new Item(random == 0 ? 11785 : 11814);
            }
            if (randomDrop(killer, 70) == 0) {
                return new Item(13256);
            }
            if (randomDrop(killer, 44) == 0) {
                return new Item(11838);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 2205, 6493 };
    }
}
