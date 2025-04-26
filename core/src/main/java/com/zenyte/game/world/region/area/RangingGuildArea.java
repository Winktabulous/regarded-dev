package com.zenyte.game.world.region.area;

import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

public class RangingGuildArea extends KingdomOfKandarin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 2651, 3430 }, { 2651, 3427 }, { 2667, 3411 }, { 2670, 3411 }, { 2686, 3427 }, { 2686, 3430 }, { 2670, 3446 }, { 2667, 3446 }, { 2661, 3440 }, { 2660, 3440 }, { 2657, 3437 }, { 2657, 3436 }}, 0) };
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        player.getAchievementDiaries().update(KandarinDiary.ENTER_RANGING_GUILD);
    }

    @Override
    public String name() {
        return "Ranging guild";
    }
}
