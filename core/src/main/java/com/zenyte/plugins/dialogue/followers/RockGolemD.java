package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 27-3-2019 | 15:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RockGolemD extends Dialogue {

    public RockGolemD(Player player, NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        player("So you're made entirely of rocks?");
        npc("Not quite, my body is formed mostly of minerals.");
        player("Aren't minerals just rocks?");
        npc("No, rocks are rocks, minerals are minerals. I am formed from minerals.");
        player("But you're a Rock Golem...");
    }
}
