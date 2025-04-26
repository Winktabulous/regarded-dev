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

class HappyHeroesHEmporiumShop : ShopScript() {
    init {
        "Happy Heroes' H'Emporium"(59, ShopCurrency.COINS, STOCK_ONLY) {
            DRAGON_BATTLEAXE(1, 110000, 200000)
            DRAGON_MACE(1, 27500, 50000)
        }
    }
}