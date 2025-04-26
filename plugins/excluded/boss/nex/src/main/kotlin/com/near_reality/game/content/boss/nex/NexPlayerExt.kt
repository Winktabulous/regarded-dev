package com.near_reality.game.content.boss.nex

import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.persistentAttribute
import com.zenyte.game.world.entity.player.Bonuses
import com.zenyte.game.world.entity.player.Bonuses.Bonus.ATT_MAGIC
import com.zenyte.game.world.entity.player.Bonuses.Bonus.ATT_RANGED
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants.*

val Player.nexGodwarsInstance get() = if (area is NexGodwarsInstance) area as NexGodwarsInstance else null

/**
 * Number of times this [Player] has killed [NexNPC].
 */
val Player.nexKillCount
    get() = notificationSettings.getKillcount("Nex")

/**
 * The fastest time in which [NexNPC] was killed in a fight where this [Player] participated.
 */
val Player.nexBestTime: String
    get() = bossTimer.personalBest("Nex")

/**
 * Number of times this [Player] has died in the [AncientChamberArea].
 */
var Player.nexDeathCount by persistentAttribute("nexDeathCount", 0)

/**
 * Whether to prompt this [Player] upon trying to pass the Ancient barrier (into prison room)
 */
var Player.showAncientBarrierDialogue by persistentAttribute("showAncientBarrierDialogue", false)

/**
 * Sends the varbit that changes the colour of the Ancient barrier object.
 */
fun Player.sendAncientPortalVarbit() =
    varManager.sendBitInstant(13184, if (NexModule.isNexSpawned()) 2 else 0)

/*
Choking mechanics
 */

private const val CHOKES = 15

/**
 * Number of choke processing cycles left for this [Player].
 */
var Player.nexChokesCount by attribute("nexChokes", 0)

/**
 * Sets [nexChokesCount] and sends a message notifying to this [Player].
 */
fun Player.startChoking(spread: Boolean) {
    nexChokesCount = CHOKES
    sendMessage(Colour.NEX.wrap(if (spread)
        "Someone has spread some smoke clouds to you!"
    else
        "You become surrounded by smoke clouds!"))
}

/**
 * Processes choking mechanics for this [Player] if [nexChokesCount] is larger than `0`.
 */
fun Player.processChoking() {
    if (nexChokesCount > 0) {
        if (isDead || isFinished) {
            nexChokesCount = 0
        } else if (variables.ticksInterval % 2 == 0) {

            if (nexChokesCount == CHOKES || variables.ticksInterval % 4 == 0)
                setForceTalk("*Cough*")

            prayerManager.drainPrayerPoints(if (wearSpectralSpiritShield()) 2 else 1)
            graphics = Graphics(1103, 0, 160)

            val meleeBonus = getBestMeleeBonus()
            val rangedBonus = bonuses.getBonus(ATT_RANGED)
            val magicBonus = bonuses.getBonus(ATT_MAGIC)
            when {
                meleeBonus > rangedBonus && meleeBonus > magicBonus -> {
                    skills.drainSkill(ATTACK, 2)
                    skills.drainSkill(STRENGTH, 2)
                }
                rangedBonus > meleeBonus && rangedBonus > magicBonus ->
                    skills.drainSkill(RANGED, 2)
                else ->
                    skills.drainSkill(MAGIC, 2)
            }
            nexChokesCount -= if (wearsSlayerHelmet()) 5 else 1
        }
    }
}

private fun Player.getBestMeleeBonus() = bonuses
    .getBonus(Bonuses.Bonus.ATT_STAB)
    .coerceAtLeast(bonuses
        .getBonus(Bonuses.Bonus.ATT_SLASH)
        .coerceAtLeast(bonuses
            .getBonus(Bonuses.Bonus.ATT_CRUSH)))

private fun Player.wearSpectralSpiritShield() = shield?.id == ItemId.SPECTRAL_SPIRIT_SHIELD

private fun Player.wearsSlayerHelmet() = helmet?.name?.contains("slayer helmet", true) == true

