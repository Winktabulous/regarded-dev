package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import mgi.types.config.enums.Enums;
import org.jetbrains.annotations.NotNull;

import java.util.BitSet;
import java.util.List;

/**
 * @author Kris | 10/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DiangoItemRetrievalService extends Interface {
    @Override
    protected void attach() {
        put(1, "Title");
        put(3, "Interact with item");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Title"), "Diango's Holiday Item Retrieval");
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Interact with item"), 0, Enums.DIANGO_ITEM_RETRIEVAL.getSize(), AccessMask.CLICK_OP1, AccessMask.CLICK_OP10);
        if (!refresh(player)) {
            player.stop(Player.StopType.INTERFACES);
        }
    }

    private boolean refresh(@NotNull final Player player) {
        final List<Integer> trackedHolidayItems = player.getTrackedHolidayItems();
        final BitSet set = new BitSet(trackedHolidayItems.size());
        for (final Int2IntMap.Entry entry : Enums.DIANGO_ITEM_RETRIEVAL.getValues().int2IntEntrySet()) {
            if (!trackedHolidayItems.contains(entry.getIntValue()) || player.containsItem(entry.getIntValue())) {
                continue;
            }
            set.set(entry.getIntKey());
        }
        if (set.isEmpty()) {
            return false;
        }
        final int[] varps = new int[4];
        for (int i = 0; i < 4; i++) {
            final long[] values = set.get(i * 32, (i + 1) * 32).toLongArray();
            varps[i] = values.length == 0 ? 0 : (int) values[0];
        }
        player.getPacketDispatcher().sendClientScript(450, varps[0], varps[1], varps[2], varps[3], "Diango's Holiday Item Retrieval");
        return true;
    }

    @Override
    protected void build() {
        bind("Interact with item", (player, slotId, itemId, option) -> {
            if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId);
                return;
            }
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some more free inventory space to do that.");
                return;
            }
            final List<Integer> trackedItems = player.getTrackedHolidayItems();
            if (!trackedItems.contains(itemId)) {
                return;
            }
            if (player.containsItem(itemId)) {
                return;
            }
            player.getInventory().addOrDrop(new Item(itemId));
            if (!refresh(player)) {
                player.stop(Player.StopType.INTERFACES);
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.DIANGO_ITEM_RETRIEVAL;
    }
}
