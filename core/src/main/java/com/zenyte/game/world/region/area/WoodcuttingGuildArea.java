package com.zenyte.game.world.region.area;

import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.kourend.GreatKourend;

/**
 * @author Kris | 19/01/2019 01:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WoodcuttingGuildArea extends GreatKourend {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{{1563, 3497}, {1563, 3478}, {1564, 3477}, {1582, 3477}, {1587, 3472}, {1595, 3472}, {1596, 3473}, {1596, 3480}, {1601, 3485}, {1601, 3497}, {1607, 3497}, {1607, 3492}, {1612, 3487}, {1617, 3487}, {1623, 3493}, {1632, 3493}, {1633, 3492}, {1633, 3490}, {1634, 3489}, {1648, 3489}, {1656, 3497}, {1656, 3502}, {1657, 3502}, {1658, 3503}, {1658, 3507}, {1655, 3510}, {1655, 3516}, {1654, 3517}, {1633, 3517}, {1631, 3519}, {1624, 3519}, {1621, 3516}, {1612, 3516}, {1610, 3514}, {1608, 3514}, {1607, 3513}, {1607, 3511}, {1604, 3511}, {1604, 3506}, {1607, 3506}, {1607, 3501}, {1601, 3501}, {1601, 3503}, {1600, 3504}, {1582, 3504}, {1577, 3499}, {1565, 3499}})};
    }

    @Override
    public void enter(final Player player) {
        player.getAchievementDiaries().update(KourendDiary.ENTER_WOODCUTTING_GUILD);
    }

    @Override
    public String name() {
        return "Woodcutting Guild";
    }
}
