package com.zenyte.game.content.well;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class WellDialogue extends Dialogue {

    private Player player;

    public WellDialogue(Player player) {
        super(player);
        this.player = player;
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

        player.sendInputInt("How many coins would you like to deposit?", value -> {
            int num = Math.min(value, player.getInventory().getAmountOf(995));
            if (num == 0) {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        plain("You don't have that much gp.");
                    }
                });
                return;
            }
            if(num + wellSession.getCoins() > wellPerk.getAmount()) {
                int overflow = (int) ((num + wellSession.getCoins()) - wellPerk.getAmount());
                num = num - overflow;
            }

            int finalNum = num;
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    player("Are you sure you want to deposit " + Utils.formatNumWDot(finalNum) + " amount of coins?");
                    options("Are you sure you want to deposit " + Utils.formatNumWDot(finalNum) + " amount of coins?", "Yes, deposit " + Utils.formatNumWDot(finalNum) + " coins.", "No")
                            .onOptionOne(() -> {
                                player.getInventory().deleteItem(995, finalNum);
                                WellHandler.get().contribute(player, finalNum, wellPerk);
                                setKey(5);
                            })
                            .onOptionTwo(() -> setKey(10));
                    plain(5, "You have deposited " + Utils.formatNumWDot(finalNum) + ".");
                    plain(10, "Let me know if you change your mind.");
                }
            });
        });
    }
}
