package com.zenyte.game.world.entity.npc.impl;

import com.zenyte.game.content.skills.fishing.FishingLocations;
import com.zenyte.game.content.skills.fishing.FishingLocations.SpotLocations;
import com.zenyte.game.content.skills.fishing.SpotDefinitions;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

public class FishingSpot extends NPC implements Spawnable {

    private int ticks;
    private SpotLocations area;
    private Location current;

    public FishingSpot(final int id, final Location location, final Direction direction, final int radius) {
        super(id, location, direction, radius);
        if (location == null)
            return;
        current = location;
        setRadius(0);
        area = SpotLocations.getArea(location.getRegionId());
		ticks = !hasOtherAreas() ? 1500 : Utils.random(200, 600);
		FishingLocations.occupied.add(location);
	}

	@Override
    public boolean isPathfindingEventAffected() {
        return false;
    }
	
	@Override
	public void processNPC() {
		super.processNPC();
		processSpot();
	}

	protected void processSpot() {
        if(--ticks == 0) {
            //Needs to be executed thru a task cus otherwise u will face where the npc goes.
            //WorldTasksManager.schedule(() -> {
            if (area != null && area.getLocations() != null && area.getLocations().length > 0) {
                Location location = area.getLocations()[Utils.random(area.getLocations().length - 1)];
                int tryCount = 100;
                while (--tryCount > 0 && FishingLocations.occupied.contains(location)) {
                    location = area.getLocations()[Utils.random(area.getLocations().length - 1)];
                }
                FishingLocations.occupied.remove(current);
                FishingLocations.occupied.add(location);

                setLocation(location);
                current = location;
            }
            ticks = !hasOtherAreas() ? 1500 : Utils.random(200, 600);
            //});
        }
    }

    public boolean hasOtherAreas() {
        return area != null && area.getLocations() != null && area.getLocations().length > 0;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return (id < 7730 || id > 7733) && SpotDefinitions.getNpcs().contains(id);
    }

    public int getTicks() {
        return ticks;
    }
}
