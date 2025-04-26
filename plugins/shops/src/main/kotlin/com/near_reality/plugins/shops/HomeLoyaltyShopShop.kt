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

class HomeLoyaltyShopShop : ShopScript() {
    init {
        "Loyalty Shop"(LOYALTY_POINTS, NO_SELLING) {
            UnivShopItem(11740, buyPrice = 1).invoke()
            UnivShopItem(2678, buyPrice = 50).invoke()
            UnivShopItem(22316, buyPrice = 250).invoke()
            UnivShopItem(4079, buyPrice = 500).invoke()
            UnivShopItem(19556, buyPrice = 7500).invoke()
            UnivShopItem(6865, buyPrice = 750).invoke()
            UnivShopItem(6866, buyPrice = 750).invoke()
            UnivShopItem(6867, buyPrice = 750).invoke()
            UnivShopItem(4566, buyPrice = 2000).invoke()
            UnivShopItem(12727, buyPrice = 30).invoke()
            UnivShopItem(2520, buyPrice = 75).invoke()
            UnivShopItem(2522, buyPrice = 75).invoke()
            UnivShopItem(2524, buyPrice = 75).invoke()
            UnivShopItem(4613, buyPrice = 150).invoke()
            UnivShopItem(11705, buyPrice = 400).invoke()
            UnivShopItem(11706, buyPrice = 400).invoke()
            UnivShopItem(11707, buyPrice = 200).invoke()
            UnivShopItem(11708, buyPrice = 200).invoke()
            UnivShopItem(11709, buyPrice = 200).invoke()
            UnivShopItem(13188, buyPrice = 5000).invoke()
            UnivShopItem(13328, buyPrice = 100).invoke()
            UnivShopItem(13681, buyPrice = 50).invoke()
            UnivShopItem(13215, buyPrice = 75).invoke()
            UnivShopItem(13216, buyPrice = 75).invoke()
            UnivShopItem(13217, buyPrice = 75).invoke()
            UnivShopItem(13218, buyPrice = 75).invoke()
            UnivShopItem(22355, buyPrice = 300).invoke()
            UnivShopItem(22358, buyPrice = 300).invoke()
            UnivShopItem(22361, buyPrice = 300).invoke()
            UnivShopItem(19699, buyPrice = 750).invoke()
            UnivShopItem(21354, buyPrice = 500).invoke()
            UnivShopItem(13203, buyPrice = 300).invoke()
            UnivShopItem(7003, buyPrice = 50).invoke()
            UnivShopItem(10171, buyPrice = 50).invoke()
            UnivShopItem(8964, buyPrice = 300).invoke()
            UnivShopItem(8957, buyPrice = 200).invoke()
            UnivShopItem(8996, buyPrice = 200).invoke()
            UnivShopItem(8959, buyPrice = 300).invoke()
            UnivShopItem(8952, buyPrice = 200).invoke()
            UnivShopItem(8991, buyPrice = 200).invoke()
            UnivShopItem(8960, buyPrice = 300).invoke()
            UnivShopItem(8953, buyPrice = 200).invoke()
            UnivShopItem(8992, buyPrice = 200).invoke()
            UnivShopItem(8961, buyPrice = 300).invoke()
            UnivShopItem(8954, buyPrice = 200).invoke()
            UnivShopItem(8993, buyPrice = 200).invoke()
            UnivShopItem(8962, buyPrice = 300).invoke()
            UnivShopItem(8955, buyPrice = 200).invoke()
            UnivShopItem(8994, buyPrice = 200).invoke()
            UnivShopItem(8965, buyPrice = 300).invoke()
            UnivShopItem(8958, buyPrice = 200).invoke()
            UnivShopItem(8997, buyPrice = 200).invoke()
        }
    }
}