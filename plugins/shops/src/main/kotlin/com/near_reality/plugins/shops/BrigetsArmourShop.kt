package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BRONZE_CHAINBODY
import com.zenyte.game.item.ItemId.BRONZE_MED_HELM
import com.zenyte.game.item.ItemId.BRONZE_PLATELEGS
import com.zenyte.game.item.ItemId.BRONZE_PLATESKIRT
import com.zenyte.game.item.ItemId.IRON_CHAINBODY
import com.zenyte.game.item.ItemId.IRON_MED_HELM
import com.zenyte.game.item.ItemId.IRON_PLATELEGS
import com.zenyte.game.item.ItemId.IRON_PLATESKIRT
import com.zenyte.game.item.ItemId.STEEL_CHAINBODY
import com.zenyte.game.item.ItemId.STEEL_MED_HELM
import com.zenyte.game.item.ItemId.STEEL_PLATELEGS
import com.zenyte.game.item.ItemId.STEEL_PLATESKIRT
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class BrigetsArmourShop : ShopScript() {
    init {
        "Briget's Armour"(220, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_CHAINBODY(5, -1, 60)
            IRON_CHAINBODY(3, -1, 210)
            STEEL_CHAINBODY(3, -1, 750)
            BRONZE_PLATELEGS(3, -1, 80)
            IRON_PLATELEGS(2, -1, 280)
            STEEL_PLATELEGS(2, -1, 1000)
            BRONZE_PLATESKIRT(3, -1, 80)
            IRON_PLATESKIRT(2, -1, 280)
            STEEL_PLATESKIRT(2, -1, 1000)
            BRONZE_MED_HELM(5, -1, 24)
            IRON_MED_HELM(3, -1, 84)
            STEEL_MED_HELM(3, -1, 300)
        }
    }
}