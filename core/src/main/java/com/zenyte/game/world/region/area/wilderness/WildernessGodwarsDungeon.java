package com.zenyte.game.world.region.area.wilderness;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 20/04/2019 20:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WildernessGodwarsDungeon extends WildernessArea {

    public static final RSPolygon polygon = new RSPolygon(new int[][]{
            { 3008, 10176 },
            { 3008, 10112 },
            { 3072, 10112 },
            { 3072, 10176}
    });

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                polygon
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getAchievementDiaries().update(WildernessDiary.ENTER_WILDERNESS_GODWARS_DUNGEON);
    }

    @Override
    public String name() {
        return "Wilderness Godwars Dungeon";
    }
}
