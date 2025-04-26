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

class WarriorGuildPotionShopShop : ShopScript() {
    init {
        "Warrior Guild Potion Shop"(69, ShopCurrency.COINS, STOCK_ONLY) {
            STRENGTH_POTION3(10, 1, 15)
            ATTACK_POTION3(10, 1, 14)
            DEFENCE_POTION3(10, 18, 144)
        }
    }
}