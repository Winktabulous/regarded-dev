package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;

public abstract class AbstractTOANPC extends NPC {
    public AbstractTOANPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }
}
