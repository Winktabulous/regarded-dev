package com.near_reality.scripts.npc.drops.table.chance.immutable

import com.near_reality.scripts.npc.drops.table.DropQuantity
import com.near_reality.scripts.npc.drops.table.chance.RollAlways

class StaticRollItemAlways(id: Int, quantity: DropQuantity)
    : StaticRollItemChance(id, quantity, 0), RollAlways
