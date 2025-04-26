package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.pyramidplunder.PlunderReward;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class SimonTempleton extends NPCPlugin {

    public static final int COINS_PER_PYRAMID = 25000;

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                if (player.carryingAny(PlunderReward.getItemIds())) {
                    player("I have some interesting artefacts I\'d like you to look at.");
                    npc("Bonzer! Let\'s have a Butcher\'s mate.");
                    npc("Do you want to flog the lot of \'em?");
                    options("Do you want to sell all your artefacts?", new DialogueOption("Yes, show me the money.", key(5)), new DialogueOption("No, let me think about it.", key(10)));
                    player(5, "Yes, show me the money.").executeAction(() -> {
                        for (int itemId : PlunderReward.getItemIds()) {
                            final PlunderReward artefact = PlunderReward.getByItem(itemId);
                            final int amount = player.getInventory().getAmountOf(itemId);
                            if (amount == 0) {
                                continue;
                            }
                            player.getInventory().deleteItem(itemId, amount);
                            player.getInventory().addOrDrop(ItemId.COINS_995, ((int) (artefact.getCoins() * PlunderReward.REWARD_MULTIPLIER)) * amount);
                        }
                    });
                    npc("Fair Dinkum mate, Bonzer doing business with you mate.");
                    player(10, "No, let me think about it.");
                    npc("No probs mate. Just give me the items you want to sell and we can do them one by one.");
                    return;
                } else if (!player.getInventory().containsItem(ItemId.PYRAMID_TOP, 1)) {
                    player("Hello, Simon.");
                    npc("G\'day, " + player.getName() + ".");
                    player("How\'s it going?");
                    npc("Not bad, mate, not bad at all. Got any artefacts for me?");
                    player("No, I haven\'t.");
                    npc("Try harder, mate! It won\'t be long till some other critter turns up and snatches them out from under your eyes.");
                    player("I will, goodbye.");
                } else {
                    npc("G\'day, mate. Got any new artefacts for me?");
                    final Item agilityArtefact = new Item(ItemId.PYRAMID_TOP, player.getInventory().getAmountOf(ItemId.PYRAMID_TOP));
                    options(TITLE, new DialogueOption("Sell it", () -> {
                        player.getInventory().ifDeleteItem(agilityArtefact, () -> player.getInventory().addOrDrop(ItemId.COINS_995, COINS_PER_PYRAMID * agilityArtefact.getAmount()));
                        setKey(5);
                    }), new DialogueOption("Keep it", key(10)));
                    item(5, agilityArtefact, "You hand over the artefact(s) and Simon hands you " + agilityArtefact.getAmount() * COINS_PER_PYRAMID + " coins.");
                    npc("Ripper! Thanks a bundle, mate! Thanks to you I can fulfill me contract. You\'re a true blue! The boss will be pleased.");
                    player("Glad I was able to help... but who is your boss? I thought you worked for the museum?");
                    npc("Mind your own bizzo, mate. But if you get anymore, you know where I\'m at.");
                    final String amountString = agilityArtefact.getAmount() == 1 ? "one" : "some";
                    final String itemPronoun = (agilityArtefact.getAmount() == 1 ? "it" : "them");
                    player(10, "I have " + amountString + ", but I want to keep " + itemPronoun + " for now.");
                    npc("Careful, mate, people might come looking for a thing like that!");
                    player("Thanks for the advice, but I\'ll hang on to " + itemPronoun + " for now.");
                }
                npc("Bye, cobber.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SIMON_TEMPLETON };
    }
}
