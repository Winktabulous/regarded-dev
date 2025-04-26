package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class TzHaarHurRinsOreandGemStoreShop : ShopScript() {
    init {
        "TzHaar-Hur-Rin's Ore and Gem Store"(114, ShopCurrency.TOKKUL, STOCK_ONLY) {
            TIN_ORE(25, 0, 4)
            COPPER_ORE(25, 0, 4)
            IRON_ORE(15, 2, 25)
            SILVER_ORE(12, 11, 112)
            COAL(20, 6, 67)
            GOLD_ORE(12, 22, 225)
            MITHRIL_ORE(4, 24, 243)
            ADAMANTITE_ORE(2, 60, 600)
            RUNITE_ORE(1, 480, 4800)
            UNCUT_SAPPHIRE(10, 3, 37)
            UNCUT_EMERALD(7, 7, 75)
            UNCUT_RUBY(4, 15, 150)
            UNCUT_DIAMOND(2, 30, 300)
            UNCUT_DRAGONSTONE(0, 150, 1500)
            UNCUT_ONYX(1, 30000, 300000)
            ONYX_BOLT_TIPS(50, 150, 1500)
        }
    }
}