package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 5:02.38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ShanomiPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Greetings " + player.getPlayerInformation().getDisplayname() + ". " + "Welcome you are in the test of combat.");
                    options();
                    player(10, "What do I do here?");
                    npc("A spare suit of plate armour need you will. Full helm, " + "plate leggings and platebody yes? Placing it in the " + "centre of the magical machines you will be doing. " + "KA-POOF! The armour, it attacks most furiously as if");
                    npc("alive! Kill it you must, yes.");
                    player("So I use a full set of plate armour on the centre " + "plate of the machines and it will animate it? Then I " + "have to kill my own armour... how bizarre!");
                    npc("Yes. It is as you are saying. For this earn tokens you " + "will. Also gain experience in combat you will. " + "Trained long and hard here have I.");
                    player("You're not from around here are you...?");
                    npc("It is as you say.");
                    player("So will I lose my armour?");
                    npc("Lose armour you will if damaged too much it becomes. " + "Rare this is, but still possible. If kill you the armour " + "does, also lose armour you will.");
                    player("So, occasionally I might lose a bit because it's being " + "bashed about and I'll obviously lose it if I die... " + "that it?");
                    npc("It is as you say.");
                    options();
                    player(30, "Where do the machines come from?");
                    npc("Make them I did, with magics.");
                    player("Magic, in the Warrior's Guild?");
                    npc("A skilled warrior also am I. Harrallak mistakes does not " + "make. Potential in my invention he sees and " + "opportunity grasps.");
                    player("I see, so you made the magical machine and Harrallak " + "saw how they could be used in the guild to train " + "warrior's combat... interesting. Harrallak certainly " + "is an intelligent guy.");
                    npc("It is as you say.");
                    options();
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
                        npc("No tokens earned have you. In training activities " + "participate you must.");
                        player("Okay, I'll go see what I can find to do around here " + "to earn some tokens.");
                    }
                    player(70, "Bye!");
                    npc("Health be with you travelling.");
                }

                private void options() {
                    options(TITLE, "What do I do here?", "Where do the machines come from?", "May I claim my tokens please?", "Bye!").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(50)).onOptionFour(() -> setKey(70));
                }
            });
        });
        bind("Claim-Tokens", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

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
                        npc("No tokens earned have you. In training activities " + "participate you must.");
                        player("Okay, I'll go see what I can find to do around here " + "to earn some tokens.");
                    }
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SHANOMI };
    }
}
