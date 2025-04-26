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

class WarrensGeneralStoreShop : ShopScript() {
    init {
        "Warrens General Store"(218, ShopCurrency.COINS, STOCK_ONLY) {
            POT(3, 0, 1)
            JUG(2, 0, 1)
            EMPTY_JUG_PACK(5, -1, 182)
            SHEARS(2, 0, 1)
            BUCKET(2, 0, 2)
            BOWL(2, 1, 5)
            TINDERBOX(2, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            SPADE(5, 1, 3)
            KNIFE(4, 2, 7)
        }
    }
}