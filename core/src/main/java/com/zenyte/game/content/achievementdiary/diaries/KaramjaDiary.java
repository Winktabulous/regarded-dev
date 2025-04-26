package com.zenyte.game.content.achievementdiary.diaries;

import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.achievementdiary.DiaryArea;
import com.zenyte.game.content.achievementdiary.DiaryChunk;
import com.zenyte.game.content.achievementdiary.DiaryComplexity;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.zenyte.game.content.achievementdiary.AreaPredicate.*;

/**
 * @author Kris | 2. okt 2018 : 22:13:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>
 */
public enum KaramjaDiary implements Diary {
	PICK_BANANAS(EASY, true, "Pick 5 bananas from the plantation located east of the", "volcano."),
	USE_ROPE_SWING(EASY, "Use the rope swing to travel to the small island north-west", "of Karamja, where the moss giants are."),
	MINE_GOLD(EASY, "Mine some gold from the rocks on the north-west", "peninsula of Karamja."), //done
	TRAVEL_TO_PORT_SARIM(EASY, "Travel to Port Sarim via the dock, east of Musa Point."), //done
	TRAVEL_TO_ARDOUGNE(EASY, "Travel to Ardougne via the port near Brimhaven."), //done
	EXPLORE_CAIRN_ISLAND(EASY, true, "Explore Cairn Island to the west of Karamja."),
	USE_FISHING_SPOTS(EASY, KARAMJA_FISHING_AREA::inArea, "Use the Fishing spots north of the banana plantation."), //done
	COLLECT_SEAWEED(EASY, true, "Collect 5 seaweed from anywhere on Karamja."),
	ATTEMPT_FIGHT_PITS_OR_CAVES(EASY, NO_PREDICATE, "Attempt the TzHaar Fight Pits or Fight Cave."), //done
	KILL_A_JOGRE(EASY, true, "Kill a jogre in the Pothole dungeon."),
	CLAIM_AN_AGILITY_ARENA_TICKET(MEDIUM, true, "Claim a ticket from the Agility Arena in Brimhaven."),
	DISCOVER_HIDDEN_WALL(MEDIUM, true, "Discover hidden wall in the dungeon below the volcano."),
	VISIT_THE_ISLE_OF_CRANDOR(MEDIUM, true, "Visit the Isle of Crandor via the dungeon below the", "volcano."),
	USE_CART_SERVICE(MEDIUM, true, "Use Vigroy and Hajedy's cart service."),
	EARN_100_PERCENT_FAVOUR(MEDIUM, true, "Earn 100% favour in the village of Tai Bwo Wannai."),
	COOK_A_SPIDER_ON_A_STICK(MEDIUM, true, "Cook a spider on a stick."),
	TRAVEL_TO_PORT_KHAZARD(MEDIUM, true, "Charter the Lady of the Waves from Cairn Isle to Port", "Khazard."),
	CUT_A_TEAK_LOG(MEDIUM, "Cut a log from a teak tree."), //done
	CUT_A_MAHOGANY_LOG(MEDIUM, "Cut a log from a mahogany tree."), //done
	CATCH_A_KARAMBWAN(MEDIUM, NO_PREDICATE, "Catch a karambwan."), //done
	EXCHANGE_GEMS(MEDIUM, true, "Exchange gems for a machete."),
	USE_GNOME_GLIDER(MEDIUM, p -> !p.inArea("Karamja"), "Use the gnome glider to travel to Karamja."),
	GROW_A_HEALTHY_FRUIT_TREE(MEDIUM, "Grow a healthy fruit tree in the patch near Brimhaven."), //done
	TRAP_A_HORNED_GRAAHK(MEDIUM, NO_PREDICATE, "Trap a horned graahk."), //done
	CHOP_THE_BRIMHAVEN_DUNGEON_VINES(MEDIUM, NO_PREDICATE, "Chop the vines to gain deeper access to Brimhaven", "Dungeon."),
	CROSS_THE_LAVA(MEDIUM, NO_PREDICATE, "Cross the lava using the stepping stones within Brimhaven", "Dungeon."), //done
	CLIMB_THE_STAIRS_IN_BRIMHAVEN_DUNGEON(MEDIUM, NO_PREDICATE, "Climb the stairs within Brimhaven Dungeon."), //done
	CHARTER_A_SHIP_FROM_SHIPYARD(MEDIUM, "Charter a ship from the shipyard in the far east of", "Karamja."), //done
	MINE_A_RED_TOPAZ(MEDIUM, NO_PREDICATE, "Mine a red topaz from a gem rock."),
	BECOME_FIGHT_PITS_CHAMPION(HARD, true, "Become the Champion of the Fight Pits."),
	KILL_KET_ZEK(HARD, NO_PREDICATE, "Successfully kill a Ket-Zek in the Fight Caves."), //done
	EAT_AN_OOMLIE_WRAP(HARD, true, "Eat an oomlie wrap."),
	CRAFT_NATURE_RUNES(HARD, NO_PREDICATE, "Craft some nature runes."), //done
	COOK_A_KARAMBWAN(HARD, "Cook a karambwan thoroughly."), //done
	KILL_A_DEATHWING(HARD, true, "Kill a deathwing in the dungeon under the Kharazi Jungle."),
	USE_THE_CROSSBOW_SHORTCUT(HARD, true, "Use the crossbow short cut south of the volcano."),
	COLLECT_PALM_LEAVES(HARD, true, 5, "Collect 5 palm leaves."),
	SLAYER_TASK_BY_DURADEL(HARD, "Be assigned a Slayer task by Duradel north of Shilo", "Village."), //done
	KILL_A_METAL_DRAGON(HARD, BRIMHAVEN_DUNGEON::inArea, "Kill a metal dragon in Brimhaven Dungeon."), //done
	CRAFT_56_NATURE_RUNES(ELITE, NO_PREDICATE, "Craft 56 Nature runes at once."), //done
	EQUIP_FIRE_CAPE(ELITE, TZHAAR_CITY::inArea, "Equip a Fire Cape or Infernal Cape in the Tzhaar city."), //done
	CHECK_PALM_TREE_HEALTH(ELITE, "Check the health of a palm tree in Brimhaven."), //done
	CREATE_AN_ANTIVENOM_POTION(ELITE, HORSESHOE_MINE::inArea, "Create an antivenom potion whilst standing in the horse", "shoe mine."), //done
	CHECK_CALQUAT_HEALTH(ELITE, "Check the health of your Calquat tree patch.");
	//done
	private final DiaryComplexity type;
	private final int objectiveLength;
	private final String[] task;
	private final Predicate<Player> predicate;
	private final boolean autoCompleted;

	KaramjaDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final String... task) {
		this(type, autoCompleted, objectiveLength, KARAMJA_TERRITORY::inArea, task);
	}

	KaramjaDiary(final DiaryComplexity type, final String... task) {
		this(type, false, 1, KARAMJA_TERRITORY::inArea, task);
	}

	KaramjaDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
		this(type, autoCompleted, 1, KARAMJA_TERRITORY::inArea, task);
	}

	KaramjaDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
		this(type, false, 1, predicate, task);
	}

	KaramjaDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
		this.type = type;
		this.autoCompleted = autoCompleted;
		this.objectiveLength = objectiveLength;
		this.predicate = predicate;
		this.task = task;
	}

	public static final KaramjaDiary[] VALUES = values();
	public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

	static {
		for (final DiaryComplexity type : DiaryComplexity.VALUES) {
			MAP.put(type, new ArrayList<>(15));
		}
		for (final KaramjaDiary value : VALUES) {
			if (value.autoCompleted) continue;
			MAP.get(value.type).add(value);
		}
	}

	@Override
	public int taskMaster() {
		return 7651;
	}

	@Override
	public DiaryArea area() {
		return DiaryArea.KARAMJA;
	}

	@Override
	public String title() {
		return "Karamja Area";
	}

	@Override
	public boolean flagging() {
		return false;
	}

	@Override
	public int diaryStarted() {
		return 3576;
	}

	@Override
	public boolean autoCompleted() {
		return autoCompleted;
	}

	@Override
	public int[][] diaryCompleted() {
		return new int[][] {new int[] {3578, 2}, new int[] {3599, 2}, new int[] {3611, 2}, new int[] {4566, 1}};
	}

	@Override
	public DiaryChunk[] chunks() {
		return new DiaryChunk[] {new DiaryChunk(2423, 3577), new DiaryChunk(6288, 3598), new DiaryChunk(6289, 3610), new DiaryChunk(6290, 4567)};
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
