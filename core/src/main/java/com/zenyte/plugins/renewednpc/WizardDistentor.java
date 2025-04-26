package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25/11/2018 16:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WizardDistentor extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Welcome to the Magicians' Guild!");
                    player("Hello there.");
                    npc("What can I do for you?");
                    options(TITLE, "Nothing thanks, I'm just looking around.", "Can you teleport me to the Rune Essence?").onOptionOne(() -> setKey(10)).onOptionTwo(() -> Aubury.teleport(player, npc));
                    player(10, "Nothing thanks, I'm just looking around.");
                    npc("That's fine with me.");
                }
            });
        });
        bind("Teleport", Aubury::teleport);
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.WIZARD_DISTENTOR_11400 };
    }
}
