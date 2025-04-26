package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Failable;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import mgi.types.config.ObjectDefinitions;

import java.util.Arrays;
import java.util.List;

public class DraynorSteppingStones implements Shortcut, Failable {

	private static final Animation JUMP = new Animation(769);
	private static final Graphics SPLASH = new Graphics(68);
	
	private static final Animation FALL_EAST = new Animation(2581);
	private static final Animation FALL_WEST = new Animation(2582);
	
	private static final Location WALK_WEST = new Location(3148, 3363, 0);
	private static final Location WALK_EAST = new Location(3155, 3363, 0);
	private static final Location SHORE_WEST = new Location(3149, 3361, 0);
	private static final Location SHORE_EAST = new Location(3155, 3361, 0);
	
	private static final RenderAnimation FALL = new RenderAnimation(765, 765, 765, 765, 765, 765, -1);
	private static final RenderAnimation SWIM = new RenderAnimation(772, 772, 772, 772, 772, 772, -1);
	
	private static final int PRESHORE_E = 51694881;
	private static final List<Integer> SUCCESS_ROCKS = Arrays.asList(new Integer[] { 51612963, 51662115 });
	
	@Override
	public void startSuccess(Player player, WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final float preciseX = object.getPreciseCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation());
		final float preciseY = object.getPreciseCoordFaceY(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation());
		final int direction = DirectionUtil.getFaceDirection(preciseX - player.getX(), preciseY - player.getY());
		final boolean west = direction == 511;
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;

			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, direction));
				} else if(ticks == 1) {
					player.setLocation(object);
					if(!SUCCESS_ROCKS.contains(object.getPositionHash()))
						stop();
				} else if(ticks == 2) {
					player.addWalkSteps(west ? 3149 : 3154, 3363);
					stop();
				}
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public void startFail(Player player, WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final float preciseX = object.getPreciseCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation());
		final float preciseY = object.getPreciseCoordFaceY(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation());
		final int direction = DirectionUtil.getFaceDirection(preciseX - player.getX(), preciseY - player.getY());
		final boolean west = direction == 511;
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(player.getLocation().getPositionHash() == PRESHORE_E || player.getLocation().getPositionHash() == SHORE_WEST.getPositionHash()) {
					player.addWalkSteps(west ? WALK_WEST.getX() : WALK_EAST.getX(), 3363, -1, false);
					player.applyHit(new Hit(Utils.random(1, 4), HitType.REGULAR));
					player.getAppearance().resetRenderAnimation();
				}
				
				if(ticks == 0) {
					player.setAnimation(JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, direction));
				} else if(ticks == 1)
					player.setLocation(object);
				else if(ticks == 2)
					player.setAnimation(west ? FALL_EAST : FALL_WEST);
				else if(ticks == 3) {
					player.sendMessage("You slip and fall...");
					player.getAppearance().setRenderAnimation(FALL);
					player.setLocation(new Location(object.getX(), object.getY()-1, 0));
					player.setGraphics(SPLASH);
				} else if(ticks == 5) {
					player.getAppearance().setRenderAnimation(SWIM);
					player.addWalkSteps(player.getX(), player.getY()-1, -1, false);
				} else if(ticks == 6)
					player.addWalkSteps(west ? SHORE_WEST.getX() : SHORE_EAST.getX(), 3361, -1, false);
				else if(ticks == 11) {
					player.applyHit(new Hit(Utils.random(1, 4), HitType.REGULAR));
					stop();
				}
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public double getSuccessModifier(final Player player, final WorldObject object) {
		if(SUCCESS_ROCKS.contains(object.getPositionHash()))
				return 1;	
		return 0;
	}
	
	@Override
	public String getStartMessage(final boolean success) {
		return "You attempt to balance on the stepping stone.";
	}
	
	@Override
	public String getEndMessage(final boolean success) {
		if (!success)
			return "You manage to swim back to shore.";
		return "You manage to make the jump.";
	}
	
	@Override
	public int getLevel(WorldObject object) {
		return 31;
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return success ? 2 : 11;
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 3;
	}

	@Override
	public double getFailXp(WorldObject object) {
		return 1;
	}
	
	@Override
	public int[] getObjectIds() {
		return new int[] { 16533 };
	}

}
