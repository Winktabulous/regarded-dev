package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.ADAMANT_2H_SWORD
import com.zenyte.game.item.ItemId.ADAMANT_DAGGER
import com.zenyte.game.item.ItemId.ADAMANT_LONGSWORD
import com.zenyte.game.item.ItemId.ADAMANT_MACE
import com.zenyte.game.item.ItemId.ADAMANT_SWORD
import com.zenyte.game.item.ItemId.BLACK_2H_SWORD
import com.zenyte.game.item.ItemId.BLACK_DAGGER
import com.zenyte.game.item.ItemId.BLACK_LONGSWORD
import com.zenyte.game.item.ItemId.BLACK_SCIMITAR
import com.zenyte.game.item.ItemId.BLACK_SWORD
import com.zenyte.game.item.ItemId.BRONZE_2H_SWORD
import com.zenyte.game.item.ItemId.BRONZE_DAGGER
import com.zenyte.game.item.ItemId.BRONZE_LONGSWORD
import com.zenyte.game.item.ItemId.BRONZE_MACE
import com.zenyte.game.item.ItemId.BRONZE_SCIMITAR
import com.zenyte.game.item.ItemId.BRONZE_SWORD
import com.zenyte.game.item.ItemId.IRON_2H_SWORD
import com.zenyte.game.item.ItemId.IRON_BATTLEAXE
import com.zenyte.game.item.ItemId.IRON_DAGGER
import com.zenyte.game.item.ItemId.IRON_LONGSWORD
import com.zenyte.game.item.ItemId.IRON_MACE
import com.zenyte.game.item.ItemId.IRON_SCIMITAR
import com.zenyte.game.item.ItemId.IRON_SWORD
import com.zenyte.game.item.ItemId.MITHRIL_2H_SWORD
import com.zenyte.game.item.ItemId.MITHRIL_BATTLEAXE
import com.zenyte.game.item.ItemId.MITHRIL_DAGGER
import com.zenyte.game.item.ItemId.MITHRIL_LONGSWORD
import com.zenyte.game.item.ItemId.MITHRIL_MACE
import com.zenyte.game.item.ItemId.MITHRIL_SCIMITAR
import com.zenyte.game.item.ItemId.MITHRIL_SWORD
import com.zenyte.game.item.ItemId.STEEL_2H_SWORD
import com.zenyte.game.item.ItemId.STEEL_BATTLEAXE
import com.zenyte.game.item.ItemId.STEEL_DAGGER
import com.zenyte.game.item.ItemId.STEEL_LONGSWORD
import com.zenyte.game.item.ItemId.STEEL_MACE
import com.zenyte.game.item.ItemId.STEEL_SCIMITAR
import com.zenyte.game.item.ItemId.STEEL_SWORD
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class BrigetsWeaponsShop : ShopScript() {
    init {
        "Briget's Weapons"(219, ShopCurrency.COINS, STOCK_ONLY) {
            IRON_BATTLEAXE(5, -1, 182)
            STEEL_BATTLEAXE(2, -1, 650)
            MITHRIL_BATTLEAXE(1, -1, 1690)
            BRONZE_SWORD(5, -1, 26)
            IRON_SWORD(4, -1, 91)
            STEEL_SWORD(4, -1, 325)
            BLACK_SWORD(3, -1, 624)
            MITHRIL_SWORD(3, -1, 845)
            ADAMANT_SWORD(2, -1, 2080)
            BRONZE_LONGSWORD(4, -1, 40)
            IRON_LONGSWORD(3, -1, 140)
            STEEL_LONGSWORD(3, -1, 500)
            BLACK_LONGSWORD(2, -1, 960)
            MITHRIL_LONGSWORD(2, -1, 1300)
            ADAMANT_LONGSWORD(1, -1, 3200)
            BRONZE_DAGGER(10, -1, 10)
            IRON_DAGGER(6, -1, 35)
            STEEL_DAGGER(5, -1, 125)
            BLACK_DAGGER(4, -1, 240)
            MITHRIL_DAGGER(3, -1, 325)
            ADAMANT_DAGGER(2, -1, 800)
            BRONZE_MACE(5, -1, 18)
            IRON_MACE(4, -1, 63)
            STEEL_MACE(4, -1, 225)
            MITHRIL_MACE(3, -1, 585)
            ADAMANT_MACE(2, -1, 1440)
            BRONZE_2H_SWORD(4, -1, 80)
            IRON_2H_SWORD(3, -1, 280)
            STEEL_2H_SWORD(2, -1, 1000)
            BLACK_2H_SWORD(1, -1, 1920)
            MITHRIL_2H_SWORD(1, -1, 2600)
            ADAMANT_2H_SWORD(1, -1, 6400)
            BRONZE_SCIMITAR(5, -1, 32)
            IRON_SCIMITAR(4, -1, 112)
            STEEL_SCIMITAR(4, -1, 400)
            BLACK_SCIMITAR(3, -1, 768)
            MITHRIL_SCIMITAR(3, -1, 1040)
        }
    }
}