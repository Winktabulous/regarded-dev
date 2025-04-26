package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.skills.CookingD;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;

public final class CookingComboItemAction implements ItemOnItemAction {

    public enum CookingCombination {
        DOUGH(1, 0, new int[] {1929, 1933}, new Item(2307), "You mix the water and flour to make some bread dough.", false), PASTRY_DOUGH(1, 0, new int[] {1929, 1933}, new Item(1953), "You mix the water and flour to make some pastry dough.", false), PIZZA_BASE(35, 1, new int[] {1929, 1933}, new Item(2283), "You mix the water and flour to make a pizza base.", false), PITTA_DOUGH(58, 1, new int[] {1929, 1933}, new Item(1863), "You mix the water and flour to make some pitta dough.", false), CHOCOLATE_CAKE(50, 30, new int[] {1891, 1975}, new Item(1897), "You enqueue the chocolate mix to the cake."), CHOCOLATE_CAKE2(50, 30, new int[] {1891, 1973}, new Item(1897), CHOCOLATE_CAKE.getMessage()), CHOPPED_TUNA(1, 0, new int[] {361, 1923}, new Item(7086), "You cut up the tuna and put it into the bowl."), CHOPPED_TUNA2(1, 0, new int[] {361, 946}, new Item(7086), CHOPPED_TUNA.getMessage()), CHOPPED_GARLIC(1, 0, new int[] {1550, 1923}, new Item(7074), "You cut up the garlic and put it into the bowl."), CHOPPED_GARLIC2(1, 0, new int[] {1550, 946}, new Item(7074), CHOPPED_GARLIC.getMessage()), CHOPPED_UGTHANKI(1, 0, new int[] {1861, 1923}, new Item(1873), "You cut up the ugthanki meat and put it into the bowl."), CHOPPED_UGTHANKI2(1, 0, new int[] {1861, 946}, new Item(1873), CHOPPED_UGTHANKI.getMessage()), CHOPPED_ONION(1, 0, new int[] {1957, 1923}, new Item(1871), "You cut up the onion and put it into the bowl."), CHOPPED_ONION2(1, 0, new int[] {1957, 946}, new Item(1871), CHOPPED_ONION.getMessage()), CHOPPED_TOMATO(1, 0, new int[] {1982, 1923}, new Item(1869), "You cut up the tomato and put it into the bowl."), CHOPPED_TOMATO2(1, 0, new int[] {1982, 946}, new Item(1869), CHOPPED_TOMATO.getMessage()), CHILLI_CON_CARNE(9, 25, new int[] {7072, 2142}, new Item(7062), "You cut up the meat and mix it with the spicy sauce."), SPICY_TOMATO(1, 0, new int[] {1869, 2169}, new Item(9994), "You mix the gnome spice into the bowl of tomato."), SCRAMBLED_EGG(13, 0, new int[] {1923, 1944}, new Item(7076), "You carefully break the egg into the bowl."), TUNA_AND_CORN(67, 0, new int[] {7086, 5988}, new Item(7068), "You mix the ingredients to make the topping."), INCOMPLETE_STEW(25, 1, new int[] {1921, 1942}, new Item(1997), "You cut up the potato and put it into the bowl."), SPICY_SAUCE(9, 25, new int[] {7074, 2169}, new Item(7072), "You enqueue the spices to the garlic to make a spicy sauce."), UNCOOKED_STEW(25, 1, new int[] {1997, 2142}, new Item(2001), "You cut up the meat and put it into the stew."), UNCOOKED_STEW2(25, 1, new int[] {1997, 2140}, new Item(2001), "You cut up the chicken and put it into the stew."), BUTTER_POTATO(39, 40, new int[] {6701, 6697}, new Item(6703), "You enqueue the butter to the potato."), CHILI_POTATO(41, 15, new int[] {6703, 7062}, new Item(7062), "You enqueue the topping to the potato."), CHEESE_POTATO(47, 40, new int[] {6703, 1985}, new Item(6705), "You enqueue the cheese to the potato."), EGG_POTATO(51, 45, new int[] {6703, 7064}, new Item(7056), "You enqueue the egg to the potato."), TUNA_POTATO(68, 10, new int[] {7068, 6703}, new Item(7060), CHILI_POTATO.getMessage()), TOMATO_PIZZA(35, 0, 2, new int[] {2283, 1982}, new Item(2285), "You enqueue the tomato to the pizza."), CHEESE_PIZZA(35, 0, 2, new int[] {2285, 1985}, new Item(2287), "You enqueue the cheese to the pizza."), MEAT_PIZZA(45, 26, 2, new int[] {2289, 2142}, new Item(2293), "You enqueue the meat to the pizza."), MEAT_PIZZA2(45, 26, 2, new int[] {2289, 2140}, new Item(2293), MEAT_PIZZA.getMessage()), ANCHOVY_PIZZA(55, 39, 2, new int[] {2289, 319}, new Item(2297), "You enqueue the anchovies to the pizza."), PINEAPPLE_PIZZA(65, 45, 2, new int[] {2289, 2116}, new Item(2301), "You enqueue the pineapple to the pizza."), PINEAPPLE_PIZZA_RING(65, 45, 2, new int[] {2289, ItemId.PINEAPPLE_RING}, new Item(2301), "You enqueue the pineapple to the pizza."), UNFILLED_PIE(10, 0, new int[] {1953, 2313}, new Item(2315), "You put the pastry dough into the pie dish to make a pie shell."), REDBERRY_PIE(10, 0, new int[] {2315, 2325}, new Item(2321), "You fill the pie with redberries."), MEAT_PIE(20, 0, new int[] {2315, 2142}, new Item(2319), "You fill the pie with chicken."), MEAT_PIE2(20, 0, new int[] {2315, 2140}, new Item(2319), "You fill the pie with meat."), APPLE_PIE(30, 0, new int[] {2315, 1955}, new Item(2317), "You fill the pie with apple."), PART_GARDEN_PIE(34, 0, new int[] {2315, 1982}, new Item(7172), "You fill the pie with tomato."), PART_GARDEN_PIE2(34, 0, new int[] {7172, 1957}, new Item(7174), "You fill the pie with onion."), RAW_GARDEN_PIE(34, 0, new int[] {7174, 1965}, new Item(7176), "You fill the pie with cabbage."), PART_FISH_PIE(47, 0, new int[] {2315, 333}, new Item(7182), "You fill the pie with trout."), PART_FISH_PIE2(47, 0, new int[] {7182, 339}, new Item(7184), "You fill the pie with pike."), RAW_FISH_PIE(47, 0, new int[] {7184, 1942}, new Item(7186), "You fill the pie with potato."), RAW_BOTANICAL_PIE(52, 0, new int[] {2315, ItemId.GOLOVANOVA_FRUIT_TOP}, new Item(19656), "You fill the pie with strange fruit."), PART_ADMIRAL_PIE(70, 0, new int[] {2315, 329}, new Item(7192), "You fill the pie with salmon."), PART_ADMIRAL_PIE2(70, 0, new int[] {7192, 361}, new Item(7194), "You fill the pie with tuna."), RAW_ADMIRAL_PIE(70, 0, new int[] {7194, 1942}, new Item(7196), RAW_FISH_PIE.getMessage()), RAW_DRAGONFRUIT_PIE(70, 0, new int[] {ItemId.PIE_SHELL, ItemId.DRAGONFRUIT}, new Item(ItemId.UNCOOKED_DRAGONFRUIT_PIE), "You fill the pie with dragonfruit."), PART_WILD_PIE(85, 0, new int[] {2315, 2136}, new Item(7202), "You fill the pie with raw bear meat."), PART_WILD_PIE2(85, 0, new int[] {7202, 2876}, new Item(7204), "You fill the pie with raw chompy."), RAW_WILD_PIE(85, 0, new int[] {7204, 3226}, new Item(7206), "You fill the pie with raw rabbit."), PART_SUMMER_PIE(95, 0, new int[] {2315, 5504}, new Item(7212), "You fill the pie with strawberries."), PART_SUMMER_PIE2(95, 0, new int[] {7212, 5982}, new Item(7214), "You fill the pie with watermelon."), RAW_SUMMER_PIE(95, 0, new int[] {7214, 1955}, new Item(7216), APPLE_PIE.getMessage()), NETTLE_WATER(1, 1, new int[] {1921, 4241}, new Item(4237), "You place the nettles into the bowl of water."), NETTLE_TEA(1, 1, new int[] {1980, 4239}, new Item(1978), "You pour the nettle tea into a tea cup.", false), ORANGE_SLICE(1, 1, new int[] {2108, 946}, new Item(2112), "You cut the orange into slices."), ORANGE_CHUNKS(1, 1, new int[] {2108, 946}, new Item(2110), "You cut the orange into chunks."), PINEAPPLE_SLICE(1, 1, new int[] {2114, 946}, new Item(2118, 4), "You cut the pineapple into rings."), PINEAPPLE_CHUNKS(1, 1, new int[] {2114, 946}, new Item(2116), "You cut the pineapple into chunks."), LIME_SLICE(1, 1, new int[] {2120, 946}, new Item(2124), "You cut the lime into slices."), LIME_CHUNKS(1, 1, new int[] {2120, 946}, new Item(2122), "You cut the lime into chunks."), LEMON_SLICE(1, 1, new int[] {2102, 946}, new Item(2106), "You cut the lemon into slices."), LEMON_CHUNKS(1, 1, new int[] {2102, 946}, new Item(2104), "You cut the lemon into chunks."), WATERMELON_SLICE(1, 1, new int[] {5982, 946}, new Item(5984, 3), "You cut the watermelon into 3 slices.");
        private final int level;
        private final double xp;
        private final int delay;
        private final int[] raw;
        private final Item product;
        private final String message;
        private final boolean consume;
        public static final CookingCombination[] VALUES = values();
        public static final Long2ObjectOpenHashMap<CookingCombination> COMBOS = new Long2ObjectOpenHashMap<>();
        public static final List<CookingCombination> CHOPPED = new ArrayList<>();
        private static final int DEFAULT_DELAY = 3;

        static {
            for (final CookingCombination combo : VALUES) {
                COMBOS.put(combo.getRaw()[0] | ((long) combo.getRaw()[1] << 32), combo);
                COMBOS.put(combo.getRaw()[1] | ((long) combo.getRaw()[0] << 32), combo);
                if (combo.toString().toLowerCase().contains("chopped") || combo.toString().equalsIgnoreCase("chilli_con_carne")) {
                    CHOPPED.add(combo);
                }
            }
        }

        CookingCombination(final int level, final double xp, final int[] raw, final Item product, final String message) {
            this(level, xp, DEFAULT_DELAY, raw, product, message, true);
        }

        CookingCombination(final int level, final double xp, final int delay, final int[] raw, final Item product, final String message) {
            this(level, xp, delay, raw, product, message, true);
        }

        CookingCombination(final int level, final double xp, final int[] raw, final Item product, final String message, final boolean consume) {
            this(level, xp, DEFAULT_DELAY, raw, product, message, consume);
        }

        CookingCombination(final int level, final double xp, final int delay, final int[] raw, final Item product, final String message, final boolean consume) {
            this.level = level;
            this.xp = xp;
            this.delay = delay;
            this.raw = raw;
            this.product = product;
            this.message = message;
            this.consume = consume;
        }

        public static final CookingCombination getCombo(final int used, final int usedOn) {
            return COMBOS.get(used | ((long) usedOn << 32));
        }

        public static final CookingCombination getComboByProduct(final int product) {
            for (final CookingCombination combo : VALUES) {
                if (product == combo.getProduct().getId()) {
                    return combo;
                }
            }
            return null;
        }

        public static final List<CookingCombination> getCombos(final int used, final int usedOn) {
            final List<CookingCombination> products = new ArrayList<>();
            for (final CookingCombination combo : VALUES) {
                if (used == combo.getRaw()[0] && usedOn == combo.getRaw()[1] || used == combo.getRaw()[1] && usedOn == combo.getRaw()[0]) {
                    products.add(combo);
                }
            }
            return products;
        }

        public static final Item[] getProducts(final int used, final int usedOn) {
            final List<Item> products = new ArrayList<Item>();
            for (final CookingCombination combo : VALUES) {
                if (used == combo.getRaw()[0] && usedOn == combo.getRaw()[1] || used == combo.getRaw()[1] && usedOn == combo.getRaw()[0]) {
                    products.add(combo.getProduct());
                }
            }
            return products.toArray(new Item[0]);
        }

        public boolean hasRequirements(final Player player) {
            final boolean checkForSpace = !isConsume() || getProduct().getAmount() > 1;
            final int spaceNeeded = isConsume() ? getProduct().getAmount() - 1 : getProduct().getAmount();
            if (checkForSpace && player.getInventory().getFreeSlots() < spaceNeeded) {
                player.sendMessage("You need at least " + spaceNeeded + " free slot(s) to make this!");
                return false;
            }
            return player.getInventory().containsItem(raw[0], 1) && player.getInventory().containsItem(raw[1], 1) && player.getSkills().getLevel(SkillConstants.COOKING) >= level;
        }

        public int getLevel() {
            return level;
        }

        public double getXp() {
            return xp;
        }

        public int getDelay() {
            return delay;
        }

        public int[] getRaw() {
            return raw;
        }

        public Item getProduct() {
            return product;
        }

        public String getMessage() {
            return message;
        }

        public boolean isConsume() {
            return consume;
        }
    }

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final List<CookingComboItemAction.CookingCombination> combos = CookingCombination.getCombos(from.getId(), to.getId());
        if (combos.isEmpty()) {
            player.sendMessage("Nothing interesting happens.");
            return;
        }
        final Item[] products = CookingCombination.getProducts(from.getId(), to.getId());
        player.getDialogueManager().start(new CookingD(player, combos, products));
    }

    @Override
    public int[] getItems() {
        final IntArrayList list = new IntArrayList();
        for (final CookingCombination combo : CookingCombination.VALUES) for (final int raw : combo.getRaw()) list.add(raw);
        return list.toArray(new int[0]);
    }
}
