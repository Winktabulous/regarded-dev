package com.zenyte.game.content.skills.cooking;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Kris | 24. march 2018 : 21:39.45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum GnomeCocktail {

    FRUIT_BLAST(0, ItemId.MIXED_BLAST, 6, 9, new Item(ItemId.PINEAPPLE), new Item(ItemId.LEMON),
            new Item(ItemId.ORANGE)),
    PINEAPPLE_PUNCH(1, ItemId.MIXED_PUNCH, 8, 9, new Item(ItemId.PINEAPPLE, 2), new Item(ItemId.LEMON),
            new Item(ItemId.ORANGE)),
    WIZARD_BLIZZARD(2, ItemId.MIXED_BLIZZARD, 18, 5, new Item(ItemId.VODKA, 2), new Item(ItemId.GIN),
            new Item(ItemId.LIME), new Item(ItemId.LEMON), new Item(ItemId.ORANGE)),
    SHORT_GREEN_GUY(3, ItemId.MIXED_SGG, 20, 5, new Item(ItemId.VODKA), new Item(ItemId.LIME, 3)),
    DRUNK_DRAGON(4, ItemId.MIXED_DRAGON, 32, 5, new Item(ItemId.VODKA), new Item(ItemId.GIN),
            new Item(ItemId.DWELLBERRIES)),
    CHOCOLATE_SATURDAY(5, ItemId.MIXED_SATURDAY, 33, 5, new Item(ItemId.WHISKY), new Item(ItemId.CHOCOLATE_BAR),
            new Item(ItemId.EQUA_LEAVES), new Item(ItemId.BUCKET_OF_MILK)),
    BLURBERRY_SPECIAL(6, ItemId.MIXED_SPECIAL, 37, 7, new Item(ItemId.VODKA), new Item(ItemId.BRANDY),
            new Item(ItemId.GIN), new Item(ItemId.LEMON, 2), new Item(ItemId.ORANGE));

    public static final GnomeCocktail[] VALUES = values();
    public static final int COMPONENT_ID = 3;
    private final int slotId;
    private final int shaker;
    private final int level;
    private final double experience;
    private final Item product;
    private final Item[] ingredients;

    GnomeCocktail(final int slotId, final int shaker, final int level, final double experience,
                  final Item product, final Item... ingredients) {
        this.slotId = slotId;
        this.shaker = shaker;
        this.level = level;
        this.experience = experience;
        this.product = product;
        this.ingredients = ingredients;
    }

    public static GnomeCocktail getBySlot(final int slotId) {
        for (GnomeCocktail cocktail : VALUES) {
            if (cocktail.getSlotId() == slotId) {
                return cocktail;
            }
        }
        return null;
    }

    public int getSlotId() {
        return slotId;
    }

    public int getShaker() {
        return shaker;
    }

    public int getLevel() {
        return level;
    }

    public double getExperience() {
        return experience;
    }

    public Item getProduct() {
        return product;
    }

    public Item[] getIngredients() {
        return ingredients;
    }

}
