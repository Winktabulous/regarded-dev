package com.zenyte.game.content.bountyhunter;

public class SkipEntry {
    private final String username;
    private final long time;

    public SkipEntry(String username, long time) {
        this.username = username;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "SkipEntry(username=" + this.getUsername() + ", time=" + this.getTime() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SkipEntry)) return false;
        final SkipEntry other = (SkipEntry) o;
        if (!other.canEqual(this)) return false;
        if (this.getTime() != other.getTime()) return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        return this$username == null ? other$username == null : this$username.equals(other$username);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SkipEntry;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $time = this.getTime();
        result = result * PRIME + (int) ($time >>> 32 ^ $time);
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        return result;
    }
}
