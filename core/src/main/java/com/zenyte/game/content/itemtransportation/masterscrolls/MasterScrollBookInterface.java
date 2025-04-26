package com.zenyte.game.content.itemtransportation.masterscrolls;

import com.google.common.base.Preconditions;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.utils.Ordinal;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Kris | 18/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MasterScrollBookInterface extends Interface {
    private static final int FAVOURITE_VARBIT = 5685;
    static final int MAXIMUM_TELEPORTS_COUNT = 1000;

    @Override
    protected void attach() {
        put(5, "Nardah");
        put(9, "Digsite");
        put(13, "Feldip Hills");
        put(17, "Lunar Isle");
        put(21, "Mort'ton");
        put(25, "Pest Control");
        put(29, "Piscatoris");
        put(33, "Tai Bwo Wannai");
        put(37, "Elf Camp");
        put(41, "Mos Le'Harmless");
        put(45, "Lumberyard");
        put(49, "Zul-andra");
        put(53, "Key Master");
        put(57, "Revenant caves");
        put(61, "Watson");
    }

    @Override
    public void open(Player player) {
        final Object scrollBook = player.getTemporaryAttributes().get("Master scroll book item attr");
        if (!(scrollBook instanceof Item)) {
            return;
        }
        final Item book = (Item) scrollBook;
        player.getInterfaceHandler().sendInterface(this);
        for (final ScrollBookTeleport teleport : ScrollBookTeleport.values) {
            refresh(player, book, teleport);
        }
        refreshFavourite(player);
    }

    private final void refresh(@NotNull final Player player, @NotNull final Item book, @NotNull final ScrollBookTeleport teleport) {
        final VarManager vm = player.getVarManager();
        final int count = Math.max(0, Math.min(MAXIMUM_TELEPORTS_COUNT, getCount(book, teleport.getScrollId())));
        if (teleport != ScrollBookTeleport.WATSON) {
            vm.sendBit(teleport.getVarbit(), count);
        } else {
            vm.sendBit(8252, count >> 8);
            vm.sendBit(8253, count & 255);
        }
        if (book.getAttributes() == null) {
            book.setId(ItemId.MASTER_SCROLL_BOOK_EMPTY);
            player.getInventory().refreshAll();
        }
    }

    private final void refreshFavourite(@NotNull final Player player) {
        player.getVarManager().sendBit(FAVOURITE_VARBIT, player.getNumericAttribute(favouriteKey).intValue());
    }

    private final void setFavourite(@NotNull final Player player, @NotNull final ScrollBookTeleport teleport) {
        player.getInterfaceHandler().closeInterfaces();
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                final String label = StringFormatUtil.formatString(teleport.toString());
                options("Set " + label + " teleport as your default teleport?", new DialogueOption("Yes", () -> {
                    player.sendMessage("Your default Master Scroll Book teleport has been set to " + label + ".");
                    player.addAttribute(favouriteKey, teleport.ordinal() + 1);
                    GameInterface.MASTER_SCROLL_BOOK.open(player);
                }), new DialogueOption("No"));
            }
        });
    }

    static final void removeFavourite(@NotNull final Player player) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Remove your default teleport?", new DialogueOption("Yes", () -> {
                    player.addAttribute(favouriteKey, 0);
                    player.sendMessage("You remove your default Master Scroll book teleport.");
                }), new DialogueOption("No"));
            }
        });
    }

    static final Optional<ScrollBookTeleport> getFavourite(@NotNull final Player player) {
        final int favouriteIndex = player.getNumericAttribute(favouriteKey).intValue();
        if (favouriteIndex == 0) {
            return Optional.empty();
        }
        return Optional.of(ScrollBookTeleport.values.get(favouriteIndex - 1));
    }

    private static final String keyPrefix = "Master scroll book scroll count ";
    private static final String favouriteKey = "Master scroll book favourite teleport index";

    static final int getCount(@NotNull final Item book, final int scrollItemId) {
        return book.getNumericAttribute(keyPrefix + scrollItemId).intValue();
    }

    static final void setCount(@NotNull final Item book, final int scrollItemId, final int count) {
        book.setAttribute(keyPrefix + scrollItemId, count);
    }

    @Override
    protected void build() {
        bind("Nardah", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.NARDAH, option));
        bind("Digsite", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.DIGSITE, option));
        bind("Feldip Hills", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.FELDIP_HILLS, option));
        bind("Lunar Isle", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.LUNAR_ISLE, option));
        bind("Mort'ton", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.MORTTON, option));
        bind("Pest Control", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.PEST_CONTROL, option));
        bind("Piscatoris", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.PISCATORIS, option));
        bind("Tai Bwo Wannai", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.TAI_BWO_WANNAI, option));
        bind("Elf Camp", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.ELF_CAMP, option));
        bind("Mos Le'Harmless", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.MOS_LE_HARMLESS, option));
        bind("Lumberyard", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.LUMBERYARD, option));
        bind("Zul-andra", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.ZUL_ANDRA, option));
        bind("Key Master", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.KEY_MASTER, option));
        bind("Revenant caves", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.REVENANT_CAVES, option));
        bind("Watson", (player, slotId, itemId, option) -> handle(player, ScrollBookTeleport.WATSON, option));
    }

    private final void handle(@NotNull final Player player, @NotNull final ScrollBookTeleport teleport, final int option) {
        final Object scrollBook = player.getTemporaryAttributes().get("Master scroll book item attr");
        if (!(scrollBook instanceof Item)) {
            return;
        }
        final Item book = (Item) scrollBook;
        switch (ScrollOption.values.get(option - 1)) {
        case ACTIVATE: 
            activate(player, book, teleport);
            break;
        case SET_AS_DEFAULT: 
            setFavourite(player, teleport);
            break;
        case REMOVE: 
            final int count = getCount(book, teleport.getScrollId());
            if (count <= 0) {
                player.sendMessage("You don't have any " + ItemDefinitions.getOrThrow(teleport.getScrollId()).getName() + "s to remove.");
                return;
            }
            player.sendInputInt("How many scrolls would you like to remove? (0-" + count + ")", value -> {
                Preconditions.checkArgument(player.getInventory().containsItem(book));
                if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(teleport.getScrollId(), 1)) {
                    player.sendMessage("You don't have enough free inventory space to remove any of the scrolls.");
                    return;
                }
                //Reobtain the count in case it has changed inbetween inputs.
                final int currentCount = getCount(book, teleport.getScrollId());
                final int removableCount = Math.max(0, Math.min(currentCount, value));
                if (removableCount <= 0) {
                    return;
                }
                setCount(book, teleport.getScrollId(), currentCount - removableCount);
                player.getInventory().addOrDrop(new Item(teleport.getScrollId(), removableCount));
                player.sendMessage("You remove " + removableCount + " " + ItemDefinitions.getOrThrow(teleport.getScrollId()).getName() + (removableCount == 1 ? "" : "s") + " from your Master Scroll Book.");
                refresh(player, book, teleport);
            });
            break;
        }
    }

    public static final List<Item> toItemList(@NotNull final Item book) {
        final ObjectArrayList<Item> list = new ObjectArrayList<Item>();
        for (final ScrollBookTeleport teleport : ScrollBookTeleport.values) {
            final int count = getCount(book, teleport.getScrollId());
            if (count > 0) {
                list.add(new Item(teleport.getScrollId(), count));
            }
        }
        return list;
    }

    private static final boolean isEmpty(@NotNull final Item scrollBook) {
        final Map<String, Object> attrs = scrollBook.getAttributes();
        return attrs == null || attrs.isEmpty();
    }

    static final void activate(@NotNull final Player player, @NotNull final Item book, @NotNull final ScrollBookTeleport teleport) {
        final int count = getCount(book, teleport.getScrollId());
        if (count <= 0) {
            player.sendMessage("You don't have any " + ItemDefinitions.getOrThrow(teleport.getScrollId()).getName() + "s in your Master Scroll Book.");
            return;
        }
        //Use a temporary attribute so that an item isn't consumed/required when using the scroll book.
        player.getTemporaryAttributes().put("master scroll book teleport", true);
        teleport.getScroll().teleport(player);
        if (isEmpty(book)) {
            book.setId(ItemId.MASTER_SCROLL_BOOK_EMPTY);
            player.getInventory().refreshAll();
        }
    }


    @Ordinal
    private enum ScrollOption {
        ACTIVATE, SET_AS_DEFAULT, REMOVE;
        private static final List<ScrollOption> values = Arrays.asList(values());
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.MASTER_SCROLL_BOOK;
    }
}
