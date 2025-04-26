package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.actions.JewelleryEnchantment;
import com.zenyte.game.content.skills.magic.actions.JewelleryEnchantmentType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mgi.utilities.CollectionUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Kris | 03/09/2019 23:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EnchantmentTabletItem extends ItemPlugin implements PairedItemOnItemPlugin {

    private enum EnchantmentTab {
        ENCHANT_SAPPHIRE_OR_OPAL(8016, JewelleryEnchantmentType.LVL1_ENCHANTMENT, "Sapphire"), ENCHANT_EMERALD_OR_JADE(8017, JewelleryEnchantmentType.LVL2_ENCHANTMENT, "Emerald"), ENCHANT_RUBY_OR_TOPAZ(8018, JewelleryEnchantmentType.LVL3_ENCHANTMENT, "Ruby"), ENCHANT_DIAMOND(8019, JewelleryEnchantmentType.LVL4_ENCHANTMENT, "Diamond"), ENCHANT_DRAGONSTONE(8020, JewelleryEnchantmentType.LVL5_ENCHANTMENT, "Dragonstone"), ENCHANT_ONYX(8021, JewelleryEnchantmentType.LVL6_ENCHANTMENT, "Onyx");
        private static final EnchantmentTab[] values = values();
        private final int tabId;
        private final JewelleryEnchantmentType type;
        private final String typeString;

        private static final EnchantmentTab get(final int id) {
            return CollectionUtils.findMatching(values, value -> value.tabId == id);
        }

        EnchantmentTab(int tabId, JewelleryEnchantmentType type, String typeString) {
            this.tabId = tabId;
            this.type = type;
            this.typeString = typeString;
        }
    }

    @Override
    public void handle() {
        bind("Info", (player, item, slotId) -> {
            final EnchantmentTabletItem.EnchantmentTab tab = Objects.requireNonNull(EnchantmentTab.get(item.getId()));
            player.sendMessage("You should use that tablet on a piece of " + tab.typeString + " jewellery.");
        });
    }

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final EnchantmentTabletItem.EnchantmentTab tab = Utils.getOrDefault(EnchantmentTab.get(from.getId()), EnchantmentTab.get(to.getId()));
        final Item jewellery = tab.tabId == from.getId() ? to : from;
        final int jewellerySlot = tab.tabId == from.getId() ? toSlot : fromSlot;
        final JewelleryEnchantment.JewelleryEnchantmentItem data = CollectionUtils.findMatching(JewelleryEnchantment.JewelleryEnchantmentItem.values, item -> item.getBase().getId() == jewellery.getId());
        assert data != null;
        player.setAnimation(data.getAnimation());
        player.setGraphics(data.getGraphics());
        player.getInventory().replaceItem(data.getProduct().getId(), 1, jewellerySlot);
        player.getInventory().deleteItem(new Item(tab.tabId, 1));
    }

    @Override
    public int[] getItems() {
        final IntArrayList list = new IntArrayList();
        for (final EnchantmentTabletItem.EnchantmentTab tab : EnchantmentTab.values) {
            list.add(tab.tabId);
        }
        return list.toIntArray();
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        final ArrayList<ItemOnItemAction.ItemPair> list = new ArrayList<ItemPair>();
        for (final EnchantmentTabletItem.EnchantmentTab tab : EnchantmentTab.values) {
            for (final JewelleryEnchantment.JewelleryEnchantmentItem jewellery : JewelleryEnchantment.JewelleryEnchantmentItem.values) {
                if (jewellery.getType() != tab.type) {
                    continue;
                }
                list.add(ItemPair.of(tab.tabId, jewellery.getBase().getId()));
            }
        }
        return list.toArray(new ItemPair[0]);
    }
}
