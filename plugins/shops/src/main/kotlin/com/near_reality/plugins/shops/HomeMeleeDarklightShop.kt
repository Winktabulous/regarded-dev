package com.near_reality.plugins.shops

import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.near_reality.scripts.shops.ShopScript

class HomeMeleeDarklightShop : ShopScript() {
    init {
        "Melee Store<Alternative>"(1001, ShopCurrency.COINS, STOCK_ONLY) {
            IRON_SCIMITAR(100, 67, 112)
            MITHRIL_SCIMITAR(100, 624, 1040)
            RUNE_SCIMITAR(100, 14088, 23480, ironmanRestricted = true)
            DARKLIGHT(100, 1012, 1688)
            SHADOW_SWORD(100, 10000, 100000)
            DRAGON_SCIMITAR(100, 75225, 125375, ironmanRestricted = true)
            DRAGON_DAGGER(100, 21135, 35225, ironmanRestricted = true)
            DRAGON_SPEAR(100, 28080, 37440, ironmanRestricted = true)
            DRAGON_LONGSWORD(100, 60000, 100000, ironmanRestricted = true)
            DRAGON_BATTLEAXE(100, 120000, 200000, ironmanRestricted = true)
            DRAGON_MACE(100, 27500, 50000, ironmanRestricted = true)
            DRAGON_HALBERD(100, 62500, 375000, ironmanRestricted = true)
            IRON_FULL_HELM(100, 91, 154)
            IRON_PLATEBODY(100, 336, 560)
            IRON_PLATELEGS(100, 168, 280)
            IRON_KITESHIELD(100, 143, 238)
            INITIATE_SALLET(2000, 2400, 6000)
            INITIATE_HAUBERK(2000, 4000, 10000)
            INITIATE_CUISSE(2000, 3200, 8000)
            PROSELYTE_SALLET(100, 3200, 8000)
            PROSELYTE_HAUBERK(100, 4800, 12000)
            PROSELYTE_CUISSE(100, 4000, 10000)
            RUNE_FULL_HELM(100, 18600, 31000, ironmanRestricted = true)
            RUNE_PLATEBODY(100, 50700, 84500, ironmanRestricted = true)
            RUNE_PLATELEGS(100, 38400, 64000, ironmanRestricted = true)
            RUNE_KITESHIELD(100, 33600, 56000, ironmanRestricted = true)
            ANTIDRAGON_SHIELD(100, 26, 2588)
            BEARHEAD(100, 1, 18795)
            MONKS_ROBE_TOP(100, 18, 24)
            MONKS_ROBE(100, 13, 18)
            CLIMBING_BOOTS(100, 1476, 2460)
            BRONZE_BOOTS(100, 102, 458, ironmanRestricted = true)
            IRON_BOOTS(100, 229, 780, ironmanRestricted = true)
            STEEL_BOOTS(100, 300, 1257, ironmanRestricted = true)
            BLACK_BOOTS(100, 584, 3580, ironmanRestricted = true)
            MITHRIL_BOOTS(100, 875, 7480, ironmanRestricted = true)
            ADAMANT_BOOTS(100, 1438, 14928, ironmanRestricted = true)
            RUNE_BOOTS(100, 2844, 28790, ironmanRestricted = true)
            AMULET_OF_STRENGTH(100, 911, 1215, ironmanRestricted = true)
            AMULET_OF_GLORY4(100, 10274, 87052, ironmanRestricted = true)
            11090(100, 0, 2430, ironmanRestricted = true)
            2570(100, 0, 2430, ironmanRestricted = true)
            2550(100, 0, 540, ironmanRestricted = true)
            11118(100, 0, 56000, ironmanRestricted = true)
            2552(100, 0, 770, ironmanRestricted = true)
            HELM_OF_NEITIZNOT(100, 48500, 85200)
            BERSERKER_HELM(100, 42000, 78000)
            WARRIOR_HELM(100, 42000, 78000)
        }
    }
}