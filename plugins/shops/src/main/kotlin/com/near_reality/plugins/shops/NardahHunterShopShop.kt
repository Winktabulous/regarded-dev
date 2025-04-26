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

class NardahHunterShopShop : ShopScript() {
    init {
        "Nardah Hunter Shop"(134, ShopCurrency.COINS, STOCK_ONLY) {
            BUTTERFLY_NET(5, 14, 24)
            BUTTERFLY_JAR(100, 0, 1)
            MAGIC_BOX(30, 420, 720)
            NOOSE_WAND(50, 2, 4)
            BIRD_SNARE(50, 3, 6)
            BOX_TRAP(25, 22, 38)
            TEASING_STICK(5, 35, 60)
            UNLIT_TORCH(20, 2, 4)
            RABBIT_SNARE(10, 10, 18)
            BIRD_SNARE_PACK(3, 353, 606)
            BOX_TRAP_PACK(3, 2240, 3840)
            MAGIC_IMP_BOX_PACK(3, 42000, 72000)
        }
    }
}