package com.zenyte.game.content.skills.hunter.plugins;

import com.zenyte.game.content.skills.hunter.node.BirdHouseType;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.DoubleItemChat;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.SkillDialogue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 24/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClockworkOnLogsPlugin implements PairedItemOnItemPlugin {
    private final ItemPair chiselOnClockworkPair = ItemPair.of(ItemId.CHISEL, ItemId.CLOCKWORK);
    private final ItemPair hammerOnClockworkPair = ItemPair.of(ItemId.HAMMER, ItemId.CLOCKWORK);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        //If the type is ambiguous, open the menu with all the options.
        if (matches(chiselOnClockworkPair, from, to) || matches(hammerOnClockworkPair, from, to)) {
            sendAmbiguousBirdhouseCreationMenu(player);
            return;
        }
        final BirdHouseType birdhouse = BirdHouseType.findThroughLogs((from.getId() == ItemId.CLOCKWORK || from.getId() == ItemId.CHISEL || from.getId() == ItemId.HAMMER) ? to.getId() : from.getId()).orElseThrow(RuntimeException::new);
        final Inventory inventory = player.getInventory();
        final Skills skills = player.getSkills();
        if (skills.getLevel(SkillConstants.CRAFTING) < birdhouse.getCraftingRequirement()) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Crafting level of at least " + birdhouse.getCraftingRequirement() + " to create this bird house."));
            return;
        }
        if (!inventory.containsItem(ItemId.HAMMER) || !inventory.containsItem(ItemId.CHISEL)) {
            player.getDialogueManager().start(new DoubleItemChat(player, new Item(ItemId.HAMMER), new Item(ItemId.CHISEL), "You need a hammer and chisel to make a birdhouse trap."));
            return;
        }
        final int createableAmount = birdhouse.getCreateableAmountThroughMaterials(player);
        if (createableAmount <= 1) {
            player.getActionManager().setAction(new BirdhouseCreationAction(birdhouse, 1));
        } else {
            sendBirdhouseCreationMenu(player, birdhouse, createableAmount);
        }
    }

    private final void sendAmbiguousBirdhouseCreationMenu(@NotNull final Player player) {
        final Item[] array = BirdHouseType.getAmbiguousBirdhouseMenu().toArray(new Item[0]);
        player.getDialogueManager().start(new SkillDialogue(player, "Which birdhouse do you wish to make?", array) {
            @Override
            public void run(int slotId, int amount) {
                final BirdHouseType birdhouse = BirdHouseType.findThroughBirdhouse(array[slotId].getId()).orElseThrow(RuntimeException::new);
                player.getActionManager().setAction(new BirdhouseCreationAction(birdhouse, amount));
            }
        });
    }

    private final void sendBirdhouseCreationMenu(@NotNull final Player player, @NotNull final BirdHouseType birdhouse, final int maximumQuantity) {
        player.getDialogueManager().start(new SkillDialogue(player, "How many do you wish to make?", new Item(birdhouse.getBirdhouseId())) {
            @Override
            public void run(int slotId, int amount) {
                player.getActionManager().setAction(new BirdhouseCreationAction(birdhouse, amount));
            }
            @Override
            public int getMaximumAmount() {
                return maximumQuantity;
            }
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        final ObjectArrayList<ItemOnItemAction.ItemPair> pairs = new ObjectArrayList<ItemPair>();
        for (final BirdHouseType birdhouse : BirdHouseType.getValues()) {
            pairs.add(ItemPair.of(ItemId.CLOCKWORK, birdhouse.getLogsId()));
            pairs.add(ItemPair.of(ItemId.HAMMER, birdhouse.getLogsId()));
            pairs.add(ItemPair.of(ItemId.CHISEL, birdhouse.getLogsId()));
        }
        pairs.add(chiselOnClockworkPair);
        pairs.add(hammerOnClockworkPair);
        return pairs.toArray(new ItemPair[0]);
    }
}
