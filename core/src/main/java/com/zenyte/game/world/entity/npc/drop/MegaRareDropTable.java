package com.zenyte.game.world.entity.npc.drop;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Kris | 04/04/2019 00:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum MegaRareDropTable {
    RUNE_SPEAR(8, new Item(1247)), SHIELD_LEFT_HALF(4, new Item(2366)), DRAGON_SPEAR(3, new Item(1249));
    private final int weight;
    private final Item item;
    private static final MegaRareDropTable[] values = values();
    private static final int TOTAL_WEIGHT;

    static {
        int weight = 0;
        for (final MegaRareDropTable value : values) {
            weight += value.weight;
        }
        TOTAL_WEIGHT = weight;
    }

    public static final Optional<Item> get(@NotNull final Player player) {
        final Item ring = player.getRing();
        final boolean row = ring != null && ring.getName().startsWith("Ring of wealth");
        final int roll = Utils.random(row ? TOTAL_WEIGHT : 127);
        int currentRoll = 0;
        for (final MegaRareDropTable value : values) {
            if ((currentRoll += value.weight) >= roll) {
                return Optional.of(new Item(value.item));
            }
        }
        return Optional.empty();
    }

    MegaRareDropTable(int weight, Item item) {
        this.weight = weight;
        this.item = item;
    }

    public int getWeight() {
        return weight;
    }

    public Item getItem() {
        return item;
    }
}
