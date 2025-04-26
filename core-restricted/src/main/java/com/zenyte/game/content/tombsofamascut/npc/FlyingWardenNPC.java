package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.SecondWardenEncounter;
import com.zenyte.game.content.tombsofamascut.encounter.WardenEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.EntityHitBar;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Savions
 */
public class FlyingWardenNPC extends TOANPC {

    public static final int ELIDINIS_ID = 11761;
    public static final int TUMEKEN_ID = 11762;
    private static final int FLOOR_ATTACK_BASE_ANIM_ID = 9674;
    private static final int BASE_FLOOR_GFX_ID = 2220;
    private static final Animation SEND_SKULL_START_ANIMATION = new Animation(9682);
    private static final Animation SKULL_RETURN_ANIMATION = new Animation(9680);
    private static final Animation SKULL_FAILURE_ANIMATION = new Animation(9681);
    private static final Animation EXPLODING_ANIMATION = new Animation(9685);
    private static final SoundEffect SEND_SKULL_SOUND = new SoundEffect(3614, 14);
    private static final SoundEffect SKULL_FAILURE_SOUND = new SoundEffect(6179);
    private static final SoundEffect[] SEND_SKULL_RETURN_SOUNDS = {new SoundEffect(6183, 1, 20), new SoundEffect(6038, 0, 29),
            new SoundEffect(6110, 0, 43), new SoundEffect(6243, 1, 43), new SoundEffect(6215, 1, 90), new SoundEffect(6115, 0, 158)};
    private static final Graphics WARDEN_GFX = new Graphics(2219);
    private static final Graphics SKULL_LANDING_GFX = new Graphics(1447);
    private static final Graphics EXPLOSION_GFX = new Graphics(2158);
    private static final Projectile SKULL_SEND_PROJECTILE = new Projectile(2226, 87, 20, 30, 20, 90, 0, 0);
    private static final Projectile SKULL_RETURN_PROJECTILE = new Projectile(2226, 20, 87, 0, 20, 58, 0, 0);
    private static final Location FIRST_PHANTOM_SPAWN_LOCATION = new Location(3943, 5153, 1);
    private static final Location SECOND_PHANTOM_SPAWN_LOCATION = new Location(3925, 5153, 1);
    private static final Location FLOOR_BASE_LOCATION = new Location(3936, 5157, 1);
    private static final Location[][][] REGULAR_FLOOR_ATTACK_LOCATIONS = {
            {
                {new Location(3939, 5157, 1), new Location(3941, 5158, 1), new Location(3937, 5159, 1), new Location(3941, 5159, 1), new Location(3936, 5160, 1), new Location(3939, 5160, 1), new Location(3936, 5161, 1), new Location(3937, 5161, 1), new Location(3940, 5163, 1), new Location(3942, 5163, 1), new Location(3936, 5164, 1), new Location(3941, 5164, 1), new Location(3937, 5165, 1), new Location(3939, 5165, 1), new Location(3940, 5165, 1), new Location(3945, 5162, 1), new Location(3945, 5163, 1), new Location(3945, 5164, 1), new Location(3946, 5165, 1)},
                {new Location(3936, 5157, 1), new Location(3937, 5157, 1), new Location(3936, 5158, 1), new Location(3939, 5158, 1), new Location(3936, 5159, 1), new Location(3938, 5159, 1), new Location(3939, 5159, 1), new Location(3940, 5159, 1), new Location(3938, 5160, 1), new Location(3940, 5160, 1), new Location(3942, 5160, 1), new Location(3941, 5161, 1), new Location(3942, 5162, 1), new Location(3941, 5163, 1), new Location(3940, 5164, 1), new Location(3942, 5164, 1), new Location(3936, 5165, 1), new Location(3941, 5165, 1), new Location(3943, 5165, 1), new Location(3944, 5161, 1), new Location(3944, 5162, 1), new Location(3944, 5163, 1), new Location(3944, 5165, 1), new Location(3945, 5165, 1)},
                {new Location(3940, 5157, 1), new Location(3937, 5158, 1), new Location(3942, 5159, 1), new Location(3937, 5160, 1), new Location(3943, 5160, 1), new Location(3938, 5161, 1), new Location(3939, 5161, 1), new Location(3940, 5161, 1), new Location(3938, 5162, 1), new Location(3939, 5162, 1), new Location(3940, 5162, 1), new Location(3941, 5162, 1), new Location(3943, 5162, 1), new Location(3938, 5163, 1), new Location(3939, 5163, 1), new Location(3943, 5163, 1), new Location(3938, 5164, 1), new Location(3938, 5165, 1), new Location(3946, 5163, 1), new Location(3944, 5164, 1)},
                {new Location(3938, 5157, 1), new Location(3938, 5158, 1), new Location(3940, 5158, 1), new Location(3941, 5160, 1), new Location(3942, 5161, 1), new Location(3943, 5161, 1), new Location(3936, 5162, 1), new Location(3937, 5162, 1), new Location(3936, 5163, 1), new Location(3937, 5163, 1), new Location(3937, 5164, 1), new Location(3939, 5164, 1), new Location(3943, 5164, 1), new Location(3942, 5165, 1), new Location(3946, 5164, 1)}
            },
            {
                {new Location(3926, 5163, 1), new Location(3927, 5164, 1), new Location(3926, 5165, 1), new Location(3927, 5165, 1), new Location(3934, 5157, 1), new Location(3933, 5158, 1), new Location(3933, 5159, 1), new Location(3929, 5160, 1), new Location(3928, 5161, 1), new Location(3933, 5161, 1), new Location(3934, 5161, 1), new Location(3935, 5161, 1), new Location(3928, 5162, 1), new Location(3930, 5162, 1), new Location(3932, 5162, 1), new Location(3934, 5162, 1), new Location(3928, 5163, 1), new Location(3934, 5163, 1), new Location(3932, 5164, 1), new Location(3934, 5164, 1), new Location(3935, 5164, 1), new Location(3932, 5165, 1), new Location(3933, 5165, 1), new Location(3936, 5157, 1), new Location(3936, 5158, 1), new Location(3936, 5160, 1), new Location(3936, 5163, 1)},
                {new Location(3927, 5162, 1), new Location(3927, 5163, 1), new Location(3932, 5158, 1), new Location(3935, 5159, 1), new Location(3930, 5160, 1), new Location(3931, 5160, 1), new Location(3933, 5160, 1), new Location(3929, 5161, 1), new Location(3931, 5162, 1), new Location(3933, 5162, 1), new Location(3930, 5163, 1), new Location(3931, 5163, 1), new Location(3932, 5163, 1), new Location(3933, 5163, 1), new Location(3935, 5163, 1), new Location(3928, 5164, 1), new Location(3929, 5164, 1), new Location(3931, 5164, 1), new Location(3928, 5165, 1), new Location(3929, 5165, 1), new Location(3931, 5165, 1), new Location(3934, 5165, 1), new Location(3936, 5159, 1), new Location(3936, 5161, 1), new Location(3936, 5164, 1)},
                {new Location(3926, 5164, 1), new Location(3932, 5157, 1), new Location(3933, 5157, 1), new Location(3934, 5158, 1), new Location(3930, 5159, 1), new Location(3931, 5159, 1), new Location(3932, 5160, 1), new Location(3934, 5160, 1), new Location(3935, 5160, 1), new Location(3930, 5161, 1), new Location(3932, 5161, 1), new Location(3930, 5164, 1), new Location(3936, 5162, 1), new Location(3936, 5165, 1)},
                {new Location(3935, 5157, 1), new Location(3931, 5158, 1), new Location(3935, 5158, 1), new Location(3932, 5159, 1), new Location(3934, 5159, 1), new Location(3931, 5161, 1), new Location(3929, 5162, 1), new Location(3935, 5162, 1), new Location(3929, 5163, 1), new Location(3933, 5164, 1), new Location(3930, 5165, 1), new Location(3935, 5165, 1)}
            },
            {
                {new Location(3926, 5164, 1), new Location(3933, 5157, 1), new Location(3934, 5157, 1), new Location(3933, 5158, 1), new Location(3930, 5159, 1), new Location(3934, 5159, 1), new Location(3928, 5161, 1), new Location(3932, 5161, 1), new Location(3929, 5162, 1), new Location(3931, 5162, 1), new Location(3932, 5162, 1), new Location(3935, 5162, 1), new Location(3931, 5164, 1), new Location(3932, 5165, 1), new Location(3934, 5165, 1), new Location(3935, 5165, 1), new Location(3938, 5157, 1), new Location(3939, 5157, 1), new Location(3938, 5158, 1), new Location(3937, 5160, 1), new Location(3941, 5160, 1), new Location(3937, 5161, 1), new Location(3938, 5162, 1), new Location(3941, 5162, 1), new Location(3942, 5162, 1), new Location(3939, 5163, 1), new Location(3940, 5163, 1), new Location(3937, 5164, 1), new Location(3942, 5164, 1), new Location(3939, 5165, 1)},
                {new Location(3927, 5164, 1), new Location(3932, 5157, 1), new Location(3931, 5158, 1), new Location(3935, 5158, 1), new Location(3932, 5159, 1), new Location(3933, 5159, 1), new Location(3931, 5160, 1), new Location(3933, 5160, 1), new Location(3935, 5160, 1), new Location(3931, 5161, 1), new Location(3928, 5163, 1), new Location(3930, 5163, 1), new Location(3928, 5164, 1), new Location(3932, 5164, 1), new Location(3933, 5164, 1), new Location(3934, 5164, 1), new Location(3928, 5165, 1), new Location(3929, 5165, 1), new Location(3939, 5158, 1), new Location(3939, 5159, 1), new Location(3940, 5159, 1), new Location(3941, 5159, 1), new Location(3939, 5161, 1), new Location(3943, 5163, 1), new Location(3941, 5164, 1), new Location(3944, 5163, 1), new Location(3945, 5164, 1), new Location(3945, 5165, 1), new Location(3946, 5165, 1)},
                {new Location(3927, 5162, 1), new Location(3926, 5163, 1), new Location(3927, 5165, 1), new Location(3935, 5157, 1), new Location(3931, 5159, 1), new Location(3935, 5159, 1), new Location(3929, 5160, 1), new Location(3932, 5160, 1), new Location(3933, 5161, 1), new Location(3935, 5161, 1), new Location(3930, 5162, 1), new Location(3933, 5162, 1), new Location(3929, 5163, 1), new Location(3931, 5163, 1), new Location(3933, 5163, 1), new Location(3935, 5163, 1), new Location(3935, 5164, 1), new Location(3933, 5165, 1), new Location(3937, 5157, 1), new Location(3940, 5158, 1), new Location(3937, 5159, 1), new Location(3942, 5159, 1), new Location(3940, 5160, 1), new Location(3942, 5160, 1), new Location(3938, 5161, 1), new Location(3941, 5161, 1), new Location(3943, 5161, 1), new Location(3937, 5162, 1), new Location(3940, 5162, 1), new Location(3937, 5163, 1), new Location(3938, 5163, 1), new Location(3941, 5163, 1), new Location(3938, 5164, 1), new Location(3943, 5164, 1), new Location(3941, 5165, 1), new Location(3943, 5165, 1), new Location(3944, 5161, 1), new Location(3945, 5162, 1), new Location(3944, 5164, 1), new Location(3944, 5165, 1)},
                {new Location(3927, 5163, 1), new Location(3926, 5165, 1), new Location(3932, 5158, 1), new Location(3934, 5158, 1), new Location(3930, 5160, 1), new Location(3934, 5160, 1), new Location(3929, 5161, 1), new Location(3930, 5161, 1), new Location(3934, 5161, 1), new Location(3928, 5162, 1), new Location(3934, 5162, 1), new Location(3932, 5163, 1), new Location(3934, 5163, 1), new Location(3929, 5164, 1), new Location(3930, 5164, 1), new Location(3930, 5165, 1), new Location(3931, 5165, 1), new Location(3940, 5157, 1), new Location(3937, 5158, 1), new Location(3941, 5158, 1), new Location(3938, 5159, 1), new Location(3938, 5160, 1), new Location(3939, 5160, 1), new Location(3943, 5160, 1), new Location(3940, 5161, 1), new Location(3942, 5161, 1), new Location(3939, 5162, 1), new Location(3943, 5162, 1), new Location(3942, 5163, 1), new Location(3939, 5164, 1), new Location(3940, 5164, 1), new Location(3937, 5165, 1), new Location(3938, 5165, 1), new Location(3940, 5165, 1), new Location(3942, 5165, 1), new Location(3944, 5162, 1), new Location(3945, 5163, 1), new Location(3946, 5163, 1), new Location(3946, 5164, 1)}
            },
    };
    private static final Location[][] FULL_FLOOR__ATTACK_LOCATIONS = {
        {new Location(3926, 5164, 1), new Location(3927, 5165, 1), new Location(3935, 5159, 1), new Location(3929, 5160, 1), new Location(3933, 5160, 1), new Location(3928, 5163, 1), new Location(3930, 5164, 1), new Location(3936, 5158, 1), new Location(3940, 5158, 1), new Location(3939, 5159, 1), new Location(3936, 5160, 1), new Location(3938, 5160, 1), new Location(3940, 5160, 1), new Location(3942, 5160, 1), new Location(3942, 5161, 1), new Location(3943, 5161, 1), new Location(3942, 5162, 1), new Location(3943, 5162, 1), new Location(3936, 5163, 1), new Location(3941, 5163, 1), new Location(3942, 5163, 1), new Location(3941, 5164, 1), new Location(3942, 5164, 1), new Location(3942, 5165, 1), new Location(3944, 5162, 1), new Location(3945, 5162, 1), new Location(3945, 5164, 1), new Location(3944, 5165, 1)},
        {new Location(3926, 5163, 1), new Location(3927, 5164, 1), new Location(3933, 5158, 1), new Location(3934, 5158, 1), new Location(3931, 5159, 1), new Location(3933, 5159, 1), new Location(3934, 5160, 1), new Location(3935, 5161, 1), new Location(3929, 5162, 1), new Location(3931, 5162, 1), new Location(3932, 5162, 1), new Location(3934, 5162, 1), new Location(3931, 5163, 1), new Location(3932, 5163, 1), new Location(3933, 5163, 1), new Location(3931, 5164, 1), new Location(3933, 5164, 1), new Location(3935, 5164, 1), new Location(3928, 5165, 1), new Location(3931, 5165, 1), new Location(3932, 5165, 1), new Location(3933, 5165, 1), new Location(3936, 5157, 1), new Location(3937, 5157, 1), new Location(3938, 5157, 1), new Location(3939, 5157, 1), new Location(3939, 5158, 1), new Location(3936, 5159, 1), new Location(3940, 5159, 1), new Location(3942, 5159, 1), new Location(3937, 5161, 1), new Location(3941, 5161, 1), new Location(3936, 5162, 1), new Location(3939, 5162, 1), new Location(3941, 5162, 1), new Location(3937, 5163, 1), new Location(3938, 5163, 1), new Location(3939, 5163, 1), new Location(3940, 5163, 1), new Location(3939, 5164, 1), new Location(3943, 5164, 1), new Location(3938, 5165, 1), new Location(3944, 5161, 1), new Location(3944, 5163, 1), new Location(3946, 5163, 1)},
        {new Location(3927, 5162, 1), new Location(3927, 5163, 1), new Location(3926, 5165, 1), new Location(3932, 5157, 1), new Location(3934, 5157, 1), new Location(3935, 5157, 1), new Location(3931, 5158, 1), new Location(3932, 5158, 1), new Location(3930, 5160, 1), new Location(3931, 5160, 1), new Location(3930, 5161, 1), new Location(3931, 5161, 1), new Location(3928, 5162, 1), new Location(3935, 5162, 1), new Location(3929, 5163, 1), new Location(3930, 5163, 1), new Location(3934, 5163, 1), new Location(3928, 5164, 1), new Location(3929, 5164, 1), new Location(3934, 5164, 1), new Location(3930, 5165, 1), new Location(3935, 5165, 1), new Location(3940, 5157, 1), new Location(3937, 5158, 1), new Location(3941, 5158, 1), new Location(3937, 5159, 1), new Location(3938, 5159, 1), new Location(3937, 5160, 1), new Location(3943, 5160, 1), new Location(3936, 5161, 1), new Location(3937, 5162, 1), new Location(3938, 5162, 1), new Location(3940, 5162, 1), new Location(3938, 5164, 1), new Location(3941, 5165, 1), new Location(3945, 5163, 1), new Location(3944, 5164, 1)},
        {new Location(3933, 5157, 1), new Location(3935, 5158, 1), new Location(3930, 5159, 1), new Location(3932, 5159, 1), new Location(3934, 5159, 1), new Location(3932, 5160, 1), new Location(3935, 5160, 1), new Location(3928, 5161, 1), new Location(3929, 5161, 1), new Location(3932, 5161, 1), new Location(3933, 5161, 1), new Location(3930, 5162, 1), new Location(3933, 5162, 1), new Location(3935, 5163, 1), new Location(3929, 5165, 1), new Location(3934, 5165, 1), new Location(3938, 5158, 1), new Location(3941, 5159, 1), new Location(3939, 5160, 1), new Location(3941, 5160, 1), new Location(3939, 5161, 1), new Location(3940, 5161, 1), new Location(3943, 5163, 1), new Location(3937, 5164, 1), new Location(3936, 5165, 1), new Location(3937, 5165, 1), new Location(3939, 5165, 1), new Location(3940, 5165, 1), new Location(3943, 5165, 1), new Location(3946, 5164, 1), new Location(3945, 5165, 1), new Location(3946, 5165, 1)}
    };
    private static final Location[][][] SKULL_LOCATIONS = {
            {
                {new Location(3933, 5162, 1), new Location(3936, 5164, 1), new Location(3939, 5162, 1), new Location(3936, 5160, 1)},
                {new Location(3932, 5164, 1), new Location(3934, 5161, 1), new Location(3936, 5164, 1), new Location(3938, 5161, 1), new Location(3940, 5164, 1)},
                {new Location(3934, 5164, 1), new Location(3934, 5162, 1), new Location(3934, 5160, 1), new Location(3938, 5164, 1), new Location(3938, 5162, 1), new Location(3938, 5160, 1)},
                {new Location(3930, 5161, 1), new Location(3932, 5164, 1), new Location(3934, 5161, 1), new Location(3936, 5164, 1), new Location(3938, 5161, 1), new Location(3940, 5164, 1), new Location(3942, 5161, 1)},
            },
            {
                //TODO
                {new Location(3933, 5162, 1), new Location(3936, 5164, 1), new Location(3939, 5162, 1), new Location(3936, 5160, 1)},
                {new Location(3932, 5164, 1), new Location(3934, 5161, 1), new Location(3936, 5164, 1), new Location(3938, 5161, 1), new Location(3940, 5164, 1)},
                {new Location(3934, 5164, 1), new Location(3934, 5162, 1), new Location(3934, 5160, 1), new Location(3938, 5164, 1), new Location(3938, 5162, 1), new Location(3938, 5160, 1)},
                {new Location(3930, 5161, 1), new Location(3932, 5164, 1), new Location(3934, 5161, 1), new Location(3936, 5164, 1), new Location(3938, 5161, 1), new Location(3940, 5164, 1), new Location(3942, 5161, 1)},
            }
    };
    private SecondWardenEncounter encounter;
    private WardenPhantom firstPhantom;
    private WardenPhantom secondPhantom;
    private int floorRotation;
    private boolean isSkullAttack;
    private int wardenAttackSpeed;
    private int currentAttackTicks = 6;
    private EnergySiphonNPC[] energySiphons;
    private boolean[] siphonsDone;
    private int skullTimer;
    private int explodingAnimationTicks = 1;

    public FlyingWardenNPC(int id, Location tile, WardenEncounter encounter) {
        super(id, tile, Direction.NORTH, 0, encounter);
        this.combat = new NPCCombat(this) {
            @Override
            public void setTarget(final Entity target, TargetSwitchCause cause) { }
            @Override
            public void forceTarget(final Entity target) { }
        };
        super.hitBar = new EntityHitBar(this) {
            @Override
            public int getType() {
                return 21;
            }
        };
        getUpdateFlags().flag(UpdateFlag.HIT);
    }

    public void setSecondWardenEncounter(final SecondWardenEncounter encounter, int wardenAttackSpeed) {
        this.encounter = encounter;
        super.toaRaidArea = encounter;
        this.wardenAttackSpeed = wardenAttackSpeed;
    }

    @Override
    public void handleIngoingHit(Hit hit) {
        super.handleIngoingHit(hit);
        final float threshold = getMaxHitpoints() * .05F;
        if (encounter.getPhaseId() == 2) {
            if (!checkSkullHitThreshold(threshold, hit)) {
                if (encounter.getPhaseId() == 2 && (float) hitpoints > threshold && (float) (hitpoints - hit.getDamage()) <= threshold) {
                    hit.setDamage(hitpoints - (int) (threshold));
                    encounter.startLastPhase();
                }
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (energySiphons != null) {
            for (EnergySiphonNPC energySiphon : energySiphons) {
                if (energySiphon != null && !energySiphon.isFinished()) {
                    energySiphon.finish();
                }
            }
        }
        if (firstPhantom != null && !firstPhantom.isFinished()) {
            firstPhantom.finish();
        }
        if (secondPhantom != null && !secondPhantom.isFinished()) {
            secondPhantom.finish();
        }
    }

    @Override public boolean setHitpoints(int amount) {
        final boolean set = super.setHitpoints(amount);
        if (encounter != null && EncounterStage.STARTED.equals(encounter.getStage())) {
            encounter.getPlayers().forEach(p -> p.getHpHud().updateValue(hitpoints));
        }
        return set;
    }

    private boolean checkSkullHitThreshold(float threshold, Hit hit) {
        for (int i = 0; i < 4; i++) {
            final int thresholdFactor = 16 - (i * 4);
            if ((float) hitpoints > threshold * thresholdFactor && (float) hitpoints - hit.getDamage() <= threshold * thresholdFactor) {
                hit.setDamage(hitpoints - (int) (threshold * thresholdFactor));
                sendSkulls(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public void processNPC() {
        if (encounter != null && EncounterStage.STARTED.equals(encounter.getStage())) {
            if (encounter.getPhaseId() == 2 || encounter.getPhaseId() == 3) {
                setGraphics(WARDEN_GFX);
            }
            if (encounter.getPhaseId() == 2) {
                if (!isSkullAttack) {
                    if (currentAttackTicks > 0 && --currentAttackTicks <= 0) {
                        sendFloorAttack();
                    }
                } else if (siphonsDone != null && skullTimer > 0 && --skullTimer <= 0) {
                    sendSkullFailureAttack();
                }
            } else if (encounter.getPhaseId() == 3) {
                if (explodingAnimationTicks > 0 && --explodingAnimationTicks == 0) {
                    setAnimation(EXPLODING_ANIMATION);
                }
            }
        }
    }

    private void sendFloorAttack() {
        currentAttackTicks = wardenAttackSpeed;
        setAnimation(new Animation(FLOOR_ATTACK_BASE_ANIM_ID + (floorRotation * 2) + (wardenAttackSpeed <= 4 ? 1 : 0)));
        final List<Location> firstTickLocation = new ArrayList<>();
        final List<Location> secondTickLocation = new ArrayList<>();
        int baseFloorGfxId = BASE_FLOOR_GFX_ID;
        final Location baseTile = encounter.getLocation(FLOOR_BASE_LOCATION);
        for (Location[] rotationGfxLocations : REGULAR_FLOOR_ATTACK_LOCATIONS[floorRotation]) {
            for (Location rotationLocation : rotationGfxLocations) {
                final Location loc = encounter.getLocation(rotationLocation);
                int delay = 90 + (loc.getTileDistance(baseTile) * 6);
                if (floorRotation == 2) {
                    delay -= 6;
                }
                if (delay < 120) {
                    firstTickLocation.add(loc);
                } else {
                    secondTickLocation.add(loc);
                }
                World.sendGraphics(new Graphics(baseFloorGfxId, delay, 0), loc);
            }
            baseFloorGfxId++;
        }
        floorRotation = (floorRotation + 1) % 3;
        WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (isSkullAttack || ticks == 2 || encounter == null ||
                        !EncounterStage.STARTED.equals(encounter.getStage()) || encounter.getPhaseId() != 2) {
                    stop();
                    return;
                }
                final List<Location> checkTiles = ticks == 0 ? firstTickLocation : secondTickLocation;
                checkTiles.forEach(loc -> {
                    for (Player p : encounter.getChallengePlayers()) {
                        if (loc.equals(p.getLocation()) && !isSkullAttack()) {
                            p.applyHit(new Hit(FlyingWardenNPC.this, (int) (18 * encounter.getParty().getDamageMultiplier()) + Utils.random(3), HitType.DEFAULT));
                        }
                    }
                });
                ticks++;
            }
        }), wardenAttackSpeed < 5 ? 1 : 2, 0);
    }

    private void sendSkulls(int index) {
        isSkullAttack = true;
        setCantInteract(true);
        if (!encounter.isInsanity()) {
            floorRotation = 0;
        }
        blockIncomingHits(1);
        setAnimation(SEND_SKULL_START_ANIMATION);
        setTransformation(id == TUMEKEN_ID ? TUMEKEN_ID + 2 : ELIDINIS_ID + 2);
        final Player[] currentPlayers = encounter.getChallengePlayers();
        for (Player p : currentPlayers) {
            if (p != null) {
                p.getActionManager().forceStop();
                p.getActionManager().setActionDelay(0);
                p.getActionManager().preventDelaySet();
            }
        }
        final int currentGroupSize = currentPlayers.length;
        final Location middleTile = getMiddleLocation();
        World.sendSoundEffect(middleTile, SEND_SKULL_SOUND);
        WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {

            int ticks;
            final List<Location> skullLocations = new ArrayList<>();
            @Override
            public void run() {
                if (encounter == null || !EncounterStage.STARTED.equals(encounter.getStage())
                    || encounter.getPhaseId() != 2 || ticks >= 5) {
                    stop();
                    return;
                }
                if (ticks == 0) {
                    for (Location loc : SKULL_LOCATIONS[currentGroupSize >= 2 ? 1 : 0][index]) {
                        final Location encounterLoc = encounter.getLocation(loc);
                        World.sendGraphics(SKULL_LANDING_GFX, encounterLoc);
                        World.sendProjectile(FlyingWardenNPC.this, encounterLoc, SKULL_SEND_PROJECTILE);
                        skullLocations.add(encounterLoc);
                    }
                    if (index == 1) {
                        firstPhantom = new WardenPhantom(id == TUMEKEN_ID + 2 ? WardenPhantom.ZEBAK_ID : WardenPhantom.AKKHA_ID, encounter.getLocation(FIRST_PHANTOM_SPAWN_LOCATION), encounter);
                        firstPhantom.spawn();
                    } else if (index == 2) {
                        secondPhantom = new WardenPhantom(id == TUMEKEN_ID + 2 ? WardenPhantom.BABA_ID : WardenPhantom.KEPHRI_ID, encounter.getLocation(SECOND_PHANTOM_SPAWN_LOCATION), encounter);
                        secondPhantom.spawn();
                    }
                } else if (ticks == 4) {
                    skullTimer = 3 + (skullLocations.size() * (encounter.isInsanity() ? 2 : 3));
                    final Player[] players = encounter.getChallengePlayers();
                    energySiphons = new EnergySiphonNPC[skullLocations.size()];
                    siphonsDone = new boolean[skullLocations.size()];
                    final int hitPoints = 1 + (currentGroupSize < 4 ? 0 : ((currentGroupSize - 2) / 2));
                    for (int i = 0; i < skullLocations.size(); i++) {
                        final Location skullLoc = skullLocations.get(i);
                        energySiphons[i] = new EnergySiphonNPC(skullLoc, FlyingWardenNPC.this, hitPoints, i);
                        energySiphons[i].spawn();
                        for (Player p : players) {
                            if (p != null && skullLoc.equals(p.getLocation())) {
                                WardenEncounter.movePlayer(p);
                            }
                        }
                    }
                }
                ticks++;
            }
        }), 1, 0);
    }

    private void sendSkullFailureAttack() {
        int missingSkulls = 0;
        for (int i = 0; i < siphonsDone.length; i++) {
            if (!siphonsDone[i]) {
                missingSkulls++;
            }
        }
        if (missingSkulls == 0) {
            return;
        }
        currentAttackTicks = 9;
        siphonsDone = null;
        setAnimation(SKULL_FAILURE_ANIMATION);
        final List<Location> firstTickLocation = new ArrayList<>();
        final List<Location> secondTickLocation = new ArrayList<>();
        int baseFloorGfxId = BASE_FLOOR_GFX_ID;
        final Location baseTile = encounter.getLocation(FLOOR_BASE_LOCATION);
        for (Location[] gfxLocations : FULL_FLOOR__ATTACK_LOCATIONS) {
            for (Location rotationLocation : gfxLocations) {
                final Location loc = encounter.getLocation(rotationLocation);
                int delay = 30 + (loc.getTileDistance(baseTile) * 6);
                if (floorRotation == 2) {
                    delay -= 6;
                }
                if (delay < 60) {
                    firstTickLocation.add(loc);
                } else {
                    secondTickLocation.add(loc);
                }
                World.sendGraphics(new Graphics(baseFloorGfxId, delay, 0), loc);
            }
            baseFloorGfxId++;
        }
        if (energySiphons != null) {
            for (EnergySiphonNPC energySiphon : energySiphons) {
                if (energySiphon != null && !energySiphon.isFinished()) {
                    World.sendGraphics(EXPLOSION_GFX, energySiphon.getLocation());
                    energySiphon.finish();
                }
            }
        }
        final int finalMissingSkulls = missingSkulls;
        WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {
            private int ticks;

            @Override
            public void run() {
                if (!isSkullAttack || ticks >= 5 || encounter == null || !EncounterStage.STARTED.equals(encounter.getStage())
                        || encounter.getPhaseId() != 2) {
                    stop();
                    return;
                }
                if (ticks == 0 || ticks == 1) {
                    if (ticks == 0) {
                        final Player[] players = encounter.getChallengePlayers();
                        for (Player p : players) {
                            if (p != null) {
                                p.sendSound(SKULL_FAILURE_SOUND);
                            }
                        }
                    }
                    final List<Location> checkTiles = ticks == 0 ? firstTickLocation : secondTickLocation;
                    checkTiles.forEach(loc -> {
                        for (Player p : encounter.getChallengePlayers()) {
                            if (loc.equals(p.getLocation())) {
                                p.applyHit(new Hit(FlyingWardenNPC.this, (int) ((encounter.isInsanity() ? 33 : 11) * finalMissingSkulls * encounter.getParty().getDamageMultiplier()), HitType.DEFAULT));
                            }
                        }
                    });
                } else if (ticks == 3) {
                    setTransformation(id == TUMEKEN_ID + 2 ? TUMEKEN_ID : ELIDINIS_ID);
                } else if (ticks == 4) {
                    setCantInteract(false);
                    isSkullAttack = false;
                }
                ticks++;
            }
        }), 0, 0);
    }

    public void markSkullDone(int index) {
        if (siphonsDone == null) {
            return;
        }
        siphonsDone[index] = true;
        if (isSkullAttack) {
            for (int i = 0; i < siphonsDone.length; i++) {
                if (!siphonsDone[i]) {
                    return;
                }
            }
            currentAttackTicks = 8;
            siphonsDone = null;
            WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {
                private int ticks;
                @Override
                public void run() {
                    if (!isSkullAttack || ticks >= 3 || encounter == null || !EncounterStage.STARTED.equals(encounter.getStage())
                            || encounter.getPhaseId() != 2) {
                        stop();
                        return;
                    }
                    if (ticks == 0) {
                        for (EnergySiphonNPC energySiphonNPC : energySiphons) {
                            if (energySiphonNPC != null) {
                                World.sendProjectile(energySiphonNPC, FlyingWardenNPC.this, SKULL_RETURN_PROJECTILE);
                                energySiphonNPC.finish();
                            }
                        }
                    } else if (ticks == 1) {
                        setAnimation(SKULL_RETURN_ANIMATION);
                        setTransformation(id == TUMEKEN_ID + 2 ? TUMEKEN_ID : ELIDINIS_ID);
                        for (Player p : encounter.getChallengePlayers()) {
                            if (p != null) {
                                for (SoundEffect sound : SEND_SKULL_RETURN_SOUNDS) {
                                    p.sendSound(sound);
                                }
                            }
                        }
                    } else if (ticks == 2) {
                        isSkullAttack = false;
                        setCantInteract(false);
                        applyHit(new Hit(FlyingWardenNPC.this, (int) (getMaxHitpoints() * .05F), HitType.DEFAULT));
                    }
                    ticks++;
                }
            }), 0, 0);
        }
    }

    @Override
    public boolean canAttack(Player source) {
        if (isSkullAttack) {
            source.sendMessage("You can't attack the warden right now.");
            return false;
        }
        return super.canAttack(source);
    }

    @Override
    public void sendDeath() {
        super.sendDeath();
        encounter.completeRoom();
        if (firstPhantom != null && !firstPhantom.isFinished()) {
            firstPhantom.finish();
        }
        if (secondPhantom != null && !secondPhantom.isFinished()) {
            secondPhantom.finish();
        }
    }

    public boolean isSkullAttack() { return isSkullAttack; }

    @Override
    public void setUnprioritizedAnimation(Animation animation) {

    }

    @Override
    public float getPointMultiplier() {
        return 2.5F;
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
