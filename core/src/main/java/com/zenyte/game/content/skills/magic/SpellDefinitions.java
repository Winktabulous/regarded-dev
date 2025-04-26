package com.zenyte.game.content.skills.magic;

import com.zenyte.game.item.Item;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mgi.types.config.enums.Enums;
import mgi.types.config.enums.IntEnum;
import mgi.types.config.items.ItemDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Kris | 23. mai 2018 : 17:31:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SpellDefinitions {
	private static final Logger log = LoggerFactory.getLogger(SpellDefinitions.class);
	public static final Map<String, SpellDefinitions> SPELLS = new HashMap<>();
	private static final Int2ObjectOpenHashMap<String> spellComponentMap = new Int2ObjectOpenHashMap<>();
	public static final Object2IntOpenHashMap<String> autocastSlotMap = new Object2IntOpenHashMap<>();

	public SpellDefinitions(final int level, final Item[] runes) {
		this.level = level;
		this.runes = runes;
	}

	private final int level;
	private final Item[] runes;
	private static final int[] RUNE_INDEXES = new int[] {365, 367, 369, 606};
	private static final int[] RUNE_AMOUNTS = new int[] {366, 368, 370, 607};

	static {
		final IntEnum[] enums = new IntEnum[] {Enums.REGULAR_SPELLS_ENUM, Enums.ANCIENT_SPELLS_ENUM, Enums.LUNAR_SPELLS_ENUM, Enums.ARCEUUS_SPELLS_ENUM};
		for (final IntEnum spellbookEnum : enums) {
			for (final Int2IntMap.Entry enumEntry : spellbookEnum.getValues().int2IntEntrySet()) {
				final int spellItem = enumEntry.getIntValue();
				final ItemDefinitions definitions = ItemDefinitions.getOrThrow(spellItem);
				final Int2ObjectMap<Object> params = definitions.getParameters();
				if (params == null) throw new RuntimeException("Spell item parameters are null!");
				final String name = (String) params.get(601);
				final int level = (int) params.get(604);
				final ArrayList<Item> runes = new ArrayList<>(4);
				final int componentId = ((int) params.get(596)) & 65535;
				for (int a = 0; a < 4; a++) {
					final int runeIndex = RUNE_INDEXES[a];
					final Object entry = params.get(runeIndex);
					if (entry == null) continue;
					final int amount = RUNE_AMOUNTS[a];
					runes.add(new Item((int) entry, (int) params.get(amount)));
				}
				final SpellDefinitions definition = new SpellDefinitions(level, runes.toArray(new Item[0]));
				final String refactoredName = name.replaceAll("-", "").toLowerCase();
				SPELLS.put(refactoredName, definition);
				spellComponentMap.put(componentId, refactoredName);
			}
		}
		for (final Int2IntMap.Entry entry : Enums.AUTOCASTABLE_SPELLS_ENUM.getValues().int2IntEntrySet()) {
			final int key = entry.getIntKey();
			final int value = entry.getIntValue();
			final ItemDefinitions itemDefinitions = ItemDefinitions.getOrThrow(value);
			final Int2ObjectMap<Object> params = Objects.requireNonNull(itemDefinitions.getParameters());
			final String name = (String) params.get(601);
			final String refactoredName = name.replaceAll("-", "").toLowerCase();
			autocastSlotMap.put(refactoredName, key);
		}
	}

	public static final String getSpellName(final int componentId) {
		try {
			return spellComponentMap.get(componentId);
		} catch (final Exception e) {
			log.error("", e);
		}
		return null;
	}

	public static final int getSpellComponent(final String spellName) {
		try {
			for (final Int2ObjectMap.Entry<String> entry : spellComponentMap.int2ObjectEntrySet()) {
				if (entry.getValue().equalsIgnoreCase(spellName)) {
					return entry.getIntKey();
				}
			}
		} catch (final Exception e) {
			log.error("", e);
		}
		return -1;
	}

	public int getLevel() {
		return level;
	}

	public Item[] getRunes() {
		return runes;
	}
}
