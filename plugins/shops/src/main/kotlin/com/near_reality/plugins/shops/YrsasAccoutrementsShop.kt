package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*

class YrsasAccoutrementsShop : ShopScript() {
    init {
        "Yrsa's Accoutrements"(54, ShopCurrency.COINS, STOCK_ONLY) {
            FREMENNIK_BLUE_SHIRT(5, 175, 325)
            FREMENNIK_RED_SHIRT(5, 175, 325)
            FREMENNIK_BROWN_SHIRT(5, 175, 325)
            FREMENNIK_GREY_SHIRT(5, 175, 325)
            FREMENNIK_BEIGE_SHIRT(5, 175, 325)
            FREMENNIK_ROBE(5, 390, 650)
            FREMENNIK_SKIRT(5, 390, 650)
            FREMENNIK_HAT(5, 390, 650)
            FREMENNIK_BOOTS(5, 390, 650)
            FREMENNIK_GLOVES(5, 390, 650)
            FREMENNIK_GREEN_CLOAK(5, 150, 325)
            FREMENNIK_BLUE_CLOAK(5, 150, 325)
            FREMENNIK_BROWN_CLOAK(5, 150, 325)
            FREMENNIK_CYAN_CLOAK(5, 150, 325)
            FREMENNIK_RED_CLOAK(5, 150, 325)
            FREMENNIK_GREY_CLOAK(5, 150, 325)
            FREMENNIK_YELLOW_CLOAK(5, 150, 325)
            FREMENNIK_TEAL_CLOAK(5, 150, 325)
            FREMENNIK_PURPLE_CLOAK(5, 150, 325)
            FREMENNIK_PINK_CLOAK(5, 150, 325)
            FREMENNIK_BLACK_CLOAK(5, 150, 325)
        }
    }
}