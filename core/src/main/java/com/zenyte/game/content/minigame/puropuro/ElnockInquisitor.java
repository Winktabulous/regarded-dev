package com.zenyte.game.content.minigame.puropuro;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Corey
 * @since 01/02/2020
 */
public class ElnockInquisitor extends NPCPlugin {
    private static final String ELNOCK_EQUIPMENT_ATTRIBUTE = "elnock_equipment";

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final Boolean receivedStarterEquipment = player.getAttributeOrDefault(ELNOCK_EQUIPMENT_ATTRIBUTE, false);
            if (receivedStarterEquipment) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("Ah, good day, it's you again. What can I do for you?");
                        final Boolean overlayEnabled = player.getAttributeOrDefault(PuroPuroImplingOverlay.PURO_PURO_OVERLAY_ATTRIBUTE, false);
                        options(TITLE, new DialogueOption("Can you remind me how to catch implings again?", key(10)), new DialogueOption("Can I trade some jarred implings please?", key(20)), new DialogueOption("Do you have some spare equipment I can use?", key(30)), overlayEnabled ? new DialogueOption("Can you stop tracking how many implings I've already jarred?", key(40)) : new DialogueOption("Can you help me keep track of the implings I've already jarred?", key(50)));
                        {
                            player(10, "Can you remind me how to catch implings again?");
                            npc("Certainly.");
                            npc("Firstly you will need a butterfly net in which to catch them and at least one special impling jar to store an impling.");
                            npc("You will also require some experience as a Hunter since these creatures are elusive. the more immature implings require less experience, but some of the rarer implings are extraordinarily hard to find and catch.");
                            npc("Once you have caught one, you may break the jar open and obtain the object the impling is carrying. Alternatively, you may exchange certain combinations of jars with me. I will return the jars to my clients. In");
                            npc("exchange I will be able to provide you with some equipment that may help you hunt butterflies more effectively.");
                            npc("Also, beware. Those imps walking around the maze do not like the fact that their kindred spirits are being captured and will attempt to steal any full jars you have on you, setting the implings free.");
                        }
                        {
                            player(20, "Can I trade some jarred implings please?").executeAction(() -> GameInterface.ELNOCKS_EXCHANGE.open(player));
                        }
                        {
                            npc(30, "I have already given you some equipment.");
                            npc("If you are ready to start hunting implings, then enter the main part of the maze.");
                            npc("Just push through the wheat that surrounds the centre of the maze and get catching!");
                        }
                        {
                            player(40, "Can you stop tracking how many implings I've already jarred?").executeAction(() -> {
                                player.getInterfaceHandler().closeInterface(GameInterface.PURO_PURO_IMPLING_OVERLAY);
                                player.addAttribute(PuroPuroImplingOverlay.PURO_PURO_OVERLAY_ATTRIBUTE, false);
                            });
                            npc("There you go. Just come back to me if you want to turn it back on.");
                        }
                        {
                            player(50, "Can you help me keep track of the implings I've already jarred?").executeAction(() -> {
                                GameInterface.PURO_PURO_IMPLING_OVERLAY.open(player);
                                player.addAttribute(PuroPuroImplingOverlay.PURO_PURO_OVERLAY_ATTRIBUTE, true);
                            });
                            npc("There you go. Just come back to me if you want to turn it off.");
                        }
                    }
                });
            } else {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        player("What's a gnome doing here?");
                        npc("I'm an investigator. I'm watching the implings.");
                        player("Why would you want to do that?");
                        npc("My client has asked me to find out where certain missing items have been going.");
                        player("Who is this client?");
                        npc("I'm not at liberty to discuss that. Investigator-client confidentiality don't you know.");
                        options(TITLE, new DialogueOption("Where is this place?", key(10)), new DialogueOption("So what are these implings?", key(40)), new DialogueOption("Can I catch these implings then?", key(60)), new DialogueOption("I've heard I may find dragon equipment in Puro-Puro.", key(80)));
                        {
                            player(10, "Where is this place?");
                            npc("I've heard it called Puro-Puro. It seems to be the home plane of the implings.");
                            npc("I don't think these creatures have a name for it. As you can see there isn't a lot " +
                                    "else here other than wheat.");
                            player("How did you get here?");
                            npc("The same way you did, I suspect. Through a portal in a wheat field. I followed one back.");
                            player("I haven't noticed them do that.");
                            npc("That's why I'm the investigator and you're the adventurer.");
                            options(TITLE, new DialogueOption("So what are these implings?", key(40)), new DialogueOption("Can I catch these implings then?", key(60)), new DialogueOption("I've heard I may find dragon equipment in Puro-Puro.", key(80)));
                        }
                        {
                            player(40, "So, what are these implings?");
                            npc("That's a very interesting question. My best guess is that they are relatives to " +
                                    "imps,  which is why there are imps here as well.");
                            npc("Implings do appear to like collecting objects and, as my clients have noted, have no concept of ownership. However, I do not sense any malicious intent.");
                            npc("It is my observation that they collect things other creatures want, rather than they want them themselves. It seems to provide them with sustenance.");
                            player("So they feed off our desire for things?");
                            npc("Possibly. Either way, it seems that they almost exclusively collect things that " +
                                    "people want, except their younglings who I infer haven't learn the best things " +
                                    "to collect yet.");
                            player("So the more experienced implings have the more desirable items?");
                            npc("that is my observation. Yes.");
                            options(TITLE, new DialogueOption("Where is this place?", key(10)), new DialogueOption("Can I catch these implings then?", key(60)), new DialogueOption("I've heard I may find dragon equipment in Puro-Puro.", key(80)));
                        }
                        {
                            player(60, "Can I catch these implings, then?");
                            npc("Indeed you may. In fact I encourage it. You will, however, require some equipment.");
                            npc("Firstly you will need a butterfly net in which to catch them and at least one special impling jar to store an impling.");
                            npc("You will also require some experience as a Hunter since these creatures are elusive. the more immature implings require less experience, but some of the rarer implings are extraordinarily hard to find and catch.");
                            npc("Once you have caught one, you may break the jar open and obtain the object the impling is carrying. Alternatively, you may exchange certain combinations of jars with me. I will return the jars to my clients. In");
                            npc("exchange I will be able to provide you with some equipment that may help you hunt butterflies more effectively.");
                            npc("Also, beware. Those imps walking around the maze do not like the fact that their kindred spirits are being captured and will attempt to steal any full jars you have on you, setting the implings free.");
                            options(TITLE, new DialogueOption("Tell me more about these jars.", key(100)), new DialogueOption("Tell me more about these thieving imps.", key(120)), new DialogueOption("So what's this equipment you can give me then?", key(140)), new DialogueOption("Do you have some spare equipment I can use?", key(160)));
                            {
                                npc(100, "You cannot use an ordinary butterfly jar as a container as the implings will escape from them with ease. However, I have done some investigation and have come up with a solution - if a butterfly jar is coated");
                                npc("with a thin layer of substance noxious to them they become incapable of escape.");
                                player("What substance is then, then?");
                                npc("I have tried a few experiments with the help of a friend back home, and it turns out that a combination of anchovy oil and flowers - marigolds, rosemary or nasturtiums - will work.");
                                player("How do you make anchovy oil then?");
                                npc("I'd grind up some cooked anchovies and pass them through a sieve.");
                                player("Where do I make these jars?");
                                npc("well, I believe there is a chemist in Rimmington that has a small still that you could use.");
                                player("Is there anywhere I can buy these jars?");
                                npc("Well I may be able to let you have a few - if it means you will start hunting these implings - although I do not have an infinite supply.");
                                options(TITLE, new DialogueOption("Tell me more about these thieving imps.", key(120)), new DialogueOption("So what's this equipment you can give me then?", key(140)), new DialogueOption("Do you have some spare equipment I can use?", key(160)));
                            }
                            {
                                player(120, "Tell me more about these thieving imps.");
                                npc("Imps and implings appear to be related, and the imps here are quite protective of their smaller relations. If you allow them to get too close then they will attempt to steal jarred implings from your pack, if you have them.");
                                npc("They will then set them free, dropping your jar on the floor. So, if you're " +
                                        "quick, you may be able to catch it again.");
                                npc("I have some impling deterrent which I may trade if you prove that you can catch implings well.");
                                options(TITLE, new DialogueOption("Tell me more about these jars.", key(100)), new DialogueOption("So what's this equipment you can give me then?", key(140)), new DialogueOption("Do you have some spare equipment I can use?", key(160)));
                            }
                            {
                                player(140, "So what's this equipment you can give me, then?");
                                npc("I have been given permission by my clients to give three pieces of equipment to able hunters.");
                                npc("firstly, I have some imp deterrent. If you bring me three baby implings, two " +
                                        "young implings and one gourmet implings already jarred, I will give you a " +
                                        "vial. Imps don't like the smell, so they will be less likely to");
                                npc("steal jarred implings from you.");
                                npc("Secondly, I have magical butterfly nets. If you bring me three gourmet implings, two earth implings and one essence impling I will give you a new net. It will help you catch both implings and butterflies.");
                                npc("Lastly, I have magical jar generators. If you bring me three essence implings, two eclectic implings and one nature impling I will give you a jar generator. This object will create either butterfly or impling jars (up to");
                                npc("a limited number of charges) without having to carry a pack full of them.");
                                options(TITLE, new DialogueOption("Tell me more about these jars.", key(100)), new DialogueOption("Tell me more about these thieving imps.", key(120)), new DialogueOption("Do you have some spare equipment I can use?", key(160)));
                            }
                            {
                                npc(160, "Yes! I have some spare equipment for you.").executeAction(() -> {
                                    player.getInventory().addOrDrop(new Item(ItemId.IMPLING_JAR, 7), new Item(ItemId.BUTTERFLY_NET));
                                    player.addAttribute(ELNOCK_EQUIPMENT_ATTRIBUTE, true);
                                });
                                npc("There you go. You have everything you need now.");
                                npc("If you are ready to start hunting implings, then enter the main part of the maze.");
                                npc("Just push through the wheat that surrounds the centre of the maze and get catching!");
                                player("Thanks, I'll get going.");
                            }
                        }
                        {
                            player(80, "I've heard I may find dragon equipment in Puro-Puro.");
                            npc("Have you indeed? Well, that may well be true, though bear in mind that implings are quite small so they are unlikely to be lugging a siezable shield around with them. However it seems that dragon items are");
                            npc("very desirable to humans then it certainly is possible that the most expert implings may try to obtain such equipment.");
                            player("So, it's true then? Cool!");
                            npc("I should warn you, though, if the impling is strong enough to collect dragon equipment, then you will have to be very skilled at hunting implings in order to catch them.");
                            options(TITLE, new DialogueOption("Where is this place?", key(10)), new DialogueOption("So what are these implings?", key(40)), new DialogueOption("Can I catch these implings then?", key(60)));
                        }
                    }
                });
            }
        });
        bind("Trade", (player, npc) -> GameInterface.ELNOCKS_EXCHANGE.open(player));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {5734};
    }
}
