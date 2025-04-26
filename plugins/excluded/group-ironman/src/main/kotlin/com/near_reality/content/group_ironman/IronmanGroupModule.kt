package com.near_reality.content.group_ironman

import com.google.common.eventbus.Subscribe
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.near_reality.content.group_ironman.player.*
import com.near_reality.content.group_ironman.util.IronmanMemberTimeMapSerializer
import com.near_reality.game.world.WorldEvent
import com.near_reality.game.world.hook
import com.near_reality.util.gson.InstantSerializer
import com.zenyte.cores.CoresManager
import com.zenyte.cores.ScheduledExternalizable
import com.zenyte.game.content.clans.ClanChannel
import com.zenyte.plugins.events.LoginEvent
import com.zenyte.plugins.events.LogoutEvent
import com.zenyte.plugins.events.ServerLaunchEvent
import kotlinx.datetime.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Handles the initialisation of players.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object IronmanGroupModule : ScheduledExternalizable {

    private val logger = LoggerFactory.getLogger(IronmanGroupModule::class.java)

    private val memberTimeMapType = object : TypeToken<MutableMap<IronmanGroupMember, Instant>>(){}.type
    private val listType = object : TypeToken<MutableMap<UUID, IronmanGroup>>() {}.type

    internal val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Instant::class.java, InstantSerializer)
        .registerTypeAdapter(memberTimeMapType, IronmanMemberTimeMapSerializer)
        .create()

    /**
     * A list of all past and present [IronmanGroup.created] ironman groups.
     */
    internal val groups = mutableMapOf<UUID, IronmanGroup>()

    private val save = AtomicBoolean(false)

    @JvmStatic
    @Subscribe
    fun onServerLaunch(event: ServerLaunchEvent) {
        event.worldThread.hook<WorldEvent.Tick> {
            if (save.get()) {
                write()
                save.set(false)
            }
        }
        GIMCommands.register()
//        RaidPartyConditions.
//        RaidPartyConditions.ironmanGroupValidatePlayerCollection = BiPredicate { _, channel ->
//            checkIfChannelMembersInSameGroupOrAllInNone(channel)
//        }
//        RaidPartyConditions.ironmanGroupValidateRaidParty = BiPredicate { _, party ->
//            checkIfChannelMembersInSameGroupOrAllInNone(party.channel)
//        }
    }

    private fun checkIfChannelMembersInSameGroupOrAllInNone(channel: ClanChannel) = channel.members
        .map { it.finalisedIronmanGroup?.uuid }
        .toSet().size == 1

    /**
     * Executes on initialization of a player.
     */
    @JvmStatic
    @Subscribe
    fun onLogin(event: LoginEvent) {
        val player = event.player
        val group = IronmanGroup.find(player)
        if (group != null) {
            if (!player.gameMode.isGroupIronman)
                logger.warn("Player {} logged in with game mode {} but is registered in group {}", player, player.gameMode, group)
            group.initPlayer(player)
        } else if (player.gameMode.isGroupIronman)
            logger.warn("Player {} logged in with game mode {} but does not belong a group!", player, player.gameMode)
    }

    @JvmStatic
    @Subscribe
    fun onLogout(event: LogoutEvent) {
        val player = event.player
        val group = IronmanGroup.find(player)
        group?.channel?.remove(player)
    }

    fun requestSave() {
        save.set(true)
    }

    fun checkIfNameNotAvailable(name: String): Boolean =
        groups.values.any { it.name.equals(name, ignoreCase = true) }

    override fun getLog(): Logger = logger

    override fun writeInterval(): Int = -1

    override fun read(reader: BufferedReader) {
        groups.clear()
        val loadedGroups = gson.fromJson<MutableMap<UUID, IronmanGroup>>(reader, listType)
        for (group in loadedGroups.values) {
            group.initVars()
            val container = IronmanGroupStorageContainer()
            container.setContainer(group.sharedStorage.bank)
            group.sharedStorage.bank = container
        }
        groups.putAll(loadedGroups)
    }

    override fun write() {
        CoresManager.slowExecutor.execute {
            out(gson.toJson(groups))
        }
    }

    override fun ifFileNotFoundOnRead() {
        requestSave()
    }

    override fun path(): String =
        "data/gim/groups.json"
}
