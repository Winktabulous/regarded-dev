package com.zenyte.game.content;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.events.InitializationEvent;
import com.zenyte.utils.Ordinal;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Kris | 16/04/2019 22:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class GodBooks {

    public static final class GodBookItem extends ItemPlugin implements PairedItemOnItemPlugin {
        @Override
        public void handle() {
            bind("Check", (player, item, slotId) -> {
                final GodBooks.GodBook book = GodBook.get(item);
                final ArrayList<Integer> addedPages = new ArrayList<>(player.getGodBooks().getPages(book));
                Collections.sort(addedPages);
                final StringBuilder builder = new StringBuilder();
                for (final Integer page : addedPages) {
                    builder.append(ItemDefinitions.getOrThrow(page).getName()).append("<br>");
                }
                if (builder.length() == 0) {
                    player.getDialogueManager().start(new ItemChat(player, item, "Your damaged book contains no pages" +
                            "."));
                    return;
                }
                player.getDialogueManager().start(new ItemChat(player, item, "Your damaged book contains the " +
                        "following pages:<br>" + builder.delete(builder.length() - 2, builder.length())));
            });
            bind("Preach", (player, item, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    final GodBooks.GodBook book = GodBook.get(item);
                    final GodBooks.GodbookTranscript transcript = book.transcript;
                    options("Select a relevant passage", new DialogueOption("Wedding Ceremony", () -> preach(player,
                            book.animationId, transcript.weddingCeremony)), new DialogueOption("Last Rites",
                            () -> preach(player, book.animationId, transcript.lastRites)), new DialogueOption(
                                    "Blessings", () -> preach(player, book.animationId, transcript.blessings)),
                            new DialogueOption("Preach", () -> preach(player, book.animationId,
                                    Utils.random(transcript.preach))));
                }
            }));
        }

        private void preach(@NotNull final Player player, final int animation, @NotNull final ForceTalk[] chat) {
            if (player.getCombatDefinitions().getSpecialEnergy() < 25) {
                player.sendMessage("You need at least 25% special attack to do this.");
                return;
            }
            player.lock(6);
            player.setAnimation(new Animation(animation));
            player.getCombatDefinitions().setSpecialEnergy(player.getCombatDefinitions().getSpecialEnergy() - 25);
            WorldTasksManager.schedule(new TickTask() {
                @Override
                public void run() {
                    if (ticks % 2 == 0) {
                        player.setForceTalk(chat[ticks / 2]);
                    }
                    if (ticks == 4) {
                        stop();
                        return;
                    }
                    ticks++;
                }
            }, 0, 0);
        }

        @Override
        public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot,
                                           final int toSlot) {
            final GodBooks.GodBook attemptA = GodBook.get(from);
            final GodBooks.GodBook book = attemptA == null ? GodBook.get(to) : attemptA;
            final Item page = book.damagedBookId == from.getId() ? to : from;
            final Item bookItem = page == from ? to : from;
            final Set<Integer> pages = player.getGodBooks().getPages(book);
            if (pages.contains(page.getId())) {
                player.sendMessage("Your damaged book already contains that page.");
                return;
            }
            pages.add(page.getId());
            player.getInventory().deleteItem(new Item(page.getId(), 1));
            player.sendMessage("You add the page to the book...");
            if (pages.size() >= 4) {
                player.sendMessage("The book is now complete!");
                player.sendMessage("You can now use it to bless holy symbols.");
                bookItem.setId(book.completedBookId);
                player.getInventory().refreshAll();
            }
        }

        @Override
        public int[] getItems() {
            final IntOpenHashSet set = new IntOpenHashSet();
            for (final GodBooks.GodBook value : GodBook.values) {
                set.add(value.completedBookId);
                set.add(value.damagedBookId);
            }
            return set.toIntArray();
        }

        @Override
        public ItemPair[] getMatchingPairs() {
            final ArrayList<ItemOnItemAction.ItemPair> list = new ArrayList<ItemPair>(GodBook.values.length * 4);
            for (final GodBooks.GodBook value : GodBook.values) {
                for (final int page : value.pages) {
                    list.add(ItemPair.of(page, value.damagedBookId));
                }
            }
            return list.toArray(new ItemPair[0]);
        }
    }


    private enum GodbookTranscript {
        SARADOMIN(new ForceTalk[]{new ForceTalk("In the name of Saradomin,"), new ForceTalk("Protector of us all,"),
                new ForceTalk("I now join you in the eyes of Saradomin.")}, new ForceTalk[]{new ForceTalk("Thy cause " +
                "was false,"), new ForceTalk("Thy skills did lack;"), new ForceTalk("See you in Lumbridge when you " +
                "get back.")}, new ForceTalk[]{new ForceTalk("Go in peace in the name of Saradomin;"), new ForceTalk(
                        "May his glory shine upon you like the sun."), new ForceTalk("Like the sun.")},
                new ForceTalk[][]{new ForceTalk[]{new ForceTalk("Protect your self, protect your friends."),
                        new ForceTalk("Mine is the glory that never ends."), new ForceTalk("This is Saradomin's " +
                        "wisdom.")}, new ForceTalk[]{new ForceTalk("The darkness in life may be avoided,"),
                        new ForceTalk("By the light of wisdom shining."),
                        new ForceTalk("This is Saradomin's wisdom.")}, new ForceTalk[]{new ForceTalk("Show love to " +
                        "your friends, and mercy to your enemies,"), new ForceTalk("And know that the wisdom of " +
                        "Saradomin will follow."), new ForceTalk("This is Saradomin's wisdom.")},
                        new ForceTalk[]{new ForceTalk("A fight begun, when the cause is just,"), new ForceTalk("Will " +
                                "prevail over all others."), new ForceTalk("This is Saradomin's wisdom.")},
                        new ForceTalk[]{new ForceTalk("The currency of goodness is honour;"), new ForceTalk("It " +
                                "retains its value through scarcity."), new ForceTalk("This is Saradomin's wisdom.")}}),
        GUTHIX(new ForceTalk[]{new ForceTalk("Light and dark, day and night,"), new ForceTalk("Balance arises from " +
                "contrast."), new ForceTalk("I unify thee in the name of Guthix.")}, new ForceTalk[]{new ForceTalk(
                        "Thy death was not in vain,"), new ForceTalk("For it brought some balance to the world."),
                new ForceTalk("May Guthix bring you rest.")}, new ForceTalk[]{new ForceTalk("May you walk the path, " +
                "and never fall,"), new ForceTalk("For Guthix walks beside thee on thy journey."), new ForceTalk("May" +
                " Guthix bring you peace.")}, new ForceTalk[][]{new ForceTalk[]{new ForceTalk("The trees, the earth, " +
                "the sky, the waters;"), new ForceTalk("All play their part upon this land."), new ForceTalk("May " +
                "Guthix bring you balance.")}}),
        ZAMORAK(new ForceTalk[]{new ForceTalk("Two great warriors, joined by hand,"), new ForceTalk("To spread " +
                "destruction across the land. "), new ForceTalk("In Zamorak's name, now two are one. ")},
                new ForceTalk[]{new ForceTalk("The weak deserve to die,"), new ForceTalk("So the strong may flourish" +
                        "."), new ForceTalk("This is the creed of Zamorak.")}, new ForceTalk[]{new ForceTalk("May " +
                "your bloodthirst never be sated,"), new ForceTalk("And may all your battles be glorious."),
                new ForceTalk("Zamorak bring you strength.")}, new ForceTalk[][]{new ForceTalk[]{new ForceTalk("There" +
                " is no opinion that cannot be proven true..."), new ForceTalk("By crushing those who choose to " +
                "disagree with it."), new ForceTalk("Zamorak give me strength!")}, new ForceTalk[]{new ForceTalk(
                        "Battles are not lost and won;"), new ForceTalk("They simply remove the weak from the " +
                "equation."), new ForceTalk("Zamorak give me strength!")}, new ForceTalk[]{new ForceTalk("Those who " +
                "fight, then run away,"), new ForceTalk("Shame Zamorak with their cowardice."), new ForceTalk(
                        "Zamorak give me strength!")}, new ForceTalk[]{new ForceTalk("Battle is by those"),
                new ForceTalk("Who choose to disagree with it."), new ForceTalk("Zamorak give me strength!")},
                new ForceTalk[]{new ForceTalk("Strike fast, strike hard, strike true"), new ForceTalk("The strength " +
                        "of Zamorak will be with you."), new ForceTalk("Zamorak give me strength!")}}),
        ARMADYL(new ForceTalk[]{new ForceTalk("As ye vow to be at peace with each other..."), new ForceTalk("And to " +
                "uphold high values of morality and friendship..."), new ForceTalk("I now pronounce you united in the" +
                " law of Armadyl.")}, new ForceTalk[]{new ForceTalk("Thou didst fight true..."), new ForceTalk("But " +
                "the foe was too great."), new ForceTalk("May thy return be as swift as the flight of Armadyl.")},
                new ForceTalk[]{new ForceTalk("For thy task is lawful..."), new ForceTalk("May the blessing of " +
                        "Armadyl"), new ForceTalk("Be upon thee.")}, new ForceTalk[][]{new ForceTalk[]{new ForceTalk(
                                "Peace shall bring thee wisdom;"), new ForceTalk("Wisdom shall bring thee peace."),
                new ForceTalk("This is the law of Armadyl.")}}),
        BANDOS(new ForceTalk[]{new ForceTalk("Big High War God want great warriors."), new ForceTalk("Because you can" +
                " make more..."), new ForceTalk("I bind you in Big High War God name.")},
                new ForceTalk[]{new ForceTalk("You not worthy of"), new ForceTalk("Big High War God;"),
                        new ForceTalk("You die too easy.")}, new ForceTalk[]{new ForceTalk("Big High War God"),
                new ForceTalk("Make you strong..."), new ForceTalk("So you smash enemies.")},
                new ForceTalk[][]{new ForceTalk[]{new ForceTalk("War is best, Peace is for weak."), new ForceTalk("If" +
                        " you not worthy of Big High War God..."), new ForceTalk("You get made dead soon.")}}),
        ZAROS(new ForceTalk[]{new ForceTalk("Ye faithful and loyal to the Great Lord..."), new ForceTalk("May ye " +
                "together succeed in your deeds."), new ForceTalk("Ye are now joined by the greatest power.")},
                new ForceTalk[]{new ForceTalk("Thy faith faltered,"), new ForceTalk("No power could save thee."),
                        new ForceTalk("Like the Great Lord, one day you shall rise again.")},
                new ForceTalk[]{new ForceTalk("By day or night,"), new ForceTalk("In defeat or victory..."),
                        new ForceTalk("The power of the Great Lord be with thee.")},
                new ForceTalk[][]{new ForceTalk[]{new ForceTalk("Though your enemies wish to silence thee,"),
                        new ForceTalk("Do not falter, defy them to the end."), new ForceTalk("Power to the Great " +
                        "Lord!")}, new ForceTalk[]{new ForceTalk("The followers of the Great Lord are few,"),
                        new ForceTalk("But they are powerful and mighty."),
                        new ForceTalk("Power to the Great Lord!")}, new ForceTalk[]{new ForceTalk("Follower of the " +
                        "Great Lord be relieved "), new ForceTalk("One day your loyalty will be rewarded."),
                        new ForceTalk("Power to the Great Lord!")}, new ForceTalk[]{new ForceTalk("Pray for the day " +
                        "that the Great Lord rises; "), new ForceTalk("It is that day thou shalt be rewarded."),
                        new ForceTalk("Power to the Great Lord!")}, new ForceTalk[]{new ForceTalk("Oppressed thou " +
                        "art, but fear not"), new ForceTalk("The day will come when the Great Lord rises."),
                        new ForceTalk("Power to the Great Lord!")}, new ForceTalk[]{new ForceTalk("Fighting " +
                        "oppression is the wisest way,"), new ForceTalk("To prove your worth to the Great Lord."),
                        new ForceTalk("Power to the Great Lord!")}});
        private final ForceTalk[] weddingCeremony;
        private final ForceTalk[] lastRites;
        private final ForceTalk[] blessings;
        private final ForceTalk[][] preach;

        GodbookTranscript(ForceTalk[] weddingCeremony, ForceTalk[] lastRites, ForceTalk[] blessings,
                          ForceTalk[][] preach) {
            this.weddingCeremony = weddingCeremony;
            this.lastRites = lastRites;
            this.blessings = blessings;
            this.preach = preach;
        }
    }


    @Ordinal
    public enum GodBook {
        SARADOMIN(1335, GodbookTranscript.SARADOMIN, 3839, 3840, 3827, 3828, 3829, 3830),
        GUTHIX(1337, GodbookTranscript.GUTHIX, 3843, 3844, 3835, 3836, 3837, 3838),
        ZAMORAK(1336, GodbookTranscript.ZAMORAK, 3841, 3842, 3831, 3832, 3833, 3834),
        BANDOS(7153, GodbookTranscript.BANDOS, 12607, 12608, 12613, 12614, 12615, 12616),
        ARMADYL(7154, GodbookTranscript.ARMADYL, 12609, 12610, 12617, 12618, 12619, 12620),
        ANCIENT(7155, GodbookTranscript.ZAROS, 12611, 12612, 12621, 12622, 12623, 12624);
        private final int animationId;
        private final GodbookTranscript transcript;
        private final int damagedBookId;
        private final int completedBookId;
        private final int[] pages;
        public static final GodBook[] values = values();

        GodBook(final int animationId, final GodbookTranscript transcript, final int damagedBookId,
                final int completedBookId, final int... pages) {
            this.animationId = animationId;
            this.transcript = transcript;
            this.damagedBookId = damagedBookId;
            this.completedBookId = completedBookId;
            this.pages = pages;
        }

        private static GodBook get(@NotNull final Item item) {
            return CollectionUtils.findMatching(values,
                    value -> value.completedBookId == item.getId() || value.damagedBookId == item.getId());
        }

        public int getAnimationId() {
            return animationId;
        }

        public GodbookTranscript getTranscript() {
            return transcript;
        }

        public int getDamagedBookId() {
            return damagedBookId;
        }

        public int getCompletedBookId() {
            return completedBookId;
        }

        public int[] getPages() {
            return pages;
        }
    }

    @Subscribe
    public static void onInitialization(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player saved = event.getSavedPlayer();
        if (saved.getGodBooks() == null || saved.getGodBooks().pages == null) {
            return;
        }
        player.getGodBooks().pages.putAll(saved.getGodBooks().pages);
        if (saved.getGodBooks().claimedBooks != null) {
            player.getGodBooks().claimedBooks.addAll(saved.getGodBooks().claimedBooks);
        }
    }

    private final Map<GodBook, Set<Integer>> pages = new HashMap<>();
    private final Set<GodBook> claimedBooks = new HashSet<>();

    public Set<Integer> getPages(@NotNull final GodBook book) {
        return pages.computeIfAbsent(book, k -> new HashSet<>());
    }

    public Set<GodBook> getClaimedBooks() {
        return claimedBooks;
    }

}
