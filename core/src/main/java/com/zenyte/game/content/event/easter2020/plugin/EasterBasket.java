package com.zenyte.game.content.event.easter2020.plugin;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.plugins.SkipPluginScan;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 12/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SkipPluginScan
public class EasterBasket extends ItemPlugin {
    private static final Projectile projectile = new Projectile(-1, 25, 5, 5, 120, 30, 0, 5);
    private static final Projectile secondProjectile = new Projectile(-1, 25, 5, 30, 50, 30, 0, 5);
    private final int[] graphics = new int[] {1277, 1278, 1279, 1280, 1281, 1282};

    @Override
    public void handle() {
        bind("Fling", (player, item, container, slotId) -> {
            final IntArrayList list = new IntArrayList(graphics);
            final int firstGfx = list.removeInt(Utils.random(list.size() - 1));
            final int secondGfx = list.removeInt(Utils.random(list.size() - 1));
            final Projectile proj = new Projectile(firstGfx, projectile.getStartHeight(), projectile.getEndHeight(), projectile.getDelay(), projectile.getAngle(), projectile.getDuration(), projectile.getDistanceOffset(), projectile.getMultiplier());
            final Projectile secondProj = new Projectile(secondGfx, secondProjectile.getStartHeight(), secondProjectile.getEndHeight(), secondProjectile.getDelay(), secondProjectile.getAngle(), secondProjectile.getDuration(), secondProjectile.getDistanceOffset(), secondProjectile.getMultiplier());
            player.setAnimation(new Animation(2876));
            final Location firstTile = player.getLocation().transform(Direction.getNPCDirection(player.getRoundedDirection()), 4);
            final Location secondTile = player.getLocation().transform(Direction.getNPCDirection(player.getRoundedDirection(1024)), 2);
            World.sendProjectile(player.getLocation(), firstTile, proj);
            World.sendProjectile(player.getLocation(), secondTile, secondProj);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.EASTER_BASKET};
    }
}
