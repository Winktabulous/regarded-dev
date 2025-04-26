package com.zenyte.game.content.area.prifddinas.zalcano.plugins;

import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoConstants;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;

public class ZalcanoNpcPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Mine", (player, npc) -> {
            PlayerCombat.attackEntity(player, npc, null);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { ZalcanoConstants.ZALCANO_MINABLE };
    }

}
