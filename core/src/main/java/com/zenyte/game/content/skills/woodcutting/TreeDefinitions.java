package com.zenyte.game.content.skills.woodcutting;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.object.ObjectId;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

/**
 * @author Kris | 13. dets 2017 : 6:07.17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public enum TreeDefinitions {

	TREE(1, 25, 1511, 5, 1, 1, 317647, 5000),
	FRUIT_TREE(1, 0, -1, 10, 1, 1, 317647, 4500),
	JUNGLE_TREE(1, 25, 1511, 8, 8, 1, 317647, 4250),
	ACHEY_TREE(1, 25, 2862, 9, 1, 1, 317647, 4000),
	OAK(15, 37.5, 1521, 12, 8, 15, 361146, 4000),
	WILLOW_TREE(30, 67.5, 1519, 15, 8, 30, 289286, 2000),
	TEAK_TREE(35, 85, 6333, 17, 8, 15, 264336, 1900),
	MAPLE_TREE(45, 100, 1517, 20, 8, 45, 221918, 1800),
	HOLLOW_TREE(45, 82.5, 3239, 20, 8, 10, 214367, 1700),
	MAHOGANY_TREE(50, 125, 6332, 22, 8, 50, 220623, 1600),
	ARCTIC_PINE(54, 140.2, 10810, 24, 8, 54, 145758, 1500),
	YEW_TREE(60, 175, 1515, 27, 8, 60, 145013, 1250),
	BLISTERWOOD(62, 76, 24691, 1, 10, 60, -1, 1250),

	SULLIUSCEP_TREE(65, 127, 6004, 30, 16, 50, 343000, 1250),
	//magic logs are the slowest in osrs.
	MAGIC_TREE(75, 250, 1513, 35, 8, 95, 72321, 1000),
	REDWOOD_TREE(90, 380, 19669, 75, 11, 90, 72321, 1000);

	private final int level;
	private final double xp;
	private final int logsId;
	private final int respawnDelay;
	private final int fallChance;
	private final int outfitChance;
	private final int speed;
	private final int clueNestBaseChance;
	public static final List<TreeDefinitions> values = Collections.unmodifiableList(Arrays.asList(values()));
	private static final Int2IntMap stumps = new Int2IntOpenHashMap();

	static {
		stumps.put(ObjectId.OAK_10820, ObjectId.TREE_STUMP_1356);
		stumps.put(1277, 1343);
		stumps.put(1279, 1345);
		stumps.put(1280, 1343);
		stumps.put(1284, 1347);
		stumps.put(29668, 29669);
		stumps.put(29670, 29671);
		stumps.put(27060, 27061);
		stumps.put(1285, 1342);
		stumps.put(1318, 1342);
		stumps.put(1319, 1342);
		stumps.put(1330, 1348);
		stumps.put(1331, 1348);
		stumps.put(1332, 1348);
		stumps.put(1290, 1354);
		stumps.put(1759, 1343);
		stumps.put(1761, 9713);
		stumps.put(2023, 3371);
		stumps.put(2887, 1344);
		stumps.put(3879, 3880);
		stumps.put(3881, 3880);
		stumps.put(3882, 3880);
		stumps.put(3883, 3884);
		stumps.put(4674, 9712);
		stumps.put(4820, 4821);
		stumps.put(5902, 1348);
		stumps.put(9034, 9035);
		stumps.put(9730, 1350);
		stumps.put(9731, 1350);
		stumps.put(9732, 1347);
		stumps.put(9733, 1345);
		stumps.put(14308, 1342);
		stumps.put(14309, 1342);
		stumps.put(16264, 21274);
		stumps.put(16265, 1356);
		stumps.put(1757, 4061);
		stumps.put(1752, 2310);
		stumps.put(1276, 1342);
		stumps.put(1751, 1356);
		stumps.put(1278, 1342);
		stumps.put(1282, 1347);
		stumps.put(1286, 1351);
		stumps.put(1283, 1347);
		stumps.put(1289, 1353);
		stumps.put(2091, 1342);
		stumps.put(2092, 1355);
		stumps.put(1753, 9714);
		stumps.put(1760, 9471);
		stumps.put(1756, 9471);
		stumps.put(1750, 9711);
		stumps.put(1758, 9471);
		stumps.put(1365, 1352);
		stumps.put(1755, 9714);
		stumps.put(ObjectId.JUNGLE_BUSH, ObjectId.SLASHED_BUSH);
		stumps.put(ObjectId.JUNGLE_BUSH_2893, ObjectId.SLASHED_BUSH_2895);
	}

	public static int getStumpId(final int treeId) {
		return stumps.getOrDefault(treeId, 1341);
	}

	public static OptionalInt findLogs(final Player player) {
		final Inventory inventory = player.getInventory();
		for (final TreeDefinitions def : values) {
			if (def.logsId == -1) {
				continue;
			}
			if (inventory.containsItem(def.logsId, 1)) {
				return OptionalInt.of(def.getLogsId());
			}
		}
		return OptionalInt.empty();
	}

	TreeDefinitions(int level, double xp, int logsId, int respawnDelay, int fallChance, int speed, int clueNestBaseChance, int outfitChance) {
		this.level = level;
		this.xp = xp;
		this.logsId = logsId;
		this.respawnDelay = respawnDelay;
		this.fallChance = fallChance;
		this.outfitChance = outfitChance;
		this.speed = speed;
		this.clueNestBaseChance = clueNestBaseChance;
	}

	public int getLevel() {
		return level;
	}

	public double getXp() {
		return xp;
	}

	public int getLogsId() {
		return logsId;
	}

	public int getRespawnDelay() {
		return respawnDelay;
	}

	public int getFallChance() {
		return fallChance;
	}
	public int getOutfitChance() {
		return outfitChance;
	}

	public int getSpeed() {
		if(speed > 1)
			return (int) (speed / 1.20D);
		return speed;
	}

	public int getClueNestBaseChance() {
		return clueNestBaseChance;
	}
}
