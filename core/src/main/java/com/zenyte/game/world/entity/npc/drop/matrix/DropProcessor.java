package com.zenyte.game.world.entity.npc.drop.matrix;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.Plugin;
import com.zenyte.tools.DropUtils;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * @author Kris | 12. sept 2018 : 19:35:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public abstract class DropProcessor implements Plugin {

	private static final Logger log = LoggerFactory.getLogger(DropProcessor.class);

	private final Long2ObjectOpenHashMap<PredicatedDrop> infoMap = new Long2ObjectOpenHashMap<>();
	private final List<DisplayedDrop> basicDrops = new ArrayList<>();
	private int[] allIds;

	public void put(final int npcId, final int id, final PredicatedDrop drop) {
		if (infoMap.containsKey(id | ((long) npcId << 32L))) {
			throw new RuntimeException("Overriding predicated drop. npc:"+npcId+" drop:"+id);
		}
		infoMap.put(id | ((long) npcId << 32L), drop);
	}

	public void put(final int id, final PredicatedDrop drop) {
		for (final int npcId : getAllIds()) {
			if (infoMap.containsKey(id | ((long) npcId << 32L))) {
                DropProcessor.log.warn("Overriding predicated drop {} for npc {}.", id, npcId);
				continue;
			}
			infoMap.put(id | ((long) npcId << 32L), drop);
		}
	}

	public int random(final int num) {
		//-1 because if wikia states 256 and you do random(256), you actually get a 1:257 rate.
		return Utils.random(num - 1);
	}

	public int rollPool(Player p, int roll, int... items) {
		if(randomDrop(p, roll) == 0) {
			int slot = random(items.length);
			return items[slot];
		}
		return -1;
	}


	public int randomDrop(Player p, final int num) {
		//-1 because if wikia states 256 and you do random(256), you actually get a 1:257 rate.
		return DropUtils.randomRoll(p, num - 1);
	}

	/**
	 * Appends a drop to the drop viewer interface. Note: The drops added here do not actually
	 * get added to the drop table - this must be done manually in the processor.
	 * @param drop the drop to display.
	 */
	public void appendDrop(final DisplayedDrop drop) {
		basicDrops.add(drop);
	}

	/**
	 * If true then the drop table loaded from {@link  com.near_reality.osrsbox_db.MonsterDefinition}
	 * will not be merged in the {@link  NPCDrops} table.
	 */
	public boolean disregardTableFromDefinitions(){
		return false;
	}

	public abstract void attach();

	public void onDeath(final NPC npc, final Player killer) {
	}

	public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
		return item;
	}

	public abstract int [] ids();

	public Long2ObjectOpenHashMap<PredicatedDrop> getInfoMap() {
		return infoMap;
	}

	public List<DisplayedDrop> getBasicDrops() {
		return basicDrops;
	}

	public int [] getAllIds() {
		if (allIds == null)
			allIds = ids();
		return allIds;
	}


	public static class PredicatedDrop {
		private final BiPredicate<Player, Integer> predicate;
		private final String information;

		public PredicatedDrop(final String information) {
			this.predicate = (player, npc) -> true;
			this.information = information;
		}

		public PredicatedDrop(BiPredicate<Player, Integer> predicate, String information) {
			this.predicate = predicate;
			this.information = information;
		}

		public BiPredicate<Player, Integer> getPredicate() {
			return predicate;
		}

		public String getInformation() {
			return information;
		}
	}


	public static class DisplayedDrop {
		private final int id;
		private final int minAmount;
		private final int maxAmount;
		private double rate;//1 : rate
		private BiPredicate<Player, Integer> predicate;
		private int[] npcIds;

		public DisplayedDrop(final int id, final int minAmount, final int maxAmount, final double rate) {
			this.id = id;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
			this.rate = rate;
		}

		public DisplayedDrop(final int id, final int minAmount, final int maxAmount, final double rate, final BiPredicate<Player, Integer> predicate, final int... npcIds) {
			this.id = id;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
			this.rate = rate;
			this.predicate = predicate;
			this.npcIds = npcIds;
		}

		public double getRate(final Player player, final int id) {
			return rate;
		}

		public DisplayedDrop(int id, int minAmount, int maxAmount) {
			this.id = id;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
		}

		public int getId() {
			return id;
		}

		public int getMinAmount() {
			return minAmount;
		}

		public int getMaxAmount() {
			return maxAmount;
		}

		public double getRate() {
			return rate;
		}

		public BiPredicate<Player, Integer> getPredicate() {
			return predicate;
		}

		public void setNpcIds(int[] npcIds) {
			this.npcIds = npcIds;
		}

		public int[] getNpcIds() {
			return npcIds;
		}

		@Override
		public String toString() {
			return "DropProcessor.DisplayedDrop(id=" + this.getId() + ", minAmount=" + this.getMinAmount() + ", maxAmount=" + this.getMaxAmount() + ", rate=" + this.getRate() + ", predicate=" + this.getPredicate() + ", npcIds=" + Arrays.toString(this.getNpcIds()) + ")";
		}
	}
}
