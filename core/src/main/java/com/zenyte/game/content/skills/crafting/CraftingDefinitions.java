package com.zenyte.game.content.skills.crafting;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 25 aug. 2018 | 20:20:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class CraftingDefinitions {
	public static final Item BALL_OF_WOOL = new Item(1759);
	public static final Item MAGIC_STRING = new Item(6038);
	public static final Item BATTLESTAFF = new Item(1391);
	public static final Item CHISEL = new Item(1755);
	public static final Item CRUSHED_GEM = new Item(1633);
	public static final Item MOLTEN_GLASS = new Item(1775);
	public static final Item GLASSBLOWING_PIPE = new Item(1785);
	public static final Item SILVER_BAR = new Item(2355);
	public static final Item GOLD_BAR = new Item(2357);
	public static final Item NEEDLE = new Item(1733);
	public static final Item THREAD = new Item(1734);
	public static final Item SOFT_CLAY = new Item(1761);
	public static final Item[][] PRODUCTS = new Item[][] {
			{new Item(1059), new Item(1061), new Item(1167), new Item(1063), new Item(1129), new Item(1095), new Item(1169)},
			{new Item(1131)},
			{new Item(1133), new Item(1097)},
			{new Item(10077), new Item(10079), new Item(10081), new Item(10083), new Item(10085)},
			{new Item(1065), new Item(1099), new Item(1135)},
			{new Item(2487), new Item(2493), new Item(2499)},
			{new Item(2489), new Item(2495), new Item(2501)},
			{new Item(2491), new Item(2497), new Item(2503)},
			{new Item(6328), new Item(6330), new Item(6326), new Item(6324), new Item(6322)},
			{new Item(10824), new Item(10822)},
			{new Item(7539), new Item(7537)},
			{new Item(13385), new Item(13389), new Item(13387)}
	};
	public static final Item[][] MATERIALS = new Item[][] {{new Item(1741), new Item(1741), new Item(1741), new Item(1741), new Item(1741), new Item(1741), new Item(1741)}, {new Item(1743)}, {new Item(1129), new Item(1095)}, {new Item(1063), new Item(1065), new Item(2487), new Item(2489), new Item(2491)}, {new Item(1745), new Item(1745, 2), new Item(1745, 3)}, {new Item(2505), new Item(2505, 2), new Item(2505, 3)}, {new Item(2507), new Item(2507, 2), new Item(2507, 3)}, {new Item(2509), new Item(2509, 2), new Item(2509, 3)}, {new Item(6289, 6), new Item(6289, 8), new Item(6289, 5), new Item(6289, 12), new Item(6289, 15)}, {new Item(10820), new Item(10820, 2)}, {new Item(7538), new Item(7536)}, {new Item(13383, 3), new Item(13383, 4), new Item(13383, 5)}};
	public static final int[][] LEVELS = new int[][] {{1, 7, 9, 11, 14, 18, 38}, {28}, {41, 44}, {32, 32, 32, 32, 32}, {57, 60, 63}, {66, 68, 71}, {73, 75, 77}, {79, 82, 84}, {45, 47, 48, 51, 53}, {43, 46}, {15, 15}, {14, 17, 22}};
	public static final double[][] EXPERIENCE = new double[][] {{13.8, 16.25, 18.5, 22, 25, 27, 37}, {35}, {40, 42}, {6, 6, 6, 6, 6}, {62, 124, 186}, {70, 140, 210}, {78, 156, 234}, {86, 172, 258}, {30, 35, 45, 50, 55}, {32, 32}, {32.5, 32.5}, {66, 88, 110}};


	public enum AmuletStringingData {
		UNSTRUNG_SYMBOL(new Item(1718), new Item(1714), BALL_OF_WOOL), UNSTRUNG_EMBLEM(new Item(ItemId.UNPOWERED_SYMBOL), new Item(ItemId.UNSTRUNG_EMBLEM), BALL_OF_WOOL), GOLD_AMULET(new Item(1692), new Item(1673), BALL_OF_WOOL), SAPPHIRE_AMULET(new Item(1694), new Item(1675), BALL_OF_WOOL), PRE_NATURE_AMULET(new Item(6041), new Item(1677), MAGIC_STRING), EMERALD_AMULET(new Item(1696), new Item(1677), BALL_OF_WOOL), RUBY_AMULET(new Item(1698), new Item(1679), BALL_OF_WOOL), DIAMOND_AMULET(new Item(1700), new Item(1681), BALL_OF_WOOL), DRAGONSTONE_AMULET(new Item(1702), new Item(1683), BALL_OF_WOOL), ONYX_AMULET(new Item(6581), new Item(6579), BALL_OF_WOOL), ZENYTE_AMULET(new Item(19541), new Item(19501), BALL_OF_WOOL), OPAL_AMULET(new Item(21108), new Item(21099), BALL_OF_WOOL), JADE_AMULET(new Item(21111), new Item(21102), BALL_OF_WOOL), TOPAZ_AMULET(new Item(21114), new Item(21105), BALL_OF_WOOL);
		public static final AmuletStringingData[] VALUES_ARR = values();
		public static final HashMap<Integer, AmuletStringingData> VALUES = new HashMap<Integer, AmuletStringingData>(VALUES_ARR.length);

		static {
			for (final AmuletStringingData data : AmuletStringingData.VALUES_ARR) {
				VALUES.put(data.getMaterials()[0].getId(), data);
			}
		}

		private final Item product;
		private final Item[] materials;

		AmuletStringingData(final Item product, final Item... materials) {
			this.product = product;
			this.materials = materials;
		}

		public Item getProduct() {
			return product;
		}

		public Item[] getMaterials() {
			return materials;
		}
	}


	public enum BattlestaffAttachingData {
		WATER_BATTLESTAFF(54, 100, new Graphics(1370), new Item(1395), new Item(571), BATTLESTAFF), EARTH_BATTLESTAFF(58, 112.5, new Graphics(1371), new Item(1399), new Item(575), BATTLESTAFF), FIRE_BATTLESTAFF(62, 125, new Graphics(1372), new Item(1393), new Item(569), BATTLESTAFF), AIR_BATTLESTAFF(66, 137.5, new Graphics(306), new Item(1397), new Item(573), BATTLESTAFF);
		private final Item product;
		private final Item[] materials;
		private final int level;
		private final double xp;
		private final Graphics graphics;
		public static final BattlestaffAttachingData[] VALUES_ARR = values();
		public static final HashMap<Integer, BattlestaffAttachingData> VALUES = new HashMap<Integer, BattlestaffAttachingData>(VALUES_ARR.length);

		static {
			for (final BattlestaffAttachingData data : VALUES_ARR) {
				VALUES.put(data.ordinal(), data);
			}
		}

		BattlestaffAttachingData(final int level, final double xp, final Graphics graphics, final Item product, final Item... materials) {
			this.level = level;
			this.xp = xp;
			this.graphics = graphics;
			this.product = product;
			this.materials = materials;
		}

		public static BattlestaffAttachingData getDataByMaterial(final Item from, final Item to) {
			for (final BattlestaffAttachingData data : BattlestaffAttachingData.VALUES_ARR) {
				loop:
				for (final Item i : data.getMaterials()) {
					if (i.getId() == from.getId()) {
						for (final Item o : data.getMaterials()) {
							if (o.getId() == to.getId() && i != o) {
								return data;
							}
						}
						continue loop;
					}
				}
			}
			return null;
		}

		public static boolean hasRequirements(final Player player, final BattlestaffAttachingData data) {
			if (player.getSkills().getLevel(SkillConstants.CRAFTING) < data.getLevel()) {
				final String product = data.getProduct().getDefinitions().getName().toLowerCase();
				final boolean vowel = product.startsWith("a") || product.startsWith("o") || product.startsWith("u") || product.startsWith("i") || product.startsWith("e");
				player.getDialogueManager().start(new PlainChat(player, "You need a Crafting level of " + data.getLevel() + " to make " + (vowel ? "an " : "a ") + product + "."));
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

		public Graphics getGraphics() {
			return graphics;
		}
	}


	public enum GemCuttingData {
		OPAL(1, 15, new Animation(890), new Item(1625), new Item(1609)),
		JADE(13, 20, new Animation(891), new Item(1627), new Item(1611)),
		RED_TOPAZ(16, 25, new Animation(892), new Item(1629), new Item(1613)),
		SAPPHIRE(20, 50, new Animation(888), new Item(1623), new Item(1607)),
		EMERALD(27, 67.5, new Animation(889), new Item(1621), new Item(1605)),
		RUBY(34, 85, new Animation(887), new Item(1619), new Item(1603)),
		DIAMOND(43, 107.5, new Animation(886), new Item(1617), new Item(1601)),
		DRAGONSTONE(55, 137.5, new Animation(885), new Item(1631), new Item(1615)),
		ONYX(67, 167.5, new Animation(2717), new Item(6571), new Item(6573)),
		AMETHYST(83, 60, new Animation(6295), new Item(21347), new Item(21338, 15), new Item(21350, 15), new Item(21352, 5), new Item(ItemId.AMETHYST_DART_TIP, 8)),
		ZENYTE(89, 200, new Animation(7185), new Item(19496), new Item(19493));
		private final Item[] products;
		private final Item material;
		private final int level;
		private final double xp;
		private final Animation animation;
		public static final GemCuttingData[] VALUES_ARR = values();
		public static final HashMap<Integer, GemCuttingData> VALUES = new HashMap<Integer, GemCuttingData>(VALUES_ARR.length);

		static {
			for (final GemCuttingData data : GemCuttingData.VALUES_ARR) {
				VALUES.put(data.ordinal(), data);
			}
		}

		GemCuttingData(final int level, final double xp, final Animation animation, final Item material, final Item... product) {
			this.level = level;
			this.xp = xp;
			this.animation = animation;
			products = product;
			this.material = material;
		}

		public static GemCuttingData getDataByMaterial(final Item from, final Item to) {
			for (final GemCuttingData data : GemCuttingData.VALUES_ARR) {
				if ((from.getId() == data.getMaterial().getId() || to.getId() == data.getMaterial().getId()) && (from.getId() == CHISEL.getId() || to.getId() == CHISEL.getId())) {
					return data;
				}
			}
			return null;
		}

		public static boolean hasRequirements(final Player player, final GemCuttingData data) {
			if (player.getSkills().getLevel(SkillConstants.CRAFTING) < data.getLevel()) {
				final String material = data.equals(OPAL) ? "opals" : data.equals(JADE) ? "jades" : data.equals(RED_TOPAZ) ? "red topaz'" : data.equals(SAPPHIRE) ? "sapphires" : data.equals(EMERALD) ? "emeralds" : data.equals(RUBY) ? "rubbies" : data.equals(DIAMOND) ? "diamonds" : data.equals(DRAGONSTONE) ? "dragonstones" : data.equals(ONYX) ? "onyx'" : data.equals(AMETHYST) ? "amethyst" : "zenytes";
				player.getDialogueManager().start(new ItemChat(player, data.getMaterial(), "You need a Crafting level of " + data.getLevel() + " to cut " + material + "."));
				return false;
			}
			return true;
		}

		public Item[] getProducts() {
			return products;
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


	public enum GlassBlowingData {
		BEER_GLASS(1, 17.5, new Item(1919), MOLTEN_GLASS), CANDLE_LANTERN(4, 19, new Item(4527), MOLTEN_GLASS), OIL_LAMP(12, 25, new Item(4525), MOLTEN_GLASS), VIAL(33, 35, new Item(229), MOLTEN_GLASS), FISHBOWL(42, 42.5, new Item(6667), MOLTEN_GLASS), UNPOWERED_ORB(46, 52.5, new Item(567), MOLTEN_GLASS), LANTERN_LENS(49, 55, new Item(4542), MOLTEN_GLASS), EMPTY_LIGHT_ORB(87, 70, new Item(10980), MOLTEN_GLASS);
		private final Item product;
		private final Item material;
		private final int level;
		private final double xp;
		private String materialsName;
		public static final GlassBlowingData[] VALUES_ARR = values();
		public static final HashMap<Integer, GlassBlowingData> VALUES = new HashMap<Integer, GlassBlowingData>(VALUES_ARR.length);
		public static final Item[] PRODUCTS = new Item[VALUES_ARR.length];

		static {
			int i = 0;
			for (final GlassBlowingData data : GlassBlowingData.VALUES_ARR) {
				VALUES.put(data.ordinal(), data);
				PRODUCTS[i++] = data.product;
			}
		}

		GlassBlowingData(final int level, final double xp, final Item product, final Item material) {
			this.level = level;
			this.xp = xp;
			this.product = product;
			this.material = material;
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

		public String getMaterialsName() {
			return materialsName;
		}
	}


	public enum JewelleryData {
		OPAL_RING(1, 10, 7, 0, new Item(21081), new Item(1609), SILVER_BAR),
		OPAL_NECKLACE(16, 35, 11, 3, new Item(21090), new Item(1609), SILVER_BAR),
		OPAL_BRACELET(22, 45, 19, 9, new Item(21117), new Item(1609), SILVER_BAR),
		OPAL_AMULET(27, 55, 15, 6, new Item(21099), new Item(1609), SILVER_BAR),
		JADE_RING(13, 32, 8, 1, new Item(21084), new Item(1611), SILVER_BAR),
		JADE_NECKLACE(25, 54, 12, 4, new Item(21093), new Item(1611), SILVER_BAR),
		JADE_BRACELET(29, 60, 20, 10, new Item(21120), new Item(1611), SILVER_BAR),
		JADE_AMULET(34, 70, 16, 7, new Item(21102), new Item(1611), SILVER_BAR),
		TOPAZ_RING(16, 35, 9, 2, new Item(21087), new Item(1613), SILVER_BAR),
		TOPAZ_NECKLACE(32, 70, 13, 5, new Item(21096), new Item(1613), SILVER_BAR),
		TOPAZ_BRACELET(38, 75, 21, 11, new Item(21123), new Item(1613), SILVER_BAR),
		TOPAZ_AMULET(45, 80, 17, 0, new Item(21105), new Item(1613), SILVER_BAR),
		UNSTRUNG_SYMBOL(16, 50, 23, 0, new Item(1714), SILVER_BAR),
		UNSTRUNG_EMBLEM(17, 50, 24, 1, new Item(1720), SILVER_BAR),
		TIARA(23, 52.5, 28, 5, new Item(5525), SILVER_BAR),
		SILVER_BOLTS(21, 50, 27, 4, new Item(9382, 10), SILVER_BAR),
		GOLD_RING(5, 15, 8, new Item(1635), GOLD_BAR),
		GOLD_NECKLACE(6, 20, 23, new Item(1654), GOLD_BAR),
		GOLD_BRACELET(7, 25, 50, new Item(11069), GOLD_BAR),
		GOLD_AMULET(8, 30, 37, new Item(1673), GOLD_BAR),
		SAPPHIRE_RING(20, 40, 9, new Item(1637), new Item(1607), GOLD_BAR),
		SAPPHIRE_NECKLACE(22, 55, 24, new Item(1656), new Item(1607), GOLD_BAR),
		SAPPHIRE_BRACELET(23, 60, 52, new Item(11071), new Item(1607), GOLD_BAR),
		SAPPHIRE_AMULET(24, 65, 38, new Item(1675), new Item(1607), GOLD_BAR),
		EMERALD_RING(27, 55, 10, new Item(1639), new Item(1605), GOLD_BAR),
		EMERALD_NECKLACE(29, 60, 25, new Item(1658), new Item(1605), GOLD_BAR),
		EMERALD_BRACELET(30, 65, 53, new Item(11076), new Item(1605), GOLD_BAR),
		EMERALD_AMULET(31, 70, 39, new Item(1677), new Item(1605), GOLD_BAR),
		RUBY_RING(34, 70, 11, new Item(1641), new Item(1603), GOLD_BAR),
		RUBY_NECKLACE(40, 75, 26, new Item(1660), new Item(1603), GOLD_BAR),
		RUBY_BRACELET(42, 80, 54, new Item(11085), new Item(1603), GOLD_BAR),
		RUBY_AMULET(50, 85, 40, new Item(1679), new Item(1603), GOLD_BAR),
		DIAMOND_RING(43, 85, 12, new Item(1643), new Item(1601), GOLD_BAR),
		DIAMOND_NECKLACE(56, 90, 27, new Item(1662), new Item(1601), GOLD_BAR),
		DIAMOND_BRACELET(58, 95, 55, new Item(11092), new Item(1601), GOLD_BAR),
		DIAMOND_AMULET(70, 100, 41, new Item(1681), new Item(1601), GOLD_BAR),
		DRAGONSTONE_RING(55, 100, 13, new Item(1645), new Item(1615), GOLD_BAR),
		DRAGONSTONE_NECKLACE(72, 105, 28, new Item(1664), new Item(1615), GOLD_BAR),
		DRAGONSTONE_BRACELET(74, 110, 56, new Item(11115), new Item(1615), GOLD_BAR),
		DRAGONSTONE_AMULET(80, 150, 42, new Item(1683), new Item(1615), GOLD_BAR),
		ONYX_RING(67, 115, 14, new Item(6575), new Item(6573), GOLD_BAR),
		ONYX_NECKLACE(82, 120, 29, new Item(6577), new Item(6573), GOLD_BAR),
		ONYX_BRACELET(84, 125, 57, new Item(11130), new Item(6573), GOLD_BAR),
		ONYX_AMULET(90, 165, 43, new Item(6579), new Item(6573), GOLD_BAR),
		ZENYTE_RING(89, 150, 15, new Item(19538), new Item(19493), GOLD_BAR),
		ZENYTE_NECKLACE(92, 165, 30, new Item(19535), new Item(19493), GOLD_BAR),
		ZENYTE_BRACELET(95, 180, 58, new Item(19532), new Item(19493), GOLD_BAR),
		ZENYTE_AMULET(98, 200, 44, new Item(19501), new Item(19493), GOLD_BAR),
		SLAYER_RING(75, 15, 16, new Item(11866), new Item(4155), GOLD_BAR),
		ETERNAL_SLAYER_RING(75, 15, 16, new Item(21268), new Item(21270), GOLD_BAR);
		private final Item product;
		private final Item[] materials;
		private final int level;
		private final int componentId;
		private final int slotId;
		private final double xp;
		public static final JewelleryData[] VALUES = values();

		JewelleryData(final int level, final double xp, final int componentId, final int slotId, final Item product, final Item... materials) {
			this.level = level;
			this.xp = xp;
			this.componentId = componentId;
			this.product = product;
			this.materials = materials;
			this.slotId = slotId;
		}

		JewelleryData(final int level, final double xp, final int componentId, final Item product, final Item... materials) {
			this(level, xp, componentId, -1, product, materials);
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

		public int getComponentId() {
			return componentId;
		}

		public int getSlotId() {
			return slotId;
		}

		public double getXp() {
			return xp;
		}
	}


	public enum LeatherShieldData {
		HARD_LEATHER_SHIELD(41, 70, new Animation(7386), new Item(22269), new Item(22251), new Item(1743, 2), new Item(4819, 15)),
		SNAKESKIN_SHIELD(56, 100, new Animation(7387), new Item(22272), new Item(22254), new Item(6289, 2), new Item(4820, 15)),
		GREEN_DRAGONHIDE_SHIELD(62, 124, new Animation(7387), new Item(22275), new Item(22257), new Item(1745, 2), new Item(1539, 15)),
		BLUE_DRAGONHIDE_SHIELD(69, 140, new Animation(7387), new Item(22278), new Item(22260), new Item(2505, 2), new Item(4822, 15)),
		RED_DRAGONHIDE_SHIELD(76, 156, new Animation(7387), new Item(22281), new Item(22263), new Item(2507, 2), new Item(4823, 15)),
		BLACK_DRAGONHIDE_SHIELD(83, 172, new Animation(7387), new Item(22284), new Item(22266), new Item(2509, 2), new Item(4824, 15));
		private final int level;
		private final double xp;
		private final Item product;
		private final Item[] materials;
		private final Animation animation;
		public static final LeatherShieldData[] VALUES = values();

		LeatherShieldData(final int level, final double xp, final Animation animation, final Item product, final Item... materials) {
			this.level = level;
			this.xp = xp;
			this.animation = animation;
			this.product = product;
			this.materials = materials;
		}

		public static boolean isMaterial(final Item from, final Item to) {
			for (final LeatherShieldData data : VALUES) {
				for (final Item material : data.getMaterials()) {
					if (from.getId() == material.getId() || to.getId() == material.getId()) {
						return true;
					}
				}
			}
			return false;
		}

		public static LeatherShieldData getDataByMaterials(final Item from, final Item to) {
			for (final LeatherShieldData data : VALUES) {
				if (data.isValidMaterial(from.getId()) && data.isValidMaterial(to.getId())) {
					return data;
				}
			}
			return null;
		}

		public boolean isValidMaterial(int itemId) {
			for (var item : getMaterials()) {
				if (item.getId() == itemId) return true;
			}
			return false;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public Item getProduct() {
			return product;
		}

		public Item[] getMaterials() {
			return materials;
		}

		public Animation getAnimation() {
			return animation;
		}
	}


	public enum PotteryFiringData {
		POT(1, 6.3, new Item(1931), new Item(1787)), PIE_DISH(7, 10, new Item(2313), new Item(1789)), BOWL(8, 15, new Item(1923), new Item(1791)), EMPTY_PLANT_POT(19, 17.5, new Item(5350), new Item(5352)), POT_LID(25, 37.5, new Item(4440), new Item(4438));
		private final Item product;
		private final Item material;
		private final int level;
		private final double xp;
		public static final PotteryFiringData[] VALUES_ARR = values();
		public static final HashMap<Integer, PotteryFiringData> VALUES = new HashMap<Integer, PotteryFiringData>(VALUES_ARR.length);

		static {
			for (final PotteryFiringData data : PotteryFiringData.VALUES_ARR) {
				VALUES.put(data.ordinal(), data);
			}
		}

		PotteryFiringData(final int level, final double xp, final Item product, final Item material) {
			this.level = level;
			this.xp = xp;
			this.product = product;
			this.material = material;
		}

		public static PotteryFiringData getData(final Item item) {
			for (final PotteryFiringData data : PotteryFiringData.VALUES_ARR) {
				if (item.getId() == data.getMaterial().getId()) {
					return data;
				}
			}
			return null;
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
	}


	public enum PotteryShapingData {
		POT(1, 6.3, new Item(1787)), PIE_DISH(7, 15, new Item(1789)), BOWL(8, 18, new Item(1791)), EMPTY_PLANT_POT(19, 20, new Item(5352)), POT_LID(25, 40, new Item(4438));
		private final Item product;
		private final int level;
		private final double xp;
		public static final PotteryShapingData[] VALUES_ARR = values();
		public static final HashMap<Integer, PotteryShapingData> VALUES = new HashMap<Integer, PotteryShapingData>(VALUES_ARR.length);

		static {
			for (final PotteryShapingData data : PotteryShapingData.VALUES_ARR) {
				VALUES.put(data.ordinal(), data);
			}
		}

		PotteryShapingData(final int level, final double xp, final Item product) {
			this.level = level;
			this.xp = xp;
			this.product = product;
		}

		public Item getProduct() {
			return product;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}
	}


	public enum SpinningData {
		WOOL(1, 2.5, new Item(1759), new Item(1737)), BOW_STRING(10, 15, new Item(1777), new Item(1779)), CROSSBOW_STRING(10, 15, new Item(9438), new Item(9436), new Item(6043), new Item(6045), new Item(6047), new Item(6049), new Item(6051)), MAGIC_STRING(19, 30, new Item(6038), new Item(6051)), ROPE(30, 25, new Item(954), new Item(10814));
		public static final SpinningData[] VALUES_ARR = values();
		public static final HashMap<Integer, SpinningData> VALUES = new HashMap<Integer, SpinningData>(VALUES_ARR.length);

		static {
			for (final SpinningData data : VALUES_ARR) {
				VALUES.put(data.ordinal(), data);
			}
		}

		private final Item product;
		private final Item[] materials;
		private final int level;
		private final double xp;

		SpinningData(final int level, final double xp, final Item product, final Item... materials) {
			this.level = level;
			this.xp = xp;
			this.product = product;
			this.materials = materials;
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
	}


	public enum Leather {
		SOFT_LEATHER(1, 1, new Item(1739), new Item(1741), 124, 132, 140, 148), HARD_LEATHER(3, 28, new Item(1739), new Item(1743), 125, 133, 141, 149), SNAKESKIN_1(15, 45, new Item(7801), new Item(6289), 126, 134, 142, 150), SNAKESKIN_2(15, 45, new Item(6287), new Item(6289), 127, 135, 143, 151), GREEN_DHIDE(20, 57, new Item(1753), new Item(1745), 128, 136, 144, 152), BLUE_DHIDE(20, 66, new Item(1751), new Item(2505), 129, 137, 145, 153), RED_DHIDE(20, 73, new Item(1749), new Item(2507), 130, 138, 146, 154), BLACK_DHIDE(20, 79, new Item(1747), new Item(2509), 131, 139, 147, 155);
		private final Item base;
		private final Item product;
		private final int cost;
		private final int level;
		private final String name;
		private final int[] components;
		public static final Leather[] VALUES = values();
		public static final Map<Integer, Leather> MAP = new HashMap<>();

		static {
			for (final Leather leather : VALUES) {
				MAP.put(leather.ordinal(), leather);
			}
		}

		Leather(final int cost, final int level, final Item base, final Item product, final int... components) {
			this.cost = cost;
			this.level = level;
			this.base = base;
			this.product = product;
			this.components = components;
			name = name().toLowerCase().replace("_", " ").replace(" 1", "").replace(" 2", "").replace("dh", "d'h");
		}

		public static Leather getLeather(final int id) {
			return MAP.get(id);
		}

		public static Leather getLeatherByComponent(final int component) {
			for (final Leather leather : VALUES) {
				for (final int id : leather.getComponents()) {
					if (component == id) {
						return leather;
					}
				}
			}
			return null;
		}

		public Item getBase() {
			return base;
		}

		public Item getProduct() {
			return product;
		}

		public int getCost() {
			return cost;
		}

		public int getLevel() {
			return level;
		}

		public String getName() {
			return name;
		}

		public int[] getComponents() {
			return components;
		}
	}


	public enum WeavingData {
		BASKET(36, 56, new Item(5376), new Item(5933, 6), "willow branches"), EMPTY_SACK(21, 38, new Item(5418), new Item(5931, 4), "jute fibres"), DRIFT_NET(26, 55, new Item(21652), new Item(5931, 2), "jute fibres"), STRIP_OF_CLOTH(10, 12, new Item(3224), new Item(1759, 4), "balls of wool");
		private final Item product;
		private final Item material;
		private final int level;
		private final double xp;
		private final String materialsName;
		public static final WeavingData[] VALUES_ARR = values();
		public static final HashMap<Integer, WeavingData> VALUES = new HashMap<Integer, WeavingData>(VALUES_ARR.length);

		static {
			for (WeavingData data : VALUES_ARR) VALUES.put(data.ordinal(), data);
		}

		WeavingData(int level, double xp, Item product, Item material, String materialsName) {
			this.level = level;
			this.xp = xp;
			this.product = product;
			this.material = material;
			this.materialsName = materialsName;
		}

		public static boolean isMaterial(final Item item) {
			for (WeavingData data : WeavingData.VALUES_ARR) {
				if (item.getId() == data.getMaterial().getId()) {
					return true;
				}
			}
			return false;
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

		public String getMaterialsName() {
			return materialsName;
		}
	}
}
