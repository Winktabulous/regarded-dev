package com.zenyte.game.content.skills.fletching;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.HashMap;

public class FletchingDefinitions {
	public static final Item HEADLESS_ARROWS = new Item(53);
	public static final Item flightedOgreArrow = new Item(ItemId.FLIGHTED_OGRE_ARROW);
	public static final Item FEATHER = new Item(314);
	public static final Item BOW_STRING = new Item(1777);
	public static final Item CROSSBOW_STRING = new Item(9438);
	public static final Item JAVELIN_SHAFT = new Item(19584);
	public static final Animation ANIMATION = new Animation(1248);
	public static final Item KNIFE = new Item(946);
	public static final Item CHISEL = new Item(1755);
	public static final Item[][] PRODUCTS = new Item[][] {
			{new Item(52, 15), new Item(19584, 15), new Item(50), new Item(48), new Item(9440)},
			{new Item(52, 30), new Item(54), new Item(56), new Item(9442), new Item(22251)},
			{new Item(52, 45), new Item(60), new Item(58), new Item(9444), new Item(22254)},
			{new Item(52, 60), new Item(64), new Item(62), new Item(9448), new Item(22257)},
			{new Item(9446)},
			{new Item(9450)},
			{new Item(52, 75), new Item(68), new Item(66), new Item(9452), new Item(22260)},
			{new Item(52, 90), new Item(72), new Item(70), new Item(21952), new Item(22263)},
			{new Item(52, 105), new Item(22266)}
	};
	public static final Item[][] MATERIALS = new Item[][] {
			{new Item(1511), new Item(1511), new Item(1511), new Item(1511), new Item(1511)},
			{new Item(1521), new Item(1521), new Item(1521), new Item(1521), new Item(1521, 2)},
			{new Item(1519), new Item(1519), new Item(1519), new Item(1519), new Item(1519, 2)},
			{new Item(1517), new Item(1517), new Item(1517), new Item(1517), new Item(1517, 2)},
			{new Item(6333)},
			{new Item(6332)},
			{new Item(1515), new Item(1515), new Item(1515), new Item(1515), new Item(1515, 2)},
			{new Item(1513), new Item(1513), new Item(1513), new Item(1513), new Item(1513, 2)},
			{new Item(19669), new Item(19669, 2)}
	};
	public static final int[][] LEVELS = new int[][] {{1, 3, 5, 10, 9}, {15, 20, 25, 24, 27}, {30, 35, 40, 39, 42}, {45, 50, 55, 54, 57}, {46}, {61}, {60, 65, 70, 69, 72}, {75, 80, 85, 78, 87}, {90, 92}};
	public static final double[][] EXPERIENCE = new double[][] {{5, 5, 5, 10, 6}, {10, 16.5, 25, 16, 50}, {15, 33.3, 41.5, 22, 83}, {20, 50, 58.3, 32, 116.5}, {27}, {41}, {25, 67.5, 75, 50, 150}, {30, 83.3, 91.5, 70, 183}, {35, 216}};


	public enum BoltTipFletchingData {
		OPAL_BOLT_TIPS(11, 1.6, new Animation(890), new Item(45, 12), new Item(1609)), JADE_BOLT_TIPS(26, 2.4, new Animation(891), new Item(9187, 12), new Item(1611)), PEARL_BOLT_TIPS(41, 4, new Animation(890), new Item(46, 6), new Item(411)), RED_TOPAZ_BOLT_TIPS(48, 4, new Animation(892), new Item(9188, 12), new Item(1613)), SAPPHIRE_BOLT_TIPS(56, 4, new Animation(888), new Item(9189, 12), new Item(1607)), EMERALD_BOLT_TIPS(58, 5.5, new Animation(889), new Item(9190, 12), new Item(1605)), RUBY_BOLT_TIPS(63, 6, new Animation(887), new Item(9191, 12), new Item(1603)), DIAMOND_BOLT_TIPS(65, 7.5, new Animation(886), new Item(9192, 12), new Item(1601)), DRAGONSTONE_BOLT_TIPS(71, 8.2, new Animation(885), new Item(9193, 12), new Item(1615)), ONYX_BOLT_TIPS(73, 33.6, new Animation(2717), new Item(9194, 24), new Item(6573));
		private final Item product;
		private final Item material;
		private final int level;
		private final double xp;
		private final Animation animation;
		public static final BoltTipFletchingData[] VALUES = values();

		BoltTipFletchingData(final int level, final double xp, final Animation animation, final Item product, final Item material) {
			this.level = level;
			this.xp = xp;
			this.animation = animation;
			this.product = product;
			this.material = material;
		}

		public static BoltTipFletchingData getDataByMaterial(final Item from, final Item to) {
			for (final FletchingDefinitions.BoltTipFletchingData data : BoltTipFletchingData.VALUES) {
				if ((from.getId() == data.getMaterial().getId() || to.getId() == data.getMaterial().getId()) && (from.getId() == CHISEL.getId() || to.getId() == CHISEL.getId())) return data;
			}
			return null;
		}

		public static boolean hasRequirements(final Player player, final BoltTipFletchingData data) {
			if (player.getSkills().getLevel(SkillConstants.FLETCHING) < data.getLevel()) {
				player.getDialogueManager().start(new PlainChat(player, "You need a Fletching level of " + data.getLevel() + " to do that."));
				return false;
			}
			return true;
		}

		public Item getProduct() {
			return product;
		}

		public Item getMaterial() {
			return material;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public Animation getAnimation() {
			return animation;
		}
	}


	public enum AmmunitionFletchingData {
		HEADLESS_ARROW(AmmunitionType.ARROW, 1, 1, new Item(53), new Item(52), FEATHER),
		BRONZE_ARROW(AmmunitionType.ARROW, 1, 1.333333, new Item(882), new Item(39), HEADLESS_ARROWS),
		IRON_ARROW(AmmunitionType.ARROW, 15, 2.5, new Item(884), new Item(40), HEADLESS_ARROWS),
		STEEL_ARROW(AmmunitionType.ARROW, 30, 5, new Item(886), new Item(41), HEADLESS_ARROWS),
		MITHRIL_ARROW(AmmunitionType.ARROW, 45, 7.5, new Item(888), new Item(42), HEADLESS_ARROWS),
		ADAMANT_ARROW(AmmunitionType.ARROW, 60, 10, new Item(890), new Item(43), HEADLESS_ARROWS),
		BROAD_ARROW(AmmunitionType.ARROW, 52, 10, new Item(4160), new Item(11874), HEADLESS_ARROWS),
		RUNE_ARROW(AmmunitionType.ARROW, 75, 12.5, new Item(892), new Item(44), HEADLESS_ARROWS),
		AMETHYST_ARROW(AmmunitionType.ARROW, 82, 13.5, new Item(21326), new Item(21350), HEADLESS_ARROWS),
		DRAGON_ARROW(AmmunitionType.ARROW, 90, 15, new Item(11212), new Item(11237), HEADLESS_ARROWS),
		OGRE_ARROW_SHAFT(AmmunitionType.ARROW, 5, 6.4, new Item(ItemId.OGRE_ARROW_SHAFT, 1), new Item(ItemId.ACHEY_TREE_LOGS)),
		FLIGHTED_OGRE_ARROWS(AmmunitionType.ARROW, 5, 0.9, new Item(ItemId.FLIGHTED_OGRE_ARROW), new Item(ItemId.OGRE_ARROW_SHAFT), new Item(ItemId.FEATHER, 4)),
		WOLFBONE_TIPS(AmmunitionType.ARROW, 5, 10, new Item(ItemId.WOLFBONE_ARROWTIPS, 1), new Item(ItemId.WOLF_BONES)),
		OGRE_ARROWS(AmmunitionType.ARROW, 5, 1, new Item(ItemId.OGRE_ARROW), flightedOgreArrow, new Item(ItemId.WOLFBONE_ARROWTIPS)),
		BRONZE_BRUTAL(AmmunitionType.ARROW, 7, 1.4, new Item(ItemId.BRONZE_BRUTAL), flightedOgreArrow, new Item(ItemId.BRONZE_NAILS)),
		IRON_BRUTAL(AmmunitionType.ARROW, 18, 2.6, new Item(ItemId.IRON_BRUTAL), flightedOgreArrow, new Item(ItemId.IRON_NAILS)),
		STEEL_BRUTAL(AmmunitionType.ARROW, 33, 5.1, new Item(ItemId.STEEL_BRUTAL), flightedOgreArrow, new Item(ItemId.STEEL_NAILS)),
		BLACK_BRUTAL(AmmunitionType.ARROW, 38, 6.5, new Item(ItemId.BLACK_BRUTAL), flightedOgreArrow, new Item(ItemId.BLACK_NAILS)),
		MITHRIL_BRUTAL(AmmunitionType.ARROW, 49, 7.5, new Item(ItemId.MITHRIL_BRUTAL), flightedOgreArrow, new Item(ItemId.MITHRIL_NAILS)),
		ADAMANT_BRUTAL(AmmunitionType.ARROW, 62, 10.2, new Item(ItemId.ADAMANT_BRUTAL), flightedOgreArrow, new Item(ItemId.ADAMANTITE_NAILS)),
		RUNE_BRUTAL(AmmunitionType.ARROW, 77, 12.5, new Item(ItemId.RUNE_BRUTAL), flightedOgreArrow, new Item(ItemId.RUNE_NAILS)),
		BRONZE_BOLT(AmmunitionType.BOLT, 9, 0.5, new Item(877), new Item(9375), FEATHER),
		BLURITE_BOLT(AmmunitionType.BOLT, 24, 1, new Item(9139), new Item(9376), FEATHER),
		IRON_BOLT(AmmunitionType.BOLT, 39, 1.5, new Item(9140), new Item(9377), FEATHER),
		SILVER_BOLT(AmmunitionType.BOLT, 43, 2.5, new Item(9145), new Item(9382), FEATHER),
		STEEL_BOLT(AmmunitionType.BOLT, 46, 3.5, new Item(9141), new Item(9378), FEATHER),
		BARBED_BOLT(AmmunitionType.BOLT, 51, 9.5, new Item(881), new Item(47), FEATHER),
		MITHRIL_BOLT(AmmunitionType.BOLT, 54, 5, new Item(9142), new Item(9379), FEATHER),
		ADAMANTITE_BOLT(AmmunitionType.BOLT, 61, 7, new Item(9143), new Item(9380), FEATHER),
		BROAD_BOLT(AmmunitionType.BOLT, 55, 3, new Item(11875), new Item(11876), FEATHER),
		RUNITE_BOLT(AmmunitionType.BOLT, 69, 10, new Item(9144), new Item(9381), FEATHER),
		DRAGON_BOLT(AmmunitionType.BOLT, 84, 12, new Item(21905), new Item(21930), FEATHER),
		OPAL_BOLT(AmmunitionType.TIPPED_BOLT, 11, 1.6, new Item(879), new Item(45), new Item(877)),
		JADE_BOLT(AmmunitionType.TIPPED_BOLT, 26, 2.4, new Item(9335), new Item(9187), new Item(9139)),
		PEARL_BOLT(AmmunitionType.TIPPED_BOLT, 41, 3.2, new Item(880), new Item(46), new Item(9140)),
		RED_TOPAZ_BOLT(AmmunitionType.TIPPED_BOLT, 48, 3.9, new Item(9336), new Item(9188), new Item(9141)),
		SAPPHIRE_BOLT(AmmunitionType.TIPPED_BOLT, 56, 4.7, new Item(9337), new Item(9189), new Item(9142)),
		EMERALD_BOLT(AmmunitionType.TIPPED_BOLT, 58, 5.5, new Item(9338), new Item(9190), new Item(9142)),
		RUBY_BOLT(AmmunitionType.TIPPED_BOLT, 63, 6.3, new Item(9339), new Item(9191), new Item(9143)),
		DIAMOND_BOLT(AmmunitionType.TIPPED_BOLT, 65, 7, new Item(9340), new Item(9192), new Item(9143)),
		DRAGONSTONE_BOLT(AmmunitionType.TIPPED_BOLT, 71, 8.2, new Item(9341), new Item(9193), new Item(9144)),
		ONYX_BOLT(AmmunitionType.TIPPED_BOLT, 73, 9.4, new Item(9342), new Item(9194), new Item(9144)),
		AMETHYST_BROAD_BOLT(AmmunitionType.TIPPED_BOLT, 76, 10.6, new Item(21316), new Item(21338), new Item(11875)),
		OPAL_DRAGON_BOLT(AmmunitionType.TIPPED_BOLT, 11, 1.6, new Item(21955), new Item(45), new Item(21905)),
		JADE_DRAGON_BOLT(AmmunitionType.TIPPED_BOLT, 26, 1.6, new Item(21957), new Item(9187), new Item(21905)),
		PEARL_DRAGON_BOLT(AmmunitionType.TIPPED_BOLT, 41, 3.2, new Item(21959), new Item(46), new Item(21905)),
		TOPAZ_DRAGON_BOLT(AmmunitionType.TIPPED_BOLT, 48, 3.9, new Item(21961), new Item(9188), new Item(21905)),
		SAPPHIRE_DRAGON_BOLT(AmmunitionType.TIPPED_BOLT, 56, 4.7, new Item(21963), new Item(9189), new Item(21905)),
		EMERALD_DRAGON_BOLT(AmmunitionType.TIPPED_BOLT, 58, 5.5, new Item(21965), new Item(9190), new Item(21905)),
		RUBY_DRAGON_BOLT(AmmunitionType.TIPPED_BOLT, 63, 6.3, new Item(21967), new Item(9191), new Item(21905)),
		DIAMOND_DRAGON_BOLT(AmmunitionType.TIPPED_BOLT, 65, 7, new Item(21969), new Item(9192), new Item(21905)),
		DRAGONSTONE_DRAGON_BOLT(AmmunitionType.TIPPED_BOLT, 71, 9.4, new Item(21971), new Item(9193), new Item(21905)),
		ONYX_DRAGON_BOLT(AmmunitionType.TIPPED_BOLT, 73, 10.6, new Item(21973), new Item(9194), new Item(21905)),
		BRONZE_DART(AmmunitionType.DART, 10, 1.8, new Item(806), new Item(819), FEATHER),
		IRON_DART(AmmunitionType.DART, 22, 3.8, new Item(807), new Item(820), FEATHER),
		STEEL_DART(AmmunitionType.DART, 37, 7.5, new Item(808), new Item(821), FEATHER),
		MITHRIL_DART(AmmunitionType.DART, 52, 11.2, new Item(809), new Item(822), FEATHER),
		ADAMANT_DART(AmmunitionType.DART, 67, 15, new Item(810), new Item(823), FEATHER),
		RUNE_DART(AmmunitionType.DART, 81, 18.8, new Item(811), new Item(824), FEATHER),
		AMETHYST_DART(AmmunitionType.DART, 90, 21, new Item(ItemId.AMETHYST_DART), new Item(ItemId.AMETHYST_DART_TIP), FEATHER),
		DRAGON_DART(AmmunitionType.DART, 95, 25, new Item(11230), new Item(11232), FEATHER),
		BRONZE_JAVELIN(AmmunitionType.JAVELIN, 3, 1, new Item(825), new Item(19570), JAVELIN_SHAFT),
		IRON_JAVELIN(AmmunitionType.JAVELIN, 17, 2, new Item(826), new Item(19572), JAVELIN_SHAFT),
		STEEL_JAVELIN(AmmunitionType.JAVELIN, 32, 5, new Item(827), new Item(19574), JAVELIN_SHAFT),
		MITHRIL_JAVELIN(AmmunitionType.JAVELIN, 47, 8, new Item(828), new Item(19576), JAVELIN_SHAFT),
		ADAMANT_JAVELIN(AmmunitionType.JAVELIN, 62, 10, new Item(829), new Item(19578), JAVELIN_SHAFT),
		RUNE_JAVELIN(AmmunitionType.JAVELIN, 77, 12.4, new Item(830), new Item(19580), JAVELIN_SHAFT),
		AMETHYST_JAVELIN(AmmunitionType.JAVELIN, 84, 13.5, new Item(21318), new Item(21352), JAVELIN_SHAFT),
		DRAGON_JAVELIN(AmmunitionType.JAVELIN, 92, 15, new Item(19484), new Item(19582), JAVELIN_SHAFT);
		private final Item product;
		private final Item[] materials;
		private final int level;
		private final double xp;
		private final AmmunitionType type;
		public static final AmmunitionFletchingData[] VALUES = values();

		AmmunitionFletchingData(final AmmunitionType type, final int level, final double xp, final Item product, final Item... materials) {
			this.type = type;
			this.level = level;
			this.xp = xp;
			this.product = product;
			this.materials = materials;
		}

		public String getProcessMessage(final AmmunitionFletchingData data, final int sets, final int quantity, final int amount) {
			switch (data.getType()) {
			case DART: 
				return "You attach " + (amount == 1 ? "a feather" : "feathers") + " to " + (amount == 1 ? "an" : amount >= sets ? sets : quantity) + " " + data.getMaterials()[0].getDefinitions().getName().toLowerCase() + (amount > 1 && !data.getMaterials()[0].getDefinitions().getName().endsWith("s") && !data.getMaterials()[0].getDefinitions().getName().endsWith("(unf)") ? "s." : ".");
			case JAVELIN: 
				return "You attach " + (amount == 1 ? "a javelin head" : "javelin heads") + " to " + (amount == 1 ? "an" : amount >= sets ? sets : quantity) + " " + data.getMaterials()[1].getDefinitions().getName().toLowerCase() + (amount > 1 && !data.getMaterials()[1].getDefinitions().getName().endsWith("s") && !data.getMaterials()[1].getDefinitions().getName().endsWith("(unf)") ? "s." : ".");
			case TIPPED_BOLT: 
				return "You attach " + (amount == 1 ? "a bolt tip" : "bolt tips") + " to " + (amount == 1 ? "an" : amount >= sets ? sets : quantity) + " " + data.getMaterials()[1].getDefinitions().getName().toLowerCase() + (amount > 1 && !data.getMaterials()[1].getDefinitions().getName().endsWith("s") && !data.getMaterials()[1].getDefinitions().getName().endsWith("(unf)") ? "s." : ".");
			default: 
				return "You attach " + (amount == 1 ? "a feather" : "feathers") + " to " + (amount == 1 ? "an" : amount >= sets ? sets : quantity) + " " + data.getMaterials()[0].getDefinitions().getName().toLowerCase() + (amount > 1 && !data.getMaterials()[0].getDefinitions().getName().endsWith("s") && !data.getMaterials()[0].getDefinitions().getName().endsWith("(unf)") ? "s." : ".");
			}
		}

		public static boolean hasRequirements(final Player player, final AmmunitionFletchingData data) {
			if (player.getSkills().getLevel(SkillConstants.FLETCHING) < data.getLevel()) {
				player.getDialogueManager().start(new PlainChat(player, "You need a Fletching level of " + data.getLevel() + " to do that."));
				return false;
			}
			return true;
		}

		public static AmmunitionFletchingData getDataByMaterial(final Item from, final Item to) {
			for (final FletchingDefinitions.AmmunitionFletchingData data : AmmunitionFletchingData.VALUES) {
				loop:
				for (final Item i : data.getMaterials()) {
					if (i.getId() == from.getId() || (data == WOLFBONE_TIPS && from.getId() == ItemId.CHISEL && to.getId() != ItemId.CHISEL) || (data == OGRE_ARROW_SHAFT && from.getId() == ItemId.KNIFE && to.getId() != ItemId.KNIFE)) {
						for (final Item o : data.getMaterials()) {
							if (o.getId() == to.getId() && (i != o || data == OGRE_ARROW_SHAFT || data == WOLFBONE_TIPS) || (data == WOLFBONE_TIPS && to.getId() == ItemId.CHISEL && from.getId() != ItemId.CHISEL) || (data == OGRE_ARROW_SHAFT && to.getId() == ItemId.KNIFE && from.getId() != ItemId.KNIFE)) return data;
						}
						continue loop;
					}
				}
			}
			return null;
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

		public AmmunitionType getType() {
			return type;
		}
	}


	public enum BowFletchingData {
		SHORTBOW(5, 5, new Animation(6678), new Item(841), new Item(50), BOW_STRING),
		LONGBOW(10, 10, new Animation(6684), new Item(839), new Item(48), BOW_STRING),
		OAK_SHORTBOW(20, 16.5, new Animation(6679), new Item(843), new Item(54), BOW_STRING),
		OAK_LONGBOW(25, 25, new Animation(6685), new Item(845), new Item(56), BOW_STRING),
		WILLOW_SHORTBOW(35, 33.3, new Animation(6680), new Item(849), new Item(60), BOW_STRING),
		WILLOW_LONGBOW(40, 41.5, new Animation(6686), new Item(847), new Item(58), BOW_STRING),
		MAPLE_SHORTBOW(50, 50, new Animation(6681), new Item(853), new Item(64), BOW_STRING),
		MAPLE_LONGBOW(55, 58.3, new Animation(6687), new Item(851), new Item(62), BOW_STRING),
		YEW_SHORTBOW(65, 67.5, new Animation(6682), new Item(857), new Item(68), BOW_STRING),
		YEW_LONGBOW(70, 75, new Animation(6688), new Item(855), new Item(66), BOW_STRING),
		MAGIC_SHORTBOW(80, 83.3, new Animation(6683), new Item(861), new Item(72), BOW_STRING),
		MAGIC_LONGBOW(85, 91.5, new Animation(6689), new Item(859), new Item(70), BOW_STRING),
		BRONZE_CROSSBOW_U(9, 12, Animation.STOP, new Item(9454), new Item(9420), new Item(9440)),
		BLURITE_CROSSBOW_U(24, 32, Animation.STOP, new Item(9456), new Item(9422), new Item(9442)),
		IRON_CROSSBOW_U(39, 44, Animation.STOP, new Item(9457), new Item(9423), new Item(9444)),
		STEEL_CROSSBOW_U(46, 54, Animation.STOP, new Item(9459), new Item(9425), new Item(9446)),
		MITHRIL_CROSSBOW_U(54, 64, Animation.STOP, new Item(9461), new Item(9427), new Item(9448)),
		ADAMANT_CROSSBOW_U(61, 82, Animation.STOP, new Item(9463), new Item(9429), new Item(9450)),
		RUNE_CROSSBOW_U(69, 100, Animation.STOP, new Item(9465), new Item(9431), new Item(9452)),
		DRAGON_CROSSBOW_U(78, 135, Animation.STOP, new Item(21921), new Item(21918), new Item(21952)),
		BRONZE_CROSSBOW(9, 6, new Animation(6671), new Item(9174), new Item(9454), CROSSBOW_STRING),
		BLURITE_CROSSBOW(24, 16, new Animation(6672), new Item(9176), new Item(9456), CROSSBOW_STRING),
		IRON_CROSSBOW(39, 22, new Animation(6673), new Item(9177), new Item(9457), CROSSBOW_STRING),
		STEEL_CROSSBOW(46, 27, new Animation(6674), new Item(9179), new Item(9459), CROSSBOW_STRING),
		MITHRIL_CROSSBOW(54, 32, new Animation(6675), new Item(9181), new Item(9461), CROSSBOW_STRING),
		ADAMANT_CROSSBOW(61, 41, new Animation(6676), new Item(9183), new Item(9463), CROSSBOW_STRING),
		RUNE_CROSSBOW(69, 50, new Animation(6677), new Item(9185), new Item(9465), CROSSBOW_STRING),
		DRAGON_CROSSBOW(78, 70, new Animation(7961), new Item(21902), new Item(21921), CROSSBOW_STRING)
		;
		private final Item product;
		private final Item[] materials;
		private final int level;
		private final double xp;
		private final Animation animation;
		public static final BowFletchingData[] VALUES_ARR = values();
		public static final HashMap<Integer, BowFletchingData> VALUES = new HashMap<Integer, BowFletchingData>(VALUES_ARR.length);

		static {
			for (BowFletchingData data : BowFletchingData.VALUES_ARR) VALUES.put(data.ordinal(), data);
		}

		BowFletchingData(final int level, final double xp, final Animation animation, final Item product, final Item... materials) {
			this.level = level;
			this.xp = xp;
			this.animation = animation;
			this.product = product;
			this.materials = materials;
		}

		public static boolean hasRequirements(final Player player, final BowFletchingData data) {
			if (player.getSkills().getLevel(SkillConstants.FLETCHING) < data.getLevel()) {
				player.getDialogueManager().start(new PlainChat(player, "You need a Fletching level of " + data.getLevel() + " to string that bow."));
				return false;
			}
			return true;
		}

		public static BowFletchingData getDataByMaterial(final Item from, final Item to) {
			for (final FletchingDefinitions.BowFletchingData data : BowFletchingData.VALUES_ARR) {
				loop:
				for (final Item i : data.getMaterials()) {
					if (i.getId() == from.getId()) {
						for (final Item o : data.getMaterials()) {
							if (o.getId() == to.getId() && i != o) return data;
						}
						continue loop;
					}
				}
			}
			return null;
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

		public Animation getAnimation() {
			return animation;
		}
	}

	public static boolean isMaterial(final Item from, final Item to) {
		for (int i = 0; i < FletchingDefinitions.MATERIALS.length; i++) {
			for (int x = 0; x < FletchingDefinitions.MATERIALS[i].length; x++) if ((from.getId() == FletchingDefinitions.MATERIALS[i][x].getId() || to.getId() == FletchingDefinitions.MATERIALS[i][x].getId()) && (from.getId() == FletchingDefinitions.KNIFE.getId() || to.getId() == FletchingDefinitions.KNIFE.getId())) return true;
		}
		return false;
	}

	public static Item[] getCategory(final Player player, final Item from, final Item to) {
		Item[] items = null;
		int category = -1;
		if (from.getId() == 1511 || to.getId() == 1511) {
			items = PRODUCTS[0];
			category = 0;
		} else if (from.getId() == 1521 || to.getId() == 1521) {
			items = PRODUCTS[1];
			category = 1;
		} else if (from.getId() == 1519 || to.getId() == 1519) {
			items = PRODUCTS[2];
			category = 2;
		} else if (from.getId() == 1517 || to.getId() == 1517) {
			items = PRODUCTS[3];
			category = 3;
		} else if (from.getId() == 6333 || to.getId() == 6333) {
			items = PRODUCTS[4];
			category = 4;
		} else if (from.getId() == 6332 || to.getId() == 6332) {
			items = PRODUCTS[5];
			category = 5;
		} else if (from.getId() == 1515 || to.getId() == 1515) {
			items = PRODUCTS[6];
			category = 6;
		} else if (from.getId() == 1513 || to.getId() == 1513) {
			items = PRODUCTS[7];
			category = 7;
		} else if (from.getId() == 19669 || to.getId() == 19669) {
			items = PRODUCTS[8];
			category = 8;
		}
		player.getTemporaryAttributes().put("LogsCategory", category);
		return items;
	}


	public enum AmmunitionType {
		ARROW(15, false),
		BOLT(10, true),
		DART(10, true),
		JAVELIN(15, false),
		TIPPED_BOLT(10, true);

		private final int sets;
		private final boolean instant;

		AmmunitionType(int sets, boolean instant) {
			this.sets = sets;
			this.instant = instant;
		}

		public int getSets() {
			return sets;
		}

		public boolean isInstant() {
			return instant;
		}

	}
}
