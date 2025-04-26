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

class ToothysPickaxesShop : ShopScript() {
    init {
        "Toothy's Pickaxes"(211, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(5, -1, 1)
            IRON_PICKAXE(3, -1, 182)
            STEEL_PICKAXE(3, -1, 650)
            MITHRIL_PICKAXE(2, -1, 1690)
            ADAMANT_PICKAXE(1, -1, 4160)
            RUNE_PICKAXE(1, 17600, 41600)
            POT(50000, 0, 1)
        }
    }
}