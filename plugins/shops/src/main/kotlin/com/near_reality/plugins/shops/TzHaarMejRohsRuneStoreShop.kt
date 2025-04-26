package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class TzHaarMejRohsRuneStoreShop : ShopScript() {
    init {
        "TzHaar-Mej-Roh's Rune Store"(113, ShopCurrency.TOKKUL, STOCK_ONLY) {
            FIRE_RUNE(5000, 0, 6)
            WATER_RUNE(5000, 0, 6)
            AIR_RUNE(5000, 0, 6)
            EARTH_RUNE(5000, 0, 6)
            MIND_RUNE(5000, 0, 4)
            BODY_RUNE(5000, 0, 4)
            CHAOS_RUNE(5000, 9, 45)
            DEATH_RUNE(5000, 18, 270)
            TZHAAR_AIR_RUNE_PACK(50, 64, 645)
            TZHAAR_WATER_RUNE_PACK(50, 64, 645)
            TZHAAR_EARTH_RUNE_PACK(50, 64, 645)
            TZHAAR_FIRE_RUNE_PACK(50, 64, 645)
        }
    }
}