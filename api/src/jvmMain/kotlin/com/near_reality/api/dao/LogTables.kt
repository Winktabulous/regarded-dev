package com.near_reality.api.dao

import com.near_reality.api.model.ClanMessageLog
import com.near_reality.api.model.CommandLog
import com.near_reality.api.model.CreditStoreCartItem
import com.near_reality.api.model.CreditStoreCheckoutLog
import com.near_reality.api.model.DeathResult
import com.near_reality.api.model.DropItemLog
import com.near_reality.api.model.DuelLog
import com.near_reality.api.model.FlowerPokerSessionLog
import com.near_reality.api.model.GrandExchangeOfferLog
import com.near_reality.api.model.GrandExchangeOfferType
import com.near_reality.api.model.GrandExchangeTransactionLog
import com.near_reality.api.model.Item
import com.near_reality.api.model.KilledByNpcLog
import com.near_reality.api.model.KilledByPlayerLog
import com.near_reality.api.model.LoginLog
import com.near_reality.api.model.LogoutLog
import com.near_reality.api.model.MiddleManLog
import com.near_reality.api.model.MiscDeathLog
import com.near_reality.api.model.PickupItemLog
import com.near_reality.api.model.PrivateMessageLog
import com.near_reality.api.model.PublicMessageLog
import com.near_reality.api.model.RareDropLog
import com.near_reality.api.model.RemnantExchangeLog
import com.near_reality.api.model.SanctionLog
import com.near_reality.api.model.SanctionType
import com.near_reality.api.model.TradeLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.json.jsonb
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@DetailedItemsTable(TradeLogItems::class)
object TradeLogs : LongIdTable("trade_logs") {
    val time = datetime("time").index()
    val user1 = username("username").index()
    val user2 = username("other_username").index()
    val items1 = itemContainer("items")
    val items2 = itemContainer("other_items")
    val location = location("location")
}

class TradeLogEntity(id: EntityID<Long>) : ModelEntity<TradeLog>(id) {
    companion object : LongEntityClass<TradeLogEntity>(TradeLogs) {
        fun new(log: TradeLog): TradeLogEntity {
            val logEntity = new {
                time = log.time.toLocalDateTime(defaultTimeZone)
                user1 = log.user1
                user2 = log.user2
                items1 = log.items1
                items2 = log.items2
                location = log.location
            }
            TradeLogItems.insert(logEntity.id.value, 1, log.user1, log.items1)
            TradeLogItems.insert(logEntity.id.value, 2, log.user2, log.items2)
            return logEntity
        }
    }
    var time by TradeLogs.time
    var user1 by TradeLogs.user1
    var user2 by TradeLogs.user2
    var items1 by TradeLogs.items1
    var items2 by TradeLogs.items2
    var location by TradeLogs.location

    override fun toModel() = TradeLog(
        time = time.toInstant(defaultTimeZone),
        user1 = user1,
        user2 = user2,
        items1 = items1,
        items2 = items2,
        location = location
    )
}

object SanctionLogs : LongIdTable("sanction_logs") {
    val time = datetime("time").index()
    val reporter = username("reporter").index()
    val offender = username("offender").index()
    val type = enumeration<SanctionType>("type").index()
    val reason = text("reason")
    val expiresAt = datetime("expires_at").nullable()
}

class SanctionLogEntity(id: EntityID<Long>) : ModelEntity<SanctionLog>(id) {
    companion object : LongEntityClass<SanctionLogEntity>(SanctionLogs) {
        fun new(log: SanctionLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            reporter = log.reporter
            offender = log.offender
            kind = log.kind
            reason = log.reason
            expiresAt = log.expiresAt?.toLocalDateTime(defaultTimeZone)
        }
    }
    var time by SanctionLogs.time
    var reporter by SanctionLogs.reporter
    var offender by SanctionLogs.offender
    var kind by SanctionLogs.type
    var reason by SanctionLogs.reason
    var expiresAt by SanctionLogs.expiresAt

    override fun toModel() = SanctionLog(
        time = time.toInstant(defaultTimeZone),
        reporter = reporter,
        offender = offender,
        kind = kind,
        reason = reason,
        expiresAt = expiresAt?.toInstant(defaultTimeZone)
    )
}

object LoginLogs : LongIdTable("login_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val ip = varchar("ip", 15).index()
}

class LoginLogEntity(id: EntityID<Long>) : ModelEntity<LoginLog>(id) {
    companion object : LongEntityClass<LoginLogEntity>(LoginLogs) {
        fun new(log: LoginLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            ip = log.ip
        }
    }
    var time by LoginLogs.time
    var username by LoginLogs.username
    var ip by LoginLogs.ip

    override fun toModel() = LoginLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        ip = ip
    )
}

object LogoutLogs : LongIdTable("logout_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val ip = varchar("ip", 15).index()
}

class LogoutLogEntity(id: EntityID<Long>) : ModelEntity<LogoutLog>(id) {
    companion object : LongEntityClass<LogoutLogEntity>(LogoutLogs) {
        fun new(log: LogoutLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            ip = log.ip
        }
    }
    var time by LogoutLogs.time
    var username by LogoutLogs.username
    var ip by LogoutLogs.ip

    override fun toModel() = LogoutLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        ip = ip
    )
}

@DetailedItemsTable(KilledByPlayerLogItems::class)
object KilledByPlayerLogs : LongIdTable("killed_by_player_logs") {
    val time = datetime("time").index()
    val killer = username("killer").index()
    val victim = username("victim").index()
    val location = location("location")
    val deathResult = jsonb<DeathResult>("death_result", Json)
}

class KilledByPlayerLogEntity(id: EntityID<Long>) : ModelEntity<KilledByPlayerLog>(id) {
    companion object : LongEntityClass<KilledByPlayerLogEntity>(KilledByPlayerLogs) {
        fun new(log: KilledByPlayerLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            killer = log.killer
            victim = log.victim
            location = log.location
            deathResult = log.deathResult
        }
    }
    var time by KilledByPlayerLogs.time
    var killer by KilledByPlayerLogs.killer
    var victim by KilledByPlayerLogs.victim
    var location by KilledByPlayerLogs.location
    var deathResult by KilledByPlayerLogs.deathResult

    override fun toModel() = KilledByPlayerLog(
        time = time.toInstant(defaultTimeZone),
        killer = killer,
        victim = victim,
        location = location,
        deathResult = deathResult
    )
}

@DetailedItemsTable(KilledByNpcLogItems::class)
object KilledByNpcLogs : LongIdTable("killed_by_npc_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val npc = npcName("npc").index()
    val npcId = integer("npc_id").index()
    val location = location("location")
    val deathResult = jsonb<DeathResult>("death_result", Json)
}

class KilledByNpcLogEntity(id: EntityID<Long>) : ModelEntity<KilledByNpcLog>(id) {
    companion object : LongEntityClass<KilledByNpcLogEntity>(KilledByNpcLogs) {
        fun new(log: KilledByNpcLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.victim
            npc = log.killer
            npcId = log.killerId
            location = log.location
            deathResult = log.deathResult
        }
    }
    var time by KilledByNpcLogs.time
    var username by KilledByNpcLogs.username
    var npc by KilledByNpcLogs.npc
    var npcId by KilledByNpcLogs.npcId
    var location by KilledByNpcLogs.location
    var deathResult by KilledByNpcLogs.deathResult

    override fun toModel() = KilledByNpcLog(
        time = time.toInstant(defaultTimeZone),
        victim = username,
        killer = npc,
        killerId = npcId,
        location = location,
        deathResult = deathResult
    )
}

@DetailedItemsTable(MiscDeathLogItems::class)
object MiscDeathLogs : LongIdTable("misc_death_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val location = location("location")
    val deathResult = jsonb<DeathResult>("death_result", Json)
}

class MiscDeathLogEntity(id: EntityID<Long>) : ModelEntity<MiscDeathLog>(id) {
    companion object : LongEntityClass<MiscDeathLogEntity>(MiscDeathLogs) {
        fun new(log: MiscDeathLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            location = log.location
            deathResult = log.deathResult
        }
    }
    var time by MiscDeathLogs.time
    var username by MiscDeathLogs.username
    var location by MiscDeathLogs.location
    var deathResult by MiscDeathLogs.deathResult

    override fun toModel() = MiscDeathLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        location = location,
        deathResult = deathResult
    )
}

object CommandLogs : LongIdTable("command_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val commandName = varchar("command_name", 50)
    val commandParameters = array<String>("command_parameters")
}

class CommandLogEntity(id: EntityID<Long>) : ModelEntity<CommandLog>(id) {
    companion object : LongEntityClass<CommandLogEntity>(CommandLogs) {
        fun new(log: CommandLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            commandName = log.commandName
            commandParameters = log.commandParameters
        }
    }
    var time by CommandLogs.time
    var username by CommandLogs.username
    var commandName by CommandLogs.commandName
    var commandParameters by CommandLogs.commandParameters

    override fun toModel() = CommandLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        commandName = commandName,
        commandParameters = commandParameters.toList()
    )
}

object PickupItemLogs : LongIdTable("pickup_item_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val item = item("item")
    val itemId = integer("item_id").index().default(-1)
    val location = location("location")
}

class PickupItemLogEntity(id: EntityID<Long>) : ModelEntity<PickupItemLog>(id) {
    companion object : LongEntityClass<PickupItemLogEntity>(PickupItemLogs) {
        fun new(log: PickupItemLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            item = log.item
            itemId = log.item.id
            location = log.location
        }
    }
    var time by PickupItemLogs.time
    var username by PickupItemLogs.username
    var item by PickupItemLogs.item
    var location by PickupItemLogs.location
    var itemId by PickupItemLogs.itemId

    override fun toModel() = PickupItemLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        item = item,
        location = location
    )
}

object DropItemLogs : LongIdTable("drop_item_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val item = item("item")
    val itemId = integer("item_id").index().default(-1)
    val location = location("location")
}

class DropItemLogEntity(id: EntityID<Long>) : ModelEntity<DropItemLog>(id) {
    companion object : LongEntityClass<DropItemLogEntity>(DropItemLogs) {
        fun new(log: DropItemLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            item = log.item
            itemId = log.item.id
            location = log.location
        }
    }
    var time by DropItemLogs.time
    var username by DropItemLogs.username
    var item by DropItemLogs.item
    var itemId by DropItemLogs.itemId
    var location by DropItemLogs.location

    override fun toModel() = DropItemLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        item = item,
        location = location
    )
}

object MiddleManLogs : LongIdTable() {
    val time = datetime("time").index()
    val requester = username("requester").index()
    val accepter = username("accepter").index()
    val middleman = username("middleman").index()
    val requesterDonatorPin = item("requester_donator_pin")
    val accepterItems = itemContainer("accepter_items")
    val accepterOSRSMillions = integer("accepter_osrs_millions")
}

class MiddleManLogEntity(id: EntityID<Long>) : ModelEntity<MiddleManLog>(id) {
    companion object : LongEntityClass<MiddleManLogEntity>(MiddleManLogs) {
        fun new(log: MiddleManLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            requester = log.requester
            accepter = log.accepter
            middleman = log.middleman
            requesterDonatorPin = log.requesterDonatorPin
            accepterItems = log.accepterItems
            accepterOSRSMillions = log.accepterOSRSMillions
        }
    }

    var time by MiddleManLogs.time
    var requester by MiddleManLogs.requester
    var accepter by MiddleManLogs.accepter
    var middleman by MiddleManLogs.middleman
    var requesterDonatorPin by MiddleManLogs.requesterDonatorPin
    var accepterItems by MiddleManLogs.accepterItems
    var accepterOSRSMillions by MiddleManLogs.accepterOSRSMillions

    override fun toModel(): MiddleManLog = MiddleManLog(
        time = time.toInstant(defaultTimeZone),
        requester = requester,
        accepter = accepter,
        middleman = middleman,
        requesterDonatorPin = requesterDonatorPin,
        accepterItems = accepterItems,
        accepterOSRSMillions = accepterOSRSMillions
    )
}

@DetailedItemsTable(FlowerPokerSessionLogItems::class)
object FlowerPokerSessionLogs : LongIdTable("flower_poker_session_logs") {
    val time = datetime("time").index()
    val player1 = username("player1").index()
    val player2 = username("player2").index()
    val player1Items = itemContainer("player1_items")
    val player2Items = itemContainer("player2_items")
    val winner = username("winner")
}

class FlowerPokerSessionLogEntity(id: EntityID<Long>) : ModelEntity<FlowerPokerSessionLog>(id) {
    companion object : LongEntityClass<FlowerPokerSessionLogEntity>(FlowerPokerSessionLogs) {
        fun new(log: FlowerPokerSessionLog): FlowerPokerSessionLogEntity {
            val logEntity = new {
                time = log.time.toLocalDateTime(defaultTimeZone)
                player1 = log.player1
                player2 = log.player2
                player1Items = log.player1Items
                player2Items = log.player2Items
                winner = log.winner
            }
            FlowerPokerSessionLogItems.insert(logEntity.id.value, 1, log.player1, log.player1Items)
            FlowerPokerSessionLogItems.insert(logEntity.id.value, 2, log.player2, log.player2Items)
            return logEntity
        }
    }
    var time by FlowerPokerSessionLogs.time
    var player1 by FlowerPokerSessionLogs.player1
    var player2 by FlowerPokerSessionLogs.player2
    var player1Items by FlowerPokerSessionLogs.player1Items
    var player2Items by FlowerPokerSessionLogs.player2Items
    var winner by FlowerPokerSessionLogs.winner

    override fun toModel() = FlowerPokerSessionLog(
        time = time.toInstant(defaultTimeZone),
        player1 = player1,
        player2 = player2,
        player1Items = player1Items,
        player2Items = player2Items,
        winner = winner
    )
}

object GrandExchangeOfferLogs : LongIdTable("grand_exchange_offer_logs") {
    val time = datetime("time").index()
    val creator = username("creator").index()
    val offerItem = item("offer_item")
    val offerPrice = integer("offer_price")
    val offerType = enumeration<GrandExchangeOfferType>("offer_type")
    val itemId = integer("item_id").index().default(-1)
}

class GrandExchangeOfferLogEntity(id: EntityID<Long>) : ModelEntity<GrandExchangeOfferLog>(id) {
    companion object : LongEntityClass<GrandExchangeOfferLogEntity>(GrandExchangeOfferLogs) {
        fun new(log: GrandExchangeOfferLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            creator = log.creator
            offerItem = log.offerItem
            offerPrice = log.offerPrice
            offerType = log.offerType
            itemId = log.offerItem.id
        }
    }
    var time by GrandExchangeOfferLogs.time
    var creator by GrandExchangeOfferLogs.creator
    var offerItem by GrandExchangeOfferLogs.offerItem
    var offerPrice by GrandExchangeOfferLogs.offerPrice
    var offerType by GrandExchangeOfferLogs.offerType
    var itemId by GrandExchangeOfferLogs.itemId

    override fun toModel() = GrandExchangeOfferLog(
        time = time.toInstant(defaultTimeZone),
        creator = creator,
        offerItem = offerItem,
        offerPrice = offerPrice,
        offerType = offerType
    )
}

object GrandExchangeTransactionLogs : LongIdTable("grand_exchange_transaction_logs") {
    val time = datetime("time").index()
    val creator = username("creator").index()
    val accepter = username("accepter").index()
    val offerItem = item("offer_item")
    val offerPrice = integer("offer_price")
    val offerType = enumeration<GrandExchangeOfferType>("offer_type")
    val itemId = integer("item_id").index().default(-1)
}

class GrandExchangeTransactionLogEntity(id: EntityID<Long>) : ModelEntity<GrandExchangeTransactionLog>(id) {
    companion object : LongEntityClass<GrandExchangeTransactionLogEntity>(GrandExchangeTransactionLogs) {
        fun new(log: GrandExchangeTransactionLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            creator = log.creator
            accepter = log.accepter
            offerItem = log.offerItem
            offerPrice = log.offerPrice
            offerType = log.offerType
            itemId = log.offerItem.id
        }
    }
    var time by GrandExchangeTransactionLogs.time
    var creator by GrandExchangeTransactionLogs.creator
    var accepter by GrandExchangeTransactionLogs.accepter
    var offerItem by GrandExchangeTransactionLogs.offerItem
    var offerPrice by GrandExchangeTransactionLogs.offerPrice
    var offerType by GrandExchangeTransactionLogs.offerType
    var itemId by GrandExchangeTransactionLogs.itemId

    override fun toModel() = GrandExchangeTransactionLog(
        time = time.toInstant(defaultTimeZone),
        creator = creator,
        accepter = accepter,
        offerItem = offerItem,
        offerPrice = offerPrice,
        offerType = offerType
    )

}

@DetailedItemsTable(DuelLogItems::class)
object DuelLogs : LongIdTable("duel_logs") {
    val time = datetime("time").index()
    val winner = username("winner")
    val player1 = username("player1").index()
    val player2 = username("player2").index()
    val player1Items = itemContainer("player1_items")
    val player2Items = itemContainer("player2_items")
}

class DuelLogEntity(id: EntityID<Long>) : ModelEntity<DuelLog>(id) {
    companion object : LongEntityClass<DuelLogEntity>(DuelLogs) {
        fun new(log: DuelLog): DuelLogEntity {
            val logEntity = new {
                time = log.time.toLocalDateTime(defaultTimeZone)
                winner = log.winner
                player1 = log.player1
                player2 = log.player2
                player1Items = log.player1Items
                player2Items = log.player2Items
            }
            DuelLogItems.insert(logEntity.id.value, 1, log.player1, log.player1Items)
            DuelLogItems.insert(logEntity.id.value, 2, log.player2, log.player2Items)
            return logEntity
        }
    }
    var time by DuelLogs.time
    var winner by DuelLogs.winner
    var player1 by DuelLogs.player1
    var player2 by DuelLogs.player2
    var player1Items by DuelLogs.player1Items
    var player2Items by DuelLogs.player2Items

    override fun toModel() = DuelLog(
        time = time.toInstant(defaultTimeZone),
        winner = winner,
        player1 = player1,
        player2 = player2,
        player1Items = player1Items,
        player2Items = player2Items
    )
}

object ClanMessageLogs : LongIdTable("clan_message_logs") {
    val time = datetime("time").index()
    val sender = username("sender").index()
    val channelName = varchar("channel_name", 20).index()
    val channelOwner = username("channel_owner").index()
    val message = varchar("message", 255).index()
}

class ClanMessageLogEntity(id: EntityID<Long>) : ModelEntity<ClanMessageLog>(id) {
    companion object : LongEntityClass<ClanMessageLogEntity>(ClanMessageLogs) {
        fun new(log: ClanMessageLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            sender = log.sender
            channelName = log.channelName
            channelOwner = log.channelOwner
            message = log.message
        }
    }

    var time by ClanMessageLogs.time
    var sender by ClanMessageLogs.sender
    var channelName by ClanMessageLogs.channelName
    var channelOwner by ClanMessageLogs.channelOwner
    var message by ClanMessageLogs.message
    override fun toModel(): ClanMessageLog = ClanMessageLog(
        time = time.toInstant(defaultTimeZone),
        sender = sender,
        channelName = channelName,
        channelOwner = channelOwner,
        message = message
    )
}

object PrivateMessageLogs : LongIdTable("private_message_logs") {
    val time = datetime("time").index()
    val sender = username("sender").index()
    val receiver = username("receiver").index()
    val message = varchar("message", 255).index()
}

class PrivateMessageLogEntity(id: EntityID<Long>) : ModelEntity<PrivateMessageLog>(id) {
    companion object : LongEntityClass<PrivateMessageLogEntity>(PrivateMessageLogs) {
        fun new(log: PrivateMessageLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            sender = log.sender
            receiver = log.receiver
            message = log.message
        }
    }

    var time by PrivateMessageLogs.time
    var sender by PrivateMessageLogs.sender
    var receiver by PrivateMessageLogs.receiver
    var message by PrivateMessageLogs.message
    override fun toModel(): PrivateMessageLog = PrivateMessageLog(
        time = time.toInstant(defaultTimeZone),
        sender = sender,
        receiver = receiver,
        message = message
    )
}

object PublicMessageLogs : LongIdTable("public_message_logs") {
    val time = datetime("time").index()
    val sender = username("sender").index()
    val message = varchar("message", 255).index()
}

class PublicMessageLogEntity(id: EntityID<Long>) : ModelEntity<PublicMessageLog>(id) {
    companion object : LongEntityClass<PublicMessageLogEntity>(PublicMessageLogs) {
        fun new(log: PublicMessageLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            sender = log.sender
            message = log.message
        }
    }

    var time by PublicMessageLogs.time
    var sender by PublicMessageLogs.sender
    var message by PublicMessageLogs.message
    override fun toModel(): PublicMessageLog = PublicMessageLog(
        time = time.toInstant(defaultTimeZone),
        sender = sender,
        message = message
    )
}

object RareDropLogs : LongIdTable("rare_drop_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val item = item("item").index()
    val itemId = integer("item_id").index().default(-1)
    val itemSource = varchar("source", 255).index()
}

class RareDropLogEntity(id: EntityID<Long>) : ModelEntity<RareDropLog>(id) {
    companion object : LongEntityClass<RareDropLogEntity>(RareDropLogs) {
        fun new(log: RareDropLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            item = log.item
            itemSource = log.itemSource
            itemId = log.item.id
        }
    }

    var time by RareDropLogs.time
    var username by RareDropLogs.username
    var item by RareDropLogs.item
    var itemSource by RareDropLogs.itemSource
    var itemId by RareDropLogs.itemId
    override fun toModel(): RareDropLog = RareDropLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        item = item,
        itemSource = itemSource
    )
}

object CreditStoreCheckoutLogs : LongIdTable("credit_store_checkout_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val cart = jsonb<List<CreditStoreCartItem>>("cart", Json)
}

class CreditStoreCheckoutLogEntity(id: EntityID<Long>) : ModelEntity<CreditStoreCheckoutLog>(id) {
    companion object : LongEntityClass<CreditStoreCheckoutLogEntity>(CreditStoreCheckoutLogs) {
        fun new(log: CreditStoreCheckoutLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            cart = log.cart
        }
    }

    var time by CreditStoreCheckoutLogs.time
    var username by CreditStoreCheckoutLogs.username
    var cart by CreditStoreCheckoutLogs.cart
    override fun toModel(): CreditStoreCheckoutLog = CreditStoreCheckoutLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        cart = cart
    )
}

object RemnantExchangeLogs : LongIdTable("remnant_exchange_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val itemId = integer("item_id").index()
    val amount = integer("item_amount")
    val value = integer("item_value")
}

class RemnantExchangeLogEntity(id : EntityID<Long>) : ModelEntity<RemnantExchangeLog>(id) {

    companion object : LongEntityClass<RemnantExchangeLogEntity>(RemnantExchangeLogs) {
        fun new(log: RemnantExchangeLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            itemId = log.item.id
            amount = log.item.amount
            value = log.value
        }
    }

    var time by RemnantExchangeLogs.time
    var username by RemnantExchangeLogs.username
    var itemId by RemnantExchangeLogs.itemId
    var amount by RemnantExchangeLogs.amount
    var value by RemnantExchangeLogs.value

    override fun toModel(): RemnantExchangeLog {
        return RemnantExchangeLog(
            time = time.toInstant(defaultTimeZone),
            username = username,
            item = Item(itemId, amount),
            value = value
        )
    }
}
