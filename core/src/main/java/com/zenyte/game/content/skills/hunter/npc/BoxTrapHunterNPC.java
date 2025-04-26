package com.zenyte.game.content.skills.hunter.npc;

import com.google.common.base.Preconditions;
import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.skills.hunter.HunterUtils;
import com.zenyte.game.content.skills.hunter.PreyInteractState;
import com.zenyte.game.content.skills.hunter.TrapState;
import com.zenyte.game.content.skills.hunter.node.PreyObject;
import com.zenyte.game.content.skills.hunter.node.TrapInteractType;
import com.zenyte.game.content.skills.hunter.node.TrapPrey;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.RetreatMechanics;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.npcs.NPCDefinitions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 30/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BoxTrapHunterNPC extends HunterNPC implements Spawnable {
    private static final Animation ferretSuccessAnimation = new Animation(5191);
    private static final Animation ferretFailureAnimation = new Animation(5192);
    private static final Animation successAnimation = new Animation(5184);
    private static final Animation failureAnimation = new Animation(5185);

    BoxTrapHunterNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.isChinchompa = id != -1 && NPCDefinitions.getOrThrow(id).getLowercaseName().contains("chinchompa");
        if (this.isChinchompa) {
            this.combat = new NPCCombat(this) {
                @Override
                public void setTarget(final Entity target, TargetSwitchCause cause) {
                    if (target != null) {
                        getRetreatMechanics().process(target);
                    }
                }
            };
            this.retreatMechanics = new RetreatMechanics(this) {
                @Override
                public boolean process(@NotNull final Entity target) {
                    this.target = target;
                    final Location npcLocation = npc.getLocation();
                    if (outOfBounds(npcLocation)) {
                        retreat(npc.getRespawnTile(), target, false);
                        return true;
                    }
                    final Location targetLocation = target.getLocation();
                    final double distance = npcLocation.getDistance(targetLocation);
                    if (distance > 16) {
                        retreat(npc.getRespawnTile(), target, false);
                    } else {
                        retreat(retreatDestination(target), target, true);
                    }
                    return true;
                }
            };
        }
    }

    private final boolean isChinchompa;
    private static final ForceTalk squeak = new ForceTalk("Squeak!");
    private static final Animation deathAnimation = new Animation(5183, 10);
    private static final Graphics deathGraphics = new Graphics(954, 10, 92);
    private static final SoundEffect deathSound = new SoundEffect(360, 10, 30);

    @Override
    public void sendDeath() {
        if (!isChinchompa) {
            super.sendDeath();
            return;
        }
        final Player player = getMostDamagePlayerCheckIronman();
        onDeath(player);
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                if (ticks == 0) {
                    setForceTalk(squeak);
                } else if (ticks == 1) {
                    setAnimation(deathAnimation);
                    setGraphics(deathGraphics);
                    World.sendSoundEffect(getLocation(), deathSound);
                    if (player != null && player.getLocation().getTileDistance(getLocation()) == 1) {
                        final Location tile = player.getFaceLocation(BoxTrapHunterNPC.this, 2, 1024);
                        final Location destination = new Location(player.getLocation());
                        final int dir = DirectionUtil.getMoveDirection(tile.getX() - destination.getX(), tile.getY() - destination.getY());
                        if (dir != -1) {
                            if (World.checkWalkStep(destination.getPlane(), destination.getX(), destination.getY(), dir, player.getSize(), false, false)) {
                                destination.setLocation(tile);
                            }
                        }
                        player.lock(2);
                        if (!destination.matches(player.getLocation())) {
                            final Location currentTile = new Location(player.getLocation());
                            final ForceMovement fm = new ForceMovement(currentTile, 30, destination, 60, DirectionUtil.getFaceDirection(currentTile.getX() - tile.getX(), currentTile.getY() - tile.getY()));
                            player.setForceMovement(fm);
                            WorldTasksManager.schedule(() -> player.setLocation(destination), 1);
                        } else {
                            player.setAnimation(new Animation(5206, 30));
                        }
                        WorldTasksManager.schedule(() -> player.applyHit(new Hit(2, HitType.REGULAR).setExecuteIfLocked()));
                    }
                } else if (ticks == 3) {
                    onFinish(player);
                    stop();
                    return;
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    void processTrap() {
        Preconditions.checkArgument(trap != null);
        if (trap.getLocation().getDistance(getLocation()) > 1) {
            return;
        }
        if (delay > 0) {
            delay--;
            return;
        }
        if (interactState == null) {
            jump();
        } else if (interactState == PreyInteractState.ANIMATED) {
            collapseOrFallIn();
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
    private void jump() {
        Preconditions.checkArgument(trap != null);
        setFaceEntity(null);
        if (prey == TrapPrey.FERRET) {
            setAnimation(interactType == TrapInteractType.SUCCESS ? ferretSuccessAnimation : ferretFailureAnimation);
        } else {
            setAnimation(interactType == TrapInteractType.SUCCESS ? successAnimation : failureAnimation);
        }
        interactState = PreyInteractState.ANIMATED;
        setRandomWalkDelay(1);
    }

    /**
     * Either collapses the trap if the {@link BirdHunterNPC} wasn't caught in it, or removes the bird and plays the animation of the bird falling on the bird snare.
     */
    private void collapseOrFallIn() {
        Preconditions.checkArgument(trap != null);
        final PreyObject preyObject = prey.getObject();
        Preconditions.checkArgument(preyObject != null);
        Preconditions.checkArgument(interactType != null);
        final boolean processing = trap.getState() == TrapState.PROCESSING;
        final boolean success = processing && interactType == TrapInteractType.SUCCESS;
        if (processing) {
            if (success) {
                //Play the creature-getting-stuck-in-box object animation from the correct angle, so we adjust the id of the object.
                final int[] array = prey.getObject().getObjects();
                final ImmutableLocation tile = trap.getLocation();
                final int objectId = array[getY() < tile.getY() ? 0 : getY() > tile.getY() ? 2 : getX() > tile.getX() ? 3 : 1];
                trap.getCollapsedObjects().forEach(obj -> obj.setId(objectId));
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
            WorldTasksManager.schedule(this::processTrap, 1);
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
            calcFollow(dummy, HunterUtils.SEEK_DISTANCE, true, false, false);
            delay = 1;
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return TrapPrey.contains(id, TrapType.BOX_TRAP);
    }
}
