package com.zenyte.plugins.flooritem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import org.jetbrains.annotations.NotNull;

/**
 * @author Corey
 * @since 15/06/2020
 */
public class WhiteApronPlugin implements FloorItemPlugin {
    
    @Override
    public void handle(Player player, FloorItem item, int optionId, String option) {
        if (player.getInventory().addItem(new Item(ItemId.WHITE_APRON)).isFailure()) {
            player.sendFilteredMessage("Not enough space in your inventory to pick the item up.");
        } else {
            World.destroyFloorItem(item);
            player.sendSound(new SoundEffect(2582));
        }
    }
    
    @Override
    public int[] getItems() {
        return new int[]{ItemId.WHITE_APRON_7957};
    }
    
    @Override
    public boolean overrideTake() {
        return true;
    }
    
    @Override
    public void telegrab(@NotNull Player player, @NotNull FloorItem item) {
        World.destroyFloorItem(item);
        player.getInventory().addItem(new Item(ItemId.WHITE_APRON)).onFailure(it -> World.spawnFloorItem(it, player));
    }
}
