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

class MythsGuildWeaponryShop : ShopScript() {
    init {
        "Myths' Guild Weaponry"(32, ShopCurrency.COINS, STOCK_ONLY) {
            DRAGON_DAGGER(2, 18000, 30000)
            DRAGON_LONGSWORD(2, 60000, 100000)
            DRAGON_MACE(2, 30000, 50000)
            DRAGON_BATTLEAXE(2, 120000, 200000)
        }
    }
}