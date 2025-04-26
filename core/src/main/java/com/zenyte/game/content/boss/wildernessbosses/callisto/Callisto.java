package com.zenyte.game.content.boss.wildernessbosses.callisto;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.LocationUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.MovementLock;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.PolygonRegionArea;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andys1814
 */
@SuppressWarnings("unused")
public class Callisto extends NPC implements CombatScript {

    private enum Phase {
        ONE,
        TWO,
        THREE
    }

    private static final Animation MELEE_ANIMATION = new Animation(10012);

    private static final Animation RANGE_ANIMATION = new Animation(10013);

    private static final Animation MAGIC_ANIMATION = new Animation(10014);

    private static final Animation HOWL_ANIMATION = new Animation(10016, 18);

    public static final Animation KNOCKBACK_ANIMATION = new Animation(1157);

    private static final Projectile RANGED_PROJECTILE = new Projectile(2350, 5, 25, 35, 15, 55, 64, 0);

    private static final Projectile MAGIC_PROJECTILE = new Projectile(133, 50, 25, 60, 15, 55, 64, 0);

    private static final Graphics STOMP_GRAPHICS = new Graphics(2349);

    private static final Graphics TRAP_SPAWN_GRAPHICS = new Graphics(2343);

    public static final Graphics KNOCKBACK_PLAYER_GRAPHICS = new Graphics(1255, 0, 92);

    private final Set<WorldObject> traps = new ObjectOpenHashSet<>();

    private final Map<Player, Integer> damageMap = new HashMap<>();

    private Phase phase = Phase.ONE;

    private final boolean artio;

    protected Class<? extends PolygonRegionArea> area;

    private boolean showHpHud = true;

    private boolean isPvmArenaVariant = false;

    public void setIsPvmArenaVariant(boolean bool) {
        this.isPvmArenaVariant = bool;
    }

    public Callisto(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.artio = id == NpcId.ARTIO;
        this.area = artio ? HuntersEnd.class : CallistosDen.class;
        // Manually set the death delay for this NPC because the new animation system broke animation duration calculation
        // TODO we can remove this if we properly find out how to calculate animation durations for new anim system
        deathDelay = 2;
    }

    @Override
    public void processNPC() {
        final Entity target = combat.getTarget();
        if (target != null && !isMovementRestricted()) {
            calcFollow(target, -1, true, true, true);
        }
        super.processNPC();
    }

    @Override
    public int attack(Entity target) {
        boolean melee = isWithinMeleeDistance(this, target);

        // Melee is primary attack so process it first
        if (melee) {
            setAnimation(MELEE_ANIMATION);
            delayHit(0, target, melee(target, 55));
            return combatDefinitions.getAttackSpeed();
        }

        boolean magic = Utils.roll(20d);

        setAnimation(magic ? MAGIC_ANIMATION : RANGE_ANIMATION);
        final List<Entity> targets = getPossibleTargets(EntityType.PLAYER);
        targets.forEach(aoeTarget -> {
            int delay = World.sendProjectile(this, aoeTarget, magic ? MAGIC_PROJECTILE : RANGED_PROJECTILE);
            if (magic) {
                delayHit(delay, aoeTarget, magic(aoeTarget, artio ? 40 : 60).onLand(hit -> {
                    if (!((Player) aoeTarget).getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
                        knockback(aoeTarget);
                    }
                }));
            } else {
                delayHit(delay, aoeTarget, ranged(aoeTarget, artio ? 40 : 55));
                setGraphics(STOMP_GRAPHICS);
            }
        });

        if (phase == Phase.TWO || phase == Phase.THREE) {
            Set<Location> trapCoords = getTrapCoords(4);
            addTrapsAtCoords(trapCoords);
        }

        return combatDefinitions.getAttackSpeed();
    }

    private void knockback(Entity target) {
        final Location middle = getMiddleLocation();
        double degrees = Math.toDegrees(Math.atan2(target.getY() - middle.getY(), target.getX() - middle.getX()));
        if (degrees < 0) {
            degrees += 360;
        }
        final double angle = Math.toRadians(degrees);
        final int px = (int) Math.round(middle.getX() + (getSize() + 4) * Math.cos(angle));
        final int py = (int) Math.round(middle.getY() + (getSize() + 4) * Math.sin(angle));
        final List<Location> tiles = LocationUtil.calculateLine(target.getX(), target.getY(), px, py, target.getPlane());
        if (!tiles.isEmpty()) tiles.remove(0);
        final Location destination = new Location(target.getLocation());
        for (final Location tile : tiles) {
            final int dir = DirectionUtil.getMoveDirection(tile.getX() - destination.getX(), tile.getY() - destination.getY());
            if (dir == -1) {
                continue;
            }
            if (!World.checkWalkStep(destination.getPlane(), destination.getX(), destination.getY(), dir, target.getSize(), false, false)) break;
            destination.setLocation(tile);
        }
        final int direction = DirectionUtil.getFaceDirection(target.getX() - destination.getX(), target.getY() - destination.getY());
        if (!destination.matches(target)) {
            target.setForceMovement(new ForceMovement(destination, 30, direction));
            target.lock();
        }
        target.faceEntity(this);
        final Location from = new Location(getLocation().getCoordFaceX(getSize()), getLocation().getCoordFaceY(getSize()), getPlane());
        World.sendGraphics(KNOCKBACK_PLAYER_GRAPHICS, target.getLocation());
        target.setAnimation(KNOCKBACK_ANIMATION);
        delayHit(this, 0, target, new Hit(this, 3, HitType.REGULAR));
        WorldTasksManager.schedule(() -> {
            target.setLocation(destination);
            target.unlock();
        });
    }

    private Set<Location> getTrapCoords(int numTraps) {
        Set<Location> coords = new ObjectOpenHashSet<>();
        while (coords.size() < numTraps) {
            final Location nextCoord = isPvmArenaVariant ? GlobalAreaManager.getArea(area).getRandomPosition() : null;
            final int x = nextCoord != null ? nextCoord.getX() : artio ? Utils.random(1751, 1767) : Utils.random(3351, 3367);
            final int y = nextCoord != null ? nextCoord.getY() : artio ? Utils.random(11533, 11552) : Utils.random(10317, 10336);
            final Location coord = new Location(x, y);
            if (coords.contains(coord) || traps.contains(coord) || !World.isFloorFree(0, x, y)) {
                continue;
            }
            coords.add(coord);
        }

        return coords;
    }

    private void addTrapsAtCoords(Set<Location> coords) {
        coords.forEach(coord -> {
            WorldObject trap = new WorldObject(47146, 10, 0, coord);
            World.spawnObject(trap);
            World.sendGraphics(TRAP_SPAWN_GRAPHICS, coord);
            traps.add(trap);

            WorldTasksManager.schedule(new TickTask() {

                @Override
                public void run() {
                    World.sendObjectAnimation(trap, new Animation(10_000));

                    if (isFinished()) {
                        activateTrap(trap);
                        stop();
                        return;
                    }

                    if (ticks == 45) {
                        World.removeObject(trap);
                        traps.remove(trap);
                        stop();
                        return;
                    }

                    final Set<Player> players = GlobalAreaManager.getArea(area).getPlayers();
                    for (Player player : players) {
                        if (player.getLocation().equals(coord)) {
                            activateTrap(trap);
                            player.addMovementLock(new MovementLock(System.currentTimeMillis() + 2400));
                            player.sendMessage(Colour.RED + "The bear trap immobilizes you." + Colour.END);
                            stop();
                            return;
                        }
                    }

                    ticks++;
                }

            }, 0, 0);
        });
    }

    private void activateTrap(WorldObject trap) {
        World.sendObjectAnimation(trap, new Animation(9999));
        WorldTasksManager.schedule(() -> {
            World.removeObject(trap);
            traps.remove(trap);
        });
    }

    private void howl() {
        setAnimation(HOWL_ANIMATION);
        setGraphics(new Graphics(artio ? 2353 : 2352));
        resetFreeze();
        addFreezeImmunity(4);
        getCombat().setCombatDelay(getCombatDefinitions().getAttackSpeed());
    }

    @Override
    public void applyHit(Hit hit) {
        if (hit.getHitType() == HitType.MAGIC) {
            // Callisto is immune to magic damage
            hit.setDamage(0);
        }
        super.applyHit(hit);
    }

    @Override
    public void handleIngoingHit(Hit hit) {
        super.handleIngoingHit(hit);
        appendPlayerDamage(hit);
    }

    private void appendPlayerDamage(Hit hit) {
        Entity source = hit.getSource();
        if (source == null) {
            return;
        }

        if (source instanceof Player player) {
            damageMap.compute(player, (k, v) -> Integer.sum(v == null ? 0 : v, hit.getDamage()));
        }
    }

    @Override
    public void postHitProcess(Hit hit) {
        // At certain increments Callisto will howl, reset his freeze and attack timers, and start laying traps.
        if (getHitpointsAsPercentage() < 66 && phase != Phase.TWO) {
            howl();
            phase = Phase.TWO;
        } else if (getHitpointsAsPercentage() < 33 && phase != Phase.ONE) {
            howl();
            phase = Phase.THREE;
        }

        List<Entity> targets = getPossibleTargets(EntityType.PLAYER);
        if (!isPvmArenaVariant && showHpHud) {
            for (Entity entity : targets) {
                if (entity instanceof final Player player) {
                    if (!player.getHpHud().isOpen()) {
                        player.getHpHud().open(id, getMaxHitpoints());
                    }
                    player.getHpHud().updateValue(getHitpoints());
                }
            }
        }
    }

    @Override
    public int getMaxHitpoints() {
        if(!isArtio())
            return 1000;
        return super.getMaxHitpoints();
    }

    @Override
    public NPC spawn() {
        setAnimation(HOWL_ANIMATION);
        if (!isPvmArenaVariant && showHpHud) {
            List<Entity> targets = getPossibleTargets(EntityType.PLAYER);
            for (Entity entity : targets) {
                if (entity instanceof final Player player) {
                    player.getHpHud().open(id, isArtio() ? getMaxHitpoints() : 1000);
                }
            }
        }
        NPC npc = super.spawn();
        if(!isArtio())
            setHitpoints(1000);
        return npc;

    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player player) {
            player.getAchievementDiaries().update(WildernessDiary.KILL_CALLISTO, 1);
        }
    }

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);
        List<Entity> targets = getPossibleTargets(EntityType.PLAYER);
        if (!isPvmArenaVariant && showHpHud) {
            for (Entity entity : targets) {
                if (entity instanceof final Player player) {
                    player.getHpHud().close();
                }
            }
        }
        if (source instanceof Player player) {
            player.getCombatAchievements().checkKcTask("callisto", 10, CAType.CALLISTO_ADEPT);
            player.getCombatAchievements().checkKcTask("callisto", 20, CAType.CALLISTO_VETERAN);
        }
        phase = Phase.ONE;
        traps.clear();
    }

    /**
     * Override default item drop logic so that we can give drops to each of the (up to) top 10 damagers.
     * <p>
     * However, the "rare" drops are only rolled once per kill. For example, only one player can get a rare per kill,
     * even if 10 players damaged Callisto.
     * <p>
     * This should be WAY easier but Zenyte drop system is putrid :)
     */
    @Override
    protected void drop(final Location tile) {
        List<Player> players = new ArrayList<>(damageMap.keySet());
        players.sort((a, b) -> damageMap.getOrDefault(b, 0) - damageMap.getOrDefault(a, 0));

        // Only drop items for 10 players at most
        for (int i = 0; i < 10; i++) {
            if (i >= players.size()) {
                break;
            }

            final Player player = players.get(i);
            if (player == null) {
                return;
            }

            if (i == 0) {
                // Only execute drop processors for the top killer
                final List<DropProcessor> processors = DropProcessorLoader.get(id);
                if (processors != null) {
                    for (final DropProcessor processor : processors) {
                        processor.onDeath(this, player);
                    }
                }
            }

            final NPCDrops.DropTable drops = NPCDrops.getTable(getId());
            if (drops == null) {
                return;
            }

            int finalI = i;
            NPCDrops.rollTable(player, drops, drop -> {
                Item item = new Item(drop.getItemId(), Utils.random(drop.getMinAmount(), drop.getMaxAmount()));
                // Only execute drop processors for the top killer
                if (finalI == 0) {
                    final List<DropProcessor> processors = DropProcessorLoader.get(id);
                    if (processors != null) {
                        final Item baseItem = item;
                        for (final DropProcessor processor : processors) {
                            if ((item = processor.drop(this, player, drop, item)) == null) {
                                return;
                            }
                            if (item != baseItem) break;
                        }
                    }
                }
                dropItem(player, item, location, drop.isAlways());
            });
        }
        damageMap.clear();
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.60;
    }

    @Override
    public double getMagicPrayerMultiplier() {
        return 0.25;
    }

    @Override
    public double getRangedPrayerMultiplier() {
        return 0.25;
    }

    @Override
    public float getXpModifier(final Hit hit) {
        return hit.getHitType() == HitType.MAGIC ? 0 : 1;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public int getRespawnDelay() {
        return artio ? 26 : 29;
    }

    public boolean isArtio() {
        return artio;
    }

    public void setShowHpHud(boolean showHpHud) {
        this.showHpHud = showHpHud;
    }
}
