package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import mgi.types.config.ObjectDefinitions;

public class CatacombsSteppingStone implements Shortcut {
	
	@Override
	public void startSuccess(Player player, WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final float preciseX = object.getPreciseCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation());
		final float preciseY = object.getPreciseCoordFaceY(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation());
		final int direction = DirectionUtil.getFaceDirection(preciseX - player.getX(), preciseY - player.getY());
		
		player.faceObject(object);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(Animation.JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, direction));
				} else if(ticks == 1) {
					player.setLocation(object);
					stop();
				}
				
				ticks++;
			}
			
			
		}, 0, 0);
	}
	
	@Override
	public int getLevel(WorldObject object) {
		return 28;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 28893 };
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 2;
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 0;
	}

}
