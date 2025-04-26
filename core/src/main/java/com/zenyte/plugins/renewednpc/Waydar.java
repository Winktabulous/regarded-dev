package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.flowerpoker.GambleBan;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.shop.Shop;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.utils.TimeUnit;

import java.time.Duration;

/**
 * @author Kris | 26/11/2018 19:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Waydar extends NPCPlugin {

    private static final Item COST = new Item(995, 30);


    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                player("Hello, there.");
                npc("I'm retired, 2 years sober from this gambling addiction");
                player("I didn't ask?");
                npc("Hmph, how about some mithril seeds?");
                npc("").setOnDisplay(() -> Shop.get("Gambling Store", false, player).open(player));
            }
        }));
        bind("Trade", (player, npc) -> Shop.get("Gambling Store", false, player).open(player));
        bind("Gamble-Ban", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if(GambleBan.isGambleBanValid(player)) {
                    npc("Your gamble ban will expire in:<br>"+ Utils.formatDuration(Duration.ofMillis(player.getNumericAttribute("GAMBLE_BAN").longValue() - System.currentTimeMillis()))+".");
                } else {
                    npc("Would you like to be banned from gambling? I can ban you for up to 7 days.");
                    options("Would you like to recieve a gamble ban?", "Yes", "No").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10));
                    npc(10, "Okay, see you later.");
                    npc(5, "How many days do you want to be gamble banned? This cannot be undone not even by a staff member.").executeAction(() -> {
                        finish();
                        player.sendInputInt("How many days do you want to be gamble banned?", value -> {
                            final int num = Math.min(value, 7);
                            player.getDialogueManager().start(new Dialogue(player, npc) {
                                @Override
                                public void buildDialogue() {
                                    npc("Are you sure you want to be gamble banned for " + num + " days?<br>THIS CANNOT BE UNDONE");
                                    options("Gamble banned for " + num + " days?", "Yes, gamble ban me for " + num + " Days", "No")
                                            .onOptionOne(() -> {
                                                long duration = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(num);
                                                player.getAttributes().put("GAMBLE_BAN", duration);
                                                setKey(5);
                                            })
                                            .onOptionTwo(() -> setKey(10));
                                    npc(5, "You have been gamble banned for " + num + " days");
                                    npc(10, "Let me know if you change your mind.");
                                }
                            });
                        });
                    });
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[]{NpcId.WAYDAR};
    }
}
