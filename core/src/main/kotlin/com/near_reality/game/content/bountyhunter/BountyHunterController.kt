package com.near_reality.game.content.bountyhunter

import com.google.common.eventbus.Subscribe
import com.near_reality.game.content.bountyhunter.tasks.BountyHunterHotspotScheduler
import com.near_reality.game.content.bountyhunter.tasks.BountyHunterPlayerCooldown
import com.near_reality.game.content.bountyhunter.tasks.BountyHunterUpdatePlayerInterfaces
import com.near_reality.game.world.Boundary
import com.near_reality.game.world.entity.player.*
import com.zenyte.GameToggles
import com.zenyte.game.item.Item
import com.zenyte.game.model.HintArrow
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.MessageType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.events.LoginEvent
import com.zenyte.plugins.events.LogoutEvent
import com.zenyte.plugins.events.ServerLaunchEvent
import com.zenyte.utils.TimeUnit
import java.time.Instant
import java.util.*

private val Instant.addTwentyMinutes: Instant
    get() {return this.plusSeconds(1200)}

/**
 * This is the Bounty Hunter controller that commands the Bounty Hunter
 * system for all players in-game.
 * @author John J. Woloszyk / Kryeus
 */
object BountyHunterController {

    /**
     * Activates a higher level of debugging for BountyHunter
     */
    @JvmStatic val DEBUG: Boolean = false

    /**
     * Zones that bounty hunter activity is active in
     */
    @JvmStatic val activeBountyZones: MutableList<Boundary> = mutableListOf(Boundary.WILDERNESS, Boundary.WILDERNESS_UNDERGROUND)

    /**
     * Exclusion Boundaries, in which the bounty hunter activity should not be active (ferox enclave)
     */
    @JvmStatic val excludedBountyZones: MutableList<Boundary> = mutableListOf(
        Boundary.FEROX_ENCLAVE1, Boundary.FEROX_ENCLAVE2, Boundary.FEROX_ENCLAVE3,
        Boundary.FEROX_ENCLAVE4, Boundary.FEROX_ENCLAVE5, Boundary.FEROX_ENCLAVE6,
        Boundary.FEROX_ENCLAVE7, Boundary.FEROX_ENCLAVE8, Boundary.FEROX_ENCLAVE9,
        Boundary.FEROX_ENCLAVE10
    )


    /**
     * The list of pairs
     */
    @JvmStatic val bountyPairs: MutableList<Pair<Player, Player>> = mutableListOf()

    /**
     * The list of players in the active zones
     */
    @JvmField val eligiblePlayersInWilderness: MutableList<Player> = mutableListOf()

    /**
     * The list of players are possible targets to be assigned to players in matchmaking
     */
    @JvmField val matchmakingPlayers: MutableList<Player> = mutableListOf()

    /**
     * The current wilderness hotspot, random at startup, cycles every 20 minutes
     */
    @JvmStatic var currentHotspot: BountyHunterHotspot = BountyHunterHotspot.random()

    /**
     * The countdown to the next new wilderness hotspot, this is used to
     * communicate time remaining to player's when clicking the button
     * on the interface and cycling the zone to the new location as a part of
     * the [process] method
     */
    @JvmStatic var countdownTimer = Instant.now().addTwentyMinutes


    /**
     * Checks current bounty pairs and returns it if one exists for a player
     */
    @JvmStatic fun hasPair(p: Player): Boolean = bountyPairs.any { it.first == p || it.second == p }


    /**
     * Finds an associated pair and if one exists, it unassigns it from the player
     */
    @JvmStatic fun unassignTarget(source: Player, overrideCooldown: Boolean) {
        if(source.isBountyPaired()) {
            source.packetDispatcher.resetHintArrow()
            if(source.getTarget().isPresent) source.getTarget().get().packetDispatcher.resetHintArrow()
            bountyPairs.removeIf{ it.first == source || it.second == source }
            updateTargetAttributes(source, null, overrideCooldown)
        }
    }

    fun handleAbandonment(p: Player) {
        p.sendMessage("You have abandoned your current bounty target.", MessageType.UNFILTERABLE)
        unassignTarget(p, false)
        p.closeBountyInterfaceIfUnengaged()
    }

    @JvmStatic fun assignTarget(subject: Player, target: Player) {
        subject.bountyHunterLastTarget = target.username.lowercase(Locale.getDefault())
        target.bountyHunterLastTarget = subject.username.lowercase(Locale.getDefault())
        bountyPairs.add(subject to target)

        subject.updateTargetData()
        target.updateTargetData()

        subject.packetDispatcher.sendHintArrow(HintArrow(target))
        target.packetDispatcher.sendHintArrow(HintArrow(subject))

        subject.sendMessage("You have been assigned a target: " + target.name)
        target.sendMessage("You have been assigned a target: " + subject.name)
    }

    /**
     * Updates attributes with target data, if target is null,
     * removes target data from attributes
     */
    @JvmStatic fun updateTargetAttributes(p: Player, target: Player?, overrideCooldown: Boolean) {
        if(target == null) {
            if(!overrideCooldown)
                p.setBountyCooldown(30)
        }
        p.updateTargetData()
    }

    /**
     * If player had a [BountyPair], removes target, sets cooldown for new match (to prevent relog to cycle targets)
     */
    @Subscribe
    @JvmStatic
    fun onPlayerLogout(logout: LogoutEvent) {
        val playerLoggingOut = logout.player
        eligiblePlayersInWilderness.remove(playerLoggingOut)
        matchmakingPlayers.remove(playerLoggingOut)
        playerLoggingOut.removePairForcefully()
        playerLoggingOut.closeBountyInterfaceIfUnengaged()
        playerLoggingOut.setBountyCooldown(60)
    }

    @JvmStatic fun getPair(p: Player): Optional<Pair<Player, Player>> {
        if(!p.isBountyPaired()) return Optional.empty()
        return Optional.of(bountyPairs.find { it.first == p || it.second == p } ?: return Optional.empty())
    }




    /**
     * The main controller method called on each GameTick that handles
     * updating the players in [activeBountyZones], assigning and unassigning targets
     * based on a variety of different circumstances
     */
    @JvmStatic fun process() {
        if(!GameToggles.BH2020_ENABLED) return
        updateWilderness()
        matchmake()
        checkAbandonedBountyPairs()
        processOverheads()
    }

    private fun matchmake() {
        for(player in eligiblePlayersInWilderness) {
            player.showBountyInterfaceIfMissing()

            if(player.skipMatchmakingDueToState())
                continue

            if(player.isBountyPaired())
                continue

            if(player.hasBountyCooldown())
                continue

            if(player.inBHMatchmaking())
                continue

            player.setTargetNameText("Searching for target...")
            matchmakingPlayers.add(player)
        }

        if(matchmakingPlayers.size < 2)
            return

        val pairsToMake = countMultiplesOf2(matchmakingPlayers.size)

        repeat(pairsToMake) {
            val pair = matchmakingPlayers.shuffled().take(2)
            if(!pair[0].bountyHunterLastTarget.equals(pair[1].username, true) && !pair[1].bountyHunterLastTarget.equals(pair[0].username, true)) {
                matchmakingPlayers.removeAll(pair)
                assignTarget(pair[0], pair[1])
            }
        }
    }

    private fun countMultiplesOf2(number: Int): Int {
        var count = 0
        var n = number

        while (n % 2 == 0 && n > 0) {
            n /= 2
            count++
        }

        return count
    }

    /**
     * Updates all overheads/skull for players currently on a BH killstreak
     */
    private fun processOverheads() {

    }


    /**
     * This updates the info panel with the appropriate data set defined by
     * [Player.bountyHunterInfoDisplay] at an interval of every 30 seconds
     * unless [forceUpdate] is set to true or they are spam clicking the
     * bounty hunter interface
     */
    @JvmStatic fun updateInfoPanel(player: Player, forceUpdate: Boolean) {
        if(!forceUpdate) {
            if(player.bountyHunterInterfaceRateLimit > 10) {
                player.sendMessage("Please wait a little while before doing this again.")
                return
            }
            if(player.bountyHunterInterfaceRateLimit == 10) {
                if(player.bountyHunterInfoCooldown != 0)
                    return
                player.bountyHunterInfoCooldown = 10
                WorldTasksManager.schedule(BountyHunterPlayerCooldown(player), 0, 0)
                player.sendMessage("Please wait a little while before doing this again.")
                player.bountyHunterInterfaceRateLimit++
                return
            }
            player.bountyHunterInterfaceRateLimit++
        }
        queueInterfaceUpdate(BHInterfaceUpdate.UPDATE_INFO_PANEL, player)
    }

    /**
     * This populates the end users interface with their bounty target information
     * based on specific defined [BHInterfaceUpdate] types
     */
    @JvmStatic fun queueInterfaceUpdate(type: BHInterfaceUpdate, p: Player) {
        when(type) {
            BHInterfaceUpdate.UPDATE_ALL -> {
                queueInterfaceUpdate(BHInterfaceUpdate.UPDATE_TARGET_ALL, p)
                queueInterfaceUpdate(BHInterfaceUpdate.UPDATE_INFO_PANEL, p)
                return
            }
            BHInterfaceUpdate.CLOSE_ALL -> {
                p.closeBountyInterfaceIfUnengaged()
            }
            BHInterfaceUpdate.UPDATE_INFO_PANEL -> {
                when(p.bountyHunterInfoDisplay) {
                    0 -> p.updateInfoPanel("BH Points", "" + p.bountyHunterPoints)
                    1 -> p.updateInfoPanel("K/D Ratio", "" + p.getBountyKDR())
                    2 -> p.updateInfoPanel("Killstreak", "" + p.bountyHunterKillstreak)
                }
                return
            }
            BHInterfaceUpdate.UPDATE_TARGET_ALL -> {
                queueInterfaceUpdate(BHInterfaceUpdate.UPDATE_NAME_STRING, p)
                queueInterfaceUpdate(BHInterfaceUpdate.UPDATE_WILDERNESS_LEVEL, p)
                queueInterfaceUpdate(BHInterfaceUpdate.UPDATE_BEST_EMBLEM, p)
                queueInterfaceUpdate(BHInterfaceUpdate.UPDATE_MULTIZONE, p)
                return
            }
            BHInterfaceUpdate.UPDATE_NAME_STRING -> {
                val target = p.getTarget()
                if(target.isPresent) {
                    p.setTargetNameText("${target.get().name} (lv-${target.get().combatLevel})")
                } else if(matchmakingPlayers.contains(p)) {
                    p.setTargetNameText("Searching for Target")
                } else {
                    p.setTargetNameText("No Target")
                }
                return
            }
            BHInterfaceUpdate.UPDATE_WILDERNESS_LEVEL -> {
                val target = p.getTarget()
                if(target.isPresent) {
                    p.setTargetWildyLevel(target.get().bountyHunterCurrentWildernessRange?.display ?: "---")
                } else {
                    p.setTargetWildyLevel("---")
                }
                return
            }
            BHInterfaceUpdate.UPDATE_BEST_EMBLEM -> {
                val target = p.getTarget()
                if(target.isPresent && target.get().getBestEmblem().isPresent) {
                    p.setTargetEmblemHidden(false)
                    p.setTargetEmblemTextHidden(false)
                    p.setTargetEmblemItemId(target.get().getBestEmblem().get().itemId)
                    p.setTargetEmblemText(target.get().getBestEmblem().get().index.toString())
                } else {
                    p.setTargetEmblemHidden(true)
                    p.setTargetEmblemText("")
                }
                return
            }
            BHInterfaceUpdate.SET_ORIGIN -> {
                p.setTargetWildyLevel("---")
                p.setTargetNameText("No Target")
                p.setTargetEmblemText("")
                return
            }
            BHInterfaceUpdate.UPDATE_MULTIZONE -> {
                val target = p.getTarget()
                p.setTargetInMulti(target.isPresent && target.get().isMultiArea)
            }
        }
    }

    /**
     * Checks players that are currently out of the [activeBountyZones] and have an active [bountyAbandonedTicker]
     */
    private fun checkAbandonedBountyPairs() {
        for(player in World.getPlayers()) {
            if(player == null)
                continue
            player.bountyAbandonedTicker.tick()
        }
    }



    /**
     * This updates the [BountyHunterController.eligiblePlayersInWilderness] list with all players currently in the wilderness
     * for use in [BountyHunterController.checkAbandonedBountyPairs]
     */
    private fun updateWilderness() {

        World.getPlayers().filter(Objects::nonNull).forEach { player ->
            run {
                if (player.inActiveBountyZone()) player.updateWildernessRange()

                if (player.shouldDequeueFromBH()) {
                    eligiblePlayersInWilderness.remove(player)
                    player.sendDeveloperMessage("Unregistering with BountyHunterController")
                    player.startAbandonmentProcess()
                    player.closeBountyInterfaceIfUnengaged()
                }
                else if (player.isEligibleForBHRegister()){
                    eligiblePlayersInWilderness.add(player)
                    player.sendDeveloperMessage("Registering with BountyHunterController")
                    player.showBountyInterfaceIfMissing()
                    queueInterfaceUpdate(BHInterfaceUpdate.UPDATE_ALL, player)
                    player.bountyAbandonedTicker.resetAbandonmentProcess()
                }
            }
        }
    }

    /**
     * Called at the end of the login processing for each player
     * should be minimal actions taken, only the necessary updates,
     * sends the BH interface data once via force through
     * [updateInfoPanel]
     */
    @Subscribe
    @JvmStatic fun onLogin(event: LoginEvent) {
        val player = event.player
        updateInfoPanel(player, true)
        queueInterfaceUpdate(BHInterfaceUpdate.SET_ORIGIN, player)
    }


    /**
     * Checks if death was in current hotspot
     */
    private fun hotspotDeath(player: Player): Boolean = currentHotspot.checkPlayer(player)


    /**
     * Completes bounty for player, upgrades [BountyHunterEmblem],
     * potentially drops a new emblem, and unassigns the pair, thus queueing
     * the player to be given a new target.
     */
    @JvmStatic fun completeBounty(killer: Player, killed: Player) {
        val hotspot = hotspotDeath(killer)
        /* Handle incrementing stats first */
        killer.bountyHunterKills++
        killer.bountyHunterKillstreak++

        var totalPoints = killer.getKillstreakBonus() + killer.getMilestonePoints() + 3

        if(hotspot)
            totalPoints *= 3

        killer.bountyHunterPoints += totalPoints
        killer.sendMessage("You have killed your target and earned " + totalPoints + "BH Points.", MessageType.UNFILTERABLE)

        if(killer.getBestEmblem().isPresent)
            upgradeEmblem(killer)
        else if (killer.getBestEmblem().isEmpty)
            dropEmblem(killer, killed.position)

        if(killer.getTargetBestEmblem().isPresent)
            downgradeEmblem(killer)

        unassignTarget(killer, false)
        updateInfoPanel(killer, true)
    }

    /**
     * Processes a drop for a T1 [BountyHunterEmblem], if the [Player] is not carrying one
     */
    private fun dropEmblem(killer: Player, position: Location) {
        World.spawnFloorItem(Item(BountyHunterEmblem.TIER_1.itemId), killer, position)
    }

    /**
     * This function upgrades the highest tier [BountyHunterEmblem]
     * that a [Player] is currently holding in their inventory, which does not
     * include L10 emblems.
     */
    private fun upgradeEmblem(p: Player) {
        val emblem = BountyHunterEmblem.getBest(p, true)
        if(emblem.isEmpty)
            return
        val upgradeEmblem = BountyHunterEmblem.getNextOrLast(emblem.get())

        /* Sanity Check to make sure its actually there first */
        if(p.inventory.containsItem(emblem.get().itemId) && p.inventory.deleteItem(emblem.get().itemId, 1).succeededAmount == 1) {
            p.inventory.addItem(upgradeEmblem.itemId, 1)
            p.sendMessage(Colour.RS_RED.wrap("Your emblem has been upgraded to the next tier."), MessageType.UNFILTERABLE)
        }
    }

    /**
     * This function downgrades the highest tier [BountyHunterEmblem]
     * that a [Player] is currently holding in their inventory
     */
    private fun downgradeEmblem(p: Player) {
        val emblem = BountyHunterEmblem.getBest(p, false)
        if(emblem.isEmpty)
            return
        if(emblem.get() == BountyHunterEmblem.TIER_1) {
            p.inventory.deleteItem(emblem.get().itemId, 1)
            return
        }
        val upgradeEmblem = BountyHunterEmblem.getPreviousOrFirst(emblem.get())

        /* Sanity Check to make sure its actually there first */
        if(p.inventory.containsItem(emblem.get().itemId) && p.inventory.deleteItem(emblem.get().itemId, 1).succeededAmount == 1) {
            p.inventory.addItem(upgradeEmblem.itemId, 1)
            p.sendMessage(Colour.RS_RED.wrap("Your emblem has been downgraded to the previous tier."), MessageType.UNFILTERABLE)
        }
    }


    /**
     * Registers BH related events on startup
     */
    @JvmStatic fun register() {
        val hotspot = BountyHunterHotspotScheduler()
        val interval = if (DEBUG)
            TimeUnit.MINUTES.toTicks(BountyHunterVars.HOTSPOT_CYCLE_DEV.toLong()).toInt()
        else TimeUnit.MINUTES.toTicks(BountyHunterVars.HOTSPOT_CYCLE_MAIN.toLong()).toInt()
        WorldTasksManager.schedule(hotspot, 0, interval)

        val interfaceUpdate = BountyHunterUpdatePlayerInterfaces()
        WorldTasksManager.schedule(interfaceUpdate, 0, BountyHunterVars.INTERFACE_UPDATE_INTERVAL)
    }

    /**
     * This is the subscriber function for the EventBus
     * that registers all events on startup through [register]
     */
    @Subscribe @JvmStatic
    fun boot(event: ServerLaunchEvent) {
        register()
    }

    /**
     * Attempts to skip the current [Player]'s target if one exists.
     * This also handles the setting of the cooldown for skipping
     * as well as resetting it after 30 minutes have passed
     */
    fun attemptSkip(p: Player) {
        if(!p.isBountyPaired()) {
            p.sendMessage(Colour.BLUE.wrap("You are not currently assigned a target to skip."))
            return
        }
        if(!p.hasSkipsAvailable()) {
            p.sendMessage(Colour.BLUE.wrap("You are out of skips, please wait " + TimeUnit.TICKS.toMinutes(p.variables.bountyHunterSkipTick.toLong()).toInt() + "minutes."))
            return
        }
        if(p.bountyHunterSkipCount == 0) {
            p.startBountySkipTimer()
        }

        p.bountyHunterSkipCount++
        p.sendMessage(Colour.BLUE.wrap("You have skipped your current bounty hunter target. (${p.bountyHunterSkipCount} of 3 allowed over 30m)"))
        unassignTarget(p, false)
    }

    /**
     * Exchanges all emblems in a player's inventory for BH points
     */
    @JvmStatic fun processEmblemExchange(p: Player) {
        val emblems = BountyHunterEmblem.getAllFromInventory(p)
        if(emblems.isEmpty()) {
            p.sendMessage(Colour.RS_RED.wrap("You do not have any emblems to exchange."))
            return
        }
        var totalPointsExchanged = 0
        for(item in emblems) {
            if(p.inventory.containsItem(item)) {
                val points = BountyHunterEmblem.forId(item.id).get().points
                p.inventory.deleteItem(item)
                p.bountyHunterPoints += points
                totalPointsExchanged += points
            }
        }
        if(totalPointsExchanged > 0)
            p.sendMessage(Colour.RS_RED.wrap("You trade all of your emblems for $totalPointsExchanged Bounty Hunter points."))
    }
}
