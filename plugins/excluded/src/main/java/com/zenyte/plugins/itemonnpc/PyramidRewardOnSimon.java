package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.content.pyramidplunder.PlunderReward;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.renewednpc.SimonTempleton;

public class PyramidRewardOnSimon implements ItemOnNPCAction {
    private static final int SELL_ARTEFACT_KEY = 5;

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        final PlunderReward artefact = PlunderReward.getByItem(item.getId());
        if (artefact != null) {
            handlePlunderReward(player, npc, item, artefact);
        } else if (item.getId() == ItemId.PYRAMID_TOP) {
            handleAgilityReward(player, item);
        } else {
            player.sendMessage("Simon wants nothing to do with this.");
        }
    }

    private void handlePlunderReward(final Player player, final NPC npc, final Item item, final PlunderReward artefact) {
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("You wanna sell that mate?");
                options("Do you want to sell your " + item.getName(), new DialogueOption("Yes, show me the money.", key(SELL_ARTEFACT_KEY)), new DialogueOption("No, I think I\'ll keep it."));
                player(SELL_ARTEFACT_KEY, "Yes, show me the money.").executeAction(() -> {
                    if (player.getInventory().containsItem(item)) {
                        player.getInventory().deleteItem(item);
                        player.getInventory().addOrDrop(new Item(ItemId.COINS_995, (int) (artefact.getCoins() * PlunderReward.REWARD_MULTIPLIER)));
                    }
                });
                npc("Cheers mate!");
            }
        });
    }

    private void handleAgilityReward(final Player player, final Item item) {
        final Inventory inventory = player.getInventory();
        final int amount = inventory.getAmountOf(ItemId.PYRAMID_TOP);
        if (amount == 0) {
            return;
        }
        final int rewardAmount = amount * SimonTempleton.COINS_PER_PYRAMID;
        player.getDialogueManager().start(new ItemChat(player, item, "You hand over the artefact(s) and Simon hands you " + rewardAmount + " coins."));
        inventory.deleteItem(ItemId.PYRAMID_TOP, amount);
        inventory.addOrDrop(ItemId.COINS_995, rewardAmount);
    }

    @Override
    public Object[] getItems() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {NpcId.SIMON_TEMPLETON};
    }
}
