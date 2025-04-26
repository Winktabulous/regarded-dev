package com.near_reality.tools.logging

import com.near_reality.api.model.CreditStoreCartItem
import com.near_reality.api.model.SanctionLevel
import com.near_reality.api.model.SanctionType
import com.zenyte.game.content.grandexchange._ExchangeOffer
import com.zenyte.game.item._Item
import com.zenyte.game.world.entity._Location
import com.zenyte.game.world.entity.player.container._Container
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a [Serializable] log message for game events.
 *
 * @author Stan van der Bend
 */
@Serializable
sealed interface GameLogMessage {

    val time: Instant

    object TimeProvider {
        @JvmStatic
        fun getCurrentInstant(): Instant = Clock.System.now()
    }

    @Serializable
    data class FlowerPokerSession(
        override val time: Instant = Clock.System.now(),
        override val username: String,
        override val otherUsername: String,
        override val items: SlotItemMap,
        override val otherItems: SlotItemMap,
        val winnerUsername: String,
    ) : GameLogMessage, PlayerWithOther, ItemContainerWithOther {
        val loserUsername: String
            get() = if (username == winnerUsername) otherUsername else username
    }

    @Serializable
    data class GrandExchangeOffer(
        override val time: Instant = Clock.System.now(),
        override val username: String,
        val offer: @Contextual _ExchangeOffer<out @Contextual _Item, out @Contextual _Container<out @Contextual _Item>>,
    ) : GameLogMessage, Player


    sealed interface GrandExchangeTransaction : GameLogMessage, PlayerWithOther, ItemLog {

        /**
         * The price per [item] amount, total price paid for is [_Item.amount] * [priceEach].
         */
        val priceEach: Int

        @Serializable
        data class Purchase(
            override val time: Instant = Clock.System.now(),
            override val username: String,
            override val otherUsername: String,
            override val item: @Contextual _Item,
            override val priceEach: Int,
        ) : GrandExchangeTransaction

        @Serializable
        data class Sell(
            override val time: Instant = Clock.System.now(),
            override val username: String,
            override val otherUsername: String,
            override val item: @Contextual _Item,
            override val priceEach: Int,
        ) : GrandExchangeTransaction
    }

    @Serializable
    data class Duel(
        override val time: Instant = Clock.System.now(),
        val winnerUsername: String,
        override val username: String,
        override val otherUsername: String,
        override val items: SlotItemMap,
        override val otherItems: SlotItemMap,
    ) : GameLogMessage, PlayerWithOther, ItemContainerWithOther


    sealed interface GroundItem : GameLogMessage, Player, ItemLog, LocationLog {

        @Serializable
        data class Drop(
            override val time: Instant = Clock.System.now(),
            override val username: String,
            override val item: @Contextual _Item,
            override val location: @Contextual _Location,
        ) : GroundItem

        @Serializable
        data class Pickup(
            override val time: Instant = Clock.System.now(),
            override val username: String,
            override val item: @Contextual _Item,
            override val location: @Contextual _Location,
        ) : GroundItem
    }

    sealed interface Message : GameLogMessage, Player {
        val contents: String

        @Serializable
        data class Clan(
            override val time: Instant = Clock.System.now(),
            override val username: String,
            override val contents: String,
            val channelName: String,
            val channelOwner: String,
        ) : Message

        @Serializable
        data class Public(
            override val time: Instant = Clock.System.now(),
            override val username: String,
            override val contents: String,
        ) : Message

        @Serializable
        data class Yell(
            override val time: Instant = Clock.System.now(),
            override val username: String,
            override val contents: String,
        ) : Message

        @Serializable
        data class Private(
            override val time: Instant = Clock.System.now(),
            override val username: String,
            override val otherUsername: String,
            override val contents: String,
        ) : Message, PlayerWithOther
    }

    @Serializable
    data class RareDrop(
        override val time: Instant = Clock.System.now(),
        override val username: String,
        override val item: @Contextual _Item,
        val source: String,
    ) : GameLogMessage, Player, ItemLog

    @Serializable
    data class Login(
        override val time: Instant = Clock.System.now(),
        override val username: String,
        val ip: String
    ) : GameLogMessage, Player

    @Serializable
    data class Logout(
        override val time: Instant = Clock.System.now(),
        override val username: String,
        val ip: String
    ) : GameLogMessage, Player

    @Serializable
    data class Trade(
        override val time: Instant = Clock.System.now(),
        override val username: String,
        override val otherUsername: String,
        override val items: SlotItemMap,
        override val otherItems: SlotItemMap,
        override val location: @Contextual _Location,
    ) : GameLogMessage, PlayerWithOther, ItemContainerWithOther, LocationLog

    @Serializable
    data class RemnantExchange(
        override val time: Instant = Clock.System.now(),
        override val username: String,
        override val item: @Contextual _Item,
        val value: Int,
    ) : GameLogMessage, Player, ItemLog

    sealed interface Death : GameLogMessage, Player, LocationLog {
        val inventory: Int2ObjectLinkedOpenHashMap<out _Item>
        val equipment: Int2ObjectLinkedOpenHashMap<out _Item>
        val lootingBag: Int2ObjectLinkedOpenHashMap<out _Item>?
        val runePouch: Int2ObjectLinkedOpenHashMap<out _Item>?
        val secondaryRunePouch: Int2ObjectLinkedOpenHashMap<out _Item>?
        val kept: Int2ObjectLinkedOpenHashMap<out _Item>
        val lost: Int2ObjectLinkedOpenHashMap<out _Item>
        val graveStone: List<_Item>

        sealed interface Killed : Death {

            val lostToKiller: List<_Item>

            @Serializable
            data class ByNpc(
                override val time: Instant = Clock.System.now(),
                override val username: String,
                override val npcId: Int,
                override val location: @Contextual _Location,
                override val inventory: SlotItemMap,
                override val equipment: SlotItemMap,
                override val lootingBag: SlotItemMap?,
                override val runePouch: SlotItemMap?,
                override val secondaryRunePouch: SlotItemMap?,
                override val kept: SlotItemMap,
                override val lost: SlotItemMap,
                override val graveStone: List<@Contextual _Item>,
                override val lostToKiller: List<@Contextual _Item>,
            ) : Killed, Npc

            @Serializable
            data class ByPlayer(
                override val time: Instant = Clock.System.now(),
                override val username: String,
                override val otherUsername: String,
                override val location: @Contextual _Location,
                override val inventory: SlotItemMap,
                override val equipment: SlotItemMap,
                override val lootingBag: SlotItemMap?,
                override val runePouch: SlotItemMap?,
                override val secondaryRunePouch: SlotItemMap?,
                override val kept: SlotItemMap,
                override val lost: SlotItemMap,
                override val graveStone: List<@Contextual _Item>,
                override val lostToKiller: List<@Contextual _Item>,
            ) : Killed, PlayerWithOther
        }


        @Serializable
        data class Misc(
            override val time: Instant = Clock.System.now(),
            override val username: String,
            override val location: @Contextual _Location,
            override val inventory: SlotItemMap,
            override val equipment: SlotItemMap,
            override val lootingBag: SlotItemMap?,
            override val runePouch: SlotItemMap?,
            override val secondaryRunePouch: SlotItemMap?,
            override val kept: SlotItemMap,
            override val lost: SlotItemMap,
            override val graveStone: List<@Contextual _Item>,
        ) : Death
    }

    @Serializable
    data class Command(
        override val time: Instant = Clock.System.now(),
        override val username: String,
        val commandName: String,
        val commandParameters: List<String>
    ) : GameLogMessage, Player

    @Serializable
    data class Sanction(
        override val time: Instant = Clock.System.now(),
        override val username: String,
        override val otherUsername: String,
        @SerialName("sanctionType") // type conflicts with sealed class encoded type
        val type : SanctionType,
        val level: SanctionLevel,
        val offenderIp: String?,
        val reason: String,
        val expiresAt: Instant?
    ) : GameLogMessage, PlayerWithOther


    @Serializable
    data class ClaimedVotes(
        override val time: Instant = Clock.System.now(),
        override val username: String,
        val votesClaimed: Int,
        val votesBonus: Int
    ) : GameLogMessage, Player

    @Serializable
    data class CreditStoreCheckout(
        override val time: Instant = Clock.System.now(),
        override val username: String,
        val cart: List<CreditStoreCartItem>
    ) : GameLogMessage, Player

    @Serializable
    data class MiddleManTrade(
        override val time: Instant = Clock.System.now(),
        val requester: String,
        val accepter: String,
        val middleman: String,
        val requesterDonatorPin: @Contextual _Item,
        val accepterItems: SlotItemMap,
        val accepterOSRSMillions: Int,
    ) : GameLogMessage

    interface ItemLog {
        val item: _Item
    }

    interface LocationLog {
        val location: _Location
    }

    interface Player {
        val username: String
    }

    interface PlayerWithOther : Player {
        val otherUsername: String
    }

    interface Npc {
        val npcId: Int
    }

    interface ItemContainer {
        val items: Int2ObjectLinkedOpenHashMap<out _Item>
    }

    interface ItemContainerWithOther : ItemContainer {
        val otherItems: Int2ObjectLinkedOpenHashMap<out _Item>
    }
}
