package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class GrandExchangeUnderwallTunnel implements Shortcut {
	
	/* Animations */
	private static final Animation DUCK = new Animation(2589);
	private static final Animation INVISIBLE = new Animation(2590);
	private static final Animation EMERGE = new Animation(2591);
	
	/* Entry tiles and paths */
	private static final Location ENTRY_WEST = new Location(3138, 3516, 0);
	private static final Location ENTRY_EAST = new Location(3141, 3513, 0);
	private static final ForceMovement ENTER_WEST = new ForceMovement(ENTRY_WEST, 60, ForceMovement.EAST);
	private static final ForceMovement ENTER_EAST = new ForceMovement(ENTRY_EAST, 60, ForceMovement.WEST);
	
	/* Invisible tiles and paths */
	private static final Location PATH_WEST = new Location(3141, 3513, 0);
	private static final Location PATH_EAST = new Location(3138, 3516, 0);
	private static final ForceMovement INVISIBLE_WEST = new ForceMovement(PATH_WEST, 120, ForceMovement.EAST);
	private static final ForceMovement INVISIBLE_EAST = new ForceMovement(PATH_EAST, 120, ForceMovement.WEST);
	
	/* Exit tiles and paths */
	private static final Location EXIT_WEST = new Location(3142, 3513, 0);
	private static final Location EXIT_EAST = new Location(3137, 3516, 0);
	private static final ForceMovement EMERGE_WEST = new ForceMovement(EXIT_WEST, 60, ForceMovement.EAST);
	private static final ForceMovement EMERGE_EAST = new ForceMovement(EXIT_EAST, 60, ForceMovement.WEST);
	
	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final boolean direction = object.getId() == 16529;
		player.setAnimation(DUCK);
		player.setForceMovement(direction ? ENTER_WEST : ENTER_EAST);
		
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 1) {
					player.setAnimation(INVISIBLE);
					player.setForceMovement(direction ? INVISIBLE_WEST : INVISIBLE_EAST);
				} else if(ticks == 5) {
					player.setAnimation(EMERGE);
					player.setForceMovement(direction ? EMERGE_WEST : EMERGE_EAST);
				} else if (ticks == 7) {
					player.setLocation(direction ? EXIT_WEST : EXIT_EAST);
					stop();
				}
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 21;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 16529, 16530 };
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 8;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 0;
	}
	
}
