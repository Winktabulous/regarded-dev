package com.zenyte.plugins.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.GameSetting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 1-3-2019 | 15:30
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class NoteD extends Dialogue {
    private final Item item;

    public NoteD(final Player player, final Item item) {
        super(player);
        this.item = item;
    }

    @Override
    public void buildDialogue() {
        final String title = item.getAmount() == 1 ? "Note this item?" : "Note these items?";
        if (player.getNumericAttribute(GameSetting.CONFIRMATION_WHEN_NOTING_OR_UNNOTING.toString()).intValue() == 1) {
            options(title, "Yes, note them.", "No.").onOptionOne(this::note);
        } else {
            note();
        }
    }

    private void note() {
        int amount = player.getInventory().getAmountOf(item.getId());
        final int notedAmount = player.getInventory().getAmountOf(item.getDefinitions().getNotedId());
        if (amount + notedAmount < 0) {
            amount = Integer.MAX_VALUE - notedAmount;
        }
        player.getDialogueManager().start(new PlainChat(player, amount == 0 ? "Nothing was noted since you have too much of this item already. Get rid of some and try again." : "The bank exchanges your item(s) for banknote(s)."));
        final int succeeded = player.getInventory().deleteItem(item.getId(), amount).getSucceededAmount();
        player.getInventory().addItem(new Item(item.getDefinitions().getNotedId(), succeeded));
    }
}
