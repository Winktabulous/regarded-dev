package com.zenyte.game.world.entity.player.action;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 07/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PlayerFollow extends Action {
    private final Player target;

    @Override
    public boolean interruptedByCombat() {
        return false;
    }

    public PlayerFollow(final Player target) {
        this.target = target;
    }

    private boolean intelligent = true;

    @Override
    public boolean start() {
        player.setFaceEntity(target);
        if (checkAll(player)) {
            player.setFollowStage(Player.FollowStage.STARTED);
            return true;
        }
        player.setFaceEntity(null);
        return false;
    }

    private boolean checkAll(Player player) {
        if (player.isDead() || player.isFinished() || target.isDead() || target.isFinished()) return false;
        if (player.getPlane() != target.getPlane()) return false;
        if (player.isFrozen() || player.isStunned()) return true;
        int distanceX = player.getX() - target.getX();
        int distanceY = player.getY() - target.getY();
        int size = player.getSize();
        int maxDistance = 16;
        if (player.getPlane() != target.getPlane() || distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) return false;
        if (player.getLocation().getTileDistance(target.getLocation()) <= 2) {
            intelligent = false;
        }
        player.resetWalkSteps();
        player.calcFollow(getDestination(), -1, true, intelligent, false);
        return true;
    }

    public Location getDestination() {
        Location lastLocation = target.getFollowTile();
        if (lastLocation == null) {
            return target.getLocation().transform(Direction.WEST);
        }
        return lastLocation;
    }

    @Override
    public boolean process() {
        return checkAll(player);
    }

    @Override
    public int processWithDelay() {
        return 0;
    }

    @Override
    public void stop() {
        player.setFaceEntity(null);
    }
}
