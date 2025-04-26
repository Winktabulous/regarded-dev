package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.shop.Shop;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.utilities.StringFormatUtil;

/**
 * @author Tommeh | 21/06/2019 | 12:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class Oziach extends NPCPlugin {

    private static final Item PRICE = new Item(995, 1000000);

    private static final Item VISAGE = new Item(11286);

    private static final Item ANTI_DRAGON_SHIELD = new Item(1540);

    private static final Item DRAGONFIRE_SHIELD = new Item(11283);

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Greetings, " + player.getName() + ". What can I do for you?");
                    options(TITLE, "Could you make a dragonfire shield?", "Can I buy some equipment?").onOptionOne(() -> setKey(5)).onOptionTwo(() -> Shop.get("Oziach's Armour", false, player).open(player));
                    npc(5, "Certainly, this will however cost you " + StringFormatUtil.format(PRICE.getAmount()) + " gold.");
                    if (!player.getInventory().containsItem(VISAGE)) {
                        npc("It seems that you don't have a draconic visage right now. Come back when you do.");
                    } else if (!player.getInventory().containsItem(ANTI_DRAGON_SHIELD)) {
                        npc("It seems that you don't have an anti-dragonbreath shield right now. Come back when you do.");
                    } else if (!player.getInventory().containsItem(PRICE)) {
                        npc("It seems that you don't have enough gold right now. Come back when you do.");
                    } else {
                        options("Are you sure you want to make a dragonfire shield?", "Yes.", "No,").onOptionOne(() -> {
                            player.getInventory().deleteItemsIfContains(new Item[] { PRICE, VISAGE, ANTI_DRAGON_SHIELD }, () -> {
                                player.getInventory().addOrDrop(DRAGONFIRE_SHIELD);
                            });
                            setKey(10);
                        });
                        item(10, DRAGONFIRE_SHIELD, "The draconic visage has succesfully been crafted onto the anti-dragonshield and resulted in a dragonfire shield!");
                    }
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.OZIACH };
    }
}
