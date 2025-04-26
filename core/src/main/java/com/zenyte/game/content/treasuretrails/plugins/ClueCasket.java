package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.content.follower.impl.MiscPet;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.rewards.ClueReward;
import com.zenyte.game.content.treasuretrails.rewards.ClueRewardTable;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.area.Entrana;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Kris | 25/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClueCasket extends ItemPlugin {
    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> {
            final ClueLevel tier = Objects.requireNonNull(ClueReward.getTier(item.getId()));
            Item mimic = null;
            int mimicSlot = -1;
            if (item.getNumericAttribute("The Mimic").intValue() == 0 && TreasureTrail.isMimicEnabled(player)) {
                if (tier == ClueLevel.ELITE || tier == ClueLevel.MASTER) {
                    final int rate = tier == ClueLevel.MASTER ? 15 : 35;
                    if (Utils.random(rate - 1) == 0) {
                        mimic = new Item(ItemId.MIMIC);
                        mimic.setAttribute("The Mimic", 1);
                        mimic.setAttribute("The Mimic rolls", tier == ClueLevel.MASTER ? 6 : 5);
                        mimic.setAttribute("The Mimic original casket", item.getId());
                        player.getInventory().deleteItem(item.getId(), 1);
                        final boolean full = !player.getInventory().hasFreeSlots();
                        player.getInventory().addOrDrop(mimic);
                        Item finalMimic1 = mimic;
                        final Optional<Int2ObjectMap.Entry<Item>> entry = player.getInventory().getContainer().getItems().int2ObjectEntrySet().stream().filter(it -> it.getValue().equals(finalMimic1)).findAny();
                        if (entry.isPresent()) {
                            //Set mimic instance to the item object in inventory, as a copy is made whenever an item is added to the inventory.
                            mimicSlot = entry.get().getIntKey();
                            mimic = player.getInventory().getItem(mimicSlot);
                        }
                        //Handle exception case.
                        if (full) {
                            player.sendMessage("You found a Mimic!");
                            player.getInterfaceHandler().closeInterfaces();
                            return;
                        }
                    }
                }
            }
            final boolean isMimic = mimic != null;
            if (isMimic) {
                final Item finalMimic = Objects.requireNonNull(mimic);
                final int finalMimicSlot = mimicSlot;
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        options("Do you want a chance of a Mimic boss fight?", new DialogueOption("Yes, I want a chance of a Mimic boss fight.", () -> {
                            if (!player.getInventory().containsItem(finalMimic)) {
                                return;
                            }
                            finalMimic.setAttribute("The Mimic initialized", 1);
                            player.sendMessage("The chest turned out to be the Mimic!");
                        }), new DialogueOption("No, I don't want those.", () -> {
                            if (!player.getInventory().containsItem(finalMimic)) {
                                return;
                            }
                            player.getInventory().deleteItem(finalMimicSlot, finalMimic);
                            final int casketId = finalMimic.getNumericAttribute("The Mimic original casket").intValue();
                            ClueCasket.open(player, new Item(casketId), Objects.requireNonNull(ClueReward.getTier(casketId)));
                        }));
                    }
                });
                return;
            }
            player.getInventory().deleteItem(new Item(item.getId()));
            open(player, item, tier);
        });
    }

    static final void open(@NotNull final Player player, @NotNull final Item item, @NotNull final ClueLevel tier) {
        boolean booster = false;
        if (player.getVariables().getClueBoosterLeft() > 0) {
            player.getVariables().setClueBoosterLeft(player.getVariables().getClueBoosterLeft() - 1);
            booster = true;
        }

        AdventCalendarManager.increaseChallengeProgress(player, 2022, 14, 1);
        final ClueRewardTable rewards = Objects.requireNonNull(ClueReward.getTable(item.getId()));
        final List<Item> loot = rewards.roll(player.inArea(Entrana.class), World.hasBoost(XamphurBoost.BONUS_CLUE_LOOT), booster);
        player.sendMessage("Well done, you've completed the Treasure Trail!");
        final String tierString = tier.toString().toLowerCase();
        final int count = player.getNumericAttribute("completed " + tierString + " treasure trails").intValue() + 1;
        player.addAttribute("completed " + tierString + " treasure trails", count);
        player.sendMessage(Colour.RED.wrap("You have completed " + count + " " + tierString + " Treasure Trail" + (count == 1 ? "" : "s") + "."));
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
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.REWARD_CASKET_BEGINNER, ItemId.REWARD_CASKET_EASY, ItemId.REWARD_CASKET_MEDIUM, ItemId.REWARD_CASKET_HARD, ItemId.REWARD_CASKET_ELITE, ItemId.REWARD_CASKET_MASTER};
    }
}
