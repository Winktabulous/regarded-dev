package com.zenyte.plugins.drop.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DarkBeastProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Dark bow
        appendDrop(new DisplayedDrop(11235, 1, 1, 192));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(192) == 0) {
                return new Item(11235);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 4005, 7250 };
    }
}
