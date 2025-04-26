package com.zenyte.game.content.skills.magic.spells.arceuus

import com.zenyte.game.content.skills.herblore.actions.Combine.HerbloreData
import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.content.skills.magic.spells.DefaultSpell
import com.zenyte.game.item.Item
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants

/**
 * @author Kris | 18/06/2022
 */
class Degrime : DefaultSpell {
    override fun getDelay(): Int {
        return 1000
    }

    override fun getSpellbook(): Spellbook {
        return Spellbook.ARCEUUS
    }

    override fun spellEffect(player: Player, optionId: Int, option: String): Boolean {
        if (!player.hasAnyGrimyHerbs()) {
            player.sendMessage("You don't have any suitable herbs to clean.")
            return false
        }
        player.animation = Animation(8981)
        player.lock(10)
        player.castSpell()
        return true
    }

    private fun Player.castSpell() = WorldTasksManager.schedule(object : TickTask() {
        override fun run() {
            val ticks = ticks++
            if (ticks == 4) {
                animation = Animation(8980)
                graphics = Graphics(1885)
                sendSound(5052)
            } else if (ticks == 6) {
                unlock()
                skills.addXp(SkillConstants.MAGIC, 83.0)
                for (i in 0 until 28) {
                    val item = inventory.getItem(i) ?: continue
                    val herb = herbs.find { it.grimyHerbId() == item.id } ?: continue
                    inventory.set(i, Item(herb.cleanHerbId()))
                    skills.addXp(SkillConstants.HERBLORE, herb.xp / 2.0)
                }
                stop()
            }
        }
    }, 0, 0)

    private fun Player.hasAnyGrimyHerbs(): Boolean = herbs.any { inventory.containsItem(it.grimyHerbId()) }

    private companion object {
        private val herbs = HerbloreData.HERBLORE.values.mapNotNull { e ->
            if (e.ordinal >= HerbloreData.GUAM_LEAF.ordinal && e.ordinal <= HerbloreData.TORSTOL.ordinal) {
                e
            } else {
                null
            }
        }

        private fun HerbloreData.grimyHerbId(): Int {
            return materials[0].id
        }

        private fun HerbloreData.cleanHerbId(): Int {
            return product.id
        }
    }
}