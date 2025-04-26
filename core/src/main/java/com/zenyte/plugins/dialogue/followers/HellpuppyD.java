package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 23-11-2018 | 23:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class HellpuppyD extends Dialogue {

    public HellpuppyD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int random = Utils.random(4);
        switch (random) {
        case 0: 
            player("How many souls have you devoured?");
            npc("None.");
            player("Awww p-");
            npc("Yet.");
            player("Oh.");
            break;
        case 1: 
            player("I wonder if I need to invest in a trowel when I take you out for a walk.");
            npc("More like a shovel.");
            break;
        case 2: 
            player("Why were the hot dogs shivering?");
            npc("Grrrrr...");
            player("Because they were served-");
            npc("GRRRRRR...");
            player("-with... chilli?");
            break;
        case 3: 
            player("Hell yeah! Such a cute puppy!");
            npc("Silence mortal! Or I'll eat your soul.");
            player("Would that go well with lemon?");
            npc("Grrr...");
            break;
        case 4: 
            player("What a cute puppy, how nice to meet you.");
            npc("It'd be nice to meat you too...");
            player("Urk... nice doggy.");
            npc("Grrr...");
            break;
        }
    }
}
