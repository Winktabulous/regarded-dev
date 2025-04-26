package com.zenyte.game.content.skills.farming.plugins;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.skills.farming.*;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.entity.player.dialogue.NPCMessage;
import com.zenyte.game.world.entity.player.dialogue.PlayerMessage;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.PlayerChat;

import java.util.ArrayList;
import java.util.Set;

import static com.zenyte.game.content.skills.farming.FarmingProduct.MAGIC;
import static com.zenyte.game.content.skills.farming.FarmingProduct.POISON_IVY;
import static com.zenyte.game.content.skills.farming.PatchState.*;

/**
 * @author Kris | 05/02/2019 19:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Gardener extends NPCPlugin {

    private final String[] tips = new String[] { "Hops are good for brewing ales. I believe there's a brewery up in " + "Keldagrim somewhere, and I've heard rumours that a place called Phasmatys used to be good for that type " + "of thing. 'Fore they all died, of course.", "You can fill plantpots with soil from any empty patch, if you have a gardening trowel.", "You can buy all the farming tools from farming shops, which can be found close to the allotments.", "If you need to get rid of your fruit trees for any reason, all you have to do is chop them down and then dig up the stump.", "Don't just throw away your weeds after you've raked a patch - put them in a compost bin and make some compost.", "Vegetables, hops and flowers need constant watering - if you ignore my advice, you will sooner or later find yourself in possession of a dead farming patch.", "You don't have to buy all your plantpots you know, you can make them yourself on a pottery wheel. If you're a good enough craftsman, that is.", "Bittercap mushrooms can only be grown in a special patch in Morytania, near the Mort Myre swamp. There the ground is especially dank and suited to growing poisonous fungii.", "Applying compost to a patch will not only reduce the chance that your crops will get diseased, but you will also grow more crops to harvest.", "You can put up to ten potatoes, cabbages or onions in vegetable sacks, although you can't have a mix in the same sack.", "There is a special patch for growing Belladonna - I believe that is is somewhere near Draynor Manor, where the ground is a tad 'unblessed'.", "The only way to cure a bush or a tree of disease is to prune away the diseased leaves with a pair of secateurs. For all other crops I would just apply some plant-cure.", "There are six main Farming areas - Elstan's area near Falador, Dantaera's area near Catherby, Kragen's area near Ardougne, Lyra's area in Morytania, Marisi's area in Hosidius and Alan's area in the Farming Guild.", "Tree seeds must be grown in a plantpot of soil into a sapling, and then transferred to a tree patch to continue growing to adulthood.", "Supercompost is far better than normal compost, but more expensive to make. You need to rot the right type of item; show me an item, and I'll tell you if it's super-compostable or not.", "You can put up to five tomatoes, strawberries, apples, bananas or oranges into a fruit basket, although you can't have a mix in the same basket.", "If you want to make your own sacks and baskets you'll need to use the loom that's near the Farming shop in Falador. If you're a good enough craftsman, that is." };

    @Override
    public void handle() {
        bindOptions(option -> option.startsWith("Pay"), (player, npc, option) -> {
            final FarmingPatch patch = FarmingPatch.getPatchByGardener(npc.getId(), option.getId()).orElseThrow(RuntimeException::new);
            final FarmingSpot spot = player.getFarming().create(patch);
            final PatchState state = spot.getState();
            if (spot.isTreePatch() && (state == GROWN || state == DISEASED || state == DEAD || state == REGAINING_PRODUCE)) {
                startChopDownDialogue(player, npc, patch);
            } else {
                if (npc.getId() == NpcId.ALAN) {
                    player.getDialogueManager().start(new Dialogue(player, npc) {

                        @Override
                        public void buildDialogue() {
                            npc("Which patch do you want me to look after?");
                            options(new DialogueOption("The Cactus Patch", () -> startPaymentDialogue(player, npc, FarmingPatch.FARMING_GUILD_CACTUS, true)), new DialogueOption("The Northern Allotment Patch", () -> startPaymentDialogue(player, npc, FarmingPatch.FARMING_GUILD_NORTH_ALLOTMENT, true)), new DialogueOption("The Southern Allotment Patch", () -> startPaymentDialogue(player, npc, FarmingPatch.FARMING_GUILD_SOUTH_ALLOTMENT, true)), new DialogueOption("The Bush Patch", () -> startPaymentDialogue(player, npc, FarmingPatch.FARMING_GUILD_BUSH, true)));
                        }
                    });
                    return;
                }
                startPaymentDialogue(player, npc, patch, true);
            }
        });
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            final Set<FarmingPatch> set = FarmingPatch.getPatchSetByGardeners(npc.getId()).orElseThrow(RuntimeException::new);
            assert !set.isEmpty();
            // If multiple patches tied to the gardener, it is guaranteed to be an allotment patch.
            final boolean multiple = set.size() > 1;
            final FarmingPatch patch = set.stream().findFirst().orElseThrow(RuntimeException::new);
            final FarmingSpot spot = player.getFarming().create(patch);
            final PatchState state = spot.getState();
            final boolean chopOptions = spot.getProduct().isTree() && (state == GROWN || state == DEAD || state == DISEASED || state == REGAINING_PRODUCE);
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    options();
                    buildTips();
                    buildSellOptions();
                    player(300, "I'll come back another time.");
                    if (chopOptions) {
                        buildChopOptions();
                    } else {
                        buildWatchOverOptions();
                    }
                }

                private void options() {
                    options(TITLE, new DialogueOption(chopOptions ? "Would you chop my tree down for me?" : "Would you look after my crops for me?", key(5)), new DialogueOption("Can you give me any farming advice?", key(100)), new DialogueOption("Can you sell me something?", key(200)), new DialogueOption("I'll come back another time.", key(300)));
                }

                private void buildTips() {
                    // Sets the new random hint
                    player(100, "Can you give me any farming advice?").executeAction(() -> {
                        final NPCMessage msg = new NPCMessage(npcId, Expression.CALM, Utils.random(tips));
                        msg.executeAction(key(1));
                        dialogue.put(101, msg);
                    });
                    // Reserve the next message/stage due to flow.
                    npc("");
                }

                private void buildWatchOverOptions() {
                    final PlayerMessage message = player(5, "Would you look after my crops for me?");
                    if (multiple) {
                        npc("I might - which patch were you thinking of?");
                        final ArrayList<Dialogue.DialogueOption> list = new ArrayList<DialogueOption>();
                        for (final FarmingPatch patch : set) {
                            list.add(new DialogueOption("The " + patch.getDescription().toLowerCase(), () -> startPaymentDialogue(player, npc, patch, false)));
                        }
                        options(TITLE, list.toArray(new DialogueOption[0]));
                    } else {
                        message.executeAction(() -> startPaymentDialogue(player, npc, patch, false));
                    }
                }

                private void buildChopOptions() {
                    player(5, "Would you chop my tree down for me?");
                    if (spot.getState() == STUMP) {
                        npc("That's a stump. It'll grow back if you just leave it alone for a while.");
                        return;
                    }
                    npc("Why? You look like you could chop it down yourself!");
                    options(TITLE, new DialogueOption("Yes, you're right - I'll do it myself.", key(10)), new DialogueOption("I can't be bothered - I'd rather pay you to do it.", key(20)));
                    player(10, "Yes, you're right - I'll do it myself.");
                    player(20, "I can't be bothered - I'd rather pay you to do it.");
                    npc("Well, it's a lot of hard work - if you pay me 200 Coins, I'll chop it down for you.");
                    if (!player.getInventory().containsItem(new Item(995, 200))) {
                        player("I don't have 200 Coins I'm afraid.");
                        return;
                    }
                    options(TITLE, new DialogueOption("Here's 200 Coins - chop my tree down please.", key(30)), new DialogueOption("I don't want to pay that much, sorry.", key(40)));
                    player(30, "Here's 200 Coins - chop my tree down please.").executeAction(() -> player.getInventory().ifDeleteItem(new Item(995, 200), () -> player.getFarming().create(patch).clear()));
                    player(40, "I don't want to pay that much, sorry.");
                }

                private void buildSellOptions() {
                    player(200, "Can you sell me something?");
                    npc("That depends on whether I have it to sell. What is it that you're looking for?");
                    options(TITLE, new DialogueOption("Some plant cure.", key(210)), new DialogueOption("A bucket of compost.", key(220)), new DialogueOption("A rake.", key(230)), new DialogueOption("A plant pot.", key(240)), new DialogueOption("(See more items)", key(205)));
                    options(205, TITLE, new DialogueOption("A watering can.", key(250)), new DialogueOption("A gardening trowel.", key(260)), new DialogueOption("A seed dibber.", key(270)), new DialogueOption("(See previous items)", key(202)), new DialogueOption("Forget it.", key(290)));
                    player(290, "Forget it, you don't have anything I need.");
                    player(218, "No thanks, I can get that much cheaper elsewhere.");
                    player(210, "Some plant cure.");
                    npc("Plant cure, eh? I might have some put aside for myself. Tell you what, I'll sell you some " + "plant cure for 25 coins if you like.");
                    if (!player.getInventory().containsItem(new Item(995, 25))) {
                        player("I don't have enough money for that, I'm afraid.");
                    } else {
                        options(TITLE, new DialogueOption("Yeah, that sounds like a fair price.", key(215)), new DialogueOption("No thanks, I can get that much cheaper elsewhere.", key(218)));
                        player(215, "Yes, that sounds like a fair price.").executeAction(purchase(new Item(995, 25), FarmingConstants.PLANT_CURE));
                        item(FarmingConstants.PLANT_CURE, npc.getName(player) + " hands over your purchase.");
                    }
                    player(220, "A bucket of compost.");
                    npc("A bucket of compost, eh? I might have one spare... tell you what, I'll sell it to you for 35" + " coins if you like.");
                    if (!player.getInventory().containsItem(new Item(995, 35))) {
                        player("I don't have enough money for that, I'm afraid.");
                    } else {
                        options(TITLE, new DialogueOption("Yeah, that sounds like a fair price.", key(225)), new DialogueOption("No thanks, I can get that much cheaper elsewhere.", key(218)));
                        player(225, "Yes, that sounds like a fair price.").executeAction(purchase(new Item(995, 35), FarmingConstants.COMPOST));
                        item(FarmingConstants.COMPOST, npc.getName(player) + " hands over your purchase.");
                    }
                    player(230, "A rake.");
                    npc("A rake, eh? I might have one spare... tell you what, I'll sell it to you for 15 coins if you" + " like.");
                    if (!player.getInventory().containsItem(new Item(995, 15))) {
                        player("I don't have enough money for that, I'm afraid.");
                    } else {
                        options(TITLE, new DialogueOption("Yeah, that sounds like a fair price.", key(235)), new DialogueOption("No thanks, I can get that much cheaper elsewhere.", key(218)));
                        player(235, "Yes, that sounds like a fair price.").executeAction(purchase(new Item(995, 15), FarmingConstants.RAKE));
                        item(FarmingConstants.RAKE, npc.getName(player) + " hands over your purchase.");
                    }
                    player(240, "A plant pot.");
                    npc("Eh, I suppose I can spare you a plant pot, but I'll want 40 coins for it.");
                    if (!player.getInventory().containsItem(new Item(995, 40))) {
                        player("I don't have enough money for that, I'm afraid.");
                    } else {
                        options(TITLE, new DialogueOption("Yeah, that sounds like a fair price.", key(245)), new DialogueOption("No thanks, I can get that much cheaper elsewhere.", key(218)));
                        player(245, "Yes, that sounds like a fair price.").executeAction(purchase(new Item(995, 40), FarmingConstants.PLANT_POT));
                        item(FarmingConstants.PLANT_POT, npc.getName(player) + " hands over your purchase.");
                    }
                    player(250, "A watering can.");
                    npc("A watering can, eh? I might have one spare... tell you what, I'll sell it to you for 25 " + "coins if you like.");
                    if (!player.getInventory().containsItem(new Item(995, 25))) {
                        player("I don't have enough money for that, I'm afraid.");
                    } else {
                        options(TITLE, new DialogueOption("Yeah, that sounds like a fair price.", key(255)), new DialogueOption("No thanks, I can get that much cheaper elsewhere.", key(218)));
                        player(255, "Yes, that sounds like a fair price.").executeAction(purchase(new Item(995, 25), FarmingConstants.WATERING_CAN));
                        item(FarmingConstants.WATERING_CAN, npc.getName(player) + " hands over your purchase.");
                    }
                    player(260, "A gardening trowel.");
                    npc("A gardening trowel, eh? I might have one spare... tell you what, I'll sell it to you for 15 " + "coins if you like.");
                    if (!player.getInventory().containsItem(new Item(995, 15))) {
                        player("I don't have enough money for that, I'm afraid.");
                    } else {
                        options(TITLE, new DialogueOption("Yeah, that sounds like a fair price.", key(265)), new DialogueOption("No thanks, I can get that much cheaper elsewhere.", key(218)));
                        player(265, "Yes, that sounds like a fair price.").executeAction(purchase(new Item(995, 15), FarmingConstants.GARDENING_TROWEL));
                        item(FarmingConstants.GARDENING_TROWEL, npc.getName(player) + " hands over your purchase.");
                    }
                    player(270, "A seed dibber.");
                    npc("A seed dibber, eh? I might have one spare... tell you what, I'll sell it to you for 15 coins" + " if you like.");
                    if (!player.getInventory().containsItem(new Item(995, 15))) {
                        player("I don't have enough money for that, I'm afraid.");
                    } else {
                        options(TITLE, new DialogueOption("Yeah, that sounds like a fair price.", key(275)), new DialogueOption("No thanks, I can get that much cheaper elsewhere.", key(218)));
                        player(275, "Yes, that sounds like a fair price.").executeAction(purchase(new Item(995, 15), FarmingConstants.SEED_DIBBER));
                        item(FarmingConstants.SEED_DIBBER, npc.getName(player) + " hands over your purchase.");
                    }
                }

                private Runnable purchase(final Item coins, final Item reward) {
                    return () -> player.getInventory().ifDeleteItem(coins, () -> player.getInventory().addOrDrop(reward));
                }
            });
        });
    }

    private void startChopDownDialogue(final Player player, final NPC npc, final FarmingPatch patch) {
        player.getDialogueManager().finish();
        final FarmingSpot spot = player.getFarming().create(patch);
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                if (spot.getState() == STUMP) {
                    npc("That's a stump. It'll grow back if you just leave it alone for a while.");
                    return;
                }
                final int cost = spot.getPatch().getType() == PatchType.REDWOOD_PATCH ? 3000 : 200;
                options("Pay " + cost + " coins to have the tree chopped down?", new DialogueOption("Pay " + cost + " coins.", () -> {
                    player.getDialogueManager().finish();
                    if (!player.getInventory().containsItem(new Item(995, cost))) {
                        player.getDialogueManager().start(new PlayerChat(player, "I don't have " + cost + " Coins " + "I'm afraid."));
                        return;
                    }
                    player.getInventory().ifDeleteItem(new Item(995, cost), () -> {
                        if (spot.getPatch().getType() == PatchType.REDWOOD_PATCH) {
                            new FadeScreen(player, () -> {
                                player.getFarming().create(patch).clear();
                                player.getDialogueManager().start(new PlainChat(player, npc.getName(player) + " chops the tree down for you."));
                            }).fade(3);
                            return;
                        }
                        player.getFarming().create(patch).clear();
                        player.getDialogueManager().start(new PlainChat(player, npc.getName(player) + " chops the tree down for you."));
                    });
                }), new DialogueOption("No."));
            }
        });
    }

    private void startPaymentDialogue(final Player player, final NPC npc, final FarmingPatch patch, final boolean quickPay) {
        player.getDialogueManager().finish();
        final FarmingSpot spot = player.getFarming().create(patch);
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                if (spot.containsFlag(PatchFlag.WATCHED_OVER)) {
                    if (quickPay) {
                        npc("I'm already looking after that patch for you.");
                    } else {
                        npc("I don't know what you're talking about - I'm already looking after that patch for you.");
                        player("Oh sorry, I forgot.");
                    }
                    return;
                }
                final PatchState state = spot.getState();
                if (state == PatchState.WEEDS) {
                    if (spot.isTreePatch()) {
                        npc("You don't have a sapling planted in that patch. Plant one and I might agree to look " + "after it for you.");
                    } else {
                        npc("You don't have any seeds planted in that patch. Plant some and I might agree to look " + "after it for you.");
                    }
                    return;
                } else if (state == GROWN || state == REGAINING_PRODUCE || state == STUMP || state == HEALTH_CHECK) {
                    npc("That patch is already fully grown! I don't know what you want me to do with it!");
                    return;
                } else if (state == DEAD) {
                    npc("The patch is already dead! There's nothing more I can do here.");
                    return;
                }
                final FarmingProduct product = spot.getProduct();
                if (product == POISON_IVY) {
                    npc("Poison ivy is pretty hardy stuff, most animals will avoid eating it.");
                    return;
                }
                final Item[] payment = product.getPayment();
                Preconditions.checkArgument(payment != null);
                final StringBuilder builder = new StringBuilder();
                int index = 0;
                final int length = payment.length;
                for (final Item item : payment) {
                    index++;
                    if (item == null)
                        continue;
                    final String prefix = length > 1 && index == (length - 1) ? " and " : ", ";
                    if (item.getName().endsWith("(5)") || item.getName().endsWith("(10)")) {
                        builder.append(item.getAmount()).append(" ").append("basket").append(item.getAmount() == 1 ? "" : "s").append(" of ").append(item.getName().toLowerCase().replaceAll("[(5)]", "").replaceAll("[(10)]", "")).append(prefix);
                        continue;
                    }
                    builder.append(item.getAmount()).append(" ").append(item.getName().toLowerCase()).append(prefix);
                }
                final String pay = builder.delete(builder.length() - 2, builder.length()).toString();
                if (!containsPayment(player, payment)) {
                    npc("I want " + pay + " for that.");
                    return;
                }
                npc("If you like, but I want " + pay + " for that.");
                options(TITLE, new DialogueOption("Okay, it's a deal.", key(15)), new DialogueOption("No, that's too" + " much.", key(25)));
                player(15, "Okay, it's a deal.").executeAction(() -> {
                    removePayment(player, payment);
                    if (spot.getProduct() == MAGIC) {
                        player.getAchievementDiaries().update(WesternProvincesDiary.PROTECT_MAGIC_TREE);
                    }
                    spot.setFlag(PatchFlag.WATCHED_OVER);
                    if (state == DISEASED) {
                        spot.cure();
                    }
                });
                npc("That'll do nicely, sir. Leave it with me - I'll make sure the patch grows for you.");
                player(25, "No, that's too much.");
                npc("Well, I'm not wasting my time for free.");
            }
        });
    }

    private boolean containsPayment(final Player player, final Item[] items) {
        final Inventory inventory = player.getInventory();
        for (final Item item : items) {
            if (item == null)
                continue;
            final int id = item.getId();
            final int notedId = item.getDefinitions().getNotedOrDefault();
            int count = inventory.getAmountOf(id);
            if (notedId != id) {
                count += inventory.getAmountOf(notedId);
            }
            if (count < item.getAmount()) {
                return false;
            }
        }
        return true;
    }

    private void removePayment(final Player player, final Item[] items) {
        final Inventory inventory = player.getInventory();
        for (final Item item : items) {
            if (item == null)
                continue;
            final int id = item.getId();
            final int notedId = item.getDefinitions().getNotedOrDefault();
            int count = item.getAmount();
            count -= inventory.deleteItem(id, count).getSucceededAmount();
            if (count > 0) {
                inventory.deleteItem(notedId, count);
            }
        }
    }

    @Override
    public int[] getNPCs() {
        return FarmingPatch.getPatchSetByGardeners().keySet().toIntArray();
    }
}
