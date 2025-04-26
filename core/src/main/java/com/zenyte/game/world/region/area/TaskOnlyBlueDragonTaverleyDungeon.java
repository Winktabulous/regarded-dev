package com.zenyte.game.world.region.area;

import com.zenyte.game.content.skills.slayer.Assignment;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 10/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TaskOnlyBlueDragonTaverleyDungeon extends TaverleyDungeon implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2882, 9852}, {2882, 9796}, {2980, 9796}, {2980, 9852}}, 1)};
    }

    @Override
    public String name() {
        return "Taverley Underground: Blue Dragons(Task-Only)";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC && ((NPC) entity).getName(player).equalsIgnoreCase("blue dragon")) {
            final Assignment assignment = player.getSlayer().getAssignment();
            if (assignment == null || assignment.getTask() != RegularTask.BLUE_DRAGONS) {
                player.getDialogueManager().start(new Dialogue(player, NpcId.EVE) {
                    @Override
                    public void buildDialogue() {
                        npc("Hey, go and train somewhere else! I'm not having people mess with my dragons unless a " +
                                "Slayer Master's told them to do it.");
                    }
                });
                return false;
            }
        }
        return true;
    }
}
