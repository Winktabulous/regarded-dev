package com.near_reality.tools.discord.staff

import com.google.common.eventbus.Subscribe
import com.near_reality.tools.WealthScanner
import com.near_reality.tools.discord.DiscordBot
import com.near_reality.tools.discord.DiscordServer.Staff
import com.near_reality.tools.discord.staff.eco_search.configureEcoSearchCommand
import com.near_reality.tools.discord.staff.log_search.configureLogSearchCommand
import com.near_reality.tools.discord.staff.model.configureModelTestCommand
import com.near_reality.tools.discord.staff.player_search.configureHardwareInfoSearchCommand
import com.near_reality.tools.discord.staff.player_search.configurePlayerSearchCommand
import com.zenyte.game.GameConstants
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import com.zenyte.plugins.events.ServerLaunchEvent
import dev.kord.core.Kord
import dev.kord.core.entity.User
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

/**
 * Represents a [DiscordBot] used for logging and performing analysis
 * through discord commands.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object DiscordStaffBot : DiscordBot() {

    /**
     * The [CoroutineScope] for this bot, which uses [Dispatchers.IO].
     */
    internal val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val gate = Semaphore(1)

    internal var cachedPlayerAccounts by atomic<List<Player>?>(null)
    private var lastCacheTime by atomic<Instant?>(null)
    private var maxCachePeriod = 5.minutes

    @Subscribe
    @JvmStatic
    fun onServerLaunch(serverLaunchEvent: ServerLaunchEvent) {
        if (GameConstants.WORLD_PROFILE.isDiscordEnabled()) {
            init(
                Staff,
                token = "OTk4MzUxNjM5ODU1NTA5NTg2.GB7otV.kqeSjIbP_ecjc__G2o7KDeLBboxUuDDmiYb2yE"
            )
        }
    }

    override fun onInit(kord: Kord) {
        configureLogSearchCommand()
        configurePlayerSearchCommand()
        configureHardwareInfoSearchCommand()
        configureEcoSearchCommand(kord)
        configureModelTestCommand()
    }

    internal fun launchGated(block: suspend () -> Unit) {
        scope.launch {
            gate.withPermit {
                block()
                delay(500L)
            }
        }
    }

    internal suspend fun loadPlayersAsync(): List<Player> {
        val lastTime = lastCacheTime
        val currentTime = Clock.System.now()
        if (cachedPlayerAccounts == null || lastTime == null || (currentTime - lastTime) > maxCachePeriod) {
            lastCacheTime = Clock.System.now()
            val playerAccounts = WealthScanner.charactersPath.toFile().walkTopDown().toList().map { file ->
                scope.async {
                    try {
                        LoginManager.deserializePlayerFromFile(file.nameWithoutExtension)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }
            }.awaitAll().filterNotNull()
            cachedPlayerAccounts = playerAccounts
            logger.info("Loading {} player accounts", playerAccounts.size)
        }
        return cachedPlayerAccounts!!
    }

    /**
     * Checks whether the argued [user] has the [Staff.developerRoleId] or [Staff.managerRoleId].
     */
    internal suspend fun isManager(user: User) =
        user.asMember(guild.id).roleIds.any { it == Staff.developerRoleId || it == Staff.managerRoleId }
}

