package com.zenyte.plugins.itemonitem;

import com.google.common.base.Preconditions;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Tommeh | 26-1-2019 | 16:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChipDarkEssenceBlockItemAction implements ItemOnItemAction {
    private static final Animation ANIMATION = new Animation(7202);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (player.getSkills().getLevel(SkillConstants.CRAFTING) < 38) {
            player.sendMessage("You need a Crafting level of at least 38 to chip dark essence blocks.");
            return;
        }
        final int blockSlot = player.getInventory().getItem(fromSlot).getId() == DARK_ESSENCE_BLOCK ? fromSlot : toSlot;
        OptionalInt optionalSlot = findFragmentsSlot(player);
        int slot = optionalSlot.isPresent() ? optionalSlot.getAsInt() : blockSlot;
        if (optionalSlot.isPresent()) {
            final Item item = player.getInventory().getItem(slot);
            if (item.getCharges() >= 111) {
                player.sendMessage("You can't hold any more fragments.");
                return;
            }
        }
        player.getInventory().deleteItem(new Item(DARK_ESSENCE_BLOCK));
        if (!optionalSlot.isPresent()) {
            player.getInventory().addItem(new Item(DARK_ESSENCE_FRAGMENTS));
            optionalSlot = findFragmentsSlot(player);
            slot = optionalSlot.orElseThrow(RuntimeException::new);
        }
        player.getInventory().getItem(slot).setCharges(Math.min(111, player.getInventory().getItem(slot).getCharges() + 4));
        player.setAnimation(ANIMATION);
        player.sendFilteredMessage("You chip the dark essence block into dark essence fragments.");
        player.getSkills().addXp(SkillConstants.CRAFTING, 8);
        player.getActionManager().setAction(new ChippingAction());
    }


    private static final class ChippingAction extends Action {
        @Override
        public boolean start() {
            delay(2);
            return true;
        }

        @Override
        public boolean process() {
            return true;
        }

        @Override
        public int processWithDelay() {
            final Inventory inventory = player.getInventory();
            if (!inventory.containsItem(DARK_ESSENCE_BLOCK) || !inventory.containsItem(CHISEL)) {
                return -1;
            }
            OptionalInt optionalSlot = findFragmentsSlot(player);
            if (optionalSlot.isPresent()) {
                final Item item = inventory.getItem(optionalSlot.getAsInt());
                if (item.getCharges() >= 111) {
                    return -1;
                }
            }
            inventory.deleteItem(new Item(DARK_ESSENCE_BLOCK));
            if (!optionalSlot.isPresent()) {
                inventory.addItem(new Item(DARK_ESSENCE_FRAGMENTS));
                optionalSlot = findFragmentsSlot(player);
                Preconditions.checkArgument(optionalSlot.isPresent());
            }
            inventory.getItem(optionalSlot.getAsInt()).setCharges(Math.min(111, inventory.getItem(optionalSlot.getAsInt()).getCharges() + 4));
            player.setAnimation(ANIMATION);
            player.sendFilteredMessage("You chip the dark essence block into dark essence fragments.");
            player.getSkills().addXp(SkillConstants.CRAFTING, 8);
            return 3;
        }
    }

    public static final OptionalInt findFragmentsSlot(@NotNull final Player player) {
        final Inventory inventory = player.getInventory();
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item != null && item.getId() == DARK_ESSENCE_FRAGMENTS) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    @Override
    public int[] getItems() {
        return new int[] {CHISEL, DARK_ESSENCE_BLOCK};
    }
}
