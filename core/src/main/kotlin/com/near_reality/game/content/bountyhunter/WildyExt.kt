package com.near_reality.game.content.bountyhunter

import com.near_reality.game.content.bountyhunter.BountyHunterController.activeBountyZones
import com.near_reality.game.content.bountyhunter.BountyHunterController.bountyPairs
import com.near_reality.game.content.bountyhunter.BountyHunterController.eligiblePlayersInWilderness
import com.near_reality.game.content.bountyhunter.BountyHunterController.excludedBountyZones
import com.near_reality.game.content.bountyhunter.BountyHunterController.getPair
import com.near_reality.game.content.bountyhunter.BountyHunterController.matchmakingPlayers
import com.near_reality.game.content.bountyhunter.BountyHunterController.queueInterfaceUpdate
import com.near_reality.game.util.Ticker
import com.near_reality.game.world.Boundary
import com.near_reality.game.world.entity.player.*
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.world.entity.player.MessageType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.variables.TickVariable
import com.zenyte.utils.TimeUnit
import java.text.DecimalFormat
import java.util.*

fun Player.inActiveBountyZone() : Boolean = Boundary.isIn(this, activeBountyZones)
fun Player.inExcludedBountyZone() : Boolean = Boundary.isIn(this, excludedBountyZones)
fun Player.canContinueBHMatchmaking() : Boolean = inActiveBountyZone() && !inExcludedBountyZone()
fun Player.isIneligibleForBHMatchmaking() : Boolean = !inActiveBountyZone() || inExcludedBountyZone()
fun Player.isEligibleForBHRegister() : Boolean = canContinueBHMatchmaking() && !isBHRegistered()
fun Player.getWildernessLevel() : Int = ((position.y - 3520 shr 3) + 1).coerceAtMost(1)
fun Player.updateWildernessRange() = with(this) { bountyHunterCurrentWildernessRange = BountyHunterWildernessRange.getForWildernessLevel(getWildernessLevel()) }
fun Player.isBHRegistered() : Boolean = eligiblePlayersInWilderness.contains(this)
fun Player.inBHMatchmaking() : Boolean = matchmakingPlayers.contains(this)
fun Player.shouldDequeueFromBH() : Boolean = isBHRegistered() && isIneligibleForBHMatchmaking()
fun Player.isBountyPaired() : Boolean = BountyHunterController.hasPair(this)
fun Player.skipMatchmakingDueToState() : Boolean = isNulled || isFinished || isDead
fun Player.closeBountyInterfaceIfUnengaged() = run { if(!isBountyPaired()) interfaceHandler.closeInterface(GameInterface.BOUNTY_HUNTER_CUSTOM) }
fun Player.unassignCurrentTarget(overrideCooldown: Boolean) = run { BountyHunterController.unassignTarget(this, overrideCooldown) }
fun Player.showBountyInterfaceIfMissing() = run {if(!interfaceHandler.isVisible(BountyHunterVars.I_PARENT)) GameInterface.BOUNTY_HUNTER_CUSTOM.open(this) }
fun Player.setBountyCooldown(seconds: Int) = run { variables.schedule(TimeUnit.SECONDS.toTicks(seconds.toLong()).toInt(), TickVariable.BH_V2_COOLDOWN) }
fun Pair<Player, Player>.getOtherPlayer(p: Player) : Player = if(first == p) second else first
fun Player.setTargetNameText(s: String) = run { packetDispatcher.sendComponentText(BountyHunterVars.I_PARENT, BountyHunterVars.I_CHILD_TARGET_NAME, s) }
fun Player.processBountyDeath() = run { bountyHunterKillstreak = 0; bountyHunterDeaths++ }
fun Player.hasBountyCooldown() : Boolean = this.variables.getTime(TickVariable.BH_V2_COOLDOWN) != 0
fun Player.setTargetEmblemHidden(hidden: Boolean) = run { packetDispatcher.sendComponentVisibility(BountyHunterVars.I_PARENT, BountyHunterVars.I_CHILD_TARGET_BEST_EMBLEM, hidden)}
fun Player.setTargetEmblemTextHidden(hidden: Boolean) = run { packetDispatcher.sendComponentVisibility(BountyHunterVars.I_PARENT, BountyHunterVars.I_CHILD_TARGET_BEST_EMBLEM_LVL, hidden)}
fun Player.setTargetEmblemText(text: String) = run { packetDispatcher.sendComponentText(BountyHunterVars.I_PARENT, BountyHunterVars.I_CHILD_TARGET_BEST_EMBLEM_LVL, text)}
fun Player.setTargetEmblemItemId(item: Int) = run { packetDispatcher.sendComponentItem(BountyHunterVars.I_PARENT, BountyHunterVars.I_CHILD_TARGET_BEST_EMBLEM, item, 90)}
fun Player.setTargetWildyLevel(text: String) = run { packetDispatcher.sendComponentText(BountyHunterVars.I_PARENT, BountyHunterVars.I_CHILD_TARGET_WILDERNESS_LVL, text)}
fun Player.setTargetInMulti(inMulti: Boolean) = run { packetDispatcher.sendComponentVisibility(BountyHunterVars.I_PARENT, BountyHunterVars.I_CHILD_TARGET_MULTI_ZONE, !inMulti)}
fun Player.updateTargetData() = queueInterfaceUpdate(BHInterfaceUpdate.UPDATE_TARGET_ALL, this)
fun Player.getBestEmblem() = BountyHunterEmblem.getBest(this, false)
fun Player.getTargetBestEmblem() = if(getTarget().isPresent) getTarget().get().getBestEmblem() else Optional.empty()
fun Player.hasSkipsAvailable() : Boolean = bountyHunterSkipCount < 3
fun Player.resetBountySkipCounter() = run { bountyHunterSkipCount = 0 }
fun Player.startBountySkipTimer() = run {
    if(this.variables.bountyHunterSkipTick == 0) {
        this.variables.bountyHunterSkipTick = TimeUnit.MINUTES.toTicks(30).toInt()
    }
}

fun Player.getBountyKDR() : String = with(this) {
    val ratio : Double = bountyHunterKills.toDouble() / bountyHunterDeaths.coerceAtLeast(1).toDouble()
    val df = DecimalFormat("#.##")
    return df.format(ratio)
}

fun Player.updateInfoPanel(title: String, info: String) = run {
    packetDispatcher.sendComponentText(BountyHunterVars.I_PARENT, BountyHunterVars.I_CHILD_INFO_LABEL, title)
    packetDispatcher.sendComponentText(BountyHunterVars.I_PARENT, BountyHunterVars.I_CHILD_INFO_DATA, info)
}


fun Ticker.initAbandon(player: Player) = run {
    this.resetWithRunnable { BountyHunterController.handleAbandonment(player) }
}

fun Player.startAbandonmentProcess() = run {
    if(isBountyPaired()) {
        bountyAbandonedTicker.initAbandon(this)
        sendMessage("Please return to the wilderness within 60 seconds to keep your target.", MessageType.UNFILTERABLE)
        if(getTarget().isPresent) getTarget().get().sendMessage("Your target has left the wilderness, you will reset in 60 seconds.", MessageType.UNFILTERABLE)
    }
}

fun Ticker.resetAbandonmentProcess() = run {
    this.resetAsInactive()
}

fun Player.getTarget(): Optional<Player> = run {
    if(this.isBountyPaired()) {
        return Optional.of(getPair(this).get().getOtherPlayer(this))
    }
    return Optional.empty()
}

fun Player.removePairForcefully() = run {
    if(this.isBountyPaired()) {
        val target = getTarget()
        bountyPairs.removeIf{this == it.first || this == it.second}
        if(target.isPresent) target.get().updateTargetData()
    }
}

fun Player.getKillstreakBonus(): Int {
    val ks = bountyHunterKillstreak
    return if(ks in 3..9)
        1
    else if(ks >= 10)
        2
    else 0
}

fun Player.getMilestonePoints(): Int {
    val kills = bountyHunterKills
    return if(kills % 500 == 0)
        25
    else if (kills % 100 == 0)
        10
    else if (kills % 50 == 0)
        5
    else if (kills % 10 == 0)
        3
    else 0
}