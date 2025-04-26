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

class SkulgrimensBattleGearShop : ShopScript() {
    init {
        "Skulgrimen's Battle Gear"(53, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_WARHAMMER(5, -1, 61)
            IRON_WARHAMMER(4, -1, 224)
            STEEL_WARHAMMER(3, -1, 832)
            BLACK_WARHAMMER(3, -1, 1274)
            MITHRIL_WARHAMMER(2, -1, 2158)
            ADAMANT_WARHAMMER(1, -1, 5356)
            RUNE_WARHAMMER(0, -1, 53950)
            ARCHER_HELM(5, 42000, 78000)
            BERSERKER_HELM(5, 42000, 78000)
            WARRIOR_HELM(5, 42000, 78000)
            FARSEER_HELM(5, 42000, 78000)
        }
    }
}