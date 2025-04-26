package com.zenyte.game.content.kebos.konar.plugins.processors;

import com.zenyte.game.content.kebos.alchemicalhydra.processor.AlchemicalHydraProcessor;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 02/11/2019 | 15:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class HydraProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(ItemId.DRAGON_THROWNAXE, 200, 400, 7500));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_KNIFE, 200, 400, 7500));
        appendDrop(new DisplayedDrop(ItemId.HYDRAS_EYE, 1, 1, 1375 * 3));
        appendDrop(new DisplayedDrop(ItemId.HYDRAS_FANG, 1, 1, 1375 * 3));
        appendDrop(new DisplayedDrop(ItemId.HYDRAS_HEART, 1, 1, 1375 * 3));
        appendDrop(new DisplayedDrop(ItemId.HYDRA_TAIL, 1, 1, 3750));
        put(8609, ItemId.DRAGON_THROWNAXE, new PredicatedDrop("This drop becomes 5 times more common during a slayer assignment."));
        put(8609, ItemId.DRAGON_KNIFE, new PredicatedDrop("This drop becomes 5 times more common during a slayer assignment."));
        put(8609, ItemId.HYDRAS_EYE, new PredicatedDrop("This drop becomes 5 times more common during a slayer assignment."));
        put(8609, ItemId.HYDRAS_FANG, new PredicatedDrop("This drop becomes 5 times more common during a slayer assignment."));
        put(8609, ItemId.HYDRAS_HEART, new PredicatedDrop("This drop becomes 5 times more common during a slayer assignment."));
        put(8609, ItemId.HYDRA_TAIL, new PredicatedDrop("This drop becomes 5 times more common during a slayer assignment."));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int rate = killer.getSlayer().isCurrentAssignment(npc) ? 1500 : 7500;
            if (random(rate) == 0) {
                return new Item(Utils.random(1) == 0 ? ItemId.DRAGON_THROWNAXE : ItemId.DRAGON_KNIFE, Utils.random(200, 400));
            }
            rate = killer.getSlayer().isCurrentAssignment(npc) ? 750 : 3750;
            if (random(rate) == 0) {
                return new Item(ItemId.HYDRA_TAIL);
            }
            rate = killer.getSlayer().isCurrentAssignment(npc) ? 275 : 1375;
            if ((random(rate * 3)) < 3) {
                return new Item(AlchemicalHydraProcessor.findLowestQuantityRingPart(killer));
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {8609};
    }
}
