package com.near_reality.game.content.wilderness.revenant.npc.drop;

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.ImmutableItem
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.Player

enum class GoodRevenantDrop(val range: IntRange, open val item: ImmutableItem) {
        REVENANT_WEAPON(IntRange(0, 2), ImmutableItem(22557, 1, 1)) {
            override val item: ImmutableItem
                get() {
                    val chance = Utils.random(4)
                    return when (chance) {
                        0 -> ImmutableItem(22542, 1, 1)
                        1 -> ImmutableItem(22547, 1, 1)
                        2 -> ImmutableItem(22552, 1, 1)
                        else -> super.item
                    }
                }
        },
        ANCIENT_RELIC(IntRange(3, 3), ImmutableItem(22305, 1, 1)),
        ANCIENT_EFFIGY(IntRange(4, 4), ImmutableItem(22302, 1, 1)),
        ANCIENT_MEDALLION(IntRange(5, 6), ImmutableItem(22299, 1, 1)),
        ANCIENT_STATUETTE(IntRange(7, 10), ImmutableItem(21813, 1, 1)),
        MAGIC_SEEDS(IntRange(11, 12), ImmutableItem(5316, 5, 9)),
        ANCIENT_CRYSTAL(IntRange(13, 15), ImmutableItem(21804, 1, 1)),
        ANCIENT_TOTEM(IntRange(16, 20), ImmutableItem(21810, 1, 1)),
        ANCIENT_EMBLEM(IntRange(21, 26), ImmutableItem(21807, 1, 1)),
        DRAGON_MED_HELM(IntRange(27, 39), ImmutableItem(1149, 1, 1));

        companion object {
            private val values = entries.toTypedArray()
            fun get(player: Player): Item {
                val random = Utils.random(if (player.variables.isSkulled) 13 else 39)
                for (value in values) {
                    if (random >= value.range.first && random <= value.range.last) {
                        val item = value.item
                        return Item(item.id, Utils.random(item.minAmount, item.maxAmount))
                    }
                }
                throw IllegalStateException()
            }
        }
    }
