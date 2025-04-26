package com.zenyte.game.content.tog;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Chris
 * @since September 13 2020
 */
public enum TearsOfGuthixMemberPerk {
    PREMIUM(MemberRank.PREMIUM, 10),
    EXPANSION(MemberRank.EXPANSION, 20),
    EXTREME(MemberRank.EXTREME, 25),
    RESPECTED(MemberRank.RESPECTED, 30),
    LEGENDARY(MemberRank.LEGENDARY, 45),
    MYTHICAL(MemberRank.MYTHICAL, 55),
    UBER(MemberRank.UBER, 70);
    private static final List<TearsOfGuthixMemberPerk> PERKS = ObjectLists.unmodifiable(new ObjectArrayList<>(values()));
    /**
     * The member rank a player must have to receive the bonus time.
     */
    private final MemberRank rank;
    /**
     * The amount of extra ticks to append to the Tear Of Guthix timer.
     */
    private final int bonusTime;

    TearsOfGuthixMemberPerk(@NotNull final MemberRank rank, final int bonusSeconds) {
        this.rank = rank;
        this.bonusTime = (int) TimeUnit.SECONDS.toTicks(bonusSeconds);
    }

    /**
     * Returns the extra amount of ticks for the highest member rank a player is eligible for.
     * If player has no member rank then no extra time is .
     */
    public static int ticks(@NotNull final Player player) {
        for (int index = PERKS.size() - 1; index >= 0; index--) {
            final TearsOfGuthixMemberPerk perk = PERKS.get(index);
            if (player.getMemberRank().equalToOrGreaterThan(perk.rank)) {
                return perk.bonusTime;
            }
        }
        return 0;
    }
}
