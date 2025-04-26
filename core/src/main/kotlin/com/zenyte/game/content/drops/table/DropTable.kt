package com.zenyte.game.content.drops.table

import com.zenyte.game.content.drops.table.impl.ImmutableDropTable
import com.zenyte.game.content.drops.table.slot.BasicSlot
import com.zenyte.game.item.Item
import com.zenyte.game.util.Utils
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.*

/**
 * @author Corey
 * @since 24/11/19
 */
open class DropTable @JvmOverloads constructor(private val random: Random = Utils.getRandom()) : TableSlot() {
    
    class InvalidRollException(msg: String) : RuntimeException(msg)
    
    private val table = ObjectArrayList<Pair<TableSlot, Int>>() // TableSlot, weight
    
    var slots = 0
        private set
    
    @JvmOverloads
    fun append(id: Int, weight: Int = 1, amount: Int = 1): DropTable {
        return append(id, weight, amount, amount)
    }
    
    fun append(id: Int, weight: Int = 1, minAmount: Int = 1, maxAmount: Int = 1): DropTable {
        assert(minAmount <= maxAmount)
        assert(id >= 1)
        assert(minAmount >= 1)
        
        appendSlot(BasicSlot(id, minAmount, maxAmount), weight)
        
        return this
    }
    
    @JvmOverloads
    fun append(table: TableSlot, weight: Int = 1): DropTable {
        appendSlot(table, weight)
        return this
    }
    
    protected open fun appendSlot(slot: TableSlot, weight: Int) {
        slots += weight
        table.add(Pair(slot, weight))
    }
    
    fun roll(): RollResult {
        val randomSlot = Utils.random(random, 1, slots)
        var currentSlot = 0
        
        table.forEach {
            currentSlot += it.second
            
            if (currentSlot >= randomSlot) {
                return it.first.evaluate(random)
            }
        }
    
        if (table.isEmpty) {
            throw InvalidRollException("Drop could not be generated: empty table")
        } else {
            throw InvalidRollException("Drop could not be generated: rolled slot $randomSlot/$slots was not found")
        }
    
    }
    
    fun roll(numberOfTimes: Int): Array<RollResult> {
        val results = arrayListOf<RollResult>()
        
        repeat(numberOfTimes) {
            results.add(roll())
        }
    
        return results.toTypedArray()
    }
    
    fun rollInt() = roll().id
    
    fun rollItem(): Item {
        val roll = roll()
        
        return Item(roll.id, roll.quantity)
    }
    
    fun toImmutable(): ImmutableDropTable {
        val immutableTable = ImmutableDropTable(random)
        
        table.forEach {
            immutableTable.append(it.first, it.second)
        }
        
        return immutableTable.build()
    }
    
    override fun evaluate(random: Random): RollResult {
        return roll()
    }
    
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is DropTable) return false
        return super.equals(other)
    }
    
    override fun hashCode(): Int {
        var result = random.hashCode()
        result = 31 * result + table.hashCode()
        result = 31 * result + slots
        return result
    }
    
}