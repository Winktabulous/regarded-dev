package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.plugins.object.OldFirePit;

/**
 * @author Kris | 24/01/2019 18:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GodwarsDungeonIceArea extends IcePlateauArea implements CycleProcessPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2942, 3839}, {2932, 3816}, {2926, 3797}, {2921, 3784}, {2917, 3777}, {2931, 3769}, {2938, 3764}, {2941, 3749}, {2940, 3739}, {2940, 3730}, {2936, 3721}, {2926, 3719}, {2923, 3723}, {2920, 3724}, {2915, 3719}, {2914, 3716}, {2911, 3717}, {2902, 3718}, {2885, 3714}, {2874, 3717}, {2819, 3728}, {2814, 3839}}, 0)};
    }

    @Override
    public String name() {
        return "Ice Plateau: Chilly side";
    }

    @Override
    public void process() {
        if (players.isEmpty()) return;
        for (final Player player : players) {
            if (OldFirePit.FirePit.GODWARS_DUNGEON_FIRE.isBuilt(player)) {
                continue;
            }
            int attr = player.getNumericTemporaryAttribute("ice plateau counter").intValue();
            if (attr++ >= 5) {
                attr = 0;
                for (int i = 0; i < SkillConstants.SKILLS.length; i++) {
                    if (i == SkillConstants.HITPOINTS) {
                        player.applyHit(new Hit(1, HitType.REGULAR));
                    } else {
                        player.drainSkill(i, 1);
                    }
                }
                player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() - 1);
            }
            player.getTemporaryAttributes().put("ice plateau counter", attr);
        }
    }
}
