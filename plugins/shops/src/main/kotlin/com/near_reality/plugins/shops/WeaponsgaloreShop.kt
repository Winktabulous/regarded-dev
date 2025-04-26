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

class WeaponsgaloreShop : ShopScript() {
    init {
        "Weapons galore"(40, ShopCurrency.COINS, STOCK_ONLY) {
            MITHRIL_LONGSWORD(4, 910, 1430)
            MITHRIL_WARHAMMER(4, 1162, 1826)
            MITHRIL_BATTLEAXE(4, 1183, 1859)
            MITHRIL_CLAWS(4, 332, 522)
            MITHRIL_2H_SWORD(4, 1820, 2860)
        }
    }
}