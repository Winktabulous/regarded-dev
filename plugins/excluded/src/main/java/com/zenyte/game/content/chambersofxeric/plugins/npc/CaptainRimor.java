package com.zenyte.game.content.chambersofxeric.plugins.npc;

import com.zenyte.game.content.chambersofxeric.map.RaidPattern;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.plugins.dialogue.MakeType;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import com.zenyte.plugins.dialogue.SkillDialogue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Kris | 31/07/2019 18:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CaptainRimor extends NPCPlugin {
    private static final int CAPTAIN_RIMOR = 7595;

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Beware the Chambers of Xeric, adventurer.");
                npc("These dungeons and the perils within them are unique. You will encounter both dangerous and mysterious creatures here.");
                npc("Remember that you may be kicked out at any time - any items dropped on the ground will be lost inside.");
                npc("Do not allow your curiosity to get the better of you.");
                player("Thanks for the warning.");
            }
        }));
        bind("Claim-cape", (player, npc) -> {
            final int killcount = player.getNumericAttribute("challengechambersofxeric").intValue();
            if (killcount < 10) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("I have no capes to give to you right now. Come talk to me whenever you\'ve completed 10, 25, 75, 150 or 300 challenge mode runs.");
                    }
                });
                return;
            }
            List<Item> list = new ObjectArrayList<>();
            list.add(new Item(ItemId.XERICS_GUARD));
            if (killcount >= 25) {
                list.add(new Item(ItemId.XERICS_WARRIOR));
            }
            if (killcount >= 75) {
                list.add(new Item(ItemId.XERICS_SENTINEL));
            }
            if (killcount >= 150) {
                list.add(new Item(ItemId.XERICS_GENERAL));
            }
            if (killcount >= 300) {
                list.add(new Item(ItemId.XERICS_CHAMPION));
            }
            player.getDialogueManager().start(new SkillDialogue(player, list.toArray(new Item[0])) {
                @Override
                public void run(final int slotId, final int amount) {
                    player.getInventory().addItem(list.get(slotId));
                }
                @Override
                public final void buildDialogue() {
                    skill(1, MakeType.TAKE, "Select the cape to claim", items);
                }
            });
        });
        bind("Layouts", (player, npc) -> {
            player.addAttribute("aware of raids layouts", 1);
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    options(new DialogueOption("What are layouts?", key(10)), new DialogueOption("Unlock slots.", key(100)), new DialogueOption("Manage layouts.", key(200)));
                    player(10, "What are layouts?");
                    npc("Layouts are predefined patterns of the floors that you will find within the Chambers of Xeric.");
                    npc("There are 26 possible layouts available, most of them consisting of seven room floors, but two come with eight rooms.");
                    npc("The floor layout is pseudo-random, meaning it is randomized to the extent of specific room types.");
                    npc("So for example, a possible layout is:<br>F S C C P P C S C F<br>The letters mark the type of the room in the order they will come in.");
                    npc("The letter C stands for a combat room, which can be one of the following rooms:<br>Tekton, Vasa Nistirio, Guardians, Skeletal mystics, Lizardmen shaman, Muttadiles, Vanguards or Vespula.");
                    npc("The letter P stands for a puzzle room, which can be one of the following rooms:<br>Ice demon, Creature keeper(also known as thieving), Tightrope or Crabs.");
                    npc("The letter S stands for a scavenger room. There are two types of scavenger rooms.<br>Lastly, the letter F stands for a farming room. There are two types of farming rooms as well.");
                    npc("Disabling a layout means that if you were the one to create the party(once anyone enters the raid, it is too late), that layout would not be a possible option in that raid.");
                    npc("So if you wish to not get a layout which comes with three puzzles, you can disable the layouts which have three puzzles and that will never occur as long as you lead the party.").executeAction(key(1));
                    final int unlockedSlotsCount = player.getNumericAttribute("raids layout count").intValue();
                    player(100, "I\'d like to unlock slots so that I could block certain layouts.");
                    if (unlockedSlotsCount >= 10) {
                        npc("You\'ve already unlocked the ten slots I\'m willing to allow.");
                    } else {
                        npc(Colour.RED.wrap("WARNING: ") + "Unlocking a slot costs 2.5m gold pieces per slot. This is a permanent unlock, meaning you cannot get your money back.").executeAction(() -> {
                            player.getDialogueManager().finish();
                            player.sendInputInt("How many slots would you like to unlock?(1-" + (10 - unlockedSlotsCount) + ")", value -> {
                                if (value < 1) {
                                    return;
                                }
                                final Inventory inventory = player.getInventory();
                                final int affordableAmount = inventory.getAmountOf(ItemId.COINS_995) / 2500000;
                                final int unlockedAmount = Math.min(affordableAmount, value);
                                if (unlockedAmount <= 0) {
                                    player.sendMessage("You cannot afford any unlocks.");
                                    return;
                                }
                                final int unlockedCount = player.getNumericAttribute("raids layout count").intValue();
                                final int unlockableCount = 10 - unlockedCount;
                                final int toUnlock = Math.min(unlockedAmount, unlockableCount);
                                inventory.deleteItem(new Item(ItemId.COINS_995, toUnlock * 2500000));
                                player.addAttribute("raids layout count", unlockedCount + toUnlock);
                                player.getDialogueManager().start(new NPCChat(player, npc.getId(), "Thank you, I\'ve unlocked " + toUnlock + " slot" + (toUnlock == 1 ? "" : "s") + " for you. You now have " + (toUnlock + unlockedCount) + "/10 slots unlocked."));
                            });
                        });
                    }
                    player(200, "I\'d like to manage my layouts.");
                    npc("Certainly.").executeAction(() -> openLayoutsInterface(player));
                }
            });
        });
    }

    private List<String> generateList(@NotNull final Player player) {
        final String[] options = RaidPattern.getLayoutCodes();
        final ObjectArrayList<String> filteredOptions = new ObjectArrayList<String>();
        final int currentSettings = player.getNumericAttribute("disabled raids layouts").intValue();
        for (int i = 0; i < options.length; i++) {
            final String option = options[i];
            final String chars = option.split(" - ")[0].replaceAll("[^FSCP]", "");
            final StringBuilder builder = new StringBuilder();
            for (final char c : chars.toCharArray()) {
                if (c == 'C') {
                    builder.append(Colour.BRICK.wrap(Character.toString(c)));
                } else if (c == 'S') {
                    builder.append(Colour.RS_PURPLE.wrap(Character.toString(c)));
                } else if (c == 'P') {
                    builder.append(Colour.BLUE.wrap(Character.toString(c)));
                } else {
                    builder.append(Colour.RS_GREEN.wrap(Character.toString(c)));
                }
                builder.append(' ');
            }
            final String string = builder.toString().trim();
            final String firstHalfOfString = string.substring(0, string.length() / 2);
            final String secondHalfOfString = string.substring((string.length() / 2) + 1);
            final boolean isDisabled = ((currentSettings >> i) & 1) == 1;
            filteredOptions.add((isDisabled ? "<str>" : "") + "Layout: " + firstHalfOfString + " - " + secondHalfOfString);
        }
        return filteredOptions;
    }

    private void sendInterface(@NotNull final Player player) {
        final List<String> filteredOptions = generateList(player);
        final int currentSettings = player.getNumericAttribute("disabled raids layouts").intValue();
        final int unlockedSlotsCount = player.getNumericAttribute("raids layout count").intValue();
        player.getDialogueManager().start(new OptionsMenuD(player, "Select the layouts to disable(" + Integer.bitCount(currentSettings) + "/" + unlockedSlotsCount + ")", filteredOptions.toArray(new String[0])) {
            @Override
            public void handleClick(int slotId) {
                final int currentSettings = player.getNumericAttribute("disabled raids layouts").intValue();
                final boolean isEnabled = ((currentSettings >> slotId) & 1) == 0;
                final int unlockedSlotsCount = player.getNumericAttribute("raids layout count").intValue();
                if (Integer.bitCount(currentSettings) >= unlockedSlotsCount && isEnabled) {
                    player.sendMessage("You cannot disable that many layouts." + (unlockedSlotsCount < 10 ? " You can purchase more unlocks from Captain Rimor." : ""));
                } else {
                    player.addAttribute("disabled raids layouts", isEnabled ? (currentSettings | (1 << slotId)) : (currentSettings & ~(1 << slotId)));
                }
                sendInterface(player);
            }
            @Override
            public boolean cancelOption() {
                return true;
            }
        });
    }

    private void openLayoutsInterface(@NotNull final Player player) {
        player.sendMessage(Colour.RED.wrap("Layout codes:"));
        player.sendMessage(Colour.BRICK.wrap("C - Combat"));
        player.sendMessage(Colour.BLUE.wrap("P - Puzzle"));
        player.sendMessage(Colour.RS_PURPLE.wrap("S - Scavengers"));
        player.sendMessage(Colour.RS_GREEN.wrap("F - Farming"));
        sendInterface(player);
    }

    @Override
    public int[] getNPCs() {
        return new int[] {CAPTAIN_RIMOR};
    }
}
