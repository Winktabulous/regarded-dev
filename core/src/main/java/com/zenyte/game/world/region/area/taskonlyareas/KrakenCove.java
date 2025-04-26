package com.zenyte.game.world.region.area.taskonlyareas;

import com.zenyte.game.content.skills.slayer.Assignment;
import com.zenyte.game.content.skills.slayer.BossTask;
import com.zenyte.game.content.skills.slayer.BossTaskSumona;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 26/01/2019 22:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KrakenCove extends PolygonRegionArea implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2235, 10053}, {2235, 9980}, {2308, 9980}, {2308, 10053}}, 0)};
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Kraken Cove";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            final int id = ((NPC) entity).getId();
            if (id == 492 || id == 493) {
                final Assignment assignment = player.getSlayer().getAssignment();
                if (assignment == null || assignment.getTask() != RegularTask.CAVE_KRAKEN) {
                    player.getDialogueManager().start(new Dialogue(player, 7412) {
                        @Override
                        public void buildDialogue() {
                            npc("You can only kill the Cave krakens while on a slayer task!");
                        }
                    });
                    return false;
                }
            } else if (id == 494 || id == 496) {
                final Assignment assignment = player.getSlayer().getAssignment();
                if (assignment != null && (assignment.getTask() == RegularTask.CAVE_KRAKEN || assignment.getTask() == BossTask.KRAKEN || assignment.getTask() == BossTaskSumona.KRAKEN_SUMONA)) {
                    return true;
                }
                player.getDialogueManager().start(new Dialogue(player, 7412) {
                    @Override
                    public void buildDialogue() {
                        npc("You can only kill the Krakens while on a slayer task!");
                    }
                });
                return false;
            }
        }
        return true;
    }
}
