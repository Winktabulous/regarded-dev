package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.MITHRIL_CHAINBODY
import com.zenyte.game.item.ItemId.MITHRIL_FULL_HELM
import com.zenyte.game.item.ItemId.MITHRIL_KITESHIELD
import com.zenyte.game.item.ItemId.MITHRIL_MED_HELM
import com.zenyte.game.item.ItemId.MITHRIL_PLATEBODY
import com.zenyte.game.item.ItemId.MITHRIL_PLATELEGS
import com.zenyte.game.item.ItemId.MITHRIL_PLATESKIRT
import com.zenyte.game.item.ItemId.MITHRIL_SQ_SHIELD
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class ArmourShopShop : ShopScript() {
    init {
        "Armour Shop"(35, ShopCurrency.COINS, STOCK_ONLY) {
            MITHRIL_CHAINBODY(4, 1365, 2145)
            MITHRIL_MED_HELM(4, 546, 858)
            MITHRIL_FULL_HELM(4, 1001, 1573)
            MITHRIL_SQ_SHIELD(4, 1092, 1716)
            MITHRIL_KITESHIELD(4, 1547, 2431)
            MITHRIL_PLATELEGS(4, 1820, 2860)
            MITHRIL_PLATESKIRT(4, 1820, 2860)
            MITHRIL_PLATEBODY(4, 3640, 5720)
        }
    }
}