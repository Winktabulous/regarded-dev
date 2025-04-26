package com.zenyte.game.content.treasuretrails.npcs;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Kris | 03/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PatchyNPC extends NPCPlugin implements ItemOnNPCAction {

    private static final int CONNECT_COST = 500;

    private static final int DISCONNECT_COST = 600;

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        final PatchyNPC.SewableClothing clothing = Objects.requireNonNull(CollectionUtils.findMatching(SewableClothing.values, v -> v.sewnItem == item.getId()));
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Would you like to have that unsewn? That'll cost you " + DISCONNECT_COST + " coins.");
                options("Unsew the " + item.getName().toLowerCase() + "?", new DialogueOption("Yes", () -> unsew(player, clothing)), new DialogueOption("No"));
            }
        });
    }

    private final void unsew(@NotNull final Player player, @NotNull final SewableClothing clothing) {
        player.getDialogueManager().finish();
        final Inventory inventory = player.getInventory();
        if (!inventory.hasFreeSlots()) {
            player.getDialogueManager().start(new NPCChat(player, 3215, "Arrr.. I'm not sure you can hold the unsewn " + "pieces right now."));
            return;
        }
        if (inventory.getAmountOf(ItemId.COINS_995) < DISCONNECT_COST || !inventory.containsItem(clothing.sewnItem, 1)) {
            player.getDialogueManager().start(new NPCChat(player, 3215, "I'll need " + DISCONNECT_COST + " coins to unsew that."));
            return;
        }
        inventory.deleteItem(clothing.sewnItem, 1);
        inventory.deleteItem(ItemId.COINS_995, DISCONNECT_COST);
        inventory.addOrDrop(new Item(clothing.baseItem, 1));
        inventory.addOrDrop(new Item(clothing.attachmentItem, 1));
        player.getDialogueManager().start(new NPCChat(player, 3215, "There you go."));
    }

    @Override
    public Object[] getItems() {
        final ObjectArrayList<Object> list = new ObjectArrayList<>();
        for (final PatchyNPC.SewableClothing value : SewableClothing.values) {
            list.add(value.sewnItem);
        }
        return list.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 3215 };
    }

    private enum SewableClothing {

        WHITE_BANDANA_EYEPATCH(ItemId.RIGHT_EYE_PATCH, ItemId.PIRATE_BANDANA, ItemId.BANDANA_EYEPATCH),
        RED_BANDANA_EYEPATCH(ItemId.RIGHT_EYE_PATCH, ItemId.PIRATE_BANDANA_7124, ItemId.BANDANA_EYEPATCH_8925),
        BLUE_BANDANA_EYEPATCH(ItemId.RIGHT_EYE_PATCH, ItemId.PIRATE_BANDANA_7130, ItemId.BANDANA_EYEPATCH_8926),
        BROWN_BANDANA_EYEPATCH(ItemId.RIGHT_EYE_PATCH, ItemId.PIRATE_BANDANA_7136, ItemId.BANDANA_EYEPATCH_8927),
        HAT_EYEPATCH(ItemId.RIGHT_EYE_PATCH, ItemId.PIRATES_HAT, ItemId.HAT_EYEPATCH),
        CRABCLAW_HOOK(ItemId.CRAB_CLAW, ItemId.PIRATES_HOOK, ItemId.CRABCLAW_HOOK),
        CAVALIER_AND_MASK(ItemId.BLACK_CAVALIER, ItemId.HIGHWAYMAN_MASK, ItemId.CAVALIER_MASK_11280),
        BERET_MASK(ItemId.MIME_MASK, ItemId.BLACK_BERET, ItemId.BERET_MASK_11282),
        PARTYHAT_AND_SPECS(ItemId.BLUE_PARTYHAT, ItemId.SAGACIOUS_SPECTACLES, ItemId.PARTYHAT__SPECS),
        TOP_HAT_AND_MONOCLE(ItemId.TOP_HAT, ItemId.MONOCLE, ItemId.TOP_HAT__MONOCLE),
        PIRATE_HAT_AND_PATCH(ItemId.BIG_PIRATE_HAT, ItemId.RIGHT_EYE_PATCH, ItemId.PIRATE_HAT__PATCH);

        private final int baseItem;

        private final int attachmentItem;

        private final int sewnItem;

        private static final SewableClothing[] values = values();

        SewableClothing(int baseItem, int attachmentItem, int sewnItem) {
            this.baseItem = baseItem;
            this.attachmentItem = attachmentItem;
            this.sewnItem = sewnItem;
        }
    }

    @Override
    public void handle() {
        bind("Sew", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("What would you like to have me sew?");
                options(TITLE, new DialogueOption("White bandana eyepatch", () -> sew(player, SewableClothing.WHITE_BANDANA_EYEPATCH)), new DialogueOption("Red bandana eyepatch", () -> sew(player, SewableClothing.RED_BANDANA_EYEPATCH)), new DialogueOption("Blue bandana eyepatch", () -> sew(player, SewableClothing.BLUE_BANDANA_EYEPATCH)), new DialogueOption("Brown bandana eyepatch", () -> sew(player, SewableClothing.BROWN_BANDANA_EYEPATCH)), new DialogueOption("More options", key(3)));
                options(TITLE, new DialogueOption("Hat eyepatch", () -> sew(player, SewableClothing.HAT_EYEPATCH)), new DialogueOption("Crabclaw hook", () -> sew(player, SewableClothing.CRABCLAW_HOOK)), new DialogueOption("Cavalier and mask", () -> sew(player, SewableClothing.CAVALIER_AND_MASK)), new DialogueOption("Beret mask", () -> sew(player, SewableClothing.BERET_MASK)), new DialogueOption("More options", key(4)));
                options(TITLE, new DialogueOption("Partyhat & specs", () -> sew(player, SewableClothing.PARTYHAT_AND_SPECS)), new DialogueOption("Top hat & monocle", () -> sew(player, SewableClothing.TOP_HAT_AND_MONOCLE)), new DialogueOption("Pirate hat & patch", () -> sew(player, SewableClothing.PIRATE_HAT_AND_PATCH)), new DialogueOption("More options", key(2)));
            }
        }));
    }

    private final void sew(@NotNull final Player player, @NotNull final SewableClothing piece) {
        player.getDialogueManager().finish();
        final Inventory inventory = player.getInventory();
        if (inventory.getAmountOf(ItemId.COINS_995) < CONNECT_COST || !inventory.containsItem(piece.baseItem, 1) || !inventory.containsItem(piece.attachmentItem, 1)) {
            final String baseName = ItemDefinitions.getOrThrow(piece.baseItem).getName().toLowerCase();
            final String attachmentName = ItemDefinitions.getOrThrow(piece.attachmentItem).getName().toLowerCase();
            final String completeName = ItemDefinitions.getOrThrow(piece.sewnItem).getName().toLowerCase();
            player.getDialogueManager().start(new NPCChat(player, 3215, "I'll need " + CONNECT_COST + " coins, " + Utils.getAOrAn(baseName) + " " + baseName + " and " + Utils.getAOrAn(attachmentName) + " " + attachmentName + " to sew the " + completeName + "."));
            return;
        }
        inventory.deleteItem(piece.baseItem, 1);
        inventory.deleteItem(piece.attachmentItem, 1);
        inventory.deleteItem(ItemId.COINS_995, CONNECT_COST);
        inventory.addItem(piece.sewnItem, 1);
        player.getDialogueManager().start(new NPCChat(player, 3215, "There you go."));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 3215 };
    }
}
