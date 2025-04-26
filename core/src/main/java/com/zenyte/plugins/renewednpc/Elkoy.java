package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;

/**
 * @author Kris | 11/05/2019 19:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Elkoy extends NPCPlugin {

    @Override
    public void handle() {
        bind("Follow", (player, npc) -> {
            player.sendMessage("Elkoy takes you through the maze...");
            new FadeScreen(player, () -> player.setLocation(player.getLocation().withinDistance(2515, 3159, 5) ? new Location(2503, 3192, 0) : new Location(2515, 3159, 0))).fade(3);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 6265, 4968, 6266 };
    }
}
