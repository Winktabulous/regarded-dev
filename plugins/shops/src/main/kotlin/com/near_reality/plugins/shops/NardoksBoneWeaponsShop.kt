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

class NardoksBoneWeaponsShop : ShopScript() {
    init {
        "Nardok's Bone Weapons"(145, ShopCurrency.COINS, STOCK_ONLY) {
            BONE_CLUB(10, 360, 600)
            BONE_SPEAR(10, 360, 600)
            BONE_DAGGER(5, 1200, 2000)
            DORGESHUUN_CROSSBOW(5, 1200, 2000)
            BONE_BOLTS(1000, 1, 3)
            BONE_BOLT_PACK(80, 210, 350)
        }
    }
}