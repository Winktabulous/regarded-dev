package com.zenyte.game.model.item.pluginextensions;

/**
 * @author Kris | 11/06/2022
 */
public enum ItemDeathStatus {
    KEEP_ON_DEATH,
    GO_TO_GRAVESTONE,
    /* Special exception for items that are modified by in-game settings. */
    GO_TO_GRAVESTONE_OR_DROP_ON_GROUND,
    KEEP_DOWNGRADED,
    GRAVESTONE_DOWNGRADED,
    DOWNGRADED_WITHOUT_ORNAMENT_KIT,
    TURNED_TO_COINS,
    DROP_ON_DEATH,
    DELETE,
}