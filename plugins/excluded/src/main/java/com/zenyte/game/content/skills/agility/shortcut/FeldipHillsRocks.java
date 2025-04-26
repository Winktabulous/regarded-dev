package com.zenyte.game.content.skills.agility.shortcut;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class FeldipHillsRocks implements Shortcut {

	private static final Animation CRAWL = new Animation(1148);
	private static final RenderAnimation RENDER = new RenderAnimation(738, 737, 737, 737, 737, 737, -1);

	private static final Location FACE = new Location(2483, 2898, 0);
	private static final Location TOP = new Location(2485, 2898, 0);
	private static final Location BOTTOM = new Location(2489, 2898, 0);
	private static final ForceMovement CRAWL_DOWN = new ForceMovement(BOTTOM, 120, ForceMovement.WEST);

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final boolean direction = object.getId() == 31758; // going up
		
		if(direction) {
			player.getAppearance().setRenderAnimation(RENDER);
			player.addWalkSteps(TOP.getX(), TOP.getY(), -1, false);
		}
		
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			
			@Override
			public void run() {
				if(direction) {
					if(player.getLocation().getPositionHash() == TOP.getPositionHash()) {
						player.getAppearance().resetRenderAnimation();
						stop();
					}
				} else {
					if(ticks == 0)
						player.setFaceLocation(FACE);
					else if(ticks == 2) {
						player.setAnimation(CRAWL);
						player.setForceMovement(CRAWL_DOWN);
					} else if(ticks == 6)
						player.setLocation(BOTTOM);
					else if(ticks == 7)
						stop();
				}
				
				ticks++;
			}
			
		}, 0, 0);
	}
		
	
	// level could not be found
	@Override
	public int getLevel(final WorldObject object) {
		return 30;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 31758, 31759 };
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
