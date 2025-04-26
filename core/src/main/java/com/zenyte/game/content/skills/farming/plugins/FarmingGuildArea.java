package com.zenyte.game.content.skills.farming.plugins;

import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.kourend.GreatKourend;
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin;

/**
 * @author Kris | 09/02/2019 17:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FarmingGuildArea extends GreatKourend implements FullMovementPlugin {
    private static final RSPolygon intermediateSection = new RSPolygon(new int[][]{{1244, 3732}, {1243, 3732}, {1243, 3735}, {1238, 3740}, {1228, 3740}, {1223, 3735}, {1223, 3726}, {1229, 3720}, {1238, 3720}, {1243, 3725}, {1243, 3728}, {1244, 3728}});
    private static final RSPolygon advancedSection = new RSPolygon(new int[][]{{1226, 3766}, {1226, 3763}, {1222, 3763}, {1222, 3762}, {1221, 3762}, {1221, 3761}, {1220, 3761}, {1220, 3749}, {1221, 3749}, {1221, 3748}, {1222, 3748}, {1222, 3747}, {1226, 3747}, {1226, 3744}, {1232, 3744}, {1232, 3747}, {1237, 3747}, {1237, 3748}, {1238, 3748}, {1238, 3749}, {1239, 3749}, {1239, 3748}, {1241, 3746}, {1247, 3746}, {1247, 3745}, {1251, 3745}, {1251, 3746}, {1256, 3746}, {1259, 3749}, {1259, 3761}, {1256, 3764}, {1242, 3764}, {1239, 3761}, {1238, 3761}, {1238, 3762}, {1237, 3762}, {1237, 3763}, {1232, 3763}, {1232, 3766}});

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{{1226, 3766}, {1226, 3763}, {1222, 3763}, {1222, 3762}, {1221, 3762}, {1221, 3761}, {1220, 3761}, {1220, 3749}, {1221, 3749}, {1221, 3748}, {1222, 3748}, {1222, 3747}, {1226, 3747}, {1226, 3744}, {1232, 3744}, {1232, 3747}, {1237, 3747}, {1237, 3748}, {1238, 3748}, {1238, 3749}, {1239, 3749}, {1239, 3748}, {1241, 3746}, {1247, 3746}, {1247, 3744}, {1242, 3744}, {1242, 3738}, {1245, 3738}, {1245, 3732}, {1243, 3732}, {1243, 3735}, {1238, 3740}, {1228, 3740}, {1223, 3735}, {1223, 3726}, {1229, 3720}, {1238, 3720}, {1243, 3725}, {1243, 3728}, {1245, 3728}, {1245, 3723}, {1253, 3723}, {1253, 3728}, {1255, 3728}, {1255, 3726}, {1261, 3720}, {1270, 3720}, {1275, 3725}, {1275, 3735}, {1270, 3740}, {1267, 3740}, {1267, 3743}, {1269, 3745}, {1269, 3751}, {1268, 3752}, {1262, 3752}, {1261, 3751}, {1261, 3746}, {1263, 3744}, {1263, 3740}, {1260, 3740}, {1255, 3735}, {1255, 3732}, {1253, 3732}, {1253, 3738}, {1256, 3738}, {1256, 3744}, {1251, 3744}, {1251, 3746}, {1256, 3746}, {1259, 3749}, {1259, 3761}, {1256, 3764}, {1242, 3764}, {1239, 3761}, {1238, 3761}, {1238, 3762}, {1237, 3762}, {1237, 3763}, {1232, 3763}, {1232, 3766}})};
    }

    @Override
    public void enter(final Player player) {
        player.getAchievementDiaries().update(KourendDiary.ENTER_FARMING_GUILD);
    }

    @Override
    public String name() {
        return "Farming Guild";
    }

    @Override
    public boolean processMovement(Player player, int x, int y) {
        if (!intermediateSection.contains(player.getLocation()) && intermediateSection.contains(x, y)) {
            if (player.getSkills().getLevel(SkillConstants.FARMING) < 65) {
                player.sendMessage("You need a Farming level of at least 65 to enter the intermediate section of the " + "Farming guild.");
                player.resetWalkSteps();
                player.getPacketDispatcher().resetMapFlag();
                return false;
            }
        } else if (!advancedSection.contains(player.getLocation()) && advancedSection.contains(x, y)) {
            if (player.getSkills().getLevel(SkillConstants.FARMING) < 65) {
                player.sendMessage("You need a Farming level of at least 85 to enter the advanced section of the " +
                        "Farming guild.");
                player.resetWalkSteps();
                player.getPacketDispatcher().resetMapFlag();
                return false;
            }
        }

        return true;
    }
}
