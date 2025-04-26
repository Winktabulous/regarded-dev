package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 7:13.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class RefPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Greetings Sir.");
                options(TITLE, "Tell me about the Shot Put.", "May I claim my tokens please?", "Do you have any tips for me?", "Bye!").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(50)).onOptionFour(() -> setKey(70));
                player(10, "Tell me about the Shot Put.");
                npc("Of course Sir. There are two different weights of shot...");
                player("Shot?");
                npc("Yes Sir. Shot. The iron spheres that are propelled by " + "the chemical energy stored in your body.");
                player("The... what?");
                npc("Your strength Sir. The stronger you are, the further " + "you can throw the shot, but there are other factors of " + "course, like your technique.");
                player("What's that then?");
                npc("The style of the shot Sir.");
                player("Iron has a style??");
                plain("The Referee sighs, rolls his eyes and continues....");
                npc("Sir, the style in which you throw the shot, not the style " + "of the iron.");
                player("Oh! You mean the spinny round thing or the chuck it straight?");
                npc("Crudely put Sir, but yes. Some are more difficult than others. " + "Experiment and see which you prefer.");
                player("Thanks for the help!");
                npc("You are welcome Sir.");
                player(30, "May I claim my tokens please?");
                if (player.getNumericAttribute("warriorsGuildTokens").intValue() > 0) {
                    if (player.getInventory().hasFreeSlots() || player.getInventory().containsItem(8851, 1)) {
                        npc("Of course! Here you go, you've earned " + player.getNumericAttribute("warriorsGuildTokens").intValue() + " tokens!").executeAction(() -> {
                            player.getInventory().addItem(new Item(8851, player.getNumericAttribute("warriorsGuildTokens").intValue()));
                            player.getAttributes().remove("warriorsGuildTokens");
                        });
                        player("Thanks!");
                    } else
                        plain("You need some free inventory space before you can accept the tokens.");
                } else {
                    npc("I'm afraid you have not earned any tokens yet. Try " + "some of the activities around the guild to earn some.");
                    player("Ok, I'll go see what I can find.");
                }
                player(50, "Do you have any tips for me?");
                npc("Tips Sir?");
                player("Yes, like how can I do better than anyone else.");
                npc("Sir may find that a fine powder applied to the hands " + "may give one an advantage when putting the shot.");
                player("You mean if I grind something up and put dust on my hands, " + "I'll chuck the ball further?");
                npc("Yes Sir.");
                player("Thanks!");
                npc("You are welcome Sir.");
                player(70, "Bye!");
                npc("Good luck Sir.");
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
                    } else
                        plain("You need some free inventory space before you can accept the tokens.");
                } else {
                    npc("I'm afraid you have not earned any tokens yet. Try " + "some of the activities around the guild to earn some.");
                    player("Ok, I'll go see what I can find.");
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.REF, NpcId.REF_6074 };
    }
}
