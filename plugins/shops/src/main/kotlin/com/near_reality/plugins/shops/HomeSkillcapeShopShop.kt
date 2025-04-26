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

class HomeSkillcapeShopShop : ShopScript() {
    init {
        "Accomplishment Cape Shop"(1010, ShopCurrency.COINS, NO_SELLING) {
            ATTACK_CAPET(100, 59400, 99000)
            STRENGTH_CAPET(100, 59400, 99000)
            DEFENCE_CAPET(100, 59400, 99000)
            RANGING_CAPET(100, 59400, 99000)
            PRAYER_CAPET(100, 59400, 99000)
            MAGIC_CAPET(100, 59400, 99000)
            RUNECRAFT_CAPET(100, 59400, 99000)
            HITPOINTS_CAPET(100, 59400, 99000)
            AGILITY_CAPET(100, 59400, 99000)
            HERBLORE_CAPET(100, 59400, 99000)
            THIEVING_CAPET(100, 59400, 99000)
            CRAFTING_CAPET(100, 59400, 99000)
            FLETCHING_CAPET(100, 59400, 99000)
            SLAYER_CAPET(100, 59400, 99000)
            CONSTRUCT_CAPET(100, 59400, 99000)
            MINING_CAPET(100, 59400, 99000)
            SMITHING_CAPET(100, 59400, 99000)
            FISHING_CAPET(100, 59400, 99000)
            COOKING_CAPET(100, 59400, 99000)
            FIREMAKING_CAPET(100, 59400, 99000)
            WOODCUT_CAPET(100, 59400, 99000)
            FARMING_CAPET(100, 59400, 99000)
            HUNTER_CAPET(100, 59400, 99000)
            ACHIEVEMENT_DIARY_CAPE(100, 59400, 99000)
        }
    }
}