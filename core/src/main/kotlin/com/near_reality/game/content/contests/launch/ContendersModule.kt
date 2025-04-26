package com.near_reality.game.content.contests.launch

import com.google.common.eventbus.Subscribe
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.near_reality.game.world.WorldEvent
import com.near_reality.game.world.hook
import com.near_reality.util.gson.InstantSerializer
import com.zenyte.cores.CoresManager
import com.zenyte.cores.ScheduledExternalizable
import com.zenyte.plugins.events.LoginEvent
import com.zenyte.plugins.events.LogoutEvent
import com.zenyte.plugins.events.ServerLaunchEvent
import kotlinx.datetime.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

object ContendersModule : ScheduledExternalizable {

    private val logger = LoggerFactory.getLogger(ContendersModule::class.java)

    private val memberTimeMapType = object : TypeToken<MutableMap<SoloContestant, Instant>>(){}.type
    private val listType = object : TypeToken<MutableMap<UUID, SoloContestant>>() {}.type

    internal val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Instant::class.java, InstantSerializer)
        .registerTypeAdapter(memberTimeMapType, ContestantTimeMapSerializer)
        .create()

    /**
     * A list of all past and present [IronmanGroup.created] ironman groups.
     */
    internal val groups = mutableMapOf<UUID, SoloContestant>()

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
    }

    /**
     * Executes on initialization of a player.
     */
    @JvmStatic
    @Subscribe
    fun onLogin(event: LoginEvent) {
        val player = event.player
        val group = SoloContestant.find(player)
        if (group != null) {
            group.initPlayer(player)
        } else {
            val newGroup = SoloContestant(player.username)
            newGroup.initPlayer(player)
            groups[newGroup.uuid] = newGroup
            requestSave()
        }
    }

    @JvmStatic
    @Subscribe
    fun onLogout(event: LogoutEvent) {
        event.player
    }

    private fun requestSave() {
        save.set(true)
    }

    override fun getLog(): Logger = logger

    override fun writeInterval(): Int = -1

    override fun read(reader: BufferedReader) {
        groups.clear()
        val contestGroups = gson.fromJson<MutableMap<UUID, SoloContestant>>(reader, listType)
        if(contestGroups.isEmpty())
            return
        for (group in contestGroups.values) {
            group.initVars()
        }
        groups.putAll(contestGroups)
    }

    override fun write() {
        CoresManager.slowExecutor.execute {
            out(gson.toJson(groups))
        }
    }

    override fun path(): String =
        "data/contests/launch.json"
}
