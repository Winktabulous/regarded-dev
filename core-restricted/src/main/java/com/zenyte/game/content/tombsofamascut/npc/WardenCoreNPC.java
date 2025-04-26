package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.WardenEncounter;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RemoveHitBar;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions
 */
public class WardenCoreNPC extends NPC implements IWardenCore {

    private static final int ID = 11771;
    private static final SoundEffect LANDING_SOUND = new SoundEffect(6201, 15);
    private final WardenEncounter encounter;
    private final MovingWardenNPC movingWardenNPC;
    private int aliveTicks;

    public WardenCoreNPC(Location tile, Direction facing, int aliveTicks, WardenEncounter encounter, MovingWardenNPC movingWardenNPC) {
        super(ID, tile, facing, 0);
        this.aliveTicks = aliveTicks;
        this.encounter = encounter;
        this.movingWardenNPC = movingWardenNPC;
        World.sendSoundEffect(location, LANDING_SOUND);
        super.hitBar = new RemoveHitBar(super.hitBar.getType());
        this.combat = new NPCCombat(this) {
            @Override
            public void setTarget(final Entity target, TargetSwitchCause cause) { }
            @Override
            public void forceTarget(final Entity target) { }
        };
    }

    @Override
    public void handleIngoingHit(Hit hit) {
        super.handleIngoingHit(hit);
        movingWardenNPC.applyHit(new Hit(this, hit.getDamage() * 5, HitType.DEFAULT));
    }

    @Override public void processNPC() {
        if (--aliveTicks <= 0 && !isFinished() && encounter.getPhase() == 1) {
            encounter.sendCoreBack();
        }
    }

    @Override public boolean isForceAttackable() { return true; }

    @Override public boolean canAttack(Player source) {
        return !encounter.isStoned(source);
    }

    @Override public void sendDeath() {
        if (!isFinished()) {
            encounter.sendCoreBack();
        }
    }

    @Override
    public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

    @Override public boolean isEntityClipped() { return false; }

    @Override public void setRespawnTask() {}

    @Override public void setTarget(Entity target, TargetSwitchCause cause) {}

    @Override public void setFaceEntity(Entity entity) {}
}
