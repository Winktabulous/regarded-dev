package com.zenyte.game.content.object;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public enum OpenStorageDefinitions {

    CHEST_25387(25387, 25388),
    ;

    private final int closed;

    private final int open;

    OpenStorageDefinitions(final int closed, final int open) {
        this.closed = closed;
        this.open = open;
    }

    public int getClosed() {
        return closed;
    }

    public int getOpen() {
        return open;
    }
}
