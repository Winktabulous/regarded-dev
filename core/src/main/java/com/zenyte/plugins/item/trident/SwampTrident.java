package com.zenyte.plugins.item.trident;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Christopher
 * @since 2/20/2020
 */

public enum SwampTrident implements Trident {
    SWAMP_TRIDENT(ItemId.UNCHARGED_TRIDENT, ItemId.UNCHARGED_TOXIC_TRIDENT, ItemId.TRIDENT_OF_THE_SWAMP, 2500),
    SWAMP_TRIDENT_E(ItemId.UNCHARGED_TRIDENT_E, ItemId.UNCHARGED_TOXIC_TRIDENT_E, ItemId.TRIDENT_OF_THE_SWAMP_E, 20_000);

    public static final SwampTrident[] values = values();
    public static final Int2ObjectOpenHashMap<SwampTrident> allTridentsMap = new Int2ObjectOpenHashMap<>();
    public static final Int2ObjectOpenHashMap<SwampTrident> unchargedMap = new Int2ObjectOpenHashMap<>();
    public static final Int2ObjectOpenHashMap<SwampTrident> chargedMap = new Int2ObjectOpenHashMap<>();
    public static final Item[] chargingMaterials = new Item[]{
            new Item(ItemId.DEATH_RUNE),
            new Item(ItemId.CHAOS_RUNE),
            new Item(ItemId.FIRE_RUNE, 5),
            new Item(ItemId.ZULRAHS_SCALES)
    };
    private static final Animation chargingAnim = new Animation(3858);

    static {
        for (SwampTrident trident : values) {
            unchargedMap.put(trident.getUnchargedId(), trident);
            chargedMap.put(trident.getChargedId(), trident);
        }
        allTridentsMap.putAll(unchargedMap);
        allTridentsMap.putAll(chargedMap);
    }

    private final int baseId;
    private final int unchargedId;
    private final int chargedId;
    private final int maxCharges;

    @Override
    public int unchargedId() {
        return unchargedId;
    }

    @Override
    public int chargedId() {
        return chargedId;
    }

    @Override
    public int maxCharges() {
        return maxCharges;
    }

    @Override
    public Item[] returnedMaterials() {
        return chargingMaterials;
    }

    @Override
    public Item[] chargingMaterials() {
        return chargingMaterials;
    }

    @Override
    public Animation chargingAnim() {
        return chargingAnim;
    }

    @Override
    public String itemName() {
        return "trident of the swamp";
    }

    @Override
    public String missingMaterialMessage() {
        return "You need at least 1 death rune, 1 chaos rune, 5 fire runes and 1 Zulrah's scale per charge to charge the trident.";
    }

    SwampTrident(int baseId, int unchargedId, int chargedId, int maxCharges) {
        this.baseId = baseId;
        this.unchargedId = unchargedId;
        this.chargedId = chargedId;
        this.maxCharges = maxCharges;
    }

    public int getBaseId() {
        return baseId;
    }

    public int getUnchargedId() {
        return unchargedId;
    }

    public int getChargedId() {
        return chargedId;
    }

    public int getMaxCharges() {
        return maxCharges;
    }
}