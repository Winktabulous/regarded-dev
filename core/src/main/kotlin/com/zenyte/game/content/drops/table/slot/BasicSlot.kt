package com.zenyte.game.content.drops.table.slot

import com.zenyte.game.content.drops.table.RollResult
import com.zenyte.game.content.drops.table.TableSlot
import com.zenyte.game.util.Utils
import java.util.*

/**
 * @author Corey
 * @since 17/07/2020
 */
data class BasicSlot(val id: Int, val minAmount: Int, val maxAmount: Int) : TableSlot() {
    
    @JvmOverloads
    constructor(id: Int, amount: Int = 1) : this(id, amount, amount)
    
    override fun evaluate(random: Random): RollResult {
        if (minAmount == maxAmount) {
            return RollResult(id, minAmount)
        }
        
        return RollResult(id, Utils.random(random, minAmount, maxAmount))
    }
    
}
