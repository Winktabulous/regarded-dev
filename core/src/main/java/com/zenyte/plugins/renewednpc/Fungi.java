package com.zenyte.plugins.renewednpc;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

import static com.zenyte.game.world.entity.npc.NpcId.*;

/**
 * @author Kris | 26/11/2018 18:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Fungi extends NPCPlugin {

    private static final Animation FIRST_ANIM = new Animation(3335);

    private static final Animation SECOND_ANIM = new Animation(3322);

    @Override
    public void handle() {
        bind("Pick", (player, npc) -> {
            if (npc.getId() != ANCIENT_FUGI_471 && npc.isLocked())
                return;

            final int id = npc.getId();
            final int transformed = switch (id) {
                case ANCIENT_FUGI_471 -> ANCIENT_ZYGOMITE;
                case 536 -> ZYGOMITE;
                case 1023 -> ZYGOMITE_1024;
                default -> -1;
            };

            if (transformed == -1)
                return;

            npc.lock();
            player.setAnimation(FIRST_ANIM);
            WorldTasksManager.schedule(new WorldTask() {

                int ticks = 0;

                @Override
                public void run() {
                    if (ticks == 0) {
                        npc.setTransformation(transformed);
                        npc.setId(id);
                        npc.setAnimation(SECOND_ANIM);
                        npc.setAttackingDelay(System.currentTimeMillis());
                        npc.unlock();
                    }
                    else if (ticks == 1) {
                        npc.setId(transformed);
                        npc.setHitpoints(npc.getMaxHitpoints());
                        npc.getCombat().setTarget(player);
                        stop();
                    }
                    ticks++;
                }
            }, 0, 1);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {ANCIENT_FUGI_471, FUNGI, FUNGI_538, ANCIENT_FUNGI};
    }
}
