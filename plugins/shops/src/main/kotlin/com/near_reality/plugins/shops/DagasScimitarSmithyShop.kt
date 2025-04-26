package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BRONZE_SCIMITAR
import com.zenyte.game.item.ItemId.DRAGON_SCIMITAR
import com.zenyte.game.item.ItemId.IRON_SCIMITAR
import com.zenyte.game.item.ItemId.MITHRIL_SCIMITAR
import com.zenyte.game.item.ItemId.STEEL_SCIMITAR
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DagasScimitarSmithyShop : ShopScript() {
    init {
        "Daga's Scimitar Smithy"(223, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_SCIMITAR(10, 2, 32)
            IRON_SCIMITAR(10, 2, 112)
            STEEL_SCIMITAR(8, 2, 400)
            MITHRIL_SCIMITAR(6, 2, 1040)
            DRAGON_SCIMITAR(4, 1, 100000)
        }
    }
}