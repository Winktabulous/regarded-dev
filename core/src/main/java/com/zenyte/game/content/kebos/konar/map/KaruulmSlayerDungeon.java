package com.zenyte.game.content.kebos.konar.map;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;

/**
 * @author Tommeh | 14/10/2019 | 19:47
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class KaruulmSlayerDungeon extends PolygonRegionArea implements CycleProcessPlugin {
    //TODO apply cannon restriction plugin for alchemical hydra instance
    private static final int BOOTS_OF_STONE = 23037;
    private static final int BOOTS_OF_BRIMSTONE = 22951;
    private static final int GRANITE_BOOTS = 21643;

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{1408, 10304}, {1408, 10240}, {1373, 10240}, {1374, 10176}, {1344, 10176}, {1344, 10112}, {1216, 10112}, {1216, 10304}})};
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public void process() {
        for (final Player player : players) {
            if (player == null || isProtectedAgainstHeat(player) || GlobalAreaManager.getArea(KaruulmSlayerDungeonLobby.class).getPlayers().contains(player)) {
                continue;
            }
            player.applyHit(new Hit(player, 4, HitType.REGULAR).setExecuteIfLocked());
        }
    }

    public static boolean isProtectedAgainstHeat(final Player player) {
        if (player.getBooleanTemporaryAttribute("karuulm_slayer_dungeon_heat_protection")) {
            return true;
        }
        if (DiaryUtil.eligibleFor(DiaryReward.RADAS_BLESSING4, player)) {
            return true;
        }
        if(player.getMemberRank().equalToOrGreaterThan(MemberRank.RESPECTED))
            return true;

        final Item boots = player.getBoots();
        final int id = boots == null ? -1 : boots.getId();
        return (id == BOOTS_OF_STONE || id == BOOTS_OF_BRIMSTONE || id == GRANITE_BOOTS);
    }

    @Override
    public String name() {
        return "Karuulm Slayer Dungeon";
    }
}
