package com.zenyte.game.content.partyroom;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 03/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PartyPetePlugin extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("Hi! I'm Party Pete.<br>Welcome to the Party Room!");

                    options(TITLE,
                            new DialogueOption("So what's this room for?", key(100)),
                            new DialogueOption("What's the big lever over there for?", key(200)),
                            new DialogueOption("What's the gold chest for?", key(300)),
                            new DialogueOption("Have you always been here in Falador?", key(400)),
                            new DialogueOption("I wanna party!", key(500)));

                    player(100, "So what's this room for?");
                    npc("This room is for partying the night away!");
                    player("How do you have a party in Gielinor?");
                    npc("Get a few mates round, get the beers in and have fun!");
                    npc("Some players organise parties so keep an eye open!");
                    player("Woop! Thanks Pete!");

                    player(200, "What's the big lever over there for?");
                    npc("Simple. With the lever, you can do some fun stuff.");
                    player("What kind of stuff?");
                    npc("A balloon drop costs 1000 gold. For this you get 200 balloons dropped across the whole of the party room. You can then have fun popping the balloons!" +
                            " If there are items in the Party Drop Chest they will be inside");
                    npc("the balloons! For 500 gold you can summon the Party Room Knights who will dance for your delight");
                    npc("Their singing isn't a delight though!");

                    player(300, "What's the gold chest for?");
                    npc("Any items that are in the chest will be dropped inside the balloons when you pull the lever!");
                    player("Cool! Sounds like a fun way to do a drop party!");
                    npc("Exactly!");
                    npc("A word of warning though. Any items that you put into the chest can't be taken out again and it costs 1000 gold pieces for each balloon drop.");

                    player(400, "Have you always been here in Falador?");
                    npc("We used to be in Seers' Village, far to the west, but we had to move - the seers were complaining about the noise level, and the knights of Camelot" +
                            " got it into their heads that the Party Room knights were making fun of");
                    npc("them.");
                    npc("We're doing well here, though. The people of Falador are happy we're here, and we've hardly ever had the White Knights telling us to keep" +
                            " the noise down.");
                    npc("We're going to turn Falador into the party capital of Gielinor!");

                    player(500, "I wanna party!");
                    npc("I've won the Dance Trophy at the Kandarin Ball three years in a trot!");
                    player("Show me your moves Pete!").executeAction(() -> {
                        if (npc instanceof PartyPeteNPC) {
                            ((PartyPeteNPC) npc).startDancing();
                        }
                    });
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                NpcId.PARTY_PETE
        };
    }
}
