package com.zenyte.game.content.achievementdiary.diaries;

import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.achievementdiary.DiaryArea;
import com.zenyte.game.content.achievementdiary.DiaryChunk;
import com.zenyte.game.content.achievementdiary.DiaryComplexity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.HarmonyIsland;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.zenyte.game.content.achievementdiary.AreaPredicate.*;

/**
 * @author Kris | 03/11/2018 23:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum MorytaniaDiary implements Diary {
    CRAFT_SNELM(EASY, true, "Craft any Snelm from scratch in Morytania."), COOK_A_THIN_SNAIL(EASY, true, "Cook a thin Snail on the Port Phasmatys range."), GET_A_SLAYER_TASK_FROM_MAZCHNA(EASY, NO_PREDICATE, "Get a slayer task from Mazchna."), //done
    KILL_A_BANSHEE(EASY, p -> p.inArea("Slayer Tower"), "Kill a Banshee in the Slayer Tower."), //done
    HAVE_SBOTT_TAN_ITEMS(EASY, "Have Sbott in Canifis tan something for you."), //done
    ENTER_MORT_MYRE_SWAMP(EASY, NO_PREDICATE, "Enter Mort Myre Swamp."), //done
    KILL_A_GHOUL(EASY, "Kill a Ghoul."), //done
    PLACE_A_SCARECROW(EASY, "Place a Scarecrow in the Morytania flower patch."), //done
    OFFER_SOME_BONEMEAL(EASY, "Offer some bonemeal at the Ectofuntus."), KILL_A_WEREWOLF(EASY, true, "Kill a werewolf in its human form using the Wolfbane", "Dagger."), RESTORE_PRAYER_POINTS_AT_NATURE_ALTAR(EASY, true, "Restore your prayer points at the nature altar."), CATCH_SWAMP_LIZARD(MEDIUM, NO_PREDICATE, "Catch a swamp lizard."), //done
    COMPLETE_CANIFIS_COURSE_LAP(MEDIUM, NO_PREDICATE, "Complete a lap of the Canifis agility course."), //done
    OBTAIN_BARK(MEDIUM, true, "Obtain some Bark from a Hollow tree."), TRAVEL_TO_DRAGONTOOTH_ISLE(MEDIUM, true, "Travel to Dragontooth Isle."), KILL_A_TERRORDOG(MEDIUM, true, NO_PREDICATE, "Kill a Terror Dog."), //done but disabled as terrordogs are unavailable.
    COMPLETE_TROUBLE_BREWING_GAME(MEDIUM, true, "Complete a game of trouble brewing."), BOARD_THE_SWAMPY_BOAT(MEDIUM, true, "Board the Swampy boat at the Hollows."), MAKE_CANNONBALLS(MEDIUM, PORT_PHASMATYS::inArea, "Make a batch of cannonballs at the Port Phasmatys", "furnace."), //done
    KILL_A_FEVER_SPIDER(MEDIUM, true, "Kill a Fever Spider on Braindeath Island."), USE_ECTOPHIAL(MEDIUM, true, "Use an ectophial to return to Port Phasmatys."), MIX_A_GUTHIX_BALANCE(MEDIUM, "Mix a Guthix Balance potion while in Morytania."), //done
    ENTER_KHARYRLL_PORTAL(HARD, true, "Enter the Kharyrll portal in your POH."), //done
    CLIMB_SPIKED_CHAIN_IN_SLAYER_TOWER(HARD, NO_PREDICATE, "Climb the advanced spike chain within Slayer Tower."), //done
    HARVEST_WATERMELON(HARD, p -> p.inArea(HarmonyIsland.class), "Harvest some Watermelon from the Allotment patch on", "Harmony Island."), CHOP_AND_BURN_MAHOGANY_LOGS(HARD, true, "Chop and burn some mahogany logs on Mos Le'Harmless."), COMPLETE_HARD_TEMPLE_TREK(HARD, true, "Complete a temple trek with a hard companion."), KILL_A_CAVE_HORROR(HARD, NO_PREDICATE, "Kill a Cave Horror."), //done
    HARVEST_BITTERCAP_MUSHROOMS(HARD, "Harvest some Bittercap Mushrooms from the patch in", "Canifis."), //done
    PRAY_AT_ALTAR_OF_NATURE(HARD, true, "Pray at the Altar of Nature with Piety activated."), USE_SALVE_BRIDGE_SHORTCUT(HARD, true, "Use the shortcut to get to the bridge over the Salve."), MINE_MITHRIL_IN_ABANDONED_MINE(HARD, true, "Mine some Mithril ore in the Abandoned Mine."), CATCH_SHAKE_IN_BURGH_DE_ROTT(ELITE, true, "Catch a shark in Burgh de Rott with your bare hands."), CREMATE_SHADE_REMAINS(ELITE, true, "Cremate any Shade remains on a Magic or Redwood pyre."), FERTILIZE_MORYTANIA_HERB_PATCH(ELITE, "Fertilize the Morytania herb patch using Lunar Magic."), CRAFT_BLACK_DRAGONHIDE_BODY(ELITE, CANIFIS_BANK::inArea, "Craft a Black dragonhide body in Canifis bank."), //done
    KILL_ABYSSAL_DEMON(ELITE, p -> p.inArea("Slayer Tower"), "Kill an Abyssal demon in the Slayer Tower."), //done
    LOOT_BARROWS_CHEST(ELITE, NO_PREDICATE, "Loot the Barrows chest while wearing any complete", "barrows set.");
    //done
    private final DiaryComplexity type;
    private final int objectiveLength;
    private final String[] task;
    private final Predicate<Player> predicate;
    private final boolean autoCompleted;

    MorytaniaDiary(final DiaryComplexity type, final String... task) {
        this(type, false, 1, MORYTANIA_TERRITORY::inArea, task);
    }

    MorytaniaDiary(final DiaryComplexity type, final boolean autoCompleted, final String... task) {
        this(type, autoCompleted, 1, MORYTANIA_TERRITORY::inArea, task);
    }

    MorytaniaDiary(final DiaryComplexity type, final Predicate<Player> predicate, final String... task) {
        this(type, false, 1, predicate, task);
    }

    MorytaniaDiary(final DiaryComplexity type, final boolean autoCompleted, final Predicate<Player> predicate, final String... task) {
        this(type, autoCompleted, 1, predicate, task);
    }

    MorytaniaDiary(final DiaryComplexity type, final boolean autoCompleted, final int objectiveLength, final Predicate<Player> predicate, final String... task) {
        this.type = type;
        this.autoCompleted = autoCompleted;
        this.objectiveLength = objectiveLength;
        this.predicate = predicate;
        this.task = task;
    }

    public static final MorytaniaDiary[] VALUES = values();
    public static final Map<DiaryComplexity, List<Diary>> MAP = new EnumMap<>(DiaryComplexity.class);

    static {
        for (final DiaryComplexity type : DiaryComplexity.VALUES) {
            MAP.put(type, new ArrayList<>(15));
        }
        for (final MorytaniaDiary value : VALUES) {
            if (value.autoCompleted) continue;
            MAP.get(value.type).add(value);
        }
    }

    @Override
    public int taskMaster() {
        return 5521;
    }

    @Override
    public DiaryArea area() {
        return DiaryArea.MORYTANIA;
    }

    @Override
    public String title() {
        return "Morytania";
    }

    @Override
    public boolean flagging() {
        return false;
    }

    @Override
    public int diaryStarted() {
        return 4454;
    }

    @Override
    public boolean autoCompleted() {
        return autoCompleted;
    }

    @Override
    public int[][] diaryCompleted() {
        return flow(4487);
    }

    @Override
    public DiaryChunk[] chunks() {
        return new DiaryChunk[] {new DiaryChunk(6315, 4527), new DiaryChunk(6316, 4528), new DiaryChunk(6317, 4529), new DiaryChunk(6318, 4530)};
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
