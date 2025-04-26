package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.follower.impl.MiscPet;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.npcs.mimic.MimicInstance;
import com.zenyte.game.content.treasuretrails.rewards.ClueReward;
import com.zenyte.game.content.treasuretrails.rewards.ClueRewardTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ImmutableItem;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.area.Entrana;
import com.zenyte.plugins.dialogue.ItemChat;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Kris | 29/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TheMimicCasket extends ItemPlugin {
    private static final List<ImmutableItem> bonusLoot = Arrays.asList(new ImmutableItem(ItemId.DEATH_RUNE, 480, 600), new ImmutableItem(ItemId.BLOOD_RUNE, 400, 500), new ImmutableItem(ItemDefinitions.getOrThrow(ItemId.GRIMY_RANARR_WEED).getNotedOrDefault(), 25), new ImmutableItem(ItemDefinitions.getOrThrow(ItemId.RAW_MANTA_RAY).getNotedOrDefault(), 25), new ImmutableItem(ItemDefinitions.getOrThrow(ItemId.WINE_OF_ZAMORAK).getNotedOrDefault(), 25));
    private static final List<ImmutableItem> thirdAgeLoot = Arrays.asList(new ImmutableItem(ItemId._3RD_AGE_FULL_HELMET), new ImmutableItem(ItemId._3RD_AGE_PLATEBODY), new ImmutableItem(ItemId._3RD_AGE_PLATELEGS), new ImmutableItem(ItemId._3RD_AGE_PLATESKIRT), new ImmutableItem(ItemId._3RD_AGE_KITESHIELD), new ImmutableItem(ItemId._3RD_AGE_RANGE_COIF), new ImmutableItem(ItemId._3RD_AGE_RANGE_TOP), new ImmutableItem(ItemId._3RD_AGE_RANGE_LEGS), new ImmutableItem(ItemId._3RD_AGE_VAMBRACES), new ImmutableItem(ItemId._3RD_AGE_MAGE_HAT), new ImmutableItem(ItemId._3RD_AGE_ROBE_TOP), new ImmutableItem(ItemId._3RD_AGE_ROBE), new ImmutableItem(ItemId._3RD_AGE_AMULET), new ImmutableItem(ItemId._3RD_AGE_DRUIDIC_ROBE_TOP), new ImmutableItem(ItemId._3RD_AGE_DRUIDIC_ROBE_BOTTOMS), new ImmutableItem(ItemId._3RD_AGE_DRUIDIC_CLOAK), new ImmutableItem(ItemId._3RD_AGE_LONGSWORD), new ImmutableItem(ItemId._3RD_AGE_BOW), new ImmutableItem(ItemId._3RD_AGE_WAND), new ImmutableItem(ItemId._3RD_AGE_DRUIDIC_STAFF), new ImmutableItem(ItemId._3RD_AGE_CLOAK), new ImmutableItem(ItemId._3RD_AGE_PICKAXE), new ImmutableItem(ItemId._3RD_AGE_AXE));

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> {
            if (item.getNumericAttribute("The Mimic initialized").intValue() == 0) {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        options("Do you want a chance of a Mimic boss fight?", new DialogueOption("Yes, I want a chance of a Mimic boss fight.", () -> {
                            if (player.getInventory().getItem(slotId) != item) {
                                return;
                            }
                            item.setAttribute("The Mimic initialized", 1);
                            player.sendMessage("The chest turned out to be the Mimic!");
                        }), new DialogueOption("No, I don't want those.", () -> {
                            if (player.getInventory().getItem(slotId) != item) {
                                return;
                            }
                            player.getInventory().deleteItem(slotId, item);
                            final int casketId = item.getNumericAttribute("The Mimic original casket").intValue();
                            ClueCasket.open(player, new Item(casketId), Objects.requireNonNull(ClueReward.getTier(casketId)));
                        }));
                    }
                });
                return;
            }
            if (player.getArea() instanceof MimicInstance) {
                player.getDialogueManager().start(new ItemChat(player, new Item(ItemId.CASKET), "It'd be bad taste to" +
                        " try opening that in here."));
                return;
            }
            if (item.getNumericAttribute("The Mimic slain").intValue() == 1) {
                int remainingRolls = item.getNumericAttribute("The Mimic rolls").intValue();
                final int originalCasketId = item.getNumericAttribute("The Mimic original casket").intValue();
                final ClueLevel tier = Objects.requireNonNull(ClueReward.getTier(originalCasketId));
                final boolean isFirstAttempt = remainingRolls == (tier == ClueLevel.MASTER ? 6 : 5);
                final boolean isRingLoot = Utils.random(isFirstAttempt ? 25 : (40 + ((6 - remainingRolls) * 4))) == 0;
                if (isRingLoot) {
                    remainingRolls--;
                }
                player.getInventory().deleteItem(slotId, item);
                final ClueRewardTable rewards = Objects.requireNonNull(ClueReward.getTable(originalCasketId));
                final ObjectArrayList<Item> loot = new ObjectArrayList<Item>();
                if (isRingLoot) {
                    loot.add(new Item(ItemId.RING_OF_3RD_AGE));
                }
                final int rate = 228 + ((6 - remainingRolls) * 22);
                if (Utils.random(rate - 1) == 0) {
                    final ImmutableItem randomBonusLoot = thirdAgeLoot.get(Utils.random(thirdAgeLoot.size() - 1));
                    final Item element = new Item(randomBonusLoot.getId());
                    loot.add(element);
                    player.sendMessage("The Mimic adds a little extra reward for you: " + Colour.RED.wrap(element.getAmount() + " x " + element.getName()));
                } else {
                    //50% chance to land on bonus loot if not third-age.
                    if (Utils.random(1) == 0) {
                        final ImmutableItem randomBonusLoot = bonusLoot.get(Utils.random(bonusLoot.size() - 1));
                        final Item element = new Item(randomBonusLoot.getId(), Utils.random(1) == 0 ? randomBonusLoot.getMinAmount() : randomBonusLoot.getMaxAmount());
                        loot.add(element);
                        player.sendMessage("The Mimic adds a little extra reward for you: " + Colour.RED.wrap(element.getAmount() + " x " + element.getName()));
                    }
                }
                loot.addAll(rewards.roll(remainingRolls, remainingRolls, player.inArea(Entrana.class), true));
                player.sendMessage("Well done, you've completed the Treasure Trail!");
                final String tierString = tier.toString().toLowerCase();
                final int count = player.getNumericAttribute("completed " + tierString + " treasure trails").intValue() + 1;
                player.addAttribute("completed " + tierString + " treasure trails", count);
                player.sendMessage("<col=3300ff>You have completed " + count + " " + tierString + " Treasure Trail" + (count == 1 ? "" : "s") + ".</col>");
                final int requirement = tier.getMilestoneRequirement();
                if (requirement != -1 && requirement <= count) {
                    tier.getMilestoneRewardConsumer().accept(player);
                }
                long value = 0;
                for (final Item it : loot) {
                    value += it.getSellPrice() * it.getAmount();
                    WorldBroadcasts.broadcast(player, BroadcastType.TREASURE_TRAILS, it.getId(), tierString);
                }
                player.sendMessage(Colour.RED.wrap("Your treasure is worth around " + StringFormatUtil.format(value) + " gold!"));
                player.getTemporaryAttributes().put("treasure trails loot", loot);
                if (tier == ClueLevel.MASTER) {
                    MiscPet.BLOODHOUND.roll(player, 999);
                }
                player.getMusic().playJingle(193);
                GameInterface.CLUE_SCROLL_REWARD.open(player);
                return;
            }
            player.getDialogueManager().start(new ItemChat(player, new Item(ItemId.CASKET), "Perhaps I should take this to Watson."));
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.MIMIC};
    }
}
