package com.zenyte.game.content.skills.herblore.actions;

import com.near_reality.game.content.crystal.CrystalShardKt;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.content.boons.impl.Mixologist;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.utils.TextUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.zenyte.game.content.skills.herblore.actions.Combine.HerbloreData.ANCIENT_ICON;

/**
 * @author Tommeh | 25 aug. 2018 | 18:07:17
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Combine extends Action {

	public static final Item vialOfWater = new Item(227);
	private static final Item vialOfBlood = new Item(ItemId.VIAL_OF_BLOOD_22446);
	private static final Item coconutMilk = new Item(5935);
	private static final Item pestleAndMortar = new Item(233);
	private static final Item knife = new Item(ItemId.KNIFE);
	private static final Item pestleAndMortarGauntlet = new Item(23865);
	private static final Animation brewingAnimation = new Animation(363);
	public static final Animation grindingAnimation = new Animation(364);
	private static final Animation cuttingAnimation = new Animation(1989);

	public enum HerbloreData {
		VENATOR_SHARD(0, 0, new Item(ItemId.ANCIENT_ESSENCE, 50_000), new Item(ItemId.VENATOR_SHARD), pestleAndMortar),
		ANCIENT_ICON(0, 0, new Item(ItemId.ANCIENT_ESSENCE, 5_000), new Item(ItemId.ANCIENT_ICON), pestleAndMortar),
		UNICORN_HORN_DUST(0, 0, new Item(235), new Item(237), pestleAndMortar),
		NIHIL_DUST(0, 0, new Item(26368), new Item(26231), pestleAndMortar),
		CHOCOLATE_DUST(0, 0, new Item(1975), new Item(1973), pestleAndMortar),
		CHOCOLATE_DUST_KNIFE(0, 0, new Item(1975), new Item(1973), knife),
		KEBBIT_TEETH_DUST(0, 0, new Item(10111), new Item(10109), pestleAndMortar),
		CRUSHED_NEST(0, 0, new Item(6693), new Item(5075), pestleAndMortar),
		DRAGON_SCALE_DUST(0, 0, new Item(241), new Item(243), pestleAndMortar),
		GOAT_HORN_DUST(0, 0, new Item(9736), new Item(9735), pestleAndMortar),
		GROUND_ASHES(0, 0, new Item(8865), new Item(592), pestleAndMortar),
		LAVA_SCALE_SHARD(0, 0, new Item(11994), new Item(11992), pestleAndMortar),
		GROUND_TOOTH(0, 0, new Item(9082), new Item(9079), pestleAndMortar),
		CRUSHED_SUPERIOR_DRAGON_BONES(0, 0, new Item(21975), new Item(22124), pestleAndMortar),
		GUAM_TAR(19, 30, new Item(10142, 15), new Item(249), new Item(1939, 15), pestleAndMortar),
		MARRENTILL_TAR(31, 42.5, new Item(10143, 15), new Item(251), new Item(1939, 15), pestleAndMortar),
		TARROMIN_TAR(39, 55, new Item(10144, 15), new Item(253), new Item(1939, 15), pestleAndMortar),
		HARRALANDER_TAR(44, 72.5, new Item(10145, 15), new Item(255), new Item(1939, 15), pestleAndMortar),
		GUAM_LEAF(3, 2.5, new Item(249), new Item(199)),
		SNAKE_WEED(3, 2.5, new Item(1526), new Item(1525)),
		MARRENTILL(5, 3.8, new Item(251), new Item(201)),
		TARROMIN(11, 5, new Item(253), new Item(203)),
		HARRALANDER(20, 6.3, new Item(255), new Item(205)),
		RANARR_WEED(25, 7.5, new Item(257), new Item(207)),
		TOADFLAX(30, 8, new Item(2998), new Item(3049)),
		IRIT_LEAF(40, 8.8, new Item(259), new Item(209)),
		AVANTOE(48, 10, new Item(261), new Item(211)),
		KWUARM(54, 11.3, new Item(263), new Item(213)),
		SNAPDRAGON(59, 11.8, new Item(3000), new Item(3051)),
		CADANTINE(65, 12.5, new Item(265), new Item(215)),
		LANTADYME(67, 13.1, new Item(2481), new Item(2485)),
		DWARF_WEED(70, 13.8, new Item(267), new Item(217)),
		TORSTOL(75, 15, new Item(269), new Item(219)),
		GUAM_POTION_UNF(3, 0, new Item(91), new Item(249), vialOfWater),
		MARRENTILL_POTION_UNF(5, 0, new Item(93), new Item(251), vialOfWater),
		TARROMIN_POTION_UNF(12, 0, new Item(95), new Item(253), vialOfWater),
		HARRALANDER_POTION_UNF(22, 0, new Item(97), new Item(255), vialOfWater),
		GUTHIX_BALANCE_1_UNF(22, 25, new Item(7658), new Item(131), new Item(1550)),
		GUTHIX_BALANCE_2_UNF(22, 25, new Item(7656), new Item(129), new Item(1550)),
		GUTHIX_BALANCE_3_UNF(22, 25, new Item(7654), new Item(127), new Item(1550)),
		GUTHIX_BALANCE_4_UNF(22, 25, new Item(7652), new Item(2430), new Item(1550)),
		RAMARR_POTION_UNF(30, 0, new Item(99), new Item(257), vialOfWater),
		TOADFLAX_POTION_UNF(34, 0, new Item(3002), new Item(2998), vialOfWater),
		IRIT_POTION_UNF(45, 0, new Item(101), new Item(259), vialOfWater),
		AVANTOE_POTION_UNF(50, 0, new Item(103), new Item(261), vialOfWater),
		KWUARM_POTION_UNF(55, 0, new Item(105), new Item(263), vialOfWater),
		SNAPDRAGON_POTION_UNF(63, 0, new Item(3004), new Item(3000), vialOfWater),
		CADANTINE_POTION_UNF(66, 0, new Item(107), new Item(265), vialOfWater),
		CADANTINE_BLOOD_POTION_UNF(80, 0, new Item(ItemId.CADANTINE_BLOOD_POTION_UNF), new Item(ItemId.CADANTINE), vialOfBlood),
		ANTIDOTE_PLUS_UNF(68, 0, new Item(5942), new Item(2998), coconutMilk),
		LANTADYME_POTION_UNF(69, 0, new Item(2483), new Item(2481), vialOfWater),
		DWARF_WEED_POTION_UNF(72, 0, new Item(109), new Item(267), vialOfWater),
		WEAPON_POISON_UNF(73, 0, new Item(5936), new Item(6016), coconutMilk),
		ANTIDOTE_PLUS_PLUS_UNF(79, 0, new Item(5951), new Item(259), coconutMilk),
		WEAPON_POISON_PLUS_PLUS_UNF(82, 0, new Item(5939), new Item(2398), coconutMilk),
		TORSTOL_POTION_UNF(78, 0, new Item(111), new Item(269), vialOfWater),
		ATTACK(3, 25, 2428, new Item(121), new Item(91), new Item(221)),
		ANTIPOISON(5, 37.5, 2446, new Item(175), new Item(93), new Item(235)),
		STRENGTH(12, 50, 113, new Item(115), new Item(95), new Item(225)),
		COMPOST(21, 60, 6470, new Item(6472), new Item(97), new Item(21622)),
		RESTORE(22, 62.5, 2430, new Item(127), new Item(97), new Item(223)),
		GUTHIX_BALANCE_1(22, 25, new Item(7666), new Item(7658), new Item(7650)),
		GUTHIX_BALANCE_2(22, 25, new Item(7664), new Item(7656), new Item(7650)),
		GUTHIX_BALANCE_3(22, 25, new Item(7662), new Item(7654), new Item(7650)),
		GUTHIX_BALANCE_4(22, 25, new Item(7660), new Item(7652), new Item(7650)),
		BLAMISH_OIL(25, 80, new Item(1582), new Item(1581), new Item(97)),
		ENERGY(26, 67.5, 3008, new Item(3010), new Item(97), new Item(1975)),
		DEFENCE(30, 75, 2432, new Item(133), new Item(99), new Item(239)),
		AGILITY(34, 80, 3032, new Item(3034), new Item(3002), new Item(2152)),
		COMBAT(36, 84, 9739, new Item(9741), new Item(97), new Item(9736)),
		PRAYER(38, 87.5, 2434, new Item(139), new Item(99), new Item(231)),
		SUPER_ATTACK(45, 100, 2436, new Item(145), new Item(101), new Item(221)),
		SUPER_ANTIPOISON(48, 106.3, 2448, new Item(181), new Item(101), new Item(235)),
		FISHING(50, 112.5, 2438, new Item(151), new Item(103), new Item(231)),
		SUPER_ENERGY(52, 117.5, 3016, new Item(3018), new Item(103), new Item(2970)),
		HUNTER(53, 120, 9998, new Item(10000), new Item(103), new Item(10111)),
		SUPER_STRENGTH(55, 125, 2440, new Item(157), new Item(105), new Item(225)),
		WEAPON_POISON(60, 137.5, new Item(187), new Item(105), new Item(241)),
		SUPER_RESTORE(63, 142.5, 3024, new Item(3026), new Item(3004), new Item(223)),
		SUPER_DEFENCE(66, 150, 2442, new Item(163), new Item(107), new Item(239)),
		MIXTURE_STEP_1_ONE_DOSE(65, 31.5, new Item(10915), new Item(3030), new Item(235)),
		MIXTURE_STEP_2_ONE_DOSE(65, 36, new Item(10923), new Item(10915), new Item(1526)),
		MIXTURE_STEP_1_TWO_DOSE(65, 42, new Item(10913), new Item(3028), new Item(235)),
		MIXTURE_STEP_2_TWO_DOSE(65, 48, new Item(10921), new Item(10913), new Item(1526)),
		MIXTURE_STEP_1_THREE_DOSE(65, 52.5, new Item(10911), new Item(3026), new Item(235)),
		MIXTURE_STEP_2_THREE_DOSE(65, 60, new Item(10919), new Item(10911), new Item(1526)),
		MIXTURE_STEP_1_FOUR_DOSE(65, 63, new Item(10909), new Item(3024), new Item(235)),
		MIXTURE_STEP_2_FOUR_DOSE(65, 72, new Item(10917), new Item(10909), new Item(1526)),
		SANFEW_SERUM_ONE_DONE(65, 96, new Item(10931), new Item(10923), new Item(10937)),
		SANFEW_SERUM_TWO_DONE(65, 128, new Item(10929), new Item(10921), new Item(10937)),
		SANFEW_SERUM_THREE_DONE(65, 160, new Item(10927), new Item(10919), new Item(10937)),
		SANFEW_SERUM_FOUR_DONE(65, 192, new Item(10925), new Item(10917), new Item(10937)),
		ANTIDOTE_PLUS(68, 155, 5943, new Item(5945), new Item(5942), new Item(6049)),
		ANTIFIRE(69, 157.5, 2452, new Item(2454), new Item(2483), new Item(241)),
		RANGING(72, 162.5, 2444, new Item(169), new Item(109), new Item(245)),
		ANCIENT_BREW(85, 190, 26340, new Item(26342), new Item(109), new Item(26368)),
		WEAPON_POISON_PLUS(73, 165, new Item(5937), new Item(5936), new Item(223)),
		MAGIC(76, 172.5, 3040, new Item(3042), new Item(2483), new Item(3138)),
		ZAMORAK_BREW(78, 175, 2450, new Item(189), new Item(111), new Item(247)),
		ANTIDOTE_PLUS_PLUS(79, 177.5, 5952, new Item(5954), new Item(5951), new Item(6051)),
		SARADOMIN_BREW(81, 180, 6685, new Item(6687), new Item(3002), new Item(6693)),
		WEAPON_POISON_PLUS_PLUS(82, 190, new Item(5940), new Item(5939), new Item(6018)),
		SUPER_COMBAT_WITH_HERB(90, 150, new Item(12695), new Item(269), new Item(2436), new Item(2440), new Item(2442)),
		SUPER_COMBAT_WITH_UNF(90, 150, new Item(12695), new Item(111), new Item(2436), new Item(2440), new Item(2442)),
		SUPER_ANTIFIRE_1(92, 130, new Item(21987), new Item(21975), new Item(2458)),
		SUPER_ANTIFIRE_2(92, 130, new Item(21984), new Item(21975), new Item(2456)),
		SUPER_ANTIFIRE_3(92, 130, new Item(21981), new Item(21975), new Item(2454)),
		SUPER_ANTIFIRE_4(92, 130, new Item(21978), new Item(21975), new Item(2452)),
		ANTI_VENOM_PLUS_4(94, 125, new Item(12913), new Item(269), new Item(12905)),
		BASTION(80, 155, new Item(ItemId.BASTION_POTION3), new Item(ItemId.CADANTINE_BLOOD_POTION_UNF), new Item(ItemId.WINE_OF_ZAMORAK)),
		CRYSTAL_DUST(0, 0, new Item(ItemId.CRYSTAL_DUST_23964, 10), new Item(CrystalShardKt.CRYSTAL_SHARD), pestleAndMortar),
		DIVINE_SUPER_ATTACK_1(70, 0.5, new Item(ItemId.DIVINE_SUPER_ATTACK_POTION1), new Item(ItemId.SUPER_ATTACK1), new Item(ItemId.CRYSTAL_DUST_23964, 1)),
		DIVINE_SUPER_ATTACK_2(70, 1.0, new Item(ItemId.DIVINE_SUPER_ATTACK_POTION2), new Item(ItemId.SUPER_ATTACK2), new Item(ItemId.CRYSTAL_DUST_23964, 2)),
		DIVINE_SUPER_ATTACK_3(70, 1.5, new Item(ItemId.DIVINE_SUPER_ATTACK_POTION3), new Item(ItemId.SUPER_ATTACK3), new Item(ItemId.CRYSTAL_DUST_23964, 3)),
		DIVINE_SUPER_ATTACK_4(70, 2.0, new Item(ItemId.DIVINE_SUPER_ATTACK_POTION4), new Item(ItemId.SUPER_ATTACK4), new Item(ItemId.CRYSTAL_DUST_23964, 4)),
		DIVINE_SUPER_STRENGTH_1(70, 0.5, new Item(ItemId.DIVINE_SUPER_STRENGTH_POTION1), new Item(ItemId.SUPER_STRENGTH1), new Item(ItemId.CRYSTAL_DUST_23964, 1)),
		DIVINE_SUPER_STRENGTH_2(70, 1.0, new Item(ItemId.DIVINE_SUPER_STRENGTH_POTION2), new Item(ItemId.SUPER_STRENGTH2), new Item(ItemId.CRYSTAL_DUST_23964, 2)),
		DIVINE_SUPER_STRENGTH_3(70, 1.5, new Item(ItemId.DIVINE_SUPER_STRENGTH_POTION3), new Item(ItemId.SUPER_STRENGTH3), new Item(ItemId.CRYSTAL_DUST_23964, 3)),
		DIVINE_SUPER_STRENGTH_4(70, 2.0, new Item(ItemId.DIVINE_SUPER_STRENGTH_POTION4), new Item(ItemId.SUPER_STRENGTH4), new Item(ItemId.CRYSTAL_DUST_23964, 4)),
		DIVINE_SUPER_DEFENCE_1(70, 0.5, new Item(ItemId.DIVINE_SUPER_DEFENCE_POTION1), new Item(ItemId.SUPER_DEFENCE1), new Item(ItemId.CRYSTAL_DUST_23964, 1)),
		DIVINE_SUPER_DEFENCE_2(70, 1.0, new Item(ItemId.DIVINE_SUPER_DEFENCE_POTION2), new Item(ItemId.SUPER_DEFENCE2), new Item(ItemId.CRYSTAL_DUST_23964, 2)),
		DIVINE_SUPER_DEFENCE_3(70, 1.5, new Item(ItemId.DIVINE_SUPER_DEFENCE_POTION3), new Item(ItemId.SUPER_DEFENCE3), new Item(ItemId.CRYSTAL_DUST_23964, 3)),
		DIVINE_SUPER_DEFENCE_4(70, 2.0, new Item(ItemId.DIVINE_SUPER_DEFENCE_POTION4), new Item(ItemId.SUPER_DEFENCE4), new Item(ItemId.CRYSTAL_DUST_23964, 4)),
		DIVINE_RANGING_1(74, 0.5, new Item(ItemId.DIVINE_RANGING_POTION1), new Item(ItemId.RANGING_POTION1), new Item(ItemId.CRYSTAL_DUST_23964, 1)),
		DIVINE_RANGING_2(74, 1.0, new Item(ItemId.DIVINE_RANGING_POTION2), new Item(ItemId.RANGING_POTION2), new Item(ItemId.CRYSTAL_DUST_23964, 2)),
		DIVINE_RANGING_3(74, 1.5, new Item(ItemId.DIVINE_RANGING_POTION3), new Item(ItemId.RANGING_POTION3), new Item(ItemId.CRYSTAL_DUST_23964, 3)),
		DIVINE_RANGING_4(74, 2.0, new Item(ItemId.DIVINE_RANGING_POTION4), new Item(ItemId.RANGING_POTION4), new Item(ItemId.CRYSTAL_DUST_23964, 4)),
		DIVINE_MAGIC_1(78, 0.5, new Item(ItemId.DIVINE_MAGIC_POTION1), new Item(ItemId.MAGIC_POTION1), new Item(ItemId.CRYSTAL_DUST_23964, 1)),
		DIVINE_MAGIC_2(78, 1.0, new Item(ItemId.DIVINE_MAGIC_POTION2), new Item(ItemId.MAGIC_POTION2), new Item(ItemId.CRYSTAL_DUST_23964, 2)),
		DIVINE_MAGIC_3(78, 1.5, new Item(ItemId.DIVINE_MAGIC_POTION3), new Item(ItemId.MAGIC_POTION3), new Item(ItemId.CRYSTAL_DUST_23964, 3)),
		DIVINE_MAGIC_4(78, 2.0, new Item(ItemId.DIVINE_MAGIC_POTION4), new Item(ItemId.MAGIC_POTION4), new Item(ItemId.CRYSTAL_DUST_23964, 4)),
		DIVINE_BATTLEMAGE_1(86, 0.5, new Item(ItemId.DIVINE_BATTLEMAGE_POTION1), new Item(ItemId.BATTLEMAGE_POTION1), new Item(ItemId.CRYSTAL_DUST_23964, 1)),
		DIVINE_BATTLEMAGE_2(86, 1.0, new Item(ItemId.DIVINE_BATTLEMAGE_POTION2), new Item(ItemId.BATTLEMAGE_POTION2), new Item(ItemId.CRYSTAL_DUST_23964, 2)),
		DIVINE_BATTLEMAGE_3(86, 1.5, new Item(ItemId.DIVINE_BATTLEMAGE_POTION3), new Item(ItemId.BATTLEMAGE_POTION3), new Item(ItemId.CRYSTAL_DUST_23964, 3)),
		DIVINE_BATTLEMAGE_4(86, 2.0, new Item(ItemId.DIVINE_BATTLEMAGE_POTION4), new Item(ItemId.BATTLEMAGE_POTION4), new Item(ItemId.CRYSTAL_DUST_23964, 4)),
		DIVINE_BASTION_1(86, 0.5, new Item(ItemId.DIVINE_BASTION_POTION1), new Item(ItemId.BASTION_POTION1), new Item(ItemId.CRYSTAL_DUST_23964, 1)),
		DIVINE_BASTION_2(86, 1.0, new Item(ItemId.DIVINE_BASTION_POTION2), new Item(ItemId.BASTION_POTION2), new Item(ItemId.CRYSTAL_DUST_23964, 2)),
		DIVINE_BASTION_3(86, 1.5, new Item(ItemId.DIVINE_BASTION_POTION3), new Item(ItemId.BASTION_POTION3), new Item(ItemId.CRYSTAL_DUST_23964, 3)),
		DIVINE_BASTION_4(86, 2.0, new Item(ItemId.DIVINE_BASTION_POTION4), new Item(ItemId.BASTION_POTION4), new Item(ItemId.CRYSTAL_DUST_23964, 4)),
		DIVINE_SUPER_COMBAT_1(97, 0.5, new Item(ItemId.DIVINE_SUPER_COMBAT_POTION1), new Item(ItemId.SUPER_COMBAT_POTION1), new Item(ItemId.CRYSTAL_DUST_23964, 1)),
		DIVINE_SUPER_COMBAT_2(97, 1.0, new Item(ItemId.DIVINE_SUPER_COMBAT_POTION2), new Item(ItemId.SUPER_COMBAT_POTION2), new Item(ItemId.CRYSTAL_DUST_23964, 2)),
		DIVINE_SUPER_COMBAT_3(97, 1.5, new Item(ItemId.DIVINE_SUPER_COMBAT_POTION3), new Item(ItemId.SUPER_COMBAT_POTION3), new Item(ItemId.CRYSTAL_DUST_23964, 3)),
		DIVINE_SUPER_COMBAT_4(97, 2.0, new Item(ItemId.DIVINE_SUPER_COMBAT_POTION4), new Item(ItemId.SUPER_COMBAT_POTION4), new Item(ItemId.CRYSTAL_DUST_23964, 4)),

		EGNIOL_POTION_GAUNTLET(1, 10, new Item(ItemId.EGNIOL_POTION_3), new Item(ItemId.GRYM_POTION_UNF), new Item(ItemId.CRYSTAL_DUST_23867, 10)),
		EGNIOL_POTION_GAUNTLET_CORRUPTED(1, 10, new Item(ItemId.EGNIOL_POTION_3), new Item(ItemId.GRYM_POTION_UNF), new Item(ItemId.CORRUPTED_DUST, 10)),
		CORRUPTED_DUST(0, 0, new Item(ItemId.CORRUPTED_DUST, 10), new Item(ItemId.CORRUPTED_SHARDS, 10), pestleAndMortarGauntlet),
		CRYSTAL_DUST_GAUNTLET(0, 0, new Item(ItemId.CRYSTAL_DUST_23867, 10), new Item(ItemId.CRYSTAL_SHARDS, 10), pestleAndMortarGauntlet),
		GRYM_POTION_UNF(0, 0, new Item(ItemId.GRYM_POTION_UNF), new Item(ItemId.GRYM_LEAF_23875), new Item(ItemId.WATERFILLED_VIAL)),
		GRYM_POTION_UNF_CORRUPTED(0, 0, new Item(ItemId.GRYM_POTION_UNF), new Item(ItemId.GRYM_LEAF), new Item(ItemId.WATERFILLED_VIAL)),
		CRUSH_CRYSTAL_BOWSTRING(0, 0, new Item(ItemId.CRYSTAL_SHARDS, 80), new Item(ItemId.CRYSTALLINE_BOWSTRING), pestleAndMortarGauntlet),
		CRUSH_CRYSTAL_SPIKE(0, 0, new Item(ItemId.CRYSTAL_SHARDS, 80), new Item(ItemId.CRYSTAL_SPIKE), pestleAndMortarGauntlet),
		CRUSH_CRYSTAL_ORB(0, 0, new Item(ItemId.CRYSTAL_SHARDS, 80), new Item(ItemId.CRYSTAL_ORB), pestleAndMortarGauntlet),
		CRUSH_CORRUPTED_BOWSTRING(0, 0, new Item(ItemId.CORRUPTED_SHARDS, 80), new Item(ItemId.CORRUPTED_BOWSTRING), pestleAndMortarGauntlet),
		CRUSH_CORRUPTED_SPIKE(0, 0, new Item(ItemId.CORRUPTED_SHARDS, 80), new Item(ItemId.CORRUPTED_SPIKE), pestleAndMortarGauntlet),
		CRUSH_CORRUPTED_ORB(0, 0, new Item(ItemId.CORRUPTED_SHARDS, 80), new Item(ItemId.CORRUPTED_ORB), pestleAndMortarGauntlet),
		FORGOTTEN_BREW(91, 145, new Item(ItemId.FORGOTTEN_BREW4), new Item(ItemId.ANCIENT_BREW_4), new Item(ItemId.ANCIENT_ESSENCE, 80)),
		;

		public static final HerbloreData[] values = values();
		public static final HashMap<Integer, HerbloreData> HERBLORE = new HashMap<>(values.length);
		public static final Set<HerbloreData> INSTANT_ACTIONS = new HashSet<>();
		private final Item product;
		private final Item[] materials;
		private final int level;
		private final double xp;
		private final int fourDosePotion;

		HerbloreData(final int level, final double xp, final int fourDosePotion, final Item product, final Item... materials) {
			this.level = level;
			this.xp = xp;
			this.product = product;
			this.materials = materials;
			this.fourDosePotion = fourDosePotion;
		}

		HerbloreData(final int level, final double xp, final Item product, final Item... materials) {
			this(level, xp, -1, product, materials);
		}

		@Override
		public String toString() {
			return name().toLowerCase().replace("_", " ");
		}

		static {
			for (final Combine.HerbloreData data : values) {
				HERBLORE.put(data.getMaterials()[0].getId(), data);
			}

			INSTANT_ACTIONS.add(EGNIOL_POTION_GAUNTLET);
			INSTANT_ACTIONS.add(EGNIOL_POTION_GAUNTLET_CORRUPTED);
			INSTANT_ACTIONS.add(CORRUPTED_DUST);
			INSTANT_ACTIONS.add(CRYSTAL_DUST_GAUNTLET);
			INSTANT_ACTIONS.add(GRYM_POTION_UNF);
			INSTANT_ACTIONS.add(GRYM_POTION_UNF_CORRUPTED);
			INSTANT_ACTIONS.add(CRUSH_CRYSTAL_BOWSTRING);
			INSTANT_ACTIONS.add(CRUSH_CRYSTAL_SPIKE);
			INSTANT_ACTIONS.add(CRUSH_CRYSTAL_ORB);
			INSTANT_ACTIONS.add(CRUSH_CORRUPTED_BOWSTRING);
			INSTANT_ACTIONS.add(CRUSH_CORRUPTED_SPIKE);
			INSTANT_ACTIONS.add(CRUSH_CORRUPTED_ORB);
		}

		public static HerbloreData get(final int id) {
			return HERBLORE.get(id);
		}

		public static HerbloreData getDataByMaterial(final Item from, final Item to) {
			for (final Combine.HerbloreData data : HerbloreData.values) {
				for (final Item i : data.getMaterials()) {
					if (i.getId() == from.getId()) {
						for (final Item o : data.getMaterials()) {
							if (o.getId() == to.getId() && i != o) {
								return data;
							}
						}
					}
				}
			}
			return null;
		}

		public static boolean hasRequirements(final Player player, final HerbloreData potion) {
			if (potion.getXp() == 0 && player.getSkills().getLevel(SkillConstants.HERBLORE) < potion.getLevel()) {
				player.getDialogueManager().start(new PlainChat(player, "You need level " + potion.getLevel() + " Herblore to combine those."));
				return false;
			}
			for (int i = 0; i < potion.getMaterials().length; i++) {
				final Item material = potion.getMaterials()[i];
				if ((material.getId() == pestleAndMortar.getId() || material.getId() == pestleAndMortarGauntlet.getId()) && !player.getInventory().containsItem(material)) {
					player.sendMessage("You need a pestle and mortar to do that.");
					return false;
				}
				if (material.getId() == knife.getId() && !player.getInventory().containsItem(material)) {
					player.sendMessage("You need a knife to do that.");
					return false;
				}
				if (!player.getInventory().containsItem(potion.getMaterials()[i])) {
					player.sendMessage("Nothing interesting happens.");
					return false;
				}
			}
			if (player.getSkills().getLevel(SkillConstants.HERBLORE) < potion.getLevel()) {
				player.sendMessage("You need level " + potion.getLevel() + " Herblore to mix a " + TextUtils.capitalizeFirstCharacter(potion.getProduct().getDefinitions().getName()) + ".");
				return false;
			}
			return true;
		}

		public Item getProduct() {
			return product;
		}

		public Item[] getMaterials() {
			return materials;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getFourDosePotion() {
			return fourDosePotion;
		}
	}

	private final HerbloreData data;
	private final int amount;
	private int cycle;

	@Override
	public boolean start() {
		return HerbloreData.hasRequirements(player, data);
	}

	@Override
	public boolean process() {
		if (cycle >= amount) {
			return false;
		}
		for (int i = 0; i < data.getMaterials().length; i++) {
			if (!player.getInventory().containsItems(data.getMaterials()[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int processWithDelay() {
		for (final Item material : data.getMaterials()) {
			if (material.getId() != pestleAndMortarGauntlet.getId() && material.getId() != pestleAndMortar.getId() && material.getId() != knife.getId()) {
				player.getInventory().deleteItem(material);
			}
		}
		final Item item = data.equals(HerbloreData.LAVA_SCALE_SHARD) ? new Item(data.getProduct().getId(), DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD3, player) ? Utils.random(6, 9) : Utils.random(3, 6)) : new Item(data.getProduct());
		switch (data) {
			case SARADOMIN_BREW -> {
				player.getDailyChallengeManager().update(SkillingChallenge.MAKE_SARADOMIN_BREWS);
				player.getAchievementDiaries().update(FaladorDiary.MIX_SARADOMIN_BREW);
			}
			case COMBAT -> player.getAchievementDiaries().update(DesertDiary.CREATE_COMBAT_POTION);
			case SUPER_COMBAT_WITH_HERB, SUPER_COMBAT_WITH_UNF -> {
				player.getDailyChallengeManager().update(SkillingChallenge.MAKE_SUPER_COMBAT_POTIONS);
				player.getAchievementDiaries().update(VarrockDiary.CREATE_SUPERCOMBAT_POTION);
			}
			case SUPER_DEFENCE -> {
				player.getAchievementDiaries().update(FremennikDiary.MIX_SUPER_DEFENCE);
				SherlockTask.CREATE_SUPER_DEFENCE_POTION.progress(player);
			}
			case IRIT_POTION_UNF -> player.getAchievementDiaries().update(KandarinDiary.CREATE_SUPER_ANTIPOISON, 1);
			case SUPER_ANTIPOISON -> player.getAchievementDiaries().update(KandarinDiary.CREATE_SUPER_ANTIPOISON, 2);
			case GUTHIX_BALANCE_1, GUTHIX_BALANCE_2, GUTHIX_BALANCE_3, GUTHIX_BALANCE_4 -> player.getAchievementDiaries().update(MorytaniaDiary.MIX_A_GUTHIX_BALANCE);
			case STRENGTH -> player.getAchievementDiaries().update(KourendDiary.CREATE_STRENGTH_POTION);
			case ANTI_VENOM_PLUS_4 -> SherlockTask.MIX_ANTIVENOM.progress(player);
			case WEAPON_POISON_PLUS_PLUS -> player.getDailyChallengeManager().update(SkillingChallenge.MAKE_WEAPON_POISON_PLUS_PLUS_POTIONS);
			case PRAYER -> player.getDailyChallengeManager().update(SkillingChallenge.MAKE_PRAYER_POTIONS);
			case SUPER_RESTORE -> player.getDailyChallengeManager().update(SkillingChallenge.MAKE_SUPER_RESTORES);
			case EGNIOL_POTION_GAUNTLET -> player.putBooleanTemporaryAttribute("egniol_potion_made", true);
		}
		final MutableBoolean bool = new MutableBoolean();
		if (data.fourDosePotion != -1) {
			if (player.getEquipment().getId(EquipmentSlot.AMULET) == 21163) {
				if (Utils.randomDouble() < 0.05F) {
					final int uses = player.getNumericAttribute("amulet of chemistry uses").intValue() + 1;
					player.addAttribute("amulet of chemistry uses", uses % 5);
					item.setId(data.fourDosePotion);
					if (uses == 5) {
						player.getEquipment().set(EquipmentSlot.AMULET, null);
						player.sendMessage("Your amulet of chemistry grants you an extra dose. " + Colour.RED.wrap("It then crumbles to dust."));
						bool.setValue(player.getNumericAttribute("AOC: cancel combining when out of charges").intValue() == 1);
					} else {
						player.sendFilteredMessage("Your amulet of chemistry grants you an extra dose. " + Colour.RED.wrap("It has " + (5 - uses) + " charge" + (uses == 9 ? "" : "s") + " left."));
					}
				}
			}
		}
		if(data != ANCIENT_ICON && data != HerbloreData.VENATOR_SHARD && player.getBoonManager().hasBoon(Mixologist.class) && Mixologist.roll()) {
			player.sendFilteredMessage("Your Mixology boon grants you an additional potion.");
			player.getSkills().addXp(SkillConstants.HERBLORE, data.getXp());
			item.setAmount(item.getAmount() + 1);
		}
		player.getInventory().addItem(item);
		if (data.getXp() != 0) {
			player.getSkills().addXp(SkillConstants.HERBLORE, data.getXp());
		}
		if (ArrayUtils.contains(data.getMaterials(), pestleAndMortar) || ArrayUtils.contains(data.getMaterials(), pestleAndMortarGauntlet)) {
			player.setAnimation(grindingAnimation);
		} else if (ArrayUtils.contains(data.getMaterials(), knife)) {
			player.setAnimation(cuttingAnimation);
			player.sendSound(new SoundEffect(2605));
			player.sendFilteredMessage("You cut the chocolate bar into tiny pieces.");
		} else {
			player.setAnimation(brewingAnimation);
		}
		cycle++;
		return bool.isTrue() ? -1 : 1;
	}

	public Combine(HerbloreData data, int amount) {
		this.data = data;
		this.amount = amount;
	}
}
