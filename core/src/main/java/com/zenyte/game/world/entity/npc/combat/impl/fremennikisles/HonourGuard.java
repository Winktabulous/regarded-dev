package com.zenyte.game.world.entity.npc.combat.impl.fremennikisles;

import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 09/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HonourGuard extends NPC implements Spawnable {
    private static final IntArrayList list = new IntArrayList(new int[] {1889, 1890, 1891, 1892, 2981, 2982});

    public HonourGuard(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
        setTargetType(EntityType.NPC);
    }

    @Override
    public NPC spawn() {
        super.spawn();
        this.combatDefinitions.setAggressionType(AggressionType.ALWAYS_AGGRESSIVE);
        return this;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    public boolean isAttackable(final Entity e) {
        return e instanceof IceTroll;
    }

    @Override
    public boolean isAcceptableTarget(final Entity target) {
        return target instanceof IceTroll;
    }

    @Override
    public boolean validate(int id, String name) {
        return list.contains(id);
    }

    @Override
    public boolean checkAggressivity() {
        if (!forceAggressive) {
            if (!combatDefinitions.isAggressive()) {
                return false;
            }
        }
        getPossibleTargets(targetType);
        if (!possibleTargets.isEmpty()) {
            this.resetWalkSteps();
            final Entity target = possibleTargets.get(Utils.random(possibleTargets.size() - 1));
            setTarget(target);
        }
        return true;
    }

    @Override
    public boolean isPotentialTarget(final Entity entity) {
        final int entityX = entity.getX();
        final int entityY = entity.getY();
        final int entitySize = entity.getSize();
        final int x = getX();
        final int y = getY();
        final int size = getSize();
        final long currentTime = Utils.currentTimeMillis();
        final long currentTick = WorldThread.getCurrentCycle();
        return !entity.isMaximumTolerance() && (entity.isMultiArea() || entity.getAttackedBy() == this || (entity.getAttackedByDelay() <= currentTick && entity.getFindTargetDelay() <= currentTime)) && (!isProjectileClipped(entity, combatDefinitions.isMelee()) || CollisionUtil.collides(x, y, size, entityX, entityY, entitySize)) && (forceAggressive || combatDefinitions.isAlwaysAggressive() || combatDefinitions.isAggressive()) && isAcceptableTarget(entity);
    }
}
