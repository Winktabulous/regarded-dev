package com.near_reality.game.world

import com.zenyte.game.item.Item
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player

fun spawnFloorItem(
    item: Item,
    owner: Player? = null,
    receiver: Player? = null,
    tile: Location? = owner?.location?:(receiver?.location)?.let { Location(it) },
    maxStack: Int = -1,
    invisibleTicks: Int = -1,
    visibleTicks: Int = -1,
    visibleToIronmenOnly: Boolean = false,
    visibleToIronmen: Boolean = false,
) = World.spawnFloorItem(
    item,
    requireNotNull(tile) { "Must specify am explicit location or owner, receiver" },
    maxStack,
    owner,
    receiver,
    invisibleTicks,
    visibleTicks,
    visibleToIronmenOnly,
    visibleToIronmen
)
