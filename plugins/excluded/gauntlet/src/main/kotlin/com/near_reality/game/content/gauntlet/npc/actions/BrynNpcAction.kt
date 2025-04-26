package com.near_reality.game.content.gauntlet.npc.actions

import com.near_reality.game.content.gauntlet.spokenToBrynn
import com.near_reality.game.util.invoke
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.util.Colour.RED
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

class BrynNpcAction : NPCActionScript() {
    init {
        npcs(NpcId.BRYN)

        "Talk-To" {
            player.dialogue(npc) {
                npc("What are you doing down here?")
                options {
                    if (!player.spokenToBrynn) {
                        dialogueOption("What is this place?") {
                            npc(
                                "This here is the Gauntlet, " +
                                        "the finest creation of the Amlodd clan. " +
                                        "It was built years ago to help train Prifddinas' finest warriors and survivalists."
                            ).executeAction {
                                player.spokenToBrynn = true
                            }
                        }
                        dialogueOption("Just passing by.") {
                            npc("Alright... Well be careful down here. I've got my eye on you.")
                        }
                    }
                    else {
                        dialogueOption("Could you explain this place to me again?") {
                            npc("This here is the Gauntlet, the finest creation of the Amlodd clan. It was built years ago to help train Prifddinas' finest warriors and survivalists.")
                            player("Train?")
                            npc("That's right. The Gauntlet contains a range of deadly creatures formed from crystal. Defeating them is the key to victory, but it is no easy task.")
                            npc("This is because all participants start with nothing at hand beyond a few basic tools.")
                            player("But how do people fight without their gear?")
                            npc("Resources can be gathered throughout the dungeon. These can be used to create the weapons, armour, food and potions needed to survive.")
                            npc("Without any armour, those crystalline creatures can be real nasty. Any armour that you make down there will greatly reduce the damage they do to you.")
                            player("Okay, I think I follow... But I thought this was all about combat, not just gathering my own equipment.")
                            npc("That's right. Once you feel you're ready, or once we decide that you've spent long enough down there, you'll have to face the fearsome Crystalline Hunllef.")
                            player("And what makes it so dangerous, if it's just for training?")
                            npc("Once you go down there, there are only two ways out... You can make a break for the exit, or if you're bested in combat we'll send someone in for you.")
                            plain("The Gauntlet is ${RED("not a safe environment")}, and ${RED("teleports are blocked inside")}! " +
                                    "If you die in there, any items stored at a gravestone ${RED("will be lost")}!")
                        }
                        dialogueOption("I'm thinking of giving the Gauntlet a go.") {
                            npc("You? Attempting the Gauntlet? If you say so!")
                        }
                    }
                }
            }
        }

    }
}

