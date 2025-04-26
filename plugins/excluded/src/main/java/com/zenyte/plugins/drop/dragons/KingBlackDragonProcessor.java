package com.zenyte.plugins.drop.dragons;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KingBlackDragonProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Kbd heads
        appendDrop(new DisplayedDrop(7980, 1, 1, 108));
        //Dragon pickaxe
        appendDrop(new DisplayedDrop(11920, 1, 1, 200));
        //Dragon Kite
        appendDrop(new DisplayedDrop(CustomItemId.DRAGON_KITE, 1, 1, 2000));

    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (randomDrop(killer, 2000) == 0) {
                return new Item(CustomItemId.DRAGON_KITE);
            }
            if (randomDrop(killer, 200) == 0) {
                return new Item(11920);
            }
            if (randomDrop(killer, 108) == 0) {
                return new Item(7980);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 239, 2642 };
    }
}
