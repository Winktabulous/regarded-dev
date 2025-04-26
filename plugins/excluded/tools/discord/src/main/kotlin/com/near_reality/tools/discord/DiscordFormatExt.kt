package com.near_reality.tools.discord

import com.near_reality.tools.logging.GameLogMessage
import com.zenyte.game.item.Item
import com.zenyte.game.item._Item
import com.zenyte.game.world.entity._Location
import com.zenyte.utils.TextUtils
import dev.kord.rest.builder.message.EmbedBuilder
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import mgi.types.config.items.ItemDefinitions

fun _Location.stringify() =
    "($x, $y, $plane)"

fun Item.stringify() =
    "${TextUtils.formatCurrency(amount)}x **${name}**"

fun _Item.stringify() =
    "${TextUtils.formatCurrency(amount)}x **${ItemDefinitions.nameOf(id)}**"

fun Int2ObjectLinkedOpenHashMap<out _Item>.stringify() =
    values.stringify()

fun Collection<_Item>.stringify() =
    if (isEmpty()) "None" else map { Item(it) }.groupBy { it.definitions.unnotedOrDefault }
        .mapValues { Item(it.key, it.value.sumOf { it.amount }) }
        .values
        .joinToString { it.stringify() }

fun EmbedBuilder.addItems(items: Int2ObjectLinkedOpenHashMap<out _Item>?, name: String) =
    addItems(items?.values, name)

fun EmbedBuilder.addItems(items: Collection<_Item>?, name: String) {
    if (!items.isNullOrEmpty())
        field(name, inline = false) { items.stringify() }
}

fun EmbedBuilder.addBiItems(m: GameLogMessage.ItemContainerWithOther) {
    val (p1, p2) = if (m is GameLogMessage.PlayerWithOther) {
        m.username to m.otherUsername
    } else "p1" to "p2"
    field("${p1}_items", inline = false) { m.items.stringify() }
    field("${p2}_items", inline = false) { m.otherItems.stringify() }
}
