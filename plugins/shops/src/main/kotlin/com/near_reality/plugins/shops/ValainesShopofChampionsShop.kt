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

class ValainesShopofChampionsShop : ShopScript() {
    init {
        "Valaine's Shop of Champions"(56, ShopCurrency.COINS, STOCK_ONLY) {
            BLUE_CAPE(2, 12, 41)
            BLACK_FULL_HELM(1, 422, 1372)
            BLACK_PLATELEGS(1, 767, 2496)
            ADAMANT_PLATEBODY(1, 5120, 21632)
        }
    }
}