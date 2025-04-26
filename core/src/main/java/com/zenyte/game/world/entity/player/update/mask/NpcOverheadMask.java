package com.zenyte.game.world.entity.player.update.mask;

import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.UpdateMask;
import com.zenyte.net.io.RSBuffer;

public class NpcOverheadMask extends UpdateMask {
    @Override
    public UpdateFlag getFlag() {
        return UpdateFlag.NPC_PRAYER_OVERHEAD;
    }

    @Override
    public void writeNPC(RSBuffer buffer, Player player, NPC npc) {
        if(npc.getOverhead() == null) {
            buffer.writeByte(0);
        } else {
            buffer.writeByte(1);
            buffer.writeBigSmart(npc.getOverhead().archive);
            buffer.writeSmart(npc.getOverhead().sprite + 1);
        }
    }
}
