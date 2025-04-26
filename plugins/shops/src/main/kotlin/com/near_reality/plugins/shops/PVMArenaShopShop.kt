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

class PVMArenaShopShop : ShopScript() {
    init {
        "PVM Arena Shop"(370, PVM_ARENA_POINTS, NO_SELLING) {
            PVM_MYSTERY_BOX(1_000_000, sellPrice = (-1), buyPrice = 2_000)
            SIR_ELDRICS_BOOST_SCROLL(1_000_000, sellPrice = (-1), buyPrice = 2_000)
            WORLD_BOOST_TOKEN(1_000_000, sellPrice = (-1), buyPrice = 8_000)
            DWARF_CANNON_SET(1_000_000, sellPrice = (-1), buyPrice = 1_000)
            CANNONBALL_PACK(1_000_000, sellPrice = (-1), buyPrice = 125)
            GRANITE_CANNONBALL_PACK(1_000_000, sellPrice = (-1), buyPrice = 200)
            CRYSTAL_KEY(1_000_000, sellPrice = (-1), buyPrice = 200)
            ENHANCED_CRYSTAL_KEY(1_000_000, sellPrice = (-1), buyPrice = 350)
            HERB_BOX(1_000_000, sellPrice = (-1), buyPrice = 55)
            XERICS_WISDOM(1_000_000, sellPrice = (-1), buyPrice = 1_800)
            TOB_BOOSTER(1_000_000, sellPrice = (-1), buyPrice = 1_500)
            DRAGON_DEFENDER(1_000_000, sellPrice = (-1), buyPrice = 1_000)
            RUNE_POUCH(1_000_000, sellPrice = (-1), buyPrice = 750)
            BAG_FULL_OF_GEMS(1_000_000, sellPrice = (-1), buyPrice = 150)
            PET_BOOSTER(1_000_000, sellPrice = (-1), buyPrice = 1_000)
            SLAYER_BOOSTER(1_000_000, sellPrice = (-1), buyPrice = 1_000)
            GANODERMIC_BOOSTER(1_000_000, sellPrice = (-1), buyPrice = 1_200)
            GAUNTLET_BOOSTER(1_000_000, sellPrice = (-1), buyPrice = 1_500)
            LARRANS_KEY_BOOSTER(1_000_000, sellPrice = (-1), buyPrice = 850)
            SHERLOCKS_NOTES(1_000_000, sellPrice = (-1), buyPrice = 250)
            NEX_BOOSTER(1_000_000, sellPrice = (-1), buyPrice = 1_800)
        }
    }
}