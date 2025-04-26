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

class TheLighthouseStoreShop : ShopScript() {
    init {
        "The Lighthouse Store"(41, ShopCurrency.COINS, CAN_SELL) {
            ROPE(10, 6, 18)
            HAMMER(10, 4, 13)
            CHISEL(10, 4, 14)
            KNIFE(10, 5, 16)
            SPADE(10, 1, 3)
            TINDERBOX(5, 1, 1)
            CANDLE(10, 1, 3)
            POISON(10, 1, 3)
            PESTLE_AND_MORTAR(10, 1, 4)
            POT(30, 1, 1)
            BUCKET(5, 1, 1)
            BUCKET_OF_WATER(5, 2, 6)
            JUG(5, 1, 1)
            EMPTY_JUG_PACK(8, -1, 154)
            JUG_OF_WATER(5, 1, 1)
            VIAL(100, 1, 2)
            EMPTY_VIAL_PACK(100, -1, 220)
            VIAL_OF_WATER(100, -1, 2)
            WATERFILLED_VIAL_PACK(100, -1, 221)
            GIN(5, 1, 5)
            BRANDY(5, 1, 5)
            VODKA(5, 1, 5)
            GROG(5, 1, 3)
            WHISKY(5, 1, 5)
            GREENMANS_ALE(5, 1, 2)
            DWARVEN_STOUT(5, 1, 2)
            WIZARDS_MIND_BOMB(5, 1, 2)
        }
    }
}