package com.zenyte.game.world.region.area;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Tommeh | 5-11-2018 | 16:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MortMyreSwampArea extends Morytania implements CannonRestrictionPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 3477, 3468 }, { 3481, 3458 }, { 3482, 3449 }, { 3494, 3440 }, { 3489, 3412 }, { 3482, 3410 }, { 3482, 3388 }, { 3479, 3383 }, { 3486, 3375 }, { 3485, 3353 }, { 3475, 3325 }, { 3424, 3325 }, { 3397, 3365 }, { 3406, 3380 }, { 3400, 3391 }, { 3404, 3401 }, { 3402, 3407 }, { 3408, 3415 }, { 3402, 3427 }, { 3407, 3435 }, { 3403, 3445 }, { 3410, 3448 }, { 3415, 3456 }, { 3418, 3456 }, { 3419, 3457 }, { 3421, 3457 }, { 3423, 3459 }, { 3425, 3459 }, { 3427, 3457 }, { 3431, 3457 }, { 3432, 3456 }, { 3436, 3456 }, { 3437, 3457 }, { 3441, 3457 }, { 3442, 3458 }, { 3445, 3458 }, { 3447, 3456 }, { 3448, 3456 }, { 3449, 3457 }, { 3453, 3457 }, { 3454, 3456 }, { 3455, 3456 }, { 3459, 3460 }, { 3463, 3462 }, { 3466, 3466 }, { 3470, 3465 } }) };
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        player.getAchievementDiaries().update(MorytaniaDiary.ENTER_MORT_MYRE_SWAMP);
    }

    @Override
    public String name() {
        return "Mort Myre Swamp";
    }
}
