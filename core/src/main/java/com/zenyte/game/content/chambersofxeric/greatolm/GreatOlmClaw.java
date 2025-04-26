package com.zenyte.game.content.chambersofxeric.greatolm;

import com.zenyte.game.content.chambersofxeric.ScalingMechanics;
import com.zenyte.game.content.chambersofxeric.npc.RaidNPC;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;

/**
 * @author Kris | 21/09/2019 21:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class GreatOlmClaw extends RaidNPC<OlmRoom> {
    GreatOlmClaw(final OlmRoom room, final int id, final Location tile) {
        super(room.getRaid(), room, id, tile);
        combat = new ClawCombatHandler(this);
        setForceMultiArea(true);
        setForceAggressive(true);
        setAggressionDistance(20);
    }

    /**
     * A set of hits since the last attack by the Great Olm. Used for purposes of Olm head turning.
     */
    protected final Set<Hit> calculatableDamage = new ObjectOpenHashSet<>();
    /**
     * Whether or not the claw has already regenerated at least once.
     */
    protected boolean regenerated;

    @Override
    public boolean grantPoints() {
        return !regenerated;
    }

    @Override
    protected void setStats() {
        super.setStats();
        hitpointsMultiplier = ScalingMechanics.getOlmClawHpMulitplier(raid);
        combatDefinitions.setHitpoints(ScalingMechanics.getOlmClawHp(raid));
        setHitpoints(combatDefinitions.getHitpoints());
    }

    /**
     * Clears all calculateable damage and returns whether or not there was any damage to be cleared.
     * @return
     */
    boolean clearCalculatableDamage() {
        final boolean contains = !calculatableDamage.isEmpty();
        calculatableDamage.clear();
        return contains;
    }

    /**
     * The animation for the claw falling back into its hole.
     * @return the respective claw's animation.
     */
    protected abstract OlmAnimation fallAnimation();

    /**
     * The game object underneath the claw NPC itself.
     * @return the game object, used for graphical purposes.
     */
    protected abstract WorldObject clawObject();

    /**
     * An abstract method that executes when the claw falls, spawning a rock without any visible claw in it.
     */
    protected abstract void spawnClawlessObject();

    /**
     * Extra bits executed when the death sequence starts.
     */
    protected void onDeath() {
    }

    /**
     * Extra bits executed when the death sequence ends.
     */
    protected abstract void onFinish();

    @Override
    public void sendDeath() {
        if (isCantInteract()) {
            return;
        }
        resetWalkSteps();
        combat.removeTarget();
        onDeath();
        if (room.getOlm().isPenultimatePhase() && room.getOlm().getRestoreStage() == 0) {
            setCantInteract(true);
            room.getOlm().setRestoreStage(1);
            room.sendAnimation(clawObject(), fallAnimation());
            room.getOlm().scheduleClawRestoreTask(this);
            return;
        }
        if (room.getOlm().getRestoreStage() == 1) {
            room.getOlm().setRestoreStage(2);
        }
        performDeath();
    }

    /**
     * Starts the death sequence, ending with the claw falling.
     */
    private final void performDeath() {
        WorldTasksManager.schedule(new WorldTask() {
            private int loop;
            @Override
            public void run() {
                if (room.getRaid().isDestroyed()) {
                    stop();
                    return;
                }
                if (loop == 0) {
                    room.sendAnimation(clawObject(), fallAnimation());
                    finish();
                } else if (loop == 1) {
                    spawnClawlessObject();
                    reset();
                    stop();
                    onFinish();
                }
                loop++;
            }
        }, 0, 1);
    }

    @Override
    public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
        return false;
    }

    @Override
    public boolean checkProjectileClip(final Player player, boolean melee) {
        return false;
    }

    public Set<Hit> getCalculatableDamage() {
        return calculatableDamage;
    }

    public void setRegenerated(boolean regenerated) {
        this.regenerated = regenerated;
    }
}
