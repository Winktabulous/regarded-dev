package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * @author Kris | 12/05/2019 14:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LighthouseBasaltRocks implements Shortcut {

    private static final Object2ObjectMap<Location, Location> routeMap = new Object2ObjectOpenHashMap<>();

    static {
        routeMap.put(new Location(2522, 3597, 0), new Location(2522, 3595, 0));
        routeMap.put(new Location(2522, 3600, 0), new Location(2522, 3602, 0));
        routeMap.put(new Location(2518, 3611, 0), new Location(2516, 3611, 0));
        routeMap.put(new Location(2514, 3613, 0), new Location(2514, 3615, 0));
        routeMap.put(new Location(2514, 3617, 0), new Location(2514, 3619, 0));
        // Reverse fill.
        final Object2ObjectOpenHashMap<Location, Location> altMap = new Object2ObjectOpenHashMap<Location, Location>();
        for (final Object2ObjectMap.Entry<Location, Location> entry : routeMap.object2ObjectEntrySet()) {
            altMap.put(entry.getValue(), entry.getKey());
        }
        routeMap.putAll(altMap);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 0;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        if (object.getId() == ObjectId.BASALT_ROCK_4558) {
            if (player.getY() <= 3617) {
                return new Location(2514, 3617, 0);
            }
            return new Location(2514, 3619, 0);
        } else if (object.getId() == ObjectId.ROCKY_SHORE) {
            if (player.getY() >= 3619) {
                return new Location(2514, 3619, 0);
            }
            return new Location(214, 3617, 0);
        } else if (object.getId() == ObjectId.BEACH) {
            if (player.getY() <= 3595) {
                return new Location(2522, 3595, 0);
            }
            return new Location(2522, 3597, 0);
        } else if (object.getId() == ObjectId.BASALT_ROCK) {
            if (player.getX() >= 3597) {
                return new Location(2522, 3597, 0);
            }
            return new Location(2522, 3595, 0);
        }
        return new Location(object);
    }

    @Override
    public boolean preconditions(final Player player, final WorldObject object) {
        if (object.getId() == 4550 && player.getY() <= 3595) {
            player.sendMessage("You\'re already at the beach.");
            return false;
        }
        if (object.getId() == 4559 && player.getY() >= 3619) {
            player.sendMessage("You\'re already at the shore.");
            return false;
        }
        return true;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 4550, 4551, 4552, 4553, 4554, 4555, 4556, 4557, 4558, 4559 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 1;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final Location dest = routeMap.get(player.getLocation());
        player.setFaceLocation(dest);
        player.setAnimation(new Animation(769));
        player.setForceMovement(new ForceMovement(player.getLocation(), 15, dest, 35, DirectionUtil.getFaceDirection(dest.getX() - player.getX(), dest.getY() - player.getY())));
        WorldTasksManager.schedule(() -> player.setLocation(dest));
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
