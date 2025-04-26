package com.zenyte.game.content.skills.magic.spells.arceuus

import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.content.skills.magic.spells.DefaultSpell
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.booleanVarbit
import kotlin.math.min

/**
 * @author Kris | 18/06/2022
 */
private var Player.vileVigourCooldown by booleanVarbit(12292)
class VileVigour : DefaultSpell {
    override fun getDelay(): Int {
        return 1000
    }

    override fun getSpellbook(): Spellbook {
        return Spellbook.ARCEUUS
    }

    override fun spellEffect(player: Player, optionId: Int, option: String): Boolean {
        if (player.variables.runEnergy >= 100.0) {
            player.sendMessage("You're already at maximum run energy.")
            return false
        }
        if (player.vileVigourCooldown) {
            player.sendMessage("You can only cast Vile Vigour every 10 seconds.")
            return false
        }
        if (player.prayerManager.prayerPoints <= 0) {
            player.sendMessage("You don't have enough prayer points to cast that spell.")
            return false
        }
        val maxToRestore = min(player.prayerManager.prayerPoints, 100 - player.variables.runEnergy.toInt())
        require(maxToRestore in 0..100)
        player.vileVigourCooldown = true
        player.prayerManager.prayerPoints -= maxToRestore
        player.variables.runEnergy += maxToRestore.toDouble()
        player.animation = Animation(8978)
        player.graphics = Graphics(1876)
        player.sendSound(5049)
        addXp(player, 76.0)
        player.launchVileVigourCooldown()
        return true
    }

    private fun Player.launchVileVigourCooldown() = WorldTasksManager.schedule({
        vileVigourCooldown = false
    }, 16)
}