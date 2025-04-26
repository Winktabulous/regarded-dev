package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 3/21/2020
 */
public class HoopSnakeOnTarMonster implements ItemOnNPCAction {
    public static final Graphics stunnedGfx = new Graphics(80, 0, 140);
    private static final Animation despawningAnim = new Animation(7676);

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        player.setAnimation(Animation.LADDER_DOWN);
        player.getInventory().deleteItem(item);
        npc.lock(20);
        npc.setTransformation(NpcId.PASSIVE_TAR_MONSTER);
        npc.setGraphics(stunnedGfx);
        WorldTasksManager.schedule(() -> npc.setAnimation(despawningAnim), 19);
        WorldTasksManager.schedule(() -> {
            npc.setId(NpcId.TAR_MONSTER);
            npc.finish();
        }, 20);
    }

    @Override
    public Object[] getItems() {
        return new Object[]{ItemId.HOOP_SNAKE};
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{NpcId.TAR_MONSTER};
    }
}
