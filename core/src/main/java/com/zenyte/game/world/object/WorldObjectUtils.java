package com.zenyte.game.world.object;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.ObjectDefinitions;

public class WorldObjectUtils {
    public static int getObjectIdOfPlayer(WorldObject worldObject, final Player player) {
        int transformedId = worldObject.getId();
        final ObjectDefinitions defs = worldObject.getDefinitions();
        if (defs.getVarp() != -1 || defs.getVarbit() != -1) {
            final int[] transmogrificationIds = defs.getTransformedIds();
            final int varValue = defs.getVarp() != -1 ? player.getVarManager().getValue(defs.getVarp()) : player.getVarManager().getBitValue(defs.getVarbit());
            transformedId = transmogrificationIds[varValue];
        }
        return transformedId;
    }

    public static String getObjectNameOfPlayer(WorldObject worldObject, final Player player) {
        return ObjectDefinitions.get(getObjectIdOfPlayer(worldObject, player)).getName();
    }

    public static boolean isMapObject(WorldObject worldObject) {
        return !World.getRegion(worldObject.getRegionId()).containsSpawnedObject(worldObject);
    }
}
