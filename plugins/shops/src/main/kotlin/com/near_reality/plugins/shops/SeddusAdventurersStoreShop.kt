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

class SeddusAdventurersStoreShop : ShopScript() {
    init {
        "Seddu's Adventurers' Store"(136, ShopCurrency.COINS, STOCK_ONLY) {
            RUNE_PLATESKIRT(1, -1, 60800)
            RUNE_PLATELEGS(1, 41600, 60800)
            RUNE_CHAINBODY(1, 33500, 47500)
            GREEN_DHIDE_CHAPS(1, 2613, 3705)
            GREEN_DHIDE_VAMB(1, -1, 2375)
            STEEL_KITESHIELD(1, -1, 807)
            BLACK_MED_HELM(1, -1, 547)
        }
    }
}