package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.CombatPointCapCalculator;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.ScalingMechanics;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.content.chambersofxeric.npc.ScavengerBeast;
import com.zenyte.game.util.WorldUtil;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.Optional;

/**
 * @author Kris | 10/08/2019 14:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class ScavengerRoom extends RaidArea {
    /**
     * The list of scavenger beasts roaming the room.
     */
    protected List<ScavengerBeast> scavengers = new ObjectArrayList<>();
    /**
     * The rough center of the room, used to spawn scavengers randomly across the room within its boundaries.
     */
    private Location center;

    ScavengerRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public void loadRoom() {
        center = new Location((chunkX << 3) + 16, (chunkY << 3) + 16, getToPlane());
        int amount = ScalingMechanics.getScavengerCount(raid);
        while (scavengers.size() < amount) {
            final Optional<Location> tile = findSpawnTile();
            if (!tile.isPresent()) {
                break;
            }
            final ScavengerBeast beast = new ScavengerBeast(this, raid, tile.get());
            scavengers.add(beast);
            beast.spawn();
        }
    }

    @Override
    public CombatPointCapCalculator buildPointsCap() {
        return new CombatPointCapCalculator().appendNPCs(getScavengers());
    }

    /**
     * Finds a suitable spawn tile for the scavenger beast within the room that doesn't collide with other scavengers.
     *
     * @return an optional spawn tile, if present, the scavenger's position is set to this. Otherwise relies on its previous spawn tile.
     */
    public final Optional<Location> findSpawnTile() {
        return WorldUtil.findEmptySquare(RandomLocation.create(center,  5), 10, 2, Optional.of(p -> {
            for (final ScavengerBeast scavenger : scavengers) {
                if (!scavenger.isDead() && !scavenger.isFinished() && p.withinDistance(scavenger, 2)) {
                    return false;
                }
            }
            return true;
        }));
    }

    public List<ScavengerBeast> getScavengers() {
        return scavengers;
    }

    public Location getCenter() {
        return center;
    }
}
