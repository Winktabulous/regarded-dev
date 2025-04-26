package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RemoveHitBar;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions
 */
public class EnergySiphonNPC extends NPC {

    private static final int ID = 11772;
    private static final int OBJECT_BLOCK_ID = 26209;
    private static final SoundEffect LANDING_SOUND = new SoundEffect(6159, 7);
    private static final SoundEffect SIPHON_SOUND = new SoundEffect(6230, 7);
    private static final Projectile SIPHON_PROJECTILE = new Projectile(2227, 100, 5, 0, 0, 90, 70, 0);
    private final FlyingWardenNPC wardenNPC;
    private final WorldObject blockObject;
    private final int index;
    private int siphonTicks = 1;

    public EnergySiphonNPC(Location tile, FlyingWardenNPC flyingWardenNPC, int hitpoints, int index) {
        super(ID, tile, Direction.SOUTH, 0);
        this.wardenNPC = flyingWardenNPC;
        getCombatDefinitions().setHitpoints(hitpoints);
        setHitpoints(hitpoints);
        this.index = index;
        World.sendSoundEffect(getLocation(), LANDING_SOUND);
        World.spawnObject(blockObject = new WorldObject(OBJECT_BLOCK_ID, 10, 0, getLocation()));
        this.combat = new NPCCombat(this) {
            @Override
            public void setTarget(final Entity target, TargetSwitchCause cause) { }
            @Override
            public void forceTarget(final Entity target) { }
        };
    }

    @Override
    public void listenScheduleHit(Hit hit) {
        if (hit != null){
            if (!HitType.MELEE.equals(hit.getHitType())) {
                hit.setDamage(0);
            } else if (hit.getSource() != null && hit.getSource() instanceof Player player) {
                hit.setDamage(1);
                player.getActionManager().forceStop();
                player.getActionManager().setActionDelay(0);
                player.getActionManager().preventDelaySet();
            }
        }
        super.listenScheduleHit(hit);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (!isDead()) {
            getHitBars().clear();
            getHitBars().add(super.hitBar);
            getUpdateFlags().flag(UpdateFlag.HIT);
        }
        if (!isFinished() && wardenNPC != null && wardenNPC.isSkullAttack() && --siphonTicks <= 0) {
            siphonTicks = 2;
            World.sendProjectile(this, wardenNPC, SIPHON_PROJECTILE);
            World.sendSoundEffect(getLocation(), SIPHON_SOUND);
        }
    }

    @Override
    public void sendDeath() {
        getHitBars().clear();
        getHitBars().add(new RemoveHitBar(hitBar.getType()));
        getUpdateFlags().flag(UpdateFlag.HIT);
        if (wardenNPC != null) {
            wardenNPC.markSkullDone(index);
        }
    }

    @Override
    public void finish() {
        super.finish();
        World.removeObject(blockObject);
    }

    @Override
    public void setUnprioritizedAnimation(Animation animation) {

    }

    @Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
        return false;
    }

    @Override public void setRespawnTask() {}

    @Override public void setTarget(Entity target, TargetSwitchCause cause) {}

    @Override public void setFaceEntity(Entity entity) {}

    @Override public boolean checkProjectileClip(Player player, boolean melee) {
        return false;
    }

    @Override
    public boolean isCycleHealable() { return false; }
}
