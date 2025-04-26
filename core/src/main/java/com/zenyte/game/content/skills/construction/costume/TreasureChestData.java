package com.zenyte.game.content.skills.construction.costume;

import com.zenyte.game.content.magicstorageunit.StorableSetPiece;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.container.ItemContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 5. march 2018 : 23:52.42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public enum TreasureChestData {

	BLACK_HERALDIC_KITESHIELD1(10665, new StorableSetPiece(7332)),
	BLACK_HERALDIC_KITESHIELD2(10668, new StorableSetPiece(7338)),
	BLACK_HERALDIC_KITESHIELD3(10671, new StorableSetPiece(7344)),
	BLACK_HERALDIC_KITESHIELD4(10674, new StorableSetPiece(7350)),
	BLACK_HERALDIC_KITESHIELD5(10677, new StorableSetPiece(7356)),
	GOLD_TRIMMED_STUDDED_LEATHER(10680, new StorableSetPiece(7362), new StorableSetPiece(7366)),
	FUR_TRIMMED_STUDDED_LEATHER(10681, new StorableSetPiece(7364), new StorableSetPiece(7368)),
	BLUE_WIZARD_GOLD(10686, new StorableSetPiece(7394), new StorableSetPiece(7390), new StorableSetPiece(7386)),
	BLUE_WIZARD_TRIM(10687, new StorableSetPiece(7396), new StorableSetPiece(7392), new StorableSetPiece(7388)),
	BLACK_WIZARD_GOLD(12453, new StorableSetPiece(12453), new StorableSetPiece(12449), new StorableSetPiece(12445)),
	BLACK_WIZARD_TRIM(12455, new StorableSetPiece(12455), new StorableSetPiece(12451), new StorableSetPiece(12447)),
	TRIMMED_BLACK_ARMOUR(10690, new StorableSetPiece(2587), new StorableSetPiece(2583), new StorableSetPiece(2585, 3472), new StorableSetPiece(2589)),
	GOLD_TRIMMED_BLACK_ARMOUR(10691, new StorableSetPiece(2595), new StorableSetPiece(2591), new StorableSetPiece(2593, 3473), new StorableSetPiece(2597)),
	HIGHWAYMAN_MASK(10692, new StorableSetPiece(2631)),
	BLUE_BERET(10693, new StorableSetPiece(2633)),
	BLACK_BERET(10694, new StorableSetPiece(2635)),
	WHITE_BERET(10695, new StorableSetPiece(2637)),
	RED_BERET(12247, new StorableSetPiece(12247)),
	BLACK_HERALDIC_HELM1(10699, new StorableSetPiece(10306)),
	BLACK_HERALDIC_HELM2(10700, new StorableSetPiece(10308)),
	BLACK_HERALDIC_HELM3(10701, new StorableSetPiece(10310)),
	BLACK_HERALDIC_HELM4(10702, new StorableSetPiece(10312)),
	BLACK_HERALDIC_HELM5(10703, new StorableSetPiece(10314)),
	TRIMMED_AMULET_OF_MAGIC(10738, new StorableSetPiece(10366)),
	PANTALOONS(10744, new StorableSetPiece(10396)),
	WIG(10740, new StorableSetPiece(10392)),
	FLARED_PANTS(10742, new StorableSetPiece(10394)),
	SLEEPING_CAP(10746, new StorableSetPiece(10398)),
	BOB_THE_CAT_SHIRT_RED(10714, new StorableSetPiece(10316)),
	BOB_THE_CAT_SHIRT_BLUE(10715, new StorableSetPiece(10318)),
	BOB_THE_CAT_SHIRT_GREEN(10716, new StorableSetPiece(10320)),
	BOB_THE_CAT_SHIRT_BLACK(10717, new StorableSetPiece(10322)),
	BOB_THE_CAT_SHIRT_PURPLE(10718, new StorableSetPiece(10324)),
	ELEGANT_CLOTHES_RED(10750, new StorableSetPiece(10404, 10424), new StorableSetPiece(10406, 10426)),
	ELEGANT_CLOTHES_GREEN(10754, new StorableSetPiece(10412, 10432), new StorableSetPiece(10414, 10434)),
	ELEGANT_CLOTHES_BLUE(10752, new StorableSetPiece(10408, 10428), new StorableSetPiece(10410, 10430)),
	ELEGANT_CLOTHES_BLACK(10748, new StorableSetPiece(10400, 10420), new StorableSetPiece(10402, 10422)),
	ELEGANT_CLOTHES_PURPLE(10756, new StorableSetPiece(10416, 10436), new StorableSetPiece(10418, 10438)),
	ELEGANT_CLOTHES_PINK(12315, new StorableSetPiece(12315, 12339), new StorableSetPiece(12317, 12341)),
	ELEGANT_CLOTHES_GOLD(12347, new StorableSetPiece(12347, 12343), new StorableSetPiece(12349, 12345)),
	BERET_MASK(11278, new StorableSetPiece(11282)),
	BRONZE_ARMOUR_TRIM(12215, new StorableSetPiece(12221), new StorableSetPiece(12215), new StorableSetPiece(12217, 12219), new StorableSetPiece(12223)),
	BRONZE_ARMOUR_GOLD(12205, new StorableSetPiece(12211), new StorableSetPiece(12205), new StorableSetPiece(12207, 12209), new StorableSetPiece(12213)),
	IRON_ARMOUR_TRIM(12225, new StorableSetPiece(12231), new StorableSetPiece(12225), new StorableSetPiece(12227, 12229), new StorableSetPiece(12233)),
	IRON_ARMOUR_GOLD(12235, new StorableSetPiece(12241), new StorableSetPiece(12235), new StorableSetPiece(12237, 12239), new StorableSetPiece(12243)),
	STEEL_ARMOUR_TRIM(20184, new StorableSetPiece(20193), new StorableSetPiece(20184), new StorableSetPiece(20187, 20190), new StorableSetPiece(20196)),
	STEEL_ARMOUR_GOLD(20169, new StorableSetPiece(20178), new StorableSetPiece(20169), new StorableSetPiece(20172, 20175), new StorableSetPiece(20181)),
	BEANIE(12245, new StorableSetPiece(12245)),
	IMP_MASK(12249, new StorableSetPiece(12249)),
	GOBLIN_MASK(12251, new StorableSetPiece(12251)),
	BLACK_CANE(12375, new StorableSetPiece(12375)),
	BLACK_PICKAXE(12297, new StorableSetPiece(12297)),
	LARGE_SPADE(20164, new StorableSetPiece(20164)),
	WOODEN_SHIELD_GOLD(20166, new StorableSetPiece(20166)),
	GOLDEN_CHEFS_HAT(20205, new StorableSetPiece(20205)),
	GOLDEN_APRON(20208, new StorableSetPiece(20208)),
	MONK_ROBES_GOLD(20199, new StorableSetPiece(20199), new StorableSetPiece(20202)),
	RED_STRAW_BOATER(10758, new StorableSetPiece(7319)),
	ORANGE_STRAW_BOATER(10760, new StorableSetPiece(7321)),
	GREEN_STRAW_BOATER(10762, new StorableSetPiece(7323)),
	BLUE_STRAW_BOATER(10764, new StorableSetPiece(7325)),
	BLACK_STRAW_BOATER(10766, new StorableSetPiece(7327)),
	PINK_STRAW_BOATER(12309, new StorableSetPiece(12309)),
	PURPLE_STRAW_BOATER(12311, new StorableSetPiece(12311)),
	WHITE_STRAW_BOATER(12313, new StorableSetPiece(12313)),
	ADAMANT_HERALDIC_KITESHIELD1(10666, new StorableSetPiece(7334)),
	ADAMANT_HERALDIC_KITESHIELD2(10669, new StorableSetPiece(7340)),
	ADAMANT_HERALDIC_KITESHIELD3(10672, new StorableSetPiece(7346)),
	ADAMANT_HERALDIC_KITESHIELD4(10675, new StorableSetPiece(7352)),
	ADAMANT_HERALDIC_KITESHIELD5(10678, new StorableSetPiece(7358)),
	GREEN_DRAGONHIDE_GOLD(10682, new StorableSetPiece(7370), new StorableSetPiece(7378)),
	GREEN_DRAGONHIDE_TRIM(10683, new StorableSetPiece(7372), new StorableSetPiece(7380)),
	RANGER_BOOTS(10696, new StorableSetPiece(2577)),
	TRIMMED_ADAMANTITE_ARMOUR(10697, new StorableSetPiece(2605), new StorableSetPiece(2599), new StorableSetPiece(2601, 3474), new StorableSetPiece(2603)),
	GOLD_TRIMMED_ADAMANTITE_ARMOUR(10698, new StorableSetPiece(2613), new StorableSetPiece(2607), new StorableSetPiece(2609, 3475), new StorableSetPiece(2611)),
	RED_HEADBAND(10768, new StorableSetPiece(2645)),
	BLACK_HEADBAND(10770, new StorableSetPiece(2647)),
	BROWN_HEADBANG(10772, new StorableSetPiece(2649)),
	WHITE_HEADBAND(12299, new StorableSetPiece(12299)),
	BLUE_HEADBAND(12301, new StorableSetPiece(12301)),
	GOLD_HEADBAND(12303, new StorableSetPiece(12303)),
	PINK_HEADBAND(12305, new StorableSetPiece(12305)),
	GREEN_HEADBAND(12307, new StorableSetPiece(12307)),
	ADAMANT_HERALDIC_HELM1(10709, new StorableSetPiece(10296)),
	ADAMANT_HERALDIC_HELM2(10710, new StorableSetPiece(10298)),
	ADAMANT_HERALDIC_HELM3(10711, new StorableSetPiece(10300)),
	ADAMANT_HERALDIC_HELM4(10712, new StorableSetPiece(10302)),
	ADAMANT_HERALDIC_HELM5(10713, new StorableSetPiece(10304)),
	TRIMMED_AMULET_OF_STRENGTH(10736, new StorableSetPiece(10364)),
	WIZARD_BOOTS(10689, new StorableSetPiece(2579)),
	MITHRIL_ARMOUR_GOLD(12277, new StorableSetPiece(12283), new StorableSetPiece(12277), new StorableSetPiece(12279, 12285), new StorableSetPiece(12281)),
	MITHRIL_ARMOUR_TRIM(12287, new StorableSetPiece(12293), new StorableSetPiece(12287), new StorableSetPiece(12289, 12295), new StorableSetPiece(12291)),
	LEPRECHAUN_HAT(12359, new StorableSetPiece(12359)),
	BLACK_LEPRECHAUN_HAT(20246, new StorableSetPiece(20246)),
	CAT_MASK(12361, new StorableSetPiece(12361)),
	PENGUIN_MASK(12428, new StorableSetPiece(12428)),
	BLACK_UNICORN_MASK(20266, new StorableSetPiece(20266)),
	WHITE_UNICORN_MASK(20269, new StorableSetPiece(20269)),
	ARMADYL_VESTMENTS(12253, new StorableSetPiece(12259), new StorableSetPiece(12253), new StorableSetPiece(12255), new StorableSetPiece(12261), new StorableSetPiece(12263), new StorableSetPiece(12257)),
	ANCIENT_VESTMENTS(12193, new StorableSetPiece(12203), new StorableSetPiece(12193), new StorableSetPiece(12195), new StorableSetPiece(12197), new StorableSetPiece(12199), new StorableSetPiece(12201)),
	BANDOS_VESTMENTS(12265, new StorableSetPiece(12271), new StorableSetPiece(12265), new StorableSetPiece(12267), new StorableSetPiece(12273), new StorableSetPiece(12275), new StorableSetPiece(12269)),
	TOWN_CRIER_HAT(12319, new StorableSetPiece(12319)),
	TOWN_CRIER_BELL(20243, new StorableSetPiece(20243)),
	TOWN_CRIER_COAT(20240, new StorableSetPiece(20240)),
	ADAMANT_CANE(12377, new StorableSetPiece(12377)),
	HOLY_SANDALS(12598, new StorableSetPiece(12598)),
	CLUELESS_SCROLL(20249, new StorableSetPiece(20249)),
	ARCEUUS_BANNER(20251, new StorableSetPiece(20251)),
	HOSIDIUS_BANNER(20254, new StorableSetPiece(20254)),
	LOVAKENGJ_BANNER(20257, new StorableSetPiece(20257)),
	PISCARILIUS_BANNER(20260, new StorableSetPiece(20260)),
	SHAYZIEN_BANNER(20263, new StorableSetPiece(20263)),
	CABBAGE_ROUND_SHIELD(20272, new StorableSetPiece(20272)),
	RUNE_HERALDIC_KITESHIELD1(10667, new StorableSetPiece(7336)),
	RUNE_HERALDIC_KITESHIELD2(10670, new StorableSetPiece(7342)),
	RUNE_HERALDIC_KITESHIELD3(10673, new StorableSetPiece(7348)),
	RUNE_HERALDIC_KITESHIELD4(10676, new StorableSetPiece(7354)),
	RUNE_HERALDIC_KITESHIELD5(10679, new StorableSetPiece(7560)),
	BLUE_DRAGONHIDE_GOLD(10684, new StorableSetPiece(7374), new StorableSetPiece(7382)),
	BLUE_DRAGONHIDE_TRIM(10685, new StorableSetPiece(7376), new StorableSetPiece(7384)),
	ENCHANTED_ROBES(10688, new StorableSetPiece(7400), new StorableSetPiece(7399), new StorableSetPiece(7398)),
	ROBIN_HOOD_HAT(10796, new StorableSetPiece(2581)),
	GOLD_TRIMMED_RUNE_ARMOUR(10798, new StorableSetPiece(2619), new StorableSetPiece(2615), new StorableSetPiece(2617, 3476), new StorableSetPiece(2621)),
	TRIMMED_RUNE_ARMOUR(10800, new StorableSetPiece(2627), new StorableSetPiece(2623), new StorableSetPiece(2625, 3477), new StorableSetPiece(2629)),
	BROWN_CAVALIER(10802, new StorableSetPiece(2639)),
	DARK_BROWN_CAVALIER(10804, new StorableSetPiece(2641)),
	BLACK_CAVALIER(10806, new StorableSetPiece(2643)),
	RED_CAVALIER(12323, new StorableSetPiece(12323)),
	BLUE_CAVALIER(12325, new StorableSetPiece(12325)),
	WHITE_CAVALIER(12321, new StorableSetPiece(12321)),
	PIRATE_HAT(10774, new StorableSetPiece(8950)),
	ZAMORAK_RUNE_ARMOUR(10776, new StorableSetPiece(2657), new StorableSetPiece(2653), new StorableSetPiece(2655, 3478), new StorableSetPiece(2659)),
	SARADOMIN_RUNE_ARMOUR(10778, new StorableSetPiece(2665), new StorableSetPiece(2661), new StorableSetPiece(2663, 3479), new StorableSetPiece(2667)),
	GUTHIX_RUNE_ARMOUR(10780, new StorableSetPiece(2673), new StorableSetPiece(2669), new StorableSetPiece(2671, 3480), new StorableSetPiece(2675)),
	GOLD_PLATED_RUNE_ARMOUR(10782, new StorableSetPiece(3486), new StorableSetPiece(3481), new StorableSetPiece(3483, 3485), new StorableSetPiece(3488)),
	RUNE_HERALDIC_HELM1(10704, new StorableSetPiece(10286)),
	RUNE_HERALDIC_HELM2(10705, new StorableSetPiece(10288)),
	RUNE_HERALDIC_HELM3(10706, new StorableSetPiece(10290)),
	RUNE_HERALDIC_HELM4(10707, new StorableSetPiece(10292)),
	RUNE_HERALDIC_HELM5(10708, new StorableSetPiece(10294)),
	TRIMMED_AMULET_OF_GLORY(10719, new StorableSetPiece(10362)),
	SARADOMIN_VESTMENTS(10784, new StorableSetPiece(10452), new StorableSetPiece(10458), new StorableSetPiece(10464), new StorableSetPiece(10446), new StorableSetPiece(10440), new StorableSetPiece(10470)),
	GUTHIX_VESTMENTS(10788, new StorableSetPiece(10454), new StorableSetPiece(10462), new StorableSetPiece(10466), new StorableSetPiece(10448), new StorableSetPiece(10442), new StorableSetPiece(10472)),
	ZAMORAK_VESTMENTS(10786, new StorableSetPiece(10456), new StorableSetPiece(10460), new StorableSetPiece(10468), new StorableSetPiece(10450), new StorableSetPiece(10444), new StorableSetPiece(10474)),
	SARADOMIN_BLESSED_DRAGONHIDE(10792, new StorableSetPiece(10390), new StorableSetPiece(10386), new StorableSetPiece(10388), new StorableSetPiece(10384)),
	GUTHIX_BLESSED_DRAGONHIDE(10794, new StorableSetPiece(10382), new StorableSetPiece(10378), new StorableSetPiece(10380), new StorableSetPiece(10376)),
	ZAMORAK_BLESSED_DRAGONHIDE(10790, new StorableSetPiece(10374), new StorableSetPiece(10370), new StorableSetPiece(10372), new StorableSetPiece(10368)),
	CAVALIER_MASK(11277, new StorableSetPiece(11280)),
	RED_DRAGONHIDE_GOLD(12327, new StorableSetPiece(12327), new StorableSetPiece(12329)),
	RED_DRAGONHIDE_TRIM(12331, new StorableSetPiece(12331), new StorableSetPiece(12333)),
	PITH_HELMET(12516, new StorableSetPiece(12516)),
	EXPLORER_BACKPACK(12514, new StorableSetPiece(12514)),
	ARMADYL_RUNE_ARMOUR(12470, new StorableSetPiece(12476), new StorableSetPiece(12470), new StorableSetPiece(12472, 12474), new StorableSetPiece(12478)),
	BANDOS_RUNE_ARMOUR(12480, new StorableSetPiece(12486), new StorableSetPiece(12480), new StorableSetPiece(12482, 12484), new StorableSetPiece(12488)),
	ANCIENT_RUNE_ARMOUR(12460, new StorableSetPiece(12466), new StorableSetPiece(12460), new StorableSetPiece(12462, 12464), new StorableSetPiece(12468)),
	ARMADYL_BLESSED_DRAGONHIDE(12508, new StorableSetPiece(12512), new StorableSetPiece(12508), new StorableSetPiece(12510), new StorableSetPiece(124506)),
	BANDOS_BLESSED_DRAGONHIDE(12500, new StorableSetPiece(12504), new StorableSetPiece(12500), new StorableSetPiece(12502), new StorableSetPiece(12498)),
	ANCIENT_BLESSED_DRAGONHIDE(12492, new StorableSetPiece(12496), new StorableSetPiece(12492), new StorableSetPiece(12494), new StorableSetPiece(12490)),
	GREEN_DRAGON_MASK(12518, new StorableSetPiece(12518)),
	BLUE_DRAGON_MASK(12520, new StorableSetPiece(12520)),
	RED_DRAGON_MASK(12522, new StorableSetPiece(12522)),
	BLACK_DRAGON_MASK(12524, new StorableSetPiece(12524)),
	RUNE_CANE(12379, new StorableSetPiece(12379)),
	ZOMBIE_HEAD(19912, new StorableSetPiece(19912)),
	CYCLOPS_HEAD(19915, new StorableSetPiece(19915)),
	GILDED_RUNE_MED_HELM(20146, new StorableSetPiece(20146)),
	GILDED_RUNE_CHAINBODY(20149, new StorableSetPiece(20149)),
	GILDED_RUNE_SQ_SHIELD(20152, new StorableSetPiece(20152)),
	GILDED_RUNE_2H_SWORD(20155, new StorableSetPiece(20155)),
	GILDED_RUNE_SPEAR(20158, new StorableSetPiece(20158)),
	GILDED_RUNE_HASTA(20161, new StorableSetPiece(20161)),
	NUNCHAKU(19918, new StorableSetPiece(19918)),
	SARADOMIN_DHIDE_BOOTS(19933, new StorableSetPiece(19933)),
	BANDOS_DHIDE_BOOTS(19924, new StorableSetPiece(19924)),
	ARMADYL_DHIDE_BOOTS(19930, new StorableSetPiece(19930)),
	GUTHIX_DHIDE_BOOTS(19927, new StorableSetPiece(19927)),
	ZAMORAK_DHIDE_BOOTS(19936, new StorableSetPiece(19936)),
	ANCIENT_DHIDE_BOOTS(19921, new StorableSetPiece(19921)),
	THIRD_AGE_RANGER_KIT(10330, new StorableSetPiece(10334), new StorableSetPiece(10330), new StorableSetPiece(10332), new StorableSetPiece(10336)),
	THIRD_AGE_MAGE_ROBES(10338, new StorableSetPiece(10342), new StorableSetPiece(10338), new StorableSetPiece(10340), new StorableSetPiece(10344)),
	THIRD_AGE_ARMOUR(10348, new StorableSetPiece(10350), new StorableSetPiece(10348), new StorableSetPiece(10346), new StorableSetPiece(10352)),
	DRAGON_CANE(12373, new StorableSetPiece(12373)),
	BRIEFCASE(12335, new StorableSetPiece(12335)),
	SAGACIOUS_SPECTACLES(12337, new StorableSetPiece(12337)),
	ROYAL_OUTFIT(12393, new StorableSetPiece(12397), new StorableSetPiece(12393), new StorableSetPiece(12395), new StorableSetPiece(12439)),
	BRONZE_DRAGON_MASK(12363, new StorableSetPiece(12363)),
	IRON_DRAGON_MASK(12365, new StorableSetPiece(12365)),
	STEEL_DRAGON_MASK(12367, new StorableSetPiece(12367)),
	MITHRIL_DRAGON_MASK(12369, new StorableSetPiece(12369)),
	LAVA_DRAGON_MASK(12371, new StorableSetPiece(12371)),
	AFRO(12430, new StorableSetPiece(12430)),
	KATANA(12357, new StorableSetPiece(12357)),
	BIG_PIRATE_HAT(12355, new StorableSetPiece(12355)),
	TOP_HAT(12432, new StorableSetPiece(12432)),
	MONOCLE(12353, new StorableSetPiece(12353)),
	BLACK_DRAGONHIDE_GOLD(12381, new StorableSetPiece(12381), new StorableSetPiece(12383)),
	BLACK_DRAGONHIDE_TRIM(12385, new StorableSetPiece(12385), new StorableSetPiece(12387)),
	MUSKETEER_OUTFIT(12441, new StorableSetPiece(12351), new StorableSetPiece(12441), new StorableSetPiece(12443)),
	PARTYHAT_AND_SPECS(12399, new StorableSetPiece(12399)),
	PIRATE_HAT_AND_PATCH(12412, new StorableSetPiece(12412)),
	TOP_HAT_AND_MONOCLE(12434, new StorableSetPiece(12434)),
	DEERSTALKER(12540, new StorableSetPiece(12540)),
	HEAVY_CASKET(19941, new StorableSetPiece(19941)),
	ARCEUUS_HOUSE_SCARF(19943, new StorableSetPiece(19943)),
	HOSIDIUS_HOUSE_SCARF(19946, new StorableSetPiece(19946)),
	LOVAKENGJ_HOUSE_SCARF(19949, new StorableSetPiece(19949)),
	PISCARILIUS_HOUSE_SCARF(19952, new StorableSetPiece(19952)),
	SHAYZIEN_HOUSE_SCARF(19955, new StorableSetPiece(19955)),
	BLACKSMITHS_HELM(19988, new StorableSetPiece(19988)),
	BUCKET_HELM(19991, new StorableSetPiece(19991)),
	RANGER_GLOVES(19994, new StorableSetPiece(19994)),
	HOLY_WRAPS(19997, new StorableSetPiece(19997)),
	RING_OF_NATURE(20005, new StorableSetPiece(20005)),
	THIRD_AGE_WAND(12422, new StorableSetPiece(12422)),
	THIRD_AGE_BOW(12424, new StorableSetPiece(12424)),
	THIRD_AGE_LONGSWORD(12426, new StorableSetPiece(12426)),
	DARK_TUXEDO_OUTFIT(19970, new StorableSetPiece(19958), new StorableSetPiece(19964), new StorableSetPiece(19961), new StorableSetPiece(19967), new StorableSetPiece(19970)),
	LIGHT_TUXEDO_OUTFIT(19985, new StorableSetPiece(19973), new StorableSetPiece(19979), new StorableSetPiece(19976), new StorableSetPiece(19982), new StorableSetPiece(19985)),
	FANCY_TIARA(20008, new StorableSetPiece(20008)),
	THIRD_AGE_AXE(20011, new StorableSetPiece(20011)),
	THIRD_AGE_PICKAXE(20014, new StorableSetPiece(20014)),
	RING_OF_COINS(20017, new StorableSetPiece(20017)),
	LESSER_DEMON_MASK(20020, new StorableSetPiece(20020)),
	GREATER_DEMON_MASK(20023, new StorableSetPiece(20023)),
	BLACK_DEMON_MASK(20026, new StorableSetPiece(20026)),
	OLD_DEMON_MASK(20029, new StorableSetPiece(20029)),
	JUNGLE_DEMON_MASK(20032, new StorableSetPiece(20032)),
	OBSIDIAN_CAPE_RED(20050, new StorableSetPiece(20050)),
	HALF_MOON_SPECTACLES(20053, new StorableSetPiece(20053)),
	ALE_OF_THE_GODS(20056, new StorableSetPiece(20056)),
	BUCKET_HELM_GOLD(20059, new StorableSetPiece(20059)),
	BOWL_WIG(20110, new StorableSetPiece(20110)),
	SHAYZIEN_HOUSE_HOOD(20125, new StorableSetPiece(20125)),
	HOSIDIUS_HOUSE_HOOD(20116, new StorableSetPiece(20116)),
	ARCEUUS_HOUSE_HOOD(20113, new StorableSetPiece(20113)),
	PISCARILIUS_HOUSE_HOOD(20122, new StorableSetPiece(20122)),
	LOVAKENGJ_HOUSE_HOOD(20119, new StorableSetPiece(20119)),
	SAMURAI_OUTFIT(20038, new StorableSetPiece(20035), new StorableSetPiece(20038), new StorableSetPiece(20044), new StorableSetPiece(20041), new StorableSetPiece(20047)),
	MUMMY_OUTFIT(20080, new StorableSetPiece(20080), new StorableSetPiece(20083), new StorableSetPiece(20089), new StorableSetPiece(20086), new StorableSetPiece(20092)),
	ANKOU_OUTFIT(20095, new StorableSetPiece(20095), new StorableSetPiece(20098), new StorableSetPiece(20104), new StorableSetPiece(20101), new StorableSetPiece(20107)),
	ROBES_OF_DARKNESS(20128, new StorableSetPiece(20128), new StorableSetPiece(20131), new StorableSetPiece(20137), new StorableSetPiece(20134), new StorableSetPiece(20140)),
	MORE(10165),
	BACK(10166);

	TreasureChestData(final int displayItem, final StorableSetPiece... pieces) {
		this.displayItem = displayItem;
		this.pieces = pieces;
	}
	
	public static final TreasureChestData[] VALUES = values();
	public static final ItemContainer[] CONTAINERS = new ItemContainer[] {
			new ItemContainer(40, false),
			new ItemContainer(40, false),
			new ItemContainer(40, false),
			new ItemContainer(40, false),
			new ItemContainer(40, false),
			new ItemContainer(40, false),
			new ItemContainer(13, false)
	};
	
	public static final Map<Integer, TreasureChestData> MAP = new HashMap<Integer, TreasureChestData>(VALUES.length * 5);
	public static final Map<Integer, TreasureChestData> DISPLAY_MAP = new HashMap<Integer, TreasureChestData>(VALUES.length);

	static {
		for (int i = 0; i <= ELEGANT_CLOTHES_PINK.ordinal(); i++)
			CONTAINERS[0].add(new Item(VALUES[i].displayItem));
		CONTAINERS[0].add(new Item(MORE.displayItem));
		int index = 39;
		for (int i = 1; i < CONTAINERS.length - 1; i++) {
			for (int x = 0; x < 40; x++) {
				if (x == 0)
                    CONTAINERS[i].add(new Item(BACK.displayItem));
                else if (x == 39)
                    CONTAINERS[i].add(new Item(MORE.displayItem));
                else
                    CONTAINERS[i].add(new Item(VALUES[index++].displayItem));
            }
        }
        CONTAINERS[6].add(new Item(BACK.displayItem));
        for (int i = ALE_OF_THE_GODS.ordinal(); i <= ROBES_OF_DARKNESS.ordinal(); i++)
            CONTAINERS[6].add(new Item(VALUES[i].displayItem));
        for (TreasureChestData val : VALUES) {
            for (StorableSetPiece p : val.pieces) {
                for (int i : p.getIds()) {
                    MAP.put(i, val);
                }
            }
            DISPLAY_MAP.put(val.getDisplayItem(), val);
        }
    }

    private final int displayItem;
    private final StorableSetPiece[] pieces;

    public int getDisplayItem() {
        return displayItem;
    }

    public StorableSetPiece[] getPieces() {
        return pieces;
    }

}
