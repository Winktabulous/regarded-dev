package com.near_reality.content.group_ironman

import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

/**
 * Represents a type of restriction that is applied for [ironman group members][IronmanGroupMember].
 *
 * @author Stan van der Bend
 */
sealed interface IronmanGroupRestriction {

    /**
     * Represents a trading restriction.
     *
     * @param thresholdJoinDate the minimum age of the group for this restriction to apply.
     */
    sealed class Trading(val thresholdJoinDate: Duration) : IronmanGroupRestriction {

        companion object {

            /**
             * Represents a list of all trading-restrictions applied to ranked group ironman members.
             */
            val all = listOf(
                ValueCap(1_000_000, 0.days),
                ValueCap(20_000_000, 7.days),
                ValueCap(50_000_000, 14.days),
                ValueCap(100_000_000, 21.days),
                Unlimited,
            )

            /**
             * Find the [Trading] restriction that applies to the argued [member].
             */
            fun find(member: IronmanGroupMember): Trading {
                val timeSinceJoined = Clock.System.now() - member.joinTime
                return all
                    .filter { timeSinceJoined >= it.thresholdJoinDate }
                    .maxBy { it.thresholdJoinDate }
            }
        }

        /**
         * Returns whether the [member] can make a trade worth [value].
         */
        abstract fun canTradeValue(member: IronmanGroupMember, value: Long): Boolean

        /**
         * Trades are limited to a value below or equal to [maxValue].
         */
        class ValueCap(val maxValue: Long, thresholdGroupAge: Duration) : Trading(thresholdGroupAge) {
            override fun canTradeValue(member: IronmanGroupMember, value: Long) =
                (member.valueReceived + value).coerceIn(0, Long.MAX_VALUE) <= maxValue
        }

        /**
         * Trades have no value limits.
         */
        object Unlimited : Trading(28.days) {
            override fun canTradeValue(member: IronmanGroupMember, value: Long) =
                true
        }
    }
}
