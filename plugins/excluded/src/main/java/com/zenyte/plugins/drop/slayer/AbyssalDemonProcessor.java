package com.zenyte.plugins.drop.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AbyssalDemonProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Abyssal whip
        appendDrop(new DisplayedDrop(4151, 1, 1, 204));
        //Abyssal dagger
        appendDrop(new DisplayedDrop(13265, 1, 1, 16384));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(16384) == 0) {
                return new Item(13265);
            }
            if (random(204) == 0) {
                return new Item(4151);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 415, 416, 7241 };
    }
}
