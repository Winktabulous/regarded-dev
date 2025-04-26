package com.zenyte.game.content.grandexchange;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.Enums;
import mgi.types.config.enums.IntEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tommeh | 20 mei 2018 | 20:06:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ItemSets {
    public static List<Item> getItems(final int set) {
        final ArrayList<Item> items = new ArrayList<Item>();
        final int enumId = Enums.ITEM_SETS.getValue(set).orElseThrow(Enums.exception());
        final IntEnum specificEnum = EnumDefinitions.getIntEnum(enumId);
        specificEnum.forEach(entry -> {
            final int itemId = entry.getIntValue();
            if (itemId != -1 && itemId != set) {
                items.add(new Item(itemId));
            }
        });
        return items;
    }

    public static void unpack(final Player player, final int set) {
        final List<Item> items = getItems(set);
        if (player.getInventory().getFreeSlots() < items.size()) {
            player.sendMessage("Not enough space in your inventory");
            return;
        }
        final ContainerResult result = player.getInventory().deleteItem(set, 1);
        if (!result.isFailure()) items.forEach(player.getInventory()::addItem);
    }

    public static void pack(final Player player, final int set) {
        final List<Item> items = getItems(set);
        player.getInventory().deleteItemsIfContains(items.toArray(new Item[0]), () -> player.getInventory().addItem(new Item(set)));
    }

    public static String contents(final int set) {
        final List<Item> items = getItems(set);
        final StringBuilder builder = new StringBuilder();
        items.forEach(item -> builder.append(item.getDefinitions().getName()).append(", "));
        return "<col=ff0000>" + builder.toString().replaceAll(", $", ".") + "</col>";
    }
}
