package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BALL_OF_WOOL
import com.zenyte.game.item.ItemId.BRONZE_ARROW
import com.zenyte.game.item.ItemId.BRONZE_PICKAXE
import com.zenyte.game.item.ItemId.COOKED_MEAT
import com.zenyte.game.item.ItemId.IRON_AXE
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.PAPYRUS
import com.zenyte.game.item.ItemId.ROPE
import com.zenyte.game.item.ItemId.TINDERBOX
import com.zenyte.game.item.ItemId.VIAL_OF_WATER
import com.zenyte.game.item.ItemId.WATERFILLED_VIAL_PACK
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.CAN_SELL

class AemadsAdventuringSuppliesShop : ShopScript() {
    init {
        "Aemad's Adventuring Supplies"(76, ShopCurrency.COINS, CAN_SELL) {
            VIAL_OF_WATER(500, 0, 2)
            WATERFILLED_VIAL_PACK(250, 80, 261)
            BRONZE_PICKAXE(2, 0, 1)
            IRON_AXE(2, 22, 72)
            COOKED_MEAT(2, 1, 5)
            TINDERBOX(2, 0, 1)
            BALL_OF_WOOL(30, 0, 2)
            BRONZE_ARROW(500, 0, 1)
            ROPE(20, 7, 23)
            PAPYRUS(50, 4, 13)
            KNIFE(2, 2, 7)
        }
    }
}