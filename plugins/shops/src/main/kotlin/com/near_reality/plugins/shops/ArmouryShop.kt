package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.ADAMANT_2H_SWORD
import com.zenyte.game.item.ItemId.BLACK_2H_SWORD
import com.zenyte.game.item.ItemId.BRONZE_2H_SWORD
import com.zenyte.game.item.ItemId.BRONZE_ARROW
import com.zenyte.game.item.ItemId.BRONZE_ARROWTIPS
import com.zenyte.game.item.ItemId.BRONZE_BOLTS
import com.zenyte.game.item.ItemId.CROSSBOW
import com.zenyte.game.item.ItemId.IRON_2H_SWORD
import com.zenyte.game.item.ItemId.IRON_ARROWTIPS
import com.zenyte.game.item.ItemId.IRON_AXE
import com.zenyte.game.item.ItemId.IRON_BATTLEAXE
import com.zenyte.game.item.ItemId.LONGBOW
import com.zenyte.game.item.ItemId.MITHRIL_2H_SWORD
import com.zenyte.game.item.ItemId.MITHRIL_ARROWTIPS
import com.zenyte.game.item.ItemId.MITHRIL_BATTLEAXE
import com.zenyte.game.item.ItemId.SHORTBOW
import com.zenyte.game.item.ItemId.STEEL_2H_SWORD
import com.zenyte.game.item.ItemId.STEEL_ARROWTIPS
import com.zenyte.game.item.ItemId.STEEL_AXE
import com.zenyte.game.item.ItemId.STEEL_BATTLEAXE
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class ArmouryShop : ShopScript() {
    init {
        "Armoury"(85, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_ARROW(200, 0, 1)
            BRONZE_BOLTS(200, 0, 1)
            SHORTBOW(4, 25, 75)
            LONGBOW(2, 40, 120)
            CROSSBOW(2, 35, 105)
            BRONZE_ARROWTIPS(800, 0, 1)
            IRON_ARROWTIPS(800, 1, 3)
            STEEL_ARROWTIPS(800, 3, 9)
            MITHRIL_ARROWTIPS(800, 8, 24)
            IRON_AXE(5, 28, 84)
            STEEL_AXE(3, 100, 300)
            IRON_BATTLEAXE(5, 91, 273)
            STEEL_BATTLEAXE(2, 325, 975)
            MITHRIL_BATTLEAXE(1, 845, 2535)
            BRONZE_2H_SWORD(4, 40, 120)
            IRON_2H_SWORD(3, 140, 420)
            STEEL_2H_SWORD(2, 500, 1500)
            BLACK_2H_SWORD(1, 960, 2880)
            MITHRIL_2H_SWORD(1, 1300, 3900)
            ADAMANT_2H_SWORD(1, 3200, 9600)
        }
    }
}