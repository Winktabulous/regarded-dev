package com.zenyte.game.content.chambersofxeric.greatolm;

import com.zenyte.game.content.chambersofxeric.ScalingMechanics;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Set;

/**
 * @author Kris | 14. jaan 2018 : 2:20.30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class LeftClaw extends GreatOlmClaw {
    /**
     * The three signs which will imply what attack the Olm is currently using, these occur throughout all phases, as long as the claws are available.
     */
    public static final int CRYSTAL_SIGN = 0;
    public static final int LIGHTNING_SIGN = 1;
    public static final int SWIRL_SIGN = 2;
    /**
     * The infinity symbol which is used during the final phase to imply that any damage the claw takes will heal it instead.
     */
    private static final int INFINITY_SIGN = 3;
    /**
     * The sound effect for the sign animation on the claw object.
     */
    private static final SoundEffect signSound = new SoundEffect(1551, 15, 0);
    /**
     * Whether or not the claw is currently clenched.
     */
    private boolean clenched;
    /**
     * Defines how long the claw will remain clenched for.
     */
    private int clenchTicks;
    /**
     * Defines how much the claw healed during the healing phase, used to decrease the amount of points the playuer actually receives, as they cannot get extra points for no reason.
     */
    private int healedDamage;
    /**
     * A set of hits which will help determine whether or not the left claw needs to clench.
     */
    protected final Set<Hit> clenchDamage = new ObjectOpenHashSet<>();

    LeftClaw(final OlmRoom room, final Location tile) {
        super(room, 7555, tile);
    }

    @Override
    public float getXpModifier(final Hit hit) {
        calculatableDamage.add(hit);
        clenchDamage.add(hit);
        if (clenched && !room.getOlm().isPenultimatePhase()) {
            if (!hit.containsAttribute("damage has been nulled")) {
                if (hit.getSource() instanceof Player) {
                    hit.putAttribute("damage has been nulled", true);
                    hit.setDamage(0);
                    ((Player) hit.getSource()).sendMessage("The Great Olm is currently protecting its left claw! You cannot see a way to attack it successfully.");
                }
            }
            return 0;
        }
        final HitType type = hit.getHitType();
        if (type == HitType.RANGED || type == HitType.MAGIC) {
            return 1.0F / 3.5F;
        }
        return 1;
    }

    public float getPointsMultiplier(final Hit hit) {
        if (healedDamage > 0) {
            final int damage = hit.getDamage();
            final float multiplier = healedDamage >= damage ? 0 : (1 - ((float) healedDamage / damage));
            healedDamage = Math.max(0, healedDamage - damage);
            return Math.max(0, Math.min(1, multiplier));
        }
        if (clenched && hit.getSource() instanceof Player && room.getOlm().isPenultimatePhase()) {
            return 0;
        }
        return super.getPointsMultiplier(hit);
    }

    @Override
    public void applyHit(final Hit hit) {
        super.applyHit(hit);
        final HitType type = hit.getHitType();
        if (type == HitType.RANGED || type == HitType.MAGIC) {
            hit.setDamage((int) (hit.getDamage() / 3.5F));
        }
        if (clenched) {
            if (room.getOlm().isPenultimatePhase()) {
                hit.setHitType(HitType.HEALED);
                healedDamage += hit.getDamage();
            }
        }
    }

    @Override
    public void processNPC() {
        if (isDead()) {
            return;
        }
        if (clenchTicks > 0) {
            clenchTicks--;
            if (clenchTicks == 0) {
                clenched = false;
                if (room.getOlm().isPenultimatePhase()) {
                    room.sendAnimation(room.getLeftClawObject(), OlmAnimation.LEFT_CLAW_DEFAULT);
                    return;
                }
                room.sendAnimation(room.getLeftClawObject(), OlmAnimation.LEFT_CLAW_STOPPING_TWITCHING);
                WorldTasksManager.schedule(() -> room.sendAnimation(room.getLeftClawObject(), OlmAnimation.LEFT_CLAW_DEFAULT));
            }
        }
    }

    /**
     * Whether or not the claw should clench.
     *
     * @return {@code true} If the claw has received more than or equal to (20 * number of people) damage in the past 10 ticks.
     */
    boolean shouldClench() {
        final MutableInt collectiveDamage = new MutableInt();
        clenchDamage.removeIf(hit -> {
            final boolean removed = hit.getScheduleTime() + 10 < WorldThread.getCurrentCycle();
            if (!removed) {
                collectiveDamage.add(hit.getDamage());
            }
            return removed;
        });
        if (collectiveDamage.intValue() <= 0) {
            return false;
        }
        final int threshold = ScalingMechanics.getOlmClenchThreshold(room);
        return collectiveDamage.intValue() >= threshold;
    }

    @Override
    protected OlmAnimation fallAnimation() {
        return OlmAnimation.LEFT_CLAW_FALL;
    }

    @Override
    protected WorldObject clawObject() {
        return room.getLeftClawObject();
    }

    /**
     * Checks if the claw should start clenching. Claw can only clench if the right claw is available and it isn't the penultimate phase.
     */
    void checkIfClenching() {
        if (clenchTicks > 0 || room.getRightClaw() == null || room.getRightClaw().isDead() || room.getRightClaw().isFinished() || room.getOlm().isPenultimatePhase()) {
            return;
        }
        clenched = true;
        clenchTicks = 45;
        WorldTasksManager.schedule(() -> {
            clenched = true;
            clenchTicks = 45;
            room.sendAnimation(room.getLeftClawObject(), OlmAnimation.LEFT_CLAW_REGAINING_POWER);
            WorldTasksManager.schedule(() -> room.sendAnimation(room.getLeftClawObject(), OlmAnimation.LEFT_CLAW_TWITCHING), 1);
        }, 1);
    }

    /**
     * Sets the claw to the protected stage, during which any damage it takes will be healed instead.
     */
    public void setProtected() {
        clenched = true;
        displaySign(INFINITY_SIGN);
        clenchTicks = 16;
    }

    /**
     * Displays the specified sign over the left claw for a duration of three ticks.
     *
     * @param sign constant sign to display.
     */
    public void displaySign(final int sign) {
        World.sendSoundEffect(getMiddleLocation(), signSound);
        switch (sign) {
        case CRYSTAL_SIGN: 
            room.sendAnimation(room.getLeftClawObject(), OlmAnimation.LEFT_CLAW_CRYSTAL);
            break;
        case LIGHTNING_SIGN: 
            room.sendAnimation(room.getLeftClawObject(), OlmAnimation.LEFT_CLAW_LIGHTNING);
            break;
        case SWIRL_SIGN: 
            room.sendAnimation(room.getLeftClawObject(), OlmAnimation.LEFT_CLAW_SWIRL);
            break;
        default: 
            room.sendAnimation(room.getLeftClawObject(), OlmAnimation.LEFT_CLAW_INFINITY);
            return;
        }
        WorldTasksManager.schedule(() -> room.sendAnimation(room.getLeftClawObject(), OlmAnimation.LEFT_CLAW_DEFAULT), 3);
    }

    @Override
    protected void spawnClawlessObject() {
        final int side = room.getSide();
        final WorldObject defeatedClaw = new WorldObject(29885, 10, side == OlmRoom.LEFT ? OlmRoom.leftSideLeftClawObject.getRotation() : OlmRoom.rightSideLeftClawObject.getRotation(), room.getLocation(side == OlmRoom.LEFT ? OlmRoom.leftSideLeftClawObject : OlmRoom.rightSideLeftClawObject));
        World.spawnObject(defeatedClaw);
    }

    @Override
    protected void onFinish() {
        if (!isCantInteract()) {
            if (room.getRightClaw() == null || room.getRightClaw().isFinished()) {
                room.switchSide();
            }
        }
    }

    public boolean isClenched() {
        return clenched;
    }

    public int getClenchTicks() {
        return clenchTicks;
    }

    public void setClenchTicks(int clenchTicks) {
        this.clenchTicks = clenchTicks;
    }

    public Set<Hit> getClenchDamage() {
        return clenchDamage;
    }
}
