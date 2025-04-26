package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BOWL
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.BUCKET_OF_MILK
import com.zenyte.game.item.ItemId.CAKE_TIN
import com.zenyte.game.item.ItemId.CHEESE
import com.zenyte.game.item.ItemId.CHOCOLATE_BAR
import com.zenyte.game.item.ItemId.COOKING_APPLE
import com.zenyte.game.item.ItemId.EGG
import com.zenyte.game.item.ItemId.EMPTY_BUCKET_PACK
import com.zenyte.game.item.ItemId.EMPTY_CUP
import com.zenyte.game.item.ItemId.EMPTY_JUG_PACK
import com.zenyte.game.item.ItemId.GRAPES
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.PAT_OF_BUTTER
import com.zenyte.game.item.ItemId.PIE_DISH
import com.zenyte.game.item.ItemId.PIZZA_BASE
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.POT_OF_CREAM
import com.zenyte.game.item.ItemId.POT_OF_FLOUR
import com.zenyte.game.item.ItemId.SPICE
import com.zenyte.game.item.ItemId.TOMATO
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class CulinaromancersFoodStoreShop : ShopScript() {
    init {
        "Culinaromancer's Chest - Food"(ShopCurrency.COINS, STOCK_ONLY) {
            CHOCOLATE_BAR(50, 5, 13)
            CHEESE(50, 1, 5)
            TOMATO(50, 1, 5)
            COOKING_APPLE(50, 0, 1)
            GRAPES(250, 0, 1)
            POT_OF_FLOUR(10, 4, 13)
            PIZZA_BASE(1, 1, 5)
            EGG(50, 1, 5)
            BUCKET_OF_MILK(50, 2, 7)
            POT_OF_CREAM(50, 2, 3)
            PAT_OF_BUTTER(10, 1, 5)
            SPICE(50, 96, 299)
            PIE_DISH(50, 1, 3)
            CAKE_TIN(50, 4, 13)
            BOWL(50, 1, 5)
            JUG(50, 0, 1)
            EMPTY_JUG_PACK(8, 56, 182)
            POT(50, 0, 1)
            EMPTY_CUP(50, 0, 2)
            BUCKET(50, 0, 2)
            EMPTY_BUCKET_PACK(50, 200, 650)
        }
    }
}