package com.zenyte.plugins.renewednpc;

import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 26/04/2019 23:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WildernessGodwarsDungeonBoulder extends NPCPlugin {

    @Override
    public void handle() {
        bind("Move", (player, npc) -> {
            if (npc.getTemporaryAttributes().containsKey("Moving lock")) {
                return;
            }
            if (player.getSkills().getLevelForXp(SkillConstants.STRENGTH) < 60) {
                player.getDialogueManager().start(new PlainChat(player, "You need a Strength level of at least 60 to lift the boulder."));
                return;
            }
            player.lock();
            npc.getTemporaryAttributes().put("Moving lock", true);
            player.setAnimation(new Animation(player.getX() >= 3055 ? 3065 : 6130));
            WorldTasksManager.schedule(new TickTask() {

                @Override
                public void run() {
                    switch(ticks++) {
                        case 1:
                            npc.addWalkSteps(3053, 10166, 1, false);
                            break;
                        case 3:
                            player.setFaceEntity(null);
                            player.addWalkSteps(player.getX() >= 3055 ? 3052 : 3055, 10165, 3, false);
                            break;
                        case 5:
                            npc.addWalkSteps(3053, 10165, 1, false);
                            break;
                        case 6:
                            player.unlock();
                            npc.getTemporaryAttributes().remove("Moving lock");
                            stop();
                            break;
                    }
                }
            }, 0, 0);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.BOULDER_6621 };
    }
}
