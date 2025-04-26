@file:Suppress("FunctionName")
package com.near_reality.content.group_ironman.npc.actions

import com.near_reality.content.group_ironman.IronmanGroup
import com.near_reality.content.group_ironman.IronmanGroupMember
import com.near_reality.content.group_ironman.IronmanGroupType
import com.near_reality.content.group_ironman.player.finalisedIronmanGroup
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Colour
import com.zenyte.game.util.ItemUtil
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.OptionsBuilder
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.plugins.renewednpc.IronmanTutorNPC
import com.zenyte.plugins.renewednpc.ZenyteGuide
import com.zenyte.utils.TextUtils
import org.apache.commons.lang3.ArrayUtils

class GroupIronTutorNpcAction : NPCActionScript() {
    init {
        npcs(NpcId.GROUP_IRON_TUTOR)

        "Talk-to" {
            player.dialogue(npc) {
                player("Hey there.")
                npc(
                    "Hail, Group Iron Man! My name is Tony, and I'm " +
                            "here to teach you everything you need to know about " +
                            "Group Iron Men. What can I do for you?"
                )
                options {
                    `What is a Group Iron Man`()

                    val group = player.finalisedIronmanGroup
                    if (group != null) {
                        if (group.isLeader(player)) {
                            `I'd like to manage my group`(group)
                        }
                    } else {
                        `How do I join or create a group`()
                    }
                    `How do I leave a group`()
                    `Can I change my Group Iron mode`()
                    `I'm fine, thanks`()
                }
            }
        }

        "Armour" {
            val group = IronmanGroup.find(player)
            if (group == null) {
                player.sendMessage("You must be in a group to claim a set of armour.")
            } else {
                player.stopAll()
                player.faceEntity(npc)
                val armour = mutableListOf<Item>()
                if (group.type == IronmanGroupType.HARDCORE) {
                    armour.add(Item(ItemId.HARDCORE_GROUP_IRON_HELM))
                    when (group.activeMembers.size) {
                        2 -> armour.add(Item(ItemId.HARDCORE_GROUP_IRON_PLATEBODY))
                        3 -> armour.add(Item(ItemId.HARDCORE_GROUP_IRON_PLATEBODY_26174))
                        4 -> armour.add(Item(ItemId.HARDCORE_GROUP_IRON_PLATEBODY_26176))
                        5 -> armour.add(Item(ItemId.HARDCORE_GROUP_IRON_PLATEBODY_26178))
                    }
                    armour.add(Item(ItemId.HARDCORE_GROUP_IRON_PLATELEGS))
                    armour.add(Item(ItemId.HARDCORE_GROUP_IRON_BRACERS))
                } else {
                    armour.add(Item(ItemId.GROUP_IRON_HELM))
                    when (group.activeMembers.size) {
                        2 -> armour.add(Item(ItemId.GROUP_IRON_PLATEBODY))
                        3 -> armour.add(Item(ItemId.GROUP_IRON_PLATEBODY_26160))
                        4 -> armour.add(Item(ItemId.GROUP_IRON_PLATEBODY_26162))
                        5 -> armour.add(Item(ItemId.GROUP_IRON_PLATEBODY_26164))
                    }
                    armour.add(Item(ItemId.GROUP_IRON_PLATELEGS))
                    armour.add(Item(ItemId.GROUP_IRON_BRACERS))
                }
                val items = ItemUtil.concatenate(
                    player.inventory.container.items.values.toTypedArray(),
                    player.equipment.container.items.values.toTypedArray(),
                    player.bank.container.items.values.toTypedArray()
                )
                var added = 0
                for (item in armour) {
                    if (!ArrayUtils.contains(items, item)) {
                        player.inventory.addItem(item).onFailure {
                            player.bank.add(item).onFailure { i2: Item ->
                                player.sendMessage(
                                    "I'm not able to give you the following item due to insufficient space: " + i2.name
                                )
                            }
                        }
                        added++
                    }
                }
                val message =
                    if (added == 0) "I think you've already got the whole set." else "There you go. Wear it with pride."
                player.dialogueManager.start(NPCChat(player, npc.id, message))
            }
        }
    }
}

fun OptionsBuilder.`I'd like to manage my group`(group: IronmanGroup) {
    dialogueOption("I'd like to manage my group.") {
        options {
            `How do I rename my group`()
            `I'd like to kick or unkick players form my group`(group)
        }
    }
}

fun OptionsBuilder.`How do I rename my group`() {
    dialogueOption("How do I rename my group?") {
        npc("You can rename your group from you group settings " +
                "menu.")
        `Can I help you with anything else`()
    }
}


fun OptionsBuilder.`I'd like to kick or unkick players form my group`(group: IronmanGroup){
    val graceDays = IronmanGroup.gracePeriod.inWholeDays
    dialogueOption("I'd like to kick/unkick players from my group.") {
        options {
            val cancellableKicks: List<IronmanGroupMember> = group.getCancellableKickedMembers()
            if (cancellableKicks.isNotEmpty()) {
                dialogueOption("I'd like to cancel my request to kick a member from the group.", noPlayerMessage = true) {
                    npc("Certainly, I can do that for you right now. Be aware, " +
                            "however, that the $graceDays day processing time will be reset if " +
                            "you change your mind.")
                    npc("Whose kick request would you like to cancel?")
                    promptUnkick(group, cancellableKicks)
                }
            }
            val kickableMembers = group.getKickableMembers().filter { it.username != player.username }
            if (kickableMembers.isNotEmpty()) {
                dialogueOption("I'd like to kick a player.", noPlayerMessage = true) {
                    npc(
                        "We can start the process of kicking a player. This will " +
                                "take up to $graceDays days to complete, you can cancel it at any" +
                                "time beforehand."
                    )
                    npc("Who would you like to kick from the group?")
                    promptKick(group, kickableMembers)
                }
            }
            `I'm fine, thanks`()
        }
    }
}

fun Dialogue.promptUnkick(group: IronmanGroup, cancellableKicks: List<IronmanGroupMember>) {
    val member = cancellableKicks.firstOrNull()
    if (member == null) {
        `Can I help you with anything else`()
        return
    }
    val name = TextUtils.formatName(member.username)
    options("Do you wish to unkick $name") {
        dialogueOption("Yes.", noPlayerMessage = true) {
            options("Are you sure you wish to unkick $name from the group?") {
                dialogueOption("No, I've changed my mind.", noPlayerMessage = true) {
                    `Can I help you with anything else`()
                }
                dialogueOption("Yes, unkick $name.", noPlayerMessage = true) {
                    npc("Very well, $name will no longer be kicked from the group.").executeAction {
                        group.unKick(member)
                    }
                    `Can I help you with anything else`()
                }
            }
        }
        dialogueOption("No.", noPlayerMessage = true) {
            promptUnkick(group, cancellableKicks - member)
        }
    }
}

fun Dialogue.promptKick(group: IronmanGroup, kickableMembers: List<IronmanGroupMember>) {
    val member = kickableMembers.firstOrNull()
    if (member == null) {
        `Can I help you with anything else`()
        return
    }
    val name = TextUtils.formatName(member.username)
    options("Do you wish to kick $name?") {
        dialogueOption("Yes.", noPlayerMessage = true) {
            options("Are you sure you wish to kick $name from the group?") {
                dialogueOption("No, I've changed my mind.", noPlayerMessage = true) {
                    `Can I help you with anything else`()
                }
                dialogueOption("Yes, kick $name.", noPlayerMessage = true) {
                    npc(
                        "Great. in ${IronmanGroup.gracePeriod.inWholeDays} days $name will be kicked from your " +
                                "group. Come back to me to stop this if you change " +
                                "your mind."
                    ).executeAction {
                        group.kick(member)
                    }
                    `Can I help you with anything else`()
                }
            }
        }
        dialogueOption("No.", noPlayerMessage = true) {
            promptKick(group, kickableMembers - member)
        }
    }
}

fun OptionsBuilder.`What is a Group Iron Man`() {
    dialogueOption("What is a Group Iron Man?") {
        npc(
            "Group Iron Man is a game mode where you do everything as a group. " +
                    "You can only trade items with player's in your group, " +
                    "take their items and accept their help."
        )
        npc(
            "This means if your friend in the group has something you want to borrow, " +
                    "you can - assuming you have Individual Prestige status or are a Hardcore Group Iron player! " +
                    "But don't confuse Individual and Group"
        )
        npc("Prestige.")
        `Can I help you with anything else`()
    }
}

fun OptionsBuilder.`What is Individual Prestige`() {
    dialogueOption("What is Individual Prestige?") {
        npc(
            "Individual Prestige is automatically awarded to you the very first time you create a group. " +
                    "It is proof that you were one of the original members of that group."
        )
        npc(
            "This proof is displayed as a gold star next to your name on the group sidepanel. " +
                    "Note that if you join an existing group, " +
                    "you will not be considered an original member."
        )
        npc(
            "Players without Individual Prestige will have limitations " +
                    "for how much wealth they can receive from other group " +
                    "members depending on how long they've been in the group."
        )
        npc(
            "The limits are gold and items worth up to 1,000,000 coins for the 1st week, " +
                    "20,000,000 coins for the 2nd week, 50,000,000 coins for the 3rd week " +
                    "and 100,000,000 coins for the 4th week."
        )
        npc(
            "After you have been with the group for 5 week, the " +
                    "limit will be removed completely."
        )
        npc(
            "During these 5 weeks you will also have restrictions to what you can equip: " +
                    "certain items will require you to have killed the monster that drops them"
        )
        npc(
            "For example, if your friend in the group gives you an " +
                    "Abyssal Whip, you can't equip it until you have killed an " +
                    "Abyssal Demon."
        )
        `Can I help you with anything else`()
    }
}

fun OptionsBuilder.`What is Group Prestige`() {
    dialogueOption("What is Group Prestige?") {
        player("What is Group Prestige?")
        npc(
            "Non-Hardcore groups can show their dedication to the group through Group Prestige. " +
                    "This status can be viewed on the group's sidepanel, just below the group's name."
        )
        npc(
            "Group Prestige is lost upon inviting new members into the group, " +
                    "or upon doing certain activities with players outside of your group."
        )
        npc(
            "You won't suffer any consequences from not having" +
                    "Group Prestige - it is purely a symbol of dedication."
        )
        `Can I help you with anything else`()
    }
}

fun OptionsBuilder.`What is a Hardcore Group Iron Man`() {
    dialogueOption("What is a Hardcore Group Iron Man?") {
        npc(
            "Hardcore Group Iron mode is a more challenging version of the regular mode, " +
                    "in which you have only a set number of lives for the entire group."
        )
        npc(
            "If you die in an unsafe area, you will lose one of your group's lives. " +
                    "When your shared lives hit 0, you will all be downgraded to regular Group Iron players."
        )
        npc(
            "Hardcore groups can't invite more players into their " +
                    "group until they lose their status, which means they will" +
                    "by default have Group Prestige."
        )
        `Can I help you with anything else`()
    }
}

fun Dialogue.`Can I help you with anything else`() {
    npc("Can I help you with anything else?")
    options {
        `What is Individual Prestige`()
        `What is Group Prestige`()
        `What is a Hardcore Group Iron Man`()
        `More questions`()
    }
}

fun OptionsBuilder.`More questions`() {
    dialogueOption("More questions.", noPlayerMessage = true) {
        options {
            `How do I join or create a group`()
            `How do I leave a group`()
            `Can I change my Group Iron mode`()
            `I'm fine, thanks`()
        }
    }
}

fun OptionsBuilder.`How do I join or create a group`() {
    dialogueOption("How do I join or create a group?") {
        npc(
            "If you are a new Group Iron Man on the island, you " +
                    "will have the option to apply to join other groups, as " +
                    "long as they are accepting new members."
        ).executeAction {
            // TODO: open Iron Group interface in tab
            player.packetDispatcher.sendClientScript(4472)
//                4472  -  727.0
//                5302  -  727.3[3]
//                2860  -  727.9
        }
        npc(
            "You can also create your own group by clicking the " +
                    "'Create Group' button. You will then be able to recruit " +
                    "people into your group."
        )
        npc(
            "To invite people to your group, make sure you have the " +
                    "invite option on by clicking the 'Invite' button. " +
                    "You can then right-click a player and invite them."
        )
        npc(
            "You need a minimum of 2 people to create a group. If " +
                    "you want your group to be displayed on the Group " +
                    "Ironman Hiscores, you can only invite new Group Iron " +
                    "players into your group."
        )
        npc(
            "You can invite experienced players inyo your group, " +
                    "but that would result in the group becoming unranked. " +
                    "This means that the group will no longer be shown on " +
                    "the Group Ironman Hiscores."
        )
        npc(
            "After you have invited players to your group, click the " +
                    "'Confirm' button to create the group."
        )
        npc(
            "If you are looking for a group, try the official Group " +
                    "Iron ${Colour.RS_RED.wrap("world 417")}."
        )
        npc("Can I help you with anything else?")
        options {
            `What is a Group Iron Man`()
            `How do I leave a group`()
            `Can I change my Group Iron mode`()
            `I'm fine, thanks`()
        }
    }
}

fun OptionsBuilder.`How do I leave a group`() {
    dialogueOption("How do I leave a group?") {
        val gracePeriod = IronmanGroup.gracePeriod.inWholeDays
        player("How do I leave a group?")
        npc(
            "You can leave your group by talking to me about group management. " +
                    "However, know that there will then be a $gracePeriod day grace period " +
                    "before it takes effect. " +
                    "In this time you can come back"
        )
        npc(" to cancel the request if you've changed your mind.")
        npc(
            "You could also be kicked from the group. " +
                    "If you are kicked from the group, you also have a $gracePeriod day grace " +
                    "period before you are removed. Only the leader of a " +
                    "group can kick other people."
        )
        npc(
            "The leader can cancel the kick request at any time " +
                    "during those $gracePeriod days if they have changed their minds " +
                    "about kicking someone."
        )
        npc(
            "When you leave the group, you will be given the choice " +
                    "between remaining an unranked Group Iron Man or " +
                    "continuing as a Regular account."
        )
        npc(
            "If you choose to remain a Group Iron Man, you must " +
                    "choose between remaining ranked or becoming " +
                    "unranked."
        )
        npc(
            "You are already a ranked Group Iron Man. If you " +
                    "choose to remain that way, you will lose your tradeable " +
                    "items when you leave the group. This includes items " +
                    "that have cosmetic overrides on them, even if that makes"
        )
        npc(" the cosmetic item untradeable.")
        npc(
            "You will also only be able to group with new Group " +
                    "Iron players. If you instead wish to group with existing " +
                    "players, perhaps the unranked mode is more suited for " +
                    "you."
        )
        npc(
            "If you choose to become an unranked Group Iron " +
                    "Man you will no longer display you or your group's " +
                    "progress on the Group Ironman Hiscores, and you " +
                    "can't go back to being a ranked Group Iron Man."
        )
        npc("Can I help you with anything else?")
        options {
            `What is a Group Iron Man`()
            `How do I join or create a group`()
            `Can I change my Group Iron mode`()
            `I'm fine, thanks`()
        }
    }
}

fun OptionsBuilder.`Can I change my Group Iron mode`() {
    dialogueOption("Can I change my Group Iron mode?") {
        npc(
            "You can change your Group Iron mode by interacting " +
                    "with the crates south of the forge."
        ).executeAction {
            // move camera to crates
        }
        npc(
            "The Hardcore armour crate can make you a Hardcore " +
                    "Group Iron Man, and the other crate can make you a " +
                    "regular Group Iron Man."
        )
        npc("Can I help you with anything else?").executeAction {
            // reset camera
        }
        npc("Can I help you with anything else?")
        options {
            `What is a Group Iron Man`()
            `How do I join or create a group`()
            `How do I leave a group`()
            `I'm fine, thanks`()
        }
    }
}

fun OptionsBuilder.`I'm fine, thanks`() {
    dialogueOption("I'm fine, thanks.")
}
