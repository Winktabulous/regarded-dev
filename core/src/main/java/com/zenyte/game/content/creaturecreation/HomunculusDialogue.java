package com.zenyte.game.content.creaturecreation;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since August 22 2020
 */
public class HomunculusDialogue extends Dialogue {
    public HomunculusDialogue(@NotNull final Player player, @NotNull final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        player("Hi there, I heard you know something about creating monsters...?");
        npc("Good! I gain know from alchemists and builders. Me make beings.");
        player("Interesting. Tell me if I'm right. By the alchemists and builders creating you, you have inherited their combined knowledge in much the same way that a child " +
                "might inherit the looks of their parents.");
        npc("Yes, you right!");
        player("So what do you need me to do?");
        npc("Inspect symbol of life altars around dungeon. You see item give. Use item on altar. Activate altar to create, you fight.");
        player("Ok, sounds like a challenge.");
    }
}
