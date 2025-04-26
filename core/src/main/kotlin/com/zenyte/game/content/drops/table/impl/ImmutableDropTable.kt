package com.zenyte.game.content.drops.table.impl

import com.zenyte.game.content.drops.table.DropTable
import com.zenyte.game.content.drops.table.TableSlot
import com.zenyte.game.util.Utils
import java.util.*

/**
 * @author Corey
 * @since 19/07/2020
 */
open class ImmutableDropTable @JvmOverloads constructor(random: Random = Utils.getRandom()) : DropTable(random) {
    
    /**
     * Whether or not the contents of the table have been finalised.
     * If `true`, appends will not modify the internal contents of the table.
     */
    private var built = false
    
    override fun appendSlot(slot: TableSlot, weight: Int) {
        if (built) {
            return
        }
        super.appendSlot(slot, weight)
    }
    
    fun build(): ImmutableDropTable {
        built = true
        return this
    }
    
}
