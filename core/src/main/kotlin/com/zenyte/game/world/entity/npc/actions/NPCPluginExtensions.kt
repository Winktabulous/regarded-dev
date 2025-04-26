package com.zenyte.game.world.entity.npc.actions

/**
 * @author Jire
 */

inline fun NPCPlugin.option(
    option: String,
    crossinline handle: NPCOptionHandler.() -> Unit
) = bind(option) { player, npc -> NPCOptionHandler(player, npc).handle() }