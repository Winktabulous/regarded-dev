package com.zenyte.game.content.treasuretrails.challenges;

import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.clues.emote.ItemRequirement;
import com.zenyte.game.world.entity.player.Emote;
import com.zenyte.game.world.region.RSPolygon;

import java.util.Arrays;
import java.util.List;

public final class EmoteRequest implements ClueChallenge {
    private final List<Emote> emotes;
    private final boolean agents;
    private final ItemRequirement[] requirements;
    private final RSPolygon polygon;
    private final ClueLevel level;

    public EmoteRequest(List<Emote> emotes, boolean agents, ItemRequirement[] requirements, RSPolygon polygon, ClueLevel level) {
        this.emotes = emotes;
        this.agents = agents;
        this.requirements = requirements;
        this.polygon = polygon;
        this.level = level;
    }

    public List<Emote> getEmotes() {
        return emotes;
    }

    public boolean isAgents() {
        return agents;
    }

    public ItemRequirement[] getRequirements() {
        return requirements;
    }

    public RSPolygon getPolygon() {
        return polygon;
    }

    public ClueLevel getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "EmoteRequest(emotes=" + this.getEmotes() + ", agents=" + this.isAgents() + ", requirements=" + Arrays.deepToString(this.getRequirements()) + ", polygon=" + this.getPolygon() + ", level=" + this.getLevel() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EmoteRequest)) return false;
        final EmoteRequest other = (EmoteRequest) o;
        if (this.isAgents() != other.isAgents()) return false;
        final Object this$emotes = this.getEmotes();
        final Object other$emotes = other.getEmotes();
        if (this$emotes == null ? other$emotes != null : !this$emotes.equals(other$emotes)) return false;
        if (!Arrays.deepEquals(this.getRequirements(), other.getRequirements())) return false;
        final Object this$polygon = this.getPolygon();
        final Object other$polygon = other.getPolygon();
        if (this$polygon == null ? other$polygon != null : !this$polygon.equals(other$polygon)) return false;
        final Object this$level = this.getLevel();
        final Object other$level = other.getLevel();
        return this$level == null ? other$level == null : this$level.equals(other$level);
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isAgents() ? 79 : 97);
        final Object $emotes = this.getEmotes();
        result = result * PRIME + ($emotes == null ? 43 : $emotes.hashCode());
        result = result * PRIME + Arrays.deepHashCode(this.getRequirements());
        final Object $polygon = this.getPolygon();
        result = result * PRIME + ($polygon == null ? 43 : $polygon.hashCode());
        final Object $level = this.getLevel();
        result = result * PRIME + ($level == null ? 43 : $level.hashCode());
        return result;
    }
}
