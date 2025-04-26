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

class KeldagrimStonemasonShop : ShopScript() {
    init {
        "Keldagrim Stonemason"(195, ShopCurrency.COINS, STOCK_ONLY) {
            LIMESTONE_BRICK(1000, 7, 26)
            MARBLE_BLOCK(20, 108333, 325000)
            GOLD_LEAF_8784(20, 43333, 130000)
            MAGIC_STONE_8788(10, 325000, 975000)
        }
    }
}