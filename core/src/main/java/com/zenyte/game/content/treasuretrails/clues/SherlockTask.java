package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.TreasureTrailType;
import com.zenyte.game.content.treasuretrails.challenges.ClueChallenge;
import com.zenyte.game.content.treasuretrails.challenges.SherlockRequest;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.region.RSPolygon;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * @author Kris | 10/04/2019 19:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum SherlockTask implements Clue {

    WIELD_DRAGON_SCIM(ClueLevel.ELITE, "Wield a dragon scimitar."),
    CAST_LV_FIVE_ENCHANT(ClueLevel.ELITE, "Cast Lvl-5 Enchant."),
    CRAFT_A_NATURE_RUNE(ClueLevel.ELITE, "Craft a nature rune."),
    //CATCH_MOTTLED_EEL(ClueLevel.ELITE, "Catch a mottled eel with aerial fishing in Lake Molch."),
    //SCORE_GOAL_IN_SKULLBALL(ClueLevel.ELITE, "Score a goal in skullball."),
    //COMPLETE_APE_ATOLL_AGILITY_LAP(ClueLevel.ELITE, "Complete a lap at the Ape Atoll Agility Course."),
    CREATE_SUPER_DEFENCE_POTION(ClueLevel.ELITE, "Create a super defence potion."),
    //STEAL_FROM_KING_LATHAS_CHEST(ClueLevel.ELITE, "Steal from a chest in King Lathas' castle in East Ardougne."),
    CRAFT_GREEN_DHIDE_BODY(ClueLevel.ELITE, "Craft a green d'hide body."),
    STRING_YEW_LONGBOW(ClueLevel.ELITE, "String a yew longbow."),
    SLAY_DUST_DEVIL(ClueLevel.ELITE, "Slay a dust devil."),
    //CATCH_BLACK_WARLOCK(ClueLevel.ELITE, "Catch a black warlock."),
    CATCH_RED_CHINCHOMPA(ClueLevel.ELITE, "Catch a red chinchompa."),
    MINE_MITHRIL_ORE(ClueLevel.ELITE, "Mine a piece of mithril ore."),
    SMITH_MITHRIL_2H(ClueLevel.ELITE, "Smith a mithril 2h sword."),
    CATCH_RAW_SHARK(ClueLevel.ELITE, "Catch a raw shark."),
    CHOP_YEW_TREE(ClueLevel.ELITE, "Chop a yew tree."),
    //FIX_MAGICAL_LAMP_IN_DORGESH_KAAN(ClueLevel.ELITE, "Fix a magical lamp in Dorgesh-Kaan."),
    BURN_A_YEW_LOG(ClueLevel.ELITE, "Burn a yew log."),
    COOK_A_SWORDFISH(ClueLevel.ELITE, "Cook a swordfish."),
    CRAFT_MULTIPLE_COSMIC_RUNES(ClueLevel.ELITE, "Craft multiple cosmic runes from a single essence.", player -> player.getSkills().getLevel(SkillConstants.RUNECRAFTING) >= 59),
    PLANT_WATERMELON_SEED(ClueLevel.ELITE, "Plant a watermelon seed."),
    ACTIVATE_CHIVALRY_PRAYER(ClueLevel.ELITE, "Activate the Chivalry prayer."),
    //HAND_IN_SHAYZIEN_ARMOUR(ClueLevel.ELITE, "Hand in a boxed set of Shayzien supply armour at tier 2 or above."),

    EQUIP_ABYSSAL_WHIP_INFRONT_OF_DEMONS(ClueLevel.MASTER, "Equip an abyssal whip in front of the abyssal demons of the Slayer Tower.", new Predicate<Player>() {
        private final RSPolygon area = new RSPolygon(new int[][]{
                {3409, 3580}, {3405, 3576}, {3405, 3572}, {3409, 3568}, {3409, 3564}, {3408, 3563}, {3408, 3559}, {3409, 3558}, {3409, 3555}, {3421, 3555},
                {3421, 3562}, {3431, 3562}, {3431, 3577}, {3425, 3577}, {3424, 3578}, {3420, 3578}, {3419, 3577}, {3416, 3577}, {3413, 3580}
        }, 2);
        @Override
        public boolean test(final Player player) {
            return area.contains(player.getLocation());
        }
    }),
    SMITH_RUNE_MED_HELM(ClueLevel.MASTER, "Smith a runite med helm."),
    TELEPORT_TO_PLANTED_SPIRIT_TREE(ClueLevel.MASTER, "Teleport to a spirit tree you planted yourself."),
    //CREATE_BARROWS_TELEPORT_TABLET(ClueLevel.MASTER, "Create a Barrows teleport tablet."),
    SLAY_A_NECHRYAEL(ClueLevel.MASTER, "Slay a Nechryael"),
    //KILL_SPIRITUAL_MAGE_WHILST_REPRESENTING_THEIR_GOD(ClueLevel.MASTER, "Kill the spiritual, magic and godly whilst representing their own god."),
    CREATE_UNSTRUNG_DRAGONSTONE_AMULET(ClueLevel.MASTER, "Create an unstrung dragonstone amulet at a furnace."),
    BURN_MAGIC_LOG(ClueLevel.MASTER, "Burn a magic log."),
    BURN_REDWOOD_LOG(ClueLevel.MASTER, "Burn a redwood log."),
    //COMPLETE_RELLEKKA_ROOFTOP_LAP_IN_GRACEFUL(ClueLevel.MASTER, "Complete a lap of the Rellekka rooftop agility course whilst sporting the finest amount of grace."),
    MIX_ANTIVENOM(ClueLevel.MASTER, "Mix an anti-venom potion."),
    //MINE_RUNITE_ORE_IN_MINING_GEAR(ClueLevel.MASTER, "Mine a piece of runite ore whilst sporting the finest mining gear."),
    STEAL_GEM_FROM_ARDOUGNE_MARKET(ClueLevel.MASTER, "Steal a gem from the Ardougne market.", player -> player.inArea("East Ardougne")),
    PICKPOCKET_AN_ELF(ClueLevel.MASTER, "Pickpocket an elf."),
    //BIND_BLOOD_RUNE_AT_BLOOD_ALTAR(ClueLevel.MASTER, "Bind a blood rune at the blood altar."),
    CREATE_RANGING_MIX(ClueLevel.MASTER, "Create a ranging mix potion."),
    FLETCH_RUNE_DART(ClueLevel.MASTER, "Fletch a rune dart."),
    //CREMATE_FIYR_REMAINS(ClueLevel.MASTER, "Cremate a set of fiyr remains."),
    DISSECT_SACRED_EEL(ClueLevel.MASTER, "Dissect a sacred eel."),
    //KILL_LIZARDMAN_SHAMAN(ClueLevel.MASTER, "Kill a lizardman shaman."),
    //CATCH_ANGLERFISH_IN_FISHING_GEAR(ClueLevel.MASTER, "Angle for an Anglerfish in your finest fishing gear."),
    //CHOP_REDWOOD_LOG_IN_LUMBERJACK(ClueLevel.MASTER, "Chop a redwood log whilst sporting the finest lumberjack gear."),
    //CRAFT_A_LIGHTORB_IN_DORGESHKAAN_BANK(ClueLevel.MASTER, "Craft a light orb in the Dorgesh-Kaan bank."),
    KILL_REANIMATED_ABYSSAL(ClueLevel.MASTER, "Kill a reanimated abyssal."),
    //KILL_FIYR_SHADE_INSIDE_CATACOMBS(ClueLevel.MASTER, "Kill a Fiyr shade inside Mort'tons shade catacombs.")
    ;

    private final String text;
    private final ClueLevel level;
    private final Predicate<Player> predicate;
    private final SherlockRequest task;

    public final void progress(@NotNull final Player player) {
        TreasureTrail.progressSherlockTask(player, predicate, this);
    }

    SherlockTask(final ClueLevel level, final String text) {
        this(level, text, null);
    }

    SherlockTask(final ClueLevel level, final String text, final Predicate<Player> predicate) {
        this.level = level;
        this.text = text;
        this.predicate = predicate;
        this.task = new SherlockRequest(this);
    }

    @Override
    public void view(@NotNull final Player player, @NotNull final Item item) {
        player.getTemporaryAttributes().put("Clue scroll item", item);
        GameInterface.CLUE_SCROLL.open(player);
    }

    @Override
    public TreasureTrailType getType() {
        return TreasureTrailType.SHERLOCK;
    }

    @Override
    public String getEnumName() {
        return toString();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public ClueChallenge getChallenge() {
        return task;
    }

    @NotNull
    @Override
    public ClueLevel level() {
        return level;
    }}
