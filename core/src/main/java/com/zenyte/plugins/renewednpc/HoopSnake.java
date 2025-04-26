package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Christopher
 * @since 3/21/2020
 */
public class HoopSnake extends NPCPlugin {
    public static final Graphics stunnedGfx = new Graphics(80);

    @Override
    public void handle() {
        bind("Stun", (player, npc) -> {
            final boolean failed = Utils.random(4) != 0 || npc.hasWalkSteps();
            if (failed) {
                player.sendFilteredMessage("The movement of the snake causes you to miss.");
                return;
            }
            npc.setTransformation(NpcId.STUNNED_HOOP_SNAKE);
            npc.setGraphics(stunnedGfx);
            WorldTasksManager.schedule(() -> {
                npc.setId(NpcId.HOOP_SNAKE);
                npc.finish();
            }, 99);
        });
        bind("Pick-up", (player, npc) -> {
            final Inventory inventory = player.getInventory();
            if (!inventory.checkSpace()) {
                return;
            }
            player.setAnimation(Animation.LADDER_DOWN);
            npc.setId(NpcId.HOOP_SNAKE);
            npc.finish();
            inventory.addOrDrop(ItemId.HOOP_SNAKE, 1);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {NpcId.HOOP_SNAKE, NpcId.STUNNED_HOOP_SNAKE};
    }
}
