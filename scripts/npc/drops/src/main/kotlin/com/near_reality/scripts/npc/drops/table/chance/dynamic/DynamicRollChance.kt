package com.near_reality.scripts.npc.drops.table.chance.dynamic

import com.near_reality.scripts.npc.drops.table.chance.RollChance
import com.zenyte.game.world.entity.player.Player

interface DynamicRollChance : RollChance {

    val rarityProvider: (Player) -> Int
}
