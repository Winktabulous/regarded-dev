package com.zenyte.game.content.theatreofblood.party

import com.zenyte.game.GameInterface
import com.zenyte.game.content.theatreofblood.TheatreOfBloodRaid
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.interfaces.PartiesOverviewInterface
import com.zenyte.game.content.theatreofblood.interfaces.PartyOverlayInterface
import com.zenyte.game.content.theatreofblood.room.TheatreRoom
import com.zenyte.game.content.theatreofblood.tobStats
import com.zenyte.game.content.theatreofblood.tobStatsHard
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.*
import kotlin.math.roundToInt

/**
 * @author Tommeh
 * @author Jire
 */
internal data class RaidingParty(val id: Int, val originalLeader: Player) {

    val originalMembers: MutableList<String> = ArrayList(5)
    val members: MutableList<String> = ArrayList(5)
    val applicants: MutableList<String> = ArrayList(10)
    val blocked: MutableSet<String> = HashSet(2047)
    val statuses: MutableMap<Player, Int> = HashMap(5)
    val creationTimeStamp = Date()

    var hardMode = false

    init {
        val originalLeaderUsername = originalLeader.username
        originalMembers.add(originalLeaderUsername)
        members.add(originalLeaderUsername)
    }

    var raid: TheatreOfBloodRaid? = null
    var preferredSize = 0
    var preferredCombatLevel = 3

    fun getMember(index: Int) = getPlayer(members[index])

    val players: List<Player>
        get() {
            val players: MutableList<Player> = ObjectArrayList(members.size)
            for (memberName in members) {
                val member: Player = getPlayer(memberName) ?: continue
                players.add(member)
            }
            return players
        }

    val leader: Player?
        get() {
            val first = members.firstOrNull() ?: return null
            return getPlayer(first)
        }

    fun removeMember(player: Player) {
        members.remove(player.username)
        if (members.isEmpty()) {
            VerSinhazaArea.removeParty(this)
            for (a in applicants) {
                val applicant: Player = getPlayer(a) ?: continue
                if (applicant.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION)) {
                    PartiesOverviewInterface.refresh(applicant)
                }
            }
        } else {
            val raid = raid
            if (raid != null) {
                members.mapNotNull { getPlayer(it) }.forEach {
                    initializeStatusHUD(it)
                    updateStatusHUD(it)
                }
                raid.spectators.mapNotNull { getPlayer(it) }.forEach {
                    initializeStatusHUD(it)
                    updateStatusHUD(it)
                }
            } else {
                for (m in members) {
                    val member: Player = getPlayer(m) ?: continue
                    if (member.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION))
                        member.updateInformation()
                    PartyOverlayInterface.refresh(member, this)
                }
                for (a in applicants) {
                    val applicant: Player = getPlayer(a) ?: continue
                    if (applicant.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION))
                        applicant.updateInformation()
                }
            }
        }
        if (player.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION)) {
            PartiesOverviewInterface.refresh(player)
        }
        PartyOverlayInterface.refresh(player, null)
    }

    fun addApplicant(player: Player) {
        applicants.add(player.username)
        for (p in VerSinhazaArea.raidingParties.values) {
            if (p == this) continue
            p.applicants.remove(player.username)
            for (m in p.members) {
                val member: Player = getPlayer(m) ?: continue
                p.run { member.updateInformation() }
            }
        }
        val leader = leader!!
        if (leader.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION))
            leader.updateInformation()
        player.updateInformation()
    }

    fun disband() {
        for (m in members) {
            val member: Player = getPlayer(m) ?: continue
            member.sendMessage("Your party has disbanded.")
            if (member.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION))
                PartiesOverviewInterface.refresh(member)
            PartyOverlayInterface.refresh(member, null)
        }
        for (a in applicants) {
            val applicant: Player = getPlayer(a) ?: continue
            if (applicant.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION))
                PartiesOverviewInterface.refresh(applicant)
        }
    }

    fun isLeader(player: Player?) = leader == player

    val age get() = ((Utils.currentTimeMillis() - creationTimeStamp.time) / 600).toInt()
    val size get() = members.size

    val Player.partyRights: PartyRights
        get() = when {
            isLeader(this) -> PartyRights.LEADER
            members.contains(username) -> PartyRights.PARTY_MEMBER
            applicants.contains(username) -> PartyRights.APPLICANT
            blocked.contains(username) -> PartyRights.BLOCKED_APPLICANT
            else -> PartyRights.CAN_APPLY
        }

    fun Player.updateInformation() {
        if (area is TheatreRoom) {
            logger.error("Tried opening tob information while inside raid.", Exception("Stack trace"))
            return
        }

        addTemporaryAttribute("tob_viewing_party_id", id)
        GameInterface.TOB_PARTY_INFORMATION.open(this)
        for (index in 0..4) {
            if (index >= members.size) {
                packetDispatcher.sendClientScript(2317, 2, "")
                continue
            }
            val member = getMember(index) ?: continue
            packetDispatcher.sendClientScript(2317, 2, member.informationString(hardMode))
        }
        if (members.contains(username) || applicants.contains(username)) {
            for (a in applicants) {
                val applicant: Player = getPlayer(a) ?: continue
                packetDispatcher.sendClientScript(2321, applicant.informationString(hardMode))
            }
        }
        packetDispatcher.sendClientScript(2323, partyRights.id, preferredSize, preferredCombatLevel, if (hardMode) 2 else 0)
    }

    fun initializeStatusHUD(player: Player) {
        val arguments = arrayOfNulls<Any>(5)
        for (index in 0..4) {
            if (index >= members.size) {
                arguments[index] = ""
                continue
            }
            val member = getMember(index)
            arguments[index] = member?.name ?: ""
        }
        if (!player.interfaceHandler.isPresent(GameInterface.TOB_PARTY)) {
            GameInterface.TOB_PARTY.open(player)
        }
        player.varManager.sendBit(6440, 2)
        player.varManager.sendBit(
            6441, if (raid!!.spectators.contains(player.username)) 0 else members.indexOf(
                player.username
            ) + 1
        )
        player.packetDispatcher.sendClientScript(2301, *arguments)
        updateStatusHUD(player)
    }

    fun updateStatusHUD(force: Boolean) {
        for (index in 0..4) {
            if (index >= members.size) {
                for (m in members) {
                    val member: Player = getPlayer(m) ?: continue
                    if (member.varManager.getBitValue(6442 + index) != 0) {
                        member.varManager.sendBit(6442 + index, 0)
                    }
                }
                for (s in raid!!.spectators) {
                    val spectator: Player = getPlayer(s) ?: continue
                    if (spectator.varManager.getBitValue(6442 + index) != 0) {
                        spectator.varManager.sendBit(6442 + index, 0)
                    }
                }
                continue
            }
            val memberName = members.getOrNull(index) ?: continue
            val member: Player = getPlayer(memberName) ?: continue
            val previousStatus = statuses.getOrDefault(member, -1)
            val currentStatus = getStatus(member)
            if (previousStatus != currentStatus || force) {
                for (o in members) {
                    val otherMember: Player = getPlayer(o) ?: continue
                    otherMember.varManager.sendBit(6442 + index, currentStatus)
                }
                for (s in raid!!.spectators) {
                    val spectator: Player = getPlayer(s) ?: continue
                    spectator.varManager.sendBit(6442 + index, currentStatus)
                }
                statuses[member] = currentStatus
            }
        }
    }

    private fun updateStatusHUD(player: Player) {
        for (index in 0..4) {
            if (index >= members.size) {
                if (player.varManager.getBitValue(6442 + index) != 0) {
                    player.varManager.sendBit(6442 + index, 0)
                }
                continue
            }
            val memberName = members[index]
            val member = getPlayer(memberName) ?: continue
            val status = statuses.getOrDefault(member, getStatus(member))
            player.varManager.sendBit(6442 + index, if (player == member) 1 else status)
        }
    }

    fun getStatus(member: Player): Int {
        if (raid!!.getActiveRoom() != member.area && !member.getBooleanTemporaryAttribute("tob_advancing_room"))
            return 31
        else if (member.isDead || member.temporaryAttributes.containsKey("tob_jailed")) return 30

        val maxHp = 99.coerceAtMost(member.maxHitpoints).toDouble()
        val currentHp = 99.coerceAtMost(member.hitpoints).toDouble()
        val fraction = currentHp / maxHp
        return (1 + (26 * fraction).roundToInt())
    }

    companion object {

        fun getPlayer(username: String): Player? = World.getPlayer(username).orElse(null)

        private fun Player.informationString(hardMode: Boolean): String {
            return StringBuilder(name).append('|')
                .append(combatLevel).append('|')
                .append(skills.getLevelForXp(SkillConstants.ATTACK)).append('|')
                .append(skills.getLevelForXp(SkillConstants.STRENGTH)).append('|')
                .append(skills.getLevelForXp(SkillConstants.RANGED)).append('|')
                .append(skills.getLevelForXp(SkillConstants.MAGIC)).append('|')
                .append(skills.getLevelForXp(SkillConstants.DEFENCE)).append('|')
                .append(skills.getLevelForXp(SkillConstants.HITPOINTS)).append('|')
                .append(skills.getLevelForXp(SkillConstants.PRAYER)).append('|')
                .append(if (hardMode) tobStatsHard.completions else tobStats.completions).append('|')
                .toString()
        }

    }

}