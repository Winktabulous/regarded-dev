package com.zenyte.game.content.treasuretrails;

import com.zenyte.game.util.Utils;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.content.treasuretrails.ClueLevel.*;

/**
 * @author Kris | 30. march 2018 : 0:11.34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Puzzle {
	HARD_CASTLE(1354, 19911, HARD), HARD_TREE(1355, 19887, HARD), ELITE_TREE(1355, 12161, ELITE), HARD_TROLL(1356, 19903, HARD), MASTER_ZULRAH(1357, 20280, MASTER), MASTER_CERBERUS(1358, 20281, MASTER), MASTER_GNOME_CHILD(1359, 20282, MASTER), THEATRE_OF_BLOOD(2318, 23417, MASTER);
	private static final Puzzle[] values = values();
	private static final Int2ObjectMap<Puzzle> map = Int2ObjectMaps.unmodifiable((Int2ObjectMap<Puzzle>) CollectionUtils.populateMap(values, new Int2ObjectOpenHashMap<>(), Puzzle::getPuzzleBox));
	private static final int[] puzzleBoxArray = new IntOpenHashSet(new IntArrayList(map.int2ObjectEntrySet().stream().mapToInt(entry -> entry.getValue().getPuzzleBox()).toArray())).toIntArray();
	private final int enumId;
	private final int puzzleBox;
	private final ClueLevel level;

	Puzzle(int enumId, int puzzleBox, ClueLevel level) {
		this.enumId = enumId;
		this.puzzleBox = puzzleBox;
		this.level = level;
	}

	public static final Puzzle random(@NotNull final ClueLevel level) {
		final ObjectArrayList<Puzzle> list = new ObjectArrayList<Puzzle>();
		for (final Puzzle value : values) {
			if (value.level == level) {
				list.add(value);
			}
		}
		if (list.isEmpty()) {
			throw new RuntimeException();
		}
		return list.get(Utils.random(list.size() - 1));
	}

	public static Int2ObjectMap<Puzzle> getMap() {
		return map;
	}

	public static int[] getPuzzleBoxArray() {
		return puzzleBoxArray;
	}

	public int getEnumId() {
		return enumId;
	}

	public int getPuzzleBox() {
		return puzzleBox;
	}

	public ClueLevel getLevel() {
		return level;
	}
}
