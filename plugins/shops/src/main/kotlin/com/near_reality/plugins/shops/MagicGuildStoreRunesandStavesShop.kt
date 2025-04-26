package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class MagicGuildStoreRunesandStavesShop : ShopScript() {
    init {
        "Magic Guild Store (Runes and Staves)"(ShopCurrency.COINS, STOCK_ONLY) {
            AIR_RUNE(5000, 2, 4)
            WATER_RUNE(5000, 2, 4)
            EARTH_RUNE(5000, 2, 4)
            FIRE_RUNE(5000, 2, 4)
            MIND_RUNE(5000, 1, 3)
            BODY_RUNE(5000, 1, 3)
            CHAOS_RUNE(250, 90, 90)
            NATURE_RUNE(250, 90, 180)
            DEATH_RUNE(250, 99, 180)
            LAW_RUNE(250, 120, 240)
            BLOOD_RUNE(250, 200, 400)
            SOUL_RUNE(250, 150, 300)
            BATTLESTAFF(5, 3500, 7000)
            STAFF_OF_FIRE(2, 750, 1500)
            STAFF_OF_WATER(2, 750, 1500)
            STAFF_OF_AIR(2, 750, 1500)
            STAFF_OF_EARTH(2, 750, 1500)
        }
    }
}