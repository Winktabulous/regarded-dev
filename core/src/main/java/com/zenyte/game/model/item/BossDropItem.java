package com.zenyte.game.model.item;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Tommeh | 21-4-2018 | 23:30
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum BossDropItem {

    ETERNAL_BOOTS(new Item(13235), new Item(6920), new Item(13227)),
    PEGASIAN_BOOTS(new Item(13237), new Item(2577), new Item(13229)),
    PRIMORDIAL_BOOTS(new Item(13239), new Item(11840), new Item(13231)),
    TOXIC_BLOWPIPE(new Item(12924), new Item(12922), new Item(1755)),
    SERPENTINE_HELMET(new Item(12929), new Item(12927), new Item(1755)),
    TRIDENT_OF_THE_SWAMP(new Item(12900), new Item(12932), new Item(11908), new Item(11907), new Item(11905)),
    TRIDENT_OF_THE_SWAMP_E(
            new Item(ItemId.UNCHARGED_TOXIC_TRIDENT_E),
            new Item(ItemId.MAGIC_FANG),
            new Item(ItemId.UNCHARGED_TRIDENT_E),
            new Item(ItemId.TRIDENT_OF_THE_SEAS_E)),
    MAGMA_HELMET(new Item(13199), new Item(12931), new Item(13201)),
    TANZANITE_HELMET(new Item(13197), new Item(12931), new Item(13200)),
    TOXIC_STAFF_OF_THE_DEAD(new Item(12902), new Item(12932), new Item(11791)),
    KODAI_WAND(new Item(21006), new Item(6914), new Item(21043)),
    SPECTRAL_SPIRIT_SHIELD(new Item(12821), new Item(12823), new Item(12831)),
    ARCANE_SPIRIT_SHIELD(new Item(12825), new Item(12827), new Item(12831)),
    ELYSIAN_SPIRIT_SHIELD(new Item(12817), new Item(12819), new Item(12831)),
    GUARDIAN_BOOTS(new Item(21733), new Item(11836), new Item(21730)),
    HOLY_GREAT_HAMMER(new Item(CustomItemId.HOLY_GREAT_WARHAMMER), new Item(CustomItemId.ANGELIC_ARTIFACT), new Item(ItemId.DRAGON_WARHAMMER), new Item(CustomItemId.DEGRADED_ESSENCE, 2_500)),
    HOLY_GREAT_LANCE(new Item(CustomItemId.HOLY_GREAT_LANCE), new Item(CustomItemId.ANGELIC_ARTIFACT), new Item(ItemId.DRAGON_HUNTER_LANCE), new Item(CustomItemId.DEGRADED_ESSENCE, 2_500)),
    AHRIM_THE_BOBBLED(32186, new Item(ItemId.AHRIMS_ARMOUR_SET), new Item(32185, 250)),
    DHAROK_THE_BOBBLED(32187, new Item(ItemId.DHAROKS_ARMOUR_SET), new Item(32185, 250)),
    GUTHAN_THE_BOBBLED(32188, new Item(ItemId.GUTHANS_ARMOUR_SET), new Item(32185, 250)),
    KARIL_THE_BOBBLED(32189, new Item(ItemId.KARILS_ARMOUR_SET), new Item(32185, 250)),
    TORAG_THE_BOBBLED(32190, new Item(ItemId.TORAGS_ARMOUR_SET), new Item(32185, 250)),
    VERAC_THE_BOBBLED(32191, new Item(ItemId.VERACS_ARMOUR_SET), new Item(32185, 250)),
    ;

    private final Item[] materials;
    private final Item item;

    private static final BossDropItem[] VALUES = values();

    BossDropItem(final int product, final Item... materials) {
        this.item = new Item(product);
        this.materials = materials;
    }

    BossDropItem(final Item product, final Item... materials) {
        this.item = product;
        this.materials = materials;
    }

    public static BossDropItem getItemByMaterials(final Item from, final Item to) {
        for (BossDropItem boots : VALUES) {
            for (Item i : boots.getMaterials()) {
                if (i.getId() == from.getId()) {
                    for (Item o : boots.getMaterials()) {
                        if (o.getId() == to.getId() && i != o)
                            return boots;
                    }
                }
            }
        }
        return null;
    }
    
    public Item[] getMaterials() {
        return materials;
    }
    
    public Item getItem() {
        return item;
    }
}
