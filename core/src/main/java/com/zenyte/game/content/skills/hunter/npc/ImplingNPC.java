package com.zenyte.game.content.skills.hunter.npc;

import com.zenyte.game.content.skills.hunter.node.Impling;
import com.zenyte.game.content.skills.magic.spells.arceuus.DarkLureKt;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.impl.misc.Bird;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Corey
 * @since 22/01/20
 */
public class ImplingNPC extends Bird implements Spawnable {
    public static final int INVISIBLE_NPC_ID = 7211;
    private final int originalId;
    private boolean invisibleSpawn;
    private Entity retreatingFrom;
    private int distanceToRetreat;
    private long tauntDelay;
    private int despawnTimer = -10;
    private Consumer<ImplingNPC> onFinished = NPC::setRespawnTask;

    private int respawnTime = 10;

    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    public ImplingNPC(int id, Location tile, Direction facing) {
        this(id, tile, facing, tile != null && tile.getRegionId() == 10307 ? 10 : 8192);
    }

    public ImplingNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.originalId = id;
        setSpawned(true);
    }

    public void setRetreatingFrom(@NotNull final Entity entity) {
        this.retreatingFrom = entity;
        this.distanceToRetreat = Utils.random(5, 15);
    }

    private final boolean calculateRetreat(final int offset) {
        resetWalkSteps();
        if (getFaceEntity() == -1) {
            setFaceEntity(retreatingFrom);
        }
        final Location middle = getMiddleLocation();
        double degrees = Math.toDegrees(Math.atan2(middle.getY() - retreatingFrom.getY(), middle.getX() - retreatingFrom.getX()));
        degrees += offset;
        if (degrees < 0) {
            degrees += 360;
        } else if (degrees > 360) {
            degrees -= 360;
        }
        final double angle = Math.toRadians(degrees);
        final int movingX = (int) Math.round(middle.getX() + (getSize() + distanceToRetreat) * Math.cos(angle));
        final int movingY = (int) Math.round(middle.getY() + (getSize() + distanceToRetreat) * Math.sin(angle));
        calcFollow(new Location(movingX, movingY, getPlane()), distanceToRetreat, true, false, false);
        return hasWalkSteps();
    }

    @Override
    public void processNPC() {
        if (!DarkLureKt.getUnderDarkLure(this)) {
            if (retreatingFrom != null) {
                if (isLocked() || isFrozen() || isStunned()) {
                    return;
                }
                if (!calculateRetreat(0)) {
                    final int multiplier = Utils.random(1) == 0 ? -1 : 1;
                    if (!calculateRetreat(512 * multiplier)) {
                        calculateRetreat(512 * (-multiplier));
                    }
                }
                if (!hasWalkSteps()) {
                    distanceToRetreat >>= 1;
                } else {
                    distanceToRetreat--;
                }
                if (distanceToRetreat <= 0) {
                    setFaceEntity(retreatingFrom = null);
                }
                return;
            }
            super.processNPC();
        }
        if (despawnTimer > 0) {
            despawnTimer--;
        } else if (despawnTimer == 0) {
            finish();
            return;
        }
        if (getId() != INVISIBLE_NPC_ID && tauntDelay < Utils.currentTimeMillis() && Utils.random(175) == 0) {
            tauntDelay = Utils.currentTimeMillis() + 10000;
            taunt(false);
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return Impling.contains(id);
    }

    @Override
    public NPC spawn() {
        super.spawn();
        if (invisibleSpawn) {
            setTransformation(INVISIBLE_NPC_ID);
            WorldTasksManager.schedule(this::appear, Utils.random(100, 300));
        } else {
            startDespawnTimer();
        }
        return this;
    }

    public ImplingNPC setInvisibleSpawn(final boolean invisible) {
        this.invisibleSpawn = invisible;
        return this;
    }

    public ImplingNPC setOnFinished(Consumer<ImplingNPC> onFinished) {
        this.onFinished = onFinished;
        return this;
    }

    private void appear() {
        World.sendGraphics(new Graphics(1119), getLocation());
        setTransformation(originalId);
        taunt(true);
        startDespawnTimer();
    }

    private void taunt(final boolean spawn) {
        World.sendSoundEffect(getLocation(), new SoundEffect(3722, spawn ? 15 : 3));
        setForceTalk(new ForceTalk("Tee hee!"));
        if (!spawn) {
            setAnimation(new Animation(6618));
        }
    }

    @Override
    public void finish() {
        super.finish();
        retreatingFrom = null;
        distanceToRetreat = 0;
        if (despawnTimer == 0) {
            World.sendGraphics(new Graphics(86, 0, 250), getLocation());
        }
        Optional.ofNullable(onFinished).ifPresent(action -> action.accept(this));
    }

    @Override
    public int getRespawnDelay() {
        return respawnTime;
    }

    private void startDespawnTimer() {
        despawnTimer = Utils.random(1300, 1700); // between 13 and 17 minutes
    }

}
