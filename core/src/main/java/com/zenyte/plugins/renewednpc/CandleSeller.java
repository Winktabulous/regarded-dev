package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 28/11/2018 12:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CandleSeller extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {

            if (TreasureTrail.talk(player, npc))
                return;

            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Do you want a lit candle for 1000 gold?");
                    options(TITLE, new DialogueOption("Yes please.", key(5)), new DialogueOption("One thousand gold?!", key(15)), new DialogueOption("No thanks, I'd rather curse the darkness.", key(25)));
                    player(5, "Yes please.").executeAction(() -> buy(player));
                    player(300, "Thanks, bye.");
                    player(400, "What's do dangerous about a naked flame?");
                    npc("Heh heh... You'll find out.");
                    player(15, "One thousand gold?!");
                    npc("Look, you're not going to be able to survive down that hole without a light source.");
                    npc("So you could go off to the candle shop to buy one more cheaply. You could even make your own" + " lantern, which is a lot better.");
                    npc("But I bet you want to find out what's down there right now, don't you? And you can pay me " + "1000 gold for the privilege!");
                    options(TITLE, new DialogueOption("All right, you win, II'll buy a candle.", key(500)), new DialogueOption("No way.", key(150)), new DialogueOption("How do you make lanterns?", key(200)));
                    player(25, "No thanks, I'd rather curse the darkness.");
                    player(150, "No way.");
                    player(200, "How do you make lanterns?");
                    npc("Out of glass. The more advanced lanterns have a metal component as well.");
                    npc("Firstly you can make a simple candle lantern out of glass. It's just like a candle, but the " + "flame isn't exposed so it's safer.");
                    npc("Then you can make an oil lamp, which is brighter but has an exposed flame. But if you make an" + " iron frame for it you can turn it into an oil lantern.");
                    npc("Finally there's the bullseye lantern. You'll need to make a frame out of steel and add a " + "glass lens.");
                    npc("Once you've made your lamp or lantern, you'll need to make lamp oil for it. The chemist near " + "Rimmington has a machine for that.");
                    npc("For any light source, you'll need a tinderbox to light it. Keep your tinderbox handy in case " + "it goes out!");
                    npc("But if all that's too complicated, you can buy a candle right here for 1000 gold!");
                    options(TITLE, new DialogueOption("All right, you win, I'll buy a candle.", key(500)), new DialogueOption("No thanks, I'd rather curse the darkness.", key(25)));
                    player(500, "All right, you win, I'll buy a candle.").executeAction(() -> buy(player));
                }

                private void buy(final Player player) {
                    // Set the current pointer to page 6, we add new pages there w/ current info.
                    forceKey(6);
                    if (player.getInventory().containsItem(new Item(995, 1000))) {
                        npc("Here you go then.");
                        player.getInventory().deleteItem(995, 1000);
                        player.getInventory().addItem(new Item(33, 1)).onFailure(item -> World.spawnFloorItem(item, player));
                        npc("I should warn you, though, it can be dangerous to take a naked flame down there. You'd be " + "better off making a lantern.");
                        options(TITLE, new DialogueOption("What's so dangerous about a naked flame?", key(400)), new DialogueOption("How do you make lanterns?", key(200)), new DialogueOption("Thanks, bye.", key(300)));
                    } else {
                        player("But I don't have that kind of money on me.");
                        npc("Well then, no candle for you!");
                    }
                    // Reset pointer to 6 so it shows the next page as this.
                    forceKey(6);
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.CANDLE_MAKER };
    }
}
