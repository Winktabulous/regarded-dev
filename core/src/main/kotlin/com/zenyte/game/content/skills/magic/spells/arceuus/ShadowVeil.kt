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
 * @author Kris | 18/06/2022
 */
private var Player.shadowVeilSetClock by clock("shadow_veil_set_clock")
private var Player.shadowVeilCooldown by booleanVarbit(12291)
var Player.shadowVeilEffect by booleanVarbit(12414)
class ShadowVeil : DefaultSpell {
    override fun getDelay(): Int {
        return 1000
    }

    override fun getSpellbook(): Spellbook {
        return Spellbook.ARCEUUS
    }

    override fun spellEffect(player: Player, optionId: Int, option: String): Boolean {
        if (player.shadowVeilCooldown) {
            player.sendMessage("You can only cast Shadow Veil every 30 seconds.")
            return false
        }
        player.shadowVeilSetClock = gameClock()
        player.shadowVeilCooldown = true
        player.shadowVeilEffect = true
        player.sendMessage("<col=a53fff>Your thieving abilities have been enhanced.</col>")
        player.animation = Animation(8979)
        player.graphics = Graphics(1881)
        player.sendSound(5032)
        addXp(player, 58.0)
        player.launchShadowVeilCooldown()
        player.launchShadowVeilExpiration(gameClock())
        return true
    }

    private fun Player.launchShadowVeilCooldown() = WorldTasksManager.schedule({
        shadowVeilCooldown = false
    }, 30)

    private fun Player.launchShadowVeilExpiration(clock: Int) = WorldTasksManager.schedule({
        if (shadowVeilSetClock != clock) return@schedule
        shadowVeilEffect = false
        sendMessage("<col=a53fff>Your Shadow Veil has faded away.</col>")
        sendSound(5062)
    }, skills.getLevel(SkillConstants.MAGIC))
}