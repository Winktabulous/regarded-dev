package com.near_reality.api.dao

import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object Db {

    lateinit var logsDatabase: Database
    lateinit var mainDatabase: Database

    fun init(logsSource: HikariDataSource, mainSource: HikariDataSource) {
        initLogsDatabase(hikariSource = logsSource)
        initMainDatabase(hikariSource = mainSource)
    }

    fun initLogsDatabase(hikariSource: HikariDataSource) {
        logsDatabase = Database.connect(hikariSource)
        transaction(logsDatabase) {
            addLogger(StdOutSqlLogger)
            create(TradeLogs)
            create(TradeLogItems)
            create(SanctionLogs)
            create(LoginLogs)
            create(LogoutLogs)
            create(KilledByPlayerLogs)
            create(KilledByPlayerLogItems)
            create(KilledByNpcLogs)
            create(KilledByNpcLogItems)
            create(MiscDeathLogs)
            create(MiscDeathLogItems)
            create(CommandLogs)
            create(PickupItemLogs)
            create(DropItemLogs)
            create(MiddleManLogs)
            create(FlowerPokerSessionLogs)
            create(FlowerPokerSessionLogItems)
            create(GrandExchangeOfferLogs)
            create(GrandExchangeTransactionLogs)
            create(DuelLogs)
            create(ClanMessageLogs)
            create(PrivateMessageLogs)
            create(PublicMessageLogs)
            create(RareDropLogs)
            create(CreditStoreCheckoutLogs)
            create(RemnantExchangeLogs)
        }
    }

    fun initMainDatabase(hikariSource: HikariDataSource) {
        mainDatabase = Database.connect(hikariSource)
        transaction(mainDatabase) {
            addLogger(StdOutSqlLogger)
            create(Users)
            create(UserIPs)
            create(AccessTokens)
            create(RefreshTokens)
            create(PasswordResetTokens)
            create(RegistrationTokens)
            create(Votes)
            create(VoteSites)
            create(CreditPackages)
            create(CreditPackageOrders)
            create(CreditStoreProducts)
            create(UserSkillStats)
            create(AccountSanctions)
            create(IPSanctions)
            create(UUIDSanctions)
            create(NewsArticles)
            create(ItemConfigs)
        }
    }


    suspend fun <T> dbQueryLogs(block: Transaction.() -> T): T = dbQuery(logsDatabase, block)

    suspend fun <T> dbQueryMain(block: Transaction.() -> T): T = dbQuery(mainDatabase, block)

    suspend fun <T> dbQuery(database: Database, block: Transaction.() -> T): T = withContext(Dispatchers.IO) {
        transaction(database) {
            block()
        }
    }
}
