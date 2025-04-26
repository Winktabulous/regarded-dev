package com.zenyte.plugins.drop.dragons;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

public class BasiliskKnightProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(ItemId.BASILISK_JAW, 1, 1, 500));
    }

    @Override
    public void onDeath(NPC npc, Player killer) {
        if(randomDrop(killer, 500) == 0) {
            npc.dropItem(killer, new Item(ItemId.BASILISK_JAW));
        }
    }

    @Override
    public int[] ids() {
        return new int[]{NpcId.BASILISK_KNIGHT};
    }
}
