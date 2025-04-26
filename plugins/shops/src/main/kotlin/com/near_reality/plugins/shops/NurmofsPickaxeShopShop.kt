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

class NurmofsPickaxeShopShop : ShopScript() {
    init {
        "Nurmof's Pickaxe Shop"(7, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(6, 0, 1)
            IRON_PICKAXE(5, 84, 140)
            STEEL_PICKAXE(4, 300, 500)
            MITHRIL_PICKAXE(3, 780, 1300)
            ADAMANT_PICKAXE(2, 1920, 3200)
            RUNE_PICKAXE(1, 19200, 32000)
        }
    }
}