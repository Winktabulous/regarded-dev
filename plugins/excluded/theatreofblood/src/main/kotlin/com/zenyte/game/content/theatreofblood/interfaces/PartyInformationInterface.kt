package com.zenyte.game.content.theatreofblood.interfaces

import com.zenyte.game.GameInterface
import com.zenyte.game.content.skills.smithing.Smelting
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.party.PartyRights
import com.zenyte.game.content.theatreofblood.party.RaidingParty.Companion.getPlayer
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.util.AccessMask
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.plugins.dialogue.PlainChat

/**
 * @author Tommeh
 * @author Jire
 */
class PartyInformationInterface : Interface() {

    override fun attach() {
        put(0, 0, "Back")
        put(0, 1, "Refresh")
        put(0, 2, "Unblock")
        put(0, 3, "Set preferred size")
        put(0, 4, "Set preferred combat level")
        put(0, 5, "Apply/Disband/Leave/Withdraw")
        put(0, 6, "Mode")
        for (i in 0 until 5) {
            put(0, 7 + i, "PartyMemberOp$i")
        }
        for (i in 0 until 10) {
            put(0, 12 + i, "ApplicantsAcceptOp$i")
        }
        for (i in 0 until 10) {
            put(0, 22 + i, "ApplicantsDeclineOp$i")
        }
    }

    override fun open(player: Player) {
        super.open(player)

        player.packetDispatcher.sendComponentSettings(
            getInterface(),
            0,
            0,
            100,
            AccessMask.CLICK_OP1
        )
    }

    override fun build() {
        bind("Back", PartiesOverviewInterface::refresh)
        bind("Refresh") { player: Player ->
            val party = VerSinhazaArea.getParty(player, true) ?: return@bind
            party.run { player.updateInformation() }
        }
        bind("Apply/Disband/Leave/Withdraw") { player: Player ->
            val party = VerSinhazaArea.getParty(
                player,
                checkMembers = false,
                checkViewers = true,
                checkSpectators = true
            ) ?: return@bind
            when (party.run { player.partyRights }) {
                PartyRights.LEADER -> {
                    VerSinhazaArea.removeParty(party)
                    party.disband()
                }
                PartyRights.PARTY_MEMBER -> party.removeMember(player)
                PartyRights.APPLICANT -> {
                    party.applicants.remove(player.username)
                    party.run { player.updateInformation() }
                    val leader = party.leader!!
                    if (leader.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION))
                        party.run { leader.updateInformation() }
                }
                PartyRights.CAN_APPLY -> {
                    val currentParty = VerSinhazaArea.getParty(player)
                    if (currentParty != null) {
                        player.interfaceHandler.closeInterface(getInterface())
                        player.dialogueManager.start(object : Dialogue(player) {
                            override fun buildDialogue() {
                                options(
                                    "You are already in a party.",
                                    "Stay in my existing party.",
                                    "Quit that one and apply to this one."
                                ).onOptionOne { party.run { player.updateInformation() } }
                                    .onOptionTwo {
                                        currentParty.removeMember(player)
                                        party.addApplicant(player)
                                        party.run { player.updateInformation() }
                                    }
                            }
                        })
                        player.sendMessage("You cannot apply for this party if you're already in another one.")
                        return@bind
                    }
                    if (party.applicants.size == 10) {
                        player.sendMessage("There are too many applicants for this party at the moment.")
                        return@bind
                    }
                    if (party.blocked.contains(player.username)) {
                        player.sendMessage("You have already applied for this party. Wait until the party leader either accepts or rejects it.")
                        return@bind
                    }
                    if (party.applicants.contains(player.username)) {
                        return@bind
                    }
                    party.addApplicant(player)
                }
                PartyRights.BLOCKED_APPLICANT -> {
                    player.interfaceHandler.closeInterfaces()
                    player.dialogueManager.start(PlainChat(player, "You have been declined by this party."))
                }
            }
        }
        for (i in 0 until 10) {
            bind("ApplicantsAcceptOp$i") { player: Player, slotId: Int, _: Int, option: Int ->
                val index = slotId - 12
                val party = VerSinhazaArea.getParty(player) ?: return@bind
                if (index >= party.applicants.size) {
                    return@bind
                }
                val username = party.applicants[index]
                val applicant = getPlayer(username) ?: return@bind
                if (party.members.size == 5) {
                    player.sendMessage("Your raiding party is already full.")
                    return@bind
                }
                if (VerSinhazaArea.getParty(applicant) != null) {
                    party.applicants.remove(applicant.username)
                    player.interfaceHandler.closeInterface(getInterface())
                    player.dialogueManager.start(
                        PlainChat(player, applicant.name + " appears to have joined another party.")
                    )
                    return@bind
                }
                if (party.applicants.remove(applicant.username)) {
                    party.members.add(applicant.username)
                    party.originalMembers.add(applicant.username)
                }
                party.members.forEach { m ->
                    val member = getPlayer(m) ?: return@forEach
                    PartyOverlayInterface.refresh(member, party)
                }
                if (applicant.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION))
                    party.run { applicant.updateInformation() }
                if (player.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION))
                    party.run { player.updateInformation() }
            }
        }
        for (i in 0 until 10) {
            bind("ApplicantsDeclineOp$i") { player: Player, slotId: Int, _: Int, option: Int ->
                val index = slotId - 22
                val party = VerSinhazaArea.getParty(player) ?: return@bind
                if (index >= party.applicants.size) {
                    return@bind
                }
                val username = party.applicants[index]
                val applicant = getPlayer(username) ?: return@bind
                applicant.sendMessage("Your application to the party of ${party.leader!!.name} has been declined")
                applicant.sendSound(rejectSound)
                party.blocked.add(applicant.username)
                party.applicants.remove(applicant.username)
                if (applicant.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION))
                    party.run { applicant.updateInformation() }
                if (player.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION))
                    party.run { player.updateInformation() }
            }
        }
        bind("Set preferred size") { player: Player ->
            val party = VerSinhazaArea.getParty(player) ?: return@bind
            if (!party.isLeader(player)) return@bind
            player.interfaceHandler.closeInterfaces()
            player.sendInputInt("Set a preferred party size (or 0 to clear it)") { s: Int ->
                val size = 5.coerceAtMost(s)
                party.preferredSize = size
                party.run { player.updateInformation() }
            }
        }
        bind("Set preferred combat level") { player: Player ->
            val party = VerSinhazaArea.getParty(player) ?: return@bind
            if (!party.isLeader(player)) return@bind
            player.interfaceHandler.closeInterfaces()
            player.sendInputInt("Set a preferred combat level (or 0 to clear it)") { l: Int ->
                val level = 126.coerceAtMost(l)
                party.preferredCombatLevel = level
                party.run { player.updateInformation() }
            }
        }
        bind("Mode") { player: Player ->
            val party = VerSinhazaArea.getParty(player) ?: return@bind
            if (true) {
                party.run { player.updateInformation() }
                player.sendMessage("Raid mode is currently disabled.")
                return@bind
            }
            if (!party.isLeader(player)) return@bind
            player.interfaceHandler.closeInterfaces()
            //TODO entry? 2030
            player.options("Select a mode. Currently set to ${if (party.hardMode) "Hard" else "Normal"} Mode.") {
                "Normal Mode." {
                    party.hardMode = false
                    party.run { player.updateInformation() }
                }
                "Hard Mode." {
                    //TODO unlocking of hard mode? maybe
                    party.hardMode = true
                    party.run { player.updateInformation() }
                }
            }
        }
        bind("Unblock") { player: Player ->
            val party = VerSinhazaArea.getParty(player) ?: return@bind
            if (!party.isLeader(player)) return@bind
            val usernames: MutableSet<String> = HashSet(party.blocked)
            party.blocked.clear()
            for (username in usernames) {
                val p = getPlayer(username) ?: continue
                if (p.interfaceHandler.isPresent(GameInterface.TOB_PARTY_INFORMATION))
                    party.run { p.updateInformation() }
            }
        }
        for (i in 0 until 5) {
            bind("PartyMemberOp$i") { player: Player, slotId: Int, _: Int, _: Int ->
                val party = VerSinhazaArea.getParty(player) ?: return@bind
                if (!party.isLeader(player)) return@bind
                if (slotId == 0) {
                    if (party.members.size == 1) {
                        VerSinhazaArea.removeParty(party)
                        party.disband()
                    } else {
                        val member = party.getMember(0) ?: return@bind
                        party.removeMember(member)
                    }
                    return@bind
                }
                val index = slotId - 7
                if (index > party.members.size) return@bind
                val member = party.getMember(index) ?: return@bind
                party.removeMember(member)
            }
        }
    }

    override fun getInterface() = GameInterface.TOB_PARTY_INFORMATION

    private companion object {

        val rejectSound = SoundEffect(2277)

    }

}