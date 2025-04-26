package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.ASGARNIAN_SEED
import com.zenyte.game.item.ItemId.BARLEY_SEED
import com.zenyte.game.item.ItemId.CABBAGE_SEED
import com.zenyte.game.item.ItemId.HAMMERSTONE_SEED
import com.zenyte.game.item.ItemId.JUTE_SEED
import com.zenyte.game.item.ItemId.KRANDORIAN_SEED
import com.zenyte.game.item.ItemId.MARIGOLD_SEED
import com.zenyte.game.item.ItemId.ONION_SEED
import com.zenyte.game.item.ItemId.POTATO_SEED
import com.zenyte.game.item.ItemId.ROSEMARY_SEED
import com.zenyte.game.item.ItemId.STRAWBERRY_SEED
import com.zenyte.game.item.ItemId.SWEETCORN_SEED
import com.zenyte.game.item.ItemId.TOMATO_SEED
import com.zenyte.game.item.ItemId.WATERMELON_SEED
import com.zenyte.game.item.ItemId.WILDBLOOD_SEED
import com.zenyte.game.item.ItemId.YANILLIAN_SEED
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DraynorSeedMarketShop : ShopScript() {
    init {
        "Draynor Seed Market"(150, ShopCurrency.COINS, STOCK_ONLY) {
            POTATO_SEED(100, 0, 2, 10)
            ONION_SEED(60, 1, 3, 10)
            CABBAGE_SEED(30, 1, 3, 10)
            TOMATO_SEED(0, 0, 4)
            SWEETCORN_SEED(0, 0, 9)
            STRAWBERRY_SEED(0, 0, 21)
            WATERMELON_SEED(0, 0, 67)
            BARLEY_SEED(100, 1, 2, 10)
            JUTE_SEED(30, 3, 6, 10)
            ROSEMARY_SEED(100, 2, 4, 10)
            MARIGOLD_SEED(100, 1, 2, 10)
            HAMMERSTONE_SEED(100, 1, 2, 10)
            ASGARNIAN_SEED(60, 1, 3, 10)
            YANILLIAN_SEED(30, 3, 7, 10)
            KRANDORIAN_SEED(10, 0, 9, 10)
            WILDBLOOD_SEED(10, 0, 16, 10)
        }
    }
}