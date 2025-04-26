package com.near_reality.game.content.wilderness.king_black_dragon

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary
import com.zenyte.game.content.skills.magic.spells.teleports.structures.LeverTeleport
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.game.world.region.dynamicregion.MapBuilder
import mgi.utilities.StringFormatUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Kris | 16/03/2019 20:03
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
@Suppress("unused")
class KingBlackDragonLever : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        when (option) {
            "Pull" -> LeverTeleport(
                KingBlackDragonInstance.insideTile,
                `object`,
                "... and teleport into the Dragon\'s lair."
            ) {
                player.achievementDiaries.update(
                    WildernessDiary.ENTER_KING_BLACK_DRAGON_LAIR
                )
            }.teleport(player)

            "Commune" -> {
                val playerCount = GlobalAreaManager["King Black Dragon Lair"].players.size
                player.sendMessage("The Lever magically communicates with you, saying there " + (if (playerCount == 0) "are no adventurers" else (if (playerCount == 1) "is 1 adventurer" else "are $playerCount adventurers")) + " inside the lair.")
            }

            "Private" -> {
                if (!player.inventory.containsItem(KingBlackDragonInstance.price)) {
                    player.sendMessage(
                        "A private King Black Dragon lair costs " + StringFormatUtil.format(
                            KingBlackDragonInstance.price.amount
                        ) + " coins. The lever cannot take funds from your bank."
                    )
                    return
                }
                player.dialogueManager.start(object : Dialogue(player) {
                    override fun buildDialogue() {
                        options(
                            "Enter a private King Black Dragon lair?", "Pay " + StringFormatUtil.format(
                                KingBlackDragonInstance.price.amount
                            ) + " coins.", "Cancel."
                        ).onOptionOne {
                            try {
                                if (!player.inventory.containsItem(KingBlackDragonInstance.price)) {
                                    player.sendMessage("Not enough coins in your inventory.")
                                    return@onOptionOne
                                }
                                val area = MapBuilder.findEmptyChunk(8, 8)
                                val instance = KingBlackDragonInstance(player, area, 281, 584)
                                instance.constructRegion()
                                player.inventory.deleteItem(KingBlackDragonInstance.price)
                            } catch (e: Exception) {
                                log.error("", e)
                            }
                        }
                    }
                })
            }
        }
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(ObjectId.LEVER_1816)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(KingBlackDragonLever::class.java)
    }
}
