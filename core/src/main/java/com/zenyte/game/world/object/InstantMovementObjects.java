package com.zenyte.game.world.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.plugins.object.LadderOA;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum InstantMovementObjects {

	DAEYALT_STAIRS_UP(39093, new Location(3696, 9765, 2), new Location(3633, 3339, 0), LadderOA.CLIMB_UP),
	DAEYALT_STAIRS_DOWN(39092, new Location(3631, 3339, 0), new Location(3696, 9764, 2), LadderOA.CLIMB_DOWN),

	HOME_MANHOLE_ENTRY_TOP(100, new Location(3103, 3492), new Location(3096, 9867), LadderOA.CLIMB_DOWN),

	TAVERLY_DUNG_STAIRS_WEST_TOP(30190, new Location(2881, 9825, 1), new Location(2883, 9825, 0)),
	TAVERLY_DUNG_STAIRS_WEST_BOTTOM(30189, new Location(2881, 9825, 0), new Location(2880, 9825, 1)),

    MYTHS_GUILD_STATUE(31626, new Location(2456, 2846, 0), new Location(1936, 9009, 1)),
    MYTHS_GUILD_CAVE_ENTRANCE(31606, new Location(2444, 2819, 0), new Location(1939, 8968, 1)),
    MYTHS_GUILD_CAVE_EXIT(31807, new Location(1938, 8966, 1), new Location(2445, 2818, 0)),
    MYTHS_GUILD_PORTAL_OF_HEROES(31621, new Location(2455, 2853, 2), new Location(2903, 3510, 0)),
    MYTHS_GUILD_PORTAL_OF_LEGENDS(31622, new Location(2459, 2853, 2), new Location(2728, 3347, 0)),
    MYTHS_GUILD_PORTAL_OF_CHAMPIONS(31618, new Location(2456, 2856, 2), new Location(3191, 3364, 0)),

    LIZARDMAN_HANDHOLDS(27362, new Location(1459, 3690, 0), new Location(1454, 3690, 0)),
    LIZARDMAN_HANDHOLDS_UP(27362, new Location(1455, 3690, 0), new Location(1460, 3689, 0)),
	LIZARDMAN_HANDHOLDS_EAST(42009, new Location(1471, 3687, 0), new Location(1475, 3687, 0)),
	LIZARDMAN_HANDHOLDS_UP_EAST(27362, new Location(1474, 3687, 0), new Location(1470, 3687, 0)),

	CRANDOR_HOLE(25154, new Location(2833, 3255, 0), new Location(2833, 9656, 0)),
	FOSSIL_ISLAND_TASK_SHORTCUT_UPSTAIRS(30847, new Location(3633, 10262, 0), new Location(3633, 10261, 0)),
	FOSSIL_ISLAND_TASK_SHORTCUT_DOWNSTAIRS(30849, new Location(3633, 10261, 0), new Location(3633, 10264, 0)),
	SALARIN_STAIRS_FIRST(15656, new Location(2620, 9497, 0), new Location(2620, 9566, 0)),
	SALARIN_STAIRS_SECOND(15657, new Location(2620, 9562, 0), new Location(2621, 9496, 0)),

	BLAST_FURNACE_STAIRCASE_UP(9138, new Location(1939, 4956, 0), new Location(2931, 10196, 0)),
    BLAST_FURNACE_STAIRCASE_DOWN(9084, new Location(2930, 10196, 0), new Location(1939, 4958, 0)),

	MOTHERLODE_ENTRANCE_MG(30374, new Location(3055, 9743, 0), new Location(3718, 5678, 0)),
	MOTHERLODE_ENTRANCE_TOP(26654, new Location(3059, 9764, 0), new Location(3728, 5692, 0)),

	CRASH_SITE_CAVERN_ABOVE(28686, new Location(2026, 5612, 0), new Location(2129, 5646, 0)),
	CRASH_SITE_CAVERN_BELOW(28687, new Location(2129, 5647, 0), new Location(2026, 5611, 0)),

    LIZARDMAN_CAVE_ENTRANCE(30380, new Location(1306, 3573, 0), new Location(1305, 9973, 0)),
    LIZARDMAN_CAVE_ENTRANCE_EXIT(30381, new Location(1305, 9974, 0), new Location(1309, 3574, 0)),

    LIZARDMAN_WEST_CREVICE_ENTRANCE(30385, new Location(1298, 9959, 0), new Location(1295, 9959, 0)),
    LIZARDMAN_WEST_CREVICE_EXIT(30384, new Location(1296, 9959, 0), new Location(1299, 9959, 0)),

    LIZARDMAN_SWEST_CREVICE_ENTRANCE(30383, new Location(1305, 9956, 0), new Location(1305, 9953, 0)),
    LIZARDMAN_SWEST_CREVICE_EXIT(30382, new Location(1305, 9954, 0), new Location(1305, 9957, 0)),

    LIZARDMAN_SEAST_CREVICE_ENTRANCE(30383, new Location(1318, 9959, 0), new Location(1318, 9956, 0)),
    LIZARDMAN_SEAST_CREVICE_EXIT(30382, new Location(1318, 9957, 0), new Location(1318, 9960, 0)),

    LIZARDMAN_EAST_CREVICE_ENTRANCE(30384, new Location(1320, 9966, 0), new Location(1323, 9966, 0)),
    LIZARDMAN_EAST_CREVICE_EXIT(30385, new Location(1322, 9966, 0), new Location(1319, 9966, 0)),

	TROLL_STRONGHOLD_ENTRACE(3771, new Location(2839, 3689, 0), new Location(2837, 10090, 2)),
	TROLL_STRONGHOLD_EXIT(3772, new Location(2838, 10091, 2), new Location(2840, 3690, 0)),
	TROLL_STRONGHOLD_EXIT2(3773, new Location(2838, 10089, 2), new Location(2840, 3690, 0)),
	TROLL_STRONGHOLD_EXIT3(3774, new Location(2838, 10090, 2), new Location(2840, 3690, 0)),

	TROLL_STRONGHOLD_LADDER_UP(18834, new Location(2831, 10077, 2), new Location(2831, 3678, 0)),
	TROLL_STRONGHOLD_LADDER_DOWN(18833, new Location(2831, 3677, 0), new Location(2830, 10077, 2)),

    CHINCHOMPA_DUNGEON(19039, new Location(2525, 2893, 0), new Location(2532, 9294, 0)),
    CHINCHOMPA_DUNGEON_EXIT(19037, new Location(2533, 9293, 0), new Location(2527, 2894, 0)),

	WEISS_STAIRS(33234, new Location(2867, 3939, 0), new Location(2845, 10351, 0)),
	WEISS_STAIRS_UP(33261, new Location(2844, 10352, 0), new Location(2869, 3941, 0)),
	WEISS_SMELLY_HOLE(33262, new Location(2856, 10334, 0), new Location(2859, 3968, 0)),

    MOS_LE_HARMLESS_EASTERN_LADDER_DUNGEON(5269, new Location(3829, 9462, 0), new Location(3831, 3062, 0)),
    MOS_LE_HARMLESS_WESTERN_LADDER_DUNGEON(5269, new Location(3814, 9462, 0), new Location(3816, 3062, 0)),

    MOS_LE_HARMLESS_EASTERN_LADDER(5270, new Location(3814, 3062, 0), new Location(3814, 9461, 0)),
    MOS_LE_HARMLESS_WESTERN_LADDER(5270, new Location(3829, 3062, 0), new Location(3829, 9461, 0)),

	RUINS_OF_UZER_UPSTAIRS(6373, new Location(3492, 3090, 0), new Location(2722, 4886, 0)),
	RUINS_OF_UZER_DOWNSTAIRS(6372, new Location(2721, 4884, 0), new Location(3491, 3090, 0)),

	FOSSIL_ISLAND_S_DUNGEON_UPSTAIRS(30869, new Location(3745, 3777, 0), new Location(3604, 10231, 0)),
	FOSSIL_ISLAND_S_DUNGEON_DOWNSTAIRS(30878, new Location(3603, 10232, 0), new Location(3747, 3779, 0)),

	FOSSIL_ISLAND_N_DUNGEON_UPSTAIRS(30842, new Location(3677, 3853, 0), new Location(3596, 10291, 0)),
	FOSSIL_ISLAND_N_DUNGEON_DOWNSTAIRS(30844, new Location(3595, 10292, 0), new Location(3680, 3854, 0)),

    MYTHS_GUILD_DUNGEON_EXIT(31806, new Location(1970, 9033, 1), new Location(2483, 2889, 0)),
    MYTHS_GUILD_DUNGEON_ENTRANCE(31766, new Location(2482, 2890, 0), new Location(1971, 9035, 1)),

    TROLL_STRONGHOLD_PATHWAY_DUNGEON_SOUTH_EXIT(3758, new Location(2906, 10017, 0), new Location(2904, 3643, 0)),
    TROLL_STRONGHOLD_PATHWAY_DUNGEON_SOUTH_ENTRANCE(3757, new Location(2903, 3644, 0), new Location(2907, 10019, 0)),
    TROLL_STRONGHOLD_PATHWAY_DUNGEON_NORTH_EXIT(3758, new Location(2906, 10036, 0), new Location(2908, 3654, 0)),
    TROLL_STRONGHOLD_PATHWAY_DUNGEON_NORTH_ENTRANCE(3757, new Location(2907, 3652, 0), new Location(2907, 10035, 0)),

    WILDERNESS_GODWARS_DUNGEON_ENTRANCE_CREVICE(26767, new Location(3065, 10160, 3), new Location(3017, 3740, 0)),
    WILDERNESS_CAVE_TO_WILDY_GODWARS(26766, new Location(3016, 3738, 0), new Location(3065, 10159, 3)),
    WILDERNESS_GODWARS_DUNGEON_SOUTHERN_CREVICE(26767, new Location(3066, 10142, 3), new Location(3062, 10130, 0)),
    WILDERNESS_GODWARS_DUNGEON_SOUTHERN_CREVICE_EXIT(26769, new Location(3062, 10131, 0), new Location(3066, 10143, 3)),
    WILDERNESS_GODWARS_DUNGEON_WESTERN_CREVICE(26767, new Location(3049, 10165, 3), new Location(3034, 10158, 0)),
    WILDERNESS_GODWARS_DUNGEON_WESTERN_CREVICE_EXIT(26769, new Location(3035, 10158, 0), new Location(3050, 10165, 3)),

    TAVERLY_DUNG_STAIRS_EAST_TOP(30190, new Location(2904, 9813, 1), new Location(2906, 9813, 0)),
	TAVERLY_DUNG_STAIRS_EAST_BOTTOM(30189, new Location(2904, 9813, 0), new Location(2903, 9813, 1)),
	
	BRIMHAVEN_DUNG_LARGE_STAIRS_TOP(21724, new Location(2644, 9593, 2), new Location(2649, 9591, 0)),
	BRIMHAVEN_DUNG_LARGE_STAIRS_BOTTOM(21722, new Location(2648, 9592, 0), new Location(2643, 9595, 2)),
	
	BRIMHAVEN_DUNG_DEMON_STAIRS_TOP(21726, new Location(2635, 9511, 2), new Location(2636, 9517, 0)),
	BRIMHAVEN_DUNG_DEMON_STAIRS_BOTTOM(21725, new Location(2635, 9514, 0), new Location(2636, 9510, 2)),
	
	BRIMHAVEN_DUNG_CREVICE_WEST(30198, new Location(2685, 9436, 0), new Location(2697, 9436, 0), new Animation(2796)),
	BRIMHAVEN_DUNG_CREVICE_EAST(30198, new Location(2696, 9436, 0), new Location(2684, 9436, 0), BRIMHAVEN_DUNG_CREVICE_WEST.getAnimation()),
	
	TZHAAR_CITY_ENTRANCE(11835, new Location(2863, 9571, 0), new Location(2480, 5175, 0)),
	TZHAAR_CITY_EXIT(11836, new Location(2479, 5176, 0), new Location(2862, 9572, 0)),
	
	LUMBRIDGE_SWAMP_DARK_HOLE(5947, new Location(3169, 3172, 0), new Location(3170, 9572, 0)),
	DRAYNOR_MANOR_VAMPIRE_STAIRS_CLIMB_DOWN(2616, new Location(3115, 3357, 0), new Location(3077, 9771, 0)),
	DRAYNOR_MANOR_VAMPIE_STAIRS_WALK_UP(2617, new Location(3077, 9768, 0), new Location(3116, 3356, 0)),
	DRAYNOR_MANOR_STAIRCAISE(11499, new Location(3108, 3364, 1), new Location(3108, 3361, 0)),
	
	ROMEO_JULIET_HOUSE_STAIRCASE_TOP(11799, new Location(3156, 3435, 1), new Location(3159, 3435, 0)),
	ROMEO_JULIET_HOUSE_STAIRCASE_BOTTOM(11797, new Location(3156, 3435, 0), new Location(3155, 3435, 1)),

	MUSUEM_SECOND_FLOOR_STAIRCASE_TOP(11799, new Location(3253, 3444, 2), new Location(3253, 3442, 1)),
	MUSEUM_SECOND_FLOOR_STAIRCASE_BOTTOM(11796, new Location(3253, 3443, 1), new Location(3253, 3446, 2)),

	DUNGEON_MANHOLE_ENTRY_TOP(881, new Location(3237, 3458, 0), new Location(3237, 9858, 0)),
	VARROCK_HOUSE_NEXT_TO_CHURCH_BOTTOM(11797, new Location(3239, 3489, 0), new Location(3242, 3489, 1)),
	VARROCK_HOUSE_NEXT_TO_CHURCH_TOP(11799, new Location(3240, 3489, 1), new Location(3238, 3489, 0)),

	VARROCK_MUSEUM_STAIR_CASE_TOP(11799, new Location(3266, 3453, 1), new Location(3266, 3451, 0)),
	VARROCK_MUSEUM_STAIR_CASE_BOTTOM(11798, new Location(3266, 3452, 0), new Location(3266, 3455, 1)),

	VARROCK_CASTLE_STAIRS_ENTRY_TOP(11799, new Location(3212, 3474, 1), new Location(3212, 3472, 0)),
	VARROCK_CASTLE_STAIRS_ENTRY_BOTTOM(11807, new Location(3212, 3473, 0), new Location(3212, 3476, 1)),

	VARROCK_QUEST_HOUSE_STAIRS_TOP(11799, new Location(3188, 3355, 1), new Location(3189, 3358, 0)),
	VARROCK_QUEST_HOUSE_STAIRS_BOTTOM(11797, new Location(3188, 3355, 0), new Location(3189, 3354, 1)),

	VARROCK_PUB_OUTSIDE_VARROCK_WALLS_TOP(11799, new Location(3285, 3493, 1), new Location(3285, 3496, 0)),
	VARROCK_PUB_OUTSIDE_VARROCK_WALLS_BOTTOM(11797, new Location(3285, 3493, 0), new Location(3285, 3492, 1)),
	
	WILDERNESS_DUNGEON_STAIRS_TOP(16664, new Location(3044, 3924, 0), new Location(3045, 10323, 0)),
	WILDERNESS_DUNGEON_STAIRS_BOTTOM(16665, new Location(3044, 10324, 0), new Location(3045, 3927, 0)),
	
	FALADOR_FURNACE_HOUSE_STAIRS_TOP(24080, new Location(2971, 3371, 1), new Location(2971, 3369, 0)),
	FALADOR_FURNASE_HOUSE_STAIRS_BOTTOM(24079, new Location(2971, 3370, 0), new Location(2972, 3373, 1)),

	FALADOR_BAR_STAIRS_TOP(24080, new Location(2959, 3370, 1), new Location(2960, 3368, 0)),
	FALADOR_BAR_STAIRS_BOTTOM(24079, new Location(2959, 3369, 0), new Location(2960, 3372, 1)),

	FALADOR_SMALL_HOUSE_3_TOP(24080, new Location(3045, 3364, 1), new Location(3045, 3362, 0)),
	FALADOR_SMALL_HOUSE_3_BOTTOM(24079, new Location(3045, 3363, 0), new Location(3045, 3366, 1)),

	FALADOR_DUNGEON_ENTRANCE_1_TOP(16664, new Location(3058, 3376, 0), new Location(3058, 9776, 0)),
	FALADOR_DUNGEON_ENTRANCE_1_BOTTOM(23969, new Location(3059, 9776, 0), new Location(3061, 3377, 0)),

	FALADOR_HOUSE_BEHIND_BANK_TOP(24078, new Location(3019, 3343, 1), new Location(3017, 3343, 0)), 
	FALADOR_HOUSE_BEHIND_BANK_BOTTOM(24077, new Location(3018, 3343, 0), new Location(3021, 3343, 1)),

	FALADOR_HOUSE_BEHIND_BANK_1_TOP(24078, new Location(3022, 3332, 1), new Location(3020, 3333, 0)),
	FALADOR_HOUSE_BEHIND_BANK_1_BOTTOM(24077, new Location(3021, 3332, 0), new Location(3024, 3333, 1)),

	FALADOR_CASTLE_STAIRS_TOP(24068, new Location(2968, 3347, 1), new Location(2971, 3347, 0)), 
	FALADOR_CASTLE_STAIRS_BOTTOM(24067, new Location(2968, 3347, 0), new Location(2968, 3348, 1)),

	FALADOR_CASTLE_STAIRS_1_TOP(24078, new Location(2984, 3338, 2), new Location(2984, 3336, 1)), 
	FALADOR_CASTLE_STAIRS_1_BOTTOM(24077, new Location(2984, 3337, 1), new Location(2984, 3340, 2)),
	
	CAMELOT_CASTLE_STAIRWELL_TOP(25604, new Location(2750, 3511, 1), new Location(2750, 3508, 0)),
	BRIMHAVEN_ROCK_ENTRANCE(2584, new Location(2824, 3118, 0), new Location(2830, 9522, 0)),
	BRIMHAVEN_HANDHOLDS_EXIT(2585, new Location(2830, 9522, 0), new Location(2825, 3120, 0)),
	BRIMHAVEN_DUNGEON_ENTRANCE(20877, new Location(2743, 3153, 0), new Location(2713, 9564, 0)),
	BRIMHAVEN_DUNGEON_EXIT(20878, new Location(2714, 9564, 0), new Location(2745, 3152, 0)),
	YANILLE_STAIRS_DOWN(16664, new Location(2603, 3078, 0), new Location(2602, 9479, 0)),
	YANILLE_STAIRS_UP(16665, new Location(2603, 9478, 0), new Location(2606, 3079, 0)),
	YANILLE_DUNGEON_STAIRS_DOWN(16664, new Location(2569, 3122, 0), new Location(2569, 9525, 0)),
	YANILLE_DUNGEON_STAIRS_UP(16665, new Location(2569, 9522, 0), new Location(2569, 3121, 0)),
	LEGENDS_GUILD_DOWN(16664, new Location(2723, 3374, 0), new Location(2727, 9775, 0)),
	LEGENDS_GUILD_UP(16665, new Location(2724, 9774, 0), new Location(2723, 3375, 0)),
	
	VARROCK_BANK_CELLAR_DOWN(11800, new Location(3187, 3433, 0), new Location(3190, 9833, 0)),
	VARROCK_BANK_CELLAR_UP(11805, new Location(3187, 9833, 0), new Location(3186, 3434, 0)),
	
	BRINE_RAT_CAVERN_EXIT(23157, new Location(2689, 10125, 0), new Location(2729, 3734, 0)),
	BRINE_RAT_CAVERN_EXIT_OTHER_HALF(23158, new Location(2689, 10124, 0), new Location(2729, 3734, 0)),
	
	MYTHS_GUILD_BOTTOM_STAIRS_UP(31627, new Location(2455, 2836, 0), new Location(2457, 2839, 1)),
	MYTHS_GUILD_BOTTOM_STAIRS_DOWN(32206, new Location(2455, 2836, 1), new Location(2457, 2839, 0)),
	
	MYTHS_GUILD_MIDDLE_STAIRS_UP(31609, new Location(2450, 2847, 1), new Location(2452, 2847, 2)),
	MYTHS_GUILD_MIDDLE_STAIRS_DOWN(31610, new Location(2450, 2847, 2), new Location(2449, 2847, 1)),
	
	REVENANT_OPENING_SOUTH_UP(31558, new Location(3218, 10058, 0), new Location(3102, 3655, 0)),
    REVENANT_OPENING_SOUTH_DOWN(31555, new Location(3073, 3654, 0), new Location(3197, 10056, 0)),

    REVENANT_OPENING_NORTH_UP(43868, new Location(3244, 10215, 0), new Location(3124, 3805, 0)),
    REVENANT_OPENING_NORTH_DOWN(31556, new Location(3124, 3831, 0), new Location(3241, 10233, 0)),

	REVENANT_CREVICE_WEST(40386, new Location(3067, 3740, 0), new Location(3187, 10127, 0)),

	WILDERNESS_SLAYER_CAVE_SOUTH_UP(40389, new Location(3384, 10050, 0), new Location(3259, 3663, 0)),
	WILDERNESS_SLAYER_CAVE_SOUTH_DOWN(40388, new Location(3259, 3664, 0), new Location(3385, 10052, 0)),

	WILDERNESS_SLAYER_CAVE_NORTH_UP(40391, new Location(3405, 10146, 0), new Location(3293, 3749, 0)),
	WILDERNESS_SLAYER_CAVE_NORTH_DOWN(40390, new Location(3293, 3746, 0), new Location(3406, 10145, 0)),
    
    SMOKEY_WELL(6279, new Location(3310, 2962, 0), new Location(3206, 9379, 0)),
    
    BRIMSTAIL_CAVE_ENTRANCE(17209, new Location(2403, 3418, 0), new Location(2409, 9812, 0)),
	BRIMSTAIL_CAVE_EXIT_RIGHT_SIDE(17222, new Location(2408, 9811, 0), new Location(2402, 3419, 0)),
    BRIMSTAIL_CAVE_EXIT_LEFT_SIDE(17223, new Location(2409, 9811, 0), new Location(2402, 3419, 0)),
    
    KELDAGRIM_START_ENTRANCE(5008, new Location(2731, 3712, 0), new Location(2773, 10162, 0)),
    KELDAGRIM_START_EXIT(5014, new Location(2771, 10161, 0), new Location(2730, 3713, 0)),
    
    KELDAGRIM_MIDDLE_ENTRANCE(5973, new Location(2781, 10161, 0), new Location(2838, 10124, 0)),
    KELDAGRIM_MIDDLE_EXIT(5998, new Location(2838, 10123, 0), new Location(2780, 10161, 0)),

	JATISZO_MINE_DOWN(21455, new Location(2397, 3812, 0), new Location(2406, 10187, 0)),
	JATISZO_MINE_UP(21578, new Location(2406, 10188, 0), new Location(2399, 3813, 0)),
   
    VARROCK_MUSEUM_UP(24427, new Location(1758, 4959, 0), new Location(3258, 3452, 0)),
    VARROCK_MUSEUM_DOWN(24428, new Location(3255, 3451, 0), new Location(1759, 4958, 0)),

	ROGUES_DEN_PASSAGEWAY(7258, new Location(3061, 4986, 1), new Location(2906, 3537, 0)),

    PATERDOMUS_TEMPLE(3443, new Location(3440, 9886, 0), new Location(3423, 3485, 0)),

	CASTLEWARS_SARADOMIN_TOP_DOWN(4415, new Location(2425, 3074, 3), new Location(2425, 3077, 2)),
	CASTLEWARS_SARADOMIN_TOP_UP(4417, new Location(2425, 3074, 2), new Location(2426, 3074, 3)),

	CASTLEWARS_SARADOMIN_2ND_DOWN(4415, new Location(2430, 3081, 2), new Location(2427, 3081, 1)),
	CASTLEWARS_SARADOMIN_2ND_UP(4417, new Location(2428, 3081, 1), new Location(2430, 3080, 2)),

	CASTLEWARS_SARADOMIN_1ST_DOWN(4415, new Location(2419, 3080, 1), new Location(2419, 3077, 0)),
	CASTLEWARS_SARADOMIN_1ST_UP(4417, new Location(2419, 3078, 0), new Location(2420, 3080, 1)),

	CASTLEWARS_ZAMORAK_1ST_UP(4418, new Location(2380, 3127, 0), new Location(2379, 3127, 1)),
	CASTLEWARS_ZAMORAK_1ST_DOWN(4415, new Location(2380, 3127, 1), new Location(2380, 3130, 0)),

	CASTLEWARS_ZAMORAK_2ND_UP(4418, new Location(2369, 3126, 1), new Location(2369, 3127, 2)),
	CASTLEWARS_ZAMORAK_2ND_DOWN(4415, new Location(2369, 3126, 2), new Location(2372, 3126, 1)),

	CASTLEWARS_ZAMORAK_TOP_UP(4418, new Location(2374, 3131, 2), new Location(2373, 3133, 3)),
	CASTLEWARS_ZAMORAK_TOP_DOWN(4415, new Location(2374, 3133, 3), new Location(2374, 3130, 2)),
	KARUULM_STAIRS_LOBBY_STAIRS_UP(34530, new Location(1330, 10205, 0), new Location(1334, 10205, 1)),
	KARUULM_STAIRS_MIDDLE_LEVEL_STAIRS_UP(34530, new Location(1314, 10188, 1), new Location(1318, 10188, 2)),

	KARUULM_STAIRS_LOBBY_STAIRS_DOWN(34531, new Location(1330, 10205, 1), new Location(1329, 10206, 0)),
	KARUULM_STAIRS_UPPER_LEVEL_STAIRS_DOWN(34531, new Location(1314, 10188, 2), new Location(1313, 10189, 1)),

	PORT_PISCARILIUS_MANHOLE(31706, new Location(1813, 3745, 0), new Location(1812, 10145, 0)),
	KOUREND_CASTLE_STAIRS_ENTRY_BOTTOM_1(11807, new Location(1615, 3665, 0), new Location(1614, 3665, 1)),
	KOUREND_CASTLE_STAIRS_ENTRY_BOTTOM_2(11807, new Location(1615, 3680, 0), new Location(1614, 3681, 1)),
	KOUREND_CASTLE_STAIRS_DOWN_2(11799, new Location(1615, 3680, 1), new Location(1618, 3680, 0)),

	KOUREND_CASTLE_STAIRS_ENTRY_MID_1(11799, new Location(1615, 3665, 1), new Location(1618, 3665, 0)),
	KOUREND_CASTLE_STAIRS_ENTRY_MID_2(11799, new Location(1615, 3681, 1), new Location(1618, 3681, 0)),

	WITCHHAVEN_DUNGEON_ENTRANCE(18270, new Location(2696, 3283, 0), new Location(2696, 9683, 0)),
	WITCHHAVEN_DUNGEON_EXIT(18354, new Location(2696, 9682, 0), new Location(2697, 3283, 0)),

	JORGUMUNDS_ENTRANCE(37433, new Location(2464, 4011, 0), new Location( 2461, 10417, 0), true),
	JORGUMUNDS_EXIT(37411, new Location(2460, 10418, 0), new Location( 2465, 4010, 0), true),

	RDI_SHIP_ENTRANCE(32319, new Location(3373, 8003, 0), new Location( 3371, 8003, 2)),
	RDI_SHIP_EXIT(32238, new Location( 3372, 8003, 2), new Location(3374, 8003, 0)),

	RANTS_CAVE_ENTRANCE(3379, new Location(2629, 2998,0), new Location(2647, 9378)),
	RANTS_CAVE_ENTRANCE2(3381, new Location(2647, 9377,0), new Location(2629, 2997)),

	ASGARNIAN_CAVE_SHORTCUT(42506, new Location(3025, 9570, 0), new Location(3035, 9557, 0)),
	ASGARNIAN_CAVE_SHORTCUT2(42507, new Location(3034, 9558,0), new Location(3026, 9572, 0)),
	LDI_REV_DUNGEON(50098, new Location(3410, 7607,0), new Location(3369, 15247, 0)),
	UDI_REV_DUNGEON(50097, new Location(3357, 7784,0), new Location(3369, 15375, 0)),

	ESCAPE_CAVES_EXIT_1(47147, new Location(3358, 10244, 0), new Location(3283, 3773, 0), new Animation(2796)),
	ESCAPE_CAVES_EXIT_2(47148, new Location(3382, 10287, 0), new Location(3321, 3829, 0), new Animation(2796)),
	ESCAPE_CAVES_EXIT_3(47148, new Location(3336, 10287, 0), new Location(3261, 3831, 0), new Animation(2796)),
	ESCAPE_CAVES_EXIT_ROPE(47149, new Location(3361, 10273, 0), new Location(3285, 3806, 0), new Animation(4435)),
	;

	private final int id;
	private final Location object;
	private final Location location;
	private final Animation animation;
	private final boolean screenFade;

	InstantMovementObjects(final int id, final Location object, final Location location) {
		this.id = id;
		this.object = object;
		this.location = location;
		animation = Animation.STOP;
		this.screenFade = false;
	}
	
	InstantMovementObjects(final int id, final Location object, final Location location, final Animation animation) {
		this.id = id;
		this.object = object;
		this.location = location;
		this.animation = animation;
		this.screenFade = false;
	}
	InstantMovementObjects(final int id, final Location object, final Location location, final boolean screenFade) {
		this.id = id;
		this.object = object;
		this.location = location;
		animation = Animation.STOP;
		this.screenFade = screenFade;
	}

	public static final InstantMovementObjects[] VALUES = values();
	public static final Int2ObjectMap<InstantMovementObjects> entries = new Int2ObjectOpenHashMap<>(VALUES.length);
	
	static {
		for(final InstantMovementObjects entry : VALUES) {
			entries.put(entry.getObject().getPositionHash(), entry);
		}
	}

	public boolean isScreenFade() {
		return screenFade;
	}

	public int getId() {
	    return id;
	}
	
	public Location getObject() {
	    return object;
	}
	
	public Location getLocation() {
	    return location;
	}
	
	public Animation getAnimation() {
	    return animation;
	}

	public int getDelay() {
		return 0;
	}

}
