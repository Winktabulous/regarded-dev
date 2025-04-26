package com.zenyte.plugins.flooritem;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/04/2019 18:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EcumenicalKeyFloorItemPlugin implements FloorItemPlugin {
    @Override
    public void handle(final Player player, final FloorItem item, final int optionId, final String option) {
        final int maxCount = DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD3, player) ? 5 : DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD2, player) ? 4 : 3;
        final int heldCount = player.getAmountOf(11942);
        if (option.equalsIgnoreCase("take")) {
            if (heldCount >= maxCount) {
                player.sendMessage("You can only own up to " + maxCount + " ecumenical keys at any given time.");
                return;
            }
            World.takeFloorItem(player, item);
        }
    }

    @Override
    public boolean canTelegrab(@NotNull final Player player, @NotNull final FloorItem item) {
        final int maxCount = DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD3, player) ? 5 : DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD2, player) ? 4 : 3;
        final int heldCount = player.getAmountOf(11942);
        if (heldCount >= maxCount) {
            player.sendMessage("You can only own up to " + maxCount + " ecumenical keys at any given time.");
            return false;
        }
        return true;
    }

    @Override
    public boolean overrideTake() {
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {11942};
    }
}
