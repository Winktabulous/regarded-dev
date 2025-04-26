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
 * @author Kris | 03/11/2018 22:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum WesternProvincesDiary implements Diary {
    CATCH_COPPER_LONGTAIL(EASY, NO_PREDICATE, "Catch a Copper Longtail."), //done
    COMPLETE_PEST_CONTROL_NOVICE(EASY, NO_PREDICATE, "Complete a novice game of Pest Control."), //done
    MINE_IRON_NEAR_PISCATORIS(EASY, PISCATORIS_MINING_AREA::inArea, "Mine some Iron Ore near Piscatoris."), //done
    COMPLETE_GNOME_COURSE_LAP(EASY, NO_PREDICATE, "Complete a lap of the Gnome agility course."), //done
    SCORE_GNOMEBALL_MATCH_GOAL(EASY, true, "Score a goal in a Gnomeball match."), CLAIM_CHOMPY_BIRD_HAT(EASY, NO_PREDICATE, "Claim any Chompy bird hat from Rantz."),  //done
    TELEPORT_TO_PEST_CONTROL(EASY, true, "Teleport to Pest Control using the Minigame teleports."), //done
    COLLECT_SWAMP_TOAD(EASY, p -> p.inArea("Tree Gnome Stronghold"), "Collect a swamp toad at the Gnome Stronghold."), //done
    TELEPORT_ESSENCE_MINE(EASY, NO_PREDICATE, "Have Brimstail teleport you to the Essence mine."), //done
    FLETCH_OAK_SHORTBOW(EASY, p -> p.inArea("Tree Gnome Stronghold"), "Fletch an Oak shortbow from the Gnome Stronghold."), //done
    KILL_TERRORBIRD(EASY, p -> p.inArea("Tree Gnome Stronghold"), "Kill a Terrorbird in the Terrorbird enclosure."), //done
    TAKE_GRAND_TREE_AGILITY_SHORTCUT(MEDIUM, NO_PREDICATE, "Take the agility shortcut from the Grand Tree to Otto's", "Grotto."), TRAVEL_TO_GNOME_STRONGHOLD(MEDIUM, NO_PREDICATE, "Travel to the Gnome Stronghold by Spirit Tree."), //done
    TRAP_A_SPINED_LARUPIA(MEDIUM, NO_PREDICATE, "Trap a Spined Larupia."), //done
    FISH_BASS(MEDIUM, NO_PREDICATE, "Fish some Bass on Ape Atoll."), //done
    CHOP_AND_BURN_TEAK_LOGS(MEDIUM, false, 1 | 2, p -> p.inArea("Ape Atoll"), "Chop and burn some teak logs on Ape Atoll.") {
        @Override
        public boolean flagging() {
            return true;
        }
    },
    //done
    COMPLETE_INTERMEDIATE_PEST_CONTROL_GAME(MEDIUM, NO_PREDICATE, "Complete an intermediate game of Pest Control."), //done
    TRAVEL_TO_FELDIP_HILLS(MEDIUM, NO_PREDICATE, "Travel to the Feldip Hills by Gnome Glider."), CLAIM_CHOMPY_BIRD_HAT_125_KILLS(MEDIUM, NO_PREDICATE, "Claim a Chompy bird hat from Rantz after registering at", "least 70 kills."),  //done
    TRAVEL_TO_FELDIP_HILLS_BY_EAGLE(MEDIUM, true, "Travel from Eagles' Peak to the Feldip Hills by Eagle."), MAKE_A_CHOCOLATE_BOMB(MEDIUM, true, "Make a Chocolate Bomb at the Grand Tree."), COMPLETE_GNOME_RESTAURANT_DELIVERY(MEDIUM, true, "Complete a delivery for the Gnome Restaurant."), CREATE_CRYSTAL_SAW(MEDIUM, true, "Turn your small crystal seed into a Crystal saw."), MINE_GOLD_ORE(MEDIUM, true, "Mine some Gold ore underneath the Grand Tree."), KILL_AN_ELF(HARD, true, "Kill an Elf with a Crystal bow."), CATCH_AND_COOK_MONKFISH(HARD, true, "Catch and cook a Monkfish in Piscatoris."), COMPLETE_VETERAN_PEST_CONTROL_GAME(HARD, NO_PREDICATE, "Complete a Veteran game of Pest Control."), //done
    CATCH_A_DASHING_KEBBIT(HARD, true, "Catch a Dashing Kebbit."),
    COMPLETE_APE_ATOLL_COURSE_LAP(HARD, true, "Complete a lap of the Ape Atoll agility course."),
    CHOP_AND_BURN_MAHOGANY_LOGS(HARD, false, 1 | 2, p -> p.inArea("Ape Atoll"), "Chop and burn some Mahogany logs on Ape Atoll.") {
        @Override
        public boolean flagging() {
            return true;
        }
    },
    //done
    MINE_ADAMANTITE_IN_TIRANNWN(HARD, true, "Mine some Adamantite ore in Tirannwn."),
    CHECK_PALM_TREE_HEALTH(HARD, "Check the health of your Palm tree in Lletya."), //done
    CLAIM_CHOMPY_BIRD_HAT_300_KILLS(HARD, NO_PREDICATE, "Claim a Chompy bird hat from Rantz after registering at", "least 125 kills."),  //done
    BUILD_ISAFDAR_PAINTING(HARD, true, "Build an Isafdar painting in your POH Quest hall."), KILL_ZULRAH(HARD, NO_PREDICATE, "Kill Zulrah."), //done
    TELEPORT_TO_APE_ATOLL(HARD, true, "Teleport to Ape Atoll."), //done
    PICKPOCKET_A_GNOME(HARD, NO_PREDICATE, "Pickpocket a Gnome."), //done
    FLETCH_MAGIC_LONGBOW(ELITE, ELVEN_LANDS::inArea, "Fletch a Magic Longbow in the Elven lands."), //done
    KILL_THERMONUCLEAR_SMOKE_DEVIL(ELITE, NO_PREDICATE, "Kill the Thermonuclear Smoke devil <col=800000>(Does not require task)<col=000000>."), //done
    PROTECT_MAGIC_TREE(ELITE, p -> p.inArea("Tree Gnome Stronghold"), "Have Prissy Scilla protect your Magic tree."), USE_ELVEN_OVERPASS_SHORTCUT(ELITE, true, "Use the Elven overpass advanced cliffside shortcut."), EQUIP_COMPLETE_VOID_SET(ELITE, true, "Equip any complete void set."), CLAIM_CHOMPY_BIRD_HAT_1000_KILLS(ELITE, NO_PREDICATE, "Claim a Chompy bird hat from Rantz after registering at", "least 300 kills."),  //done
    PICKPOCKET_ELF(ELITE, NO_PREDICATE, "Pickpocket an Elf.");
    //done
    private final DiaryComplexity type;
    private final int objectiveLength;
    private final String[] task;
    private final Predicate<Player> predicate;
    private final boolean autoCompleted;

    WesternProvincesDiary(final DiaryComplexity type, final String... task) {
        this(type, false, 1, WESTERN_PROVINCES_TERRITORY::inArea, task);
    }

    WesternProvincesDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
        this(type, autoCompleted, 1, WESTERN_PROVINCES_TERRITORY::inArea, task);
    }

    WesternProvincesDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
        this(type, false, 1, predicate, task);
    }

    WesternProvincesDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
        this.type = type;
        this.autoCompleted = autoCompleted;
        this.objectiveLength = objectiveLength;
        this.predicate = predicate;
        this.task = task;
    }

    public static final WesternProvincesDiary[] VALUES = values();
    public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

    static {
        for (final DiaryComplexity type : DiaryComplexity.VALUES) {
            MAP.put(type, new ArrayList<>(15));
        }
        for (final WesternProvincesDiary value : VALUES) {
            if (value.autoCompleted) continue;
            MAP.get(value.type).add(value);
        }
    }

    @Override
    public int taskMaster() {
        return 5518;
    }

    @Override
    public DiaryArea area() {
        return DiaryArea.WESTERN_PROVINCES;
    }

    @Override
    public String title() {
        return "Western Area";
    }

    @Override
    public boolean flagging() {
        return false;
    }

    @Override
    public int diaryStarted() {
        return 4456;
    }

    @Override
    public boolean autoCompleted() {
        return autoCompleted;
    }

    @Override
    public int[][] diaryCompleted() {
        return flow(4471);
    }

    @Override
    public DiaryChunk[] chunks() {
        return new DiaryChunk[] {new DiaryChunk(6327, 4511), new DiaryChunk(6328, 4512), new DiaryChunk(6329, 4513), new DiaryChunk(6330, 4514)};
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
