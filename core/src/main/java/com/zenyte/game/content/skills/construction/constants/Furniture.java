package com.zenyte.game.content.skills.construction.constants;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.FurnitureData;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TextUtils;
import mgi.types.config.ObjectDefinitions;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

import static com.zenyte.game.content.skills.construction.ConstructionConstants.WATERING_CAN;
import static com.zenyte.game.content.skills.construction.constants.Furniture.ConstructionAction.REFRESH_ROOM;

//import static com.zenyte.game.content.skills.construction.constants.Furniture.ConstructionAction.REFRESH_HOUSE;

public enum Furniture {

	/**
	 * Garden
	 */
	PLANT(1, 31, 5134, 8180, new Item(8431), WATERING_CAN),
	SMALL_FERN(6, 70, 5135, 8181, new Item(8433), WATERING_CAN),
	FERN(12, 100, 5136, 8182, new Item(8435), WATERING_CAN),
	DOCK_LEAF(1, 31, 5137, 8183, new Item(8431), WATERING_CAN),
	THISTLE(6, 70, 5138, 8184, new Item(8433), WATERING_CAN),
	REEDS(12, 100, 5139, 8185, new Item(8435), WATERING_CAN),
	BUSH(6, 70, 5129, 8187, new Item(8433), WATERING_CAN),
	TALL_PLANT(12, 70, 5130, 8188, new Item(8435), WATERING_CAN),
	SHORT_PLANT(1, 31, 5131, 8189, new Item(8431), WATERING_CAN),
	LARGE_LEAF_BUSH(6, 70, 5132, 8190, new Item(8433), WATERING_CAN),
	HUGE_PLANT(12, 100, 5133, 8191, new Item(8435), WATERING_CAN),
	DEAD_TREE(5, 31, 4531, 8173, new Item(8417), WATERING_CAN),
	NICE_TREE(10, 44, 4532, 8174, new Item(8419), WATERING_CAN),
	OAK_TREE(15, 70, 4533, 8175, new Item(8421), WATERING_CAN),
	WILLOW_TREE(30, 100, 4534, 8176, new Item(8423), WATERING_CAN),
	MAPLE_TREE(45, 122, 5126, 8177, new Item(8425), WATERING_CAN),
	YEW_TREE(60, 141, 5121, 8178, new Item(8427), WATERING_CAN),
	MAGIC_TREE(75, 223, 5127, 8179, new Item(8429), WATERING_CAN),
	BIG_DEAD_TREE(5, 31, 4538, 8173, new Item(8417), WATERING_CAN),
	BIG_NICE_TREE(10, 44, 4539, 8174, new Item(8419), WATERING_CAN),
	BIG_OAK_TREE(15, 70, 4540, 8175, new Item(8421), WATERING_CAN),
	BIG_WILLOW_TREE(30, 100, 4541, 8176, new Item(8423), WATERING_CAN),
	BIG_MAPLE_TREE(45, 122, 4535, 8177, new Item(8425), WATERING_CAN),
	BIG_YEW_TREE(60, 141, 4536, 8178, new Item(8427), WATERING_CAN),
	BIG_MAGIC_TREE(75, 223, 4537, 8179, new Item(8429), WATERING_CAN),
	
	LOW_LEVEL_PLANTS(1, 0, 0, 8096, new Item(8431), WATERING_CAN),
	MID_LEVEL_PLANTS(1, 0, 0, 8097, new Item(8433), WATERING_CAN),
	HIGH_LEVEL_PLANTS(1, 0, 0, 8098, new Item(8435), WATERING_CAN),
	
	
	EXIT_PORTAL(1, 100, 4525, 8168, new Item(2351, 10)),
	DECORATIVE_ROCK(5, 100, 4526, 8169, new Item(3420, 5)),
	POND(10, 100, 4527, 8170, new Item(1761, 10)),
	IMP_STATUE(15, 150, 4528, 8171, new Item(3420, 5), new Item(1761, 5)),
	DUNGEON_ENTRANCE(70, 500, 4529, 8172, new Item(8786)),
	TIP_JAR(40, 651, 29146, 20634, new Item(8782, 2), new Item(1775), new Item(8784), new Item(13204, 5)),
	/**
	 * Parlour
	 */
	CRUDE_WOODEN_CHAIR(1, 58, 6752, 8309, new Item(960, 2), new Item(4819, 2)),
	WOODEN_CHAIR(8, 87, 6753, 8310, new Item(960, 3), new Item(4819, 3)),
	ROCKING_CHAIR(14, 96, 6754, 8311, new Item(960, 3), new Item(4819, 3)),
	OAK_CHAIR(19, 120, 6755, 8312, new Item(8778, 2)),
	OAK_ARMCHAIR(26, 180, 6756, 8313, new Item(8778, 3)),
	TEAK_ARMCHAIR(35, 180, 6757, 8314, new Item(8780, 2)),
	MAHOGANY_ARMCHAIR(50, 280, 6758, 8315, new Item(8782, 2)),
	BROWN_RUG(2, 30, new int[] { 6761, 6760, 6759 }, 8316, new Item(8790, 2)),
	RUG(13, 60, new int[] { 6764, 6763, 6762 }, 8317, new Item(8790, 4)),
	OPULENT_RUG(65, 360, new int[] { 6767, 6766, 6765 }, 8318, new Item(8790, 4), new Item(8784)),
	TORN_CURTAINS(2, 132, 6774, 8322, new Item(960, 3), new Item(8790, 3), new Item(4819, 3)),
	CURTAINS(18, 225, 6775, 8323, new Item(960, 3), new Item(8778, 3)),
	OPULENT_CURTAINS(40, 315, 6776, 8324, new Item(960, 3), new Item(8780, 3)),
	CLAY_FIREPLACE(3, 30, 6780, 8325, new Item(1761, 3)),
	STONE_FIREPLACE(33, 40, 6782, 8326, new Item(3420, 2)),
	MARBLE_FIREPLACE(63, 500, 6784, 8327, new Item(8786)),
	WOODEN_BOOKCASE(4, 126, 6768, 8319, new Item(960, 4), new Item(4819, 4)),
	/**
	 * Kitchen
	 */
	OAK_BOOKCASE(29, 180, 6769, 8320, new Item(8778, 3)),
	MAHOGANY_BOOKCASE(40, 420, 6770, 8321, new Item(8782, 3)),
	WOOD_KITCHEN_TABLE(12, 87, 13577, 8246, new Item(960, 3), new Item(4819, 3)),
	OAK_KITCHEN_TABLE(32, 180, 13578, 8247, new Item(8778, 3)),
	TEAK_KITCHEN_TABLE(52, 270, 13579, 8248, new Item(8780, 3)),
	WOODEN_LARDER(8, 228, 13565, 8233, new Item(960, 8), new Item(4819, 8)),
	OAK_LARDER(33, 480, 13566, 8234, new Item(8778, 8)),
	TEAK_LARDER(43, 750, 13567, 8235, new Item(8780, 8), new Item(8790, 2)),
	PUMP_AND_DRAIN(7, 100, 13559, 8230, new Item(2353, 5)),
	PUMP_AND_TUB(27, 200, 13561, 8231, new Item(2353, 10)),
	SINK(47, 300, 13563, 8232, new Item(2353, 15)),
	WOODEN_SHELVES_1(6, 87, 13545, 8223, new Item(960, 3), new Item(4819, 3)),
	WOODEN_SHELVES_2(12, 147, 13546, 8224, new Item(960, 3), new Item(4819, 3), new Item(1761, 6)),
	WOODEN_SHELVES_3(23, 147, 13554, 8225, new Item(960, 3), new Item(4819, 3), new Item(1761, 6)),
	OAK_SHELVES_1(34, 240, 13548, 8226, new Item(8778, 3), new Item(1761, 6)),
	OAK_SHELVES_2(45, 240, 13556, 8227, new Item(8778, 3), new Item(1761, 6)),
	TEAK_SHELVES_1(56, 330, 13557, 8228, new Item(8780, 3), new Item(1761, 6)),
	TEAK_SHELVES_2(67, 930, 13558, 8229, new Item(8780, 3), new Item(1761, 6), new Item(8784, 2)),
	FIREPIT(5, 40, 13528, 8216, new Item(2353), new Item(1761)),
	FIREPIT_WITH_HOOK(11, 60, 13529, 8217, new Item(2353, 2), new Item(1761, 2)),
	FIREPIT_WITH_POT(17, 80, 13531, 8218, new Item(2353, 3), new Item(1761, 2)),
	SMALL_OVEN(24, 80, 13533, 8219, new Item(2353, 4)),
	LARGE_OVEN(29, 100, 13536, 8220, new Item(2353, 5)),
	STEEL_RANGE(34, 120, 13539, 8221, new Item(2353, 6)),
	FANCY_RANGE(42, 160, 13542, 8222, new Item(2353, 8)),
	BEER_BARREL(7, 87, 13568, 8239, new Item(960, 3), new Item(4819, 3)),
	CIDER_BARREL(12, 91, 13569, 8240, new Item(960, 3), new Item(4819, 3), new Item(5763, 8)),
	ASGARNIAN_ALE(18, 184, 13570, 8241, new Item(8778, 3), new Item(1905, 8)),
	GREENMANS_ALE(26, 184, 13571, 8242, new Item(8778, 3), new Item(1909, 8)),
	DRAGON_BITTER(36, 224, 13572, 8243, new Item(8778, 3), new Item(1911, 8), new Item(2353, 2)),
	CHEFS_DELIGHT(48, 224, 13573, 8244, new Item(8778, 3), new Item(5755, 8), new Item(2353, 2)),
	CAT_BLANKET(5, 15, 13574, 8236, new Item(8790)),
	CAT_BASKET(19, 58, 13575, 8237, new Item(960, 2), new Item(4819, 2)),
	CUSHIONED_BASKET(33, 58, 13576, 8238, new Item(960, 2), new Item(4819, 2), new Item(1737, 2)),
	/**
	 * Workshop
	 */
	WOODEN_WORKBENCH(17, 143, 6791, 8375, new Item(960, 5), new Item(4819, 5)),
	OAK_WORKBENCH(32, 300, 6792, 8376, new Item(8778, 5)),
	STEEL_FRAMED_BENCH(46, 440, 6793, 8377, new Item(8778, 6), new Item(2353, 4)),
	BENCH_WITH_VICE(62, 140, 6794, 8378, new Item(8377), new Item(8778, 2), new Item(2353)),
	BENCH_WITH_LATHE(77, 140, 6795, 8379, new Item(8378), new Item(8778, 2), new Item(2353)),
	PLUMING_STAND(16, 120, 6803, 8392, new Item(8778, 2)),
	SHIELD_EASEL(41, 240, 6804, 8393, new Item(8778, 4)),
	BANNER_EASEL(66, 510, 6805, 8394, new Item(8778, 8), new Item(8790, 2)),
	REPAIR_BENCH(15, 120, 6800, 8389, new Item(8778, 2)),
	WHETSTONE(35, 260, 6801, 8390, new Item(8778, 4), new Item(3420)),
	ARMOUR_STAND(55, 500, 6802, 8391, new Item(8778, 8), new Item(3420)),
	TOOL_STORE_1(15, 120, 6786, 8384, new Item(8778, 2)),
	TOOL_STORE_2(25, 120, 6787, 8385, new Item(8384), new Item(8778, 2)),
	TOOL_STORE_3(35, 120, 6788, 8386, new Item(8385), new Item(8778, 2)),
	TOOL_STORE_4(45, 120, 6789, 8387, new Item(8386), new Item(8778, 2)),
	TOOL_STORE_5(55, 120, 6790, 8388, new Item(8387), new Item(8778, 2)),
	CRAFTING_TABLE_1(16, 240, 6796, 8380, new Item(8778, 4)),
	CRAFTING_TABLE_2(25, 1, 6797, 8381, new Item(8380), new Item(1775)),
	CRAFTING_TABLE_3(34, 2, 6798, 8382, new Item(8381), new Item(1775, 2)),
	CRAFTING_TABLE_4(42, 120, 6799, 8383, new Item(8382), new Item(8778, 2)),
	/**
	 * Bedroom
	 */
	WOODEN_BED(20, 117, 13148, 8031, new Item(960, 3), new Item(8790, 2), new Item(4819, 3)),
	OAK_BED(30, 210, 13149, 8032, new Item(8778, 3), new Item(8790, 2)),
	LARGE_OAK_BED(34, 330, 13150, 8033, new Item(8778, 5), new Item(8790, 2)),
	TEAK_BED(40, 300, 13151, 8034, new Item(8780, 3), new Item(8790, 2)),
	LARGE_TEAK_BED(45, 480, 13152, 8035, new Item(8780, 5), new Item(8790, 2)),
	REGULAR_4_POSTER(53, 450, 13153, 8036, new Item(8782, 3), new Item(8790, 2)),
	GILDED_4_POSTER(60, 1330, 13154, 8037, new Item(8782, 5), new Item(8790, 2), new Item(8784, 2)),
	SHOE_BOX(20, 58, 13155, 8038, new Item(960, 2), new Item(4819, 2)),
	OAK_DRAWERS(27, 120, 13156, 8039, new Item(8778, 2)),
	OAK_WARDROBE(39, 180, 13157, 8040, new Item(8778, 3)),
	TEAK_DRAWERS(51, 180, 13158, 8041, new Item(8780, 2)),
	TEAK_WARDROBE(63, 270, 13159, 8042, new Item(8780, 3)),
	MAHOGANY_WARDROBE(75, 420, 13160, 8043, new Item(8782, 3)),
	GILDED_WARDROBE(87, 720, 13161, 8044, new Item(8782, 3), new Item(8784)),
	SHAVING_STAND(21, 30, 13162, 8045, new Item(960), new Item(4819), new Item(1775)),
	OAK_SHAVING_STAND(29, 61, 13163, 8046, new Item(8778), new Item(1775)),
	OAK_DRESSER(37, 121, 13164, 8047, new Item(8778, 2), new Item(1775)),
	TEAK_DRESSER(46, 181, 13165, 8048, new Item(8780), new Item(1775)),
	FANCY_TEAK_DRESSER(56, 182, 13166, 8049, new Item(8780, 2), new Item(1775, 2)),
	MAHOGANY_DRESSER(64, 281, 13167, 8050, new Item(8782, 2), new Item(1775)),
	GILDED_DRESSER(74, 582, 13168, 8051, new Item(8782, 2), new Item(1775, 2), new Item(8784)),
	OAK_CLOCK(25, 142, 13169, 8052, new Item(8778, 2), new Item(8792)),
	TEAK_CLOCK(55, 202, 13170, 8053, new Item(8780, 2), new Item(8792)),
	SERVANTS_MONEYBAG(58, 595, 28859, 21213, new Item(8782, 2), new Item(8790), new Item(8784)),
	GILDED_CLOCK(85, 602, 13171, 8054, new Item(8782, 2), new Item(8792), new Item(8784)),
	/**
	 * Corridor
	 */
	SPIKE_TRAP(72, 223, 13356, 8143, new Item(995, 50000)),
	MAN_TRAP(76, 273, 13357, 8144, new Item(995, 75000)),
	TANGLE_VINE(80, 316, 13358, 8145, new Item(995, 100000)),
	MARBLE_TRAP(84, 387, 13359, 8146, new Item(995, 150000)),
	TELEPORT_TRAP(88, 447, 13360, 8147, new Item(995, 200000)),
	SKELETON_GUARD(70, 223, 13366, 8131, new Item(995, 50000)),
	GUARD_DOG(74, 273, 13367, 8132, new Item(995, 75000)),
	HOBGOBLIN_GUARD(78, 316, 13368, 8133, new Item(995, 100000)),
	BABY_RED_DRAGON(82, 387, 13372, 8134, new Item(995, 150000)),
	HUGE_SPIDER(86, 447, 13370, 8135, new Item(995, 200000)),
	TROLL_GUARD(90, 1000, 13369, 8136, new Item(995, 1000000)),
	HELLHOUND(94, 2236, 2715, 8137, new Item(995, 5000000)),
	OAK_DOOR(74, 600, new int[] { 13344, 13345 }, 8122, new Item(8778, 10)),
	STEEL_PLATED_DOOR(84, 800, new int[] { 13346, 13347 }, 8123, new Item(8778, 10), new Item(2353, 10)),
	MARBLE_DOOR(94, 2000, new int[] { 13348, 13349 }, 8124, new Item(8786)),
	DECORATIVE_BLOOD(72, 4, 13312, 8125, new Item(1763, 4)),
	DECORATIVE_PIPE(83, 120, 13311, 8126, new Item(2353, 6)),
	HANGING_SKELETON(94, 3, 13310, 8127, new Item(964, 2), new Item(526, 6)),
	CANDLES(72, 243, 13342, 8128, new Item(8778, 4), new Item(33, 4)),
	TORCHES(84, 244, 13341, 8129, new Item(8778, 4), new Item(594, 4)),
	SKULL_TORCHES(94, 246, 13343, 8130, new Item(8778, 4), new Item(594, 4), new Item(964, 4)),
	/**
	 * Oubliette
	 */
	SPIKES(65, 623, new int[] { 13334, 13334 }, 8302, new Item(2353, 20), new Item(995, 50000)),
	TENTACLE_POOL(71, 326, new int[] { 13331, 13331 }, 8303, new Item(1929, 20), new Item(995, 50000)),
	FLAME_PIT(77, 357, new int[] { 13337, 13337 }, 8304, new Item(590, 20), new Item(995, 125000)),
	ROCNAR(83, 387, 13373, 8305, new Item(995, 150000)),
	OAK_CAGE(65, 640, new int[] { 13313, 13313 }, 8297, new Item(8778, 10), new Item(2353, 2)),
	OAK_AND_STEEL_CAGE(70, 800, new int[] { 13316, 13316 }, 8298, new Item(8778, 10), new Item(2353, 10)),
	STEEL_CAGE(75, 400,new int[] {  13319, 13319 }, 8299, new Item(2353, 20)),
	SPIKED_CAGE(80, 500, new int[] { 13322, 13322 }, 8300, new Item(2353, 25)),
	BONE_CAGE(85, 603, new int[] { 13325, 13325 }, 8301, new Item(8778, 10), new Item(526, 10)),
	OAK_LADDER(68, 300, 13328, 8306, new Item(8778, 5)),
	TEAK_LADDER(78, 450, 13329, 8307, new Item(8780, 5)),
	MAHOGANY_LADDER(88, 700, 13330, 8308, new Item(8782, 5)),
	/**
	 * Dining room
	 */
	WOOD_DINING_TABLE(10, 115, 13293, 8115, new Item(960, 4), new Item(4819, 4)),
	OAK_DINING_TABLE(22, 240, 13293, 8116, new Item(8778, 4)),
	CARVED_OAK_TABLE(31, 360, 13295, 8117, new Item(8778, 6)),
	TEAK_TABLE(38, 360, 13296, 8118, new Item(8780, 4)),
	CARVED_TEAK_TABLE(45, 600, 13297, 8119, new Item(8780, 6), new Item(8790, 4)),
	MAHOGANY_TABLE(52, 840, 13298, 8120, new Item(8782, 6)),
	OPULENT_TABLE(72, 3100, 13299, 8121, new Item(8782, 6), new Item(8790, 4), new Item(8784, 4), new Item(8786, 2)),
	WOODEN_BENCH(10, 115, 13300, 8108, new Item(960, 4), new Item(4819, 4)),
	OAK_BENCH(22, 240, 13301, 8109, new Item(8778, 4)),
	CARVED_OAK_BENCH(31, 240, 13302, 8110, new Item(8778, 4)),
	TEAK_DINING_BENCH(38, 360, 13303, 8111, new Item(8780, 4)),
	CARVED_TEAK_BENCH(44, 360, 13304, 8112, new Item(8780, 4)),
	MAHOGANY_BENCH(52, 560, 13305, 8113, new Item(8782, 4)),
	GILDED_BENCH(61, 1760, 13306, 8114, new Item(8782, 4), new Item(8784, 4)),
	/**
	 * TODO: Decorations have a lot of variations under the name of TYPE wall decoration (13782+)
	 */
	OAK_DECORATION(16, 120, 6778, 8102, new Item(8778, 2)),
	TEAK_DECORATION(36, 180, 6779, 8103, new Item(8780, 2)),
	GILDED_DECORATION(56, 1020, 13782, 8104, new Item(8782, 3), new Item(8784, 2)),
	ROPE_BELL_PULL(26, 64, 13307, 8099, new Item(8778), new Item(954)),
	BELL_PULL(37, 120, 13308, 8100, new Item(8780), new Item(8790)),
	POSH_BELL_PULL(60, 420, 13309, 8101, new Item(8780), new Item(8790, 2), new Item(8784)),
	/**
	 * Skill hall
	 */
	OAK_STAIRCASE_DS(27, 680, 13498, 8249, new Item(8778, 10), new Item(2353, 4)),
    OAK_STAIRCASE(27, 680, 13497, 8249, new Item(8778, 10), new Item(2353, 4)),
    TEAK_STAIRCASE_DS(48, 980, 13500, 8252, new Item(8780, 10), new Item(2353, 4)),
    TEAK_STAIRCASE(48, 980, 13499, 8252, new Item(8780, 10), new Item(2353, 4)),
    SPIRAL_STAIRCASE_DS(67, 1040, 13503, 8258, new Item(8780, 10), new Item(3420, 7)),
    SPIRAL_STAIRCASE(67, 1040, 13503, 8258, new Item(8780, 10), new Item(3420, 7)),
    MARBLE_STAIRCASE_DS(82, 3200, 13502, 8255, new Item(8782, 5), new Item(8786, 5)),
    MARBLE_STAIRCASE(82, 3200, 13501, 8255, new Item(8782, 5), new Item(8786, 5)),
    MARBLE_SPIRAL_DS(97, 4400, 13505, 8259, new Item(8780, 10), new Item(8786, 7)),
    MARBLE_SPIRAL(97, 4400, 13505, 8259, new Item(8780, 10), new Item(8786, 7)),
	
	CW_ARMOUR_1(28, 135, 13494, 8273, new Item(8778, 2), new Item(4071), new Item(4069), new Item(4070)),
	CW_ARMOUR_2(28, 150, 13495, 8274, new Item(8778, 2), new Item(4506), new Item(4504), new Item(4505)),
	CW_ARMOUR_3(28, 165, 13496, 8275, new Item(8778, 2), new Item(4511), new Item(4509), new Item(4510)),
	/**
	 * Requires 68/88/99 smithing respectively.
	 * Grants 25 smithing experience for each (Same for all)
	 */
	MITHRIL_ARMOUR(28, 135, 13491, 8085, new Item(8778, 2), new Item(1159), new Item(1121), new Item(1085)),
	ADAMANT_ARMOUR(28, 150, 13492, 8271, new Item(8778, 2), new Item(1161), new Item(1123), new Item(1091)),
	RUNITE_ARMOUR(28, 165, 13493, 8272, new Item(8778, 2), new Item(1163), new Item(1127), new Item(1093)),
	/**
	 * Grants slayer experience alongside.
	 */
	CRAWLING_HAND(38, 211, 13481, 8260, new Item(8780, 2), new Item(7982)),
	COCKATRICE_HEAD(38, 224, 13482, 8087, new Item(8780, 2), new Item(7983)),
	BASILISK_HEAD(38, 243, 13483, 8262, new Item(8780, 2), new Item(7984)),
	KURASK_HEAD(58, 357, 13484, 8088, new Item(8782, 2), new Item(7985)),
	ABYSSAL_HEAD(58, 389, 13485, 8264, new Item(8782, 2), new Item(7986)),
	KBD_HEADS(78, 1103, 13486, 8089, new Item(8782, 2), new Item(7987), new Item(8784, 2)),
	KQ_HEAD(78, 1103, 13487, 8266, new Item(8782, 2), new Item(7988), new Item(8784, 2)),
	VORKATH_HEAD(82, 1103, 31977, 21912, new Item(8782, 2), new Item(8784, 2), new Item(21909)),
	/**
	 * Grants equal amount of experience to both fishing and construction.
	 */
	MOUNTED_BASS(36, 151, 13488, 8267, new Item(8778, 2), new Item(7990)),
	MOUNTED_SWORDFISH(56, 230, 13489, 8268, new Item(8780, 2), new Item(7992)),
	MOUNTED_SHARK(76, 350, 13490, 8269, new Item(8782, 2), new Item(7994)),
	/**
	 * Requires 14, 44 & 90 runecrafting respectively.
	 * Grants 25 experience in rune crafting for each (Same for all)
	 */
	RUNE_CASE_1(41, 190, 13507, 8276, new Item(8780, 2), new Item(1775), new Item(556), new Item(555), new Item(557), new Item(554)),
	RUNE_CASE_2(41, 212, 13508, 8277, new Item(8780, 2), new Item(1775, 2), new Item(559), new Item(564), new Item(562), new Item(561)),
	RUNE_CASE_3(41, 212, 13509, 8278, new Item(8780, 2), new Item(1775, 2), new Item(563), new Item(560), new Item(565), new Item(566)),
	
	DISPLAYED_RUNE_CASES(41, 190, 13507, 8095, new Item(8780, 2), new Item(1775, 2)),
	
	/**
	 * Games room
	 */
	CLAY_ATTACK_STONE(39, 100, 13392, 8153, new Item(1761, 10)),
	ATTACK_STONE(59, 200, 13393, 8154, new Item(3420, 10)),
	MARBLE_ATTACK_STONE(79, 2000, 13394, 8155, new Item(8786, 4)),
	
	MAGICAL_BALANCE_1(37, 176, 13395, 8156, new Item(556, 500), new Item(555, 500), new Item(557, 500), new Item(554, 500)),
	MAGICAL_BALANCE_2(57, 252, 13396, 8157, new Item(556, 1000), new Item(555, 1000), new Item(557, 1000), new Item(554, 1000)),
	MAGICAL_BALANCE_3(77, 356, 13397, 8158, new Item(556, 2000), new Item(555, 2000), new Item(557, 2000), new Item(554, 2000)),
	
	HOOP_AND_STICK(30, 120, 13398, 8162, new Item(8778, 2)),
	DARTBOARD(54, 290, 13400, 8163, new Item(8780, 3), new Item(2353)),
	ARCHERY_TARGET(81, 600, 13402, 8164, new Item(8780, 6), new Item(2353, 3)),
	
	JESTER(39, 360, 13390, 8159, new Item(8780, 4)),
	TREASURE_HUNT(49, 800, 13379, 8160, new Item(8780, 8), new Item(2353, 4)),
	HANGMAN(59, 1200, 13404, 8161, new Item(8780, 12), new Item(2353, 6)),
	
	OAK_PRIZE_CHEST(34, 240, 13385, 8165, new Item(8778, 4)),
	TEAK_PRIZE_CHEST(44, 660, 13387, 8166, new Item(8780, 4), new Item(8784)),
	MAHOGANY_PRIZE_CHEST(54, 860, 13389, 8167, new Item(8782, 4), new Item(8784)),
	/**
	 * Combat room
	 */
	BOXING_RING(32, 420,  new int[] { 13129, 13130, 13130, 13129, 13130, 13130, 13129, 13130, 13129, 13130, 13126, 13127, 13128, 13128, 13126, 13126, 13128, 13126 }, 8023, new Item(8778, 6), new Item(8790, 4)),
	FENCING_RING(41, 570, new int[] { 13133, 13133, 13133, 13133, 13133, 13133, 13133, 13133, 13133, 13133, 13135, 13136, 13134, 13134, 13135, 13135, 13134, 13135 }, 8024, new Item(8778, 8), new Item(8790, 6)),
	COMBAT_RING(51, 630, new int[] { 13137, 13137, 13137, 13137, 13137, 13137, 13137, 13137, 13137, 13137, 13138, 13140, 13139, 13139, 13138, 13138, 13139, 13138 }, 8025, new Item(8780, 6), new Item(8790, 6)),
	RANGING_PEDESTALS(71, 720, new int[] { 0, 0, 13145, 0, 0, 0, 0, 13145, 0, 0, 0, 0, 0, 0, 13147, 0, 0, 13147 }, 8026, new Item(8780, 8)),
	BALANCE_BEAM(81, 1000, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13144, 0, 0, 13142, 13143, 0, 0, 0 }, 8027, new Item(8780, 10), new Item(2353, 5)),
	
	GLOVE_RACK(34, 120, 13381, 8028, new Item(8778, 2)),
	WEAPONS_RACK(44, 180, 13382, 8029, new Item(8780, 4)),
	EXTRA_WEAPONS_RACK(54, 440, 13383, 8030, new Item(8780, 4), new Item(2353, 4)),
	
	COMBAT_DUMMY(48, 660, 29336, 20745, new Item(8780, 5), new Item(8790, 4), new Item(1783, 5)),
	UNDEAD_COMBAT_DUMMY(53, 220, 29337, 20746, new Item(20745), new Item(8921), new Item(4286)),
	/**
	 * Quest hall
	 */
	ANTI_DRAGON_SHIELD(47, 280, 13522, 8282, new Item(8780, 3), new Item(1540)),
	AMULET_OF_GLORY(47, 290, 13523, 8283, new Item(8780, 3), new Item(1704)),
	CAPE_OF_LEGENDS(47, 300, 13524, 8284, new Item(8780, 3), new Item(1052)),
	MYTHICAL_CAPE(47, 370, 31986, 21913, new Item(8780, 3), new Item(22114)),
	
	SMALL_MAP(38, 211, 13525, 8294, new Item(8780, 2), new Item(8004)),
	MEDIUM_MAP(58, 451, 13526, 8295, new Item(8782, 3), new Item(8005)),
	LARGE_MAP(78, 591, 13527, 8296, new Item(8782, 4), new Item(8006)),
	
	KING_ARTHUR(35, 211, 13510, 8086, new Item(8080, 2), new Item(7995)),
	ELENA(35, 211, 13511, 8286, new Item(8080, 2), new Item(7996)),
	GIANT_DWARF(35, 211, 13512, 8287, new Item(8780, 2), new Item(7997)),
	MISCELLANIANS(55, 311, 13513, 8093, new Item(8782, 2), new Item(7998)),
	
	LUMBRIDGE(44, 314, 13517, 8091, new Item(8780, 3), new Item(8002)),
	THE_DESERT(44, 314, 13514, 8290, new Item(8780, 3), new Item(7999)),
	MORYTANIA(44, 314, 13518, 8291, new Item(8780, 3), new Item(8003)),
	KARAMJA(65, 464, 13516, 8292, new Item(8782, 3), new Item(8001)),
	ISAFDAR(65, 464, 13515, 8094, new Item(8782, 3), new Item(8000)),
	
	SILVERLIGHT(42, 187, 13519, 8090, new Item(8780, 2), new Item(2402)),
	EXCALIBUR(42, 194, 13521, 8280, new Item(8780, 0), new Item(35)),
	DARKLIGHT(42, 202, 13520, 8281, new Item(8780, 2), new Item(8281)),
	/**
	 * Menagerie
	 */
	OAK_SCRATCHING_POST(39, 124, 26858, 12715, new Item(8778, 2), new Item(954)),
	TEAK_SCRATCHING_POST(49, 204, 26859, 12716, new Item(8780, 2), new Item(954), new Item(3420)),
	MAHOGANY_SCRATCHING_POST(59, 304, 26860, 12717, new Item(8782, 2), new Item(954), new Item(3420)),
	
	OAK_FEEDER(37, 182, 26870, 12722, new Item(8778, 3), new Item(1927)),
	TEAK_FEEDER(48, 272, 26871, 12723, new Item(8780, 3), new Item(1927)),
	MAHOGANY_FEEDER(59, 862, 26872, 12724, new Item(8782, 4), new Item(1927), new Item(8784)),
	
	OAK_HOUSE(37, 240, 26297, 12704, new Item(8778, 4)),
	TEAK_HOUSE(48, 360, 26298, 12705, new Item(12704), new Item(8780, 4)),
	MAHOGANY_HOUSE(59, 560, 26299, 12706, new Item(12705), new Item(8782, 4)),
	CONSECRATED_HOUSE(70, 1560, 26830, 12707, new Item(12706), new Item(8782, 4), new Item(8788)),
	DESECRATED_HOUSE(81, 160, 26831, 12708, new Item(12707), new Item(8782), new Item(3420)),
	NATURE_HOUSE(92, 158, 26832, 12709, new Item(12708), new Item(8782), new Item(1929, 2), new Item(6034, 3)),
	
	GRASSLAND_HABITAT(37, 37, new int[] { 26834, 26840, 26846, 26852 }, 12710, new Item(8417), new Item(6032, 2), new Item(5331)),
	FOREST_HABITAT(47, 51, new int[] { 26835, 26841, 26847, 26853 }, 12711, new Item(8419), new Item(6032, 3), new Item(5331)),
	DESERT_HABITAT(57, 34, new int[] { 26836, 26842, 26848, 26854 }, 12712, new Item(8431), new Item(1783, 5), new Item(5331)),
	POLAR_HABITAT(67, 271, new int[] { 26837, 26843, 26849, 26855 }, 12713, new Item(8778, 3), new Item(555, 2000), new Item(6696, 5)),
	VOLCANIC_HABITAT(77, 46, new int[] { 26838, 26844, 26850, 26856 }, 12714, new Item(6983, 5), new Item(4699, 100)),
	
	SIMPLE_ARENA(63, 139, new int[] { 26862, 26865 }, 12718, new Item(8778, 2), new Item(8790), new Item(954)),
	ADVANCED_ARENA(73, 199, new int[] { 26863, 26865 }, 12719, new Item(8780, 2), new Item(8790), new Item(954)),
	GLORIOUS_ARENA(83, 299, new int[] { 26864, 26865 }, 12720, new Item(8782, 2), new Item(8790), new Item(954)),
	
	PET_LIST(38, 198, 26868, 12721, new Item(8778, 3), new Item(8790), new Item(970)),
	/**
	 * Study
	 */
	GLOBE(41, 180, 13649, 8341, new Item(8778, 3)),
	ORNAMENTAL_GLOBE(50, 270, 13650, 8342, new Item(8780, 3)),
	LUNAR_GLOBE(59, 570, 13651, 8343, new Item(8780, 3), new Item(8784)),
	CELESTIAL_GLOBE(68, 570, 13652, 8344, new Item(8780, 3), new Item(8784)),
	ARMILLARY_SPHERE(77, 960, 13653, 8345, new Item(8782, 2), new Item(8784, 2), new Item(2353, 4)),
	SMALL_ORRERY(86, 1320, 13654, 8346, new Item(8782, 3), new Item(8784, 3)),
	LARGE_ORRERY(95, 1420, 13655, 8347, new Item(8782, 3), new Item(8784, 5)),
	
	OAK_LECTERN(40, 60, 13642, 8334, new Item(8778)),
	EAGLE_LECTERN(47, 120, 13643, 8335, new Item(8778, 2)),
	DEMON_LECTERN(47, 120, 13644, 8336, new Item(8778, 2)),
	TEAK_EAGLE_LECTERN(57, 180, 13645, 8337, new Item(8780, 2)),
	TEAK_DEMON_LECTERN(57, 180, 13646, 8338, new Item(8780, 2)),
	MAHOGANY_EAGLE_LECTERN(67, 580, 13647, 8339, new Item(8782, 2), new Item(8784)),
	MAHOGANY_DEMON_LECTERN(67, 580, 13648, 8340, new Item(8782, 2), new Item(8784)),
	
	CRYSTAL_BALL(42, 280, 13659, 8351, new Item(8780, 3), new Item(567)),
	ELEMENTAL_SPHERE(54, 580, 13660, 8352, new Item(8780, 3), new Item(567), new Item(8784)),
	CRYSTAL_OF_POWER(66, 890, 13661, 8353, new Item(8782, 2), new Item(567), new Item(8784, 2)),
	
	WOODEN_TELESCOPE(44, 121, 13656, 8348, new Item(8778, 2), new Item(1775)),
	TEAK_TELESCOPE(64, 181, 13657, 8349, new Item(8780, 2), new Item(1775)),
	MAHOGANY_TELESCOPE(84, 281, 13658, 8350, new Item(8782, 2), new Item(1775)),
	
	ALCHEMICAL_CHART(43, 30, 13662, 8354, new Item(8790, 2)),
	ASTRONOMICAL_CHART(63, 45, 13663, 8355, new Item(8790, 3)),
	INFERNAL_CHART(83, 60, 13664, 8356, new Item(8790, 4)),
	/**
	 * Costume room
	 */
	OAK_ARMOUR_CASE(46, 180, 18778, 9826, new Item(8778, 3)),
	TEAK_ARMOUR_CASE(64, 270, 18780, 9827, new Item(8780, 3)),
	MAHOGANY_ARMOUR_CASE(82, 420, 18782, 9828, new Item(8782, 3)),
	
	OAK_CAPE_RACK(54, 240, 18766, 9817, new Item(8778, 4)),
	TEAK_CAPE_RACK(63, 360, 18767, 9818, new Item(8780, 4)),
	MAHOGANY_CAPE_RACK(72, 560, 18768, 9819, new Item(8782, 4)),
	GILDED_CAPE_RACK(81, 860, 18769, 9820, new Item(8782, 4), new Item(8784)),
	MARBLE_CAPE_RACK(90, 500, 18770, 9821, new Item(8786)),
	MAGICAL_CAPE_RACK(99, 1000, 18771, 9822, new Item(8788)),
	
	OAK_COSTUME_BOX(44, 120, 18772, 9823, new Item(8778, 2)),
	TEAK_COSTUME_BOX(62, 180, 18774, 9824, new Item(8780, 2)),
	MAHOGANY_COSTUME_BOX(80, 280, 18776, 9825, new Item(8782, 2)),
	
	OAK_MAGIC_WARDROBE(42, 240, 18784, 9829, new Item(8778, 4)),
	CARVED_OAK_MAGIC_WARDROBE(51, 360, 18786, 9830, new Item(8778, 6)),
	TEAK_MAGIC_WARDROBE(60, 360, 18788, 9831, new Item(8780, 4)),
	CARVED_TEAK_MAGIC_WARDROBE(69, 540, 18790, 9832, new Item(8780, 6)),
	MAHOGANY_MAGIC_WARDROBE(78, 540, 18792, 9833, new Item(8782, 4)),
	GILDED_MAGIC_WARDROBE(87, 860, 18794, 9834, new Item(8782, 4), new Item(8784)),
	MARBLE_MAGIC_WARDROBE(96, 500, 18796, 9835, new Item(8786)),
	
	OAK_TOY_BOX(50, 120, 18798, 9836, new Item(8778, 2)),
	TEAK_TOY_BOX(68, 180, 18800, 9837, new Item(8780, 2)),
	MAHOGANY_TOY_BOX(86, 280, 18802, 9838, new Item(8782, 2)),
	
	OAK_TREASURE_CHEST(48, 120, 18804, 9839, new Item(8778, 2)),
	TEAK_TREASURE_CHEST(66, 180, 18806, 9840, new Item(8780, 2)),
	MAHOGANY_TREASURE_CHEST(84, 280, 18808, 9841, new Item(8782, 2)),
	/**
	 * Chapel
	 */
	STEEL_TORCHES(45, 80, 13202, 8070, new Item(2353, 2)),
	WOODEN_TORCHES(49, 58, 13200, 8069, new Item(960, 2), new Item(4819, 2)),
	STEEL_CANDLESTICKS(53, 124, 13204, 8071, new Item(2353, 6), new Item(36, 5)),
	GOLD_CANDLESTICKS(57, 46, 13206, 8072, new Item(2357, 6), new Item(36, 5)),
	INCENSE_BURNERS(61, 280, 13208, 8073, new Item(8778, 4), new Item(2353, 2)),
	MAHOGANY_BURNERS(65, 600, 13210, 8074, new Item(8782, 4), new Item(2353, 2)),
	MARBLE_BURNERS(69, 1600, 13212, 8075, new Item(8786, 2), new Item(2353, 2)),
	
	OAK_ALTAR(45, 240, 13179, 8062, new Item(8778, 4)),
	TEAK_ALTAR(50, 360, 13182, 8063, new Item(8780, 4)),
	CLOTH_COVERED_ALTAR(56, 390, 13185, 8064, new Item(8780, 4), new Item(8790, 2)),
	MAHOGANY_ALTAR(60, 590, 13188, 8065, new Item(8782, 4), new Item(8790, 2)),
	LIMESTONE_ALTAR(64, 910, 13191, 8066, new Item(8782, 6), new Item(8790, 2), new Item(3420, 2)),
	MARBLE_ALTAR(70, 1030, 13194, 8067, new Item(8786, 2), new Item(8790, 2)),
	GILDED_ALTAR(75, 2230, 13197, 8068, new Item(8786, 2), new Item(8790, 2), new Item(8784, 4)),
	
	SARADOMIN_SYMBOL(48, 120, 13172, 8055, REFRESH_ROOM, new Item(8778, 2)),
	ZAMORAK_SYMBOL(48, 120, 13173, 8056, REFRESH_ROOM, new Item(8778, 2)),
	GUTHIX_SYMBOL(48, 120, 13174, 8057, REFRESH_ROOM, new Item(8778, 2)),
	SARADOMIN_ICON(59, 960, 13175, 8058, REFRESH_ROOM, new Item(8780, 4), new Item(8784, 2)),
	GUTHIX_ICON(59, 960, 13177, 8060, REFRESH_ROOM, new Item(8780, 4), new Item(8784, 2)),
	ZAMORAK_ICON(59, 960, 13176, 8059, REFRESH_ROOM, new Item(8780, 4), new Item(8784, 2)),
	ICON_OF_BOB(71, 1160, 13178, 8061, REFRESH_ROOM, new Item(8782, 4), new Item(8784, 2)),
	
	SMALL_STATUES(49, 40, new int[] { 13271, 13274, 13277, 13280 }, 8082, (con, ref, data, object, onEnter) -> {
		object.setId(data.getFurniture().getObjectIds()[ConstructionAction.getStatueOffset(ref)]);
	}, REFRESH_ROOM, new Item(3420, 2)),
	MEDIUM_STATUES(69, 500, new int[] { 13272, 13275, 13278, 13281 }, 8083, (con, ref, data, object, onEnter) -> {
		object.setId(data.getFurniture().getObjectIds()[ConstructionAction.getStatueOffset(ref)]);
	}, REFRESH_ROOM, new Item(8486)),
	LARGE_STATUES(89, 1500, new int[] { 13273, 13276, 13279, 13282 }, 8084, (con, ref, data, object, onEnter) -> {
		object.setId(data.getFurniture().getObjectIds()[ConstructionAction.getStatueOffset(ref)]);
	}, REFRESH_ROOM, new Item(8486, 3)),
	
	WINDCHIMES(49, 323, 13214, 8079, new Item(8778, 4), new Item(4819, 4), new Item(2353, 4)),
	BELLS(58, 480, 13215, 8080, new Item(8780, 4), new Item(2353, 6)),
	ORGAN(69, 680, 13216, 8081, new Item(8782, 4), new Item(2353, 6)),
	
	SHUTTERED_WINDOW(49, 228, new int[] { 13253, 13226, 13235, 13244, 13217, 13262, 27073 }, 8076, (con, ref, data, object, onEnter) -> {
		object.setId(data.getFurniture().getObjectIds()[con.getDecoration()]);
	}, REFRESH_ROOM, new Item(960, 8), new Item(4819, 8)),
	DECORATIVE_WINDOW(69, 4, new int[] { 13254, 13227, 13236, 13245, 13218, 13263, 27074, 13256, 13229, 13238, 13247, 13220, 13265, 27076 }, 8077, (con, ref, data, object, onEnter) -> {
		object.setId(data.getFurniture().getObjectIds()[con.getDecoration()] + (ConstructionAction.getWindowIconOffset(ref) * 2));
	}, REFRESH_ROOM, new Item(1775, 8)),
	STAINED_GLASS(89, 5, new int[] { 13255, 13228, 13237, 13246, 13219, 13264, 27075, 13257, 132300, 13239, 13248, 13221, 13266, 27077 }, 8078,(con, ref, data, object, onEnter) -> {
		object.setId(data.getFurniture().getObjectIds()[con.getDecoration()] + (ConstructionAction.getWindowIconOffset(ref) * 2));
	}, REFRESH_ROOM, new Item(1775, 16)),
	/**
	 * Portal chamber
	 */
	TELEPORT_FOCUS(50, 40, 13640, 8331, new Item(3420, 2)),
	GREATER_FOCUS(65, 500, 13641, 8332, new Item(8786)),
	SCRYING_POOL(80, 2000, 13639, 8333, new Item(8786, 4)),
	
	TEAK_PORTAL(50, 270, 13636, 8328, new Item(8780, 3)),
	MAHOGANY_PORTAL(65, 420, 13637, 8329, new Item(8782, 3)),
	MARBLE_PORTAL(80, 1500, 13638, 8330, new Item(8786, 3)),
	
	TEAK_VARROCK_PORTAL(13615),
	TEAK_LUMBRIDGE_PORTAL(13616),
	TEAK_FALADOR_PORTAL(13617),
	TEAK_CAMELOT_PORTAL(13618),
	TEAK_ARDOUGNE_PORTAL(13619),
	TEAK_YANILLE_PORTAL(13620),
	TEAK_KHARYRLL_PORTAL(29338),
	TEAK_LUNAR_ISLE_PORTAL(29339),
	TEAK_SENNTISTEN_PORTAL(29340),
	TEAK_ANNAKARL_PORTAL(29341),
	TEAK_WATERBIRTH_ISLAND_PORTAL(29342),
	TEAK_FISHING_GUILD_PORTAL(29343),
	TEAK_MARIM_PORTAL(29344),
	TEAK_KOUREND_PORTAL(29345),
	
	MAHOGANY_VARROCK_PORTAL(13622),
	MAHOGANY_LUMBRIDGE_PORTAL(13623),
	MAHOGANY_FALADOR_PORTAL(13624),
	MAHOGANY_CAMELOT_PORTAL(13625),
	MAHOGANY_ARDOUGNE_PORTAL(13626),
	MAHOGANY_YANILLE_PORTAL(13627),
	MAHOGANY_KHARYRLL_PORTAL(29346),
	MAHOGANY_LUNAR_ISLE_PORTAL(29347),
	MAHOGANY_SENNTISTEN_PORTAL(29348),
	MAHOGANY_ANNAKARL_PORTAL(29349),
	MAHOGANY_WATERBIRTH_ISLAND_PORTAL(29350),
	MAHOGANY_FISHING_GUILD_PORTAL(29351),
	MAHOGANY_MARIM_PORTAL(29352),
	MAHOGANY_KOUREND_PORTAL(29353),
	
	MARBLE_VARROCK_PORTAL(13629),
	MARBLE_LUMBRIDGE_PORTAL(13630),
	MARBLE_FALADOR_PORTAL(13631),
	MARBLE_CAMELOT_PORTAL(13632),
	MARBLE_ARDOUGNE_PORTAL(13633),
	MARBLE_YANILLE_PORTAL(13634),
	MARBLE_KHARYRLL_PORTAL(29354),
	MARBLE_LUNAR_ISLE_PORTAL(29355),
	MARBLE_SENNTISTEN_PORTAL(29356),
	MARBLE_ANNAKARL_PORTAL(29357),
	MARBLE_WATERBIRTH_ISLAND_PORTAL(29358),
	MARBLE_FISHING_GUILD_PORTAL(29359),
	MARBLE_MARIM_PORTAL(29360),
	MARBLE_KOUREND_PORTAL(29361),
	/**
	 * Formal garden
	 */
	GAZEBO(65, 1200, 6748, 8192, new Item(8782, 8), new Item(2353, 4)),
	SMALL_FOUNTAIN(71, 500, 6749, 8193, new Item(8786)),
	LARGE_FOUNTAIN(75, 1000, 6750, 8194, new Item(8786, 2)),
	POSH_FOUNTAIN(81, 1500, 6751, 8195, new Item(8786, 3)),
	
	SMALL_SUNFLOWER(66, 70, 5149, 8213, new Item(8457), WATERING_CAN),
	SMALL_MARIGOLDS(71, 100, 5150, 8214, new Item(8459), WATERING_CAN),
	SMALL_ROSES(76, 122, 5151, 8215, new Item(8461), WATERING_CAN),
	
	SUNFLOWER(66, 70, 5146, 8213, new Item(8457), WATERING_CAN),
	MARIGOLDS(71, 100, 5147, 8214, new Item(8459), WATERING_CAN),
	ROSES(76, 122, 5148, 8215, new Item(8461), WATERING_CAN),
	
	SMALL_ROSEMARY(66, 70, 5143, 8210, new Item(8251), WATERING_CAN),
	SMALL_DAFFODILS(71, 100, 5144, 8211, new Item(8453), WATERING_CAN),
	SMALL_BLUEBELLS(76, 122, 5145, 8212, new Item(8455), WATERING_CAN),
	
	ROSEMARY(66, 70, 5140, 8210, new Item(8251), WATERING_CAN),
	DAFFODILS(71, 100, 5141, 8211, new Item(8453), WATERING_CAN),
	BLUEBELLS(76, 122, 5142, 8212, new Item(8455), WATERING_CAN),
	
	THORNY_HEDGE(56, 70, new int[] { 6727, 6729, 6728 }, 8203, new Item(8437), WATERING_CAN),
	NICE_HEDGE(60, 100, new int[] { 6730 }, 8204, new Item(8439), WATERING_CAN),
	SMALL_BOX_HEDGE(64, 122, new int[] { 6733, 6735, 6734 }, 8205, new Item(8441), WATERING_CAN),
	TOPIARY_HEDGE(68, 141, new int[] { 6736, 6738, 6737 }, 8206, new Item(8443), WATERING_CAN),
	FANCY_HEDGE(72, 158, new int[] { 6739, 6741, 6740 }, 8207, new Item(8445), WATERING_CAN),
	TALL_FANCY_HEDGE(76, 223, new int[] { 6742, 6744, 6743 }, 8208, new Item(8447), WATERING_CAN),
	TALL_BOX_HEDGE(80, 316, new int[] { 6745, 6747, 6746 }, 8209, new Item(8449), WATERING_CAN),
	
	KRAKEN_TOPIARY(29231),
	ZULRAH_TOPIARY(29232),
	KALPHITE_QUEEN_TOPIARY(29233),
	CERBERUS_TOPIARY(29234),
	ABYSSAL_SIRE_TOPIARY(29235),
	SKOTIZO_TOPIARY(29236),
	VORKATH_TOPIARY(31985),
	
	BOUNDARY_STONES(55, 100, 5152, 8196, new Item(1761, 10)),
	WOODEN_FENCE(59, 280, 5153, 8197, new Item(960, 10)),
	STONE_WALL(63, 200, 5154, 8198, new Item(3420, 10)),
	IRON_RAILINGS(67, 220, 5155, 8199, new Item(2351, 10), new Item(3420, 6)),
	PICKET_FENCE(71, 640, 5631, 8200, new Item(8778, 10), new Item(2353, 2)),
	GARDEN_FENCE(75, 940, 5632, 8201, new Item(8780, 10), new Item(2353, 2)),
	MARBLE_WALL(79, 4000, 5907, 8202, new Item(8786, 8)),
	
	/**
	 * Throne room
	 */
	OAK_THRONE(60, 800, 13665, 8357, new Item(8778, 5), new Item(8786)),
	TEAK_THRONE(67, 1450, 13666, 8358, new Item(8780, 5), new Item(8786, 2)),
	MAHOGANY_THRONE(74, 2200, 13667, 8359, new Item(8782, 5), new Item(8786, 3)),
	GILDED_THRONE(81, 1700, 13668, 8360, new Item(8782, 5), new Item(8786, 2), new Item(8784, 3)),
	SKELETON_THRONE(88, 7003, 13669, 8361, new Item(8788, 5), new Item(8786, 4), new Item(526, 5), new Item(964, 2)),
	CRYSTAL_THRONE(95, 15000, 13670, 8362, new Item(8788, 15)),
	DEMONIC_THRONE(99, 25000, 13671, 8363, new Item(8788, 25)),
	
	ROUND_SHIELD(66, 120, 13737, 8105, new Item(8778, 2)),
	SQUARE_SHIELD(76, 360, 13769, 8106, new Item(8780, 4)),
	KITE_SHIELD(86, 420, 13753, 8107, new Item(8782, 3)),
	
	FLOOR_DECORATION(61, 700, 13686, 8370, new Item(8782, 5)),
	THRONE_STEEL_CAGE(68, 1100, 13681, 8371, new Item(8782, 5), new Item(2353, 20)),
	TRAPDOOR(74, 770, 13675, 8372, new Item(8782, 5), new Item(8793, 10)),
	LESSER_MAGIC_CAGE(82, 2700, 13682, 8373, new Item(8782, 5), new Item(8788, 2)),
	GREATER_MAGIC_CAGE(89, 4700, 13683, 8374, new Item(8782, 5), new Item(8788, 4)),
	
	OAK_LEVER(68, 300, 13672, 8365, new Item(8778, 5)),
	TEAK_LEVER(78, 450, 13674, 8364, new Item(8780, 5)),
	MAHOGANY_LEVER(88, 700, 13673, 8366, new Item(8782, 5)),
	
	OAK_TRAPDOOR(68, 300, 13675, 8367, new Item(8778, 5)),
	TEAK_TRAPDOOR(78, 450, 13676, 8368, new Item(8780, 5)),
	MAHOGANY_TRAPDOOR(88, 700, 13677, 8369, new Item(8782, 5)),
	/**
	 * Superior garden
	 */
	//Requires 83 farming as well
	SPIRIT_TREE(75, 700, 29227, 20635, new Item(5375)),
	OBELISK(80, 3000, 31554, 21788, new Item(21804, 4), new Item(8786, 4)),
	FAIRY_RING(85, 535, 29228, 20636, new Item(6004, 10), new Item(20609)),
	SPIRIT_TREE_AND_FAIRY_RING(95, 1170, 29422, 20637, new Item(5375), new Item(6004, 10), new Item(20609)),
	
	TOPIARY_BUSH(65, 141, 29230, 20638, new Item(8443)),
	
	RESTORATION_POOL(65, 706, 29237, 20639, new Item(3420, 5), new Item(1929, 5), new Item(566, 1000), new Item(559, 1000)),
	REVITALISATION_POOL(70, 850, 29238, 20640, new Item(20639), new Item(12625, 10)),
	REJUVENATION_POOL(80, 900, 29239, 20641, new Item(20640), new Item(2434, 10)),
	FANCY_REJUVENATION_POOL(85, 1950, 29240, 20642, new Item(20641), new Item(3024, 10), new Item(8786, 2)),
	ORNATE_REJUVENATION_POOL(90, 3107, 29241, 20643, new Item(20642), new Item(12905, 10), new Item(8784, 5), new Item(565, 1000)),
	
	ZEN_THEME(65, 297, new int[] { 29246, 29245, 29244, 29242, 29242, 29242, 29243, 29247 }, 20644, new Item(1783, 6), new Item(6955), new Item(8419)),
	OTHERWORDLY_THEME(75, 316, new int[] { 29252, 29251, 29250, 29248, 29248, 29248, 29249, 29253 }, 20645, new Item(6034, 8), new Item(1767), new Item(6004, 4)),//Also requires magic secateurs
	VOLCANIC_THEME(85, 4464, new int[] { 29260, 29259, 29258, 29257, 29256, 29255, 29254, 29261 }, 20646, new Item(6983, 2), new Item(6573, 6), new Item(554, 1000), new Item(4699, 2000)),
	
	REDWOOD_FENCE(75, 240, new int[] { 29262, 29263, 29264 }, 20647, new Item(19669, 10), new Item(2353, 2)),
	MARBLE_WALL2(79, 4000, new int[] { 5907, 5907, 5907 }, 8202, new Item(8786, 8)),
	OBSIDIAN_FENCE(83, 2741, new int[] { 29267, 29268, 29269 }, 20648, new Item(6526, 10), new Item(6528, 2), new Item(6522, 25)),
	
	TEAK_GARDEN_BENCH(66, 540, new int[] { 29270, 29271 }, 20649, new Item(8780, 6)),
	GNOME_BENCH(77, 840, new int[] { 29272, 29273 }, 20650, new Item(8782, 6)),
	MARBLE_DECORATIVE_BENCH(88, 3000, new int[] { 29274, 29275 }, 20651, new Item(8786, 6)),
	OBSIDIAN_DECORATIVE_BENCH(98, 2331, new int[] { 29276, 29277 }, 20652, new Item(8786, 3), new Item(6573), new Item(554, 250), new Item(4699, 500)),
	
	/**
	 * Raids
	 */
	SMALL_STORAGE_UNIT(30, 150, 29770, 21037, new Item(21036, 2)),
	MEDIUM_STORAGE_UNIT(60, 150, 29779, 21038, new Item(21036, 4)),
	LARGE_STORAGE_UNIT(90, 150, 29780, 21039, new Item(21036, 6)),
	
	/**
	 * Stash units
	 */
	STASH_UNITS_EASY(27, 150, 29780, 20532, new Item(960, 2), new Item(4819, 10)),
	STASH_UNITS_MEDIUM(42, 250, 29780, 20533, new Item(8778, 2), new Item(4819, 10)),
	STASH_UNITS_HARD(55, 400, 29780, 20534, new Item(8780, 2), new Item(4819, 10)),
	STASH_UNITS_ELITE(77, 600, 29780, 20535, new Item(8782, 2), new Item(4819, 10)),
	STASH_UNITS_MASTER(88, 1500, 29780, 20536, new Item(8782, 2), new Item(8784), new Item(4819, 10)),
	
	
	/**
	 * Treasure room
	 */
	DEMON(75, 707, 13378, 8138, new Item(995, 500000)),
	KALPHITE_SOLDIER(80, 866, 13374, 8139, new Item(995, 750000)),
	TOK_XIL(85, 2236, 13377, 8140, new Item(995, 5000000)),
	DAGANNOTH(90, 2738, 13376, 8141, new Item(995, 7500000)),
	STEEL_DRAGON(95, 3162, 13375, 8142, new Item(995, 10000000)),
	RUNE_DRAGON(99, 5000, 31984, 21911, new Item(995, 25000000)),
	
	WOODEN_CRATE(75, 143, 13283, 8148, new Item(960, 5), new Item(4819, 5)),
	OAK_CHEST(79, 340, 13285, 8149, new Item(8778, 5), new Item(2353, 2)),
	TEAK_CHEST(83, 530, 13287, 8150, new Item(8780, 5), new Item(2353, 4)),
	MAHOGANY_CHEST(87, 1000, 13289, 8151, new Item(8782, 5), new Item(8784)),
	MAGIC_CHEST(91, 1000, 13291, 8152, new Item(8788)),
	/**
	 * Achievement gallery
	 */
	ANCIENT_ALTAR(80, 1490, 29147, 20617, new Item(3420, 10), new Item(8788), new Item(20611), new Item(9050)),
	LUNAR_ALTAR(80, 1957, 29148, 20618, new Item(3420, 10), new Item(8788), new Item(20613), new Item(9075, 10000)),
	DARK_ALTAR(80, 3888, 29149, 20619, new Item(3420, 10), new Item(8788), new Item(20615), new Item(565, 5000), new Item(566, 5000)),
	OCCULT_ALTAR_ANCIENT(90, 3445, 29150, 20620, new Item(3420, 10), new Item(8788), new Item(20613), new Item(9075, 10000), new Item(20615), new Item(565, 5000), new Item(566, 5000)),//TODO Special circumstances
	OCCULT_ALTAR_LUNAR(90, 3445, 29150, 20621, new Item(3420, 10), new Item(8788), new Item(20611), new Item(9050), new Item(20615), new Item(565, 5000), new Item(566, 5000)),//TODO Special circumstances
	OCCULT_ALTAR_DARK(90, 3445, 29150, 20622, new Item(3420, 10), new Item(8788), new Item(20611), new Item(9050), new Item(20613), new Item(9075, 10000)),//TODO Special circumstances

	MAHOGANY_ADVENTURE_LOG(83, 504, 29151, 20623, new Item(8782, 3), new Item(970, 2), new Item(4155)),
	GILDED_ADVENTURE_LOG(88, 1100, 29152, 20624, new Item(8782, 3), new Item(8784, 2), new Item(4155)),
	MARBLE_ADVENTURE_LOG(93, 1160, 29153, 20625, new Item(8786, 2), new Item(3420, 4), new Item(4155)),
	
	BASIC_JEWELLERY_BOX(81, 605, 29154, 20626, new Item(8790), new Item(2353), new Item(3853, 3), new Item(2552, 8)),
	FANCY_JEWELLERY_BOX(86, 1350, 29155, 20627, new Item(8784), new Item(11105, 5), new Item(11118, 5)),
	ORNATE_JEWELLERY_BOX(91, 2680, 29156, 20628, new Item(8784, 2), new Item(1712, 8), new Item(11980, 8)),
	
	BOSS_LAIR_DISPLAY(87, 1483, 29157, 20629, new Item(2353, 4), new Item(1775, 5), new Item(8782, 10)),
	
	KRAKEN_DISPLAY(29158),
	KALPHITE_QUEEN_DISPLAY(29160),
	ZULRAH_DISPLAY(29159),
	CERBERUS_DISPLAY(29161),
	ABYSSAL_SIRE_DISPLAY(29162),
	SKOTIZO_DISPLAY(29163),
	GROTESQUE_GUARDIANS_DISPLAY(31689),
	VORKATH_DISPLAY(31978),
	
	MOUNTED_EMBLEM(80, 5300, 29164, 20630, new Item(8786), new Item(8784), new Item(12856)),
	MOUNTED_COINS(80, 800, 29165, 20631, new Item(8786), new Item(8784), new Item(995, 100000000)),
	CAPE_HANGER(80, 800, 29166, 20632, new Item(8786), new Item(8784)),
	
	UNTRIMMED_ACHIEVEMENT_DIARY_CAPE(29167, 19476, 13070),
	TRIMMED_ACHIEVEMENT_DIARY_CAPE(29168, 13069, 13070),
	FIRE_CAPE(29169, 6570, -1),
	INFERNAL_CAPE(26713, 21295, -1),
	INFERNAL_MAX_CAPE(26714, 21285, 21282),
	REGULAR_MAX_CAPE(29170, 13342, 13281),
	FIRE_MAX_CAPE(29171, 13329, 13330),
	SARADOMIN_MAX_CAPE(29172, 13331, 13332),
	ZAMORAK_MAX_CAPE(29173, 13333, 13334),
	GUTHIX_MAX_CAPE(29174, 13335, 13336),
	AVAS_MAX_CAPE(29175, 13337, 13338),
	ARDOUGNE_MAX_CAPE(29625, 20760, 20764),
	IMBUED_SARADOMIN_MAX_CAPE(31979, 21776, 21778),
	IMBUED_ZAMORAK_MAX_CAPE(31980, 21780, 21782),
	IMBUED_GUTHIX_MAX_CAPE(31981, 21784, 21786),
	ASSEMBLER_MAX_CAPE(31982, 21898, 21900),
	UNTRIMMED_MUSIC_CAPE(29176, 13221, 13223),
	TRIMMED_MUSIC_CAPE(29177, 13222, 13223),
	UNTRIMMED_QUEST_CAPE(29178, 9813, 9814),
	TRIMMED_QUEST_CAPE(29179, 13068, 9814),
	UNTRIMMED_AGILITY_CAPE(29180, 9771, 9773),
	TRIMMED_AGILITY_CAPE(29181, 9772, 9773),
	UNTRIMMED_ATTACK_CAPE(29182, 9747, 9749),
	TRIMMED_ATTACK_CAPE(29183, 9748, 9749),
	UNTRIMMED_CONSTRUCTION_CAPE(29184, 9789, 9791),
	TRIMMED_CONSTRUCTION_CAPE(29185, 9790, 9791),
	UNTRIMMED_COOKING_CAPE(29186, 9801, 9803),
	TRIMMED_COOKING_CAPE(29187, 9802, 9803),
	UNTRIMMED_CRAFTING_CAPE(29188, 9780, 9782),
	TRIMMED_CRAFTING_CAPE(29189, 9781, 9782),
	UNTRIMMED_DEFENCE_CAPE(29190, 9753, 9755),
	TRIMMED_DEFENCE_CAPE(29191, 9754, 9755),
	UNTRIMMED_FARMING_CAPE(29192, 9810, 9812),
	TRIMMED_FARMING_CAPE(29193, 9811, 9812),
	UNTRIMMED_FIREMAKING_CAPE(29194, 9804, 9806),
	TRIMMED_FIREMAKING_CAPE(29195, 9805, 9806),
	UNTRIMMED_FISHING_CAPE(29196, 9798, 9800),
	TRIMMED_FISHING_CAPE(29197, 9799, 9800),
	UNTRIMMED_FLETCHING_CAPE(29198, 9783, 9785),
	TRIMMED_FLETCHING_CAPE(29199, 9784, 9785),
	UNTRIMMED_HERBLORE_CAPE(29200, 9774, 9776),
	TRIMMED_HERBLORE_CAPE(29201, 9775, 9776),
	UNTRIMMED_HITPOINTS_CAPE(29202, 9768, 9770),
	TRIMMED_HITPOINTS_CAPE(29203, 9769, 9770),
	UNTRIMMED_HUNTER_CAPE(29204, 9948, 9950),
	TRIMMED_HUNTER_CAPE(29205, 9949, 9950),
	UNTRIMMED_MAGIC_CAPE(29206, 9762, 9764),
	TRIMMED_MAGIC_CAPE(29207, 9763, 9764),
	UNTRIMMED_MINING_CAPE(29208, 9792, 9794),
	TRIMMED_MINING_CAPE(29209, 9793, 9794),
	UNTRIMMED_PRAYER_CAPE(29210, 9759, 9761),
	TRIMMED_PRAYER_CAPE(29211, 9760, 9761),
	UNTRIMMED_RANGED_CAPE(29212, 9756, 9758),
	TRIMMED_RANGED_CAPE(29213, 9757, 9758),
	UNTRIMMED_RUNECRAFTING_CAPE(29214, 9765, 9767),
	TRIMMED_RUNECRAFTING_CAPE(29215, 9766, 9767),
	UNTRIMMED_SLAYER_CAPE(29216, 9786, 9788),
	TRIMMED_SLAYER_CAPE(29217, 9787, 9788),
	UNTRIMMED_SMITHING_CAPE(29218, 9795, 9797),
	TRIMMED_SMITHING_CAPE(29219, 9796, 9797),
	UNTRIMMED_STRENGTH_CAPE(29220, 9750, 9752),
	TRIMMED_STRENGTH_CAPE(29221, 9751, 9752),
	UNTRIMMED_THIEVING_CAPE(29222, 9777, 9779),
	TRIMMED_THIEVING_CAPE(29223, 9778, 9779),
	UNTRIMMED_WOODCUTTING_CAPE(29224, 9807, 9809),
    TRIMMED_WOODCUTTING_CAPE(29225, 9808, 9809),
    CHAMPIONS_CAPE(30403, 21439, -1),
    MOUNTED_MYTHICAL_CAPE(31983, 22114, -1),

    QUEST_LIST(80, 310, 29226, 20633, new Item(970, 10), new Item(8784));

    public static final Furniture[] VALUES = values();
    public static final Map<Integer, Furniture> MAP = new HashMap<Integer, Furniture>(VALUES.length * 2);
    public static final Map<Integer, Furniture> DISPLAYED_MAP = new HashMap<Integer, Furniture>(VALUES.length);

    static {
        for (final Furniture furniture : VALUES) {
            for (final int id : furniture.getObjectIds()) {
                MAP.put(id, furniture);
			}
			DISPLAYED_MAP.put(furniture.getItemId(), furniture);
		}
	}

    private final int level;
	private final int itemId;
    private final int[] objectIds;
    private final Item[] materials;
    private final double xp;
    private final ConstructionAction action;
    private final int refreshType;
    private final int nails;
	private final int ticks;
    private final boolean wateringCan;
	
	Furniture(final int objectId) {
		this(0, 0, new int[] { objectId }, 0);
	}
	
	Furniture(final int objectId, final int capeId, final int hoodId) {
		this(0, 0, new int[] { objectId }, 0, capeId == -1 ? null : new Item(capeId),
                hoodId == -1 ? null : new Item(hoodId));
	}

	Furniture(final int level, final double xp, final int objectId, final int itemId, final Item... materials) {
		this(level, xp, new int[] { objectId }, itemId, materials);
	}
	
	Furniture(final int level, final double xp, final int objectId, final int itemId, final int refreshType, final Item... materials) {
		this(level, xp, new int[] { objectId }, itemId, null, refreshType, materials);
	}
	
	Furniture(final int level, final int xp, final int objectId, final int itemId, final ConstructionAction action, final Item... materials) {
		this(level, xp, new int[] { objectId }, itemId, action, 0, materials);
	}
	
	Furniture(final int level, final double xp, final int[] objectIds, final int itemId, final Item... materials) {
		this(level, xp, objectIds, itemId, null, 0, materials);
	}
	
	Furniture(final int level, final double xp, final int[] objectIds, final int itemId, final ConstructionAction action, final Item... materials) {
		this(level, xp, objectIds, itemId, action, 0, materials);
	}
	
	Furniture(final int level, final int xp, final int objectId, final int itemId, final ConstructionAction action, final int refreshType, final Item... materials) {
		this(level, xp, new int[] { objectId }, itemId, action, refreshType, materials);
	}
	
	Furniture(final int level, final double xp, final int[] objectIds, final int itemId, final int refreshType, final Item... materials) {
		this(level, xp, objectIds, itemId, null, refreshType, materials);
	}
	
	Furniture(final int level, final double xp, final int[] objectIds, final int itemId, final ConstructionAction action, final int refreshType, final Item... materials) {
		this.level = level;
		this.xp = xp;
		this.objectIds = objectIds;
		this.itemId = itemId;
		this.action = action;
		this.refreshType = refreshType;
		this.materials = materials;
		wateringCan = ArrayUtils.contains(materials, WATERING_CAN);
		nails = getAmountOfNails();
		ticks = getAmountOfTicks();
	}
	
	public int getObjectId() {
		return objectIds[0];
	}
	
	private int getAmountOfNails() {
		for (int i = 0; i < materials.length; i++) {
			final Item mat = materials[i];
			if (mat == null)
				continue;
			if (mat.getId() == 960)
				return mat.getAmount();
		}
		return -1;
	}
	
	private int getAmountOfTicks() {
		int amount = 0;
		for (int i = 0; i < materials.length; i++) {
			final Item mat = materials[i];
			if (mat == null)
				continue;
			amount += mat.getAmount();
			if (mat.getId() >= 8778 && mat.getId() <= 8782)
				return mat.getAmount();
		}
		return Math.max(amount / 3, 1);
	}
	
	@Override
	public String toString() {
		return TextUtils.capitalizeFirstCharacter(ObjectDefinitions.get(objectIds[0]).getName().toLowerCase().replace("_", " "));
	}
	
    public int getLevel() {
        return level;
    }

    public int getItemId() {
        return itemId;
    }

    public int[] getObjectIds() {
        return objectIds;
    }

    public Item[] getMaterials() {
        return materials;
    }

    public double getXp() {
        return xp;
    }

    public ConstructionAction getAction() {
        return action;
    }

    public int getRefreshType() {
        return refreshType;
    }

    public int getNails() {
        return nails;
    }

    public int getTicks() {
        return ticks;
    }

    public boolean isWateringCan() {
        return wateringCan;
    }

	public interface ConstructionAction {

		int REFRESH_ROOM = 1, REFRESH_HOUSE = 2;

		static int getWindowIconOffset(final RoomReference room) {
			for (final FurnitureData data : room.getFurnitureData()) {
				if (data == null)
					continue;
				final Furniture furniture = data.getFurniture();
				if (furniture == Furniture.ICON_OF_BOB)
					return 0;
				else if (furniture == Furniture.GUTHIX_ICON || furniture == Furniture.GUTHIX_SYMBOL)
					return 1;
				else if (furniture == Furniture.ZAMORAK_ICON || furniture == Furniture.ZAMORAK_SYMBOL)
					return 3;
			}
			return 2;
		}

		static int getStatueOffset(final RoomReference room) {
			for (final FurnitureData data : room.getFurnitureData()) {
				if (data == null)
					continue;
				final Furniture furniture = data.getFurniture();
				if (furniture == Furniture.ICON_OF_BOB)
					return 3;
				else if (furniture == Furniture.GUTHIX_ICON || furniture == Furniture.GUTHIX_SYMBOL)
                    return 1;
                else if (furniture == Furniture.ZAMORAK_ICON || furniture == Furniture.ZAMORAK_SYMBOL)
                    return 2;
            }
            return 0;
        }

        void onRefresh(final Construction construction, final RoomReference ref, final FurnitureData data, final WorldObject object, final boolean onEnter);

    }

}