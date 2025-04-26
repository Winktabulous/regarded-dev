package com.zenyte.game.content.kebos.konar.plugins.processors;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 02/11/2019 | 14:30
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class DrakeProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(ItemId.DRAKES_TOOTH, 1, 1, 1920));
        appendDrop(new DisplayedDrop(ItemId.DRAKES_CLAW, 1, 1, 1920));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_THROWNAXE, 100, 200, 7500));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_KNIFE, 100, 200, 7500));
        put(8612, ItemId.DRAKES_TOOTH, new PredicatedDrop("This drop becomes 5 times more common during a slayer assignment."));
        put(8612, ItemId.DRAKES_CLAW, new PredicatedDrop("This drop becomes 5 times more common during a slayer assignment."));
        put(8612, ItemId.DRAGON_THROWNAXE, new PredicatedDrop("This drop becomes 5 times more common during a slayer assignment."));
        put(8612, ItemId.DRAGON_KNIFE, new PredicatedDrop("This drop becomes 5 times more common during a slayer assignment."));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int rate = killer.getSlayer().isCurrentAssignment(npc) ? 384 : 1920;
            if (random(rate) == 0) {
                return new Item(Utils.random(1) == 0 ? ItemId.DRAKES_TOOTH : ItemId.DRAKES_CLAW);
            }
            rate = killer.getSlayer().isCurrentAssignment(npc) ? 1500 : 7500;
            if (random(rate) == 0) {
                return new Item(Utils.random(1) == 0 ? ItemId.DRAGON_THROWNAXE : ItemId.DRAGON_KNIFE, Utils.random(100, 200));
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {8612};
    }
}
