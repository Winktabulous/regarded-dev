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

class RichardsFarmingshopShop : ShopScript() {
    init {
        "Richard's Farming shop"(84, ShopCurrency.COINS, STOCK_ONLY) {
            RAKE(500, 4, 6)
            SEED_DIBBER(500, 4, 6)
            SECATEURS(500, 3, 5)
            SPADE(500, 2, 3)
            GARDENING_TROWEL(500, 8, 12)
            WATERING_CAN(500, 5, 8)
            FILLED_PLANT_POT(500, 0, 1)
            PLANT_POT_PACK(30, 70, 100)
            COMPOST(500, 14, 20)
            COMPOST_PACK(10, 1400, 2000)
            EMPTY_SACK(500, 0, 1)
            SACK_PACK(30, 70, 100)
            BASKET(500, 0, 1)
            BASKET_PACK(30, 70, 100)
            POTATO(0, 0, 1)
            ONION(0, -1, 3)
            CABBAGE(0, -1, 1)
            TOMATO(0, -1, 4)
            SWEETCORN(0, -1, 9)
            STRAWBERRY(0, -1, 17)
            WATERMELON(0, -1, 48)
            HAMMERSTONE_HOPS(0, -1, 4)
            ASGARNIAN_HOPS(0, -1, 5)
            YANILLIAN_HOPS(0, -1, 7)
            KRANDORIAN_HOPS(0, -1, 10)
            WILDBLOOD_HOPS(0, -1, 15)
            JUTE_FIBRE(0, -1, 6)
            BARLEY(0, -1, 4)
            PLANT_CURE(100, 28, 40)
        }
    }
}