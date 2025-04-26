package com.near_reality.api.service.store

import com.near_reality.api.model.CreditPackageOrder
import com.zenyte.game.world.entity.persistentAttribute
import com.zenyte.game.world.entity.player.Player

internal var Player.storeClaimedOrders : MutableSet<Int> by persistentAttribute("store_claimed_orders", mutableSetOf())
internal var Player.storeClaimedOrdersToMentionOnNextLogin : MutableList<CreditPackageOrder> by persistentAttribute("store_claimed_orders_to_mention_on_next_login", mutableListOf())
