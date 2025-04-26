package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 26/11/2018 18:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Ghommal extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    int caIndx = -1;
                    for (int indx = 0; indx < CATierType.values.length; indx++) {
                        if (player.getCombatAchievements().isEligibleForRewards(CATierType.values[indx])) {
                            caIndx = indx;
                            break;
                        }
                    }

                    if (caIndx != -1) {
                        final CATierType tier = CATierType.values[caIndx];
                        player("I think I've completed a combat tier!");
                        npc("Oh, which tier have you done?");
                        player("I've completed all the " + tier.name().toLowerCase() + " Combat Achievement tasks!");
                        if (!player.getCombatAchievements().completedPreviousTiers(tier)) {
                            npc("I can see that. However, I expect you to finish the previous tiers first before you can claim your reward from me.");
                            player("Time to get moving then.");
                        } else {
                            npc("I can see that. Nice job. You'll be expecting some rewards?");
                            player("Yes please");
                            final Item hilt = new Item(ItemId.GHOMMALS_HILT_1 + caIndx * 2);
                            final Item oldHilt = caIndx > 0 ? new Item(ItemId.GHOMMALS_HILT_1 + (caIndx - 1) * 2) : null;
                            if (player.getInventory().getFreeSlots() >= (oldHilt != null && player.getInventory().containsItem(oldHilt) ? 1 : 2)
                                    && (tier.equals(CATierType.MASTER) || player.getInventory().hasSpaceFor(ItemId.GHOMMALS_LUCKY_PENNY))) {
                                npc("I will provide you with one of our ancient sword hilts. You can view additional rewards in your combat achievement log.").executeAction(() -> {
                                    if (oldHilt != null) {
                                        player.removeItem(oldHilt);
                                    }
                                    player.getInventory().addItem(hilt);
                                    player.getInventory().addItem(new Item(tier.getLampId()));
                                    if (tier.equals(CATierType.MASTER)) {
                                        player.getInventory().addItem(new Item(ItemId.GHOMMALS_LUCKY_PENNY, 1));
                                    }
                                    player.getCombatAchievements().setTierCompleted(tier);
                                });
                                player("Thank you so much!");
                            } else {
                                npc("Please come back to me when you have some available space to receive your rewards.");
                            }
                        }
                    } else {
                        int currentTier = -1;
                        for (int indx = 0; indx < CATierType.values.length; indx++) {
                            if (!player.getCombatAchievements().hasTierCompleted(CATierType.values[indx])) {
                                break;
                            }
                            currentTier = indx;
                        }
                        if (currentTier != -1 && (player.getAmountOf(ItemId.GHOMMALS_HILT_1 + currentTier * 2) < 1
                                || (currentTier >= CATierType.MASTER.ordinal() && player.getAmountOf(ItemId.GHOMMALS_LUCKY_PENNY) < 1))) {
                            player("I have lost my combat achievement rewards.");
                            if (player.getInventory().getFreeSlots() < (currentTier >= CATierType.MASTER.ordinal() ? 2 : 1)) {
                                npc("Well come back to me when you have some more space available.");
                            } else {
                                final Item hilt = new Item(ItemId.GHOMMALS_HILT_1 + (currentTier * 2));
                                final CATierType tier = CATierType.values[currentTier];
                                npc("No problem, I have provided you with your previous rewards again.").executeAction(() -> {
                                    player.getInventory().addItem(hilt);
                                    if (tier.ordinal() >= CATierType.MASTER.ordinal()) {
                                        player.getInventory().addItem(new Item(ItemId.GHOMMALS_LUCKY_PENNY));
                                    }
                                });
                            }
                        } else {
                            npc("Please come back to me after completing some combat achievements!");
                        }
                    }
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.GHOMMAL };
    }
}
