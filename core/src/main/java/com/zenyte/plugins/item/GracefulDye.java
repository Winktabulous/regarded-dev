package com.zenyte.plugins.item;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.SkillDialogue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 08/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GracefulDye extends ItemPlugin implements PairedItemOnItemPlugin {
    private static final int GRACEFUL_DYE = 2710;


    private enum RecolourDye {
        PURPLE, CYAN, YELLOW, RED, GREEN, WHITE, BLUE;

        RecolourDye() {
        }
    }


    private enum RegularGraceful {
        GRACEFUL_HOOD(ItemId.GRACEFUL_HOOD), GRACEFUL_TOP(ItemId.GRACEFUL_TOP), GRACEFUL_LEGS(ItemId.GRACEFUL_LEGS), GRACEFUL_CAPE(ItemId.GRACEFUL_CAPE), GRACEFUL_BOOTS(ItemId.GRACEFUL_BOOTS), GRACEFUL_GLOVES(ItemId.GRACEFUL_GLOVES);
        private final int itemId;
        private static final RegularGraceful[] values = values();

        RegularGraceful(int itemId) {
            this.itemId = itemId;
        }
    }


    private enum RecolouredGraceful {
        PURPLE_HOOD(RecolourDye.PURPLE, ItemId.GRACEFUL_HOOD_13579), CYAN_HOOD(RecolourDye.CYAN, ItemId.GRACEFUL_HOOD_13591), YELLOW_HOOD(RecolourDye.YELLOW, ItemId.GRACEFUL_HOOD_13603), RED_HOOD(RecolourDye.RED, ItemId.GRACEFUL_HOOD_13615), GREEN_HOOD(RecolourDye.GREEN, ItemId.GRACEFUL_HOOD_13627), WHITE_HOOD(RecolourDye.WHITE, ItemId.GRACEFUL_HOOD_13667), BLUE_HOOD(RecolourDye.BLUE, ItemId.GRACEFUL_HOOD_21061), PURPLE_TOP(RecolourDye.PURPLE, ItemId.GRACEFUL_TOP_13583), CYAN_TOP(RecolourDye.CYAN, ItemId.GRACEFUL_TOP_13595), YELLOW_TOP(RecolourDye.YELLOW, ItemId.GRACEFUL_TOP_13607), RED_TOP(RecolourDye.RED, ItemId.GRACEFUL_TOP_13619), GREEN_TOP(RecolourDye.GREEN, ItemId.GRACEFUL_TOP_13631), WHITE_TOP(RecolourDye.WHITE, ItemId.GRACEFUL_TOP_13671), BLUE_TOP(RecolourDye.BLUE, ItemId.GRACEFUL_TOP_21067), PURPLE_LEGS(RecolourDye.PURPLE, ItemId.GRACEFUL_LEGS_13585), CYAN_LEGS(RecolourDye.CYAN, ItemId.GRACEFUL_LEGS_13597), YELLOW_LEGS(RecolourDye.YELLOW, ItemId.GRACEFUL_LEGS_13609), RED_LEGS(RecolourDye.RED, ItemId.GRACEFUL_LEGS_13621), GREEN_LEGS(RecolourDye.GREEN, ItemId.GRACEFUL_LEGS_13633), WHITE_LEGS(RecolourDye.WHITE, ItemId.GRACEFUL_LEGS_13673), BLUE_LEGS(RecolourDye.BLUE, ItemId.GRACEFUL_LEGS_21070), PURPLE_CAPE(RecolourDye.PURPLE, ItemId.GRACEFUL_CAPE_13581), CYAN_CAPE(RecolourDye.CYAN, ItemId.GRACEFUL_CAPE_13593), YELLOW_CAPE(RecolourDye.YELLOW, ItemId.GRACEFUL_CAPE_13605), RED_CAPE(RecolourDye.RED, ItemId.GRACEFUL_CAPE_13617), GREEN_CAPE(RecolourDye.GREEN, ItemId.GRACEFUL_CAPE_13629), WHITE_CAPE(RecolourDye.WHITE, ItemId.GRACEFUL_CAPE_13669), BLUE_CAPE(RecolourDye.BLUE, ItemId.GRACEFUL_CAPE_21064), PURPLE_BOOTS(RecolourDye.PURPLE, ItemId.GRACEFUL_BOOTS_13589), CYAN_BOOTS(RecolourDye.CYAN, ItemId.GRACEFUL_BOOTS_13601), YELLOW_BOOTS(RecolourDye.YELLOW, ItemId.GRACEFUL_BOOTS_13613), RED_BOOTS(RecolourDye.RED, ItemId.GRACEFUL_BOOTS_13625), GREEN_BOOTS(RecolourDye.GREEN, ItemId.GRACEFUL_BOOTS_13637), WHITE_BOOTS(RecolourDye.WHITE, ItemId.GRACEFUL_BOOTS_13677), BLUE_BOOTS(RecolourDye.BLUE, ItemId.GRACEFUL_BOOTS_21076), PURPLE_GLOVES(RecolourDye.PURPLE, ItemId.GRACEFUL_GLOVES_13587), CYAN_GLOVES(RecolourDye.CYAN, ItemId.GRACEFUL_GLOVES_13599), YELLOW_GLOVES(RecolourDye.YELLOW, ItemId.GRACEFUL_GLOVES_13611), RED_GLOVES(RecolourDye.RED, ItemId.GRACEFUL_GLOVES_13623), GREEN_GLOVES(RecolourDye.GREEN, ItemId.GRACEFUL_GLOVES_13635), WHITE_GLOVES(RecolourDye.WHITE, ItemId.GRACEFUL_GLOVES_13675), BLUE_GLOVES(RecolourDye.BLUE, ItemId.GRACEFUL_GLOVES_21073);
        private final RecolourDye dye;
        private final int recolouredPiece;
        private static final RecolouredGraceful[] values = values();

        RecolouredGraceful(RecolourDye dye, int recolouredPiece) {
            this.dye = dye;
            this.recolouredPiece = recolouredPiece;
        }
    }


    public static final class GracefulCleaning implements PairedItemOnItemPlugin {
        @Override
        public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
            final Item cloth = from.getId() == ItemId.CLEANING_CLOTH ? from : to;
            final Item piece = cloth == to ? from : to;
            final int slot = piece.getDefinitions().getSlot();
            final GracefulDye.RegularGraceful regularPiece = CollectionUtils.findMatching(RegularGraceful.values, p -> ItemDefinitions.getOrThrow(p.itemId).getSlot() == slot);
            Preconditions.checkArgument(regularPiece != null);
            final int pieceSlot = piece == from ? fromSlot : toSlot;
            player.getInventory().replaceItem(regularPiece.itemId, 1, pieceSlot);
            player.sendMessage("You clean the " + piece.getName().toLowerCase() + ".");
        }

        @Override
        public ItemPair[] getMatchingPairs() {
            final ObjectArrayList<ItemOnItemAction.ItemPair> pairs = new ObjectArrayList<ItemPair>();
            for (final GracefulDye.RecolouredGraceful piece : RecolouredGraceful.values) {
                pairs.add(ItemPair.of(ItemId.CLEANING_CLOTH, piece.recolouredPiece));
            }
            return pairs.toArray(new ItemPair[0]);
        }
    }

    @Override
    public void handle() {
        bind("Info", (player, item, container, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(item, "The graceful dye can recolour " + Colour.RED.wrap("any one piece") + " of a Graceful set to any colour besides dark blue. The white outfit requires the player to have completed all of the achievement diaries.");
                item(item, "The piece can be reverted to normal by using a cleaning cloth on the said piece. The dye " + Colour.RED.wrap("will not") + " be refunded should you clean the piece.");
            }
        }));
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final ObjectArrayList<Item> list = new ObjectArrayList<Item>();
        final ObjectArrayList<GracefulDye.RecolourDye> colourList = new ObjectArrayList<RecolourDye>();
        final Item gracefulPiece = from.getId() == GRACEFUL_DYE ? to : from;
        final int slot = gracefulPiece.getDefinitions().getSlot();
        //Reconsidering the idea of unlockable graceful colour, might instead do agility arena.
        final boolean unlockedDarkBlue = player.getNumericAttribute("dark blue graceful unlocked").intValue() == 1;
        final boolean unlockedWhite = isWhiteUnlocked(player);
        for (final GracefulDye.RecolouredGraceful recolour : RecolouredGraceful.values) {
            final ItemDefinitions def = ItemDefinitions.getOrThrow(recolour.recolouredPiece);
            if (def.getSlot() != slot || (!unlockedDarkBlue && recolour.dye == RecolourDye.BLUE) || (!unlockedWhite && recolour.dye == RecolourDye.WHITE)) {
                continue;
            }
            list.add(new Item(recolour.recolouredPiece));
            colourList.add(recolour.dye);
        }
        player.getDialogueManager().start(new SkillDialogue(player, "What colour would you like to use?", list.toArray(new Item[0])) {
            @Override
            public void run(int slotId, int amount) {
                recolour(player, from, to, fromSlot, toSlot, colourList.get(slotId));
            }
            @Override
            public int getMaximumAmount() {
                return 1;
            }
        });
    }

    private static final boolean isWhiteUnlocked(@NotNull final Player player) {
        for (final DiaryReward reward : DiaryReward.VALUES) {
            if (!DiaryUtil.eligibleFor(reward, player)) {
                return false;
            }
        }
        return true;
    }

    private static final void recolour(@NotNull final Player player, @NotNull final Item from, @NotNull final Item to, final int fromSlot, final int toSlot, @NotNull final RecolourDye dye) {
        final Item dyeItem = from.getId() == GRACEFUL_DYE ? from : to;
        final Item gracefulPiece = dyeItem == from ? to : from;
        final int dyeSlot = dyeItem == from ? fromSlot : toSlot;
        final int gracefulSlot = dyeSlot == fromSlot ? toSlot : fromSlot;
        final Inventory inventory = player.getInventory();
        Preconditions.checkArgument(inventory.getItem(dyeSlot) == dyeItem);
        Preconditions.checkArgument(inventory.getItem(gracefulSlot) == gracefulPiece);
        final int gracefulEquipSlot = gracefulPiece.getDefinitions().getSlot();
        final GracefulDye.RecolouredGraceful recolourConst = CollectionUtils.findMatching(RecolouredGraceful.values, value -> value.dye == dye && ItemDefinitions.getOrThrow(value.recolouredPiece).getSlot() == gracefulEquipSlot);
        Preconditions.checkArgument(recolourConst != null);
        inventory.deleteItem(dyeSlot, dyeItem);
        inventory.replaceItem(recolourConst.recolouredPiece, 1, gracefulSlot);
        player.sendMessage("You pour the magical dye over your " + gracefulPiece.getName().toLowerCase() + " and recolour it " + dye.name().toLowerCase() + ".");
    }

    @Override
    public int[] getItems() {
        return new int[] {GRACEFUL_DYE};
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        final ObjectArrayList<ItemOnItemAction.ItemPair> pairs = new ObjectArrayList<ItemPair>();
        for (final GracefulDye.RegularGraceful piece : RegularGraceful.values) {
            pairs.add(ItemPair.of(GRACEFUL_DYE, piece.itemId));
        }
        return pairs.toArray(new ItemPair[0]);
    }
}
