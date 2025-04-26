package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BRONZE_KITESHIELD
import com.zenyte.game.item.ItemId.BRONZE_SQ_SHIELD
import com.zenyte.game.item.ItemId.IRON_KITESHIELD
import com.zenyte.game.item.ItemId.IRON_SQ_SHIELD
import com.zenyte.game.item.ItemId.MITHRIL_SQ_SHIELD
import com.zenyte.game.item.ItemId.STEEL_KITESHIELD
import com.zenyte.game.item.ItemId.STEEL_SQ_SHIELD
import com.zenyte.game.item.ItemId.WOODEN_SHIELD
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class CassiesShieldShopShop : ShopScript() {
    init {
        "Cassie's Shield Shop"(10, ShopCurrency.COINS, STOCK_ONLY) {
            WOODEN_SHIELD(5, 12, 20)
            BRONZE_SQ_SHIELD(3, 28, 48)
            BRONZE_KITESHIELD(3, 40, 68)
            IRON_SQ_SHIELD(2, 100, 168)
            IRON_KITESHIELD(0, 142, 223)
            STEEL_SQ_SHIELD(0, 360, 600)
            STEEL_KITESHIELD(0, 510, 850)
            MITHRIL_SQ_SHIELD(0, 936, 1560)
        }
    }
}