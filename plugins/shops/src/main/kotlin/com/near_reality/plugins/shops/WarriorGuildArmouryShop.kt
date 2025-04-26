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

class WarriorGuildArmouryShop : ShopScript() {
    init {
        "Warrior Guild Armoury"(67, ShopCurrency.COINS, STOCK_ONLY) {
            IRON_BATTLEAXE(5, 109, 218)
            STEEL_BATTLEAXE(2, 390, 780)
            MITHRIL_BATTLEAXE(1, 1014, 2028)
            BRONZE_SWORD(5, 15, 31)
            IRON_SWORD(4, 54, 109)
            STEEL_SWORD(4, 195, 390)
            BLACK_SWORD(3, 374, 748)
            MITHRIL_SWORD(3, 507, 1014)
            ADAMANT_SWORD(2, 1248, 2496)
            BRONZE_LONGSWORD(4, 24, 48)
            IRON_LONGSWORD(3, 84, 168)
            STEEL_LONGSWORD(3, 300, 600)
            BLACK_LONGSWORD(2, 576, 1152)
            MITHRIL_LONGSWORD(2, 780, 1560)
            ADAMANT_LONGSWORD(1, 1920, 3840)
            BRONZE_DAGGER(10, 6, 12)
            IRON_DAGGER(6, 21, 42)
            STEEL_DAGGER(5, 75, 150)
            BLACK_DAGGER(4, 144, 288)
            MITHRIL_DAGGER(3, 195, 390)
            ADAMANT_DAGGER(2, 480, 960)
            BRONZE_MACE(5, 10, 21)
            IRON_MACE(4, 37, 75)
            STEEL_MACE(4, 135, 270)
            MITHRIL_MACE(3, 351, 702)
            ADAMANT_MACE(2, 864, 1728)
            BRONZE_2H_SWORD(4, 48, 96)
            IRON_2H_SWORD(3, 168, 336)
            STEEL_2H_SWORD(2, 600, 1200)
            BLACK_2H_SWORD(1, 1152, 2304)
            MITHRIL_2H_SWORD(1, 1560, 3120)
            ADAMANT_2H_SWORD(1, 3840, 7680)
            BRONZE_CHAINBODY(5, 36, 72)
            IRON_CHAINBODY(3, 126, 252)
            STEEL_CHAINBODY(3, 450, 900)
            BRONZE_MED_HELM(5, 14, 28)
            IRON_MED_HELM(3, 50, 100)
            STEEL_MED_HELM(3, 180, 360)
        }
    }
}