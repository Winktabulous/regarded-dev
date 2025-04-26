package com.zenyte.game.content.tombsofamascut.encounter;

import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.npc.*;
import com.zenyte.game.content.tombsofamascut.raid.*;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.AbstractEntity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.PartialMovementPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.utils.TimeUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Savions.
 */
public class AkkhaEncounter extends TOARaidArea implements PartialMovementPlugin, CycleProcessPlugin {

	private static final Location QUADRANT_BASE_LOCATION = new Location(3681, 5408, 1);
	public static final int FAKE_SHADOW_ID = 11798;
	public static final int[] TRAIL_ORB_NPC_IDS = { 11801, 11800, 11803, 11802 };
	public static final int[] QUADRANT_GFX_IDS = {2257, 2256, 2259, 2258};
	private static final Location[] QUADRANT_START_TILES = {new Location(3681, 5408, 1), new Location(3681, 5407, 1), new Location(3680, 5407, 1), new Location(3680, 5408, 1)};
	private static final Location[] SHADOW_LOCATIONS = {new Location(3685, 5412, 1), new Location(3685, 5401, 1), new Location(3674, 5401, 1), new Location(3674, 5412, 1)};
	private static final OrbPath[] UNSTABLE_ORB_PATHS = {new OrbPath(new Location(3670, 5405, 1), Direction.NORTH_EAST), new OrbPath(new Location(3670, 5406, 1), Direction.NORTH_EAST), new OrbPath(new Location(3670, 5406, 1), Direction.EAST), new OrbPath(new Location(3670, 5407, 1), Direction.NORTH_EAST), new OrbPath(new Location(3670, 5407, 1), Direction.EAST), new OrbPath(new Location(3670, 5407, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3670, 5408, 1), Direction.NORTH_EAST), new OrbPath(new Location(3670, 5408, 1), Direction.EAST), new OrbPath(new Location(3670, 5408, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3670, 5409, 1), Direction.EAST), new OrbPath(new Location(3670, 5409, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3670, 5410, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3671, 5402, 1), Direction.NORTH_EAST), new OrbPath(new Location(3671, 5403, 1), Direction.NORTH_EAST), new OrbPath(new Location(3671, 5403, 1), Direction.EAST), new OrbPath(new Location(3671, 5404, 1), Direction.NORTH_EAST), new OrbPath(new Location(3671, 5404, 1), Direction.EAST), new OrbPath(new Location(3671, 5404, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3671, 5405, 1), Direction.NORTH), new OrbPath(new Location(3671, 5405, 1), Direction.NORTH_EAST), new OrbPath(new Location(3671, 5405, 1), Direction.EAST), new OrbPath(new Location(3671, 5405, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3671, 5410, 1), Direction.SOUTH), new OrbPath(new Location(3671, 5410, 1), Direction.NORTH_EAST), new OrbPath(new Location(3671, 5410, 1), Direction.EAST), new OrbPath(new Location(3671, 5410, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3671, 5411, 1), Direction.NORTH_EAST), new OrbPath(new Location(3671, 5411, 1), Direction.EAST), new OrbPath(new Location(3671, 5411, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3671, 5412, 1), Direction.EAST), new OrbPath(new Location(3671, 5412, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3671, 5413, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3672, 5401, 1), Direction.NORTH_EAST), new OrbPath(new Location(3672, 5402, 1), Direction.NORTH), new OrbPath(new Location(3672, 5402, 1), Direction.NORTH_EAST), new OrbPath(new Location(3672, 5402, 1), Direction.EAST), new OrbPath(new Location(3672, 5413, 1), Direction.SOUTH), new OrbPath(new Location(3672, 5413, 1), Direction.EAST), new OrbPath(new Location(3672, 5413, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3672, 5414, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3673, 5400, 1), Direction.NORTH_EAST), new OrbPath(new Location(3673, 5401, 1), Direction.NORTH), new OrbPath(new Location(3673, 5401, 1), Direction.NORTH_EAST), new OrbPath(new Location(3673, 5401, 1), Direction.EAST), new OrbPath(new Location(3673, 5414, 1), Direction.SOUTH), new OrbPath(new Location(3673, 5414, 1), Direction.EAST), new OrbPath(new Location(3673, 5414, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3673, 5415, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3674, 5399, 1), Direction.NORTH_EAST), new OrbPath(new Location(3674, 5400, 1), Direction.NORTH), new OrbPath(new Location(3674, 5400, 1), Direction.NORTH_EAST), new OrbPath(new Location(3674, 5400, 1), Direction.EAST), new OrbPath(new Location(3674, 5415, 1), Direction.SOUTH), new OrbPath(new Location(3674, 5415, 1), Direction.EAST), new OrbPath(new Location(3674, 5415, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3674, 5416, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3675, 5398, 1), Direction.NORTH_EAST), new OrbPath(new Location(3675, 5399, 1), Direction.NORTH), new OrbPath(new Location(3675, 5399, 1), Direction.NORTH_EAST), new OrbPath(new Location(3675, 5399, 1), Direction.EAST), new OrbPath(new Location(3675, 5416, 1), Direction.SOUTH), new OrbPath(new Location(3675, 5416, 1), Direction.EAST), new OrbPath(new Location(3675, 5416, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3675, 5417, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3676, 5398, 1), Direction.NORTH), new OrbPath(new Location(3676, 5398, 1), Direction.NORTH_EAST), new OrbPath(new Location(3676, 5417, 1), Direction.SOUTH), new OrbPath(new Location(3676, 5417, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3677, 5398, 1), Direction.NORTH_WEST), new OrbPath(new Location(3677, 5398, 1), Direction.NORTH), new OrbPath(new Location(3677, 5398, 1), Direction.NORTH_EAST), new OrbPath(new Location(3677, 5417, 1), Direction.SOUTH), new OrbPath(new Location(3677, 5417, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3677, 5417, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3678, 5397, 1), Direction.NORTH_EAST), new OrbPath(new Location(3678, 5398, 1), Direction.NORTH_WEST), new OrbPath(new Location(3678, 5398, 1), Direction.NORTH), new OrbPath(new Location(3678, 5398, 1), Direction.NORTH_EAST), new OrbPath(new Location(3678, 5398, 1), Direction.EAST), new OrbPath(new Location(3678, 5417, 1), Direction.SOUTH), new OrbPath(new Location(3678, 5417, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3678, 5417, 1), Direction.EAST), new OrbPath(new Location(3678, 5417, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3678, 5418, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3679, 5397, 1), Direction.NORTH), new OrbPath(new Location(3679, 5397, 1), Direction.NORTH_EAST), new OrbPath(new Location(3679, 5418, 1), Direction.SOUTH), new OrbPath(new Location(3679, 5418, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3680, 5397, 1), Direction.NORTH_WEST), new OrbPath(new Location(3680, 5397, 1), Direction.NORTH), new OrbPath(new Location(3680, 5397, 1), Direction.NORTH_EAST), new OrbPath(new Location(3680, 5418, 1), Direction.SOUTH), new OrbPath(new Location(3680, 5418, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3680, 5418, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3681, 5397, 1), Direction.NORTH_WEST), new OrbPath(new Location(3681, 5397, 1), Direction.NORTH), new OrbPath(new Location(3681, 5397, 1), Direction.NORTH_EAST), new OrbPath(new Location(3681, 5418, 1), Direction.SOUTH), new OrbPath(new Location(3681, 5418, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3681, 5418, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3682, 5397, 1), Direction.NORTH_WEST), new OrbPath(new Location(3682, 5397, 1), Direction.NORTH), new OrbPath(new Location(3682, 5418, 1), Direction.SOUTH), new OrbPath(new Location(3682, 5418, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3683, 5397, 1), Direction.NORTH_WEST), new OrbPath(new Location(3683, 5398, 1), Direction.WEST), new OrbPath(new Location(3683, 5398, 1), Direction.NORTH_WEST), new OrbPath(new Location(3683, 5398, 1), Direction.NORTH), new OrbPath(new Location(3683, 5398, 1), Direction.NORTH_EAST), new OrbPath(new Location(3683, 5417, 1), Direction.SOUTH), new OrbPath(new Location(3683, 5417, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3683, 5417, 1), Direction.WEST), new OrbPath(new Location(3683, 5417, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3683, 5418, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3684, 5398, 1), Direction.NORTH_WEST), new OrbPath(new Location(3684, 5398, 1), Direction.NORTH), new OrbPath(new Location(3684, 5398, 1), Direction.NORTH_EAST), new OrbPath(new Location(3684, 5417, 1), Direction.SOUTH), new OrbPath(new Location(3684, 5417, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3684, 5417, 1), Direction.SOUTH_EAST), new OrbPath(new Location(3685, 5398, 1), Direction.NORTH_WEST), new OrbPath(new Location(3685, 5398, 1), Direction.NORTH), new OrbPath(new Location(3685, 5417, 1), Direction.SOUTH), new OrbPath(new Location(3685, 5417, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3686, 5398, 1), Direction.NORTH_WEST), new OrbPath(new Location(3686, 5399, 1), Direction.WEST), new OrbPath(new Location(3686, 5399, 1), Direction.NORTH_WEST), new OrbPath(new Location(3686, 5399, 1), Direction.NORTH), new OrbPath(new Location(3686, 5416, 1), Direction.SOUTH), new OrbPath(new Location(3686, 5416, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3686, 5416, 1), Direction.WEST), new OrbPath(new Location(3686, 5417, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3687, 5399, 1), Direction.NORTH_WEST), new OrbPath(new Location(3687, 5400, 1), Direction.WEST), new OrbPath(new Location(3687, 5400, 1), Direction.NORTH_WEST), new OrbPath(new Location(3687, 5400, 1), Direction.NORTH), new OrbPath(new Location(3687, 5415, 1), Direction.SOUTH), new OrbPath(new Location(3687, 5415, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3687, 5415, 1), Direction.WEST), new OrbPath(new Location(3687, 5416, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3688, 5400, 1), Direction.NORTH_WEST), new OrbPath(new Location(3688, 5401, 1), Direction.WEST), new OrbPath(new Location(3688, 5401, 1), Direction.NORTH_WEST), new OrbPath(new Location(3688, 5401, 1), Direction.NORTH), new OrbPath(new Location(3688, 5414, 1), Direction.SOUTH), new OrbPath(new Location(3688, 5414, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3688, 5414, 1), Direction.WEST), new OrbPath(new Location(3688, 5415, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3689, 5401, 1), Direction.NORTH_WEST), new OrbPath(new Location(3689, 5402, 1), Direction.WEST), new OrbPath(new Location(3689, 5402, 1), Direction.NORTH_WEST), new OrbPath(new Location(3689, 5402, 1), Direction.NORTH), new OrbPath(new Location(3689, 5413, 1), Direction.SOUTH), new OrbPath(new Location(3689, 5413, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3689, 5413, 1), Direction.WEST), new OrbPath(new Location(3689, 5414, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3690, 5402, 1), Direction.NORTH_WEST), new OrbPath(new Location(3690, 5403, 1), Direction.WEST), new OrbPath(new Location(3690, 5403, 1), Direction.NORTH_WEST), new OrbPath(new Location(3690, 5404, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3690, 5404, 1), Direction.WEST), new OrbPath(new Location(3690, 5404, 1), Direction.NORTH_WEST), new OrbPath(new Location(3690, 5405, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3690, 5405, 1), Direction.WEST), new OrbPath(new Location(3690, 5405, 1), Direction.NORTH_WEST), new OrbPath(new Location(3690, 5405, 1), Direction.NORTH), new OrbPath(new Location(3690, 5410, 1), Direction.SOUTH), new OrbPath(new Location(3690, 5410, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3690, 5410, 1), Direction.WEST), new OrbPath(new Location(3690, 5410, 1), Direction.NORTH_WEST), new OrbPath(new Location(3690, 5411, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3690, 5411, 1), Direction.WEST), new OrbPath(new Location(3690, 5411, 1), Direction.NORTH_WEST), new OrbPath(new Location(3690, 5412, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3690, 5412, 1), Direction.WEST), new OrbPath(new Location(3690, 5413, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3691, 5405, 1), Direction.NORTH_WEST), new OrbPath(new Location(3691, 5406, 1), Direction.WEST), new OrbPath(new Location(3691, 5406, 1), Direction.NORTH_WEST), new OrbPath(new Location(3691, 5407, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3691, 5407, 1), Direction.WEST), new OrbPath(new Location(3691, 5407, 1), Direction.NORTH_WEST), new OrbPath(new Location(3691, 5408, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3691, 5408, 1), Direction.WEST), new OrbPath(new Location(3691, 5408, 1), Direction.NORTH_WEST), new OrbPath(new Location(3691, 5409, 1), Direction.SOUTH_WEST), new OrbPath(new Location(3691, 5409, 1), Direction.WEST), new OrbPath(new Location(3691, 5410, 1), Direction.SOUTH_WEST)};
	private static final Location[][] QUADRANT_TILES = {
			{new Location(3681, 5408, 1), new Location(3681, 5409, 1), new Location(3681, 5410, 1), new Location(3681, 5411, 1), new Location(3681, 5412, 1), new Location(3681, 5413, 1), new Location(3681, 5414, 1), new Location(3681, 5415, 1), new Location(3681, 5416, 1), new Location(3681, 5417, 1), new Location(3682, 5408, 1), new Location(3682, 5409, 1), new Location(3682, 5410, 1), new Location(3682, 5411, 1), new Location(3682, 5412, 1), new Location(3682, 5413, 1), new Location(3682, 5414, 1), new Location(3682, 5415, 1), new Location(3682, 5416, 1), new Location(3682, 5417, 1), new Location(3683, 5408, 1), new Location(3683, 5409, 1), new Location(3683, 5410, 1), new Location(3683, 5411, 1), new Location(3683, 5412, 1), new Location(3683, 5413, 1), new Location(3683, 5414, 1), new Location(3683, 5415, 1), new Location(3683, 5416, 1), new Location(3684, 5408, 1), new Location(3684, 5409, 1), new Location(3684, 5410, 1), new Location(3684, 5411, 1), new Location(3684, 5412, 1), new Location(3684, 5413, 1), new Location(3684, 5414, 1), new Location(3684, 5415, 1), new Location(3684, 5416, 1), new Location(3685, 5408, 1), new Location(3685, 5409, 1), new Location(3685, 5410, 1), new Location(3685, 5411, 1), new Location(3685, 5412, 1), new Location(3685, 5413, 1), new Location(3685, 5414, 1), new Location(3685, 5415, 1), new Location(3685, 5416, 1), new Location(3686, 5408, 1), new Location(3686, 5409, 1), new Location(3686, 5410, 1), new Location(3686, 5411, 1), new Location(3686, 5412, 1), new Location(3686, 5413, 1), new Location(3686, 5414, 1), new Location(3686, 5415, 1), new Location(3687, 5408, 1), new Location(3687, 5409, 1), new Location(3687, 5410, 1), new Location(3687, 5411, 1), new Location(3687, 5412, 1), new Location(3687, 5413, 1), new Location(3687, 5414, 1), new Location(3688, 5408, 1), new Location(3688, 5409, 1), new Location(3688, 5410, 1), new Location(3688, 5411, 1), new Location(3688, 5412, 1), new Location(3688, 5413, 1), new Location(3689, 5408, 1), new Location(3689, 5409, 1), new Location(3689, 5410, 1), new Location(3689, 5411, 1), new Location(3689, 5412, 1), new Location(3690, 5408, 1), new Location(3690, 5409, 1)},
			{new Location(3681, 5398, 1), new Location(3681, 5399, 1), new Location(3681, 5400, 1), new Location(3681, 5401, 1), new Location(3681, 5402, 1), new Location(3681, 5403, 1), new Location(3681, 5404, 1), new Location(3681, 5405, 1), new Location(3681, 5406, 1), new Location(3681, 5407, 1), new Location(3682, 5398, 1), new Location(3682, 5399, 1), new Location(3682, 5400, 1), new Location(3682, 5401, 1), new Location(3682, 5402, 1), new Location(3682, 5403, 1), new Location(3682, 5404, 1), new Location(3682, 5405, 1), new Location(3682, 5406, 1), new Location(3682, 5407, 1), new Location(3683, 5399, 1), new Location(3683, 5400, 1), new Location(3683, 5401, 1), new Location(3683, 5402, 1), new Location(3683, 5403, 1), new Location(3683, 5404, 1), new Location(3683, 5405, 1), new Location(3683, 5406, 1), new Location(3683, 5407, 1), new Location(3684, 5399, 1), new Location(3684, 5400, 1), new Location(3684, 5401, 1), new Location(3684, 5402, 1), new Location(3684, 5403, 1), new Location(3684, 5404, 1), new Location(3684, 5405, 1), new Location(3684, 5406, 1), new Location(3684, 5407, 1), new Location(3685, 5399, 1), new Location(3685, 5400, 1), new Location(3685, 5401, 1), new Location(3685, 5402, 1), new Location(3685, 5403, 1), new Location(3685, 5404, 1), new Location(3685, 5405, 1), new Location(3685, 5406, 1), new Location(3685, 5407, 1), new Location(3686, 5400, 1), new Location(3686, 5401, 1), new Location(3686, 5402, 1), new Location(3686, 5403, 1), new Location(3686, 5404, 1), new Location(3686, 5405, 1), new Location(3686, 5406, 1), new Location(3686, 5407, 1), new Location(3687, 5401, 1), new Location(3687, 5402, 1), new Location(3687, 5403, 1), new Location(3687, 5404, 1), new Location(3687, 5405, 1), new Location(3687, 5406, 1), new Location(3687, 5407, 1), new Location(3688, 5402, 1), new Location(3688, 5403, 1), new Location(3688, 5404, 1), new Location(3688, 5405, 1), new Location(3688, 5406, 1), new Location(3688, 5407, 1), new Location(3689, 5403, 1), new Location(3689, 5404, 1), new Location(3689, 5405, 1), new Location(3689, 5406, 1), new Location(3689, 5407, 1), new Location(3690, 5406, 1), new Location(3690, 5407, 1)},
			{new Location(3671, 5406, 1), new Location(3671, 5407, 1), new Location(3672, 5403, 1), new Location(3672, 5404, 1), new Location(3672, 5405, 1), new Location(3672, 5406, 1), new Location(3672, 5407, 1), new Location(3673, 5402, 1), new Location(3673, 5403, 1), new Location(3673, 5404, 1), new Location(3673, 5405, 1), new Location(3673, 5406, 1), new Location(3673, 5407, 1), new Location(3674, 5401, 1), new Location(3674, 5402, 1), new Location(3674, 5403, 1), new Location(3674, 5404, 1), new Location(3674, 5405, 1), new Location(3674, 5406, 1), new Location(3674, 5407, 1), new Location(3675, 5400, 1), new Location(3675, 5401, 1), new Location(3675, 5402, 1), new Location(3675, 5403, 1), new Location(3675, 5404, 1), new Location(3675, 5405, 1), new Location(3675, 5406, 1), new Location(3675, 5407, 1), new Location(3676, 5399, 1), new Location(3676, 5400, 1), new Location(3676, 5401, 1), new Location(3676, 5402, 1), new Location(3676, 5403, 1), new Location(3676, 5404, 1), new Location(3676, 5405, 1), new Location(3676, 5406, 1), new Location(3676, 5407, 1), new Location(3677, 5399, 1), new Location(3677, 5400, 1), new Location(3677, 5401, 1), new Location(3677, 5402, 1), new Location(3677, 5403, 1), new Location(3677, 5404, 1), new Location(3677, 5405, 1), new Location(3677, 5406, 1), new Location(3677, 5407, 1), new Location(3678, 5399, 1), new Location(3678, 5400, 1), new Location(3678, 5401, 1), new Location(3678, 5402, 1), new Location(3678, 5403, 1), new Location(3678, 5404, 1), new Location(3678, 5405, 1), new Location(3678, 5406, 1), new Location(3678, 5407, 1), new Location(3679, 5398, 1), new Location(3679, 5399, 1), new Location(3679, 5400, 1), new Location(3679, 5401, 1), new Location(3679, 5402, 1), new Location(3679, 5403, 1), new Location(3679, 5404, 1), new Location(3679, 5405, 1), new Location(3679, 5406, 1), new Location(3679, 5407, 1), new Location(3680, 5398, 1), new Location(3680, 5399, 1), new Location(3680, 5400, 1), new Location(3680, 5401, 1), new Location(3680, 5402, 1), new Location(3680, 5403, 1), new Location(3680, 5404, 1), new Location(3680, 5405, 1), new Location(3680, 5406, 1), new Location(3680, 5407, 1)},
			{new Location(3671, 5408, 1), new Location(3671, 5409, 1), new Location(3672, 5408, 1), new Location(3672, 5409, 1), new Location(3672, 5410, 1), new Location(3672, 5411, 1), new Location(3672, 5412, 1), new Location(3673, 5408, 1), new Location(3673, 5409, 1), new Location(3673, 5410, 1), new Location(3673, 5411, 1), new Location(3673, 5412, 1), new Location(3673, 5413, 1), new Location(3674, 5408, 1), new Location(3674, 5409, 1), new Location(3674, 5410, 1), new Location(3674, 5411, 1), new Location(3674, 5412, 1), new Location(3674, 5413, 1), new Location(3674, 5414, 1), new Location(3675, 5408, 1), new Location(3675, 5409, 1), new Location(3675, 5410, 1), new Location(3675, 5411, 1), new Location(3675, 5412, 1), new Location(3675, 5413, 1), new Location(3675, 5414, 1), new Location(3675, 5415, 1), new Location(3676, 5408, 1), new Location(3676, 5409, 1), new Location(3676, 5410, 1), new Location(3676, 5411, 1), new Location(3676, 5412, 1), new Location(3676, 5413, 1), new Location(3676, 5414, 1), new Location(3676, 5415, 1), new Location(3676, 5416, 1), new Location(3677, 5408, 1), new Location(3677, 5409, 1), new Location(3677, 5410, 1), new Location(3677, 5411, 1), new Location(3677, 5412, 1), new Location(3677, 5413, 1), new Location(3677, 5414, 1), new Location(3677, 5415, 1), new Location(3677, 5416, 1), new Location(3678, 5408, 1), new Location(3678, 5409, 1), new Location(3678, 5410, 1), new Location(3678, 5411, 1), new Location(3678, 5412, 1), new Location(3678, 5413, 1), new Location(3678, 5414, 1), new Location(3678, 5415, 1), new Location(3678, 5416, 1), new Location(3679, 5408, 1), new Location(3679, 5409, 1), new Location(3679, 5410, 1), new Location(3679, 5411, 1), new Location(3679, 5412, 1), new Location(3679, 5413, 1), new Location(3679, 5414, 1), new Location(3679, 5415, 1), new Location(3679, 5416, 1), new Location(3679, 5417, 1), new Location(3680, 5408, 1), new Location(3680, 5409, 1), new Location(3680, 5410, 1), new Location(3680, 5411, 1), new Location(3680, 5412, 1), new Location(3680, 5413, 1), new Location(3680, 5414, 1), new Location(3680, 5415, 1), new Location(3680, 5416, 1), new Location(3680, 5417, 1)}
	};
	private static final SoundEffect TRAIL_MOVE_SOUND = new SoundEffect(5722, 4);
	private static final SoundEffect FIRE_HIT_SOUND = new SoundEffect(4827);
	private static final SoundEffect QUADRANT_SOUND = new SoundEffect(5727);
	private static final Graphics FIRE_HIT_GFX = new Graphics(128, 0, 124);
	private final boolean feelingSpecial;
	private long lastQuadrantSound;
	private Akkha akkha;
	private List<TrailOrb> trailOrbList = new ArrayList<>();
	private AkkhaShadow[] shadows = new AkkhaShadow[4];
	private AkkhaFinalShadow[] finalShadows = new AkkhaFinalShadow[4];
	private int lastPhaseIndex = -1;
	private int unstableTicks = -1;
	private List<UnstableOrb> unstableOrbs = new ArrayList<>();

	public AkkhaEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
		super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
		feelingSpecial = party.getPartySettings().isActive(InvocationType.FEELING_SPECIAL);
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
		spawnAkkha();
	}

	@Override public void onRoomStart() {
		akkha.setMaxHealth();
		players.forEach(p -> {
			p.getHpHud().open(akkha.getId(), akkha.getMaxHitpoints());
		});
	}

	@Override public void onRoomEnd() {
		players.forEach(p -> p.getHpHud().close());
		resetNpcs();
		party.getPathsCompleted().add(TOAPathType.HET);
		spawnTeleportNPC();
	}

	@Override public void onRoomReset() {
		players.forEach(p -> p.getHpHud().close());
		spawnAkkha();
		resetNpcs();
		lastPhaseIndex = -1;
		unstableTicks = -1;
	}

	private void resetNpcs() {
		trailOrbList.removeIf(orb -> {
			if (!orb.isFinished()) {
				orb.finish();
			}
			return true;
		});
		for (Player p : players) {
			if (p != null) {
				p.getTemporaryAttributes().remove("akkha_shadow_time");
				p.getTemporaryAttributes().remove("akkha_lightening_time");
				p.getTemporaryAttributes().remove("akkha_icing_time");
				p.getTemporaryAttributes().remove("akkha_fire_time");
				for (int id : TRAIL_ORB_NPC_IDS) {
					p.getTemporaryAttributes().remove("mark_akkha_trail_orb_explosion_" + id);
				}
			}
		}
		for (AkkhaShadow shadow : shadows) {
			if (shadow != null && !shadow.isDead() && !shadow.isFinished()) {
				shadow.finish();
			}
		}
		shadows = new AkkhaShadow[4];
		for (AkkhaFinalShadow shadow : finalShadows) {
			if (shadow != null && !shadow.isDead() && !shadow.isFinished()) {
				shadow.finish();
			}
		}
		finalShadows = new AkkhaFinalShadow[4];
		unstableOrbs.removeIf(orb -> {
			if (!orb.isFinished()) {
				orb.finish();
			}
			return true;
		});
	}

	public void spawnShadows(boolean firstTime) {
		final Player[] players = getChallengePlayers();
		for (Player p : players) {
			if (p != null) {
				p.sendMessage(firstTime ? "<col=ef0083>Shadows appear throughout the room!</col>" : "<col=ef0083>The shadows in the room are restored!</col>");
			}
		}
		for (int i = 0; i < 4; i++) {
			if (shadows[i] == null || shadows[i].isDying() || shadows[i].isFinished()) {
				shadows[i] = new AkkhaShadow(getLocation(SHADOW_LOCATIONS[i]), Direction.values[(5 + (i * 2)) % Direction.values.length],
						this, party.getBossLevels()[TOAPathType.HET.ordinal()], i, akkha);
				shadows[i].spawn();
				if (!firstTime) {
					shadows[i].setVulnerableType(2);
				}
			} else {
				shadows[i].setHitpoints(shadows[i].getMaxHitpoints());
				shadows[i].resetCounter();
				shadows[i].setCounting(true);
				if (shadows[i].getVulnerableType() != 2) {
					shadows[i].setVulnerableType(0);
				}
			}
		}
	}

	public void onShadowFinish(int index) {
		for (int i = 0; i < shadows.length; i++) {
			if (i != index && !shadows[i].isDying() && !shadows[i].isFinished() && shadows[i].getVulnerableType() != 2) {
				shadows[i].setVulnerableType(1);
			}
		}
	}

	public void sendFakeShadowDeaths() {
		for (AkkhaFinalShadow shadow : finalShadows) {
			if (shadow != null && !shadow.isDying() && !shadow.isFinished() && shadow.getId() == FAKE_SHADOW_ID) {
				shadow.sendDeath();
			}
		}
	}

	public void teleportFakeShadows() {
		if (lastPhaseIndex == -1 || finalShadows[lastPhaseIndex] == null) {
			return;
		}
		final int newIndex = IntStream.rangeClosed(0, 3).filter(i -> i != lastPhaseIndex).toArray()[Utils.random(2)];
		if (finalShadows[newIndex] == null) {
			return;
		}
		finalShadows[lastPhaseIndex].setCantInteract(true);
		finalShadows[lastPhaseIndex].lock(1);
		finalShadows[lastPhaseIndex].setAnimation(Akkha.BECOME_INVISIBLE_ANIM);
		finalShadows[newIndex].setAnimation(Akkha.BECOME_INVISIBLE_ANIM);
		WorldTasksManager.schedule(addRunningTask(() -> {
			if (EncounterStage.STARTED.equals(stage) && !finalShadows[lastPhaseIndex].isDying() && !finalShadows[lastPhaseIndex].isFinished()
					&& !finalShadows[newIndex].isDying() && !finalShadows[newIndex].isFinished()) {
				final Location oldLocation = finalShadows[lastPhaseIndex].getLocation();
				final Location newLocation = finalShadows[newIndex].getLocation();
				final Direction oldDirection = Direction.values[(1 + (lastPhaseIndex * 2)) % Direction.values.length];
				final Direction newDirection = Direction.values[(1 + (newIndex * 2)) % Direction.values.length];
				final AkkhaFinalShadow oldShadow = finalShadows[lastPhaseIndex];
				finalShadows[lastPhaseIndex].setLocation(new Location(newLocation));
				finalShadows[newIndex].setLocation(new Location(oldLocation));
				finalShadows[lastPhaseIndex].setAnimation(Akkha.BECOME_VISIBLE_ANIM);
				finalShadows[newIndex].setAnimation(Akkha.BECOME_VISIBLE_ANIM);
				finalShadows[lastPhaseIndex].setCantInteract(false);
				finalShadows[lastPhaseIndex].setFaceLocation(finalShadows[lastPhaseIndex].getLocation().transform(newDirection.getOffsetX() * 5, newDirection.getOffsetY() * 5));
				finalShadows[newIndex].setFaceLocation(finalShadows[newIndex].getLocation().transform(oldDirection.getOffsetX() * 5, oldDirection.getOffsetY() * 5));
				finalShadows[lastPhaseIndex].resetCurrentReceivedHits();
				finalShadows[lastPhaseIndex] = finalShadows[newIndex];
				finalShadows[newIndex] = oldShadow;
				lastPhaseIndex = newIndex;
			}
		}));
	}

	private void spawnUnstableOrbs() {
		final List<OrbPath> orbs = new ArrayList<>(Arrays.asList(UNSTABLE_ORB_PATHS));
		Collections.shuffle(orbs);
		for (int i = 0; i < 6; i++) {
			final OrbPath path = orbs.get(i);
			final UnstableOrb orb = new UnstableOrb(getLocation(path.location), path.direction, this);
			orb.spawn();
			unstableOrbs.add(orb);
		}
	}

	public boolean onVulnerableQuadrant(Location location) {
		final int index = getQuadrantIndex(location);
		return shadows[index] != null && !shadows[index].isDying() && !shadows[index].isFinished();
	}

	private void spawnAkkha() {
		if (akkha != null) {
			akkha.finish();
		}
		akkha = new Akkha(this, party.getBossLevels()[TOAPathType.HET.ordinal()]);
		akkha.spawn();
	}

	public void sendQuadrantBlasts(boolean shadow, int[] quadrants) {
		if (WorldThread.getCurrentCycle() > lastQuadrantSound) {
			lastQuadrantSound = WorldThread.getCurrentCycle();
			final Player[] players = getChallengePlayers();
			for (Player p : players) {
				if (p != null) {
					p.sendSound(QUADRANT_SOUND);
				}
			}
		}
		for (int i : quadrants) {
			final int index = i;
			final Location baseTile = getLocation(QUADRANT_START_TILES[index]);
			for (Location loc : QUADRANT_TILES[index]) {
				final Location tile = getLocation(loc);
				final int distance = (tile.getTileDistance(baseTile) * 3);
				World.sendGraphics(new Graphics(QUADRANT_GFX_IDS[index], 30 + (shadow ? 27 - distance : distance), index == 3 ? 124 : 0), tile);
			}
			WorldTasksManager.schedule(addRunningTask(new WorldTask() {
				int ticks = -1;
				@Override public void run() {
					if (++ticks > 1) {
						stop();
						return;
					}
					for (Location loc : QUADRANT_TILES[index]) {
						final Location tile = getLocation(loc);
						final int distance = tile.getTileDistance(baseTile) * 3;
						final int delay = shadow ? 27 - distance : distance;
						if ((ticks == 0 && delay >= 18) || (ticks == 1 && delay < 18)) {
							continue;
						}
						for (Player p : getChallengePlayers()) {
							if (p.getLocation().equals(tile)) {
								p.applyHit(new Hit(akkha, akkha.getMaxHit(14) + Utils.random(2), HitType.DEFAULT));
								addEffect(p, index);
							}
						}
					}
				}
			}), 0, 0);
		}
	}

	public void removeTrailOrb(TrailOrb trailOrb) {
		trailOrbList.remove(trailOrb);
	}

	public void spawnTrailOrb(final Location location) {
		if (World.isFloorFree(location, 1) && trailOrbList.stream().map(AbstractEntity::getLocation).noneMatch(loc -> loc.equals(location))) {
			final TrailOrb trailOrb = new TrailOrb(TRAIL_ORB_NPC_IDS[getQuadrantIndex(location)], location, this);
			trailOrb.spawn();
			trailOrbList.add(trailOrb);
		}
	}

	public int getQuadrantIndex(final Location location) {
		final Location baseLocation = getLocation(QUADRANT_BASE_LOCATION);
		if (location.getX() >= baseLocation.getX()) {
			return location.getY() >= baseLocation.getY() ? 0 : 1;
		} else {
			return location.getY() >= baseLocation.getY() ? 3 : 2;
		}
	}

	@Override public void process() {
		if (EncounterStage.STARTED.equals(stage)) {
			if (lastPhaseIndex != -1 && unstableTicks > 0 && --unstableTicks <= 0) {
				unstableTicks = 3;
				spawnUnstableOrbs();
			}
			if (akkha != null && !akkha.isFinished() && !akkha.isDying()) {
				final Player[] challengePlayers = getChallengePlayers();
				for (Player p : challengePlayers) {
					if (p != null) {
						for (int index = 0; index < TRAIL_ORB_NPC_IDS.length; index++) {
							if (p.getTemporaryAttributes().remove("mark_akkha_trail_orb_explosion_" + TRAIL_ORB_NPC_IDS[index], true)) {
								p.applyHit(new Hit(akkha, akkha.getMaxHit(13) + Utils.random(2), HitType.DEFAULT));
								p.sendSound(Akkha.QUADRANT_HIT_SOUNDS[index]);
								p.setGraphics(new Graphics(QUADRANT_GFX_IDS[index], 0, index == 3 ? 124 : 0));
								addEffect(p, index);
								break;
							}
						}
					}
				}
			}
		}
	}

	public void startLastPhase() {
		lastPhaseIndex = Utils.random(3);
		for (int i = 0; i < 4; i++) {
			finalShadows[i] = new AkkhaFinalShadow(lastPhaseIndex == i ? Akkha.FINAL_ID : FAKE_SHADOW_ID, getLocation(SHADOW_LOCATIONS[i]),
					Direction.values[(1 + (i * 2)) % Direction.values.length], this, akkha.getMaxHitpoints());
			finalShadows[i].spawn();
		}
		getPlayers().forEach(p -> {
			p.getHpHud().updateValue((int) (akkha.getMaxHitpoints() * 0.2));
			p.setTinting(Akkha.RESET_TINT);
		});
		unstableTicks = 3;
		akkha.resetEffects();
	}

	public void addEffect(Player player, int quadrant) {
		switch(quadrant) {
			case 0 -> addShadowEffect(player);
			case 1 -> addLighteningEffect(player);
			case 2 -> addFreezeEffect(player);
			case 3 -> addBlastEffect(player);
		}
	}

	private void addShadowEffect(Player player) {
		long time = (long) player.getTemporaryAttributes().getOrDefault("akkha_shadow_time", 0L);
		if (WorldThread.getCurrentCycle() > time + 4) {
			player.getTemporaryAttributes().put("akkha_shadow_time", WorldThread.getCurrentCycle());
			player.getPacketDispatcher().sendGameMessage("<col=ff3045>You have been consumed by darkness!</col>", true);
			WorldTasksManager.schedule(addRunningTask(new WorldTask() {
				int ticks;
				@Override public void run() {
					if (!checkEffectConditions(player) || ticks++ >= 4) {
						stop();
						return;
					}
					player.applyHit(new Hit(akkha, 5, HitType.DEFAULT));
				}
			}), 0, 0);
		}
	}

	private void addLighteningEffect(Player player) {
		long time = (long) player.getTemporaryAttributes().getOrDefault("akkha_lightening_time", 0L);
		if (WorldThread.getCurrentCycle() > time + 6) {
			player.getTemporaryAttributes().put("akkha_lightening_time", WorldThread.getCurrentCycle());
			player.getPacketDispatcher().sendGameMessage("<col=ff3045>You have been struck by lightning and can't use protection prayers!</col>", true);
			player.getTemporaryAttributes().put("prayer delay", System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));
			player.getPrayerManager().deactivatePrayer(Prayer.PROTECT_FROM_MAGIC);
			player.getPrayerManager().deactivatePrayer(Prayer.PROTECT_FROM_MELEE);
			player.getPrayerManager().deactivatePrayer(Prayer.PROTECT_FROM_MISSILES);
		}
	}

	private void addFreezeEffect(Player player) {
		long time = (long) player.getTemporaryAttributes().getOrDefault("akkha_icing_time", 0L);
		if (WorldThread.getCurrentCycle() > time + 25) {
			player.getTemporaryAttributes().put("akkha_icing_time", WorldThread.getCurrentCycle());
			player.getPacketDispatcher().sendGameMessage("<col=ff3045>Your attacks have been slowed by some ice!</col>", true);
			WorldTasksManager.schedule(addRunningTask(() -> {
				if (checkEffectConditions(player)) {
					player.getPacketDispatcher().sendGameMessage("<col=229628>The ice around you melts away.</col>", true);
				}
			}), 24);
		}
	}

	private void addBlastEffect(Player player) {
		long time = (long) player.getTemporaryAttributes().getOrDefault("akkha_fire_time", 0L);
		if (WorldThread.getCurrentCycle() > time + 30) {
			player.setForceTalk("Argh! It burns!");
			player.getTemporaryAttributes().put("akkha_fire_time", WorldThread.getCurrentCycle());
			player.getPacketDispatcher().sendGameMessage("<col=ff3045>You have been set alight!</col>", true);
			WorldTasksManager.schedule(addRunningTask(new WorldTask() {
				int cycles;
				@Override public void run() {
					if (!checkEffectConditions(player)) {
						stop();
						return;
					}
					player.sendSound(FIRE_HIT_SOUND);
					player.applyHit(new Hit(akkha, 3, HitType.DEFAULT));
					player.setGraphics(FIRE_HIT_GFX);
					for (Player p : getChallengePlayers()) {
						if (p != null && !p.getUsername().equals(player.getUsername()) && p.getLocation().getTileDistance(p.getLocation()) <= 1) {
							addBlastEffect(p);
						}
					}
					if (cycles++ >= 4) {
						player.getPacketDispatcher().sendGameMessage("<col=229628>The fire around you goes out.</col>", true);
						stop();
					}

				}
			}), 1, 2);
		}
	}

	private boolean checkEffectConditions(Player player) {
		return EncounterStage.STARTED.equals(stage) && !player.isDying() && !player.isFinished() && insideChallengeArea(player);
	}

	@Override public boolean processMovement(Player player, int x, int y) {
		if (insideChallengeArea(player) && !player.isDying() && akkha != null && akkha.isTrailing()) {
			final Object locationObject = player.getTemporaryAttributes().get("akkha_last_player_location");
			final Location newLocation = new Location(x, y, 1);
			if (locationObject instanceof final Location previousLocation && !newLocation.equals(previousLocation)) {
				player.sendSound(TRAIL_MOVE_SOUND);
				spawnTrailOrb(previousLocation);
				if (feelingSpecial) {
					final Direction direction = Direction.getDirection(previousLocation, newLocation);
					spawnTrailOrb(newLocation.transform(direction.getOffsetX(), direction.getOffsetY()));
				}
				player.getTemporaryAttributes().put("akkha_last_player_location", newLocation);
			}
		}
		return true;
	}

	public void removeUnstableOrb(UnstableOrb orb) { unstableOrbs.remove(orb); }

	public AkkhaShadow[] getShadows() { return shadows; }

	public Akkha getAkkha() { return akkha; }

	static class OrbPath {
		final Location location;
		final Direction direction;

		OrbPath(Location location, Direction direction) {
			this.location = location;
			this.direction = direction;
		}
	}
}