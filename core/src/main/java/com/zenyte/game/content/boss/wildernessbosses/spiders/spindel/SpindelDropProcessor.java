package com.zenyte.game.content.boss.wildernessbosses.spiders.spindel;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

public class SpindelDropProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(ItemId.FANGS_OF_VENENATIS, 1, 1, 402));
        appendDrop(new DisplayedDrop(ItemId.TREASONOUS_RING, 1,1, 500));
        appendDrop(new DisplayedDrop(ItemId.VOIDWAKER_GEM, 1, 1, 547));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_PICKAXE, 1, 1, 251));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_2H_SWORD, 1, 1, 251));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (randomDrop(killer,547) == 0) {
                return new Item(ItemId.VOIDWAKER_GEM);
            }
            if (randomDrop(killer,500) == 0) {
                return new Item(ItemId.TREASONOUS_RING);
            }
            if (randomDrop(killer, 402) == 0) {
                return new Item(ItemId.FANGS_OF_VENENATIS);
            }
            if (randomDrop(killer,251) == 0) {
                int random = random(2);
                if(random == 0)
                    return new Item(ItemId.DRAGON_PICKAXE);
                else
                    return new Item(ItemId.DRAGON_2H_SWORD);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[]{NpcId.SPINDEL};
    }
}
