package com.zenyte.plugins.drop.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 21/04/2019 17:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AbyssalSireProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(ItemId.UNSIRED, 1, 1, 40));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (randomDrop(killer, 40) == 0) {
                return new Item(ItemId.UNSIRED);
            }
        }
        return super.drop(npc, killer, drop, item);
    }

    @Override
    public int[] ids() {
        return new int[] {
                5886, 5887, 5888, 5889, 5890, 5891, 5908
        };
    }
}
