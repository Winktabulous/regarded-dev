package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.actions.FadeScreenAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.HashMap;
import java.util.Map;

public class GrappleWall implements Shortcut {
	
	private static final Map<Integer, Location> GRAPPLES = new HashMap<Integer, Location>();
	
	private static final Animation CROSSBOW = new Animation(4455);
	private static final Graphics GRAPPLE = new Graphics(760, 0, 100);
	private static final int MITH_GRAPPLE = 9419;
	
	private static final Location FALLY_SOUTH_START = new Location(3032, 3388, 0);
	private static final Location FALLY_NORTH_GRAPPLE = new Location(3033, 3390, 0);
	private static final Location FALLY_SOUTH_GRAPPLE = new Location(3032, 3389, 0);
	private static final Location FALLY_WALKWAY_NORTH = new Location(3033, 3389, 1);
	private static final Location FALLY_WALKWAY_SOUTH = new Location(3032, 3389, 1);
	
	private static final Location YANILLE_NORTH_GRAPPLE = new Location(2556, 3075, 0);
	private static final Location YANILLE_SOUTH_GRAPPLE = new Location(2556, 3072, 0);
	private static final Location YANILLE_WALKWAY_NORTH = new Location(2556, 3074, 1);
	private static final Location YANILLE_WALKWAY_SOUTH = new Location(2556, 3073, 1);
	
	static {
		GRAPPLES.put(FALLY_NORTH_GRAPPLE.getPositionHash(), FALLY_WALKWAY_NORTH);
		GRAPPLES.put(FALLY_SOUTH_GRAPPLE.getPositionHash(), FALLY_WALKWAY_SOUTH);
		GRAPPLES.put(YANILLE_NORTH_GRAPPLE.getPositionHash(), YANILLE_WALKWAY_NORTH);
		GRAPPLES.put(YANILLE_SOUTH_GRAPPLE.getPositionHash(), YANILLE_WALKWAY_SOUTH);
	}

	@Override
    public boolean preconditions(final Player player, final WorldObject object) {
        if (GRAPPLES.get(object.getPositionHash()) == null)
            return false;

        if(player.getWeapon() == null || !player.getWeapon().getName().toLowerCase().contains("crossbow")) {
            player.sendMessage("You need a crossbow equipped to do that.");
            return false;
        }

        if(player.getAmmo() == null || player.getAmmo().getId() != MITH_GRAPPLE) {
            player.sendMessage("You need a mithril grapple tipped bolt with a rope to do that.");
            return false;
        }
        return true;
    }
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.faceObject(object);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(CROSSBOW);
					player.setGraphics(GRAPPLE);
				} else if(ticks == 8) 
					new FadeScreenAction(player, 4).run();
				else if(ticks == 11)
					player.setAnimation(Animation.STOP);
				else if(ticks == 12) {
					player.setLocation(GRAPPLES.get(object.getPositionHash()));
					stop();
				}
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {	
		final boolean yanille = object.getRegionId() == 10032;
		final boolean north = yanille ? (object.getPositionHash() == YANILLE_NORTH_GRAPPLE.getPositionHash()) : (object.getPositionHash() == FALLY_NORTH_GRAPPLE.getPositionHash());
		return yanille ? (north ? YANILLE_NORTH_GRAPPLE : YANILLE_SOUTH_GRAPPLE) : (north ? FALLY_NORTH_GRAPPLE : FALLY_SOUTH_START);
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return object.getRegionId() == 10032 ? 39 : 11;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 17047, 17049, 17050 };
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 13;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 0;
	}
	
}
