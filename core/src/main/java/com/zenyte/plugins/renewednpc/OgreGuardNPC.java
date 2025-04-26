package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 21/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OgreGuardNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Hello, human. Would you like to enter the city?");
                options(TITLE, new DialogueOption("Yes.", () -> new FadeScreen(player, () -> player.setLocation(new Location(2459, 3047, 0))).fade(3)), new DialogueOption("No."));
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.OGRE_GUARD };
    }
}
