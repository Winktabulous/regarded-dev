package com.zenyte.game.world.entity.npc;

import com.near_reality.game.world.entity.CombatCooldownKt;
import com.near_reality.game.world.entity.TargetSwitchCause;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.game.world.region.areatype.AreaType;
import com.zenyte.game.world.region.areatype.AreaTypes;
import com.zenyte.utils.TimeUnit;

public class NPCCombat {
    protected NPC npc;
    protected int combatDelay;
    protected Entity target;
    private boolean forceRetaliate;

    public NPCCombat(final NPC npc) {
        this.npc = npc;
    }

    public boolean process() {
        if (combatDelay > 0) {
            combatDelay--;
        }
        if (target == null) {
            return false;
        }
        CombatCooldownKt.clearAttackedByIfExpired(npc);
        if (!checkAll()) {
            removeTarget();
            return false;
        }
//        npc.setForceTalk("target = "+target);
        if (combatDelay <= 0 && !npc.isFacing(target)) {
            npc.setFaceEntity(target);
        }
        return true;
    }

    public void processAttack() {
        if (combatDelay <= 0) {
            combatDelay = combatAttack();
        }
    }

    public int combatAttack() {
        if (target == null) {
            return 0;
        }
        final boolean melee = isMelee();
        if (npc.isProjectileClipped(target, melee)) {
            return 0;
        }
        int distance = melee || npc.isForceFollowClose() ? 0 : npc.getAttackDistance();
        if (CollisionUtil.collides(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize())) {
            return 0;
        }
        if (outOfRange(target, distance, target.getSize(), melee)) {
            return 0;
        }
        addAttackedByDelay(target);
        return CombatScriptsHandler.specialAttack(npc, target);
    }

    public boolean isMelee() {
        return npc.getCombatDefinitions().isMelee();
    }

    public void removeTarget() {
        if (target == null) {
            return;
        }
        target = null;
        npc.setFaceEntity(null);
    }

    public void reset() {
        combatDelay = 0;
        removeTarget();
    }

    public boolean underCombat() {
        return target != null;
    }

    public void setTarget(final Entity target) {
        setTarget(target, TargetSwitchCause.OTHER);
    }

    public void setTarget(final Entity target, TargetSwitchCause cause) {
        this.target = target;
        if (cause == TargetSwitchCause.OTHER)
            npc.setFaceEntity(target);
        if (!checkAll()) {
            removeTarget();
            return;
        }
        if (target == null) {
            return;
        }
        npc.resetWalkSteps();
        npc.setAttacking(target);
        target.setAttackedBy(npc);
        final long currentTick = WorldThread.getCurrentCycle();
        npc.setAttackedAtTick(currentTick);
        target.setAttackedTick(currentTick);
        npc.setRandomWalkDelay(1);
        target.setFindTargetDelay(Utils.currentTimeMillis() + 5000);
    }

    public void forceTarget(final Entity target) {
        if (target instanceof Player) {
            //Make every other creature within viewport radius of the player unaggressive towards the player, as this monster needs to take aggression over.
            CharacterLoop.forEach(target.getLocation(), 15, NPC.class, n -> {
                NPCCombat combat = n.getCombat();
                Entity t = combat.getTarget();
                if (t == target) {
                    combat.setTarget(null);
                }
            });
        }

        npc.getCombat().reset();
        if (target instanceof NPC) {
            ((NPC) target).getCombat().reset();
        }

        npc.setAttacking(null);

        npc.setAttackedBy(null);
        target.setAttackedBy(null);

        npc.resetAttackedByDelay();
        target.resetAttackedByDelay();

        npc.setAttackingDelay(0);
        target.setAttackingDelay(0);

        setTarget(target);
    }

    protected boolean checkAll() {
        if (target == null || target.isFinished() || target.isNulled() || target.isDead() || npc.isDead() || npc.isFinished() || npc.isLocked() || npc.getPlane() != target.getPlane()) {
            return false;
        }
        if (npc.getRetreatMechanics().process(target)) {
            return false;
        }

        /*if (retreat()) {
            npc.forceWalkRespawnTile();
            return false;
        }*/
        if (!attackable(target, TargetSwitchCause.OTHER, false)) {
            removeTarget();
            return false;
        }
        if (npc.isMovementRestricted()) {
            return true;
        }
        if (colliding()) {
            final Direction direction = Utils.random(Direction.cardinalDirections);
            /* Npc will attempt one tile movement in a random cardinal direction when colliding, every tick. */
            npc.addWalkSteps(npc.getX() + direction.getOffsetX(), npc.getY() + direction.getOffsetY(), 1, true);
            return true;
        }
        return appendMovement();
    }

    private boolean retreat() {
        final Location location = npc.getLocation();
        final Location respawn = npc.getRespawnTile();
        final int dx = location.getX() - respawn.getX();
        final int dy = location.getY() - respawn.getY();
        final int size = npc.getSize();
        final int distance = npc.getMaxDistance();
        return dx > size + distance || dx < -1 - distance || dy > size + distance || dy < -1 - distance;
    }

    protected boolean attackable(Entity target) {
        return attackable(target, TargetSwitchCause.OTHER, false);
    }

    protected boolean attackable(Entity target, TargetSwitchCause cause) {
        return attackable(target, cause, false);
    }

    protected boolean attackable(Entity target, TargetSwitchCause cause, boolean debug) {

        if (target instanceof Player playerTarget && PlayerAttributesKt.getPvmArenaInRevivalState(playerTarget))
            return false;

        if (npc.isForceMultiArea() || target.isMultiArea())
            return true;

        if (npc.isForceAttackable())
            return true;

        final AreaType targetAreaType = target.getAreaType();
        final boolean targetInSingleWay = AreaTypes.SINGLE_WAY.equals(targetAreaType);
        final boolean targetInSinglePlus = AreaTypes.SINGLES_PLUS.equals(targetAreaType);
        final boolean targetInWilderness = WildernessArea.isWithinWilderness(target);

        final int targetAttackedByCooldownTicks = CombatCooldownKt.getAttackedByCooldownTicks(target);
        final boolean targetAttackedByCooldown = targetAttackedByCooldownTicks <= 0;

        final Entity targetAttackedBy = target.getAttackedBy();
        final boolean targetAttackedByAnyone = targetAttackedBy != null;
        final boolean targetAttackedByNpc = targetAttackedByAnyone && targetAttackedBy instanceof NPC;
        final boolean targetAttackedByPlayer = targetAttackedByAnyone && targetAttackedBy instanceof Player;
        final boolean targetAttackedByMe = targetAttackedByAnyone && targetAttackedBy == npc;
        final boolean targetAttackedByAnyoneButMe = targetAttackedByAnyone && targetAttackedBy != npc;

        final long targetAttackingCooldownTicks = CombatCooldownKt.getAttackCooldownTicks(target);
        final boolean targetAttackingCooldown = targetAttackingCooldownTicks <= 0;

        final Entity targetAttacking = target.getAttacking();
        final boolean targetAttackingAnyone = targetAttacking != null;
        final boolean targetAttackingAnyoneButMe = targetAttackingAnyone && targetAttacking != npc;
        final boolean targetAttackingMe = targetAttackingAnyone && targetAttacking == npc;

        final long attackedByCooldownTicks = CombatCooldownKt.getAttackCooldownTicks(npc);
        final boolean attackedByCooldown = attackedByCooldownTicks <= 0;

        final Entity attackedBy = npc.getAttackedBy();
        final boolean attackedByAnyone = attackedBy != null;
        final boolean attackedByTarget = attackedByAnyone && attackedBy == target;
        final boolean attackedByAnyoneButTarget = attackedByAnyone && attackedBy != target;
        final boolean attackedByPlayer =  attackedByAnyone && attackedBy instanceof Player;

        if (targetInSinglePlus) {
            if (attackedByAnyoneButTarget) {
                if (attackedByPlayer) {
                    if (!attackedByCooldown) {
                        if (debug)
                            npc.setForceTalk(Colour.MAROON.wrap("my cooldown B")+" is active (target = "+ target +", cooldown = "+attackedByCooldownTicks+").");
                        return false;
                    }
                }
            }
            if (targetAttackingAnyoneButMe) {
                if (targetAttacking instanceof Player) {
                    if (!targetAttackingCooldown) {
                        if (debug)
                            npc.setForceTalk(Colour.RS_PINK.wrap(target + "'s cooldown A")+" is active (cooldown = "+(targetAttackingCooldownTicks)+").");
                        return false;
                    }
                }
            }
            if (!targetAttackingMe && targetAttackedByAnyoneButMe) {
                if (!targetAttackedByCooldown) {
                    if (debug)
                        npc.setForceTalk(Colour.RS_PINK.wrap(target + "'s cooldown B")+" is active (cooldown = "+(targetAttackedByCooldownTicks)+").");
                    return false;
                }
            }
            if (targetAttackingMe || targetAttackedByMe) {
//                if (debug)
//                    npc.setForceTalk("Can attack "+ target +" because I am already attacking them..");
                return true;
            }
            if (targetAttackedByNpc || targetAttackedByPlayer) {
                if (targetAttackedByCooldown) {
                    if (debug)
                        npc.setForceTalk("Can PJ "+ target +" because attack cooled down");
                    return true;
                }
                if (debug)
                    npc.setForceTalk(target + " is already in combat (cooldown = "+targetAttackedByCooldownTicks+").");
                return false;
            }
        }
        if (targetInSingleWay) {

            /*
            Custom mechanic because players didn't like being attacked by aggressive monsters
            in single-way areas in the wilderness.
             */
            if (targetInWilderness && cause == TargetSwitchCause.AGGRESSION) {
                if (debug)
                    npc.setForceTalk("Can't attack "+ target +" because they are in a single-way area in the wilderness and I am aggressive.");
                return false;
            }

            if (attackedByAnyoneButTarget) {
                if (!attackedByCooldown) {
                    if (debug)
                        npc.setForceTalk("I am already in combat by "+attackedBy+". (cooldown = " + targetAttackedByCooldownTicks + ").");
                    return false;
                }
            }
            if (targetAttackedByAnyoneButMe) {
                if (!targetAttackedByCooldown) {
                    if (debug)
                        npc.setForceTalk(target + " is already in combat  by "+targetAttackedBy+" (cooldown_a = "+(targetAttackingCooldownTicks)+", cooldown_b = "+targetAttackedByCooldownTicks+").");
                    return false;
                }
                if (debug)
                    npc.setForceTalk("Can PJ "+ target +" because attack cooled down");
                return true;
            }
            if (targetAttackedByMe) {
//                if (debug)
//                    npc.setForceTalk("Can attack "+ target +" because I am already attacking them. (cooldown = "+(targetAttackingCooldownTicks)+").");
                return true;
            }
        }
        if (debug)
            npc.setForceTalk("Can attack "+ target +" because they are not attacked by anyone.");
        return true;
    }

    public boolean colliding() {
        return CollisionUtil.collides(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize());
    }

    protected boolean appendMovement() {
        final boolean melee = npc.getCombatDefinitions().isMelee();
        final int maxDistance = npc.isForceFollowClose() || melee ? 0 : npc.getAttackDistance();
        if (npc.isProjectileClipped(target, npc.isForceFollowClose() || melee) || outOfRange(target, maxDistance, target.getSize(), melee)) {
            npc.resetWalkSteps();
            npc.calcFollow(target, npc.isRun() ? 2 : 1, true, npc.isIntelligent(), npc.isEntityClipped());
        }
        return true;
    }

    public boolean outOfRange(final Position targetPosition, final int maximumDistance, final int targetSize, final boolean checkDiagonal) {
        final Location target = targetPosition.getPosition();
        final int distanceX = npc.getX() - target.getX();
        final int distanceY = npc.getY() - target.getY();
        final int npcSize = npc.getSize();
        if (checkDiagonal) {
            if (distanceX == -npcSize && distanceY == -npcSize || distanceX == targetSize && distanceY == targetSize || distanceX == -npcSize && distanceY == targetSize || distanceX == targetSize && distanceY == -npcSize) {
                return true;
            }
        }
        return distanceX > targetSize + maximumDistance || distanceY > targetSize + maximumDistance || distanceX < -npcSize - maximumDistance || distanceY < -npcSize - maximumDistance;
    }

    public void addAttackedByDelay(final Entity target) {
        target.setAttackedBy(npc);

        final long currentTick = WorldThread.getCurrentCycle();
        target.setAttackedTick(currentTick);

        final int pjTimer = CombatCooldownKt.getStealCooldownDurationInTicks(npc, target);
        final long delayTick = currentTick + pjTimer;
        target.setAttackedByDelay(delayTick);

        npc.setAttackingDelay(Utils.currentTimeMillis() + TimeUnit.TICKS.toMillis(pjTimer));
    }

    public NPC getNpc() {
        return npc;
    }

    public int getCombatDelay() {
        return combatDelay;
    }

    public void setCombatDelay(int combatDelay) {
        this.combatDelay = combatDelay;
    }

    public Entity getTarget() {
        return target;
    }

    public boolean isForceRetaliate() {
        return forceRetaliate;
    }

    public void setForceRetaliate(boolean forceRetaliate) {
        this.forceRetaliate = forceRetaliate;
    }


}
