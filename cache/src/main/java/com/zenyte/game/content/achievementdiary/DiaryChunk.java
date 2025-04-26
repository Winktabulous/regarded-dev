package com.zenyte.game.content.achievementdiary;

public final class DiaryChunk {

    private final int varbit;
    private final int greenVarbit;

    public DiaryChunk(final int varbit, final int greenVarbit) {
        this.varbit = varbit;
        this.greenVarbit = greenVarbit;
    }

    public int getVarbit() {
        return varbit;
    }

    public int getGreenVarbit() {
        return greenVarbit;
    }

}