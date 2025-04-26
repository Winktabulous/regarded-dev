package com.zenyte.game.content.tombsofamascut;

import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;

public abstract class AbstractTOARaidArea extends DynamicArea {
    protected AbstractTOARaidArea(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY) {
        super(allocatedArea, copiedChunkX, copiedChunkY);
    }

    public boolean isOverlyDraining(){
        return false;
    }

    public boolean isQuietPrayers() {
        return false;
    }

    public boolean isDeadlyPrayers() {
        return false;
    }

    public boolean isDeHydration() {
        return false;
    }
}
