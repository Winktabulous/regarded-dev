package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.ADAMANT_JAVELIN
import com.zenyte.game.item.ItemId.ADAMANT_THROWNAXE
import com.zenyte.game.item.ItemId.BRONZE_JAVELIN
import com.zenyte.game.item.ItemId.BRONZE_THROWNAXE
import com.zenyte.game.item.ItemId.IRON_JAVELIN
import com.zenyte.game.item.ItemId.IRON_THROWNAXE
import com.zenyte.game.item.ItemId.MITHRIL_JAVELIN
import com.zenyte.game.item.ItemId.MITHRIL_THROWNAXE
import com.zenyte.game.item.ItemId.RUNE_JAVELIN
import com.zenyte.game.item.ItemId.RUNE_THROWNAXE
import com.zenyte.game.item.ItemId.STEEL_JAVELIN
import com.zenyte.game.item.ItemId.STEEL_THROWNAXE
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class AuthenticThrowingWeaponsShop : ShopScript() {
    init {
        "Authentic Throwing Weapons"(65, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_JAVELIN(900, 2, 5)
            IRON_JAVELIN(800, 2, 7)
            STEEL_JAVELIN(700, 12, 31)
            MITHRIL_JAVELIN(600, 32, 83)
            ADAMANT_JAVELIN(500, 82, 208)
            RUNE_JAVELIN(400, 208, 520)
            BRONZE_THROWNAXE(900, 1, 3)
            IRON_THROWNAXE(800, 4, 9)
            STEEL_THROWNAXE(700, 12, 33)
            MITHRIL_THROWNAXE(600, 35, 91)
            ADAMANT_THROWNAXE(500, 87, 228)
            RUNE_THROWNAXE(400, 232, 572)
        }
    }
}