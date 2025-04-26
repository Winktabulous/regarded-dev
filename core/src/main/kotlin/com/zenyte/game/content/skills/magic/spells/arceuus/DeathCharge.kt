package com.zenyte.game.content.skills.magic.spells.arceuus

import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.content.skills.magic.spells.DefaultSpell
import com.zenyte.game.gameClock
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.booleanVarbit
import com.zenyte.game.world.entity.player.clock

/**
 * @author Kris | 19/06/2022
 */
private var Player.deathChargeCooldown by booleanVarbit(12138)
var Player.deathChargeEffect by booleanVarbit(12411)
private var Player.deathChargeSetClock by clock("death_charge_set_clock")

fun Player.invokeDeathChargeEffect() {
    if (!deathChargeEffect) return
    sendMessage("<col=a53fff>Some of your special attack energy has been restored.</col>")
    deathChargeEffect = false
    combatDefinitions.specialEnergy = combatDefinitions.specialEnergy + 15
}

class DeathCharge : DefaultSpell {
    override fun getDelay(): Int {
        return 1000
    }

    override fun getSpellbook(): Spellbook {
        return Spellbook.ARCEUUS
    }

    override fun spellEffect(player: Player, optionId: Int, option: String): Boolean {
        if (player.deathChargeEffect) {
            player.sendMessage("You already have a Death Charge pending on you.")
            return false
        }
        if (player.deathChargeCooldown) {
            player.sendMessage("Death Charge is still on cooldown.")
            return false
        }
        player.sendMessage("<col=a53fff>Upon the death of your next foe, some of your special attack energy will be restored.</col>")
        player.deathChargeCooldown = true
        player.deathChargeEffect = true
        player.animation = Animation(8970)
        player.graphics = Graphics(1854)
        player.sendSound(5034)
        addXp(player, 90.0)
        player.deathChargeSetClock = gameClock()
        player.scheduleExpiration(gameClock())
        player.scheduleEffectTimeout(gameClock())
        return true
    }

    private fun Player.scheduleExpiration(clock: Int) = WorldTasksManager.schedule({
        if (clock != deathChargeSetClock) return@schedule
        deathChargeCooldown = false
    }, 100)

    private fun Player.scheduleEffectTimeout(clock: Int) = WorldTasksManager.schedule({
        if (clock != deathChargeSetClock) return@schedule
        sendMessage("<col=a53fff>Your Death Charge has faded away.</col>")
        sendSound(5056)
        deathChargeEffect = false
    }, skills.getLevel(SkillConstants.MAGIC))
}
