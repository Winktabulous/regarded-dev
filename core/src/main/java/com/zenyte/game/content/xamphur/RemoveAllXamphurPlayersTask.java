package com.zenyte.game.content.xamphur;

import com.zenyte.game.content.donation.HomeTeleport;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.GlobalAreaManager;

public class RemoveAllXamphurPlayersTask extends TickTask {
    @Override
    public void run() {
        if(XamphurHandler.get().isXamphurActive())
            stop();

        switch(ticks) {
            case 100 -> {
                for(Player player: GlobalAreaManager.getArea(XamphurArea.class).getPlayers()) {
                    if(player != null && !player.isNulled() && !player.isStaff()) {
                        player.sendMessage("You will be sent home in 120 seconds as the event has concluded.");
                    }
                }
            }
            case 200 -> {
                for(Player player: GlobalAreaManager.getArea(XamphurArea.class).getPlayers()) {
                    if(player != null && !player.isNulled() && !player.isStaff()) {
                        player.sendMessage("You will be sent home in 60 seconds as the event has concluded.");
                    }
                }
            }
            case 300 -> {
                for(Player player: GlobalAreaManager.getArea(XamphurArea.class).getPlayers()) {
                    if(player != null && !player.isNulled() && !player.isStaff()) {
                        player.teleport(HomeTeleport.HOME.getLocation());
                    }
                }
                stop();
            }
        }
        ticks++;
    }
}
