package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.content.treasuretrails.ClueEntry;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.challenges.FaloTheBardChallenge;
import com.zenyte.game.content.treasuretrails.clues.Clue;
import com.zenyte.game.content.treasuretrails.clues.FaloTheBardClue;
import com.zenyte.game.content.treasuretrails.clues.emote.ItemRequirement;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Kris | 04/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FaloTheBardNPC extends NPCPlugin implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        final List<String> list = TreasureTrail.getCluesList(item);
        if (list == null) {
            final Optional<ClueEntry> clue = TreasureTrail.findFaloClue(player);
            if (!clue.isPresent()) {
                player.sendFilteredMessage("Nothing interesting happens.");
                return;
            }
            final ClueEntry scroll = clue.get();
            final FaloTheBardChallenge challenge = (FaloTheBardChallenge) scroll.getChallenge();
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    item(item, "You show your object to Falo.");
                    final ItemRequirement[] requirements = challenge.getTask().getItemRequirements();
                    for (final ItemRequirement requirement : requirements) {
                        if (!requirement.fulfilledBy(item.getId())) {
                            npc("No, I don't think that is correct.");
                            return;
                        }
                    }
                    plain("Falo's eyes light up.");
                    npc("Yes yes yes! That is it, thank you so much " + player.getName() + ". The memories have come flooding back. I get why the lyrics are what they are now.").executeAction(() -> {
                        if (player.getInventory().getItem(scroll.getSlot()) != scroll.getItem()) {
                            return;
                        }
                        player.getDialogueManager().finish();
                        TreasureTrail.progress(player, scroll, Optional.of(new Dialogue(player, npc) {

                            @Override
                            public void buildDialogue() {
                                player("I'm glad to have helped, hopefully you'll never forget again!");
                            }
                        }));
                    });
                }
            });
            return;
        }
        final String constantName = list.get(0);
        final Clue clueScroll = TreasureTrail.getClues().get(constantName);
        if (clueScroll instanceof FaloTheBardClue) {
            talk(player, npc);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public Object[] getItems() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STATUE_7306 };
    }

    @Override
    public void handle() {
        bind("Talk-to", this::talk);
    }

    private void talk(@NotNull final Player player, @NotNull final NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                final Optional<ClueEntry> clue = TreasureTrail.findFaloClue(player);
                if (!clue.isPresent()) {
                    npc("Good evening, " + player.getName() + ".");
                    return;
                }
                final ClueEntry scroll = clue.get();
                assert scroll.getChallenge() instanceof FaloTheBardChallenge;
                final FaloTheBardChallenge challenge = (FaloTheBardChallenge) scroll.getChallenge();
                final Item clueItem = scroll.getItem();
                final int stage = clueItem.getNumericAttribute("Falo the Bard Stage").intValue();
                if (stage == 0) {
                    npc("Thank you for coming, " + player.getName() + ", I need your help.");
                    npc("Here goes...<br>" + challenge.getTask().getText()).executeAction(() -> {
                        player.sendMessage("You quickly jot down the song onto your clue scroll.");
                        clueItem.setAttribute("Falo the Bard Stage", 1);
                    });
                    player("What a beautiful song!");
                    npc("You have very kind words. I would be extremely grateful if you could remind me which object the song was about.");
                } else if (stage == 1) {
                    npc("I really hope you can help me. Please show me the object my song is about!");
                    player("Can you sing me the song again?");
                    npc("Okay, here goes...<br>" + challenge.getTask().getText());
                } else {
                    throw new RuntimeException();
                }
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 7306 };
    }
}
