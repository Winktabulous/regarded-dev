package com.zenyte.plugins.drop.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 21/04/2019 18:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FossilIslandWyvern extends DropProcessor {
    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(1920) == 0) {
                return new Item(21643);
            } else if (random(384) == 0) {
                return new Item(21646);
            }
        }
        return super.drop(npc, killer, drop, item);
    }

    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(21637, 1, 1, 3000));
        appendDrop(new DisplayedDrop(21643, 1, 1, 1920));
        appendDrop(new DisplayedDrop(21646, 1, 1, 384));
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        if (random(3000) == 0) {
            npc.dropItem(killer, new Item(21637));
        }
    }

    @Override
    public int[] ids() {
        return new int[] {
                7794, 7793, 7792
        };
    }
}
