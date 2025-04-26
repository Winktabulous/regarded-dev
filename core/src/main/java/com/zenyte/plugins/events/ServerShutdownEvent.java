package com.zenyte.plugins.events;

import com.zenyte.plugins.Event;

public class ServerShutdownEvent implements Event {
    public ServerShutdownEvent() {
    }

    @Override
    public String toString() {
        return "ServerShutdownEvent()";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerShutdownEvent)) return false;
        final ServerShutdownEvent other = (ServerShutdownEvent) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ServerShutdownEvent;
    }

    @Override
    public int hashCode() {
        final int result = 1;
        return result;
    }
}
