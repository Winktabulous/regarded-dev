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

class TamayusSpearStallShop : ShopScript() {
    init {
        "Tamayu's Spear Stall"(111, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_SPEARKP(10, 0, 26)
            IRON_SPEARKP(10, 0, 91)
            STEEL_SPEARKP(5, 0, 325)
            MITHRIL_SPEARKP(2, 0, 845)
            ADAMANT_SPEARKP(0, 0, 2080)
            RUNE_SPEARKP(0, 0, 20800)
            CLEANING_CLOTH(10, 45, 60)
        }
    }
}