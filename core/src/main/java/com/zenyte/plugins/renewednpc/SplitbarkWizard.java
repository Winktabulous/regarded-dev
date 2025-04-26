package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.plugins.dialogue.MakeType;
import com.zenyte.plugins.dialogue.SkillDialogue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Kris | 26/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SplitbarkWizard extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Hello there, can I help you?");
                options(new DialogueOption("What do you do here?", key(100)), new DialogueOption("What's that you're " +
                        "wearing?", key(200)), new DialogueOption("Can you make me some armour please?", key(300)), new DialogueOption("No thanks.", key(400)));
                player(100, "What do you do here?");
                npc("I've been studying the practice of making split-bark armour.");
                options(new DialogueOption("Split-bark armour, what's that?", key(110)), new DialogueOption("Can you make me some?", key(130)));
                player(110, "Split-bark armour, what's that?");
                npc("Split-bar armour is special armour for mages. It's much more resistant to physical attacks than " +
                        "normal robes.");
                npc("It's actually very easy for me to make, but I've been having trouble getting hold of the pieces.");
                options(new DialogueOption("Well good luck with that.", key(120)), new DialogueOption("Can you make me some?", key(130)));
                player(120, "Well good luck with that.");
                player(130, "Can you make me some?");
                //Original message: Unfortunately both these items can only be found in Morytania, especially the cloth which is found in the tombs of shades.
                npc("I need bark from a hollow tree, and some fine cloth. Unfortunately, bark can only be found in Morytania. Jackie at Edgeville will sell you fine cloth.");
                npc("Of course I'd happily sell you some at a discounted price if you bring me those items.");
                options(new DialogueOption("Okay, guess I'll go looking then!", key(140)), new DialogueOption("Okay, how much do I need?", key(150)));
                player(140, "Okay, guess I'll go looking then!");
                player(150, "Okay, how much do I need?");
                npc("I need 1 piece of each for either gloves or boots,<br>2 pieces of each for a hat,<br>3 pieces of each for leggings,<br>and 4 pieces of each for a top.");
                npc("I'll charge you 1,000 coins for either gloves or boots,<br>6,000 coins for a hat,<br>32,000 " +
                        "coins for leggings,<br>and 37,000 for a top.").executeAction(key(140));
                player(200, "What's that you're wearing?");
                npc("This is split-bark armour, it's special armour for mages. It's much more resistant to physical " +
                        "attacks than normal robes.");
                npc("It's actually very easy for me to make, but I've been having trouble getting hold of the pieces.");
                options(new DialogueOption("Well good luck with that.", key(120)), new DialogueOption("Can you make me some?", key(130)));
                player(300, "Can you make me some armour please?");
                npc("Certainly, what would you like me to make?").executeAction(() -> sendMenu(player));
                player(400, "No thanks.");
            }
        }));
        bind("Trade", (player, npc) -> sendMenu(player));
    }


    private enum SplitbarkArmour {
        HELM(ItemId.SPLITBARK_HELM, 2, 6000), BODY(ItemId.SPLITBARK_BODY, 4, 37000), LEGS(ItemId.SPLITBARK_LEGS, 3, 32000), GLOVES(ItemId.SPLITBARK_GAUNTLETS, 1, 1000), BOOTS(ItemId.SPLITBARK_BOOTS, 1, 1000);
        private final int product;
        private final int materialCount;
        private final int price;
        private static final List<SplitbarkArmour> values = Collections.unmodifiableList(Arrays.asList(values()));

        SplitbarkArmour(int product, int materialCount, int price) {
            this.product = product;
            this.materialCount = materialCount;
            this.price = price;
        }
    }

    private final void sendMenu(@NotNull final Player player) {
        final ObjectArrayList<Item> list = new ObjectArrayList<Item>();
        for (final SplitbarkWizard.SplitbarkArmour value : SplitbarkArmour.values) {
            list.add(new Item(value.product));
        }
        final Inventory inventory = player.getInventory();
        final int maxQuantityMaterialWise = Math.min(inventory.getAmountOf(ItemId.FINE_CLOTH), inventory.getAmountOf(ItemId.BARK));
        final int maxQuantityCoinsWise = inventory.getAmountOf(ItemId.COINS_995) / 1000;
        final int maxQuantity = Math.max(1, Math.min(maxQuantityMaterialWise, maxQuantityCoinsWise));
        player.getDialogueManager().start(new SkillDialogue(player, "How many do you wish to buy?", list.toArray(new Item[0])) {
            @Override
            public MakeType type() {
                return MakeType.BUY;
            }
            @Override
            public int getMaximumAmount() {
                return maxQuantity;
            }
            @Override
            public void run(int slotId, int amount) {
                final SplitbarkWizard.SplitbarkArmour constant = Objects.requireNonNull(CollectionUtils.findMatching(SplitbarkArmour.values, value -> value.product == list.get(slotId).getId()));
                if (!inventory.containsItem(ItemId.BARK, constant.materialCount) || !inventory.containsItem(ItemId.FINE_CLOTH, constant.materialCount) || !inventory.containsItem(ItemId.COINS_995, constant.price)) {
                    final String quantifiedLabel = constant.materialCount == 1 ? "1 piece of" : (constant.materialCount + " pieces of");
                    player.getDialogueManager().start(new NPCChat(player, NpcId.WIZARD_JALARAST, "You need " + quantifiedLabel + " bark, " + quantifiedLabel + " of fine cloth and " + StringFormatUtil.format(constant.price) + " coins for a " + ItemDefinitions.getOrThrow(constant.product).getName().toLowerCase() + "."));
                    return;
                }
                final int maxQuantityMaterialWise = Math.min(inventory.getAmountOf(ItemId.FINE_CLOTH), inventory.getAmountOf(ItemId.BARK)) / constant.materialCount;
                final int maxQuantityCoinsWise = inventory.getAmountOf(ItemId.COINS_995) / constant.price;
                final int maxQuantity = Math.min(amount, Math.min(maxQuantityMaterialWise, maxQuantityCoinsWise));
                if (maxQuantity <= 0) {
                    return;
                }
                inventory.deleteItem(ItemId.BARK, maxQuantity * constant.materialCount);
                inventory.deleteItem(ItemId.FINE_CLOTH, maxQuantity * constant.materialCount);
                inventory.deleteItem(ItemId.COINS_995, maxQuantity * constant.price);
                inventory.addOrDrop(new Item(constant.product, maxQuantity));
                player.getDialogueManager().start(new NPCChat(player, NpcId.WIZARD_JALARAST, "There you go, enjoy your new armour!"));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {NpcId.WIZARD_JALARAST};
    }
}
