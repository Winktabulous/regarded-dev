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

class OziachsArmourShop : ShopScript() {
    init {
        "Oziach's Armour"(153, ShopCurrency.COINS, STOCK_ONLY) {
            RUNE_PLATEBODY(2, 26000, 84500)
            GREEN_DHIDE_BODY(2, 3120, 10140)
            ANTIDRAGON_SHIELD(35, 8, 26)
        }
    }
}