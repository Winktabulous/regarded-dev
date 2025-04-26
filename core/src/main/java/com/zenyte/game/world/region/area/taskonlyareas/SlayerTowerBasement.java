package com.zenyte.game.world.region.area.taskonlyareas;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.SlayerTower;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 26/01/2019 22:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SlayerTowerBasement extends SlayerTower implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{3396, 9978}, {3396, 9924}, {3450, 9924}, {3450, 9978}}, 3)};
    }

    @Override
    public String name() {
        return "Slayer tower basement";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            final String name = ((NPC) entity).getDefinitions().getName();
            if (!player.getSlayer().isCurrentAssignment(entity)) {
                player.getDialogueManager().start(new Dialogue(player, 5042) {
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
