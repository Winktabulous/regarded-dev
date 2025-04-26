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

class RasolotheWanderingMerchantShop : ShopScript() {
    init {
        "Rasolo the Wandering Merchant"(100, ShopCurrency.COINS, STOCK_ONLY) {
            SPINACH_ROLL(1, 0, 2)
            COCKTAIL_GUIDE(1, 0, 4)
            BLUE_HAT(1, 16, 320)
            DRAGON_DAGGER(1, 3000, 60000)
            NEWCOMER_MAP(1, 0, 2)
            BAILING_BUCKET(1, 1, 20)
            SWAMP_PASTE(1, 3, 60)
            POISON(1, 0, 2)
            PAPYRUS(1, 1, 20)
            MACHETE(1, 4, 80)
            HOLY_MOULD(1, 0, 10)
            SICKLE_MOULD(1, 1, 20)
            WATERSKIN4(1, 3, 60)
            DESERT_BOOTS(1, 2, 40)
            SHANTAY_PASS(1, 0, 10)
            BLACK_TOY_HORSEY(1, 10, 200)
            SAMPLE_BOTTLE(1, 0, 10)
            GREY_BOOTS(1, 50, 1000)
            GREENMANS_ALE(1, 0, 4)
            FREMENNIK_PINK_CLOAK(1, 25, 500)
            KEG_OF_BEER(1, 25, 500)
            FLAMTAER_HAMMER(1, 1000, 20000)
            OLIVE_OIL3(1, 2, 40)
            LIMESTONE_BRICK(1, 2, 40)
            INSTRUCTION_MANUAL(1, 0, 20)
        }
    }
}