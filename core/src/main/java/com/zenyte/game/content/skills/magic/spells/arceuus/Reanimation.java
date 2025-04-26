package com.zenyte.game.content.skills.magic.spells.arceuus;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mgi.Indice;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.CollectionUtils;

import java.util.function.IntPredicate;

/**
 * @author Kris | 13. juuli 2018 : 22:40:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum Reanimation {
	REANIMATE_GOBLIN(ReanimationType.BASIC, 6, 130, 13448, 7018, new NPCEntry("Goblin", 30, id -> NPCDefinitions.getOrThrow(id).getCombatLevel() >= 11), new NPCEntry("Goblin", 35)),
	REANIMATE_MONKEY(ReanimationType.BASIC, 14, 182, 13451, 7019, new NPCEntry("Monkey", 35)),
	REANIMATE_IMP(ReanimationType.BASIC, 24, 286, 13454, 7020, new NPCEntry("Imp", 25, i -> i < 15000)),
	REANIMATE_MINOTAUR(ReanimationType.BASIC, 32, 364, 13457, 7021, new NPCEntry("Minotaur", 50)), //TODO: lowercase + contains, random -1
	REANIMATE_SCORPION(ReanimationType.BASIC, 38, 454, 13460, 7022, new NPCEntry("Scorpion", 25, (npcId) -> {
		final NPCDefinitions def = NPCDefinitions.getOrThrow(npcId);
		return !def.getName().contains("Crystalline") && !def.getName().contains("Corrupted");
	}), new NPCEntry("Giant lobster", 25), new NPCEntry("Scorpia", 18)),
	REANIMATE_BEAR(ReanimationType.BASIC, 42, 480, 13463, 7023, new NPCEntry("Black bear", 25), new NPCEntry("Grizzly bear", 25), new NPCEntry("Bear Cub", 25), new NPCEntry("Grizzly bear cub", 25)),
	REANIMATE_UNICORN(ReanimationType.BASIC, 44, 494, 13466, 7024, new NPCEntry("Unicorn", 35, (npcId) -> {
		final NPCDefinitions def = NPCDefinitions.getOrThrow(npcId);
		return !def.getName().contains("Crystalline") && !def.getName().contains("Corrupted");
	})),
	REANIMATE_DOG(ReanimationType.BASIC, 52, 520, 13469, 7025, new NPCEntry("Guard dog", 25), new NPCEntry("Wild dog", 25)),
	REANIMATE_CHAOS_DRUID(ReanimationType.ADEPT, 60, 584, 13472, 7026, new NPCEntry("Elder chaos druid", 20), new NPCEntry("Chaos druid warrior", 25), new NPCEntry("Chaos druid", 20)),
	REANIMATE_GIANT(ReanimationType.ADEPT, 74, 650, 13475, 7027, new NPCEntry("Obor", 1), new NPCEntry("Fire giant", 20), new NPCEntry("Ice giant", 21), new NPCEntry("Moss giant", 24), new NPCEntry("Hill giant", 25), new NPCEntry("Black knight titan", 10)),
	REANIMATE_OGRE(ReanimationType.ADEPT, 80, 716, 13478, 7028, new NPCEntry("Ogre", 30)),
	REANIMATE_ELF(ReanimationType.ADEPT, 86, 754, 13481, 7029, new NPCEntry("Elf Warrior", 40, i -> NPCDefinitions.getOrThrow(i).getCombatLevel() > 90), new NPCEntry("Elf warrior", 50)),
	REANIMATE_TROLL(ReanimationType.ADEPT, 93, 780, 13484, 7030, new NPCEntry("Troll spectator", 45), new NPCEntry("Mountain troll", 45), new NPCEntry("Thrower troll", 45), new NPCEntry("Stick", 28), new NPCEntry("Kraka", 28), new NPCEntry("Pee Hat", 28), new NPCEntry("Troll general", 28), new NPCEntry("Ice troll", 20)),
	REANIMATE_HORROR(ReanimationType.ADEPT, 104, 832, new int[] {13486, 13487}, 7031, new NPCEntry("Jungle horror", 40), new NPCEntry("Cave horror", 30)),
	REANIMATE_KALPHITE(ReanimationType.EXPERT, 114, 884, 13490, 7032, new NPCEntry("Kalphite queen", 20), new NPCEntry("Kalphite guardian", 35), new NPCEntry("Kalphite soldier", 90), new NPCEntry("Kalphite worker", 250)),
	REANIMATE_DAGANNOTH(ReanimationType.EXPERT, 124, 936, 13493, 7033, new NPCEntry("Dagannoth Prime", 20), new NPCEntry("Dagannoth supreme", 20), new NPCEntry("Dagannoth prime", 20), new NPCEntry("Dagannoth", 40, i -> {
		final int level = NPCDefinitions.getOrThrow(i).getCombatLevel();
		return level == 74 || level == 92 || level == 88;
	}), new NPCEntry("Dagannoth", 35)),
	REANIMATE_BLOODVELD(ReanimationType.EXPERT, 130, 1040, 13496, 7034, new NPCEntry("Insatiable bloodveld", 1), new NPCEntry("Insatiable mutated bloodveld", 1), new NPCEntry("Bloodveld", 35)),
	REANIMATE_TZHAAR(ReanimationType.EXPERT, 138, 1104, 13499, 7035, new NPCEntry("Tzhaar-ket", 35)),
	REANIMATE_DEMON(ReanimationType.EXPERT, 144, 1170, 13502, 7036, new NPCEntry("Lesser demon", 50), new NPCEntry("Greater demon", 40), new NPCEntry("Black demon", 35)),
	REANIMATE_AVIANSIE(ReanimationType.MASTER, 156, 1234, 13505, 7037, new NPCEntry("Aviansie", 20, i -> i == 3181 || i == 3179 || i == 3177 || i == 3183 || i == 3182 || i == 3175 || i == 3171 || i == 3169), new NPCEntry("Aviansie", 35)),
	REANIMATE_ABYSSAL_CREATURE(ReanimationType.MASTER, 170, 1300, 13508, 7038, new NPCEntry("Greater abyssal demon", 1), new NPCEntry("Abyssal demon", 25), new NPCEntry("Abyssal walker", 40)),
	REANIMATE_DRAGON(ReanimationType.MASTER, 186, 1560, 13511, 7039, new NPCEntry("Brutal black dragon", 20), new NPCEntry("Brutal red dragon", 20), new NPCEntry("Brutal blue dragon", 20), new NPCEntry("Brutal green dragon", 28), new NPCEntry("Black dragon", 35, id -> id != 6502), new NPCEntry("Green dragon", 35), new NPCEntry("Red dragon", 40), new NPCEntry("Blue dragon", 50));

	private static final Reanimation[] VALUES = values();

	public static final class ReanimatedNPCProcessor extends DropProcessor {
		@Override
		public void attach() {
			loop:
			for (final int i : getAllIds()) {
				final NPCDefinitions definitions = NPCDefinitions.get(i);
				if (definitions == null) {
					continue;
				}
				final String name = definitions.getName().toLowerCase();
				for (final Reanimation value : VALUES) {
					for (final Reanimation.NPCEntry entry : value.entries) {
						if (name.contains(entry.name) && (entry.predicate == null || entry.predicate.test(i))) {
							this.appendDrop(new DisplayedDrop(value.itemId[0], 1, 1, entry.rate, (player, npcId) -> npcId == i, i));
							continue loop;
						}
					}
				}
			}
		}

		@Override
		public void onDeath(final NPC npc, final Player killer) {
			final String name = npc.getDefinitions().getName().toLowerCase();
			for (final Reanimation value : VALUES) {
				for (final Reanimation.NPCEntry entry : value.entries) {
					if (name.contains(entry.name) && (entry.predicate == null || entry.predicate.test(npc.getId()))) {
						for (int i : value.itemId) {
							if (random(entry.rate) == 0) {
								final Item ensouledHead = new Item(i);
								ensouledHead.setAttribute("ensouled head drop time", Utils.currentTimeMillis());
								ensouledHead.setAttribute("Tradability", Boolean.TRUE);
								npc.dropItem(killer, ensouledHead);
							}
						}
						return;
					}
				}
			}
		}

		@Override
		public int[] ids() {
			final IntOpenHashSet set = new IntOpenHashSet();
			loop:
			for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS); i++) {
				final NPCDefinitions definitions = NPCDefinitions.get(i);
				if (definitions == null) {
					continue;
				}
				final String name = definitions.getName().toLowerCase();
				if (name.contains("Reanimated")) {
					continue;
				}
				for (final Reanimation value : VALUES) {
					for (final Reanimation.NPCEntry entry : value.entries) {
						if (name.contains(entry.name) && (entry.predicate == null || entry.predicate.test(i))) {
							set.add(i);
							continue loop;
						}
					}
				}
			}
			return set.toIntArray();
		}
	}

	private final double magicExperience;
	private final double prayerExperience;
	private final int[] itemId;
	private final int npcId;

	public ReanimationType getType() {
		return type;
	}

	private final ReanimationType type;
	private final NPCEntry[] entries;

	Reanimation(ReanimationType type, final double magicExperience, final double prayerExperience, final int itemId, final int npcId, final NPCEntry... entries) {
		this.type = type;
		this.magicExperience = magicExperience;
		this.prayerExperience = prayerExperience;
		this.itemId = new int[] {itemId};
		this.npcId = npcId;
		this.entries = entries;
	}

	Reanimation(ReanimationType type, final double magicExperience, final double prayerExperience, final int[] itemId, final int npcId, final NPCEntry... entries) {
		this.type = type;
		this.magicExperience = magicExperience;
		this.prayerExperience = prayerExperience;
		this.itemId = itemId;
		this.npcId = npcId;
		this.entries = entries;
	}

	public double getMagicExperience() {
		return magicExperience;
	}

	public double getPrayerExperience() {
		return prayerExperience;
	}

	public int[] getItemId() {
		return itemId;
	}

	public int getNpcId() {
		return npcId;
	}

	public NPCEntry[] getEntries() {
		return entries;
	}

	private static final class NPCEntry {
		private final String name;
		private final int rate;
		private final IntPredicate predicate;

		NPCEntry(final String name, final int rate) {
			this(name, rate, null);
		}

		NPCEntry(final String name, final int rate, final IntPredicate predicate) {
			this.name = name.toLowerCase();
			this.rate = rate;
			this.predicate = predicate;
		}
	}
}
