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

class SirTiffyCashienArmouryShopShop : ShopScript() {
    init {
        "Initiate & Proselyte Armour"(1006, ShopCurrency.COINS, STOCK_ONLY) {
            INITIATE_SALLET(2000, 2400, 6000)
            INITIATE_HAUBERK(2000, 4000, 10000)
            INITIATE_CUISSE(2000, 3200, 8000)
            PROSELYTE_SALLET(2000, 3200, 8000)
            PROSELYTE_HAUBERK(2000, 4800, 12000)
            PROSELYTE_CUISSE(2000, 4000, 10000)
            PROSELYTE_TASSET(2000, 4000, 10000)
        }
    }
}