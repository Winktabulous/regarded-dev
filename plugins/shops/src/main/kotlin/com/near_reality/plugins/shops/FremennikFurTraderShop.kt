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

class FremennikFurTraderShop : ShopScript() {
    init {
        "Fremennik Fur Trader"(52, ShopCurrency.COINS, STOCK_ONLY) {
            BEAR_FUR(3, 9, 12)
            GREY_WOLF_FUR(3, 47, 60)
            POLAR_KEBBIT_FUR(0, 9, 12)
            COMMON_KEBBIT_FUR(0, 11, 14)
            FELDIP_WEASEL_FUR(0, 13, 16)
            DESERT_DEVIL_FUR(0, 16, 20)
            TATTY_LARUPIA_FUR(0, 57, 72)
            LARUPIA_FUR(0, 76, 96)
            TATTY_GRAAHK_FUR(0, 85, 108)
            GRAAHK_FUR(0, 114, 144)
            TATTY_KYATT_FUR(0, 114, 144)
            KYATT_FUR(0, 152, 192)
        }
    }
}