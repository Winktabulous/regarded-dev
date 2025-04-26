package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.ADAMANT_ARROW
import com.zenyte.game.item.ItemId.MAPLE_LONGBOW
import com.zenyte.game.item.ItemId.MAPLE_SHORTBOW
import com.zenyte.game.item.ItemId.MITHRIL_ARROW
import com.zenyte.game.item.ItemId.OAK_LONGBOW
import com.zenyte.game.item.ItemId.OAK_SHORTBOW
import com.zenyte.game.item.ItemId.STEEL_ARROW
import com.zenyte.game.item.ItemId.WILLOW_LONGBOW
import com.zenyte.game.item.ItemId.WILLOW_SHORTBOW
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class BriansArcherySuppliesShop : ShopScript() {
    init {
        "Brian's Archery Supplies"(21, ShopCurrency.COINS, STOCK_ONLY) {
            STEEL_ARROW(1500, 7, 12)
            MITHRIL_ARROW(1000, 20, 32)
            ADAMANT_ARROW(800, 52, 80)
            OAK_SHORTBOW(4, 65, 100)
            OAK_LONGBOW(4, 104, 160)
            WILLOW_SHORTBOW(3, 130, 200)
            WILLOW_LONGBOW(3, 208, 320)
            MAPLE_SHORTBOW(2, 260, 400)
            MAPLE_LONGBOW(2, 416, 640)
        }
    }
}