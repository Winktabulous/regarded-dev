package com.zenyte.game.content.skills.firemaking;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

/**
 * @author Kris | 28/10/2018 23:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum FiremakingTool {
    TINDERBOX(new Animation(733), 590), TRAINING_BOW(new Animation(6713), 9705), BOW(new Animation(6714), 841, 839), OAK_BOW(new Animation(6715), 843, 845), WILLOW_BOW(new Animation(6716), 847, 849), MAPLE_BOW(new Animation(6717), 851, 853), YEW_BOW(new Animation(6718), 855, 857), MAGIC_BOW(new Animation(6719), 859, 861), SEERCULL(new Animation(6720), 6724);
    public static final FiremakingTool[] values = values();
    private final Animation animation;
    private final int[] bowIds;

    FiremakingTool(final Animation animation, final int... bowIds) {
        this.animation = animation;
        this.bowIds = bowIds;
    }

    public static Optional<FiremakingTool> getAvailableTool(final Player player) {
        final Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainer().getContainerSize(); i++) {
            final Item item = inventory.getItem(i);
            if (item == null) continue;
            for (final FiremakingTool tool : values) {
                if (ArrayUtils.contains(tool.bowIds, item.getId())) return Optional.of(tool);
            }
        }
        return Optional.empty();
    }

    public static Optional<FiremakingTool> getTool(final int... ids) {
        for (int i = values.length - 1; i >= 0; i--) {
            final FiremakingTool tool = values[i];
            for (int id : ids) {
                if (ArrayUtils.contains(tool.bowIds, id)) return Optional.of(tool);
            }
        }
        return Optional.empty();
    }

    public Animation getAnimation() {
        return animation;
    }

    public int[] getBowIds() {
        return bowIds;
    }
}
