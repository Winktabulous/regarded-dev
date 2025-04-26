package com.near_reality.game.content.gauntlet;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

import java.util.Arrays;
import java.util.Optional;

public enum GauntletArmorTier {
    BASIC(ItemId.CRYSTAL_HELM_BASIC, ItemId.CRYSTAL_BODY_BASIC, ItemId.CRYSTAL_LEGS_BASIC, ItemId.CORRUPTED_HELM_BASIC, ItemId.CORRUPTED_BODY_BASIC, ItemId.CORRUPTED_LEGS_BASIC),
    ATTUNED(ItemId.CRYSTAL_HELM_ATTUNED, ItemId.CRYSTAL_BODY_ATTUNED, ItemId.CRYSTAL_LEGS_ATTUNED, ItemId.CORRUPTED_HELM_ATTUNED, ItemId.CORRUPTED_BODY_ATTUNED, ItemId.CORRUPTED_LEGS_ATTUNED),
    PERFECTED(ItemId.CRYSTAL_HELM_PERFECTED, ItemId.CRYSTAL_BODY_PERFECTED, ItemId.CRYSTAL_LEGS_PERFECTED, ItemId.CORRUPTED_HELM_PERFECTED, ItemId.CORRUPTED_BODY_PERFECTED, ItemId.CORRUPTED_LEGS_PERFECTED);

    private static final GauntletArmorTier[] TIERS = values();

    private final int helmetId;
    private final int bodyId;
    private final int legsId;
    private final int corruptedHelmetId;
    private final int corruptedBodyId;
    private final int corruptedLegsId;

    GauntletArmorTier(int helmetId, int bodyId, int legsId, int corruptedHelmetId, int corruptedBodyId, int corruptedLegsId) {
        this.helmetId = helmetId;
        this.bodyId = bodyId;
        this.legsId = legsId;
        this.corruptedHelmetId = corruptedHelmetId;
        this.corruptedBodyId = corruptedBodyId;
        this.corruptedLegsId = corruptedLegsId;
    }

    public boolean test(Player player, GauntletType type) {
        return player.getEquipment().getId(EquipmentSlot.HELMET) == (type.isCorrupted() ? corruptedHelmetId : helmetId) && player.getEquipment().getId(EquipmentSlot.PLATE) == (type.isCorrupted() ? corruptedBodyId : bodyId) &&  player.getEquipment().getId(EquipmentSlot.LEGS) == (type.isCorrupted() ? corruptedLegsId : legsId);
    }

    public static Optional<GauntletArmorTier> get(Player player, GauntletType type) {
        return Arrays.stream(TIERS).filter(tier -> tier.test(player, type)).findFirst();
    }
}
