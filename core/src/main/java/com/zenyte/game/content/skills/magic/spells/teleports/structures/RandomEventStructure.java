package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.EvilBobIsland;

/**
 * @author Kris | 25/06/2019 20:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RandomEventStructure extends RegularStructure {
    private static final Location edgeville = new Location(3088, 3489, 0);

    @Override
    public boolean isAreaPrevented(final Player player, final Teleport teleport) {
        return false;
    }

    @Override
    public Location getRandomizedLocation(final Player player, final Teleport teleport) {
        final Location dest = teleport.getDestination();
        if (dest != null) {
            final RegionArea area = GlobalAreaManager.getArea(dest);
            if (area != null && area.getClass().equals(EvilBobIsland.class)) {
                dest.setLocation(edgeville);
            }
        }
        return new Location(Utils.getOrDefault(randomize(player, teleport), edgeville));
    }

    private Location randomize(final Player player, final Teleport teleport) {
        final int randomization = teleport.getRandomizationDistance();
        final Location destination = new Location(teleport.getDestination());
        int count = RANDOMIZATION_ATTEMPT_COUNT;
        while (--count > 0) {
            final Location tile = RandomLocation.random(destination, randomization);
            World.loadRegion(tile.getRegionId());
            if (ProjectileUtils.isProjectileClipped(player, null, destination, tile, true) || !World.isFloorFree(tile, player.getSize())) continue;
            return tile;
        }
        return null;
    }
}
