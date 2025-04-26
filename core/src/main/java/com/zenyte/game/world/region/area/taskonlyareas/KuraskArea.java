package com.zenyte.game.world.region.area.taskonlyareas;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.FremennikSlayerDungeon;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 26/01/2019 22:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KuraskArea extends FremennikSlayerDungeon implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2699, 9984}, {2685, 9973}, {2695, 9952}, {2719, 9949}, {2726, 9971}, {2717, 9984}, {2705, 9988}}, 0)};
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Kurasks: Task only";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            final String name = ((NPC) entity).getDefinitions().getName();
            if (name.equalsIgnoreCase("Kurask")) {
                if (!player.getSlayer().isCurrentAssignment(entity)) {
                    player.getDialogueManager().start(new Dialogue(player, 7518) {
                        @Override
                        public void buildDialogue() {
                            npc("You can only kill these Kurasks while on a slayer task!");
                        }
                    });
                    return false;
                }
            }
        }
        return true;
    }
}
