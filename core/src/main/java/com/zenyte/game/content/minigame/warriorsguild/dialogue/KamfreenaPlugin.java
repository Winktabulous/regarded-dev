package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 7:40.00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class KamfreenaPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Why hello there! I'm Kamfreena. Like the look of my " + "pets? I think they're eyeing you up.");
                    player("That was a really bad pun.");
                    npc("Sorry... I don't get to see the rest of the guild much, " + "stuck up here. The Cyclopes don't talk much you see.");
                    player("Shouldn't that be cyclopses?");
                    npc("Nope! Cyclopes is the plural of cyclops. One cyclops, " + "many cyclopes.");
                    player("Oh, right, thanks.");
                    options();
                    player(10, "Where are they from?");
                    npc("They're from the far east lands.");
                    options();
                    player(30, "How did they get here?");
                    npc("Ahhh.. our guildmaster, Harrallak, went on an expedition " + "there. He brought them back with him.");
                    options();
                    player(50, "Why are they here?");
                    npc("For the warriors to train on of course! They also drop " + "a rather nice blade.");
                    player("Oh? What would that be?");
                    npc("Defenders.");
                    player("Err what are they?");
                    npc("It's a blade you can defend with using your shield hand, " + "like I have.");
                    player("Wow!");
                    npc("For every 10 tokens you collect around the guild, you can " + "spend one minute in with my pets. As you get defenders " + "you can show them to me to earn even better ones... " + "but remember if you lose them you'll have");
                    npc("to start at bronze again. I'd advise keeping a spare in your bank.");
                    player("Ok!");
                    npc("Oh by the way, you'll need to earn 100 tokens before I'll let you in!");
                    player("Right, I'd better go play some games then!");
                    options();
                    player(70, "Bye!");
                    npc("See you back here soon I hope!");
                }

                private void options() {
                    options(TITLE, "Where are they from?", "How did they get here?", "Why are they here?", "Bye!").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(50)).onOptionFour(() -> setKey(70));
                }
            });
        });
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
                    player("Okay, I'll go see what I can find.");
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.KAMFREENA };
    }
}
