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

public enum KandarinDiary implements Diary {
    CATCH_A_MACKEREL(EASY, p -> p.inArea("Catherby"), "Catch a Mackerel at Catherby."), //done
    BUY_CANDLE(EASY, p -> p.inArea("Catherby"), "Buy a candle from the Chandler in Catherby."), COLLECT_FLAX(EASY, false, 5, SEERS_VILLAGE_FLAX_FIELD::inArea, "Collect five Flax from the Seers' flax fields."), //done
    PLAY_THE_ORGAN(EASY, true, "Play the Organ in Seers' Church."), PLANT_JUTE_SEEDS(EASY, NO_PREDICATE, "Plant some" +
            " Jute seeds in the patch north of McGrubor's", "Wood."), //done
    HAVE_GALAHAD_MAKE_YOU_TEA(EASY, true, "Have Galahad make you a cup of tea."), DEFEAT_EACH_ELEMENTAL(EASY, true, "Defeat one of each elemental in the workshop."), GET_A_PET_FISH(EASY, true, "Get a pet fish from Harry in Catherby."), BUY_STEW(EASY, true, "Buy a Stew from the Seers' pub."), SPEAK_TO_SHERLOCK(EASY, true, "Speak to Sherlock between the Sorcerer's Tower and Keep Le Faye."), CROSS_COAL_TRUCK_LOG(EASY, "Cross the Coal truck log shortcut."), COMPLETE_BARBARIAN_AGILITY_COURSE_LAP(MEDIUM, false, NO_PREDICATE, "Complete a lap of the Barbarian agility course."), //done
    CREATE_SUPER_ANTIPOISON(MEDIUM, false, 1 | 2, SEERS_VILLAGE_CATHERBY_TERRITORY::inArea, "Create a Super Antipoison potion from scratch in the", "Seers/Catherby Area.") {
        @Override
        public boolean flagging() {
            return true;
        }
    },
    //done
    ENTER_RANGING_GUILD(MEDIUM, false, p -> p.inArea("Ranging guild"), "Enter the Ranging guild."), USE_GRAPPLE_SHORTCUT(MEDIUM, true, "Use the grapple shortcut to get from the water obelisk to", "Catherby shore."), CATCH_AND_COOK_BASS(MEDIUM, false, 1 | 2, p -> p.inArea("Catherby"), "Catch and cook a Bass in Catherby.") {
        @Override
        public boolean flagging() {
            return true;
        }
    },
    //done
    TELEPORT_TO_CAMELOT(MEDIUM, true, "Teleport to Camelot."), //done
    STRING_MAPLE_SHORTBOW(MEDIUM, SEERS_VILLAGE_BANK::inArea, "String a Maple shortbow in Seers' Village bank."), //done
    PICK_LIMPWURT_ROOT(MEDIUM, p -> p.inArea("Catherby"), "Pick some Limpwurt root from the farming patch in", "Catherby."), //done
    CREATE_MIND_HELMET(MEDIUM, true, "Create a Mind helmet."), KILL_A_FIRE_GIANT(MEDIUM, WATERFALL_DUNGEON::inArea, "Kill a Fire Giant inside Baxtorian Waterfall."), //done TODO spawns for waterfall dungeon
    COMPLETE_BARBARIAN_ASSAULT_WAVE(MEDIUM, true, "Complete a wave of Barbarian Assault."), STEAL_FROM_HEMENSTER_CHEST(MEDIUM, true, "Steal from the chest in Hemenster."), TRAVEL_TO_MCGRUBORS_WOODS(MEDIUM, NO_PREDICATE, "Travel to McGrubor's Wood by Fairy Ring."), //done
    MINE_COAL(MEDIUM, COAL_TRUCKS::inArea, "Mine some coal near the coal trucks."), //done TODO shortcut log balance
    CATCH_LEAPING_STURGEON(HARD, NO_PREDICATE, "Catch a Leaping Sturgeon."), //done
    COMPLETE_SEERS_VILLAGE_AGILITY_COURSE_LAP(HARD, NO_PREDICATE, "Complete a lap of the Seers' Village agility course."), //done
    CREATE_YEW_LONGBOW(HARD, false, 1 | 2 | 4, SEERS_VILLAGE_CATHERBY_TERRITORY::inArea, "Create a Yew Longbow from " +
            "scratch around Seers' Village.") {
        @Override
        public boolean flagging() {
            return true;
        }
    },
    //done
    ENTER_SEERS_VILLAGE_COURTHOUSE(HARD, "Enter the Seers' Village courthouse with piety turned on."), //done
    CHARGE_WATER_ORB(HARD, NO_PREDICATE, "Charge a Water Orb."), //done
    BURN_MAPLE_LOGS(HARD, SEERS_VILLAGE::inArea, "Burn some Maple logs with a bow in Seers' Village."), //done
    KILL_A_SHADOW_HOUND(HARD, true, "Kill a Shadow Hound in the Shadow dungeon."), KILL_A_MITHRIL_DRAGON(HARD, "Kill a Mithril Dragon."), //done
    PURCHASE_AND_EQUIP_GRANITE_BODY(HARD, true, "Purchase and equip a granite body from Barbarian", "Assault."), DECORATE_HOUSE_WITH_FANCY_STONE(HARD, true, SEERS_VILLAGE_ESTATE_AGENT::inArea, "Have the Seers' estate agent decorate your house with", "Fancy Stone."), //done
    SMITH_ADAMANT_SPEAR(HARD, NO_PREDICATE, "Smith an Adamant spear at Otto's Grotto."), //done
    READ_BARBARIAN_ASSAULT_BLACKBOARD(ELITE, true, "Read the Blackboard at Barbarian Assault after reaching", "level 5 in every role."), PICK_DWARF_WEED(ELITE, NO_PREDICATE, "Pick some Dwarf weed from the herb patch at Catherby."), //done
    FISH_AND_COOK_SHARKS(ELITE, true, p -> p.inArea("Catherby"), "Fish and Cook 5 Sharks in Catherby using the Cooking", "gauntlets."), MIX_A_STAMINA_MIX(ELITE, true, "Mix a Stamina Mix on top of the Seers' Village bank."), SMITH_A_RUNE_HASTA(ELITE, NO_PREDICATE, "Smith a Rune Hasta at Otto's Grotto."), //done
    CONSTRUCT_A_PYRE_SHIP(ELITE, true, "Construct a Pyre ship from Magic Logs.<col=800000>(Requires Chewed", "<col=800000>Bones.)<col=000000>"), TELEPORT_TO_CATHERBY(ELITE, true, "Teleport to Catherby.");
    //done
    private final DiaryComplexity type;
    private final int objectiveLength;
    private final String[] task;
    private final Predicate<Player> predicate;
    private final boolean autoCompleted;

    KandarinDiary(final DiaryComplexity type, final String... task) {
        this(type, false, 1, p -> true, task);
    }

    KandarinDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
        this(type, autoCompleted, 1, p -> true, task);
    }

    KandarinDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
        this(type, false, 1, predicate, task);
    }

    KandarinDiary(final DiaryComplexity type, final boolean autoCompleted, final Predicate<Player> predicate, final String... task) {
        this(type, autoCompleted, 1, predicate, task);
    }

    KandarinDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
        this.type = type;
        this.autoCompleted = autoCompleted;
        this.objectiveLength = objectiveLength;
        this.predicate = predicate;
        this.task = task;
    }

    public static final KandarinDiary[] VALUES = values();
    public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

    static {
        for (final DiaryComplexity type : DiaryComplexity.VALUES) {
            MAP.put(type, new ArrayList<>(15));
        }
        for (final KandarinDiary value : VALUES) {
            if (value.autoCompleted) continue;
            MAP.get(value.type).add(value);
        }
    }

    @Override
    public int taskMaster() {
        return 5517;
    }

    @Override
    public DiaryArea area() {
        return DiaryArea.KANDARIN;
    }

    @Override
    public String title() {
        return "Kandarin";
    }

    @Override
    public boolean flagging() {
        return false;
    }

    @Override
    public int diaryStarted() {
        return 4451;
    }

    @Override
    public boolean autoCompleted() {
        return autoCompleted;
    }

    @Override
    public int[][] diaryCompleted() {
        return flow(4475);
    }

    @Override
    public DiaryChunk[] chunks() {
        return new DiaryChunk[] {new DiaryChunk(6307, 4515), new DiaryChunk(6308, 4516), new DiaryChunk(6309, 4517), new DiaryChunk(6310, 4518)};
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
