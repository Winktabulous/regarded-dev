package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 19:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class WindowStatusEvent implements ClientProtEvent {

    private final int mode, width, height;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Mode: " + mode + ", width: " + width + ", height: " + height);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        if ((width > 781 && height > 541 && player.getTemporaryAttributes().get("welcomeScreen") != null) || player.getPlayerInformation().getMode() == mode) {
            return;
        }
        player.getInterfaceHandler().setResizable(mode == 2);
        player.getPlayerInformation().setMode(mode);
        //player.getInterfaceHandler().sendWelcomeScreen();
        player.getInterfaceHandler().sendGameFrame();
    }

    public WindowStatusEvent(int mode, int width, int height) {
        this.mode = mode;
        this.width = width;
        this.height = height;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}
