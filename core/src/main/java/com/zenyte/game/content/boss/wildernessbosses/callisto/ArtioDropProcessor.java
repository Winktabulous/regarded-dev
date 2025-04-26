package com.zenyte.game.content.boss.wildernessbosses.callisto;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Andys1814
 */
public final class ArtioDropProcessor extends DropProcessor {


    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(27667, 1, 1, 402));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_2H_SWORD, 1, 1, 251));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_PICKAXE, 1, 1, 251));
        appendDrop(new DisplayedDrop(27681, 1, 1, 547));
        appendDrop(new DisplayedDrop(ItemId.TYRANNICAL_RING, 1, 1, 500));
    }

    @Override
    public Item drop(NPC npc, Player killer, Drop drop, Item item) {
        if (!drop.isAlways()) {
            if (random(402) == 0) {
                return new Item(27667);
            }
            if (random(251) == 0) {
                return new Item(Utils.roll(50) ? ItemId.DRAGON_2H_SWORD : ItemId.DRAGON_PICKAXE);
            }
            if (random(547) == 0) {
                return new Item(27681);
            }
            if (random(500) == 0) {
                return new Item(ItemId.TYRANNICAL_RING);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {
            NpcId.ARTIO,
        };
    }

}
