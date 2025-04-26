package com.zenyte.plugins.renewednpc;

import com.near_reality.game.content.bountyhunter.BountyHunterController;
import com.zenyte.game.content.universalshop.UniversalShopInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.shop.Shop;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.PlainChat;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.item.ItemId.BLOOD_MONEY;

/**
 * @author Kris | 07/05/2019 19:42
 * @author Andys1814 | 08/04/2022
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class EdgevilleEmblemTrader extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("Hello, wanderer.");
                    final ObjectArrayList<Dialogue.DialogueOption> optionList = new ObjectArrayList<>();
                    optionList.add(new DialogueOption("What rewards have you got?", () -> Shop.get("Blood money Store", player.isIronman(), player).open(player)));
                    if (player.getVariables().isSkulled()) {
                        optionList.add(new DialogueOption("Can you make my PK skull last longer?", () -> promptSkull(player)));
                    } else {
                        optionList.add(new DialogueOption("Can I have a PK skull, please?", () -> promptSkull(player)));
                    }
                    if (hasEmblem(player)) {
                        optionList.add(new DialogueOption("I want to exchange my archaic emblems.", () -> {
                            player.getDialogueManager().start(new Dialogue(player, npc) {
                                @Override
                                public void buildDialogue() {
                                    npc("Would you like to sell them all to me for " + StringFormatUtil.format(getEmblemsReward(player)) + " Blood money?");
                                    options("Sell all mysterious emblems?", new DialogueOption("Sell all emblems.", () -> sellEmblems(player)), new DialogueOption("No, keep the emblems."));
                                }
                            });
                        }));
                    }
                    optionList.add(new DialogueOption("I want to exchange my bounty hunter emblems.", () -> BountyHunterController.processEmblemExchange(player)));
                    options(TITLE, optionList.toArray(new DialogueOption[0]));
                    player(25, "That's nice.");
                }
            });
        });
        bind("Rewards", (player, npc) -> UniversalShopInterface.openInterfaceToTab(player, 11));
        bind("BH Shop", (player, npc) -> UniversalShopInterface.openInterfaceToTab(player, 9));
        bind("Exchange Emblems", (player, npc) -> BountyHunterController.processEmblemExchange(player));

    }

    public static void promptSkull(@NotNull final Player player) {
        player.getDialogueManager().finish();
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Obtain a PK skull?", new DialogueOption("Yes, skull me.", () -> player.getVariables().setSkull(true)), new DialogueOption("No, don't skull me."));
            }
        });
    }


    @Override
    public int[] getNPCs() {
        return new int[] {308};
    }

    private enum Emblem {
        TIER_ONE(12746, 15),
        TIER_TWO(12748, 20),
        TIER_THREE(12749, 30),
        TIER_FOUR(12750, 45),
        TIER_FIVE(12751, 60),
        TIER_SIX(12752, 100),
        TIER_SEVEN(12753, 130),
        TIER_EIGHT(12754, 180),
        TIER_NINE(12755, 250),
        TIER_TEN(12756, 350);

        private static final Emblem[] values = values();
        private final int id;
        private final int bloodMoneyReward;

        private static int getReward(final int item) {
            final int unnoted = ItemDefinitions.getOrThrow(item).getUnnotedOrDefault();
            for (final EdgevilleEmblemTrader.Emblem value : values) {
                if (value.id == unnoted) {
                    return value.bloodMoneyReward;
                }
            }
            throw new IllegalStateException();
        }

        Emblem(int id, int bloodMoneyReward) {
            this.id = id;
            this.bloodMoneyReward = bloodMoneyReward;
        }
    }

    private boolean hasEmblem(@NotNull final Player player) {
        final Inventory inventory = player.getInventory();
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null || !(item.getId() >= 12746 && item.getId() <= 12756) || item.getId() == 12747) {
                continue;
            }
            return true;
        }
        return false;
    }

    private int getEmblemsReward(@NotNull final Player player) {
        final Inventory inventory = player.getInventory();

        int total = 0;
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null || !(item.getId() >= 12746 && item.getId() <= 12756) || item.getId() == 12747) {
                continue;
            }
            total += item.getAmount() * Emblem.getReward(item.getId());
        }

        return total;
    }

    private void sellEmblems(@NotNull final Player player) {
        final Inventory inventory = player.getInventory();

        int total = 0;
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null || !(item.getId() >= 12746 && item.getId() <= 12756) || item.getId() == 12747) {
                continue;
            }
            final int count = inventory.deleteItem(item).getSucceededAmount();
            total += count * Emblem.getReward(item.getId());
        }

        player.getInventory().addOrDrop(BLOOD_MONEY, total);
        player.getDialogueManager().start(new PlainChat(player, "The Emblem Trader awards you with " + total + " Blood Money in exchange for your emblem(s)."));
    }

}
