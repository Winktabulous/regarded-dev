package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25/11/2018 16:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Sedridor extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("Welcome adventurer, to the world renowned<br> Wizards' Tower. How may I help you?");
                    player("Hello there.");
                    npc("Hello again " + player.getName() + ". What can I do for you?");
                    player("Can you teleport me to the Rune Essence?").executeAction(() -> Aubury.teleport(player, npc));
                }
            });
        });
        bind("Teleport", Aubury::teleport);
    }

    @Override
    public int[] getNPCs() {
        return new int[] {5034};
    }
}
