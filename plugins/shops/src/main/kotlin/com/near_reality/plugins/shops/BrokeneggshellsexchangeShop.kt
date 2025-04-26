package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.EASTER_MYSTERY_BOX
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.NO_SELLING

class BrokeneggshellsexchangeShop : ShopScript() {
    init {
        "Broken egg shells exchange"(369, ShopCurrency.BROKEN_EGG_SHELLS, NO_SELLING) {
            EASTER_MYSTERY_BOX(1_000_000, sellPrice = (-1), buyPrice = 200)
        }
    }
}