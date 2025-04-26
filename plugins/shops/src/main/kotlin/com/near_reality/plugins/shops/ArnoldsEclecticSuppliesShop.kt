package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.BEER
import com.zenyte.game.item.ItemId.BREAD
import com.zenyte.game.item.ItemId.BUCKET_OF_MILK
import com.zenyte.game.item.ItemId.GLASSBLOWING_PIPE
import com.zenyte.game.item.ItemId.HARPOON
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.MONKFISH
import com.zenyte.game.item.ItemId.NEEDLE
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.RAW_MONKFISH
import com.zenyte.game.item.ItemId.SMALL_FISHING_NET
import com.zenyte.game.item.ItemId.THREAD
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class ArnoldsEclecticSuppliesShop : ShopScript() {
    init {
        "Arnold's Eclectic Supplies"(86, ShopCurrency.COINS, STOCK_ONLY) {
            SMALL_FISHING_NET(2, 2, 5)
            HARPOON(2, 2, 5)
            RAW_MONKFISH(1, 126, 241)
            MONKFISH(0, 126, 241)
            BREAD(1, 6, 12)
            POT(4, 0, 1)
            BUCKET_OF_MILK(1, 4, 6)
            NEEDLE(3, 0, 1)
            THREAD(15, 0, 1)
            BEER(10, 1, 2)
            GLASSBLOWING_PIPE(2, 1, 2)
            KNIFE(1, 3, 6)
        }
    }
}