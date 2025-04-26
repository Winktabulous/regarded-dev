package com.zenyte.game.content.kebos.alchemicalhydra.npc.combat.phases;

import com.zenyte.game.content.kebos.alchemicalhydra.instance.AlchemicalHydraInstance;
import com.zenyte.game.content.kebos.alchemicalhydra.model.FireWallBlock;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.AlchemicalHydra;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.combat.HydraPhaseSequence;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entities;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.MovementLock;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

import static com.zenyte.game.content.kebos.alchemicalhydra.instance.AlchemicalHydraInstance.hydraCenterLocation;

/**
 * @author Tommeh | 02/11/2019 | 20:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class FlamePhase implements HydraPhaseSequence {
    private static final Animation flameAttackAnim = new Animation(8248);
    private static final Animation rangedAttackAnim = new Animation(8249);
    private static final Animation magicAttackAnim = new Animation(8250);
    private static final Animation flameCollisionAnim = new Animation(1114);
    private static final Graphics flameGfx = new Graphics(1668, 0, 0);
    private static final Graphics delayedFlameGfx = new Graphics(1668, 30, 0);
    private static final Graphics halfDelayedFlameGfx = new Graphics(1668, 15, 0);
    private static final Projectile flameAttackProj = new Projectile(1667, 42, 0, 37, 2, 15, 0, 5);
    private static final ForceTalk yowch = new ForceTalk("Yowch!");
    private int attacks;
    private boolean initialSpecial;
    private boolean performingSpecialAttack;
    private boolean walkingToMiddle;

    public boolean isPerformingSpecialAttack() {
        return performingSpecialAttack;
    }

    private final List<FireGraphics> fireGraphics = new ObjectArrayList<>();
    private final List<Location> fire = new ObjectArrayList<>();

    private Location getNextLocation(@NotNull final Location player) {
        final int px = player.getX();
        final int py = player.getY();
        int radius = 0;
        final Location tile = new Location(player);
        while (++radius <= 15) {
            for (int y = py - radius; y < py + radius; y++) {
                tile.setLocation(px - radius, y, tile.getPlane());
                if (isFree(player, tile)) {
                    return tile;
                }
            }
            for (int x = px - radius; x < px + radius; x++) {
                tile.setLocation(x, py + radius, tile.getPlane());
                if (isFree(player, tile)) {
                    return tile;
                }
            }
            for (int y = py + radius; y >= py - radius; y--) {
                tile.setLocation(px + radius, y, tile.getPlane());
                if (isFree(player, tile)) {
                    return tile;
                }
            }
            for (int x = px + radius; x >= px - radius; x--) {
                tile.setLocation(x, py - radius, tile.getPlane());
                if (isFree(player, tile)) {
                    return tile;
                }
            }
        }
        return player;
    }

    private boolean isFree(@NotNull final Location player, @NotNull final Location tile) {
        return !fire.contains(tile) && World.isFloorFree(tile, 1) && !ProjectileUtils.isProjectileClipped(null, null, player, tile, true);
    }

    private Location getNextFollowLocation(final Location flame, final Location player) {
        if (flame.matches(player)) {
            return player;
        }
        final int direction = DirectionUtil.getFaceDirection(player.getX() - flame.getX(), player.getY() - flame.getY());
        final int entityDirection = Entities.getRoundedDirection(direction, 0);
        final Direction directionConstant = CollectionUtils.findMatching(Direction.values, dir -> dir.getNPCDirection() == entityDirection);
        assert directionConstant != null;
        return flame.transform(directionConstant, 1);
    }

    private Location getOutsideInnerCenterLocation(final AlchemicalHydraInstance instance, final FireWallBlock block) {
        final java.util.List<Location> border = instance.getCenterBorder();
        Location location = null;
        double previousDistance = 50;
        for (final Location point : border) {
            final double distance = point.getDistance(instance.getLocation(block.getMovingFireLocation()));
            if (distance < previousDistance) {
                location = point;
                previousDistance = distance;
            }
        }
        return location;
    }

    @Override
    public int autoAttack(final AlchemicalHydra hydra, final Player player) {
        final AttackType style = hydra.getCombatDefinitions().getAttackStyle();
        final int maxHit = (int) (hydra.getMaxHit() * hydra.getAttackModifier());
        if (style.equals(AttackType.MAGIC)) {
            hydra.setAnimation(magicAttackAnim);
            hydra.delayHit(World.sendProjectile(hydra, player, magicAttackProj), player, new Hit(hydra, hydra.getRandomMaxHit(hydra, maxHit, AttackType.MAGIC, player), HitType.MAGIC));
        } else {
            hydra.setAnimation(rangedAttackAnim);
            hydra.delayHit(World.sendProjectile(hydra, player, rangedAttackProj), player, new Hit(hydra, hydra.getRandomMaxHit(hydra, maxHit, AttackType.RANGED, player), HitType.RANGED));
        }
        attacks++;
        hydra.incrementAutoAttacks();
        return hydra.getCombatDefinitions().getAttackSpeed();
    }

    private final List<WorldObject> spawnBlockingObjects(@NotNull final Rectangle outer, final int plane) {
        final int minX = (int) outer.getMinX();
        final int maxX = (int) outer.getMaxX();
        final int minY = (int) outer.getMinY();
        final int maxY = (int) outer.getMaxY();
        final ObjectArrayList<WorldObject> list = new ObjectArrayList<WorldObject>();
        for (int x = minX + 1; x < maxX; x++) {
            for (int y = minY + 1; y < maxY; y++) {
                final WorldObject object = new WorldObject(26209, 10, 0, x, y, plane);
                World.spawnObject(object);
                list.add(object);
            }
        }
        return list;
    }

    private void shovePlayer(@NotNull Player player, @NotNull final Location destination) {
        player.lock(3);
        player.setAnimation(flameCollisionAnim);
        player.autoForceMovement(destination, 0, 30);
        player.sendMessage("The Alchemical Hydra forces you out from beneath itself.");
    }

    private void stun(@NotNull final Player player) {
        player.stopAll();
        player.addMovementLock(new MovementLock(System.currentTimeMillis() + 4200));
        player.sendMessage("The Alchemical Hydra temporarily stuns you.");
    }

    private void animateBlock(@NotNull final AlchemicalHydra hydra, @NotNull final FireWallBlock block) {
        hydra.faceDirection(block.getDirection());
        hydra.setAnimation(flameAttackAnim);
    }

    private void constructBlock(@NotNull final AlchemicalHydra hydra, @NotNull final FireWallBlock block) {
        hydra.faceDirection(block.getDirection());
        for (final Location tile : block.getTiles()) {
            if (!World.isFloorFree(tile, 1)) {
                continue;
            }
            appendFire(new FireGraphics(42, new Graphics(1668, tile.getTileDistance(hydra.getMiddleLocation()) * 5, 0), tile));
        }
    }

    private boolean finishPerformingSpecial(@NotNull final AlchemicalHydra hydra, @NotNull final Player player) {
        hydra.unlock();
        hydra.setCantInteract(false);
        hydra.getReceivedHits().clear();
        hydra.setAttackDistance(16);
        hydra.getCombat().setTarget(player);
        return false;
    }

    private void executeFireTrail(@NotNull final AlchemicalHydra hydra, @NotNull final FireWallBlock block, @NotNull final Player player, @NotNull final List<WorldObject> blockingObjects) {
        final AlchemicalHydraInstance instance = hydra.getInstance();
        final Location startLocation = instance.getLocation(block.getMovingFireLocation());
        hydra.setAnimation(flameAttackAnim);
        hydra.setFaceLocation(startLocation);
        World.sendProjectile(hydra, startLocation, flameAttackProj);
        WorldTasksManager.schedule(new TickTask() {
            private Location fireTile;
            private final Runnable onDistanceRunnable = () -> {
                Location nextLocation = getNextFollowLocation(fireTile, new Location(player.getLocation()));
                if (!fire.contains(nextLocation)) {
                    appendFire(new FireGraphics(42, halfDelayedFlameGfx, new Location(fireTile = nextLocation)));
                    nextLocation = getNextFollowLocation(fireTile, new Location(player.getLocation()));
                    if (!fire.contains(nextLocation)) {
                        appendFire(new FireGraphics(43, delayedFlameGfx, new Location(fireTile = nextLocation)));
                    }
                }
            };
            @Override
            public void run() {
                if (ticks == 0) {
                    appendFire(new FireGraphics(42, flameGfx, fireTile = new Location(startLocation)));
                    performingSpecialAttack = finishPerformingSpecial(hydra, player);
                    if (fireTile.getTileDistance(player.getLocation()) > 2) {
                        onDistanceRunnable.run();
                    }
                } else if (ticks >= 1 && ticks <= 16) {
                    if (fireTile.getTileDistance(player.getLocation()) <= 2) {
                        if ((ticks & 1) == 1) {
                            final Location nextLocation = getNextFollowLocation(fireTile, new Location(player.getLocation()));
                            if (!fire.contains(nextLocation)) {
                                appendFire(new FireGraphics(43, delayedFlameGfx, new Location(fireTile = nextLocation)));
                            }
                        }
                    } else {
                        onDistanceRunnable.run();
                    }
                } else if (ticks == 17) {
                    blockingObjects.forEach(World::removeObject);
                } else if (ticks == 35) {
                    hydra.setAttackDistance(3);
                    stop();
                }
                ticks++;
            }
        }, flameAttackProj.getTime(hydra, startLocation), 0);
    }

    private void appendFire(@NotNull final FireGraphics fire) {
        fireGraphics.add(fire);
    }

    @Override
    public boolean attack(final AlchemicalHydra hydra, final Player player) {
        if (attacks == 3 && !initialSpecial || attacks == 21) {
            final AlchemicalHydraInstance instance = hydra.getInstance();
            hydra.getCombat().setTarget(null);
            hydra.setInteractingWith(null);
            hydra.resetWalkSteps();
            hydra.addWalkSteps(instance.getX(hydraCenterLocation.getX()), instance.getY(hydraCenterLocation.getY()), -1, false);
            final Rectangle outer = instance.getOuterCenter();
            final Rectangle inner = instance.getInnerCenter();
            fire.clear();
            performingSpecialAttack = true;
            walkingToMiddle = true;
            WorldTasksManager.schedule(new TickTask() {
                FireWallBlock block;
                private List<FireWallBlock> list;
                private boolean cleanedCenter;
                private List<WorldObject> blockingObjects;
                @Override
                public void run() {
                    if (!cleanedCenter) {
                        if (hydra.getWalkSteps().size() <= 2) {
                            cleanedCenter = true;
                            blockingObjects = spawnBlockingObjects(outer, hydra.getPlane());
                            if (inner.contains(player.getX(), player.getY())) {
                                shovePlayer(player, getOutsideInnerCenterLocation(instance, (list = instance.buildWallTiles()).get(0)));
                            }
                            return;
                        }
                    }
                    if (!hydra.hasWalkSteps()) {
                        if (walkingToMiddle) {
                            if (list == null) {
                                list = instance.buildWallTiles();
                            }
                            walkingToMiddle = false;
                            WorldTasksManager.schedule(() -> stun(player));
                        }
                        switch (ticks++) {
                        case 1: 
                            hydra.lock();
                            hydra.setCantInteract(true);
                        case 4: 
                            animateBlock(hydra, block = list.get(ticks == 2 ? 0 : 1));
                            break;
                        case 2: 
                        case 5: 
                            constructBlock(hydra, block);
                            break;
                        case 9: 
                            executeFireTrail(hydra, block, player, blockingObjects);
                            stop();
                            break;
                        }
                    }
                }
            }, 0, 0);
            if (attacks == 3) {
                initialSpecial = true;
                attacks = 0;//Also reset the counter so it uses the lightning special every 9 attacks.
            } else if (attacks == 21) {
                attacks = 0;
            }
            return true;
        }
        autoAttack(hydra, player);
        return false;
    }

    @Override
    public void process(final AlchemicalHydra hydra, final Player player) {
        for (final FlamePhase.FireGraphics graphics : fireGraphics) {
            graphics.process();
        }
        if (player.getNumericTemporaryAttribute("flame_burn_delay").longValue() > Utils.currentTimeMillis()) {
            return;
        }
        if (player.isDead() || player.isFinished()) {
            return;
        }
        final Location nextLocation = player.getNextLocation();
        if (nextLocation != null) {
            if (!hydra.getInstance().inside(nextLocation)) {
                return;
            }
        }
        for (final Location tile : fire) {
            if (!player.getLocation().matches(tile)) {
                continue;
            }
            final Location currentTile = new Location(player.getLocation());
            final Location destination = getNextLocation(currentTile);
            CombatUtilities.delayHit(hydra, -1, player, new Hit(Utils.random(20), HitType.REGULAR));
            hydra.setPlayerHitByFlameWallOnce();
            player.sendMessage("The fire scorches you leaving a lingering burn...");
            player.setForceTalk(yowch);
            //Movement lock means it'll be without stun message; Set the delay to 300ms to account for tick lag.
            player.addMovementLock(new MovementLock(System.currentTimeMillis() + 300));
            player.setAnimation(flameCollisionAnim);
            player.addTemporaryAttribute("flame_burn_delay", Utils.currentTimeMillis() + 1200);
            player.autoForceMovement(destination, 0, 30);
            player.getVariables().schedule(25, TickVariable.HYDRA_BLEED);
        }
    }

    public boolean isWalkingToMiddle() {
        return walkingToMiddle;
    }


    private class FireGraphics {
        private int ticks;
        private final Graphics graphics;
        private final Location tile;

        private void process() {
            switch (ticks--) {
            case 0: 
                fire.remove(tile);
                break;
            case 43: 
                World.sendGraphics(delayedFlameGfx, tile);
                break;
            case 42: 
                if (graphics != delayedFlameGfx) {
                    World.sendGraphics(graphics, tile);
                }
                fire.add(tile);
                break;
            }
        }

        public FireGraphics(int ticks, Graphics graphics, Location tile) {
            this.ticks = ticks;
            this.graphics = graphics;
            this.tile = tile;
        }
    }
}
