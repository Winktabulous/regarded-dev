package com.zenyte.game.world.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.object.LadderOA;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum containing all the special ladders in the game that don't follow the generic rule of height +- 1.
 * 
 * @author Kris | 19. veebr 2018 : 17:08.32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Ladder {

	EDGEVILLE_UNDERGROUND(new LadderObject(17385, new Location(3097, 9867, 0)), new Location(3102, 3492, 0)),
    APE_ATOLL_TEMPLE_BAMBOO_LADDER_SPECIAL1(new LadderObject(4773, new Location(2789, 2784, 1)), new Location(2788, 2784, 2)),
    APE_ATOLL_TEMPLE_BAMBOO_LADDER_SPECIAL2(new LadderObject(4773, new Location(2787, 2795, 1)), new Location(2787, 2794, 2)),
	APE_ATOLL_TEMPLE_BAMBOO_LADDER_SPECIAL3(new LadderObject(4774, new Location(2729, 2766, 0)), new Location(2730, 2766, 2)),
	APE_ATOLL_TEMPLE_BAMBOO_LADDER_SPECIAL4(new LadderObject(4776, new Location(2729, 2766, 2)), new Location(2730, 2766, 0)),
	APE_ATOLL_TEMPLE_BAMBOO_LADDER_SPECIAL5(new LadderObject(4775, new Location(2713, 2766, 0)), new Location(2712, 2766, 2)),
	APE_ATOLL_TEMPLE_BAMBOO_LADDER_SPECIAL6(new LadderObject(4777, new Location(2713, 2766, 2)), new Location(2712, 2766, 0)),
    APE_ATOLL_ROPE(new LadderObject(4881, new Location(2808, 9201, 0)), new Location(2806, 2785, 0)),

	FALADOR_UPSTAIRS_LADDER(new LadderObject(24082, new Location(3050, 3355, 1)), new Location(3050, 3354, 2)),
	FALADOR_DOWNSTAIRS_LADDER(new LadderObject(24084, new Location(3050, 3355, 2)), new Location(3050, 3356, 1)),

	FARMING_GUILD_WEST_LADDER_UP(new LadderObject(34477, new Location(1224, 3755, 0)), new Location(1225, 3755, 1)),
    FARMING_GUILD_WEST_LADDER_DOWN(new LadderObject(34478, new Location(1224, 3755, 1)), new Location(1223, 3755, 0)),

    ECTOFUNTUS_LADDER_UP(new LadderObject(16110, new Location(3668, 9888, 3)), new Location(3654, 3519, 0)),

    DORGESH_KAAN(new LadderObject(22600, new Location(2716, 5241, 0)), new Location(2720, 5241, 3)),

    FARMING_GUILD_EAST_LADDER_UP(new LadderObject(34477, new Location(1233, 3755, 0)), new Location(1232, 3755, 1)),
    FARMING_GUILD_EAST_LADDER_DOWN(new LadderObject(34478, new Location(1233, 3755, 1)), new Location(1234, 3755, 0)),

    NEITIZNOT_EASTERN_LADDER_UP(new LadderObject(21395, new Location(2373, 3800, 0)), new Location(2372, 3800, 2)),
    NEITIZNOT_EASTERN_LADDER_DOWN(new LadderObject(21396, new Location(2373, 3800, 2)), new Location(2372, 3800, 0)),

    NEITIZNOT_WESTERN_LADDER_UP(new LadderObject(21512, new Location(2363, 3799, 0)), new Location(2364, 3799, 2)),
    NEITIZNOT_WESTERN_LADDER_DOWN(new LadderObject(21513, new Location(2363, 3799, 2)), new Location(2362, 3799, 0)),

	GOBLIN_VILLAGE_LADDER_UP(new LadderObject(16450, new Location(2954, 3497, 0)), new Location(2955, 3497, 2)),
	GOBLIN_VILLAGE_LADDER_DOWN(new LadderObject(16556, new Location(2954, 3497, 2)), new Location(2955, 3497, 0)),

	CRANDOR_CLIMBING_ROPE(new LadderObject(25213, new Location(2833, 9657, 0)), new Location(2834, 3258, 0)),

	SOPHANEM_DUNGEON_LADDER_DOWN(new LadderObject(20277, new Location(2799, 5159, 0)), new Location(3315, 2796, 0)),
    SOPHANEM_DUNGEON_LADDER_UP(new LadderObject(20575, new Location(3315, 2797, 0)), new Location(2799, 5160, 0)),
    SOPHANEM_DUNGEON_SECOND_LADDER_DOWN(new LadderObject(20278, new Location(2800, 5159, 0)), new Location(3318, 9273, 2)),
    SOPHANEM_DUNGEON_SECOND_LADDER_UP(new LadderObject(20284, new Location(3318, 9274, 2)), new Location(2800, 5160, 0)),

    HEROS_GUILD_LADDER(new LadderObject(17384, new Location(2892, 3507, 0)), new Location(2893, 9907, 0)),
    HEROS_GUILD_LADDER_UP(new LadderObject(17387, new Location(2892, 9907, 0)), new Location(2893, 3507, 0)),

    FOSSIL_ISLAND_WESTERN_LADDER(new LadderObject(30940, new Location(3746, 3831, 0)), new Location(3745, 3831, 1)),
    FOSSIL_ISLAND_WESTERN_LADDER_DOWN(new LadderObject(30941, new Location(3746, 3831, 1)), new Location(3747, 3831, 0)),
    FOSSIL_ISLAND_EASERN_LADDER(new LadderObject(30938, new Location(3729, 3831, 0)), new Location(3730, 3831, 1)),
    FOSSIL_ISLAND_EASERN_LADDER_DOWN(new LadderObject(30939, new Location(3729, 3831, 1)), new Location(3728, 3831, 0)),

    SOPHANEM_DUNGEON_L1DOWN(new LadderObject(20285, new Location(3286, 9274, 2)), new Location(3286, 9273, 0)),
    SOPHANEM_DUNGEON_L1UP(new LadderObject(20284, new Location(3286, 9274, 0)), new Location(3286, 9275, 2)),
    SOPHANEM_DUNGEON_L2DOWN(new LadderObject(20285, new Location(3271, 9274, 2)), new Location(3272, 9274, 0)),
    SOPHANEM_DUNGEON_L2UP(new LadderObject(20284, new Location(3271, 9274, 0)), new Location(3270, 9274, 2)),
    SOPHANEM_DUNGEON_L3DOWN(new LadderObject(20285, new Location(3280, 9255, 2)), new Location(3280, 9254, 0)),
    SOPHANEM_DUNGEON_L3UP(new LadderObject(20284, new Location(3280, 9255, 0)), new Location(3280, 3256, 2)),
    SOPHANEM_DUNGEON_L4DOWN(new LadderObject(20285, new Location(3317, 9250, 2)), new Location(3317, 9251, 0)),
    SOPHANEM_DUNGEON_L4UP(new LadderObject(20284, new Location(3317, 9250, 0)), new Location(3317, 9249, 2)),
    SOPHANEM_DUNGEON_L5DOWN(new LadderObject(20285, new Location(3323, 9241, 2)), new Location(3323, 9240, 0)),
    SOPHANEM_DUNGEON_L5UP(new LadderObject(20284, new Location(3323, 9241, 0)), new Location(3323, 9242, 2)),
    SOPHANEM_DUNGEON_L6DOWN(new LadderObject(20285, new Location(3271, 9235, 2)), new Location(3270, 9235, 0)),
    SOPHANEM_DUNGEON_L6UP(new LadderObject(20284, new Location(3271, 9235, 0)), new Location(3272, 9235, 2)),
    SOPHANEM_DUNGEON_L7DOWN(new LadderObject(20285, new Location(3267, 9221, 2)), new Location(3267, 9222, 0)),
    SOPHANEM_DUNGEON_L7UP(new LadderObject(20284, new Location(3267, 9221, 0)), new Location(3267, 9220, 2)),
    SOPHANEM_DUNGEON_L8DOWN(new LadderObject(20285, new Location(3317, 9224, 2)), new Location(3317, 9223, 0)),
    SOPHANEM_DUNGEON_L8UP(new LadderObject(20284, new Location(3317, 9224, 0)), new Location(3317, 9225, 2)),

	NEITIZNOT_SOUTHERN_LADDER_UP(new LadderObject(21514, new Location(2330, 3802, 0)), new Location(2329, 3802, 1)),
	NEITIZNOT_SOUTHERN_LADDER_DOWN(new LadderObject(21515, new Location(2330, 3802, 1)), new Location(2331, 3802, 0)),

	NEITIZNOT_NORTHERN_LADDER_UP(new LadderObject(21514, new Location(2330, 3810, 0)), new Location(2329, 3810, 1)),
	NEITIZNOT_NORTHERN_LADDER_DOWN(new LadderObject(21515, new Location(2330, 3810, 1)), new Location(2331, 3810, 0)),





	MYTHS_GUILD_UNDERGROUND(new LadderObject(32205, new Location(1937, 9009, 1)), new Location(2457, 2849, 0)),

    PIRATES_COVE_SOUTH_LADDER_UP(new LadderObject(16960, new Location(2213, 3795, 0)), new Location(2213, 3796, 1)),
    PIRATES_COVE_SOUTH_LADDER_DOWN(new LadderObject(16962, new Location(2213, 3795, 1)), new Location(2213, 3794, 0)),
    PIRATES_COVE_MIDDLE_LADDER_UP(new LadderObject(16959, new Location(2214, 3801, 1)), new Location(2214, 3800, 2)),
    PIRATES_COVE_MIDDLE_LADDER_DOWN(new LadderObject(16961, new Location(2214, 3801, 2)), new Location(2214, 3802, 1)),
    PIRATES_COVE_NORTH_LADDER_UP(new LadderObject(16960, new Location(2212, 3809, 0)), new Location(2213, 3809, 1)),
    PIRATES_COVE_NORTH_LADDER_DOWN(new LadderObject(16962, new Location(2212, 3809, 1)), new Location(2211, 3809, 0)),

    PIRATES_COVE_SHIP_BOTTOM_STAIRS(new LadderObject(16946, new Location(2225, 3808, 0)), new Location(2225, 3806, 1)),
    PIRATES_COVE_SHIP_BOTTOM_STAIRS_DOWN(new LadderObject(16948, new Location(2225, 3807, 1)), new Location(2225, 3809, 0)),

    PIRATES_COVE_SHIP_MIDDLE_STAIRS(new LadderObject(16945, new Location(2222, 3792, 1)), new Location(2220, 3792, 2)),
    PIRATES_COVE_SHIP_MIDDLE_MIDDLE_DOWN(new LadderObject(16947, new Location(2221, 3792, 2)), new Location(2223, 3792, 1)),

    PIRATES_COVE_SHIP_MIDDLE_STAIRS_ALT(new LadderObject(16945, new Location(2226, 3792, 1)), new Location(2228, 3792, 2)),
    PIRATES_COVE_SHIP_MIDDLE_MIDDLE_DOWN_ALT(new LadderObject(16947, new Location(2226, 3792, 2)), new Location(2225, 3792, 1)),


    PIRATES_COVE_SHIP_TOPSOUTH_STAIRS(new LadderObject(16945, new Location(2221, 3806, 2)), new Location(2221, 3808, 3)),
    PIRATES_COVE_SHIP_TOPSOUTH_MIDDLE_DOWN(new LadderObject(16947, new Location(2221, 3806, 3)), new Location(2221, 3805, 2)),
    PIRATES_COVE_SHIP_TOPSOUTH_STAIRS_ALT(new LadderObject(16945, new Location(2227, 3806, 2)), new Location(2227, 3808, 3)),
    PIRATES_COVE_SHIP_TOPSOUTH_MIDDLE_DOWN_ALT(new LadderObject(16947, new Location(2227, 3806, 3)), new Location(2227, 3805, 2)),

    PIRATES_COVE_SHIP_TOPNORTH_STAIRS(new LadderObject(16945, new Location(2221, 3794, 2)), new Location(2221, 3792, 3)),
    PIRATES_COVE_SHIP_TOPNORTH_MIDDLE_DOWN(new LadderObject(16947, new Location(2221, 3793, 3)), new Location(2221, 3795, 2)),
    PIRATES_COVE_SHIP_TOPNORTH_STAIRS_ALT(new LadderObject(16945, new Location(2227, 3794, 2)), new Location(2227, 3792, 3)),
    PIRATES_COVE_SHIP_TOPNORTH_MIDDLE_DOWN_ALT(new LadderObject(16947, new Location(2227, 3793, 3)), new Location(2227, 3795, 2)),


    BARBARIAN_COURSE_TOP_LADDER(new LadderObject(16682, new Location(2532, 3545, 1)), new Location(2532, 3546, 0)),
	BARBARIAN_COURSE_BOTTOM_LADDER(new LadderObject(16683, new Location(2532, 3545, 0)), new Location(2532, 3546, 1)),

	TAVERLY_DUNGEON_TOP_LADDER(new LadderObject(16680, new Location(2884, 3397, 0)), new Location(2884, 9798, 0)),
	TAVERLY_DUNGEON_BOTTOM_LADDER(new LadderObject(17385, new Location(2884, 9797, 0)), new Location(2884, 3398, 0)),

	MUDSKIPPER_POINT_TRAPDOOR(new LadderObject(1738, new Location(3008, 3150, 0)), new Location(3009, 9550, 0)),
	MUDSKIPPER_POINT_LADDER(new LadderObject(17385, new Location(3008, 9550, 0)), new Location(3009, 3150, 0)),

	MELZAR_LADDER_DOWN(new LadderObject(17384, new Location(2924, 3250, 0)), new Location(2924, 9649, 0)),
	MELZAR_LADDER_UP(new LadderObject(17385, new Location(2924, 9650, 0)), new Location(2924, 3249, 0)),

	TZHAAR_CAVE_ENTRANCE_TOP(new LadderObject(11441, new Location(2856, 3168, 0)), new Location(2855, 9569, 0)),
	TZHAAR_CAVE_ENTRACE_BOTTOM(new LadderObject(18969, new Location(2856, 9569, 0)), new Location(2856, 3167, 0)),

	VARROCK_WALL_STAIRS_TOP(new LadderObject(11802, new Location(3175, 3420, 1)), new Location(3175, 3421, 0)),
	VARROCK_WALL_STAIRS_BOTTOM(new LadderObject(11801, new Location(3175, 3420, 0)), new Location(3175, 3419, 1)),

	VARROCK_SMALL_HOUSE_TOP_1(new LadderObject(11795, new Location(3187, 3414, 1)), new Location(3186, 3414, 0)),
	VARROCK_SMALL_HOUSE_BOTTOM_1(new LadderObject(11794, new Location(3187, 3414, 0)), new Location(3188, 3414, 1)),

	VARROCK_WALL_STAIRS_TOP_1(new LadderObject(11802, new Location(3178, 3401, 1)), new Location(3179, 3401, 0)),
	VAROOCK_WALL_STAIRS_BOTTOM_1(new LadderObject(11801, new Location(3178, 3401, 0)), new Location(3177, 3401, 1)),

	VARROCK_MIDDLE_BANK_LADDER_TOP(new LadderObject(11795, new Location(3184, 3447, 1)), new Location(3185, 3447, 0)),
	VARROCK_MIDDLE_BANK_LADDER_BOTTOM(new LadderObject(11794, new Location(3184, 3447, 0)), new Location(3183, 3447, 1)),

	VARROCK_ZAFFS_MAGIC_STORE_LADDER_TOP(new LadderObject(11795, new Location(3202, 3434, 1)), new Location(3202, 3435, 0)),
	VARROCK_ZAFFS_MAGIC_STORE_LADDER_BOTTOM(new LadderObject(11794, new Location(3202, 3434, 0)), new Location(3202, 3433, 1)),

	VARROCK_THESSALIA_CLOTHES_STORE_LADDER_TOP(new LadderObject(11795, new Location(3202, 3416, 1)), new Location(3202, 3417, 0)),
	VARROCK_THESSALIA_CLOTHES_STORE_LADDER_BOTTOM(new LadderObject(11794, new Location(3202, 3416, 0)), new Location(3202, 3417, 1)),

	WIZARDS_TOWER_GO_DOWN(new LadderObject(2147, new Location(3104, 3162, 0)), new Location(3104, 9576, 0)),
	WIZARDS_TOWER_CLIMB_UP(new LadderObject(2148, new Location(3103, 9576, 0)), new Location(3105, 3162, 0)),

	DRAYNOR_JAIL_LADDER(new LadderObject(6436, new Location(3118, 9643, 0)), new Location(3118, 3243, 0)), // trapdoor needs enqueue
	DRAYNOR_DUNGEON_LADDER(new LadderObject(17385, new Location(3084, 9672, 0)), new Location(3084, 3271, 0)), // trapdoor needs enqueue

	LUMBRIDGE_CASTLE_TRAPDOOR(new LadderObject(14880, new Location(3209, 3216, 0)), new Location(3209, 9615, 0)),
	LUMBRIDGE_CASTLE_LADDER(new LadderObject(17385, new Location(3209, 9616, 0)), new Location(3210, 3216, 0)),
	LUMBRIDGE_SWAMP_ROPE_BOTTOM(new LadderObject(5946, new Location(3169, 9572, 0)), new Location(3170, 3172, 0)),

	DRAYNOR_MANOR_LADDER_DOWN(new LadderObject(133, new Location(3092, 3362, 0)), new Location(3118, 9754, 0)),
	DRAYNOR_MANOR_LADDER_UP(new LadderObject(132, new Location(3117, 9754, 0)), new Location(3093, 3363, 0)),

	VARROCK_CASTLE_LADDER_TOP(new LadderObject(11802, new Location(3224, 3472, 2)), new Location(3223, 3472, 1)),
	VARROCK_CASTLE_LADDER_BOTTOM(new LadderObject(11801, new Location(3224, 3472, 1)), new Location(3225, 3472, 2)),

	VARROCK_TOWERCASTLE_LADDER_TOP(new LadderObject(11802, new Location(3205, 3497, 3)), new Location(3204, 3497, 2)),
	VARROCK_TOWERCASTLE_LADDER_BOTTOM(new LadderObject(11801, new Location(3205, 3497, 2)), new Location(3206, 3497, 3)),

	VARROCK_DUNGEON_ENTRANCE_TOP(new LadderObject(11803, new Location(3244, 3383, 0)), new Location(3245, 9783, 0)),
	VARROCK_DUNGEON_ENTRANCE_BOTTOM(new LadderObject(2405, new Location(3244, 9783, 0)), new Location(3243, 3383, 0)),

	FALADOR_HAIRDRESSER_STORE_TOP(new LadderObject(24084, new Location(2946, 3378, 1)), new Location(2946, 3379, 0)),
	FALADOR_HAIRDRESSER_STORE_BOTTOM(new LadderObject(24082, new Location(2946, 3378, 0)), new Location(2946, 3377, 1)),

	FALADOR_SMALL_HOUSE_1_TOP(new LadderObject(24084, new Location(3027, 3353, 1)), new Location(3027, 3354, 0)),
	FALADOR_SMALL_HOUSE_1_BOTTOM(new LadderObject(24082, new Location(3027, 3353, 0)), new Location(3027, 3352, 1)),

	FALADOR_SMALL_HOUSE_2_TOP(new LadderObject(24085, new Location(3035, 3344, 1)), new Location(3035, 3345, 0)),
	FALADOR_SMALL_HOUSE_2_BOTTOM(new LadderObject(24082, new Location(3035, 3344, 0)), new Location(3036, 3344, 1)),

	HILLGIANT_DUNGEON_ENTRANCE_IN_VARROCK_TOP(new LadderObject(17384, new Location(3116, 3452, 0)), new Location(3117, 9852, 0)),
	HILLGIANT_DUNGEON_ENTRANCE_IN_VARROCK_BOTTOM(new LadderObject(17385, new Location(3116, 9852, 0)), new Location(3115, 3452, 0)),
	TRAPDOOR_DUNGEON_ENTRANCE_VARROCK_BOTTOM(new LadderObject(10560, new Location(3190, 9758, 0)), new Location(3191, 3355, 0)),

	DUNGEON_MANHOLE_ENTRY_BOTTOM(new LadderObject(11806, new Location(3237, 9858, 0)), new Location(3237, 3459, 0)),
	AGILITY_TRAINING_AREA_TOP(new LadderObject(14758, new Location(3005, 3963, 0)), new Location(3005, 10362, 0)),
	AGILITY_TRAINING_AREA_BOTTOM(new LadderObject(17358, new Location(3005, 10363, 0)), new Location(3005, 3962, 0)),

	EDGEVILLE_WILDERNESS_DUNGEON_TOP(new LadderObject(16680, new Location(3088, 3571, 0)), new Location(3089, 9971, 0)),
	EDGEVILLE_WILDERNESS_DUNGEON_BOTTOM(new LadderObject(17385, new Location(3088, 9971, 0)), new Location(3089, 3571, 0)),

	VOLCANO_TOP(new LadderObject(11441, new Location(2856, 3168, 0)), new Location(2856, 9568, 0)),
	VOLCANO_BOTTOM(new LadderObject(18969, new Location(2856, 9569, 0)), new Location(2856, 3167, 0)),

	DESERT_MINING_CAMP_TOP(new LadderObject(18903, new Location(3290, 3036, 0)), new Location(3290, 3037, 1)),
	DESERT_MINING_CAMP_BOTTOM(new LadderObject(18904, new Location(3290, 3036, 1)), new Location(3290, 3035, 0)),

	KBD_ENTRANCE_TOP(new LadderObject(18957, new Location(3017, 3849, 0)), new Location(3069, 10255, 0)),
	KBD_ENTRANCE_BOTTOM(new LadderObject(18988, new Location(3069, 10256, 0)), new Location(3017, 3848, 0)),

	LAVA_MAZE_DUNGEON_TOP(new LadderObject(18989, new Location(3069, 3856, 0)), new Location(3017, 10248, 0)),
	LAVA_MAZE_DUNGEON_BOTTOM(new LadderObject(18990, new Location(3017, 10249, 0)), new Location(3069, 3855, 0)),

	FALADOR_STORE_TOWER_LADDER_1_TOP(new LadderObject(24084, new Location(2954, 3389, 1)), new Location(2954, 3390, 0)),
	FALADOR_STORE_TOWER_LADDER_1_BOTTOM(new LadderObject(24083, new Location(2954, 3389, 0)), new Location(2954, 3388, 1)),

	FALADOR_STORE_TOWER_LADDER_2_TOP(new LadderObject(24084, new Location(2954, 3390, 2)), new Location(2955, 3390, 1)),
	FALADOR_STORE_TOWER_LADDER_2_BOTTOM(new LadderObject(24082, new Location(2954, 3390, 1)), new Location(2953, 3390, 2)),

	FALADOR_STORE_TOWER_LADDER_3_TOP(new LadderObject(24084, new Location(2954, 3389, 3)), new Location(2954, 3388, 2)),
	FALADOR_STORE_TOWER_LADDER_3_BOTTOM(new LadderObject(24082, new Location(2954, 3389, 2)), new Location(2954, 3390, 3)),

	FALADOR_DUNGEON_ENTRANCE_2_TOP(new LadderObject(11867, new Location(3019, 3450, 0)), new Location(3020, 9850, 0)),
	FALADOR_DUNGEON_ENTRANCE_2_BOTTOM(new LadderObject(17387, new Location(3019, 9850, 0)), new Location(3020, 3450, 0)),

	//FALADOR_MINING_DUNGEON_ENTRY_1_TOP(new LadderObject(30367, new Location(3020, 3339, 0)), new Location(3021, 9739, 0)),
	FALADOR_MINING_DUNGEON_ENTRY_1_BOTTOM(new LadderObject(17385, new Location(3020, 9739, 0)), new Location(3021, 3339, 0)),

	//FALADOR_MINING_DUNGEON_ENTRY_2_TOP(new LadderObject(30367, new Location(3019, 3340, 0)), new Location(3019, 9741, 0)),
	FALADOR_MINING_DUNGEON_ENTRY_2_BOTTOM(new LadderObject(17385, new Location(3019, 9740, 0)), new Location(3019, 3341, 0)),

	//FALADOR_MINING_DUNGEON_ENTRY_3_TOP(new LadderObject(30367, new Location(3018, 3339, 0)), new Location(3017, 9739, 0)),
	FALADOR_MINING_DUNGEON_ENTRY_3_BOTTOM(new LadderObject(17385, new Location(3018, 9739, 0)), new Location(3017, 3339, 0)),

	//FALADOR_MINING_DUNGEON_ENTRY_4_TOP(new LadderObject(30367, new Location(3019, 3338, 0)), new Location(3019, 9737, 0)),
	FALADOR_MINING_DUNGEON_ENTRY_4_BOTTOM(new LadderObject(17835, new Location(3019, 9738, 0)), new Location(3019, 3337, 0)),

	BRIMHAVEN_AGILITY_DOWN(new LadderObject(3617, new Location(2809, 3194, 0)), new Location(2805, 9590, 0)),
	BRIMHAVEN_AGILITY_UP(new LadderObject(3610, new Location(2805, 9591, 0)), new Location(2809, 3193, 0)),

	OMNI_ALTAR_LADDER_DOWN(new LadderObject(29365, new Location(2452, 3231, 0)), new Location(3015, 5629, 0)), // has up + down option
	OMNI_ALTAR_LADDER_UP(new LadderObject(29636, new Location(3015, 5630, 0)), new Location(2452, 3232, 0)), // has up + down option

	YANILLE_WIZARDS_DOWN(new LadderObject(17384, new Location(2594, 3085, 0)), new Location(2594, 9486, 0)),
	YANILLE_WIZARDS_UP(new LadderObject(17385, new Location(2594, 9485, 0)), new Location(2594, 3086, 0)),

	WATER_OBELISK_LADDER_DOWN(new LadderObject(17384, new Location(2842, 3424, 0)), new Location(2842, 9823, 0)),
	WATER_OBELISK_LADDER_UP(new LadderObject(17385, new Location(2842, 9824, 0)), new Location(2842, 3423, 0)),

	BAMBOO_LADDER_DOWN(new LadderObject(4780, new Location(2763, 2703, 0)), new Location(2764, 9103, 0)),
	BAMBOO_LADDER_UP(new LadderObject(4781, new Location(2763, 9103, 0)), new Location(2764, 2703, 0)),

	/*WATERBIRTH_LADDER_UP2(new LadderObject(10217, new Location(1957, 4371,0)), new Location(1957, 4373, 1)),
	WATERBIRTH_LADDER_DOWN2(new LadderObject(10218, new Location(1957, 4372,1)), new Location(1957, 4370, 0)),

	WATERBIRTH_LADDER_UP3(new LadderObject(10226, new Location(1932, 4378,1)), new Location(1932, 4380, 2)),
	WATERBIRTH_LADDER_DOWN3(new LadderObject(10225, new Location(1932, 4379,2)), new Location(1932, 4377, 1)),

	WATERBIRTH_LADDER_UP4(new LadderObject(10228, new Location(1961, 4391,2)), new Location(1961, 4393, 3)),
	WATERBIRTH_LADDER_DOWN4(new LadderObject(10227, new Location(1961, 4392,3)), new Location(1961, 4392, 2)),*/

	LIGHTHOUSE_LADDER_UP(new LadderObject(4412, new Location(2519, 4618,0)), new Location(2510, 3644, 0)),
	LIGHTHOUSE_LADDER_DOWN(new LadderObject(4383, new Location(2509, 3644,0)), new Location(2520, 4618, 0)),
	
	SLAYER_TOWER_LADDER_UP(new LadderObject(30192, new Location(3412, 9931,3)), new Location(3417, 3536, 0)),
	SLAYER_TOWER_LADDER_DOWN(new LadderObject(30191, new Location(3417, 3535,0)), new Location(3412, 9932, 3)),

	LUNAR_ISLE_LADDER_DUNGEON_DOWN(new LadderObject(14996, new Location(2142, 3944, 0)), new Location(2329, 10353, 2)),
	LUNAR_ISLE_LADDER_DUNGEON_UP(new LadderObject(14995, new Location(2330, 10353, 2)), new Location(2141, 3944, 0)),
	
	LUNAR_ISLE_SHIP_LOW_UP(new LadderObject(16960, new Location(2118, 3894, 0)), new Location(2118, 3893, 1)),
	LUNAR_ISLE_SHIP_LOW_DOWN(new LadderObject(16962, new Location(2118, 3894, 1)), new Location(2118, 3895, 0)),
	
	LUNAR_ISLE_SHIP_LOW_IN_UP(new LadderObject(16946, new Location(2141, 3911, 0)), new Location(2141, 3909, 1)),
	LUNAR_ISLE_SHIP_LOW_IN_DOWN(new LadderObject(16947, new Location(2141, 3910, 1)), new Location(2141, 3912, 0)),
	
	LUNAR_ISLE_SHIP_MED_UP(new LadderObject(16959, new Location(2127, 3893, 1)), new Location(2128, 3893, 2)),
	LUNAR_ISLE_SHIP_MED_DOWN(new LadderObject(16961, new Location(2127, 3893, 2)), new Location(2126, 3893, 1)),

	LUNAR_ISLE_SHIP_MEDW_UP(new LadderObject(16945, new Location(2138, 3895, 1)), new Location(2136, 3895, 2)),
	LUNAR_ISLE_SHIP_MEDW_DOWN(new LadderObject(16947, new Location(2137, 3895, 2)), new Location(2139, 3895, 1)),
	
	LUNAR_ISLE_SHIP_MEDE_UP(new LadderObject(16945, new Location(2142, 3895, 1)), new Location(2144, 3895, 2)),
	LUNAR_ISLE_SHIP_MEDE_DOWN(new LadderObject(16947, new Location(2142, 3895, 2)), new Location(2141, 3895, 1)),
	
	LUNAR_ISLE_SHIP_HIGHNW_UP(new LadderObject(16945, new Location(2137, 3909, 2)), new Location(2137, 3911, 3)),
	LUNAR_ISLE_SHIP_HIGHNW_DOWN(new LadderObject(16947, new Location(2137, 3909, 3)), new Location(2137, 3908, 2)),
	
	LUNAR_ISLE_SHIP_HIGHNE_UP(new LadderObject(16945, new Location(2143, 3909, 2)), new Location(2143, 3911, 3)),
	LUNAR_ISLE_SHIP_HIGHNE_DOWN(new LadderObject(16947, new Location(2143, 3909, 3)), new Location(2143, 3908, 2)),
	
	LUNAR_ISLE_SHIP_HIGHSE_UP(new LadderObject(16945, new Location(2143, 3897, 2)), new Location(2143, 3895, 3)),
	LUNAR_ISLE_SHIP_HIGHSE_DOWN(new LadderObject(16947, new Location(2143, 3896, 3)), new Location(2143, 3898, 2)),
	
	LUNAR_ISLE_SHIP_HIGHSW_UP(new LadderObject(16945, new Location(2137, 3897, 2)), new Location(2137, 3895, 3)),
	LUNAR_ISLE_SHIP_HIGHSW_DOWN(new LadderObject(16947, new Location(2137, 3896, 3)), new Location(2137, 3898, 2)),

	SHILO_VILLAGE_GEM_ROCKS_UNDERGROUND_UP(new LadderObject(23586, new Location(2825, 2998, 0)), new Location(2838, 9387, 0)),
	SHILO_VILLAGE_GEM_ROCKS_UNDERGROUND_DOWN(new LadderObject(23584, new Location(2838, 9388, 0)), new Location(2825, 2997, 0)),

    VARROCK_BEAR_PEN_DOWN(new LadderObject(7142, new Location(3230, 3504, 0)), new Location(3231, 9904, 0)),
    VARROCK_BEAR_PEN_UP(new LadderObject(17385, new Location(3230, 9904, 0)), new Location(3230, 3505, 0)),

    PORT_SARIM_MANHOLE_DOWN(new LadderObject(10321, new Location(3018, 3232, 0)), new Location(2962, 9650, 0)),
    PORT_SARIM_MANHOLE_UP(new LadderObject(10309, new Location(2962, 9651, 0)), new Location(3018, 3231, 0)),
	
	ENTRANA_LADDER_DOWN(new LadderObject(2408, new Location(2820, 3374, 0)), new Location(2825, 9772, 0)),
	
	PATTERDOMUS_TEMPLE_UP(new LadderObject(17385, new Location(3405, 9907, 0)), new Location(3405, 3506, 0)),
	
	EXPERIMENTS_UP(new LadderObject(17387, new Location(3578, 9927, 0)), new Location(3578, 3526, 0)),
	
	TEMPLE_OF_IKOV_DOWN(new LadderObject(17384, new Location(2677, 3405, 0)), new Location(2677, 9804, 0)),
	TEMPLE_OF_IKOV_UP(new LadderObject(17385, new Location(2677, 9805, 0)), new Location(2677, 3404, 0)),
	

	CASTLEWARS_ZAMORAK_UNDERGROUND_UP(new LadderObject(17387, new Location(2369, 9525, 0)), new Location(2369, 3126, 0)),
	CASTLEWARS_ZAMORAK_UNDERGROUND_DOWN(new LadderObject(4912, new Location(2369, 3125, 0)), new Location(2369, 9526, 0)),

	CASTLEWARS_SARADOMIN_UNDERGROUND_UP(new LadderObject(17387, new Location(2430, 9482, 0)), new Location(2430, 3081, 0)),
	CASTLEWARS_SARADOMIN_UNDERGROUND_DOWN(new LadderObject(4912, new Location(2430, 3082, 0)), new Location(2430, 9481, 0)),

	CASTLEWARS_MIDDLE_SOUTH_UP(new LadderObject(17387, new Location(2399, 9499, 0)), new Location(2399, 3100, 0)),
	CASTLEWARS_MIDDLE_NORTH_UP(new LadderObject(17387, new Location(2400, 9508, 0)), new Location(2400, 3107, 0)),

	KOUREND_MANHOLE_LADDER(new LadderObject(31708, new Location(1813, 10145, 0)), new Location(1813, 3746, 0)),
	
	CHAOS_DRUID_TOWER_DUNGEON_DOWN(new LadderObject(17384, new Location(2562, 3356, 0)), new Location(2563, 9756, 0)),
	CHAOS_DRUID_TOWER_DUNGEON_UP(new LadderObject(17387, new Location(2562, 9756, 0)), new Location(2563, 3356, 0)),

	RANGING_GUILD_TURRETS_DOWN(new LadderObject(16679, new Location(2668, 3412, 2)), new Location(2668, 3413, 1)),

	SOPHANEM_BROKEN_TEMPLE_NW_LADDER_UP(new LadderObject(20356, new Location(3308, 2803, 0)), new Location(3307, 2803, 2)),
	SOPHANEM_BROKEN_TEMPLE_NW_LADDER_DOWN(new LadderObject(20357, new Location(3308, 2803, 2)), new Location(3309, 2803, 0)),

	SOPHANEM_TEMPLE_EAST_LADDER_UP(new LadderObject(20353, new Location(3284, 2768, 0)), new Location(3283, 2768, 1)),
	SOPHANEM_TEMPLE_EAST_LADDER_DOWN(new LadderObject(20354, new Location(3284, 2768, 1)), new Location(3285, 2768, 0)),

	SOPHANEM_TEMPLE_WEST_LADDER_UP(new LadderObject(20353, new Location(3278, 2768, 0)), new Location(3279, 2768, 1)),
	SOPHANEM_TEMPLE_WEST_LADDER_DOWN(new LadderObject(20354, new Location(3278, 2768, 1)), new Location(3277, 2768, 0)),

	SOPHANEM_TEMPLE_EAST_SECOND_LADDER_UP(new LadderObject(20356, new Location(3279, 2771, 1)), new Location(3279, 2770, 3)),
	SOPHANEM_TEMPLE_EAST_SECOND_LADDER_DOWN(new LadderObject(20357, new Location(3279, 2771, 3)), new Location(3279, 2772, 1)),

	SOPHANEM_TEMPLE_WEST_SECOND_LADDER_UP(new LadderObject(20356, new Location(3283, 2771, 1)), new Location(3283, 2770, 3)),
	SOPHANEM_TEMPLE_WEST_SECOND_LADDER_DOWN(new LadderObject(20357, new Location(3283, 2771, 3)), new Location(3283, 2772, 1)),

	TOWER_OF_LIFE_BASEMENT_LADDER_UP(new LadderObject(17974, new Location(3038, 4375, 0)), new Location(2648, 3213, 0)),
	EXPERIMENTS_LADDER_UP(new LadderObject(17387, new Location(3504, 9970, 0)), new Location(3504, 3571, 0)),
	;
	
	private final LadderObject ladder;
	private final Location to;
	
	Ladder(final LadderObject ladder, final Location to) {
		this.ladder = ladder;
		this.to = to;
	}

    public static final Ladder[] VALUES = values();
    public static final Map<Integer, Ladder> LADDER_MAP = new HashMap<Integer, Ladder>(VALUES.length);

    static {
        for (final Ladder ladder : VALUES) {
            LADDER_MAP.put(ladder.getLadder().getLocation().getPositionHash(), ladder);
        }
    }

	public static final void use(final Player player, final String option, final WorldObject ladder, final Location destination) {
		player.lock(1);
		player.setAnimation(option.contains("down") ? LadderOA.CLIMB_DOWN : LadderOA.CLIMB_UP);
		WorldTasksManager.schedule(() -> player.setLocation(destination));
    }

    public LadderObject getLadder() {
        return ladder;
    }

    public Location getTo() {
        return to;
    }

}
