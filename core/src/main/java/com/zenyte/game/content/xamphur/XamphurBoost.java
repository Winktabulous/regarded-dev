package com.zenyte.game.content.xamphur;

import com.zenyte.game.content.serverevent.WorldBoostType;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.entity.player.SkillConstants;

public enum XamphurBoost implements WorldBoostType {

//    EXP_BOOST_50("XP Boost (%s)"),
//    MARKS_OF_GRACE_X2("Marks of Grace (x2)"),
    GOLDEN_NUGGETS_X2("Golden Nuggets (x2)"),
    SLAYER_POINTS_X2("Slayer Points (x2)"),
//    VOTE_LOYALTY_X2("Vote & Loyalty points (x2)"),
    RC_RUNES_X2("Runecrafting (x2 Runes)"),
    BRIMSTONE_KEY_DROPS_X2("Brimstone Keys (+ 100% DR)"),
    LARRANS_KEY_DROPS_X2("Larrans Chest (x2 Loot)"),
//    COIN_BG_RESOURCE_CLUES_CKEYS_X2("x2 Coin bags, Resource Boxes, Clue Scroll, and Crystal keys"),
//    FASTER_FISHING_50PCNT("Fishing Speed (50% faster)"),
//    WINTERTODT_50PCNT_BOOST("Bonus Wintertodt Points (50% point increase)"),
    BONUS_CLUE_LOOT("Clue Rewards Bonus"),
    BONUS_BLOOD_MONEY("Blood Money (x2)"),
    BONUS_ZALCANO_LOOT("Zalcano (x2 Loot)"),
    BONUS_BARROWS_DR_25("Barrows (25% Drop rate)"),
    BONUS_SLAYER_SUPERIOR("Superior Slayer (x2)"),
    BONUX_COX_POINTS_25PCNT("CoX points (25% more)"),
    BONUS_PET_RATES("Bonus Pet Rates (25%)"),
    BONUS_GAUNTLET("Gauntlet (x2 Loot)"),
    DOUBLE_COINS("Coins (x2 amount)"),
    PEST_CONTROL("Pest control points (50%)"),
    NIGHTMARE("Nightmare (25% Drop rate)"),
    TOB_PURPLE_BOOST("ToB Uniques (20% more)"),
    TOA_BOOST("ToA Uniques (20% more)"),
    NEX_BOOST("Nex Uniques (20% more)")
    ;
//    BONUS_SEEDS_MSTR_FARMER_50PCNT("Master farmer (bonus)")
    ;

    public static final XamphurBoost[] VALUES = values();
    private final String mssg;

    XamphurBoost(String mssg) {
        this.mssg = mssg;
    }

    public String getMssg() {
        /*if (this == EXP_BOOST_50) {
            String skill = SkillConstants.SKILLS[(int) World.getTemporaryAttributes().get(("BOOST_SKILL"))];
            return String.format(mssg, skill);
        }*/
        return mssg;
    }

    public BroadcastType getBroadcastType() {
        return BroadcastType.XAMPHUR;
    }

}
