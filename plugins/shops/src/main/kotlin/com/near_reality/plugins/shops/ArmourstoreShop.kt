package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.ADAMANT_CHAINBODY
import com.zenyte.game.item.ItemId.ADAMANT_FULL_HELM
import com.zenyte.game.item.ItemId.ADAMANT_KITESHIELD
import com.zenyte.game.item.ItemId.ADAMANT_MED_HELM
import com.zenyte.game.item.ItemId.ADAMANT_PLATEBODY
import com.zenyte.game.item.ItemId.ADAMANT_PLATELEGS
import com.zenyte.game.item.ItemId.ADAMANT_PLATESKIRT
import com.zenyte.game.item.ItemId.ADAMANT_SQ_SHIELD
import com.zenyte.game.item.ItemId.MITHRIL_CHAINBODY
import com.zenyte.game.item.ItemId.MITHRIL_FULL_HELM
import com.zenyte.game.item.ItemId.MITHRIL_KITESHIELD
import com.zenyte.game.item.ItemId.MITHRIL_MED_HELM
import com.zenyte.game.item.ItemId.MITHRIL_PLATEBODY
import com.zenyte.game.item.ItemId.MITHRIL_PLATELEGS
import com.zenyte.game.item.ItemId.MITHRIL_PLATESKIRT
import com.zenyte.game.item.ItemId.MITHRIL_SQ_SHIELD
import com.zenyte.game.item.ItemId.RUNE_CHAINBODY
import com.zenyte.game.item.ItemId.RUNE_FULL_HELM
import com.zenyte.game.item.ItemId.RUNE_KITESHIELD
import com.zenyte.game.item.ItemId.RUNE_MED_HELM
import com.zenyte.game.item.ItemId.RUNE_PLATEBODY
import com.zenyte.game.item.ItemId.RUNE_PLATELEGS
import com.zenyte.game.item.ItemId.RUNE_PLATESKIRT
import com.zenyte.game.item.ItemId.RUNE_SQ_SHIELD
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class ArmourstoreShop : ShopScript() {
    init {
        "Armour store"(184, ShopCurrency.COINS, STOCK_ONLY) {
            RUNE_MED_HELM(0, 6400, 19200)
            RUNE_FULL_HELM(0, 11733, 35200)
            RUNE_PLATEBODY(0, 21667, 65000)
            RUNE_PLATESKIRT(0, 21333, 64000)
            RUNE_SQ_SHIELD(0, 12800, 38400)
            RUNE_KITESHIELD(0, 18133, 54400)
            RUNE_CHAINBODY(0, 16667, 50000)
            RUNE_PLATELEGS(0, 21333, 64000)
            ADAMANT_MED_HELM(0, 640, 1920)
            ADAMANT_FULL_HELM(0, 1173, 3520)
            ADAMANT_PLATEBODY(0, 5546, 16640)
            ADAMANT_PLATESKIRT(0, 2133, 6400)
            ADAMANT_SQ_SHIELD(0, 1280, 3840)
            ADAMANT_KITESHIELD(0, 1813, 5440)
            ADAMANT_CHAINBODY(0, 1600, 4800)
            ADAMANT_PLATELEGS(0, 2133, 6400)
            MITHRIL_MED_HELM(0, 260, 780)
            MITHRIL_FULL_HELM(0, 476, 1430)
            MITHRIL_PLATEBODY(0, 1733, 5200)
            MITHRIL_PLATESKIRT(0, 866, 2600)
            MITHRIL_SQ_SHIELD(0, 520, 1560)
            MITHRIL_KITESHIELD(0, 736, 2210)
            MITHRIL_CHAINBODY(0, 650, 1950)
            MITHRIL_PLATELEGS(0, 866, 2600)
        }
    }
}