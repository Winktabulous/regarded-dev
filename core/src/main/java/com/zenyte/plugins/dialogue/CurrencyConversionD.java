package com.zenyte.plugins.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 23-3-2019 | 17:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CurrencyConversionD extends Dialogue {
    private final Item item;
    private final int slotId;
    private Item conversion;
    private final Item toDelete;
    private final Item coins;
    private final Item tokens;

    public CurrencyConversionD(Player player, Item item, final int slotId) {
        super(player);
        this.item = item;
        this.slotId = slotId;
        conversion = item.getId() == 995 ? new Item(13204, item.getAmount() / 1000) : new Item(995, item.getAmount() > 2147483 ? 2147483000 : item.getAmount() * 1000);
        toDelete = conversion.getId() == 995 ? new Item(13204, conversion.getAmount() / 1000) : new Item(995, conversion.getAmount() > 2147483 ? 2147483000 : conversion.getAmount() * 1000);
        coins = conversion.getId() == 995 ? conversion : item;
        tokens = conversion.getId() == 13204 ? conversion : item;
    }

    @Override
    public void buildDialogue() {
        options("Exchange your " + item.getName().toLowerCase() + (item.getId() == 13204 ? "s" : "") + " for " + conversion.getName().toLowerCase() + (conversion.getId() == 13204 ? "s" : "") + "?", "Yes", "No").onOptionOne(() -> {
            if (player.getInventory().getItem(slotId) != item) {
                return;
            }
            if (player.getInventory().getAmountOf(conversion.getId()) + conversion.getAmount() < 0) {
                setKey(5);
                return;
            }
            if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(conversion.getId(), 1)) {
                setKey(10);
                return;
            }
            final int succeeded = player.getInventory().deleteItem(toDelete).getSucceededAmount();
            conversion = item.getId() == 995 ? new Item(13204, succeeded / 1000) : new Item(995, succeeded > 2147483 ? 2147483000 : succeeded * 1000);
            if (conversion.getAmount() == 0) {
                return;
            }
            player.getInventory().addItem(conversion);
            setKey(15);
        });
        item(5, conversion, "This conversion would result in an overflow, get rid of some " + conversion.getName().toLowerCase() + (conversion.getId() == 13204 ? "s" : "") + " and try again.");
        plain(10, "You don't have enough space to do this conversion, try again when you do.");
        doubleItem(15, coins, tokens, "The bank exchanges your " + item.getName().toLowerCase() + (item.getId() == 13204 ? "s" : "") + " for " + conversion.getName().toLowerCase() + (conversion.getId() == 13204 ? "s" : "") + ".");
    }
}
