package com.zenyte.game.content.event.easter2020.plugin.object;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.SkipPluginScan;

/**
 * @author Corey
 * @since 02/04/2020
 */
@SkipPluginScan
public class WaterTank implements ObjectAction {
    
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getInventory().addItem(new Item(ItemId.BUCKET_OF_WATER)).isFailure()) {
            player.sendMessage("You do not have enough space in your inventory to do this.");
        } else {
            player.setAnimation(new Animation(827));
            player.sendMessage("You fill a bucket of water from the tank.");
        }
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{EasterConstants.WATER_TANK};
    }
    
}
