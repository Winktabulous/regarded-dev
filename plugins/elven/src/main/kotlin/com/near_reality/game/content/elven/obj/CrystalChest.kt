package com.near_reality.game.content.elven.obj

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary
import com.zenyte.game.content.boons.impl.Locksmith
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.privilege.MemberRank
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the interaction of a crystal chest object.
 *
 *  @author Stan van der Bend
 *
 *  @author Kris | 04/04/2019 13:17
 *  @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
@Suppress("UNUSED")
class CrystalChest : ObjectAction{

    private val taverlyChest = Location(2914, 3452, 0)

    override fun handleObjectAction(player: Player, obj: WorldObject, name: String, optionId: Int, option: String) {

        if (player.inventory.containsItem(ItemId.CRYSTAL_KEY)) {

            if (obj.positionHash == taverlyChest.positionHash)
                player.achievementDiaries.update(FaladorDiary.UNLOCK_CRYSTAL_CHEST)

            player.animation = Animation(832)
            player.lock(2)
            if(player.boonManager.hasBoon(Locksmith::class.java) && Locksmith.roll()) {
                player.sendFilteredMessage("Your Locksmith perk saves your key from being consumed.")
            } else {
                player.inventory.deleteItem(ItemId.CRYSTAL_KEY, 1)
            }

            if (player.memberRank.equalToOrGreaterThan(MemberRank.EXPANSION) && Utils.random(getChance(player)) == 0) {
                player.sendMessage(Colour.RS_GREEN.wrap("You find double the loot from the crystal chest."))
                NewCrystalChestLoot.rollTable(player, false).forEach(player.inventory::addOrDrop)
            }
            NewCrystalChestLoot.rollTable(player, false).forEach(player.inventory::addOrDrop)
        } else
            player.sendMessage("This chest is securely locked shut.")
    }

    private fun getChance(player: Player): Int {
        val memberRank: MemberRank = player.memberRank
        return when {
            memberRank.equalToOrGreaterThan(MemberRank.UBER) -> 3
            memberRank.equalToOrGreaterThan(MemberRank.MYTHICAL) -> 3
            memberRank.equalToOrGreaterThan(MemberRank.LEGENDARY) -> 4
            memberRank.equalToOrGreaterThan(MemberRank.RESPECTED) -> 6
            memberRank.equalToOrGreaterThan(MemberRank.EXTREME) -> 6
            memberRank.equalToOrGreaterThan(MemberRank.EXPANSION) -> 9
            else -> 9
        }
    }

    override fun getObjects() = arrayOf(ObjectId.CLOSED_CHEST_172)
}
