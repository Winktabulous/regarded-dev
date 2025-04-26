package com.near_reality.scripts.npc.drops.table.chance.dynamic

import com.near_reality.scripts.npc.drops.table.DropQuantity
import com.near_reality.scripts.npc.drops.table.chance.RollItemChance
import com.near_reality.scripts.npc.drops.table.chance.RollOneIn
import com.zenyte.game.world.entity.player.Player

class DynamicRollItemOneIn(
    id: Int,
    quantity: DropQuantity,
    override val rarityProvider: (Player) -> Int
) : RollItemChance(id, quantity), RollOneIn, DynamicRollChance
