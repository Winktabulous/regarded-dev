package com.zenyte.game.content.boss.abyssalsire

import com.zenyte.game.content.boss.abyssalsire.WeakReferenceHelper.exists
import com.zenyte.game.content.boss.abyssalsire.respiratorysystems.AbyssalSireRespiratorySystem
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell
import com.zenyte.game.world.region.PolygonRegionArea
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin

/**
 * @author Jire
 * @author Kris
 */
open class AbyssalNexusArea
internal constructor() :
    PolygonRegionArea(), CannonRestrictionPlugin, PlayerCombatPlugin {

    override fun polygons() = arrayOf(RSPolygon(11850, 12363))

    override fun enter(player: Player) {
        player.viewDistance = Player.SMALL_VIEWPORT_RADIUS shl 1
    }

    override fun leave(player: Player, logout: Boolean) = player.resetViewDistance()

    override fun name() = "Abyssal Nexus"

    override fun restrictionMessage() = "That horrible slime on the ground makes this area unsuitable for a cannon."

    override fun processCombat(player: Player, entity: Entity, style: String): Boolean {
        if (entity is AbyssalSire && entity.target.exists() && entity.target!!.get() != player) {
            player.sendMessage("Someone else is fighting the Sire.")
            return false
        } else if (entity is AbyssalSireRespiratorySystem && entity.sireRef.exists()) {
            val sire = entity.sireRef.get()!!
            if (sire.target.exists() && sire.target!!.get() != player) {
                player.sendMessage("Someone else is fighting the Sire.")
                return false
            }
        }

        return true
    }

    override fun onAttack(player: Player, entity: Entity, style: String?, spell: CombatSpell?, splash: Boolean) {
        if (splash && entity is AbyssalSire) {
            entity.onScheduledAttack(player, spell)
        }
    }

    private companion object {
        const val CENTER_PATHWAY_REGION_ID = 12106
    }

}