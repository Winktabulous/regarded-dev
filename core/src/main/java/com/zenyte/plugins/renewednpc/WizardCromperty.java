package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25/11/2018 16:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WizardCromperty extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Hello there. My name is Cromperty. I am a Wizard,<br>and an inventor.");
                    npc("You must be " + player.getName() + ". My good friend Sedridor has told me about you. As both wizard and inventor, he has aided me in my great invention!");
                    player("Can you teleport me to the Rune Essence?").executeAction(() -> Aubury.teleport(player, npc));
                }
            });
        });
        bind("Teleport", Aubury::teleport);
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.WIZARD_CROMPERTY, NpcId.WIZARD_CROMPERTY_8481 };
    }
}
