package com.zenyte.game;

import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

/**
 * @author Kris | 14/04/2019 13:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GameClock {

    private static final SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm zzz");
    private static final Logger log = LoggerFactory.getLogger(GameClock.class);
    private static String lastTime;
    private static Interface plugin;

    public static String gameTime() {
        return format.format(Date.from(Instant.now()));
    }

    /**
     * Game clock needs to be perfectly in synchronization with the real-time clock, so for that purpose we re-calculate it every tick - and if the time has changed, we refresh it
     * for all players.
     */
    public static void process() {
        final String time = gameTime();
        if (time.equals(lastTime)) {
            return;
        }
        final Interface plugin = getPlugin();
        if (plugin == null) {
            return;
        }
        lastTime = time;
        for (final Player player : World.getPlayers()) {
            if (player.isNulled()) {
                continue;
            }
            try {
                player.getPacketDispatcher().sendComponentText(plugin.getInterface(), plugin.getComponent("Time"), "Time: <col=ffffff>" + gameTime());
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    private static Interface getPlugin() {
        if (plugin != null) {
            return plugin;
        }
        GameInterface.GAME_NOTICEBOARD.getPlugin().ifPresent(anInterface -> plugin = anInterface);
        return plugin;
    }

}
