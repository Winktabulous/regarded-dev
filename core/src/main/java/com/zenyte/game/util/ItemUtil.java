package com.zenyte.game.util;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.MessageType;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class ItemUtil {
    public static Item[] concatenate(final Item[]... arrays) {
        final ArrayList<Item> list = new ArrayList<Item>();
        for (final Item[] array : arrays) {
            Arrays.stream(array).filter(Objects::nonNull).forEach(list::add);
        }
        final Item[] items = new Item[list.size()];
        return list.toArray(items);
    }

    public static Item[] concatenate(final Collection<Item> arg0, final Collection<Item> arg1) {
        final ArrayList<Item> list = new ArrayList<Item>();
        list.addAll(arg0);
        list.addAll(arg1);
        final Item[] items = new Item[list.size()];
        return list.toArray(items);
    }

    /**
     * Creates a string based on the amount and name of item. If amount is greater than one than it pluralizes the name.
     * e.g "5 buckets", "1 bucket"
     * @param item
     */
    public static String toPrettyString(Item item) {
        final String name = item.getName();
        String prettyName = name;
        if (!name.endsWith("s")) {
            prettyName += Utils.plural(item.getAmount());
        }
        return item.getAmount() + " " + prettyName;
    }

    public static void sendItemExamine(final Player player, final Item item) {
        if (item == null) {
            return;
        }
        sendItemExamine(player, item.getId(), item.getAmount());
    }

    public static void sendItemExamine(final Player player, final int id) {
        sendItemExamine(player, id, 1);
    }

    public static void sendItemExamine(final Player player, final int id, final int amount) {
        final ItemDefinitions definitions = ItemDefinitions.getOrThrow(id);
        if (amount >= 100_000) {
            player.sendMessage(StringFormatUtil.format(amount) + " x " + definitions.getName() + ".", MessageType.EXAMINE_ITEM);
            return;
        }
        if (definitions.isNoted()) {
            player.sendMessage("Swap this note at any bank for the equivalent item", MessageType.EXAMINE_ITEM);
            return;
        }
        final String examine = definitions.getExamine();
        if (examine == null || examine.isEmpty()) {
            return;
        }
        player.sendMessage(examine, MessageType.EXAMINE_ITEM);
    }
}
