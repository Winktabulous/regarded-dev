package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.ItemChat;

public class DaeyaktEssenceNpcAction implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        if(item.getId() == ItemId.DAEYALT_SHARD) {
            int amt = player.getInventory().getAmountOf(item.getId());
            player.getInventory().deleteItem(item.getId(), amt);
            Item essence = new Item(ItemId.DAEYALT_ESSENCE, amt);
            player.getBank().add(essence);
            player.getDialogueManager().start(new ItemChat(player, essence, "Noranna converts "+ Utils.formatNumberWithCommas(amt)+" of your daeyalt shards into daeyalt essence. She then sends the essence to your bank."));

        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] {ItemId.DAEYALT_SHARD};
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {NpcId.NORANNA_TYTANIN, 9822};
    }
}
