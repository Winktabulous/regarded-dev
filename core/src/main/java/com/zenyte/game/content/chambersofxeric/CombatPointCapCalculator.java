package com.zenyte.game.content.chambersofxeric;

import com.zenyte.game.content.chambersofxeric.npc.RaidNPC;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Kris | 23/09/2019 02:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class CombatPointCapCalculator {
    /**
     * The number of points that have been appended in the builder.
     */
    private int points;

    /**
     * Appends points equal to 5 times the health of all the NPCs in the list passed to it.
     *
     * @param npcs the npcs in the list to append.
     * @return this for chaining.
     */
    public CombatPointCapCalculator appendNPCs(@NotNull final List<? extends RaidNPC<?>> npcs) {
        for (final RaidNPC<?> n : npcs) {
            if (n == null) {
                continue;
            }
            points += 5 * n.getMaxHitpoints();
        }
        return this;
    }

    /**
     * Appends points equal to 5 times the health of the NPCs passed to it.
     *
     * @param npc the npc to append.
     * @return this for chaining.
     */
    public CombatPointCapCalculator appendNPC(@NotNull final RaidNPC<?> npc) {
        points += 5 * npc.getMaxHitpoints();
        return this;
    }

    /**
     * Appends points equal to 5 times the health of the NPCs passed to it.
     *
     * @param npc the npc to append.
     * @param repetitions the number of iterations for the npc.
     * @return this for chaining.
     */
    public CombatPointCapCalculator appendNPC(@NotNull final RaidNPC<?> npc, final int repetitions) {
        points += 5 * npc.getMaxHitpoints() * repetitions;
        return this;
    }

    /**
     * Appends extra points in case of some irregular NPCs or mechanics.
     *
     * @param points the points to append.
     * @return this for chaining.
     */
    public CombatPointCapCalculator appendExtra(final int points) {
        this.points += points;
        return this;
    }

    /**
     * Builds the amount of combat points that can be granted based on the entities within it.
     *
     * @return the number of points that can be granted out of this room, with 5% extra to account for some wiggle-room.
     */
    public int build() {
        return (int) (points * 1.05F);
    }
}
