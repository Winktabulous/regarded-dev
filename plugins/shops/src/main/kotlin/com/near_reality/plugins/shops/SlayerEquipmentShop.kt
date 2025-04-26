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

class SlayerEquipmentShop : ShopScript() {
    init {
        "Slayer Equipment"(ShopCurrency.COINS, STOCK_ONLY) {
            ENCHANTED_GEM(50, 0, 1)
            MIRROR_SHIELD(50, 4000, 5000)
            LEAFBLADED_SPEAR(50, 24800, 31000)
            BROAD_ARROWS_4160(50000, 48, 60)
            BAG_OF_SALT(5000, 8, 10)
            ROCK_HAMMER(50, 400, 500)
            FACEMASK(50, 160, 200)
            EARMUFFS(50, 160, 200)
            NOSE_PEG(50, 160, 200)
            SLAYERS_STAFF(50, 16800, 21000)
            SPINY_HELMET(50, 520, 650)
            FISHING_EXPLOSIVE_6664(5000, 48, 60)
            ICE_COOLER(5000, 0, 1)
            SLAYER_GLOVES_6720(50, 160, 200)
            UNLIT_BUG_LANTERN(50, 104, 130)
            INSULATED_BOOTS(50, 160, 200)
            FUNGICIDE_SPRAY_10(50, 240, 300)
            FUNGICIDE(5000, 240, 300)
            WITCHWOOD_ICON(50, 720, 900)
            SLAYER_BELL(50, 120, 150)
            BROAD_ARROWHEADS(3000, 44, 55)
            BROAD_ARROWHEAD_PACK(800, 4400, 5500)
            UNFINISHED_BROAD_BOLTS(5000, 44, 55)
            UNFINISHED_BROAD_BOLT_PACK(1000, 4400, 5500)
            ROCK_THROWNHAMMER(5000, 160, 200)
            BOOTS_OF_STONE(100, 160, 200)
            BRACELET_OF_SLAUGHTER(100, 0, 40000, ironmanRestricted = true)
            EXPEDITIOUS_BRACELET(100, 0, 40000, ironmanRestricted = true)
        }
    }
}