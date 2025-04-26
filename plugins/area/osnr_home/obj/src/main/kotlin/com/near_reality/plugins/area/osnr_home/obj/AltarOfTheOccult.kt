package com.near_reality.plugins.area.osnr_home.obj

import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.start
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

class AltarOfTheOccult : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        val spellbook = when (option) {
            "Standard" -> Spellbook.NORMAL
            "Ancient" -> Spellbook.ANCIENT
            "Lunar" -> Spellbook.LUNAR
            "Arceuus" -> Spellbook.ARCEUUS
            else -> return
        }
        if (player.combatDefinitions.spellbook == spellbook) {
            player.dialogueManager.start {
                plain("You are already using this spellbook.<br><br>Choose a different one.")
            }
            return
        }
        player.animation = Animation(PRAY_ANIM)
        player.combatDefinitions.setSpellbook(spellbook, true)
        player.dialogueManager.start {
            item(Item(ItemId.ANCIENT_STAFF), "Your spellbook has been changed.")
        }
    }

    override fun getObjects() = arrayOf(24911, ObjectId.ALTAR_OF_THE_OCCULT)

    private companion object {
        const val PRAY_ANIM = 645
    }

}
