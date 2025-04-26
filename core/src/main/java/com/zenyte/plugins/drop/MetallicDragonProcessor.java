
package com.zenyte.plugins.drop;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

public class MetallicDragonProcessor extends DropProcessor {
    @Override
    public void attach() {

    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if(item.getDefinitions().getLowercaseName().contains("bar") && killer.isMember()) {
            item.setId(item.getDefinitions().getNotedId());
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {
                NpcId.BRONZE_DRAGON, NpcId.BRONZE_DRAGON_271, NpcId.BRONZE_DRAGON_7253,
                NpcId.IRON_DRAGON, NpcId.IRON_DRAGON_273, NpcId.IRON_DRAGON_7254, NpcId.IRON_DRAGON_8080,
                NpcId.STEEL_DRAGON, NpcId.STEEL_DRAGON_274, NpcId.STEEL_DRAGON_275, NpcId.STEEL_DRAGON_7255, NpcId.STEEL_DRAGON_8086,
                NpcId.MITHRIL_DRAGON, NpcId.MITHRIL_DRAGON_8088, NpcId.MITHRIL_DRAGON_8089,
                NpcId.ADAMANT_DRAGON, NpcId.ADAMANT_DRAGON_8090,
                NpcId.RUNE_DRAGON, NpcId.RUNE_DRAGON_8031, NpcId.RUNE_DRAGON_8091
        };
    }
}
