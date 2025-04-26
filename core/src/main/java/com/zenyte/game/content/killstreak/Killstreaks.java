package com.zenyte.game.content.killstreak;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.content.well.WellPerk;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.MessageType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import org.apache.commons.lang3.StringUtils;

public class Killstreaks {
    public static final int BLOODMONEY_ITEM_ID = 13_307;

    public static void handlePlayerKill(Player attacker, Player victim) {
        if(!WildernessArea.isWithinWilderness(victim.getLocation().getX(), victim.getLocation().getY()))
            return;
        if(attacker.getIP().equalsIgnoreCase(victim.getIP()))
            return;

        if (attacker.getKillstreakLog().entryExists(victim)) {
            attacker.sendMessage("You recently killed " + victim.getName() + " and therefore did not receive any rewards for that kill..</col>");
            return;
        }

        int totalBloodmoney = getTotalBloodmoneyForKill(attacker, victim);
        if(World.hasBoost(XamphurBoost.BONUS_BLOOD_MONEY))
            totalBloodmoney *= 2;
        if (attacker.getVariables().getBloodMoneyBoosterLeft() > 0) {
            attacker.getVariables().setBloodMoneyBoosterLeft(attacker.getVariables().getBloodMoneyBoosterLeft() - 1);
            totalBloodmoney *= 1.25;
        }
        if(World.hasBoost(WellPerk.DOUBLE_BLOOD_MONEY))
            totalBloodmoney *= 2;
        int attackerCurrentStreak = PlayerAttributesKt.getPvpKillStreak(attacker);
        PlayerAttributesKt.setPvpKillStreak(attacker, attackerCurrentStreak + 1);

        attacker.sendMessage(
                "You have received <col=ff0000>" + totalBloodmoney + "</col> blood money. Your killstreak is now <col=ff0000>"
                        + (attackerCurrentStreak + 1) + "</col>.");
        attacker.getInventory().addOrDrop(new Item(BLOODMONEY_ITEM_ID, totalBloodmoney));

        PlayerAttributesKt.setPvpKills(attacker, PlayerAttributesKt.getPvpKills(attacker) + 1);
        PlayerAttributesKt.setPvpDeaths(victim, PlayerAttributesKt.getPvpDeaths(victim) + 1);

        attacker.getKillstreakLog().addEntry(victim);

        announceShutdown(attacker, victim);

        resetKillstreak(victim, true);
        announceKillstreak(attacker, attackerCurrentStreak + 1);
    }

    /**
     * Resets a players killstreak
     *
     * @param player - the player
     * @param announce - announce to the world
     */
    private static void resetKillstreak(Player player, boolean announce) {
        PlayerAttributesKt.setPvpKillStreak(player, 0);
    }

    /**
     * Globally announce killstreak to the whole world
     *
     * @param player - the player
     * @param newKillstreak
     */
    private static void announceKillstreak(Player player, int newKillstreak) {
        if (newKillstreak <= 0) return;

        if (newKillstreak % 5 == 0) {
            World.sendMessage(MessageType.FILTERABLE, "<img=9><col=ff0000> " + player.getName()
                    + " has a killstreak of " + newKillstreak +
                    " and can be shutdown for " + getKillstreakBoost(newKillstreak)
                    + " bonus Blood money!</col>");
        }
    }

    private static void announceShutdown(Player attacker, Player victim) {
        int victimsKillstreak = PlayerAttributesKt.getPvpKillStreak(victim);
        if (victimsKillstreak >= 5) {
            World.sendMessage(MessageType.FILTERABLE,
                    "<img=9><col=ff0000> " + attacker.getName()
                            + " has shut down " + victim.getName() +
                            " with a killing spree of " + victimsKillstreak + "!</col>");
        }
    }

    private static int getTotalBloodmoneyForKill(Player attacker, Player victim) {
        int victimStreak = PlayerAttributesKt.getPvpKillStreak(victim);
        int yourStreak = PlayerAttributesKt.getPvpKillStreak(attacker);

        int killstreakBoost = getKillstreakBoost(yourStreak);
        int rankBoost = getRankBoost(attacker.getMemberRank());
        int totalPercentBoost = killstreakBoost + rankBoost;

        int shutdownBonus = getBloodmoneyForShutdown(victimStreak);

        // Victims wilderness level
        final int level = WildernessArea.getWildernessLevel(victim.getLocation()).orElse(0);
        if (level == 0)
            return 0;
        return getBloodmoneyForKill((float) (totalPercentBoost * 1.2), level) + shutdownBonus;
    }

    private static final int getBloodmoneyForKill(float percentageBoost, int wildernessLevel) {
        final int wildernessLevelBase = 10;
        final int wildernessDivisions = (int) ((float) wildernessLevel / 10) * 3;

        int wildernessBloodMoney = wildernessLevelBase + wildernessDivisions;
        int amountToAdd = (int)((float)(wildernessBloodMoney) / 100 * percentageBoost);

        wildernessBloodMoney += amountToAdd;

        int finalAmount = Math.min(wildernessBloodMoney, 50);
        return Utils.random( finalAmount - 4, finalAmount);
    }

    /**
     * Get's the percentage boost from killstreak
     *
     * @param killstreak - the killstreak boost
     * @return - the percent of boost you get for specific killstreaks
     */
    private static int getKillstreakBoost(int killstreak) {
        if (killstreak <= 0) return 0;
        if (killstreak >= 21) return 30;

        int baseBoost = 5;
        int currentStreak = killstreak;

        // every 5 we add get 5%, with a maximum of 30
        int currentBoost = Math.min((currentStreak / 5) * 5, 25);
        baseBoost += currentBoost;

        return baseBoost;
    }

    private static int getBloodmoneyForShutdown(int killstreak) {
        if (killstreak <= 5) return 0;
        return Math.min(((killstreak / 5) * 5) + 5, 30);
    }


    private static int getRankBoost(MemberRank rank) {
        switch (rank) {
            case PREMIUM: return 5;
            case EXPANSION: return 10;
            case EXTREME: return 15;
            case RESPECTED: return 20;
            case LEGENDARY: return 30;
            case MYTHICAL: return 40;
            case AMASCUT:
            case UBER: {
                return 50;
            }
        }
        return 0;
    }

    private static String getChatIconForKillstreak(int killstreak) {
        if (killstreak >= 5 && killstreak <= 10) {
            return "<img=9>";
        }
        if (killstreak >= 11 && killstreak <= 15) {
            return "<img=10>";
        }
        if (killstreak >= 16 && killstreak <= 20) {
            return "<img=11>";
        }
        if (killstreak >= 21 && killstreak <= 25) {
            return "<img=12>";
        }
        if (killstreak >= 26) {
            return "<img=13>";
        }
        return "";
    }

}
