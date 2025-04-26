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

class HomeHerbloreShopShop : ShopScript() {
    init {
        "Herblore Store"(ShopCurrency.COINS, STOCK_ONLY) {
            PESTLE_AND_MORTAR(100, 1, 5)
            EMPTY_VIAL_PACK(10000, 80, 260)
            WATERFILLED_VIAL_PACK(10000, 100, 301)
            222(10000, 1, 3) // eye of newt
            226(10000, 0, 2500, ironmanRestricted = true) // limpwurt root
            238(10000, 0, 1000, ironmanRestricted = true) // unicorn horn
            236(10000, 0, 1500, ironmanRestricted = true) // unicorn horn dust
            593(10000, 0, 100, ironmanRestricted = true) // ashes
            224(10000, 0, 5000, ironmanRestricted = true) // red spiders eggs
            1974(10000, 0, 750, ironmanRestricted = true) // chocolate
            1976(10000, 0, 1250, ironmanRestricted = true) // chocolate dust
            240(10000, 0, 750, ironmanRestricted = true) // white berries
            2153(10000, 0, 1000, ironmanRestricted = true) // toad's legs
            9738(10000, 0, 1250, ironmanRestricted = true) // goat horn
            9737(10000, 0, 1750, ironmanRestricted = true) // goat horn dust
            232(10000, 0, 4000, ironmanRestricted = true) // snape grass
            2971(10000, 0, 500, ironmanRestricted = true) // mort myre
            244(10000, 0, 1250, ironmanRestricted = true) // blue dragon scale
            242(10000, 0, 1750, ironmanRestricted = true) // blue dragon scale dust
            2680(10000, 0, 7500, ironmanRestricted = true) // snake weed
            10938(10000, 0, 5000, ironmanRestricted = true) // nail beast nails
            246(10000, 0, 4000, ironmanRestricted = true) // wine of zamorak
            248(10000, 0, 3000, ironmanRestricted = true) // jangerberries
            3139(10000, 0, 2500, ironmanRestricted = true) // potato cactus
            6694(10000, 0, 7500, ironmanRestricted = true) // crushed nest
        }
    }
}