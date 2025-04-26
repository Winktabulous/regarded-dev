package com.zenyte.game.content.achievementdiary.diaries;

import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.achievementdiary.DiaryArea;
import com.zenyte.game.content.achievementdiary.DiaryChunk;
import com.zenyte.game.content.achievementdiary.DiaryComplexity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.game.world.region.area.wilderness.WildernessGodwarsDungeon;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.zenyte.game.content.achievementdiary.AreaPredicate.*;

/**
 * @author Kris | 20. sept 2018 : 18:03:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>
 */
public enum WildernessDiary implements Diary {
	CAST_LOW_ALCHEMY(EASY, FOUNTAIN_OF_RUNE::inArea, "Cast Low Alchemy at the Fountain of Rune."), //done
	USE_LEVER(EASY, NO_PREDICATE, "Enter the Wilderness from the Ardougne or Edgeville lever."), //done
	PRAY_AT_CHAOS_ALTAR(EASY, WILDERNESS_WEST_CHAOS_ALTAR::inArea, "Pray at the Chaos Altar in Western Wilderness."), //done
	ENTER_CHAOS_RUNECRAFTING_TEMPLE(EASY, NO_PREDICATE, "Enter the Chaos Runecrafting temple."), //done
	KILL_A_MAMMOTH(EASY, "Kill a Mammoth in the Wilderness."), //done
	KILL_AN_EARTH_WARRIOR(EASY, "Kill an Earth Warrior in the Wilderness beneath Edgeville."), //done
	RESTORE_PRAYER_POINTS(EASY, true, "Restore some prayer points at the demonic ruins."), ENTER_KING_BLACK_DRAGON_LAIR(EASY, "Enter the King Black Dragon's lair."), //done
	COLLECT_SPIDERS_EGGS(EASY, 5, "Collect 5 Red spiders' eggs from the Wilderness."), //done
	MINE_IRON_ORE(EASY, "Mine some Iron ore in the Wilderness."), //done
	TELEPORT_TO_ABYSS(EASY, "Have the Mage of Zamorak teleport you to the Abyss."), //done
	EQUIP_TEAM_CAPE(EASY, "Equip any team cape in the Wilderness."), //done
	MINE_MITHRIL_ORE(MEDIUM, "Mine some Mithril ore in the wilderness."), //done
	CHOP_YEW_LOGS(MEDIUM, "Chop some yew logs from a fallen Ent."),
	ENTER_WILDERNESS_GODWARS_DUNGEON(MEDIUM, "Enter the Wilderness Godwars Dungeon."), COMPLETE_AGILITY_COURSE_LAP(MEDIUM, "Complete a lap of the Wilderness Agility course."), //done
	KILL_A_GREEN_DRAGON(MEDIUM, "Kill a Green Dragon."), //done
	KILL_AN_ANKOU(MEDIUM, "Kill an Ankou in the Wilderness."), //done
	CHARGE_AN_EARTH_ORB(MEDIUM, "Charge an Earth Orb."), //done
	KILL_A_BLOODVELD(MEDIUM, p -> p.inArea(WildernessGodwarsDungeon.class), "Kill a Bloodveld in the Wilderness Godwars Dungeon."), SELL_MYSTERIOUS_EMBLEM(MEDIUM, true, "Sell a Mysterious Emblem to the Emblem Trader in Edgeville."), SMITH_GOLDEN_HELMET(MEDIUM, true, "Smith a Golden helmet in the Resource Area."), OPEN_THE_MUDDY_CHEST(MEDIUM, true, "Open the Muddy Chest in the lava maze."), CAST_GOD_SPELL(HARD, "Cast one of the 3 God spells against another player in the", "Wilderness."), //done
	CHARGE_AIR_ORB(HARD, "Charge an Air Orb."), //done
	CATCH_A_BLACK_SALAMANDER(HARD, "Catch a Black Salamander in the Wilderness."), //done
	SMITH_ADAMANT_SCIMITAR(HARD, WILDERNESS_RESOURCE_AREA::inArea, "Smith an Adamant scimitar in the Resource Area."), //done
	KILL_A_LAVA_DRAGON(HARD, false, 1 | 2, LAVA_DRAGON_ISLE::inArea, "Kill a Lava Dragon & Bury the bones on Lava Dragon Isle.") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	//done
	KILL_THE_CHAOS_ELEMENTAL(HARD, "Kill the Chaos Elemental."), //done
	KILL_CRAZY_ARCHEAOLOGIST(HARD, 1 | 2 | 4, "Kill the Crazy Arc. Chaos Fanatic & Scorpia.") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	//done
	TAKE_AGILITY_SHORTCUT(HARD, "Take the agility shortcut from Trollheim into the", "Wilderness."),
	KILL_A_SPIRITUAL_WARRIOR(HARD, p -> p.inArea(WildernessGodwarsDungeon.class), "Kill a Spiritual warrior in the Wilderness Godwars Dungeon."),
	FISH_RAW_LAVA_EEL(HARD, true, "Fish some Raw Lava Eel in the Wilderness."),
	KILL_CALLISTO(ELITE, 1 | 2 | 4, "Kill Callisto, Venenatis & Vet'ion.") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	//done
	TELEPORT_TO_GHORROCK(ELITE, true, "Teleport to Ghorrock."), //done
	FISH_AND_COOK_DARK_CRAB(ELITE, false, 1 | 2, WILDERNESS_RESOURCE_AREA::inArea, "Fish and Cook a Dark Crab in the Resource Area.") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	SMITH_RUNE_SCIMITAR(ELITE, true, "Smith a rune scimitar from scratch in the Resource Area."),
	STEAL_FROM_ROGUES_CHEST(ELITE, "Steal from the Rogues' chest."),
	SLAY_A_SPIRITRUAL_MAGE(ELITE, p -> p.inArea(WildernessGodwarsDungeon.class), "Slay a spiritual mage inside the wilderness Godwars", "Dungeon."),
	CUT_AND_BURN_MAGIC_LOGS(ELITE, false, 1 | 2, WILDERNESS_RESOURCE_AREA::inArea, "Cut and burn some magic logs in the Resource Area.") {
		@Override
		public boolean flagging() {
			return true;
		}
	};
	//done
	private final DiaryComplexity type;
	private final int objectiveLength;
	private final String[] task;
	private final Predicate<Player> predicate;
	private final boolean autoCompleted;

	WildernessDiary(final DiaryComplexity type, final String... task) {
		this(type, false, 1, WildernessArea::isWithinWilderness, task);
	}

	WildernessDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
		this(type, autoCompleted, 1, WildernessArea::isWithinWilderness, task);
	}

	WildernessDiary(final DiaryComplexity type, final int objectiveLength, final String... task) {
		this(type, false, objectiveLength, WildernessArea::isWithinWilderness, task);
	}

	WildernessDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
		this(type, false, 1, predicate, task);
	}

	WildernessDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
		this.type = type;
		this.autoCompleted = autoCompleted;
		this.objectiveLength = objectiveLength;
		this.predicate = predicate;
		this.task = task;
	}

	public static final WildernessDiary[] VALUES = values();
	public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

	static {
		for (final DiaryComplexity type : DiaryComplexity.VALUES) {
			MAP.put(type, new ArrayList<>(15));
		}
		for (final WildernessDiary value : VALUES) {
			if (value.autoCompleted) continue;
			MAP.get(value.type).add(value);
		}
	}

	@Override
	public int taskMaster() {
		return 5514;
	}

	@Override
	public DiaryArea area() {
		return DiaryArea.WILDERNESS;
	}

	@Override
	public String title() {
		return "Wilderness Area";
	}

	@Override
	public boolean flagging() {
		return false;
	}

	@Override
	public int diaryStarted() {
		return 4457;
	}

	@Override
	public boolean autoCompleted() {
		return autoCompleted;
	}

	@Override
	public int[][] diaryCompleted() {
		return flow(4466);
	}

	@Override
	public DiaryChunk[] chunks() {
		return new DiaryChunk[] {new DiaryChunk(6323, 4507), new DiaryChunk(6324, 4508), new DiaryChunk(6325, 4509), new DiaryChunk(6326, 4510)};
	}

	@Override
	public Map<DiaryComplexity, List<Diary>> map() {
		return MAP;
	}

	public DiaryComplexity type() {
		return type;
	}

	public int objectiveLength() {
		return objectiveLength;
	}

	public String[] task() {
		return task;
	}

	public Predicate<Player> predicate() {
		return predicate;
	}
}
