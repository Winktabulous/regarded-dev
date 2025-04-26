package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.ASGARNIAN_HOPS
import com.zenyte.game.item.ItemId.BARLEY
import com.zenyte.game.item.ItemId.BASKET
import com.zenyte.game.item.ItemId.BASKET_PACK
import com.zenyte.game.item.ItemId.CABBAGE
import com.zenyte.game.item.ItemId.COMPOST
import com.zenyte.game.item.ItemId.COMPOST_PACK
import com.zenyte.game.item.ItemId.EMPTY_SACK
import com.zenyte.game.item.ItemId.FILLED_PLANT_POT
import com.zenyte.game.item.ItemId.GARDENING_TROWEL
import com.zenyte.game.item.ItemId.HAMMERSTONE_HOPS
import com.zenyte.game.item.ItemId.JUTE_FIBRE
import com.zenyte.game.item.ItemId.KRANDORIAN_HOPS
import com.zenyte.game.item.ItemId.ONION
import com.zenyte.game.item.ItemId.PLANT_CURE
import com.zenyte.game.item.ItemId.PLANT_POT_PACK
import com.zenyte.game.item.ItemId.POTATO
import com.zenyte.game.item.ItemId.RAKE
import com.zenyte.game.item.ItemId.SACK_PACK
import com.zenyte.game.item.ItemId.SECATEURS
import com.zenyte.game.item.ItemId.SEED_DIBBER
import com.zenyte.game.item.ItemId.SPADE
import com.zenyte.game.item.ItemId.STRAWBERRY
import com.zenyte.game.item.ItemId.SWEETCORN
import com.zenyte.game.item.ItemId.TOMATO
import com.zenyte.game.item.ItemId.WATERING_CAN
import com.zenyte.game.item.ItemId.WATERMELON
import com.zenyte.game.item.ItemId.WILDBLOOD_HOPS
import com.zenyte.game.item.ItemId.YANILLIAN_HOPS
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class AlicesFarmingshopShop : ShopScript() {
    init {
        "Alice's Farming shop"(178, ShopCurrency.COINS, STOCK_ONLY) {
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