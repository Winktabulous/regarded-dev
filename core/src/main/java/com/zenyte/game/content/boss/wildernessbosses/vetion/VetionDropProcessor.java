package com.zenyte.game.content.boss.wildernessbosses.vetion;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

import static com.zenyte.game.item.ItemId.SKULL_OF_VETION;

/**
 * @author Andys1814
 */
public final class VetionDropProcessor extends DropProcessor {

    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(SKULL_OF_VETION, 1, 1, 137));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_2H_SWORD, 1, 1, 179));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_PICKAXE, 1, 1, 179));
        appendDrop(new DisplayedDrop(ItemId.VOIDWAKER_BLADE, 1, 1, 252));
        appendDrop(new DisplayedDrop(ItemId.RING_OF_THE_GODS, 1, 1, 358));
    }

    @Override
    public Item drop(NPC npc, Player killer, Drop drop, Item item) {
        if (!drop.isAlways()) {
            if (random(137) == 0) {
                return new Item(SKULL_OF_VETION);
            }
            if (random(179) == 0) {
                return new Item(Utils.roll(50) ? ItemId.DRAGON_2H_SWORD : ItemId.DRAGON_PICKAXE);
            }
            if (random(252) == 0) {
                return new Item(ItemId.VOIDWAKER_BLADE);
            }
            if (random(358) == 0) {
                return new Item(ItemId.RING_OF_THE_GODS);
            }
        }
        if(item.getId() == ItemId.DRAGON_BONES)
            return item.toNote();
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {
            NpcId.VETION, 6612
        };
    }

}
