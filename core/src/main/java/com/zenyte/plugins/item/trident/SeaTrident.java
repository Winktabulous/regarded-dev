package com.zenyte.plugins.item.trident;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Christopher
 * @since 2/20/2020
 */

public enum SeaTrident implements Trident {
    SEA_TRIDENT(ItemId.UNCHARGED_TRIDENT, ItemId.TRIDENT_OF_THE_SEAS, 2500),
    SEA_TRIDENT_FULL(ItemId.UNCHARGED_TRIDENT, ItemId.TRIDENT_OF_THE_SEAS_FULL, 0),
    SEA_TRIDENT_ENHANCED(ItemId.UNCHARGED_TRIDENT_E, ItemId.TRIDENT_OF_THE_SEAS_E, 20_000);

    private static final SeaTrident[] values = values();
    public static final Int2ObjectOpenHashMap<SeaTrident> allTridentsMap = new Int2ObjectOpenHashMap<>();
    public static final Int2ObjectOpenHashMap<SeaTrident> chargedMap = new Int2ObjectOpenHashMap<>();
    private static final Item[] chargingRunes = new Item[]{
            new Item(ItemId.DEATH_RUNE),
            new Item(ItemId.CHAOS_RUNE),
            new Item(ItemId.FIRE_RUNE, 5)
    };
    public static final Item[] chargingMaterials = new Item[]{
            new Item(ItemId.DEATH_RUNE),
            new Item(ItemId.CHAOS_RUNE),
            new Item(ItemId.FIRE_RUNE, 5),
            new Item(ItemId.COINS_995, 10)
    };
    private static final Animation chargingAnim = new Animation(7137);

    static {
        for (SeaTrident trident : values) {
            if (trident != SEA_TRIDENT_FULL) {
                allTridentsMap.put(trident.getUnchargedId(), trident);
            }
            chargedMap.put(trident.getChargedId(), trident);
        }
        allTridentsMap.putAll(chargedMap);
    }

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
        return chargingRunes;
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
        return "trident of the seas";
    }

    @Override
    public String missingMaterialMessage() {
        return "You need at least 1 death rune, 1 chaos rune, 5 fire runes and 10 gold pieces per charge to charge the trident.";
    }

    SeaTrident(int unchargedId, int chargedId, int maxCharges) {
        this.unchargedId = unchargedId;
        this.chargedId = chargedId;
        this.maxCharges = maxCharges;
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