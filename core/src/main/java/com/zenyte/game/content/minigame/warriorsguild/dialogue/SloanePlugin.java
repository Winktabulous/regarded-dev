package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 7:01.35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SloanePlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Ahhh, hello there, " + player.getPlayerInformation().getDisplayname() + ".");
                options(TITLE, "What can I do here?", "That's a big axe!", "May I claim my tokens please?", "Bye!").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(50)).onOptionFour(() -> setKey(70));
                player(10, "What can I do here?");
                npc("Ahh, the shot put is a great test of strength and can be " + "quite rewarding. Mind you do it properly though, you " + "might want to dust your hands with some powdery " + "substance first. It'll give you better grip.");
                player("I'll remember that. What should I use?");
                npc("I personally prefer ground ashes, it makes a nice fine " + "powder and gives some really good grip.");
                player("Ok, what else?");
                npc("Basically you'll need to go in there, make sure you've " + "got nothing cluttering up your hands, pick up a shot " + "and throw it.... depending upon your technique, you " + "can get quite long throws.");
                player("What was your best?");
                npc("Ahh that would be telling...");
                player(30, "That's a big axe!");
                npc("Yes indeed it is. Have yo be mighty strong to wield it too.");
                player("But you don't look that strong!");
                npc("Maybe, maybe not, but I still had to beat a Barbarian to get it. " + "Mind you, usually they don't part with them. This was an " + "unusual circumstance.");
                player("Oh?");
                npc("I bet him he couldn't catch a squirrel whilst still holding " + "his axe but that I could... and that if I won I'd get his axe.");
                player("What happened?");
                npc("He went running after the squirrel... nearly caught it too... " + "it shot up a tree and he tried to climb up it, only got " + "a bit tangled up with his axe cause he couldn't hand on to it " + "at the same time... he fell out of the tree and it");
                npc("was my turn....");
                npc("I simply went and chopped the tree down with this big old axe, " + "still holding it. The squirrel was so petrified it " + "simply jumped straight out of the tree onto me, I won " + "the axe and let the poor creature go!");
                player("I sense a moral in there somewhere.");
                npc("Aye indeed. Brawn isn't all you need to wield a big axe, " + "brains are required too!");
                player(50, "May I claim my tokens please?");
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
                player(70, "Bye!");
                npc("Be well, warrior " + player.getPlayerInformation().getDisplayname() + ".");
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
        return new int[] { NpcId.SLOANE };
    }
}
