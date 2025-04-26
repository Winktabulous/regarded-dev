package com.zenyte.game.content.boss.wildernessbosses.vetion;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;

import static com.zenyte.game.item.ItemId.DRAGON_BONES;

/**
 * @author Andys1814
 */
public final class CalvarionDropProcessor extends DropProcessor {

    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(27673, 1, 1, 402));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_2H_SWORD, 1, 1, 251));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_PICKAXE, 1, 1, 251));
        appendDrop(new DisplayedDrop(27684, 1, 1, 547));
        appendDrop(new DisplayedDrop(ItemId.RING_OF_THE_GODS, 1, 1, 500));
    }

    @Override
    public Item drop(NPC npc, Player killer, Drop drop, Item item) {
        if (item.getId() == DRAGON_BONES) {
            item.setId(item.getDefinitions().getNotedId());
        }
        if (!drop.isAlways()) {
            if (random(402) == 0) {
                return new Item(27673);
            }
            if (random(251) == 0) {
                return new Item(Utils.roll(50) ? ItemId.DRAGON_2H_SWORD : ItemId.DRAGON_PICKAXE);
            }
            if (random(547) == 0) {
                return new Item(27684);
            }
            if (random(500) == 0) {
                return new Item(ItemId.RING_OF_THE_GODS);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {
            NpcId.CALVARION,
        };
    }

}
