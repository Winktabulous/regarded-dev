package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.content.skills.agility.Failable;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class TaverleyDungeonStrangeFloor implements Shortcut, Failable {
	
	private static final Location WEST_START = new Location(2876, 9813, 0);
	private static final Location EAST_START = new Location(2882, 9813, 0);
	
	private static final Location WEST_END = new Location(2880, 9813, 0);
	private static final Location EAST_END = new Location(2878, 9813, 0);

	private static final Animation RUN = new Animation(1995);
	private static final Animation JUMP = new Animation(1603);
	private static final Animation SPIKES = new Animation(1111);
	
	private static final ForceMovement RUN_WEST = new ForceMovement(WEST_END, 60, ForceMovement.WEST);
	private static final ForceMovement RUN_EAST = new ForceMovement(EAST_END, 60, ForceMovement.EAST);
	
	private static final ForceMovement JUMP_WEST = new ForceMovement(WEST_END, 20, ForceMovement.EAST);
	private static final ForceMovement JUMP_EAST = new ForceMovement(EAST_END, 20, ForceMovement.WEST);
	
	@Override
	public void startSuccess(Player player, WorldObject object) {
		final boolean west = player.getX() < object.getX();
		player.setFaceLocation(object);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(RUN);
					player.setForceMovement(west ? RUN_EAST : RUN_WEST);
				} else if(ticks == 1)
					player.setAnimation(JUMP);
				else if(ticks == 2)
					player.setForceMovement(west ? JUMP_WEST : JUMP_EAST);
				else if (ticks == 3)
					player.setLocation(west ? WEST_END : EAST_END);
				else if (ticks == 4) {
					player.getAchievementDiaries().update(FaladorDiary.JUMP_OVER_STRANGE_FLOOR);
					stop();
				}
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public void startFail(Player player, WorldObject object) {
		final boolean west = player.getX() < object.getX();
		player.setFaceLocation(object);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(RUN);
					player.setForceMovement(west ? RUN_EAST : RUN_WEST);
				} else if(ticks == 1)
					player.setAnimation(JUMP);
				else if(ticks == 2) {
					player.setForceMovement(west ? JUMP_WEST : JUMP_EAST);
					player.applyHit(new Hit(Utils.random(1, 4), HitType.REGULAR));
					player.sendMessage("You trigger the trap as you jump over it.");
					World.sendObjectAnimation(object, SPIKES);
				} else if(ticks == 3) {
					player.setLocation(west ? WEST_END : EAST_END);
				} else if(ticks == 4)
					stop();
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public String getEndMessage(final boolean success) {
		return success ? "You make the jump easily." : null;
	}
	
	@Override
	public int getLevel(WorldObject object) {
		return 80;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 16510 };
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 5;
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 12.5;
	}

	@Override
	public double getFailXp(WorldObject object) {
		return 1;
	}
	
	@Override
	public double getSuccessModifier(final Player player, final WorldObject object) {	
		return 0;
	}
	
	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return player.getX() < object.getX() ? WEST_START : EAST_START;
	}

}
