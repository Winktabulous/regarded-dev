package com.near_reality.plugins.item

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue

class HauntedWineBottleItemAction : ItemActionScript() {
    init {
        items(ItemId.HAUNTED_WINE_BOTTLE)

        "Wake-ghost" {
            player.lock(2)
            player.animation = Animation(9162)
            player.graphics = Graphics(1994)
            WorldTasksManager.schedule({
                player.forceTalk = ForceTalk("WooooOOoOoo!")
            }, 2)
        }

        "Talk-to" {
            player.dialogue {
                player("Hello...?")
                if (!player.equipment.containsAnyOf(ItemId.GHOSTSPEAK_AMULET, ItemId.GHOSTSPEAK_AMULET_4250)) {
                    bottle("WoooOooOooOooooOOOoo.")
                } else {
                    when (player.hauntedWineBottleTalkCount) {
                        0 -> {
                            bottle("Yes, ${player.name}?")
                            player("I didn't expect you to talk to me!")
                            bottle("Well, you have something that lets you speak to ghosts, and I am a ghost.")
                            player("True! Nice to meet you!")
                            bottle("Likewise, I think. Please don't disturb me too much. I'm trying to rest.").executeAction {
                                player.hauntedWineBottleTalkCount = 1
                            }
                        }
                        1 -> {
                            bottle("What do you want?")
                            player("What is it like living in a tiny bottle?")
                            bottle("Living? LIVING? I'm trapped in here, that isn't living!")
                            bottle("...I'm also a ghost.")
                            player("Good point.").executeAction {
                                player.hauntedWineBottleTalkCount = 2
                            }
                        }
                        2 -> {
                            bottle("I'm awake.")
                            player("Can I talk to you occasionally?")
                            bottle("If you have to. Try and make it interesting.").executeAction {
                                player.hauntedWineBottleTalkCount = 3
                            }
                        }
                        3 -> {
                            player.hauntedWineBottleTalkCount++
                            val dialogue = randomDialogues.random()
                            dialogue(this)
                        }
                    }
                }
            }
        }
    }

    var Player.hauntedWineBottleTalkCount
        get() = getNumericAttribute("hauntedWineBottleTalkCount").toInt()
        set(value) { attributes["hauntedWineBottleTalkCount"] = value }

    fun Dialogue.bottle(message: String) =
        item(Item(ItemId.HAUNTED_WINE_BOTTLE), message)

    val randomDialogues = arrayOf<Dialogue.() -> Unit>(
        {
            bottle("I'm trying to sleep, what is it?")
            player("Who were you when you used to be alive?")
            bottle("I used to be an adventurer like you...")
            bottle("...then I took an arrow to the knee.")
            player("That's unfortunate. I've never heard of that happening before.")
            bottle("You're one lucky adventurer then.")
        },
        {
            bottle("Hmm?")
            player("Who is your favourite god?")
            bottle("Zaros. We had some great times together.")
        },
        {
            bottle("Hello again, ${player.name}.")
            player("Any words of wisdom for me?")
            bottle("Don't get trapped in a bottle.")
            player("Thanks.")
        },
        {
            bottle("Hello...?")
            player("Can you do any impressions?")
            bottle("Sure, what would you like to hear?")
            player("Surprise me!")
            bottle("Okay, woooOooOooOooooO I'm a ghost, woOoOooOoooo.")
            player("Very funny.")
        },
        {
            bottle("Hi ${player.name}.")
            player("How many times have we spoken to each other now?")
            bottle("${player.hauntedWineBottleTalkCount} times.")
            player("Wow! How are you keeping count?")
            bottle("It's not like I have much else to do.")
            player("I suppose. Here's to many more great conversations!")
            bottle("... lovely.")
        },
        {
            bottle("What?")
            player("Why do you slap me each time I wake you up?")
            bottle("How would YOU like to be rudely awoken when you're trying to sleep?")
            player("I suppose.")
        },
        {
            bottle("What do you want?")
            player("What is your favourite number?")
            bottle("Two. I've always liked two. It's small, simple, and doesn't play any tricks.")
            player("Interesting, I've always liked eight myself.")
        },
        {
            bottle("Yes?")
            player("How old are you?")
            bottle("I don't remember anymore. Pretty old.")
        },
        {
            bottle("Hi?")
            player("You're a lovely colour of green today!")
            bottle("Thank you very much!")
        },
        {
            bottle("Please stop waking me up!")
            player("I'm sorry!")
        },
        {
            bottle("What is it now?")
            player("Nothing. I just wanted to see if you were still here.")
            bottle("I'm trapped in a bottle. I'm not going anywhere.")
        }
    )

}
