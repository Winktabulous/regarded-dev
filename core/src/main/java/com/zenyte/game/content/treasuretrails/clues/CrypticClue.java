package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureTrailType;
import com.zenyte.game.content.treasuretrails.challenges.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Bonuses;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.IntArray;
import com.zenyte.utils.StaticInitializer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zenyte.game.content.treasuretrails.ClueLevel.*;

/**
 * @author Kris | 30. march 2018 : 2:14.05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@StaticInitializer
public enum CrypticClue implements Clue {
   CRYPTIC_1(EASY, "A crate found in the tower of a church is your next location.", new SearchRequest(new GameObject[] {new GameObject(357, new Location(2612, 3304, 1), "Search")})),
   CRYPTIC_2(EASY, "Belladonna, my dear. If only I had gloves, then I could hold you at last.", new TalkRequest(IntArray.of(0))),
   CRYPTIC_3(EASY, "Dig between some ominous stones in Falador.", new DigRequest(new Location(3040, 3399, 0))),
   CRYPTIC_4(EASY, "Dig in the centre of a great kingdom of 5 cities.", new DigRequest(new Location(1639, 3673, 0))),
   CRYPTIC_5(EASY, "Dig near some giant mushrooms, behind the Grand Tree.", new DigRequest(new Location(2458, 3504, 0))),
   CRYPTIC_6(EASY, "Dig under Ithoi's cabin.", new DigRequest(new Location(2529, 2838, 0))),
   CRYPTIC_7(EASY, "Dig where only the skilled, the wealthy, or the brave can choose not to visit again.", new DigRequest(new Location(3221, 3219, 0))),
   CRYPTIC_8(EASY, "Impossible to make angry", new TalkRequest(IntArray.of(2577))),
   CRYPTIC_9(EASY, "I wonder how many bronze swords he has handed out.", new TalkRequest(IntArray.of(403))),
   CRYPTIC_10(EASY, "I would make a chemistry joke, but I'm afraid I wouldn't get a reaction.", new TalkRequest(IntArray.of(1146))),
   CRYPTIC_11(EASY, "I wouldn't wear this jean on my legs.", new TalkRequest(IntArray.of(6955))),  //NPC not in-game
   CRYPTIC_12(EASY, "Look in the ground floor crates of houses in Falador.", new SearchRequest(new GameObject[] {new GameObject(24088, new Location(3029, 3355, 0), "Search")})),
   CRYPTIC_13(EASY, "Monk's residence in the far west. See robe storage device.",
           new SearchRequest(new GameObject[] {new GameObject(ObjectId.DRAWERS_350, new Location(1746, 3490, 0), "Search")})),
   //Change to open drawer ID
   CRYPTIC_14(EASY, "One of the sailors in Port Sarim is your next destination.", new TalkRequest(IntArray.of(3644))),
   CRYPTIC_15(EASY, "Salty peter.", new TalkRequest(IntArray.of(6922))),
   CRYPTIC_16(EASY, "Search a bookcase in Lumbridge swamp.", new SearchRequest(new GameObject[] {new GameObject(9523, new Location(3146, 3177, 0), "Search")})),
   CRYPTIC_17(EASY, "Search a bookcase in the Wizards tower.", new SearchRequest(new GameObject[] {new GameObject(12539, new Location(3113, 3158, 0), "Search")})),
   //CRYPTIC_18(EASY,
   // "Search a crate in the Haymaker's arms.", // Area moved, @kris
   CRYPTIC_19(EASY, "Search a wardrobe in Draynor.", new SearchRequest(new GameObject[] {new GameObject(5622, new Location(3087, 3261, 0), "Search")})),
   //Change object ID to open wardrobe ID
   CRYPTIC_20(EASY, "Search chests found in the upstairs of shops in Port Sarim.", new SearchRequest(new GameObject[] {new GameObject(375, new Location(3016, 3205, 1), "Search")})),
   //Change object ID to open chest ID
   CRYPTIC_21(EASY, "Search for a crate in a building in Hemenster.", new SearchRequest(new GameObject[] {new GameObject(357, new Location(2636, 3453, 0), "Search")})),
   CRYPTIC_22(EASY, "Search for a crate in Varrock Castle.", new SearchRequest(new GameObject[] {new GameObject(5113, new Location(3224, 3492, 0), "Search")})),
   CRYPTIC_23(EASY, "Search for a crate on the ground floor of a house in Seers' Village.", new SearchRequest(new GameObject[] {new GameObject(25775, new Location(2699, 3470, 0), "Search")})),
   CRYPTIC_24(EASY, "Search the bookcase in the monastery.", new SearchRequest(new GameObject[] {new GameObject(380, new Location(3054, 3483, 0), "Search")})),
   CRYPTIC_25(EASY, "Search the boxes in a shop in Taverley.", new SearchRequest(new GameObject[] {new GameObject(360, new Location(2886, 3449, 0), "Search")})),
   CRYPTIC_26(EASY, "Search the boxes in one of the tents in Al Kharid.", new SearchRequest(new GameObject[] {new GameObject(361, new Location(3308, 3206, 0), "Search")})),
   CRYPTIC_27(EASY, "Search the boxes in the goblin house near Lumbridge.", new SearchRequest(new GameObject[] {new GameObject(359, new Location(3245, 3245, 0), "Search")})),
   CRYPTIC_28(EASY, "Search the boxes in the house near the south entrance to Varrock.", new SearchRequest(new GameObject[] {new GameObject(5111, new Location(3203, 3384, 0), "Search")})),
   CRYPTIC_29(EASY, "Search the boxes just outside the Armour shop in East Ardougne.", new SearchRequest(new GameObject[] {new GameObject(361, new Location(2654, 3299, 0), "Search")})),
   CRYPTIC_30(EASY, "Search the boxes of Falador's general store.", new SearchRequest(new GameObject[] {new GameObject(24088, new Location(2955, 3390, 0), "Search")})),
   /*CRYPTIC_31(EASY,
            "Search the bucket in the Port Sarim jail.",
            new SearchRequest(new GameObject[]{ new GameObject(9568, new Location(3013, 3179, 0),  "Search") })),
    //OSRS has long dialogue to get into the cell, not finished on zenyte*/
   CRYPTIC_32(EASY, "Search the bush at the digsite centre.", new SearchRequest(new GameObject[] {new GameObject(2357, new Location(3345, 3378, 0), "Search")})),

   /* Disabled CRYPTIC_33 because Near-Reality map does not have barbarian village
   CRYPTIC_33(EASY, "Search the chest in Barbarian Village.",
           new SearchRequest(new GameObject[] {new GameObject(375, new Location(3085, 3429, 0), "Search")})),
   */
   //Change object ID to open chest ID
   CRYPTIC_34(EASY, "Search the chest in the Duke of Lumbridge's bedroom.", new SearchRequest(new GameObject[] {new GameObject(375, new Location(3209, 3218, 1), "Search")})),
   //Change object ID to open chest ID
   CRYPTIC_35(EASY, "Search the chest in the left-hand tower of Camelot Castle.", new SearchRequest(new GameObject[] {new GameObject(25592, new Location(2748, 3495, 2), "Search")})),
   //Change object ID to open chest ID
   CRYPTIC_36(EASY, "Search the chests in the Dwarven Mine.", new SearchRequest(new GameObject[] {new GameObject(375, new Location(3000, 9798, 0), "Search")})),
   //Change object ID to open chest ID
   CRYPTIC_37(EASY, "Search the chests upstairs in Al Kharid Palace.", new SearchRequest(new GameObject[] {new GameObject(375, new Location(3301, 3169, 1), "Search")})),
   //Change object ID to open chest ID
   // CRYPTIC_38(EASY, Coffin is missing since custom home
   CRYPTIC_39(EASY, "Search the crate in the left-hand tower of Lumbridge Castle.", new SearchRequest(new GameObject[] {new GameObject(357, new Location(3228, 3212, 1), "Search")})),
   CRYPTIC_40(EASY, "Search the crate in the Toad and Chicken pub.", new SearchRequest(new GameObject[] {new GameObject(354, new Location(2913, 3536, 0), "Search")})),
   CRYPTIC_41(EASY, "Search the crate near a cart in Port Khazard.", new SearchRequest(new GameObject[] {new GameObject(366, new Location(2660, 3149, 0), "Search")})),
   CRYPTIC_42(EASY, "Search the crates in a bank in Varrock.",
           new SearchRequest(new GameObject[] {
                   new GameObject(ObjectId.CRATE_5107, new Location(3195, 9825, 0), "Search"),
                   new GameObject(ObjectId.CRATE_5107, new Location(3187, 9825, 0), "Search"),
                   new GameObject(ObjectId.CRATE_5107, new Location(3196, 9826, 0), "Search")
           })),
   CRYPTIC_43(EASY, "Search the crates in a house in Yanille that has a piano.", new SearchRequest(new GameObject[] {new GameObject(357, new Location(2598, 3105, 0), "Search")})),
   CRYPTIC_44(EASY, "Search the crates in Canifis.", new SearchRequest(new GameObject[] {new GameObject(24344, new Location(3509, 3497, 0), "Search")})),
   CRYPTIC_45(EASY, "Search the crates in Draynor Manor.", new SearchRequest(new GameObject[] {new GameObject(11485, new Location(3106, 3369, 2), "Search")})),
   CRYPTIC_46(EASY, "Search the crates in East Ardougne's general store.", new SearchRequest(new GameObject[] {new GameObject(357, new Location(2615, 3291, 0), "Search")})),
   CRYPTIC_47(EASY, "Search the crates in Horvik's armoury.", new SearchRequest(new GameObject[] {new GameObject(5106, new Location(3228, 3433, 0), "Search")})),
   /* Disabled CRYPTIC_48 because Near-Reality map does not have barbarian village
   CRYPTIC_48(EASY, "Search the crates in the Barbarian Village helmet shop.", new SearchRequest(new GameObject[] {new GameObject(11600, new Location(3073, 3430, 0), "Search")})),
   */
   CRYPTIC_49(EASY, "Search the crates in the Dwarven mine.", new SearchRequest(new GameObject[] {new GameObject(357, new Location(3035, 9849, 0), "Search")})),
   CRYPTIC_50(EASY, "Search the crates in the guard house of the northern gate of East Ardougne.", new SearchRequest(new GameObject[] {new GameObject(356, new Location(2645, 3338, 0), "Search")})),
   CRYPTIC_51(EASY, "Search the crates in the most north-western house in Al Kharid.", new SearchRequest(new GameObject[] {new GameObject(358, new Location(3289, 3202, 0), "Search")})),
   CRYPTIC_52(EASY, "Search the crates in the outhouse of the long building in Taverley.", new SearchRequest(new GameObject[] {new GameObject(357, new Location(2914, 3433, 0), "Search")})),
   CRYPTIC_53(EASY, "Search the crates in the Port Sarim Fishing shop.", new SearchRequest(new GameObject[] {new GameObject(9534, new Location(3012, 3222, 0), "Search")})),
   CRYPTIC_54(EASY, "Search the crates in the shed just north of East Ardougne.", new SearchRequest(new GameObject[] {new GameObject(355, new Location(2617, 3347, 0), "Search")})),
   CRYPTIC_55(EASY, "Search the crates near a cart in Varrock.", new SearchRequest(new GameObject[] {new GameObject(5107, new Location(3226, 3452, 0), "Search")})),
   CRYPTIC_56(EASY, "Search the drawers above Varrock's shops.", new SearchRequest(new GameObject[] {new GameObject(7194, new Location(3206, 3419, 1), "Search")})),
   //Replace drawer ID with open drawer ID
   /*CRYPTIC_57(EASY,
            "Search the drawers found upstairs in East Ardougne's houses.",
            new SearchRequest(new GameObject[]{ new GameObject(348, new Location(2574, 3326, 1),  "Search") })),Code does not allow cus it's also used as a key-drawer. Cba.
    //Replace drawer ID with open drawer ID*/
   CRYPTIC_58(EASY, "Search the drawers in a house in Draynor Village.", new SearchRequest(new GameObject[] {new GameObject(350, new Location(3097, 3277, 0), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_59(EASY, "Search the drawers in Catherby's Archery shop.", new SearchRequest(new GameObject[] {new GameObject(350, new Location(2825, 3442, 0), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_60(EASY, "Search the drawers in Falador's chain mail shop.", new SearchRequest(new GameObject[] {new GameObject(348, new Location(2969, 3311, 0), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_61(EASY, "Search the drawers in one of Gertrude's bedrooms.", new SearchRequest(new GameObject[] {new GameObject(7194, new Location(3156, 3406, 0), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_62(EASY, "Search the drawers in the ground floor of a shop in Yanille.", new SearchRequest(new GameObject[] {new GameObject(350, new Location(2570, 3085, 0), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_63(EASY, "Search the drawers in the house next to the Port Sarim mage shop.", new SearchRequest(new GameObject[] {new GameObject(348, new Location(3024, 3259, 0), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_64(EASY, "Search the drawers in the upstairs of a house in Catherby.", new SearchRequest(new GameObject[] {new GameObject(350, new Location(2809, 3451, 1), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_65(EASY, "Search the drawers of houses in Burthorpe.", new SearchRequest(new GameObject[] {new GameObject(348, new Location(2929, 3570, 0), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_66(EASY, "Search the drawers on the ground floor of a building facing Ardougne's Market.",
           new SearchRequest(new GameObject[] {new GameObject(ObjectId.DRAWERS_350, new Location(2653, 3320, 0), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_67(EASY, "Search the drawers upstairs in Falador's shield shop.", new SearchRequest(new GameObject[] {new GameObject(348, new Location(2971, 3386, 1), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_68(EASY, "Search the drawers, upstairs in the bank to the East of Varrock.", new SearchRequest(new GameObject[] {new GameObject(7194, new Location(3250, 3420, 1), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_69(EASY, "Search the drawers upstairs of houses in the eastern part of Falador.", new SearchRequest(new GameObject[] {new GameObject(350, new Location(3035, 3347, 1), "Search")})),

   //Replace drawer ID with open drawer ID

   //Doesn't exist due to rework
   //CRYPTIC_70(EASY, "Search the open crate found in a small farmhouse in Hosidius. Cabbages grow outside.", new SearchRequest(new GameObject[] {new GameObject(27533, new Location(1687, 3628, 0), "Search")})),
   CRYPTIC_71(EASY, "Search the tents in the Imperial Guard camp in Burthorpe for some boxes.", new SearchRequest(new GameObject[] {new GameObject(3686, new Location(2885, 3540, 0), "Search")})),
   CRYPTIC_72(EASY, "Search the wheelbarrow in Rimmington mine.", new SearchRequest(new GameObject[] {new GameObject(9625, new Location(2978, 3239, 0), "Search")})),
   CRYPTIC_73(EASY, "Search through chests found in the upstairs of houses in eastern Falador.", new SearchRequest(new GameObject[] {new GameObject(375, new Location(3041, 3364, 1), "Search")})),
   //Change object ID to open chest ID
   CRYPTIC_74(EASY, "Search through some drawers in the upstairs of a house in Rimmington.", new SearchRequest(new GameObject[] {new GameObject(352, new Location(2970, 3214, 1), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_75(EASY, "Search through some drawers found in Taverley's houses.", new SearchRequest(new GameObject[] {new GameObject(350, new Location(2894, 3418, 0), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_76(EASY, "Search upstairs in the houses of Seers' Village for some drawers.", new SearchRequest(new GameObject[] {new GameObject(25766, new Location(2716, 3471, 1), "Search")})),
   //Replace drawer ID with open drawer ID
   CRYPTIC_77(EASY, "Someone watching the fights in the Duel Arena is your next destination.", new TalkRequest(IntArray.of(3351))),
   //NPC isn't on zenyte at the moment
   CRYPTIC_78(EASY, "Speak to Arhein in Catherby.", new TalkRequest(IntArray.of(3200))),
   CRYPTIC_79(EASY, "Speak to Doric, who lives north of Falador.", new TalkRequest(IntArray.of(3893))),
   CRYPTIC_80(EASY, "Speak to Ellis in Al Kharid.", new TalkRequest(IntArray.of(3231))),
   CRYPTIC_81(EASY, "Speak to Gaius in Taverley.", new TalkRequest(IntArray.of(7798))),
   CRYPTIC_82(EASY, "Speak to Hans to solve the clue.", new TalkRequest(IntArray.of(3105))),
   CRYPTIC_83(EASY, "Speak to Jatix in Taverley.", new TalkRequest(IntArray.of(8532))),
   CRYPTIC_84(EASY, "Speak to Ned in Draynor Village.", new TalkRequest(IntArray.of(4280))),
   CRYPTIC_85(EASY, "Speak to Rusty north of Falador.", new TalkRequest(IntArray.of(3281))),
   CRYPTIC_86(EASY, "Speak to Sarah at Falador farm.", new TalkRequest(IntArray.of(501))),
   CRYPTIC_87(EASY, "Speak to Sir Kay in Camelot Castle.", new TalkRequest(IntArray.of(NpcId.SIR_KAY))),
   CRYPTIC_88(EASY, "Speak to the bartender of the Blue Moon Inn in Varrock.", new TalkRequest(IntArray.of(1312))),
   CRYPTIC_89(EASY, "Speak to the Lady of the Lake.", new TalkRequest(IntArray.of(3530))),
   CRYPTIC_90(EASY, "Speak to the staff of Sinclair mansion.", new TalkRequest(IntArray.of(4215))),
   CRYPTIC_91(EASY, "Strength potions with red spiders' eggs? He is quite a herbalist.", new TalkRequest(IntArray.of(5036))),
   CRYPTIC_92(EASY, "Surrounded by white walls and gems.", new TalkRequest(IntArray.of(6529))),
   CRYPTIC_93(EASY, "Talk to Ambassador Spanfipple in the White Knights Castle.", new TalkRequest(IntArray.of(2556))),
   //NPC isn't in-game yet
   CRYPTIC_94(EASY, "Talk to a party-goer in Falador.", new TalkRequest(IntArray.of(5795))),
   CRYPTIC_95(EASY, "Talk to Cassie in Falador.", new TalkRequest(IntArray.of(3214))),
   CRYPTIC_96(EASY, "Talk to Ermin.", new TalkRequest(IntArray.of(2567))),
   CRYPTIC_97(EASY, "Talk to the barber in the Falador barber shop.", new TalkRequest(IntArray.of(1305))),
   CRYPTIC_98(EASY, "Talk to the bartender of the Rusty Anchor in Port Sarim.", new TalkRequest(IntArray.of(1313))),
   CRYPTIC_99(EASY, "Talk to the Doomsayer.", new TalkRequest(IntArray.of(NpcId.DOOMSAYER, NpcId.DOOMSAYER_6774))),
   /*CRYPTIC_100(EASY,
            "Talk to the mother of a basement dwelling son.",
            new TalkRequest(IntArray.of(4808))),*/
   //NPC Need to add NPC since custom home
   CRYPTIC_101(EASY, "Talk to the Squire in the White Knights' castle in Falador.", new TalkRequest(IntArray.of(4737))),
   CRYPTIC_102(EASY, "Talk to Zeke in Al Kharid.", new TalkRequest(IntArray.of(2875))),
   CRYPTIC_103(EASY, "Thanks, Grandma!", new TalkRequest(IntArray.of(6964))),
   CRYPTIC_104(EASY, "The hand ain't listening.", new TalkRequest(IntArray.of(4590))),
   CRYPTIC_105(MEDIUM, "A town with a different sort of night-life is your destination. Search for some crates in one of the houses.", new SearchRequest(new GameObject[] {new GameObject(24344, new Location(3498, 3507, 0), "Search")})),
   CRYPTIC_106(MEDIUM, "Find a crate close to the monks that like to paaarty!", new SearchRequest(new GameObject[] {new GameObject(354, new Location(2614, 3204, 0), "Search")})),
   CRYPTIC_107(MEDIUM, "Go to the village being attacked by trolls, search the drawers in one of the houses.",
           new KeyRequest(ItemId.KEY_MEDIUM,
                   IntArray.of(NpcId.PENDA),
                   new GameObject[] {new GameObject(ObjectId.DRAWERS_350, new Location(2921, 3577, 0), "Search")})),
   //Change drawer ID to open drawer ID
   CRYPTIC_108(MEDIUM, "Go to this building to be illuminated, and check the drawers while you are there.",
           new KeyRequest(ItemId.KEY_MEDIUM_2834,
                   IntArray.of(NpcId.MARKET_GUARD, NpcId.MARKET_GUARD_3949, NpcId.MARKET_GUARD_5732),
                   new GameObject[] {new GameObject(ObjectId.DRAWERS_350, new Location(2512, 3641, 1), "Search")})),
   //Change drawer ID to open drawer ID
   CRYPTIC_109(MEDIUM, "In a town where everyone has perfect vision, seek some locked drawers in a house that sits opposite a workshop.",
           new KeyRequest(ItemId.KEY_MEDIUM_2836,
                   IntArray.of(NpcId.CHICKEN, NpcId.CHICKEN_1174),
                   new GameObject[] {new GameObject(ObjectId.DRAWERS_25766, new Location(2709, 3478, 0), "Search")})),
   //Change drawer ID to open drawer ID
   CRYPTIC_110(MEDIUM, "In a town where the guards are armed with maces, search the upstairs rooms of the Public House.",
           new KeyRequest(ItemId.KEY_MEDIUM_2838,
                   IntArray.of(NpcId.GUARD_DOG),
                   new GameObject[] {new GameObject(ObjectId.DRAWERS, new Location(2574, 3326, 1), "Search")})),
   //Change drawer ID to open drawer ID
   CRYPTIC_111(MEDIUM, "In a town where thieves steal from stalls, search for some drawers in the upstairs of a house near the bank.",
           new KeyRequest(ItemId.KEY_MEDIUM_2840,
                   IntArray.of(NpcId.GUARD_5418),
                   new GameObject[] {new GameObject(ObjectId.DRAWERS, new Location(2611, 3324, 1), "Search")})),
   //Change drawer ID to open drawer ID
   CRYPTIC_112(MEDIUM, "In a town where wizards are known to gather, search upstairs in a large house to the north.",
           new KeyRequest(ItemId.KEY_MEDIUM_3606,
                   IntArray.of(NpcId.MAN_3106, NpcId.MAN_3107, NpcId.MAN_3108, NpcId.MAN_3109, NpcId.MAN_3110),
                   new GameObject[] {new GameObject(ObjectId.CLOSED_CHEST_375, new Location(2593, 3108, 1), "Search")})),
   //Change chest ID to open chest ID
   CRYPTIC_113(MEDIUM, "In a village made of bamboo, look for some crates under one of the houses.", new SearchRequest(new GameObject[] {new GameObject(356, new Location(2800, 3074, 0), "Search")})),
   CRYPTIC_114(MEDIUM, "Probably filled with wizards socks.",
           new KeyRequest(ItemId.KEY_MEDIUM_3608,
                   IntArray.of(NpcId.WIZARD_3257),
                   new GameObject[] {
                           new GameObject(ObjectId.DRAWERS_350, new Location(3116, 9562, 0), "Search"),
                           new GameObject(ObjectId.DRAWERS_350, new Location(3118, 9561, 0), "Search"),
                  }
            )
   ),
   //Change drawer ID to open drawer ID
   CRYPTIC_115(MEDIUM, "Search the upstairs drawers of a house in a village where pirates are known to have a good time.",
           new KeyRequest(ItemId.KEY_MEDIUM_7297,
                   IntArray.of(NpcId.PIRATE_522, NpcId.PIRATE_GUARD),
                   new GameObject[] {new GameObject(ObjectId.DRAWERS, new Location(2809, 3165, 1), "Search")})),
   //Change drawer ID to open drawer ID
   /*CRYPTIC_116(MEDIUM,
            "Speak to a referee.",
            new TalkChallengeRequest(ChallengeScroll.GNOME_BALL_REFEREE, IntArray.of(3157))),*/
   CRYPTIC_117(MEDIUM, "Speak to Donovan, the Family Handyman.", new TalkRequest(IntArray.of(4212))),
   CRYPTIC_118(MEDIUM, "Speak to Hajedy.", new TalkRequest(IntArray.of(5356))),
   CRYPTIC_119(MEDIUM, "Speak to Hazelmere.", new TalkChallengeRequest(ChallengeScroll.HAZELMERE, IntArray.of(1422, 4647))),
   CRYPTIC_120(MEDIUM, "Speak to Kangai Mau.", new TalkRequest(IntArray.of(5316))),
   CRYPTIC_121(MEDIUM, "Speak to Roavar.", new TalkRequest(IntArray.of(6527))),
   CRYPTIC_122(MEDIUM, "Speak to Ulizius.", new TalkRequest(IntArray.of(947))),
   CRYPTIC_123(MEDIUM, "The dead, red dragon watches over this chest. He must really dig the view.",
           new KeyRequest(ItemId.KEY_MEDIUM_7299,
                   IntArray.of(
                           NpcId.BARBARIAN_3262,
                           NpcId.BARBARIAN_3071,
                           5227,
                           NpcId.BARBARIAN,
                           NpcId.BARBARIAN_3056,
                           NpcId.BARBARIAN_3069,
                           NpcId.BARBARIAN_3059,
                           NpcId.BARBARIAN_3057,
                           NpcId.BARBARIAN_3062
                   ),
                   new GameObject[] {new GameObject(ObjectId.CLOSED_CHEST_375, new Location(3353, 3332, 0), "Search")})),
   //Change chest ID to open chest ID
   CRYPTIC_124(MEDIUM, "The treasure is buried in a small building full of bones. Here is a hint: it's not near a " +
           "graveyard.", new DigRequest(new Location(3356, 3507, 0))),
   CRYPTIC_125(MEDIUM, "This crate holds a better reward than a broken arrow.", new SearchRequest(new GameObject[] {new GameObject(356, new Location(2671, 3437, 0), "Search")})),
   /*CRYPTIC_126(MEDIUM,
            "Try not to step on any aquatic nasties while searching this crate.",
            new SearchRequest(new GameObject[]{ new GameObject(18204, new Location(2764, 3273, 0),  "Search") })),*/
   CRYPTIC_127(MEDIUM, "You'll need to look for a town with a central fountain. Look for a locked chest in the town's chapel.",
           new KeyRequest(ItemId.KEY_MEDIUM_7302,
                   IntArray.of(NpcId.MONK_4246, 6222),
                   new GameObject[] {new GameObject(ObjectId.CLOSED_CHEST_5108, new Location(3256, 3487, 0), "Search")})),
   //Change chest ID to open chest ID
   CRYPTIC_128(HARD, "46 is my number. My body is the colour of burnt orange and crawls among those with eight. Three mouths I have, yet I cannot eat. My blinking blue eye hides my grave.", new DigRequest(new Location(3170, 3885, 0))),
   CRYPTIC_129(HARD, "'A bag belt only?', he asked his balding brothers.", new TalkRequest(IntArray.of(2577))),
   CRYPTIC_130(HARD, "Aggie I see. Lonely and southern I feel. I am neither inside nor outside the house, yet no home would be complete without me. The treasure lies beneath me!", new DigRequest(new Location(3085, 3255, 0))),
   CRYPTIC_131(HARD, "A great view - watch the rapidly drying hides get splashed. Check the box you are sitting on.", new SearchRequest(new GameObject[] {new GameObject(359, new Location(2523, 3493, 1), "Search")})),
   CRYPTIC_132(HARD, "A I Q 0 4 4 0", new DigRequest(new Location(3000, 3110, 0))),
   CRYPTIC_133(HARD, "A I R 2 3 3 1", new DigRequest(new Location(2702, 3246, 0))),
   CRYPTIC_134(HARD, "A L P 1 1 4 0", new DigRequest(new Location(2504, 3633, 0))),
   /*CRYPTIC_135(HARD,
            "And so on, and so on, and so on. " +
                    "Walking from the land of many unimportant things leads to a choice of paths.",
            new DigRequest(new Location(2592, 3879, 0))),*/
   CRYPTIC_136(HARD, "Between where the best are commemorated for a year, and a celebratory cup, not just for beer.", new DigRequest(new Location(3388, 3152, 0))),
   CRYPTIC_137(HARD, "B I P 7 0 1 3", new DigRequest(new Location(3407, 3330, 0))),
   CRYPTIC_138(HARD, "B J R 1 1 2 3", new DigRequest(new Location(2648, 4729, 0))),
   CRYPTIC_139(HARD, "B L P 6 2 0 0", new DigRequest(new Location(2439, 5132, 0))),
   CRYPTIC_141(HARD, "C I S 0 0 0 9", new DigRequest(new Location(1630, 3868, 0))),
   CRYPTIC_142(HARD, "Citric cellar.", new PuzzleRequest(IntArray.of(16))),
   CRYPTIC_143(HARD, "C K P 0 2 2 4", new DigRequest(new Location(2073, 4846, 0))),
   /*CRYPTIC_144(HARD,
            "Come have a cip with this great soot covered denizen.",
            new TalkChallengeRequest(ChallengeScroll.MINER_MAGNUS, IntArray.of(3654))),*/
//    CRYPTIC_145(HARD,
   //           "Come to the evil ledge, Yew know yew want to. Try not to get stung.",
   //           new DigRequest(new Location(1630, 3868, 0))),
//    Not accessible area since custom home
   CRYPTIC_146(HARD, "Covered in shadows, the centre of the circle is where you will find the answer.", new DigRequest(new Location(3488, 3289, 0))),
   CRYPTIC_147(HARD, "Dig where 4 siblings and I all live with our evil overlord.", new DigRequest(new Location(3195, 3357, 0))),
   /*CRYPTIC_148(HARD,
            "D I P 8 5 1 1",
            new DigRequest(new Location(3041, 4770, 0))),*/
   CRYPTIC_149(HARD, "D K S 2 3 1 0", new DigRequest(new Location(2747, 3720, 0))),
   CRYPTIC_150(HARD, "Four blades I have, yet draw no blood; Still I turn my prey to powder. If you are brave, come search my roof; It is there my blades are louder.", new SearchRequest(new GameObject[] {new GameObject(12963, new Location(3166, 3309, 2), "Search")})),
   CRYPTIC_151(HARD, "Generally speaking, his nose was very bent.", new PuzzleRequest(IntArray.of(669))),
   CRYPTIC_152(HARD, "Gold I see, yet gold I require. Give me 875 if death you desire.", new PuzzleRequest(IntArray.of(2345))),
   CRYPTIC_153(HARD, "He knows just how easy it is to lose track of time.", new TalkChallengeRequest(ChallengeScroll.BROTHER_KOJO, IntArray.of(3606))),
   CRYPTIC_154(HARD, "His head might be hollow, but the crates nearby are filled with surprises.", new SearchRequest(new GameObject[] {new GameObject(354, new Location(3478, 3091, 0), "Search")})),
   CRYPTIC_155(HARD, "I am a token of the greatest love. I have no beginning or end. My eye is red, I can fit like a glove. Go to the place where it's money they lend, And dig by the gate to be my friend.", new DigRequest(new Location(3191, 9825, 0))),
   CRYPTIC_156(HARD, "Identify the back of this over-acting brother. (He's a long way from home.)", new PuzzleRequest(IntArray.of(3352))),
   CRYPTIC_157(HARD, "If you look closely enough, it seems that the archers have lost more than their needles.", new SearchRequest(new GameObject[] {new GameObject(300, new Location(2671, 3415, 0), "Search")})),
   CRYPTIC_158(HARD, "I have many arms but legs, I have just one. I have little family but my seed you can grow on, I am not dead, yet I am but a spirit, and my power, on your quests, you will earn the right to free it.", new TalkChallengeRequest(ChallengeScroll.SPIRIT_TREE, IntArray.of(4982))),
   CRYPTIC_159(HARD, "I lie lonely and forgotten in mid wilderness, where the dead rise from their beds. Feel free to quarrel and wind me up, and dig while you shoot their heads.", new DigRequest(new Location(3174, 3663, 0))),
   CRYPTIC_160(HARD, "In the city where merchants are said to have lived, talk to a man with a splendid cape, but a hat dropped by goblins.", new TalkRequest(IntArray.of(2658))),
   CRYPTIC_161(HARD, "It seems to have reached the end of the line, and it's still empty.", new SearchRequest(new GameObject[] {new GameObject(6045, new Location(3041, 9820, 0), "Search")})),
   CRYPTIC_162(HARD, "I watch the sea. I watch you fish. I watch your tree.", new PuzzleRequest(IntArray.of(2670))), 
   CRYPTIC_163(HARD, "Leader of the Yak City.", new TalkRequest(IntArray.of(1878))),
   CRYPTIC_164(HARD, "Mine was the strangest birth under the sun. I left the crimson sack, yet life had not begun. Entered the world, and yet was seen by none.", new DigRequest(new Location(2832, 9586, 0))),
   CRYPTIC_165(HARD, "Must be full of railings.", new SearchRequest(new GameObject[] {new GameObject(6176, new Location(2576, 3464, 0), "Search")})),
   CRYPTIC_166(HARD, "My giant guardians below the market streets would be fans of rock and roll, if only they could grab hold of it. Dig near my green bubbles!", new DigRequest(new Location(3161, 9904, 0))),
   CRYPTIC_167(HARD, "My home is grey, and made of stone; A castle with a search for a meal. Hidden in some drawers I am, across from a wooden wheel.", new SearchRequest(new GameObject[] {new GameObject(5618, new Location(3213, 3216, 1), "Search")})),
   //Change drawer ID to open drawer ID
   CRYPTIC_168(HARD, "My name is like a tree, yet it is spelt with a 'g'. Come see the fur which is right near me.", new PuzzleRequest(IntArray.of(3503))),
   CRYPTIC_169(HARD, "Often sought out by scholars of histories past, find me where words of wisdom speak volumes.", new PuzzleRequest(IntArray.of(3637, 3636, 3638))),
   CRYPTIC_170(HARD, "Probably filled with books on magic.", new SearchRequest(new GameObject[] {new GameObject(380, new Location(3096, 9571, 0), "Search")})),
   CRYPTIC_171(HARD, "Read 'How to breed scorpions.' By O.W.Thathurt.", new SearchRequest(new GameObject[] {new GameObject(380, new Location(2702, 3409, 1), "Search")})),
   CRYPTIC_172(HARD, "Rotting next to a ditch. Dig next to the fish.", new DigRequest(new Location(3547, 3183, 0))),
   CRYPTIC_173(HARD, "'Small shoe.' Often found with rod on mushroom.", new PuzzleRequest(IntArray.of(6080))),
   CRYPTIC_174(HARD, "Snah? I feel all confused, like one of those cakes...", new TalkRequest(IntArray.of(3105))),
   CRYPTIC_175(HARD, "Surviving.", new PuzzleRequest(IntArray.of(4736))),
   CRYPTIC_176(HARD, "The beasts to my east snap claws and tails, The rest to my west can slide and eat fish. The force to my north will jump and they'll wail, Come dig by my fire and make a wish.", new DigRequest(new Location(2598, 3267, 0))),
   CRYPTIC_177(HARD, "The cheapest water for miles around, but they react badly to religious icons.", new SearchRequest(new GameObject[] {new GameObject(354, new Location(3178, 2987, 0), "Search")})),
   CRYPTIC_178(HARD, "The effects of this fire are magnified.", new DigRequest(new Location(1179, 3626, 0))),
   CRYPTIC_179(HARD, "The keeper of Melzars... Spare? Skeleton? Anar?", new PuzzleRequest(IntArray.of(822))),
   /*CRYPTIC_180(HARD,
            "The King's magic won't be wasted by me.",
            new PuzzleRequest(IntArray.of(1779))),*/
   // NPC not accessible right now
   CRYPTIC_181(HARD, "The magic of 4 colours, an early experience you could learn. The large beast caged up top, " +
           "rages, as his demised kin's loot now returns.", new TalkRequest(IntArray.of(7747))),
   CRYPTIC_182(HARD, "The mother of the reptilian sacrifice.", new TalkRequest(IntArray.of(2035))),
   /* CRYPTIC_183(HARD,
            "There is no 'worthier' lord.",
            new PuzzleRequest(IntArray.of(3427))),*/
   CRYPTIC_184(HARD, "THEY'RE EVERYWHERE!!!! But they were here first. Dig for treasure where the ground is rich with" +
           " ore.", new DigRequest(new Location(3081, 3421, 0))),
   CRYPTIC_185(HARD, "This aviator is at the peak of his profession.", new PuzzleRequest(IntArray.of(10461))),
   CRYPTIC_186(HARD, "This village has a problem with cartloads of the undead. Try checking the bookcase to find an answer.", new SearchRequest(new GameObject[] {new GameObject(394, new Location(2833, 2991, 0), "Search")})),
   CRYPTIC_187(HARD, "When no weapons are at hand, then is the time to reflect. In Saradomin's name, redemption draws closer...", new SearchRequest(new GameObject[] {new GameObject(350, new Location(2818, 3351, 0), "Search")})),
   //Clue 188 is yanille dungeon, the obstacles don't work so I can't get to the area.
   //Clue 189 is elemental workshop, can't access it
   CRYPTIC_190(HARD, "You will need to under-cook to solve this one.", new SearchRequest(new GameObject[] {new GameObject(357, new Location(3219, 9617, 0), "Search")})),
   CRYPTIC_191(ELITE, "A demon's best friend holds the next step of this clue.", new KillRequest(IntArray.of(104, 105, 135, 3133, 7256, 7877))),
   CRYPTIC_192(ELITE, "A Guthixian ring lies between two peaks. Search the stones and you'll find what you seek.", new SearchRequest(new GameObject[] {new GameObject(26633, new Location(2922, 3484, 0), "Search")})),
   CRYPTIC_193(ELITE, "A reck you say; let's pray there aren't any ghosts.", new TalkRequest(IntArray.of(NpcId.FATHER_AERECK))),
   CRYPTIC_194(ELITE, "A ring of water surrounds 4 powerful rings, dig above the ladder located there.", new DigRequest(new Location(1910, 4367, 0))),
   CRYPTIC_195(ELITE, "Desert insects is what I see. Taking care of them was my responsibility. Your solution is found by digging near me.", new DigRequest(new Location(3307, 9505, 0))),
//    CRYPTIC_196(ELITE,
//            "Dig under Razorlor's toad batta.",
//           new DigRequest(new Location(3307, 9505, 0))),
// Probably not accessible (tarns lair)
   CRYPTIC_197(ELITE, "Dig where the forces of Zamorak and Saradomin collide.", new DigRequest(new Location(3049, 4839, 0))),
   CRYPTIC_198(ELITE, "Dobson is my last name, and with gardening I seek fame.", new TalkRequest(IntArray.of(5315))),
   CRYPTIC_199(ELITE, "Does one really need a fire to stay warm here?", new DigRequest(new Location(3816, 3810, 0))),
   CRYPTIC_200(ELITE, "Even the seers say this clue goes right over their heads.", new SearchRequest(new GameObject[] {new GameObject(14934, new Location(2707, 3488, 2), "Search")})),
   CRYPTIC_201(ELITE, "Ghostly bones.", new KillRequest(IntArray.of(2514, 2515, 2516, 2517, 2518, 2519, 6608, 7257, 7296, 7864))),
   /*CRYPTIC_202(ELITE,
            "Green is the colour of my death as the winter-guise, I swoop towards the ground.",
            new DigRequest(new Location(2780, 3784, 0))),*/
   // On OSRS you need a sled for this
   CRYPTIC_203(ELITE, "His bark is worse than his bite.", new TalkRequest(IntArray.of(6524))),
   CRYPTIC_204(ELITE, "I am the one who watches the giants. The giants in turn watch me. I watch with two while they watch with one. Come seek where I may be.", new TalkRequest(IntArray.of(2461))),
   CRYPTIC_205(ELITE, "I burn between heroes and legends.", new TalkRequest(IntArray.of(NpcId.CANDLE_MAKER))),
   /*CRYPTIC_206(ELITE,
            "I live in a deserted crack collecting soles.",
            new TalkRequest(IntArray.of(326, 327, 4738))),*/
   //TODO Not accessible.
   CRYPTIC_207(ELITE, "In a while...", new KillRequest(IntArray.of(4184))),
   CRYPTIC_208(ELITE, "Let's hope you don't " +
            "meet a watery death when you encounter this fiend.", new KillRequest(IntArray.of(2916, 2917))),
   CRYPTIC_209(ELITE, "Mix yellow with blue and add heat, make sure you bring protection.", new KillRequest(IntArray.of(260, 261, 262, 263, 264, 7868, 7869, 7870, 8073, 8076, 8082))),
   CRYPTIC_210(ELITE, "Reflection is the weakness for these eyes of evil.", new KillRequest(IntArray.of(122, 417, 418, 7395))),
   CRYPTIC_211(ELITE, "Right on the blessed border, cursed by the evil ones. On the spot inaccessible by both; I will be waiting. The bugs' imminent possession holds the answer.", new DigRequest(new Location(3410, 3324, 0))),
   CRYPTIC_212(HARD, "Scattered coins and gems fill the floor. The chest you seek is in the north east.", new KeyRequest(19812, IntArray.of(239, 2642), new GameObject[] {new GameObject(375, new Location(2288, 4702, 0), "Search")})),
   //Change chest ID to open chest ID
   CRYPTIC_213(ELITE, "'See you in your dreams' said the vegetable man.", new TalkRequest(IntArray.of(1120))),
   /*CRYPTIC_214(ELITE,
            "Show this to Sherlock.",
            new SherlockRequest()),*/
   CRYPTIC_215(ELITE, "Speak to a Wyse man.", new TalkRequest(IntArray.of(5422))),
   /*CRYPTIC_216(ELITE,
            "Stop crying! Talk to the head.",
            new TalkRequest(IntArray.of(4258, 5310))),*/
   //TODO Not accessible.
   /*CRYPTIC_217(ELITE,
            "Surround my bones in fire, ontop the wooden pyre. Finally lay me to rest, before my one last test.",
            new KillRequest(IntArray.of(5566))),*/
   //Requires a lot more work
   CRYPTIC_218(ELITE, "The beasts retreat, for their Queen is gone; the song of this town still plays on. Dig near the birthplace of a blade, be careful not to melt your spade.", new DigRequest(new Location(2342, 3677, 0))),
   CRYPTIC_219(ELITE, "Varrock is where I reside, not the land of the dead, but I am so old, I should be there instead. Let's hope your reward is as good as it says, just 1 gold one and you can have it read.", new TalkRequest(IntArray.of(5082))),
   CRYPTIC_220(ELITE, "With a name like that, you'd expect a little more than just a few scimitars.", new TalkRequest(IntArray.of(5250))),
   CRYPTIC_221(ELITE, "W marks the spot.", new DigRequest(new Location(2867, 3546, 0))),
   CRYPTIC_222(ELITE, "You'll get licked.", new KillRequest(IntArray.of(484, 485, 486, 487, 3138))),
   CRYPTIC_223(ELITE, "You'll have to plug your nose if you use this source of herbs.", new KillRequest(IntArray.of(2, 3, 4, 5, 6, 7))),
   CRYPTIC_224(ELITE, "You might have to turn over a few stones to progress.", new KillRequest(IntArray.of(100, 102))),
   CRYPTIC_225(ELITE, "You must be 100 to play with me.", new TalkRequest(IntArray.of(1773))),
   CRYPTIC_226(ELITE, "You were 3 and I was the 6th. Come speak to me.", new TalkRequest(IntArray.of(403))),
   CRYPTIC_227(ELITE, "You will have to fly high where a sword cannot help you.", new KillRequest(IntArray.of(3169, 3170, 3171, 3172, 3173, 3174, 3175, 3176, 3177, 3178, 3179, 3180, 3181, 3182, 3183))),
   /*CRYPTIC_228(MASTER,
            "2 musical birds. Dig in front of the spinning light.",
            new DigRequest(new Location(2867, 3546, 0))),*/
   //Area not accessible
   CRYPTIC_229(MASTER, "A chisel and hammer reside in his home, strange for one of magic. Impress him with your magical equipment.", new TalkRequest(IntArray.of(8480, 8481), player -> player.getBonuses().getBonus(Bonuses.Bonus.ATT_MAGIC) >= 100)),
   //Need +100 magical attack bonus
   /*CRYPTIC_230(MASTER,
            "A dwarf, approaching death, but very much in the light.",
            new TalkRequest(IntArray.of(4010))),*/
   //Not accessible
   /*CRYPTIC_231(MASTER,
            "A massive battle rages beneath so be careful when you dig by the large broken crossbow.",
            new DigRequest(new Location(2927, 3762, 0))),*/
   //TODO Agility shortcut not added
   CRYPTIC_232(MASTER, "Anger Abbot Langley.", new TalkRequest(IntArray.of(2577), player -> player.getBonuses().getBonus(Bonuses.Bonus.PRAYER) < 0)),
   //Need negative prayer bonus
   CRYPTIC_233(MASTER, "Anger those who adhere to Saradomin's edicts to prevent travel.", new TalkRequest(IntArray.of(1165, 1166, 1167, 1168, 1169, 1170), player -> !player.getEquipment().getContainer().isEmpty())),
   //Need weapon/armour
   //CRYPTIC_234(MASTER,
   //       "Buried beneath the ground, who knows where it's found. " +
   //               "Lucky for you, A man called Jorral may have a clue.",
   //     new TalkRequest(IntArray.of(3490))),
   //I don't know if I needed to include this since it's a challenge itself iirc
   //CRYPTIC_235(MASTER,
   //       "Come brave adventurer, your sense is on fire. If you talk to me, it's an old god you desire.",
   //       new TalkRequest(IntArray.of(11111))),
   //NPC isn't on the npcn list.
   /*CRYPTIC_236(MASTER,
            "Darkness wanders around me, but fills my mind with knowledge.",
            new TalkRequest(IntArray.of(7045))),*/
   /*CRYPTIC_237(MASTER,
            "Dig in front of the icy arena where 1 of 4 was fought.",
            new DigRequest(new Location(2874, 3757, 0))),*/
   //Not accessible
   CRYPTIC_238(MASTER, "Faint sounds of 'Arr', fire giants found deep, the eastern tip of a lake, are the rewards you" +
           " could reap.", new DigRequest(new Location(3055, 10338, 0))),
   //CRYPTIC_239(MASTER,
   //       "Falo the bard wants to see you.",
   //     new TalkRequest(IntArray.of(7306))),
   //I don't know if I needed to include this since it's a challenge itself iirc
   CRYPTIC_240(MASTER, "Fiendish cooks probably won't dig the dirty dishes.", new DigRequest(new Location(3043, 4974, 1))),
   CRYPTIC_241(MASTER, "Ghommal wishes to be impressed by how strong your equipment is.", new TalkRequest(IntArray.of(2457), player -> player.getBonuses().getBonus(Bonuses.Bonus.STRENGTH) >= 100)),
   //Need +100 total melee strength bonus
   CRYPTIC_242(MASTER, "Great demons, dragons and spiders protect this blue rock, beneath which, you may find what you seek.", new DigRequest(new Location(3045, 10265, 0))),
   CRYPTIC_243(MASTER, "Guthix left his mark in a fiery lake, dig at the tip of it.", new DigRequest(new Location(3069, 3935, 0))),
   /*CRYPTIC_244(MASTER,
            "Here, there are tears, but nobody is crying. Speak to the guardian and show off your alignment to balance.",
            new TalkRequest(IntArray.of(5785))),
    //TODO Need at least three guthix related items, not accessible, maybe wrong ID not sure*/
   /*CRYPTIC_245(MASTER,
            "Hopefully this set of armour will help you to keep surviving.",
            new TalkRequest(IntArray.of(4736),
                    player -> (player.getEquipment().getId(EquipmentSlot.PLATE) == ItemId.WHITE_PLATEBODY) && player.getEquipment().getId(EquipmentSlot.LEGS) == ItemId.WHITE_PLATELEGS)),*/
   //Need white platebody and platelegs.
   //CRYPTIC_246(MASTER,
   //        "If you're feeling brave, dig beneath the dragon's eye",
   //        new DigRequest(new Location(3068, 3934, 0))),
   //Not accessible
   CRYPTIC_247(MASTER, "I lie beneath the first descent to the holy encampment.", new DigRequest(new Location(2914, 5300, 1))),
   /*CRYPTIC_248(ELITE,
            "Kill the spiritual, magic and godly whilst representing their own god",
            new KillRequest(IntArray.of(2212, 2244, 3161, 3168))),*/
   //TODO You need to wear armour matching the mage you kill, zammy items for zammy spiritual mages.
   CRYPTIC_249(MASTER, "My life was spared but these voices remain, now guarding these iron gates is my bane.", new TalkRequest(IntArray.of(5870))),
   CRYPTIC_250(MASTER, "One of several rhyming brothers, in business attire with an obsession for paper work.", new TalkRequest(IntArray.of(13))),
   CRYPTIC_251(MASTER, "Pentagrams and demons, burnt bones and remains, I wonder what the blood contains.", new DigRequest(new Location(3297, 3890, 0))),
   CRYPTIC_252(MASTER, "Robin wishes to see your finest ranged equipment.", new TalkRequest(IntArray.of(2995), player -> player.getBonuses().getBonus(Bonuses.Bonus.ATT_RANGED) >= 182)),
   //Need +182 ranged attack bonus
   CRYPTIC_253(MASTER, "She's small but can build both literally and figuratively, as long as you have their favour.", new TalkRequest(IntArray.of(6772))),
   CRYPTIC_254(MASTER, "Shhhh!", new TalkRequest(IntArray.of(7044))),
   CRYPTIC_255(MASTER, "South of a river in a town surrounded by the undead, what lies beneath the furnace?", new DigRequest(new Location(2857, 2966, 0))),
   //CRYPTIC_256(MASTER,
   //        "The far north eastern corner where 1 of 4 was defeated, the shadows still linger.",
   //       new DigRequest(new Location(1, 1, 1))),
   //Not accessible
   CRYPTIC_257(MASTER, "This place sure is a mess.", new TalkRequest(IntArray.of(6926))),
   CRYPTIC_258(MASTER, "Under a giant robotic bird that cannot fly.", new DigRequest(new Location(1756, 4940, 0))),
   /*CRYPTIC_259(MASTER,
            "Where safe to speak, the man who offers the pouch of smallest size wishes to see your alignment.",
            new TalkRequest(IntArray.of(2582))),*/
   CRYPTIC_260(BEGINNER, "Always walking around the castle grounds and somehow knows everyone's age.", new TalkRequest(IntArray.of(3105))),
   //CRYPTIC_261(BEGINNER,
   // "Buried beneath the ground, who knows where it's found." +
   //"Lucky for you, A man called Reldo may have a clue.")//TODO: Beginner HC
   CRYPTIC_262(BEGINNER, "In the place Duke Horacio calls home, talk to a man with a hat dropped by goblins.", new TalkRequest(IntArray.of(4626))),
   CRYPTIC_263(BEGINNER, "In a village of barbarians, I am the one who guards the village from up high.", new TalkRequest(IntArray.of(3063))),
   /*CRYPTIC_264(BEGINNER,
            "Talk to Charlie the Tramp in Varrock.",
            new TalkRequest(IntArray.of(5209))),//TODO: Charlie's tasks./Handled differently.*/
   CRYPTIC_265(BEGINNER, "Near the open desert I reside, to get past me you must abide. Go forward if you dare, for " +
           "when you pass me, you'll be sweating by your hair.", new TalkRequest(IntArray.of(4642)));
   private final ClueLevel level;
   private final String hint;
   private final ClueChallenge challenge;
   private static final CrypticClue[] values = values();
   private static final CrypticClue[] masterValues = Stream.of(values).filter(clue -> clue.level == MASTER).collect(Collectors.toList()).toArray(new CrypticClue[0]);
   public static final Int2ObjectMap<List<Clue>> objectMap = new Int2ObjectOpenHashMap<>();
   public static final Int2ObjectMap<List<CrypticClue>> npcMap = new Int2ObjectOpenHashMap<>();
   public static final Int2ObjectMap<List<CrypticClue>> npcKillMap = new Int2ObjectOpenHashMap<>();
   public static final Int2ObjectMap<List<CrypticClue>> npcKeyMap = new Int2ObjectOpenHashMap<>();
   private static final Set<CrypticClue> keyClues = new ObjectOpenHashSet<>();

   public static final Optional<CrypticClue> getClueFromKeyObject(final WorldObject object) {
      for (final CrypticClue clue : keyClues) {
         final KeyRequest request = ((KeyRequest) clue.getChallenge());
         for (final GameObject obj : request.getValidObjects()) {
            if (obj.getTile().matches(object)) {
               return Optional.of(clue);
            }
         }
      }
      return Optional.empty();
   }

   static {
      for (final CrypticClue value : values) {
         if (value.challenge instanceof SearchRequest) {
            final SearchRequest search = (SearchRequest) value.challenge;
            for (final GameObject obj : search.getValidObjects()) {
               final int id = obj.getId();
               objectMap.computeIfAbsent(id, a -> new ArrayList<>()).add(value);
            }
         } else if (value.challenge instanceof TalkRequest) {
            final TalkRequest talk = (TalkRequest) value.challenge;
            for (final int npc : talk.getValidNPCs()) {
               npcMap.computeIfAbsent(npc, a -> new ArrayList<>()).add(value);
            }
         } else if (value.challenge instanceof KillRequest) {
            final KillRequest kill = (KillRequest) value.challenge;
            for (final int npc : kill.getValidNPCs()) {
               npcKillMap.computeIfAbsent(npc, a -> new ArrayList<>()).add(value);
            }
         } else if (value.challenge instanceof TalkChallengeRequest) {
            final TalkChallengeRequest talk = (TalkChallengeRequest) value.challenge;
            for (final int npc : talk.getValidNPCs()) {
               npcMap.computeIfAbsent(npc, a -> new ArrayList<>()).add(value);
            }
         } else if (value.challenge instanceof KeyRequest) {
            keyClues.add(value);
            final KeyRequest key = (KeyRequest) value.challenge;
            for (final int npc : key.getValidNPCs()) {
               npcKeyMap.computeIfAbsent(npc, a -> new ArrayList<>()).add(value);
            }
            for (final GameObject obj : key.getValidObjects()) {
               final int id = obj.getId();
               objectMap.computeIfAbsent(id, a -> new ArrayList<>()).add(value);
            }
         } else if (value.challenge instanceof PuzzleRequest) {
            final PuzzleRequest talk = (PuzzleRequest) value.challenge;
            for (final int npc : talk.getValidNPCs()) {
               npcMap.computeIfAbsent(npc, a -> new ArrayList<>()).add(value);
            }
         }
      }
   }

   public static final CrypticClue random() {
      return Utils.getRandomElement(values);
   }

   public final List<String> selectMasterCrypticClues() {
      assert this.level == MASTER;
      final ObjectArrayList<String> list = new ObjectArrayList<String>();
      list.add(this.getEnumName());
      int count = 1000;
      while (--count > 0 && list.size() < 3) {
         final CrypticClue random = Utils.getRandomElement(masterValues);
         if (list.contains(random.getEnumName())) {
            continue;
         }
         list.add(random.getEnumName());
      }
      assert list.size() == 3;
      return list;
   }

   CrypticClue(final ClueLevel level, final String hint, final ClueChallenge challenge) {
      this.level = level;
      this.hint = hint;
      this.challenge = challenge;
   }

   @Override
   public void view(@NotNull Player player, @NotNull Item item) {
      player.getTemporaryAttributes().put("Clue scroll item", item);
      GameInterface.CLUE_SCROLL.open(player);
   }

   @Override
   public TreasureTrailType getType() {
      return TreasureTrailType.CRYPTIC;
   }

   @Override
   public String getEnumName() {
      return toString();
   }

   @Override
   public String getText() {
      return this.hint;
   }

   @NotNull
   @Override
   public ClueLevel level() {
      return level;
   }

   public ClueLevel getLevel() {
      return level;
   }

   public String getHint() {
      return hint;
   }

   public ClueChallenge getChallenge() {
      return challenge;
   }
}
