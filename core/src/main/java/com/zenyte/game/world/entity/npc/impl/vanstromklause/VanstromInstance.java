package com.zenyte.game.world.entity.npc.impl.vanstromklause;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;

public class VanstromInstance extends DynamicArea {

    private final int ENERGY_BARRIER = 38000;
    private final int ENERGY_BARRIER_LONG = 38001;

    private final Location L1 = new Location(3575, 3353);
    private final Location L2 = new Location(3575, 3357);
    private final Location L3 = new Location(3575, 3361);

    private final VanstromKlause vanstromKlause;

    public VanstromInstance(AllocatedArea allocatedArea, Player player) {
        super(allocatedArea, 3522 / 8, 3332 / 8);
        vanstromKlause = new VanstromKlause(this, player);
        vanstromKlause.spawn();
        player.getBossTimer().startTracking("Vanstrom Klause");
    }

    @Override
    public void constructed() {
        World.spawnObject(new WorldObject(ENERGY_BARRIER_LONG, 10, 0, getLocation(L1)));
        World.spawnObject(new WorldObject(ENERGY_BARRIER_LONG, 10, 0, getLocation(L2)));
        World.spawnObject(new WorldObject(ENERGY_BARRIER, 10, 0, getLocation(L3)));
    }

    @Override
    public void destroyRegion() {
        super.destroyRegion();
        World.removeObject(World.getObjectWithType(L1, 10));
        World.removeObject(World.getObjectWithType(L2, 10));
        World.removeObject(World.getObjectWithType(L3, 10));
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {
        player.getHpHud().close();
        if(!vanstromKlause.isFinished()) {
            vanstromKlause.finish();
        }
    }

    @Override
    public String name() {
        return "Vanstrom Klause fight";
    }
}
