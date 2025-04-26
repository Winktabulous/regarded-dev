package com.near_reality.api.dao

import com.near_reality.api.model.Item
import com.near_reality.api.model.SlotItemMap
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.batchInsert
import kotlin.reflect.KClass

annotation class DetailedItemsTable(val detailsTable: KClass<out LogItems>)

abstract class LogItems(name: String, parent: IdTable<Long>) : IntIdTable(name) {
    val parent = reference("parent", parent).index()
    val index = integer("index").index()
    val owner = username("owner").index()
    val itemId = integer("item_id").index()
    val amount = integer("item_amount")
    val slot = integer("slot")

    fun insert(parent: Long, index: Int, owner: String, slotItemMap: SlotItemMap) {
        batchInsert(slotItemMap.keys.toList()) { slot ->
            val item = slotItemMap[slot]!!
            this[this@LogItems.parent] = parent
            this[this@LogItems.index] = index
            this[this@LogItems.owner] = owner
            this[this@LogItems.itemId] = item.id
            this[this@LogItems.amount] = item.amount
            this[this@LogItems.slot] = slot
        }
    }
    fun insert(parent: Long, index: Int, owner: String, items: List<Item>) {
        batchInsert(items.withIndex()) { (slot, item) ->
            this[this@LogItems.parent] = parent
            this[this@LogItems.index] = index
            this[this@LogItems.owner] = owner
            this[this@LogItems.itemId] = item.id
            this[this@LogItems.amount] = item.amount
            this[this@LogItems.slot] = slot
        }
    }
}

object TradeLogItems : LogItems("trade_log_items", TradeLogs)
object KilledByPlayerLogItems : LogItems("killed_by_player_log_items", KilledByPlayerLogs)
object KilledByNpcLogItems : LogItems("killed_by_npc_log_items", KilledByNpcLogs)
object MiscDeathLogItems : LogItems("death_misc_log_items", KilledByNpcLogs)
object FlowerPokerSessionLogItems : LogItems("flower_poker_session_log_items", FlowerPokerSessionLogs)
object DuelLogItems : LogItems("duel_log_items", DuelLogs)
