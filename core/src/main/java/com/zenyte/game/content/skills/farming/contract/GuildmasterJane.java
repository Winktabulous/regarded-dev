package com.zenyte.game.content.skills.farming.contract;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Christopher
 * @since 4/8/2020
 */
public class GuildmasterJane extends NPCPlugin implements ItemOnNPCAction {

    public static final int JANE_VARBIT = 7947;

    static {
        VarManager.appendPersistentVarbit(JANE_VARBIT);
    }

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new GuildmasterJaneDialogue(player, npc, GuildmasterJaneDialogue.JaneDialogueType.FULL)));
        bind("Contract", ((player, npc) -> player.getDialogueManager().start(new GuildmasterJaneDialogue(player, npc, GuildmasterJaneDialogue.JaneDialogueType.CONTRACT_OPTION))));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 8628, NpcId.GUILDMASTER_JANE, NpcId.GUILDMASTER_JANE_8587 };
    }

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Ah, would you like to trade in some " + item.getName() + " for seed packs?");
                npc("How many would you like to trade in?").executeAction(() -> {
                    finish();
                    player.sendInputInt("How many " + item.getName() + " would you like to trade in?", value -> {
                        final int valueClamped = Math.min(value, item.getAmount());
                        player.getInventory().deleteItem(new Item(item.getId(), valueClamped));
                        for (int i = 0; i < valueClamped; i++) {
                            Item item = new Item(ItemId.SEED_PACK);
                            FarmingContract.applySeedPackAttributes(player, item, FarmingContractTier.FIVE);
                            player.getInventory().addItem(item);
                        }
                        player.getDialogueManager().start(new Dialogue(player) {
                            @Override
                            public void buildDialogue() {
                                doubleItem(item.getId(), ItemId.SEED_PACK, "You trade in your " + item.getName() + " for " + valueClamped + " seed pack" + (valueClamped > 1 ? "s" : "") + ".");
                            }
                        });
                    });
                });
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.SPIRIT_SEED, ItemId.SPIRIT_SAPLING, ItemId.SPIRIT_SEEDLING, ItemId.SPIRIT_SEEDLING_W };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 8628, NpcId.GUILDMASTER_JANE, NpcId.GUILDMASTER_JANE_8587 };
    }

}
