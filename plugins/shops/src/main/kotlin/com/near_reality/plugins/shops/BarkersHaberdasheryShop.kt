package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BLACK_CAPE
import com.zenyte.game.item.ItemId.BLUE_CAPE
import com.zenyte.game.item.ItemId.GREEN_CAPE
import com.zenyte.game.item.ItemId.GREY_BOOTS
import com.zenyte.game.item.ItemId.GREY_GLOVES
import com.zenyte.game.item.ItemId.GREY_HAT
import com.zenyte.game.item.ItemId.GREY_ROBE_BOTTOMS
import com.zenyte.game.item.ItemId.GREY_ROBE_TOP
import com.zenyte.game.item.ItemId.PURPLE_BOOTS
import com.zenyte.game.item.ItemId.PURPLE_GLOVES
import com.zenyte.game.item.ItemId.PURPLE_HAT
import com.zenyte.game.item.ItemId.PURPLE_ROBE_BOTTOMS
import com.zenyte.game.item.ItemId.PURPLE_ROBE_TOP
import com.zenyte.game.item.ItemId.RED_BOOTS
import com.zenyte.game.item.ItemId.RED_CAPE
import com.zenyte.game.item.ItemId.RED_GLOVES
import com.zenyte.game.item.ItemId.RED_HAT
import com.zenyte.game.item.ItemId.RED_ROBE_BOTTOMS
import com.zenyte.game.item.ItemId.RED_ROBE_TOP
import com.zenyte.game.item.ItemId.TEAL_BOOTS
import com.zenyte.game.item.ItemId.TEAL_GLOVES
import com.zenyte.game.item.ItemId.TEAL_HAT
import com.zenyte.game.item.ItemId.TEAL_ROBE_BOTTOMS
import com.zenyte.game.item.ItemId.TEAL_ROBE_TOP
import com.zenyte.game.item.ItemId.YELLOW_BOOTS
import com.zenyte.game.item.ItemId.YELLOW_CAPE
import com.zenyte.game.item.ItemId.YELLOW_GLOVES
import com.zenyte.game.item.ItemId.YELLOW_HAT
import com.zenyte.game.item.ItemId.YELLOW_ROBE_BOTTOMS
import com.zenyte.game.item.ItemId.YELLOW_ROBE_TOP
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class BarkersHaberdasheryShop : ShopScript() {
    init {
        "Barker's Haberdashery"(171, ShopCurrency.COINS, STOCK_ONLY) {
            GREY_BOOTS(5, 200, 650)
            GREY_ROBE_TOP(5, 200, 650)
            GREY_ROBE_BOTTOMS(5, 200, 650)
            GREY_HAT(5, 200, 650)
            GREY_GLOVES(5, 200, 650)
            RED_BOOTS(5, 200, 650)
            RED_ROBE_TOP(5, 200, 650)
            RED_ROBE_BOTTOMS(5, 200, 650)
            RED_HAT(5, 200, 650)
            RED_GLOVES(5, 200, 650)
            YELLOW_BOOTS(5, 200, 650)
            YELLOW_ROBE_TOP(5, 200, 650)
            YELLOW_ROBE_BOTTOMS(5, 200, 650)
            YELLOW_HAT(5, 200, 650)
            YELLOW_GLOVES(5, 200, 650)
            TEAL_BOOTS(5, 200, 650)
            TEAL_ROBE_TOP(5, 200, 650)
            TEAL_ROBE_BOTTOMS(5, 200, 650)
            TEAL_HAT(5, 200, 650)
            TEAL_GLOVES(5, 200, 650)
            PURPLE_BOOTS(5, 200, 650)
            PURPLE_ROBE_TOP(5, 200, 650)
            PURPLE_ROBE_BOTTOMS(5, 200, 650)
            PURPLE_HAT(5, 200, 650)
            PURPLE_GLOVES(5, 200, 650)
            RED_CAPE(5, 0, 2)
            BLACK_CAPE(5, 2, 9)
            BLUE_CAPE(5, 12, 41)
            YELLOW_CAPE(5, 12, 41)
            GREEN_CAPE(5, 12, 41)
        }
    }
}