package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureTrailType;
import com.zenyte.game.content.treasuretrails.challenges.ClueChallenge;
import com.zenyte.game.content.treasuretrails.challenges.EmoteRequest;
import com.zenyte.game.content.treasuretrails.clues.emote.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Emote;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RSPolygon;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Kris | 30. march 2018 : 2:44.55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum EmoteClue implements Clue {

    EMOTE_1(ClueLevel.HARD, emotes(Emote.BECKON), true,
            "Beckon on the east coast of the Kharazi Jungle. Beware of double agents! Equip any vestment stole and a heraldic rune shield.",
            get(new int[][]{
                    { 2949, 2947 },
                    { 2949, 2936 },
                    { 2963, 2919 },
                    { 2959, 2898 },
                    { 2953, 2886 },
                    { 2987, 2885 },
                    { 2985, 2947 }
            }), any("Any stole", item(GUTHIX_STOLE), item(SARADOMIN_STOLE), item(ZAMORAK_STOLE), item(ARMADYL_STOLE), item(BANDOS_STOLE), item(ANCIENT_STOLE)), any("Any heraldic rune shield",
            item(RUNE_SHIELD_H1), item(RUNE_SHIELD_H2), item(RUNE_SHIELD_H3), item(RUNE_SHIELD_H4), item(RUNE_SHIELD_H5))),

    EMOTE_2(ClueLevel.MEDIUM, emotes(Emote.CHEER, Emote.HEADBANG), false,
            "Cheer in the Barbarian Agility Arena. Headbang before you talk to me. Equip a steel platebody, maple shortbow and a Wilderness cape.",
            get(new int[][]{
                    { 2550, 3560 },
                    { 2554, 3560 },
                    { 2554, 3542 },
                    { 2529, 3542 },
                    { 2529, 3551 },
                    { 2528, 3552 },
                    { 2528, 3557 },
                    { 2546, 3557 },
                    { 2546, 3556 },
                    { 2550, 3556 }
            }), item(STEEL_PLATEBODY), item(MAPLE_SHORTBOW), range("Any team cape", TEAM1_CAPE, TEAM50_CAPE)),

    EMOTE_3(ClueLevel.ELITE, emotes(Emote.BOW), false,
            "Bow upstairs in the Edgeville Monastery. Equip a completed prayer book.",
            get(new int[][]{
                    { 3044, 3499 },
                    { 3050, 3499 },
                    { 3050, 3500 },
                    { 3055, 3500 },
                    { 3055, 3499 },
                    { 3060, 3499 },
                    { 3060, 3482 },
                    { 3054, 3482 },
                    { 3054, 3483 },
                    { 3050, 3483 },
                    { 3050, 3482 },
                    { 3044, 3482 }
            }, 1), any("Any god book",
            item(HOLY_BOOK), item(BOOK_OF_BALANCE), item(UNHOLY_BOOK),
            item(BOOK_OF_LAW), item(BOOK_OF_WAR), item(BOOK_OF_DARKNESS),
            item(26488), item(26490), item(26492),
            item(26494), item(26496), item(26498)
    )),

    //TODO: Cannot be done
    /*EMOTE_4(ClueLevel.ELITE, emotes(Emote.CHEER), false,
            "Cheer in the Shadow dungeon. Equip a rune crossbow, climbing boots and any mitre.",
            get(new int[][]{
                    { 2624, 5120 },
                    { 2624, 5056 },
                    { 2752, 5056 },
                    { 2752, 5120 }
            }), any("Any mitre", item(GUTHIX_MITRE), item(SARADOMIN_MITRE), item(ZAMORAK_MITRE), item(ANCIENT_MITRE), item(BANDOS_MITRE), item(ARMADYL_MITRE)), item(RUNE_CROSSBOW),
            item(CLIMBING_BOOTS), item(RING_OF_VISIBILITY)),*/

    EMOTE_5(ClueLevel.HARD, emotes(Emote.CHEER), true,
            "Cheer at the top of the agility pyramid. Beware of double agents! Equip a blue mystic robe top, and any rune heraldic shield.",
            get(new int[][]{
                    { 3042, 4701 },
                    { 3048, 4701 },
                    { 3048, 4695 },
                    { 3042, 4695 }
            }, 3), item(MYSTIC_ROBE_TOP), any("Any rune heraldic shield", item(RUNE_SHIELD_H1), item(RUNE_SHIELD_H2), item(RUNE_SHIELD_H3), item(RUNE_SHIELD_H4), item(RUNE_SHIELD_H5))),

    /*EMOTE_6(ClueLevel.MASTER, emotes(Emote.DANCE), true,
            "Dance in Iban's temple. Beware of double agents! Equip Iban's staff, a black mystic top and a black mystic bottom.",
            null,
            any("Any iban's staff", item(IBANS_STAFF), item(IBANS_STAFF_U)), item(MYSTIC_ROBE_TOP_DARK), item(MYSTIC_ROBE_BOTTOM_DARK)),//TODO: Find Iban's temple.*/

    /*EMOTE_7(ClueLevel.ELITE, emotes(Emote.DANCE), false,
            "Dance on the Fishing Platform. Equip barrows gloves, an amulet of glory and a dragon med helm.",
            get(new int[][]{
                    { 2760, 3294 },
                    { 2760, 3271 },
                    { 2796, 3271 },
                    { 2796, 3294 }
            }), any("Any amulet of glory", item(AMULET_OF_GLORY), item(AMULET_OF_GLORY1), item(AMULET_OF_GLORY2), item(AMULET_OF_GLORY3), item(AMULET_OF_GLORY4), item(AMULET_OF_GLORY5),
            item(AMULET_OF_GLORY6)), item(BARROWS_GLOVES), item(DRAGON_MED_HELM)),*/

    EMOTE_8(ClueLevel.MASTER, emotes(Emote.FLAP), true,
            "Flap at the death altar. Beware of double agents! Equip a death tiara, a legend's cape and any ring of wealth.",
            get(new int[][]{
                    { 2176, 4864 },
                    { 2176, 4800 },
                    { 2240, 4800 },
                    { 2240, 4864 }
            }), any("Any ring of wealth", item(RING_OF_WEALTH), item(RING_OF_WEALTH_1), item(RING_OF_WEALTH_2), item(RING_OF_WEALTH_3), item(RING_OF_WEALTH_4), item(RING_OF_WEALTH_5),
            item(RING_OF_WEALTH_I), item(RING_OF_WEALTH_I1), item(RING_OF_WEALTH_I2), item(RING_OF_WEALTH_I3), item(RING_OF_WEALTH_I4), item(RING_OF_WEALTH_I5)), item(DEATH_TIARA), item(CAPE_OF_LEGENDS)),

    EMOTE_9(ClueLevel.ELITE, emotes(Emote.HEADBANG), false,
            "Headbang in the Fight Arena pub. Equip a pirate bandana, a dragonstone necklace and and a magic longbow.",
            get(new int[][]{
                    { 2563, 3155 },
                    { 2563, 3139 },
                    { 2573, 3139 },
                    { 2573, 3145 },
                    { 2571, 3145 },
                    { 2571, 3151 },
                    { 2568, 3151 },
                    { 2568, 3155 }
            }),
            any("Any pirate bandana",
                    item(PIRATE_BANDANA), item(PIRATE_BANDANA_7124), item(PIRATE_BANDANA_7130),
                    item(PIRATE_BANDANA_7136)), item(DRAGON_NECKLACE), item(MAGIC_LONGBOW)),

    EMOTE_10(ClueLevel.MASTER, emotes(Emote.JIG), true,
            "Do a jig at the barrow's chest. Beware of double agents! Equip any full barrows set.",
            get(new int[][]{
                    { 3547, 9700 },
                    { 3547, 9690 },
                    { 3557, 9690 },
                    { 3557, 9700 }
            }, 0),
            any("Any full barrows set",
                    anyAhrimSet(), anyDharokSet(), anyGuthanSet(), anyKarilSet(), anyToragSet(), anyVeracSet())),

    EMOTE_11(ClueLevel.HARD, emotes(Emote.JIG), false,
            "Jig at Jiggig. Beware of double agents! Equip a Rune spear, rune platelegs and any rune heraldic helm.",
            get(new int[][]{
                    { 2452, 3057 },
                    { 2452, 3036 },
                    { 2494, 3036 },
                    { 2494, 3057 }
            }),
            range("Any rune heraldic helm", RUNE_HELM_H1, RUNE_HELM_H5), item(RUNE_SPEAR), item(RUNE_PLATELEGS)),

    /*EMOTE_12(ClueLevel.EASY, emotes(Emote.CHEER), false,
            "Cheer at the games room. Have nothing equipped at all when you do.",
            get(new int[][]{
                    { 2176, 4992 },
                    { 2176, 4928 },
                    { 2240, 4928 },
                    { 2240, 4992 }
            }), emptySlot("Nothing at all", EquipmentSlot.HELMET, EquipmentSlot.CAPE, EquipmentSlot.AMULET, EquipmentSlot.WEAPON, EquipmentSlot.PLATE, EquipmentSlot.SHIELD, EquipmentSlot.LEGS,
            EquipmentSlot.HANDS, EquipmentSlot.BOOTS, EquipmentSlot.RING, EquipmentSlot.AMMUNITION)),*/

    EMOTE_13(ClueLevel.EASY, emotes(Emote.PANIC), false,
            "Panic on the pier where you catch the Fishing trawler. Have nothing equipped at all when you do.",
            get(new int[][]{
                    { 2663, 3163 },
                    { 2663, 3160 },
                    { 2669, 3160 },
                    { 2669, 3161 },
                    { 2686, 3161 },
                    { 2686, 3164 },
                    { 2683, 3164 },
                    { 2683, 3163 },
                    { 2677, 3163 },
                    { 2677, 3175 },
                    { 2676, 3175 },
                    { 2676, 3163 },
                    { 2668, 3163 },
                    { 2668, 3169 },
                    { 2665, 3169 },
                    { 2665, 3165 },
                    { 2666, 3165 },
                    { 2666, 3163 }
            }), emptySlot("Nothing at all", EquipmentSlot.HELMET, EquipmentSlot.CAPE, EquipmentSlot.AMULET, EquipmentSlot.WEAPON, EquipmentSlot.PLATE, EquipmentSlot.SHIELD, EquipmentSlot.LEGS,
            EquipmentSlot.HANDS, EquipmentSlot.BOOTS, EquipmentSlot.RING, EquipmentSlot.AMMUNITION)),

    EMOTE_14(ClueLevel.HARD, emotes(Emote.PANIC), true,
            "Panic in the heart of the Haunted Woods. Beware of double agents! Have no items equipped when you do.",
            get(new int[][]{
                    { 3551, 3520 },
                    { 3552, 3456 },
                    { 3639, 3456 },
                    { 3638, 3520 }
            }), emptySlot("Nothing at all", EquipmentSlot.HELMET, EquipmentSlot.CAPE, EquipmentSlot.AMULET, EquipmentSlot.WEAPON, EquipmentSlot.PLATE, EquipmentSlot.SHIELD, EquipmentSlot.LEGS,
            EquipmentSlot.HANDS, EquipmentSlot.BOOTS, EquipmentSlot.RING, EquipmentSlot.AMMUNITION)),

    EMOTE_15(ClueLevel.MASTER, emotes(Emote.ANGRY), true,
            "Show your anger towards the Statue of Saradomin in Ellamaria's garden. Beware of double agents! Equip a zamorak godsword.",
            get(new int[][]{
                    { 3227, 3480 },
                    { 3227, 3473 },
                    { 3234, 3473 },
                    { 3234, 3480 }
            }), item(ZAMORAK_GODSWORD)),

    EMOTE_17(ClueLevel.MASTER, emotes(Emote.ANGRY), true,
            "Show your anger at the Wise old man. Beware of double agents! Equip an abyssal whip, a legend's cape and some spined chaps.",
            get(new int[][]{
                    { 3087, 3256 },
                    { 3087, 3251 },
                    { 3095, 3251 },
                    { 3095, 3256 }
            }, 0), any("Abyssal whip", item(ABYSSAL_WHIP), item(VOLCANIC_ABYSSAL_WHIP), item(FROZEN_ABYSSAL_WHIP)), item(CAPE_OF_LEGENDS), item(SPINED_CHAPS)),

    EMOTE_18(ClueLevel.MEDIUM, emotes(Emote.BECKON, Emote.BOW), false,
            "Beckon in the Digsite, near the eastern winch. Bow before you talk to me. Equip a green gnome hat, snakeskin boots and an iron pickaxe.",
            get(new int[][]{
                    { 3367, 3431 },
                    { 3367, 3423 },
                    { 3373, 3423 },
                    { 3373, 3431 }
            }), item(GREEN_HAT), item(SNAKESKIN_BOOTS), item(IRON_PICKAXE)),

    EMOTE_19(ClueLevel.MEDIUM, emotes(Emote.BECKON, Emote.CLAP), false,
            "Beckon in Tai Bwo Wannai. Clap before you talk to me. Equip green dragonhide chaps, a ring of dueling and a mithril medium helmet.",
            get(new int[][]{
                    { 2803, 3078 },
                    { 2801, 3078 },
                    { 2799, 3076 },
                    { 2797, 3076 },
                    { 2797, 3078 },
                    { 2796, 3079 },
                    { 2793, 3079 },
                    { 2792, 3078 },
                    { 2792, 3076 },
                    { 2790, 3076 },
                    { 2787, 3078 },
                    { 2785, 3078 },
                    { 2784, 3077 },
                    { 2784, 3075 },
                    { 2782, 3075 },
                    { 2781, 3074 },
                    { 2780, 3074 },
                    { 2779, 3073 },
                    { 2778, 3073 },
                    { 2777, 3072 },
                    { 2776, 3072 },
                    { 2774, 3070 },
                    { 2774, 3069 },
                    { 2772, 3067 },
                    { 2772, 3065 },
                    { 2773, 3064 },
                    { 2773, 3062 },
                    { 2774, 3061 },
                    { 2775, 3061 },
                    { 2778, 3058 },
                    { 2778, 3056 },
                    { 2779, 3055 },
                    { 2782, 3055 },
                    { 2783, 3056 },
                    { 2786, 3053 },
                    { 2788, 3053 },
                    { 2789, 3052 },
                    { 2792, 3052 },
                    { 2793, 3053 },
                    { 2795, 3053 },
                    { 2798, 3056 },
                    { 2799, 3055 },
                    { 2802, 3055 },
                    { 2803, 3056 },
                    { 2803, 3057 },
                    { 2805, 3057 },
                    { 2806, 3058 },
                    { 2807, 3058 },
                    { 2808, 3059 },
                    { 2809, 3059 },
                    { 2813, 3063 },
                    { 2814, 3063 },
                    { 2816, 3065 },
                    { 2816, 3069 },
                    { 2814, 3071 },
                    { 2814, 3072 },
                    { 2813, 3073 },
                    { 2812, 3073 },
                    { 2811, 3074 },
                    { 2810, 3074 },
                    { 2809, 3075 },
                    { 2806, 3075 }
            }), item(GREEN_DHIDE_CHAPS), any("Ring of dueling", item(RING_OF_DUELING1), item(RING_OF_DUELING2), item(RING_OF_DUELING3), item(RING_OF_DUELING4), item(RING_OF_DUELING5), item(RING_OF_DUELING6), item(RING_OF_DUELING7), item(RING_OF_DUELING8)), item(MITHRIL_MED_HELM)),

    EMOTE_20(ClueLevel.MEDIUM, emotes(Emote.BECKON, Emote.ANGRY), false,
            "Beckon in the combat ring of Shayzien. Show your anger before you talk to me. Equip an adamant platebody, adamant full helm and adamant platelegs.",
            get(new int[][]{
                    { 1541, 3617 },
                    { 1538, 3621 },
                    { 1538, 3626 },
                    { 1541, 3628 },
                    { 1547, 3628 },
                    { 1549, 3625 },
                    { 1549, 3619 },
                    { 1546, 3617 }
            }), item(ADAMANT_PLATELEGS), item(ADAMANT_PLATEBODY), item(ADAMANT_FULL_HELM)),

    EMOTE_21(ClueLevel.MASTER, emotes(Emote.BOW), true,
            "Bow in the Iorwerth Camp. Beware of double agents! Equip a charged crystal bow.",
            get(new int[][]{
                    { 2198, 3261 },
                    { 2189, 3252 },
                    { 2189, 3248 },
                    { 2192, 3246 },
                    { 2206, 3246 },
                    { 2213, 3261 }
            }, 0), any("Imbued crystal bow", item(NEW_CRYSTAL_BOW_I), item(CRYSTAL_BOW_FULL_I), item(CRYSTAL_BOW_910_I), item(CRYSTAL_BOW_810_I), item(CRYSTAL_BOW_710_I), item(CRYSTAL_BOW_610_I),
            item(CRYSTAL_BOW_510_I), item(CRYSTAL_BOW_410_I), item(CRYSTAL_BOW_310_I), item(CRYSTAL_BOW_210_I), item(CRYSTAL_BOW_110_I),
            item(NEW_CRYSTAL_BOW), item(CRYSTAL_BOW_FULL), item(CRYSTAL_BOW_910), item(CRYSTAL_BOW_810), item(CRYSTAL_BOW_710), item(CRYSTAL_BOW_610),
            item(CRYSTAL_BOW_510), item(CRYSTAL_BOW_410), item(CRYSTAL_BOW_310), item(CRYSTAL_BOW_210), item(CRYSTAL_BOW_110),
            item(BOW_OF_FAERDHINEN), item(BOW_OF_FAERDHINEN_C), item(BOW_OF_FAERDHINEN_C_25884), item(BOW_OF_FAERDHINEN_C_25886), item(BOW_OF_FAERDHINEN_C_25888),
            item(BOW_OF_FAERDHINEN_C_25890), item(BOW_OF_FAERDHINEN_C_25892), item(BOW_OF_FAERDHINEN_C_25894), item(BOW_OF_FAERDHINEN_C_25896)
    )),

    EMOTE_22(ClueLevel.EASY, emotes(Emote.BOW), false,
            "Bow outside the entrance to the Legends' Guild. Equip iron platelegs, an emerald amulet and an oak longbow.",
            get(new int[][]{
                    { 2724, 3350 },
                    { 2724, 3345 },
                    { 2734, 3345 },
                    { 2734, 3350 }
            }), item(IRON_PLATELEGS), item(OAK_LONGBOW), item(EMERALD_AMULET)),

    EMOTE_23(ClueLevel.ELITE, emotes(Emote.BOW), false,
            "Bow on the ground floor of the Legend's guild. Equip Legend's cape, a dragon battleaxe and an amulet of glory.",
            get(new int[][]{
                    { 2724, 3383 },
                    { 2724, 3381 },
                    { 2722, 3381 },
                    { 2722, 3376 },
                    { 2723, 3376 },
                    { 2723, 3373 },
                    { 2728, 3373 },
                    { 2728, 3374 },
                    { 2734, 3374 },
                    { 2734, 3375 },
                    { 2736, 3375 },
                    { 2736, 3382 },
                    { 2734, 3382 },
                    { 2734, 3383 }
            }, 0), item(CAPE_OF_LEGENDS), item(DRAGON_BATTLEAXE), any("Any amulet of glory", item(AMULET_OF_GLORY), item(AMULET_OF_GLORY1), item(AMULET_OF_GLORY2), item(AMULET_OF_GLORY3),
            item(AMULET_OF_GLORY4), item(AMULET_OF_GLORY5), item(AMULET_OF_GLORY6))),

    EMOTE_24(ClueLevel.EASY, emotes(Emote.BOW), false,
            "Bow in the ticket office of the Duel Arena. Equip an iron chain body, leather chaps and coif.",
            get(new int[][]{
                    { 3311, 3245 },
                    { 3311, 3240 },
                    { 3317, 3240 },
                    { 3317, 3245 }
            }), item(IRON_CHAINBODY), item(LEATHER_CHAPS), item(COIF)),

    EMOTE_25(ClueLevel.HARD, emotes(Emote.BOW), true,
            "Bow at the top of the lighthouse. Beware of double agents! Equip a blue dragonhide body, blue dragonhide vambraces and no jewelry.",
            get(new int[][]{
                    { 2507, 3646 },
                    { 2504, 3643 },
                    { 2504, 3639 },
                    { 2506, 3636 },
                    { 2511, 3636 },
                    { 2514, 3639 },
                    { 2514, 3643 },
                    { 2511, 3646 }
            }, 2), item(BLUE_DHIDE_BODY), item(BLUE_DHIDE_VAMB), emptySlot("No jewelry", EquipmentSlot.AMULET, EquipmentSlot.RING)),

    EMOTE_26(ClueLevel.HARD, emotes(Emote.BLOW_KISS), true,
            "Blow a kiss between the tables in Shilo Village bank. Beware of double agents! Equip a blue mystic hat, bone spear and rune platebody.",
            get(new int[][]{
                    { 2851, 2953 },
                    { 2851, 2955 },
                    { 2854, 2955 },
                    { 2854, 2953 }
            }, 0), item(MYSTIC_HAT), item(BONE_SPEAR), item(RUNE_PLATEBODY)),

    EMOTE_27(ClueLevel.ELITE, emotes(Emote.BLOW_KISS), false,
            "Blow a kiss in the heart of the lava maze. Equip black dragonhide chaps, a spotted cape and a rolling pin.",
            get(new int[][]{
                    { 3065, 3864 },
                    { 3064, 3863 },
                    { 3064, 3858 },
                    { 3062, 3854 },
                    { 3064, 3852 },
                    { 3065, 3852 },
                    { 3072, 3854 },
                    { 3072, 3863 },
                    { 3071, 3864 }
            }, 0), item(BLACK_DHIDE_CHAPS), any("Spotted cape", item(SPOTTED_CAPE), item(SPOTTED_CAPE_10073)), item(ROLLING_PIN)),

    EMOTE_28(ClueLevel.MASTER, emotes(Emote.BLOW_KISS), true,
            "Blow a kiss outside K'ril Tsutsaroth's chamber. Beware of double agents! Equip a zamorak full helm and the shadow sword.",
            get(new int[][]{
                    { 2924, 5337 },
                    { 2927, 5337 },
                    { 2927, 5332 },
                    { 2924, 5332 }
            }, 2), item(ZAMORAK_FULL_HELM), item(SHADOW_SWORD)),

    EMOTE_29(ClueLevel.EASY, emotes(Emote.CHEER), false,
            "Cheer at the Druids' Circle. Equip a blue wizard hat, a bronze two-handed sword and HAM boots.",
            get(new int[][]{
                    { 2927, 3490 },
                    { 2925, 3490 },
                    { 2921, 3488 },
                    { 2920, 3486 },
                    { 2920, 3480 },
                    { 2921, 3478 },
                    { 2925, 3477 },
                    { 2927, 3477 },
                    { 2931, 3478 },
                    { 2932, 3480 },
                    { 2933, 3484 },
                    { 2931, 3488 }
            }, 0), item(BLUE_WIZARD_HAT), item(BRONZE_2H_SWORD), item(HAM_BOOTS)),

    EMOTE_30(ClueLevel.MEDIUM, emotes(Emote.CHEER, Emote.DANCE), false,
            "Cheer in the Edgeville general store. Dance before you talk to me. Equip a brown apron, leather boots and leather gloves.",
            get(new int[][]{
                    {3080, 3510},
                    {3080, 3505},
                    {3085, 3505},
                    {3085, 3510}
            }), item(BROWN_APRON), item(LEATHER_BOOTS), item(LEATHER_GLOVES)),

    //TODO: Grant access to the pen through the shortcut.
    EMOTE_31(ClueLevel.MEDIUM, emotes(Emote.CHEER, Emote.ANGRY), false,
            "Cheer in the Ogre Pen in the Training Camp. Show you are angry before you talk to me. Equip a green dragonhide body and chaps and a steel square shield.",
            get(new int[][]{
                    { 2523, 3378 },
                    { 2523, 3373 },
                    { 2534, 3373 },
                    { 2534, 3378 }
            }), item(GREEN_DHIDE_BODY), item(GREEN_DHIDE_CHAPS), item(STEEL_SQ_SHIELD)),

    EMOTE_32(ClueLevel.MASTER, emotes(Emote.CHEER), true,
            "Cheer in the Entrana church. Beware of double agents! Equip a full set of black dragonhide armour.",
            get(new int[][]{
                    { 2854, 3356 },
                    { 2849, 3356 },
                    { 2849, 3353 },
                    { 2841, 3353 },
                    { 2841, 3345 },
                    { 2849, 3345 },
                    { 2849, 3342 },
                    { 2854, 3342 },
                    { 2854, 3346 },
                    { 2858, 3346 },
                    { 2858, 3352 },
                    { 2854, 3352 }
            }), item(BLACK_DHIDE_VAMB), item(BLACK_DHIDE_CHAPS), item(BLACK_DHIDE_BODY)),

    EMOTE_33(ClueLevel.EASY, emotes(Emote.CHEER), false,
            "Cheer for the monks at Port Sarim. Equip a coif, steel plateskirt and a sapphire necklace.",
            get(new int[][]{
                    { 3040, 3238 },
                    { 3040, 3234 },
                    { 3053, 3234 },
                    { 3053, 3238 }
            }, 0), item(COIF), item(STEEL_PLATESKIRT), item(SAPPHIRE_NECKLACE)),

    EMOTE_34(ClueLevel.EASY, emotes(Emote.CLAP), false,
            "Clap in the main exam room in the Exam Centre. Equip a white apron, green gnome boots and leather gloves.",
            get(new int[][]{
                    { 3357, 3349 },
                    { 3357, 3332 },
                    { 3368, 3332 },
                    { 3368, 3349 }
            }), item(WHITE_APRON), item(GREEN_BOOTS), item(LEATHER_GLOVES)),

    EMOTE_35(ClueLevel.EASY, emotes(Emote.CLAP), false,
            "Clap on the causeway to the Wizards' Tower. Equip an iron medium helmet, emerald ring and a white apron.",
            get(new int[][]{
                    { 3112, 3208 },
                    { 3112, 3173 },
                    { 3116, 3173 },
                    { 3116, 3177 },
                    { 3117, 3178 },
                    { 3117, 3181 },
                    { 3116, 3182 },
                    { 3116, 3194 },
                    { 3117, 3195 },
                    { 3117, 3198 },
                    { 3116, 3199 },
                    { 3116, 3208 }
            }), item(IRON_MED_HELM), item(EMERALD_RING), item(WHITE_APRON)),

    EMOTE_36(ClueLevel.EASY, emotes(Emote.CLAP), false,
            "Clap on the top level of the mill, north of East Ardougne. Equip a blue gnome robe top, HAM robe bottom and an unenchanted tiara.",
            get(new int[][]{
                    { 2632, 3389 },
                    { 2630, 3387 },
                    { 2630, 3385 },
                    { 2632, 3383 },
                    { 2634, 3383 },
                    { 2636, 3385 },
                    { 2636, 3387 },
                    { 2634, 3389 }
            }, 2), item(BLUE_ROBE_TOP), item(HAM_ROBE), item(TIARA)),

    EMOTE_37(ClueLevel.MEDIUM, emotes(Emote.CLAP, Emote.SPIN), false,
            "Clap in Seers court house. Spin before you talk to me. Equip an adamant halberd, blue mystic robe bottom and a diamond ring.",
            get(new int[][]{
                    { 2732, 3472 },
                    { 2732, 3465 },
                    { 2740, 3465 },
                    { 2740, 3472 }
            }), item(ADAMANT_HALBERD), item(MYSTIC_ROBE_BOTTOM), item(DIAMOND_RING)),

    EMOTE_38(ClueLevel.MASTER, emotes(Emote.CLAP), true,
            "Clap in the magic axe hut. Beware of double agents! Equip only some flared trousers.",
            get(new int[][]{
                    { 3188, 3963 },
                    { 3187, 3962 },
                    { 3187, 3959 },
                    { 3188, 3958 },
                    { 3194, 3958 },
                    { 3195, 3959 },
                    { 3195, 3962 },
                    { 3194, 3963 }
            }), item(FLARED_TROUSERS), emptySlot("Nothing else", EquipmentSlot.HELMET, EquipmentSlot.CAPE, EquipmentSlot.AMULET, EquipmentSlot.WEAPON, EquipmentSlot.PLATE, EquipmentSlot.SHIELD,
            EquipmentSlot.HANDS, EquipmentSlot.BOOTS, EquipmentSlot.RING, EquipmentSlot.AMMUNITION)),

    //TODO: Verify text due to runelite.
    EMOTE_39(ClueLevel.MEDIUM, emotes(Emote.CLAP, Emote.SPIN), false,
            "Clap your hands north of Mount Karuulm<br> Spin before you talk to me. Equip an adamant warhammer, a ring of life and a pair of mithril boots.",
            get(new int[][]{
                    { 1286, 3870 },
                    { 1288, 3833 },
                    { 1315, 3833 },
                    { 1325, 3838 },
                    { 1332, 3837 },
                    { 1330, 3871 }
            }), item(ADAMANT_WARHAMMER), item(RING_OF_LIFE), item(MITHRIL_BOOTS)),

    EMOTE_40(ClueLevel.MEDIUM, emotes(Emote.CRY, Emote.BOW), false,
            "Cry in the Catherby Ranging shop. Bow before you talk to me. Equip blue gnome boots, a hard leather body and an unblessed silver sickle.",
            get(new int[][]{
                    { 2821, 3446 },
                    { 2821, 3441 },
                    { 2826, 3441 },
                    { 2826, 3446 }
            }), item(BLUE_BOOTS), item(HARDLEATHER_BODY), item(SILVER_SICKLE)),

    EMOTE_41(ClueLevel.MEDIUM, emotes(Emote.CRY, Emote.LAUGH), false,
            "Cry on the shore of Catherby beach. Laugh before you talk to me, equip an adamant sq shield, a bone dagger and mithril platebody.",
            get(new int[][]{
                    { 2832, 3437 },
                    { 2832, 3431 },
                    { 2847, 3428 },
                    { 2854, 3422 },
                    { 2865, 3422 },
                    { 2867, 3425 },
                    { 2867, 3430 },
                    { 2860, 3437 }
            }), item(ADAMANT_SQ_SHIELD), item(BONE_DAGGER), item(MITHRIL_PLATEBODY)),

    EMOTE_42(ClueLevel.MEDIUM, emotes(Emote.CRY, Emote.NO), false,
            "Cry on top of the western tree in the Gnome Agility Arena. Indicate 'no' before you talk to me. Equip a steel kiteshield, ring of forging and green dragonhide chaps.",
            get(new int[][]{
                    { 2472, 3422 },
                    { 2478, 3422 },
                    { 2478, 3418 },
                    { 2472, 3418 }
            }, 2), item(STEEL_KITESHIELD), item(RING_OF_FORGING), item(GREEN_DHIDE_CHAPS)),

    EMOTE_43(ClueLevel.MASTER, emotes(Emote.CRY), true,
            "Cry in the TzHaar gem store. Beware of double agents! Equip a fire cape and TokTz-Xil-Ul.",
            get(new int[][]{
                    { 2463, 5147 },
                    { 2464, 5147 },
                    { 2467, 5150 },
                    { 2467, 5151 },
                    { 2466, 5151 },
                    { 2465, 5152 },
                    { 2464, 5152 },
                    { 2462, 5150 },
                    { 2462, 5148 }
            }), any("Fire cape", item(FIRE_CAPE), item(FIRE_MAX_CAPE)), item(TOKTZXILUL)),

    EMOTE_44(ClueLevel.MEDIUM, emotes(Emote.CRY, Emote.JUMP_FOR_JOY), false,
            "Cry in the Draynor Village jail. Jump for joy before you talk to me. Equip an adamant sword, a sapphire amulet and an adamant plateskirt.",
            get(new int[][]{
                    { 3121, 3247 },
                    { 3121, 3240 },
                    { 3131, 3240 },
                    { 3131, 3247 }
            }), item(ADAMANT_SWORD), item(SAPPHIRE_AMULET), item(ADAMANT_PLATESKIRT)),

    EMOTE_45(ClueLevel.EASY, emotes(Emote.DANCE), false,
            "Dance at the crossroads north of Draynor. Equip an iron chain body, a sapphire ring and a longbow.",
            get(new int[][]{
                    { 3109, 3297 },
                    { 3108, 3296 },
                    { 3108, 3294 },
                    { 3109, 3293 },
                    { 3111, 3293 },
                    { 3112, 3294 },
                    { 3112, 3296 },
                    { 3111, 3297 }
            }), item(IRON_CHAINBODY), item(SAPPHIRE_RING), item(LONGBOW)),

    EMOTE_46(ClueLevel.EASY, emotes(Emote.DANCE), false,
            "Dance in the Party Room. Equip a steel full helmet, steel platebody and an iron plateskirt.",
            get(new int[][]{
                    { 3037, 3386 },
                    { 3036, 3385 },
                    { 3036, 3382 },
                    { 3037, 3381 },
                    { 3037, 3376 },
                    { 3036, 3375 },
                    { 3036, 3372 },
                    { 3037, 3371 },
                    { 3040, 3371 },
                    { 3041, 3372 },
                    { 3051, 3372 },
                    { 3052, 3371 },
                    { 3055, 3371 },
                    { 3056, 3372 },
                    { 3056, 3375 },
                    { 3055, 3376 },
                    { 3055, 3381 },
                    { 3056, 3382 },
                    { 3056, 3385 },
                    { 3055, 3386 },
                    { 3052, 3386 },
                    { 3051, 3385 },
                    { 3041, 3385 },
                    { 3040, 3386 }
            }), item(STEEL_FULL_HELM), item(STEEL_PLATEBODY), item(IRON_PLATESKIRT)),

    EMOTE_47(ClueLevel.EASY, emotes(Emote.DANCE), false,
            "Dance in the shack in Lumbridge Swamp. Equip a bronze dagger, iron full helmet and a gold ring.",
            get(new int[][]{
                    { 3202, 3171 },
                    { 3202, 3167 },
                    { 3206, 3167 },
                    { 3206, 3171 }
            }), item(BRONZE_DAGGER), item(IRON_FULL_HELM), item(GOLD_RING)),

    EMOTE_48(ClueLevel.MEDIUM, emotes(Emote.DANCE, Emote.BLOW_KISS), false,
            "Dance in the dark caves beneath Lumbridge Swamp. Blow a kiss before you talk to me. Equip an air staff, Bronze full helm and an amulet of power.",
            get(new int[][]{
                    { 3140, 9602 },
                    { 3140, 9533 },
                    { 3263, 9533 },
                    { 3263, 9602 }
            }), item(STAFF_OF_AIR), item(BRONZE_FULL_HELM), item(AMULET_OF_POWER)),

    EMOTE_49(ClueLevel.HARD, emotes(Emote.DANCE), true,
            "Dance at the cat-doored pyramid in Sophanem. Beware of double agents! Equip a ring of life, an uncharged amulet of glory and an adamant two-handed sword.",
            get(new int[][]{
                    { 3291, 2780 },
                    { 3291, 2785 },
                    { 3299, 2785 },
                    { 3299, 2780 }
            }), item(RING_OF_LIFE), item(AMULET_OF_GLORY), item(ADAMANT_2H_SWORD)),

    EMOTE_50(ClueLevel.MEDIUM, emotes(Emote.DANCE, Emote.BOW), false,
            "Dance in the centre of Canifis. Bow before you talk to me. Equip a green gnome robe top, mithril plate legs and an iron two-handed sword.",
            get(new int[][]{
                    { 3487, 3494 },
                    { 3487, 3481 },
                    { 3505, 3481 },
                    { 3505, 3494 }
            }), item(GREEN_ROBE_TOP), item(MITHRIL_PLATELEGS), item(IRON_2H_SWORD)),

    EMOTE_51(ClueLevel.MASTER, emotes(Emote.DANCE), true,
            "Dance in the King Black Dragon's lair. Beware of double agents! Equip a black dragonhide body, black dragonhide vambs and a black dragon mask.",
            get(new int[][]{
                    { 2240, 4736 },
                    { 2240, 4672 },
                    { 2304, 4672 },
                    { 2304, 4736 }
            }), item(BLACK_DHIDE_BODY), item(BLACK_DHIDE_VAMB), item(BLACK_DRAGON_MASK)),

    EMOTE_52(ClueLevel.EASY, emotes(Emote.DANCE), false,
            "Dance at the entrance to the Grand Exchange. Equip a pink skirt, pink robe top and a body tiara.",
            get(new int[][]{
                    { 3162, 3473 },
                    { 3162, 3463 },
                    { 3167, 3463 },
                    { 3167, 3473 }
            }), item(PINK_SKIRT), item(PINK_ROBE_TOP), item(BODY_TIARA)),

    EMOTE_53(ClueLevel.MASTER, emotes(Emote.GOBLIN_SALUTE), true,
            "Goblin Salute in the Goblin Village. Beware of double agents! Equip a bandos godsword, a bandos cloak and a bandos platebody.",
            get(new int[][]{
                    { 2956, 3518 },
                    { 2945, 3512 },
                    { 2947, 3493 },
                    { 2964, 3493 },
                    { 2968, 3517 }
            }), item(BANDOS_PLATEBODY), item(BANDOS_CLOAK), item(BANDOS_GODSWORD)),

    EMOTE_54(ClueLevel.EASY, emotes(Emote.HEADBANG), false,
            "Headbang in the mine north of Al Kharid. Equip a desert shirt, leather gloves and leather boots.",
            get(new int[][]{
                    { 3300, 3320 },
                    { 3293, 3311 },
                    { 3295, 3308 },
                    { 3290, 3299 },
                    { 3294, 3292 },
                    { 3290, 3282 },
                    { 3293, 3280 },
                    { 3291, 3275 },
                    { 3307, 3275 },
                    { 3306, 3278 },
                    { 3308, 3282 },
                    { 3303, 3287 },
                    { 3307, 3293 },
                    { 3304, 3298 },
                    { 3308, 3304 },
                    { 3305, 3308 },
                    { 3304, 3315 }
            }), item(DESERT_SHIRT), item(LEATHER_GLOVES), item(LEATHER_BOOTS)),

    EMOTE_55(ClueLevel.HARD, emotes(Emote.HEADBANG), true,
            "Headbang at the exam center. Beware of double agents! Equip a mystic fire staff, a diamond bracelet and rune boots.",
            get(new int[][]{
                    { 3357, 3349 },
                    { 3368, 3349 },
                    { 3368, 3332 },
                    { 3357, 3332 }
            }), item(MYSTIC_FIRE_STAFF), item(DIAMOND_BRACELET), item(RUNE_BOOTS)),

    EMOTE_56(ClueLevel.ELITE, emotes(Emote.HEADBANG), false,
            "Headbang at the top of Slayer Tower. Equip a seercull, a combat bracelet and helm of Neitiznot.",
            get(new int[][] { { 3405, 3539 }, { 3405, 3535 }, { 3409, 3531 }, { 3413, 3531 }, { 3416, 3534 }, { 3419, 3534 },
                    { 3420, 3533 }, { 3424, 3533 }, { 3426, 3535 }, { 3426, 3536 }, { 3432, 3536 }, { 3432, 3535 }, { 3434, 3533 },
                    { 3438, 3533 }, { 3439, 3534 }, { 3442, 3534 }, { 3445, 3531 }, { 3449, 3531 }, { 3453, 3535 }, { 3453, 3539 },
                    { 3449, 3543 }, { 3449, 3547 }, { 3450, 3548 }, { 3450, 3552 }, { 3449, 3553 }, { 3449, 3558 }, { 3450, 3559 },
                    { 3450, 3563 }, { 3449, 3564 }, { 3449, 3568 }, { 3453, 3572 }, { 3453, 3576 }, { 3449, 3580 }, { 3445, 3580 },
                    { 3442, 3577 }, { 3439, 3577 }, { 3438, 3578 }, { 3434, 3578 }, { 3433, 3577 }, { 3425, 3577 }, { 3424, 3578 },
                    { 3420, 3578 }, { 3419, 3577 }, { 3416, 3577 }, { 3413, 3580 }, { 3409, 3580 }, { 3405, 3576 }, { 3405, 3572 },
                    { 3409, 3568 }, { 3409, 3564 }, { 3408, 3563 }, { 3408, 3559 }, { 3409, 3558 }, { 3409, 3553 }, { 3408, 3552 },
                    { 3408, 3548 }, { 3409, 3547 }, { 3409, 3543 } }, 2), item(SEERCULL), any("Combat bracelet", range(COMBAT_BRACELET4, COMBAT_BRACELET), item(COMBAT_BRACELET5), item(COMBAT_BRACELET6)), item(HELM_OF_NEITIZNOT)),

    EMOTE_57(ClueLevel.EASY, emotes(Emote.JIG), false,
            "Dance a jig by the entrance to the Fishing Guild. Equip an emerald ring, a sapphire amulet, and a bronze chain body.",
            get(new int[][]{
                    { 2607, 3394 },
                    { 2607, 3390 },
                    { 2615, 3390 },
                    { 2615, 3394 }
            }), item(EMERALD_RING), item(SAPPHIRE_AMULET), item(BRONZE_CHAINBODY)),

    /*EMOTE_58(ClueLevel.MEDIUM, emotes(Emote.JIG, Emote.BOW), false,
            "Dance a jig under Shantay's Awning. Bow before you talk to me. Equip a pointed blue snail helmet, an air staff and a bronze square shield.",
            get(new int[][]{
                    { 3301, 3125 },
                    { 3301, 3121 },
                    { 3306, 3121 },
                    { 3306, 3125 }
            }), any("Bruise blue snelm (pointed)", item(BRUISE_BLUE_SNELM_3343)), item(STAFF_OF_AIR), item(BRONZE_SQ_SHIELD)),*/

    EMOTE_59(ClueLevel.EASY, emotes(Emote.JIG), false,
            "Do a jig in Varrock's rune store. Equip an air tiara and a staff of water.",
            get(new int[][]{
                    { 3252, 3405 },
                    { 3252, 3403 },
                    { 3250, 3403 },
                    { 3250, 3401 },
                    { 3252, 3399 },
                    { 3254, 3399 },
                    { 3256, 3401 },
                    { 3256, 3403 },
                    { 3254, 3405 }
            }), item(AIR_TIARA), item(STAFF_OF_WATER)),

    EMOTE_60(ClueLevel.EASY, emotes(Emote.JUMP_FOR_JOY), false,
            "Jump for joy at the beehives. Equip a desert shirt, green gnome robe bottoms and a steel axe.",
            get(new int[][]{
                    { 2752, 3452 },
                    { 2752, 3437 },
                    { 2755, 3437 },
                    { 2756, 3436 },
                    { 2759, 3436 },
                    { 2760, 3437 },
                    { 2765, 3437 },
                    { 2767, 3439 },
                    { 2767, 3449 },
                    { 2764, 3452 },
                    { 2762, 3452 },
                    { 2761, 3451 },
                    { 2756, 3451 },
                    { 2755, 3452 }
            }), item(DESERT_SHIRT), item(GREEN_ROBE_BOTTOMS), item(STEEL_AXE)),

    EMOTE_61(ClueLevel.MEDIUM, emotes(Emote.JUMP_FOR_JOY, Emote.JIG), false,
            "Jump for joy in Yanille bank. Dance a jig before you talk to me. Equip a brown apron, adamantite medium helmet and snakeskin chaps.",
            get(new int[][]{
                    { 2609, 3098 },
                    { 2609, 3088 },
                    { 2617, 3088 },
                    { 2617, 3098 }
            }), item(BROWN_APRON), item(ADAMANT_MED_HELM), item(SNAKESKIN_CHAPS)),

    EMOTE_62(ClueLevel.MEDIUM, emotes(Emote.JUMP_FOR_JOY, Emote.SHRUG), false,
            "Jump for joy in the TzHaar sword shop. Shrug before you talk to me. Equip a Steel longsword, Blue D'hide body and blue mystic gloves.",
            get(new int[][]{
                    { 2481, 5148 },
                    { 2480, 5149 },
                    { 2478, 5149 },
                    { 2476, 5148 },
                    { 2475, 5147 },
                    { 2475, 5146 },
                    { 2476, 5144 },
                    { 2480, 5144 },
                    { 2481, 5145 },
                    { 2481, 5146 },
                    { 2480, 5147 }
            }), item(STEEL_LONGSWORD), item(BLUE_DHIDE_BODY), item(MYSTIC_GLOVES)),

    EMOTE_63(ClueLevel.ELITE, emotes(Emote.JUMP_FOR_JOY), false,
            "Jump for joy in the Ancient Cavern. Equip a granite shield, splitbark body and any rune heraldic helm.",
            get(new int[][]{{1590, 5390}, {1590, 5294}, {1807, 5294}, {1807, 5390}}), item(GRANITE_SHIELD), item(SPLITBARK_BODY), range("Any rune heraldic helm", RUNE_HELM_H1, RUNE_HELM_H5)),

    EMOTE_64(ClueLevel.ELITE, emotes(Emote.JUMP_FOR_JOY), false,
            "Jump for joy at the Neitiznot rune rock. Equip Rune boots, a proselyte hauberk and a dragonstone ring.",
            get(new int[][]{
                    { 2375, 3859 },
                    { 2367, 3850 },
                    { 2373, 3844 },
                    { 2377, 3847 },
                    { 2383, 3847 },
                    { 2377, 3858 }
            }), item(RUNE_BOOTS), item(PROSELYTE_HAUBERK), item(DRAGONSTONE_RING)),

    EMOTE_65(ClueLevel.MASTER, emotes(Emote.JUMP_FOR_JOY), true,
            "Jump for joy in the centre of Zul-Andra. Beware of double agents! Equip a dragon 2h sword, bandos boots and an obsidian cape.",
            get(new int[][]{
                    { 2192, 3070 },
                    { 2192, 3041 },
                    { 2218, 3041 },
                    { 2218, 3070 }
            }), item(DRAGON_2H_SWORD), item(BANDOS_BOOTS), item(OBSIDIAN_CAPE)),

    EMOTE_66(ClueLevel.ELITE, emotes(Emote.LAUGH), false,
            "Laugh by the fountain of heroes. Equip splitbark legs, dragon boots and a Rune longsword.",
            get(new int[][]{
                    { 2913, 9899 },
                    { 2913, 9888 },
                    { 2926, 9888 },
                    { 2926, 9899 }
            }), item(SPLITBARK_LEGS), item(DRAGON_BOOTS), item(RUNE_LONGSWORD)),

    EMOTE_67(ClueLevel.HARD, emotes(Emote.LAUGH), true,
            "Laugh in Jokul's tent in the Mountain Camp. Beware of double agents! Equip a rune full helmet, blue dragonhide chaps and a fire battlestaff.",
            get(new int[][]{
                    { 2811, 3683 },
                    { 2811, 3679 },
                    { 2814, 3679 },
                    { 2814, 3683 }
            }), item(RUNE_FULL_HELM), item(BLUE_DHIDE_CHAPS), item(FIRE_BATTLESTAFF)),

    EMOTE_68(ClueLevel.EASY, emotes(Emote.LAUGH), false,
            "Laugh at the crossroads south of the Sinclair Mansion. Equip a cowl, a blue wizard robe top and an iron scimitar.",
            get(new int[][]{
                    { 2733, 3543 },
                    { 2733, 3534 },
                    { 2745, 3534 },
                    { 2745, 3543 }
            }), item(LEATHER_COWL), item(BLUE_WIZARD_ROBE), item(IRON_SCIMITAR)),

    EMOTE_69(ClueLevel.ELITE, emotes(Emote.LAUGH),  false,
            "Laugh in front of the gem store in Ardougne market. Equip a Castlewars bracelet, a dragonstone amulet and a ring of forging.",
            get(new int[][]{
                    { 2665, 3307 },
                    { 2665, 3298 },
                    { 2672, 3298 },
                    { 2672, 3307 }
            }), any("Castle wars bracelet", range(CASTLE_WARS_BRACELET3, CASTLE_WARS_BRACELET1)), item(DRAGONSTONE_AMULET), item(RING_OF_FORGING)),

    EMOTE_70(ClueLevel.EASY, emotes(Emote.PANIC), false,
            "Panic in the Limestone Mine. Equip bronze platelegs, a steel pickaxe and a steel medium helmet.",
            get(new int[][]{
                    { 3367, 3495 },
                    { 3378, 3495 },
                    { 3378, 3507 },
                    { 3367, 3507 }
            }), item(BRONZE_PLATELEGS), item(STEEL_PICKAXE), item(STEEL_MED_HELM)),

    EMOTE_71(ClueLevel.MEDIUM, emotes(Emote.PANIC, Emote.WAVE), false,
            "Panic by the mausoleum in Morytania. Wave before you speak to me. Equip a mithril plate skirt, a maple longbow and no boots.",
            get(new int[][]{
                    { 3490, 3583 },
                    { 3490, 3565 },
                    { 3515, 3565 },
                    { 3515, 3583 }
            }), item(MITHRIL_PLATESKIRT), item(MAPLE_LONGBOW), emptySlot("No boots", EquipmentSlot.BOOTS)),

    EMOTE_72(ClueLevel.HARD, emotes(Emote.PANIC), true,
            "Panic on the Wilderness volcano bridge. Beware of double agents! Equip any headband and crozier.",
            get(new int[][]{
                    { 3360, 3937 },
                    { 3360, 3935 },
                    { 3374, 3935 },
                    { 3374, 3937 }
            }), any("Any headband", range(RED_HEADBAND, BROWN_HEADBAND), range(WHITE_HEADBAND, GREEN_HEADBAND)), any("Any crozier", item(ANCIENT_CROZIER), item(ARMADYL_CROZIER),
            item(BANDOS_CROZIER), range(SARADOMIN_CROZIER, ZAMORAK_CROZIER))),

    EMOTE_73(ClueLevel.HARD, emotes(Emote.PANIC), true,
            "Panic by the pilot on White Wolf Mountain. Beware of double agents! Equip mithril platelegs, a ring of life and a rune axe.",
            get(new int[][]{
                    { 2843, 3500 },
                    { 2843, 3495 },
                    { 2849, 3492 },
                    { 2855, 3491 },
                    { 2859, 3491 },
                    { 2860, 3500 },
                    { 2854, 3507 },
                    { 2847, 3507 },
                    { 2843, 3502 }
            }), item(MITHRIL_PLATELEGS), item(RING_OF_LIFE), item(RUNE_AXE)),

    EMOTE_74(ClueLevel.MASTER, emotes(Emote.PANIC), true,
            "Panic by the big egg where no one dare goes and the ground is burnt. Beware of double agents! Equip a dragon med helm, a TokTz-Ket-Xil, a brine sabre, rune platebody and an uncharged " +
                    "amulet of glory.",
            get(new int[][]{
                    { 3220, 3831 },
                    { 3220, 3826 },
                    { 3225, 3826 },
                    { 3230, 3830 },
                    { 3230, 3836 },
                    { 3223, 3837 }
            }), item(DRAGON_MED_HELM), item(TOKTZKETXIL), item(BRINE_SABRE), item(RUNE_PLATEBODY), item(AMULET_OF_GLORY)),

    /*EMOTE_75(ClueLevel.ELITE, emotes(Emote.PANIC), false,
            "Panic at the area flowers meet snow. Equip Blue D'hide vambs, a dragon spear and a rune plateskirt.",
            get(new int[][]{
                    { 2777, 3788 },
                    { 2772, 3782 },
                    { 2778, 3777 },
                    { 2783, 3781 },
                    { 2781, 3787 }
            }), item(BLUE_DHIDE_VAMB), item(DRAGON_SPEAR), item(RUNE_PLATESKIRT), item(SLED_4084)),*/

    /*EMOTE_76(null, emotes(Emote.PUSH_UP), true,
            "Do a push up at the bank of the Warrior's guild. Beware of double agents! Equip a dragon battleaxe, a dragon defender and a slayer helm of any kind.",
            get(new int[][]{
                    { 2841, 3546 },
                    { 2841, 3540 },
                    { 2843, 3540 },
                    { 2843, 3537 },
                    { 2845, 3537 },
                    { 2846, 3536 },
                    { 2847, 3536 },
                    { 2848, 3537 },
                    { 2849, 3537 },
                    { 2849, 3546 }
            }), item(DRAGON_BATTLEAXE), item(DRAGON_DEFENDER), any("Any slayer helmet", item(SLAYER_HELMET), item(BLACK_SLAYER_HELMET), item(GREEN_SLAYER_HELMET), item(PURPLE_SLAYER_HELMET),
            item(RED_SLAYER_HELMET), item(TURQUOISE_SLAYER_HELMET), item(SLAYER_HELMET_I), item(BLACK_SLAYER_HELMET_I), item(GREEN_SLAYER_HELMET_I), item(PURPLE_SLAYER_HELMET_I), item(RED_SLAYER_HELMET_I), item(TURQUOISE_SLAYER_HELMET_I), item(HYDRA_SLAYER_HELMET), item(HYDRA_SLAYER_HELMET_I))),
*/
    EMOTE_77(ClueLevel.MASTER, emotes(Emote.RASPBERRY), true,
            "Blow a raspberry at the bank of the Warrior's guild. Beware of double agents! Equip a dragon battleaxe, a dragon defender and a slayer helm of any kind.",
            get(new int[][]{
                    { 2841, 3546 },
                    { 2841, 3540 },
                    { 2843, 3540 },
                    { 2843, 3537 },
                    { 2845, 3537 },
                    { 2846, 3536 },
                    { 2847, 3536 },
                    { 2848, 3537 },
                    { 2849, 3537 },
                    { 2849, 3546 }
            }), item(DRAGON_BATTLEAXE), item(DRAGON_DEFENDER), any("Any slayer helmet", item(SLAYER_HELMET), item(BLACK_SLAYER_HELMET), item(GREEN_SLAYER_HELMET), item(PURPLE_SLAYER_HELMET),
            item(RED_SLAYER_HELMET), item(TURQUOISE_SLAYER_HELMET), item(SLAYER_HELMET_I), item(BLACK_SLAYER_HELMET_I), item(GREEN_SLAYER_HELMET_I), item(PURPLE_SLAYER_HELMET_I), item(RED_SLAYER_HELMET_I), item(TURQUOISE_SLAYER_HELMET_I), item(HYDRA_SLAYER_HELMET), item(HYDRA_SLAYER_HELMET_I))),

    EMOTE_78(ClueLevel.EASY, emotes(Emote.RASPBERRY), false,
            "Blow a raspberry at the monkey cage in Ardougne Zoo. Equip a studded leather body, bronze platelegs and a normal staff with no orb.",
            get(new int[][]{
                    { 2599, 3284 },
                    { 2595, 3280 },
                    { 2595, 3276 },
                    { 2596, 3275 },
                    { 2598, 3271 },
                    { 2600, 3271 },
                    { 2604, 3274 },
                    { 2609, 3274 },
                    { 2609, 3284 }
            }), item(STUDDED_BODY), item(BRONZE_PLATELEGS), item(STAFF)),

    EMOTE_79(ClueLevel.EASY, emotes(Emote.RASPBERRY), false,
            "Blow raspberries outside the entrance to Keep Le Faye. Equip a coif, an iron platebody and leather gloves.",
            get(new int[][]{
                    { 2751, 3409 },
                    { 2751, 3394 },
                    { 2764, 3394 },
                    { 2764, 3409 }
            }), item(COIF), item(IRON_PLATEBODY), item(LEATHER_GLOVES)),

    EMOTE_80(ClueLevel.HARD, emotes(Emote.RASPBERRY), true,
            "Blow a raspberry in the Fishing Guild bank. Beware of double agents! Equip an elemental shield, blue dragonhide chaps and a rune warhammer.",
            get(new int[][]{
                    { 2584, 3423 },
                    { 2583, 3422 },
                    { 2583, 3419 },
                    { 2584, 3418 },
                    { 2587, 3418 },
                    { 2588, 3417 },
                    { 2588, 3414 },
                    { 2589, 3413 },
                    { 2593, 3413 },
                    { 2593, 3419 },
                    { 2592, 3420 },
                    { 2591, 3420 },
                    { 2590, 3421 },
                    { 2590, 3422 },
                    { 2589, 3423 }
            }), item(ELEMENTAL_SHIELD), item(BLUE_DHIDE_CHAPS), item(RUNE_WARHAMMER)),

    EMOTE_81(ClueLevel.HARD, emotes(Emote.SALUTE), true,
            "Salute in the banana plantation. Beware of double agents! Equip a diamond ring, amulet of power, and nothing on your chest and legs.",
            get(new int[][]{
                    { 2908, 3183 },
                    { 2908, 3180 },
                    { 2911, 3180 },
                    { 2912, 3179 },
                    { 2912, 3174 },
                    { 2911, 3173 },
                    { 2910, 3171 },
                    { 2907, 3168 },
                    { 2907, 3167 },
                    { 2905, 3165 },
                    { 2905, 3160 },
                    { 2909, 3156 },
                    { 2911, 3156 },
                    { 2913, 3154 },
                    { 2927, 3154 },
                    { 2928, 3153 },
                    { 2931, 3153 },
                    { 2932, 3152 },
                    { 2935, 3152 },
                    { 2935, 3158 },
                    { 2938, 3161 },
                    { 2938, 3183 }
            }), item(DIAMOND_RING), item(AMULET_OF_POWER), emptySlot("Nothing on chest & legs", EquipmentSlot.PLATE, EquipmentSlot.LEGS)),

    EMOTE_82(ClueLevel.ELITE, emotes(Emote.SALUTE), true,
            "Salute in the Warriors' guild bank. Equip only a black salamander.",
            get(new int[][]{
                    { 2841, 3546 },
                    { 2841, 3540 },
                    { 2843, 3540 },
                    { 2843, 3537 },
                    { 2845, 3537 },
                    { 2846, 3536 },
                    { 2847, 3536 },
                    { 2848, 3537 },
                    { 2849, 3537 },
                    { 2849, 3546 }
            }), item(BLACK_SALAMANDER), emptySlot("Nothing else", EquipmentSlot.HELMET, EquipmentSlot.CAPE, EquipmentSlot.AMULET, EquipmentSlot.PLATE, EquipmentSlot.SHIELD, EquipmentSlot.LEGS, EquipmentSlot.HANDS,
            EquipmentSlot.BOOTS, EquipmentSlot.RING, EquipmentSlot.AMMUNITION)),

    EMOTE_83(ClueLevel.HARD, emotes(Emote.SALUTE), true,
            "Salute in the centre of the mess hall. Beware of double agents! Equip a rune halberd rune platebody, and an amulet of strength.",
            get(new int[][]{
                    { 1643, 3616 },
                    { 1649, 3616 },
                    { 1649, 3646 },
                    { 1643, 3646 }
            }), item(RUNE_HALBERD), item(RUNE_PLATEBODY), item(AMULET_OF_STRENGTH)),

    EMOTE_84(ClueLevel.EASY, emotes(Emote.SHRUG), false,
            "Shrug in the mine near Rimmington. Equip a gold necklace, a gold ring and a bronze spear.",
            get(new int[][]{
                    { 2984, 3252 },
                    { 2973, 3252 },
                    { 2972, 3251 },
                    { 2971, 3251 },
                    { 2970, 3250 },
                    { 2970, 3249 },
                    { 2968, 3247 },
                    { 2967, 3247 },
                    { 2966, 3246 },
                    { 2966, 3245 },
                    { 2965, 3244 },
                    { 2965, 3243 },
                    { 2964, 3242 },
                    { 2964, 3237 },
                    { 2965, 3236 },
                    { 2965, 3235 },
                    { 2968, 3232 },
                    { 2969, 3232 },
                    { 2970, 3231 },
                    { 2970, 3230 },
                    { 2972, 3229 },
                    { 2974, 3229 },
                    { 2975, 3228 },
                    { 2976, 3228 },
                    { 2977, 3227 },
                    { 2984, 3227 },
                    { 2985, 3228 },
                    { 2986, 3228 },
                    { 2988, 3230 },
                    { 2988, 3231 },
                    { 2989, 3232 },
                    { 2989, 3233 },
                    { 2990, 3234 },
                    { 2990, 3237 },
                    { 2991, 3238 },
                    { 2991, 3245 },
                    { 2990, 3246 },
                    { 2989, 3246 },
                    { 2988, 3247 },
                    { 2988, 3248 },
                    { 2986, 3250 },
                    { 2985, 3250 },
                    { 2985, 3251 }
            }), item(GOLD_NECKLACE), item(GOLD_RING), item(BRONZE_SPEAR)),

    EMOTE_85(ClueLevel.MEDIUM, emotes(Emote.SHRUG, Emote.YAWN), false,
            "Shrug in Catherby bank. Yawn before you talk to me. Equip a maple longbow, green d'hide chaps and an iron med helm.",
            get(new int[][]{
                    { 2806, 3446 },
                    { 2806, 3438 },
                    { 2813, 3438 },
                    { 2813, 3446 }
            }), item(MAPLE_LONGBOW), item(GREEN_DHIDE_CHAPS), item(IRON_MED_HELM)),

    EMOTE_86(ClueLevel.HARD, emotes(Emote.SHRUG), true,
            "Shrug in the Zamorak temple found in the Eastern Wilderness. Beware of double agents! Equip rune platelegs, an iron platebody and blue dragonhide vambraces.",
            get(new int[][]{
                    { 3239, 3617 },
                    { 3239, 3616 },
                    { 3238, 3615 },
                    { 3237, 3615 },
                    { 3234, 3612 },
                    { 3234, 3606 },
                    { 3237, 3603 },
                    { 3243, 3603 },
                    { 3246, 3606 },
                    { 3246, 3612 },
                    { 3243, 3615 },
                    { 3242, 3615 },
                    { 3241, 3616 },
                    { 3241, 3617 }
            }), item(RUNE_PLATELEGS), item(IRON_PLATEBODY), item(BLUE_DHIDE_VAMB)),

    EMOTE_87(ClueLevel.ELITE, emotes(Emote.SHRUG), false,
            "Shrug in the Shayzien command tent. Equip a blue mystic robe bottom, a rune kiteshield and any bob shirt.",
            get(new int[][]{
                    { 1488, 3635 },
                    { 1488, 3636 },
                    { 1487, 3635 },
                    { 1487, 3634 }
            }), item(MYSTIC_ROBE_BOTTOM), item(RUNE_KITESHIELD), range("Any bob shirt", BOBS_RED_SHIRT, BOBS_PURPLE_SHIRT)),

    EMOTE_88(ClueLevel.MASTER, emotes(Emote.SLAP_HEAD), true,
            "Slap your head in the centre of the Kourend catacombs. Beware of double agents! Equip the arclight and the amulet of the damned.",
            get(new int[][]{
                    { 1665, 10054 },
                    { 1663, 10054 },
                    { 1662, 10053 },
                    { 1661, 10053 },
                    { 1659, 10051 },
                    { 1659, 10050 },
                    { 1658, 10049 },
                    { 1658, 10047 },
                    { 1659, 10046 },
                    { 1659, 10045 },
                    { 1661, 10043 },
                    { 1662, 10043 },
                    { 1663, 10042 },
                    { 1665, 10042 },
                    { 1666, 10043 },
                    { 1667, 10043 },
                    { 1669, 10045 },
                    { 1669, 10046 },
                    { 1670, 10047 },
                    { 1670, 10049 },
                    { 1669, 10050 },
                    { 1669, 10051 },
                    { 1667, 10053 },
                    { 1666, 10053 }
            }), item(ARCLIGHT), any("Amulet of the damned", item(AMULET_OF_THE_DAMNED), item(AMULET_OF_THE_DAMNED_FULL))),

    EMOTE_89(ClueLevel.EASY, emotes(Emote.SPIN), false,
            "Spin at the crossroads north of Rimmington. Equip a green gnome hat, cream gnome top and leather chaps.",
            get(new int[][]{
                    { 2977, 3282 },
                    { 2977, 3270 },
                    { 2988, 3270 },
                    { 2988, 3282 }
            }), item(GREEN_HAT), item(CREAM_ROBE_TOP), item(LEATHER_CHAPS)),

    EMOTE_90(ClueLevel.EASY, emotes(Emote.SPIN), false,
            "Spin in Draynor Manor by the fountain. Equip an iron platebody, studded leather chaps and a bronze full helmet.",
            get(new int[][]{
                    { 3085, 3340 },
                    { 3094, 3340 },
                    { 3094, 3332 },
                    { 3093, 3331 },
                    { 3086, 3331 },
                    { 3084, 3333 },
                    { 3084, 3337 },
                    { 3085, 3338 }
            }), item(IRON_PLATEBODY), item(STUDDED_CHAPS), item(BRONZE_FULL_HELM)),

    EMOTE_91(ClueLevel.MASTER, emotes(Emote.SPIN), true,
            "Spin in front of the Soul altar. Beware of double agents! Equip a dragon pickaxe, helm of neitiznot and a pair of rune boots.",
            get(new int[][]{
                    { 1808, 3876 },
                    { 1798, 3861 },
                    { 1800, 3841 },
                    { 1811, 3835 },
                    { 1827, 3841 },
                    { 1843, 3863 },
                    { 1845, 3877 }
            }), any("Dragon pickaxe", item(DRAGON_PICKAXE), item(DRAGON_PICKAXE_12797), item(INFERNAL_PICKAXE), item(INFERNAL_PICKAXE_UNCHARGED)), item(HELM_OF_NEITIZNOT), item(RUNE_BOOTS)),

    EMOTE_92(ClueLevel.EASY, emotes(Emote.SPIN), false,
            "Spin in the Varrock Castle courtyard. Equip a black axe, a coif and a ruby ring.",
            get(new int[][]{
                    { 3207, 3471 },
                    { 3205, 3469 },
                    { 3202, 3469 },
                    { 3202, 3461 },
                    { 3204, 3459 },
                    { 3222, 3459 },
                    { 3224, 3461 },
                    { 3224, 3469 },
                    { 3221, 3469 },
                    { 3219, 3471 }
            }), item(BLACK_AXE), item(COIF), item(RUBY_RING)),
    EMOTE_93(ClueLevel.ELITE, emotes(Emote.SPIN), false,
            "Spin in West Ardougne Church. Equip a dragon spear and red dragonhide chaps.",
            get(new int[][]{
                    { 2527, 3296 },
                    { 2527, 3289 },
                    { 2524, 3289 },
                    { 2524, 3285 },
                    { 2533, 3285 },
                    { 2533, 3296 }
            }), item(DRAGON_SPEAR), item(RED_DHIDE_CHAPS)),

    EMOTE_94(ClueLevel.MEDIUM, emotes(Emote.SPIN, Emote.SALUTE), false,
            "Spin on the bridge by the Barbarian Village. Salute before you talk to me. Equip purple gloves, a steel kiteshield and a mithril full helmet.",
            get(new int[][]{
                    { 3103, 3422 },
                    { 3103, 3420 },
                    { 3108, 3420 },
                    { 3108, 3422 }
            }), item(PURPLE_GLOVES), item(STEEL_KITESHIELD), item(MITHRIL_FULL_HELM)),

    EMOTE_95(ClueLevel.MASTER, emotes(Emote.STAMP), true,
            "Stamp in the Enchanted valley west of the waterfall. Beware of double agents! Equip a dragon axe.",
            get(new int[][]{
                    { 3021, 4524 },
                    { 3021, 4515 },
                    { 3038, 4515 },
                    { 3038, 4524 }
            }), item(DRAGON_AXE)),

    EMOTE_96(ClueLevel.EASY, emotes(Emote.THINK), false,
            "Think in middle of the wheat field by the Lumbridge mill. Equip a blue gnome robetop, a turquoise gnome robe bottom and an oak shortbow.",
            get(new int[][]{
                    { 3155, 3308 },
                    { 3153, 3306 },
                    { 3153, 3297 },
                    { 3155, 3295 },
                    { 3157, 3295 },
                    { 3162, 3290 },
                    { 3164, 3290 },
                    { 3165, 3291 },
                    { 3165, 3292 },
                    { 3164, 3293 },
                    { 3164, 3296 },
                    { 3165, 3297 },
                    { 3165, 3299 },
                    { 3162, 3302 },
                    { 3162, 3303 },
                    { 3160, 3305 },
                    { 3160, 3306 },
                    { 3159, 3307 },
                    { 3158, 3307 },
                    { 3157, 3308 }
            }), item(BLUE_ROBE_TOP), item(TURQUOISE_ROBE_BOTTOMS), item(OAK_SHORTBOW)),

    EMOTE_97(ClueLevel.MEDIUM, emotes(Emote.THINK, Emote.SPIN), false,
            "Think in the centre of the Observatory. Spin before you talk to me. Equip a mithril chain body, green dragonhide chaps and a ruby amulet.",
            get(new int[][]{
                    { 2438, 3165 },
                    { 2438, 3159 },
                    { 2444, 3159 },
                    { 2444, 3165 }
            }), item(MITHRIL_CHAINBODY), item(GREEN_DHIDE_CHAPS), item(RUBY_AMULET)),

    EMOTE_98(ClueLevel.EASY, emotes(Emote.WAVE), false,
            "Wave along the south fence of the Lumber Yard. Equip a hard leather body, leather chaps and a bronze axe.",
            get(new int[][]{
                    { 3305, 3494 },
                    { 3305, 3490 },
                    { 3314, 3490 },
                    { 3314, 3494 }
            }), item(HARDLEATHER_BODY), item(LEATHER_CHAPS), item(BRONZE_AXE)),

    EMOTE_99(ClueLevel.EASY, emotes(Emote.WAVE), false,
            "Wave in the Falador gem store. Equip a Mithril pickaxe, Black platebody and an Iron Kiteshield.",
            get(new int[][]{
                    { 2944, 3338 },
                    { 2944, 3332 },
                    { 2947, 3332 },
                    { 2947, 3338 }
            }), item(MITHRIL_PICKAXE), item(BLACK_PLATEBODY), item(IRON_KITESHIELD)),

    EMOTE_100(ClueLevel.EASY, emotes(Emote.WAVE), false,
            "Wave on Mudskipper Point. Equip a black cape, leather chaps and a steel mace.",
            get(new int[][]{
                    { 2993, 3132 },
                    { 2976, 3116 },
                    { 2984, 3102 },
                    { 3009, 3109 },
                    { 3008, 3132 }
            }), item(BLACK_CAPE), item(LEATHER_CHAPS), item(STEEL_MACE)),

   /* EMOTE_101(null, emotes(Emote.WAVE), true,
            "Wave on the northern wall of Castle Drakan. Beware of double agents! Wear a dragon sq shield, splitbark body and any boater.",
            get(NOPE))*/

   /*EMOTE_102(null, emotes(Emote.YAWN), true,
           "Yawn in the 7th room of Pyramid Plunder. Beware of double agents! Equip a pharaoh sceptre and a full set of menaphite robes.",
           ),*/

    EMOTE_103(ClueLevel.EASY, emotes(Emote.YAWN), false,
            "Yawn in the Varrock library. Equip a green gnome robe top, HAM robe bottom and an iron warhammer.",
            get(new int[][]{
                    { 3207, 3498 },
                    { 3207, 3490 },
                    { 3215, 3490 },
                    { 3215, 3494 },
                    { 3218, 3494 },
                    { 3218, 3498 }
            }), item(GREEN_ROBE_TOP), item(HAM_ROBE), item(IRON_WARHAMMER)),

    EMOTE_104(ClueLevel.EASY, emotes(Emote.YAWN), false,
            "Yawn in Draynor Marketplace. Equip studded leather chaps, an iron kiteshield and a steel longsword.",
            get(new int[][]{
                    { 3072, 3258 },
                    { 3072, 3244 },
                    { 3088, 3244 },
                    { 3088, 3251 },
                    { 3087, 3251 },
                    { 3087, 3256 },
                    { 3083, 3256 },
                    { 3083, 3258 }
            }), item(STUDDED_CHAPS), item(IRON_KITESHIELD), item(STEEL_LONGSWORD)),
    EMOTE_105(ClueLevel.MEDIUM, emotes(Emote.YAWN, Emote.SHRUG), false,
            "Yawn in the Castle Wars lobby. Shrug before you talk to me. Equip a ruby amulet, a mithril scimitar and a Wilderness cape.",
            get(new int[][]{
                    { 2438, 3099 },
                    { 2436, 3099 },
                    { 2435, 3098 },
                    { 2435, 3096 },
                    { 2436, 3095 },
                    { 2437, 3095 },
                    { 2437, 3092 },
                    { 2436, 3092 },
                    { 2435, 3091 },
                    { 2435, 3089 },
                    { 2436, 3088 },
                    { 2437, 3088 },
                    { 2437, 3085 },
                    { 2436, 3085 },
                    { 2435, 3084 },
                    { 2435, 3082 },
                    { 2436, 3081 },
                    { 2438, 3081 },
                    { 2439, 3082 },
                    { 2443, 3082 },
                    { 2444, 3081 },
                    { 2446, 3081 },
                    { 2447, 3082 },
                    { 2447, 3084 },
                    { 2446, 3085 },
                    { 2445, 3085 },
                    { 2445, 3087 },
                    { 2446, 3088 },
                    { 2446, 3089 },
                    { 2445, 3089 },
                    { 2445, 3091 },
                    { 2446, 3091 },
                    { 2446, 3092 },
                    { 2445, 3093 },
                    { 2445, 3095 },
                    { 2446, 3095 },
                    { 2447, 3096 },
                    { 2447, 3098 },
                    { 2446, 3099 },
                    { 2444, 3099 },
                    { 2443, 3098 },
                    { 2439, 3098 }
            }), item(RUBY_AMULET), item(MITHRIL_SCIMITAR), range("Any team cape", TEAM1_CAPE, TEAM50_CAPE)),

    EMOTE_106(ClueLevel.HARD, emotes(Emote.YAWN), true,
            "Yawn in the rogues' general store. Beware of double agents! Equip an adamant square shield, blue dragon vambraces and a rune pickaxe.",
            get(new int[][]{
                    { 3024, 3705 },
                    { 3024, 3699 },
                    { 3028, 3699 },
                    { 3028, 3705 }
            }), item(ADAMANT_SQ_SHIELD), item(BLUE_DHIDE_VAMB), item(RUNE_PICKAXE)),

    EMOTE_107(ClueLevel.ELITE, emotes(Emote.YAWN), false,
            "Yawn at the top of Trollheim. Equip a lava battlestaff, black dragonhide vambraces and a mind shield.",
            get(new int[][]{
                    { 2891, 3685 },
                    { 2886, 3680 },
                    { 2884, 3675 },
                    { 2888, 3671 },
                    { 2893, 3672 },
                    { 2897, 3677 },
                    { 2896, 3683 }
            }), item(LAVA_BATTLESTAFF), item(BLACK_DHIDE_VAMB), item(MIND_SHIELD)),

    EMOTE_108(ClueLevel.MEDIUM, emotes(Emote.YAWN, Emote.YES), false,
            "Yawn in the centre of Arceuus library. Nod your head before you talk to me. Equip blue dragonhide vambraces, adamant boots and an adamant dagger.",
            get(new int[][]{
                    { 1628, 3819 },
                    { 1622, 3813 },
                    { 1622, 3803 },
                    { 1628, 3797 },
                    { 1634, 3797 },
                    { 1634, 3798 },
                    { 1636, 3798 },
                    { 1636, 3797 },
                    { 1644, 3797 },
                    { 1644, 3805 },
                    { 1643, 3805 },
                    { 1643, 3807 },
                    { 1644, 3807 },
                    { 1644, 3813 },
                    { 1638, 3819 },
                    { 1634, 3819 },
                    { 1634, 3818 },
                    { 1632, 3818 },
                    { 1632, 3819 }
            }), item(BLUE_DHIDE_VAMB), item(ADAMANT_BOOTS), item(ADAMANT_DAGGER)),

    /*
    EMOTE_109(null, emotes(null))//BULLROARER
*/

    EMOTE_110(ClueLevel.BEGINNER, emotes(Emote.RASPBERRY), false,
            "Blow a raspberry in Gypsy Aris' tent. Equip a gold ring and a gold necklace.",
            get(new int[][]{
                    { 3199, 3427 },
                    { 3199, 3421 },
                    { 3207, 3421 },
                    { 3207, 3427 },
            }), item(GOLD_RING), item(GOLD_NECKLACE)),

    EMOTE_111(ClueLevel.BEGINNER, emotes(Emote.BOW), false,
            "Bow to the Tournament Guard at home.",
            get(new int[][]{
                    { 3075, 3495 },
                    { 3075, 3489 },
                    { 3082, 3489 },
                    { 3082, 3495 }
            })),

    EMOTE_112(ClueLevel.BEGINNER, emotes(Emote.CHEER), false,
            "Cheer at Iffie Nitter. Equip a chef hat and a red cape.",
            get(new int[][]{
                    { 3204, 3420 },
                    { 3201, 3417 },
                    { 3205, 3413 },
                    { 3206, 3413 },
                    { 3206, 3412 },
                    { 3207, 3411 },
                    { 3208, 3411 },
                    { 3209, 3412 },
                    { 3209, 3417 },
                    { 3210, 3418 },
                    { 3208, 3420 }
            }), item(CHEFS_HAT), item(RED_CAPE)),

    EMOTE_113(ClueLevel.BEGINNER, emotes(Emote.CLAP), false,
            "Clap at Bob's Brilliant Axes. Equip a bronze axe and leather boots.",
            get(new int[][]{
                    { 3228, 3206 },
                    { 3228, 3205 },
                    { 3227, 3205 },
                    { 3227, 3202 },
                    { 3228, 3202 },
                    { 3228, 3201 },
                    { 3234, 3201 },
                    { 3234, 3206 }
            }), item(BRONZE_AXE), item(LEATHER_BOOTS)),

    EMOTE_114(ClueLevel.BEGINNER, emotes(Emote.PANIC), false,
            "Panic at Al Kharid mine.",
            get(new int[][]{
                    { 3300, 3320 },
                    { 3293, 3311 },
                    { 3295, 3308 },
                    { 3290, 3299 },
                    { 3294, 3292 },
                    { 3290, 3282 },
                    { 3293, 3280 },
                    { 3291, 3275 },
                    { 3307, 3275 },
                    { 3306, 3278 },
                    { 3308, 3282 },
                    { 3303, 3287 },
                    { 3307, 3293 },
                    { 3304, 3298 },
                    { 3308, 3304 },
                    { 3305, 3308 },
                    { 3304, 3315 }
            })),

    EMOTE_115(ClueLevel.BEGINNER, emotes(Emote.SPIN), false,
            "Spin at Flynn's Mace Shop.",
            get(new int[][]{
                    { 2950, 3391 },
                    { 2950, 3389 },
                    { 2948, 3389 },
                    { 2948, 3385 },
                    { 2953, 3385 },
                    { 2953, 3391 }
            }));

    private final Set<Emote> emotes;
    private final boolean agents;
    private final ClueLevel level;
    private final String hint;
    private final ItemRequirement[] requirements;
    private final RSPolygon polygon;
    private final ClueChallenge challenge;

    EmoteClue(final ClueLevel level, final Set<Emote> emotes, final boolean agents, final String hint, final RSPolygon polygon, final ItemRequirement... items) {
        this.emotes = emotes;
        this.agents = agents;
        this.level = level;
        this.hint = hint;
        this.requirements = items;
        this.polygon = polygon;
        assert emotes.size() == 1 || (emotes.size() == 2 && !agents) : "Two emotes with double agents detected.";
        this.challenge = new EmoteRequest(Collections.unmodifiableList(new ArrayList<>(emotes)), agents, requirements, polygon, level);
    }

    public static final Set<Emote> emotes(final Emote... emotes) {
        return new LinkedHashSet<>(Arrays.asList(emotes));
    }

    public static final RSPolygon get(final int[][] tiles) {
        return new RSPolygon(tiles);
    }

    public static final RSPolygon get(final int[][] tiles, final int plane) {
        return new RSPolygon(tiles, plane);
    }

    private static SingleItemRequirement item(int itemId) {
        return new SingleItemRequirement(itemId);
    }

    private static RangeItemRequirement range(int startItemId, int endItemId) {
        return range(null, startItemId, endItemId);
    }

    private static RangeItemRequirement range(String name, int startItemId, int endItemId) {
        return new RangeItemRequirement(name, startItemId, endItemId);
    }

    private static AnyRequirementCollection any(String name, ItemRequirement... requirements) {
        return new AnyRequirementCollection(name, requirements);
    }

    private static AllRequirementsCollection all(ItemRequirement... requirements) {
        return new AllRequirementsCollection(requirements);
    }

    private static SlotLimitationRequirement emptySlot(String description, EquipmentSlot... slots) {
        return new SlotLimitationRequirement(description, slots);
    }

    @Override
    public void view(@NotNull Player player, @NotNull Item item) {
        player.getTemporaryAttributes().put("Clue scroll item", item);
        GameInterface.CLUE_SCROLL.open(player);
    }

    @Override
    public TreasureTrailType getType() {
        return TreasureTrailType.EMOTE;
    }

    @Override
    public String getEnumName() {
        return toString();
    }

    @Override
    public String getText() {
        return this.hint.replace(". ", ". <br>").replace("! ", "! <br>");
    }

    @Override
    public ClueChallenge getChallenge() {
        return challenge;
    }

    @NotNull
    @Override
    public ClueLevel level() {
        return level;
    }

    public Set<Emote> getEmotes() {
        return emotes;
    }

    public boolean isAgents() {
        return agents;
    }

    public ClueLevel getLevel() {
        return level;
    }

    public String getHint() {
        return hint;
    }

    public ItemRequirement[] getRequirements() {
        return requirements;
    }

    public RSPolygon getPolygon() {
        return polygon;
    }

    @NotNull
    private static AllRequirementsCollection anyVeracSet() {
        return all(
                any("Any veracs helm", item(VERACS_HELM), range(VERACS_HELM_100, VERACS_HELM_0)),
                any("Any veracs flail", item(VERACS_FLAIL), range(VERACS_FLAIL_100, VERACS_FLAIL_0)),
                any("Any veracs brassard", item(VERACS_BRASSARD), range(VERACS_BRASSARD_100, VERACS_BRASSARD_0)),
                any("Any veracs plateskirt", item(VERACS_PLATESKIRT), range(VERACS_PLATESKIRT_100, VERACS_PLATESKIRT_0)));
    }

    @NotNull
    private static AllRequirementsCollection anyToragSet() {
        return all(
                any("Any torags helm", item(TORAGS_HELM), range(TORAGS_HELM_100, TORAGS_HELM_0)),
                any("Any torags hammers", item(TORAGS_HAMMERS), range(TORAGS_HAMMERS_100, TORAGS_HAMMERS_0)),
                any("Any torags platebody", item(TORAGS_PLATEBODY), range(TORAGS_PLATEBODY_100, TORAGS_PLATEBODY_0)),
                any("Any torags platelegs", item(TORAGS_PLATELEGS), range(TORAGS_PLATELEGS_100, TORAGS_PLATELEGS_0)));
    }

    @NotNull
    private static AllRequirementsCollection anyKarilSet() {
        return all(
                any("Any karils coif", item(KARILS_COIF), range(KARILS_COIF_100, KARILS_COIF_0)),
                any("Any karils crossbow", item(KARILS_CROSSBOW), range(KARILS_CROSSBOW_100, KARILS_CROSSBOW_0)),
                any("Any karils leathertop", item(KARILS_LEATHERTOP), range(KARILS_LEATHERTOP_100, KARILS_LEATHERTOP_0)),
                any("Any karils leatherskirt", item(KARILS_LEATHERSKIRT), range(KARILS_LEATHERSKIRT_100, KARILS_LEATHERSKIRT_0)));
    }

    @NotNull
    private static AllRequirementsCollection anyGuthanSet() {
        return all(
                any("Any guthans helm", item(GUTHANS_HELM), range(GUTHANS_HELM_100, GUTHANS_HELM_0)),
                any("Any guthans warspear", item(GUTHANS_WARSPEAR), range(GUTHANS_WARSPEAR_100, GUTHANS_WARSPEAR_0)),
                any("Any guthans platebody", item(GUTHANS_PLATEBODY), range(GUTHANS_PLATEBODY_100, GUTHANS_PLATEBODY_0)),
                any("Any guthans chainskirt", item(GUTHANS_CHAINSKIRT), range(GUTHANS_CHAINSKIRT_100, GUTHANS_CHAINSKIRT_0)));
    }

    @NotNull
    private static AllRequirementsCollection anyDharokSet() {
        return all(
                any("Any dharoks helm", item(DHAROKS_HELM), range(DHAROKS_HELM_100, DHAROKS_HELM_0)),
                any("Any dharoks greataxe", item(DHAROKS_GREATAXE), range(DHAROKS_GREATAXE_100, DHAROKS_GREATAXE_0)),
                any("Any dharoks platebody", item(DHAROKS_PLATEBODY), range(DHAROKS_PLATEBODY_100, DHAROKS_PLATEBODY_0)),
                any("Any dharoks platelegs", item(DHAROKS_PLATELEGS), range(DHAROKS_PLATELEGS_100, DHAROKS_PLATELEGS_0)));
    }

    @NotNull
    private static AllRequirementsCollection anyAhrimSet() {
        return all(
                any("Any ahrims hood", item(AHRIMS_HOOD), range(AHRIMS_HOOD_100, AHRIMS_HOOD_0)),
                any("Any ahrims staff", item(AHRIMS_STAFF), range(AHRIMS_STAFF_100, AHRIMS_STAFF_0)),
                any("Any ahrims robetop", item(AHRIMS_ROBETOP), range(AHRIMS_ROBETOP_100, AHRIMS_ROBETOP_0)),
                any("Any ahrims robeskirt", item(AHRIMS_ROBESKIRT), range(AHRIMS_ROBESKIRT_100, AHRIMS_ROBESKIRT_0)));
    }
}
