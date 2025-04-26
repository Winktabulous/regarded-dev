package com.zenyte.plugins.events;

import com.zenyte.game.world.WorldThread;
import com.zenyte.plugins.Event;

/**
 * @author Kris | 21/03/2019 23:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ServerLaunchEvent implements Event {

    private final WorldThread worldThread;

    public ServerLaunchEvent(WorldThread worldThread) {
        this.worldThread = worldThread;
    }

    public WorldThread getWorldThread() {
        return this.worldThread;
    }

}
