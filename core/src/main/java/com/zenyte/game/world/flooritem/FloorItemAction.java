package com.zenyte.game.world.flooritem;


import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.FloorItemEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.FloorItemStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.plugins.flooritem.FloorItemPlugin;
import com.zenyte.plugins.flooritem.FloorItemPluginLoader;
import mgi.types.config.items.ItemDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tom
 */
public enum FloorItemAction {
    ;

    private static final Logger logger = LoggerFactory.getLogger(FloorItemAction.class);

    public static void handle(final Player player, final int itemId, final Location tile, final int optionId,
                              final boolean forcerun) {
        if (optionId == 6) {
            ItemUtil.sendItemExamine(player, itemId);
            return;
        }
        final FloorItem item = World.getRegion(tile.getRegionId(), true).getFloorItem(itemId, tile, player);
        if (item == null || player.isDead() || player.isFinished() || player.isLocked()) {
            return;
        }
        final int regionId = tile.getRegionId();
        if (!player.getMapRegionsIds().contains(regionId)) {
            return;
        }
        if (forcerun && player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
            player.setLocation(new Location(tile));
            return;
        } else if (forcerun) {
            player.setRun(true);
        }
        player.stopAll();
        final ItemDefinitions definitions = item.getDefinitions();
        if (definitions == null) {
            return;
        }
        final String option = definitions.getGroundOptions()[optionId - 1];
        if (option == null) {
            return;
        }
        player.setFacedInteractableEntity(item);
        if (logger.isDebugEnabled())
            logger.debug(item.getName() + "(" + itemId + "), " + option + "(" + optionId + "), " + tile);

        player.setRouteEvent(new FloorItemEvent(player, new FloorItemStrategy(item), () -> {
            if (player.getLocation().getPositionHash() != item.getLocation().getPositionHash()) {
                player.setFaceLocation(item.getLocation());
            }
            final FloorItemPlugin plugin = FloorItemPluginLoader.plugins.get(item.getId());
            if (option.equals("Take") && (plugin == null || !plugin.overrideTake())) {
                World.takeFloorItem(player, item);
                return;
            }
            if (plugin != null) {
                plugin.handle(player, item, optionId, option);
            }
        }));
    }

}
