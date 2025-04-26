package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.DIAMOND
import com.zenyte.game.item.ItemId.EMERALD
import com.zenyte.game.item.ItemId.RUBY
import com.zenyte.game.item.ItemId.SAPPHIRE
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class ArdougneGemStallShop : ShopScript() {
    init {
        "Ardougne Gem Stall"(80, ShopCurrency.COINS, STOCK_ONLY) {
            SAPPHIRE(2, 200, 375)
            EMERALD(1, 400, 750)
            RUBY(1, 800, 1500)
            DIAMOND(0, 1600, 3000)
        }
    }
}