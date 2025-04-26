package com.zenyte.game.world.entity.player.action;

import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.pathfinding.events.player.CombatEntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.PredictedEntityStrategy;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 17/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PeltAction extends Action {
    private static final Animation animation = new Animation(15096);
    private static final Graphics graphics = new Graphics(2504, 65, 0);
    private static final Projectile projectile = new Projectile(2503, 25, 10, 45, 5, 20, 0, 0);

    public PeltAction(@NotNull final Player target) {
        this.target = target;
    }

    private final Player target;

    @Override
    public boolean initiateOnPacketReceive() {
        if (processWithDelay() != 0) {
            player.getActionManager().forceStop();
        }
        return false;
    }

    @Override
    public boolean start() {
        player.setCombatEvent(new CombatEntityEvent(player, new PredictedEntityStrategy(target)));
        player.setLastTarget(target);
        player.setFaceEntity(target);
        return pathfind();
    }

    boolean pathfind() {
        final int maxDistance = 10;
        player.resetWalkSteps();
        if (player.isProjectileClipped(target, false) || !withinRange(target, target.getSize()) || (target.hasWalkSteps() && !withinRange(target.getNextPosition(target.isRun() ? 2 : 1), target.getSize()))) {
            appendWalksteps();
            if (player.getWalkSteps().size() >= 2) {
                final int size = target.getSize();
                final Location nextPos = player.getNextPosition(1);
                final int distanceX = nextPos.getX() - target.getNextPosition(1).getX();
                final int distanceY = nextPos.getY() - target.getNextPosition(1).getY();
                if (!(distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) && !ProjectileUtils.isProjectileClipped(player, target, nextPos, target, false)) {
                    player.getWalkSteps().resetAllButFirst();
                }
            }
        }
        return true;
    }

    final void appendWalksteps() {
        player.getCombatEvent().process();
    }

    final boolean withinRange(final Position targetPosition, final int targetSize) {
        final Location target = targetPosition.getPosition();
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int npcSize = player.getSize();
        if (distanceX == -npcSize - 10 && distanceY == -npcSize - 10 || distanceX == targetSize + 10 && distanceY == targetSize + 10 || distanceX == -npcSize - 10 && distanceY == targetSize + 10 || distanceX == targetSize + 10 && distanceY == -npcSize - 10) {
            return false;
        }
        return !(distanceX > targetSize + 10 || distanceY > targetSize + 10 || distanceX < -npcSize - 10 || distanceY < -npcSize - 10);
    }

    @Override
    public boolean process() {
        return pathfind();
    }

    protected boolean isWithinAttackDistance() {
        final boolean checkProjectile = target.checkProjectileClip(player, false);
        if (checkProjectile && ProjectileUtils.isProjectileClipped(player, target, player.getNextPosition(player.isRun() ? 2 : 1), target, false)) {
            return false;
        }
        int maxDistance = 10;
        final Location nextLocation = target.getNextPosition(target.isRun() ? 2 : 1);
        if ((player.isFrozen() || player.isStunned()) && (CollisionUtil.collides(player.getX(), player.getY(), player.getSize(), nextLocation.getX(), nextLocation.getY(), target.getSize()) || !withinRange(target, maxDistance, target.getSize()))) {
            return false;
        }
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int size = target.getSize();
        return distanceX <= size + maxDistance && distanceX >= -1 - maxDistance && distanceY <= size + maxDistance && distanceY >= -1 - maxDistance;
    }

    final boolean withinRange(final Position targetPosition, final int maximumDistance, final int targetSize) {
        final Location target = targetPosition.getPosition();
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int npcSize = player.getSize();
        if (distanceX == -npcSize - maximumDistance && distanceY == -npcSize - maximumDistance || distanceX == targetSize + maximumDistance && distanceY == targetSize + maximumDistance || distanceX == -npcSize - maximumDistance && distanceY == targetSize + maximumDistance || distanceX == targetSize + maximumDistance && distanceY == -npcSize - maximumDistance) {
            return false;
        }
        return !(distanceX > targetSize + maximumDistance || distanceY > targetSize + maximumDistance || distanceX < -npcSize - maximumDistance || distanceY < -npcSize - maximumDistance);
    }

    @Override
    public int processWithDelay() {
        if (!isWithinAttackDistance()) {
            return 0;
        }
        final Item weapon = player.getWeapon();
        if (weapon != null && weapon.getId() == ItemId.SNOWBALL) {
            weapon.setAmount(weapon.getAmount() - 1);
            if (weapon.getAmount() <= 0) {
                player.getEquipment().set(EquipmentSlot.WEAPON, null);
                player.sendFilteredMessage("You have run out of snowballs!");
            } else {
                player.getEquipment().refresh(EquipmentSlot.WEAPON.getSlot());
            }
        }

        AdventCalendarManager.increaseChallengeProgress(player, 2022, 23, 1);
        player.setInvalidAnimation(animation);
        World.sendProjectile(player, target, projectile);
        target.setGraphics(graphics);
        return -1;
    }

    @Override
    public boolean interruptedByCombat() {
        return false;
    }

    @Override
    public boolean interruptedByDialogue() {
        return false;
    }

    @Override
    public void stop() {
        player.resetWalkSteps();
    }
}
