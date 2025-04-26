package com.zenyte.game.world.entity.npc.drop.matrix;

import com.google.gson.Gson;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.GameConstants;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.DefaultGson;
import com.zenyte.game.world.entity.player.Player;
import dev.kord.gateway.ratelimit.IdentifyRateLimiter;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import static com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops.DropTable.GUARANTEED_WEIGHT;

public class NPCDrops {

	/**
	 * JSON Sensitive! Do not adjust!
	 */
	public static final class DropTable {
		Logger log = LoggerFactory.getLogger(DropTable.class);

		public static final int GUARANTEED_WEIGHT = 100_000;

		private int npcId;
		private transient int weight;
		private final Drop[] drops;

		public DropTable(int npcId, int weight, Drop[] drops) {
			this.npcId = npcId;
			this.weight = weight;
			this.drops = drops;
		}

		public int getNpcId() {
			return npcId;
		}

		public void setNpcId(int npcId) {
			this.npcId = npcId;
		}

		public int getWeight() {
			return weight;
		}

		public Drop[] getDrops() {
			return drops;
		}

		public transient ArrayList<Drop> GUARANTEED_DROPS = new ArrayList<>();
		public transient LinkedList<Drop> DYNAMIC_DROPS = new LinkedList<>();

		private transient boolean finalized = false;

		private transient Int2ObjectMap<ObjectArrayList<Drop>> dropMappings = new Int2ObjectOpenHashMap<>();

		public void addDynamicDrop(Drop drop) {
			if(finalized)
				throw new IllegalStateException("Drops are attempted to be added to a table after finalization.");
			if(DYNAMIC_DROPS == null)
				DYNAMIC_DROPS = new LinkedList<>();
			DYNAMIC_DROPS.add(drop);
		}

		public LinkedList<Drop> byRarity() {
			DYNAMIC_DROPS.sort(Comparator.comparingInt(Drop::getBaseRate));
			return new LinkedList<>(DYNAMIC_DROPS);
		}

		public void addGuaranteedDrop(Drop drop) {
			if(finalized)
				throw new IllegalStateException("Drops are attempted to be added to a table after finalization.");
			if(GUARANTEED_DROPS == null)
				GUARANTEED_DROPS = new ArrayList<>();
			GUARANTEED_DROPS.add(drop);
		}

		public Drop[] getGuaranteedDrops() {
			if(GUARANTEED_DROPS == null)
				GUARANTEED_DROPS = new ArrayList<>();
			return GUARANTEED_DROPS.toArray(new Drop[0]);
		}

		public Drop[] getDynamicDrops() {
			if(DYNAMIC_DROPS == null)
				DYNAMIC_DROPS = new LinkedList<>();
			return DYNAMIC_DROPS.toArray(new Drop[0]);
		}

		public void finalizeTable() {
			if(DYNAMIC_DROPS == null)
				DYNAMIC_DROPS = new LinkedList<>();
			if(GUARANTEED_DROPS == null)
				GUARANTEED_DROPS = new ArrayList<>();
			DYNAMIC_DROPS.sort(Comparator.comparingInt(Drop::getBaseRate));

			ObjectArrayList<Drop> always = new ObjectArrayList<>();
			always.addAll(GUARANTEED_DROPS);
			if(dropMappings == null)
				dropMappings = new Int2ObjectOpenHashMap<>();
			if(always.size() != 0)
				dropMappings.put(GUARANTEED_WEIGHT, always);

			for(Drop drop: DYNAMIC_DROPS){
				if(!dropMappings.containsKey(drop.getBaseRate())) {
					ObjectArrayList<Drop> dt = new ObjectArrayList<>();
					dt.add(drop);
					dropMappings.put(drop.getBaseRate(), dt);
				} else {
					dropMappings.get(drop.getBaseRate()).add(drop);
				}
			}

			int newTotalWeight = 0;
			for(Int2ObjectMap.Entry<ObjectArrayList<Drop>> entry : dropMappings.int2ObjectEntrySet()) {
				int weight = entry.getIntKey();
				/* similarly weighted objects should increase the chance of rolling
				that set by the number of items in it as each item is picked randomly

				i.e. two 1/500 drops means a 1/250 chance to get one of the two which is equivalent to the rarity * 2
				*/
				if(weight != 0 && weight != GUARANTEED_WEIGHT)
				 	newTotalWeight += (int) (weight * (double) entry.getValue().size());
			}
			weight = newTotalWeight;
			finalized = true;
		}

		public Int2ObjectMap<ObjectArrayList<Drop>> getMappedDrops() {
			return dropMappings;
		}
	}


	public static final class DisplayedNPCDrop {
		private final int itemId;
		private final int minAmount;
		private final int maxAmount;
		private final BiFunction<Player, Integer, Double> function;
		private BiPredicate<Player, Integer> predicate;

		public DisplayedNPCDrop(final int itemId, final int minAmount, final int maxAmount, final double weight, final int tableWeight) {
			this.itemId = itemId;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
			this.function = (player, npcId) -> weight == GUARANTEED_WEIGHT ? 1 : (weight / tableWeight * 100.0);
		}

		public DisplayedNPCDrop(final int itemId, final int minAmount, final int maxAmount, final BiFunction<Player, Integer, Double> function) {
			this.itemId = itemId;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
			this.function = function;
		}

		public int getItemId() {
			return itemId;
		}

		public int getMinAmount() {
			return minAmount;
		}

		public int getMaxAmount() {
			return maxAmount;
		}

		public BiFunction<Player, Integer, Double> getFunction() {
			return function;
		}

		public BiPredicate<Player, Integer> getPredicate() {
			return predicate;
		}

		public void setPredicate(BiPredicate<Player, Integer> predicate) {
			this.predicate = predicate;
		}
	}


	public static final class DisplayedDropTable {
		private int npcId;
		private final List<DisplayedNPCDrop> drops;

		public DisplayedDropTable(int npcId, List<DisplayedNPCDrop> drops) {
			this.npcId = npcId;
			this.drops = drops;
		}

		public int getNpcId() {
			return npcId;
		}

		public void setNpcId(int npcId) {
			this.npcId = npcId;
		}

		public List<DisplayedNPCDrop> getDrops() {
			return drops;
		}
	}

	private static Int2ObjectMap<DropTable> drops;
	private static Int2ObjectMap<DisplayedDropTable> displayedDrops;
	private static Int2ObjectMap<List<ItemDrop>> dropsByItem;

	public static Drop[] getDrops(final int npcId) {
		final NPCDrops.DropTable table = drops.get(npcId);
		if (table == null) return null;
		return table.drops;
	}

	public static DropTable getTable(final int npcId) {
		return drops.get(npcId);
	}

	public static List<ItemDrop> getTableForItem(final int itemId) {
		return dropsByItem.get(itemId);
	}

	public static boolean equalsIgnoreRates(final int npc1, final int npc2) {
		final List<DropProcessor> processorA = DropProcessorLoader.get(npc1);
		final List<DropProcessor> processorB = DropProcessorLoader.get(npc2);
		final NPCDrops.DropTable tableA = drops.get(npc1);
		final NPCDrops.DropTable tableB = drops.get(npc2);
		if (tableA == tableB && Objects.equals(processorA, processorB)) {
			return true;
		}
		if (tableA == null || tableB == null) {
			return false;
		}
		if (tableA.drops.length != tableB.drops.length) {
			return false;
		}
		for (int i = 0; i < tableA.drops.length; i++) {
			final Drop drop = tableA.drops[i];
			if (CollectionUtils.findMatching(tableB.drops, d -> d.getItemId() == drop.getItemId() && d.getMinAmount() == drop.getMinAmount() && d.getMaxAmount() == drop.getMaxAmount()) != null) {
				continue;
			}
			return false;
		}
		return Objects.equals(processorA, processorB);
	}

	/**
	 * Do not make a subscribable event out of this as it needs to be executed before those.
	 */
	public static void init() {
		try {
			drops = new Int2ObjectOpenHashMap<>();
			displayedDrops = new Int2ObjectOpenHashMap<>();
			dropsByItem = new Int2ObjectOpenHashMap<>(ItemDefinitions.getDefinitions().length);

			Gson gson = DefaultGson.getGson();
			File folder = new File("cache/data/npcs/drops/");
			for (File file : Objects.requireNonNull(folder.listFiles())) {
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					NPCDrops.DropTable table = gson.fromJson(br, NPCDrops.DropTable.class);

					initDropTable(table);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Runnable runnable : onInitRunnables) {
			try {
				runnable.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static final ObjectList<Runnable> onInitRunnables = new ObjectArrayList<>();

	public static void runOnInit(Runnable runnable) {
		onInitRunnables.add(runnable);
	}

	public static void initDropTable(NPCDrops.DropTable table) {
		int weight = 0;
		for (final Drop drop : table.getDrops()) {
			if (drop.getBaseRate() == Drop.GUARANTEED_RATE) {
				table.addGuaranteedDrop(drop);
				continue;
			}
			table.addDynamicDrop(drop);
			weight += drop.getBaseRate();
		}
		Arrays.sort(table.getDrops(), Comparator.comparingInt(Drop::getBaseRate));
		table.weight = weight;
		table.finalizeTable();
		drops.put(table.npcId, table);
		final ObjectList<DisplayedNPCDrop> dropsList = new ObjectArrayList<>(table.drops.length);
		for (final Drop drop : table.drops) {
			if (drop.getItemId() == ItemId.TOOLKIT) {
				continue;
			}
			dropsList.add(new DisplayedNPCDrop(drop.getItemId(), drop.getMinAmount(), drop.getMaxAmount(), drop.getBaseRate(), weight));
		}
		final NPCDrops.DisplayedDropTable clone = new DisplayedDropTable(table.npcId, dropsList);
		displayedDrops.put(table.npcId, clone);
	}




	public static void populateDropViewerData(final List<ItemDefinitions> searchableItemDefinitions) {
		for (final Int2ObjectMap.Entry<List<DropProcessor>> dropProcessorEntry :
				DropProcessorLoader.getProcessors().int2ObjectEntrySet()) {
			final int npcId = dropProcessorEntry.getIntKey();
			final List<DropProcessor> processorsList = dropProcessorEntry.getValue();
			for (final DropProcessor processor : processorsList) {
				for (final DropProcessor.DisplayedDrop displayedDrop : processor.getBasicDrops()) {
					final NPCDrops.DisplayedDropTable table = NPCDrops.displayedDrops.computeIfAbsent(npcId,
							__ -> new NPCDrops.DisplayedDropTable(npcId, new ObjectArrayList<>()));
					final NPCDrops.DisplayedNPCDrop drop = new NPCDrops.DisplayedNPCDrop(displayedDrop.getId(),
							displayedDrop.getMinAmount(), displayedDrop.getMaxAmount(),
							(player, id) -> 1.0 / displayedDrop.getRate(player, id) * 100.0);
					table.getDrops().add(drop);
					if (displayedDrop.getPredicate() != null) {
						drop.setPredicate(displayedDrop.getPredicate());
					}
				}
			}
		}
		for (final Int2ObjectMap.Entry<NPCDrops.DisplayedDropTable> table :
				NPCDrops.displayedDrops.int2ObjectEntrySet()) {
			for (final NPCDrops.DisplayedNPCDrop drop : table.getValue().getDrops()) {
				NPCDrops.dropsByItem.computeIfAbsent(drop.getItemId(), __ -> new ObjectArrayList<>())
						.add(new ItemDrop(table.getIntKey(), drop));
			}
		}
		for (final ItemDefinitions def : ItemDefinitions.getDefinitions()) {
			if (def == null || !NPCDrops.dropsByItem.containsKey(def.getId())) continue;
			searchableItemDefinitions.add(def);
		}
	}

	public void save() {
		if (!GameConstants.WORLD_PROFILE.isDevelopment()) {
			throw new IllegalStateException("Saving drops may only be done on development worlds as it reflects on the actual in-use drops.");
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/npcs/drops.json"))) {
			writer.write(DefaultGson.getGson().toJson(new ArrayList<>(drops.values())));
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void rollTable(Player player, final DropTable drops, final Consumer<Drop> consumer) {
		/* Null check for de-registered players in zone */
		if(player == null || player.getAttributes() == null)
			return;
		final boolean isBot = PlayerAttributesKt.getFlaggedAsBot(player);
		final int weight = drops.getWeight();
		int randomRate = Utils.random(Math.max(DropTable.GUARANTEED_WEIGHT, weight));
		if (isBot) {
			randomRate = (int) (randomRate * 0.01);
		}
		final Drop[] array = drops.getDrops();
		int currentWeight = 0;
		for (int i = array.length - 1; i >= 0; i--) {
			final Drop drop = array[i];
			final int rate = drop.getBaseRate();
			if (rate == Drop.GUARANTEED_RATE) {
				consumer.accept(drop);
				continue;
			}
			if ((currentWeight += rate) >= randomRate) {
				//Treat toolkits as if they're "nothing".
				if (drop.getItemId() == ItemId.TOOLKIT) {
					return;
				}
				consumer.accept(drop);
				return;
			}
		}
	}



	/**
	 * Processes the drops, sending all of the "always" drops and picking one random drop.
	 * @param table the droptable.
	 * @param consumer the consumer executed on the lucky drops.
	 */
	public static void rollTableOld(final Player player, final DropTable table, final Consumer<Drop> consumer) {
		/* this is the total of all numerators on the table, i.e. 2/64+40/64+22/64 = X/64 where 64 is the weight and 2,40,22 are the individual drop "rate"s */
		int weight = table.getWeight();
		if(weight == 0)
			weight = 100000;

		final double playerDRBoost = player.getDropRateBonus();

		final double randomRate = Utils.randomDouble(weight);

		double startRollGap = 0;
		final Int2ObjectMap<ObjectArrayList<Drop>> array = table.getMappedDrops();
		IntSet rolls = array.keySet();
		LinkedList<Integer> possibleRolls = new LinkedList<>(rolls);
		Collections.sort(possibleRolls);

		double stopRollGap = 0;
		for(Drop drop: table.getGuaranteedDrops()) {
			consumer.accept(drop);
		}
		Integer lastRoll = 0;
		for (Integer roll: possibleRolls) {
			assert roll >= lastRoll;
			lastRoll = roll;
			ObjectArrayList<Drop> dropsAtRarity = array.get(roll.intValue());
			if(roll == GUARANTEED_WEIGHT) {
				continue;
			}
			/* Ex. base rate = 2/2000 is effectively 0.1% => we want a 10% DR boost to result in it becoming 0.11% */
			/* We roll a 2.015 on the random rate, placing us outside of the 2,000 denominator without a booster. */
			/* If we apply a 10% increase to just the base rate of the drop, it now is placed at 2.200 instead of 2.000 which grants us this rare drop */
			final int baseRate = roll;
			final double adjustedRate = baseRate * (1.00D + (playerDRBoost / 100D));
			stopRollGap += adjustedRate;
			if (randomRate >= startRollGap && randomRate <= stopRollGap) {
				Collections.shuffle(dropsAtRarity);
				Drop drop = dropsAtRarity.get(0);
				//Treat toolkits as if they're "nothing".
				if (drop.getItemId() == ItemId.TOOLKIT) {
					return;
				}
				consumer.accept(drop);
				return;
			} else {
				startRollGap = stopRollGap;
			}
		}
	}


}
