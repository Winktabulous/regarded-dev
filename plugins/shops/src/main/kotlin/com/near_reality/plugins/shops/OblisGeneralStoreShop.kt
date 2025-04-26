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

class OblisGeneralStoreShop : ShopScript() {
    init {
        "Obli's General Store"(106, ShopCurrency.COINS, CAN_SELL) {
            TINDERBOX(2, 0, 1)
            VIAL(10, 1, 3)
            EMPTY_VIAL_PACK(100, 100, 300)
            PESTLE_AND_MORTAR(3, 2, 6)
            POT(3, 0, 1)
            BRONZE_AXE(3, 8, 24)
            BRONZE_PICKAXE(2, 0, 1)
            IRON_AXE(5, 28, 84)
            LEATHER_BODY(12, 10, 31)
            LEATHER_GLOVES(10, 3, 9)
            LEATHER_BOOTS(10, 2, 9)
            COOKED_MEAT(2, 2, 6)
            BREAD(10, 6, 18)
            BRONZE_BAR(10, 4, 12)
            SPADE(10, 1, 4)
            CANDLE(10, 1, 4)
            UNLIT_TORCH(10, 2, 6)
            CHISEL(10, 0, 1)
            HAMMER(10, 0, 1)
            PAPYRUS(10, 5, 15)
            CHARCOAL(10, 22, 67)
            VIAL_OF_WATER(10, 1, 3)
            WATERFILLED_VIAL_PACK(50, 100, 301)
            MACHETE(50, 20, 60)
            ROPE(10, 9, 27)
        }
    }
}