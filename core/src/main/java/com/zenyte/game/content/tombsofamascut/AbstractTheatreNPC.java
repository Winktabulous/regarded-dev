package com.zenyte.game.content.tombsofamascut;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;

public abstract class AbstractTheatreNPC extends NPC {
    public AbstractTheatreNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }
}
