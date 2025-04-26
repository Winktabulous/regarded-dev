package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 08/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MagicTutor extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                options(TITLE, new DialogueOption("Can you toggle my runes to equip when I pick them up please?", key(5)), new DialogueOption("Goodbye.", key(50)));
                npc(5, "Certainly. If you own a rune pouch and pick up runes that you have stored in it, I can make those runes be automatically added to your rune pouch, providing your have space " +
                        "for" +
                        " them all! Or you can let them go into");
                npc("your inventory like normal.");
                npc("How do you want to handle picking up runes?");
                options(TITLE,
                        new DialogueOption("Automatically send to rune pouch.", () -> {
                            setKey(22);
                            player.addAttribute("put looted runes in rune pouch", 1);
                        }),
                        new DialogueOption("Place them in my inventory.", () -> {
                            setKey(20);
                            player.addAttribute("put looted runes in rune pouch", 0);
                        }));
                npc(20, "There you go! Your runes will now go to your inventory when you pick them up.");
                npc(22, "There you go! Your runes will now go to your rune pouch when you pick them up.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[]{
                3218
        };
    }
}
