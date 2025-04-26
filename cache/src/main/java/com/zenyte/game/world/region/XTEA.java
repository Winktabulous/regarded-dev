package com.zenyte.game.world.region;

public final class XTEA {

    private final int mapsquare;
    private final int[] key;

    public XTEA(final int mapsquare, final int[] key) {
        this.mapsquare = mapsquare;
        this.key = key;
    }

    public int getMapsquare() {
        return mapsquare;
    }

    public int[] getKey() {
        return key;
    }

}
