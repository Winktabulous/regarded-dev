package com.zenyte.game.content.skills.magic.spells.arceuus

import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.content.skills.magic.spells.DefaultSpell
import com.zenyte.game.content.skills.prayer.actions.Bones
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants

/**
 * @author Kris | 19/06/2022
 */
@Suppress("unused")
class SinisterOffering : DefaultSpell {
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
        if (!player.hasBones()) {
            player.sendMessage("You aren't holding any suitable bones.")
            return false
        }
        player.prayerOfferingSpellCooldown = true
        player.animation = Animation(8975)
        player.graphics = Graphics(1872)
        player.sendSound(5033)
        player.convertBones()
        addXp(player, 180.0)
        player.cooldown()
        return true
    }

    private fun Player.hasBones(): Boolean {
        return inventory.container.items.values.any {
            val bone = Bones.getBone(it.id)
            bone != null && !(bone == Bones.SUPERIOR_DRAGON_BONES && skills.getLevelForXp(SkillConstants.PRAYER) < 70)
        }
    }

    private fun Player.convertBones() {
        var count = 3
        var prayerRestored = 0
        for (i in 0 until 28) {
            val item = inventory.getItem(i) ?: continue
            val bone = Bones.getBone(item.id) ?: continue
            if (bone == Bones.SUPERIOR_DRAGON_BONES && skills.getLevelForXp(SkillConstants.PRAYER) < 70) continue
            inventory.set(i, null)
            skills.addXp(SkillConstants.PRAYER, bone.xp * 3)
            prayerRestored += if (bone == Bones.SUPERIOR_DRAGON_BONES || bone == Bones.OURG_BONES || bone == Bones.DAGANNOTH_BONES || bone == Bones.HYDRA_BONES) 2 else 1
            if (--count <= 0) break
        }
        prayerManager.restorePrayerPoints(prayerRestored)
    }

    private fun Player.cooldown() = WorldTasksManager.schedule({
        prayerOfferingSpellCooldown = false
    }, 9)
}