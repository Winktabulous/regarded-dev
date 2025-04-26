package com.zenyte.game.content.boss.bryophyta;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 26/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BryophytaProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(ItemId.BRYOPHYTAS_ESSENCE, 1, 1, 58));
        appendDrop(new DisplayedDrop(ItemId.MOSSY_KEY, 1, 1, 12));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(58) == 0) {
                return new Item(ItemId.BRYOPHYTAS_ESSENCE);
            } else if (random(12) == 0) {
                return new Item(ItemId.MOSSY_KEY);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {
                8195
        };
    }
}
