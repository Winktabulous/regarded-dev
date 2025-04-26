package com.zenyte.game.world.region.area.taskonlyareas;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 27/01/2019 16:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StrongholdSlayerDungeon extends PolygonRegionArea implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2423, 9843}, {2416, 9809}, {2396, 9802}, {2381, 9778}, {2402, 9757}, {2479, 9771}, {2503, 9832}, {2473, 9841}}, 0)};
    }

    @Override
    public void enter(Player player) {
        player.getTeleportManager().unlock(PortalTeleport.STRONGHOLD_SLAYER_CAVE);
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Stronghold Slayer Dungeon";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            final String name = ((NPC) entity).getDefinitions().getName();
            if (!player.getSlayer().isCurrentAssignment(entity)) {
                player.getDialogueManager().start(new Dialogue(player, 6797) {
                    @Override
                    public void buildDialogue() {
                        npc("You can only kill " + name + " while on a slayer task!");
                    }
                });
                return false;
            }
        }
        return true;
    }
}
