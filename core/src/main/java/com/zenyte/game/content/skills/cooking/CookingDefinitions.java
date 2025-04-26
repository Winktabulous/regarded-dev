package com.zenyte.game.content.skills.cooking;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.utils.Ordinal;
import com.zenyte.utils.TextUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;

import java.util.ArrayList;

public class CookingDefinitions {
	public static final Animation FIRE = new Animation(897);
	public static final Animation STOVE = new Animation(896);
	public static final Item GLOVES = new Item(775);
	public static final int SINEW = 9436;
	public static final String BAKE = "You successfully bake a";


	@Ordinal
	public enum CookingData {
		/** Raids fish */
		PYSK(1, 21, 10, 20855, 20856, 20854), SUPHI(15, 35, 13, 20857, 20858, 20854), LECKISH(30, 50, 16, 20859, 20860, 20854), BRAWK(45, 65, 19, 20861, 20862, 20854), MYCIL(60, 75, 22, 20863, 20864, 20854), ROQED(75, 85, 25, 20865, 20866, 20854), KYREN(90, 97, 28, 20867, 20868, 20854), 
		/** Raids bats */
		GUANIC_BAT(1, 21, 10, 20870, 20871, 20869), PRAEL_BAT(15, 35, 13, 20872, 20873, 20869), GIRAL_BAT(30, 50, 16, 20874, 20875, 20869), PHLUXIA_BAT(45, 65, 19, 20876, 20877, 20869), KRYKET_BAT(60, 75, 22, 20878, 20879, 20869), MURNG_BAT(75, 85, 25, 20880, 20881, 20869), PSYKK_BAT(90, 97, 28, 20882, 20883, 20869), 
		/** Fish */
		SHRIMPS(1, 34, 30, 317, 315, 7954), ANCHOVIES(1, 34, 30, 321, 319, 323), SARDINE(1, 40, 38, 327, 325, 369), POISON_KARAMBWAN(1, 99, 80, 3142, 3146, 3148), KARAMBWAN(30, 99, 180, 3142, 3144, 3148), HERRING(5, 41, 50, 345, 347, 357), MACKEREL(10, 45, 60, 353, 355, 357), TROUT(15, 50, 70, 335, 333, 343), COD(18, 52, 75, 341, 339, 343), PIKE(20, 53, 80, 349, 351, 343), SALMON(25, 58, 90, 331, 329, 343), SLIMY_EEL(28, 58, 95, 3379, 3381, 3383), TUNA(30, 65, 100, 359, 361, 367), RAINBOW_FISH(35, 60, 110, 10136, 10138, 10140), CAVE_EEL(38, 40, 115, 5001, 5003, 5002), LOBSTER(40, 74, 120, 377, 379, 381), BASS(43, 80, 130, 363, 365, 367), SWORDFISH(45, 86, 140, 371, 373, 375), LAVA_EEL(53, 53, 30, 2148, 2149, 3383), MONKFISH(62, 92, 150, 7944, 7946, 7948), SHARK(80, 95, 210, 383, 385, 387), SEA_TURTLE(82, 95, 212, 395, 397, 399), MANTA_RAY(91, 95, 255, 389, 391, 393), ANGLERFISH(84, 90, 230, 13439, 13441, 13443), DARK_CRAB(90, 95, 215, 11934, 11936, 11938), 
		/** Misc meats */
		RAW_BEEF(1, 30, 31, 2132, 2142, 2146), RAW_RAT_MEAT(1, 30, 31, 2134, 2142, 2146), RAW_BEAR_MEAT(1, 30, 31, 2136, 2142, 2146), RAW_YAK_MEAT(1, 30, 40, ItemId.RAW_YAK_MEAT, ItemId.COOKED_MEAT, ItemId.BURNT_MEAT), CHICKEN(1, 31, 30, 2138, 2140, 2144), UGTHANKI_MEAT(1, 99, 40, 1859, 1861, 2146), RABBIT(1, 30, 30, 3226, 3228, 7222), ROAST_BIRD_MEAT(11, 40, 66, 9978, 9980, 9982), THIN_SNAIL(12, 42, 70, 3363, 3369, 3375), LEAN_SNAIL(17, 51, 80, 3365, 3371, 3375), FAT_SNAIL(22, 63, 95, 3367, 3373, 3375), CRAB_MEAT(21, 65, 100, 7518, 7521, 7520), 
		/** Meats used for Sinew on a range. */
		RAW_BEEF_SINEW(1, 30, 31, 2132, 9436, 2146), RAW_RAT_MEAT_SINEW(1, 30, 31, 2134, 9436, 2146), RAW_BEAR_MEAT_SINEW(1, 30, 31, 2136, 9436, 2146), CHICKEN_SINEW(1, 31, 30, 2138, 9436, 2144), UGTHANKI_MEAT_SINEW(1, 99, 40, 1859, 9436, 2146), RABBIT_SINEW(1, 30, 30, 3226, 9436, 7222), ROAST_BIRD_MEAT_SINEW(11, 40, 66, 9978, 9436, 9982), THIN_SNAIL_SINEW(12, 42, 70, 3363, 9436, 3375), LEAN_SNAIL_SINEW(17, 51, 80, 3365, 9436, 3375), FAT_SNAIL_SINEW(22, 63, 95, 3367, 9436, 3375), CRAB_MEAT_SINEW(21, 65, 100, 7518, 9436, 7520), 
		/** Bread and pastry */
		BREAD(1, 31, 40, 2307, 2309, 2311), PITTA_BREAD(58, 75, 40, 1863, 1865, 1867), FISHCAKE(31, 70, 100, 7529, 7530, 7531), CAKE(40, 75, 180, 1889, 1891, 1903), PIZZA(35, 65, 143, 2287, 2289, 2305), 
		/** Misc items */
		POTATO(7, 33, 15, 1942, 6701, 6699), SCRAMBLED_EGGS(13, 41, 50, 7076, 7078, 7090), FRIED_ONIONS(42, 80, 60, 1871, 7084, 7092), FRIED_MUSHROOMS(46, 81, 60, 7080, 7082, 7094), SWEETCORN(28, 58, 104, 5986, 5988, 5990), STEW(25, 58, 117, 2001, 2003, 2005), CHOC_SATURDAY(33, 33, 0, 9572, 9573, 9573, "You briefly warm the drink in the oven."), DRUNK_DRAGON(32, 32, 0, 9576, 2092, 2092, CHOC_SATURDAY.getMessage()), BOWL_OF_HOT_WATER(1, 1, 0, 1921, 4456, -1, "You boil the water."), NETTLE_WATER(20, 1, 52, 4237, 4240, -1, "You boil the water to make some nettle tea."), 
		/** Pies for bake pie spell */
		REDBERRY_PIE(10, 50, 78, 2321, 2325, 2329, BAKE + " delicious redberry pie."), MEAT_PIE(20, 55, 105, 2319, 2327, 2329, BAKE + " tasty meat pie."), MUD_PIE(29, 60, 128, 7168, 7170, 2329, BAKE + " mucky mud pie."), APPLE_PIE(30, 63, 130, 2317, 2323, 2329, BAKE + " traditional apple pie."), GARDEN_PIE(34, 66, 138, 7176, 7178, 2329, BAKE + " tasty garden pie."), FISH_PIE(47, 75, 164, 7186, 7188, 2329, BAKE + " tasty fish pie."), BOTANICAL_PIE(52, 80, 180, 19656, 19662, 2329, BAKE + " fruity botanical pie."), ADMIRAL_PIE(70, 94, 210, 7196, 7198, 2329, BAKE + " tasty admiral pie."), WILD_PIE(85, 99, 240, 7206, 7208, 2329, BAKE + " tasty wild pie."), SUMMER_PIE(95, 99, 264, 7216, 7218, 2329, BAKE + " tasty summer pie."), DRAGONFRUIT_PIE(73, 97, 220, ItemId.UNCOOKED_DRAGONFRUIT_PIE, ItemId.DRAGONFRUIT_PIE, ItemId.BURNT_PIE, BAKE + " tasty dragonfruit pie."), MUSHROOM_PIE(60, 84, 200, 21684, 21690, 2329, BAKE + " tasty mushroom pie.");
		private final int level;
		private final int burnLevel;
		private final double xp;
		private final int raw;
		private final int cooked;
		private final int burnt;
		private final String message;

		CookingData(final int level, final int burnLevel, final double xp, final int raw, final int cooked, final int burnt) {
			this(level, burnLevel, xp, raw, cooked, burnt, "");
		}

		CookingData(final int level, final int burnLevel, final double xp, final int raw, final int cooked, final int burnt, final String message) {
			this.level = level;
			this.burnLevel = burnLevel;
			this.xp = xp;
			this.raw = raw;
			this.cooked = cooked;
			this.burnt = burnt;
			this.message = message;
		}

		public static final CookingData[] values = values();
		private static final Int2ObjectMap<CookingData> map = new Int2ObjectOpenHashMap<>();

		static {
			CollectionUtils.populateMap(values, map, value -> value.raw);
		}

		public static final CookingData[] PIES = new CookingData[] {REDBERRY_PIE, MEAT_PIE, MUD_PIE, APPLE_PIE, GARDEN_PIE, FISH_PIE, BOTANICAL_PIE, ADMIRAL_PIE, WILD_PIE, SUMMER_PIE};

		public static final CookingData getData(final int raw) {
			return map.get(raw);
		}

		public String getName(final int item) {
			return ItemDefinitions.get(item).getName();
		}

		public static CookingData getDataByProduct(final Item item) {
			for (final CookingData data : values) {
				if (item.getId() == data.getCooked()) {
					return data;
				}
			}
			return null;
		}

		public boolean isRaidsFood() {
			return ordinal() <= PSYKK_BAT.ordinal();
		}

		public static CookingData[] isCooking(final Player player, final Item item, final boolean fire) {
			final ArrayList<CookingDefinitions.CookingData> list = new ArrayList<CookingData>(2);
			for (final CookingData data : values) {
				if (fire && data.getCooked() == SINEW) {
					continue;
				}
				if (item.getId() == data.getRaw()) {
					list.add(data);
				}
			}
			return list.toArray(new CookingData[0]);
		}

		public static boolean hasRequirements(final Player player, final CookingData food) {
			if (!player.getInventory().containsItem(food.getRaw(), 1)) {
				return false;
			}
			if (player.getSkills().getLevel(SkillConstants.COOKING) < food.getLevel()) {
				player.sendMessage("You need level " + food.getLevel() + " Cooking to cook a " + TextUtils.capitalizeFirstCharacter(food.getName(food.getCooked())) + ".");
				return false;
			}
			return true;
		}

		public int getLevel() {
			return level;
		}

		public int getBurnLevel() {
			return burnLevel;
		}

		public double getXp() {
			return xp;
		}

		public int getRaw() {
			return raw;
		}

		public int getCooked() {
			return cooked;
		}

		public int getBurnt() {
			return burnt;
		}

		public String getMessage() {
			return message;
		}
	}
}
