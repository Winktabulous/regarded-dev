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

class HomeMeleeShop : ShopScript() {
    init {
        "Melee Store"(1001, ShopCurrency.COINS, STOCK_ONLY) {
            UnivShopItem(1323, buyPrice=112).invoke()
            UnivShopItem(1329, buyPrice=1040).invoke()
            UnivShopItem(1333, buyPrice=23480, ironmanRestricted=true).invoke()
            UnivShopItem(6746, buyPrice=1688).invoke()
            UnivShopItem(10858, buyPrice=100000).invoke()
            UnivShopItem(4587, buyPrice=125375, ironmanRestricted=true).invoke()
            UnivShopItem(1215, buyPrice=35225, ironmanRestricted=true).invoke()
            UnivShopItem(1249, buyPrice=37440, ironmanRestricted=true).invoke()
            UnivShopItem(1305, buyPrice=100000, ironmanRestricted=true).invoke()
            UnivShopItem(1377, buyPrice=200000, ironmanRestricted=true).invoke()
            UnivShopItem(1434, buyPrice=50000, ironmanRestricted=true).invoke()
            UnivShopItem(3204, buyPrice=375000, ironmanRestricted=true).invoke()
            UnivShopItem(1153, buyPrice=154).invoke()
            UnivShopItem(1115, buyPrice=560).invoke()
            UnivShopItem(1067, buyPrice=280).invoke()
            UnivShopItem(1191, buyPrice=238).invoke()
            UnivShopItem(5574, buyPrice=6000).invoke()
            UnivShopItem(5575, buyPrice=10000).invoke()
            UnivShopItem(5576, buyPrice=8000).invoke()
            UnivShopItem(9672, buyPrice=8000).invoke()
            UnivShopItem(9674, buyPrice=12000).invoke()
            UnivShopItem(9676, buyPrice=10000).invoke()
            UnivShopItem(1163, buyPrice=31000, ironmanRestricted=true).invoke()
            UnivShopItem(1127, buyPrice=84500, ironmanRestricted=true).invoke()
            UnivShopItem(1079, buyPrice=64000, ironmanRestricted=true).invoke()
            UnivShopItem(1201, buyPrice=56000, ironmanRestricted=true).invoke()
            UnivShopItem(1540, buyPrice=2588).invoke()
            UnivShopItem(4502, buyPrice=18795).invoke()
            UnivShopItem(544, buyPrice=24).invoke()
            UnivShopItem(542, buyPrice=18).invoke()
            UnivShopItem(3105, buyPrice=2460).invoke()
            UnivShopItem(4119, buyPrice=458, ironmanRestricted=true).invoke()
            UnivShopItem(4121, buyPrice=780, ironmanRestricted=true).invoke()
            UnivShopItem(4123, buyPrice=1257, ironmanRestricted=true).invoke()
            UnivShopItem(4125, buyPrice=3580, ironmanRestricted=true).invoke()
            UnivShopItem(4127, buyPrice=7480, ironmanRestricted=true).invoke()
            UnivShopItem(4129, buyPrice=13928, ironmanRestricted=true).invoke()
            UnivShopItem(4131, buyPrice=28790, ironmanRestricted=true).invoke()
            UnivShopItem(1725, buyPrice=1215, ironmanRestricted=true).invoke()
            UnivShopItem(1712, buyPrice=87052, ironmanRestricted=true).invoke()
            UnivShopItem(11090, buyPrice=2430, ironmanRestricted=true).invoke()
            UnivShopItem(2570, buyPrice=2430, ironmanRestricted=true).invoke()
            UnivShopItem(2550, buyPrice=540, ironmanRestricted=true).invoke()
            UnivShopItem(11118, buyPrice=56000, ironmanRestricted=true).invoke()
            UnivShopItem(2552, buyPrice=770, ironmanRestricted=true).invoke()
            UnivShopItem(10828, buyPrice=85200).invoke()
            UnivShopItem(3751, buyPrice=78000).invoke()
            UnivShopItem(3753, buyPrice=78000).invoke()
            UnivShopItem(MITHRIL_GLOVES, buyPrice = 1950).invoke()
            UnivShopItem(RUNE_GLOVES, buyPrice = 6500).invoke()
            UnivShopItem(BARROWS_GLOVES, buyPrice = 130_000).invoke()
            UnivShopItem(DWARVEN_HELMET, buyPrice = 60_000).invoke()
        }
    }
}