package com.near_reality.game.content.elven.obj

import com.zenyte.game.content.boons.impl.Locksmith
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Analytics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.privilege.MemberRank
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import mgi.utilities.StringFormatUtil

/**
 * Represents an [ObjectAction] plugin for the elven crystal key chest.
 *
 * @author Stan van der Bend (mostly taken from [CrystalChest])
 */
@Suppress("UNUSED")
class ElvenCrystalChest : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        chestObject: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        when(option) {
            "Open" -> {
                if (!tryHandle(player, ItemId.CRYSTAL_KEY) && !tryHandle(player, ItemId.ENHANCED_CRYSTAL_KEY))
                    player.sendMessage("This chest is securely locked shut.")
            }
            "Check" -> player.showTimesOpenedDialogue()
        }
    }

    private fun Player.showTimesOpenedDialogue() = dialogue {
        val times = player.variables.timesOpenedEnhancedCrystalChest

        plain("You have opened the Elven Crystal Chest " +
                "${StringFormatUtil.formatNumberUS(times)} time${if(times != 1) "s" else ""}.")
    }

    private fun tryHandle(player: Player, itemId: Int): Boolean {
        if (!player.inventory.containsItem(itemId, 1))
            return false
        if (itemId == ItemId.ENHANCED_CRYSTAL_KEY)
            player.variables.timesOpenedEnhancedCrystalChest++
        player.animation = animation
        player.lock(2)
        if(player.boonManager.hasBoon(Locksmith::class.java) && Locksmith.roll()) {
            player.sendFilteredMessage("Your Locksmith perk saves your key from being consumed.")
        } else {
            player.inventory.deleteItem(itemId, 1)
        }
        if (player.memberRank.equalToOrGreaterThan(MemberRank.EXPANSION) && Utils.random(getChance(player)) == 0) {
            player.sendMessage(Colour.RS_GREEN.wrap("You find double the loot from the elven crystal chest."))
            rollLoot(itemId, player)
        }
        rollLoot(itemId, player)
        player.inventory.addOrDrop(Item(995, Utils.random(25_000, 100_000)))
        Analytics.flagInteraction(player, Analytics.InteractionType.ELVEN_CRYSTAL_CHEST)
        return true
    }

    private fun rollLoot(itemId: Int, player: Player) {
        if (itemId == ItemId.CRYSTAL_KEY) {
            NewCrystalChestLoot.rollTable(player, false).forEach { player.inventory.addOrDrop(it) }
        } else {
            NewCrystalChestLoot.rollTable(player, true).forEach { player.inventory.addOrDrop(it) }
        }
    }

    private fun getChance(player: Player): Int {
        val rank = player.memberRank
        return when {
            rank.equalToOrGreaterThan(MemberRank.UBER) -> 3
            rank.equalToOrGreaterThan(MemberRank.MYTHICAL) -> 3
            rank.equalToOrGreaterThan(MemberRank.LEGENDARY) ->  4
            rank.equalToOrGreaterThan(MemberRank.RESPECTED) -> 6
            rank.equalToOrGreaterThan(MemberRank.EXTREME) ->  6
            rank.equalToOrGreaterThan(MemberRank.EXPANSION) -> 9
            else -> return 9
        }
    }

    override fun getObjects(): Array<Any> =
        arrayOf(ObjectId.ELVEN_CRYSTAL_CHEST_36582)

    private companion object {
        val animation = Animation(832)
    }
}
