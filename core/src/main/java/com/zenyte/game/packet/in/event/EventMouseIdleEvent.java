package com.zenyte.game.packet.in.event;

import com.zenyte.game.GameConstants;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tommeh | 25-1-2019 | 20:07
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class EventMouseIdleEvent implements ClientProtEvent {

    private static final Logger log = LoggerFactory.getLogger(EventMouseIdleEvent.class);

    private static final boolean ENABLE_IDLE_LOGOUTS = false;

    @Override
    public void log(@NotNull final Player player) {
        this.log(player, "");
    }

    @Override
    public void handle(Player player) {
        player.getTemporaryAttributes().put("User deemed inactive", true);

        if (ENABLE_IDLE_LOGOUTS) {
            idleLogout(player);
        }
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    private void idleLogout(Player player) {
        if (player.getPrivilege().eligibleTo(PlayerPrivilege.SUPPORT)
                && !GameConstants.WORLD_PROFILE.isDevelopment()) {
            if (player.isLocked() || player.isUnderCombat() || player.getNumericTemporaryAttribute("staff timeout " +
                    "disabled").intValue() == 1) {
                return;
            }
            player.logout(false);
            log.info("Idle logout: " + player.getName());
        }
    }
}
