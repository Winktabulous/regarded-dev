package com.near_reality.game.content.contests.launch

import com.near_reality.game.content.challenges.ChallengeType
import com.near_reality.game.world.entity.player.UsernameProvider
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import java.util.*

class SoloContestant(override val username: String) : ChallengeType, UsernameProvider {

    override lateinit var name: String
    override lateinit var uuid: UUID

    fun isMember(playerName: String) = name.equals(playerName, true)
    fun isMember(usernameProvider: UsernameProvider) = isMember(usernameProvider.username)
    fun initPlayer(player: Player) {
        if (!this::name.isInitialized) {
            name = player.username
        }
        if (!this::uuid.isInitialized) {
            uuid = UUID.randomUUID()
        }
    }

    val activeMembers
        get() = mutableListOf(this)

    init {
        initVars()
    }

    fun initVars() {
        if (!this::uuid.isInitialized) {
            name = username
        }
        if (!this::uuid.isInitialized) {
            uuid = UUID.randomUUID()
        }
    }

    fun ifOnline(block: Player.() -> Unit) =
        World.getPlayerByUsername(username)?.run(block)

    fun findPlayer() =
        Optional.ofNullable(World.getPlayerByUsername(username))

    fun isOnline() =
        World.getPlayerByUsername(username) != null

    companion object {
        fun find(usernameProvider: UsernameProvider) =  ContendersModule.groups.values.find { it.isMember(usernameProvider) }
        fun find(uuid: UUID) =  ContendersModule.groups[uuid]
    }
}