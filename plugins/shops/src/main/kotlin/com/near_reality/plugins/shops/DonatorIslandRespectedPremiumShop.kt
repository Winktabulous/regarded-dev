package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DonatorIslandRespectedPremiumShop : ShopScript() {
    init {
        "Respected Premium Supplies"(ShopCurrency.COINS, STOCK_ONLY) {
            454(250, 0, 100, ironmanRestricted = true) // coal
            2362(250, 0, 2500, ironmanRestricted = true) // adamant bars
            1514(250, 0, 1000, ironmanRestricted = true) // magic logs
            392(250, 0, 1500, ironmanRestricted = true) // manta ray
            390(250, 0, 1500, ironmanRestricted = true) // manta ray raw
            1620(250, 0, 2000, ironmanRestricted = true) // uncut ruby
            5295(15, 0, 20000, ironmanRestricted = true) // ranarr seed
            5296(15, 0, 2500, ironmanRestricted = true) // toadflax seed
            5297(15, 0, 500, ironmanRestricted = true) // irit seed
            5298(15, 0, 750, ironmanRestricted = true) // avantoe seed
            5299(15, 0, 1000, ironmanRestricted = true) // kwuarm seed
            6050(100, 0, 500, ironmanRestricted = true) // yew roots
        }
    }
}