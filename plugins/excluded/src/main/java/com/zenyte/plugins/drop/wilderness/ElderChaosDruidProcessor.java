
package com.zenyte.plugins.drop.wilderness;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 21/04/2019 17:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ElderChaosDruidProcessor extends DropProcessor {
    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(300) <= 2) {
                final int random = Utils.random(2);
                return new Item(random == 0 ? 20517 : random == 1 ? 20520 : 20595);
            }
            if(random(1000) == 0) {
                return new Item(ItemId.STAFF_OF_LIGHT);
            }
        }
        return super.drop(npc, killer, drop, item);
    }

    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(20595, 1, 1, 300));
        appendDrop(new DisplayedDrop(20517, 1, 1, 300));
        appendDrop(new DisplayedDrop(20520, 1, 1, 300));
        appendDrop(new DisplayedDrop(ItemId.STAFF_OF_LIGHT, 1, 1, 1000));
    }

    @Override
    public int[] ids() {
        return new int[] {6607};
    }
}
