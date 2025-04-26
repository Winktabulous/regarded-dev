package com.zenyte.game.content.treasuretrails.challenges;

public class SongRequest implements ClueChallenge {
    private final String song;

    public SongRequest(String song) {
        this.song = song;
    }

    public String getSong() {
        return song;
    }

    @Override
    public String toString() {
        return "SongRequest(song=" + this.getSong() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SongRequest)) return false;
        final SongRequest other = (SongRequest) o;
        if (!other.canEqual(this)) return false;
        final Object this$song = this.getSong();
        final Object other$song = other.getSong();
        return this$song == null ? other$song == null : this$song.equals(other$song);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SongRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $song = this.getSong();
        result = result * PRIME + ($song == null ? 43 : $song.hashCode());
        return result;
    }
}
