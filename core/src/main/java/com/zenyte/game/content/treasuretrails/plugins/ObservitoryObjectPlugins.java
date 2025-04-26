package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

public class ObservitoryObjectPlugins implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == 25434) {
            player.setLocation(new Location(2335, 9350));
        }
        if (object.getId() == 25429) {
            // Move em to the ruined building
            if (player.getPosition().equals(2355, 9394, 0)) {
                player.setLocation(new Location(2457, 3186));
            } else {
                // go to observitory
                player.setLocation(new Location(2439, 3164));
            }
        }

        if (object.getId() == 25432) {
            player.setLocation(new Location(2355, 9394, 0));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                25434, // The stairwell in the observitory
                25429, // stairwells in the dungeoun to observitory
                25432 // The stairwell at the ruined building
        };
    }
}
