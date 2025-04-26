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

class HomeJewelleryShopShop : ShopScript() {
    init {
        "Jewellery Store"(ShopCurrency.COINS, STOCK_ONLY) {
            UnivShopItem(1478, buyPrice=3280).invoke()
            UnivShopItem(1692, buyPrice=1270, ironmanRestricted=true).invoke()
            UnivShopItem(1725, buyPrice=1820, ironmanRestricted=true).invoke()
            UnivShopItem(1727, buyPrice=1890, ironmanRestricted=true).invoke()
            UnivShopItem(1729, buyPrice=1800, ironmanRestricted=true).invoke()
            UnivShopItem(1731, buyPrice=2870, ironmanRestricted=true).invoke()
            UnivShopItem(1712, buyPrice=87052, ironmanRestricted=true).invoke()
            UnivShopItem(11091, buyPrice=2430, ironmanRestricted=true).invoke()
            UnivShopItem(2551, buyPrice=540, ironmanRestricted=true).invoke()
            UnivShopItem(2553, buyPrice=770, ironmanRestricted=true).invoke()
        }
    }
}