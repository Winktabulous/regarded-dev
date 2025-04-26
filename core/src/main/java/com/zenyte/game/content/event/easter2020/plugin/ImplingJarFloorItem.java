package com.zenyte.game.content.event.easter2020.plugin;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.flooritem.FloorItemPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Corey
 * @since 29/03/2020
 */
@SkipPluginScan
public class ImplingJarFloorItem implements FloorItemPlugin {
    
    @Override
    public void handle(Player player, FloorItem item, int optionId, String option) {
        if (player.getInventory().addItem(new Item(EasterConstants.EasterItem.IMPLING_JAR.getItemId())).isFailure()) {
            player.sendMessage("You do not have enough space to pick this up.");
        } else {
            player.sendMessage("You pick up a jar.");
        }
    }
    
    @Override
    public int[] getItems() {
        return new int[]{EasterConstants.EasterItem.IMPLING_JAR.getItemId()};
    }
    
    @Override
    public boolean overrideTake() {
        return true;
    }
    
    @Override
    public boolean canTelegrab(@NotNull Player player, @NotNull FloorItem item) {
        return false;
    }
    
    @Override
    public void telegrab(@NotNull Player player, @NotNull FloorItem item) {
    
    }
    
}
