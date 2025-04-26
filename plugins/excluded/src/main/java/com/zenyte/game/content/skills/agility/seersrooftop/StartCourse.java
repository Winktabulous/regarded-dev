/**
 * 
 */
package com.zenyte.game.content.skills.agility.seersrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
/**
 * @author Noele | May 1, 2018 : 2:40:50 AM
 * @see https://noeles.life || noele@zenyte.com
 */
public final class StartCourse extends AgilityCourseObstacle {
	
	private static final Animation CLIMB = new Animation(737);
	private static final Animation HANG = new Animation(1118);
	
	private static final Location MAILBOX = new Location(2729, 3488, 1);
	private static final Location FINISH = new Location(2729, 3491, 3);

	public StartCourse() {
		super(SeersRooftopCourse.class, 1);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.faceObject(object);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0)
					player.setAnimation(CLIMB);
				else if(ticks == 1) {
					player.setAnimation(HANG);
					player.setLocation(MAILBOX);
					player.sendFilteredMessage("...jump, and grab hold of the sign!");
				} else if(ticks == 3) {
					player.setAnimation(Animation.STOP);
					player.setLocation(FINISH);
					player.addAttribute("SeersTrapdoor", 0);
					MarkOfGrace.spawn(player, SeersRooftopCourse.MARK_LOCATIONS, 60, 20);
					stop();
				}
				ticks++;
			}
		}, 0, 0);
	}

	@Override
	public String getFilterableStartMessage(final boolean success) {
		return "You climb up the wall...";
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 60;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 4;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 45;
	}
	
	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.WALL_14927 };
	}
}
