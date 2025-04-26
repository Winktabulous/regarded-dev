package com.zenyte.game.content.minigame.wintertodt;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Corey
 * @since 17:53 - 25/08/2019
 */
public class WintertodtPillarGapShortcut implements Shortcut {
    private static final Animation JUMP = new Animation(741);

    @Override
    public boolean preconditions(Player player, WorldObject object) {
        if (player.getTemporaryAttributes().containsKey("wintertodt pillar lock " + object.getPositionHash())) {
            player.sendMessage("You only just jumped over that.");
            return false;
        }
        return true;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        final boolean jumpingWest = player.getX() > object.getX();
        final Direction direction = jumpingWest ? Direction.WEST : Direction.EAST;
        final int faceDirection = direction.getDirection();
        final Location newLocation = object.getPosition().transform(direction, 1);
        player.faceObject(object);
        player.setAnimation(JUMP);
        player.sendSound(new SoundEffect(2462));
        player.autoForceMovement(newLocation, 15, 30, faceDirection);
        player.addTemporaryAttribute("wintertodt pillar lock " + object.getPositionHash(), true);
        WorldTasksManager.schedule(() -> player.getTemporaryAttributes().remove("wintertodt pillar lock " + object.getPositionHash()), 6);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 60;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 1;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 18;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {29326};
    }
}
