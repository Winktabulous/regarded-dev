package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 08/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RangedTutor extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("How may I help you today?");
                if (player.getNumericAttribute("equip ammunition picked up").intValue() == 0) {
                    options(TITLE, new DialogueOption("Automatically equip ammunition picked.", () -> {
                        setKey(5);
                        player.addAttribute("equip ammunition picked up", 1);
                    }), new DialogueOption("Cancel."));
                    player(5, "I'd like to automatically equip the ammunition that I pick up.");
                    npc("Of course, from here on out, all ammunition that you pick up, granted you have at least one of the " +
                            "same type ammunition equipped and can carry the entire stack, will be equipped automatically.");
                } else {
                    options(TITLE, new DialogueOption("Don't equip ammunition picked.", () -> {
                        setKey(5);
                        player.addAttribute("equip ammunition picked up", 0);
                    }), new DialogueOption("Cancel."));
                    player(5, "I no longer want to equip the ammunition I pick up.");
                    npc("Of course, from here on out, all ammunition you pick up will instead be put in your inventory.");
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                3217
        };
    }
}
