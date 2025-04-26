package com.near_reality.plugins.shops

import com.near_reality.game.item.CustomItemId

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

class HomeVoteShopShop : ShopScript() {
    init {
        "Vote Shop"(1009, VOTE_POINTS, STOCK_ONLY) {
            UnivShopItem(CustomItemId.NR_VOTE_SHARD, buyPrice = 1, sellPrice = 1).invoke()
            UnivShopItem(30210, buyPrice=1).invoke()
            UnivShopItem(30215, buyPrice=1).invoke()
            UnivShopItem(6199, buyPrice=6, ironmanRestricted = true).invoke()
            UnivShopItem(30031, buyPrice=35).invoke()
            UnivShopItem(20754, buyPrice=1).invoke()
            UnivShopItem(22374, buyPrice=1).invoke()
            UnivShopItem(2710, buyPrice=2).invoke()
            UnivShopItem(19782, buyPrice=5).invoke()
            UnivShopItem(32157, buyPrice=2).invoke()
            UnivShopItem(32158, buyPrice=6).invoke()
            UnivShopItem(32166, buyPrice=3).invoke()
            UnivShopItem(32156, buyPrice=10).invoke()
            UnivShopItem(32154, buyPrice=3).invoke()
            UnivShopItem(32151, buyPrice=3).invoke()
            UnivShopItem(32167, buyPrice=10).invoke()
            UnivShopItem(32153, buyPrice=8).invoke()
            UnivShopItem(32152, buyPrice=5).invoke()
            UnivShopItem(32150, buyPrice=5).invoke()
            UnivShopItem(32149, buyPrice=5).invoke()
            UnivShopItem(32155, buyPrice=8).invoke()
            UnivShopItem(10551, buyPrice=12).invoke()
            UnivShopItem(6570, buyPrice=10).invoke()
            UnivShopItem(8850, buyPrice=3).invoke()
            UnivShopItem(12954, buyPrice=15).invoke()
            UnivShopItem(32159, buyPrice=4).invoke()
            UnivShopItem(19677, buyPrice=3).invoke()
            UnivShopItem(19634, buyPrice=20).invoke()
            UnivShopItem(25781, buyPrice=10).invoke()
            UnivShopItem(12019, buyPrice=10).invoke()
            UnivShopItem(11200, buyPrice=20).invoke()
            UnivShopItem(13226, buyPrice=8).invoke()
            UnivShopItem(13639, buyPrice=8).invoke()
            UnivShopItem(12020, buyPrice=8).invoke()
            UnivShopItem(12791, buyPrice=10).invoke()
            UnivShopItem(11738, buyPrice=1).invoke()
            UnivShopItem(10859, buyPrice=1).invoke()
            UnivShopItem(7409, buyPrice=3).invoke()
            UnivShopItem(4251, buyPrice=3).invoke()
            UnivShopItem(11941, buyPrice=2).invoke()
            UnivShopItem(5509, buyPrice=3).invoke()
            UnivShopItem(5510, buyPrice=3).invoke()
            UnivShopItem(5512, buyPrice=3).invoke()
            UnivShopItem(5514, buyPrice=3).invoke()
            UnivShopItem(13353, buyPrice=6).invoke()
            UnivShopItem(1580, buyPrice=6).invoke()
            UnivShopItem(775, buyPrice=6).invoke()
            UnivShopItem(776, buyPrice=6).invoke()
            UnivShopItem(777, buyPrice=6).invoke()
            UnivShopItem(11896, buyPrice=15).invoke()
            UnivShopItem(11897, buyPrice=15).invoke()
            UnivShopItem(11898, buyPrice=15).invoke()
            UnivShopItem(11899, buyPrice=20).invoke()
            UnivShopItem(11900, buyPrice=20).invoke()
            UnivShopItem(11901, buyPrice=20).invoke()
            UnivShopItem(9472, buyPrice=3).invoke()
            UnivShopItem(751, buyPrice=6).invoke()
            UnivShopItem(9470, buyPrice=27).invoke()
            UnivShopItem(12337, buyPrice=20).invoke()
            UnivShopItem(6665, buyPrice=5).invoke()
            UnivShopItem(9944, buyPrice=3).invoke()
            UnivShopItem(9945, buyPrice=3).invoke()
            UnivShopItem(12638, buyPrice=6).invoke()
            UnivShopItem(12637, buyPrice=6).invoke()
            UnivShopItem(12639, buyPrice=6).invoke()
            UnivShopItem(19699, buyPrice=25).invoke()
            UnivShopItem(6182, buyPrice=3).invoke()
            UnivShopItem(6180, buyPrice=3).invoke()
            UnivShopItem(6181, buyPrice=3).invoke()
            UnivShopItem(21209, buyPrice=90).invoke()
            UnivShopItem(6818, buyPrice=100).invoke()
            UnivShopItem(6547, buyPrice=12).invoke()
            UnivShopItem(430, buyPrice=12).invoke()
            UnivShopItem(10863, buyPrice=10).invoke()
            UnivShopItem(10864, buyPrice=10).invoke()
            UnivShopItem(10865, buyPrice=10).invoke()
            UnivShopItem(5609, buyPrice=18).invoke()
            UnivShopItem(5608, buyPrice=18).invoke()
            UnivShopItem(13116, buyPrice = 10).invoke()
        }
    }
}