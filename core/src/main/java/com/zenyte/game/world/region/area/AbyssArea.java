package com.zenyte.game.world.region.area;

import com.zenyte.game.content.skills.runecrafting.abyss.AbyssObstacle;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import mgi.types.config.ObjectDefinitions;

import java.util.Objects;

/**
 * @author Kris | 19/04/2019 23:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AbyssArea extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{3008, 4864}, {3008, 4802}, {3072, 4802}, {3072, 4864}})};
    }

    @Override
    public void enter(final Player player) {
        for (final AbyssObstacle obstacle : AbyssObstacle.VALUES) {
            player.getVarManager().sendBit(Objects.requireNonNull(ObjectDefinitions.get(obstacle.getId())).getVarbitId(), 0);
        }
    }

    @Override
    public void leave(final Player player, final boolean logout) {
    }

    @Override
    public String name() {
        return "Abyss";
    }
}
