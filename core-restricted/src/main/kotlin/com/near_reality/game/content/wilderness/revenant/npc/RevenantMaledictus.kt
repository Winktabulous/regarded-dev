package com.near_reality.game.content.wilderness.revenant.npc

import com.near_reality.game.content.wilderness.revenant.npc.drop.GoodRevenantDrop
import com.near_reality.game.content.wilderness.revenant.npc.drop.MediocreReventantDrop
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.HintArrow
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.utils.TimeUnit
import org.slf4j.LoggerFactory
import kotlin.math.floor
import kotlin.math.sqrt

private val rangedProjectile = Projectile(2033, 75, 25, 25, 15, 18, 64, 5)
private val magicProjecitle = Projectile(1456, 85, 0, 25, 50, 45, 64, 5)

private val standardAttackAnimation = Animation(9282)
private val bloodAttackAnimation = Animation(9279)
private val freezeAttackAnimation = Animation(9278)
private val targetedMagicAttackAnimation = Animation(9277)

private val bloodAttackGraphics = Graphics(382)
private val iceBarrageImpactGraphics = Graphics(369)

/**
 * @author Andys1814
 */
@Suppress("unused")
class RevenantMaledictus(val spawn: Spawn) :
    NPC(NpcId.REVENANT_MALEDICTUS, spawn.tile, Direction.SOUTH, 5),
    CombatScript
{

    init {
        isForceMultiArea = true
        spawned = true
    }

    private val damageMap = mutableMapOf<Player, Int>()

    override fun attack(target: Entity?): Int {
        val targets = getPossibleTargets(Entity.EntityType.PLAYER)

        val roll = Utils.random(18)
        if (roll < 3) {
            setAnimation(bloodAttackAnimation)
            targets.forEach {
                val tile = it.location.copy()
                World.sendGraphics(bloodAttackGraphics, tile)
                WorldTasksManager.schedule({
                    if (it.location == tile) {
                        val damage = Utils.random(30)
                        it.applyHit(Hit(damage, HitType.MAGIC))
                        applyHit(Hit(floor(damage * 0.65).toInt(), HitType.HEALED))
                    }
                }, 3)
            }
        } else if (roll < 5) {
            setAnimation(freezeAttackAnimation)
            targets.forEach {
                val tile = it.location.copy()
                World.sendGraphics(bloodAttackGraphics, tile)
                WorldTasksManager.schedule({
                    if (it.location == tile) {
                        it.graphics = iceBarrageImpactGraphics
                        (it as Player).run {
                            this.freeze(6)
                            this.attackingDelay = System.currentTimeMillis() + 3600
                            this.sendMessage("You have been frozen in place by the Revenant Maledictus' attack!")
                        }

                    }
                }, 3)
            }
        } else if (roll == 5) {
            val selectedTarget = targets.randomOrNull()
            if (selectedTarget != null) {
                setAnimation(targetedMagicAttackAnimation)

                val tile = selectedTarget.location.copy()
                val delay = World.sendProjectile(this, tile, magicProjecitle)
                WorldTasksManager.schedule({
                    val firstBlast = Graphics(134)
                    sendBlast(firstBlast, tile)

                    val secondBlast = Graphics(2034, 15, 0)
                    sendBlast(secondBlast, tile.transform(-1, -1))
                    sendBlast(secondBlast, tile.transform(-1, 1))
                    sendBlast(secondBlast, tile.transform(1, 1))
                    sendBlast(secondBlast, tile.transform(1, -1))

                    val thirdBlast = Graphics(2034, 25, 0)
                    sendBlast(thirdBlast, tile.transform(-2, -1))
                    sendBlast(thirdBlast, tile.transform(-2, 0))
                    sendBlast(thirdBlast, tile.transform(-2, 1))
                    sendBlast(thirdBlast, tile.transform(-1, 2))
                    sendBlast(thirdBlast, tile.transform(0, 2))
                    sendBlast(thirdBlast, tile.transform(1, 2))
                    sendBlast(thirdBlast, tile.transform(2, 1))
                    sendBlast(thirdBlast, tile.transform(2, 0))
                    sendBlast(thirdBlast, tile.transform(2, -1))
                    sendBlast(thirdBlast, tile.transform(-1, -2))
                    sendBlast(thirdBlast, tile.transform(0, -2))
                    sendBlast(thirdBlast, tile.transform(1, -2))
                    targets.forEach {
                        val distance = it.location.getDistance(tile)
                        if (distance < 3) {
                            var damage = Utils.random(25)
                            (it as Player).run {
                                if (this.prayerManager.isActive(Prayer.PROTECT_FROM_MAGIC)) {
                                    damage /= 2
                                }
                            }
                            it.applyHit(Hit(damage, HitType.MAGIC))
                        }
                    }
                }, delay)
            }
        } else {
            // Standard attack
            setAnimation(standardAttackAnimation)
            targets.forEach {
                val delay = World.sendProjectile(this, it, rangedProjectile)
                val hit = Hit(getRandomMaxHit(this, 30, AttackType.RANGED, it), HitType.RANGED)
                delayHit(delay, target, hit)
            }
        }
        return 6
    }

    private fun sendBlast(graphics: Graphics, tile: Location) {
        if (World.isFloorFree(tile, 1)) {
            World.sendGraphics(graphics, tile)
        }
    }

    override fun handleIngoingHit(hit: Hit) {
        super.handleIngoingHit(hit)
        if (hit.source != null) {
            if (hit.source is Player) {
                damageMap.compute(hit.source as Player) { _, value -> (value ?: 0) + hit.damage }
            }
        }
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)
        damageMap.clear()
        instance = null
    }

    override fun drop(tile: Location?) {
        if (damageMap.entries.isEmpty()) {
            return
        }

        val sortedDamageDealerMap = damageMap.entries.sortedByDescending { it.value }
        val (topDamageDealer, _) = sortedDamageDealerMap[0]
        dropItem(topDamageDealer, Item(if (Utils.roll(50.0)) ItemId.ANCIENT_EMBLEM else ItemId.ANCIENT_TOTEM))

        // Roll two drops using the Revenant dragon table
        for (i in 0..2) {
            // Use 135 for combat level because we're rolling as if this was Revenant dragon
            val combatLevel = 135
            val clampedLevel = 1.coerceAtLeast(144.coerceAtMost(combatLevel))
            val chanceA = 2000 / sqrt(clampedLevel.toDouble()).toInt()
            if (chanceA == 0) {
                dropItem(topDamageDealer, GoodRevenantDrop.get(topDamageDealer), tile, false)
            } else {
                dropItem(topDamageDealer, MediocreReventantDrop.get(), tile, false)
            }
        }

        sortedDamageDealerMap.forEachIndexed { index, (damager, _) ->
            // Activate Forinthry surge for the top 3 damage dealers, if they have an Amulet of avarice equipped
            if (index < 3) {
                if (damager.equipment?.getId(EquipmentSlot.AMULET) == ItemId.AMULET_OF_AVARICE) {
                    com.near_reality.game.content.wilderness.revenant.ForinthrySurge.activate(damager)
                }
            }
            dropItem(damager, Item(ItemId.BLIGHTED_SUPER_RESTORE4), damager.location, true)
            val blightedFood = randomBlightedFood()
            dropItem(damager, blightedFood, damager.location, true)
            dropItem(damager, blightedFood, damager.location, true)
        }
    }

    /**
     * Could probably move this into a more reusable spot in the future if it makes sense.
     */
    private fun randomBlightedFood(): Item {
        val options = arrayOf(
            Item(ItemId.BLIGHTED_ANGLERFISH),
            Item(ItemId.BLIGHTED_KARAMBWAN),
            Item(ItemId.BLIGHTED_MANTA_RAY)
        )
        return options.random()
    }

    companion object {

        private val logger = LoggerFactory.getLogger(RevenantMaledictus::class.java)

        private var rollingSpawnNumerator = 0

        const val FLAT_SPAWN_DENOMINATOR = 10000

        val SPAWN_COOLDOWN = TimeUnit.MINUTES.toMillis(30)

        private var lastSpawn: Long? = null

        var instance: RevenantMaledictus? = null

        fun onRevenantDeath(revenant: NPC) {
            if (lastSpawn != null) {
                if (System.currentTimeMillis() - lastSpawn!! < SPAWN_COOLDOWN) {
                    logger.info("Skipping revenant death counter for Maledcitus spawn due to cooldown")
                    return
                }
            }

            // Increment our rolling spawn chance by the dying revenant's combat level
            rollingSpawnNumerator += revenant.combatLevel

            if (Utils.random(FLAT_SPAWN_DENOMINATOR) < rollingSpawnNumerator) {
                spawn()
            }
        }

        /**
         * Spawns a global instance of [RevenantMaledictus] at a random location within the Forinthry dungeon.
         */
        fun spawn() {
            val spawn = Spawn.random()

            val maledictus = RevenantMaledictus(spawn)
            maledictus.spawn()

            instance = maledictus
            lastSpawn = System.currentTimeMillis()
            rollingSpawnNumerator = 0

            val dungeon = GlobalAreaManager.getArea(com.near_reality.game.content.wilderness.revenant.area.ForinthryDungeon::class.java)
            dungeon.players.forEach {
                it.sendMessage(spawn.localHintMessage())
                it.packetDispatcher.sendHintArrow(HintArrow(maledictus))
            }

            WorldBroadcasts.sendMessage(spawn.globalHintMessage(), BroadcastType.MALEDICTUS, true)
        }

    }

    enum class Spawn(val tile: Location, val hint: String) {
        CYCLOPS_DEMON(
            tile = Location(3173, 10190),
            hint = "north"
        ),
        DRAGON(
            tile = Location(3235, 10200),
            hint = "north"
        ),
        IMPS(
            tile = Location(3214, 10191),
            hint = "north"
        ),
        HELLHOUNDS(
            tile = Location(3243, 10171),
            hint = "middle"
        ),
        EASTERN_PYREFIEND(
            tile = Location(3250, 10142),
            hint = "middle"
        ),
        WESTERN_PYREFIEND(
            tile = Location(3173, 10153),
            hint = "middle"
        ),
        DARK_BEAST(
            tile = Location(3206, 10161),
            hint = "middle"
        ),
        NORTHERN_ORK(
            tile = Location(3222, 10129),
            hint = "middle"
        ),
        SOUTHERN_ORK(
            tile = Location(3213, 10094),
            hint = "south"
        ),
        SOUTHERN_DEMON(
            tile = Location(3159, 10113),
            hint = "south"
        ),
        SOUTHERN_DEMON_OUTSIDE(
            tile = Location(3185, 10116),
            hint = "south"
        );

        fun localHintMessage(entrance: Boolean = false): String {
            val action = if (entrance) "is at large" else "has awoken"
            return "<col=0xef1020>A superior revenant $action in the $hint of the caves..</col>"
        }

        fun globalHintMessage(): String {
            return " <img=68>A superior revenant has awoken in the $hint of the revenant caves"
        }

        companion object {
            fun random(): Spawn = Utils.random(values())
        }

    }

}
