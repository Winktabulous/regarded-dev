package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DonatorIslandPremiumShop : ShopScript() {
    init {
        "Premium Supplies"(ShopCurrency.COINS, STOCK_ONLY) {
            2360(250, 0, 1000, ironmanRestricted = true) // mithril bars
            1516(250, 0, 500, ironmanRestricted = true) // yew logs
            386(250, 0, 1000, ironmanRestricted = true) // sharks
            384(250, 0, 1000, ironmanRestricted = true) // sharks raw
            1624(250, 0, 1000, ironmanRestricted = true) // uncut sapphires
            1622(250, 0, 1500, ironmanRestricted = true) // uncut emeralds
            5291(30, 0, 250, ironmanRestricted = true) // guam seed
            5292(30, 0, 300, ironmanRestricted = true) // marrentill seed
            5293(30, 0, 350, ironmanRestricted = true) // tarromin seed
            5294(30, 0, 400, ironmanRestricted = true) // harralander seed
            22879(30, 0, 3000, ironmanRestricted = true) // snape grass seed
        }
    }
}