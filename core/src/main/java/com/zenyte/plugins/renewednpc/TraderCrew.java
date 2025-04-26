package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25/11/2018 16:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TraderCrew extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("Can I help you?");
                    options(TITLE, new DialogueOption("Yes, who are you?", () -> setKey(100000)), new DialogueOption("Yes, I'd like to charter a ship.", () -> setKey(120000)), new DialogueOption("No thanks.", () -> setKey(130000)));
                    player(100000, "Yes, who are you?");
                    if (npc.getId() == 1328) {
                        npc("Why, I'm Trader Stan, owner and operator of the largest fleet of trading ships and chartered vessels to ever sail the seas!");
                    } else {
                        npc("I'm one of Trader Stan's crew; we are all part of the largest fleet of trading and sailing vessels to ever sail the seven seas.");
                    }
                    npc("If you want to get to a port in a hurry, then you can charter one of my ships to take you there - if the price is right...");
                    player("So, where exactly can I go with your ships?");
                    npc("We run ships from Port Phasmatys over to Port Tyras, stopping at Port Sarim, Catherby, Brimhaven, Musa Point, the Shipyard and Port Khazard.");
                    npc("We might dock at Mos Le'Harmless once in a while, as well, if you catch my meaning...");
                    player("Wow, that's a lot of ports. I take it you have some exotic stuff to trade?");
                    if (npc.getId() == 1328) {
                        npc("We certainly do! I an my crewmen have access to items bought and sold from around the world. Would you like to take a look? Or would you like to charter a ship?");
                    } else {
                        npc("We certainly do! We have access to items bought and sold from around the world. Would you like to take a look? Or would you like to charter a ship?");
                    }
                    options(TITLE, new DialogueOption("Yes, let's see what you're trading.", () -> setKey(110000)), new DialogueOption("Yes, I would like to charter a ship.", () -> setKey(120000)), new DialogueOption("No thanks.", () -> setKey(130000)));
                    player(110000, "Yes, let's see what you're trading.").executeAction(() -> player.openShop("Trader Stan's Trading Post"));


                    player(120000, "Yes, I would like to charter a ship.");
                    npc("Certainly sir. Where would you like to go?").executeAction(() -> GameInterface.SHIP_DESTINATION_CHART.open(player));

                    player(130000, "No thanks.");

                }
            });
        });
        bind("Charter", (player, npc) -> GameInterface.SHIP_DESTINATION_CHART.open(player));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                1328, 1329, 1330, 1331, 1332, 1333, 1334
        };
    }
}
