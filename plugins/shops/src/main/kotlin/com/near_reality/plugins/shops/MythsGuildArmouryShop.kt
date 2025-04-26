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

class MythsGuildArmouryShop : ShopScript() {
    init {
        "Myths' Guild Armoury"(31, ShopCurrency.COINS, STOCK_ONLY) {
            SHIELD_RIGHT_HALF(1, 250000, 750000)
            DRAGON_METAL_SHARD(1, 600000, 1800000)
        }
    }
}