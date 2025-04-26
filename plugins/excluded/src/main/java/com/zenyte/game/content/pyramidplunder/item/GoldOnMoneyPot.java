package com.zenyte.game.content.pyramidplunder.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class GoldOnMoneyPot implements ItemOnObjectAction {
    private static final int KIT_KEY = 5;
    private static final int COINS_AMOUNT = 2;

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (item.getAmount() < COINS_AMOUNT) {
            player.sendMessage("The snake charmer seems uninterested in this amount of money. Perhaps try offering " +
                    "more.");
            return;
        }
        player.getInventory().deleteItem(ItemId.COINS_995, COINS_AMOUNT);
        player.getDialogueManager().start(new Dialogue(player, NpcId.ALI_THE_SNAKE_CHARMER) {
            @Override
            public void buildDialogue() {
                plain("The snake charmer snaps out of his trance and directs his full attention to you.");
                options(TITLE,
                        new DialogueOption("Wow, a snake charmer. Can I have a go? Can I have a go? Please?",
                                key(KIT_KEY)),
                        new DialogueOption("Never mind."));

                player(KIT_KEY, "Wow, a snake charmer. Can I have a go? Can I have a go? Please?").executeAction(() -> {
                    player.getInventory().addOrDrop(ItemId.SNAKE_CHARM);
                    player.getInventory().addOrDrop(ItemId.SNAKE_BASKET);
                });

                npc("If it means that you'll leave me alone, I will give you my snake charming super starter kit " +
                        "complete with flute and basket.");
                plain("The snake charmer gives you a snake charming flute and a basket.");
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[]{ItemId.COINS_995};
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.MONEY_POT};
    }
}
