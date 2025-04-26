package com.zenyte.game.content.skills.smithing;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.content.boons.impl.MasterOfTheCraft;
import com.zenyte.game.content.treasuretrails.clues.CharlieTask;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.utils.TextUtils;
import mgi.types.config.enums.EnumDefinitions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Tommeh | 25 aug. 2018 | 19:05:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Smithing extends Action {
	public static final Animation ANIMATION = new Animation(898);
	public static final Item HAMMER = new Item(2347);
	public static final Item[] BARS = new Item[] {new Item(2349), new Item(2351), new Item(2353), new Item(2359), new Item(2361), new Item(2363), new Item(9467)};
	public static final int[] BAR_LEVELS = new int[] {1, 15, 30, 50, 70, 85, 8};
	public static final Item[][] PRODUCTS = new Item[][]{
			{new Item(1205), new Item(1277), new Item(1321), new Item(1291), new Item(1307), new Item(1351), new Item(1422), new Item(1337), new Item(1375), new Item(3095), new Item(1103), new Item(1075), new Item(1087), new Item(1117), new Item(4819, 15), new Item(1139), new Item(1155), new Item(1173), new Item(1189), null, new Item(819, 10), new Item(39, 15), new Item(864, 5), new Item(1794), new Item(19570, 5), new Item(9375, 10), new Item(9420)},
			{new Item(1203), new Item(1279), new Item(1323), new Item(1293), new Item(1309), new Item(1349), new Item(1420), new Item(1335), new Item(1363), new Item(3096), new Item(1101), new Item(1067), new Item(1081), new Item(1115), new Item(4820, 15), new Item(1137), new Item(1153), new Item(1175), new Item(1191), new Item(4540), new Item(820, 10), new Item(40, 15), new Item(863, 5), new Item(7225), new Item(19572, 5), new Item(9377, 10), new Item(9423)},
			{new Item(1207), new Item(1281), new Item(1325), new Item(1295), new Item(1311), new Item(1353), new Item(1424), new Item(1339), new Item(1365), new Item(3097), new Item(1105), new Item(1069), new Item(1083), new Item(1119), new Item(1539, 15), new Item(1141), new Item(1157), new Item(1177), new Item(1193), new Item(4544), new Item(821, 10), new Item(41, 15), new Item(865, 5), new Item(2370), new Item(19574, 5), new Item(9378, 10), new Item(9425)},
			{new Item(1209), new Item(1285), new Item(1329), new Item(1299), new Item(1315), new Item(1355), new Item(1428), new Item(1343), new Item(1369), new Item(3099), new Item(1109), new Item(1071), new Item(1085), new Item(1121), new Item(4822, 15), new Item(1143), new Item(1159), new Item(1181), new Item(1197), null, new Item(822, 10), new Item(42, 15), new Item(866, 5), new Item(9416), new Item(19576, 5), new Item(9379, 10), new Item(9427)},
			{new Item(1211), new Item(1287), new Item(1331), new Item(1301), new Item(1317), new Item(1357), new Item(1430), new Item(1345), new Item(1371), new Item(3100), new Item(1111), new Item(1073), new Item(1091), new Item(1123), new Item(4823, 15), new Item(1145), new Item(1161), new Item(1183), new Item(1199), null, new Item(823, 10), new Item(43, 15), new Item(867, 5), null, new Item(19578, 5), new Item(9380, 10), new Item(9429)},
			{new Item(1213), new Item(1289), new Item(1333), new Item(1303), new Item(1319), new Item(1359), new Item(1432), new Item(1347), new Item(1373), new Item(3101), new Item(1113), new Item(1079), new Item(1093), new Item(1127), new Item(4824, 15), new Item(1147), new Item(1163), new Item(1185), new Item(1201), null, new Item(824, 10), new Item(44, 15), new Item(868, 5), null, new Item(19580, 5), new Item(9381, 10), new Item(9431)},
			{new Item(9376, 10), new Item(9422), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
	};
	private static final double[] EXPERIENCE = new double[] {12.5, 25, 37.5, 50, 62.5, 75, 17};
	private final int objectId;
	private final int tier;
	private final int componentId;
	private final int amount;
	private int cycle;
	private int ticks;

	public static boolean isBar(Item item) {
		for (Item bar : BARS) {
			if (item.getId() == bar.getId()) return true;
		}
		return false;
	}

	public static Item getBarInInventory(Player player) {
		for (int i = 5; i > -1; i--) {
			if (player.getInventory().containsItems(BARS[i]) && player.getSkills().getLevel(SkillConstants.SMITHING) >= BAR_LEVELS[i]) return BARS[i];
		}
		return null;
	}

	public static int getLevel(final int id) {
		if (id == 9422) {
			return 13;
		} else if (id == 9376) {
			return 8;
		}
		final EnumDefinitions list = EnumDefinitions.get(846);
		if (list == null) {
			return 0;
		}
		return list.getIntValue(id);
	}

	public static int getRequiredBars(final int id) {
		final EnumDefinitions list = EnumDefinitions.get(845);
		if (list == null) {
			return 0;
		}
		return list.getIntValue(id);
	}

	private boolean hasLevel(final Item product, final boolean dialogue) {
		if (player.getSkills().getLevel(SkillConstants.SMITHING) < getLevel(product.getId())) {
			final String message = "You need a Smithing level of " + getLevel(product.getId()) + " to make a " + TextUtils.capitalizeFirstCharacter(PRODUCTS[tier][componentId].getDefinitions().getName()) + ".";
			if (dialogue) {
				player.getDialogueManager().start(new PlainChat(player, message));
			} else {
				player.sendMessage(message);
			}
			return false;
		}
		return true;
	}

	final Map<Integer, Item> usedFakeInventorySlots = new HashMap<>();
	@Override
	public boolean start() {
		if (player.isDead() || player.isFinished()) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			return false;
		}
		final Item product = PRODUCTS[tier][componentId];
		if (!hasLevel(product, true)) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			return false;
		}
		if (!player.getInventory().containsItem(BARS[tier].getId(), getRequiredBars(product.getId()))) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			return false;
		}
		return true;
	}

	@Override
	public boolean process() {
		if (cycle >= amount) {
			return false;
		}
		final Item product = PRODUCTS[tier][componentId];
		if (!hasLevel(product, false)) {
			// boost can run out during smithing
			return false;
		}
		return player.getInventory().containsItem(BARS[tier].getId(), getRequiredBars(product.getId()));
	}

	@Override
	public int processWithDelay() {
		int tickCount = player.getBoonManager().hasBoon(MasterOfTheCraft.class) ? 2 : 4;
		final Item product = PRODUCTS[tier][componentId];
		String name = PRODUCTS[tier][componentId].getDefinitions().getName();
		name = name.replace(BARS[tier].getName().replace("bar", ""), "");
		name = name.replace("knife", "knives").replace("sq shield", "square shield").replace("med", "medium").replace("2h sword", "two-handed sword").replace("helm", "helmet").toLowerCase();
		if (ticks == 0) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			player.setAnimation(ANIMATION);
			player.sendFilteredMessage("You hammer the " + BARS[tier].getDefinitions().getName().replace("bar", "").toLowerCase() + "and make " + (name.endsWith("s") ? "" : "a ") + name + ".");
		} else if (ticks == tickCount) {
			final Item bar = new Item(BARS[tier].getId(), getRequiredBars(product.getId()));
			final boolean isBot = PlayerAttributesKt.getFlaggedAsBot(player);
			player.getInventory().deleteItemsIfContains(new Item[] {bar}, () -> {
				if (product.getId() == PRODUCTS[6][1].getId() && objectId == 2031) {
					player.getAchievementDiaries().update(FaladorDiary.SMITH_BLURITE_LIMBS);
				} else if (product.getId() == PRODUCTS[3][13].getId()) {
					player.getAchievementDiaries().update(DesertDiary.CREATE_MITHRIL_PLATEBODY);
					player.getAchievementDiaries().update(ArdougneDiary.SMITH_MITHRIL_PLATEBODY);
				} else if (product.getId() == PRODUCTS[5][26].getId()) {
					player.getAchievementDiaries().update(ArdougneDiary.MAKE_RUNE_CROSSBOW, 2);
				} else if (product.getId() == PRODUCTS[5][20].getId()) {
					player.getAchievementDiaries().update(VarrockDiary.SMITH_AND_FLETCH_10_RUNE_DARTS, 1);
				} else if (product.getId() == PRODUCTS[4][13].getId()) {
					player.getAchievementDiaries().update(LumbridgeDiary.SMITH_ADAMANT_PLATEBODY);
				} else if (product.getId() == PRODUCTS[4][2].getId()) {
					player.getAchievementDiaries().update(WildernessDiary.SMITH_ADAMANT_SCIMITAR);
				} else if (product.getId() == 1315) {
					SherlockTask.SMITH_MITHRIL_2H.progress(player);
				} else if (product.getId() == 1147) {
					SherlockTask.SMITH_RUNE_MED_HELM.progress(player);
				} else if (product.getId() == 1127) {
					player.getDailyChallengeManager().update(SkillingChallenge.SMITH_RUNE_PLATEBODIES);
				}
				if (product.getId() == 1203) {
					CharlieTask.SMITH_AN_IRON_DRAGGER.progress(player);
				}
				if (!isBot || Utils.randomBoolean(100)) {
					player.getInventory().addItem(PRODUCTS[tier][componentId]);
					player.getSkills().addXp(SkillConstants.SMITHING, bar.getAmount() * EXPERIENCE[tier]);
				}
				cycle++;
			});
			return ticks = 0;
		}
		ticks++;
		return 0;
	}

	public Smithing(int objectId, int tier, int componentId, int amount) {
		this.objectId = objectId;
		this.tier = tier;
		this.componentId = componentId;
		this.amount = amount;
	}
}
