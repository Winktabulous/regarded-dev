package com.zenyte.plugins.drop.wilderness;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:29
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChaosElementalProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Dragon 2h sword
        appendDrop(new DisplayedDrop(7158, 1, 1, 96));
        //Dragon pickaxe
        appendDrop(new DisplayedDrop(11920, 1, 1, 142));
    }

    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(142) == 0) {
                return new Item(11920);
            }
            if (random(96) == 0) {
                return new Item(7158);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 2054, 6505 };
    }
}
