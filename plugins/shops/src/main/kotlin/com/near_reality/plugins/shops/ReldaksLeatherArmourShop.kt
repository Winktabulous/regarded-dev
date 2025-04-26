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

class ReldaksLeatherArmourShop : ShopScript() {
    init {
        "Reldak's Leather Armour"(147, ShopCurrency.COINS, STOCK_ONLY) {
            FROGLEATHER_BODY(50, 400, 1000)
            FROGLEATHER_CHAPS(50, 360, 900)
            FROGLEATHER_BOOTS(50, 80, 200)
        }
    }
}