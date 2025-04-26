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

class HomeMagicShop : ShopScript() {
    init {
        "Magic Store"(1005, ShopCurrency.COINS, STOCK_ONLY) {
            UnivShopItem(32056, buyPrice=175).invoke()
            UnivShopItem(558, quantity = 100_000_000, buyPrice=3).invoke()
            UnivShopItem(556, quantity = 100_000_000, buyPrice=4).invoke()
            UnivShopItem(554, quantity = 100_000_000, buyPrice=4).invoke()
            UnivShopItem(555, quantity = 100_000_000, buyPrice=4).invoke()
            UnivShopItem(557, quantity = 100_000_000, buyPrice=4).invoke()
            UnivShopItem(559, quantity = 100_000_000, buyPrice=3).invoke()
            UnivShopItem(562, quantity = 100_000_000, buyPrice=90).invoke()
            UnivShopItem(560, quantity = 100_000_000, buyPrice=220).invoke()
            UnivShopItem(565, quantity = 100_000_000, buyPrice=575).invoke()
            UnivShopItem(563, quantity = 100_000_000, buyPrice=280).invoke()
            UnivShopItem(561, quantity = 100_000_000, buyPrice=230).invoke()
            UnivShopItem(564, quantity = 100_000_000, buyPrice=75).invoke()
            UnivShopItem(9075, buyPrice=50).invoke()
            UnivShopItem(21880, buyPrice=750, ironmanRestricted=true).invoke()
            UnivShopItem(566, quantity = 100_000_000, buyPrice=300).invoke()
            UnivShopItem(4695, buyPrice=9).invoke()
            UnivShopItem(4696, buyPrice=12).invoke()
            UnivShopItem(4698, buyPrice=9).invoke()
            UnivShopItem(4697, buyPrice=11).invoke()
            UnivShopItem(4694, buyPrice=9).invoke()
            UnivShopItem(4699, buyPrice=9).invoke()
            UnivShopItem(4089, buyPrice=9000).invoke()
            UnivShopItem(4091, buyPrice=72000).invoke()
            UnivShopItem(4093, buyPrice=48000).invoke()
            UnivShopItem(4095, buyPrice=6000).invoke()
            UnivShopItem(4097, buyPrice=6000).invoke()
            UnivShopItem(1381, buyPrice=2560).invoke()
            UnivShopItem(1383, buyPrice=2560).invoke()
            UnivShopItem(1385, buyPrice=2560).invoke()
            UnivShopItem(1387, buyPrice=2560).invoke()
            UnivShopItem(1391, buyPrice=12889, ironmanRestricted=true).invoke()
            UnivShopItem(772, buyPrice=4322).invoke()
            UnivShopItem(4675, buyPrice=80744).invoke()
            UnivShopItem(1409, buyPrice=200000).invoke()
            UnivShopItem(1727, buyPrice=900).invoke()
            UnivShopItem(3755, buyPrice=78000).invoke()
            UnivShopItem(2415, buyPrice=80000).invoke()
            UnivShopItem(2416, buyPrice=80000).invoke()
            UnivShopItem(2417, buyPrice=80000).invoke()
            UnivShopItem(2890, buyPrice=96).invoke()
            UnivShopItem(9731, buyPrice=96).invoke()
            UnivShopItem(579, buyPrice=800).invoke()
            UnivShopItem(577, buyPrice=1566).invoke()
            UnivShopItem(1011, buyPrice=1122).invoke()
            UnivShopItem(772, buyPrice=384).invoke()
            UnivShopItem(6107, buyPrice=852).invoke()
            UnivShopItem(6108, buyPrice=685).invoke()
            UnivShopItem(6109, buyPrice=6969).invoke()
            UnivShopItem(6111, buyPrice=255).invoke()
            UnivShopItem(6110, buyPrice=257).invoke()
            UnivShopItem(6106, buyPrice=244).invoke()
        }
    }
}