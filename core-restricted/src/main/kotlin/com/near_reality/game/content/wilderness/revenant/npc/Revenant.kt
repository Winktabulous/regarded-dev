package com.near_reality.game.content.wilderness.revenant.npc

import com.near_reality.game.content.wilderness.revenant.npc.RevenantMaledictus.Companion.onRevenantDeath
import com.near_reality.game.content.wilderness.revenant.npc.drop.GoodRevenantDrop
import com.near_reality.game.content.wilderness.revenant.npc.drop.MediocreReventantDrop
import com.zenyte.game.content.boons.impl.RevItUp
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.Spawnable
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell
import com.zenyte.game.world.entity.player.action.combat.magic.spelleffect.BindEffect
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.region.CharacterLoop
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author Tommeh | 7 aug. 2018 | 13:25:40
 * @see [Rune-Server
 * profile](https://www.rune-server.ee/members/tommeh/)}
 */
class Revenant(id: Int, tile: Location?, facing: Direction?, radius: Int) :
    NPC(id, tile, facing, radius),
    CombatScript,
    Spawnable
{
    private var healedAmount = 0

    init {
        if (!isAbstractNPC) {
            aggressionDistance = 2
            attackDistance = 8
        }
    }

    override fun updateCombatDefinitions() {
        super.updateCombatDefinitions()
        getCombatDefinitions().attackStyle = AttackType.MAGIC
    }

    public override fun onFinish(source: Entity?) {
        super.onFinish(source)
        healedAmount = 0
        onRevenantDeath(this)
    }

    override fun isTolerable(): Boolean = false

    override fun validate(id: Int, name: String): Boolean = isRevenant(id)

    override fun attack(target: Entity): Int {
        if (target !is Player) {
            return 0
        }
        val constants = RevenantProjectileType.REVENANTS[getId()]
        val heal = Utils.random(3) == 0 && getHitpoints() <= maxHitpoints / 2
        if (heal && healedAmount < 300) {
            graphics = HEAL_GFX
            var amount = maxHitpoints / 4
            if ((amount + healedAmount) > 300) {
                amount = 300 - healedAmount
            }
            healedAmount += amount
            setHitpoints(getHitpoints() + (amount))
        } else {
            val style = if (target.prayerManager.isActive(Prayer.PROTECT_FROM_MAGIC)) "Ranged" else "Magic"
            getCombatDefinitions().setAttackStyle(style)
            if (style == "Magic") {
                val projectile = Projectile(1415, constants!!.startHeight, 25, constants.delay, 15, 15, 0, 5)
                val freezeDelay = target.getNumericTemporaryAttribute("revenant_freeze").toLong()
                setAnimation(getCombatDefinitions().attackAnim)
                val originalLocation = target.location
                schedule({
                    val currentLocation = target.location
                    val distance = originalLocation.getDistance(currentLocation)
                    val negateDamage = distance >= 12
                    if (negateDamage) {
                        target.sendDeveloperMessage("Canceling revenant multi attack because out of reach.")
                        return@schedule
                    }
                    if (Utils.random(8) == 0 && freezeDelay < Utils.currentTimeMillis()) {
                        target.graphics = CombatSpell.ICE_BARRAGE.hitGfx
                        target.temporaryAttributes["revenant_freeze"] = Utils.currentTimeMillis() + 20000
                        val hitDamage = getRandomMaxHit(this, getCombatDefinitions().maxHit, CombatScript.MAGIC, target)
                        BindEffect(7).spellEffect(null, target, hitDamage)
                        delayHit(-1, target, Hit(this, hitDamage, HitType.MAGIC))
                    } else {
                        CharacterLoop.forEach(currentLocation, 0, Player::class.java) { p: Player ->
                            if (!p.isDead) {
                                p.graphics = MAGIC_HIT_GFX
                                val magicHitDamage = getRandomMaxHit(this, getCombatDefinitions().maxHit, CombatScript.MAGIC, p)
                                val magicHit = Hit(this, magicHitDamage, HitType.MAGIC)
                                delayHit(this, -1, p, magicHit)
                            }
                        }
                    }
                }, World.sendProjectile(getFaceLocation(target), target, projectile))
            } else {
                val projectile = Projectile(206, constants!!.startHeight, 25, constants.delay, 15, 15, 0, 5)
                setAnimation(getCombatDefinitions().attackAnim)
                val projectileArrivalDelay = World.sendProjectile(this, target, projectile)
                val rangedHitDamage = getRandomMaxHit(this, getCombatDefinitions().maxHit, CombatScript.RANGED, target)
                val rangedHit = Hit(this, rangedHitDamage, HitType.RANGED)
                delayHit(this, projectileArrivalDelay, target, rangedHit)
            }
        }
        return getCombatDefinitions().attackSpeed
    }

    override fun handleOutgoingHit(target: Entity, hit: Hit) {
        if (target is Player) {
            val bracelet = target.equipment.getItem(EquipmentSlot.HANDS)
            if (bracelet != null && bracelet.id == BRACELET_OF_ETHEREUM.id && bracelet.charges > 0) {
                hit.damage = 0
                target.chargesManager.removeCharges(bracelet, 1, target.equipment.container, EquipmentSlot.HANDS.slot)
            }
        }
        super.handleOutgoingHit(target, hit)
    }

    override fun isAcceptableTarget(entity: Entity): Boolean {
        return entity !is Player || entity.equipment.getItem(EquipmentSlot.HANDS)?.id != ItemId.BRACELET_OF_ETHEREUM
    }

    override fun drop(tile: Location) {
        try {
            val killer = dropRecipient ?: return
            onDrop(killer)
            val processors = DropProcessorLoader.get(id)
            if (processors != null) {
                for (processor in processors) {
                    processor.onDeath(this, killer)
                }
            }
            val level = combatLevel
            val clampedLevel = max(1.0, min(144.0, level.toDouble())).toInt()
            var chanceA = 2000 / (sqrt(clampedLevel.toDouble()).toInt())
            val chanceB = 15 + ((level + 60.0f).pow(2) / 200)
            if (killer.variables.revenantBoosterTick > 0) {
                chanceA = (chanceA * 0.9).toInt()
            }
            val a = Utils.random(chanceA - 1)
            var amount: Int = Utils.random(1, max(1.0, sqrt(level * 3.0)).toInt())
            if (amount > 0) {
                if (killer.boonManager.hasBoon(RevItUp::class.java)) amount *= 2
            }
            if (killer.getNumericAttribute("ethereum absorption").toInt() == 1) {
                val bracelet = killer.equipment.getId(EquipmentSlot.HANDS)
                if (bracelet == ItemId.BRACELET_OF_ETHEREUM || bracelet == ItemId.BRACELET_OF_ETHEREUM_UNCHARGED) {
                    val braceletItem = killer.gloves
                    if (braceletItem.charges + amount > 16000) {
                        val amt = 16000 - braceletItem.charges
                        amount -= amt
                        braceletItem.charges += amt
                    } else {
                        braceletItem.charges += amount
                        amount = 0
                    }
                    if (bracelet == ItemId.BRACELET_OF_ETHEREUM_UNCHARGED) {
                        braceletItem.id = ItemId.BRACELET_OF_ETHEREUM
                        killer.equipment.refresh(EquipmentSlot.HANDS.slot)
                    }
                }
            }
            if(amount > 0)
                dropItem(killer, Item(ItemId.REVENANT_ETHER, amount))

            when {
                a == 0 -> dropItem(killer, GoodRevenantDrop.get(killer), tile, false)
                a < (chanceB + 1) -> dropItem(killer, MediocreReventantDrop.get(), tile, false)
                else -> {
                    val coinRewardDrop = if (killer.boonManager.hasBoon(RevItUp::class.java))
                        Utils.random(10_000, 35_000)
                    else
                        Utils.random(5_000, 17_500)
                    dropItem(killer, Item(ItemId.COINS_995, coinRewardDrop), tile, true)
                }
            }
        } catch (e: Exception) {
            logger.error("Error when dropping loot from revenant. ", e)
        }
    }

    override fun sendNotifications(player: Player) {
        player.notificationSettings.increaseKill("Revenant")
    }

    companion object {
        private val HEAL_GFX = Graphics(1221)
        private val MAGIC_HIT_GFX = Graphics(1454, 0, 92)
        private val BRACELET_OF_ETHEREUM = Item(ItemId.BRACELET_OF_ETHEREUM)

        fun isRevenant(id: Int): Boolean {
            return id == 7881 || id >= 7931 && id <= 7940
        }
    }
}

private enum class RevenantProjectileType(val id: Int, val startHeight: Int, val delay: Int) {
    IMP(7881, 10, 35),
    GOBLIN(7931, 20, 20),
    PYREFIEND(7932, 25, 35),
    HOBGOBLIN(7933, 25, 35),
    CYCLOPS(7934, 50, 38),
    HELLHOUND(7935, 45, 20),
    DEMON(7936, 45, 30),
    ORK(7937, 45, 30),
    DARK_BEAST(7938, 40, 30),
    KNIGHT(7939, 30, 35),
    DRAGON(7940, 40, 30);

    companion object {
        @JvmField
        val REVENANTS: MutableMap<Int, RevenantProjectileType> = HashMap()
        private val VALUES = entries.toTypedArray()

        init {
            for (revenant in VALUES) {
                REVENANTS[revenant.id] = revenant
            }
        }
    }
}
