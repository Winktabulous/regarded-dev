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

class AuburysRuneShopShop : ShopScript() {
    init {
        "Aubury's Rune Shop"(160, ShopCurrency.COINS, STOCK_ONLY) {
            AIR_RUNE(5000, 2, 4)
            MIND_RUNE(5000, 1, 3)
            FIRE_RUNE(5000, 2, 4)
            WATER_RUNE(5000, 2, 4)
            EARTH_RUNE(5000, 2, 4)
            BODY_RUNE(5000, 1, 3)
            CHAOS_RUNE(250, 90, 90)
            DEATH_RUNE(250, 99, 180)
            FIRE_RUNE_PACK(80, 236, 430)
            WATER_RUNE_PACK(80, 236, 430)
            AIR_RUNE_PACK(80, 236, 430)
            EARTH_RUNE_PACK(80, 236, 430)
            MIND_RUNE_PACK(40, 181, 330)
            CHAOS_RUNE_PACK(35, 5472, 9950)
        }
    }
}