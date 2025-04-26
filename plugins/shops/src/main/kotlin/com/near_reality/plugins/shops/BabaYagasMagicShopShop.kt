package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.AIR_RUNE
import com.zenyte.game.item.ItemId.AIR_RUNE_PACK
import com.zenyte.game.item.ItemId.ASTRAL_RUNE
import com.zenyte.game.item.ItemId.BATTLESTAFF
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.BODY_RUNE
import com.zenyte.game.item.ItemId.CHAOS_RUNE
import com.zenyte.game.item.ItemId.CHAOS_RUNE_PACK
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.EARTH_RUNE
import com.zenyte.game.item.ItemId.EARTH_RUNE_PACK
import com.zenyte.game.item.ItemId.FIRE_RUNE
import com.zenyte.game.item.ItemId.FIRE_RUNE_PACK
import com.zenyte.game.item.ItemId.LAW_RUNE
import com.zenyte.game.item.ItemId.LUNAR_SIGNET
import com.zenyte.game.item.ItemId.MIND_RUNE
import com.zenyte.game.item.ItemId.MIND_RUNE_PACK
import com.zenyte.game.item.ItemId.MOONCLAN_MANUAL
import com.zenyte.game.item.ItemId.NATURE_RUNE
import com.zenyte.game.item.ItemId.SOUL_RUNE
import com.zenyte.game.item.ItemId.STAFF_OF_AIR
import com.zenyte.game.item.ItemId.STAFF_OF_EARTH
import com.zenyte.game.item.ItemId.STAFF_OF_FIRE
import com.zenyte.game.item.ItemId.STAFF_OF_WATER
import com.zenyte.game.item.ItemId.WATER_RUNE
import com.zenyte.game.item.ItemId.WATER_RUNE_PACK
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class BabaYagasMagicShopShop : ShopScript() {
    init {
        "Baba Yaga's Magic Shop"(43, ShopCurrency.COINS, STOCK_ONLY) {
            AIR_RUNE(5000, 2, 4, 5)
            WATER_RUNE(5000, 2, 4, 5)
            EARTH_RUNE(5000, 2, 4, 5)
            FIRE_RUNE(5000, 2, 4, 5)
            MIND_RUNE(5000, 1, 3, 5)
            BODY_RUNE(5000, 1, 16, 5)
            CHAOS_RUNE(250, 49, 90, 5)
            NATURE_RUNE(250, 99, 180, 5)
            DEATH_RUNE(250, 99, 180, 5)
            LAW_RUNE(250, 132, 240, 5)
            BLOOD_RUNE(250, 220, 400, 5)
            SOUL_RUNE(250, 161, 300, 5)
            ASTRAL_RUNE(250, 27, 50, 5)
            FIRE_RUNE_PACK(80, 236, 430)
            WATER_RUNE_PACK(80, 236, 430)
            AIR_RUNE_PACK(80, 236, 430)
            EARTH_RUNE_PACK(80, 236, 430)
            MIND_RUNE_PACK(40, 181, 330)
            CHAOS_RUNE_PACK(35, 5472, 9950)
            BATTLESTAFF(5, 3850, 7000)
            STAFF_OF_FIRE(2, 900, 1500)
            STAFF_OF_WATER(2, 900, 1500)
            STAFF_OF_AIR(2, 900, 1500)
            STAFF_OF_EARTH(2, 900, 1500)
            LUNAR_SIGNET(5, 0, 2)
            MOONCLAN_MANUAL(100, 0, 1)
        }
    }
}