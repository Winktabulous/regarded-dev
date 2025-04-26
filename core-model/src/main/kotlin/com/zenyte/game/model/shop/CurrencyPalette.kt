package com.zenyte.game.model.shop

interface CurrencyPalette {
    val id: Int
    val isStackable: Boolean
    val isPhysical: Boolean
    val maximumAmount: Int
}