package com.zenyte.game.content.lootkeys;

import com.google.common.reflect.TypeToken;
import com.near_reality.game.content.commands.DeveloperCommands;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.login.LoginManager;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;

import java.lang.reflect.Type;

import static com.zenyte.game.content.lootkeys.LootkeyConstants.*;

public class LootkeyChest implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {

        if (!DeveloperCommands.INSTANCE.getEnabledLootKeys()){
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain("Lootkeys is currently disabled.");
                }
            });
            return;
        }

        switch (option) {
            case "Loot":

                if (player.getLootkeySettings() == null) {
                    player.getDialogueManager().start(new Dialogue(player, 10382) {
                        @Override
                        public void buildDialogue() {
                            npc("You might want to speak to myself first, mate.");
                        }
                    });
                    return;
                }

                boolean containsItems = player.getLootkeySettings().getCurrentItemsInChest() != null;
                if (containsItems && player.getLootkeySettings().getCurrentItemsInChest().isEmpty()) {
                    LootkeySettings.clear(player);
                    containsItems = player.getLootkeySettings().getCurrentItemsInChest() != null;
                }

                if (!containsItems) {
                    var amountOfKeys = player.getInventory().getAmountOf(LOOT_KEY_ITEM_ID);
                    if (amountOfKeys == 0) {
                        player.getDialogueManager().start(new Dialogue(player, 10382) {
                            @Override
                            public void buildDialogue() {
                                npc("You don't seem to have any loot keys on you there, mate.");
                            }
                        });
                        return;
                    } else if (amountOfKeys == 1) {

                        claimLootkey(player, 0);

                    } else { // What to do when we have more than 1 key...
                        player.getDialogueManager().start(selectKey(player));
                    }
                } else {
                    openInterface(player);
                }
        }
    }

    private static Dialogue selectKey(Player player) {
        return new Dialogue(player) {
            @Override
            public void buildDialogue() {
                int count = player.getInventory().getAmountOf(LOOT_KEY_ITEM_ID);
                if (count > 5) {
                    count = 5;
                }

                String[] options = new String[count];
                var map = player.getInventory().getContainer().findAllById(LOOT_KEY_ITEM_ID);
                var values = map.values().toArray();

                for (int i = 0; i < count; i++) {

                    var item = (Item) values[i];
                    var string = "Loot key "+ (i + 1) + " ("
                            + Utils.formatNumberWithCommas(item.getNumericAttribute(LOOT_KEY_ITEM_LOOT_VALUE_ATTR).longValue()) + "gp)";
                    options[i] = string;
                }

                options("Select loot key", options)
                        .onOptionOne(() -> claimLootkey(player, 0))
                        .onOptionTwo(() -> claimLootkey(player, 1))
                        .onOptionThree(() -> claimLootkey(player, 2))
                        .onOptionFour(() -> claimLootkey(player, 3))
                        .onOptionFive(() -> claimLootkey(player, 4))

                ;

            }
        };
    }


    /**
     *
     * @param player
     * @param itemSlotOrder - This is the itemSlotOrder of the option, for example:
     *             if the player has loot key in the first itemSlotOrder of invy and last itemSlotOrder of invy, to get the
     *             last lootkey, the itemSlotOrder value would be <code>1</code>, and the first loot key would be <code>0</code>
     */
    private static void claimLootkey(Player player, int itemSlotOrder) {

        if (player.getLootkeySettings().getCurrentItemsInChest() != null) {
            return;
        }

        Analytics.flagInteraction(player, Analytics.InteractionType.LOOT_CHEST);
        player.setAnimation(new Animation(832));

        var map = player.getInventory().getContainer().findAllById(LOOT_KEY_ITEM_ID);
        var keys = map.keySet().toArray();
        var index = (Integer) keys[itemSlotOrder];
        Item lootKeyItem = player.getInventory().getItem(index);

        if (lootKeyItem == null && lootKeyItem.getId() != LOOT_KEY_ITEM_ID) {
            return;
        }

        Type listItemType = new TypeToken<Int2ObjectLinkedOpenHashMap<Item>>() {}.getType();
        Object attributes = lootKeyItem.getAttribute(LOOT_KEY_ITEM_LOOT_ITEMS_ATTR);
        if (attributes == null) {
            player.getInventory().deleteItem(index, new Item(LOOT_KEY_ITEM_ID));
            player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
            return;
        }

        var result = player.getInventory().deleteItem(index, new Item(LOOT_KEY_ITEM_ID));
        if (result.getSucceededAmount() == 1) {
            player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
            Int2ObjectLinkedOpenHashMap<Item> getItems = LoginManager.gson.get().fromJson(attributes.toString(), listItemType);
            player.getLootkeySettings().setCurrentItemsInChest(getItems);
            player.getLootkeySettings().incrementKeysClaimed();

            LootkeySettings.sendOpenChest(player);
            WorldTasksManager.schedule(() -> openInterface(player));
        }
    }


    private static void openInterface(Player player) {
        GameInterface.WILDERNESS_LOOT_KEY.open(player);
    }


    @Override
    public Object[] getObjects() {
        return new Object[] { 43485, 43486 };
    }
}
