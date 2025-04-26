package com.zenyte.game.content.skills.agility.pyramid;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public final class LowWall extends AgilityCourseObstacle {
    private static final Animation climbAnim = new Animation(840, 15);

    public LowWall() {
        super(AgilityPyramid.class, 1);
    }

    @Override
    public String getFilterableStartMessage(boolean success) {
        return "You climb the low wall...";
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.sendSound(new SoundEffect(2453, 1, 30));
        player.faceObject(object);
        player.setAnimation(climbAnim);
        final boolean forward = Math.abs(object.getFaceDirection().getDirection() - Direction.getNPCDirection(player.getRoundedDirection()).getDirection()) <= 257;
        final Direction direction = forward ? object.getFaceDirection() : object.getFaceDirection().getCounterClockwiseDirection(4);
        final Location destination = player.getLocation().transform(direction, 2);
        final Location currentTile = new Location(player.getLocation());
        player.setLocation(destination);
        player.setForceMovement(new ForceMovement(currentTile, 1, destination, 80, direction.getDirection()));
    }

    @Override
    public String getFilterableEndMessage(boolean success) {
        return "... and make it over.";
    }

    @Override
    public int getLevel(WorldObject object) {
        return 30;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 2;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 8;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {10865};
    }
}
