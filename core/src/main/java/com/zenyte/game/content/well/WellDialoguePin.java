package com.zenyte.game.content.well;

import com.zenyte.game.content.DonatorPin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class WellDialoguePin extends Dialogue {

    private final Player player;
    private final DonatorPin pin;

    public WellDialoguePin(Player player, DonatorPin pin) {
        super(player);
        this.player = player;
        this.pin = pin;
    }

    @Override
    public void buildDialogue() {
        StringBuilder options = new StringBuilder();
        for (WellPerk value : WellPerk.VALUES) {
            WellSession wellSession = WellHandler.get().get(value);
            boolean actived = wellSession.isActived();
            StringBuilder sb = new StringBuilder();
            if(actived)
                sb.append("<str=ff0000>");
            sb.append(value.getMssg());
            sb.append("(").append(Utils.formatNumWDot(wellSession.getCoins())).append("/").append(Utils.formatNumWDot(value.getAmount())).append(")");
            options.append(sb).append(",");
        }
        options("Choose a perk", options.toString().split(","))
                .onOptionOne(() -> {
                    finish();
                    donate(WellPerk.VALUES[0]);
                })
                .onOptionTwo(() -> {
                    finish();
                    donate(WellPerk.VALUES[1]);
                })
                .onOptionThree(() -> {
                    finish();
                    donate(WellPerk.VALUES[2]);
                })
                .onOptionFour(() -> {
                    finish();
                    donate(WellPerk.VALUES[3]);
                })
                .onOptionFive(() -> {
                    finish();

                    donate(WellPerk.VALUES[4]);
                });
    }

    public void donate(WellPerk wellPerk) {
        WellSession wellSession = WellHandler.get().get(wellPerk);
        if(wellSession.isActived()) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain("The well is already full for this perk.");
                }
            });
            return;
        }

        int num = pin.getValue();

            if(num + wellSession.getCoins() > wellPerk.getAmount()) {
                int overflow = (int) ((num + wellSession.getCoins()) - wellPerk.getAmount());
                num = num - overflow;
            }

            int finalNum = num;
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    player("Are you sure you want to deposit the pin for "+Utils.formatNumWDot(pin.getValue()));
                    options("Deposit pin for "+Utils.formatNumWDot(pin.getValue())+"?", "Yes, deposit pin", "No")
                            .onOptionOne(() -> {
                                player.getInventory().deleteItem(pin.getItemId(), 1);
                                WellHandler.get().contribute(player, finalNum, wellPerk);
                                setKey(5);
                            })
                            .onOptionTwo(() -> setKey(10));
                    plain(5, "You have deposited the pin.");
                    plain(10, "Let me know if you change your mind.");
                }
            });
    }
}
