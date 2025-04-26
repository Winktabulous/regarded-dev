package com.zenyte.plugins.drop.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 19:31
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KuraskProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Leaf-bladed sword
        appendDrop(new DisplayedDrop(11902, 1, 1, 192));
        //Mystic robe top (light)
        appendDrop(new DisplayedDrop(4111, 1, 1, 256));
        //Leaf-bladed battleaxe
        appendDrop(new DisplayedDrop(20727, 1, 1, 384));
        //Kurask head
        appendDrop(new DisplayedDrop(7978, 1, 1, 1500));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(1500) == 0) {
                return new Item(7978);
            }
            if (random(384) == 0) {
                return new Item(20727);
            }
            if (random(256) == 0) {
                return new Item(4111);
            }
            if (random(192) == 0) {
                return new Item(11902);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 410, 411 };
    }
}
