package com.zenyte.game.content.theatreofblood

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class RewardRegistry {

    private val playerRewardContainerMap: MutableMap<String, MutableList<Item>> by lazy { mutableMapOf() }
    private val gson: Gson by lazy { Gson() }

    companion object {
        private var instance: RewardRegistry? = null

        fun getRewardRegistry(): RewardRegistry {
            return instance ?: RewardRegistry().also { instance = it }
        }

        private const val DATA_PATH = "data/tob/playerTobGlobalChest.json"
    }

    init {
        loadFromFile()
    }

    fun removedPlayerFromRewardMap(player: Player): Boolean {
        val returnValue = playerRewardContainerMap.remove(player.username) != null
        saveToFile()
        return returnValue
    }

    fun addRewardContainerForPlayer(player: Player, container: Container) {
        if (playerRewardContainerMap.containsKey(player.username))
            playerRewardContainerMap[player.username]!!.addAll(container.itemsAsList)
        else
            playerRewardContainerMap[player.username] = container.itemsAsList
        saveToFile()
    }

    fun getContainer(player: Player): MutableList<Item>? {
        return playerRewardContainerMap[player.username]
    }

    private fun saveToFile() {
        val json = gson.toJson(playerRewardContainerMap)
        try {
            Files.write(Paths.get(DATA_PATH), json.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace(System.err)
        }
    }

    private fun loadFromFile() {
        try {
            val file = File(DATA_PATH)
            if (!file.exists()) file.mkdirs()
            val json = String(Files.readAllBytes(Paths.get(DATA_PATH)))
            if (json.isNotEmpty()) {
                val type = object : TypeToken<Map<String?, List<Item?>?>?>() {}.type
                playerRewardContainerMap.putAll(gson.fromJson(json, type))
            }
        } catch (e: Exception) {
            e.printStackTrace(System.err)
        }
    }
}