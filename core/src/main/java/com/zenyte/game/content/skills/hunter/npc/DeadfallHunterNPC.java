package com.zenyte.game.content.skills.hunter.npc;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.skills.hunter.PreyInteractState;
import com.zenyte.game.content.skills.hunter.TrapState;
import com.zenyte.game.content.skills.hunter.node.PreyObject;
import com.zenyte.game.content.skills.hunter.node.TrapInteractType;
import com.zenyte.game.content.skills.hunter.node.TrapPrey;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCTileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * @author Kris | 31/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DeadfallHunterNPC extends HunterNPC implements Spawnable {
    private static final Animation collapseAnimation = new Animation(5275);
    private static final Animation retreatAnimation = new Animation(5277);

    DeadfallHunterNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    void processTrap() {
        Preconditions.checkArgument(trap != null);
        if (!trap.getLocation().withinDistance(this, 1) || hasWalkSteps()) {
            return;
        }
        if (delay > 0) {
            delay--;
            return;
        }
        if (interactState == null) {
            land();
        } else if (interactState == PreyInteractState.ANIMATED) {
            collapseOrDie();
        } else if (interactState == PreyInteractState.INTERACTED) {
            setTrapCheckable();
        }
    }

    /**
     * Executes the sequence which makes the npc dive in on the bird snare.
     * <p>Schedules a timer-based task one game tick later for the {@link BirdHunterNPC} to rise back up off the ground
     * assuming the bird didn't fall to the trap. This forced time-scheduling is necessary to ensure that the rise sequence
     * gets executed even if the trap vanishes by either dismantling or collapsing.</p>
     */
    private void land() {
        Preconditions.checkArgument(trap != null);
        setAnimation(interactType == TrapInteractType.SUCCESS ? collapseAnimation : retreatAnimation);
        interactState = PreyInteractState.ANIMATED;
    }

    /**
     * Either collapses the trap if the {@link BirdHunterNPC} wasn't caught in it, or removes the bird and plays the animation of the bird falling on the bird snare.
     */
    private void collapseOrDie() {
        Preconditions.checkArgument(trap != null);
        final PreyObject preyObject = prey.getObject();
        Preconditions.checkArgument(preyObject != null);
        Preconditions.checkArgument(interactType != null);
        final boolean processing = trap.getState() == TrapState.PROCESSING;
        final boolean success = processing && interactType == TrapInteractType.SUCCESS;
        if (processing) {
            if (success) {
                //Set the collapsed object id.
                trap.getCollapsedObjects().forEach(object -> object.setId(calculateFallingTrapId(object)));
            }
            trap.collapse();
        }
        interactState = PreyInteractState.INTERACTED;
        World.sendSoundEffect(getLocation(), new SoundEffect(trapType.getSuccessfulCatchSound(), 5));
        if (success) {
            World.sendSoundEffect(getLocation(), new SoundEffect(trapType.getCreatureDeathSound(), 5));
            trap.setPrey(prey);
            finish();
            setRespawnTask();
            final HunterDummyNPC dummy = trap.getNpc().get();
            if (dummy != null) {
                dummy.setTransformation(trap.getType().getInactiveDummyNpcId());
            }
            //Last one needs to be scheduled as the NPC has been removed from the game.
            WorldTasksManager.schedule(this::processTrap);
        }
    }

    @Override
    public boolean isPotentialTarget(final Entity entity) {
        if (!(entity instanceof HunterDummyNPC)) {
            return false;
        }
        final HunterDummyNPC npc = (HunterDummyNPC) entity;
        if (npc.getId() == trapType.getActiveDummyNpcId()) {
            final WeakReference<HunterTrap> trapReference = npc.getTrap();
            if (trapReference == null) {
                return false;
            }
            final HunterTrap trap = trapReference.get();
            if (trap == null || !trap.getState().equals(TrapState.ACTIVE)) {
                return false;
            }
            final Player player = trap.getPlayer().get();
            return player != null && !player.isNulled() && player.getSkills().getLevel(SkillConstants.HUNTER) >= prey.getLevel();
        }
        return false;
    }

    private int calculateFallingTrapId(@NotNull final WorldObject object) {
        final int rotation = object.getRotation();
        final PreyObject preyObject = prey.getObject();
        Preconditions.checkArgument(preyObject != null);
        final int[] objects = preyObject.getObjects();
        Preconditions.checkArgument(objects != null);
        Preconditions.checkArgument(objects.length == 2);
        switch (rotation) {
        case 0: 
            return objects[getY() > object.getY() ? 0 : 1];
        case 2: 
            return objects[getY() < object.getY() ? 0 : 1];
        case 1: 
            return objects[getX() > object.getX() ? 0 : 1];
        case 3: 
            return objects[getX() < object.getX() ? 0 : 1];
        }
        throw new IllegalStateException();
    }

    /**
     * Marks the trap as checkable by the player.
     */
    private void setTrapCheckable() {
        Preconditions.checkArgument(trap != null);
        final PreyObject preyObject = prey.getObject();
        Preconditions.checkArgument(preyObject != null);
        trap.postCollapse(preyObject);
    }

    @Override
    public void interact() {
        Preconditions.checkArgument(trap != null);
        final HunterDummyNPC dummy = trap.getNpc().get();
        if (dummy != null) {
            WorldTasksManager.schedule(() -> setFaceEntity(dummy));
        }
        final WorldObject originalTrap = Objects.requireNonNull(trap.getDefaultObjects().get(0));
        final int rotation = originalTrap.getRotation();
        final Location location = originalTrap.transform(rotation == 0 ? 1 : 0, rotation == 3 ? 1 : 0, 0);
        int flag = RouteEvent.BLOCK_FLAG_EAST | RouteEvent.BLOCK_FLAG_WEST;
        if (rotation != 0) {
            flag = ((flag << rotation) & 15) + (flag >> (4 - rotation));
        }
        setRouteEvent(new NPCTileEvent(this, new TileStrategy(location, 1, flag), null));
        delay = 1;
    }

    @Override
    public boolean validate(int id, String name) {
        return TrapPrey.contains(id, TrapType.DEADFALL);
    }
}
