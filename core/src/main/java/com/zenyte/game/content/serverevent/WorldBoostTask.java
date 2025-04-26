package com.zenyte.game.content.serverevent;

import com.google.common.eventbus.Subscribe;
import com.near_reality.tools.discord.community.DiscordBroadcastKt;
import com.near_reality.tools.discord.community.DiscordCommunityBot;
import com.zenyte.game.model.ui.testinterfaces.ServerEventsInterface;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.StaticInitializer;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

import static com.zenyte.game.model.ui.InterfaceHandler.Journal.SERVER_EVENTS;
@StaticInitializer
public class WorldBoostTask implements WorldTask {

    @Subscribe
    public static void on(ServerLaunchEvent event) {
        WorldTasksManager.schedule(new WorldBoostTask(), 1 , 0);
    }

    private int tick;

    @Override
    public void run() {
        tick++;

        if(World.getWorldBoosts().isEmpty())
            return;

        ObjectListIterator<WorldBoost> iterator = World.getWorldBoosts().iterator();

        boolean updated = false;
        while (iterator.hasNext()) {
            WorldBoost worldBoost = iterator.next();

            if(worldBoost.isExpired()) {
                WorldBroadcasts.sendMessage(worldBoost.getBoostType().getMssg()+" boost deactivated", BroadcastType.XAMPHUR, false);
                iterator.remove();
                updated = true;
                DiscordBroadcastKt.boostEnd(DiscordCommunityBot.INSTANCE, worldBoost);
            }
        }

        if(updated || tick % 50 == 0) {
            for (Player player : World.getPlayers()) {
                if (player.getInterfaceHandler().getJournal() == SERVER_EVENTS)
                    ServerEventsInterface.update(player);
            }
        }
    }
}
