package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 30/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WatsonPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Hello there " + player.getName() + ".");
                // You hand in four clues - easy, medium, hard and elite to receive a master clue; we bitpack the value to avoid unnecessarily verbose vars.
                final int depositedCluesHash = player.getNumericAttribute("Watson handed in clue scrolls hash").intValue();
                final Inventory inventory = player.getInventory();
                final boolean canHandInEasy = (depositedCluesHash & 1) == 0 && inventory.containsItem(ClueItem.EASY.getClue(), 1);
                final boolean canHandInMedium = (depositedCluesHash & 2) == 0 && inventory.containsItem(ClueItem.MEDIUM.getClue(), 1);
                final boolean canHandInHard = (depositedCluesHash & 4) == 0 && inventory.containsItem(ClueItem.HARD.getClue(), 1);
                final boolean canHandInElite = (depositedCluesHash & 8) == 0 && inventory.containsItem(ClueItem.ELITE.getClue(), 1);
                final boolean canDepositAny = canHandInEasy || canHandInMedium || canHandInHard || canHandInElite;
                final int depositableCluesCount = BooleanUtils.toInteger(canHandInEasy) + BooleanUtils.toInteger(canHandInMedium) + BooleanUtils.toInteger(canHandInHard) + BooleanUtils.toInteger(canHandInElite);
                final boolean canDepositMoreThanOne = depositableCluesCount > 1;
                options(TITLE, new DialogueOption("Who are you?", key(100)), new DialogueOption("Have you heard of Sherlock?", key(200)), new DialogueOption("So what are master clue scrolls anyway?", key(300)), canDepositAny ? new DialogueOption("I have something for you.", key(400)) : null, new DialogueOption("Goodbye.", key(500)));
                player(100, "Who are you?");
                npc("I am Watson, the master of clue scrolls. I have lived a long life here in Kourend. The lands are beautiful here.");
                player("You're the master are you?");
                npc("Ha! It is not so much due to my ego, but the fact that I give out master tier clue scrolls.");
                player("Interesting. Do you get many treasure hunters here in Kourend?");
                npc("I have seen many, but few are good enough. I only give out my clue scrolls to the very best.");
                player("Ah! I am just the adventurer you are looking for. If you hand me a clue, I'll be on my way.");
                npc("Not so fast " + player.getName() + ". I require payment. You must bring me a clue scroll of all lower tiers.");
                npc("I will remember which ones you have given me though. I expect you'll find that useful.");
                options(TITLE, new DialogueOption("Have you heard of Sherlock?", key(200)), new DialogueOption("So what are master clue scrolls anyway?", key(300)), canDepositAny ? new DialogueOption("I have something for you.", key(400)) : null, new DialogueOption("Goodbye.", key(500)));
                player(200, "Have you heard of Sherlock?");
                npc("Never heard of the bloke. I haven't a clue who he is.");
                player("He also claims to be the master of all clue scrolls.");
                npc("As I said, I've never heard of him. If he was such a big deal, I would have heard of him...");
                player("Something tells me you're lying! I bet you're just his underling aren't you.");
                options(TITLE, new DialogueOption("Who are you?", key(100)), new DialogueOption("So what are master clue scrolls anyway?", key(300)), canDepositAny ? new DialogueOption("I have something for you.", key(400)) : null, new DialogueOption("Goodbye.", key(500)));
                player(300, "So what are master clue scrolls anyway?");
                npc("Master clue scrolls are the most challenging of all clue scrolls. They will test you in ways you have not been tested before.");
                npc("You will be led all over Gielinor with your strengths, your weaknesses, your mental strength and your reactions all scrutinised.");
                player("I assume there is treasure to be found?");
                npc("That is for you to find out, " + player.getName() + ".");
                player(400, "I have something for you.");
                options(TITLE, canHandInEasy ? new DialogueOption("Hand over easy clue.", () -> setKey(deposit(player, ClueItem.EASY, false) ? 410 : 460)) : null, canHandInMedium ? new DialogueOption("Hand over medium clue.", () -> setKey(deposit(player, ClueItem.MEDIUM, false) ? 420 : 460)) : null, canHandInHard ? new DialogueOption("Hand over hard clue.", () -> setKey(deposit(player, ClueItem.HARD, false) ? 430 : 460)) : null, canHandInElite ? new DialogueOption("Hand over elite clue.", () -> setKey(deposit(player, ClueItem.ELITE, false) ? 440 : 460)) : null, new DialogueOption(canDepositMoreThanOne ? "Hand over all clues." : "Cancel.", () -> {
                    if (!canDepositMoreThanOne) {
                        return;
                    }
                    setKey(depositAll(player) ? 450 : 470);
                }), depositableCluesCount > 1 && depositableCluesCount < 4 ? new DialogueOption("Cancel.") : null);
                item(410, new Item(ItemId.CLUE_SCROLL_EASY), "You hand over the clue scroll to Watson.");
                item(420, new Item(ItemId.CLUE_SCROLL_MEDIUM), "You hand over the clue scroll to Watson.");
                item(430, new Item(ItemId.CLUE_SCROLL_HARD), "You hand over the clue scroll to Watson.");
                item(440, new Item(ItemId.CLUE_SCROLL_ELITE), "You hand over the clue scroll to Watson.");
                plain(450, "You hand over all the clue scrolls to Watson.");
                plain(460, "You no longer possess the clue scroll.");
                plain(470, "You no longer possess any of the clue scrolls.");
                player(500, "Goodbye.");
            }
        }));
        bind("Check", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                final int depositedCluesHash = player.getNumericAttribute("Watson handed in clue scrolls hash").intValue();
                final StringBuilder builder = new StringBuilder();
                if ((depositedCluesHash & 1) == 1) {
                    builder.append("an easy clue scroll, ");
                }
                if ((depositedCluesHash & 2) == 2) {
                    builder.append("a medium clue scroll, ");
                }
                if ((depositedCluesHash & 4) == 4) {
                    builder.append("a hard clue scroll, ");
                }
                if ((depositedCluesHash & 8) == 8) {
                    builder.append("an elite clue scroll, ");
                }
                if (builder.length() > 0) {
                    builder.delete(builder.length() - 2, builder.length());
                }
                final int index = builder.lastIndexOf(", ");
                if (index != -1) {
                    builder.replace(index, index + 2, " and ");
                }
                npc(builder.length() == 0 ? "You haven't given me any clue scrolls yet." : "You have given me " + builder + " so far.");
            }
        }));
    }

    private final boolean depositAll(@NotNull final Player player) {
        final int depositedCluesHash = player.getNumericAttribute("Watson handed in clue scrolls hash").intValue();
        final boolean containsEasy = (depositedCluesHash & 1) == 0 && deposit(player, ClueItem.EASY, true);
        final boolean containsMedium = (depositedCluesHash & 2) == 0 && deposit(player, ClueItem.MEDIUM, true);
        final boolean containsHard = (depositedCluesHash & 4) == 0 && deposit(player, ClueItem.HARD, true);
        final boolean containsElite = (depositedCluesHash & 8) == 0 && deposit(player, ClueItem.ELITE, true);
        return containsEasy || containsMedium || containsHard || containsElite;
    }

    private final boolean deposit(@NotNull final Player player, @NotNull final ClueItem type, final boolean plural) {
        // Ensure that the starting index is valid.
        assert ClueItem.EASY.ordinal() == 1;
        final Inventory inventory = player.getInventory();
        final boolean success = inventory.deleteItem(type.getClue(), 1).getSucceededAmount() > 0;
        if (success) {
            final int ordinal = type.ordinal() - 1;
            final int depositedCluesHash = player.getNumericAttribute("Watson handed in clue scrolls hash").intValue() | (1 << ordinal);
            final boolean completed = depositedCluesHash == (1 | 2 | 4 | 8);
            player.addAttribute("Watson handed in clue scrolls hash", completed ? 0 : depositedCluesHash);
            if (completed) {
                player.getDialogueManager().finish();
                inventory.addOrDrop(new Item(ClueItem.MASTER.getScrollBox()));
                player.getDialogueManager().start(new Dialogue(player, 7304) {

                    @Override
                    public void buildDialogue() {
                        item(new Item(ClueItem.MASTER.getScrollBox()), "You hand over the clue scroll" + (plural ? 's' : "") + " and receive a master scroll box in return.");
                    }
                });
                return true;
            }
        }
        return success;
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.WATSON_7304, NpcId.WATSON };
    }
}
