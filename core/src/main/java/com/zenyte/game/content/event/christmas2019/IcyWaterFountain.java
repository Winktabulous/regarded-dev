package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Corey
 * @since 20/12/2019
 */
public class IcyWaterFountain implements ItemOnObjectAction {
    
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.getInventory().set(slot, new Item(ChristmasConstants.ICY_WATER_BUCKET));
        player.getPacketDispatcher().sendSoundEffect(new SoundEffect(2609));
        player.setAnimation(new Animation(832));
        player.sendFilteredMessage("You fill the bucket with icy water from the fountain.");
    }
    
    @Override
    public Object[] getItems() {
        return new Object[]{ItemId.BUCKET};
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{ChristmasConstants.ICY_WATER_FOUNTAIN};
    }
    
}
