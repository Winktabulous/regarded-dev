package com.near_reality.game.world.entity.player.container.impl

import com.near_reality.game.world.entity.player.ironGroupTradeAddItemCheck
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.Trade

/**
 * Check whether the [player] can add the [item] to this [Trade].
 *
 * @return `true` if the item can be added, `false` otherwise.
 */
fun Trade.canAddItem(player: Player, item: Item): Boolean
    = player.ironGroupTradeAddItemCheck?.invoke(this, item)?:true
