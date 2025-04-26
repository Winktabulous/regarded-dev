package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 5:17.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class AjjatPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Greetings, fellow warrior. I am Ajjat, former black " + "knight and now training officer here in the warrior guild.");
                options(TITLE, "Black Knight? Why are you here?", "What's the Dummy Room all about?", "May I claim my tokens please?", "Bye!").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(50)).onOptionFour(() -> setKey(70));
                player(10, "Black Knight? Why are you here?");
                npc("Indeed I was, however their... methods... did not match " + "with my ideals.. so I left. Harrallak, recognising my " + "talent as a warrior, took me in and offered me a job here.");
                player("Hmm... well if Harrallak trusts you, I guess I can.");
                player(30, "What's the Dummy Room all about?");
                npc("Ahh yes, the dummies. Another ingenious invention of the " + "noble Dwarf Gamfred. They're mechanical you see and pop " + "out of the floor. You have to hit them with the correct " + "attack mode before they disappear again.");
                player("So how do I tell which one is which?");
                npc("There are two different ways. One indication is their " + "colour, the other is the pose and the weapons they are " + "holding, for instance, the one holding daggers you will " + "need to hit with a piercing attack.");
                npc("In the room you will find a poster on the wall which can help " + "you recognise each different dummy.");
                player("That sounds ingenious!");
                npc("Indeed. You may find that you need several weapons to " + "be successful 100% of the time, but keep trying. The " + "weapons shop upstairs may help you there.");
                player(50, "May I claim my tokens please?");
                if (player.getNumericAttribute("warriorsGuildTokens").intValue() > 0) {
                    if (player.getInventory().hasFreeSlots() || player.getInventory().containsItem(8851, 1)) {
                        npc("Of course! Here you go, you've earned " + player.getNumericAttribute("warriorsGuildTokens").intValue() + " tokens!").executeAction(() -> {
                            player.getInventory().addItem(new Item(8851, player.getNumericAttribute("warriorsGuildTokens").intValue()));
                            player.getAttributes().remove("warriorsGuildTokens");
                        });
                        player("Thanks!");
                    } else {
                        plain("You need some free inventory space before you can accept the tokens.");
                    }
                } else {
                    npc("I'm afraid you have not earned any tokens yet. Try some " + "of the activities around the guild to earn some.");
                    player("Ok, I'll go see what I can find.");
                }
                player(70, "Bye!");
                npc("Farewell warrior. Stay away from the dark side.");
            }
        }));
        bind("Claim-Tokens", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                player("May I claim my tokens please?");
                if (player.getNumericAttribute("warriorsGuildTokens").intValue() > 0) {
                    if (player.getInventory().hasFreeSlots() || player.getInventory().containsItem(8851, 1)) {
                        npc("Of course! Here you go, you've earned " + player.getNumericAttribute("warriorsGuildTokens").intValue() + " tokens!").executeAction(() -> {
                            player.getInventory().addItem(new Item(8851, player.getNumericAttribute("warriorsGuildTokens").intValue()));
                            player.getAttributes().remove("warriorsGuildTokens");
                        });
                        player("Thanks!");
                    } else {
                        plain("You need some free inventory space before you can accept the tokens.");
                    }
                } else {
                    npc("I'm afraid you have not earned any tokens yet. Try some " + "of the activities around the guild to earn some.");
                    player("Ok, I'll go see what I can find.");
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.AJJAT };
    }
}
