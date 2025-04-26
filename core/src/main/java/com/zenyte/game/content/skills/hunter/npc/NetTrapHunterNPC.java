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
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 02/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NetTrapHunterNPC extends HunterNPC implements Spawnable {
    NetTrapHunterNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    void processTrap() {
        Preconditions.checkArgument(trap != null);
        final HunterDummyNPC dummy = trap.getNpc().get();
        if (dummy == null) {
            throw new IllegalStateException();
        }
        if (!getLocation().matches(dummy.getLocation())) {
            if (!hasWalkSteps()) {
                calcFollow(dummy.getLocation(), HunterUtils.SEEK_DISTANCE, true, true, false);
            }
            return;
        }
        if (delay > 0) {
            delay--;
            return;
        }
        if (interactState == null) {
            walkOnNet();
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
    private void walkOnNet() {
        Preconditions.checkArgument(trap != null);
        interactState = PreyInteractState.ANIMATED;
        if (interactType == TrapInteractType.SUCCESS) {
            trap.setPrey(prey);
            finish();
            setRespawnTask();
        }
        final HunterDummyNPC dummy = trap.getNpc().get();
        if (dummy != null) {
            dummy.setTransformation(trap.getType().getInactiveDummyNpcId());
        }
        WorldTasksManager.schedule(this::processTrap);
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
                trap.collapse();
            } else {
                //Run the pre-defined failure sequence.
                trap.findCollapseRunnable().ifPresent(Runnable::run);
            }
        }
        interactState = PreyInteractState.INTERACTED;
        World.sendSoundEffect(getLocation(), new SoundEffect(trapType.getSuccessfulCatchSound(), 5));
        if (success) {
            World.sendSoundEffect(getLocation(), new SoundEffect(trapType.getCreatureDeathSound(), 5));
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
            calcFollow(dummy.getLocation(), HunterUtils.SEEK_DISTANCE, true, false, false);
        }
        delay = 1;
    }

    @Override
    public boolean validate(int id, String name) {
        return TrapPrey.contains(id, TrapType.NET_TRAP_SWAMP_LIZARD) || TrapPrey.contains(id, TrapType.NET_TRAP_ORANGE_SALAMANDER) || TrapPrey.contains(id, TrapType.NET_TRAP_RED_SALAMANDER) || TrapPrey.contains(id, TrapType.NET_TRAP_BLACK_SALAMANDER);
    }
}
