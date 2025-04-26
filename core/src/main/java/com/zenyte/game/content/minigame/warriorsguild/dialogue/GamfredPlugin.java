package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.content.minigame.warriorsguild.catapultroom.CatapultRoom;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 6:10.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class GamfredPlugin extends NPCPlugin {

    /**
     *  once u move on the target and equip
     *  hides tabs:
     *  quest, inventory, prayer, spellbook, options, emotes
     *  equipment is overran with the inventory interface.
     *
     *  If player isn't wielding the shield:
     * 	if hitTarget && aint wielding shield.
     * 		npc("Watch out! You'll need to equip the shield as soon as "
     * 				+ "you're on the target spot else you could get hit! Speak "
     * 				+ "to me to get one, and make sure both your hands are "
     * 				+ "free to equip it.");
     *
     * 		x++;
     */
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Ello there. I'm Gamfred, the engineer in this here guild. " + "Have you seen my catapult?");
                options(TITLE, "That's not a catapult, it's a large crossbow.", "Yes, beautiful piece of engineering.", "No, where is it?", "May I claim my tokens please?", "Bye!").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(50)).onOptionFour(() -> setKey(70)).onOptionFive(() -> setKey(100));
                player(10, "That's not a catapult, it's a large crossbow.");
                npc("WHAT!? I'll have you know that is the finest piece of " + "dwarven engineering for miles around! How DARE " + "you insult my work!");
                player(30, "Yes, beautiful piece of engineering.");
                npc("Nice to meet someone who appreciates fine work, have " + "you tried it out yet?");
                options(TITLE, "Yes.", "No, how do I do that?").onOptionOne(() -> setKey(120)).onOptionTwo(() -> setKey(200));
                player(120, "Yes");
                npc("What did you think?");
                options(TITLE, "It was ok I guess.", "It was fun!", "I didn't like it.", "May I have a shield please?").onOptionOne(() -> setKey(130)).onOptionTwo(() -> setKey(140)).onOptionThree(() -> setKey(150)).onOptionFour(() -> setKey(160));
                player(100, "Bye");
                player(130, "It was ok I guess.");
                npc("Well I guess not everyone will like it.");
                player(140, "It was fun!");
                npc("Glad to hear it. Try again sometime. We have more tests to run.");
                player(150, "I didn't like it.");
                npc("Well I guess not everyone will like it. But give it another " + "chance before you go.");
                player(160, "May I have a shield please?");
                npc("Of course.").executeAction(() -> player.getInventory().addItem(CatapultRoom.SHIELD));
                item(CatapultRoom.SHIELD, "The dwarf hands you a large shield.");
                player(200, "No, how do I do that?");
                npc("Well ye take the big defence shield in both hands and " + "watch the catapult. My assistant will fire different things " + "at you and you need to defend against them.");
                options(TITLE, "May I have a shield please?", "Sounds boring.").onOptionOne(() -> setKey(161)).onOptionTwo(() -> setKey(210));
                player(210, "Sounds boring.");
                npc("Your loss...");
                player(50, "No, where is it?");
                npc("Are ye blind lad? Tis over there in the next room with " + "me assistant working it!");
                player(70, "May I claim my tokens please?");
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
        bind("Claim-Shield", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                player("May I have a shield please?");
                npc("Of course.").executeAction(() -> player.getInventory().addItem(CatapultRoom.SHIELD));
                item(CatapultRoom.SHIELD, "The dwarf hands you a large shield.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.GAMFRED };
    }
}
