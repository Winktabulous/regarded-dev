package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.AIR_RUNE
import com.zenyte.game.item.ItemId.AIR_RUNE_PACK
import com.zenyte.game.item.ItemId.BLUE_WIZARD_HAT
import com.zenyte.game.item.ItemId.BODY_RUNE
import com.zenyte.game.item.ItemId.CHAOS_RUNE
import com.zenyte.game.item.ItemId.CHAOS_RUNE_PACK
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.EARTH_RUNE
import com.zenyte.game.item.ItemId.EARTH_RUNE_PACK
import com.zenyte.game.item.ItemId.EYE_OF_NEWT
import com.zenyte.game.item.ItemId.EYE_OF_NEWT_PACK
import com.zenyte.game.item.ItemId.FIRE_RUNE
import com.zenyte.game.item.ItemId.FIRE_RUNE_PACK
import com.zenyte.game.item.ItemId.MIND_RUNE
import com.zenyte.game.item.ItemId.MIND_RUNE_PACK
import com.zenyte.game.item.ItemId.WATER_RUNE
import com.zenyte.game.item.ItemId.WATER_RUNE_PACK
import com.zenyte.game.item.ItemId.WIZARD_HAT
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class BettysMagicEmporiumShop : ShopScript() {
    init {
        "Betty's Magic Emporium"(15, ShopCurrency.COINS, STOCK_ONLY) {
            FIRE_RUNE(5000, 2, 4)
            WATER_RUNE(5000, 2, 4)
            AIR_RUNE(5000, 2, 4)
            EARTH_RUNE(5000, 2, 4)
            MIND_RUNE(5000, 1, 3)
            BODY_RUNE(5000, 1, 3)
            CHAOS_RUNE(250, 54, 96)
            DEATH_RUNE(250, 108, 220)
            FIRE_RUNE_PACK(80, 258, 430)
            WATER_RUNE_PACK(80, 258, 430)
            AIR_RUNE_PACK(80, 258, 430)
            EARTH_RUNE_PACK(80, 258, 430)
            MIND_RUNE_PACK(40, 198, 330)
            CHAOS_RUNE_PACK(35, 5970, 9950)
            EYE_OF_NEWT(300, 1, 3)
            EYE_OF_NEWT_PACK(60, 180, 300)
            BLUE_WIZARD_HAT(1, 1, 2)
            WIZARD_HAT(1, 1, 2)
        }
    }
}