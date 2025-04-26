package com.zenyte.game.world.entity.npc.combatdefs;

import java.util.Arrays;

/**
 * @author Kris | 18/11/2018 02:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StatDefinitions {
    int[] combatStats = new int[5];
    int[] aggressiveStats = new int[5];
    int[] defensiveStats = new int[5];
    int[] otherBonuses = new int[3];

    int[] getArray(final StatType type) {
        final int index = type.ordinal() / 5;
        return index == 0 ? combatStats : index == 1 ? aggressiveStats : index == 2 ? defensiveStats : otherBonuses;
    }

    public void set(final StatType type, final int value) {
        getArray(type)[type.index()] = value;
    }

    public int get(final StatType type) {
        return getArray(type)[type.index()];
    }

    public StatDefinitions clone() {
        final StatDefinitions defs = new StatDefinitions();
        defs.combatStats = Arrays.copyOf(combatStats, combatStats.length);
        defs.aggressiveStats = Arrays.copyOf(aggressiveStats, aggressiveStats.length);
        defs.defensiveStats = Arrays.copyOf(defensiveStats, defensiveStats.length);
        defs.otherBonuses = Arrays.copyOf(otherBonuses, otherBonuses.length);
        return defs;
    }

    public int getAggressiveStat(final StatType type) {
        return aggressiveStats[type.ordinal()];
    }

    public int[] getCombatStats() {
        return combatStats;
    }

    public void setCombatStats(int[] combatStats) {
        this.combatStats = combatStats;
    }

    public int[] getAggressiveStats() {
        return aggressiveStats;
    }

    public void setAggressiveStats(int[] aggressiveStats) {
        this.aggressiveStats = aggressiveStats;
    }

    public int[] getDefensiveStats() {
        return defensiveStats;
    }

    public void setDefensiveStats(int[] defensiveStats) {
        this.defensiveStats = defensiveStats;
    }

    public int[] getOtherBonuses() {
        return otherBonuses;
    }

    public void setOtherBonuses(int[] otherBonuses) {
        this.otherBonuses = otherBonuses;
    }

    public int getAttackLevel() {
        return combatStats[0];
    }

    public int getStrengthLevel() {
        return combatStats[1];
    }

    public int getDefenceLevel() {
        return combatStats[2];
    }

    public int getMagicLevel() {
        return combatStats[3];
    }

    public int getRangedLevel() {
        return combatStats[4];
    }
}
