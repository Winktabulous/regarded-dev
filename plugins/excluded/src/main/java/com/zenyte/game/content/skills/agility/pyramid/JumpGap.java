package com.zenyte.game.content.skills.agility.pyramid;

import com.zenyte.game.content.skills.agility.IrreversibleDirection;
import com.zenyte.game.content.skills.agility.pyramid.area.AgilityPyramidArea;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public final class JumpGap extends FailableAgilityPyramidObstacle implements IrreversibleDirection {
    private static final Animation jumpAnim = new Animation(3067);
    private static final Animation fallAnim = new Animation(3068);

    public JumpGap() {
        super(5);
    }

    @Override
    public int permanentSuccessLevel() {
        return 75;
    }

    @Override
    public String getFilterableStartMessage(boolean success) {
        return "You jump the gap...";
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        cross(player, object, true);
    }

    @Override
    public void startFail(Player player, WorldObject object) {
        cross(player, object, false);
    }

    @Override
    public void endFail(Player player, WorldObject object) {
        player.applyHit(new Hit(8, HitType.REGULAR));
        player.sendFilteredMessage("... and miss your footing.");
    }

    private void cross(final Player player, final WorldObject object, final boolean success) {
        final Direction walkDirection = Direction.getNPCDirection(player.getRoundedDirection());
        final Location destination = player.getLocation().transform(walkDirection, success ? 3 : 1);
        player.faceObject(object);
        player.sendSound(new SoundEffect(success ? 2465 : 2463, 1, 20));
        player.setAnimation(success ? jumpAnim : fallAnim);
        WorldTasksManager.schedule(() -> player.setForceMovement(new ForceMovement(destination, 30, walkDirection.getDirection())));
        WorldTasksManager.schedule(() -> {
            if (!success) {
                player.setAnimation(Animation.STOP);
            }
            player.setLocation(success ? destination : AgilityPyramidArea.getLowerTile(destination.transform(object.getFaceDirection(), 2)));
        }, success ? 1 : 7);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 22;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 30;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return success ? 2 : 9;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {10859};
    }

    @Override
    public double getFailXp(WorldObject object) {
        return 0;
    }

    @Override
    public Direction getReverseDirection(final Player player, final WorldObject object) {
        return object.getFaceDirection().getCounterClockwiseDirection(6);
    }

    @Override
    public boolean failOnReverse() {
        return true;
    }
}
