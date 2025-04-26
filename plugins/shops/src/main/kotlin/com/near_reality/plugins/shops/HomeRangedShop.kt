package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*

class HomeRangedShop : ShopScript() {
    init {
        "Ranged Store"(1002, ShopCurrency.COINS, STOCK_ONLY) {
            UnivShopItem(884, buyPrice=18, quantity = 10_000_000, ironmanRestricted=true).invoke()
            UnivShopItem(888, buyPrice=76, quantity = 10_000_000).invoke()
            UnivShopItem(892, buyPrice=400, quantity = 10_000_000, ironmanRestricted=true).invoke()
            UnivShopItem(841, buyPrice=50).invoke()
            UnivShopItem(853, buyPrice=400).invoke()
            UnivShopItem(857, buyPrice=1200, ironmanRestricted=true).invoke()
            UnivShopItem(861, buyPrice=2600, ironmanRestricted=true).invoke()
            UnivShopItem(9142, buyPrice=89, quantity = 10_000_000, ironmanRestricted=true).invoke()
            UnivShopItem(9143, buyPrice=216, quantity = 10_000_000, ironmanRestricted=true).invoke()
            UnivShopItem(9144, buyPrice=540, quantity = 10_000_000, ironmanRestricted=true).invoke()
            UnivShopItem(9185, buyPrice=38643, ironmanRestricted=true).invoke()
            UnivShopItem(864, quantity = 10_000_000, buyPrice=21).invoke()
            UnivShopItem(868, buyPrice=975, quantity = 10_000_000, ironmanRestricted=true).invoke()
            UnivShopItem(807, quantity = 10_000_000, buyPrice=21).invoke()
            UnivShopItem(810, buyPrice=360, quantity = 10_000_000, ironmanRestricted=true).invoke()
            UnivShopItem(811, buyPrice=720, quantity = 10_000_000, ironmanRestricted=true).invoke()
            UnivShopItem(8880, quantity = 1000, buyPrice=2000).invoke()
            UnivShopItem(8882, buyPrice=3, quantity = 10_000_000).invoke()
            UnivShopItem(13193, quantity = 10000, buyPrice=350).invoke()
            UnivShopItem(1169, buyPrice=200).invoke()
            UnivShopItem(1129, buyPrice=21).invoke()
            UnivShopItem(1095, buyPrice=20).invoke()
            UnivShopItem(1135, buyPrice=7098, ironmanRestricted=true).invoke()
            UnivShopItem(1099, buyPrice=2730, ironmanRestricted=true).invoke()
            UnivShopItem(1065, buyPrice=1750, ironmanRestricted=true).invoke()
            UnivShopItem(2499, buyPrice=5616, ironmanRestricted=true).invoke()
            UnivShopItem(2493, buyPrice=2592, ironmanRestricted=true).invoke()
            UnivShopItem(2487, buyPrice=1800, ironmanRestricted=true).invoke()
            UnivShopItem(2501, buyPrice=8088, ironmanRestricted=true).invoke()
            UnivShopItem(2495, buyPrice=3732, ironmanRestricted=true).invoke()
            UnivShopItem(2489, buyPrice=2160, ironmanRestricted=true).invoke()
            UnivShopItem(2503, buyPrice=8088, ironmanRestricted=true).invoke()
            UnivShopItem(2497, buyPrice=3732, ironmanRestricted=true).invoke()
            UnivShopItem(2491, buyPrice=2592, ironmanRestricted=true).invoke()
            UnivShopItem(4740, buyPrice = 50, sellPrice = 27, quantity = 10_000_000).invoke()
            UnivShopItem(3749, buyPrice=81500).invoke()
            UnivShopItem(6328, buyPrice=5700, ironmanRestricted=true).invoke()
            UnivShopItem(10498, buyPrice=3500).invoke()
            UnivShopItem(10499, buyPrice=4467).invoke()
            UnivShopItem(1478, buyPrice=3280).invoke()
            UnivShopItem(1712, buyPrice=87052, ironmanRestricted=true).invoke()
        }
    }
}