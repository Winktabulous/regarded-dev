package com.zenyte.game.content.treasuretrails;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.collectionlog.CollectionLog;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.List;
import java.util.Optional;

/**
 * @author Kris | 25/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClueRewardInterface extends Interface {
    @Override
    protected void attach() {
        put(3, "Item list");
    }

    @Override
    public void open(Player player) {
        final Object loot = player.getTemporaryAttributes().remove("treasure trails loot");
        if (!(loot instanceof List)) {
            throw new IllegalStateException("No loot list provided.");
        }
        @SuppressWarnings("unchecked cast")
        final List<Item> rewards = (List<Item>) loot;
        final Container container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.BARROWS_CHEST, Optional.of(player));
        container.add(rewards.toArray(new Item[0]));
        container.setFullUpdate(true);
        player.getPacketDispatcher().sendUpdateItemContainer(container);
        player.getInterfaceHandler().sendInterface(this);
        player.getTemporaryAttributes().put("treasure trails rewards container", container);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Item list"), 0, 10, AccessMask.CLICK_OP10);
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        final Object container = player.getTemporaryAttributes().remove("treasure trails rewards container");
        if (!(container instanceof Container)) {
            return;
        }
        final Container typedContainer = (Container) container;
        final Inventory inventory = player.getInventory();
        final CollectionLog log = player.getCollectionLog();
        for (final Int2ObjectMap.Entry<Item> itemEntry : typedContainer.getItems().int2ObjectEntrySet()) {
            final Item item = itemEntry.getValue();
            if (item == null) {
                continue;
            }
            inventory.addOrDrop(item);
            log.add(item);
        }
    }

    @Override
    protected void build() {
        bind("Item list", (player, slotId, itemId, option) -> ItemUtil.sendItemExamine(player, itemId));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.CLUE_SCROLL_REWARD;
    }
}
