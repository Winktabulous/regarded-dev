package com.zenyte.game.content.minigame.wintertodt;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.plugins.item.LightSourceItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Corey
 * @since 20:01 - 29/08/2019
 */
public class Ignisia extends NPCPlugin {
    private static final Int2ObjectOpenHashMap<String> tradeableItems = new Int2ObjectOpenHashMap<>();

    static {
        tradeableItems.put(RewardCrate.PYROMANCER_BOOTS, "boots");
        tradeableItems.put(RewardCrate.PYROMANCER_GARB, "garb");
        tradeableItems.put(RewardCrate.PYROMANCER_HOOD, "hood");
        tradeableItems.put(RewardCrate.PYROMANCER_ROBE, "robe");
        tradeableItems.put(RewardCrate.WARM_GLOVES, "gloves");
        tradeableItems.put(LightSourceItem.LightSource.BRUMA_TORCH.getLitId(), "torch");
    }

    @Override
    public void handle() {
        bind("Trade-in", (player, npc) -> {
            if (!player.getInventory().containsAnyOf(tradeableItems.keySet().toIntArray())) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("Do you have something for me?");
                        player("Err... nope. Sorry.");
                    }
                });
                return;
            }
            Item itemToTrade = null;
            for (final Int2ObjectMap.Entry<String> entry : tradeableItems.int2ObjectEntrySet()) {
                if (player.getInventory().containsItem(entry.getIntKey(), 1)) {
                    itemToTrade = new Item(entry.getIntKey());
                }
            }
            Item finalItemToTrade = itemToTrade;
            if (finalItemToTrade == null) {
                throw new NullPointerException("Null item to trade-in to Ignisia for " + player.getName());
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("Do you have something for me?");
                    options("Select an Option", new DialogueOption("Trade in " + tradeableItems.get(finalItemToTrade.getId()) + ".", key(10)), new DialogueOption("I've changed my mind.", key(15)));
                    options(10, "Exchange your " + finalItemToTrade.getName() + "?", new DialogueOption("Yes.", key(25)), new DialogueOption("No.")).onOptionOne(() -> {
                        tradeItem(player, finalItemToTrade);
                        setKey(25);
                    });
                    player(15, "I've changed my mind.");
                    item(25, new Item(RewardCrate.SUPPLY_CRATE), "You exchanged 1 x " + finalItemToTrade.getName() + " for an extra supply crate.");
                    npc("Thank you for your help.");
                }
            });
        });
    }

    private void tradeItem(final Player player, final Item itemToTrade) {
        final Item crate = new Item(RewardCrate.SUPPLY_CRATE);
        int rolls = 1;
        if (player.getMemberRank().equalToOrGreaterThan(MemberRank.EXPANSION)) {
            rolls++;
        }
        if (player.getMemberRank().equalToOrGreaterThan(MemberRank.LEGENDARY)) {
            rolls++;
        }
        crate.setAttribute("rolls", rolls);
        player.getInventory().deleteItem(itemToTrade);
        player.getInventory().addItem(crate);
    }

    @Override
    public int[] getNPCs() {
        return new int[] {7374};
    }
}
