package com.zenyte.game.content.skills.magic.spells.arceuus

import com.google.common.base.CaseFormat
import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.content.skills.magic.spells.DefaultSpell
import com.zenyte.game.gameClock
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.ProjectileUtils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.*
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.booleanVarbit
import com.zenyte.game.world.entity.player.calog.CATierType
import com.zenyte.game.world.entity.player.clock
import com.zenyte.game.world.entity.player.`var`.VarCollection
import java.util.*
import kotlin.math.max

/**
 * @author Kris | 16/06/2022
 */
private var Player.thrallCooldown by booleanVarbit(12290)
var Player.currentThrall by weakReferenceAttribute<ThrallNPC>("currently_spawned_thrall")
var ThrallNPC.summoner by weakReferenceAttribute<Player>("thrall_summoner_player")
var ThrallNPC.attackClock by clock("thrall_attack_clock")
private var Entity.summonTime by clock("thrall_summon_time")
private var Player.thrallDespawnTask by weakReferenceAttribute<WorldTask>("thrall_despawn_task")
enum class ThrallSpell(
    val type: ThrallType,
    private val prayerDrain: Int,
    val maxHit: Int,
    val attackRange: Int,
    val npcId: Int,
    private val castXp: Double,
    private val thrallName: String
) : DefaultSpell {
    LESSER_GHOST(ThrallType.GHOST, 2, 1, 6, NpcId.LESSER_GHOSTLY_THRALL, 55.0, "lesser ghostly thrall"),
    LESSER_SKELETON(ThrallType.SKELETON, 2, 1, 6, NpcId.LESSER_SKELETAL_THRALL, 55.0, "lesser skeletal thrall"),
    LESSER_ZOMBIE(ThrallType.ZOMBIE, 2, 1, 1, NpcId.LESSER_ZOMBIFIED_THRALL, 55.0, "lesser zombified thrall"),
    SUPERIOR_GHOST(ThrallType.GHOST, 4, 2, 6, NpcId.SUPERIOR_GHOSTLY_THRALL, 70.0, "superior ghostly thrall"),
    SUPERIOR_SKELETON(ThrallType.SKELETON, 4, 2, 6, NpcId.SUPERIOR_SKELETAL_THRALL, 70.0, "superior skeletal thrall"),
    SUPERIOR_ZOMBIE(ThrallType.ZOMBIE, 4, 2, 1, NpcId.SUPERIOR_ZOMBIFIED_THRALL, 70.0, "superior zombified thrall"),
    GREATER_GHOST(ThrallType.GHOST, 6, 3, 6, NpcId.GREATER_GHOSTLY_THRALL, 88.0, "greater ghostly thrall"),
    GREATER_SKELETON(ThrallType.SKELETON, 6, 3, 6, NpcId.GREATER_SKELETAL_THRALL, 88.0, "greater skeletal thrall"),
    GREATER_ZOMBIE(ThrallType.ZOMBIE, 6, 3, 1, NpcId.GREATER_ZOMBIFIED_THRALL, 88.0, "greater zombified thrall");

    override fun spellEffect(player: Player, optionId: Int, option: String): Boolean {
        if (player.thrallCooldown) {
            player.sendMessage("You can only cast resurrection spells every 10 seconds.")
            return false
        }
        if (player.shield?.id != ItemId.BOOK_OF_THE_DEAD && !player.inventory.containsItem(ItemId.BOOK_OF_THE_DEAD)) {
            player.sendMessage("You must have a Book of the Dead in your possession to use this spell.")
            return false
        }
        if (player.prayerManager.prayerPoints < prayerDrain) {
            player.sendMessage("You don't have enough Prayer points to cast that spell.")
            return false
        }
        player.prayerManager.prayerPoints -= prayerDrain
        player.thrallCooldown = true
        val currentThrall = player.currentThrall
        if (currentThrall != null && !currentThrall.isFinished) {
            player.removeCurrentThrall(currentThrall, this)
        }
        val thrall = player.setCurrentThrall()
        thrall.summonTime = gameClock()
        player.summonTime = gameClock()
        thrall.spawn()
        thrall.attackClock = gameClock() + 5
        thrall.summoner = player
        addXp(player, castXp)
        player.animation = Animation(8973)
        player.graphics = Graphics(type.reanimateGraphics)
        player.sendSound(type.spawnSound)
        thrall.animation = Animation(type.spawnAnim)
        thrall.graphics = Graphics(type.spawnGraphics)
        World.sendSoundEffect(thrall.location, SoundEffect(type.spawnSound, 12))
        player.launchThrallSpawnTimer()
        player.launchThrallDespawnTimer(this)
        player.launchThrallCooldownResetTimer()
        return true
    }

    private fun Player.launchThrallSpawnTimer() = WorldTasksManager.schedule({
        val thrall = currentThrall ?: return@schedule
        if (thrall.summonTime != summonTime) return@schedule
        sendMessage("<col=ff289d>You resurrect a $thrallName.</col>")
    }, 3)

    private fun Player.launchThrallDespawnTimer(thrallSpell: ThrallSpell) {
        thrallDespawnTask?.stop()

        val despawnTask = WorldTask {
            val thrall = currentThrall ?: return@WorldTask
            if (thrall.summonTime != summonTime) return@WorldTask
            removeCurrentThrall(thrall, thrallSpell)
        }
        thrallDespawnTask = despawnTask
        var ticks = skills.getLevel(SkillConstants.MAGIC)
        if (combatAchievements.hasTierCompleted(CATierType.GRANDMASTER))
            ticks += ticks
        else if (combatAchievements.hasTierCompleted(CATierType.MASTER))
            ticks += ticks / 2
        WorldTasksManager.schedule(despawnTask, max(6, ticks))
    }

    private fun Player.launchThrallCooldownResetTimer() = WorldTasksManager.schedule({
        thrallCooldown = false
    }, 16)

    private fun Player.setCurrentThrall(): ThrallNPC {
        val thrall = ThrallNPC(npcId, getSpawnTile(location))
        thrall.randomWalkDelay = 5
        currentThrall = thrall
        VarCollection.THRALL_ACTIVE.updateSingle(this)
        return thrall
    }

    override fun getSpellName(): String = "resurrect " + CaseFormat.UPPER_CAMEL.to(
        CaseFormat.LOWER_UNDERSCORE,
        toString().lowercase(Locale.getDefault())
    ).replace("_".toRegex(), " ")

    private fun getSpawnTile(tile: Location): Location {
        var count = 100
        var spawnTile: Location?
        while (ProjectileUtils.isProjectileClipped(
                null, null, tile,
                RandomLocation.create(tile, 1).also {
                    spawnTile = it
                }, true
            ) || !World.isFloorFree(spawnTile, 1) || tile.matches(spawnTile)
        ) {
            if (--count == 0) {
                return tile
            }
        }
        return spawnTile ?: tile
    }

    override fun getDelay(): Int {
        return 1000
    }

    override fun getSpellbook(): Spellbook {
        return Spellbook.ARCEUUS
    }

    companion object {
        operator fun get(npc: ThrallNPC): ThrallSpell {
            for (value in values()) {
                if (value.npcId == npc.id) return value
            }
            throw IllegalArgumentException("Npc $npc does not conform to the ThrallNPC type.")
        }

        private fun Player.removeCurrentThrall(thrall: NPC, thrallSpell: ThrallSpell) {
            currentThrall = null
            thrall.remove()
            sendMessage("<col=ff289d>Your ${thrallSpell.thrallName} returns to the grave.</col>")
            sendSound(5054)
            VarCollection.THRALL_ACTIVE.updateSingle(this)
        }
    }
}

enum class ThrallType(
    val spawnAnim: Int,
    val spawnGraphics: Int,
    val spawnSound: Int,
    val reanimateGraphics: Int,
    val attackAnim: Int,
    val attackSound: Int,
    val hitSound: Int,
    val projectile: Projectile?
) {
    GHOST(
        9047,
        1903,
        5061,
        1873,
        5540,
        211,
        212,
        Projectile(1907, 27, 31, 15, 21, 15, 32, 5)
    ),
    SKELETON(
        9048,
        1904,
        5025,
        1874,
        5512,
        2700,
        -1,
        Projectile(1906, 25, 31, 57, 21, 15, 32, 5)
    ),
    ZOMBIE(
        9046,
        1905,
        5040,
        1875,
        5568,
        918,
        -1,
        null
    )
}