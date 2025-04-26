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

class LliannsWaresShop : ShopScript() {
    init {
        "Lliann's Wares"(10000, ShopCurrency.COINS, STOCK_ONLY) {// Change UID for shop (Llian's NPC ID = 9170)
            CRYSTAL_CROWN(3, 0, 250000000, 250000000)
            CRYSTAL_OF_ITHELL(10, 0, 2500000,200)
            CRYSTAL_OF_IORWERTH(10, 0, 2500000,200)
            CRYSTAL_OF_TRAHAEARN(10, 0, 2500000,200)
            CRYSTAL_OF_CADARN(10, 0, 2500000,200)
            CRYSTAL_OF_CRWYS(10, 0, 2500000,200)
            CRYSTAL_OF_MEILYR(10, 0, 2500000,200)
            CRYSTAL_OF_HEFIN(10, 0, 2500000,200)
            CRYSTAL_OF_AMLODD(10, 0, 2500000,200)
            ELVEN_BOOTS(50, 0, 10000,150000)
            ELVEN_GLOVES(50, 0, 10000,150000)
            ELVEN_TOP(100, 0, 5000,30000)
            ELVEN_SKIRT(100, 0, 5000,30000)
            ELVEN_TOP_24015(100, 0, 5000,30000)
            ELVEN_SKIRT_24018(100, 0, 5000,30000)
            ELVEN_TOP_24021(100, 0, 5000,30000)
            ELVEN_LEGWEAR(100, 0, 5000,30000)
            ELVEN_TOP_24027(100, 0, 5000,30000)
        }
    }
}