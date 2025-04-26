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

class ToadandChickenShop : ShopScript() {
    init {
        "Toad and Chicken"(3, ShopCurrency.COINS, STOCK_ONLY) {
            ASGARNIAN_ALE(12, 0, 2)
            WIZARDS_MIND_BOMB(12, 0, 2)
            DWARVEN_STOUT(12, 0, 2)
        }
    }
}