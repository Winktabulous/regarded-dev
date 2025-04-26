package com.near_reality.api

import com.near_reality.api.dao.*
import com.near_reality.api.dao.Db.dbQueryMain
import com.near_reality.api.model.*
import com.near_reality.api.responses.UserLoginResponse
import com.near_reality.api.util.BCrypt
import com.near_reality.api.util.defaultTimeZone
import com.near_reality.game.world.info.DatabaseProfile
import com.near_reality.game.world.info.WorldProfile
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.SlotItemMap
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zenyte.game.GameConstants
import com.zenyte.game.content.grandexchange.ExchangeType
import com.zenyte.game.item._Item
import com.zenyte.game.world.entity._Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.datetime.toLocalDateTime
import mgi.types.config.npcs.NPCDefinitions
import org.slf4j.LoggerFactory

@Suppress("unused")
object GameDatabase {

    private val logger = LoggerFactory.getLogger("GameLoggerDatabaseModule")
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @JvmStatic
    fun init(profile: WorldProfile) {
        tryInitDatabase("logs", profile.logsDatabase)
        tryInitDatabase("main", profile.mainDatabase)
    }

    private fun tryInitDatabase(name: String, databaseProfile: DatabaseProfile?) {
        try {
            if (databaseProfile == null)
                logger.warn("No $name database settings found in world profile, not initializing db service")
            else if (databaseProfile.enabled) {
                when(name) {
                    "logs" -> Db.initLogsDatabase(hikari(databaseProfile))
                    "main" -> Db.initMainDatabase(hikari(databaseProfile))
                    else -> logger.error("Unknown database service $name")
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to initialize $name database service", e)
        }
    }

    private fun hikari(settings: DatabaseProfile): HikariDataSource {
        val hikariConfig = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "${settings.databaseUrl}:${settings.databasePort}/${settings.databaseName}"
            username = settings.databaseUser
            password = settings.databasePassword
            maximumPoolSize = 15
            isAutoCommit = true // TODO: change later and schedule commits every x minutes
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }

    suspend fun retrieveItemConfigs(): List<ItemConfig> {
        if (!GameConstants.WORLD_PROFILE.isMainDatabaseEnabled())
            return emptyList()
        return Db.dbQueryMain { ItemConfigEntity.all().map { it.toModel() } }
    }

    suspend fun retrieveCreditStoreProducts(): List<CreditStoreProduct> {
        if (!GameConstants.WORLD_PROFILE.isMainDatabaseEnabled())
            return emptyList()
        return Db.dbQueryMain { CreditStoreProductEntity.all().map { it.toModel() } }
    }

    suspend fun append(log: GameLogMessage) {
        if (!GameConstants.WORLD_PROFILE.isLogsDatabaseEnabled())
            return
        Db.dbQueryLogs {
            when (log) {
                is GameLogMessage.CreditStoreCheckout -> CreditStoreCheckoutLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    cart = log.cart
                }
                is GameLogMessage.MiddleManTrade -> MiddleManLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    requester = log.requester
                    accepter = log.accepter
                    middleman = log.middleman
                    requesterDonatorPin = log.requesterDonatorPin.toItemAdminCp()
                    accepterItems = log.accepterItems.toSlotItemMapAdminCp()
                    accepterOSRSMillions = log.accepterOSRSMillions
                }
                is GameLogMessage.ClaimedVotes -> Unit
                is GameLogMessage.Command -> CommandLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    commandName = log.commandName
                    commandParameters = log.commandParameters
                }

                is GameLogMessage.Death.Killed.ByNpc -> KilledByNpcLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    npcId = log.npcId
                    npc = NPCDefinitions.get(log.npcId)?.name?:"null"
                    username = log.username
                    location = log.location.toLocationAdminCp()
                    deathResult = toDeathResultAdminCp(log)
                }
                is GameLogMessage.Death.Killed.ByPlayer -> KilledByPlayerLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    killer = log.otherUsername
                    victim = log.username
                    location = log.location.toLocationAdminCp()
                    deathResult = toDeathResultAdminCp(log)
                }
                is GameLogMessage.Death.Misc -> MiscDeathLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    location = log.location.toLocationAdminCp()
                    deathResult = toDeathResultAdminCp(log)
                }
                is GameLogMessage.Duel -> DuelLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    winner = log.winnerUsername
                    player1 = log.username
                    player2 = log.otherUsername
                    player1Items = log.items.toSlotItemMapAdminCp()
                    player2Items = log.otherItems.toSlotItemMapAdminCp()
                }
                is GameLogMessage.FlowerPokerSession -> FlowerPokerSessionLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    player1 = log.username
                    player2 = log.otherUsername
                    player1Items = log.items.toSlotItemMapAdminCp()
                    player2Items = log.otherItems.toSlotItemMapAdminCp()
                    winner = log.winnerUsername
                }
                is GameLogMessage.GrandExchangeOffer -> GrandExchangeOfferLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    creator = log.username
                    offerItem = log.offer.item.toItemAdminCp()
                    offerPrice = log.offer.price
                    offerType = log.offer.type.toExchangeTypeAdminCp()
                }
                is GameLogMessage.GrandExchangeTransaction.Purchase -> GrandExchangeTransactionLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    accepter = log.username
                    creator = log.otherUsername
                    offerItem = log.item.toItemAdminCp()
                    offerPrice = log.priceEach
                    offerType = GrandExchangeOfferType.BUY
                }
                is GameLogMessage.GrandExchangeTransaction.Sell -> GrandExchangeTransactionLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    creator = log.username
                    accepter = log.otherUsername
                    offerItem = log.item.toItemAdminCp()
                    offerPrice = log.priceEach
                    offerType = GrandExchangeOfferType.SELL
                }
                is GameLogMessage.GroundItem.Drop -> DropItemLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    item = log.item.toItemAdminCp()
                    location = log.location.toLocationAdminCp()
                }
                is GameLogMessage.GroundItem.Pickup -> PickupItemLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    item = log.item.toItemAdminCp()
                    location = log.location.toLocationAdminCp()
                }
                is GameLogMessage.Login -> LoginLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    ip = log.ip
                }
                is GameLogMessage.Logout -> LogoutLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    ip = log.ip
                }
                is GameLogMessage.Message.Clan -> ClanMessageLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    sender = log.username
                    message = log.contents
                    channelName = log.channelName
                    channelOwner = log.channelOwner
                }
                is GameLogMessage.Message.Private -> PrivateMessageLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    sender = log.username
                    receiver = log.otherUsername
                    message = log.contents
                }
                is GameLogMessage.Message.Public -> PublicMessageLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    sender = log.username
                    message = log.contents
                }
                is GameLogMessage.Message.Yell -> Unit
                is GameLogMessage.RareDrop -> RareDropLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    item = log.item.toItemAdminCp()
                    itemSource = log.source
                }
                is GameLogMessage.Sanction -> SanctionLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    reporter = log.username
                    offender = log.otherUsername
                    kind = log.type
                    reason = log.reason
                    expiresAt = log.expiresAt?.toLocalDateTime(defaultTimeZone)
                }
                is GameLogMessage.Trade -> TradeLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    user1 = log.username
                    user2 = log.otherUsername
                    items1 = log.items.toSlotItemMapAdminCp()
                    items2 = log.otherItems.toSlotItemMapAdminCp()
                    location = log.location.toLocationAdminCp()
                }
                is GameLogMessage.RemnantExchange -> RemnantExchangeLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    itemId = log.item.id
                    amount = log.item.amount
                    value = log.value
                }
            }
        }
    }

    private fun toDeathResultAdminCp(log: GameLogMessage.Death) = DeathResult(
        inventory = log.inventory.toSlotItemMapAdminCp(),
        equipment = log.equipment.toSlotItemMapAdminCp(),
        lootingBag = log.lootingBag?.toSlotItemMapAdminCp() ?: emptyMap(),
        runePouch1 = log.runePouch?.toSlotItemMapAdminCp() ?: emptyMap(),
        runePouch2 = log.secondaryRunePouch?.toSlotItemMapAdminCp() ?: emptyMap(),
        kept = log.kept.toSlotItemMapAdminCp(),
        lost = log.lost.toSlotItemMapAdminCp(),
        lostToKiller = if (log is GameLogMessage.Death.Killed) log.lostToKiller.map { it.toItemAdminCp() } else emptyList(),
        grave = log.graveStone.map { it.toItemAdminCp() },
    )

    private fun SlotItemMap.toSlotItemMapAdminCp(): com.near_reality.api.model.SlotItemMap {
        return buildMap {
            this@toSlotItemMapAdminCp.forEach { (slot, item) -> put(slot, item.toItemAdminCp()) }
        }
    }

    private fun _Item.toItemAdminCp(): Item {
        return Item(
            id = id,
            amount = amount,
        )
    }

    private fun _Location.toLocationAdminCp(): Location {
        return Location(
            hash = positionHash
        )
    }

    private fun ExchangeType.toExchangeTypeAdminCp(): GrandExchangeOfferType {
        return when (this) {
            ExchangeType.BUYING -> GrandExchangeOfferType.BUY
            ExchangeType.SELLING -> GrandExchangeOfferType.SELL
        }
    }


    suspend fun validateUserLogin(username: String, password: String): UserLoginResponse = dbQueryMain {
        val userEntity = UserEntity.findByUsername(username)
        if (userEntity == null)
            UserLoginResponse.UserNotExist
        else {
            val correct = BCrypt.checkpw(password, userEntity.password)
            if (correct)
                UserLoginResponse.Success(userEntity.toApiModel(false))
            else
                UserLoginResponse.InvalidPassword
        }
    }
}
