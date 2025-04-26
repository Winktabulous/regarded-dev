package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.AIR_RUNE
import com.zenyte.game.item.ItemId.AIR_RUNE_PACK
import com.zenyte.game.item.ItemId.BODY_RUNE
import com.zenyte.game.item.ItemId.CHAOS_RUNE
import com.zenyte.game.item.ItemId.CHAOS_RUNE_PACK
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.EARTH_RUNE
import com.zenyte.game.item.ItemId.EARTH_RUNE_PACK
import com.zenyte.game.item.ItemId.FIRE_RUNE
import com.zenyte.game.item.ItemId.FIRE_RUNE_PACK
import com.zenyte.game.item.ItemId.MIND_RUNE
import com.zenyte.game.item.ItemId.MIND_RUNE_PACK
import com.zenyte.game.item.ItemId.WATER_RUNE
import com.zenyte.game.item.ItemId.WATER_RUNE_PACK
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class BattleRunesShop : ShopScript() {
    init {
        "Battle Runes"(238, ShopCurrency.COINS, STOCK_ONLY) {
            FIRE_RUNE(100, 2, 4)
            WATER_RUNE(100, 2, 4)
            AIR_RUNE(100, 2, 4)
            EARTH_RUNE(100, 2, 4)
            MIND_RUNE(100, 1, 3)
            BODY_RUNE(100, 1, 3)
            CHAOS_RUNE(50, 54, 90)
            DEATH_RUNE(50, 108, 180)
            FIRE_RUNE_PACK(35, 258, 430)
            WATER_RUNE_PACK(35, 258, 430)
            AIR_RUNE_PACK(35, 258, 430)
            EARTH_RUNE_PACK(35, 258, 430)
            MIND_RUNE_PACK(25, 198, 330)
            CHAOS_RUNE_PACK(25, 5970, 9950)
        }
    }
}