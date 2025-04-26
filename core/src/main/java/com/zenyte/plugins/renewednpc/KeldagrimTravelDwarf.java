package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;

/**
 * @author Tommeh | 27-4-2019 | 17:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KeldagrimTravelDwarf extends NPCPlugin {
    private static final Location PAST_RIVER_KELDA = new Location(2836, 10140, 0);
    private static final Location BEYOND_RIVER_KELDA = new Location(2840, 10132, 0);
    private static final Location KELDAGRIM = new Location(2887, 10223, 0);
    private static final Location SOUTH_EAST_DOCKS = new Location(2857, 10133, 0);

    @Override
    public void handle() {
        bind("Travel", (player, npc) -> {
            final Location destination = npc.getId() == 4896 ? PAST_RIVER_KELDA : npc.getId() == 2432 ? KELDAGRIM : npc.getId() == 2433 ? BEYOND_RIVER_KELDA : SOUTH_EAST_DOCKS;
            new FadeScreen(player, () -> {
                player.setLocation(destination);
                player.setFaceEntity(null);
            }).fade(3);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {7726, 2433, 4896, 4897};
    }
}
