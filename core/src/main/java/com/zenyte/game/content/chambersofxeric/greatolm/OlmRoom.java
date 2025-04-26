package com.zenyte.game.content.chambersofxeric.greatolm;

import com.zenyte.game.content.chambersofxeric.CombatPointCapCalculator;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.ScalingMechanics;
import com.zenyte.game.content.chambersofxeric.greatolm.scripts.DeepBurn;
import com.zenyte.game.content.chambersofxeric.greatolm.scripts.TransitionalFallingCrystals;
import com.zenyte.game.content.chambersofxeric.map.BossChunk;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Kris | 16. nov 2017 : 4:10.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class OlmRoom extends BossChunk implements CycleProcessPlugin, FullMovementPlugin, PlayerCombatPlugin {
    /**
     * Constants defining the left and the right sides of the olm room.
     */
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    /**
     * The head object on the left side of the Olm room in the static map.
     */
    static final WorldObject leftSideHeadObject = new WorldObject(29882, 10, 3, 3220, 5738, 0);
    /**
     * The head object on the right side of the olm room in the static map.
     */
    static final WorldObject rightSideheaDObject = new WorldObject(29882, 10, 1, 3238, 5738, 0);
    /**
     * The left claw object in the left side of the olm room in the static map.
     */
    static final WorldObject leftSideLeftClawObject = new WorldObject(29885, 10, 3, 3220, 5743, 0);
    /**
     * The right claw object in the left side of the olm room in the static map.
     */
    static final WorldObject leftSideRightClawObject = new WorldObject(29888, 10, 3, 3220, 5733, 0);
    /**
     * The left claw object in the right side of the olm room in the static map.
     */
    static final WorldObject rightSideLeftClawObject = new WorldObject(29885, 10, 1, 3238, 5733, 0);
    /**
     * The right claw object in the right side of the olm room in the static map.
     */
    static final WorldObject rightSideRightClawObject = new WorldObject(29888, 10, 1, 3238, 5743, 0);
    /**
     * The animation played when the olm rises from beneath.
     */
    private static final SoundEffect riseSound = new SoundEffect(3405, 15, 0);
    /**
     * The number of total phases that the Olm will go through before it can die for good.
     */
    private int totalPhases;
    /**
     * The list of acid pools in the Olm's room, used to prevent collisions. Takes priority over crystal clusters.
     */
    private final List<AcidPool> acidPools = new ObjectArrayList<>();
    /**
     * The list of crystal clusters in the Olm's room, used to prevent collisions.
     */
    private final List<CrystalCluster> crystalClusters = new ObjectArrayList<>();
    /**
     * The south-westernmost corner of the room from where Olm can still see and attack you.
     */
    private final Location leftCorner = this.getLocation(new Location(3227, 5730, 0));
    /**
     * The north-easternmost corner of the room from where Olm can still see and attack you.
     */
    private final Location rightCorner = this.getLocation(new Location(3237, 5748, 0));
    /**
     * The line that separates the room into two halves. Only the 'y' coordinate is relevant here.
     */
    private final Location separatingLine = this.getLocation(new Location(3237, 5740, 0));
    /**
     * The south-eastern tile which defines the border for the southern side. South and including on this tile, Olm cannot see you unless it is facing south. Only the 'y' coordinate
     * is relevant here.
     */
    private final Location southernMiddleLine = this.getLocation(new Location(3237, 5734, 0));
    /**
     * The north-eastern tile which defines the border for the northern side. North and including on this tile, Olm cannot see you unless it is facing north. Only the 'y' coordinate
     * is relevant here.
     */
    private final Location northernMiddleLine = this.getLocation(new Location(3237, 5746, 0));
    /**
     * The tile which defines the 'face' location of the Olm on the left side. It is used to fly projectiles out of the correct starting position.
     */
    private final Location leftSideHead = this.getLocation(new Location(3227, 5740, 0));
    /**
     * The tile which defines the 'face' location of the Olm on the right side. It is used to fly projectiles out of the correct starting position.
     */
    private final Location rightSideHead = this.getLocation(new Location(3238, 5740, 0));
    /**
     * The south-western tile from where the fire wall starts when fired from the eastern side. Only the 'x' coordinate is relevant here.
     */
    private final Location fireWallSouthWesternLoc = this.getLocation(new Location(3228, 5731, 0));
    /**
     * The north-eastern tile from where the fire wall starts when fired from the western side. Only the 'x' coordinate is relevant here.
     */
    private final Location fireWallNorthEasternLoc = this.getLocation(new Location(3237, 5748, 0));
    /**
     * The center of the room and the entrance position where the player lands when they climb down the rope.
     */
    private Location center;
    private Location entrance;
    /**
     * The Gear Olm NPC.
     */
    private GreatOlm olm;
    /**
     * Great Olm's left claw NPC.
     */
    private LeftClaw leftClaw;
    /**
     * Great Olm's right claw NPC.
     */
    private RightClaw rightClaw;
    /**
     * The head object in the dynamic map.
     */
    private WorldObject headObject;
    /**
     * The left claw object in the dynamic map.
     */
    private WorldObject leftClawObject;
    /**
     * The right claw object in the dynamic map.
     */
    private WorldObject rightClawObject;
    /**
     * The value of the current phase.
     */
    private int currentPhase = 1;
    /**
     * The side Olm is currently positioned at.
     */
    private int side = Utils.random(1);
    /**
     * The current phase element with which Olm has risen, a value of -1 defines absence.
     */
    private int phase = -1;
    /**
     * The game cycle tick when the previous Olm animation is meant to end, this is used to smoothen Olm's fall animation right after it performs an attack.
     */
    private transient long animationEndTick;
    /**
     * The delay before the Olm can perform another room switch, used to block back-to-back switches in case of simultaneous claw deaths. Adds a delay of 20 ticks before another
     * switch can execute - the switch itself takes around 30 ticks.
     */
    private transient long switchDelay;
    private transient OlmAnimation latestHeadAnimation;

    private boolean bypassMode = false;

    public void setBypassMode() {
        bypassMode = true;
    }

    public OlmRoom(final Raid raid, final int x, final int y) {
        super(null, raid, x, y);
    }

    /**
     * Sets the total phases that the olm goes through.
     */
    public void setTotalPhases() {
        totalPhases = ScalingMechanics.getTotalOlmPhases(raid);
    }

    /**
     * Gets the movement direction for the force movement mask when the player is being "pushed off" of an object, either fire wall or crystal burst.
     *
     * @param player the player who is being pushed.
     * @param tile   the tile to which the player is being pushed.
     * @return an approximate main direction where the player is being pushed. Even if the push is diagonally, the player still faces one of the main four directions.
     */
    public static final int getMovementDirection(@NotNull final Player player, @NotNull final Location tile) {
        return getMovementDirection(player.getLocation(), tile);
    }

    public static final int getMovementDirection(@NotNull final Location location, @NotNull final Location tile) {
        final int offsetX = location.getX() - tile.getX();
        final int offsetY = location.getY() - tile.getY();
        if (offsetX < 0) {
            return ForceMovement.EAST;
        } else if (offsetX > 0) {
            return ForceMovement.WEST;
        } else if (offsetY < 0) {
            return ForceMovement.NORTH;
        }
        return ForceMovement.SOUTH;
    }

    @Override
    public boolean sendDeath(final Player player, final Entity source) {
        if (olm != null) {
            final DeepBurn atk = olm.getBurnAttack();
            if (atk != null) {
                atk.getInfectionsMap().removeInt(player);
            }
        }
        return super.sendDeath(player, source);
    }

    /**
     * We add no extra reductions to the Olm room, giving it a cap of half a max integer. There is no way to abuse the room as there are other checks in place to prevent this, and
     * calculating it is rather complicated as the NPCs spawn dynamically one after another.
     *
     * @return the combat point cap calculator.
     */
    @Override
    public CombatPointCapCalculator buildPointsCap() {
        return new CombatPointCapCalculator().appendExtra(Integer.MAX_VALUE >> 1);
    }

    /**
     * Whether or not the room contains a pool on the specified tile.
     *
     * @param tile the tile which to check.
     * @return whether there's an acid pool on the tile or not.
     */
    public boolean containsPool(@NotNull final Location tile) {
        return containsPool(tile.getX(), tile.getY(), tile.getPlane());
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        if (raid == null) {
            return;
        }
        if (raid.isCompleted()) {
            player.getVarManager().sendBit(5425, 5);
        }
    }

    public boolean canLay(@NotNull final Player player, @NotNull final LayableObjectType type) {
        if (type == LayableObjectType.BIRD_SNARE || type == LayableObjectType.BOX_TRAP || type == LayableObjectType.FIRE) {
            player.sendMessage("You can't do that here!");
            return false;
        }
        return super.canLay(player, type);
    }

    @Override
    public void leave(final Player player, boolean logout) {
        super.leave(player, logout);
        if (raid == null) {
            return;
        }
        if (this.inChamber(player.getLocation())) {
            final int points = raid.getPoints(player);
            if (points >= 8400) {
                raid.decrementPoints(player, (int) (points * 0.4F));
            } else {
                raid.decrementGroupPoints();
            }
        }
    }

    /**
     * Whether or not the specified location already contains an acid pool.
     *
     * @param x     the x coordinate to check
     * @param y     the y coordinate to check
     * @param plane the plane to check
     * @return contains or not.
     */
    private boolean containsPool(final int x, final int y, final int plane) {
        if (acidPools.isEmpty()) {
            return false;
        }
        final int posHash = y + (x << 14) + (plane << 28);
        for (final AcidPool pool : acidPools) {
            if (pool.getPositionHash() == posHash && pool.getTicks() <= 15) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether or not the specified location contains a crystal cluster underneath it.
     *
     * @param tile the tile which to check.
     * @return whether or not there's a crystal cluster there.
     */
    public boolean containsCrystalCluster(@NotNull final Location tile) {
        if (crystalClusters.isEmpty()) {
            return false;
        }
        for (final CrystalCluster cluster : crystalClusters) {
            if (cluster.getPositionHash() == tile.getPositionHash()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether the location is within the attackable boundaries in the chamber.
     *
     * @param tile location to check
     * @return whether it's a valid location or not.
     */
    public boolean inChamber(@NotNull final Location tile) {
        final int x = tile.getX();
        final int y = tile.getY();
        return x >= leftCorner.getX() && x <= rightCorner.getX() && y >= leftCorner.getY() && y <= rightCorner.getY();
    }

    /**
     * Sends an animation on the specified body part of the great olm. Filters the name of the animation to ensure there are no mix-ups
     * between the animations and the bodyparts.
     *
     * @param object    the object to animate
     * @param animation the animation to perform
     */
    void sendAnimation(@NotNull final WorldObject object, @NotNull final OlmAnimation animation) {
        if (object == leftClawObject) {
            if (animation.toString().startsWith("LEFT") && (leftClaw == null || !leftClaw.isFinished() || !leftClaw.isCantInteract())) {
                sendObjectAnimation(object, leftClaw != null && leftClaw.isClenched() ? animation.getSpecial() : animation.getPrimary());
            }
        } else if (object == rightClawObject) {
            if (animation.toString().startsWith("RIGHT") && (rightClaw == null || !rightClaw.isFinished() || !rightClaw.isCantInteract())) {
                sendObjectAnimation(object, animation.getPrimary());
            }
        } else if (animation.toString().startsWith("HEAD")) {
            this.latestHeadAnimation = animation;
            //if (!animation.isTurnAnimation()) {
            this.animationEndTick = WorldThread.WORLD_CYCLE + (Math.min(TimeUnit.MILLISECONDS.toTicks(animation.getAnimationLength()), 1));
            //}
            sendObjectAnimation(object, currentPhase == totalPhases ? animation.getSpecial() : animation.getPrimary());
        }
    }

    /**
     * Executes an animation on the requested object.
     *
     * @param object    the object on which to execute the animation.
     * @param animation the animation which to execute on the object.
     */
    private void sendObjectAnimation(final WorldObject object, final Animation animation) {
        World.sendObjectAnimation(object, animation);
    }

    /**
     * Performs the rise sequence on the Great Olm.
     */
    public void rise() {
        if (getRaid().isDestroyed()) {
            return;
        }
        World.spawnObject(headObject = new WorldObject(29880, 10, side == LEFT ? leftSideHeadObject.getRotation() : rightSideheaDObject.getRotation(), getLocation(side == LEFT ? leftSideHeadObject : rightSideheaDObject)));
        World.spawnObject(leftClawObject = new WorldObject(29883, 10, side == LEFT ? leftSideLeftClawObject.getRotation() : rightSideLeftClawObject.getRotation(), getLocation(side == LEFT ? leftSideLeftClawObject : rightSideLeftClawObject)));
        World.spawnObject(rightClawObject = new WorldObject(29886, 10, side == LEFT ? leftSideRightClawObject.getRotation() : rightSideRightClawObject.getRotation(), getLocation(side == LEFT ? leftSideRightClawObject : rightSideRightClawObject)));
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (getRaid().isDestroyed()) {
                    stop();
                    return;
                }
                if (ticks++ == 0) {
                    World.sendSoundEffect(headObject.transform(2, 2, 0), riseSound);
                    sendAnimation(headObject, OlmAnimation.HEAD_RISE);
                    sendAnimation(leftClawObject, OlmAnimation.LEFT_CLAW_RISE);
                    sendAnimation(rightClawObject, OlmAnimation.RIGHT_CLAW_RISE);
                } else {
                    World.spawnObject(headObject = new WorldObject(29881, 10, side == LEFT ? leftSideHeadObject.getRotation() : rightSideheaDObject.getRotation(), getLocation(side == LEFT ? leftSideHeadObject : rightSideheaDObject)));
                    World.spawnObject(leftClawObject = new WorldObject(29884, 10, side == LEFT ? leftSideLeftClawObject.getRotation() : rightSideLeftClawObject.getRotation(), getLocation(side == LEFT ? leftSideLeftClawObject : rightSideLeftClawObject)));
                    World.spawnObject(rightClawObject = new WorldObject(29887, 10, side == LEFT ? leftSideRightClawObject.getRotation() : rightSideRightClawObject.getRotation(), getLocation(side == LEFT ? leftSideRightClawObject : rightSideRightClawObject)));
                    final int xOffset = side == LEFT ? 3 : 0;
                    olm = new GreatOlm(OlmRoom.this, new Location(headObject.getX() + xOffset + (side == LEFT ? -1 : 1), headObject.getY(), headObject.getPlane()));
                    olm.spawn();
                    olm.getCombat().setCombatDelay(5);
                    leftClaw = new LeftClaw(OlmRoom.this, new Location(leftClawObject.getX() + xOffset, leftClawObject.getY(), leftClawObject.getPlane()));
                    leftClaw.spawn();
                    final IntArrayList phases = new IntArrayList(new int[] {GreatOlm.ACID, GreatOlm.FLAME, GreatOlm.CRYSTAL});
                    final int lastPhase = phase;
                    phases.rem(lastPhase);
                    phase = Utils.getRandomCollectionElement(phases);
                    rightClaw = new RightClaw(OlmRoom.this, new Location(rightClawObject.getX() + xOffset, rightClawObject.getY(), rightClawObject.getPlane()));
                    rightClaw.spawn();
                    if (!olm.isPenultimatePhase()) {
                        final String message = "The Great Olm rises with the power of " + (phase == GreatOlm.ACID ? "<col=00ff00>Acid" : phase == GreatOlm.FLAME ? "<col=ff0000>Flame" : "<col=006000>Crystals") + "</col>.";
                        olm.everyone(GreatOlm.ENTIRE_CHAMBER).forEach(player -> player.sendMessage(message));
                    }
                    sendAnimation(headObject, OlmAnimation.HEAD_MIDDLE_DEFAULT);
                    sendAnimation(leftClawObject, OlmAnimation.LEFT_CLAW_DEFAULT);
                    sendAnimation(rightClawObject, OlmAnimation.RIGHT_CLAW_DEFAULT);
                    stop();
                }
            }
        }, 10, 4);
    }

    @Override
    public boolean processCombat(final Player player, final Entity entity, final String style) {
        if (style.equals("Melee") && (entity instanceof GreatOlm || entity instanceof RightClaw) || !inChamber(player.getLocation())) {
            player.sendMessage("I can't reach that!");
            player.resetWalkSteps();
            return false;
        }
        if (entity instanceof LeftClaw) {
            final LeftClaw claw = (LeftClaw) entity;
            if (claw.isClenched() && !olm.isPenultimatePhase()) {
                player.sendMessage("The Great Olm is currently protecting its left claw! You cannot see a way to attack it successfully.");
                return false;
            }
        }
        return true;
    }

    /**
     * Performs the fall sequence on the Great Olm, additionally switches the side variable.
     */
    private void fall() {
        if (getRaid().isDestroyed()) {
            return;
        }
        sendAnimation(headObject, OlmAnimation.HEAD_FALL);
        olm.finish();
        if (leftClaw != null) {
            leftClaw.finish();
        }
        if (rightClaw != null) {
            rightClaw.finish();
        }
        WorldTasksManager.schedule(() -> {
            World.spawnObject(new WorldObject(29882, 10, side == LEFT ? leftSideHeadObject.getRotation() : rightSideheaDObject.getRotation(), getLocation(side == LEFT ? leftSideHeadObject : rightSideheaDObject)));
            side = side == LEFT ? RIGHT : LEFT;
        }, 4);
    }

    /**
     * Switches the side of the Great Olm, unless it's already the last, penultimate phase.
     */
    void switchSide() {
        if (switchDelay > System.currentTimeMillis()) {
            return;
        }
        if (olm.isPenultimatePhase()) {
            olm.everyone(GreatOlm.ENTIRE_CHAMBER).forEach(p -> p.sendMessage("The Great Olm is giving its all. This is its final stand."));
            return;
        }
        switchDelay = System.currentTimeMillis() + TimeUnit.TICKS.toMillis(20);
        queueOrExecute(this::forceSwitch);
    }

    /**
     * Executes the runnable passed if the delay since last animation played has ran up, otherwise delays it enough to finish the last animation, then executing the runnable.
     *
     * @param runnable the runnable to execute.
     */
    void queueOrExecute(@NotNull final Runnable runnable) {
        if (WorldThread.WORLD_CYCLE < this.animationEndTick) {
            WorldTasksManager.schedule(runnable::run, (int) (this.animationEndTick - WorldThread.WORLD_CYCLE));
            return;
        }
        runnable.run();
    }

    /**
     * Forces the room switch by starting the transitional falling crystals sequence and performing the fall animation on the Olm itself. Rises back up after 25 ticks.
     */
    private void forceSwitch() {
        new TransitionalFallingCrystals(33).handle(olm);
        fall();
        WorldTasksManager.schedule(() -> {
            currentPhase++;
            rise();
        }, 24);
    }

    @Override
    public String name() {
        return "Chambers of Xeric: The Great Olm Chamber";
    }

    @Override
    public void process() {
        if (!acidPools.isEmpty()) {
            acidPools.removeIf(pool -> !pool.process());
        }
        if (!crystalClusters.isEmpty()) {
            crystalClusters.removeIf(cluster -> !cluster.process());
        }
        if (acidPools.isEmpty()) {
            return;
        }
        for (final Player player : getPlayers()) {
            if (containsPool(player.getLocation())) {
                player.applyHit(new Hit(Utils.random(3, 6), HitType.POISON));
                player.getToxins().applyToxin(ToxinType.POISON, 4);
            }
        }
    }

    @Override
    public boolean processMovement(final Player player, final int x, final int y) {
        final long acid = player.getNumericTemporaryAttribute("acidDrip").longValue();
        if (acid == 0) {
            return true;
        }
        if (acid < Utils.currentTimeMillis() || this.containsPool(x, y, player.getPlane())) {
            return true;
        }
        acidPools.add(new AcidPool(new Location(x, y, player.getPlane())));
        return true;
    }

    public int getTotalPhases() {
        return totalPhases;
    }

    public List<AcidPool> getAcidPools() {
        return acidPools;
    }

    public List<CrystalCluster> getCrystalClusters() {
        return crystalClusters;
    }

    public Location getLeftCorner() {
        return leftCorner;
    }

    public Location getRightCorner() {
        return rightCorner;
    }

    public Location getSeparatingLine() {
        return separatingLine;
    }

    public Location getSouthernMiddleLine() {
        return southernMiddleLine;
    }

    public Location getNorthernMiddleLine() {
        return northernMiddleLine;
    }

    public Location getLeftSideHead() {
        return leftSideHead;
    }

    public Location getRightSideHead() {
        return rightSideHead;
    }

    public Location getFireWallSouthWesternLoc() {
        return fireWallSouthWesternLoc;
    }

    public Location getFireWallNorthEasternLoc() {
        return fireWallNorthEasternLoc;
    }

    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public Location getEntrance() {
        return entrance;
    }

    public void setEntrance(Location entrance) {
        this.entrance = entrance;
    }

    public GreatOlm getOlm() {
        return olm;
    }

    public LeftClaw getLeftClaw() {
        return leftClaw;
    }

    public void setLeftClaw(LeftClaw leftClaw) {
        this.leftClaw = leftClaw;
    }

    public RightClaw getRightClaw() {
        return rightClaw;
    }

    public void setRightClaw(RightClaw rightClaw) {
        this.rightClaw = rightClaw;
    }

    public WorldObject getHeadObject() {
        return headObject;
    }

    public WorldObject getLeftClawObject() {
        return leftClawObject;
    }

    public WorldObject getRightClawObject() {
        return rightClawObject;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public int getSide() {
        return side;
    }

    public int getPhase() {
        return phase;
    }

    public long getAnimationEndTick() {
        return animationEndTick;
    }

    public long getSwitchDelay() {
        return switchDelay;
    }

    public OlmAnimation getLatestHeadAnimation() {
        return latestHeadAnimation;
    }

    public boolean getBypassMode() {
        return bypassMode;
    }
}
