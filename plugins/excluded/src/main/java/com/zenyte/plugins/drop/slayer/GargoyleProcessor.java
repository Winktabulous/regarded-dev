package com.zenyte.plugins.drop.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GargoyleProcessor extends DropProcessor {

    private static final int BRITTLE_KEY = 21724;

    @Override
    public void attach() {
        //Granite maul
        appendDrop(new DisplayedDrop(4153, 1, 1, 128));
        //Mystic robe top (dark)
        appendDrop(new DisplayedDrop(4101, 1, 1, 204));
        //Brittle key
        appendDrop(new DisplayedDrop(BRITTLE_KEY, 1, 1, 50));
        put(BRITTLE_KEY, new PredicatedDrop("This item is only dropped if you don't already have one."));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (killer.getSlayer().isCurrentAssignment(npc) && !killer.getBooleanAttribute("brittle-entrance_unlocked") && !killer.containsItem(BRITTLE_KEY) && random(50) == 0) {
                return new Item (BRITTLE_KEY);
            }
            if (random(204) == 0) {
                return new Item(4101);
            }
            if (random(128) == 0) {
                return new Item(4153);
            }

        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 412, 1543, 7407 };
    }
}
