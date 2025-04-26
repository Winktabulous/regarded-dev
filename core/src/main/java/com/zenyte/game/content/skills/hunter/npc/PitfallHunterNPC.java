package com.zenyte.game.content.skills.hunter.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.skills.hunter.HunterUtils;
import com.zenyte.game.content.skills.hunter.TrapState;
import com.zenyte.game.content.skills.hunter.node.TrapPrey;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.util.WorldUtil;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.pathfinding.RouteFinder;
import com.zenyte.game.world.entity.pathfinding.RouteResult;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCTileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.*;

import static com.zenyte.game.util.Direction.*;

/**
 * @author Kris | 30/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PitfallHunterNPC extends HunterNPC implements Spawnable {
    /**
     * The maximum distance from how far the {@link PitfallHunterNPC} will begin to follow the {@link Player} when they cross a trap.
     */
    private static final int MAX_FOLLOW_DISTANCE = 10;
    /**
     * The sound effect played when the {@link PitfallHunterNPC} falls in a trap.
     */
    private static final SoundEffect caughtSound = new SoundEffect(TrapType.PITFALL.getSuccessfulCatchSound(), 5);
    /**
     * The sound effect played when the {@link PitfallHunterNPC} dies inside the trap.
     */
    private static final SoundEffect growlSound = new SoundEffect(TrapType.PITFALL.getCreatureDeathSound(), 5);
    /**
     * The animation played by the {@link PitfallHunterNPC} when they jump over the pitfall trap.
     */
    private static final Animation jump = new Animation(5231, 20);
    /**
     * The animation played by the {@link PitfallHunterNPC} when they die in the pitfall trap.
     */
    private static final Animation die = new Animation(5234);
    /**
     * The original spawn location of the monster. Upon every respawn, we generate a new randomized nearby spawn location based off of this.
     */
    @NotNull
    private final ImmutableLocation originalTile;
    /**
     * The name of this creature in a shortened form.
     */
    @NotNull
    private final String shortenedName;
    /**
     * The player whom the {@link PitfallHunterNPC} is currently following.
     */
    @Nullable
    private WeakReference<Player> target;
    /**
     * The location of the pitfall trap which this {@link PitfallHunterNPC} last crossed. A {@link PitfallHunterNPC} will not cross the same pitfall back-to-back.
     */
    @Nullable
    private Location lastLeaptTile;

    public PitfallHunterNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.combat = new PitfallCombat(this);
        this.originalTile = new ImmutableLocation(tile == null ? new Location(0) : tile);
        shortenedName = prey == TrapPrey.SPINED_LARUPIA ? "larupia" : prey == TrapPrey.HORNED_GRAAHK ? "graahk" : "kyatt";
        this.combat = new NPCCombat(this) {
            @Override
            public void removeTarget() {
                final Entity target = this.target;
                super.removeTarget();
                if (target instanceof Player) {
                    HunterUtils.setTeasedNPC((Player) target, null);
                }
            }
            @Override
            public void setTarget(final Entity target, TargetSwitchCause cause) {
                final Entity t = this.target;
                if (t instanceof Player) {
                    HunterUtils.setTeasedNPC((Player) t, null);
                }
                super.setTarget(target, cause);
                if (target instanceof Player) {
                    HunterUtils.setTeasedNPC((Player) target, npc);
                }
            }
        };
    }

    @Override
    public NPC spawn() {
        //Pitfall npcs have a randomized respawn location.
        WorldUtil.findEmptySquare(originalTile.transform(Utils.random(10) - 7, Utils.random(10) - 7, 0), 5, getSize(), Optional.empty()).ifPresent(tile -> setRespawnTile(new ImmutableLocation(tile)));
        return super.spawn();
    }

    /**
     * The {@link PitfallHunterNPC} do not chase after traps inactively like several other hunter creatures, instead, they must be teased and dragged into it.
     * <p>Therefore there is no need to process nearby traps every step the creature takes.</p>
     */
    @Override
    protected void checkTraps() {
        //No-op
    }

    @Override
    public boolean triggersAutoRetaliate() {
        return false;
    }

    @Override
    public void processHit(final Hit hit) {
        //Damage should not be applied to pitfall creatures.
    }

    /**
     * The sequence played by the {@link PitfallHunterNPC} when it gets caught in a pitfall trap and dies.
     *
     * @param player the player who the {@link PitfallHunterNPC} was following at the time.
     * @param trap   the game object which represents the hunter trap.
     */
    private void fallIn(@NotNull final Player player, @NotNull final WorldObject trap) {
        final int rot = trap.getRotation();
        final Direction dir = (rot & 1) == 0 ? (getY() > trap.getY() ? SOUTH : NORTH) : (getX() > trap.getX() ? WEST : EAST);
        setFaceEntity(null);
        temporaryAttributes.put("ignoreWalkingRestrictions", true);
        resetWalkSteps();
        addWalkSteps(trap.getX(), trap.getY(), 3, false);
        HunterUtils.setTeasedNPC(player, null);
        HunterUtils.findTrap(TrapType.PITFALL, trap, npc -> HunterUtils.getTrapOwnershipPredicate().test(player, npc)).ifPresent(hunterTrap -> hunterTrap.setState(TrapState.PROCESSING));
        WorldTasksManager.schedule(() -> {
            World.sendSoundEffect(this, caughtSound);
            setAnimation(die);
        }, 2);
        WorldTasksManager.schedule(() -> {
            World.sendSoundEffect(this, growlSound);
            setRespawnTask();
            unlock();
            setCantInteract(false);
            lastLeaptTile = null;
            temporaryAttributes.remove("ignoreWalkingRestrictions");
            final int nwVar = rot == 1 || rot == 2 ? 3 : 4;
            final int seVar = rot == 1 || rot == 2 ? 4 : 3;
            player.getVarManager().sendBit(trap.getDefinitions().getVarbit(), dir == NORTH || dir == WEST ? nwVar : seVar);
            HunterUtils.findTrap(TrapType.PITFALL, trap, npc -> HunterUtils.getTrapOwnershipPredicate().test(player, npc)).ifPresent(hunterTrap -> {
                hunterTrap.setPrey(TrapPrey.get(getId()).orElseThrow(RuntimeException::new));
                hunterTrap.setState(TrapState.ACTIVE);
                hunterTrap.resetTimer();
            });
        }, 4);
    }

    /**
     * The sequence played by the {@link PitfallHunterNPC} when it jumps over the pitfall trap.
     *
     * @param player the player who the {@link PitfallHunterNPC} was following at the time.
     * @param trap   the game object which represents the hunter trap.
     */
    private void jumpOver(@NotNull final Player player, @NotNull final WorldObject trap) {
        final int rot = trap.getRotation();
        final int px = getX();
        final int py = getY();
        final int z = getPlane();
        final Direction dir = (rot & 1) == 0 ? (py > trap.getY() ? SOUTH : NORTH) : (px > trap.getX() ? WEST : EAST);
        final Location destination = new Location(dir == WEST ? (px - 4) : dir == EAST ? (px + 4) : px, dir == SOUTH ? (py - 4) : dir == NORTH ? (py + 4) : py, player.getPlane());
        final PitfallHunterNPC.FlyingCreatureProjectile type = Objects.requireNonNull(CollectionUtils.findMatching(FlyingCreatureProjectile.values, value -> value.prey == prey));
        final Projectile firstProj = dir == EAST || dir == NORTH ? type.right : type.left;
        final Projectile secondProj = dir == EAST || dir == NORTH ? type.left : type.right;
        final Location mainProjectileStart = new Location(getLocation());
        final Location mainProjectileEnd = new Location(destination);
        final Location secondProjectileStart = new Location(mainProjectileStart);
        final Location secondProjectileEnd = new Location(mainProjectileEnd);
        final int firstYOffset = dir == EAST || dir == WEST ? 1 : 0;
        final int secondYOffset = dir == NORTH || dir == SOUTH ? 1 : 0;
        setFaceEntity(null);
        setAnimation(jump);
        setFaceLocation(new Location(px + (dir == EAST ? 200 : dir == WEST ? -200 : 0), py + (dir == NORTH ? 200 : dir == SOUTH ? -200 : 0), z));
        mainProjectileStart.moveLocation(0, firstYOffset, 0);
        mainProjectileEnd.moveLocation(0, firstYOffset, 0);
        World.sendProjectile(mainProjectileStart, mainProjectileEnd, firstProj);
        secondProjectileStart.moveLocation(1, secondYOffset, 0);
        secondProjectileEnd.moveLocation(1, secondYOffset, 0);
        World.sendProjectile(secondProjectileStart, secondProjectileEnd, secondProj);
        WorldTasksManager.schedule(() -> setLocation(destination), 1);
        WorldTasksManager.schedule(() -> resume(player), 3);
    }

    /**
     * The {@link} resuming to attack the player.
     *
     * @param player the player whom to attack.
     */
    private void resume(@NotNull final Player player) {
        unlock();
        setCantInteract(false);
        combat.setTarget(player);
    }

    /**
     * Calculates the destination to where the {@link PitfallHunterNPC} jumps, based on the location of the trap and the player(coordinates are relative to the player as the player can jump from
     * two possible sides of the trap.
     *
     * @param player    the player whom to use in relation to calculation the destination.
     * @param leaptTrap the game object representing the trap to leap over.
     * @return an optional location if available. Creature will not jump anywhere if no value is present.
     */
    @NotNull
    private Optional<Location> calculateDestination(@NotNull final Player player, @NotNull final WorldObject leaptTrap) {
        final int rot = leaptTrap.getRotation();
        final int tx = leaptTrap.getX();
        final int ty = leaptTrap.getY();
        final int nx = getX();
        final int ny = getY();
        final int z = getPlane();
        if ((rot & 1) == 1) {
            if (nx + 1 < tx) {
                if (player.getX() < tx) return Optional.of(new Location(tx - 2, ty, z));
            } else if (nx > tx + 1) {
                return Optional.of(new Location(tx + 2, ty, z));
            }
        } else {
            if (ny - 1 < ty) {
                return Optional.of(new Location(tx, ty - 2, z));
            } else if (ny + 1 > ty) {
                if (player.getY() > ty) return Optional.of(new Location(tx, ty + 2, z));
            }
        }
        return Optional.empty();
    }

    /**
     * Performs the follow sequence on the {@link PitfallHunterNPC} after the {@link Player} jumps over this trap, if possible.
     *
     * @param player    the player whom to follow.
     * @param leaptTrap the trap which to leap over.
     */
    public void follow(@NotNull final Player player, @NotNull final WorldObject leaptTrap) {
        final Optional<Location> optionalDest = calculateDestination(player, leaptTrap);
        if (!optionalDest.isPresent()) {
            return;
        }
        final Location dest = optionalDest.get();
        if (getLocation().getDistance(dest) > MAX_FOLLOW_DISTANCE) {
            return;
        }
        if (target != null && player == target.get() && leaptTrap.matches(lastLeaptTile)) {
            WorldTasksManager.schedule(() -> player.sendMessage("The " + shortenedName + " won't jump the same pit " +
                    "twice in a row."), 2);
            return;
        }
        combat.reset();
        setFaceEntity(player);
        setRouteEvent(new PitfallTileEvent(this, new TileStrategy(dest), () -> {
            target = new WeakReference<>(player);
            lastLeaptTile = leaptTrap;
            setCantInteract(true);
            lock();
            WorldTasksManager.schedule(() -> {
                final HunterTrap trap = HunterUtils.findTrap(TrapType.PITFALL, leaptTrap, npc -> HunterUtils.getTrapOwnershipPredicate().test(player, npc)).orElseThrow(RuntimeException::new);
                if (success(trap, prey)) {
                    fallIn(player, leaptTrap);
                } else {
                    jumpOver(player, leaptTrap);
                }
            });
        }).setOnFailure(() -> combat.setTarget(player)));
    }

    /**
     * Prevent the creature from facing the player when being teased.
     *
     * @param entity the entity whom to face.
     */
    @Override
    public void setInteractingWith(final Entity entity) {
    }

    @Override
    public boolean validate(int id, String name) {
        return TrapPrey.contains(id, TrapType.PITFALL);
    }

    public int getRespawnDelay() {
        return 10;
    }

    @Override
    void processTrap() {
    }

    @Override
    public void interact() {
    }


    /**
     * When the {@link PitfallHunterNPC} jumps over a trap, two projectiles are sent out from the starting position to the end position.
     * These projectiles create a functional replica of the {@link PitfallHunterNPC}'s models when sent at the correct positions.
     * <p>{@link NPC}s cannot use exactmove in OSRS as of yet, therefore the work-around is to simulate the process using projectiles.</p>
     */
    private enum FlyingCreatureProjectile {
        LARUPIA(TrapPrey.SPINED_LARUPIA, 939, 940), KYATT(TrapPrey.SABRE_TOOTHED_KYATT, 937, 938), GRAAHK(TrapPrey.HORNED_GRAAHK, 941, 942);
        private static final List<FlyingCreatureProjectile> values = Collections.unmodifiableList(Arrays.asList(values()));
        private final TrapPrey prey;
        private final Projectile left;
        private final Projectile right;

        FlyingCreatureProjectile(@NotNull final TrapPrey prey, final int left, final int right) {
            this.prey = prey;
            this.left = new Projectile(left, 0, 0, 53, 0, 16, 0, 5);
            this.right = new Projectile(right, 0, 0, 53, 0, 16, 0, 5);
        }
    }


    /**
     * The event which the {@link PitfallHunterNPC} executes when being requested to move into a trap.
     */
    private static final class PitfallTileEvent extends NPCTileEvent {
        PitfallTileEvent(final NPC entity, final TileStrategy strategy, final Runnable event) {
            super(entity, strategy, event, 0);
        }

        /**
         * The process method called every tick, used to execute the event.
         *
         * @return whether to continue processing this event, or to stop it from executing any further.
         */
        public boolean process() {
            if (entity.isTeleported()) {
                return STOP;
            }
            if (!initiated) {
                return initiate();
            }
            if (!entity.hasWalkSteps()) {
                final RouteResult steps = RouteFinder.findRoute(entity, 1, strategy, true);
                if (steps == RouteResult.ILLEGAL) {
                    cancel();
                    return STOP;
                }
                if (!steps.isAlternative() && steps.getSteps() <= 0) {
                    if (delay > 0) {
                        delay--;
                        return CONTINUE;
                    }
                    resetFlag();
                    execute();
                    return STOP;
                }
                cancel();
                return STOP;
            }
            return CONTINUE;
        }

        /**
         * The initiation method for this event, attempts to move the {@link #entity} to the destination.
         *
         * @return whether to continue executing this event, or to finish it.
         */
        protected boolean initiate() {
            initiated = true;
            final RouteResult steps = RouteFinder.findRoute(entity, 1, strategy, true);
            if (steps == RouteResult.ILLEGAL) {
                cancel();
                return STOP;
            }
            if (!steps.isAlternative() && steps.getSteps() == 0) {
                resetFlag();
                execute();
                return STOP;
            }
            final int[] bufferX = steps.getXBuffer();
            final int[] bufferY = steps.getYBuffer();
            entity.resetWalkSteps();
            if (entity.isFrozen() || entity.isStunned()) {
                return CONTINUE;
            }
            for (int step = steps.getSteps() - 1; step >= 0; step--) {
                if (!entity.addWalkSteps(bufferX[step], bufferY[step], 25, true)) {
                    cancel();
                    return STOP;
                }
            }
            return CONTINUE;
        }
    }


    /**
     * A special combat class that avoids some of the restrictions set by the regular {@link NPCCombat}.
     */
    private static class PitfallCombat extends NPCCombat {
        public PitfallCombat(final NPC npc) {
            super(npc);
        }

        @Override
        public void setTarget(final Entity target, TargetSwitchCause cause) {
            this.target = target;
            npc.setFaceEntity(target);
            if (target != null) {
                target.setAttackedBy(npc);
            }
            npc.resetWalkSteps();
            if (target != null) {
                target.setFindTargetDelay(Utils.currentTimeMillis() + 5000);
            }
        }
    }
}
