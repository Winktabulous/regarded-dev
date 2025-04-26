package com.zenyte.game.world.entity;

public enum Entities {
    ;

    public static int getRoundedDirection(final int baseDirection, final int offset) {
        final int direction = (baseDirection + offset) & 2047;
        if (direction < 128 || direction >= 1920) {
            return 6;
        } else if (direction < 384) {
            return 5;
        } else if (direction < 640) {
            return 3;
        } else if (direction < 896) {
            return 0;
        } else if (direction < 1152) {
            return 1;
        } else if (direction < 1408) {
            return 2;
        } else if (direction < 1664) {
            return 4;
        } else {
            return 7;
        }
    }

}
