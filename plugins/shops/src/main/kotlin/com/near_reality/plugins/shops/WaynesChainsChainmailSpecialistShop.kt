package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class WaynesChainsChainmailSpecialistShop : ShopScript() {
    init {
        "Wayne's Chains - Chainmail Specialist"(14, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_CHAINBODY(3, 39, 60)
            IRON_CHAINBODY(2, 136, 210)
            STEEL_CHAINBODY(1, 487, 750)
            BLACK_CHAINBODY(1, 936, 1440)
            MITHRIL_CHAINBODY(1, 1267, 1950)
            ADAMANT_CHAINBODY(1, 3120, 4800)
        }
    }
}