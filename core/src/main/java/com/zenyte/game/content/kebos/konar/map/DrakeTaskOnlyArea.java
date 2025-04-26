package com.zenyte.game.content.kebos.konar.map;

import com.zenyte.game.content.kebos.konar.npc.Drake;
import com.zenyte.game.content.skills.slayer.Assignment;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.content.skills.slayer.SlayerMaster;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Tommeh | 24/10/2019 | 20:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class DrakeTaskOnlyArea extends KaruulmSlayerDungeon implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{1337, 10241}, {1337, 10237}, {1340, 10237}, {1340, 10230}, {1341, 10230}, {1341, 10229}, {1344, 10229}, {1344, 10226}, {1345, 10226}, {1345, 10225}, {1349, 10225}, {1349, 10226}, {1350, 10226}, {1350, 10229}, {1350, 10229}, {1353, 10229}, {1353, 10230}, {1354, 10230}, {1354, 10237}, {1361, 10237}, {1361, 10238}, {1362, 10238}, {1362, 10246}, {1361, 10246}, {1361, 10247}, {1350, 10247}, {1350, 10252}, {1349, 10252}, {1349, 10253}, {1345, 10253}, {1345, 10252}, {1344, 10252}, {1344, 10249}, {1341, 10249}, {1341, 10248}, {1340, 10248}, {1340, 10241}}, 1)};
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof Drake) {
            final Assignment assignment = player.getSlayer().getAssignment();
            if (assignment == null || assignment.getTask() != RegularTask.DRAKES) {
                player.getDialogueManager().start(new Dialogue(player, SlayerMaster.KONAR_QUO_MATEN.getNpcId()) {
                    @Override
                    public void buildDialogue() {
                        npc("You can only kill Drakes in this area if you're assigned to kill them.");
                    }
                });
                return false;
            }
        }
        return true;
    }

    @Override
    public String name() {
        return "Karuulm Slayer Dungeon (Drake Task-only)";
    }
}
