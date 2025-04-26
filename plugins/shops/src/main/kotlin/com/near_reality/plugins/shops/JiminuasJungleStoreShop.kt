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

class JiminuasJungleStoreShop : ShopScript() {
    init {
        "Jiminua's Jungle Store"(108, ShopCurrency.COINS, CAN_SELL) {
            TINDERBOX(2, 0, 1)
            CANDLE(10, 1, 4)
            UNLIT_TORCH(10, 2, 6)
            POT(3, 0, 1)
            ROPE(10, 9, 27)
            LEATHER_BODY(12, 10, 31)
            LEATHER_GLOVES(10, 3, 9)
            LEATHER_BOOTS(10, 3, 9)
            COOKED_MEAT(2, 2, 6)
            BREAD(10, 6, 18)
            VIAL(10, 1, 3)
            EMPTY_VIAL_PACK(100, 100, 300)
            VIAL_OF_WATER(50, 1, 3)
            WATERFILLED_VIAL_PACK(50, 100, 301)
            PESTLE_AND_MORTAR(3, 2, 6)
            ANTIPOISON3(10, 144, 432)
            PAPYRUS(50, 5, 15)
            CHARCOAL(50, 22, 67)
            KNIFE(2, 3, 9)
            HAMMER(10, 0, 1)
            MACHETE(50, 20, 60)
            CHISEL(10, 0, 1)
            SPADE(10, 1, 4)
            BRONZE_AXE(2, 8, 24)
            BRONZE_PICKAXE(2, 0, 1)
            IRON_AXE(5, 28, 84)
            BRONZE_BAR(10, 4, 12)
            PURE_ESSENCE(0, 2, 6)
        }
    }
}