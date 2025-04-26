package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.encounter.SecondWardenEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.content.tombsofamascut.raid.TOAPathType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.InteractableEntity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Savions
 */
public class WardenPhantom extends NPC {

    public static final int ZEBAK_ID = 11774;
    public static final int BABA_ID = 11775;
    public static final int KEPHRI_ID = 11776;
    public static final int AKKHA_ID = 11777;
    private static final int ZEBAK_MAGE_PROJECTILE_LAUNCH_ID = 2181;
    private static final int ZEBAK_RANGE_PROJECTILE_LAUNCH_ID = 2187;
    private static final int RUBBLE_LANDING_SOUND_ID = 6014;
    private static final int RUBBLE_FALLING_BASE_GFX_ID = 2250;
    private static final Location FACE_LOCATION = new Location(3936, 5161, 1);
    private static final Location ZEBAK_HIDDEN_NPC_LOCATION = new Location(3938, 5156, 1);
    private static final Location ZEBAK_INITIAL_PROJECTILE_LOCATION = new Location(3941, 5159, 1);
    private static final Location KEPHRI_INITIAL_LAUNCH_LOCATION = new Location(3928, 5156, 1);
    private static final Animation ZEBAK_ATTACK_ANIMATION = new Animation(9626);
    private static final Animation AKKHA_RANGE_ANIMATION = new Animation(9772);
    private static final Animation AKKHA_MAGE_ANIMATION = new Animation(9774);
    private static final Animation AKKHA_CHANGE_ANIMATION = new Animation(9777);
    private static final Animation BABA_ATTACK_ANIMATION = new Animation(9743);
    private static final Animation KEPHRI_ATTACK_ANIMATION = new Animation(9577);
    private static final SoundEffect ZEBAK_THROW_SOUND = new SoundEffect(5823);
    private static final SoundEffect AKKHA_RANGE_IMPACT_SOUND = new SoundEffect(5640, 1, 60);
    private static final SoundEffect AKKHA_MAGE_IMPACT_SOUND = new SoundEffect(5774, 1, 60);
    private static final SoundEffect BABA_THROW_SOUND = new SoundEffect(5949);
    private static final SoundEffect KEPHRI_THROW_SOUND = new SoundEffect(6435);
    private static final SoundEffect KEPHRI_PROJECTILE_SOUND = new SoundEffect(5896, 12);
    private static final SoundEffect KEPHRI_BOMB_EXPLODE_SOUND = new SoundEffect(6475, 6);
    private static final Graphics ZEBAK_MAGE_SPLIT_GFX = new Graphics(2186, 0, 535);
    private static final Graphics ZEBAK_RANGE_SPLIT_GFX = new Graphics(2185, 0, 535);
    private static final Graphics ZEBAK_MAGE_IMPACT_GFX = new Graphics(131, 90, 90);
    private static final Graphics ZEBAK_RANGE_IMPACT_GFX = new Graphics(1103, 90, 90);
    private static final Graphics KEPHRI_INCOMING_GFX = new Graphics(1447);
    private static final Graphics KEPHRI_BOMB_EXPLODE_GFX = new Graphics(2157, 0, 38);
    private static final Projectile ZEBAK_INITIAL_RANGE_PROJECTILE = new Projectile(2178, 50, 125, 30, 20, 90, 0, 0);
    private static final Projectile ZEBAK_INITIAL_MAGE_PROJECTILE = new Projectile(2176, 50, 125, 30, 20, 90, 0, 0);
    private static final Projectile AKKHA_RANGE_PROJECTILE = new Projectile(2255, 101, 34, 0, 12, 54, 32, 0);
    private static final Projectile AKKHA_MAGE_PROJECTILE = new Projectile(2253, 101, 34, 0, 12, 54, 32, 0);
    private static final Projectile KEPHRI_INITIAL_PROJECTILE = new Projectile(1481, 175, 250, 39, 50, 51, 0, 0);
    private static final Projectile KEPHRI_SECOND_PROJECTILE = new Projectile(2266, 250, 9, 0, 50, 120, 0, 0);
    private static final int MAGE_SPLIT_SOUND_ID = 5878;
    private static final int RANGE_SPLIT_SOUND_ID = 5896;
    private final boolean stayVigilant;
    private final boolean aerialAssault;
    private final SecondWardenEncounter encounter;
    private HiddenZebakNPC hiddenZebakNPC;
    private int currentAttackTicks = 7;
    private boolean usingMage;
    private int akkhaAttacks;
    private int babaDropSpeedIncrease;

    public WardenPhantom(int id, Location tile, SecondWardenEncounter encounter) {
        super(id, tile, Direction.SOUTH, 0);
        this.encounter = encounter;
        this.stayVigilant = encounter.getParty().getPartySettings().isActive(InvocationType.STAY_VIGILANT);
        this.aerialAssault = encounter.getParty().getPartySettings().isActive(InvocationType.AERIAL_ASSAULT);
        if (id == ZEBAK_ID) {
            currentAttackTicks = 8;
            usingMage = Utils.random(1) == 0;
            hiddenZebakNPC = new HiddenZebakNPC(encounter.getLocation(ZEBAK_HIDDEN_NPC_LOCATION));
            hiddenZebakNPC.spawn();
        } else if (id == BABA_ID) {
            babaDropSpeedIncrease = Math.min(2, encounter.getParty().getBossLevels()[TOAPathType.APMEKEN.ordinal()] / 2);
        }
        setFaceLocation(encounter.getLocation(FACE_LOCATION));
        this.combat = new NPCCombat(this) {
            @Override
            public void setTarget(final Entity target, TargetSwitchCause cause) { }
            @Override
            public void forceTarget(final Entity target) { }
        };
    }

    @Override
    public void processNPC() {
        if (EncounterStage.STARTED.equals(encounter.getStage()) && currentAttackTicks > 0 && --currentAttackTicks <= 0) {
            currentAttackTicks = attack();
        }
    }

    private int attack() {
        final Player[] players = encounter.getChallengePlayers();
        if (id == ZEBAK_ID) {
            if (Utils.random(2) == 0) {
                usingMage = !usingMage;
            }
            setAnimation(ZEBAK_ATTACK_ANIMATION);
            for (Player p : players) {
                if (p != null) {
                    p.sendSound(ZEBAK_THROW_SOUND);
                    p.sendSound(new SoundEffect(usingMage ? MAGE_SPLIT_SOUND_ID : RANGE_SPLIT_SOUND_ID, 0, 120));
                }
            }
            final Location baseTile = encounter.getLocation(ZEBAK_INITIAL_PROJECTILE_LOCATION);
            World.sendProjectile(this, baseTile, usingMage ? ZEBAK_INITIAL_MAGE_PROJECTILE : ZEBAK_INITIAL_RANGE_PROJECTILE);
            WorldTasksManager.schedule(encounter.addRunningTask(() -> {
                if (!EncounterStage.STARTED.equals(encounter.getStage()) || isFinished()) {
                    return;
                }
                final Player[] updatedPlayers = encounter.getChallengePlayers();
                if (updatedPlayers.length < 1) {
                    return;
                }
                hiddenZebakNPC.setGraphics(usingMage ? ZEBAK_MAGE_SPLIT_GFX : ZEBAK_RANGE_SPLIT_GFX);
                for (Player p : updatedPlayers) {
                    if (p != null) {
                        World.sendProjectile(baseTile, p, new Projectile(usingMage ? ZEBAK_MAGE_PROJECTILE_LAUNCH_ID : ZEBAK_RANGE_PROJECTILE_LAUNCH_ID,
                                125, 22, 0, 1, 90, 0, 0));
                        p.setGraphics(usingMage ? ZEBAK_MAGE_IMPACT_GFX : ZEBAK_RANGE_IMPACT_GFX);
                    }
                }
                WorldTasksManager.schedule(encounter.addRunningTask(() -> {
                    if (!EncounterStage.STARTED.equals(encounter.getStage()) || isFinished()) {
                        return;
                    }
                    final Player[] updatedPlayers1 = encounter.getChallengePlayers();
                    final boolean mage = usingMage;
                    for (Player player : updatedPlayers1) {
                        player.scheduleHit(WardenPhantom.this, new Hit(WardenPhantom.this, Utils.random((int)
                                (20 * encounter.getParty().getDamageMultiplier())), mage ? HitType.MAGIC : HitType.RANGED),-1);
                    }
                }), 2);
            }), 3);
            return 8;
        } else if (id == AKKHA_ID) {
            if ((stayVigilant && Utils.random(3) == 0) || (!stayVigilant && ++akkhaAttacks > 3)) {
                akkhaAttacks = 0;
                setAnimation(AKKHA_CHANGE_ANIMATION);
                usingMage = !usingMage;
            } else {
                setAnimation(usingMage ? AKKHA_MAGE_ANIMATION : AKKHA_RANGE_ANIMATION);
                final boolean finalMage = usingMage;
                WorldTasksManager.schedule(encounter.addRunningTask(() -> {
                    if (!EncounterStage.STARTED.equals(encounter.getStage()) || isFinished()) {
                        return;
                    }
                    final Player[] updatedPlayers = encounter.getChallengePlayers();
                    if (updatedPlayers.length < 1) {
                        return;
                    }
                    for (Player p : players) {
                        if (p != null) {
                            World.sendProjectile(WardenPhantom.this, p, finalMage ? AKKHA_MAGE_PROJECTILE : AKKHA_RANGE_PROJECTILE);
                            p.sendSound(finalMage ? AKKHA_MAGE_IMPACT_SOUND : AKKHA_RANGE_IMPACT_SOUND);
                            CombatUtilities.delayHit(WardenPhantom.this, 1, p, new Hit(WardenPhantom.this, Utils.random((int)
                                    (20 * encounter.getParty().getDamageMultiplier())), finalMage ? HitType.MAGIC : HitType.RANGED));
                        }
                    }
                }), 1);
            }
            return 7;
        } else if (id == BABA_ID) {
            setAnimation(BABA_ATTACK_ANIMATION);
            final List<Location> tiles = new ArrayList<>();
            for (Player p : players) {
                if (p != null) {
                    p.sendSound(BABA_THROW_SOUND);
                    p.sendSound(new SoundEffect(RUBBLE_LANDING_SOUND_ID, 1, 150 - (babaDropSpeedIncrease * 60)));
                    final Location pTile = new Location(p.getLocation());
                    if (!tiles.contains(pTile)) {
                        tiles.add(pTile);
                    }
                }
            }
            tiles.forEach(loc -> World.sendGraphics(new Graphics(RUBBLE_FALLING_BASE_GFX_ID + babaDropSpeedIncrease, 20, 0), loc));
            WorldTasksManager.schedule(encounter.addRunningTask(() -> {
                if (!EncounterStage.STARTED.equals(encounter.getStage()) || isFinished()) {
                    return;
                }
                final Player[] updatedPlayers = encounter.getChallengePlayers();
                if (updatedPlayers.length < 1) {
                    return;
                }
                tiles.forEach(loc -> {
                    for (Player p : updatedPlayers) {
                        if (p != null && loc.equals(p.getLocation())) {
                            p.applyHit(new Hit(this, (int) (19 * encounter.getParty().getDamageMultiplier()) + Utils.random(5), HitType.DEFAULT));
                        }
                    }
                });
            }), 5 - (babaDropSpeedIncrease * 2));
            return 7 - babaDropSpeedIncrease;
        } else {
            setAnimation(KEPHRI_ATTACK_ANIMATION);
            for (Player p : players) {
                if (p != null) {
                    p.sendSound(KEPHRI_THROW_SOUND);
                }
            }
            final Location baseTile = encounter.getLocation(KEPHRI_INITIAL_LAUNCH_LOCATION);
            World.sendProjectile(this, baseTile, KEPHRI_INITIAL_PROJECTILE);
            WorldTasksManager.schedule(encounter.addRunningTask(() -> {
                if (!EncounterStage.STARTED.equals(encounter.getStage()) || isFinished()) {
                    return;
                }
                final Player[] updatedPlayers = encounter.getChallengePlayers();
                if (updatedPlayers.length < 1) {
                    return;
                }
                World.sendSoundEffect(baseTile, KEPHRI_PROJECTILE_SOUND);
                final List<Location> tiles = new ArrayList<>();
                final List<Location> projectileTiles = new ArrayList<>();
                for (Player p : updatedPlayers) {
                    if (p != null) {
                        final Location pLocation = new Location(p.getLocation());
                        if (!projectileTiles.contains(pLocation)) {
                            projectileTiles.add(pLocation);
                        }
                        if (aerialAssault) {
                            for (int dx = -1; dx <= 1; dx++) {
                                for (int dy = -1; dy <= 1; dy++) {
                                    final Location tLocation = pLocation.transform(dx, dy);
                                    if (!tiles.contains(tLocation)) {
                                        tiles.add(tLocation);
                                    }
                                }
                            }
                        } else if (!tiles.contains(pLocation)) {
                            tiles.add(pLocation);
                        }
                    }
                }
                tiles.forEach(loc -> World.sendGraphics(KEPHRI_INCOMING_GFX, loc));
                projectileTiles.forEach(loc -> World.sendProjectile(baseTile, loc, KEPHRI_SECOND_PROJECTILE));
                WorldTasksManager.schedule(encounter.addRunningTask(() -> {
                    if (!EncounterStage.STARTED.equals(encounter.getStage()) || isFinished()) {
                        return;
                    }
                    final Player[] updatedPlayers2 = encounter.getChallengePlayers();
                    if (updatedPlayers2.length < 1) {
                        return;
                    }
                    tiles.forEach(loc -> {
                        if (World.isFloorFree(loc, 1)) {
                            for (Player p : updatedPlayers2) {
                                if (p != null && loc.equals(p.getLocation())) {
                                    p.applyHit(new Hit(WardenPhantom.this, (int) (20 * encounter.getParty().getDamageMultiplier()) + Utils.random(3), HitType.MAGIC));
                                }
                            }
                            World.sendGraphics(KEPHRI_BOMB_EXPLODE_GFX, loc);
                        }
                    });
                    projectileTiles.forEach(loc -> World.sendSoundEffect(loc, KEPHRI_BOMB_EXPLODE_SOUND));
                }), 3);
            }), 2);
            return 7;
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (hiddenZebakNPC != null && !hiddenZebakNPC.isFinished()) {
            hiddenZebakNPC.finish();
        }
    }

    @Override
    public void setUnprioritizedAnimation(Animation animation) {

    }

    @Override
    public double getMagicPrayerMultiplier() {
        return id == AKKHA_ID ? .15 : (id == KEPHRI_ID ? .75 : super.getMagicPrayerMultiplier());
    }

    @Override
    public double getRangedPrayerMultiplier() {
        return id == AKKHA_ID ? .15 : super.getRangedPrayerMultiplier();
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

    static class HiddenZebakNPC extends NPC {

        public HiddenZebakNPC(Location tile) {
            super(11744, tile, Direction.SOUTH, 0);
        }

        @Override public void setRespawnTask() {

        }

        @Override public void setTarget(Entity target, TargetSwitchCause cause) {}

        @Override public void setFaceEntity(Entity entity) {}

        @Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

        @Override public void setFaceLocation(Location tile) {}

        @Override public boolean isEntityClipped() { return false; }

        @Override public boolean isValidAnimation(int animID) { return true; }

        @Override
        public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) { return false; }
    }
}
