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

class TutabsMagicalMarketShop : ShopScript() {
    init {
        "Tutab's Magical Market"(227, ShopCurrency.COINS, STOCK_ONLY) {
            FIRE_RUNE(1000, 5, 17)
            WATER_RUNE(1000, 5, 17)
            AIR_RUNE(1000, 5, 17)
            EARTH_RUNE(1000, 5, 17)
            LAW_RUNE(250, 126, 240)
            EYE_OF_GNOME(10, 1, 3)
            MONKEY_DENTURES(10, 3, 10)
            MONKEY_TALISMAN(10, 333, 1000)
        }
    }
}