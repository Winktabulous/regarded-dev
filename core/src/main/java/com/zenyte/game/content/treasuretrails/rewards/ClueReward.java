package com.zenyte.game.content.treasuretrails.rewards;

import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.treasuretrails.ClueLevel;

import java.util.Objects;

/**
 * @author Kris | 25/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClueReward {

    private static final ClueRewardTable beginnerTable = new BeginnerRewardsTable();
    private static final ClueRewardTable easyTable = new EasyRewardsTable();
    private static final ClueRewardTable mediumTable = new MediumRewardsTable();
    private static final ClueRewardTable hardTable = new HardRewardsTable();
    private static final ClueRewardTable eliteTable = new EliteRewardsTable();
    private static final ClueRewardTable masterTable = new MasterRewardsTable();

    static {
        beginnerTable.calculate();
        easyTable.calculate();
        mediumTable.calculate();
        hardTable.calculate();
        eliteTable.calculate();
        masterTable.calculate();
    }

    public static final ClueRewardTable getTable(final int casket) {
        return Objects.requireNonNull(ClueItem.getMap().get(casket)).getLevel().getTable();
    }

    public static final ClueLevel getTier(final int casket) {
        return Objects.requireNonNull(ClueItem.getMap().get(casket)).getLevel();
    }

    public static ClueRewardTable getBeginnerTable() {
        return beginnerTable;
    }

    public static ClueRewardTable getEasyTable() {
        return easyTable;
    }

    public static ClueRewardTable getMediumTable() {
        return mediumTable;
    }

    public static ClueRewardTable getHardTable() {
        return hardTable;
    }

    public static ClueRewardTable getEliteTable() {
        return eliteTable;
    }

    public static ClueRewardTable getMasterTable() {
        return masterTable;
    }

}
