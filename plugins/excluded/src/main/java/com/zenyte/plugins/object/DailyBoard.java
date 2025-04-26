package com.zenyte.plugins.object;

import com.google.common.collect.ImmutableList;
import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.variables.PlayerVariables;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.Calendar;

import static com.zenyte.game.content.achievementdiary.DiaryReward.*;

/**
 * @author Kris | 27/08/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DailyBoard implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.setFaceLocation(player.getLocation().transform(Direction.SOUTH));
        final PlayerVariables vars = player.getVariables();
        final Colour red = Colour.RS_RED;
        final Colour green = Colour.RS_GREEN;
        final boolean resurrectable = vars.getZulrahResurrections() == 0 && DiaryUtil.eligibleFor(WESTERN_BANNER4, player);
        final boolean zaffsBattlestaves = !vars.isClaimedBattlestaves() && (DiaryUtil.eligibleFor(VARROCK_ARMOUR1, player) || DiaryUtil.eligibleFor(VARROCK_ARMOUR2, player) || DiaryUtil.eligibleFor(VARROCK_ARMOUR3, player) || DiaryUtil.eligibleFor(VARROCK_ARMOUR4, player));
        final int teletabPurchases = vars.getTeletabPurchases();
        final int spellbookSwaps = vars.getSpellbookSwaps();
        final int spellbookSwapLimit = player.getSkills().getLevelForXp(SkillConstants.MAGIC) < 99 ? 0 : 5;
        final int partyAdvertisements = vars.getRaidAdvertsQuota();
        final int fountainTeleports = vars.getFountainOfRuneTeleports();
        final int fountainTeleportsLimit = DiaryUtil.eligibleFor(WILDERNESS_SWORD4, player) ? Integer.MAX_VALUE : DiaryUtil.eligibleFor(WILDERNESS_SWORD3, player) ? 1 : 0;
        final int ardougneTeleports = vars.getArdougneFarmTeleports();
        final int ardougneTeleportsLimit = DiaryUtil.eligibleFor(ARDOUGNE_CLOAK4, player) ? Integer.MAX_VALUE : DiaryUtil.eligibleFor(ARDOUGNE_CLOAK3, player) ? 5 : DiaryUtil.eligibleFor(ARDOUGNE_CLOAK2, player) ? 3 : 0;
        final int fishingColonyTeleports = vars.getFishingColonyTeleports();
        final int fishingColonyTeleportsLimit = DiaryUtil.eligibleFor(WESTERN_BANNER4, player) ? Integer.MAX_VALUE : DiaryUtil.eligibleFor(WESTERN_BANNER3, player) ? 3 : DiaryUtil.eligibleFor(WESTERN_BANNER2, player) ? 2 : DiaryUtil.eligibleFor(WESTERN_BANNER1, player) ? 1 : 0;
        final int sherlockTeleports = vars.getSherlockTeleports();
        final int sherlockTeleportsLimit = DiaryUtil.eligibleFor(KANDARIN_HEADGEAR4, player) ? Integer.MAX_VALUE : DiaryUtil.eligibleFor(KANDARIN_HEADGEAR3, player) ? 1 : 0;
        final int rellekkaTeleports = vars.getRellekkaTeleports();
        final int rellekkaTeleportsLimit = DiaryUtil.eligibleFor(FREMENNIK_SEA_BOOTS4, player) ? Integer.MAX_VALUE : (DiaryUtil.eligibleFor(FREMENNIK_SEA_BOOTS3, player) || DiaryUtil.eligibleFor(FREMENNIK_SEA_BOOTS2, player) || DiaryUtil.eligibleFor(FREMENNIK_SEA_BOOTS1, player)) ? 1 : 0;
        final int faladorShieldRecharge = vars.getFaladorPrayerRecharges();
        final int faladorShieldRechargeLimit = DiaryUtil.eligibleFor(FALADOR_SHIELD4, player) ? Integer.MAX_VALUE : (DiaryUtil.eligibleFor(FALADOR_SHIELD3, player) || DiaryUtil.eligibleFor(FALADOR_SHIELD2, player) || DiaryUtil.eligibleFor(FALADOR_SHIELD1, player)) ? 1 : 0;
        final int explorersRingLimit = DiaryUtil.eligibleFor(EXPLORERS_RING3, player) ? 4 : DiaryUtil.eligibleFor(EXPLORERS_RING4, player) || DiaryUtil.eligibleFor(EXPLORERS_RING2, player) ? 3 : DiaryUtil.eligibleFor(EXPLORERS_RING1, player) ? 2 : 0;
        final int explorersRingCharges = Math.min(explorersRingLimit, vars.getRunReplenishments());
        final int explorersRingAlchemy = vars.getFreeAlchemyCasts();
        final int explorersRingAlchemyLimit = DiaryUtil.eligibleFor(EXPLORERS_RING1, player) || DiaryUtil.eligibleFor(EXPLORERS_RING2, player) || DiaryUtil.eligibleFor(EXPLORERS_RING3, player) || DiaryUtil.eligibleFor(EXPLORERS_RING4, player) ? 30 : 0;
        final int cabbageTeleports = vars.getCabbageFieldTeleports();
        final int cabbageTeleportsLimit = DiaryUtil.eligibleFor(EXPLORERS_RING3, player) || DiaryUtil.eligibleFor(EXPLORERS_RING4, player) ? Integer.MAX_VALUE : DiaryUtil.eligibleFor(EXPLORERS_RING2, player) ? 3 : 0;
        final int nardahTeleports = vars.getNardahTeleports();
        final int nardahTeleportsLimit = DiaryUtil.eligibleFor(DESERT_AMULET4, player) ? Integer.MAX_VALUE : DiaryUtil.eligibleFor(DESERT_AMULET3, player) || DiaryUtil.eligibleFor(DESERT_AMULET2, player) ? 1 : 0;
        final int kourendWoodlandTeleports = vars.getKourendWoodlandTeleports();
        final int kourendWoodlandsTeleportsLimit = DiaryUtil.eligibleFor(RADAS_BLESSING4, player) || DiaryUtil.eligibleFor(RADAS_BLESSING3, player) ? Integer.MAX_VALUE : DiaryUtil.eligibleFor(RADAS_BLESSING2, player) ? 5 : DiaryUtil.eligibleFor(RADAS_BLESSING1, player) ? 3 : 0;
        final int mountKaruulmTeleports = vars.getMountKaruulmTeleports();
        final int mountKaruulmTeleportsLimit = DiaryUtil.eligibleFor(RADAS_BLESSING4, player) ? Integer.MAX_VALUE : DiaryUtil.eligibleFor(RADAS_BLESSING3, player) ? 3 : 0;
        final int grappleSearches = vars.getGrappleAndCrossbowSearches();
        final int grappleSearchesLimit = player.getSkills().getLevelForXp(SkillConstants.FLETCHING) < 99 ? 0 : 3;
        final ImmutableList<String> entries = ImmutableList.<String>builder().add("Zulrah resurrection: " + (resurrectable ? green.wrap("Available") : red.wrap("Unavailable"))).add("Zaff\'s battlestaves: " + (zaffsBattlestaves ? green.wrap("Available") : red.wrap("Unavailable"))).add("Fountain of Rune teleport: " + (fountainTeleports == fountainTeleportsLimit ? red.wrap("Unavailable") : green.wrap("Available"))).add("Teletab purchases: " + (teletabPurchases >= 1000 ? red.wrap("Unavailable") : green.wrap(teletabPurchases + "/" + 1000))).add("Spellbook swaps: " + (spellbookSwapLimit == 0 ? red.wrap("Unavailable") : (spellbookSwaps == spellbookSwapLimit ? red : green).wrap(spellbookSwaps + "/" + spellbookSwapLimit))).add("CoX Party advertisements: " + (partyAdvertisements == 0 ? red.wrap("Unavailable") : green.wrap((15 - partyAdvertisements) + "/" + 15))).add("Ardougne Farm teleports: " + (ardougneTeleportsLimit == 0 ? red.wrap("Unavailable") : ardougneTeleportsLimit == Integer.MAX_VALUE ? green.wrap("Available") : (ardougneTeleports == ardougneTeleportsLimit ? red : green).wrap(ardougneTeleports + "/" + ardougneTeleportsLimit))).add("Fishing Colony teleports: " + (fishingColonyTeleportsLimit == 0 ? red.wrap("Unavailable") : fishingColonyTeleportsLimit == Integer.MAX_VALUE ? green.wrap("Available") : (fishingColonyTeleports == fishingColonyTeleportsLimit ? red : green).wrap(fishingColonyTeleports + "/" + fishingColonyTeleportsLimit))).add("Sherlock teleports: " + (sherlockTeleportsLimit == 0 ? red.wrap("Unavailable") : sherlockTeleportsLimit == Integer.MAX_VALUE ? green.wrap("Available") : (sherlockTeleports == sherlockTeleportsLimit ? red : green).wrap(sherlockTeleports + "/" + sherlockTeleportsLimit))).add("Rellekka teleports: " + (rellekkaTeleportsLimit == 0 ? red.wrap("Unavailable") : rellekkaTeleportsLimit == Integer.MAX_VALUE ? green.wrap("Available") : (rellekkaTeleports == rellekkaTeleportsLimit ? red : green).wrap(rellekkaTeleports + "/" + rellekkaTeleportsLimit))).add("Cabbage field teleports: " + (cabbageTeleportsLimit == 0 ? red.wrap("Unavailable") : cabbageTeleportsLimit == Integer.MAX_VALUE ? green.wrap("Available") : (cabbageTeleports == cabbageTeleportsLimit ? red : green).wrap(cabbageTeleports + "/" + cabbageTeleportsLimit))).add("Nardah teleports: " + (nardahTeleportsLimit == 0 ? red.wrap("Unavailable") : nardahTeleportsLimit == Integer.MAX_VALUE ? green.wrap("Available") : (nardahTeleports == nardahTeleportsLimit ? red : green).wrap(nardahTeleports + "/" + nardahTeleportsLimit))).add("Kourend Woodlands teleports: " + (kourendWoodlandsTeleportsLimit == 0 ? red.wrap("Unavailable") : kourendWoodlandsTeleportsLimit == Integer.MAX_VALUE ? green.wrap("Available") : (kourendWoodlandTeleports == kourendWoodlandsTeleportsLimit ? red : green).wrap(kourendWoodlandTeleports + "/" + kourendWoodlandsTeleportsLimit))).add("Mount Karuulm teleports: " + (mountKaruulmTeleportsLimit == 0 ? red.wrap("Unavailable") : mountKaruulmTeleportsLimit == Integer.MAX_VALUE ? green.wrap("Available") : (mountKaruulmTeleports == mountKaruulmTeleportsLimit ? red : green).wrap(mountKaruulmTeleports + "/" + mountKaruulmTeleportsLimit))).add("Falador shield restores: " + (faladorShieldRechargeLimit == 0 ? red.wrap("Unavailable") : faladorShieldRechargeLimit == Integer.MAX_VALUE ? green.wrap("Available") : (faladorShieldRecharge == faladorShieldRechargeLimit ? red : green).wrap(faladorShieldRecharge + "/" + faladorShieldRechargeLimit))).add("Explorer\'s ring energy replenish: " + (explorersRingCharges == explorersRingLimit ? red.wrap("Unavailable") : green.wrap(explorersRingCharges + "/" + explorersRingLimit))).add("Explorer\'s ring alchemy: " + (explorersRingAlchemy == explorersRingAlchemyLimit ? red.wrap("Unavailable") : green.wrap(explorersRingAlchemy + "/" + explorersRingAlchemyLimit))).add("Grapple and crossbow searches: " + (grappleSearches == grappleSearchesLimit ? red.wrap("Unavailable") : green.wrap(grappleSearches + "/" + grappleSearchesLimit))).build();
        final Calendar currentCalendar = Calendar.getInstance();
        final int hoursRemaining = 24 - Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        Diary.sendJournal(player, "Daily board | Approx. hours until reset: " + hoursRemaining, new ArrayList<>(entries));
    }

    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DAILY_BOARD };
    }
}
