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

class MageArenaStaffsShop : ShopScript() {
    init {
        "Mage Arena Staffs"(245, ShopCurrency.COINS, STOCK_ONLY) {
            SARADOMIN_STAFF(5, 0, 80000)
            GUTHIX_STAFF(5, 0, 80000)
            ZAMORAK_STAFF(5, 0, 80000)
        }
    }
}