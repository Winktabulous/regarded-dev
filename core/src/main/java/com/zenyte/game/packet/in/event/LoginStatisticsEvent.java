package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;

/**
 * Sends by client upon login.
 *
 * @author Stan van der Bend
 * @see com.zenyte.net.game.ClientProt#LOGIN_STATISTICS
 */
public class LoginStatisticsEvent implements ClientProtEvent {

    private final int field4237;
    private final int field4233;
    private final int field4238;
    private final int field4242;
    private final int field4240;
    private final int field4241;
    private final int field4236;

    public LoginStatisticsEvent(int field4237, int field4233, int field4238, int field4242, int field4240, int field4241, int field4236) {
        this.field4237 = field4237;
        this.field4233 = field4233;
        this.field4238 = field4238;
        this.field4242 = field4242;
        this.field4240 = field4240;
        this.field4241 = field4241;
        this.field4236 = field4236;
    }

    @Override
    public void handle(Player player) {
        player.log(
                LogLevel.INFO,
                String.format("LoginStatisticsEvent(%d, %d, %d, %d, %d, %d, %d)",
                        field4237, field4233, field4238,
                        field4242, field4240, field4241,
                        field4236
                )
        );
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }
}
