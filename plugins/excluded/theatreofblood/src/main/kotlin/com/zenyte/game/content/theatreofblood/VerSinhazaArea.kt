package com.zenyte.game.content.theatreofblood

import com.zenyte.game.GameInterface
import com.zenyte.game.content.theatreofblood.interfaces.PartyOverlayInterface
import com.zenyte.game.content.theatreofblood.party.RaidingParty
import com.zenyte.game.content.theatreofblood.room.TheatreRoom
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.area.Morytania
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import org.slf4j.LoggerFactory

/**
 * @author Tommeh
 * @author Jire
 */
class VerSinhazaArea : Morytania(), CycleProcessPlugin {

    override fun polygons() = Companion.polygons

    override fun enter(player: Player) {
        PartyOverlayInterface.refresh(player, null)
        PartyOverlayInterface.fade(player, 200, 0, "")
    }

    override fun leave(player: Player, logout: Boolean) {
        val nextArea = GlobalAreaManager.getArea(player.location)
        if (nextArea is TheatreRoom) return
        player.interfaceHandler.closeInterface(GameInterface.TOB_PARTY)

        val party = getParty(player, true) ?: return
        val name = player.username
        if (party.members.contains(name) || party.applicants.contains(name)) {
            if (party.members.contains(name))
                party.removeMember(player)
            else party.applicants.remove(name)
        }
    }

    override fun name() = "Ver Sinhaza"

    override fun process() {}

    internal companion object {

        lateinit var statistics : Array<TheatreOfBloodScores>

        internal val logger = LoggerFactory.getLogger(VerSinhazaArea::class.java)

        val raidingParties: Int2ObjectMap<RaidingParty> = Int2ObjectLinkedOpenHashMap()

        private var nextPartyID = 0

        fun createParty(host: Player) = RaidingParty(nextPartyID++, host).apply {
            raidingParties.put(id, this)
        }

        fun removeParty(party: RaidingParty) = raidingParties.remove(party.id)

        fun getParty(id: Int) = raidingParties[id]

        fun getPartyByIndex(index: Int): RaidingParty? {
            val keys = raidingParties.keys
            if (index >= keys.size) return null

            val ids = keys.toIntArray()
            val partyID = ids[index]
            return raidingParties[partyID]
        }

        fun getParty(
            player: Player,
            checkMembers: Boolean,
            checkViewers: Boolean,
            checkSpectators: Boolean,
            checkApplicants: Boolean
        ): RaidingParty? {
            for (party in raidingParties.values) {
                if (checkMembers
                    && party.members.contains(player.username)
                    || checkViewers && player.getNumericTemporaryAttribute("tob_viewing_party_id").toInt() == party.id
                    || checkSpectators && party.raid != null && party.raid!!.spectators.contains(player.username)
                    || checkApplicants && party.applicants.contains(player.username)
                ) return party
            }
            return null
        }

        fun getParty(
            player: Player,
            checkMembers: Boolean,
            checkViewers: Boolean,
            checkSpectators: Boolean
        ) = getParty(player, checkMembers, checkViewers, checkSpectators, false)

        fun getParty(player: Player, checkViewers: Boolean) =
            getParty(player, false, checkViewers, false)

        fun getParty(player: Player) =
            getParty(player, checkMembers = true, checkViewers = false, checkSpectators = false)

        fun getArea(player: Player): TheatreRoom? {
            val party = getParty(player) ?: return null
            party.raid ?: return null
            return player.area as? TheatreRoom
        }

        private val polygons = arrayOf(
            RSPolygon(
                arrayOf(
                    intArrayOf(3643, 3239),
                    intArrayOf(3658, 3239),
                    intArrayOf(3662, 3237),
                    intArrayOf(3664, 3237),
                    intArrayOf(3668, 3239),
                    intArrayOf(3683, 3239),
                    intArrayOf(3687, 3235),
                    intArrayOf(3687, 3225),
                    intArrayOf(3688, 3224),
                    intArrayOf(3689, 3224),
                    intArrayOf(3693, 3228),
                    intArrayOf(3696, 3228),
                    intArrayOf(3698, 3226),
                    intArrayOf(3698, 3225),
                    intArrayOf(3697, 3224),
                    intArrayOf(3697, 3223),
                    intArrayOf(3696, 3222),
                    intArrayOf(3697, 3221),
                    intArrayOf(3698, 3221),
                    intArrayOf(3699, 3220),
                    intArrayOf(3699, 3219),
                    intArrayOf(3698, 3218),
                    intArrayOf(3697, 3218),
                    intArrayOf(3696, 3217),
                    intArrayOf(3697, 3216),
                    intArrayOf(3697, 3215),
                    intArrayOf(3698, 3214),
                    intArrayOf(3698, 3213),
                    intArrayOf(3696, 3211),
                    intArrayOf(3693, 3211),
                    intArrayOf(3689, 3215),
                    intArrayOf(3688, 3215),
                    intArrayOf(3687, 3214),
                    intArrayOf(3687, 3204),
                    intArrayOf(3683, 3200),
                    intArrayOf(3668, 3200),
                    intArrayOf(3666, 3198),
                    intArrayOf(3665, 3198),
                    intArrayOf(3664, 3199),
                    intArrayOf(3662, 3199),
                    intArrayOf(3661, 3198),
                    intArrayOf(3660, 3198),
                    intArrayOf(3658, 3200),
                    intArrayOf(3643, 3200),
                    intArrayOf(3638, 3205),
                    intArrayOf(3638, 3211),
                    intArrayOf(3637, 3210),
                    intArrayOf(3636, 3210),
                    intArrayOf(3635, 3211),
                    intArrayOf(3635, 3213),
                    intArrayOf(3634, 3214),
                    intArrayOf(3632, 3214),
                    intArrayOf(3631, 3215),
                    intArrayOf(3631, 3216),
                    intArrayOf(3633, 3218),
                    intArrayOf(3633, 3221),
                    intArrayOf(3631, 3223),
                    intArrayOf(3631, 3224),
                    intArrayOf(3632, 3225),
                    intArrayOf(3634, 3225),
                    intArrayOf(3635, 3226),
                    intArrayOf(3635, 3228),
                    intArrayOf(3636, 3229),
                    intArrayOf(3637, 3229),
                    intArrayOf(3638, 3228),
                    intArrayOf(3638, 3234)
                )
            )
        )

    }

}