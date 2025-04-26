package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.farming.BasketData;
import com.zenyte.game.content.skills.farming.actions.FillBasket;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class FillBasketD extends SkillDialogue {

    private final Item clickedItem;

    public FillBasketD(final Player player, final Item click, final Item... items) {
        super(player, "What would you like to fill your basket with?", items);
        this.clickedItem = click;
    }

    @Override
    public void run(int slotId, int amount) {
        final BasketData data = BasketData.getBasket(items[slotId].getId());
        if(data != null) {
            player.getActionManager().setAction(new FillBasket(amount, data, clickedItem));
        }
    }
}
