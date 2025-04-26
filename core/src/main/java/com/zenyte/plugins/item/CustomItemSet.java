package com.zenyte.plugins.item;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import dev.kord.core.entity.ReactionEmoji;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Tommeh | 07/06/2019 | 17:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class CustomItemSet extends ItemPlugin {
    @Override
    public void handle() {
        bind("Open", (player, item, slotId) -> {
            final CustomItemSet.ItemSet set = ItemSet.get(item.getId());
            if (set == null) {
                return;
            }
            if (!player.getInventory().checkSpace(set.getItems().length)) {
                return;
            }
            player.getInventory().deleteItem(item);
            for (final int id : set.getItems()) {
                player.getInventory().addItem(id, 1);
            }
        });
    }

    @Override
    public int[] getItems() {
        return ItemSet.SETS.keySet().toIntArray();
    }


    public enum ItemSet {
        INFINITY(2724, 6918, 6916, 6924, 6922, 6920),
        VOID_KNIGHT(2726, 8839, 8840, 8842, 11663, 11664, 11665),
        ELITE_VOID_KNIGHT(2728, 13072, 13073, 8842, 11663, 11664, 11665),
        THIRD_AGE_RANGE(2730, 10334, 10330, 10332, 10336),
        THIRD_AGE_MELEE(2732, 10350, 10348, 10346, 10352),
        THIRD_AGE_MAGE(2734, 10342, 10338, 10340),
        THIRD_AGE_DRUIDIC(2736, 23336, 23339, 23342, 23345),
        CORRUPTED(2738, 20838, 20840, 20842, 20846),
        RANGER(2740, 2581, 12596, 23249, 2577),
        SANTA(2742, 12887, 12888, 12889, 12890, 12891),
        BUNNY(2744, 13663, 13664, 13182, 13665),
        WHITE_ANKOU(2746, CustomItemId.WHITE_ANKOU_MASK, CustomItemId.WHITE_ANKOU_TOP, CustomItemId.WHITE_ANKOUS_LEGGINGS, CustomItemId.WHITE_ANKOU_GLOVES, CustomItemId.WHITE_ANKOU_SOCKS),
        GREEN_ANKOU(2748, CustomItemId.GREEN_ANKOU_MASK, CustomItemId.GREEN_ANKOU_TOP, CustomItemId.GREEN_ANKOUS_LEGGINGS, CustomItemId.GREEN_ANKOU_GLOVES, CustomItemId.GREEN_ANKOU_SOCKS),
        BLUE_ANKOU(2721, CustomItemId.BLUE_ANKOU_MASK, CustomItemId.BLUE_ANKOU_TOP, CustomItemId.BLUE_ANKOUS_LEGGINGS, CustomItemId.BLUE_ANKOU_GLOVES, CustomItemId.BLUE_ANKOU_SOCKS),
        GOLD_ANKOU(2720, CustomItemId.GOLD_ANKOU_MASK, CustomItemId.GOLD_ANKOU_TOP, CustomItemId.GOLD_ANKOUS_LEGGINGS, CustomItemId.GOLD_ANKOU_GLOVES, CustomItemId.GOLD_ANKOU_SOCKS),
        BLACK_ANKOU(2717, CustomItemId.BLACK_ANKOU_MASK, CustomItemId.BLACK_ANKOU_TOP, CustomItemId.BLACK_ANKOUS_LEGGINGS, CustomItemId.BLACK_ANKOU_GLOVES, CustomItemId.BLACK_ANKOU_SOCKS),
        PURPLE_ANKOU(2718, CustomItemId.PURPLE_ANKOU_MASK, CustomItemId.PURPLE_ANKOU_TOP, CustomItemId.PURPLE_ANKOUS_LEGGINGS, CustomItemId.PURPLE_ANKOU_GLOVES, CustomItemId.PURPLE_ANKOU_SOCKS),
        BLACK_GREEN_ANKOU(CustomItemId.BLACK_GREEN_ANKOU_SET, CustomItemId.BLACK_GREEN_ANKOU_GLOVES, CustomItemId.BLACK_GREEN_ANKOU_LEGGINGS, CustomItemId.BLACK_GREEN_ANKOU_TOP, CustomItemId.BLACK_GREEN_ANKOU_SOCKS, CustomItemId.BLACK_GREEN_ANKOU_MASK),
        BLACK_PURPLE_ANKOU(CustomItemId.BLACK_PURPLE_ANKOU_SET, CustomItemId.BLACK_PURPLE_ANKOU_GLOVES, CustomItemId.BLACK_PURPLE_ANKOU_LEGGINGS, CustomItemId.BLACK_PURPLE_ANKOU_TOP, CustomItemId.BLACK_PURPLE_ANKOU_SOCKS, CustomItemId.BLACK_PURPLE_ANKOU_MASK),
        BLACK_WHITE_ANKOU(CustomItemId.BLACK_WHITE_ANKOU_SET, CustomItemId.BLACK_WHITE_ANKOU_GLOVES, CustomItemId.BLACK_WHITE_ANKOU_LEGGINGS, CustomItemId.BLACK_WHITE_ANKOU_TOP, CustomItemId.BLACK_WHITE_ANKOU_SOCKS, CustomItemId.BLACK_WHITE_ANKOU_MASK),
        BLACK_YELLOW_ANKOU(CustomItemId.BLACK_YELLOW_ANKOU_SET, CustomItemId.BLACK_YELLOW_ANKOU_GLOVES, CustomItemId.BLACK_YELLOW_ANKOU_LEGGINGS, CustomItemId.BLACK_YELLOW_ANKOU_TOP, CustomItemId.BLACK_YELLOW_ANKOU_SOCKS, CustomItemId.BLACK_YELLOW_ANKOU_MASK),
        GILDED_ANKOU(CustomItemId.GILDED_ANKOU_SET, CustomItemId.GILDED_ANKOU_GLOVES, CustomItemId.GILDED_ANKOU_LEGGINGS, CustomItemId.GILDED_ANKOU_TOP, CustomItemId.GILDED_ANKOU_SOCKS, CustomItemId.GILDED_ANKOU_MASK),
        GREEN_BLACK_ANKOU(CustomItemId.GREEN_BLACK_ANKOU_SET, CustomItemId.GREEN_BLACK_ANKOU_GLOVES, CustomItemId.GREEN_BLACK_ANKOU_LEGGINGS, CustomItemId.GREEN_BLACK_ANKOU_TOP, CustomItemId.GREEN_BLACK_ANKOU_SOCKS, CustomItemId.GREEN_BLACK_ANKOU_MASK),
        PURPLE_BLACK_ANKOU(CustomItemId.PURPLE_BLACK_ANKOU_SET, CustomItemId.PURPLE_BLACK_ANKOU_GLOVES, CustomItemId.PURPLE_BLACK_ANKOU_LEGGINGS, CustomItemId.PURPLE_BLACK_ANKOU_TOP, CustomItemId.PURPLE_BLACK_ANKOU_SOCKS, CustomItemId.PURPLE_BLACK_ANKOU_MASK),
        RED_BLACK_ANKOU(CustomItemId.RED_BLACK_ANKOU_SET, CustomItemId.RED_BLACK_ANKOU_GLOVES, CustomItemId.RED_BLACK_ANKOU_LEGGINGS, CustomItemId.RED_BLACK_ANKOU_TOP, CustomItemId.RED_BLACK_ANKOU_SOCKS, CustomItemId.RED_BLACK_ANKOU_MASK),
        WHITE_BLUE_ANKOU(CustomItemId.WHITE_BLUE_ANKOU_SET, CustomItemId.WHITE_BLUE_ANKOU_GLOVES, CustomItemId.WHITE_BLUE_ANKOU_LEGGINGS, CustomItemId.WHITE_BLUE_ANKOU_TOP, CustomItemId.WHITE_BLUE_ANKOU_SOCKS, CustomItemId.WHITE_BLUE_ANKOU_MASK),
        WHITE_PURPLE_ANKOU(CustomItemId.WHITE_PURPLE_ANKOU_SET, CustomItemId.WHITE_PURPLE_ANKOU_GLOVES, CustomItemId.WHITE_PURPLE_ANKOU_LEGGINGS, CustomItemId.WHITE_PURPLE_ANKOU_TOP, CustomItemId.WHITE_PURPLE_ANKOU_SOCKS, CustomItemId.WHITE_PURPLE_ANKOU_MASK),
        ROBES_OF_RUIN(2715, ItemId.HOOD_OF_RUIN, ItemId.ROBE_TOP_OF_RUIN, ItemId.ROBE_BOTTOM_OF_RUIN, ItemId.GLOVES_OF_RUIN, ItemId.SOCKS_OF_RUIN, ItemId.CLOAK_OF_RUIN, ItemId.INFINITE_MONEY_BAG),

        MYSTERY_BOX(32215, 32165, 32165, 32165, 32165, 32165, 32165, 32165, 32165, 32165, 32165, 32206, 32206),
        ;

        private final int id;
        private final int[] items;
        public static final ItemSet[] all = values();
        public static final Int2ObjectOpenHashMap<ItemSet> SETS = new Int2ObjectOpenHashMap(all.length);

        static {
            for (final CustomItemSet.ItemSet set : all) {
                SETS.put(set.id, set);
            }
        }

        ItemSet(final int id, final int... items) {
            this.id = id;
            this.items = items;
        }

        public static ItemSet get(final int id) {
            return SETS.get(id);
        }

        public int getId() {
            return id;
        }

        public int[] getItems() {
            return items;
        }
    }
}
