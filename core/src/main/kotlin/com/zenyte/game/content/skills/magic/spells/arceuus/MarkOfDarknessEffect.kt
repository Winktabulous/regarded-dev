package com.zenyte.game.content.skills.magic.spells.arceuus

import com.zenyte.game.gameClock
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.action.combat.magic.spelleffect.SpellEffect
import com.zenyte.game.world.entity.player.clock
import kotlin.math.max

/**
 * @author Kris | 16/06/2022
 */
var Entity.hasMarkOfDarkness by attribute("mark_of_darkness_effect", false)
private var Entity.markOfDarknessSetClock by clock("mark_of_darkness_set_clock")

class MarkOfDarknessEffect : SpellEffect {
    override fun spellEffect(player: Entity, target: Entity, damage: Int) {
        if (player !is Player) return
        player.skills.addXp(SkillConstants.MAGIC, 2.0)
        player.actionManager.forceStop()
        player.resetWalkSteps()
        target.hasMarkOfDarkness = true
        player.markOfDarknessSetClock = gameClock()
        target.markOfDarknessSetClock = gameClock()
        if (target is Player) {
            target.sendMessage("<col=a53fff>A Mark of Darkness has been placed upon you.</col>")
            target.sendSound(5015)
        }
        target.scheduleMarkOfDarknessExpiration(player)
    }

    private fun Entity.scheduleMarkOfDarknessExpiration(caster: Player) = WorldTasksManager.schedule({
        if (markOfDarknessSetClock != caster.markOfDarknessSetClock) return@schedule
        if (this is Player) {
            sendMessage("<col=6800bf>Your Mark of Darkness has faded away.</col>")
            sendSound(5000)
        }
        graphics = Graphics(1886)
        hasMarkOfDarkness = false
    }, max(6, caster.skills.getLevel(SkillConstants.MAGIC)))
}