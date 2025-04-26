package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DonatorIslandUberPremiumShop : ShopScript() {
    init {
        "Uber Premium Supplies"(ShopCurrency.COINS, STOCK_ONLY) {
            2364(250, 0, 15000, ironmanRestricted = true) // rune bar
            19670(250, 0, 750, ironmanRestricted = true) // redwood logs
            13442(250, 0, 2500, ironmanRestricted = true) // anglerfish
            13440(250, 0, 2500, ironmanRestricted = true) // anglerfish raw
            1618(250, 0, 3500, ironmanRestricted = true) // uncut diamond
            5300(10, 0, 30000, ironmanRestricted = true) // snapdragon seed
            5302(10, 0, 1250, ironmanRestricted = true) // lantadyme seed
            5303(10, 0, 1500, ironmanRestricted = true) // dwarf weed seed
            5304(10, 0, 25000, ironmanRestricted = true) // torstol seed
            6052(100, 0, 3000, ironmanRestricted = true) // magic roots
        }
    }
}