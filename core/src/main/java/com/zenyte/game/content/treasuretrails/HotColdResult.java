package com.zenyte.game.content.treasuretrails;

import com.zenyte.game.content.treasuretrails.challenges.HotColdChallenge;
import com.zenyte.game.item.Item;

public final class HotColdResult {
    private final int slotId;
    private final Item clue;
    private final HotColdChallenge challenge;
    private final int distance;
    private final DeviceTemperature temperature;

    public HotColdResult(int slotId, Item clue, HotColdChallenge challenge, int distance, DeviceTemperature temperature) {
        this.slotId = slotId;
        this.clue = clue;
        this.challenge = challenge;
        this.distance = distance;
        this.temperature = temperature;
    }

    public int getSlotId() {
        return slotId;
    }

    public Item getClue() {
        return clue;
    }

    public HotColdChallenge getChallenge() {
        return challenge;
    }

    public int getDistance() {
        return distance;
    }

    public DeviceTemperature getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "HotColdResult(slotId=" + this.getSlotId() + ", clue=" + this.getClue() + ", challenge=" + this.getChallenge() + ", distance=" + this.getDistance() + ", temperature=" + this.getTemperature() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof HotColdResult)) return false;
        final HotColdResult other = (HotColdResult) o;
        if (this.getSlotId() != other.getSlotId()) return false;
        if (this.getDistance() != other.getDistance()) return false;
        final Object this$clue = this.getClue();
        final Object other$clue = other.getClue();
        if (this$clue == null ? other$clue != null : !this$clue.equals(other$clue)) return false;
        final Object this$challenge = this.getChallenge();
        final Object other$challenge = other.getChallenge();
        if (this$challenge == null ? other$challenge != null : !this$challenge.equals(other$challenge)) return false;
        final Object this$temperature = this.getTemperature();
        final Object other$temperature = other.getTemperature();
        return this$temperature == null ? other$temperature == null : this$temperature.equals(other$temperature);
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getSlotId();
        result = result * PRIME + this.getDistance();
        final Object $clue = this.getClue();
        result = result * PRIME + ($clue == null ? 43 : $clue.hashCode());
        final Object $challenge = this.getChallenge();
        result = result * PRIME + ($challenge == null ? 43 : $challenge.hashCode());
        final Object $temperature = this.getTemperature();
        result = result * PRIME + ($temperature == null ? 43 : $temperature.hashCode());
        return result;
    }
}
