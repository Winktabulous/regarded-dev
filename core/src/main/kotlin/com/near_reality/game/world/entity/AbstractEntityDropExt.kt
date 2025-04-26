package com.near_reality.game.world.entity

import com.zenyte.game.world.entity.AbstractEntity
import com.zenyte.game.world.entity.ReceivedDamage
import com.zenyte.game.world.entity.player.privilege.GameMode
import it.unimi.dsi.fastutil.objects.ObjectArrayList

fun AbstractEntity.sortedReceivedDamage(): List<MutableMap.MutableEntry<Pair<String, GameMode>, ObjectArrayList<ReceivedDamage>>> {
    return receivedDamage.entries.sortedByDescending { it.value.sumOf { pair -> pair.damage } }
}