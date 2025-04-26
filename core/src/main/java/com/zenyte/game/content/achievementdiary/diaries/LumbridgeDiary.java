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
 * @author Kris | 2. okt 2018 : 13:44:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>
 */
public enum LumbridgeDiary implements Diary {
	COMPLETE_DRAYNOR_VILLAGE_COURSE(EASY, NO_PREDICATE, "Complete a lap of the Draynor Village agility course."),  // done
	SLAY_A_CAVE_BUG(EASY, p -> p.inArea("Lumbridge Swamp Caves"), "Slay a Cave bug beneath Lumbridge Swamp."), //done
	TELEPORT_TO_ESSENCE_MINE(EASY, NO_PREDICATE, "Have Sedridor teleport you to the Essence Mine."),  // done
	CRAFT_WATER_RUNES(EASY, NO_PREDICATE, "Craft some water runes."),  // done
	LEARN_YOUR_AGE(EASY, "Learn your age from Hans in Lumbridge."),  // done
	PICKPOCKET_A_MAN(EASY, "Pickpocket a man or woman in Lumbridge."), //done
	CHOP_AND_BURN_LOGS(EASY, 1 | 2, "Chop and burn some oak logs in Lumbridge.") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	//done
	KILL_A_ZOMBIE(EASY, DRAYNOR_SEWERS::inArea, "Kill a Zombie in Draynor Sewers."), //done
	CATCH_ANCHOVIES(EASY, AL_KHARID_FISHING_AREA::inArea, "Catch some Anchovies in Al Kharid."), //done
	BAKE_BREAD(EASY, LUMBRIDGE_KITCHEN::inArea, "Bake some Bread on the Lumbridge kitchen range."), //done
	MINE_IRON(EASY, AL_KHARID_MINE::inArea, "Mine some Iron ore at the Al Kharid mine."), //done
	ENTER_HAM_HIDEOUT(EASY, NO_PREDICATE, "Enter the H.A.M. Hideout."), //done TODO entrance with picklock
	COMPLETE_ALKHARID_COURSE(MEDIUM, NO_PREDICATE, "Complete a lap of the Al Kharid agility course."), //done
	GRAPPLE_RIVER_LUM(MEDIUM, true, "Grapple across the River Lum."), PURCHASE_AN_UPGRADED_AVA(MEDIUM, true, "Purchase an upgraded device from Ava."), TRAVEL_TO_WIZARDS_TOWER(MEDIUM, NO_PREDICATE, "Travel to the Wizards' Tower by Fairy ring."), //done
	CAST_LUMBRIDGE_TELEPORT(MEDIUM, true, "Cast the teleport to Lumbridge spell."), //done
	CATCH_SALMON(MEDIUM, "Catch some Salmon in Lumbridge."), //done
	CRAFT_A_COIF(MEDIUM, LUMBRIDGE_COW_PEN::inArea, "Craft a coif in the Lumbridge cow pen."), //done
	CHOP_WILLOWS(MEDIUM, DRAYNOR_VILLAGE::inArea, "Chop some willow logs in Draynor Village."), //done
	PICKPOCKET_MASTER_GARDENER(MEDIUM, NO_PREDICATE, "Pickpocket Martin the Master Gardener."), GET_SLAYER_TASK_FROM_CHAELDAR(MEDIUM, NO_PREDICATE, "Get a slayer task from Chaeldar."), //done
	CATCH_IMPLING(MEDIUM, p -> p.inArea("Puro-Puro"), "Catch an Essence or Eclectic impling in Puro-Puro."),
	CRAFT_LAVA_RUNES(MEDIUM, NO_PREDICATE, "Craft some Lava runes at the fire altar in Al Kharid."), //done
	CAST_BONES_TO_PEACHES(HARD, AL_KHARID_PALACE::inArea, "Cast Bones to Peaches in Al Kharid palace."), //done
	SQUEEZE_PAST_JUTTING_WALL(HARD, NO_PREDICATE, "Squeeze past the jutting wall on your way to the cosmic", "altar."), CRAFT_COSMIC_RUNES(HARD, NO_PREDICATE, "Craft 56 Cosmic runes simultaneously."), //done
	TRAVEL_TO_EDGEVILLE_ON_CANOE(HARD, "Travel from Lumbridge to Edgeville on a Waka Canoe."), //done
	COLLECT_TEARS_OF_GUTHIX(HARD, true, "Collect at least 100 Tears of Guthix in one visit."), TAKE_TRAIN_TO_KELDAGRIM(HARD, true, "Take the train from Dorgesh-Kaan to Keldagrim."), PURCHASE_BARROWS_GLOVES(HARD, NO_PREDICATE, "Purchase some Barrows gloves from the Lumbridge bank", "chest."), PICK_BELLADONNA(HARD, NO_PREDICATE, "Pick some Belladonna from the farming patch at Draynor", "Manor."), //done
	LIGHT_MINING_HELMET(HARD, true, "Light your mining helmet in the Lumbridge castle", "basement."),
	RECHARGE_PRAYER(HARD, true, "Recharge your prayer at Clan Wars with Smite activated."), //done
	CRAFT_AMULET_OF_POWER(HARD, 1 | 2 | 4, "Craft, string and enchant an Amulet of Power in", "Lumbridge.") {
		@Override
		public boolean flagging() {
			return true;
		}
	},
	//done
	STEAL_FROM_DORGESH_KAAN_CHEST(ELITE, true, "Steal from a Dorgesh-Kaan rich chest."), PICKPOCKET_MOVARIO(ELITE, true, "Pickpocket Movario on the Dorgesh-Kaan Agility course."), CHOP_MAGIC_LOGS(ELITE, MAGE_TRAINING_ARENA::inArea, "Chop some magic logs at the Mage Training Arena."), //done
	SMITH_ADAMANT_PLATEBODY(ELITE, DRAYNOR_SEWERS::inArea, "Smith an Adamant platebody down Draynor sewer."), //done
	CRAFT_140_WATER_RUNES(ELITE, NO_PREDICATE, "Craft 140 or more Water runes at once."),  // done
	PERFORM_QUEST_CAPE_EMOTE(ELITE, true, WISE_OLD_MAN_HOUSE::inArea, "Perform the Quest cape emote in the Wise Old " +
            "Man's", "house.");
	//Done but disabled cus cape unobtainable.
	private final DiaryComplexity type;
	private final int objectiveLength;
	private final String[] task;
	private final Predicate<Player> predicate;
	private final boolean autoCompleted;

	LumbridgeDiary(final DiaryComplexity type, final String... task) {
		this(type, false, 1, LUMBRIDGE_TERRITORY::inArea, task);
	}

	LumbridgeDiary(final DiaryComplexity type, final int objectiveLength, final String... task) {
		this(type, false, objectiveLength, LUMBRIDGE_TERRITORY::inArea, task);
	}

	LumbridgeDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
		this(type, autoCompleted, 1, LUMBRIDGE_TERRITORY::inArea, task);
	}

	LumbridgeDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
		this(type, false, 1, predicate, task);
	}

	LumbridgeDiary(final DiaryComplexity type, final boolean autoCompleted, final Predicate<Player> predicate, final String... task) {
		this(type, autoCompleted, 1, predicate, task);
	}

	LumbridgeDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
		this.type = type;
		this.autoCompleted = autoCompleted;
		this.objectiveLength = objectiveLength;
		this.predicate = predicate;
		this.task = task;
	}

	public static final LumbridgeDiary[] VALUES = values();
	public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

	static {
		for (final DiaryComplexity type : DiaryComplexity.VALUES) {
			MAP.put(type, new ArrayList<>(15));
		}
		for (final LumbridgeDiary value : VALUES) {
			if (value.autoCompleted) continue;
			MAP.get(value.type).add(value);
		}
	}

	@Override
	public int taskMaster() {
		return 5523;
	}

	@Override
	public DiaryArea area() {
		return DiaryArea.LUMBRIDGE_AND_DRAYNOR;
	}

	@Override
	public String title() {
		return "Lumbridge & Draynor";
	}

	@Override
	public boolean flagging() {
		return false;
	}

	@Override
	public int diaryStarted() {
		return 4453;
	}

	@Override
	public boolean autoCompleted() {
		return autoCompleted;
	}

	@Override
	public int[][] diaryCompleted() {
		return flow(4495);
	}

	@Override
	public DiaryChunk[] chunks() {
		return new DiaryChunk[] {new DiaryChunk(6311, 4535), new DiaryChunk(6312, 4536), new DiaryChunk(6313, 4537), new DiaryChunk(6314, 4538)};
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
