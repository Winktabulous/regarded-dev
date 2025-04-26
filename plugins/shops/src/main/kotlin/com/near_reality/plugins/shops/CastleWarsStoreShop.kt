package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.CASTLEWARS_CLOAK
import com.zenyte.game.item.ItemId.CASTLEWARS_CLOAK_4516
import com.zenyte.game.item.ItemId.CASTLEWARS_HOOD
import com.zenyte.game.item.ItemId.CASTLEWARS_HOOD_4515
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_11893
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_11894
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_11895
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_11896
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_11897
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_11898
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_11899
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_11900
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_11901
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_4070
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_4504
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_4505
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_4509
import com.zenyte.game.item.ItemId.DECORATIVE_ARMOUR_4510
import com.zenyte.game.item.ItemId.DECORATIVE_HELM
import com.zenyte.game.item.ItemId.DECORATIVE_HELM_4506
import com.zenyte.game.item.ItemId.DECORATIVE_HELM_4511
import com.zenyte.game.item.ItemId.DECORATIVE_SHIELD
import com.zenyte.game.item.ItemId.DECORATIVE_SHIELD_4507
import com.zenyte.game.item.ItemId.DECORATIVE_SHIELD_4512
import com.zenyte.game.item.ItemId.DECORATIVE_SWORD
import com.zenyte.game.item.ItemId.DECORATIVE_SWORD_4503
import com.zenyte.game.item.ItemId.DECORATIVE_SWORD_4508
import com.zenyte.game.item.ItemId.DRAGON_LONGSWORD
import com.zenyte.game.item.ItemId.GUTHIX_HALO
import com.zenyte.game.item.ItemId.SARADOMIN_HALO
import com.zenyte.game.item.ItemId.ZAMORAK_HALO
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopPolicy.STOCK_ONLY

class CastleWarsStoreShop : ShopScript() {
    init {
        "Castle Wars Store"(300, ShopCurrency.CASTLE_WARS_TICKETS, STOCK_ONLY) {
            DECORATIVE_HELM(100, 0, 4)
            DECORATIVE_ARMOUR(100, 0, 8)
            DECORATIVE_SWORD(100, 0, 5)
            DECORATIVE_SHIELD(100, 0, 6)
            DECORATIVE_ARMOUR_4070(100, 0, 6)
            DECORATIVE_ARMOUR_11893(100, 0, 6)
            DECORATIVE_HELM_4506(100, 0, 40)
            DECORATIVE_ARMOUR_4504(100, 0, 80)
            DECORATIVE_SWORD_4503(100, 0, 50)
            DECORATIVE_SHIELD_4507(100, 0, 60)
            DECORATIVE_ARMOUR_4505(100, 0, 60)
            DECORATIVE_ARMOUR_11894(100, 0, 60)
            DECORATIVE_HELM_4511(100, 0, 400)
            DECORATIVE_ARMOUR_4509(100, 0, 800)
            DECORATIVE_SWORD_4508(100, 0, 500)
            DECORATIVE_SHIELD_4512(100, 0, 600)
            DECORATIVE_ARMOUR_4510(100, 0, 600)
            DECORATIVE_ARMOUR_11895(100, 0, 600)
            CASTLEWARS_HOOD(100, 0, 10)
            CASTLEWARS_CLOAK(100, 0, 10)
            CASTLEWARS_HOOD_4515(100, 0, 10)
            CASTLEWARS_CLOAK_4516(100, 0, 10)
            DRAGON_LONGSWORD(100, 0, 100)
            DRAGON_LONGSWORD(100, 0, 100)
            DECORATIVE_ARMOUR_11898(100, 0, 20)
            DECORATIVE_ARMOUR_11896(100, 0, 40)
            DECORATIVE_ARMOUR_11897(100, 0, 30)
            DECORATIVE_ARMOUR_11899(100, 0, 40)
            DECORATIVE_ARMOUR_11900(100, 0, 30)
            DECORATIVE_ARMOUR_11901(100, 0, 40)
            SARADOMIN_HALO(100, 0, 75)
            ZAMORAK_HALO(100, 0, 75)
            GUTHIX_HALO(100, 0, 75)
        }
    }
}