package com.zenyte.plugins.item;

import com.google.common.primitives.Ints;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.utils.StringUtilities;
import mgi.utilities.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Kris | 06/06/2019 18:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RunecraftingPouch extends ItemPlugin {
    public static final List<Integer> pouches = Arrays.stream(Pouch.POUCHES).mapToInt(p -> p.id).boxed().collect(Collectors.toList());


    private enum Pouch {
        SMALL(ItemId.SMALL_POUCH, 3, 0), MEDIUM(ItemId.MEDIUM_POUCH, 6, 25), LARGE(ItemId.LARGE_POUCH, 9, 50), GIANT(ItemId.GIANT_POUCH, 12, 75);
        private final int id;
        private final int capacity;
        private final int level;
        private static final Pouch[] POUCHES = values();

        Pouch(int id, int capacity, int level) {
            this.id = id;
            this.capacity = capacity;
            this.level = level;
        }
    }

    @Override
    public void handle() {
        bind("Check", (player, item, container, slotId) -> {
            final int pureEssence = item.getNumericAttribute("pure essence").intValue();
            final int runeEssence = item.getNumericAttribute("rune essence").intValue();
            final int daeyaltEssence = item.getNumericAttribute("daeyalt essence").intValue();
            player.sendMessage("Your pouch currently contains " + runeEssence + " rune essence, " + pureEssence + " pure essence and "+daeyaltEssence+" daeyalt essence.");
        });
        bind("Fill", RunecraftingPouch::fill);
        bind("Empty", (player, item, container, slotId) -> {
            final int pureEssence = item.getNumericAttribute("pure essence").intValue();
            final int runeEssence = item.getNumericAttribute("rune essence").intValue();
            final int daeyaltEssence = item.getNumericAttribute("daeyalt essence").intValue();
            if (pureEssence <= 0 && runeEssence <= 0 && daeyaltEssence <= 0) {
                player.sendMessage("Your pouch is empty.");
                return;
            }
            if (pureEssence > 0) {
                final int succeededPure = player.getInventory().addItem(new Item(ItemId.PURE_ESSENCE, pureEssence)).getSucceededAmount();
                item.setAttribute("pure essence", pureEssence - succeededPure);
            }
            if (runeEssence > 0) {
                final int succeededRune = player.getInventory().addItem(new Item(ItemId.RUNE_ESSENCE, runeEssence)).getSucceededAmount();
                item.setAttribute("rune essence", runeEssence - succeededRune);
            }
            if(daeyaltEssence > 0) {
                final int succeededDaeyalt = player.getInventory().addItem(new Item(ItemId.DAEYALT_ESSENCE, daeyaltEssence)).getSucceededAmount();
                item.setAttribute("daeyalt essence", daeyaltEssence - succeededDaeyalt);
            }
        });
    }

    public static void fill(final Player player, final Item item, final Container container, final int slotId) {
        final RunecraftingPouch.Pouch pouch = Objects.requireNonNull(CollectionUtils.findMatching(Pouch.POUCHES, p -> p.id == item.getId()));
        if (player.getSkills().getLevelForXp(SkillConstants.RUNECRAFTING) < pouch.level) {
            player.getDialogueManager().start(new ItemChat(player, item, "You need a Runecrafting level of at least " + pouch.level + " to use this pouch."));
            return;
        }
        final int currentCount = item.getNumericAttribute("pure essence").intValue() + item.getNumericAttribute("rune essence").intValue() + item.getNumericAttribute("daeyalt essence").intValue();
        if (currentCount >= pouch.capacity) {
            player.sendMessage("Your pouch is already full of essence.");
            return;
        }
        final int pureInInventory = container.getAmountOf(ItemId.PURE_ESSENCE);
        final int runeInInventory = container.getAmountOf(ItemId.RUNE_ESSENCE);
        final int daeyaltInInventory = container.getAmountOf(ItemId.DAEYALT_ESSENCE);
        if (pureInInventory == 0 && runeInInventory == 0 && daeyaltInInventory == 0) {
            player.sendMessage("You have no essence in your " + StringUtilities.formatEnum(container.getType()) + " to fill the pouch with.");
            return;
        }
        final int addablePure = Math.min(pouch.capacity - currentCount, pureInInventory);
        final int addableRune = Math.min(pouch.capacity - currentCount - addablePure, runeInInventory);
        final int addableDaeyalt = Math.min(pouch.capacity - currentCount - addablePure - addableRune, daeyaltInInventory);
        if (addablePure == 0 && addableRune == 0 && addableDaeyalt == 0) {
            return;
        }
        final boolean isBank = container.getType() == ContainerType.BANK;
        final int addedPure = isBank ? player.getBank().remove(new Item(ItemId.PURE_ESSENCE, addablePure)).getSucceededAmount() : player.getInventory().deleteItem(new Item(ItemId.PURE_ESSENCE, addablePure)).getSucceededAmount();
        final int addedRune = isBank ? player.getBank().remove(new Item(ItemId.RUNE_ESSENCE, addableRune)).getSucceededAmount() : player.getInventory().deleteItem(new Item(ItemId.RUNE_ESSENCE, addableRune)).getSucceededAmount();
        final int addedDaeyalt = isBank ? player.getBank().remove(new Item(ItemId.DAEYALT_ESSENCE, addableDaeyalt)).getSucceededAmount() : player.getInventory().deleteItem(new Item(ItemId.DAEYALT_ESSENCE, addableDaeyalt)).getSucceededAmount();
        if (isBank) {
            player.getBank().refreshContainer();
        }
        item.setAttribute("pure essence", item.getNumericAttribute("pure essence").intValue() + addedPure);
        item.setAttribute("rune essence", item.getNumericAttribute("rune essence").intValue() + addedRune);
        item.setAttribute("daeyalt essence", item.getNumericAttribute("daeyalt essence").intValue() + addedDaeyalt);
    }

    @Override
    public int[] getItems() {
        return Ints.toArray(pouches);
    }
}
