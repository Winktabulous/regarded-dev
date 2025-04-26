package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.sailing.Sailing;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 26/11/2018 18:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CustomsOfficer extends NPCPlugin {

    private static final Item COST = new Item(995, 30);

    private static final Location MUSA_POINT_NPC_LOCATION = new Location(2954, 3147, 0);

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final boolean musapoint = npc.getLocation().withinDistance(MUSA_POINT_NPC_LOCATION, 15);
            final String departure = musapoint ? "Karamja" : "Brimhaven";
            final String destination = musapoint ? "Port Sarim" : "Ardougne";
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    player("Can I journey on this ship?");
                    npc("You need to be searched before you can board.");
                    npc("Well you've got some odd stuff, but it's all legal. Now<br>you need to pay a boarding charge" + " of " + COST.getAmount() + " coins.");
                    options(TITLE, "Ok.", "Oh, I'll not bother then.").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(15));
                    player(10, "Ok.").executeAction(() -> {
                        if (player.getInventory().containsItem(COST)) {
                            player.sendMessage("You pay " + COST.getAmount() + " coins and board the ship.");
                            Sailing.sail(player, departure, destination);
                        } else {
                            setKey(20);
                        }
                    });
                    player(15, "Oh, I'll not bother then.");
                    npc(20, "You don't have enough gold on you to go on this trip.");
                }
            });
        });
        bind("Pay-Fare", (player, npc) -> {
            final boolean musapoint = npc.getLocation().withinDistance(MUSA_POINT_NPC_LOCATION, 15);
            final String departure = musapoint ? "Karamja" : "Brimhaven";
            final String destination = musapoint ? "Port Sarim" : "Ardougne";
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("You need to be searched before you can board.");
                    npc("Well you've got some odd stuff, but it's all legal. Now<br>you need to pay a boarding charge" + " of " + COST.getAmount() + " coins.");
                    options(TITLE, "Ok.", "Oh, I'll not bother then.").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(15));
                    player(10, "Ok.").executeAction(() -> {
                        if (player.getInventory().containsItem(COST)) {
                            player.sendMessage("You pay " + COST.getAmount() + " coins and board the ship.");
                            Sailing.sail(player, departure, destination);
                        } else {
                            setKey(20);
                        }
                    });
                    player(15, "Oh, I'll not bother then.");
                    npc(20, "You don't have enough gold on you to go on this trip.");
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.CUSTOMS_OFFICER };
    }
}
