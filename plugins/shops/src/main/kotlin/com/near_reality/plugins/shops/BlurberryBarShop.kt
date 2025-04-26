package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.PREMADE_BLURB_SP
import com.zenyte.game.item.ItemId.PREMADE_CHOC_SDY
import com.zenyte.game.item.ItemId.PREMADE_DR_DRAGON
import com.zenyte.game.item.ItemId.PREMADE_FR_BLAST
import com.zenyte.game.item.ItemId.PREMADE_P_PUNCH
import com.zenyte.game.item.ItemId.PREMADE_SGG
import com.zenyte.game.item.ItemId.PREMADE_WIZ_BLZD
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class BlurberryBarShop : ShopScript() {
    init {
        "Blurberry Bar"(93, ShopCurrency.COINS, STOCK_ONLY) {
            PREMADE_BLURB_SP(10, 18, 30)
            PREMADE_CHOC_SDY(10, 18, 30)
            PREMADE_DR_DRAGON(10, 18, 30)
            PREMADE_FR_BLAST(10, 18, 30)
            PREMADE_P_PUNCH(10, 18, 30)
            PREMADE_SGG(10, 18, 30)
            PREMADE_WIZ_BLZD(10, 18, 30)
        }
    }
}