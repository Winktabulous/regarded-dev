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

class HomeFoodShopShop : ShopScript() {
    init {
        "Consumables Store"(1007, ShopCurrency.COINS, STOCK_ONLY) {
            UnivShopItem(326, buyPrice=25).invoke()
            UnivShopItem(334, buyPrice=78).invoke()
            UnivShopItem(330, buyPrice=98).invoke()
            UnivShopItem(362, buyPrice=146, ironmanRestricted=true).invoke()
            UnivShopItem(380, buyPrice=244).invoke()
            UnivShopItem(374, buyPrice=289, ironmanRestricted=true).invoke()
            UnivShopItem(7947, buyPrice=483).invoke()
            UnivShopItem(386, buyPrice=1250, ironmanRestricted=true).invoke()
            UnivShopItem(3145, buyPrice=750).invoke()
            UnivShopItem(2290, buyPrice=48).invoke()
            UnivShopItem(2302, buyPrice=200).invoke()
            UnivShopItem(2447, buyPrice=250, ironmanRestricted=true).invoke()
            UnivShopItem(2429, buyPrice=250, ironmanRestricted=true).invoke()
            UnivShopItem(114, buyPrice=250, ironmanRestricted=true).invoke()
            UnivShopItem(2433, buyPrice=250, ironmanRestricted=true).invoke()
            UnivShopItem(2437, buyPrice=4000).invoke()
            UnivShopItem(2441, buyPrice=5000).invoke()
            UnivShopItem(2443, buyPrice=4000).invoke()
            UnivShopItem(3041, buyPrice=4000).invoke()
            UnivShopItem(2445, buyPrice=5000).invoke()
            UnivShopItem(2453, buyPrice=3500).invoke()
            UnivShopItem(2435, buyPrice=7500).invoke()
            UnivShopItem(3009, buyPrice=2000).invoke()
            UnivShopItem(6686, buyPrice=10000, ironmanRestricted=true).invoke()
            UnivShopItem(3025, buyPrice=10000, ironmanRestricted=true).invoke()
            UnivShopItem(3017, buyPrice=2000, ironmanRestricted=true).invoke()
            UnivShopItem(4843, buyPrice=3000, ironmanRestricted=true).invoke()
            UnivShopItem(4418, buyPrice=15000, ironmanRestricted=true).invoke()
            UnivShopItem(7509, buyPrice=382).invoke()
        }
    }
}