package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BASKET
import com.zenyte.game.item.ItemId.BASKET_PACK
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.COMPOST
import com.zenyte.game.item.ItemId.COMPOST_PACK
import com.zenyte.game.item.ItemId.EMPTY_BUCKET_PACK
import com.zenyte.game.item.ItemId.EMPTY_PLANT_POT
import com.zenyte.game.item.ItemId.EMPTY_SACK
import com.zenyte.game.item.ItemId.FILLED_PLANT_POT
import com.zenyte.game.item.ItemId.GARDENING_TROWEL
import com.zenyte.game.item.ItemId.PLANT_CURE
import com.zenyte.game.item.ItemId.PLANT_POT_PACK
import com.zenyte.game.item.ItemId.RAKE
import com.zenyte.game.item.ItemId.SACK_PACK
import com.zenyte.game.item.ItemId.SECATEURS
import com.zenyte.game.item.ItemId.SEED_DIBBER
import com.zenyte.game.item.ItemId.SPADE
import com.zenyte.game.item.ItemId.WATERING_CAN
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class AllannasFarmingShopShop : ShopScript() {
    init {
        "Allanna's Farming Shop"(115, ShopCurrency.COINS, STOCK_ONLY) {
            RAKE(5, 3, 6)
            SEED_DIBBER(5, 3, 6)
            SECATEURS(5, 3, 5)
            SPADE(5, 1, 3)
            GARDENING_TROWEL(3, 7, 12)
            WATERING_CAN(30, 4, 8)
            EMPTY_PLANT_POT(100, 0, 1)
            FILLED_PLANT_POT(100, 0, 1)
            PLANT_POT_PACK(10, 60, 100)
            COMPOST(100, 12, 20)
            COMPOST_PACK(10, 1200, 2000)
            BUCKET(100, 1, 2)
            EMPTY_BUCKET_PACK(15, 300, 500)
            EMPTY_SACK(100, 0, 1)
            SACK_PACK(30, 60, 100)
            BASKET(100, 0, 1)
            BASKET_PACK(30, 60, 100)
            PLANT_CURE(40, 24, 40)
        }
    }
}