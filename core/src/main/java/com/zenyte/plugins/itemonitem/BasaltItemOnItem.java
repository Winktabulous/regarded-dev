package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.SkillDialogue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.CollectionUtils;

import java.util.List;

/**
 * @author Kris | 10/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BasaltItemOnItem implements PairedItemOnItemPlugin {

    private enum BasaltType {
        ICY_BASALT(22599, new Item(22603), new Item(22595, 3), new Item(22593, 1)),
        STONY_BASALT(22601, new Item(22603), new Item(22597, 3), new Item(22593, 1));

        private static final BasaltType[] values = values();
        private final int basaltId;
        private final Item[] requirements;

        BasaltType(final int basaltId, final Item... requirements) {
            this.basaltId = basaltId;
            this.requirements = requirements;
        }
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Inventory inventory = player.getInventory();
        final List<Item> list = new ObjectArrayList<>();
        for (final BasaltType value : BasaltType.values) {
            if (!inventory.containsItems(value.requirements)) {
                continue;
            }
            list.add(new Item(value.basaltId));
        }
        if (list.isEmpty()) {
            player.sendMessage("You do not have enough salts to empower the basalt.");
            return;
        }

        player.getDialogueManager().start(new SkillDialogue(player, "What would you like to make?", list.toArray(new Item[0])) {
            @Override
            public void run(int slotId, int amount) {
                final Item produce = list.get(slotId);
                assert produce != null;
                final BasaltType basaltType = CollectionUtils.findMatching(BasaltType.values, type -> type.basaltId == produce.getId());
                assert basaltType != null;
                int maximumQuantity = Integer.MAX_VALUE;
                for (final Item item : basaltType.requirements) {
                    final int invQuantity = inventory.getAmountOf(item.getId()) / item.getAmount();
                    if (invQuantity < maximumQuantity) {
                        maximumQuantity = invQuantity;
                    }
                }
                final int quantity = Math.min(amount, maximumQuantity);
                if (quantity <= 0) {
                    player.sendMessage("You do not have enough salts to empower the basalt.");
                    return;
                }
                for (final Item item : basaltType.requirements) {
                    inventory.deleteItem(new Item(item.getId(), item.getAmount() * quantity));
                }
                inventory.addOrDrop(new Item(basaltType.basaltId, quantity));
                if (quantity < amount) {
                    player.sendMessage("You do not have enough salts to empower more basalt.");
                }
            }
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[]{
                ItemPair.of(22603, 22595), ItemPair.of(22603, 22593), ItemPair.of(22603, 22597),
        };
    }
}
