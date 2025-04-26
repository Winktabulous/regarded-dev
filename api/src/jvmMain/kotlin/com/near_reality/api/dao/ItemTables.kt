package com.near_reality.api.dao

import com.near_reality.api.model.ItemConfig
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object ItemConfigs : LongIdTable("item_configs") {
    val name = varchar("item_name", 128).index()
    val canTrade = bool("tradeable")
    val ecoValue = integer("custom_price").nullable()
    val generalStore = integer("general_store").nullable()
    val grandExchangeAverage = integer("ge_average").nullable()
    val grandExchangeBuyMin = integer("ge_min_buy").nullable()
    val grandExchangeSellMin = integer("ge_min_sell").nullable()
    val grandExchangeBuyMax = integer("ge_max_buy").nullable()
    val grandExchangeSellMax = integer("ge_max_sell").nullable()
    val amountInEco = integer("amount_in_eco").nullable()
    val protectionValue = integer("protection_value").nullable()
    val averageTradeValue = integer("average_trade_value").nullable()
}

class ItemConfigEntity(id: EntityID<Long>) : ModelEntity<ItemConfig>(id) {
    companion object : LongEntityClass<ItemConfigEntity>(ItemConfigs) {
        fun new(itemDetails: ItemConfig) = ItemConfigEntity.new(itemDetails.id.toLong()) {
            setFrom(itemDetails)
        }
        fun edit(itemDetails: ItemConfig) = ItemConfigEntity.findById(itemDetails.id.toLong())?.apply {
            setFrom(itemDetails)
        }
    }
    var name by ItemConfigs.name
    var canTrade by ItemConfigs.canTrade
    var ecoValue by ItemConfigs.ecoValue
    var generalStore by ItemConfigs.generalStore
    var grandExchangeAverage by ItemConfigs.grandExchangeAverage
    var grandExchangeBuyMin by ItemConfigs.grandExchangeBuyMin
    var grandExchangeSellMin by ItemConfigs.grandExchangeSellMin
    var grandExchangeBuyMax by ItemConfigs.grandExchangeBuyMax
    var grandExchangeSellMax by ItemConfigs.grandExchangeSellMax
    var amountInEco by ItemConfigs.amountInEco
    var protectionValue by ItemConfigs.protectionValue
    var averageTradeValue by ItemConfigs.averageTradeValue

    fun setFrom(itemDetails: ItemConfig) {
        name = itemDetails.name
        canTrade = itemDetails.tradeable
        ecoValue = itemDetails.ecoValue
        generalStore = itemDetails.generalStore
        grandExchangeAverage = itemDetails.grandExchangeAverage
        grandExchangeBuyMin = itemDetails.grandExchangeBuyMin
        grandExchangeSellMin = itemDetails.grandExchangeSellMin
        grandExchangeBuyMax = itemDetails.grandExchangeBuyMax
        grandExchangeSellMax = itemDetails.grandExchangeSellMax
        amountInEco = itemDetails.amountInEco
        protectionValue = itemDetails.protectionValue
        averageTradeValue = itemDetails.averageTradeValue
    }

    override fun toModel(): ItemConfig {
        return ItemConfig(
            id = id.value.toInt(),
            name = name,
            tradeable = canTrade,
            ecoValue = ecoValue,
            generalStore = generalStore,
            grandExchangeAverage = grandExchangeAverage,
            grandExchangeBuyMin = grandExchangeBuyMin,
            grandExchangeSellMin = grandExchangeSellMin,
            grandExchangeBuyMax = grandExchangeBuyMax,
            grandExchangeSellMax = grandExchangeSellMax,
            amountInEco = amountInEco,
            protectionValue = protectionValue,
            averageTradeValue = averageTradeValue
        )
    }
}
