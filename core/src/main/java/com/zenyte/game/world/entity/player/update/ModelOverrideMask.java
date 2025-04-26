package com.zenyte.game.world.entity.player.update;

import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.io.RSBuffer;

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
public class ModelOverrideMask extends UpdateMask {

    @Override
    public void writeNPC(RSBuffer buffer, Player player, NPC npc) {
        if(npc.getOverrides() == null || npc.getOverrides().empty()) {
            buffer.write128Byte(1);
        } else {
            buffer.write128Byte(2);
            int[] models = npc.getDefinitions().getModels();
            int[] overwrittenModels = npc.getOverrides().override(models);
            buffer.writeByte(overwrittenModels.length);
            for(int model: overwrittenModels) {
                buffer.writeShort128(model);
            }
        }

    }

    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.MODEL_OVERRIDE;
    }
}
