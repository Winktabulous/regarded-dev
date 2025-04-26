package com.zenyte.game.content.skills.farming;

import com.google.common.base.Preconditions;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import mgi.types.config.enums.Enums;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Kris | 24/12/2018 23:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Storable {
    FARMING_RAKE(8, 100, 1435, 5341) {
        @Override
        void _refresh(@NotNull final Player player, final Item item) {
            final int amount = item.getAmount();
            final VarManager manager = player.getVarManager();
            manager.sendBit(8357, amount >> 1);
            manager.sendBit(1435, amount & 0x1);
        }
    },
    SEED_DIBBER(9, 100, 1436, 5343) {
        @Override
        void _refresh(@NotNull final Player player, final Item item) {
            final int amount = item.getAmount();
            final VarManager manager = player.getVarManager();
            manager.sendBit(8358, amount >> 1);
            manager.sendBit(1436, amount & 0x1);
        }
    },
    SPADE(10, 100, 1437, 952) {
        @Override
        void _refresh(@NotNull final Player player, final Item item) {
            final int amount = item.getAmount();
            final VarManager manager = player.getVarManager();
            manager.sendBit(8361, amount >> 1);
            manager.sendBit(1437, amount & 0x1);
        }
    },
    SECATEURS(11, 100, 1438, 7409, 5329) {
        @Override
        void _refresh(@NotNull final Player player, final Item item) {
            super._refresh(player, item);
            final int amount = item.getAmount();
            final VarManager manager = player.getVarManager();
            player.getVarManager().sendBit(1848, item.getId() == 7409 && amount > 0 ? 1 : 0);
            manager.sendBit(8359, amount >> 1);
            manager.sendBit(1438, amount & 0x1);
        }
    },
    WATERING_CAN(12, 1, -1, Enums.FARMING_WATERING_CANS.getValues().values().toIntArray()) {
        @Override
        void _refresh(@NotNull final Player player, final Item item) {
            player.getVarManager().sendBit(1439,
                    item.getAmount() == 0 ? 0 :
                            Enums.FARMING_WATERING_CANS.getKey(item.getId()).orElseThrow(Enums.exception()));
        }
    },
    GARDENING_TROWEL(13, 100, 1440, 5325) {
        @Override
        void _refresh(@NotNull final Player player, final Item item) {
            final int amount = item.getAmount();
            final VarManager manager = player.getVarManager();
            manager.sendBit(8360, amount >> 1);
            manager.sendBit(1440, amount & 0x1);
        }
    },
    PLANT_CURE(14, 1000, 6268, 6036),
    BOTTOMLESS_BUCKET(15, 1, -1, 22997, 22994) {
        @Override
        void _refresh(@NotNull final Player player, final Item item) {
            final VarManager manager = player.getVarManager();
            manager.sendBit(7915, item.getAmount() == 0 ? 0 : item.getId() == 22997 ? 2 : 1);
        }
    },
    EMPTY_BUCKET(16, 1000, -1, 1925) {
        @Override
        void _refresh(@NotNull final Player player, final Item item) {
            final int amount = item.getAmount();
            final VarManager manager = player.getVarManager();
            manager.sendBit(6265, amount / (int) Math.pow(2, 8));
            manager.sendBit(4731, (amount & 0xFF) / (int) Math.pow(2, 5));
            manager.sendBit(1441, amount & 0x1F);
        }
    },
    NORMAL_COMPOST(17, 1000, -1, 6032) {
        @Override
        void _refresh(@NotNull final Player player, final Item item) {
            final int amount = item.getAmount();
            final VarManager manager = player.getVarManager();
            manager.sendBit(6266, amount / (int) Math.pow(2, 8));
            manager.sendBit(1442, amount & 0xFF);
        }
    },
    SUPER_COMPOST(18, 1000, -1, 6034) {
        @Override
        void _refresh(@NotNull final Player player, final Item item) {
            final int amount = item.getAmount();
            final VarManager manager = player.getVarManager();
            manager.sendBit(6267, amount / (int) Math.pow(2, 8));
            manager.sendBit(1443, amount & 0xFF);
        }
    },
    ULTRACOMPOST(19, 1000, 5732, 21483);

    private final int componentId;
    private final int[] itemIds;
    private final int maximumAmount;
    private final int varbitId;

    static final Storable[] values = values();

    Storable(final int componentId, int maximumAmount, final int varbitId, final int... itemIds) {
        this.componentId = componentId;
        this.itemIds = itemIds;
        this.varbitId = varbitId;
        this.maximumAmount = maximumAmount;
    }

    static {
        final int[] array = Storable.WATERING_CAN.itemIds;
        Arrays.sort(array);
        ArrayUtils.reverse(array);
    }

    boolean equals(final int id) {
        return ArrayUtils.contains(getItemIds(), id);
    }

    public int getBaseId() {
        Preconditions.checkArgument(itemIds.length > 0);
        return itemIds[itemIds.length - 1];
    }

    void refresh(@NotNull final Player player, final Item item) {
        if (item == null)
            return;
        _refresh(player, item);
    }

    void _refresh(final Player player, final Item item) {
        Preconditions.checkArgument(varbitId != -1);
        player.getVarManager().sendBit(varbitId, item.getAmount());
    }

    public int getComponentId() {
        return componentId;
    }

    public int[] getItemIds() {
        return itemIds;
    }

    public int getMaximumAmount() {
        return maximumAmount;
    }
}
