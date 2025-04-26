package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 15/04/2019 17:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChasmOfFire extends PolygonRegionArea implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{1408, 10112}, {1408, 10048}, {1472, 10048}, {1472, 10112}})};
    }

    @Override
    public void enter(final Player player) {
    }

    @Override
    public void leave(final Player player, final boolean logout) {
    }

    @Override
    public boolean attack(final Player player, final Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            final String name = ((NPC) entity).getDefinitions().getName();
            if (!player.getSlayer().isCurrentAssignment(entity)) {
                player.sendMessage("You can only kill " + name + "s while you're on a slayer task.");
                return false;
            }
        }
        return true;
    }

    @Override
    public String name() {
        return "Chasm of Fire";
    }
}
