package com.near_reality.game.content.boss.nex.npc.actions

import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

class AshuelotReisNPCAction : NPCActionScript() {
    init {
        npcs(NpcId.ASHUELOT_REIS_11289)

        "Bank" { GameInterface.BANK.open(player) }
        "Presets" { GameInterface.PRESET_MANAGER.open(player) }
        "Collect" { GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player)}
        "Talk-to" {
            player.dialogue(npc) {
                npc("You made it?")
                player("You didn't think I would?")
                npc("Nex's followers are capable of bringing down even the strongest of warrior. " +
                        "I did fear your might not make it past them.")
                npc("Still, they are nothing compared to the Angel of Death herself.")
                player("So how do I deal with her?")
                npc("You will never be able to truly defeat her. The best you can do is to try and keep her contained. " +
                        "When you are ready to face her, just go through the barrier.")
                npc("If it helps, while my magic is limited here, I should still be able to transfer items in and out of the prison for you. " +
                        "Let me know if you'd like me to do this before you enter.")
                options {
                    dialogueOption("Tell me more about Nex.") {
                        npc("Long ago, these lands were dominated by a great empire, ruled over by an ancient god. " +
                                "The Empty Lord is what they often called him, but his true name was Zaros.")

                        player("Zaros? I've heard of him.")
                        npc("At the height of his power, Zaros seemed untouchable, but he was eventually defeated by those he trusted the most. " +
                                "With him gone, the empire collapsed. War broke out soon after.")
                        npc("Although their god was gone, many Zarosians fought to keep his empire alive. Among them was Nex. " +
                                "She was Zaros' most powerful weapon, and she led a great army to try and restore his empire.")
                        npc("Not that she needed an army of course. Nex alone was powerful enough to hold back hundreds of lesser beings.")
                        player("So how did she end up here?")
                        npc("The forces of Saradomin knew that Nex needed to be dealt with. " +
                                "They were able to lure her into these caverns.")
                        npc("After great effort and sacrifice, they managed to place Nex and her followers into a frozen, enchanted sleep.")
                        npc("Once Nex was no longer an immediate threat, they built the Temple of Lost Ancients around her. " +
                                "Most of the temple has since fallen to ruin, but you can still see some of the remains.")
                        player("But she wasn't frozen forever, was she? You said you were fooled into freeing her.")
                        npc("Yes. Later in the war, a great battle broke out in the Temple of Lost Ancients. " +
                                "I was a Saradominist soldier at the time, and I was among those who fought for him in that battle.")
                        npc("By then, many of us had grown tired of war. One of our priests discovered this. " +
                                "He came to us and revealed that he felt the same way, and that he had secretly converted to Guthix. " +
                                "He encouraged us to join him.")
                        npc("We abandoned the cause of Saradomin and became followers of Guthix. " +
                                "Our new leader taught us the ways of the druids, including an ancient ritual that had the power to awaken Guthix himself.")
                        npc("We believed this ritual to be the only way to end the war.")
                        player("But I'm guessing that's not how things went?")
                        npc("No... When our leader led us to perform the ritual, it was not Guthix we woke, but Nex and her forces. " +
                                "The ritual also destroyed the protective enchantments on their prison.")
                        player("And that was exactly what your leader wanted?")
                        npc("It was. He had never been a follower of Guthix, or Saradomin. His real goal had always been to free Nex.")
                        npc("The rest is as I already told you. Nex posed such a danger, that the four armies temorarily joined forces to drive her back into the prison. " +
                                "They resealed it, but were unable to fix the enchantments we destroyed.")
                        npc("As for me, I was one of the first to fall after Nex was freed, but I didn't truly die. " +
                                "Something about our ritual tied me to this place. I've been trapped here ever since. " +
                                "Not dead, but not really alive either.")
                        player("I'm sorry.")
                        npc("Don't be. This is the punishment for my sins.")
                        player("What of the one that led the ritual. What came of him?")
                        npc("He'd achieved his goal, so he left. I never saw him again.")
                        player("But who was he really?")
                        npc("He never gave us his true name, but as Nex was freed, she spoke to him.")
                        player("She knew him?")
                        npc("It seemed that way. I heard little of what they said, but I did hear what she called him.")
                        player("What was it?")
                        npc("Sliske.")
                        npc("Forgive me, but this conversation has brought back many memories. Let us speak no more of this.")
                        player("Of course.")
                    }
                    dialogueOption("Can I access my bank?") {
                        npc("Of course.").executeAction {
                            GameInterface.BANK.open(player)
                        }
                    }
                    dialogueOption("I'd like to collect some items.") {
                        npc("Of course.").executeAction {
                            GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player)
                        }
                    }
                    dialogueOption("I need to go.") {
                        npc("Good luck out there.")
                    }
                }
            }
        }
    }
}

