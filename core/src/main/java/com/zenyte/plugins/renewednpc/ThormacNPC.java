package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 12/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ThormacNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Hello, how may I help you today?");
                options(TITLE, new DialogueOption("Enchant a battlestaff.", key(5)), new DialogueOption("Nothing."));
                player(5, "I'd like to enchant a battlestaff.");
                npc("Certainly.").executeAction(() -> GameInterface.BATTLESTAFF_ENCHANTMENT.open(player));
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.THORMAC };
    }
}
