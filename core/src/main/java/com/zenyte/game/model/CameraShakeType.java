package com.zenyte.game.model;

/**
 * @author Kris | 4. dets 2017 : 14:10.51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum CameraShakeType {

    LEFT_AND_RIGHT(0),
    UP_AND_DOWN(1),
    FRONT_AND_BACK(2),
    STRONG_LEFT_AND_RIGHT(3),
    STRONG_UP_AND_DOWN(4);

    private final int type;

    CameraShakeType(final int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
