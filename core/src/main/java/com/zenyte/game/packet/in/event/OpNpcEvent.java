package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCHandler;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 19:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpNpcEvent implements ClientProtEvent {
    private final int index;
    private final int option;
    private final boolean run;

    @Override
    public void log(@NotNull final Player player) {
        final NPC npc = World.getNPCs().get(index);
        if (npc == null) {
            log(player, "Index: " + index + ", option: " + option + ", run: " + run + "; null");
            return;
        }
        final Location tile = npc.getLocation();
        log(player, "Index: " + index + ", option: " + option + ", run: " + run + "; id: " + npc.getId() + ", name: " + npc.getName(player) + ", x: " + tile.getX() + ", y: " + tile.getY() + ", z: " + tile.getPlane());
    }

    @Override
    public void handle(Player player) {
        final NPC npc = World.getNPCs().get(index);
        if (npc != null) {
            NPCHandler.handle(player, npc, run, option);
        }
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public OpNpcEvent(int index, int option, boolean run) {
        this.index = index;
        this.option = option;
        this.run = run;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}
