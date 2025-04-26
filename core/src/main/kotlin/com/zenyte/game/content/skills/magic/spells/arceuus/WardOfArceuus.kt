package com.zenyte.game.content.skills.magic.spells.arceuus

import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.content.skills.magic.spells.DefaultSpell
import com.zenyte.game.gameClock
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.booleanVarbit
import com.zenyte.game.world.entity.player.clock

/**
 * @author Kris | 16/06/2022
 */
var Entity.hasWardOfArceuus by attribute("ward_of_arceuus_effect", false)
private var Player.wardOfArceuusCooldown by booleanVarbit(12293)
private var Entity.wardOfArceuusSetClock by clock("ward_of_arceuus_set_clock")
class WardOfArceuus : DefaultSpell {
    override fun getDelay(): Int {
        return 1000
    }

    override fun getSpellName(): String {
        return "ward of arceuus"
    }

    override fun getSpellbook(): Spellbook {
        return Spellbook.ARCEUUS
    }

    override fun spellEffect(player: Player, optionId: Int, option: String): Boolean {
        if (player.wardOfArceuusCooldown) {
            player.sendMessage("You can only cast Ward of Arceuus every 30 seconds.")
            return false
        }
        player.hasWardOfArceuus = true
        player.wardOfArceuusCooldown = true
        player.wardOfArceuusSetClock = gameClock()
        player.sendMessage("<col=3366ff>Your defence against Arceuus magic has been strengthened.</col>")
        player.graphics = Graphics(1851)
        player.animation = Animation(8970)
        player.sendSound(5044)
        addXp(player, 83.0)
        player.queueWardOfArceuusCooldownExpiration()
        player.queueWardOfArceuusExpiration(gameClock())
        return true
    }

    private fun Player.queueWardOfArceuusCooldownExpiration() = WorldTasksManager.schedule({
        wardOfArceuusCooldown = false
    }, 50)


    private fun Player.queueWardOfArceuusExpiration(expiration: Int) = WorldTasksManager.schedule({
        if (expiration != wardOfArceuusSetClock) return@schedule
        sendMessage("<col=3366ff>Your Ward of Arceuus has expired.</col>")
        sendSound(5043)
        hasWardOfArceuus = false
    }, skills.getLevel(SkillConstants.MAGIC))
}