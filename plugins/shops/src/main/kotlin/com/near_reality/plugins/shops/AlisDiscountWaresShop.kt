package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.MENAPHITE_PURPLE_HAT
import com.zenyte.game.item.ItemId.MENAPHITE_PURPLE_KILT
import com.zenyte.game.item.ItemId.MENAPHITE_PURPLE_ROBE
import com.zenyte.game.item.ItemId.MENAPHITE_PURPLE_TOP
import com.zenyte.game.item.ItemId.MENAPHITE_RED_HAT
import com.zenyte.game.item.ItemId.MENAPHITE_RED_KILT
import com.zenyte.game.item.ItemId.MENAPHITE_RED_ROBE
import com.zenyte.game.item.ItemId.MENAPHITE_RED_TOP
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.CAN_SELL

class AlisDiscountWaresShop : ShopScript() {
    init {
        "Ali's Discount Wares"(122, ShopCurrency.COINS, CAN_SELL) {
            MENAPHITE_PURPLE_HAT(25, 21, 35)
            MENAPHITE_PURPLE_TOP(25, 12, 20)
            MENAPHITE_PURPLE_ROBE(25, 24, 40)
            MENAPHITE_PURPLE_KILT(25, 12, 20)
            MENAPHITE_RED_HAT(25, 21, 35)
            MENAPHITE_RED_TOP(25, 12, 20)
            MENAPHITE_RED_ROBE(25, 24, 40)
            MENAPHITE_RED_KILT(25, 12, 20)
        }
    }
}