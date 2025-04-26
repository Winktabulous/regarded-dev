package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.GodBooks;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 12/06/2019 08:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class JossiksGodBooks extends Interface {
    @Override
    protected void attach() {
        put(3, "Saradomin");
        put(4, "Guthix");
        put(5, "Zamorak");
        put(6, "Bandos");
        put(7, "Armadyl");
        put(8, "Ancient");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
        for (final GodBooks.GodBook book : GodBooks.GodBook.values) {
            refreshBook(player, book);
        }
    }

    private void refreshBook(@NotNull final Player player, @NotNull final GodBooks.GodBook book) {
        int value = 0;
        final GodBooks books = player.getGodBooks();
        if (books.getClaimedBooks().contains(book)) {
            value = 2;
            if (!player.containsItem(book.getCompletedBookId()) && !player.containsItem(book.getDamagedBookId())) {
                value = 1;
            }
        }
        if (books.getPages(book).size() >= 4) {
            value = 3;
        }
        player.getVarManager().sendVar(261 + book.ordinal(), value);
    }

    @Override
    protected void build() {
        bind("Saradomin", player -> manageBook(player, GodBooks.GodBook.SARADOMIN));
        bind("Guthix", player -> manageBook(player, GodBooks.GodBook.GUTHIX));
        bind("Zamorak", player -> manageBook(player, GodBooks.GodBook.ZAMORAK));
        bind("Bandos", player -> manageBook(player, GodBooks.GodBook.BANDOS));
        bind("Armadyl", player -> manageBook(player, GodBooks.GodBook.ARMADYL));
        bind("Ancient", player -> manageBook(player, GodBooks.GodBook.ANCIENT));
    }

    private final void manageBook(@NotNull final Player player, @NotNull final GodBooks.GodBook book) {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You need some free inventory space to do that.");
            return;
        }
        final boolean containsBook = player.containsItem(book.getDamagedBookId()) || player.containsItem(book.getCompletedBookId());
        if (containsBook) {
            player.sendMessage("You already own a book from this god.");
            return;
        }
        final GodBooks books = player.getGodBooks();
        if (books.getPages(book).size() >= 4) {
            player.getInventory().addOrDrop(new Item(book.getCompletedBookId(), 1));
            refreshBook(player, book);
            return;
        }
        if (books.getClaimedBooks().contains(book)) {
            player.getInventory().addOrDrop(new Item(book.getDamagedBookId(), 1));
            refreshBook(player, book);
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(new Item(book.getCompletedBookId()), "Unlock the damaged book for 25,000 coins?");
                options("Unlock the book?", new DialogueOption("Unlock it.", () -> {
                    if (!player.getInventory().hasFreeSlots()) {
                        player.sendMessage("You need some free inventory space to do that.");
                        return;
                    }
                    if (!player.getInventory().containsItem(995, 25000)) {
                        player.sendMessage("You need at least 25,000 coins to unlock a god book.");
                        return;
                    }
                    player.getInventory().deleteItem(995, 25000);
                    player.getInventory().addOrDrop(new Item(book.getDamagedBookId()));
                    player.getGodBooks().getClaimedBooks().add(book);
                    player.getInterfaceHandler().closeInterfaces();
                    GameInterface.JOSSIKS_SALVAGED_GODBOOKS.open(player);
                }), new DialogueOption("Cancel."));
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.JOSSIKS_SALVAGED_GODBOOKS;
    }
}
