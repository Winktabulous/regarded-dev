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

class MulticannonPartsShop : ShopScript() {
    init {
        "Multicannon Parts"(6, ShopCurrency.COINS, STOCK_ONLY) {
            AMMO_MOULD(5, 0, 5)
            INSTRUCTION_MANUAL(5, 0, 10)
        //    CANNON_BASE(5, 0, 375000)
        //    CANNON_STAND(5, 0, 375000)
        //    CANNON_BARRELS(5, 0, 375000)
        //    CANNON_FURNACE(5, 0, 375000)
        }
    }
}