package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.ADAMANT_BATTLEAXE
import com.zenyte.game.item.ItemId.BLACK_BATTLEAXE
import com.zenyte.game.item.ItemId.BRONZE_BATTLEAXE
import com.zenyte.game.item.ItemId.IRON_BATTLEAXE
import com.zenyte.game.item.ItemId.MITHRIL_BATTLEAXE
import com.zenyte.game.item.ItemId.STEEL_BATTLEAXE
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class BriansBattleaxeBazaarShop : ShopScript() {
    init {
        "Brian's Battleaxe Bazaar"(16, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_BATTLEAXE(4, 28, 52)
            IRON_BATTLEAXE(3, 100, 182)
            STEEL_BATTLEAXE(2, 260, 650)
            BLACK_BATTLEAXE(1, 686, 1248)
            MITHRIL_BATTLEAXE(1, 929, 1690)
            ADAMANT_BATTLEAXE(1, 2160, 4160)
        }
    }
}