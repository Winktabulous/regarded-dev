package com.zenyte.game.content.skills.magic.spells.arceuus

import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.content.skills.magic.spells.DefaultSpell
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player

/**
 * @author Kris | 19/06/2022
 */
class LesserCorruption : DefaultSpell {
    override fun getDelay(): Int {
        return 1000
    }

    override fun getSpellbook(): Spellbook {
        return Spellbook.ARCEUUS
    }

    override fun spellEffect(player: Player, optionId: Int, option: String): Boolean {
        if (player.corruptionSpellCooldown) {
            player.sendMessage("You can only cast corruption spells every 30 seconds.")
            return false
        }
        player.animation = Animation(8977)
        player.graphics = Graphics(1877)
        World.sendSoundEffect(player.location, SoundEffect(5037, 10))
        player.corruptionSpellCooldown = true
        player.corruptionType = 1
        addXp(player, 75.0)
        player.corruptionCooldown()
        return true
    }
}