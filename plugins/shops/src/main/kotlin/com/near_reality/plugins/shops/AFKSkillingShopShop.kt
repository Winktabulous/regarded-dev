package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.item.ItemId.CRYSTAL_KEY
import com.zenyte.game.item.ItemId.ECLECTIC_IMPLING_JAR
import com.zenyte.game.item.ItemId.GRICOLLERS_CAN
import com.zenyte.game.item.ItemId.IMCANDO_HAMMER
import com.zenyte.game.model.shop.ShopCurrency.AFK_POINTS
import com.zenyte.game.model.shop.ShopPolicy.NO_SELLING

class AFKSkillingShopShop : ShopScript() {
	init {
		"AFK Skilling Shop"(292, AFK_POINTS, NO_SELLING) {
			32240(1000, -1, 756000)
			IMCANDO_HAMMER(100, -1, 220000)
			GRICOLLERS_CAN(100, -1, 110000)
			12019(100, -1, 110000) // coal bag
			3050(1000, -1, 225)
			6694(1000, -1, 325)
			1516(1000, -1, 110)
			1618(1000, -1, 180)
			2354(1000, -1, 150)
			22936(1000, -1, 225)
			//SEED_PACK(1000, -1, 11250)
			1938(1000, -1, 110)
			1988(1000, -1, 110)
			ECLECTIC_IMPLING_JAR(1000, -1, 1000)
			CRYSTAL_KEY(1000, -1, 3000)
		}
	}
}
