package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;

public class ArcaneKnowledge extends Boon {
    @Override
    public String name() {
        return "Arcane Knowledge";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_ArcaneKnowledge;
    }

    @Override
    public String description() {
        return "Grants an additional rune slot in your divine rune pouch (max 5)";
    }

    @Override
    public int item() {
        return 27281;
    }

    public static void attemptAddInventory(Player player, int option, int slotId, RunePouch pouch, Item item) {
        int amount = option == 1 ? 1 : option == 2 ? 5 : player.getInventory().getItem(slotId).getAmount();
        final int inPouch = pouch.getAmountOf(item.getId());
        if ((amount + (long) inPouch) >= 16000) {
            amount = 16000 - inPouch;
        }
        if (amount <= 0) {
            player.sendMessage("You can't put that many runes in your pouch.");
            return;
        }
        pouch.getContainer().deposit(player, player.getInventory().getContainer(), slotId, amount);
        pouch.getContainer().refresh(player);
        player.getInventory().refreshAll();
    }
}
