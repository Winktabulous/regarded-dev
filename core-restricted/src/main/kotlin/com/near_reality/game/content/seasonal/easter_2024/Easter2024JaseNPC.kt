package com.near_reality.game.content.seasonal.easter_2024

import com.near_reality.game.item.CustomNpcId
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.world.World
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.region.GlobalAreaManager

class Easter2024JaseNPC(
    internal val home: Boolean,
    location: Location
) : NPC(CustomNpcId.JASE_EASTER_2024, location, Direction.EAST, if (home) 3 else 4) {

    private var lastForceChat : String? = null

    override fun processNPC() {
        if(!Easter2024Manager.enabled)
            return
        when(Easter2024Manager.state) {
            Easter2024State.WAITING -> {
                sendRandomForceChat(
                    interval = if (home) 20 else 15,
                    possibleChats = setOf(
                        "I think the chicken is gone now...",
                        "We must have been victorious in our fight...",
                        "I hope the chicken doesn't come back soon...",
                        "May the gods be with us...",
                    )
                )
            }
            Easter2024State.CHICKEN_SPAWNING -> {
                sendRandomForceChat(
                    interval = 10,
                    possibleChats = setOf(
                        "The chicken is near, I can feel it...",
                        "It may come any moment now...",
                        "I hope we are prepared for the chicken...",
                        "May the gods be with us...",
                    )
                )
            }
            Easter2024State.CHICKEN_SPAWNED -> {
                val chicken = Easter2024Manager.chickenOrNull ?: return
                if (home) {
                    sendRandomForceChat(
                        interval = 40,
                        possibleChats = setOf(
                            "The chicken has ${chicken.hitpoints} health left, help defeat her at ::easter!",
                            "There are currently ${GlobalAreaManager.getArea(Easter2024EventArea::class.java).players.size} players fighting the colossal chicken at ::easter!"
                        )
                    )
                } else
                    helpAttendingPlayers()
            }
            Easter2024State.CHICKEN_KILLED ->  {
                sendRandomForceChat(
                    interval = if(home) 15 else 5,
                    possibleChats = setOf(
                        "The chicken has been defeated!",
                        "The chicken is no more!",
                        "The chicken has been vanquished!",
                        "The chicken is dead!",
                    )
                )
            }
        }
        super.processNPC()
    }

    private fun helpAttendingPlayers(): Boolean {
        val chickenType = Easter2024Manager.chickenTypeOrNull ?: return true
        if (everyNthWorldCycle(10)) {
            val possibleHelp = sequence {
                when (chickenType) {
                    ChickenType.Red -> yield(Help.Water)
                    ChickenType.Blue -> yield(Help.Sand)
                    ChickenType.Green -> yield(Help.Fire)
                    ChickenType.Rainbow -> yieldAll(listOf(Help.Sand, Help.Water, Help.Fire))
                }
            }.toSet()
            val help = possibleHelp.random()
            setForceTalk(help.spawnItemChat)
            setAnimation(Animation(723))
            freeze(3)
            val spawnLocations = List(25) { location.random(5) }.filter { World.isFloorFree(it) }
            spawnLocations.forEach {
                World.sendGraphics(Graphics(144, 30, 100), it)
            }
            WorldTasksManager.schedule(1) {
                spawnLocations.forEach { spawnLocation ->
                    help.spawnItems.forEach { itemToSpawn ->
                        World.spawnFloorItem(itemToSpawn, spawnLocation, null, 0, 10)
                    }
                }
            }
            WorldTasksManager.schedule(2) {
                setForceTalk(help.itemSpawnedChat)
            }
        }
        return false
    }

    private fun sendRandomForceChat(interval: Int, possibleChats: Set<String?>) {
        if (everyNthWorldCycle(interval)) {
            val chat = (possibleChats - lastForceChat).random()
            lastForceChat = chat
            setForceTalk(chat)
        }
    }

    private fun everyNthWorldCycle(n: Int) = WorldThread.getCurrentCycle() % n == 0L

    private sealed class Help(
        val spawnItemChat: String,
        val spawnItems: Set<Item>,
        val itemSpawnedChat: String
    ) {
        data object Sand : Help(
            "Accio bucket!",
            setOf(Item(ItemId.BUCKET, 1)),
            "Quick! Fill the bucket with sand and throw it on the chicken!"
        )
        data object Fire : Help(
            "Accio tinderbox and Axe!",
            setOf(Item(ItemId.BRONZE_AXE), Item(ItemId.TINDERBOX, 1)),
            "Quick! Light the chicken on fire!"
        )
        data object Water : Help(
            "Accio bucket!",
            setOf(Item(ItemId.BUCKET, 1)),
            "Quick! Fill the bucket with water and throw it on the chicken!"
        )
    }
}
