package com.zenyte.game.world.entity.player;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

/**
 * @author Chris
 * @since August 18 2020
 */
public enum WhackAnimation {
    EASTER_CARROT(EasterConstants.EasterItem.EASTER_CARROT.getItemId(), 15236),
    RUBBER_CHICKEN(ItemId.RUBBER_CHICKEN, 1833);
    private final int itemId;
    private final Animation animation;
    private static final EnumSet<WhackAnimation> whackAnimations = EnumSet.allOf(WhackAnimation.class);

    WhackAnimation(@NonNegative int itemId, @NonNegative int animId) {
        this.itemId = itemId;
        this.animation = new Animation(animId);
    }

    public static Animation getAnimation(@NotNull final Item item) {
        for (WhackAnimation whackAnimation : whackAnimations) {
            if (item.getId() == whackAnimation.itemId) {
                return whackAnimation.animation;
            }
        }
        throw new IllegalArgumentException("Could not find a whack animation for item: " + item.getId());
    }
}
