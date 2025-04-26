package com.near_reality.tools.discord.community

import com.google.common.eventbus.Subscribe
import com.near_reality.game.world.entity.player.disableDiscordLinkRequests
import com.near_reality.tools.discord.DiscordBot
import com.near_reality.tools.discord.DiscordServer
import com.zenyte.game.GameConstants
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.plugins.events.ServerLaunchEvent
import dev.kord.core.Kord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Represents a [DiscordBot] that forwards [world broadcasts][BroadcastType] to Discord.
 *
 * @author Stan van der Bend
 */
object DiscordCommunityBot : DiscordBot() {

    internal val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onInit(kord: Kord) {
        configureAccountLinking(kord)
    }

    @Subscribe
    @JvmStatic
    fun onServerLaunched(event: ServerLaunchEvent) {
        val profile = GameConstants.WORLD_PROFILE
        if (profile.isDiscordEnabled()){
            scope.launch {
                init(
                    server = if (profile.isDevelopment() || profile.isBeta())
                        DiscordServer.Staff
                    else
                        DiscordServer.Main,
                    token = profile.discordToken?: error("No Discord token found.")
                )
            }
            GameCommands.Command(PlayerPrivilege.PLAYER, "enablediscordlinking") { p, args ->
                p.disableDiscordLinkRequests = false
                p.dialogue {
                    plain("You enabled Discord linking to your account.")
                }
            }
            GameCommands.Command(PlayerPrivilege.DEVELOPER, "broadcast") { p, args ->
                p.options {
                    "lvl 99" {
                        broadcast(p, BroadcastType.LVL_99, SkillConstants.ATTACK)
                    }
                }
            }
        }
    }
}
