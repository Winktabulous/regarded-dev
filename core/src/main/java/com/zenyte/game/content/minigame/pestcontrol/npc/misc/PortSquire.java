package com.zenyte.game.content.minigame.pestcontrol.npc.misc;

import com.zenyte.game.content.sailing.Sailing;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 27/11/2018 11:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PortSquire extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Hi, how can I help you?");
                options(TITLE, new DialogueOption("Who are you?", () -> setKey(10)), new DialogueOption("Where does this ship go?", () -> setKey(50000)), new DialogueOption("I'd like to go to your outpost.", () -> setKey(500000)), new DialogueOption("I'm fine, thanks.", () -> setKey(200000)));
                player(10, "Who are you?");
                {
                    npc("I'm a Squire for the Void Knights.");
                    player("The who?");
                    npc("The Void Knights, they are great warriors of balance who do Guthix's work here in Gielinor.");
                    options(TITLE, new DialogueOption("Wow, can I join?", () -> setKey(1000)), new DialogueOption("What kind of work?", () -> setKey(1100)), new DialogueOption("What's 'Gielinor'?", () -> setKey(3000)), new DialogueOption("Uh huh, sure.", () -> setKey(4000)));
                    player(1000, "Wow, can I join?");
                    {
                        npc("Entry is strictly invite only, however we do need help continuing Guthix's work.");
                        options(TITLE, new DialogueOption("What kind of work?", () -> setKey(1100)), new DialogueOption("Good luck with that.", () -> setKey(1800)));
                        player(1100, "What kind of work?");
                        {
                            npc("Ah well you see we try to keep Gielinor as Guthix intended, it's very challenging. Actually we've been having some problems recently, maybe you could help us?");
                            options(TITLE, new DialogueOption("Yeah ok, what's the problem?", () -> setKey(1200)), new DialogueOption("What's 'Gielinor'?", () -> setKey(3000)), new DialogueOption("I'd rather not, sorry.", () -> setKey(1500)));
                            player(1200, "Yeah ok, what's the problem?");
                            {
                                npc("Well the order has become quite diminished over the years, it's a very long process to learn the skills of a Void Knight. Recently there have been breaches into our realm from somewhere else, and strange creatures");
                                npc("have been pouring through. We can't let that happen, and we'd be very grateful if you'd help us.");
                                options(TITLE, new DialogueOption("How can I help?", () -> setKey(1300)), new DialogueOption("Sorry, but I can't.", () -> setKey(1400)));
                                player(1300, "How can I help?");
                                {
                                    npc("We send launchers from our outpost to the nearby islands. If you go and wait in the lander there that'd really help.");
                                }
                                {
                                    player(1400, "Sorry, but I can't.");
                                }
                            }
                            player(1500, "I'd rather not, sorry.");
                        }
                        player(1800, "Good luck with that.");
                    }
                    player(3000, "What's 'Gielinor'?");
                    {
                        npc("It is the name that guthix gave this world, so we honour him with its use.");
                    }
                    player(4000, "Uh huh, sure.");
                }
                player(50000, "Where does this ship go?");
                {
                    npc("To the Void Knight outpost. It's a small island just off Karamja.");
                    options(TITLE, new DialogueOption("I'd like to go to your outpost.", () -> setKey(500000)), new DialogueOption("That's nice.", () -> setKey(51000)));
                    player(51000, "That's nice.");
                    player(500000, "I'd like to go to your outpost.");
                    npc("Certainly, right this way.").executeAction(() -> Sailing.sail(player, "Port Sarim", "the Void Knight outpost"));
                }
                player(200000, "I'm fine thanks.");
            }
        }));
        bind("Travel", (player, npc) -> Sailing.sail(player, "Port Sarim", "the Void Knight outpost"));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SQUIRE_1770 };
    }
}
