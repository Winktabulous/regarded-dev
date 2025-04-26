package com.zenyte.game.content.skills.hunter.object;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.skills.hunter.TrapState;
import com.zenyte.game.content.skills.hunter.node.PreyObject;
import com.zenyte.game.content.skills.hunter.node.TrapPrey;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.npc.HunterDummyNPC;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;
import mgi.utilities.CollectionUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * @author Kris | 25/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class HunterTrap {
    /**
     * A weak reference to the {@link Player} who owns and built this {@link HunterTrap}.
     */
    @NotNull
    private final WeakReference<Player> player;
    /**
     * A weak reference to the {@link HunterDummyNPC} who owns and built this {@link HunterTrap}.
     */
    @NotNull
    private final WeakReference<HunterDummyNPC> npc;
    /**
     * A list of the {@link WorldObject} objects which declare the original objects of this trap.
     */
    @NotNull
    private final List<WorldObject> defaultObjects;
    /**
     * A list of the {@link WorldObject} objects which declare the built objects of this trap.
     */
    @NotNull
    private final List<WorldObject> builtObjects;
    /**
     * A list of the {@link WorldObject} objects which declare the collapsed objects of this trap.
     */
    @NotNull
    private final List<WorldObject> collapsedObjects;
    /**
     * The specific location upon which the trap is placed.
     */
    @NotNull
    private final ImmutableLocation location;
    /**
     * The type of this hunter trap.
     */
    @NotNull
    private final TrapType type;
    /**
     * A mutable integer representing how many game ticks this trap has been active for.
     */
    @NotNull
    private final MutableInt ticks;
    /**
     * A list of tick runnables which effectively define what happens to the trap at different game ticks.
     */
    @NotNull
    private final List<TickRunnable> tickRunnables;
    /**
     * The hashcode of this specific hunter trap, based on the fully-immutable fields of the hunter trap.
     */
    private final int hashCode;
    /**
     * The current state of this hunter trap.
     */
    @NotNull
    private TrapState state;
    /**
     * The prey which got caught in this trap.
     */
    @Nullable
    private TrapPrey prey;
    @Nullable
    private Runnable removalRunnable;

    /**
     * Constructs a new hunter trap object and sets its state to active. Uses weak references to link the player, object and npc.
     *
     * @param player         the player who owns this trap.
     * @param npc            the nullable dummy npc, in the case of traps where the prey faces at the trap while walking towards it.
     * @param location       the location the trap is being set up on.
     * @param defaultObjects the objects that represents this trap before it's built.
     * @param builtObjects   the objects that represents this trap after it's built.
     */
    public HunterTrap(@NotNull final Player player, @Nullable final HunterDummyNPC npc, @NotNull final Location location, @NotNull final List<WorldObject> defaultObjects, @NotNull final List<WorldObject> builtObjects, @NotNull final List<WorldObject> collapsedObjects, @NotNull final TrapType type) {
        this.player = new WeakReference<>(player);
        this.npc = new WeakReference<>(npc);
        this.location = new ImmutableLocation(location);
        this.defaultObjects = Collections.unmodifiableList(defaultObjects);
        this.builtObjects = Collections.unmodifiableList(builtObjects);
        this.collapsedObjects = Collections.unmodifiableList(collapsedObjects);
        this.tickRunnables = new LinkedList<>();
        this.type = type;
        this.state = TrapState.INACTIVE;
        this.ticks = new MutableInt();
        this.hashCode = Objects.hash(defaultObjects, builtObjects, type, location);
    }

    public boolean isFreshTrap() {
        return ticks.intValue() <= 15;
    }

    /**
     * Sets the state to a non-null value.
     *
     * @param state the state to set to.
     */
    public void setState(@NotNull final TrapState state) {
        this.state = Objects.requireNonNull(state);
    }

    public void resetTimer() {
        ticks.setValue(0);
    }

    /**
     * Sets this {@link HunterTrap} trap up by removing the original objects and spawning the built objects in their place, and setting the state of this trap to {@link TrapState#ACTIVE}.
     * Additionally, spawns the invisible dummy NPC underneath it.
     */
    public void setup(@NotNull final Optional<Runnable> onSetup) {
        Preconditions.checkArgument(state == TrapState.INACTIVE, "The trap is not inactive and therefore cannot be built.");
        defaultObjects.forEach(World::removeObject);
        builtObjects.forEach(World::spawnObject);
        state = TrapState.ACTIVE;
        final HunterDummyNPC dummy = npc.get();
        if (dummy != null) {
            dummy.spawn();
        }
        final Player player = this.player.get();
        if (player != null) {
            player.getHunter().appendTrap(this);
        }
        onSetup.ifPresent(Runnable::run);
    }

    public void collapse() {
        collapse(Optional.empty());
    }

    public void failNetTrap() {
        Preconditions.checkArgument(state != TrapState.INACTIVE, "Trap has not yet been set up.");
        Preconditions.checkArgument(state != TrapState.COLLAPSED, "Trap has is already collapsed.");
        Preconditions.checkArgument(state != TrapState.REMOVED, "Trap is already removed.");
        builtObjects.forEach(World::removeObject);
        collapsedObjects.forEach(World::spawnObject);
        ticks.setValue(301);
        state = TrapState.COLLAPSED;
    }

    public void finalizeNetTrapFailure(@NotNull final WorldObject object) {
        Preconditions.checkArgument(state == TrapState.COLLAPSED);
        collapsedObjects.forEach(World::removeObject);
        World.spawnObject(object);
    }

    /**
     * Collapses this {@link HunterTrap} trap by removing the colliding objects and spawning the collapsed ones in their place, and setting the state of this trap to {@link TrapState#COLLAPSED}.
     */
    public void collapse(@NotNull final Optional<Runnable> onCollapse) {
        Preconditions.checkArgument(state != TrapState.INACTIVE, "Trap has not yet been set up.");
        Preconditions.checkArgument(state != TrapState.COLLAPSED, "Trap has is already collapsed.");
        Preconditions.checkArgument(state != TrapState.REMOVED, "Trap is already removed.");
        builtObjects.forEach(World::removeObject);
        collapsedObjects.forEach(World::spawnObject);
        ticks.setValue(101);
        state = TrapState.COLLAPSED;
        onCollapse.ifPresent(Runnable::run);
    }

    /**
     * Runs a sequence after the trap has already collapsed, to set the object's status to the final version of the collapsed object.
     * @param preyObject the prey who fell into the trap.
     */
    public void postCollapse(@NotNull final PreyObject preyObject) {
        Preconditions.checkArgument(state == TrapState.COLLAPSED, "Trap has not yet collapsed.");
        collapsedObjects.forEach(World::removeObject);
        collapsedObjects.forEach(object -> object.setId(preyObject.getFinalObject()));
        collapsedObjects.forEach(World::spawnObject);
        ticks.setValue(101);
    }

    /**
     * Removes this {@link HunterTrap} trap by removing the spawned objects and re-adding the original ones in their place, and setting the state of this trap to {@link TrapState#REMOVED}
     */
    public void remove() {
        Preconditions.checkArgument(state != TrapState.INACTIVE, "Trap has not yet been set up.");
        Preconditions.checkArgument(state != TrapState.REMOVED, "Trap is already removed.");
        builtObjects.forEach(World::removeObject);
        collapsedObjects.forEach(World::removeObject);
        defaultObjects.forEach(World::spawnObject);
        state = TrapState.REMOVED;
        final HunterDummyNPC dummy = npc.get();
        if (dummy != null) {
            dummy.finish();
        }
        final Player player = this.player.get();
        if (player != null) {
            player.getHunter().removeTrap(this);
        }
    }

    /**
     * Adds a consumer to {@link HunterTrap#tickRunnables} list.
     *
     * @param tick     the tick at which the consumer should execute.
     * @param runnable the consumer to execute.
     */
    public void addConsumer(final int tick, @NotNull final Runnable runnable) {
        Preconditions.checkArgument(tick >= 0, "Tick value cannot be negative.");
        tickRunnables.add(new TickRunnable(tick, runnable));
    }

    /**
     * Processes this hunter trap by executing any runnables found on current tick and incrementing the tick forward.
     */
    public void process() {
        Preconditions.checkArgument(!tickRunnables.isEmpty(), "No tick runnables found.");
        final int currentTick = ticks.getAndIncrement();
        for (final HunterTrap.TickRunnable tickRunnable : tickRunnables) {
            if (currentTick == tickRunnable.tick) {
                tickRunnable.runnable.run();
            }
        }
    }

    public Optional<Runnable> findCollapseRunnable() {
        final HunterTrap.TickRunnable runnable = CollectionUtils.findMatching(tickRunnables, tickRunnable -> tickRunnable.tick == TimeUnit.MINUTES.toTicks(3));
        if (runnable == null) {
            return Optional.empty();
        }
        return Optional.of(runnable.runnable);
    }

    /**
     * Return the pre-defined hashCode value based on the immutable fields of this object.
     *
     * @return the hashcode of this {@link HunterTrap}.
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * Compare this object against the passed object. Every field aside from hashcode(which is by nature the same as it relies on immutable fields) needs to be compared.
     *
     * @param obj the object to compare against.
     * @return whether or not the two objects equal.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        final HunterTrap trap = (HunterTrap) obj;
        return Objects.equals(trap.defaultObjects, defaultObjects) && Objects.equals(trap.builtObjects, builtObjects) && Objects.equals(trap.location, location) && Objects.equals(trap.type, type);
    }

    public WeakReference<Player> getPlayer() {
        return player;
    }

    public WeakReference<HunterDummyNPC> getNpc() {
        return npc;
    }

    public List<WorldObject> getDefaultObjects() {
        return defaultObjects;
    }

    public List<WorldObject> getCollapsedObjects() {
        return collapsedObjects;
    }

    public ImmutableLocation getLocation() {
        return location;
    }

    public TrapType getType() {
        return type;
    }

    public TrapState getState() {
        return state;
    }

    public TrapPrey getPrey() {
        return prey;
    }

    public void setPrey(TrapPrey prey) {
        this.prey = prey;
    }

    public Runnable getRemovalRunnable() {
        return removalRunnable;
    }

    public void setRemovalRunnable(Runnable removalRunnable) {
        this.removalRunnable = removalRunnable;
    }


    /**
     * A wrapper class to hold the tick value and the runnable together.
     */
    private static class TickRunnable {
        private final int tick;
        private final Runnable runnable;

        public TickRunnable(int tick, Runnable runnable) {
            this.tick = tick;
            this.runnable = runnable;
        }
    }
}
