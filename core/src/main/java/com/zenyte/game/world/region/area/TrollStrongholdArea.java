package com.zenyte.game.world.region.area;

import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

public class TrollStrongholdArea extends PolygonRegionArea {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{
                {2816, 10112},
                {2816, 10048},
                {2880, 10048},
                {2880, 10112}
        })};
    }

    @Override
    public void enter(Player player) {
        player.getAchievementDiaries().update(FremennikDiary.ENTER_TROLL_STRONGHOLD);
    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "Troll Stronghold";
    }
}
