package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.skills.farming.BasketData;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Action;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class FillBasket extends Action {
    private final int amount;
    private final BasketData data;
    private final Item clicked;
    private int cycle;
    private String producePlural;

    @Override
    public boolean start() {
        if (data == null) return false;
        this.producePlural = data.getFull().getName().replace("(5)", "").toLowerCase();
        return true;
    }

    @Override
    public boolean process() {
        if (!player.getInventory().containsItem(data.getProduce())) {
            player.sendMessage("You don't have any " + this.producePlural + " left!");
            return false;
        }
        if (!player.getInventory().containsItem(clicked)) {
            player.sendMessage("You have run out of baskets to fill!");
            return false;
        }
        return cycle < amount;
    }

    public FillBasket(int amount, BasketData data, Item clicked) {
        this.amount = amount;
        this.data = data;
        this.clicked = clicked;
    }

    @Override
    public int processWithDelay() {
        final int amount = player.getInventory().getAmountOf(data.getProduce().getId());
        final int space = clicked.getId() == 5376 ? 5 : (8 - (clicked.getId() - data.getBasket().getId())) / 2;
        final int deleteAmount = amount > space ? space : amount;
        player.getInventory().deleteItem(clicked);
        player.getInventory().deleteItem(data.getProduce().getId(), deleteAmount);
        if (clicked.getId() == 5376) player.getInventory().addItem((data.getBasket().getId() + ((deleteAmount - 1) * 2)), 1);
         else player.getInventory().addItem((clicked.getId() + ((deleteAmount) * 2)), 1);
        cycle++;
        return 3;
    }
}
