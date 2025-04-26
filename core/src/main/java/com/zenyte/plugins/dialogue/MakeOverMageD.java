package com.zenyte.plugins.dialogue;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 17 mrt. 2018 : 00:18:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MakeOverMageD extends Dialogue {
    public static final Item PRICE = new Item(995, 500);
    private final boolean dialogue;

    public MakeOverMageD(final Player player, final NPC npc, final boolean dialogue) {
        super(player, npc);
        this.dialogue = dialogue;
    }

    @Override
    public void buildDialogue() {
        final boolean enoughCoins = player.getInventory().containsItem(PRICE);
        //if (dialogue) {
        //if (npc.getId() == 1307) {
        npc("Hello there! I am known as the make-over mage! I have<br>spent many years researching magics that can change<br>your physical appearance!").executeAction(() -> GameInterface.CHARACTER_DESIGN.open(player));
        /*} else {
                npc("Hello there! I am known as the make-over mage! I have<br>spent many years researching magics that can change<br>your physical appearance!");
                npc("I can alter your physical form for a small fee of only<br>" + PRICE.getAmount() + " coins! Would you like me to perform my magics upon you?");
                options(TITLE, "Tell me more about this 'make-over'.", "Sure, do it.", "No thanks.").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(15)).onOptionThree(() -> setKey(20));
                player(5, "Tell me more about this 'make-over'.");
                npc("Why, of course! Basically, and I will try and explain<br>this so that you will understand it correctly,");
                npc("I use my secret magical technique to melt your body down into a puddle of its elements.");
                npc("When I have broken down all trace of your body, I<br> then rebuild it into the form I am thinking of!");
                npc("Or, you know, somewhere vaguely close enough<br>anyway.");
                player("Uh... that doesn't sound particularly safe to me...");
                npc("It's as safe as houses! Why, I have only had thirty-six<br>major accidents this month!");
                npc("So what do you say? Feel like a change?<br>It's only " + PRICE.getAmount() + " coins.");
                options(TITLE, "Sure, do it.", "No thanks.").onOptionOne(() -> setKey(15)).onOptionTwo(() -> setKey(20));
                player(15, "Sure, do it.");
                if (enoughCoins) {
                    npc("You of course agree that if by some accident you are<br>turned into a frog you have no rights for compensation<br>or refund.").executeAction(() -> GameInterface.MAKEOVER.open(player));
                } else {
                    npc("Great. Come back with " + PRICE.getAmount() + " coins, and I'll get started.");
                }
                player(20, "No thanks. I'm happy as Saradomin made me.");
                npc("Ehhh... suit yourself.");
            }*/
        /*} else {
            if (!enoughCoins) {
                npc("A makeover costs " + PRICE.getAmount() + " coins.");
            } else {
                options(TITLE, "Pay " + PRICE.getAmount() + " coins for a makeover.", "Cancel.")
                        .onOptionOne(() -> {
                            GameInterface.MAKEOVER.open(player);
                            finish();
                        })
                        .onOptionTwo(() -> {
                            GameInterface.MAKEOVER.open(player);
                            finish();
                        });
            }
        }*/
    }
}
