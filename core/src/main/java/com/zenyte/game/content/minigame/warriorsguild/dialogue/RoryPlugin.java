package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 18. dets 2017 : 0:20.42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class RoryPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, rory) -> {
            player.getDialogueManager().start(new Dialogue(player, 2135) {

                @Override
                public void buildDialogue() {
                    World.findNPC(2135, rory.getLocation()).ifPresent(lorelai -> lorelai.setFaceLocation(player.getLocation()));
                    npc("Ahh I see you've met Rory. As a young Cyclops he's " + "not ready for combat yet so he keeps me company.");
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.RORY };
    }
}
