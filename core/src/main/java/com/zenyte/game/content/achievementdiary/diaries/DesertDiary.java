package com.zenyte.game.content.achievementdiary.diaries;

import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.achievementdiary.DiaryArea;
import com.zenyte.game.content.achievementdiary.DiaryChunk;
import com.zenyte.game.content.achievementdiary.DiaryComplexity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.DesertArea;
import com.zenyte.game.world.region.area.SmokeDungeonArea;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.zenyte.game.content.achievementdiary.AreaPredicate.BEDABIN_CAMP;

/**
 * @author Kris | 23. sept 2018 : 05:56:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>
 */
public enum DesertDiary implements Diary {
	CATCH_GOLDEN_WARBLER(EASY, "Catch a Golden Warbler."), //done
	MINE_CLAY(EASY, false, 5, p -> DesertArea.polygon.contains(p.getLocation()), "Mine 5 clay in the north-eastern desert."),  // done
	ENTER_KALPHITE_HIVE(EASY, "Enter the Kalphite Hive."),  // done
	ENTER_DESERT_WITH_DESERT_ROBES(EASY, "Enter the Desert with a set of Desert robes equipped."),  // done
	KILL_VULTURE(EASY, NO_PREDICATE, "Kill a Vulture."),  // done
	GET_HERB_CLEANED(EASY, true, "Have the Nardah Herbalist clean a Herb for you."), COLLECT_POTATO_CACTUS(EASY, true, "Collect 5 Potato Cactus from the Kalphite Hive."), SELL_ARTIFACTS(EASY, true, "Sell some artifacts to Simon Templeton."), OPEN_SARCOPHAGUS(EASY, true, "Open the Sarcophagus in the first room of Pyramid", "Plunder."), CUT_CACTUS(EASY, true, "Cut a desert cactus open to fill a waterskin."), CARPET_TO_POLLNIVNEACH(EASY, true, "Travel from the Shantay Pass to Pollnivneach by Magic", "Carpet."), CLIMB_AGILITY_PYRAMID(MEDIUM, NO_PREDICATE, "Climb to the summit of the Agility Pyramid."),  //done
	SLAY_DESERT_LIZARD(MEDIUM, "Slay a desert lizard."),  // done
	CATCH_ORANGE_SALAMANDER(MEDIUM, "Catch an Orange Salamander."), //done
	STEAL_DARK_PHOENIX_FEATHER(MEDIUM, true, "Steal a feather from the Desert Phoenix."), CARPET_TO_UZER(MEDIUM, true, "Travel to Uzer via Magic Carpet."), TRAVEL_TO_DESERT_VIA_EAGLE(MEDIUM, true, "Travel to the Desert via Eagle."), PRAY_AT_ELIDINIS_STATUETTE(MEDIUM, "Pray at the Elidinis statuette in Nardah."),  // done
	CREATE_COMBAT_POTION(MEDIUM, "Create a combat potion in the desert."),  // done
	TELEPORT_TO_ENAKHRAS_TEMPLE(MEDIUM, true, "Teleport to Enakhra's Temple with the Camulet."), VISIT_THE_GENIE(MEDIUM, true, "Visit the Genie."), TELEPORT_POLLNIVNEACH_HOUSE(MEDIUM, true, "Teleport to Pollnivneach with a redirected teleport to", "house tablet."), CHOP_TEAK_LOGS(MEDIUM, "Chop some Teak logs near Uzer."),  // done
	PICKPOCKET_MENAPHITE_THUG(HARD, true, "Knock out and pickpocket a Menaphite Thug."), MINE_GRANITE(HARD, true, "Mine some Granite."), REFILL_WATERSKINS_WITH_HUMIDIFY(HARD, "Refill your waterskins in the Desert using Lunar magic."),  // done
	KILL_KALPHITE_QUEEN(HARD, NO_PREDICATE, "Kill the Kalphite Queen."),  // done
	COMPLETE_POLLNIVNEACH_AGILITY_COURSE_LAP(HARD, true, "Complete a lap of the Pollnivneach agility course."), SLAY_DUST_DEVIL(HARD, p -> p.inArea(SmokeDungeonArea.class), "Slay a Dust Devil with a Slayer helmet equipped."),  // done
	ACTIVATE_ANCIENT_MAGICKS(HARD, true, "Activate Ancient Magicks at the altar in the Jaldraocht", "Pyramid."), DEFEAT_LOCUST_RIDER_WITH_KERIS(HARD, true, "Defeat a Locust Rider with Keris."), BURN_YEW_LOGS(HARD, "Burn some yew logs on the Nardah Mayor's balcony."),  // done
	CREATE_MITHRIL_PLATEBODY(HARD, "Create a Mithril Platebody in Nardah."),  // done
	BAKE_WILD_PIE(ELITE, "Bake a wild pie at the Nardah Clay Oven."),  // done : The ingredients are unobtainable | Chompy meat
	CAST_ICE_BARRAGE(ELITE, "Cast Ice Barrage against a foe in the Desert."), //done
	FLETCH_DRAGON_DARTS(ELITE, BEDABIN_CAMP::inArea, "Fletch some Dragon darts at the Bedabin Camp."), //done
	SPEAK_TO_KQ_HEAD(ELITE, true, "Speak to the KQ head in your POH."), STEAL_FROM_GRAND_GOLD_CHEST(ELITE, true, "Steal from the Grand Gold Chest in the final room of", "Pyramid Plunder."), RESTORE_85_PRAYER_POINTS(ELITE, "Restore at least 85 Prayer points when praying at the", "Altar in Sophanem.");
	//done
	private final DiaryComplexity type;
	private final int objectiveLength;
	private final String[] task;
	private final Predicate<Player> predicate;
	private final boolean autoCompleted;

	DesertDiary(final DiaryComplexity type, final String... task) {
		this(type, false, 1, p -> DesertArea.polygon.contains(p.getLocation()), task);
	}

	DesertDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
		this(type, autoCompleted, 1, p -> DesertArea.polygon.contains(p.getLocation()), task);
	}

	DesertDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
		this(type, false, 1, predicate, task);
	}

	DesertDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
		this.type = type;
		this.autoCompleted = autoCompleted;
		this.objectiveLength = objectiveLength;
		this.predicate = predicate;
		this.task = task;
	}

	public static final DesertDiary[] VALUES = values();
	public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

	static {
		for (final DiaryComplexity type : DiaryComplexity.VALUES) {
			MAP.put(type, new ArrayList<>(15));
		}
		for (final DesertDiary value : VALUES) {
			if (value.autoCompleted) continue;
			MAP.get(value.type).add(value);
		}
	}

	@Override
	public int taskMaster() {
		return 5520;
	}

	@Override
	public boolean flagging() {
		return false;
	}

	@Override
	public DiaryArea area() {
		return DiaryArea.DESERT;
	}

	@Override
	public String title() {
		return area().getAreaName();
	}

	@Override
	public int diaryStarted() {
		return 4452;
	}

	@Override
	public boolean autoCompleted() {
		return autoCompleted;
	}

	@Override
	public int[][] diaryCompleted() {
		return flow(4483);
	}

	@Override
	public DiaryChunk[] chunks() {
		return new DiaryChunk[] {new DiaryChunk(6295, 4523), new DiaryChunk(6296, 4524), new DiaryChunk(6297, 4525), new DiaryChunk(6298, 4526)};
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
