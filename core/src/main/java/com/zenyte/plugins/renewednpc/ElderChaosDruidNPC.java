package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.skills.prayer.actions.Bones;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;

/**
 * @author Kris | 24/01/2019 14:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ElderChaosDruidNPC extends NPCPlugin implements ItemOnNPCAction {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final Container inventory = player.getInventory().getContainer();
            for (int i = 0; i < 28; i++) {
                final Item item = inventory.get(i);
                if (item == null || !item.getDefinitions().isNoted()) {
                    continue;
                }
                final Bones bone = Bones.getBone(item.getDefinitions().getNotedId());
                if (bone == null)
                    continue;
                final int count = inventory.getAmountOf(item.getId());
                final int space = inventory.getFreeSlotsSize();
                final int coins = inventory.getAmountOf(995);
                player.getDialogueManager().start(new Dialogue(player, npc) {

                    @Override
                    public void buildDialogue() {
                        if (coins < 50) {
                            npc("You don't have enough coins to exchange bones.");
                            return;
                        }
                        final ArrayList<Dialogue.DialogueOption> optionsList = new ArrayList<DialogueOption>(5);
                        optionsList.add(new DialogueOption("Exchange '" + item.getName() + "': 50 coins", () -> exchange(player, item, 1)));
                        // Block of shit code to ensure we always calculate max possible, considering the fact
                        // the ingredients are removed first, thus giving us more free space in scenarios.
                        int costOfAll = Math.min(space, count) * 50;
                        if (space == (count - 2) && coins == costOfAll + 100) {
                            costOfAll += 100;
                        } else {
                            if (space == count - 1)
                                costOfAll += 50;
                            else if (coins == costOfAll + 50) {
                                costOfAll += 50;
                            }
                        }
                        if (costOfAll > coins) {
                            costOfAll = (int) Math.floor(coins / 50.0F) * 50;
                        }
                        if (costOfAll <= 0) {
                            npc("You don't have enough space to exchange the bones.");
                            return;
                        }
                        final int realCount = costOfAll / 50;
                        if (realCount >= 5) {
                            optionsList.add(new DialogueOption("Exchange 5: 250 coins", () -> exchange(player, item, 5)));
                        }
                        if (realCount > 1)
                            optionsList.add(new DialogueOption("Exchange All: " + costOfAll + " coins", () -> exchange(player, item, realCount)));
                        if (realCount > 1)
                            optionsList.add(new DialogueOption("Exchange X", () -> {
                                player.getDialogueManager().finish();
                                player.sendInputInt("How many would you like to exchange?", value -> exchange(player, item, Math.min(value, realCount)));
                            }));
                        optionsList.add(new DialogueOption("Cancel"));
                        options(TITLE, optionsList.toArray(new DialogueOption[0]));
                    }
                });
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("You have no bones which I could unnote with you.");
                }
            });
        });
    }

    private static void exchange(final Player player, final Item item, int count) {
        if (count <= 0)
            return;
        final ContainerResult coinResult = player.getInventory().deleteItem(new Item(995, count * 50));
        count = coinResult.getSucceededAmount() / 50;
        final ContainerResult result = player.getInventory().deleteItem(item.getId(), count);
        count = result.getSucceededAmount();
        player.getInventory().addItem(new Item(item.getDefinitions().getUnnotedOrDefault(), count)).onFailure(remaining -> World.spawnFloorItem(remaining, player));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.ELDER_CHAOS_DRUID_7995 };
    }

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        final Container inventory = player.getInventory().getContainer();
        final Bones bone = Bones.getBone(item.getDefinitions().getNotedId());
        if (bone == null)
            throw new IllegalStateException();
        final int count = inventory.getAmountOf(item.getId());
        final int space = inventory.getFreeSlotsSize();
        final int coins = inventory.getAmountOf(995);
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                if (coins < 50) {
                    npc("You don't have enough coins to exchange bones.");
                    return;
                }
                final ArrayList<Dialogue.DialogueOption> optionsList = new ArrayList<DialogueOption>(5);
                optionsList.add(new DialogueOption("Exchange '" + item.getName() + "': 50 coins", () -> exchange(player, item, 1)));
                // Block of shit code to ensure we always calculate max possible, considering the fact
                // the ingredients are removed first, thus giving us more free space in scenarios.
                int costOfAll = Math.min(space, count) * 50;
                if (space == (count - 2) && coins == costOfAll + 100) {
                    costOfAll += 100;
                } else {
                    if (space == count - 1)
                        costOfAll += 50;
                    else if (coins == costOfAll + 50) {
                        costOfAll += 50;
                    }
                }
                if (costOfAll > coins) {
                    costOfAll = (int) Math.floor(coins / 50.0F) * 50;
                }
                if (costOfAll <= 0) {
                    npc("You don't have enough space to exchange the bones.");
                    return;
                }
                final int realCount = costOfAll / 50;
                if (realCount >= 5) {
                    optionsList.add(new DialogueOption("Exchange 5: 250 coins", () -> exchange(player, item, 5)));
                }
                if (realCount > 1)
                    optionsList.add(new DialogueOption("Exchange All: " + costOfAll + " coins", () -> exchange(player, item, realCount)));
                if (realCount > 1)
                    optionsList.add(new DialogueOption("Exchange X", () -> {
                        player.getDialogueManager().finish();
                        player.sendInputInt("How many would you like to exchange?", value -> exchange(player, item, Math.min(value, realCount)));
                    }));
                optionsList.add(new DialogueOption("Cancel"));
                options(TITLE, optionsList.toArray(new DialogueOption[0]));
            }
        });
    }

    @Override
    public Object[] getItems() {
        final ObjectArrayList<Integer> list = new ObjectArrayList<Integer>();
        for (final Bones bone : Bones.VALUES) {
            for (final Item item : bone.getItems()) {
                final int noted = item.getDefinitions().getNotedOrDefault();
                if (noted != item.getId()) {
                    list.add(noted);
                }
            }
        }
        return list.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { NpcId.ELDER_CHAOS_DRUID_7995 };
    }
}
