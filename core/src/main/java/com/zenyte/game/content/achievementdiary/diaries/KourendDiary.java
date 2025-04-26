package com.zenyte.game.content.achievementdiary.diaries;

import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.achievementdiary.DiaryArea;
import com.zenyte.game.content.achievementdiary.DiaryChunk;
import com.zenyte.game.content.achievementdiary.DiaryComplexity;
import com.zenyte.game.content.skills.farming.plugins.FarmingGuildArea;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.KourendCastle;
import com.zenyte.game.world.region.area.kourend.GreatKourend;
import com.zenyte.game.world.region.area.kourend.MolchAndLizardmanTemple;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.zenyte.game.content.achievementdiary.AreaPredicate.*;

/**
 * @author Kris | 15/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum KourendDiary implements Diary {
    MINE_IRON_IN_MT_KARUULM(EASY, p -> MOUNT_KARUULM_MINE.inArea(p), "Mine some Iron at the Mount Karuulm mine."),  //done
    KILL_A_SANDCRAB(EASY, "Kill a Sandcrab."),  //done
    HAND_A_BOOK_AT_LIBRARY(EASY, true, "Hand in a book at the Arceuus Library."), STEAL_FROM_FOOD_STALL(EASY, "Steal from a Hosidius Food Stall."), //done
    BROWSE_WARRENWS_GENERAL_STORE(EASY, true, "Browse the Warrens General Store."),
    TAKE_LANDS_END_BOAT(EASY, NO_PREDICATE, "Take a boat to Land's End."), //done
    PRAY_AT_ALTAR(EASY, p -> p.inArea(KourendCastle.class), "Pray at the Altar in Kourend Castle."),  //done
    DIG_SALTPETRE(EASY, true, "Dig up some Saltpetre."), ENTER_POH(EASY, true, "Enter your Player Owned House from Hosidius."), HEAL_A_WOUNDED_SOLDIER(EASY, true, "Heal a wounded Shayzien soldier."), CREATE_STRENGTH_POTION(EASY, p -> LOVAKENGJ_PUB.inArea(p), "Create a Strength potion in the Lovakengj Pub."),  //done
    FISH_A_TROUT(EASY, p -> p.inArea(GreatKourend.class), "Fish a Trout from the River Molch."), FAIRY_RING_TO_MT_KARUULM(MEDIUM, NO_PREDICATE, "Travel to the Fairy Ring south of Mount Karuulm."), //done
    KILL_A_LIZARDMAN(MEDIUM, "Kill a Lizardman."), //done
    USE_KHAREDST_MEMOIRS(MEDIUM, true, "Use Kharedst's memoirs to teleport to all five cities in", "Great Kourend."), MINE_VOLCANIC_SULPHER(MEDIUM, true, "Mine some Volcanic sulphur."), ENTER_FARMING_GUILD(MEDIUM, "Enter the Farming Guild."), //done
    SWITCH_TO_NECROMANCY_SPELLBOOK(MEDIUM, "Switch to the Necromancy Spellbook at Tyss."), //done
    REPAIR_A_PISC_CRANE(MEDIUM, true, "Repair a Piscarilius crane."), DELIVER_SOME_INTELLIGENCE_TO_GINEA(MEDIUM, true, "Deliver some intelligence to Captain Ginea."), CATCH_A_BLUEGILL(MEDIUM, true, "Catch a Bluegill on Molch Island."), USE_BOULDER_LEAP(MEDIUM, "Use the boulder leap in the Arceuus essence mine."), //done
    SUBDUE_WINTERTODT(MEDIUM, true,"Subdue the Wintertodt."), //done
    CATCH_A_CHINCHOMPA(MEDIUM, "Catch a Chinchompa in the Kourend Woodland."), //done
    CHOP_SOME_MAHOGANY(MEDIUM, "Chop some Mahogany logs north of the Farming Guild."), //done
    ENTER_WOODCUTTING_GUILD(HARD, "Enter the Woodcutting Guild."), //done
    SMELT_AN_ADAMANTITE_BAR(HARD, p -> FORSAKEN_TOWER.inArea(p), "Smelt an Adamantite bar in The Forsaken Tower."),  //done
    KILL_A_LIZARDMAN_SHAMAN(HARD, p -> p.inArea(MolchAndLizardmanTemple.class), "Kill a Lizardman Shaman in the Lizardman Temple."),  //done
    MINE_SOME_LOVAKITE(HARD, "Mine some Lovakite."),  //done
    PLANT_SOME_LOGOVANO_SEEDS(HARD, true, "Plant some Logavano seeds at the Tithe Farm."), KILL_A_ZOMBIE(HARD, true, "Kill a Zombie in the Shayzien Crypts."), TELEPORT_TO_XERICS_HEART(HARD, NO_PREDICATE, "Teleport to Xeric's Heart using Xeric's Talisman."),  //done
    DELIVER_AN_ARTIFACT_TO_CAPTAIN_KHALED(HARD, true, "Deliver an artefact to Captain Khaled."), KILL_A_WYRM(HARD, NO_PREDICATE, "Kill a Wyrm in the Karuulm Slayer Dungeon."),  //done
    CAST_MONSTER_EXAMINE(HARD, "Cast Monster Examine on a Troll south of Mount", "Quidamortem."),  //done
    CRAFT_ONE_OR_MORE_BLOOD_RUNES(ELITE, "Craft one or more Blood runes."),  //done
    CHOP_REDWOODS(ELITE, "Chop some Redwood logs."),  //done
    DEFEAT_SKOTIZO(ELITE, true, "Defeat Skotizo in the Catacombs of Kourend."), CATCH_ANGLERFISH(ELITE, 1 | 2, "Catch an Anglerfish and cook it whilst in Great Kourend.") {
        @Override
        public boolean flagging() {
            return true;
        }
    },
    //done
    KILL_A_HYDRA(ELITE, NO_PREDICATE, "Kill a Hydra in the Karuulm Slayer Dungeon."),  //done
    CREATE_APE_ATOLL_TELEPORT(ELITE, true, "Create an Ape Atoll teleport tablet."), COMPLETE_A_RAID(ELITE, NO_PREDICATE, "Complete a Raid in the Chambers of Xeric."), CREATE_BATTLESTAFF(ELITE, false, 1 | 2, p -> p.inArea(FarmingGuildArea.class), "Create your own Battlestaff from scratch within the", "Farming Guild.") {
        @Override
        public boolean flagging() {
            return true;
        }
    };
    private final DiaryComplexity type;
    private final int objectiveLength;
    private final String[] task;
    private final Predicate<Player> predicate;
    private final boolean autoCompleted;

    KourendDiary(final DiaryComplexity type, final String... task) {
        this(type, false, 1, KEBOS_TERRITORY::inArea, task);
    }

    KourendDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
        this(type, autoCompleted, 1, KEBOS_TERRITORY::inArea, task);
    }

    KourendDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
        this(type, false, 1, predicate, task);
    }

    KourendDiary(final DiaryComplexity type, final int objectiveLength, final String... task) {
        this(type, false, objectiveLength, KEBOS_TERRITORY::inArea, task);
    }

    KourendDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
        this.type = type;
        this.autoCompleted = autoCompleted;
        this.objectiveLength = objectiveLength;
        this.predicate = predicate;
        this.task = task;
    }

    public static final KourendDiary[] VALUES = values();
    public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

    static {
        for (final DiaryComplexity type : DiaryComplexity.VALUES) {
            MAP.put(type, new ArrayList<>(15));
        }
        for (final KourendDiary value : VALUES) {
            if (value.autoCompleted) continue;
            MAP.get(value.type).add(value);
        }
    }

    @Override
    public int taskMaster() {
        return 8538;
    }

    @Override
    public DiaryArea area() {
        return DiaryArea.KEBOS;
    }

    @Override
    public String title() {
        return "Kourend & Kebos";
    }

    @Override
    public boolean flagging() {
        return false;
    }

    @Override
    public int diaryStarted() {
        return 7924;
    }

    @Override
    public boolean autoCompleted() {
        return autoCompleted;
    }

    @Override
    public int[][] diaryCompleted() {
        return flow(7925);
    }

    @Override
    public DiaryChunk[] chunks() {
        return new DiaryChunk[] {new DiaryChunk(7933, 7929), new DiaryChunk(7934, 7930), new DiaryChunk(7935, 7931), new DiaryChunk(7936, 7932)};
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
