package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.ADAMANT_ARROW
import com.zenyte.game.item.ItemId.ADAMANT_ARROWTIPS
import com.zenyte.game.item.ItemId.ADAMANT_BRUTAL
import com.zenyte.game.item.ItemId.ARROW_SHAFT
import com.zenyte.game.item.ItemId.BLACK_BRUTAL
import com.zenyte.game.item.ItemId.BRONZE_ARROW
import com.zenyte.game.item.ItemId.BRONZE_ARROWTIPS
import com.zenyte.game.item.ItemId.BRONZE_BRUTAL
import com.zenyte.game.item.ItemId.COMP_OGRE_BOW
import com.zenyte.game.item.ItemId.IRON_ARROW
import com.zenyte.game.item.ItemId.IRON_ARROWTIPS
import com.zenyte.game.item.ItemId.IRON_BRUTAL
import com.zenyte.game.item.ItemId.LONGBOW
import com.zenyte.game.item.ItemId.MITHRIL_ARROW
import com.zenyte.game.item.ItemId.MITHRIL_ARROWTIPS
import com.zenyte.game.item.ItemId.MITHRIL_BRUTAL
import com.zenyte.game.item.ItemId.OAK_LONGBOW
import com.zenyte.game.item.ItemId.OAK_SHORTBOW
import com.zenyte.game.item.ItemId.RUNE_ARROW
import com.zenyte.game.item.ItemId.RUNE_ARROWTIPS
import com.zenyte.game.item.ItemId.RUNE_BRUTAL
import com.zenyte.game.item.ItemId.SHORTBOW
import com.zenyte.game.item.ItemId.STEEL_ARROW
import com.zenyte.game.item.ItemId.STEEL_ARROWTIPS
import com.zenyte.game.item.ItemId.STEEL_BRUTAL
import com.zenyte.game.item.ItemId.WILLOW_LONGBOW
import com.zenyte.game.item.ItemId.WILLOW_SHORTBOW
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DargaudsBowandArrowsShop : ShopScript() {
    init {
        "Dargaud's Bow and Arrows"(66, ShopCurrency.COINS, STOCK_ONLY) {
            ARROW_SHAFT(1000, 0, 1)
            BRONZE_ARROWTIPS(500, 0, 1)
            IRON_ARROWTIPS(400, 0, 2)
            STEEL_ARROWTIPS(300, 3, 6)
            MITHRIL_ARROWTIPS(200, 8, 16)
            ADAMANT_ARROWTIPS(200, 20, 40)
            RUNE_ARROWTIPS(150, 100, 200)
            BRONZE_ARROW(1000, 0, 1)
            IRON_ARROW(500, 1, 3)
            STEEL_ARROW(500, 6, 12)
            MITHRIL_ARROW(500, 16, 32)
            ADAMANT_ARROW(450, 40, 80)
            RUNE_ARROW(400, 200, 400)
            BRONZE_BRUTAL(0, 2, 5)
            IRON_BRUTAL(0, 5, 10)
            STEEL_BRUTAL(0, 10, 20)
            BLACK_BRUTAL(0, 17, 35)
            MITHRIL_BRUTAL(0, 25, 50)
            ADAMANT_BRUTAL(0, 47, 95)
            RUNE_BRUTAL(0, 225, 450)
            SHORTBOW(20, 25, 50)
            OAK_SHORTBOW(20, 50, 100)
            WILLOW_SHORTBOW(20, 100, 200)
            LONGBOW(20, 40, 80)
            OAK_LONGBOW(20, 80, 160)
            COMP_OGRE_BOW(0, 90, 180)
            WILLOW_LONGBOW(10, 160, 320)
        }
    }
}