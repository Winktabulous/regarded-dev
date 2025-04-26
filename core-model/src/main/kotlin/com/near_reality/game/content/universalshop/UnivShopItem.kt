package com.near_reality.game.content.universalshop

data class UnivShopItem (
    val id: Int,
    val quantity: Int = 1,
    val buyPrice: Int = -1,
    val sellPrice: Int = -1,
    val ironmanRestricted: Boolean = false
){
    val canBuy
        get() = buyPrice != -1

    val canSell
        get() = sellPrice != -1
}