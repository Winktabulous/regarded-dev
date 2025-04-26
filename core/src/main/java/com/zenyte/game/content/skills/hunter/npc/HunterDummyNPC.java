package com.zenyte.game.content.skills.hunter.npc;

import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

/**
 * @author Kris | 25/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HunterDummyNPC extends NPC {

    public HunterDummyNPC(final int id, @NotNull final Location tile) {
        super(id, tile, Direction.SOUTH, 0);
        this.spawned = true;
        this.spawnLocation = new Location(tile);
    }

    @Override
    public NPC spawn() {
        if (!isFinished()) {
            throw new RuntimeException("The NPC has already been spawned: " + getId() + ", " + getDefinitions().getName() + ", " + getNpcSpawn() + ", " + getLocation());
        }
        World.addNPC(this);
        location.setLocation(spawnLocation);
        setFinished(false);
        updateLocation();
        if (!combatDefinitionsMap.isEmpty()) {
            combatDefinitionsMap.clear();
        }
        updateCombatDefinitions();
        return this;
    }

    private WeakReference<HunterTrap> trap;
    private Location spawnLocation;

    public void setTrap(@NotNull final HunterTrap trap) {
        this.trap = new WeakReference<>(trap);
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public boolean addWalkSteps(final int destX, final int destY, final int maxStepsCount, final boolean check) {
        return false;
    }

    public WeakReference<HunterTrap> getTrap() {
        return trap;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
