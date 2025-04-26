package com.near_reality.scripts.npc.drops.table.chance.immutable

import com.near_reality.scripts.npc.drops.table.DropQuantity
import com.near_reality.scripts.npc.drops.table.chance.RollOneIn

class StaticRollItemOneIn(
    id: Int,
    quantity: DropQuantity,
    rarity: Int = 1
) : StaticRollItemChance(id, quantity, rarity), RollOneIn
