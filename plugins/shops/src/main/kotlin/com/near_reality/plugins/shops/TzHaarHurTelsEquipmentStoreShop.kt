package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class TzHaarHurTelsEquipmentStoreShop : ShopScript() {
    init {
        "TzHaar-Hur-Tel's Equipment Store"(113, ShopCurrency.TOKKUL, STOCK_ONLY) {
            TOKTZXILUL(500, 34, 375)
            TOKTZXILAK(1, 6000, 60000)
            TOKTZXILEK(1, 3550, 37500)
            TZHAARKETOM(1, 7500, 75000)
            TOKTZMEJTAL(1, 5000, 52500)
            TZHAARKETEM(1, 4000, 45000)
            TOKTZKETXIL(1, 6750, 67500)
            OBSIDIAN_CAPE(1, 9000, 90000)
        }
    }
}