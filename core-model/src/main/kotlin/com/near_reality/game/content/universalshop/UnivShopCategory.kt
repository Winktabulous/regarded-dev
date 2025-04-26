package com.near_reality.game.content.universalshop

import com.zenyte.game.model.shop.ShopCurrency

enum class UnivShopCategory(
    val index: Int,
    val dbTable: Int,
    val prettyName: String,
    val itemId: Int,
    val currency: ShopCurrency = ShopCurrency.COINS,
    val canBuy: Boolean = true,
    val canSell: Boolean = false,
) {
    Overview(0, 0, "Overview", 25093),
    Melee(1, 1001, "Melee", 4587),
    Ranged(2, 1008, "Ranged", 861),
    Magic(3, 1007, "Magic", 21006),
    Supplies(4, 1011, "Supplies", 2570),
    Skilling(5, 1010, "Skilling", 11920),
    Jewelry(6, 1012, "Jewelry", 1478),
    General(7, 1003, "General", 1923),
    Slayer(8, 1004, "Slayer", 11864, currency = ShopCurrency.SLAYER_POINTS),
    BountyHunter(9, 1005, "Bounty Hunter", 27997, currency = ShopCurrency.BH_POINTS),
    Capes(10, 1006, "Capes", 9769),
    BloodMoney(11, 1009, "Blood Money", 13307, currency = ShopCurrency.BLOOD_MONEY),
    Loyalty(12, 1013, "Loyalty", 21034,  currency = ShopCurrency.LOYALTY_POINTS),
    Vote(13, 1014, "Vote", 4067, currency = ShopCurrency.VOTE_POINTS)
    ;

}