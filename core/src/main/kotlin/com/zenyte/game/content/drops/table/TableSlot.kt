package com.zenyte.game.content.drops.table

import java.util.*

/**
 * @author Corey
 * @since 17/07/2020
 */
abstract class TableSlot {
    
    abstract fun evaluate(random: Random): RollResult
    
}
