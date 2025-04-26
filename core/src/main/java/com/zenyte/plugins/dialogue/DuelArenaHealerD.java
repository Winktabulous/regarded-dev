/**
 */
package com.zenyte.plugins.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.renewednpc.Healer;

/**
 * @author Noele | Jun 1, 2018 : 2:55:06 AM
 * @author https://noeles.life || noele@zenyte.com
 */
public class DuelArenaHealerD extends Dialogue {

    private static final Animation HEAL = new Animation(881);

    private static final Item SKILLCAPE = new Item(9768);

    private static final Item HOOD = new Item(9770);

    private final boolean heal;

    public DuelArenaHealerD(final Player player, final NPC npc, final boolean heal) {
        super(player, npc);
        this.heal = heal;
    }

    @Override
    public void buildDialogue() {
        if (heal) {
            if (player.getHitpoints() == player.getMaxHitpoints())
                npc("You look healthy to me!");
            else {
                npc.setAnimation(HEAL);
                player.setHitpoints(player.getMaxHitpoints());
            }
        } else {
            player(0, "Hi!");
            if (npc.getId() != NpcId.JARAAH) {
                // nurses
                npc(1, "Hi. How can I help?");
                options(2, TITLE, "Can you heal me?", "Do you see a lot of injured fighters?", "Do you come here often?", (npc.getId() == 3343 && player.getMaxHitpoints() >= 99 ? "Can I buy a Skillcape of Hitpoints from you?" : "")).onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10)).onOptionThree(() -> setKey(15)).onOptionFour(() -> setKey(19));
                player(5, "Can you heal me?").executeAction(() -> Healer.heal(player, npc, true));
                // second option
                player(10, "Do you see a lot of injured fighters?");
                npc(11, "Yes I do. Thankfully we can cope with almost anything. Jaraah really is a wonderful surgeon, his methods are a little unorthodox but he gets the job done.");
                npc(12, "I shouldn't tell you this but his nickname is 'The Butcher'.");
                player(13, "That's reassuring.");
                // third option
                player(15, "Do you come here often?");
                npc(16, "I work here, so yes!");
                npc(17, "You're silly!");
                // skillcape
                player(19, "Can I buy a Skillcape of Hitpoints from you?");
                options(20, TITLE, "Skill cape", "Hood").onOptionOne(() -> setKey(25)).onOptionTwo(() -> setKey(40));
                player(25, "Can I buy a Skillcape of Hitpoints from you?");
                npc(26, "Why certainly my friend. However, owning such an item makes you part of an elite group and wearing it will double the speed of your hitpoints replenishing.");
                npc(27, "Such a privilege will cost you 99,000 coins.");
                options(28, TITLE, "Sure, that's not too expensive for such a magnificent cape.", "Sorry, that's much too pricey.").onOptionOne(() -> setKey(30)).onOptionTwo(() -> setKey(34));
                player(30, "Sure, that's not too expensive for such a magnificent cape.");
                if (player.getInventory().containsItem(995, 99000)) {
                    if (player.getInventory().hasFreeSlots()) {
                        item(31, SKILLCAPE, "Tafani hands you a Skillcape of Hitpoints.").executeAction(() -> {
                            player.getInventory().deleteItem(995, 99000);
                            player.getInventory().addItem(SKILLCAPE);
                        });
                    } else
                        setKey(50);
                } else {
                    player(31, "But unfortunately, I don't have enough money with me.");
                    npc(32, "Well come back and see me when you do.");
                }
                player(34, "Sorry, that's much too pricey.");
                npc(35, "Well I'm sorry you feel that way. Still.. let me know if you change your mind.");
                player(40, "May I have another hood for my cape, please?");
                npc(41, "Most certainly, and free of charge!");
                if (player.getInventory().hasFreeSlots())
                    item(42, HOOD, "Tafani hands you another hood for your skillcape.").executeAction(() -> player.getInventory().addItem(HOOD));
                else
                    setKey(50);
                plain(50, "You don't have any room in your inventory!");
            } else {
                // the butcher
                npc(1, "What? Can't you see I'm busy?!");
                options(2, TITLE, "Can you heal me?", "You must see some gruesome things?", "Why do they call you 'The Butcher'?").onOptionOne(() -> Healer.heal(player, npc, true)).onOptionTwo(() -> setKey(10)).onOptionThree(() -> setKey(15));
                player(10, "You must see some gruesome things?");
                npc(11, "It's a gruesome business and with the tools they give me it gets more gruesome before it gets better!");
                player(15, "Why do they call you 'The Butcher'?");
                npc(16, "'The Butcher'?");
                npc(17, "Ha!");
                npc(18, "Would you like me to demonstrate?");
                player(19, "Er...I'll give it a miss, thanks.");
            }
        }
        npc.unlock();
    }
}
