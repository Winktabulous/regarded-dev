package com.near_reality.api.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
sealed class Log {
    abstract val time : Instant
}

@Serializable
data class TradeLog(
    override val time: Instant = Clock.System.now(),
    val user1: String,
    val user2: String,
    val items1: SlotItemMap,
    val items2: SlotItemMap,
    val location: Location,
) : Log()

@Serializable
data class SanctionLog(
    override val time: Instant = Clock.System.now(),
    val reporter: String,
    val offender: String,
    val reason: String,
    val expiresAt: Instant?,
    val kind: SanctionType
) : Log()


@Serializable
data class LoginLog(
    override val time: Instant = Clock.System.now(),
    val username: String,
    val ip: String,
) : Log()

@Serializable
data class LogoutLog(
    override val time: Instant = Clock.System.now(),
    val username: String,
    val ip: String
) : Log()

@Serializable
data class KilledByPlayerLog(
    override val time: Instant = Clock.System.now(),
    val killer: String,
    val victim: String,
    val location: Location,
    val deathResult: DeathResult
) : Log()

@Serializable
data class KilledByNpcLog(
    override val time: Instant = Clock.System.now(),
    val killer: String,
    val killerId: Int,
    val victim: String,
    val location: Location,
    val deathResult: DeathResult
) : Log()

@Serializable
data class MiscDeathLog(
    override val time: Instant = Clock.System.now(),
    val username: String,
    val location: Location,
    val deathResult: DeathResult
) : Log()


@Serializable
data class CommandLog(
    override val time: Instant = Clock.System.now(),
    val username: String,
    val commandName: String,
    val commandParameters: List<String> = emptyList()
) : Log()

@Serializable
data class PickupItemLog(
    override val time: Instant = Clock.System.now(),
    val username: String,
    val item: Item,
    val location: Location
) : Log()

@Serializable
data class DropItemLog(
    override val time: Instant = Clock.System.now(),
    val username: String,
    val item: Item,
    val location: Location
) : Log()

@Serializable
data class MiddleManLog(
    override val time: Instant = Clock.System.now(),
    val requester: String,
    val accepter: String,
    val middleman: String,
    val requesterDonatorPin: Item,
    val accepterItems: SlotItemMap,
    val accepterOSRSMillions: Int,
) : Log()

@Serializable
data class FlowerPokerSessionLog(
    override val time: Instant = Clock.System.now(),
    val player1: String,
    val player2: String,
    val player1Items: SlotItemMap,
    val player2Items: SlotItemMap,
    val winner: String
) : Log()

@Serializable
data class GrandExchangeOfferLog(
    override val time: Instant = Clock.System.now(),
    val creator: String,
    val offerItem: Item,
    val offerPrice: Int,
    val offerType: GrandExchangeOfferType,
) : Log()

@Serializable
data class GrandExchangeTransactionLog(
    override val time: Instant = Clock.System.now(),
    val creator : String,
    val accepter: String,
    val offerItem: Item,
    val offerPrice: Int,
    val offerType: GrandExchangeOfferType,
) : Log()

@Serializable
data class DuelLog(
    override val time: Instant = Clock.System.now(),
    val winner: String,
    val player1: String,
    val player2: String,
    val player1Items: SlotItemMap,
    val player2Items: SlotItemMap,
) : Log()

@Serializable
data class ClanMessageLog(
    override val time: Instant = Clock.System.now(),
    val sender: String,
    val message: String,
    val channelName: String,
    val channelOwner: String
) : Log()

@Serializable
data class PrivateMessageLog(
    override val time: Instant = Clock.System.now(),
    val sender: String,
    val receiver: String,
    val message: String
) : Log()

@Serializable
data class PublicMessageLog(
    override val time: Instant = Clock.System.now(),
    val sender: String,
    val message: String
) : Log()

@Serializable
data class RareDropLog(
    override val time: Instant = Clock.System.now(),
    val username: String,
    val item: Item,
    val itemSource: String,
) : Log()

@Serializable
data class CreditStoreCheckoutLog(
    override val time: Instant = Clock.System.now(),
    val username: String,
    val cart: List<CreditStoreCartItem>
) : Log()

@Serializable
data class RemnantExchangeLog(
    override val time: Instant = Clock.System.now(),
    val username: String,
    val item: Item,
    val value: Int, // per item
) : Log()
