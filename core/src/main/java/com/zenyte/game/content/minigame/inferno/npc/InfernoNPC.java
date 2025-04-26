package com.zenyte.game.content.minigame.inferno.npc;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.model.InfernoMechanics;
import com.zenyte.game.content.minigame.inferno.model.InfernoWave;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackDefinitions;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCCollidingEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 29/11/2019 | 21:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class InfernoNPC extends NPC implements InfernoMechanics, CombatScript {
    public transient Inferno inferno;

    public InfernoNPC(final int id, final Location location, final Inferno inferno) {
        super(id, location, Direction.SOUTH, 16);
        this.inferno = inferno;
        setMaxDistance(64);
        setAggressionDistance(64);
        setForceAggressive(true);
        supplyCache = false;
        inferno.add(this);
        randomWalkDelay = Integer.MAX_VALUE >> 1;
        combat = new NPCCombat(this) {
            @Override
            protected boolean checkAll() {
                if (target == null) return false;
                if (target.isFinished() || npc.isDead() || npc.isFinished() || target.getLocation().getDistance(getLocation()) >= 64 || (target.getNextLocation() != null && target.getNextLocation().getDistance(getLocation()) >= 64)) {
                    return false;
                }
                if (target.isDead() || npc.isMovementRestricted()) {
                    return true;
                }
                if (colliding()) {
                    //TODO: Change into a more efficent pathfinding formula or write a non-pf structure.
                    npc.setRouteEvent(new NPCCollidingEvent(npc, new EntityStrategy(target)));
                    return true;
                }
                return appendMovement();
            }
            @Override
            public int combatAttack() {
                if (target == null || target.isDead()) {
                    return 0;
                }
                return super.combatAttack();
            }
        };
    }

    @Override
    public boolean isProjectileClipped(final Position target, final boolean closeProximity) {
        if (inferno.getWave() == InfernoWave.WAVE_69) {
            return false;
        }
        return super.isProjectileClipped(target, closeProximity);
    }

    @Override
    public boolean applyDamageFromHitsAfterDeath() {
        return true;
    }

    @Override
    public NPC spawn() {
        final NPC npc = super.spawn();
        onSpawn();
        this.resetWalkSteps();
        return npc;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public void setRespawnTask() {
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        inferno.check(this, source);
    }

    @Override
    public void playAttackSound(final Entity target) {
        final AttackDefinitions attDefs = combatDefinitions.getAttackDefinitions();
        final SoundEffect sound = attDefs.getStartSound();
        if (sound != null) {
            inferno.playSound(sound);
        }
    }

    @Override
    public int attack(final Entity target) {
        playAttackSound(inferno.getPlayer());
        if (target instanceof Player) {
            return attack((Player) target);
        }
        return combatDefinitions.getAttackSpeed();
    }
}
