package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Corey
 */
public final class TempleOfIkovStairs implements ObjectAction {
    
    private static final int UPSTAIRS_OBJECT = 96;
    private static final int DOWNSTAIRS_OBJECT = 98;
    
    private static final Location UPSTAIRS_OBJECT_LOCATION = new Location(2638, 9763);
    private static final Location DOWNSTAIRS_OBJECT_LOCATION = new Location(2650, 9804);
    
    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        switch (object.getId()) {
            case UPSTAIRS_OBJECT:
                if (object.getPosition().getPositionHash() != UPSTAIRS_OBJECT_LOCATION.getPositionHash()) {
                    player.sendMessage("Nothing interesting happens.");
                    return;
                }
                player.setLocation(new Location(2649, 9805));
                break;
            
            case DOWNSTAIRS_OBJECT:
                if (object.getPosition().getPositionHash() != DOWNSTAIRS_OBJECT_LOCATION.getPositionHash()) {
                    player.sendMessage("Nothing interesting happens.");
                    return;
                }
                player.setLocation(new Location(2641, 9764));
                break;
        }
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{UPSTAIRS_OBJECT, DOWNSTAIRS_OBJECT};
    }
    
}
