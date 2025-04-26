package com.zenyte.game.content.skills.hunter.npc;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.skills.hunter.HunterUtils;
import com.zenyte.game.content.skills.hunter.PreyInteractState;
import com.zenyte.game.content.skills.hunter.TrapState;
import com.zenyte.game.content.skills.hunter.node.PreyObject;
import com.zenyte.game.content.skills.hunter.node.TrapInteractType;
import com.zenyte.game.content.skills.hunter.node.TrapPrey;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 28/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class BirdHunterNPC extends HunterNPC implements Spawnable {
    private static final Animation landAnimation = new Animation(5171);
    private static final Animation riseAnimation = new Animation(5172);

    BirdHunterNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    void processTrap() {
        Preconditions.checkArgument(trap != null);
        if (!trap.getLocation().matches(getLocation())) {
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
        setFaceEntity(null);
        setFaceLocation(getLocation().transform(Direction.NORTH));
        setAnimation(landAnimation);
        interactState = PreyInteractState.ANIMATED;
        WorldTasksManager.schedule(() -> {
            if (trap.getState() == TrapState.PROCESSING && interactType == TrapInteractType.SUCCESS) {
                return;
            }
            stopChasingTrap();
            setAnimation(riseAnimation);
            setRandomWalkDelay(2);
            //The bird will instantly fly off as the trap fails.
            final Location destTile = getLocation().transform(Direction.NORTH, Utils.random(1, 5));
            WorldTasksManager.schedule(() -> addWalkSteps(destTile.getX(), destTile.getY(), -1, true));
        });
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
                trap.getCollapsedObjects().forEach(object -> object.setId(preyObject.getFirstObject()));
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
        calcFollow(trap.getLocation(), HunterUtils.SEEK_DISTANCE, true, false, false);
        delay = 1;
    }

    @Override
    public boolean validate(int id, String name) {
        return TrapPrey.contains(id, TrapType.BIRD_SNARE);
    }
}
