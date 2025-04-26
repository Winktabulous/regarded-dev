package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.PIRATE_BANDANA
import com.zenyte.game.item.ItemId.PIRATE_BANDANA_7124
import com.zenyte.game.item.ItemId.PIRATE_BANDANA_7130
import com.zenyte.game.item.ItemId.PIRATE_BANDANA_7136
import com.zenyte.game.item.ItemId.PIRATE_BOOTS
import com.zenyte.game.item.ItemId.PIRATE_LEGGINGS
import com.zenyte.game.item.ItemId.PIRATE_LEGGINGS_7126
import com.zenyte.game.item.ItemId.PIRATE_LEGGINGS_7132
import com.zenyte.game.item.ItemId.PIRATE_LEGGINGS_7138
import com.zenyte.game.item.ItemId.STRIPY_PIRATE_SHIRT
import com.zenyte.game.item.ItemId.STRIPY_PIRATE_SHIRT_7122
import com.zenyte.game.item.ItemId.STRIPY_PIRATE_SHIRT_7128
import com.zenyte.game.item.ItemId.STRIPY_PIRATE_SHIRT_7134
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class DodgyMikesSecondhandClothingShop : ShopScript() {
    init {
        "Dodgy Mike's Second-hand Clothing"(229, ShopCurrency.COINS, STOCK_ONLY) {
            PIRATE_BOOTS(15, 210, 350)
            STRIPY_PIRATE_SHIRT(10, 180, 300)
            PIRATE_BANDANA(20, 60, 100)
            PIRATE_LEGGINGS(10, 210, 350)
            STRIPY_PIRATE_SHIRT_7122(10, 180, 300)
            PIRATE_BANDANA_7124(20, 60, 100)
            PIRATE_LEGGINGS_7126(10, 210, 350)
            STRIPY_PIRATE_SHIRT_7128(10, 180, 300)
            PIRATE_BANDANA_7130(20, 60, 100)
            PIRATE_LEGGINGS_7132(10, 210, 350)
            STRIPY_PIRATE_SHIRT_7134(10, 180, 300)
            PIRATE_BANDANA_7136(20, 60, 100)
            PIRATE_LEGGINGS_7138(10, 210, 350)
        }
    }
}