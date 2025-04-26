package com.zenyte.game.content.tombsofamascut.encounter;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.npc.FlyingWardenNPC;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.content.tombsofamascut.raid.EncounterType;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.InteractableEntity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfBoundaryException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Savions
 */
public class SecondWardenEncounter extends TOARaidArea implements CycleProcessPlugin {

    private static final int HIDDEN_WARDEN_BASE_ANIM_ID = 9691;
    private static final int HIDDEN_WARDEN_NPC_ID = 11765;
    private static final WorldObject CRYSTAL_OBJECT = new WorldObject(45138, 10, 0, new Location(3936, 5154, 1));
    private static final WorldObject[][] FLOOR_REPLACEMENT_OBJECTS = {
            {new WorldObject(45730, 22, 1, new Location(3933, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3930, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3928, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3936, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3939, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3941, 5165, 1))},
            {new WorldObject(45730, 22, 1, new Location(3935, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3931, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3929, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3937, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3944, 5165, 1)), new WorldObject(45732, 22, 1, new Location(3946, 5165, 1))},
            {new WorldObject(45731, 22, 1, new Location(3926, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3932, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3943, 5165, 1))},
            {new WorldObject(45730, 22, 1, new Location(3927, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3934, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3938, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3940, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3942, 5165, 1)), new WorldObject(45730, 22, 1, new Location(3945, 5165, 1))},
            {new WorldObject(45726, 22, 3, new Location(3927, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3927, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3933, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3933, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3930, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3930, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3936, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3936, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3941, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3941, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3945, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3945, 5165, 1))},
            {new WorldObject(45726, 22, 3, new Location(3934, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3934, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3931, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3931, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3928, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3928, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3943, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3943, 5165, 1))},
            {new WorldObject(45726, 22, 3, new Location(3935, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3935, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3929, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3929, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3937, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3937, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3939, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3939, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3942, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3942, 5165, 1)), new WorldObject(45736, 22, 3, new Location(3946, 5164, 1)), new WorldObject(45737, 22, 1, new Location(3946, 5165, 1))},
            {new WorldObject(45736, 22, 0, new Location(3926, 5164, 1)), new WorldObject(45735, 22, 1, new Location(3926, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3932, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3932, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3938, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3938, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3940, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3940, 5165, 1)), new WorldObject(45726, 22, 3, new Location(3944, 5164, 1)), new WorldObject(45727, 22, 1, new Location(3944, 5165, 1))},
            {new WorldObject(45726, 22, 3, new Location(3927, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3927, 5164, 1)), new WorldObject(45728, 22, 0, new Location(3934, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3934, 5164, 1)), new WorldObject(45726, 22, 3, new Location(3931, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3931, 5164, 1)), new WorldObject(45726, 22, 3, new Location(3936, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3936, 5164, 1)), new WorldObject(45726, 22, 3, new Location(3942, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3942, 5164, 1)), new WorldObject(45728, 22, 0, new Location(3944, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3944, 5164, 1)), new WorldObject(45736, 22, 3, new Location(3946, 5163, 1)), new WorldObject(45726, 22, 2, new Location(3946, 5164, 1))},
            {new WorldObject(45736, 22, 0, new Location(3926, 5163, 1)), new WorldObject(45726, 22, 0, new Location(3926, 5164, 1)), new WorldObject(45728, 22, 0, new Location(3935, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3935, 5164, 1)), new WorldObject(45726, 22, 3, new Location(3932, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3932, 5164, 1)), new WorldObject(45726, 22, 3, new Location(3939, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3939, 5164, 1)), new WorldObject(45728, 22, 0, new Location(3941, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3941, 5164, 1)), new WorldObject(45726, 22, 3, new Location(3943, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3943, 5164, 1)), new WorldObject(45728, 22, 0, new Location(3945, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3945, 5164, 1))},
            {new WorldObject(45728, 22, 0, new Location(3933, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3933, 5164, 1)), new WorldObject(45728, 22, 0, new Location(3929, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3929, 5164, 1)), new WorldObject(45728, 22, 0, new Location(3938, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3938, 5164, 1)), new WorldObject(45728, 22, 0, new Location(3940, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3940, 5164, 1))},
            {new WorldObject(45728, 22, 0, new Location(3930, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3930, 5164, 1)), new WorldObject(45728, 22, 0, new Location(3928, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3928, 5164, 1)), new WorldObject(45728, 22, 0, new Location(3937, 5163, 1)), new WorldObject(45738, 22, 0, new Location(3937, 5164, 1))},
            {new WorldObject(45736, 22, 0, new Location(3927, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3927, 5163, 1)), new WorldObject(45728, 22, 0, new Location(3933, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3933, 5163, 1)), new WorldObject(45728, 22, 0, new Location(3936, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3936, 5163, 1)), new WorldObject(45726, 22, 3, new Location(3941, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3941, 5163, 1)), new WorldObject(45726, 22, 3, new Location(3944, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3944, 5163, 1))},
            {new WorldObject(45728, 22, 0, new Location(3934, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3934, 5163, 1)), new WorldObject(45726, 22, 3, new Location(3928, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3928, 5163, 1)), new WorldObject(45728, 22, 0, new Location(3940, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3940, 5163, 1)), new WorldObject(45728, 22, 0, new Location(3943, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3943, 5163, 1))},
            {new WorldObject(45726, 22, 3, new Location(3935, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3935, 5163, 1)), new WorldObject(45726, 22, 3, new Location(3931, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3931, 5163, 1)), new WorldObject(45728, 22, 0, new Location(3929, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3929, 5163, 1)), new WorldObject(45726, 22, 3, new Location(3938, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3938, 5163, 1)), new WorldObject(45728, 22, 0, new Location(3942, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3942, 5163, 1)), new WorldObject(45736, 22, 3, new Location(3945, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3945, 5163, 1))},
            {new WorldObject(45728, 22, 0, new Location(3932, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3932, 5163, 1)), new WorldObject(45726, 22, 3, new Location(3930, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3930, 5163, 1)), new WorldObject(45728, 22, 0, new Location(3937, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3937, 5163, 1)), new WorldObject(45728, 22, 0, new Location(3939, 5162, 1)), new WorldObject(45738, 22, 0, new Location(3939, 5163, 1))},
            {new WorldObject(45726, 22, 3, new Location(3933, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3933, 5162, 1)), new WorldObject(45726, 22, 3, new Location(3930, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3930, 5162, 1)), new WorldObject(45726, 22, 3, new Location(3936, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3936, 5162, 1)), new WorldObject(45726, 22, 3, new Location(3939, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3939, 5162, 1))},
            {new WorldObject(45728, 22, 0, new Location(3934, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3934, 5162, 1)), new WorldObject(45728, 22, 0, new Location(3931, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3931, 5162, 1)), new WorldObject(45728, 22, 0, new Location(3938, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3938, 5162, 1)), new WorldObject(45728, 22, 0, new Location(3941, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3941, 5162, 1)), new WorldObject(45736, 22, 3, new Location(3944, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3944, 5162, 1))},
            {new WorldObject(45728, 22, 0, new Location(3935, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3935, 5162, 1)), new WorldObject(45728, 22, 0, new Location(3932, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3932, 5162, 1)), new WorldObject(45736, 22, 0, new Location(3928, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3928, 5162, 1)), new WorldObject(45726, 22, 3, new Location(3940, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3940, 5162, 1)), new WorldObject(45726, 22, 3, new Location(3943, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3943, 5162, 1))},
            {new WorldObject(45728, 22, 0, new Location(3929, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3929, 5162, 1)), new WorldObject(45728, 22, 0, new Location(3937, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3937, 5162, 1)), new WorldObject(45728, 22, 0, new Location(3942, 5161, 1)), new WorldObject(45738, 22, 0, new Location(3942, 5162, 1))},
            {new WorldObject(45728, 22, 0, new Location(3935, 5160, 1)), new WorldObject(45738, 22, 3, new Location(3935, 5161, 1)), new WorldObject(45728, 22, 0, new Location(3936, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3936, 5161, 1)), new WorldObject(45726, 22, 3, new Location(3938, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3938, 5161, 1)), new WorldObject(45728, 22, 0, new Location(3940, 5160, 1)), new WorldObject(45738, 22, 1, new Location(3940, 5161, 1))},
            {new WorldObject(45728, 22, 0, new Location(3932, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3932, 5161, 1)), new WorldObject(45728, 22, 0, new Location(3930, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3930, 5161, 1)), new WorldObject(45728, 22, 0, new Location(3937, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3937, 5161, 1)), new WorldObject(45728, 22, 0, new Location(3939, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3939, 5161, 1)), new WorldObject(45736, 22, 3, new Location(3943, 5160, 1)), new WorldObject(45738, 22, 2, new Location(3943, 5161, 1))},
            {new WorldObject(45728, 22, 0, new Location(3933, 5160, 1)), new WorldObject(45738, 22, 2, new Location(3933, 5161, 1)), new WorldObject(45726, 22, 3, new Location(3931, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3931, 5161, 1)), new WorldObject(45736, 22, 0, new Location(3929, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3929, 5161, 1)), new WorldObject(45726, 22, 3, new Location(3942, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3942, 5161, 1))},
            {new WorldObject(45726, 22, 3, new Location(3934, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3934, 5161, 1)), new WorldObject(45726, 22, 3, new Location(3941, 5160, 1)), new WorldObject(45738, 22, 2, new Location(3941, 5161, 1))},
            {new WorldObject(45728, 22, 0, new Location(3935, 5159, 1)), new WorldObject(45726, 22, 3, new Location(3933, 5159, 1)), new WorldObject(45738, 22, 0, new Location(3935, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3933, 5160, 1)), new WorldObject(45728, 22, 0, new Location(3936, 5159, 1)), new WorldObject(45736, 22, 3, new Location(3942, 5159, 1)), new WorldObject(45738, 22, 0, new Location(3936, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3942, 5160, 1))},
            {new WorldObject(45728, 22, 0, new Location(3932, 5159, 1)), new WorldObject(45738, 22, 0, new Location(3932, 5160, 1)), new WorldObject(45726, 22, 3, new Location(3937, 5159, 1)), new WorldObject(45726, 22, 3, new Location(3940, 5159, 1)), new WorldObject(45738, 22, 0, new Location(3937, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3940, 5160, 1))},
            {new WorldObject(45726, 22, 3, new Location(3931, 5159, 1)), new WorldObject(45738, 22, 0, new Location(3931, 5160, 1)), new WorldObject(45728, 22, 0, new Location(3939, 5159, 1)), new WorldObject(45728, 22, 0, new Location(3941, 5159, 1)), new WorldObject(45738, 22, 0, new Location(3939, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3941, 5160, 1))},
            {new WorldObject(45728, 22, 0, new Location(3934, 5159, 1)), new WorldObject(45736, 22, 0, new Location(3930, 5159, 1)), new WorldObject(45738, 22, 0, new Location(3934, 5160, 1)), new WorldObject(45738, 22, 0, new Location(3930, 5160, 1)), new WorldObject(45728, 22, 0, new Location(3938, 5159, 1)), new WorldObject(45738, 22, 0, new Location(3938, 5160, 1))},
            {new WorldObject(45728, 22, 0, new Location(3932, 5158, 1)), new WorldObject(45738, 22, 0, new Location(3932, 5159, 1)), new WorldObject(45728, 22, 0, new Location(3936, 5158, 1)), new WorldObject(45738, 22, 0, new Location(3936, 5159, 1)), new WorldObject(45726, 22, 3, new Location(3938, 5158, 1)), new WorldObject(45738, 22, 0, new Location(3938, 5159, 1))},
            {new WorldObject(45726, 22, 3, new Location(3935, 5158, 1)), new WorldObject(45738, 22, 0, new Location(3935, 5159, 1)), new WorldObject(45726, 22, 3, new Location(3933, 5158, 1)), new WorldObject(45738, 22, 0, new Location(3933, 5159, 1)), new WorldObject(45728, 22, 0, new Location(3937, 5158, 1)), new WorldObject(45738, 22, 0, new Location(3937, 5159, 1))},
            {new WorldObject(45734, 22, 0, new Location(3931, 5158, 1)), new WorldObject(45738, 22, 0, new Location(3931, 5159, 1)), new WorldObject(45726, 22, 3, new Location(3940, 5158, 1)), new WorldObject(45738, 22, 0, new Location(3940, 5159, 1))},
            {new WorldObject(45728, 22, 0, new Location(3934, 5158, 1)), new WorldObject(45738, 22, 0, new Location(3934, 5159, 1)), new WorldObject(45728, 22, 0, new Location(3939, 5158, 1)), new WorldObject(45738, 22, 0, new Location(3939, 5159, 1)), new WorldObject(45736, 22, 3, new Location(3941, 5158, 1)), new WorldObject(45738, 22, 0, new Location(3941, 5159, 1))},
    };
    private static final Location VOID_LOCATION = new Location(3936, 5130);
    private static final Location WARDEN_LOCATION = new Location(3934, 5152, 1);
    private static final Location PLAYER_SPAWN_LOCATION = new Location(3935, 5162, 1);
    private static final Location THUNDER_MIN_LOCATION = new Location(3926, 5157, 1);
    private static final Location FLOOR_REMOVAL_SOUND_LOCATION = new Location(3936, 5158, 1);
    private static final Location[] HIDDEN_WARDEN_LOCATIONS = {new Location(3934, 5152, 1), new Location(3934, 5147, 1), new Location(3934, 5142, 1), new Location(3934, 5137, 1), new Location(3934, 5132, 1)};
    private static final Animation WARDEN_LAST_PHASE_START_ANIMATION = new Animation(9684);
    private static final Animation PLAYER_PUSH_ANIMATION = new Animation(1114);
    private static final Graphics THUNDER_INCOMING_GRAPHICS = new Graphics(1446);
    private static final Graphics THUNDER_GRAPHICS = new Graphics(2197);
    private static final SoundEffect THUNDER_SOUND = new SoundEffect(6116);
    private static final SoundEffect SELF_HEAL_SOUND = new SoundEffect(167, 15);
    private static final SoundEffect LAST_PHASE_START_SOUND = new SoundEffect(1465);
    private static final SoundEffect[] FLOOR_REMOVAL_SOUNDS = {new SoundEffect(6247, 9), new SoundEffect(6153, 9, 30), new SoundEffect(6124, 9, 60), new SoundEffect(6219, 9, 90)};
    private static final SoundEffect[] LAID_TO_REST_SOUNDS = {new SoundEffect(6143), new SoundEffect(6044, 1, 330)};
    private static final Projectile FLOOR_REMOVAL_PROJECTILE = new Projectile(2228, 0, 200, 0, 20, 90, 0, 0);
    private final boolean insanity;
    private final List<Location> occupiedThunderLocations = new ArrayList<>();
    private final int wardenAttackSpeed;
    private int phaseId = 2;
    private FlyingWardenNPC wardenNPC;
    private int thunderTicks = 7;
    private int floorRemovalIndex;
    private int floorRemovalTicks = 10;

    public SecondWardenEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
        super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
        int attackSpeed = 6;
        if (party.getPartySettings().isActive(InvocationType.OVERCLOCKED)) {
            attackSpeed--;
        }
        if (party.getPartySettings().isActive(InvocationType.OVERCLOCKED_2)) {
            attackSpeed--;
        }
        this.insanity = party.getPartySettings().isActive(InvocationType.INSANITY);
        if (insanity) {
            attackSpeed--;
            thunderTicks = 6;
        }
        this.wardenAttackSpeed = attackSpeed;
    }

    @Override
    public void constructRegion() {
        if (constructed) {
            return;
        }
        GlobalAreaManager.add(this);
        try {
            MapBuilder.copyPlanesMap(area, staticChunkX, staticChunkY, chunkX, chunkY, sizeX, sizeY, 0, 1);
        } catch (OutOfBoundaryException e) {
            e.printStackTrace();
        }
        constructed = true;
        constructed();
    }

    public void preparePhase(WardenEncounter encounter, FlyingWardenNPC wardenNPC) {
        stage = EncounterStage.STARTED;
        this.wardenNPC = wardenNPC;
        wardenNPC.setSecondWardenEncounter(this, wardenAttackSpeed);
        wardenNPC.getUpdateFlags().flag(UpdateFlag.COMBAT_LEVEL_CHANGE);
        setChallengeTime(encounter.getChallengeTime());
        setChallengeName(encounter.getChallengeName());
        setTeamSize(encounter.getTeamSize());
        wardenNPC.setLocation(getLocation(WARDEN_LOCATION));
        final Location spawnLocation = getLocation(PLAYER_SPAWN_LOCATION);
        encounter.getPlayers().forEach(p -> {
            if (p != null) {
                p.getTOAManager().setCurrentEncounter(EncounterType.WARDENS_SECOND_ROOM);
                p.faceDirection(Direction.NORTH);
                p.getPacketDispatcher().resetCamera();
                p.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
                p.getHpHud().open(wardenNPC.getId(), wardenNPC.getMaxHitpoints());
                if (!p.isDying() && encounter.insideChallengeArea(p)) {
                    p.setLocation(spawnLocation.transform(2, 2));
                } else {
                    p.setLocation(getLocation(encounterType.getRandomizedSpawnTile()));
                }
            }
        });
    }

    public void startLastPhase() {
        if (phaseId != 2) {
            return;
        }
        phaseId = 3;
        if (wardenNPC != null) {
            World.sendSoundEffect(wardenNPC.getMiddleLocation(), SELF_HEAL_SOUND);
            wardenNPC.applyHit(new Hit(wardenNPC, (int) (wardenNPC.getMaxHitpoints() * .2F), HitType.HEALED));
            wardenNPC.setAnimation(WARDEN_LAST_PHASE_START_ANIMATION);
        }
        WorldTasksManager.schedule(addRunningTask(() -> {
            for (Player p : getChallengePlayers()) {
                if (p != null) {
                    p.getPacketDispatcher().resetCamera();
                    p.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, 5, 0, 0);
                    p.getPacketDispatcher().sendCameraShake(CameraShakeType.UP_AND_DOWN, 2, 0, 0);
                    p.getPacketDispatcher().sendCameraShake(CameraShakeType.FRONT_AND_BACK, 2, 0, 0);
                    p.sendSound(LAST_PHASE_START_SOUND);
                }
            }
        }), 3);
    }

    public int getPhaseId() {
        return phaseId;
    }

    @Override
    public void onRoomStart() {

    }

    @Override
    public void onRoomEnd() {
        phaseId = 4;
        players.forEach(p -> {
            if (p != null) {
                p.getTOAManager().sendHud();
                p.getPacketDispatcher().resetCamera();
                for (SoundEffect sound : LAID_TO_REST_SOUNDS) {
                    p.sendSound(sound);
                }
            }
        });
        for (int i = 0; i < HIDDEN_WARDEN_LOCATIONS.length; i++) {
            new HiddenNPC(getLocation(HIDDEN_WARDEN_LOCATIONS[i]), HIDDEN_WARDEN_BASE_ANIM_ID + i).spawn();
        }
        WorldTasksManager.schedule(() -> {
            if (!isDestroyed()) {
                World.spawnObject(new WorldObject(CRYSTAL_OBJECT.getId(), CRYSTAL_OBJECT.getType(),
                        CRYSTAL_OBJECT.getRotation(), getLocation(CRYSTAL_OBJECT.getLocation())));
            }
        }, 12);
    }

    @Override
    public void onRoomReset() {
        if (!hasFailedTombs()) {
            party.constructEncounter(EncounterType.WARDENS_FIRST_ROOM);
            final TOARaidArea area = party.getCurrentRaidArea();
            if (area != null) {
                players.forEach(p -> {
                    if (p != null) {
                        p.getTOAManager().setCurrentEncounter(EncounterType.WARDENS_FIRST_ROOM);
                        p.setLocation(area.getLocation(EncounterType.WARDENS_FIRST_ROOM.getRandomizedSpawnTile()));
                    }
                });
            }
        }
    }

    public boolean isInsanity() {
        return insanity;
    }

    @Override
    public void process() {
        if (EncounterStage.STARTED.equals(stage) && phaseId == 3) {
            if (floorRemovalIndex < FLOOR_REPLACEMENT_OBJECTS.length && floorRemovalTicks > 0 && --floorRemovalTicks == 0) {
                floorRemovalTicks = insanity ? 10 : 15;
                final int baseY = getY(THUNDER_MIN_LOCATION.getY());
                final int currentRow = (baseY + 8) - (floorRemovalIndex / 4);
                final Location soundLocation = getLocation(FLOOR_REMOVAL_SOUND_LOCATION);
                final Location voidLocation = getLocation(VOID_LOCATION);
                WorldTasksManager.schedule(addRunningTask(new WorldTask() {
                    int ticks;

                    @Override
                    public void run() {
                        if (ticks >= 4 || !EncounterStage.STARTED.equals(stage) || phaseId != 3
                                || wardenNPC == null || wardenNPC.isFinished()) {
                            return;
                        }
                        if (ticks == 0) {
                            for (SoundEffect sound : FLOOR_REMOVAL_SOUNDS) {
                                World.sendSoundEffect(soundLocation, sound);
                            }
                        }
                        final Player[] updatedPlayers = getChallengePlayers();
                        if (updatedPlayers.length < 1) {
                            return;
                        }
                        for (WorldObject object : FLOOR_REPLACEMENT_OBJECTS[floorRemovalIndex]) {
                            final Location location = getLocation(object.getLocation());
                            if (location.getY() == currentRow) {
                                World.sendProjectile(location, voidLocation, FLOOR_REMOVAL_PROJECTILE);
                            }
                            World.spawnObject(new WorldObject(object.getId(), object.getType(), object.getRotation(), location));
                            for (Player p : updatedPlayers) {
                                if (p != null && p.getLocation().equals(location)) {
                                    pushPlayer(p, currentRow - 1);
                                }
                            }
                        }
                        floorRemovalIndex++;
                        ticks++;
                    }
                }), 0, 0);
            }
            if (thunderTicks > 0) {
                if (--thunderTicks == 2 || thunderTicks == 1 || thunderTicks == 0) {
                    if (thunderTicks == 0) {
                        thunderTicks = 6;
                        occupiedThunderLocations.clear();
                    }
                    final Location minLocation = getLocation(THUNDER_MIN_LOCATION);
                    final List<Location> availableLocations = new ArrayList<>();
                    for (int y = 0; y <= 8 - (floorRemovalIndex / 4); y++) {
                        for (int x = 0; x <= 20; x++) {
                            final Location loc = minLocation.transform(x, y);
                            if (World.isFloorFree(loc, 1) && !occupiedThunderLocations.contains(loc)) {
                                availableLocations.add(loc);
                            }
                        }
                    }
                    Collections.shuffle(availableLocations);
                    final List<Location> thunderTiles = availableLocations.subList(0, Math.max(2, (int) (availableLocations.size() * .15)));
                    thunderTiles.forEach(loc -> {
                        World.sendGraphics(THUNDER_INCOMING_GRAPHICS, loc);
                        occupiedThunderLocations.add(loc);
                    });
                    WorldTasksManager.schedule(addRunningTask(() -> {
                        if (!EncounterStage.STARTED.equals(stage) || phaseId != 3 || wardenNPC == null || wardenNPC.isFinished()) {
                            return;
                        }
                        final Player[] currentPlayers = getChallengePlayers();
                        if (currentPlayers.length < 1) {
                            return;
                        }
                        for (Player p : currentPlayers) {
                            if (p != null) {
                                p.sendSound(THUNDER_SOUND);
                            }
                        }
                        thunderTiles.forEach(loc -> {
                            World.sendGraphics(THUNDER_GRAPHICS, loc);
                            for (Player p : currentPlayers) {
                                if (p != null && loc.equals(p.getLocation())) {
                                    p.applyHit(new Hit(wardenNPC, (int) (9 * party.getDamageMultiplier()) + Utils.random(4), HitType.DEFAULT));
                                }
                            }
                        });
                    }), 4);
                }
            }
        }
    }

    private void pushPlayer(Player player, int nextY) {
        Location nextLocation = null;
        radiusLoop:
        for (int radius = 1; radius <= 2; radius++) {
            for (int x = -radius; x <= radius; x++) {
                final Location location = new Location(player.getX() + x, nextY, 1);
                if (World.isFloorFree(location, 1)) {
                    nextLocation = location;
                    break radiusLoop;
                }
            }
        }
        if (nextLocation != null) {
            player.stopAll();
            player.lock(1);
            player.sendMessage("You are pushed off.");
            player.setAnimation(PLAYER_PUSH_ANIMATION);
            player.autoForceMovement(player.getLocation(), nextLocation, 0, 29);
        }
    }

    static class HiddenNPC extends NPC {

        public HiddenNPC(Location tile, int animId) {
            super(HIDDEN_WARDEN_NPC_ID, tile, Direction.NORTH, 0);
            setAnimation(new Animation(animId));
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
