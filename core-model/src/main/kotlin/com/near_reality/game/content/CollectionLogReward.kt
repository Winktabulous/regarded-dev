package com.near_reality.game.content

import com.zenyte.game.item._Item

data class CollectionLogReward(
    val struct: Int,
    val rewards: Array<_Item>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CollectionLogReward

        if (struct != other.struct) return false
        return rewards.contentEquals(other.rewards)
    }

    override fun hashCode(): Int {
        var result = struct
        result = 31 * result + rewards.contentHashCode()
        return result
    }

    fun toSet(): CollectionLogRewardSet =
        CollectionLogRewardSet(
            rewards.getOrNull(0)?.id ?: -1, rewards.getOrNull(0)?.amount ?: -1,
            rewards.getOrNull(1)?.id ?: -1, rewards.getOrNull(1)?.amount ?: -1,
            rewards.getOrNull(2)?.id ?: -1, rewards.getOrNull(2)?.amount ?: -1,
            rewards.getOrNull(3)?.id ?: -1, rewards.getOrNull(3)?.amount ?: -1
        )
}