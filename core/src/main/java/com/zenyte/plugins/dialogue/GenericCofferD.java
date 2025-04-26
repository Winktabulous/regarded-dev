package com.zenyte.plugins.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class GenericCofferD extends Dialogue {
    private final String attribute;
    private final int varbit;
    private static final Item CASH_SMALL = new Item(1002, 1);
    private static final Item CASH_MED = new Item(1003, 1);
    private static final Item CASH_BIG = new Item(1004, 1);

    public GenericCofferD(final Player player, final String attribute, final int varbit) {
        super(player);
        this.attribute = attribute;
        this.varbit = varbit;
    }

    @Override
    public void buildDialogue() {
        options("Select an Option", buildOptions()).onOptionOne(() -> {
            finish();
            if (player.getNumericAttribute(attribute).intValue() != 0) {
                handleWithdraw();
            } else {
                handleDeposit();
            }
        }).onOptionTwo(() -> {
            finish();
            if (player.getNumericAttribute(attribute).intValue() != 0 && player.getInventory().getAmountOf(995) != 0) handleDeposit();
        });
    }

    private void handleWithdraw() {
        if (!player.getInventory().hasFreeSlots()) {
            player.getDialogueManager().start(new PlainChat(player, "You don't have any space in your inventory!"));
            return;
        }
        final int goldTilFull = Integer.MAX_VALUE - player.getInventory().getAmountOf(995);
        final String amount = player.getNumericAttribute(attribute).intValue() > goldTilFull ? TextUtils.formatCurrency(goldTilFull) : TextUtils.formatCurrency(player.getNumericAttribute(attribute).intValue());
        player.sendInputInt("How much do you want to withdraw? (" + amount + ")", withdrawAmt -> {
            if (withdrawAmt > player.getNumericAttribute(attribute).intValue()) withdrawAmt = player.getNumericAttribute(attribute).intValue();
             else if (withdrawAmt > goldTilFull) {
                player.sendMessage("You can't fit that many coins in your inventory!");
                return;
            }
            if (player.getNumericAttribute(attribute).intValue() >= withdrawAmt) {
                player.getAttributes().put(attribute, player.getNumericAttribute(attribute).intValue() - withdrawAmt);
                player.getInventory().addItem(995, withdrawAmt);
                if (player.getNumericAttribute(attribute).intValue() == 0) if (varbit != 0) player.getVarManager().sendBit(varbit, 0);
                final int cofferNew = player.getNumericAttribute(attribute).intValue();
                final Item goldItem = cofferNew < 10000 ? (cofferNew < 1000 ? CASH_SMALL : CASH_MED) : CASH_BIG;
                player.getDialogueManager().start(new ItemChat(player, goldItem, "The coffer now contains " + TextUtils.formatCurrency(cofferNew) + " coins."));
            }
        });
    }

    private void handleDeposit() {
        final int goldTilFull = Integer.MAX_VALUE - player.getNumericAttribute(attribute).intValue();
        final String amount = player.getInventory().getAmountOf(995) > goldTilFull ? TextUtils.formatCurrency(goldTilFull) : TextUtils.formatCurrency(player.getInventory().getAmountOf(995));
        player.sendInputInt("How much do you want to deposit? (" + amount + ")", depositAmt -> {
            if (depositAmt > player.getInventory().getAmountOf(995)) depositAmt = player.getInventory().getAmountOf(995);
             else if (depositAmt > goldTilFull) {
                player.sendMessage("You can't fit that many coins in the coffer!");
                return;
            }
            if (player.getInventory().containsItem(995, depositAmt)) {
                player.getInventory().deleteItem(995, depositAmt);
                player.getAttributes().put(attribute, player.getNumericAttribute(attribute).intValue() + depositAmt);
                if (depositAmt > 1) if (varbit != 0) player.getVarManager().sendBit(varbit, 1);
                final int cofferNew = player.getNumericAttribute(attribute).intValue();
                final Item goldItem = cofferNew < 10000 ? (cofferNew < 1000 ? CASH_SMALL : CASH_MED) : CASH_BIG;
                player.getDialogueManager().start(new ItemChat(player, goldItem, "The coffer now contains " + TextUtils.formatCurrency(cofferNew) + " coins."));
            }
        });
    }

    // todo potentially store 3 static arrays/lists instead of creating it on-run
    private String[] buildOptions() {
        final List<String> options = new ArrayList<>();
        if (player.getNumericAttribute(attribute).intValue() != 0) options.add("Withdraw");
        if (player.getInventory().getAmountOf(995) != 0) options.add("Deposit");
        options.add("Cancel");
        return options.toArray(new String[options.size()]);
    }
}
