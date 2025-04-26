package com.zenyte.game.content.skills.magic.spells.arceuus

import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.content.skills.magic.spells.DefaultSpell
import com.zenyte.game.content.skills.prayer.actions.Bones
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.booleanVarbit

/**
 * @author Kris | 19/06/2022
 */
var Player.prayerOfferingSpellCooldown by booleanVarbit(12423)
@Suppress("unused")
class DemonicOffering : DefaultSpell {
    override fun getDelay(): Int {
        return 1000
    }

    override fun getSpellbook(): Spellbook {
        return Spellbook.ARCEUUS
    }

    override fun spellEffect(player: Player, optionId: Int, option: String): Boolean {
        if (player.prayerOfferingSpellCooldown) {
            player.sendMessage("You can only cast offering spells every 5 seconds.")
            return false
        }
        if (!player.hasAshes()) {
            player.sendMessage("You aren't holding any demonic ashes.")
            return false
        }
        player.prayerOfferingSpellCooldown = true
        player.animation = Animation(8975)
        player.graphics = Graphics(1871)
        player.sendSound(5051)
        addXp(player, 175.0)
        player.convertAshes()
        player.cooldown()
        return true
    }

    private fun Player.hasAshes(): Boolean {
        return inventory.container.items.values.any {
            Bones.getAsh(it.id) != null
        }
    }

    private fun Player.convertAshes() {
        var count = 3
        var prayerRestored = 0
        for (i in 0 until 28) {
            val item = inventory.getItem(i) ?: continue
            val bone = Bones.getAsh(item.id) ?: continue
            inventory.set(i, null)
            skills.addXp(SkillConstants.PRAYER, bone.xp * 3)
            prayerRestored += if (bone == Bones.INFERNAL_ASHES) 2 else 1
            if (--count <= 0) break
        }
        prayerManager.restorePrayerPoints(prayerRestored)
    }

    private fun Player.cooldown() = WorldTasksManager.schedule({
        prayerOfferingSpellCooldown = false
    }, 9)
}