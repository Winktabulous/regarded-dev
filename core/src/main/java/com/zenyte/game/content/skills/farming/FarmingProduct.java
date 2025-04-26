package com.zenyte.game.content.skills.farming;

import com.zenyte.game.content.boons.BoonManager;
import com.zenyte.game.content.boons.impl.Botanist;
import com.zenyte.game.content.boons.impl.IWantItAll;
import com.zenyte.game.content.skills.farming.actions.Harvesting;
import com.zenyte.game.content.skills.woodcutting.TreeDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.wilderness.WildernessResourceArea;
import com.zenyte.utils.Articles;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

import static com.zenyte.game.content.skills.farming.PatchState.*;

/**
 * @author Kris | 02/02/2019 17:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum FarmingProduct {
    POTATO(PatchType.ALLOTMENT, 10, new Item(5318, 3), new Item(1942), 1, 50, 150, 202, 8, 9, 0,
            GrowthStage.normal(6, 9), GrowthStage.normal(70, 73), GrowthStage.toxic(135, 137), GrowthStage.toxic(199, 201), new Stage(10), new Item(6032, 2)),
    ONION(PatchType.ALLOTMENT, 10, new Item(5319, 3), new Item(1957), 5, 50, 150, 202, 9.5, 10.5, 0,
            GrowthStage.normal(13, 16), GrowthStage.normal(77, 80), new GrowthStage(-1, 142, 143, 144), new GrowthStage(-1, 206, 207, 208), new Stage(17), new Item(5438)),
    CABBAGE(PatchType.ALLOTMENT, 10, new Item(5324, 3), new Item(1965), 7, 50, 150, 202, 10, 11.5, 0,
            GrowthStage.normal(20, 23), GrowthStage.normal(84, 87), new GrowthStage(-1, 149, 150, 151), new GrowthStage(-1, 213, 214, 215), new Stage(24), new Item(5458)),
    TOMATO(PatchType.ALLOTMENT, 10, new Item(5322, 3), new Item(1982), 12, 50, 150, 202, 12.5, 14, 0,
            GrowthStage.normal(27, 30), GrowthStage.normal(91, 94), new GrowthStage(-1, 156, 157, 158), new GrowthStage(-1, 220, 221, 222), new Stage(31), new Item(5478, 2)),
    SWEETCORN(PatchType.ALLOTMENT, 10, new Item(5320, 3), new Item(5986), 20, 45, 150, 202, 17, 19, 0,
            GrowthStage.normal(34, 39), GrowthStage.normal(98, 103), new GrowthStage(-1, 163, 164, 165, 166, 167), new GrowthStage(-1, 227, 228, 229, 230, 231), new Stage(40), new Item(5931, 10)),
    STRAWBERRY(PatchType.ALLOTMENT, 10, new Item(5323, 3), new Item(5504), 31, 45, 150, 202, 26, 29, 0,
            GrowthStage.normal(43, 48), GrowthStage.normal(107, 112), new GrowthStage(-1, 172, 173, 174, 175, 176), new GrowthStage(-1, 236, 237, 238, 239, 240), new Stage(49), new Item(5386)),
    WATERMELON(PatchType.ALLOTMENT, 10, new Item(5321, 3), new Item(5982), 47, 40, 150, 202, 48.5, 54.4, 0,
            GrowthStage.normal(52, 59), GrowthStage.normal(116, 123), new GrowthStage(-1, 181, 182, 183, 184, 185, 186, 187), new GrowthStage(-1, 245, 246, 247, 248, 249, 250, 251), new Stage(60), new Item(5970, 10)),
    SNAPE_GRASS(PatchType.ALLOTMENT, 10, new Item(22879, 3), new Item(231), 61, 40, 150, 202, 82, 90.2, 0,
            GrowthStage.normal(128, 134), GrowthStage.normal(63, 69), new GrowthStage(-1, 196, 197, 198, 202, 203, 204), new GrowthStage(-1, 193, 194, 195, 209, 210, 211), new Stage(138), new Item(247, 5)),
    MARIGOLD(PatchType.FLOWER_PATCH, 5, new Item(5096), new Item(6010), 2, 30, 20, 70, 8.5, 47, 0,
            new GrowthStage(8, 9, 10, 11), new GrowthStage(72, 73, 74, 75), new GrowthStage(-1, 137, 138, 139), new GrowthStage(-1, 201, 202, 203), new Stage(12)),
    ROSEMARY(PatchType.FLOWER_PATCH, 5, new Item(5097), new Item(6014), 11, 30, 20, 70, 12, 66.5, 0,
            new GrowthStage(13, 14, 15, 16), new GrowthStage(77, 78, 79, 80), new GrowthStage(-1, 142, 143, 144), new GrowthStage(-1, 206, 207, 208), new Stage(17)),
    NASTURTIUM(PatchType.FLOWER_PATCH, 5, new Item(5098), new Item(6012), 24, 30, 20, 70, 19.5, 111, 0,
            new GrowthStage(18, 19, 20, 21), new GrowthStage(82, 83, 84, 85), new GrowthStage(-1, 147, 148, 149), new GrowthStage(-1, 211, 212, 213), new Stage(22)),
    WOAD(PatchType.FLOWER_PATCH, 5, new Item(5099), new Item(1793), 25, 30, 20, 70, 20.5, 115.5, 0,
            new GrowthStage(23, 24, 25, 26), new GrowthStage(87, 88, 89, 90), new GrowthStage(-1, 152, 153, 154), new GrowthStage(-1, 216, 217, 218), new Stage(27)),
    LIMPWURT(PatchType.FLOWER_PATCH, 5, new Item(5100), new Item(225), 26, 30, 20, 70, 21.5, 18.5, 0,
            new GrowthStage(28, 29, 30, 31), new GrowthStage(92, 93, 94, 95), new GrowthStage(-1, 157, 158, 159), new GrowthStage(-1, 221, 222, 223), new Stage(32)),
    WHITE_LILY(PatchType.FLOWER_PATCH, 5, new Item(22887), new Item(22932), 58, 30, 20, 70, 42, 250, 0,
            new GrowthStage(37, 38, 39, 40), new GrowthStage(101, 102, 103, 104), new GrowthStage(-1, 166, 167, 168), new GrowthStage(-1, 230, 231, 232), new Stage(41)),
    SCARECROW(PatchType.FLOWER_PATCH, 5, null, new Item(6059), 0, 0, 0, 0, 0, 0, 0,
            new GrowthStage(36, 35, 34, 33), null, null, null, null),
    GUAM(PatchType.HERB_PATCH, 20, new Item(5291), new Item(199), 9, 26, 25 + 40, 120, 11, 12.5, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    MARRENTILL(PatchType.HERB_PATCH, 20, new Item(5292), new Item(201), 14, 26, 28 + 40, 120, 13.5, 15, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    TARROMIN(PatchType.HERB_PATCH, 20, new Item(5293), new Item(203), 19, 26, 31 + 40, 120, 16, 18, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    HARRALANDER(PatchType.HERB_PATCH, 20, new Item(5294), new Item(205), 26, 26, 36 + 40, 120, 21.5, 24, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    GOUTWEED(PatchType.HERB_PATCH, 20, new Item(6311), new Item(3261), 29, 26, 39 + 40, 120, 105, 45, 0,
            new GrowthStage(192, 193, 194, 195), null, new GrowthStage(-1, 198, 199, 200), new GrowthStage(-1, 201, 202, 203), new Stage(196)),
    RANARR(PatchType.HERB_PATCH, 20, new Item(5295), new Item(207), 32, 26, 39 + 40, 120, 26.5, 30.5, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    TOADFLAX(PatchType.HERB_PATCH, 20, new Item(5296), new Item(3049), 38, 26, 43 + 40, 120, 34, 38.5, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    IRIT(PatchType.HERB_PATCH, 20, new Item(5297), new Item(209), 44, 26, 46 + 40, 120, 43, 48.5, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    AVANTOE(PatchType.HERB_PATCH, 20, new Item(5298), new Item(211), 50, 26, 50 + 40, 120, 54.5, 61.5, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    KWUARM(PatchType.HERB_PATCH, 20, new Item(5299), new Item(213), 56, 26, 54 + 40, 120, 69, 78, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    SNAPDRAGON(PatchType.HERB_PATCH, 20, new Item(5300), new Item(3051), 62, 26, 57 + 40, 120, 87.5, 98.5, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    CADANTINE(PatchType.HERB_PATCH, 20, new Item(5301), new Item(215), 67, 26, 60 + 40, 120, 106.5, 120, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    LANTADYME(PatchType.HERB_PATCH, 20, new Item(5302), new Item(2485), 73, 26, 64 + 40, 120, 134.5, 151.5, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    DWARF_WEED(PatchType.HERB_PATCH, 20, new Item(5303), new Item(217), 79, 26, 67 + 40, 120, 170.5, 192, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    TORSTOL(PatchType.HERB_PATCH, 20, new Item(5304), new Item(219), 85, 26, 71 + 40, 120, 199.5, 224.5, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 128, 129, 130), new GrowthStage(-1, 170, 171, 172), new Stage(8)),
    HAMMERSTONE(PatchType.HOPS_PATCH, 10, new Item(5307, 4), new Item(5994), 4, 40, 120, 192, 9, 10, 0,
            new GrowthStage(4, 5, 6, 7), new GrowthStage(68, 69, 70, 71), new GrowthStage(-1, 133, 134, 135), new GrowthStage(-1, 197, 198, 199), new Stage(8), new Item(6010)),
    ASGARNIAN(PatchType.HOPS_PATCH, 10, new Item(5308, 4), new Item(5996), 8, 40, 120, 192, 10.9, 12, 0,
            new GrowthStage(11, 12, 13, 14, 15), new GrowthStage(75, 76, 77, 78, 79), new GrowthStage(-1, 140, 141, 142, 143), new GrowthStage(-1, 204, 205, 206, 207), new Stage(16), new Item(5458)),
    YANILLIAN(PatchType.HOPS_PATCH, 10, new Item(5309, 4), new Item(5998), 16, 40, 120, 192, 14.5, 16, 0,
            new GrowthStage(19, 20, 21, 22, 23, 24), new GrowthStage(83, 84, 85, 86, 87, 88), new GrowthStage(-1, 148, 149, 150, 151, 152), new GrowthStage(-1, 212, 213, 214, 215, 216), new Stage(25), new Item(5968)),
    KRANDORIAN(PatchType.HOPS_PATCH, 10, new Item(5310, 4), new Item(6000), 21, 40, 120, 192, 17.5, 19.5, 0,
            new GrowthStage(28, 29, 30, 31, 32, 33, 34), new GrowthStage(92, 93, 94, 95, 96, 97, 98), new GrowthStage(-1, 157, 158, 159, 160, 161, 162), new GrowthStage(-1, 221, 222, 223, 224, 225, 226), new Stage(35), new Item(5478, 3)),
    WILDBLOOD(PatchType.HOPS_PATCH, 10, new Item(5311, 4), new Item(6002), 28, 40, 120, 192, 23, 26, 0,
            new GrowthStage(38, 39, 40, 41, 42, 43, 44, 45), new GrowthStage(102, 103, 104, 105, 106, 107, 108, 109), new GrowthStage(-1, 167, 168, 169, 170, 171, 172, 173), new GrowthStage(-1, 231, 232, 233, 234, 235, 236, 237),
            new Stage(46), new Item(6012)),
    BARLEY(PatchType.HOPS_PATCH, 10, new Item(5305, 4), new Item(6006), 3, 40, 120, 192, 8.5, 9.5, 0,
            new GrowthStage(49, 50, 51, 52), new GrowthStage(113, 114, 115, 116), new GrowthStage(-1, 178, 179, 180), new GrowthStage(-1, 242, 243, 244), new Stage(53), new Item(6032, 3)),
    JUTE(PatchType.HOPS_PATCH, 10, new Item(5306, 4), new Item(5931), 13, 40, 120, 192, 13, 14.5, 0,
            new GrowthStage(56, 57, 58, 59, 60), new GrowthStage(120, 121, 122, 123, 124), new GrowthStage(-1, 185, 186, 187, 188), new GrowthStage(-1, 249, 250, 251, 252),
            new Stage(61), new Item(6008, 6)),
    REDBERRY(PatchType.BUSH_PATCH, 20, new Item(5101), new Item(1951), 10, 17, 30, 150, 11.5, 4.5, 64,
            new GrowthStage(5, 6, 7, 8, 9), null, new GrowthStage(-1, 71, 72, 73, 74), new GrowthStage(-1, 135, 136, 137, 138),
            new Stage(11, 10, 250, true), new Item(5478, 4)),
    CADAVABERRY(PatchType.BUSH_PATCH, 20, new Item(5102), new Item(753), 22, 17, 42, 150, 18, 7, 102.5,
            new GrowthStage(15, 16, 17, 18, 19, 20), null, new GrowthStage(-1, 80, 81, 82, 83, 84), new GrowthStage(-1, 145, 146, 147, 148, 149),
            new Stage(22, 21, 251, true), new Item(5968, 3)),
    DWELLBERRY(PatchType.BUSH_PATCH, 20, new Item(5103), new Item(2126), 36, 17, 56, 150, 31.5, 12, 177.5,
            new GrowthStage(26, 27, 28, 29, 30, 31, 32), null, new GrowthStage(-1, 91, 92, 93, 94, 95, 96), new GrowthStage(-1, 155, 156, 157, 158, 159, 161),
            new Stage(34, 33, 252, true), new Item(5406, 3)),
    JANGERBERRY(PatchType.BUSH_PATCH, 20, new Item(5104), new Item(247), 48, 17, 68, 150, 50.5, 19, 184.5,
            new GrowthStage(38, 39, 40, 41, 42, 43, 44, 45), null, new GrowthStage(-1, 103, 104, 105, 106, 107, 108, 109), new GrowthStage(-1, 167, 168, 169, 171, 172, 173, 174),
            new Stage(47, 46, 253, true), new Item(5982, 6)),
    WHITEBERRY(PatchType.BUSH_PATCH, 20, new Item(5105), new Item(239), 59, 17, 79, 150, 78, 29, 437.5,
            new GrowthStage(51, 52, 53, 54, 55, 56, 57, 58), null, new GrowthStage(-1, 116, 117, 118, 119, 120, 121, 122), new GrowthStage(-1, 181, 182, 183, 184, 185, 186, 187),
            new Stage(60, 59, 254, true), new Item(6004, 8)),
    POISON_IVY(PatchType.BUSH_PATCH, 20, new Item(5106), new Item(6018), 70, 0, 90, 150, 120, 45, 675,
            new GrowthStage(197, 198, 199, 200, 201, 202, 203, 204), null, null, null, new Stage(206, 205, 255, true)),
    TEAK(PatchType.HARDWOOD_TREE_PATCH, 640, new Item(21477), null, 35, 10, 15, 0, 35, 0, 7290,
            new GrowthStage(8, 9, 10, 11, 12, 13, 14), null, new GrowthStage(-1, 18, 19, 20, 21, 22, 23), new GrowthStage(-1, 24, 25, 26, 27, 28, 29),
            new Stage(16, 17, 15, true), new Item(ItemId.LIMPWURT_ROOT, 15)),
    MAHOGANY(PatchType.HARDWOOD_TREE_PATCH, 640, new Item(21480), null, 55, 10, 15, 0, 63, 0, 15720,
            new GrowthStage(30, 31, 32, 33, 34, 35, 36, 37), null, new GrowthStage(-1, 41, 42, 43, 44, 45, 46, 47), new GrowthStage(-1, 48, 49, 50, 51, 52, 53, 54),
            new Stage(39, 40, 38, true), new Item(ItemId.YANILLIAN_HOPS, 25)),
    OAK(PatchType.TREE_PATCH, 40, new Item(5370), new Item(6043), 15, 16, 15, 0, 14, 0, 467.3,
            new GrowthStage(8, 9, 10, 11), null, new GrowthStage(-1, 73, 74, 75), new GrowthStage(-1, 137, 138, 139), new Stage(13, 14, 12, true), new Item(5968)),
    WILLOW(PatchType.TREE_PATCH, 40, new Item(5371), new Item(6045), 30, 14, 15, 0, 25, 0, 1456.5,
            new GrowthStage(15, 16, 17, 18, 19, 20), null, new GrowthStage(-1, 80, 81, 82, 83, 84), new GrowthStage(-1, 144, 145, 146, 147, 148), new Stage(22, 23, 21, true), new Item(5386)),
    MAPLE(PatchType.TREE_PATCH, 40, new Item(5372), new Item(6047), 45, 12, 15, 0, 45, 0, 3403.4,
            new GrowthStage(24, 25, 26, 27, 28, 29, 30, 31), null, new GrowthStage(-1, 89, 90, 91, 92, 93, 94, 95), new GrowthStage(-1, 153, 154, 155, 156, 157, 158, 159),
            new Stage(33, 34, 32, true), new Item(5396)),
    YEW(PatchType.TREE_PATCH, 40, new Item(5373), new Item(6049), 60, 10, 15, 0, 81, 0, 7069.9,
            new GrowthStage(35, 36, 37, 38, 39, 40, 41, 42, 43, 44), null, new GrowthStage(-1, 100, 101, 102, 103, 104, 105, 106, 107, 108), new GrowthStage(-1, 164, 165, 166, 167, 168, 169, 170, 171, 172),
            new Stage(46, 47, 45, true), new Item(6016, 10)),
    MAGIC(PatchType.TREE_PATCH, 40, new Item(5374), new Item(6051), 75, 8, 15, 0, 145.5, 0, 13768.3,
            new GrowthStage(48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59), null, new GrowthStage(-1, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123), new GrowthStage(-1, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187),
            new Stage(61, 62, 60, true), new Item(5974, 25)),
    //CRYSTAL(PatchType.CRYSTAL_TREE_PATCH, 40, new Item(5374), new Item(6051), 74, 0, 15, 0, 126, 0, 13240,
    //        new GrowthStage(48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59), null, new GrowthStage(-1, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123), new GrowthStage(-1, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187),
    //        new Stage(61, 62, 60, true)),
    APPLE(PatchType.FRUIT_TREE_PATCH, 160, new Item(5496), new Item(1955), 27, 17, 15, 0, 22, 8.5, 1199.5,
            new GrowthStage(8, 9, 10, 11, 12, 13), null, new GrowthStage(-1, 21, 22, 23, 24, 25, 26), new GrowthStage(-1, 27, 28, 29, 30, 31, 32),
            new Stage(20, 33, 34, true, 14, 15, 16, 17, 18, 19), new Item(5986, 9)),
    BANANA(PatchType.FRUIT_TREE_PATCH, 160, new Item(5497), new Item(1963), 33, 17, 15, 0, 28, 10.5, 1750.5,
            new GrowthStage(35, 36, 37, 38, 39, 40), null, new GrowthStage(-1, 48, 49, 50, 51, 52, 53), new GrowthStage(-1, 54, 55, 56, 57, 58, 59),
            new Stage(47, 60, 61, true, 41, 42, 43, 44, 45, 46), new Item(5386, 4)),
    ORANGE(PatchType.FRUIT_TREE_PATCH, 160, new Item(5498), new Item(2108), 39, 17, 15, 0, 35.5, 13.5, 2470.2,
            new GrowthStage(72, 73, 74, 75, 76, 77), null, new GrowthStage(-1, 85, 86, 87, 88, 89, 90), new GrowthStage(-1, 91, 92, 93, 94, 95, 96),
            new Stage(84, 97, 98, true, 78, 79, 80, 81, 82, 83), new Item(5406, 3)),
    CURRY(PatchType.FRUIT_TREE_PATCH, 160, new Item(5499), new Item(5970), 42, 17, 15, 0, 40, 15, 2906.9,
            new GrowthStage(99, 100, 101, 102, 103, 104), null, new GrowthStage(-1, 112, 113, 114, 115, 116, 117), new GrowthStage(-1, 118, 119, 120, 121, 122, 123),
            new Stage(111, 124, 125, true, 105, 106, 107, 108, 109, 110), new Item(5416, 5)),
    PINEAPPLE(PatchType.FRUIT_TREE_PATCH, 160, new Item(5500), new Item(2114), 51, 17, 15, 0, 57, 21.5, 4605.7,
            new GrowthStage(136, 137, 138, 139, 140, 141), null, new GrowthStage(-1, 149, 150, 151, 152, 153, 154), new GrowthStage(-1, 155, 156, 157, 158, 159, 160),
            new Stage(148, 161, 162, true, 142, 143, 144, 145, 146, 147), new Item(5982, 10)),
    PAPAYA(PatchType.FRUIT_TREE_PATCH, 160, new Item(5501), new Item(5972), 57, 17, 15, 0, 72, 27, 6146.4,
            new GrowthStage(163, 164, 165, 166, 167, 168), null, new GrowthStage(-1, 176, 177, 178, 179, 180, 181), new GrowthStage(-1, 182, 183, 184, 185, 186, 187),
            new Stage(175, 188, 189, true, 169, 170, 171, 172, 173, 174), new Item(2114, 10)),
    PALM(PatchType.FRUIT_TREE_PATCH, 160, new Item(5502), new Item(5974), 68, 17, 15, 0, 110.5, 41.5, 10150.1,
            new GrowthStage(200, 201, 202, 203, 204, 205), null, new GrowthStage(-1, 213, 214, 215, 216, 217, 218), new GrowthStage(-1, 219, 220, 221, 222, 223, 224),
            new Stage(212, 225, 226, true, 206, 207, 208, 209, 210, 211), new Item(5972, 15)),
    DRAGONFRUIT(PatchType.FRUIT_TREE_PATCH, 160, new Item(22866), new Item(22929), 81, 17, 15, 0, 140, 70, 17335,
            new GrowthStage(227, 228, 229, 230, 231, 232), null, new GrowthStage(-1, 240, 241, 242, 243, 244, 245), new GrowthStage(-1, 246, 247, 248, 249, 250, 251),
            new Stage(239, 252, 253, true, 233, 234, 235, 236, 237, 238), new Item(5974, 15)),
    WEEDS(null, 5, null, new Item(6055), 0, 0, 15, 0, 0, 0, 0,
            new GrowthStage(3, 2, 1, 0), null, null, null, null),
    GRAPE(PatchType.GRAPEVINE_PATCH, 5, new Item(13657), new Item(1987), 36, 0, 40, 150, 31.5, 40, 625,
            new GrowthStage(2, 3, 4, 5, 6, 7, 8), null, null, null, new Stage(10, -1, 9, false, 15, 14, 13, 12, 11)) {},
    MUSHROOM(PatchType.MUSHROOM_PATCH, 40, new Item(5282), new Item(6004), 53, 25, 50, 150, 61.5, 57.7, 0,
            new GrowthStage(4, 5, 6, 7, 8, 9), null, new GrowthStage(-1, 16, 17, 18, 19, 20), new GrowthStage(-1, 21, 22, 23, 24, 25), new Stage(10, -1, -1, false, 0, 15, 14, 13, 12, 11)) {},
    BELLADONNA(PatchType.BELLADONNA_PATCH, 80, new Item(5281), new Item(2398), 63, 25, 60, 80, 91, 512, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 9, 10, 11), new GrowthStage(-1, 12, 13, 14), new Stage(8)),
    CACTUS(PatchType.CACTUS_PATCH, 80, new Item(5280), new Item(6016), 55, 10, 80, 120, 66.5, 25, 374,
            new GrowthStage(8, 9, 10, 11, 12, 13, 14), null, new GrowthStage(-1, 19, 20, 21, 22, 23, 24), new GrowthStage(-1, 25, 26, 27, 28, 29, 30),
            new Stage(18, -1, 31, true, 15, 16, 17), new Item(ItemId.CADAVA_BERRIES, 6)),
    POTATO_CACTUS(PatchType.CACTUS_PATCH, 10, new Item(22873), new Item(3138), 64, 10, 80, 150, 68, 68, 230,
            new GrowthStage(32, 33, 34, 35, 36, 37, 38), null, new GrowthStage(-1, 46, 47, 48, 49, 50, 51), new GrowthStage(-1, 52, 53, 54, 55, 56, 57),
            new Stage(45, -1, 58, true, 39, 40, 41, 42, 43, 44), new Item(ItemId.SNAPE_GRASS, 8)),
    GIANT_SEAWEED(PatchType.GIANT_SEAWEED_PATCH, 10, new Item(21490), new Item(21504), 23, 10, 140, 190, 19, 21, 0,
            new GrowthStage(4, 5, 6, 7), null, new GrowthStage(-1, 11, 12, 13), new GrowthStage(-1, 14, 15, 16), new Stage(8), new Item(ItemId.NUMULITE, 200)),
    CALQUAT(PatchType.CALQUAT_PATCH, 160, new Item(5503), new Item(5980), 72, 17, 15, 0, 129.5, 48.5, 12096,
            new GrowthStage(4, 5, 6, 7, 8, 9, 10, 11), null, new GrowthStage(-1, 19, 20, 21, 22, 23, 24, 25), new GrowthStage(-1, 26, 27, 28, 29, 30, 31, 32),
            new Stage(18, 33, 34, true, 12, 13, 14, 15, 16, 17), new Item(ItemId.POISON_IVY_BERRIES, 8)),
    REDWOOD(PatchType.REDWOOD_PATCH, 640, new Item(22859), null, 90, 10, 15, 0, 230, 0, 22450,
            new GrowthStage(8, 9, 10, 11, 12, 13, 14, 15, 16, 17), null, new GrowthStage(-1, 19, 20, 21, 22, 23, 24, 25, 26, 27), new GrowthStage(-1, 28, 29, 30, 31, 32, 33, 34, 35, 36),
            new Stage(18, -1, 37, true), new Item(22929, 6)),
    SPIRIT_TREE(PatchType.SPIRIT_TREE_PATCH, 320, new Item(5375), null, 83, 15, 15, 0, 199.5, 0, 19301,
            new GrowthStage(8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19), null, new GrowthStage(-1, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31), new GrowthStage(-1, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43),
            new Stage(20, -1, 44, true), new Item(4014), new Item(9082), new Item(4012, 5)),
    CELASTRUS(PatchType.CELASTRUS_PATCH, 160, new Item(22856), new Item(22935), 85, 15, 90, 160, 204, 23.5, 14130,
            new GrowthStage(8, 9, 10, 11, 12), null, new GrowthStage(-1, 19, 20, 21, 22), new GrowthStage(-1, 24, 25, 26, 27),
            new Stage(16, 28, 13, false, 17, 14, 15), new Item(3138, 8)),
    ATTAS_PLANT(PatchType.ANIMA_PATCH, 640, new Item(22881), null, 76, 0, 15, 0, 100, 0, 0,
            new GrowthStage(8, 9, 10, 11, 12, 13, 14, 15), null, null, null, new Stage(16)),
    IASOR_PLANT(PatchType.ANIMA_PATCH, 640, new Item(22883), null, 76, 0, 15, 0, 100, 0, 0,
            new GrowthStage(17, 18, 19, 20, 21, 22, 23, 24), null, null, null, new Stage(25)),
    KRONOS_PLANT(PatchType.ANIMA_PATCH, 640, new Item(22885), null, 76, 0, 15, 0, 100, 0, 0,
            new GrowthStage(26, 27, 28, 29, 30, 31, 32, 33), null, null, null, new Stage(34)),
    HESPORI(PatchType.HESPORI_PATCH, 640, new Item(22875), null, 65, 0, 15, 0, 62, 12600, 0,
            new GrowthStage(4, 5, 6), null, null, null, new Stage(8, 8, 7, false)),
    COMPOST(PatchType.COMPOST_BIN, 35, null, new Item(6032), 0, 0, 15, 0, 0, 0, 0,
            new GrowthStage(31), null, null, null, new Stage(94)),
    SUPERCOMPOST(PatchType.COMPOST_BIN, 35, null, new Item(6034), 0, 0, 15, 0, 0, 0, 0,
            new GrowthStage(95), null, null, null, new Stage(126)),
    ULTRACOMPOST(PatchType.COMPOST_BIN, 35, null, new Item(21483), 0, 0, 15, 0, 0, 0, 0, null, null, null, null, null),
    TOMATOES(PatchType.COMPOST_BIN, 35, null, new Item(2518), 0, 0, 15, 0, 0, 0, 0,
            new GrowthStage(159), null, null, null, new Stage(222));
    private final GrowthStage regularStage;
    private final GrowthStage wateredStage;
    private final GrowthStage diseasedStage;
    private final GrowthStage deadStage;
    private final PatchType type;
    private static final IntArrayList seeds;
    private final int lowEndHarvestChance;
    private final int highEndHarvestChance;
    private final int levelRequired;
    private final int cycleTime;
    private final Stage miscStage;
    private final double plantExperience;
    private final double harvestExperience;
    private final double checkHealthXP;
    private final Item seed;
    private Item product;
    private final Item[] payment;
    public static final Int2ObjectOpenHashMap<FarmingProduct> products = new Int2ObjectOpenHashMap<>();
    public static final FarmingProduct[] values = values();

    public String getSeedName() {
        assert seed != null;
        final String name = seed.getName().toLowerCase();
        if (seed.getAmount() > 1) {
            return seed.getAmount() + " " + name + "s";
        }
        return Articles.prepend(name);
    }

    public String getProductName() {
        assert product != null;
        final String name = product.getName().toLowerCase();
        final boolean plural = product.getAmount() > 1;
        if (plural) {
            return "some " + name + "s";
        }
        return Articles.prepend(name);
    }

    public String getPatchName() {
        return Articles.prepend(type.getSanitizedName());
    }

    public String getSeedsPlural() {
        assert seed != null;
        final String name = seed.getName().toLowerCase();
        return name.endsWith("s") ? name : (name + "s");
    }

    static {
        for (FarmingProduct product : values) {
            if (product.seed != null) {
                products.put(product.seed.getId(), product);
            }
        }
    }

    FarmingProduct(final PatchType type, final int cycleTime, final Item seed, final Item product, final int levelRequired, final int diseaseChance, final int lowEndHarvestChance, final int highEndHarvestChance, final double plantExperience, final double harvestExperience, final double checkHealthXP, final GrowthStage regular, final GrowthStage watered, final GrowthStage diseased, final GrowthStage dead, final Stage stage, final Item... payment) {
        this.type = type;
        this.cycleTime = type == PatchType.HERB_PATCH ? (int) (TimeUnit.MINUTES.toMillis(cycleTime) * 0.08D) : (int) (TimeUnit.MINUTES.toMillis(cycleTime) * FarmingConstants.FARMING_TIMER_MULTIPLIER);
        this.seed = seed;
        this.product = product;
        this.payment = payment;
        this.lowEndHarvestChance = lowEndHarvestChance;
        this.highEndHarvestChance = highEndHarvestChance;
        this.levelRequired = levelRequired;
        this.diseaseChance = diseaseChance;
        this.plantExperience = plantExperience;
        this.harvestExperience = harvestExperience;
        this.checkHealthXP = checkHealthXP;
        this.regularStage = regular;
        this.wateredStage = watered;
        this.diseasedStage = diseased;
        this.deadStage = dead;
        this.miscStage = stage;
        /*if (regularStage != null) {
            assert wateredStage == null || regularStage.values.length == wateredStage.values.length : "Exception in " +
                    "watered stages for product " + name();
            assert diseasedStage == null || regularStage.values.length == diseasedStage.values.length : "Exception in " +
                    "diseased stages for product " + name();
            assert deadStage == null || regularStage.values.length == deadStage.values.length : "Exception in " +
                    "dead stages for product " + name();
        }*/
    }

    public PatchState getState(final int value) {
        assert !(value < 0 || value > 255) : "Invalid varbit value: " + value;
        if (this == WEEDS || this == SCARECROW) {
            return PatchState.WEEDS;
        }
        if (this.miscStage.harvestStage == value) {
            return PatchState.GROWN;
        }
        if (this.miscStage.clearStage == value) {
            return STUMP;
        }
        if (this.miscStage.checkHealthStage == value) {
            return PatchState.HEALTH_CHECK;
        }
        if (ArrayUtils.contains(this.regularStage.values, value)) {
            return PatchState.GROWING;
        }
        if (this.wateredStage != null) {
            if (ArrayUtils.contains(this.wateredStage.values, value)) {
                return WATERED;
            }
        }
        if (this.diseasedStage != null) {
            if (ArrayUtils.contains(this.diseasedStage.values, value)) {
                return PatchState.DISEASED;
            }
        }
        if (this.deadStage != null) {
            if (ArrayUtils.contains(this.deadStage.values, value)) {
                return PatchState.DEAD;
            }
        }
        if (ArrayUtils.contains(this.miscStage.productStages, value)) {
            return PatchState.REGAINING_PRODUCE;
        }
        if (this == REDWOOD && value >= 41) {
            return GROWN;
        }
        if (this == COMPOST || this == SUPERCOMPOST || this == ULTRACOMPOST || this == TOMATOES) {
            return GROWING;
        }
        throw new IllegalStateException("Unknown state: " + value + ", " + this.name());
    }

    public int transform(final PatchState current, final PatchState target, final int currentValue) {
        if (current == PatchState.WEEDS && target == GROWING) {
            assert this.regularStage.amount() > 0;
            return this.regularStage.first();
        }
        final int[] targetArray = array(target);
        final int[] array = array(current);
        //Unable to transform from watered/diseased/dead to non-array state.
        assert (targetArray == null) == (array == null);
        if (array != null) {
            final int offset = /* || target == PatchState.WATERED*/ target != PatchState.GROWING || current == DISEASED ? 0 : 1;
            final int index = ArrayUtils.indexOf(array, currentValue) + offset;
            if (index >= targetArray.length) {
                assert current == target || current == WATERED && target == GROWING;
                if (target == PatchState.GROWING) {
                    return this.miscStage.checkHealthStage == -1 ? this.miscStage.harvestStage : this.miscStage.checkHealthStage;
                }
                assert false;
            }
            return targetArray[index];
        }
        if (target == REGAINING_PRODUCE) {
            final int[] stages = this.miscStage.productStages;
            assert stages.length > 0;
            if (currentValue == stages[stages.length - 1]) {
                return this.miscStage.harvestStage;
            } else {
                final int index = ArrayUtils.indexOf(stages, currentValue);
                assert index < stages.length - 1;
                return stages[index + 1];
            }
        }
        if (target == STUMP) {
            return this.miscStage.clearStage;
        } else if (target == PatchState.GROWN) {
            //If current is a fruit tree stump, we transform it to regaining state, starting at zero fruit.
            if (current == STUMP && (this.type == PatchType.FRUIT_TREE_PATCH || this.type == PatchType.CELASTRUS_PATCH)) {
                assert this.miscStage.productStages.length != 0;
                return this.miscStage.productStages[0];
            }
            return this.miscStage.harvestStage;
        } else if (target == PatchState.WEEDS) {
            return Math.max(this == SCARECROW ? 33 : 0, currentValue - 1);
        }
        return -1;
    }

    private int[] array(final PatchState state) {
        if (state == PatchState.DISEASED) {
            return this.diseasedStage.values;
        } else if (state == PatchState.DEAD) {
            return this.deadStage.values;
        } else if (state == PatchState.GROWING) {
            return this.regularStage.values;
        } else if (state == WATERED) {
            return this.wateredStage.values;
        }
        return null;
    }

    private final int diseaseChance;

    static {
        seeds = new IntArrayList();
        for (final FarmingProduct product : values) {
            if (product.getSeed() == null) {
                continue;
            }
            if (product.isTree()) {
                final Seedling seedling = Seedling.getSapling(product.getSeed().getId());
                if (seedling != null) {
                    seeds.add(seedling.getSeed());
                }
                continue;
            }
            seeds.add(product.getSeed().getId());
        }
    }

    public boolean isTree() {
        return type != null && type.isTree();
    }

    @NotNull
    public TreeDefinitions getTreeDefinitions() {
        switch (this) {
        case OAK:
            return TreeDefinitions.OAK;
        case WILLOW:
            return TreeDefinitions.WILLOW_TREE;
        case MAPLE:
            return TreeDefinitions.MAPLE_TREE;
        case YEW:
            return TreeDefinitions.YEW_TREE;
        case MAGIC:
            return TreeDefinitions.MAGIC_TREE;
        case TEAK:
            return TreeDefinitions.TEAK_TREE;
        case MAHOGANY:
            return TreeDefinitions.MAHOGANY_TREE;
        case REDWOOD:
            return TreeDefinitions.REDWOOD_TREE;
        }
        if (isTree()) {
            return TreeDefinitions.FRUIT_TREE;
        }
        throw new IllegalStateException();
    }

    @NotNull
    public Item getLeaves() {
        switch (this) {
        case OAK:
            return new Item(6022);
        case WILLOW:
            return new Item(6024);
        case MAPLE:
            return new Item(6028);
        case YEW:
            return new Item(6026);
        case MAGIC:
            return new Item(6030);
        }
        if (isTree() || type == PatchType.BUSH_PATCH) {
            return new Item(6020);
        }
        throw new IllegalStateException();
    }

    public static final FarmingProduct[] supercompostableProducts = new FarmingProduct[] {PINEAPPLE, WATERMELON, PALM, PAPAYA, MUSHROOM, POISON_IVY, JANGERBERRY, WHITEBERRY, TOADFLAX, AVANTOE, KWUARM, SNAPDRAGON, CADANTINE, LANTADYME, DWARF_WEED, TORSTOL, OAK, WILLOW, MAPLE, YEW, MAGIC, CALQUAT};

    public static IntArrayList getSeeds() {
        return seeds;
    }

    public GrowthStage getRegularStage() {
        return regularStage;
    }

    public GrowthStage getWateredStage() {
        return wateredStage;
    }

    public GrowthStage getDiseasedStage() {
        return diseasedStage;
    }

    public GrowthStage getDeadStage() {
        return deadStage;
    }

    public PatchType getType() {
        return type;
    }

    public int getLowEndHarvestChance() {
        return lowEndHarvestChance;
    }

    public int getHighEndHarvestChance() {
        return highEndHarvestChance;
    }

    public int getDiseaseChance() {
        return diseaseChance;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public int getCycleTime() {
        return cycleTime;
    }

    public Stage getMiscStage() {
        return miscStage;
    }

    public double getPlantExperience() {
        return plantExperience;
    }

    public double getHarvestExperience() {
        return harvestExperience;
    }

    public double getCheckHealthXP() {
        return checkHealthXP;
    }

    public Item getSeed() {
        return seed;
    }

    public Item getProduct() {
        return product;
    }

    public Item getProductHarvest(Player p) {
        final BoonManager boons = p.getBoonManager();
        final boolean wantItAll = type != PatchType.HERB_PATCH && boons.hasBoon(IWantItAll.class) && IWantItAll.roll();
        final boolean botanist = type == PatchType.HERB_PATCH && boons.hasBoon(Botanist.class);
        double modifier = wantItAll || botanist ? IWantItAll.GATHER_QUANTITY_MULTIPLIER :  1.0;
        if (p.inArea(WildernessResourceArea.class))
            modifier += WildernessResourceArea.GATHER_QUANTITY_MULTIPLIER;
        if (modifier != 1.0) {
            final Item copy = product.copy();
            copy.setAmount(product.getAmount() * 2);
            if (botanist)
                return copy.toNote();
            else
                return copy;
        } else
            return product;
    }

    public Item[] getPayment() {
        return payment;
    }


    public static final class GrowthStage {
        @NotNull
        private final int[] values;

        static final GrowthStage normal(final int start, final int end) {
            return new GrowthStage(IntStream.rangeClosed(start, end).toArray());
        }

        static final GrowthStage toxic(final int start, final int end) {
            final IntArrayList list = new IntArrayList(end - start + 1);
            list.add(-1);
            for (final int i : IntStream.rangeClosed(start, end).toArray()) {
                list.add(i);
            }
            return new GrowthStage(list.toIntArray());
        }

        private GrowthStage(@NotNull final int... values) {
            this.values = values;
        }

        public int get(final int stage) {
            assert stage >= 0 && stage < values.length;
            return values[stage];
        }

        public int first() {
            assert values.length > 0;
            return values[0];
        }

        public int last() {
            assert values.length > 0;
            return values[values.length - 1];
        }

        public int amount() {
            return values.length;
        }

        public int[] getValues() {
            return values;
        }
    }


    public static class Stage {
        private Stage(final int harvestStage) {
            this(harvestStage, -1, -1, true);
        }

        private Stage(final int harvestStage, final int clearStage, final int checkHealthStage, boolean regenerates) {
            this(harvestStage, clearStage, checkHealthStage, regenerates, new int[0]);
        }

        private Stage(final int harvestStage, final int clearStage, final int checkHealthStage, boolean regenerates, final int... productStages) {
            this.harvestStage = harvestStage;
            this.clearStage = clearStage;
            this.checkHealthStage = checkHealthStage;
            this.productStages = productStages;
            this.regenerates = regenerates;
        }

        protected final int harvestStage;
        private final int clearStage;
        private final int checkHealthStage;
        private final int[] productStages;
        private final boolean regenerates;

        public int getHarvestStage() {
            return harvestStage;
        }

        public int getClearStage() {
            return clearStage;
        }

        public int getCheckHealthStage() {
            return checkHealthStage;
        }

        public int[] getProductStages() {
            return productStages;
        }

        public boolean regenerates() {
            return regenerates;
        }
    }
}
