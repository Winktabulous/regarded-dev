package com.zenyte.game.content.tombsofamascut.encounter;

import com.zenyte.game.content.tombsofamascut.npc.HetSeal;
import com.zenyte.game.content.tombsofamascut.npc.SealOrb;
import com.zenyte.game.content.tombsofamascut.object.MirrorObjectAction;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.content.tombsofamascut.raid.EncounterType;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.RSColour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Savions.
 */
public class HetEncounter extends TOARaidArea implements CycleProcessPlugin {

	private static final OrbPair[] ORB_PAIRS = {new OrbPair(new Location(3671, 5285), Direction.EAST), new OrbPair(new Location(3670, 5282), Direction.WEST), new OrbPair(new Location(3672, 5287), Direction.SOUTH), new OrbPair(new Location(3670, 5282), Direction.NORTH), new OrbPair(new Location(3673, 5288), Direction.SOUTH), new OrbPair(new Location(3671, 5275), Direction.WEST), new OrbPair(new Location(3670, 5282), Direction.EAST), new OrbPair(new Location(3671, 5275), Direction.NORTH), new OrbPair(new Location(3678, 5290), Direction.SOUTH), new OrbPair(new Location(3670, 5278), Direction.WEST), new OrbPair(new Location(3673, 5288), Direction.EAST), new OrbPair(new Location(3670, 5278), Direction.NORTH), new OrbPair(new Location(3678, 5290), Direction.EAST), new OrbPair(new Location(3673, 5272), Direction.WEST), new OrbPair(new Location(3671, 5285), Direction.SOUTH), new OrbPair(new Location(3673, 5272), Direction.NORTH), new OrbPair(new Location(3672, 5287), Direction.EAST), new OrbPair(new Location(3675, 5271), Direction.WEST), new OrbPair(new Location(3683, 5290), Direction.SOUTH), new OrbPair(new Location(3675, 5271), Direction.NORTH), new OrbPair(new Location(3683, 5290), Direction.WEST), new OrbPair(new Location(3679, 5270), Direction.WEST), new OrbPair(new Location(3685, 5289), Direction.SOUTH), new OrbPair(new Location(3679, 5270), Direction.NORTH), new OrbPair(new Location(3685, 5289), Direction.WEST), new OrbPair(new Location(3682, 5270), Direction.NORTH), new OrbPair(new Location(3688, 5287), Direction.SOUTH), new OrbPair(new Location(3685, 5271), Direction.NORTH), new OrbPair(new Location(3688, 5287), Direction.WEST), new OrbPair(new Location(3685, 5271), Direction.WEST), new OrbPair(new Location(3687, 5288), Direction.SOUTH), new OrbPair(new Location(3687, 5272), Direction.NORTH), new OrbPair(new Location(3687, 5288), Direction.WEST), new OrbPair(new Location(3687, 5272), Direction.WEST), new OrbPair(new Location(3689, 5284), Direction.SOUTH), new OrbPair(new Location(3688, 5273), Direction.NORTH), new OrbPair(new Location(3689, 5284), Direction.WEST), new OrbPair(new Location(3688, 5273), Direction.WEST), new OrbPair(new Location(3690, 5282), Direction.SOUTH), new OrbPair(new Location(3688, 5275), Direction.WEST), new OrbPair(new Location(3690, 5282), Direction.WEST), new OrbPair(new Location(3688, 5275), Direction.NORTH),};
	private static final Location SEAL_LOCATION = new Location(3679, 5279);
	private static final Location LIGHT_SOUND_LOCATION = new Location(3676, 5280);
	private static final Location CASTER_STATUE_LOCATION = new Location(3676, 5279);
	private static final Location SHIELD_STATUE_LOCATION = new Location(3682, 5279);
	private static final Location SEAL_CENTRE_LOCATION = new Location(3680, 5280);
	private static final Location LIGHT_START_LOCATION = new Location(3676, 5280);
	private static final Location SHIELD_STATUE_HIT_LOCATION = new Location(3683, 5280);
	private static final Location[] ROOF_COLLAPSE_GFX_LOCATIONS = {new Location(3678, 5275), new Location(3676, 5277), new Location(3674, 5275),
			new Location(3677, 5283), new Location(3674, 5283), new Location(3686, 5284)};
	private static final Location BARRIER_BASE_LOCATION = new Location(3669, 5279);
	private static final SoundEffect LIGHT_SOUND_EFFECT = new SoundEffect(6540, 15);
	private static final SoundEffect LIGHT_HIT_SOUND_EFFECT = new SoundEffect(6546, 5);
	private static final SoundEffect ROOF_SOUND = new SoundEffect(6542, 15);
	private static final SoundEffect SHIELD_STATUE_HIT_SOUND = new SoundEffect(2655);
	private static final SoundEffect WALLS_MOVING_SOUND = new SoundEffect(6535, 15);
	private static final SoundEffect CHALLENGE_END_SOUND = new SoundEffect(6539);
	private static final SoundEffect MIRROR_ROTATE_SOUND = new SoundEffect(6538, 3);
	private static final Animation PLAYER_MOVE_ANIM = new Animation(1114);
	private static final Graphics ROOF_COLLAPSE_GFX = new Graphics(60, 15, 0);
	private static final Graphics SHIELD_STATUE_HIT_GFX = new Graphics(732, 0, 254);
	private static final Graphics ORB_SPAWN_GFX = new Graphics(382);
	private static final SoundEffect ORB_SPAWN_SOUND = new SoundEffect(6545, 4);
	public static final int BREAKABLE_WALL_1 = 45463;
	public static final int BREAKABLE_WALL_2 = 45465;
	public static final int BREAKABLE_WALL_BROKEN_ID = 45466;
	public static final int MIRROR_PICK_UP = 45455;
	public static final int MIRROR_STATIC = 45456;
	public static final int MIRROR_STATIC_DIRTY = 45457;
	private static final int REGULAR_WALL_1 = 45459;
	private static final int REGULAR_WALL_2 = 45461;
	private static final int CASTER_STATUE_ID = 45486;
	private static final int SHIELD_STATUE_ID = 45485;
	private static final int LIGHT_HORIZONTAL_GFX_ID = 2114;
	private static final int LIGHT_VERTICAL_GFX_ID = 2064;
	private static final int LIGHT_SOUTH_EAST_GFX_ID = 2116;
	private static final int LIGHT_SOUTH_WEST_GFX_ID = 2117;
	private static final int LIGHT_NORTH_WEST_GFX_ID = 2118;
	private static final int LIGHT_NORTH_EAST_GFX_ID = 2119;
	private static final int LIGHT_END_GFX_ID = 2120;
	private static final int BARRIER_ID = 45135;
	private static final WorldObject[][] ROOM_SETS = {{new WorldObject(BREAKABLE_WALL_2, 10, 2, new Location(3671, 5277)), new WorldObject(MIRROR_PICK_UP, 11, 1, new Location(3675, 5271)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3672, 5277)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3673, 5277)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3674, 5277)), new WorldObject(BREAKABLE_WALL_2, 10, 0, new Location(3675, 5277)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3678, 5274)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3678, 5275)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3678, 5276)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3678, 5277)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3678, 5278)), new WorldObject(MIRROR_STATIC, 11, 0, new Location(3674, 5273)), new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3674, 5282)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3675, 5282)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3676, 5282)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3677, 5282)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3678, 5282)), new WorldObject(BREAKABLE_WALL_2, 10, 2, new Location(3676, 5285)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3677, 5285)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3678, 5285)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3679, 5285)), new WorldObject(MIRROR_STATIC, 11, 1, new Location(3673, 5286)), new WorldObject(MIRROR_PICK_UP, 11, 3, new Location(3675, 5289)), new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3680, 5275)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3681, 5275)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3682, 5275)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3683, 5275)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3684, 5275)), new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3682, 5278)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3683, 5278)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3684, 5278)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3685, 5278)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3686, 5278)), new WorldObject(MIRROR_STATIC, 11, 3, new Location(3684, 5273)), new WorldObject(BREAKABLE_WALL_2, 10, 2, new Location(3685, 5284)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3686, 5284)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3687, 5284)), new WorldObject(BREAKABLE_WALL_2, 10, 0, new Location(3680, 5285)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3682, 5282)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5283)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5284)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5285)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3682, 5286)), new WorldObject(MIRROR_STATIC, 11, 2, new Location(3685, 5285)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3688, 5284)), new WorldObject(BREAKABLE_WALL_2, 10, 0, new Location(3689, 5284)), new WorldObject(MIRROR_PICK_UP, 11, 2, new Location(3689, 5285))},
			{new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3671, 5281)), new WorldObject(MIRROR_PICK_UP, 11, 1, new Location(3675, 5271)), new WorldObject(BREAKABLE_WALL_2, 10, 2, new Location(3675, 5276)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3676, 5276)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3677, 5276)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3678, 5276)), new WorldObject(BREAKABLE_WALL_2, 10, 0, new Location(3679, 5276)), new WorldObject(MIRROR_STATIC, 11, 0, new Location(3673, 5277)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3672, 5281)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3673, 5281)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3674, 5281)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3675, 5281)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3675, 5283)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3675, 5284)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3675, 5285)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3675, 5286)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3675, 5287)), new WorldObject(BREAKABLE_WALL_2, 10, 1, new Location(3679, 5282)), new WorldObject(BREAKABLE_WALL_1, 10, 3, new Location(3679, 5283)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3679, 5284)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3679, 5285)), new WorldObject(BREAKABLE_WALL_2, 10, 3, new Location(3679, 5286)), new WorldObject(MIRROR_STATIC, 11, 1, new Location(3677, 5282)), new WorldObject(MIRROR_PICK_UP, 11, 3, new Location(3675, 5289)), new WorldObject(BREAKABLE_WALL_2, 10, 1, new Location(3686, 5278)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3686, 5279)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3682, 5275)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3682, 5276)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5277)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3682, 5278)), new WorldObject(BREAKABLE_WALL_2, 10, 2, new Location(3683, 5274)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3684, 5274)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3685, 5274)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3686, 5274)), new WorldObject(BREAKABLE_WALL_2, 10, 0, new Location(3687, 5274)), new WorldObject(MIRROR_STATIC, 11, 3, new Location(3683, 5272)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3682, 5284)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3682, 5285)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5286)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3682, 5287)), new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3682, 5283)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3683, 5283)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3684, 5283)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3685, 5283)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3686, 5283)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3686, 5280)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3686, 5281)), new WorldObject(BREAKABLE_WALL_2, 10, 3, new Location(3686, 5282)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3682, 5288)), new WorldObject(MIRROR_STATIC, 11, 3, new Location(3689, 5280)), new WorldObject(MIRROR_PICK_UP, 11, 2, new Location(3689, 5285))},
			{new WorldObject(MIRROR_PICK_UP, 11, 1, new Location(3675, 5271)), new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3678, 5278)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3679, 5278)), new WorldObject(MIRROR_STATIC, 11, 0, new Location(3673, 5277)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3674, 5281)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3674, 5282)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3674, 5283)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3674, 5284)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3674, 5285)), new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3676, 5285)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3677, 5285)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3678, 5285)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3679, 5285)), new WorldObject(MIRROR_PICK_UP, 11, 3, new Location(3675, 5289)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3680, 5278)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3681, 5278)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3682, 5278)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3683, 5278)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3684, 5278)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3685, 5278)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3686, 5278)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3687, 5278)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3687, 5279)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3682, 5272)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5273)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5274)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3682, 5275)), new WorldObject(MIRROR_STATIC, 11, 3, new Location(3686, 5279)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3680, 5285)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3687, 5280)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3687, 5281)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3687, 5282)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3687, 5283)), new WorldObject(BREAKABLE_WALL_2, 10, 1, new Location(3681, 5284)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3681, 5285)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3681, 5286)), new WorldObject(BREAKABLE_WALL_2, 10, 3, new Location(3681, 5287)), new WorldObject(BREAKABLE_WALL_2, 10, 1, new Location(3683, 5284)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3683, 5285)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3683, 5286)), new WorldObject(BREAKABLE_WALL_2, 10, 3, new Location(3683, 5287)), new WorldObject(BREAKABLE_WALL_2, 10, 2, new Location(3680, 5282)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3681, 5282)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3682, 5282)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3683, 5282)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3684, 5282)), new WorldObject(BREAKABLE_WALL_2, 10, 0, new Location(3685, 5282)), new WorldObject(BREAKABLE_WALL_2, 10, 2, new Location(3685, 5286)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3686, 5286)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3687, 5286)), new WorldObject(BREAKABLE_WALL_2, 10, 0, new Location(3688, 5286)), new WorldObject(MIRROR_STATIC, 11, 2, new Location(3689, 5284)), new WorldObject(MIRROR_STATIC, 11, 1, new Location(3688, 5282)), new WorldObject(MIRROR_PICK_UP, 11, 2, new Location(3689, 5285))},
			{new WorldObject(BREAKABLE_WALL_2, 10, 2, new Location(3671, 5277)), new WorldObject(MIRROR_PICK_UP, 11, 1, new Location(3675, 5271)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3672, 5277)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3673, 5277)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3674, 5277)), new WorldObject(BREAKABLE_WALL_2, 10, 0, new Location(3675, 5277)), new WorldObject(BREAKABLE_WALL_2, 10, 1, new Location(3678, 5274)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3678, 5275)), new WorldObject(BREAKABLE_WALL_1, 10, 3, new Location(3678, 5276)), new WorldObject(BREAKABLE_WALL_1, 10, 3, new Location(3678, 5277)), new WorldObject(BREAKABLE_WALL_2, 10, 3, new Location(3678, 5278)), new WorldObject(MIRROR_STATIC, 11, 1, new Location(3675, 5273)), new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3674, 5282)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3675, 5282)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3676, 5282)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3677, 5282)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3678, 5282)), new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3676, 5285)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3677, 5285)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3678, 5285)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3679, 5285)), new WorldObject(MIRROR_STATIC, 11, 1, new Location(3673, 5280)), new WorldObject(MIRROR_PICK_UP, 11, 3, new Location(3675, 5289)), new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3680, 5275)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3681, 5275)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3682, 5275)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3683, 5275)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3684, 5275)), new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3682, 5278)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3683, 5278)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3684, 5278)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3685, 5278)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3686, 5278)), new WorldObject(MIRROR_STATIC, 11, 2, new Location(3684, 5277)), new WorldObject(BREAKABLE_WALL_2, 10, 2, new Location(3685, 5284)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3686, 5284)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3687, 5284)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3680, 5285)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3682, 5282)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5283)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5284)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5285)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3682, 5286)), new WorldObject(MIRROR_STATIC, 11, 2, new Location(3687, 5281)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3688, 5284)), new WorldObject(BREAKABLE_WALL_2, 10, 0, new Location(3689, 5284)), new WorldObject(MIRROR_PICK_UP, 11, 2, new Location(3689, 5285))},
			{new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3671, 5281)), new WorldObject(MIRROR_PICK_UP, 11, 1, new Location(3675, 5271)), new WorldObject(BREAKABLE_WALL_2, 10, 2, new Location(3675, 5276)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3676, 5276)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3677, 5276)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3678, 5276)), new WorldObject(BREAKABLE_WALL_2, 10, 0, new Location(3679, 5276)), new WorldObject(MIRROR_STATIC, 11, 0, new Location(3675, 5273)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3672, 5281)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3673, 5281)), new WorldObject(REGULAR_WALL_1, 10, 0, new Location(3674, 5281)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3675, 5281)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3675, 5283)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3675, 5284)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3675, 5285)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3675, 5286)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3675, 5287)), new WorldObject(BREAKABLE_WALL_2, 10, 1, new Location(3679, 5282)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3679, 5283)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3679, 5284)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3679, 5285)), new WorldObject(BREAKABLE_WALL_2, 10, 3, new Location(3679, 5286)), new WorldObject(MIRROR_PICK_UP, 11, 3, new Location(3675, 5289)), new WorldObject(BREAKABLE_WALL_2, 10, 1, new Location(3686, 5278)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3686, 5279)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3682, 5275)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5276)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5277)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3682, 5278)), new WorldObject(BREAKABLE_WALL_2, 10, 2, new Location(3683, 5274)), new WorldObject(BREAKABLE_WALL_1, 10, 0, new Location(3684, 5274)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3685, 5274)), new WorldObject(BREAKABLE_WALL_1, 10, 2, new Location(3686, 5274)), new WorldObject(BREAKABLE_WALL_2, 10, 0, new Location(3687, 5274)), new WorldObject(MIRROR_STATIC, 11, 1, new Location(3684, 5276)), new WorldObject(REGULAR_WALL_2, 10, 1, new Location(3682, 5284)), new WorldObject(REGULAR_WALL_1, 10, 1, new Location(3682, 5285)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5286)), new WorldObject(REGULAR_WALL_1, 10, 3, new Location(3682, 5287)), new WorldObject(REGULAR_WALL_2, 10, 2, new Location(3682, 5283)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3683, 5283)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3684, 5283)), new WorldObject(REGULAR_WALL_1, 10, 2, new Location(3685, 5283)), new WorldObject(REGULAR_WALL_2, 10, 0, new Location(3686, 5283)), new WorldObject(BREAKABLE_WALL_1, 10, 1, new Location(3686, 5280)), new WorldObject(BREAKABLE_WALL_1, 10, 3, new Location(3686, 5281)), new WorldObject(BREAKABLE_WALL_2, 10, 3, new Location(3686, 5282)), new WorldObject(REGULAR_WALL_2, 10, 3, new Location(3682, 5288)), new WorldObject(MIRROR_STATIC, 11, 3, new Location(3688, 5276)), new WorldObject(MIRROR_STATIC, 11, 2, new Location(3688, 5286)), new WorldObject(MIRROR_PICK_UP, 11, 2, new Location(3689, 5285))}
	};
	private static final RSColour[] HET_HUD_COLOURS = { new RSColour(0, 4, 4), new RSColour(0, 19, 15), new RSColour(0, 26, 21)};
	private final List<WorldObject> roomObjects = new ArrayList<>();
	private final ArrayList<SealOrb> orbs = new ArrayList<>();
	private HetSeal seal;
	private int downTicks;
	private boolean putDown;
	private int comingUpTicks;
	private int lightTicks = 9;
	private int roomRotation = Utils.random(ROOM_SETS.length - 1);
	private int orbTicks = 7;
	private int orbIndex = Utils.random(ORB_PAIRS.length - 1);

	public HetEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
		super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
	}

	@Override public void enter(Player player) {
		super.enter(player);
		for (int animId = 9760; animId <= 9792; animId++) {
			player.getPacketDispatcher().sendClientScript(1846, animId);
		}
		player.getPacketDispatcher().sendClientScript(1846, 9538);
		player.getPacketDispatcher().sendClientScript(1846, 9539);
		player.getPacketDispatcher().sendClientScript(1846, 9540);
		player.getPacketDispatcher().sendClientScript(1846, 9541);
	}

	@Override public void constructed() {
		super.constructed();
		spawnSeal();
		World.spawnObject(new WorldObject(CASTER_STATUE_ID, 10, 1, getLocation(CASTER_STATUE_LOCATION)));
		World.spawnObject(new WorldObject(SHIELD_STATUE_ID, 10, 3, getLocation(SHIELD_STATUE_LOCATION)));
	}

	private void spawnSeal() {
		if (seal != null) {
			seal.finish();
		}
		seal = new HetSeal(getLocation(SEAL_LOCATION), this);
		seal.spawn();
	}

	private void removeMirrorsFromInventories() {
		for (Player p : players) {
			if (p != null) {
				p.getInventory().deleteItem(MirrorObjectAction.MIRROR_ITEM_ID, 99);
			}
		}
	}

	private void comeUp() {
		comingUpTicks = 2;
		World.sendSoundEffect(getLocation(SEAL_CENTRE_LOCATION), WALLS_MOVING_SOUND);
		final Player[] challengePlayers = getChallengePlayers();
		for (WorldObject object : ROOM_SETS[roomRotation]) {
			final WorldObject roomObject = new WorldObject(object.getId(), object.getType(), object.getRotation(), getLocation(object.getLocation()));
			if (teamSize > 1 && object.getId() == MIRROR_STATIC && Utils.random(1) == 0) {
				roomObject.setId(MIRROR_STATIC_DIRTY);
			}
			World.spawnObject(roomObject);
			roomObjects.add(roomObject);
		}
		for (Player p : challengePlayers) {
			for (WorldObject object : roomObjects) {
				if (p.getLocation().equals(object.getLocation())) {
					movePlayer(p);
				}
			}
		}
		roomRotation = (roomRotation + 1) % ROOM_SETS.length;
	}

	private void movePlayer(final Player player) {
		Location nextLocation = null;
		radiusLoop : for (int radius = 1; radius <= 2; radius++) {
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					if (x != -radius && x != radius && y != -radius && y != radius) {
						continue;
					}
					final Location attemptLocation = player.getLocation().transform(x, y);
					if (World.isFloorFree(attemptLocation, 1) && World.getObjectWithType(attemptLocation, 10) == null) {
						nextLocation = attemptLocation;
						break radiusLoop;
					}
				}
			}
		}
		if (nextLocation != null) {
			player.stopAll();
			player.lock(1);
			player.setAnimation(PLAYER_MOVE_ANIM);
			player.autoForceMovement(player.getLocation(), nextLocation, 0, 29);
		}
	}

	private void removeObjects() {
		roomObjects.removeIf(object -> {
			World.removeObject(object);
			return true;
		});
	}

	@Override public void process() {
		if (EncounterStage.STARTED.equals(stage)) {
			if (--orbTicks <= 0) {
				orbTicks = 7;
				for (int i = 0; i < 2; i++) {
					spawnOrb();
				}
			}
			if (downTicks > 0 && --downTicks <= 0) {
				seal.setTransformation(HetSeal.ID);
				comeUp();
			}
			if (putDown) {
				downTicks = 24;
				putDown = false;
				final Player[] challengePlayers = getChallengePlayers();
				for (Player p : challengePlayers) {
					if (p != null) {
						p.sendMessage("<col=185820>The statue has been struck! The seal weakens!</col>");
						p.getPacketDispatcher().sendGameMessage("The room begins to shake violently!", true);
						p.sendSound(SHIELD_STATUE_HIT_SOUND);
						p.getPacketDispatcher().sendCameraShake(CameraShakeType.FRONT_AND_BACK, 2, 10, 10);
					}
				}
				WorldTasksManager.schedule(() -> {
					for (Player p : challengePlayers) {
						if (p != null && !p.isFinished()) {
							p.getPacketDispatcher().resetCamera();
						}
					}
				}, 2);
				World.sendGraphics(SHIELD_STATUE_HIT_GFX, getLocation(SHIELD_STATUE_HIT_LOCATION));
				seal.setTransformation(HetSeal.ID + 1);
				WorldTasksManager.schedule(addRunningTask(() -> {
					if (EncounterStage.STARTED.equals(stage)) {
						removeMirrorsFromInventories();
						removeObjects();
						World.sendSoundEffect(getLocation(SEAL_CENTRE_LOCATION), ROOF_SOUND);
						for (Location roofLoc : ROOF_COLLAPSE_GFX_LOCATIONS) {
							World.sendGraphics(ROOF_COLLAPSE_GFX, getLocation(roofLoc));
						}
					}
				}), 0);
			}
			if (downTicks <= 0 && comingUpTicks <= 0 && lightTicks > 0 && --lightTicks <= 0) {
				lightTicks = 9;
				shootLight();
			}
			if (comingUpTicks > 0 && --comingUpTicks <= 0) {
				roomObjects.forEach(object -> {
					if (object.getId() != MIRROR_PICK_UP && object.getId() != MIRROR_STATIC && object.getId() != MIRROR_STATIC_DIRTY) {
						object.setId(object.getId() - 1);
						World.spawnObject(object);
					}
				});
			}
		}
	}

	private void spawnOrb() {
		final OrbPair orbPair = ORB_PAIRS[orbIndex];
		orbIndex += 1 + Utils.random(ORB_PAIRS.length - 2);
		orbIndex %= ORB_PAIRS.length;
		final Location location = getLocation(orbPair.location);
		World.sendGraphics(ORB_SPAWN_GFX, location);
		WorldTasksManager.schedule(addRunningTask(() -> {
			World.sendSoundEffect(location, ORB_SPAWN_SOUND);
			final SealOrb firstOrb = new SealOrb(location, orbPair.direction, HetEncounter.this);
			firstOrb.spawn();
			orbs.add(firstOrb);
		}), 3);
	}

	private void shootLight() {
		World.sendSoundEffect(getLocation(LIGHT_SOUND_LOCATION), LIGHT_SOUND_EFFECT);
		Location lastTile = getLocation(LIGHT_START_LOCATION);
		Direction moveDirection = Direction.WEST;
		for (int i = 0; i < 200; i++) {
			lastTile = lastTile.transform(moveDirection.getOffsetX(), moveDirection.getOffsetY());
			final WorldObject object = World.getObjectOfSlot(lastTile, 10);
			final int delay = 1 + i;
			final Player[] challengePlayers = getChallengePlayers();
			for (final Player p : challengePlayers) {
				if (p != null && lastTile.equals(p.getLocation())) {
					final int baseDamage = (int) Math.floor(party.getDamageMultiplier() * 5F);
					p.scheduleHit(seal, new Hit(seal, baseDamage + Utils.random(3), HitType.DEFAULT), 1);
				}
			}
			final boolean unProtect = insideLargeObject(getLocation(SHIELD_STATUE_LOCATION), lastTile);
			if ((object == null || object.getId() == BREAKABLE_WALL_BROKEN_ID) &&
					(!insideLargeObject(getLocation(CASTER_STATUE_LOCATION), lastTile) && !insideLargeObject(getLocation(SEAL_LOCATION), lastTile) && !unProtect)) {
				sendLight(lastTile, moveDirection, delay);
			} else {
				if (object != null && (object.getId() == MIRROR_PICK_UP || object.getId() == MIRROR_STATIC)) {
					if (object.getRotation() == 0) {
						moveDirection = rotateLight(lastTile, Direction.NORTH_EAST, moveDirection, delay);
					} else if (object.getRotation() == 1) {
						moveDirection = rotateLight(lastTile, Direction.SOUTH_EAST, moveDirection, delay);
					} else if (object.getRotation() == 2) {
						moveDirection = rotateLight(lastTile, Direction.SOUTH_WEST, moveDirection, delay);
					} else if (object.getRotation() == 3) {
						moveDirection = rotateLight(lastTile, Direction.NORTH_WEST, moveDirection, delay);
					}
					if (moveDirection != null) {
						World.sendSoundEffect(lastTile, MIRROR_ROTATE_SOUND);
						continue;
					}
				}
				if (unProtect) {
					putDown = true;
				}
				sendLight(lastTile, null, delay - 1);
				World.sendSoundEffect(lastTile, LIGHT_HIT_SOUND_EFFECT);
				break;
			}
		}
	}

	private boolean insideLargeObject(final Location southWestTile, final Location tile) {
		return tile.getX() >= southWestTile.getX() && tile.getX() <= southWestTile.getX() + 2 &&
				tile.getY() >= southWestTile.getY() && tile.getY() <= southWestTile.getY() + 2;
	}

	private Direction rotateLight(Location tile, Direction rotation, Direction currentDirection, int delay) {
		final Direction prevDirection = Direction.getDirection(rotation.getOffsetX(), 0);
		final Direction nextDirection = Direction.getDirection(0, rotation.getOffsetY());
		currentDirection = Direction.getOppositeDirection(currentDirection);
		if (!prevDirection.equals(currentDirection) && !nextDirection.equals(currentDirection)) {
			return null;
		}
		sendLight(tile, rotation, delay);
		return prevDirection.equals(currentDirection) ? nextDirection : prevDirection;
	}

	private void sendLight(Location location, Direction direction, int delay) {
		int gfxId = LIGHT_HORIZONTAL_GFX_ID;
		if (direction == null) {
			gfxId = LIGHT_END_GFX_ID;
		} else if (Direction.NORTH.equals(direction) || Direction.SOUTH.equals(direction)) {
			gfxId = LIGHT_VERTICAL_GFX_ID;
		} else if (Direction.NORTH_WEST.equals(direction)) {
			gfxId = LIGHT_NORTH_WEST_GFX_ID;
		} else if (Direction.NORTH_EAST.equals(direction)) {
			gfxId = LIGHT_NORTH_EAST_GFX_ID;
		} else if (Direction.SOUTH_WEST.equals(direction)) {
			gfxId = LIGHT_SOUTH_WEST_GFX_ID;
		} else if (Direction.SOUTH_EAST.equals(direction)) {
			gfxId = LIGHT_SOUTH_EAST_GFX_ID;
		}
		World.sendGraphics(new Graphics(gfxId, delay, 255), location);
	}

	public WorldObject getObject(WorldObject location) {
		for (WorldObject barrier : roomObjects) {
			if (barrier != null) {
				if (location.equals(barrier.getLocation())) {
					return barrier;
				}
			}
		}
		return null;
	}

	public boolean canDestroySeal() {
		return downTicks > 0;
	}

	public HetSeal getSeal() {
		return seal;
	}

	public void addObject(final WorldObject object) {
		roomObjects.add(object);
	}

	public void removeObject(final WorldObject object) {
		roomObjects.remove(object);
	}

	@Override public void onRoomStart() {
		seal.setMaxHealth();
		comeUp();
		players.forEach(p -> {
			p.getHpHud().open(HetSeal.ID, seal.getMaxHitpoints());
			p.getHpHud().updateValue(seal.getMaxHitpoints());
			p.getHpHud().sendColorChange(HET_HUD_COLOURS[0], HET_HUD_COLOURS[1], HET_HUD_COLOURS[2]);
		});
	}

	@Override public void onRoomEnd() {
		removeObjects();
		removeMirrorsFromInventories();
		final Player[] challengePlayers = getChallengePlayers();
		for (Player p : challengePlayers) {
			p.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, 5, 15, 10);
			p.sendSound(CHALLENGE_END_SOUND);
		}
		WorldTasksManager.schedule(() -> {
			for (Player p : challengePlayers) {
				if (p != null) {
					p.getPacketDispatcher().resetCamera();
				}
			}
		}, 1);
		for (int i = 0; i < 3; i++) {
			World.removeObject(new WorldObject(BARRIER_ID, 10, 3, getLocation(BARRIER_BASE_LOCATION).transform(0, i)));
		}
		orbs.removeIf(orb -> {
			if (!orb.isDying() && !orb.isFinished()) {
				orb.finish();
			}
			return true;
		});
	}

	@Override public void onRoomReset() {
		spawnSeal();
		removeObjects();
		downTicks = 0;
		comingUpTicks = 0;
		putDown = false;
		orbTicks = 7;
		orbIndex = Utils.random(ORB_PAIRS.length - 1);
		removeMirrorsFromInventories();
		orbs.removeIf(orb -> {
			if (!orb.isDying() && !orb.isFinished()) {
				orb.finish();
			}
			return true;
		});
	}

	@Override public void leave(Player player, boolean logout) {
		super.leave(player, logout);
		player.getHpHud().close();
	}

	static class OrbPair {

		final Location location;
		final Direction direction;

		OrbPair(final Location location, Direction direction) {
			this.location = location;
			this.direction = direction;
		}
	}
}