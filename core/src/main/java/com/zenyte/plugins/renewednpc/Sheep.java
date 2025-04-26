package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.plugins.dialogue.PlayerChat;

/**
 * @author Kris | 25/11/2018 16:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Sheep extends NPCPlugin {

    private static final Item SHEARS = new Item(1735);

    private static final Item WOOL = new Item(1737);

    private static final Animation SHEAR = new Animation(893);

    private static final Animation WADDLE = new Animation(3570);

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new PlayerChat(player, "That's a sheep...I" + " think. I can't talk to sheep.")));
        bind("Shear", (player, npc) -> {
            if (!player.getInventory().containsItem(SHEARS)) {
                player.sendMessage("You need a pair of shears to do this.");
                return;
            }
            player.faceEntity(npc);
            player.setAnimation(SHEAR);
            player.sendSound(761);
            player.lock();
            npc.lock();
            if (npc.getId() == NpcId.SHEEP) {
                WorldTasksManager.schedule(new WorldTask() {

                    private int ticks;

                    @Override
                    public void run() {
                        if (ticks == 0) {
                            npc.setAnimation(WADDLE);
                        } else if (ticks == 1) {
                            player.lock(1);
                            npc.addWalkSteps(npc.getX() - 3, npc.getY() - 3);
                            player.sendFilteredMessage("The... whatever it is... manages to get away from you!");
                        } else if (ticks == 2) {
                            stop();
                        }
                        ticks++;
                    }
                }, 0, 0);
                return;
            }
            WorldTasksManager.schedule(new WorldTask() {

                private int ticks;

                @Override
                public void run() {
                    if (ticks == 1) {
                        npc.setTransformation(2698);
                        player.sendFilteredMessage("You get some wool.");
                        player.getInventory().addItem(WOOL);
                    } else if (ticks == 2) {
                        player.lock(1);
                        npc.lock(1);
                        stop();
                    }
                    ticks++;
                }
            }, 0, 0);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                NpcId.SHEEP, NpcId.SHEEP_1299, NpcId.SHEEP_1300, NpcId.SHEEP_1301, NpcId.SHEEP_1302,
                NpcId.SHEEP_1303, NpcId.SHEEP_1304, NpcId.SHEEP_1308, NpcId.SHEEP_1309, NpcId.SHEEP_2691,
                NpcId.SHEEP_2692, NpcId.SHEEP_2693, NpcId.SHEEP_2694, NpcId.SHEEP_2695, NpcId.SHEEP_2696,
                NpcId.SHEEP_2697, NpcId.SHEEP_2698, NpcId.SHEEP_2699, NpcId.SHEEP_2786, NpcId.SHEEP_2787,
                NpcId.SHEEP_2788, NpcId.SHEEP_2789, NpcId.SHEEP_5726, NpcId.SHEEP_5843, NpcId.SHEEP_5844,
                NpcId.SHEEP_5845, NpcId.SHEEP_5846, NpcId.SHEEP_10594
        };
    }
}
