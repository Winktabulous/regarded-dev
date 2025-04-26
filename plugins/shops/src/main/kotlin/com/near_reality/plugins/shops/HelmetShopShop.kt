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

class HelmetShopShop : ShopScript() {
    init {
        "Helmet Shop"(154, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_MED_HELM(5, 14, 24)
            IRON_MED_HELM(3, 50, 84)
            STEEL_MED_HELM(3, 180, 300)
            MITHRIL_MED_HELM(1, 468, 780)
            ADAMANT_MED_HELM(1, 1152, 1920)
            BRONZE_FULL_HELM(4, 26, 44)
            IRON_FULL_HELM(3, 92, 154)
            STEEL_FULL_HELM(2, 330, 550)
            MITHRIL_FULL_HELM(1, 858, 1430)
            ADAMANT_FULL_HELM(1, 2112, 3520)
        }
    }
}