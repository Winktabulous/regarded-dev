package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.WardenEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import mgi.types.config.HitbarDefinitions;

/**
 * @author Savions
 */
public class StaticWardenNPC extends TOANPC {

    private static final int PLATFORM_OBJECT_ID = 45606;
    private static final Animation SPECIAL_ANIMATION = new Animation(9667);
    private static final Animation DEATH_ANIMATION = new Animation(9669);
    private static final Graphics FIRST_SPECIAL_GFX = new Graphics(2214);
    private static final Graphics SECOND_SPECIAL_GFX = new Graphics(2215);
    private static final Graphics DEATH_GFX = new Graphics(2216);
    private static final Graphics DEATH_LEGS_GFX = new Graphics(2217);
    private static final SoundEffect[] SPECIAL_SOUNDS = {new SoundEffect(6142, 0, 2), new SoundEffect(6085, 0, 40),
            new SoundEffect(6043, 0, 52), new SoundEffect(6155, 0, 60), new SoundEffect(6226, 0, 90),
            new SoundEffect(6061, 0, 99), new SoundEffect(6220, 0, 101), new SoundEffect(6261, 0, 159)};
    private final WardenEncounter encounter;
    private int platformPercentage;
    private int chargeTicks = 3;
    private int fullCharges;
    private boolean sendLegsGfx;

    private HitBar platformHitbar = new HitBar() {
        @Override
        public int getType() {
            return 36;
        }

        @Override
        public int getPercentage() {
            final int multiplier = getMultiplier();
            final float mod = 100F / (multiplier);
            return Math.min((int) ((platformPercentage + mod) / mod), multiplier);
        }

        public int getMultiplier() {
            final int type = getType();
            return HitbarDefinitions.get(type).getSize();
        }
    };

    public StaticWardenNPC(int id, Location tile, Direction facing, WardenEncounter encounter) {
        super(id, tile, facing, 64, encounter);
        this.encounter = encounter;
        setCantInteract(true);
        spawnPlatformObject();
        super.hitBar = platformHitbar;
        getUpdateFlags().flag(UpdateFlag.HIT);
    }

    private void spawnPlatformObject() {
        World.spawnObject(new WorldObject(PLATFORM_OBJECT_ID + (platformPercentage / 5), 10,
                Direction.WEST.equals(getSpawnDirection()) ? 1 : 3, getLocation()));
    }

    public void charge(int charge) {
        platformPercentage = Math.min(100, platformPercentage + charge);
        spawnPlatformObject();
        if (platformPercentage == 100) {
            setAnimation(SPECIAL_ANIMATION);
            final Player[] players = encounter.getChallengePlayers();
            for (Player p : players) {
                if (p != null) {
                    for (SoundEffect sound : SPECIAL_SOUNDS) {
                        p.sendSound(sound);
                    }
                }
            }
            final boolean isElidinis = id == WardenEncounter.ELIDINIS_NPC_ID || id == WardenEncounter.ELIDINIS_NPC_ID + 2;
            if (fullCharges % 2 == 0) {
                setGraphics(FIRST_SPECIAL_GFX);
                encounter.sendRotatingTraps(isElidinis);
            } else {
                setGraphics(SECOND_SPECIAL_GFX);
                encounter.sendFirstPhaseProjectile(isElidinis);
            }
            fullCharges++;
            platformPercentage = 0;
        }
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (encounter != null && EncounterStage.STARTED.equals(encounter.getStage())) {
            if (encounter.getPhase() == 0) {
                if (chargeTicks > 0 && --chargeTicks <= 0) {
                    charge(2);
                    chargeTicks = encounter.isAncientHaste() ? 1 : 2;
                }
                getHitBars().clear();
                getHitBars().add(super.hitBar);
                getUpdateFlags().flag(UpdateFlag.HIT);
            } else if (sendLegsGfx) {
                setGraphics(DEATH_LEGS_GFX);
            }
        }
    }

    public void removeObeliskHitBar() {
        getHitBars().clear();
        getHitBars().add(new RemoveHitBar(hitBar.getType()));
        getUpdateFlags().flag(UpdateFlag.HIT);
        super.hitBar = new EntityHitBar(this) {
            @Override
            public int getType() {
                return 21;
            }
        };
    }

    @Override
    public void sendDeath() {
        setAnimation(DEATH_ANIMATION);
        setGraphics(DEATH_GFX);
    }

    public void setSendLegsGfx() {
        sendLegsGfx = true;
    }

    public int getTotalCharged() {
        return fullCharges * 100 + platformPercentage;
    }

    @Override
    public float getPointMultiplier() {
        return 2F;
    }

    @Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
        return false;
    }

    @Override public void setRespawnTask() {}

    @Override public void setTarget(Entity target, TargetSwitchCause cause) {}

    @Override public void setFaceEntity(Entity entity) {}

    @Override
    public boolean isCycleHealable() { return false; }
}
