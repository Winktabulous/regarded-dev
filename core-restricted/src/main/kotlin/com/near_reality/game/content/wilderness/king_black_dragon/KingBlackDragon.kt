package com.near_reality.game.content.wilderness.king_black_dragon

import com.zenyte.game.content.boss.BossRespawnTimer
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.Toxins
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.Spawnable
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.npc.impl.slayer.dragons.Dragonfire
import com.zenyte.game.world.entity.npc.impl.slayer.dragons.DragonfireProtection
import com.zenyte.game.world.entity.npc.impl.slayer.dragons.DragonfireType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat
import com.zenyte.game.world.entity.player.calog.CAType
import com.zenyte.game.world.entity.player.perk.PerkWrapper
import kotlin.math.floor
import kotlin.math.max

/**
 * @author Kris | 23. apr 2018 : 15:46.37
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
open class KingBlackDragon(id: Int, tile: Location?, direction: Direction?, radius: Int) :
    NPC(id, tile, direction, radius),
    Spawnable,
    CombatScript
{
    private var instance: KingBlackDragonInstance? = null

    override fun getRespawnDelay(): Int =
        BossRespawnTimer.KING_BLACK_DRAGON.timer.toInt()

    override fun isEntityClipped(): Boolean =
        false

    override fun isTolerable(): Boolean =
        false

    override fun validate(id: Int, name: String): Boolean =
        name == "king black dragon"

    init {
        this.aggressionDistance = 64
        this.maxDistance = 64
        this.attackDistance = 10
    }

    override fun attack(target: Entity): Int {
        if (target !is Player) return 0
        val npc = this
        val player = target
        val random = Utils.random(if (isWithinMeleeDistance(npc, target)) 2 else 1)
        if (random == 0) {
            npc.setAnimation(DRAGONFIRE_ANIM)
            World.sendProjectile(npc, target, DRAGONFIRE_PROJ)
            val perk = player.perkManager.isValid(PerkWrapper.BACKFIRE)
            val modifier: Double = if (!perk) 1.0 else max(0.0, Utils.randomDouble() - 0.25f)
            val dragonfire = Dragonfire(DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(this, player))
            val deflected = if (!perk) 0 else (floor(dragonfire.maximumDamage * modifier)
                .toInt())
            delayHit(
                npc,
                DRAGONFIRE_PROJ.getTime(npc, target),
                target,
                Hit(
                    npc,
                    Utils.random(max(0.0, (dragonfire.damage - deflected).toDouble()).toInt()),
                    HitType.REGULAR
                ).onLand {
                    player.sendFilteredMessage(String.format(dragonfire.message, "dragon\'s fiery breath"))
                    PlayerCombat.appendDragonfireShieldCharges(player)
                    target.setGraphics(DRAGONFIRE_GFX)
                    if (perk) {
                        dragonfire.backfire(npc, player, 0, deflected)
                    }
                })
        } else if (random == 2) {
            if (Utils.random(1) == 0) {
                npc.setAnimation(ATTACK_ANIM)
            } else {
                npc.setAnimation(SECONDARY_ATTACK_ANIM)
            }
            delayHit(npc, 0, target, Hit(npc, getRandomMaxHit(npc, 25, CombatScript.MELEE, target), HitType.MELEE))
        } else {
            val atk = Utils.random(2)
            when (atk) {
                0 -> {
                    npc.setAnimation(DRAGONFIRE_ANIM)
                    World.sendProjectile(npc, target, POISON_PROJ)
                    val perk = player.perkManager.isValid(PerkWrapper.BACKFIRE)
                    val modifier: Double = if (!perk) 1.0 else max(0.0, Utils.randomDouble() - 0.25f)
                    val dragonfire: Dragonfire.DragonfireBuilder = object : Dragonfire.DragonfireBuilder(
                        DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(
                            this, player
                        )
                    ) {
                        override fun getDamage(): Int {
                            val tier = accumulativeTier
                            return if (tier == 0.0f) 65 else if (tier == 0.25f) 60 else if (tier == 0.5f) 35 else if (tier == 0.75f) 25 else 10
                        }
                    }
                    val deflected = if (!perk) 0 else (floor(dragonfire.maximumDamage * modifier)
                        .toInt())
                    delayHit(
                        npc,
                        POISON_PROJ.getTime(npc, target),
                        target,
                        Hit(
                            npc,
                            Utils.random(max(0.0, (dragonfire.damage - deflected).toDouble()).toInt()),
                            HitType.REGULAR
                        ).onLand { hit: Hit? ->
                            player.sendFilteredMessage(String.format(dragonfire.message, "dragon\'s poisonous breath"))
                            if (Utils.random(3) == 0) {
                                target.getToxins().applyToxin(Toxins.ToxinType.POISON, 8, npc)
                            }
                            if (perk) {
                                dragonfire.backfire(npc, player, 0, deflected)
                            }
                            target.setGraphics(POISON_GFX)
                            PlayerCombat.appendDragonfireShieldCharges(player)
                        })
                }

                1 -> {
                    npc.setAnimation(DRAGONFIRE_ANIM)
                    World.sendProjectile(npc, target, FREEZING_PROJ)
                    val perk = player.perkManager.isValid(PerkWrapper.BACKFIRE)
                    val modifier: Double = if (!perk) 1.0 else max(0.0, Utils.randomDouble() - 0.25f)
                    val dragonfire: Dragonfire.DragonfireBuilder = object : Dragonfire.DragonfireBuilder(
                        DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(
                            this, player
                        )
                    ) {
                        override fun getDamage(): Int {
                            val tier = accumulativeTier
                            return if (tier == 0.0f) 65 else if (tier == 0.25f) 60 else if (tier == 0.5f) 35 else if (tier == 0.75f) 25 else 10
                        }
                    }
                    val deflected = if (!perk) 0 else (floor(dragonfire.maximumDamage * modifier)
                        .toInt())
                    delayHit(
                        npc,
                        FREEZING_PROJ.getTime(npc, target),
                        target,
                        Hit(
                            npc,
                            Utils.random(max(0.0, (dragonfire.damage - deflected).toDouble()).toInt()),
                            HitType.REGULAR
                        ).onLand { hit: Hit? ->
                            target.setGraphics(FREEZING_GFX)
                            PlayerCombat.appendDragonfireShieldCharges(player)
                            player.sendFilteredMessage(String.format(dragonfire.message, "dragon\'s icy breath"))
                            if (perk) {
                                dragonfire.backfire(npc, player, 0, deflected)
                            }
                            if (Utils.random(3) == 0) {
                                player.freeze(
                                    16,
                                    0
                                ) { entity: Entity? -> player.sendMessage("The dragon\'s icy attack freezes you.") }
                            }
                        })
                }

                2 -> {
                    npc.setAnimation(DRAGONFIRE_ANIM)
                    World.sendProjectile(npc, target, SHOCKING_PROJ)
                    val perk = player.perkManager.isValid(PerkWrapper.BACKFIRE)
                    val modifier: Double = if (!perk) 1.0 else max(0.0, Utils.randomDouble() - 0.25f)
                    val dragonfire: Dragonfire.DragonfireBuilder = object : Dragonfire.DragonfireBuilder(
                        DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(
                            this, player
                        )
                    ) {
                        override fun getDamage(): Int {
                            val tier = accumulativeTier
                            return if (tier == 0.0f) 65 else if (tier == 0.25f) 60 else if (tier == 0.5f) 35 else if (tier == 0.75f) 25 else 10
                        }
                    }
                    val deflected = if (!perk) 0 else (floor(dragonfire.maximumDamage * modifier)
                        .toInt())
                    delayHit(
                        npc,
                        SHOCKING_PROJ.getTime(npc, target),
                        target,
                        Hit(
                            npc,
                            Utils.random(max(0.0, (dragonfire.damage - deflected).toDouble()).toInt()),
                            HitType.REGULAR
                        ).onLand { hit: Hit? ->
                            target.setGraphics(SHOCKING_GFX)
                            PlayerCombat.appendDragonfireShieldCharges(player)
                            player.sendFilteredMessage(String.format(dragonfire.message, "dragon\'s shocking breath"))
                            if (perk) {
                                dragonfire.backfire(npc, player, 0, deflected)
                            }
                            if (Utils.random(3) == 0) {
                                player.skills.drainCombatSkills(2)
                                player.sendMessage("The dragon\'s shocking attack drains your stats.")
                            }
                        })
                }
            }
        }
        return 4
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)

        if (source is Player) {
            if (instance != null) {
                instance!!.increaseKc()
                if (instance!!.kc >= 10) {
                    source.combatAchievements.complete(CAType.WHO_IS_THE_KING_NOW)
                }
            }

            source.combatAchievements.complete(CAType.BIG_BLACK_AND_FIERY)
            source.combatAchievements.checkKcTask("king black dragon", 10, CAType.KING_BLACK_DRAGON_NOVICE)
            source.combatAchievements.checkKcTask("king black dragon", 25, CAType.KING_BLACK_DRAGON_CHAMPION)
            if (source.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)) {
                source.combatAchievements.complete(CAType.CLAW_CLIPPER)
            }

            val protections = DragonfireProtection.getProtection(
                this, source
            )
            if ((protections.contains(DragonfireProtection.SUPER_ANTIFIRE_POTION) || protections.contains(
                    DragonfireProtection.ANTIFIRE_POTION
                )) && (protections.contains(DragonfireProtection.ANTI_DRAGON_SHIELD) || protections.contains(
                    DragonfireProtection.DRAGONFIRE_SHIELD
                ))
            ) {
                source.combatAchievements.complete(CAType.ANTIFIRE_PROTECTION)
            }
            if (AttackType.STAB == source.combatDefinitions.attackType) {
                source.combatAchievements.complete(CAType.HIDE_PENETRATION)
            }
        }
    }

    fun setInstance(instance: KingBlackDragonInstance?) {
        this.instance = instance
    }

    companion object {
        private val DRAGONFIRE_PROJ = Projectile(393, 40, 30, 40, 15, 28, 0, 5)
        private val POISON_PROJ = Projectile(394, 40, 30, 40, 15, 28, 0, 5)
        private val FREEZING_PROJ = Projectile(395, 40, 30, 40, 15, 28, 0, 5)
        private val SHOCKING_PROJ = Projectile(396, 40, 30, 40, 15, 28, 0, 5)
        private val DRAGONFIRE_GFX = Graphics(430, 0, 90)
        private val POISON_GFX = Graphics(429, 0, 90)
        private val FREEZING_GFX = Graphics(431, 0, 90)
        private val SHOCKING_GFX = Graphics(428, 0, 90)
        private val ATTACK_ANIM = Animation(80)
        private val SECONDARY_ATTACK_ANIM = Animation(91)
        private val DRAGONFIRE_ANIM = Animation(81)
    }
}
