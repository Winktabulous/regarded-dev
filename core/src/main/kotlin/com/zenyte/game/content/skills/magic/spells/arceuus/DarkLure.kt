package com.zenyte.game.content.skills.magic.spells.arceuus

import com.zenyte.game.content.skills.hunter.npc.ImplingNPC
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.magic.spelleffect.SpellEffect
import com.zenyte.game.world.entity.player.booleanVarbit

/**
 * @author Kris | 18/06/2022
 */
var Player.darkLureCooldown by booleanVarbit(12289)
var Entity.underDarkLure by attribute("under_dark_lure_effect", false)

class DarkLureEffect : SpellEffect {
    override fun spellEffect(player: Entity, target: Entity, damage: Int) {
        if (player is Player) {
            player.darkLureCooldown = true
            player.launchDarkLureCooldown()
        }
        target.underDarkLure = true
        target.launchDarkLureRemoval()
    }

    private fun Player.launchDarkLureCooldown() = WorldTasksManager.schedule({
        darkLureCooldown = false
    }, 16)

    private fun Entity.launchDarkLureRemoval() = WorldTasksManager.schedule({
        underDarkLure = false
    }, if (this is ImplingNPC) 10 else 99)
}