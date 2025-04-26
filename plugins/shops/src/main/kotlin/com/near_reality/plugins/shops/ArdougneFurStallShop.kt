package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BEAR_FUR
import com.zenyte.game.item.ItemId.COMMON_KEBBIT_FUR
import com.zenyte.game.item.ItemId.DESERT_DEVIL_FUR
import com.zenyte.game.item.ItemId.FELDIP_WEASEL_FUR
import com.zenyte.game.item.ItemId.GRAAHK_FUR
import com.zenyte.game.item.ItemId.GREY_WOLF_FUR
import com.zenyte.game.item.ItemId.KYATT_FUR
import com.zenyte.game.item.ItemId.LARUPIA_FUR
import com.zenyte.game.item.ItemId.POLAR_KEBBIT_FUR
import com.zenyte.game.item.ItemId.TATTY_GRAAHK_FUR
import com.zenyte.game.item.ItemId.TATTY_KYATT_FUR
import com.zenyte.game.item.ItemId.TATTY_LARUPIA_FUR
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class ArdougneFurStallShop : ShopScript() {
    init {
        "Ardougne Fur Stall"(79, ShopCurrency.COINS, STOCK_ONLY) {
            BEAR_FUR(3, -1, 12)
            GREY_WOLF_FUR(3, -1, 60)
            POLAR_KEBBIT_FUR(0, 13, 12)
            COMMON_KEBBIT_FUR(0, 14, 14)
            FELDIP_WEASEL_FUR(0, 16, 16)
            DESERT_DEVIL_FUR(0, 20, 20)
            TATTY_LARUPIA_FUR(0, 61, 72)
            LARUPIA_FUR(0, 81, 96)
            TATTY_GRAAHK_FUR(0, 91, 108)
            GRAAHK_FUR(0, 122, 144)
            TATTY_KYATT_FUR(0, 144, 144)
            KYATT_FUR(0, 192, 192)
        }
    }
}