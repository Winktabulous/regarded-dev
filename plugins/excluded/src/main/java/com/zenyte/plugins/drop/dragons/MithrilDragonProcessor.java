package com.zenyte.plugins.drop.dragons;

import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19/01/2019 01:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MithrilDragonProcessor extends DropProcessor {

    @Override
    public void attach() {
        this.put(2359, new PredicatedDrop("This drop will be noted if you have the 'Duly noted' Slayer reward unlocked and if you're on an assignment."));
        appendDrop(new DisplayedDrop(11335, 1, 1, 32768));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (item.getId() == 2359 && killer != null && killer.getSlayer() != null
                && killer.getSlayer().isUnlocked("Duly noted")
                && killer.getSlayer().getAssignment() != null
                && killer.getSlayer().getAssignment().getTask() == RegularTask.MITHRIL_DRAGONS) {
            return new Item(2360, item.getAmount());
        } else if (!drop.isAlways()) {
            if (random(32768) == 0) {
                return new Item(11335);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 2919, 8088, 8089 };
    }
}
