package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

public class Asyff extends NPCPlugin {
    @Override
    public void handle() {
        bind("Fur clothing", (player, npc) -> {
            GameInterface.CUSTOM_FUR_CLOTHING.open(player);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.ASYFF }; // 2887
    }
}
