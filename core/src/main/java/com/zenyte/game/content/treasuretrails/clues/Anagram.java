package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.PuzzleType;
import com.zenyte.game.content.treasuretrails.challenges.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.zenyte.game.content.treasuretrails.ClueLevel.*;
import static com.zenyte.game.content.treasuretrails.PuzzleType.LIGHT_BOX;
import static com.zenyte.game.content.treasuretrails.PuzzleType.PUZZLE_BOX;
import static com.zenyte.game.content.treasuretrails.clues.ChallengeScroll.*;

/**
 * An enum containing all of the anagrams in OSRS. If the challenge says light
 * box, it means it will be either a light box or a puzzle box - as it always
 * supports both.
 *
 * @author Kris | 30. march 2018 : 23:17.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Anagram implements Clue {
    A_BARAEK("A Baker", MEDIUM, BARAEK, 2881),
    A_CAPTAIN_TOBIAS("A Basic Anti Pot", MEDIUM, CAPTAIN_TOBIAS, 3644),
    A_ARETHA("A Heart", MEDIUM, ARETHA, 7042),
    A_ZENESHA("A Zen She", HARD, PUZZLE_BOX, 8681, 1176, 4584),
    A_CAM_THE_CAMEL("Ace Match Elm", HARD, PUZZLE_BOX, 5952),
    A_JARAAH("Aha Jar", MEDIUM, 3344),
    //A_CAPTAIN_NINTO("An Paint Tonic", HARD, PUZZLE_BOX, 2569),
    A_CAROLINE("Arc O Line", MEDIUM, CAROLINE, 5067),
    A_ORACLE("Are Col", MEDIUM, ORACLE, 821),
    A_CHARLIE_THE_TRAMP("Armchair The Pelt", MEDIUM, CHARLIE_THE_TRAMP, 5209),
    A_RAMARA_DU_CROISSANT("Arr! So I am a crust, and?", HARD, PUZZLE_BOX, 4296),
    A_REGATH("At Herg", ELITE, REGATH, 7056),
    //A_SABA("A Bas", MEDIUM, 4093),
    A_FATHER_AERECK("Area Chef Trek", MEDIUM, FATHER_AERECK, 2812),
    A_BRIMSTAIL("Bail Trims", MEDIUM, NpcId.BRIMSTAIL, NpcId.BRIMSTAIL_11430, NpcId.BRIMSTAIL_11431),
    //A_BRAMBICKLE("Baker Climb", HARD, PUZZLE_BOX, 2551),
    A_BOLKOY("By Look", HARD, BOLKOY, 4965),
    A_LUMBRIDGE_GUIDE("Blue Grim Guided", HARD, PUZZLE_BOX, 306, 1179, 1181, 3393, 8635),
    A_SACRIFICE("Car If Ices", MASTER, LIGHT_BOX, 2037),
    A_ONEIROMANCER("Career In Moon", ELITE, ONEIROMANCER, 3835, 8049, 8158, 8166, 8174),
    //A_GNOME_COACH("C On Game Hoc", HARD, GNOME_COACH, 3142),
    A_OLD_CRONE("Cool Nerd", ELITE, OLD_CRONE, 2996),
    A_PROSPECTOR_PERCY("Copper Ore Crypts", HARD, PROSPECTOR_PERCY, 6562),
    A_EDWARD("DED WAR", MASTER, LIGHT_BOX, 2199),
    A_DARK_MAGE("Dekagram", HARD, DARK_MAGE, 2583, 7752, 7753),
    A_MANDRITH("DIM THARN", MASTER, PUZZLE_BOX, 6599),
    A_DOOMSAYER("Do Say More", HARD, DOOMSAYER, 6773, 6774),
    B_MANDRITH("Dr Hitman", ELITE, LIGHT_BOX, 6599),
    A_DRUNKEN_DWARF("Dr Warden Funk", HARD, PUZZLE_BOX, 322, 2408, 2409, 2429, 4305),
    A_BRUNDT_THE_CHIEFTAIN("Dt Run B", MEDIUM, BRUNDT_THE_CHIEFTAIN, 9263, 3926, 8048, 8145, 8153, 8161, 8169),
    A_DUGOPUL("Duo Plug", MASTER, LIGHT_BOX, 5245),
    A_LOWE("El Ow", MEDIUM, 2883),
    A_RECRUITER("Err Cure It", MEDIUM, RECRUITER, 4262, 7734),
    //A_RUNOLF("Forlun", MASTER, LIGHT_BOX, 1082),
    A_KING_BOLREN("Goblin Kern", MEDIUM, 4963),
    A_GABOOTY("Got A Boy", MEDIUM, GABOOTY, 6424, 6425),
    A_UGLUG_NAR("Gulag Run", HARD, PUZZLE_BOX, 861),
    A_OTTO_GODBLESSED("Goblets Odd Toes", MEDIUM, OTTO_GODBLESSED, 2914, 2915),
    A_LUTHAS("Halt Us", MEDIUM, 3647),
    A_RIKI_THE_SCULPTORS_MODEL("He Do Pose. It Is Cultrrl, Mk?", HARD, PUZZLE_BOX, 2348, 2349, 2350, 2351, 2352, 2353, 2354, 2355),
    //A_EOHRIC("Heoric", MEDIUM, EOHRIC, 4103),
    A_HORPHIS("HIS PHOR", MEDIUM, HORPHIS, 7046),
    A_MARISI("I Am Sir", MEDIUM, MARISI, 6921),
    A_FYCIE("Icy Fe", MEDIUM, 1471),
    //A_DOMINIC_ONION("I Doom Icon Inn", MEDIUM, DOMINIC_ONION, 1120),
    A_SHIRATTI_THE_CUSTODIAN("I Eat Its Chart Hints Do U", HARD, PUZZLE_BOX, 4760),
    A_NIEVE("I Even", MEDIUM, NIEVE, 6797, 7108, 7109, 7110),
    A_FAIRY_NUFF("I Faffy Run", HARD, PUZZLE_BOX, 1841, 5836),
    A_IMMENIZZ("Im N Zezim", MASTER, LIGHT_BOX, 5735),
    A_SIR_KAY("Kay Sir", MEDIUM, SIR_KAY, 3521, 4349, 4352, 4355),
    A_KAYLEE("Leakey", MEDIUM, KAYLEE, 1316),
    A_ODD_OLD_MAN("Land Doomd", HARD, PUZZLE_BOX, 1259),
    A_KING_ROALD("Lark In Dog", MEDIUM, KING_ROALD, 1399, 4163, 5215, 6389, 8042, 8636),
    A_GALLOW("Low Lag", MEDIUM, GALLOW, 6775),
    A_GUARD_VEMMELDO("Ladder Memo Guv", ELITE, GUARD_VEMMELDO, 2574),
    //A_AMBASSADOR_ALVIJAR("Majors Lava Bads Air", ELITE, AMBASSADOR_ALVIJAR, 5177),
    A_LUMINATA("Mal in Tau", MASTER, LIGHT_BOX, 4469),
    B_CAM_THE_CAMEL("Me Am The Calc", HARD, PUZZLE_BOX, 5952),
    C_CAM_THE_CAMEL("Machete Clam", ELITE, CAM_THE_CAMEL, 5952),
    A_FEMI("Me if", MEDIUM, 1431),
    //A_OLD_MAN_RAL("Mold La Ran", MASTER, LIGHT_BOX, 3772),
    A_BROTHER_OMAD("Motherboard", HARD, BROTHER_OMAD, 4244),
    A_RADIMUS_ERKLE("Mus Kil Reader", MASTER, LIGHT_BOX, 3953),
    A_LAMMY_LANGLE("My Mangle Lal", HARD, PUZZLE_BOX, 6814),
    A_ORONWEN("No Owner", ELITE, ORONWEN, 1478),
    A_EDMOND("Nod Med", MEDIUM, EDMOND, 2202, 4256),
    A_CAPN_IZZY_NO_BEARD("O Birdz A Zany En Pc", HARD, CAPN_IZZY_NO_BEARDS, 5789),
    A_COOK("Ok Co", MEDIUM, COOK, 4626),
    A_WIZARD_FRUMSCONE("Or Zinc Fumes Ward", ELITE, PUZZLE_BOX, 3246),
    A_NURSE_WOONED("Our Own Needs", ELITE, NURSE_WOONED, 6873),
    A_FLAX_KEEPER("Peak Reflex", MEDIUM, FLAX_KEEPER, 5522),
    A_PARTY_PETE("Peaty Pert", MEDIUM, 5792),
    A_PROFESSOR_ONGLEWIP("Profs Lose Wrong Pie", HARD, PUZZLE_BOX, 2560),
    A_BROTHER_TRANQUILITY("Quit Horrible Tyrant", HARD, BROTHER_TRANQUILITY, 550, 551, 552),
    A_SQUIRE("Que Sir", MEDIUM, SQUIRE, 4737),
    A_KARIM("R Ak Mi", MEDIUM, KARIM, 2877),
    //A_MARTIN_THWAIT("Rat Mat Within", HARD, MARTIN_THWAIT, 3193),
    A_TRADER_STAN("Red Art Tans", HARD, PUZZLE_BOX, 9299),
    A_CLERRIS("R SLICER", MEDIUM, CLERRIS, 7040),
    A_DUNSTAN("Sand Nut", MEDIUM, DUNSTAN, 4105),
    //A_QUEEN_SIGRID("Sequin Dirge", HARD, PUZZLE_BOX, 765),
    A_GUILDMASTER_LARS("Slam Duster Grail", MASTER, LIGHT_BOX, 7236),
    A_HANS("Snah", MEDIUM, 3105, 3077, 6784, 7979),
    A_LISSE_ISAAKSON("Snakes So I Sail", ELITE, LISSE_ISAAKSON, 1888),
    A_DOCKMASTER("Tamed Rocks", MEDIUM, DOCKMASTER, 6966),
    A_WINGSTONE("Ten Wigs On", MASTER, LIGHT_BOX, 2552),
    D_CAM_THE_CAMEL("Them Cal Came", HARD, PUZZLE_BOX, 5952),
    A_HICKTON("Thickno", MEDIUM, HICKTON, 3212),
    A_NEW_RECRUIT_TONY("Twenty Cure Iron", MASTER, LIGHT_BOX, 6872),
    A_SIGLI_THE_HUNTSMAN("Unleash Night Mist", ELITE, SIGLI_THE_HUNTSMAN, 3924),
    A_STEVE("Veste", MEDIUM, STEVE, 6798, 6799, 6797),
    //A_EVIL_DAVE("Veil Veda", HARD, EVIL_DAVE, 901, 902, 3394, 4806, 4807),
    A_YSGAWYN("YAWNS GY", MASTER, LIGHT_BOX, NpcId.YSGAWYN),
    AN_EARL("An Earl", BEGINNER, 2878),
    CARPET_AHOY("Carpet Ahoy", BEGINNER, 5036),
    DISORDER("Char game disorder", BEGINNER, false,NpcId.ARCHMAGE_SEDRIDOR, NpcId.ARCHMAGE_SEDRIDOR_11433),
    I_CORD("I Cord", BEGINNER, 3893),
    IN_BAR("In Bar", BEGINNER, 2892),
    RAIN_COVE("Rain Cove", BEGINNER, 3561),
    RUG_DETER("Rug Deter", BEGINNER, 3500, 7723, 7284),
    SIR_SHARE_RED("Sir Share Red", BEGINNER, 1305),
    TAUNT_ROOF("Taunt Roof", BEGINNER, 1260);
    static final Anagram[] values = values();
    public static final Int2ObjectMap<List<Anagram>> npcMap = new Int2ObjectOpenHashMap<>();

    static {
        for (final Anagram value : values) {
            if (!value.addToNPCMap) continue;
            for (final int id : value.npcIds) {
                npcMap.computeIfAbsent(id, a -> new ArrayList<>()).add(value);
            }
        }
    }

    private final String anagram;
    private final ClueLevel level;
    private final ChallengeScroll challenge;
    private final PuzzleType boxId;
    private final int[] npcIds;
    private final ClueChallenge clueChallenge;
    private final boolean addToNPCMap;

    Anagram(final String anagram, final ClueLevel level, boolean addToNPCMap, final int... npcIds) {
        this(anagram, level, null, null, addToNPCMap, npcIds);
    }

    Anagram(final String anagram, final ClueLevel level, final int... npcIds) {
        this(anagram, level, null, null, true, npcIds);
    }

    Anagram(final String anagram, final ClueLevel level, final PuzzleType puzzleType, final int... npcIds) {
        this(anagram, level, null, puzzleType, true, npcIds);
    }

    Anagram(final String anagram, final ClueLevel level, final ChallengeScroll challenge, final int... npcIds) {
        this(anagram, level, challenge, null, true, npcIds);
    }

    Anagram(final String anagram, final ClueLevel level, final ChallengeScroll challenge, final PuzzleType puzzleType, final boolean addToNPCMap, final int... npcIds) {
        this.anagram = anagram;
        this.level = level;
        this.challenge = challenge;
        this.boxId = puzzleType;
        this.npcIds = npcIds;
        this.addToNPCMap = addToNPCMap;
        clueChallenge = challenge != null ? new TalkChallengeRequest(challenge, npcIds) : boxId == null ? new TalkRequest(npcIds) : boxId.equals(PUZZLE_BOX) ? new PuzzleRequest(npcIds) : new LightBoxRequest(npcIds);
    }

    @Override
    public void view(@NotNull Player player, @NotNull Item item) {
        player.getTemporaryAttributes().put("Clue scroll item", item);
        GameInterface.CLUE_SCROLL.open(player);
    }

    @Override
    public String getEnumName() {
        return toString();
    }

    @Override
    public String getText() {
        return "This anagram reveals who to speak to next: " + anagram.toUpperCase();
    }

    @Override
    public ClueChallenge getChallenge() {
        return clueChallenge;
    }

    @NotNull
    @Override
    public ClueLevel level() {
        return level;
    }

    public String getAnagram() {
        return anagram;
    }

    public ClueLevel getLevel() {
        return level;
    }

    public PuzzleType getBoxId() {
        return boxId;
    }

    public int[] getNpcIds() {
        return npcIds;
    }

    public ClueChallenge getClueChallenge() {
        return clueChallenge;
    }
}
