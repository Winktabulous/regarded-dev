package com.near_reality.content.group_ironman.`object`

import com.near_reality.content.group_ironman.IronmanGroupType
import com.near_reality.content.group_ironman.dialogue.ChangeGroupIronManModeDialogue
import com.near_reality.content.group_ironman.player.finalisedIronmanGroup
import com.near_reality.content.group_ironman.player.inIronmanGroupCreationInterface
import com.near_reality.content.group_ironman.player.ironmanGroupType
import com.near_reality.scripts.`object`.actions.ObjectActionScript
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectId.ARMOUR_CRATE
import com.zenyte.game.world.`object`.ObjectId.HARDCORE_ARMOUR_CRATE

class ArmourCrateObjectAction : ObjectActionScript() {
    init {
        ARMOUR_CRATE.armourCrate(IronmanGroupType.NORMAL)
        //HARDCORE_ARMOUR_CRATE.armourCrate(IronmanGroupType.HARDCORE)
        HARDCORE_ARMOUR_CRATE {
            player.dialogue {
                plain("Currently only non-hardcore groups are accepted, " +
                        "support for Hardcore groups will soon be a reality.")
            }
        }
    }

    fun Int.armourCrate(groupType: IronmanGroupType) = invoke {
        when {
            player.finalisedIronmanGroup != null ->
                player.dialogue(NpcId.GROUP_IRON_TUTOR) { npc("You cannot change your Group Iron Mode as you are already part of a ${player.finalisedIronmanGroup!!.allMembers.size}-player Iron group.") }
            player.inIronmanGroupCreationInterface ->
                player.dialogue { plain("You can't change your game mode or get a new helmet while creating a group.") }
            player.ironmanGroupType != groupType ->
                player.dialogueManager.start(ChangeGroupIronManModeDialogue(player, groupType))
            else -> {
                val helmet = Item(groupType.helmetId, 1)
                if (player.containsItem(helmet))
                    player.dialogue { plain("You already have a helmet.") }
                else {
                    player.inventory.addItem(helmet)
                    player.dialogue { item(helmet, "You take a helmet.") }
                }
            }
        }
    }

}