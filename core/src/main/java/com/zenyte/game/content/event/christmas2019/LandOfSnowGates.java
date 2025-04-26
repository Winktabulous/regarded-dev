package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Corey
 * @since 14/12/2019
 */
public class LandOfSnowGates implements ObjectAction {
    
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        int x, y;
    
        if (object.getId() == ChristmasConstants.LAND_OF_SNOW_GATES) {
            if (!AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SPOKEN_TO_PERSONAL_IMP)) {
                player.sendMessage("You should speak with the Queen first.");
                return;
            }
            x = 2463 + Utils.random(0, 3);
            y = 5385 + Utils.random(0, 1);
        } else {
            x = 2097 + Utils.random(0, 1);
            y = 5404 + Utils.random(0, 3);
        }
    
        new FadeScreen(player, () -> player.setLocation(new Location(x, y))).fade(3);
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{ChristmasConstants.LAND_OF_SNOW_GATES, ChristmasConstants.SCOURGE_GATES};
    }
    
}
