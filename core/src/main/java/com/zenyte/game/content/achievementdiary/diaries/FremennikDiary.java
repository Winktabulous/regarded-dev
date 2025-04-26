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

public enum FremennikDiary implements Diary {
    CATCH_CERULEAN_TWITCH(EASY, NO_PREDICATE, "Catch a Cerulean twitch."), //done
    CHANGE_YOUR_BOOTS(EASY, true, "Change your boots at Yrsa's Shoe Store."), KILL_ROCK_CRABS(EASY, 5, "Kill 5 Rock crabs."), //done
    CRAFT_A_TIARA(EASY, 1 | 2 | 4, "Craft a tiara from scratch in Rellekka.") {
        @Override
        public boolean flagging() {
            return true;
        }
    },
    //done
    BROWSE_THE_STONEMASONS_SHOP(EASY, NO_PREDICATE, "Browse the Stonemasons shop."), //done
    COLLECT_SNAPE_GRASS(EASY, false, 5, p -> p.inArea("Waterbirth Island"), "Collect 5 Snape grass on Waterbirth Island."), //done
    STEAL_FROM_CRAFTING_STALL(EASY, KELDAGRIM::inArea, "Steal from the Keldagrim crafting or baker's stall."), //done
    FILL_A_BUCKET(EASY, "Fill a bucket with water at the Rellekka well."), //done
    ENTER_TROLL_STRONGHOLD(EASY, true, "Enter the Troll Stronghold."), //done
    CHOP_AND_BURN_OAK_LOGS(EASY, 1 | 2, "Chop and burn some oak logs in the Fremennik Province.") {
        @Override
        public boolean flagging() {
            return true;
        }
    },
    //done
    SLAY_BRINE_RAT(MEDIUM, true, "Slay a Brine rat."), TRAVEL_TO_SNOWY_AREA_VIA_EAGLE(MEDIUM, true, "Travel to the Snowy Hunter Area via Eagle."), MINE_COAL_IN_RELLEKKA(MEDIUM, "Mine some coal in Rellekka."), //done
    STEAL_FROM_RELLEKKA_FISH_STALLS(MEDIUM, "Steal from the Rellekka Fish stalls."), //done
    TRAVEL_TO_MISCELLANIA(MEDIUM, NO_PREDICATE, "Travel to Miscellania by Fairy ring."), //done
    CATCH_A_SNOWY_KNIGHT(MEDIUM, true, "Catch a Snowy knight."), PICK_UP_YOUR_PET_ROCK(MEDIUM, true, "Pick up your Pet Rock from your POH Menagerie."), VISIT_THE_LIGHTHOUSE(MEDIUM, true, "Visit the Lighthouse from Waterbirth island."), MINE_SOME_GOLD(MEDIUM, true, "Mine some gold at the Arzinian mine."), TELEPORT_TO_TROLLHEIM(HARD, true, "Teleport to Trollheim."), //done
    CATCH_SABRE_TOOTHED_KYATT(HARD, NO_PREDICATE, "Catch a Sabre-toothed Kyatt."), //done
    MIX_SUPER_DEFENCE(HARD, "Mix a super defence potion in the Fremennik province."), //done
    STEAL_FROM_GEM_STALL(HARD, KELDAGRIM::inArea, "Steal from the Keldagrim Gem Stall."), //done
    CRAFT_FREMENNIK_SHIELD(HARD, true, "Craft a Fremennik shield on Neitiznot."), MINE_ADAMANTITE_ORE(HARD, false, 5, JATISZO_MINE::inArea, "Mine 5 Adamantite ores on Jatizso."), //done
    OBTAIN_SUPPORT_FROM_KINGDOM(HARD, true, "Obtain 100% support from your kingdom subjects."), TELEPORT_TO_WATERBIRTH_ISLAND(HARD, true, "Teleport to Waterbirth Island."), //done
    OBTAIN_BLAST_FURNACE_PERMISSION(HARD, true, "Obtain the Blast Furnace Foreman's permission to use the", "Blast Furnace for free."), KILL_DAGANNOTH_KINGS(ELITE, false, 1 | 2 | 4, NO_PREDICATE, "Kill each of the Dagannoth Kings.") {
        @Override
        public boolean flagging() {
            return true;
        }
    },
    //done
    CRAFT_ASTRAL_RUNES(ELITE, NO_PREDICATE, "Craft 56 astral runes at once."), //done
    CREATE_A_DRAGONSTONE_AMULET(ELITE, p -> p.inArea("Neitiznot"), "Create a dragonstone amulet in the Neitiznot furnace."), //done
    COMPLETE_RELLEKKA_AGILITY_COURSE_LAP(ELITE, "Complete a lap of the Rellekka agility course."), KILL_GODWARS_GENERALS(ELITE, false, 1 | 2 | 4 | 8, NO_PREDICATE, "Kill each of the Godwars generals.") {
        @Override
        public boolean flagging() {
            return true;
        }
    },
    //done
    SLAY_A_SPIRITUAL_MAGE(ELITE, NO_PREDICATE, "Slay a Spiritual mage within the Godwars Dungeon.");
    //done
    private final DiaryComplexity type;
    private final int objectiveLength;
    private final String[] task;
    private final Predicate<Player> predicate;
    private final boolean autoCompleted;

    FremennikDiary(final DiaryComplexity type, final int objectiveLength, final String... task) {
        this(type, false, objectiveLength, FREMENNIK_TERRITORY::inArea, task);
    }

    FremennikDiary(final DiaryComplexity type, final String... task) {
        this(type, false, 1, FREMENNIK_TERRITORY::inArea, task);
    }

    FremennikDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
        this(type, autoCompleted, 1, FREMENNIK_TERRITORY::inArea, task);
    }

    FremennikDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
        this(type, false, 1, predicate, task);
    }

    FremennikDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
        this.type = type;
        this.autoCompleted = autoCompleted;
        this.objectiveLength = objectiveLength;
        this.predicate = predicate;
        this.task = task;
    }

    public static final FremennikDiary[] VALUES = values();
    public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

    static {
        for (final DiaryComplexity type : DiaryComplexity.VALUES) {
            MAP.put(type, new ArrayList<>(15));
        }
        for (final FremennikDiary value : VALUES) {
            if (value.autoCompleted) continue;
            MAP.get(value.type).add(value);
        }
    }

    @Override
    public int taskMaster() {
        return 5526;
    }

    @Override
    public DiaryArea area() {
        return DiaryArea.FREMENNIK;
    }

    @Override
    public String title() {
        return "Fremennik";
    }

    @Override
    public boolean flagging() {
        return false;
    }

    @Override
    public int diaryStarted() {
        return 4450;
    }

    @Override
    public boolean autoCompleted() {
        return autoCompleted;
    }

    @Override
    public int[][] diaryCompleted() {
        return flow(4491);
    }

    @Override
    public DiaryChunk[] chunks() {
        return new DiaryChunk[] {new DiaryChunk(6303, 4531), new DiaryChunk(6304, 4532), new DiaryChunk(6305, 4533), new DiaryChunk(6306, 4534)};
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
