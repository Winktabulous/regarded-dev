package com.zenyte.game.content.event.easter2020.plugin.object;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.event.easter2020.plugin.npc.EasterBird;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Corey
 * @since 02/04/2020
 */
@SkipPluginScan
public class WaterBowl implements ItemOnObjectAction {
    
    public static void waterOnBowl(final Player player, final Item item) {
        if (EasterBird.Varbit.WATER_BOWL.isSet(player)) {
            player.getDialogueManager().start(new PlainChat(player, "The bowl is already full of water."));
            return;
        }
        
        EasterBird.Varbit.WATER_BOWL.sendVar(player);
        player.getDialogueManager().start(new PlainChat(player, "You fill the bowl with water."));
        player.getInventory().deleteItem(item);
    }
    
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        waterOnBowl(player, item);
    }
    
    @Override
    public Object[] getItems() {
        return new Object[]{ItemId.BUCKET_OF_WATER};
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{EasterConstants.WATER_BOWL};
    }
}
