package com.zenyte.game.content.area.prifddinas.zalcano.formation;

import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoConstants;

/**
 * All the rock formations
 */
public enum ZalcanoRockFormations {

    DEPLETED(ZalcanoConstants.DEPLETED_ROCK_FORMATION),
    GLOWING(ZalcanoConstants.GLOWING_ROCK_FORMATION),
    REGULAR(ZalcanoConstants.ROCK_FORMATION);

    private final int objectId;

    ZalcanoRockFormations(int objectId) {
        this.objectId = objectId;
    }

    public int getObjectId() {
        return objectId;
    }
}
