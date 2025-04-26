package com.zenyte.game.content.chambersofxeric;

import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.near_reality.game.item.CustomItemId.ORB_OF_XERIC;

public class Raids1BypassTask extends TickTask {

    private static final Logger logger = LoggerFactory.getLogger(Raids1BypassTask.class);

    private final Player player;

    public Raids1BypassTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if(player == null || player.isNulled() || player.isDead()) {
            logger.error("Exiting task of RaidOrbBypass due to nulled character");
            stop();
            return;
        }
        player.pendingRaidBypass = true;
        if(!(player.getArea() instanceof RaidArea)){
            unlockStopTask("You have left the raid somehow during this period.");
        }
        if(ticks <= 0 && player.getRaid().isEmpty()) {
            unlockStopTask("You are not in a valid raid session. Please try again.");
        }
        if(player.getRaid().get().getPlayers().size() != 1) {
            unlockStopTask("You can only use this when there are no other players in your party.");
        }
        if(ticks <= 0 && player.getRaid().get().isBypassed) {
            unlockStopTask("You have already activated an orb for this raid. This would be pointless.");
        }
        switch (ticks) {
            case 0 -> {
                if(player.getInventory().deleteItem(ORB_OF_XERIC, 1).getSucceededAmount() == 1) {
                    player.lock(4);
                } else {
                    unlockStopTask( "You failed to keep the orb in your fingers whilst activating it.");
                }
            }
            case 1 -> player.sendMessage("Please wait as we are modifying your experience.");
            case 2 -> player.getRaid().get().runBypassMode(player);
            case 3 -> finishTask();
            case 4 -> addSupplies();
            case 5 -> stop();
        }

        ticks++;
    }

    private void addSupplies() {
        player.getInventory().addOrDrop(new Item(ItemId.OVERLOAD_4_20988));
        player.getInventory().addOrDrop(new Item(ItemId.PRAYER_ENHANCE_4));
        player.getInventory().addOrDrop(new Item(ItemId.XERICS_AID_4, 3));
        player.getInventory().addOrDrop(new Item(ItemId.REVITALISATION_4, 3));
    }


    private void unlockStopTask(String msg) {
        player.sendMessage(msg);
        player.pendingRaidBypass = false;
        player.unlock();
        stop();
    }

    private void finishTask() {
        if(player.getRaid().isEmpty()) {
            player.sendMessage("Raid is empty apparently...");
            return;
        }
        player.sendDeveloperMessage("Completed Orb of Xeric bypass task");
        player.getRaid().get().isBypassed = true;
        player.pendingRaidBypass = false;
    }
}
