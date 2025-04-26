package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.utils.Ordinal;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Kris | 14/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GuthixRestTeaMixing implements PairedItemOnItemPlugin {

    /**
     * Partial phases of the tea. There is no correlation between the partial tea item ids and the ingredients, so bitwise operations cannot be performed here.
     */
    @Ordinal
    public enum PartialTea {
        HERB_TEA_MIX_1(ItemId.HARRALANDER), HERB_TEA_MIX_2(ItemId.GUAM_LEAF), HERB_TEA_MIX_3(ItemId.MARRENTILL), HERB_TEA_MIX_4(ItemId.HARRALANDER, ItemId.MARRENTILL), HERB_TEA_MIX_5(ItemId.HARRALANDER, ItemId.GUAM_LEAF), HERB_TEA_MIX_6(ItemId.GUAM_LEAF, ItemId.GUAM_LEAF), HERB_TEA_MIX_7(ItemId.GUAM_LEAF, ItemId.MARRENTILL), HERB_TEA_MIX_8(ItemId.HARRALANDER, ItemId.MARRENTILL, ItemId.GUAM_LEAF), HERB_TEA_MIX_9(ItemId.GUAM_LEAF, ItemId.GUAM_LEAF, ItemId.MARRENTILL), HERB_TEA_MIX_10(ItemId.GUAM_LEAF, ItemId.GUAM_LEAF, ItemId.HARRALANDER), COMPLETE_MIX(ItemId.GUAM_LEAF, ItemId.GUAM_LEAF, ItemId.MARRENTILL, ItemId.HARRALANDER);
        private static final PartialTea[] values = values();
        private final int[] ingredients;
        private final int teaId;

        PartialTea(@NotNull final int... ingredients) {
            this.ingredients = ingredients;
            Arrays.sort(this.ingredients);
            this.teaId = ingredients.length == 4 ? ItemId.GUTHIX_REST3 : (ItemId.HERB_TEA_MIX + (ordinal() << 1));
        }

        public int getTeaId() {
            return teaId;
        }
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (player.getSkills().getLevel(SkillConstants.HERBLORE) < 18) {
            player.getDialogueManager().start(new ItemChat(player, new Item(ItemId.GUTHIX_REST3), "You need a Herblore level of at least 18 to mix a Guthix Rest Tea."));
            return;
        }
        final Item herb = isHerb(from) ? from : to;
        final Item mix = herb == from ? to : from;
        final int[] existingArray = mix.getId() == ItemId.CUP_OF_HOT_WATER ? new int[0] : Objects.requireNonNull(CollectionUtils.findMatching(PartialTea.values, tea -> tea.teaId == mix.getId())).ingredients;
        final IntArrayList ingredientsList = new IntArrayList(existingArray);
        ingredientsList.add(herb.getId());
        final int[] ingredientsArray = ingredientsList.toIntArray();
        Arrays.sort(ingredientsArray);
        final GuthixRestTeaMixing.PartialTea upgradedTea = CollectionUtils.findMatching(PartialTea.values, tea -> Arrays.equals(tea.ingredients, ingredientsArray));
        if (upgradedTea == null) {
            player.sendFilteredMessage("Nothing interesting happens.");
            return;
        }
        final Inventory inventory = player.getInventory();
        inventory.set(mix == from ? fromSlot : toSlot, new Item(upgradedTea == PartialTea.COMPLETE_MIX ? getCompleteTeaId(player) : upgradedTea.teaId));
        inventory.deleteItem(herb == from ? fromSlot : toSlot, herb);
        player.sendFilteredMessage("You place the " + herb.getName().toLowerCase().replace(" leaf", "") + " into the steamy mixture" + (upgradedTea == PartialTea.COMPLETE_MIX ? " and make Guthix Rest Tea." : "."));
        player.getSkills().addXp(SkillConstants.HERBLORE, 13.5F + (ingredientsList.size() * 0.5F));
    }

    /**
     * Gets the complete guthix rest tea's item id based on whether or not the amulet of chemistry effect activates.
     * @param player the player mixing the tea.
     * @return the item id of the guthix rest tea.
     */
    private final int getCompleteTeaId(@NotNull final Player player) {
        if (player.getEquipment().getId(EquipmentSlot.AMULET) == 21163) {
            if (Utils.randomDouble() < 0.05F) {
                final int uses = player.getNumericAttribute("amulet of chemistry uses").intValue() + 1;
                player.addAttribute("amulet of chemistry uses", uses % 5);
                if (uses == 5) {
                    player.getEquipment().set(EquipmentSlot.AMULET, null);
                    player.sendMessage("Your amulet of chemistry grants you an extra dose. " + Colour.RED.wrap("It then crumbles to dust."));
                } else {
                    player.sendFilteredMessage("Your amulet of chemistry grants you an extra dose. " + Colour.RED.wrap("It has " + (5 - uses) + " charge" + (uses == 9 ? "" : "s") + " left."));
                }
                return ItemId.GUTHIX_REST4;
            }
        }
        return ItemId.GUTHIX_REST3;
    }

    /**
     * Checks to see whether the item passed is a guam leaf, a marrentill or a harralander.
     *
     * @param item the item passed.
     * @return whether or not the item is one of the three aforementioned clean herbs.
     */
    private boolean isHerb(@NotNull final Item item) {
        final int id = item.getId();
        return id == ItemId.GUAM_LEAF || id == ItemId.MARRENTILL || id == ItemId.HARRALANDER;
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        final ObjectArrayList<ItemOnItemAction.ItemPair> pairs = new ObjectArrayList<ItemPair>();
        for (final GuthixRestTeaMixing.PartialTea partialTea : PartialTea.values) {
            pairs.add(ItemPair.of(ItemId.GUAM_LEAF, partialTea.teaId));
            pairs.add(ItemPair.of(ItemId.MARRENTILL, partialTea.teaId));
            pairs.add(ItemPair.of(ItemId.HARRALANDER, partialTea.teaId));
        }
        pairs.add(ItemPair.of(ItemId.GUAM_LEAF, ItemId.CUP_OF_HOT_WATER));
        pairs.add(ItemPair.of(ItemId.MARRENTILL, ItemId.CUP_OF_HOT_WATER));
        pairs.add(ItemPair.of(ItemId.HARRALANDER, ItemId.CUP_OF_HOT_WATER));
        return pairs.toArray(new ItemPair[0]);
    }
}
