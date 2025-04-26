package com.zenyte.plugins.drop;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 06/06/2019 18:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AbyssalCreatureDropProcessor extends DropProcessor {
    @Override
    public void attach() {
        put(5509, new PredicatedDrop("Only dropped if the player is not holding any runecrafting pouches."));
        put(5510, new PredicatedDrop("Only dropped if the largest pouch the player is carrying is a small runecrafting pouch."));
        put(5512, new PredicatedDrop("Only dropped if the largest pouch the player is carrying is a medium runecrafting pouch."));
        put(5514, new PredicatedDrop("Only dropped if the largest pouch the player is carrying is a large runecrafting pouch."));
        appendDrop(new DisplayedDrop(5509, 1, 1, 42));
        appendDrop(new DisplayedDrop(5510, 1, 1, 42));
        appendDrop(new DisplayedDrop(5512, 1, 1, 42));
        appendDrop(new DisplayedDrop(5514, 1, 1, 42));
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        if (Utils.random(41) == 0) {
            if (!killer.containsItem(5509)) {
                npc.dropItem(killer, new Item(5509));
            } else if (!killer.containsItem(5510)) {
                npc.dropItem(killer, new Item(5510));
            } else if (!killer.containsItem(5512)) {
                npc.dropItem(killer, new Item(5512));
            } else if (!killer.containsItem(5514)) {
                npc.dropItem(killer, new Item(5514));
            }
        }
    }

    @Override
    public int[] ids() {
        return new int[] {
                2584, 2585, 2586
        };
    }
}
