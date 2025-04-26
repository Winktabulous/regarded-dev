package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BIRD_SNARE
import com.zenyte.game.item.ItemId.BIRD_SNARE_PACK
import com.zenyte.game.item.ItemId.BOX_TRAP
import com.zenyte.game.item.ItemId.BOX_TRAP_PACK
import com.zenyte.game.item.ItemId.BUTTERFLY_JAR
import com.zenyte.game.item.ItemId.BUTTERFLY_NET
import com.zenyte.game.item.ItemId.MAGIC_BOX
import com.zenyte.game.item.ItemId.MAGIC_IMP_BOX_PACK
import com.zenyte.game.item.ItemId.NOOSE_WAND
import com.zenyte.game.item.ItemId.RABBIT_SNARE
import com.zenyte.game.item.ItemId.TEASING_STICK
import com.zenyte.game.item.ItemId.UNLIT_TORCH
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class AlecksHunterEmporiumShop : ShopScript() {
    init {
        "Aleck's Hunter Emporium"(98, ShopCurrency.COINS, STOCK_ONLY) {
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