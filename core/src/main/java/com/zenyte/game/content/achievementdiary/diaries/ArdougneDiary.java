package com.zenyte.game.content.achievementdiary.diaries;

import com.zenyte.game.content.achievementdiary.*;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.zenyte.game.content.achievementdiary.AreaPredicate.*;

/**
 * @author Kris | 20. sept 2018 : 18:03:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public enum ArdougneDiary implements Diary {
	TELEPORT_RUNE_ESSENCE_MINE(EASY, "Have Wizard Cromperty teleport you to the Rune Essence", "mine."), //done
	STEAL_CAKE(EASY, "Steal a cake from the Ardougne market stalls."), //done
	SELL_SILK(EASY, true, "Sell Silk to Silk trader in Ardougne for 60 coins each."), USE_EAST_ARDOUGNE_ALTAR(EASY, EAST_ARDOUGNE_CHURCH::inArea, "Use the altar in East Ardougne's church."), //done
	GO_FISHING_ON_FISHING_TRAWLER(EASY, true, "Go out fishing on the Fishing Trawler."), ENTER_COMBAT_TRAINING_CAMP(EASY, COMBAT_TRAINING_CAMP::inArea, "Enter the Combat Training Camp north of W. Ardougne."), //done
	IDENTIFY_RUSTY_SWORD(EASY, true, "Have Tindel Marchant identify a Rusted Sword for you."), USE_ARDOUGNE_LEVER(EASY, "Use the Ardougne Lever to teleport to the Wilderness."), //done
	VIEW_ALECKS_HUNTER_EMPORIUM(EASY, "View Aleck's Hunter Emporium in Yanille."), //done
	CHECK_INSURED_PETS(EASY, "Check what pets you have Insured with Probita in", "Ardougne."), ENTER_UNICORN_PEN(MEDIUM, "Enter the Unicorn pen in Ardougne zoo using Fairy rings."), //done
	GRAPPLE_YANILLE_SOUTH_WALL(MEDIUM, true, "Grapple over Yanille's south wall."), HARVEST_STRAWBERRIES(MEDIUM, "Harvest some strawberries from the Ardougne farming", "patch."),
	CAST_ARDOUGNE_TELEPORT_SPELL(MEDIUM, true, "Cast the Ardougne Teleport spell."), //done
	TRAVEL_CASTLE_WARS_AIR_BALLOON(MEDIUM, true, "Travel to Castlewars by Hot Air Balloon."), CLAIM_BUCKETS_OF_SAND(MEDIUM, true, "Claim buckets of sand from Bert in Yanille."), 
	//CATCH_FISH_ON_FISHING_PLATFORM(MEDIUM, p -> p.inArea("Fishing platform"), "Catch any fish on the Fishing Platform."),//done Wrong fishing platform. 2782 3278 real one.
	PICKPOCKET_MASTER_FARMER(MEDIUM, "Pickpocket the master farmer north of Ardougne."), //done
	COLLECT_NIGHTSHADE(MEDIUM, true, "Collect some Nightshade from the Skavid Caves."), KILL_A_SWORDCHICK(MEDIUM, true, "Kill a swordchick in the Tower of Life."), EQUIP_UPGRADED_IBAN_STAFF(MEDIUM, true, "Equip Iban's upgraded staff or upgrade an Iban staff."), VISIT_ISLAND_EAST_NECROMANCER_TOWER(MEDIUM, true, "Visit the Island East of the Necromancer's tower."), RECHARGE_JEWELLERY_AT_LEGENDS_GUILD(HARD, true, "Recharge some Jewellery at the Totem in the Legends", "Guild."), ENTER_THE_MAGIC_GUILD(HARD, "Enter the Magic Guild."), //done
	STEAL_FROM_KING_LATHAS_CHEST(HARD, true, "Attempt to steal from King Lathas' chest."), GET_PUT_TO_MONKEY_CAGE(HARD, true, "Have a zookeeper put you in Ardougne Zoo's monkey", "cage."),
	TELEPORT_WATCHTOWER(HARD, true, "Teleport to the Watchtower."), //done
	CATCH_RED_SALAMANDER(HARD, NO_PREDICATE, "Catch a Red Salamander."), //done
	CHECK_PALM_TREE_HEALTH(HARD, NO_PREDICATE, "Check the health of a Palm tree near tree gnome village."), //done
	PICK_POISON_IVY_BERRIES(HARD, "Pick some Poison Ivy berries from the patch south of", "Ardougne."), //done
	SMITH_MITHRIL_PLATEBODY(HARD, "Smith a Mithril platebody near Ardougne."), //done
	ENTER_YOUR_POH(HARD, true, "Enter your POH from Yanille."), //done
	SMITH_DRAGON_SQ_SHIELD(HARD, WEST_ARDOUGNE::inArea, "Smith a Dragon sq shield in West Ardougne."), //done
	CRAFT_DEATH_RUNES(HARD, NO_PREDICATE, "Craft some Death runes."), //done
	CATCH_AND_COOK_MANTA_RAY(ELITE, true, "Catch a Manta ray in the Fishing Trawler and cook it in Port", "Khazard."), PICKLOCK_YANILLE_AGILITY_DUNGEON_DOOR(ELITE, true, "Attempt to picklock the door to the basement of Yanille", "Agility dungeon."), PICKPOCKET_A_HERO(ELITE, "Pickpocket a Hero."), //done
	MAKE_RUNE_CROSSBOW(ELITE, false, 1 | 2 | 4 | 8 | 22, p -> AreaPredicate.inAreas(p, WITCHHAVEN, YANILLE), "Make a rune crossbow yourself from scratch within", "Witchaven or Yanille.") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	//done
	EQUIP_IMBUED_SALVE_AMULET(ELITE, true, "Imbue a salve amulet at Nightmare Zone or equip an", "imbued salve amulet."), PICK_TORSTOL(ELITE, NO_PREDICATE, "Pick some Torstol from the patch north of Ardougne."), //done
	COMPLETE_ARDOUGNE_ROOFTOP_LAP(ELITE, "Complete a lap of Ardougne's rooftop agility course."), CAST_ICE_BARRAGE_ON_PLAYER_IN_CW(ELITE, true/*player -> CastleWars.isUserPlaying(player)*/, "Cast Ice Barrage on another player within Castle wars.");
	private final DiaryComplexity type;
	private final int objectiveLength;
	private final String[] task;
	private final Predicate<Player> predicate;
	private final boolean autoCompleted;

	ArdougneDiary(final DiaryComplexity type, final String... task) {
		this(type, false, 1, ARDOUGNE_TERRITORY::inArea, task);
	}

	ArdougneDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
		this(type, autoCompleted, 1, ARDOUGNE_TERRITORY::inArea, task);
	}

	ArdougneDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
		this(type, false, 1, predicate, task);
	}

	ArdougneDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
		this.type = type;
		this.autoCompleted = autoCompleted;
		this.objectiveLength = objectiveLength;
		this.predicate = predicate;
		this.task = task;
	}

	public static final ArdougneDiary[] VALUES = values();
	public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

	static {
		for (final DiaryComplexity type : DiaryComplexity.VALUES) {
			MAP.put(type, new ArrayList<>(15));
		}
		for (final ArdougneDiary value : VALUES) {
			if (value.autoCompleted) continue;
			MAP.get(value.type).add(value);
		}
	}

	@Override
	public int taskMaster() {
		return 5519;
	}

	@Override
	public DiaryArea area() {
		return DiaryArea.ARDOUGNE;
	}

	@Override
	public String title() {
		return "Ardougne Area";
	}

	@Override
	public boolean flagging() {
		return false;
	}

	@Override
	public int diaryStarted() {
		return 4448;
	}

	@Override
	public boolean autoCompleted() {
		return autoCompleted;
	}

	@Override
	public int[][] diaryCompleted() {
		return flow(4458);
	}

	@Override
	public DiaryChunk[] chunks() {
		return new DiaryChunk[] {new DiaryChunk(6291, 4499), new DiaryChunk(6292, 4500), new DiaryChunk(6293, 4501), new DiaryChunk(6294, 4502)};
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
