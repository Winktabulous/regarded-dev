package com.zenyte.game.content.treasuretrails.coordinateutils;

import com.google.common.base.Preconditions;

public final class SextantPosition {
    static final int BASE_FRAME_POSITION = 934;
    static final int BASE_ARM_POSITION = 949;
    static final int FRAME_POSITIONS_COUNT = 15;
    static final int ARM_POSITIONS_COUNT = 58;

    SextantPosition(final int framePosition, final int armPosition) {
        Preconditions.checkArgument(framePosition >= 0);
        Preconditions.checkArgument(armPosition >= 0);
        Preconditions.checkArgument(framePosition < FRAME_POSITIONS_COUNT);
        Preconditions.checkArgument(armPosition < ARM_POSITIONS_COUNT);
        this.framePosition = framePosition;
        this.armPosition = armPosition;
    }

    private final int framePosition;
    private final int armPosition;

    public int getFramePosition() {
        return framePosition;
    }

    public int getArmPosition() {
        return armPosition;
    }

    @Override
    public String toString() {
        return "SextantPosition(framePosition=" + this.getFramePosition() + ", armPosition=" + this.getArmPosition() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SextantPosition)) return false;
        final SextantPosition other = (SextantPosition) o;
        if (this.getFramePosition() != other.getFramePosition()) return false;
        return this.getArmPosition() == other.getArmPosition();
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getFramePosition();
        result = result * PRIME + this.getArmPosition();
        return result;
    }
}
