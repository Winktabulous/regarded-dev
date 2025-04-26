package com.near_reality.scripts.npc.drops.table.chance.dynamic

import com.near_reality.scripts.npc.drops.table.DropQuantity
import com.near_reality.scripts.npc.drops.table.chance.RollItemChance
import com.zenyte.game.world.entity.player.Player

class DynamicRollItemChance(
    id: Int,
    quantity: DropQuantity,
    override val rarityProvider: (Player) -> Int
) : RollItemChance(id, quantity), DynamicRollChance
