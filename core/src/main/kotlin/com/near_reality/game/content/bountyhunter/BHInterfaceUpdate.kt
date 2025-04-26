package com.near_reality.game.content.bountyhunter

/**
 * This represents a type of interface update that will be queued by certain methods of
 * the [BountyHunterController]
 * @author John J. Woloszyk / Kryeus
 */
enum class BHInterfaceUpdate {
    CLOSE_ALL, UPDATE_INFO_PANEL, UPDATE_TARGET_ALL, UPDATE_BEST_EMBLEM, UPDATE_WILDERNESS_LEVEL, UPDATE_NAME_STRING, SET_ORIGIN, UPDATE_MULTIZONE, UPDATE_ALL;
}