package com.near_reality.game.content.challenges

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.zenyte.cores.CoresManager
import com.zenyte.cores.ScheduledExternalizable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.util.*
import kotlin.reflect.KClass

abstract class ChallengeRegistry<T : ChallengeType> : ScheduledExternalizable {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val challenges = mutableMapOf<Int, Challenge<T>>()
    private val challengeProgress = mutableMapOf<UUID, MutableMap<Int, ChallengeProgress>>()
    val structList = mutableListOf<Int>()

    companion object {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        private val type = object : TypeToken<Map<UUID, MutableMap<Int, ChallengeProgress>>>() {}.type
        private val comparator = Comparator
            .comparingInt<Pair<UUID, ChallengeProgress>> { it.second.count }
            .reversed()
            .thenComparingLong { it.second.lastUpdate }
        private val comparatorReversed = Comparator
            .comparingInt<Pair<UUID, ChallengeProgress>> { it.second.count }
            .thenComparingLong { it.second.lastUpdate }
    }

    protected fun register(structId: Int, name: String, function: T.() -> Int) {
        challenges[structId] = Challenge(structId, name, function)
        structList.add(structId)
    }

    abstract fun registerChallenges()

    abstract fun resolveType(uuid: UUID): T?

    abstract fun type() : KClass<T>

    operator fun get(structId: Int) = challenges[structId]

    operator fun get(t: T) =
        challengeProgress.computeIfAbsent(t.uuid) { HashMap() }

    operator fun get(t: T, structId: Int) =
        this[t].computeIfAbsent(structId) { ChallengeProgress() }


    fun cashIn(type: T) {
        for (challenge in challenges.values) {
            var amount = challenge.function.apply(type)
            if (amount > 0) {
                val challengeProgress = this[type, challenge.structId]
                amount = amount.coerceAtMost(challenge.maxCount)
                if (amount > challengeProgress.count) {
                    challengeProgress.count = amount
                    save()
                }
            }
        }
    }

    fun challengeCompleted(structId: Int): Int {
        val challenge = challenges.getOrDefault(structId, null) ?: return 0
        val list = challengeProgress.mapNotNull { (uuid, progressMap) -> progressMap[structId]?.let { uuid to it } }
        var completedCount = 0
        for (entry in list) {
            if (entry.second.count >= challenge.maxCount) {
                completedCount++
            }
        }

        return completedCount
    }

    fun buildTransmit(structId: Int): String? =
        if (challenges.containsKey(structId)) {

            val comp = if(structId == 10419) comparatorReversed else comparator

            challengeProgress
                .filter { it.value[structId]?.count != 0 }
                .mapNotNull { (uuid, progressMap) -> progressMap[structId]?.let { uuid to it } }
                .sortedWith(comp)
                .joinToString(separator = "") { (uuid, progress) ->
                    val type: T? = resolveType(uuid)
                    if (type != null) {
                        "${type.name}|${progress.count}|"
                    } else {
                        ""
                    }
                }
        } else
            null

    private fun save() {
        CoresManager.slowExecutor.execute(::write)
    }

    override fun getLog(): Logger = logger

    override fun writeInterval(): Int = -1

    override fun ifFileNotFoundOnRead() = init()

    override fun read(reader: BufferedReader) {
        init()
        challengeProgress.clear()
        challengeProgress.putAll(gson.fromJson<Map<UUID, MutableMap<Int, ChallengeProgress>>>(reader, type))
    }

    private fun init() {
        challenges.clear()
        registerChallenges()
        ChallengesManager.add(type(), this)
    }

    override fun write() {
        out(gson.toJson(challengeProgress))
    }

    override fun path(): String = "data/gim/challenge_progress.json"
}
