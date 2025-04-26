package com.near_reality.game.content.wilderness

import com.google.common.eventbus.Subscribe
import com.near_reality.game.content.wilderness.event.hot_zone.WildernessHotZoneEvent
import com.near_reality.game.world.PlayerEvent
import com.near_reality.game.world.entity.player.pvpDeaths
import com.near_reality.game.world.entity.player.pvpKillStreak
import com.near_reality.game.world.entity.player.pvpKills
import com.near_reality.game.world.hook
import com.zenyte.game.content.killstreak.Killstreaks
import com.zenyte.game.content.well.WellPerk
import com.zenyte.game.content.xamphur.XamphurBoost
import com.zenyte.game.item.Item
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.MessageType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.privilege.MemberRank
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import com.zenyte.plugins.events.ServerLaunchEvent
import kotlin.math.min

@Suppress("unused")
object WildernessKillStreakModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
        event.worldThread.hook<PlayerEvent.Died> {
            val killer = ((it.killer as? Player))?:return@hook
            Killstreaks.handlePlayerKill(killer, it.player)
        }
    }

    private fun handlePlayerKill(attacker: Player, victim: Player) {

        if (attacker == victim)
            return

        if (!WildernessArea.isWithinWilderness(victim))
            return

        if (attacker.ip.equals(victim.ip, ignoreCase = true))
            return

        if (attacker.killstreakLog.entryExists(victim)) {
            attacker.sendMessage("You recently killed " + victim.name + " and therefore did not receive any rewards for that kill..</col>")
            return
        }

        val debugInfoBuilder = StringBuilder()
        var bloodMoneyReward = getTotalBloodmoneyForKill(attacker, victim)

        debugInfoBuilder.append("BM(base=$bloodMoneyReward")
        if (World.hasBoost(XamphurBoost.BONUS_BLOOD_MONEY)) {
            bloodMoneyReward *= 2
            debugInfoBuilder.append(", xamphur=2x")
        }

        if (attacker.variables.bloodMoneyBoosterLeft > 0) {
            attacker.variables.bloodMoneyBoosterLeft = attacker.variables.bloodMoneyBoosterLeft - 1
            bloodMoneyReward = (bloodMoneyReward * 1.25).toInt()
            debugInfoBuilder.append(", booster=1.25x")
        }

        if (WildernessHotZoneEvent.inHotZone(victim)) {
            bloodMoneyReward *= 2
            debugInfoBuilder.append(", hotzone=2x")
        }

        if (World.hasBoost(WellPerk.DOUBLE_BLOOD_MONEY)) {
            bloodMoneyReward *= 2
            debugInfoBuilder.append(", well=2x")
        }

        debugInfoBuilder.append(") = $bloodMoneyReward")

        attacker.sendDeveloperMessage(debugInfoBuilder.toString())

        val attackerCurrentStreak = attacker.pvpKillStreak
        attacker.pvpKillStreak = attackerCurrentStreak + 1

        attacker.sendMessage(
            "You have received <col=ff0000>" + bloodMoneyReward + "</col> blood money. Your killstreak is now <col=ff0000>"
                    + (attackerCurrentStreak + 1) + "</col>."
        )
        attacker.inventory.addOrDrop(Item(Killstreaks.BLOODMONEY_ITEM_ID, bloodMoneyReward))

        attacker.pvpKills = attacker.pvpKills + 1
        victim.pvpDeaths = victim.pvpDeaths + 1

        attacker.killstreakLog.addEntry(victim)

        announceShutdown(attacker, victim)

        resetKillstreak(victim, true)
        announceKillstreak(attacker, attackerCurrentStreak + 1)
    }

    /**
     * Resets a players killstreak
     *
     * @param player - the player
     * @param announce - announce to the world
     */
    private fun resetKillstreak(player: Player, announce: Boolean) {
        player.pvpKillStreak = 0
    }

    /**
     * Globally announce killstreak to the whole world
     *
     * @param player - the player
     * @param newKillstreak
     */
    private fun announceKillstreak(player: Player, newKillstreak: Int) {
        if (newKillstreak <= 0) return

        if (newKillstreak % 5 == 0) {
            World.sendMessage(
                MessageType.FILTERABLE, ("<img=9><col=ff0000> " + player.name
                        + " has a killstreak of " + newKillstreak +
                        " and can be shutdown for " + getKillstreakBoost(newKillstreak)
                        + " bonus Blood money!</col>")
            )
        }
    }

    private fun announceShutdown(attacker: Player, victim: Player) {
        val victimsKillstreak = victim.pvpKillStreak
        if (victimsKillstreak >= 5) {
            World.sendMessage(
                MessageType.FILTERABLE,
                ("<img=9><col=ff0000> " + attacker.name
                        + " has shut down " + victim.name +
                        " with a killing spree of " + victimsKillstreak + "!</col>")
            )
        }
    }

    private fun getTotalBloodmoneyForKill(attacker: Player, victim: Player): Int {
        val victimStreak = victim.pvpKillStreak
        val yourStreak = attacker.pvpKillStreak

        // 0..30
        val killstreakBoost = getKillstreakBoost(yourStreak)
        // 0..50
        val rankBoost = getRankBoost(attacker.memberRank)
        val totalPercentBoost = killstreakBoost + rankBoost
        // 0..80
        val shutdownBonus = getBloodmoneyForShutdown(victimStreak)

        // Victims wilderness level
        val level = WildernessArea.getWildernessLevel(victim.location).orElse(0)
        if (level == 0) return 0
        return getBloodmoneyForKill((totalPercentBoost * 1.2).toFloat(), level) + shutdownBonus
    }

    private fun getBloodmoneyForKill(percentageBoost: Float, wildernessLevel: Int): Int {
        val wildernessLevelBase = 10
        val wildernessDivisions = (wildernessLevel.toFloat() / 10).toInt() * 3

        var wildernessBloodMoney = wildernessLevelBase + wildernessDivisions
        val amountToAdd = ((wildernessBloodMoney).toFloat() / 100 * percentageBoost).toInt()

        wildernessBloodMoney += amountToAdd

        val finalAmount = min(wildernessBloodMoney.toDouble(), 50.0).toInt()
        return Utils.random(finalAmount - 4, finalAmount)
    }

    /**
     * Get's the percentage boost from killstreak
     *
     * @param killstreak - the killstreak boost
     * @return - the percent of boost you get for specific killstreaks
     */
    private fun getKillstreakBoost(killstreak: Int): Int {
        if (killstreak <= 0) return 0
        if (killstreak >= 21) return 30

        var baseBoost = 5
        val currentStreak = killstreak

        // every 5 we add get 5%, with a maximum of 30
        val currentBoost = min(((currentStreak / 5) * 5).toDouble(), 25.0).toInt()
        baseBoost += currentBoost

        return baseBoost
    }

    private fun getBloodmoneyForShutdown(killstreak: Int): Int {
        if (killstreak <= 5) return 0
        return min((((killstreak / 5) * 5) + 5).toDouble(), 30.0).toInt()
    }


    private fun getRankBoost(rank: MemberRank?): Int {
        return when (rank) {
            MemberRank.PREMIUM -> 5
            MemberRank.EXPANSION -> 10
            MemberRank.EXTREME -> 15
            MemberRank.RESPECTED -> 20
            MemberRank.LEGENDARY -> 30
            MemberRank.MYTHICAL -> 40
            MemberRank.AMASCUT, MemberRank.UBER -> 50
            else -> 0
        }
    }
}
//
//class BloodMoneyReward(
//    val privilege: MemberRank,
//    val myKillstreak: Int,
//    val shutdownKillstreak: Int,
//    val wildernessLevel: Int,
//    val totalAmount: Int,
//    val totalAmountWithBooster: Int,
//    val totalAmountWithXamphur: Int,
//    val totalAmountWithXamphurAndBooster: Int,
//    val shutdownBonus: Int
//
//)
//fun main() {
//    val rewards = mutableListOf<BloodMoneyReward>()
//    val shutdownKdr = 0..15 step 5
//    listOf(MemberRank.NONE, MemberRank.PREMIUM, MemberRank.UBER).forEach { privilege ->
//        // 0..50
//        val streakRanges = 0..25 step 5
//        streakRanges.forEach { yourStreak ->
//            shutdownKdr.forEach { shutdownKillstreak ->
//                val wildernessLevels = 0..60 step 10
//                wildernessLevels.forEach { wildernessLevel ->
//                    val reward = bloodMoneyReward(privilege, yourStreak, wildernessLevel, shutdownKillstreak)
//
//                    rewards.add(reward)
//                }
//            }
//        }
//    }
//    printAsciiTable(rewards)
//
////    printAsciiTable(listOf(bloodMoneyReward(MemberRank.UBER, 10, 50, 10)))
//}
//
//private fun bloodMoneyReward(
//    privilege: MemberRank,
//    yourStreak: Int,
//    wildernessLevel: Int,
//    shutdownKillstreak: Int,
//): BloodMoneyReward {
//    val rankBoost = Killstreaks.getRankBoost(privilege)
//    // 0..30
//    val killstreakBoost = Killstreaks.getKillstreakBoost(yourStreak)
//    // (0..80)
//    val totalBoost = 1.2 * (rankBoost + killstreakBoost).toDouble()
//
//    val min = 10
//    val wildernessLevelExtra = (wildernessLevel / 10) * 3
//    val baseAmount = min + wildernessLevelExtra
//    val bonusAmount = (baseAmount) / (100.0) * (totalBoost)
//
//    val shutdownBonus = Killstreaks.getBloodmoneyForShutdown(shutdownKillstreak)
//    val totalAmount = shutdownBonus + (baseAmount + bonusAmount).coerceAtMost(50.0)
//
//    val totalAmountWithBooster = totalAmount * 1.25
//    val totalAmountWithXamphur = (totalAmount * 2)
//    val totalAmountWithXamphurAndBooster = (totalAmount * 2) * 1.25
//
//    val reward = BloodMoneyReward(
//        privilege,
//        yourStreak,
//        shutdownKillstreak,
//        wildernessLevel,
//        totalAmount.toInt(),
//        totalAmountWithBooster.toInt(),
//        totalAmountWithXamphur.toInt(),
//        totalAmountWithXamphurAndBooster.toInt(),
//        shutdownBonus
//    )
//    return reward
//}
//
//fun printAsciiTable(rewards: List<BloodMoneyReward>) {
//    val header = listOf(
//        "Privilege", "My Killstreak", "Shutdown Killstreak", "Wilderness Level",
//        "Total Amount", "Total w/ Booster", "Total w/ Xamphur", "Total w/ Xamphur + Booster"
//    )
//
//    val separator = "-".repeat(header.joinToString(" | ").length)
//
//    println(separator)
//    println(header.joinToString(" | "))
//    println(separator)
//
//    rewards.forEach { reward ->
//        val row = listOf(
//            reward.privilege.toString().padEnd(header[0].length),
//            "${reward.myKillstreak.toString().padEnd(2)} (my kdr)".padEnd(header[1].length),
//            "${reward.shutdownKillstreak.toString().padEnd(2)} (other kdr +${reward.shutdownBonus})".padEnd(header[2].length),
//            "${reward.wildernessLevel.toString().padEnd(2)} (wildy lvl)".padEnd(header[3].length),
//            "${reward.totalAmount} (base)".padEnd(header[4].length),
//            "${reward.totalAmountWithBooster} (+ booster)".padEnd(header[5].length),
//            "${reward.totalAmountWithXamphur} (+ xamphur)".padEnd(header[6].length),
//            "${reward.totalAmountWithXamphurAndBooster} (+ xamphur + booster)".padEnd(header[7].length)
//        )
//        println(row.joinToString(" | "))
//    }
//    println(separator)
//}
