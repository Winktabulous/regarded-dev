package com.zenyte.game.content.boss.smokedevil;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.YanilleUndergroundArea;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

/**
 * @author Kris | 23/10/2018 14:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ThermonuclearBossArea extends YanilleUndergroundArea implements CannonRestrictionPlugin, EntityAttackPlugin, LootBroadcastPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2354, 9460}, {2351, 9457}, {2351, 9455}, {2351, 9443}, {2354, 9440}, {2354, 9439}, {2355, 9438}, {2356, 9438}, {2357, 9439}, {2360, 9439}, {2361, 9438}, {2364, 9438}, {2365, 9439}, {2369, 9439}, {2370, 9438}, {2372, 9438}, {2373, 9439}, {2375, 9439}, {2376, 9440}, {2376, 9441}, {2377, 9442}, {2377, 9445}, {2378, 9446}, {2378, 9448}, {2377, 9449}, {2377, 9456}, {2376, 9457}, {2376, 9459}, {2375, 9460}, {2362, 9460}, {2361, 9461}, {2359, 9461}, {2358, 9460}}, 0)};
    }

    @Override
    public void enter(final Player player) {
    }

    @Override
    public void leave(final Player player, boolean logout) {
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            final String name = ((NPC) entity).getDefinitions().getName();
            if (name.equals("Thermonuclear smoke devil") && player.getKillcount((NPC) entity) > 0) {
                if (!player.getSlayer().isCurrentAssignment(entity)) {
                    player.sendMessage("You can only kill the Thermonuclear smoke devil while you\'re on a slayer task.");
                    return false;
                }
            } else if (name.equals("Smoke devil")) {
                if (!player.getSlayer().isCurrentAssignment(entity)) {
                    player.sendMessage("You can only kill Smoke devils while you\'re on a slayer task.");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String name() {
        return "Thermonuclear Boss Room";
    }
}
