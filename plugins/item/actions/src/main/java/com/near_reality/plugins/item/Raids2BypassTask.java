package com.near_reality.plugins.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class Raids2BypassTask implements WorldTask {
    private final Player player;
    public Raids2BypassTask(@NotNull Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if((boolean) player.getTemporaryAttributes().getOrDefault("TOB_pending_bypass", false)) {
            player.sendMessage("You already have activated an orb for this raid.");
            stop();
        } else {
            if(player.getInventory().deleteItem(ItemId.ORB_OF_BLOOD, 1).getSucceededAmount() == 1) {
                player.lock(4);
                player.getTemporaryAttributes().put("TOB_bypass_hook", true);
                player.getTemporaryAttributes().put("TOB_pending_bypass", true);
            }
            stop();
        }
    }
}
