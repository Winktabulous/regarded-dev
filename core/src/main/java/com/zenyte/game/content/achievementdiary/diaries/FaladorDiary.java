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

/**
 * @author Kris | 23. sept 2018 : 19:02:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>
 */
public enum FaladorDiary implements Diary {
	FIND_OUT_FAMILY_CREST(EASY, true, "Find out what your family crest is from Sir Renitee."), CLIMB_OVER_FALADOR_WALL(EASY, NO_PREDICATE, "Climb over the western Falador wall."),  // done
	BROWSE_SARAHS_FARM_SHOP(EASY, p -> p.inArea("Falador Farm House"), "Browse Sarah's farm shop."),  // done
	GET_A_HAIRCUT(EASY, "Get a Haircut from the Falador Hairdresser."),  // done
	FILL_A_BUCKET(EASY, "Fill a bucket from the pump north of Falador west bank."), //done
	KILL_A_DUCK(EASY, p -> p.inArea("Falador Park"), "Kill a duck in Falador park."),  // done
	MAKE_MIND_TIARA(EASY, NO_PREDICATE, "Make a mind tiara."),  // done
	TAKE_BOAT_TO_ENTRANA(EASY, NO_PREDICATE, "Take the boat to Entrana."),  // done
	REPAIR_BROKEN_STRUT(EASY, NO_PREDICATE, "Repair a broken strut in the Motherlode Mine."), CLAIM_A_SECURITY_BOOK(EASY, true, "Claim a security book from the Security guard at Port", "Sarim jail."),
	SMITH_BLURITE_LIMBS(EASY, NO_PREDICATE, "Smith some Blurite Limbs on Doric's Anvil."),  // done
	LIGHT_BULLSEYE_LANTERN(MEDIUM, p -> p.inArea("Rimmington Chemist House"), "Light a Bullseye lantern at the " +
            "Chemist's in Rimmington."),  // done
	TELEGRAB_WINE_OF_ZAMORAK(MEDIUM, p -> p.inArea("Chaos Temple"), "Telegrab some Wine of Zamorak at the Chaos Temple by", "the Wilderness."),  // done
	UNLOCK_CRYSTAL_CHEST(MEDIUM, NO_PREDICATE, "Unlock the Crystal chest in Taverley"), PLACE_A_SCARECROW(MEDIUM, p -> p.inArea("Falador Farming"), "Place a Scarecrow in the Falador farming patch."),  // done
	KILL_A_MOGRE(MEDIUM, true, "Kill a Mogre at Mudskipper Point."), VISIT_RAT_PITS(MEDIUM, true, "Visit the Port Sarim Rat Pits."), GRAPPLE_FALADOR_WALL(MEDIUM, true, "Grapple up and then jump off the north Falador wall."), PICKPOCKET_FALADOR_GUARD(MEDIUM, "Pickpocket a Falador guard."),  // done
	PRAY_ALTAR_OF_GUTHIX(MEDIUM, NO_PREDICATE, "Pray at the Altar of Guthix in Taverley whilst wearing full", "Initiate."),  // done
	MINE_GOLD_ORE(MEDIUM, p -> p.inArea("Crafting guild"), "Mine some Gold ore at the Crafting Guild."),  // done
	SQUEEZE_THROUGH_CREVICE(MEDIUM, NO_PREDICATE, "Squeeze through the crevice in the Dwarven mines."), CHOP_BURN_WILLOW_LOGS(MEDIUM, false, 1 | 2, p -> p.inArea("Taverley"), "Chop and burn some Willow logs in Taverley") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	CRAFT_FRUIT_BASKET(MEDIUM, true, /* p -> p.inArea("Falador Farm House"),*/ "Craft a fruit basket on the Falador Farm loom."), //done
	TELEPORT_FALADOR(MEDIUM, true, "Teleport to Falador."), //done
	CRAFT_MIND_RUNES(HARD, NO_PREDICATE, "Craft 140 Mind runes simultaneously."), //done
	CHANGE_FAMILY_CREST(HARD, true, "Change your family crest to the Saradomin symbol."), KILL_GIANT_MOLE(HARD, NO_PREDICATE, "Kill the Giant Mole beneath Falador park."), KILL_SKELETAL_WYVERN(HARD, p -> p.inArea("Asgarnian Ice Dungeon"), "Kill a Skeletal Wyvern in the Asgarnia Ice Dungeon."), //done
	COMPLETE_ROOFTOP_COURSE(HARD, "Complete a lap of the Falador rooftop agility course."), //done
	ENTER_MINING_GUILD(HARD, NO_PREDICATE, "Enter the mining guild wearing full prospector."), KILL_BLUE_DRAGON(HARD, true, "Kill the Blue Dragon under the Heroes' Guild."), CRACK_WALL_SAFE(HARD, NO_PREDICATE, "Crack a wall safe within Rogues Den."), RECHARGE_PRAYER(HARD, p -> p.inArea("Port Sarim Church"), "Recharge your prayer in the Port Sarim church while", "wearing full Proselyte."), //done
	ENTER_WARRIORS_GUILD(HARD, NO_PREDICATE, "Enter the Warriors' Guild."), //done
	EQUIP_DWARVEN_HELMET(HARD, true, "Equip a dwarven helmet within the dwarven mines."), CRAFT_AIR_RUNES(ELITE, NO_PREDICATE, "Craft 252 Air Runes simultaneously."), //done
	PURCHASE_WHITE_2H(ELITE, true, "Purchase a White 2h Sword from Sir Vyvin."), FIND_THREE_MAGIC_ROOTS(ELITE, true, 3, NO_PREDICATE, "Find at least 3 magic roots at once when digging up your", "magic tree in Falador."), PERFORM_SKILLCAPE_EMOTE(ELITE, p -> p.inArea("Falador Castle Upper Floor"), "Perform a skillcape or quest cape emote at the top of", "Falador Castle."), //done
	JUMP_OVER_STRANGE_FLOOR(ELITE, NO_PREDICATE, "Jump over the strange floor in Taverley dungeon."), MIX_SARADOMIN_BREW(ELITE, p -> p.inArea("Falador East Bank"), "Mix a Saradomin brew in Falador east bank.");
	//done
	private final DiaryComplexity type;
	private final int objectiveLength;
	private final String[] task;
	private final Predicate<Player> predicate;
	private final boolean autoCompleted;

	FaladorDiary(final DiaryComplexity type, final String... task) {
		this(type, false, 1, p -> p.inArea("Falador"), task);
	}

	FaladorDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
		this(type, autoCompleted, 1, p -> p.inArea("Falador"), task);
	}

	FaladorDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
		this(type, false, 1, predicate, task);
	}

	FaladorDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
		this.type = type;
		this.autoCompleted = autoCompleted;
		this.objectiveLength = objectiveLength;
		this.predicate = predicate;
		this.task = task;
	}

	public static final FaladorDiary[] VALUES = values();
	public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

	static {
		for (final DiaryComplexity type : DiaryComplexity.VALUES) {
			MAP.put(type, new ArrayList<>(15));
		}
		for (final FaladorDiary value : VALUES) {
			if (value.autoCompleted) continue;
			MAP.get(value.type).add(value);
		}
	}

	@Override
	public int taskMaster() {
		return 5524;
	}

	@Override
	public boolean flagging() {
		return false;
	}

	@Override
	public DiaryArea area() {
		return DiaryArea.FALADOR;
	}

	@Override
	public String title() {
		return "Falador Area";
	}

	@Override
	public int diaryStarted() {
		return 4449;
	}

	@Override
	public boolean autoCompleted() {
		return autoCompleted;
	}

	@Override
	public int[][] diaryCompleted() {
		return flow(4462);
	}

	@Override
	public DiaryChunk[] chunks() {
		return new DiaryChunk[] {new DiaryChunk(6299, 4503), new DiaryChunk(6300, 4504), new DiaryChunk(6301, 4505), new DiaryChunk(6302, 4506)};
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
