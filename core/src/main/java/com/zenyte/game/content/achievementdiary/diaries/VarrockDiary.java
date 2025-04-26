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
 * @author Kris | 28. sept 2018 : 20:21:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>
 */
public enum VarrockDiary implements Diary {
	BROWSE_THESSELIA_STORE(EASY, "Browse Thessalia's store."),  // done
	TELEPORT_TO_ESSENCE_MINE(EASY, "Have Aubury teleport you to the Essence mine."),  // done
	MINE_IRON(EASY, VARROCK_MINE::inArea, "Mine some Iron in the south east mining patch near", "Varrock."), //done
	MAKE_A_PLANK(EASY, true, "Make a normal plank at the Sawmill."), //done
	ENTER_SECOND_LEVEL_SOS(EASY, NO_PREDICATE, "Enter the second level of the Stronghold of Security."), //done
	JUMP_OVER_VARROCK_FENCE(EASY, NO_PREDICATE, "Jump over the fence south of Varrock."), //done
	CHOP_DOWN_DYING_TREE(EASY, p -> p.inArea("Lumberyard"), "Chop down a dying tree in the Lumber Yard."), BUY_NEWSPAPER(EASY, true, "Buy a newspaper."), GIVE_DOG_BONE(EASY, true, "Give a dog a bone!"),
	SPIN_AND_FIRE_A_BOWL(EASY, true, 1 | 2, p -> p.inArea("Barbarian Village"), "Spin a bowl on the pottery wheel and fire it in the oven in", "Barb Village.") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	//done
	OBTAIN_50_KUDOS(EASY, true, "Speak to Haig Halen after obtaining at least 50 Kudos."), CRAFT_EARTH_RUNES(EASY, NO_PREDICATE, "Craft some Earth runes."), //done
	CATCH_TROUT(EASY, true, "Catch some trout in the River Lum at Barbarian Village."), //done
	STEAL_FROM_TEA_STALL(EASY, "Steal from the Tea stall in Varrock."), //done
	GET_STRENGTH_POTION_FROM_APOTHECARY(MEDIUM, true, "Have the Apothecary in Varrock make you a strength", "potion."), ENTER_CHAMPIONS_GUILD(MEDIUM, NO_PREDICATE, "Enter the Champions' Guild."), //done
	SELECT_COLOUR_FOR_KITTEN(MEDIUM, true, "Select a colour for your kitten."), USE_SPIRIT_TREE(MEDIUM, NO_PREDICATE, "Use the spirit tree north of Varrock."), //done
	PERFORM_SOS_EMOTES(MEDIUM, 1 | 2 | 4 | 8, NO_PREDICATE, "Perform the 4 emotes from the Stronghold of Security.") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	//done
	ENTER_TOLNA_DUNGEON(MEDIUM, true, "Enter the Tolna dungeon after completing A Soul's Bane."), TELEPORT_TO_DIGSITE(MEDIUM, NO_PREDICATE, "Teleport to the digsite using a Digsite pendant."), //done
	CAST_VARROCK_TELEPORT(MEDIUM, true, "Cast the teleport to Varrock spell."), //done
	SLAYER_TASK_FROM_VANNAKA(MEDIUM, NO_PREDICATE, "Get a Slayer task from Vannaka."), //done
	MAKE_MAHOGANY_PLANKS(MEDIUM, true, "Make 20 Mahogany Planks in one go."), //done
	PICK_WHITE_TREE_FRUIT(MEDIUM, true, "Pick a White tree fruit."), USE_BALLOON_TRAVEL(MEDIUM, true, "Use the balloon to travel from Varrock."), COMPLETE_AGILITY_COURSE_LAP(MEDIUM, "Complete a lap of the Varrock Agility course."), //done
	PURCHASE_AND_EQUIP_SPOTTIER_CAPE(HARD, true, "Trade furs with the Fancy Dress Seller for a spottier cape", "and equip it."), OBTAIN_153_KUDOS(HARD, true, "Speak to Orlando Smith when you have achieved 153", "Kudos."),
	MAKE_WAKA_CANOE(HARD, true, "Make a Waka Canoe near Edgeville."), //done
	TELEPORT_TO_PADDEWWA(HARD, true, "Teleport to Paddewwa."), //done
	TELEPORT_TO_BARBARIAN_VILLAGE(HARD, NO_PREDICATE, "Teleport to Barbarian Village with a skull sceptre."), //done
	CHOP_AND_BURN_YEW_LOGS(HARD, 1 | 2, "Chop some yew logs in Varrock and burn them at the top of", "the Varrock church.") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	//done
	DECORATE_HOUSE_WITH_FANCY_STONE(HARD, true, "Have the Varrock estate agent decorate your house with", "Fancy Stone."), //done
	COLLECT_2_YEW_ROOTS(HARD, true, "Collect at least 2 yew roots from the Tree patch in Varrock", "Palace."), PRAY_AT_VARROCK_ALTAR(HARD, VARROCK_PALACE_ALTAR_ROOM::inArea, "Pray at the altar in Varrock palace with Smite active."), //done
	SQUEEZE_THROUGH_OBSTACLE_PIPE(HARD, p -> p.inArea("Edgeville Dungeon") || p.inArea("Varrock Sewers"), "Squeeze through the obstacle pipe in Edgeville dungeon."), CREATE_SUPERCOMBAT_POTION(ELITE, VARROCK_WEST_BANK::inArea, "Create a super combat potion in Varrock west bank."), //done
	MAKE_20_MAHOGANY_PLANKS(ELITE, false, 20, LUMBERYARD::inArea, "Use Lunar magic to make 20 mahogany planks at the", "Lumberyard."), //done
	BAKE_A_SUMMER_PIE(ELITE, true, "Bake a summer pie in the Cooking Guild."), //done
	SMITH_AND_FLETCH_10_RUNE_DARTS(ELITE, 1 | 2, "Smith and fletch ten rune darts within Varrock.") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	//done
	CRAFT_100_EARTH_RUNES(ELITE, NO_PREDICATE, "Craft 100 or more earth runes simultaneously.");
	//done
	private final DiaryComplexity type;
	private final int objectiveLength;
	private final String[] task;
	private final Predicate<Player> predicate;
	private final boolean autoCompleted;

	VarrockDiary(final DiaryComplexity type, final int objectiveLength, final String... task) {
		this(type, false, objectiveLength, p -> p.inArea("Varrock"), task);
	}

	VarrockDiary(final DiaryComplexity type, final String... task) {
		this(type, false, 1, p -> p.inArea("Varrock"), task);
	}

	VarrockDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
		this(type, autoCompleted, 1, p -> p.inArea("Varrock"), task);
	}

	VarrockDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
		this(type, false, 1, predicate, task);
	}

	VarrockDiary(final DiaryComplexity type, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
		this(type, false, objectiveLength, predicate, task);
	}

	VarrockDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
		this.type = type;
		this.autoCompleted = autoCompleted;
		this.objectiveLength = objectiveLength;
		this.predicate = predicate;
		this.task = task;
	}

	public static final VarrockDiary[] VALUES = values();
	public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

	static {
		for (final DiaryComplexity type : DiaryComplexity.VALUES) {
			MAP.put(type, new ArrayList<>(15));
		}
		for (final VarrockDiary value : VALUES) {
			if (value.autoCompleted) continue;
			MAP.get(value.type).add(value);
		}
	}

	@Override
	public int taskMaster() {
		return 5525;
	}

	@Override
	public boolean flagging() {
		return false;
	}

	@Override
	public DiaryArea area() {
		return DiaryArea.VARROCK;
	}

	@Override
	public String title() {
		return "Varrock";
	}

	@Override
	public int diaryStarted() {
		return 4455;
	}

	@Override
	public boolean autoCompleted() {
		return autoCompleted;
	}

	@Override
	public int[][] diaryCompleted() {
		return flow(4479);
	}

	@Override
	public DiaryChunk[] chunks() {
		return new DiaryChunk[] {new DiaryChunk(6319, 4519), new DiaryChunk(6320, 4520), new DiaryChunk(6321, 4521), new DiaryChunk(6322, 4522)};
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
