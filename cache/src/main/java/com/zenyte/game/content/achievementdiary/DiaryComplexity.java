package com.zenyte.game.content.achievementdiary;


import mgi.utilities.StringFormatUtil;

public enum DiaryComplexity {

    EASY, MEDIUM, HARD, ELITE;

    public static final DiaryComplexity[] VALUES = values();

    @Override
    public String toString() {
        return StringFormatUtil.formatString(name());
    }
}
