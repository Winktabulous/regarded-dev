
package com.zenyte.plugins.drop.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 12/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TorturedGorillaDropProcessor extends DropProcessor {
    @Override
    public void attach() {
        //Zenyte shard
        appendDrop(new DisplayedDrop(19529, 1, 1, 3000));
        //Limbs
        appendDrop(new DisplayedDrop(19592, 1, 1, 5000));
        //Spring
        appendDrop(new DisplayedDrop(19601, 1, 1, 5000));
        //Light frame
        appendDrop(new DisplayedDrop(19586, 1, 1, 7500));
        //Heavy frame
        appendDrop(new DisplayedDrop(19589, 1, 1, 15000));
        //Monkey tail
        appendDrop(new DisplayedDrop(19610, 1, 1, 15000));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random = random(15000);
            if (random <= 1) {
                return random == 0 ? new Item(19589) : new Item(19610);
            }
            random = random(7500);
            if (random == 0) {
                return new Item(19586);
            }
            random = random(5000);
            if (random <= 1) {
                return random == 0 ? new Item(19592) : new Item(19601);
            }
            if ((random(3000)) == 0) {
                return new Item(19529);
            }
        }
        return super.drop(npc, killer, drop, item);
    }

    @Override
    public int[] ids() {
        return new int[] {7095, 7096, 7097, 7150, 7151, 7153};
    }
}
