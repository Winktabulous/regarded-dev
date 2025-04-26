package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;

public class StarflowerNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Pick", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                if (!player.getInventory().hasFreeSlots()) {
                    player.sendMessage("You need some inventory space to pick any more starflowers.");
                    return;
                }
                npc.sendDeath();
                player.getInventory().addOrDrop(new Item(9017));
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.STARFLOWER_1857 };
    }
}
