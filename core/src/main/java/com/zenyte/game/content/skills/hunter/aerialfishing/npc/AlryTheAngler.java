package com.zenyte.game.content.skills.hunter.aerialfishing.npc;

import com.zenyte.game.content.skills.hunter.aerialfishing.item.AerialFishingTools;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Cresinkel
 */

public class AlryTheAngler extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            final boolean goldenTenchBool = player.getInventory().containsItem(ItemId.GOLDEN_TENCH);
            final Item goldenTenchItem = new Item(ItemId.GOLDEN_TENCH);
            @Override
            public void buildDialogue() {
                if (goldenTenchBool) {
                    player(" I found this big fish...");
                    item(goldenTenchItem, "You show Alry the golden tench.");
                    npc("Now that is a biggun! How she managed to wrangle one like that I'll never know.");
                    npc("If you don't want it, I could take it off your hands for a few of those pearls!");
                    player("I'm interested... How many pearls are we talking?");
                    npc("I'll give you a hundred pearls for it. What do you say?");
                    options("Trade in your golden tench for 100 molch pearls?", "It's a deal.", "No thanks.")
                            .onOptionOne(() -> {
                                setKey(30);
                            })
                            .onOptionTwo(() -> {
                                setKey(40);
                            });
                    options(30, "Are you REALLY sure?", "No, keep the tench.", "Yes, exchange the golden tench.")
                            .onOptionOne(() -> {
                                setKey(50);
                            })
                            .onOptionTwo(() -> {
                                setKey(60);
                            });
                    player(40, "No thanks.");
                    npc("Well, let me know if you change your mind.");
                    player(50, "On seconds thoughts, I'd rather keep it.");
                    npc("As you wish.");
                    player(60, "It's a deal.").executeAction(() -> {
                        player.getInventory().deleteItem(ItemId.GOLDEN_TENCH, 1);
                        player.getInventory().addItem(ItemId.MOLCH_PEARL, 100);
                    });
                    npc("Pleasure doing business with you!");
                } else if (player.getEquipment().containsItem(ItemId.CORMORANTS_GLOVE_22817)) {
                    player("Hello again.");
                    npc("Ah, it's you. How are you finding things?");
                    player("It certainly is a challenge...");
                    npc("Yes, your type do often seem to underestimate it! Are you finished?");
                    options(TITLE, "Not yet, I think I'll keep trying.", "Yeah, I'll try again another time.")
                            .onOptionOne(() -> {
                                setKey(10);
                            })
                            .onOptionTwo(() -> {
                                setKey(20);
                            });
                    player(10, "Not yet, I think I'll keep trying.");
                    npc("Just come back and let me know when you get tired!");
                    player(20, "Yeah, I'll try again another time.").executeAction(() -> {
                        player.getEquipment().deleteItem(new Item(ItemId.CORMORANTS_GLOVE_22817));
                        setKey(150);
                    });
                    plain(150, "You give the glove and cormorant back to Alry.");
                } else {
                    player("Hello there.");
                    npc("What brings you to these parts, stranger?");
                    options(TITLE, "What is this place?", "Could I have a go with your bird?", "What's in the sack?")
                            .onOptionOne(() -> {
                                setKey(70);
                            })
                            .onOptionTwo(() -> {
                                setKey(80);
                            })
                            .onOptionThree(() -> {
                                setKey(90);
                            });
                    player(70, "What is this place?");
                    npc("I suppose it is a little different to the places your type are used to...");
                    npc("This is my humble little hideaway! Plenty of fish around to sustain a man like me, with the help of my trusty cormorant.");
                    player("A bird helps you to fish?");
                    npc("Sure she does! I haven't used a rod in years, just send her out to the waters and she takes care of it for me!");
                    options(TITLE, "Could I have a go with your bird?", "I don't think that's for me.")
                            .onOptionOne(() -> {
                                setKey(100);
                            })
                            .onOptionTwo(() -> {
                                setKey(110);
                            });
                    player(100, "Could I have a go with your bird?");
                    npc("I don't know, I doubt that your type's up to the task...");
                    npc("But it would be quite the amusing sight. Go on!").executeAction(() -> {
                        if (player.getEquipment().getId(EquipmentSlot.WEAPON) == -1 && player.getEquipment().getId(EquipmentSlot.HANDS) == -1) {
                            setKey(120);
                        } else {
                            setKey(130);
                        }
                    });

                    player(110, "I don't think that's for me... I'll leave that to you.");
                    npc("No surprises there! Your type never have been cut out for it.");

                    player(80, "Could I have a go with your bird?");
                    npc("I don't know, I doubt that your type's up to the task...");
                    npc("But it would be quite the amusing sight. Go on!").executeAction(() -> {
                        if (player.getSkills().getLevel(SkillConstants.HUNTER) < 35 || player.getSkills().getLevel(SkillConstants.FISHING) < 43) {
                            setKey(160);
                        } else if (player.getEquipment().getId(EquipmentSlot.WEAPON) == -1 && player.getEquipment().getId(EquipmentSlot.HANDS) == -1) {
                            setKey(120);
                        } else {
                            setKey(130);
                        }
                    });

                    npc(120, "I'll keep an eye on you and make sure you don't have too much trouble with her!").executeAction(() -> {
                        player.getEquipment().set(EquipmentSlot.WEAPON, new Item(ItemId.CORMORANTS_GLOVE_22817));
                    });

                    npc(130, "You're going to need to free up your hands first, though. Nothing in or on your hands.");

                    npc(160, "You do not have the minimum required levels participate in this minigame.");

                    player(90, "What's in the sack?");
                    npc("Oh, this? This is my trusty old fish sack!");
                    player("Fish sack?");
                    npc("Yeah, fish sack! I might sell you one if you've found any of those pearls around here.");
                    options(TITLE, "Let's take a look.", "No thanks.")
                            .onOptionOne(() -> {
                                player.openShop("Alry the Angler's Angling Accessories");
                            })
                            .onOptionTwo(() -> {
                                setKey(140);
                            });

                    player(140, "No thanks.");
                }
            }
        }));
        bind("Trade", (player, npc) -> player.openShop("Alry The Angler's Angling Accessories"));
        bind("Get bird", (player, npc) -> {
                    if (AerialFishingTools.hasBird(player)) {
                        player.sendMessage("You're already equipped for aerial fishing. Try talking to Alry instead.");
                    } else {
                        player.getDialogueManager().start(new Dialogue(player, npc) {
                            @Override
                            public void buildDialogue() {
                                player("Could I have a go with your bird?");
                                npc("I don't know, I doubt that your type's up to the task...");
                                npc("But it would be quite the amusing sight. Go on!").executeAction(() -> {
                                    if (player.getSkills().getLevel(SkillConstants.HUNTER) < 35 || player.getSkills().getLevel(SkillConstants.FISHING) < 43) {
                                        setKey(30);
                                    } else if (player.getEquipment().getId(EquipmentSlot.WEAPON) == -1 && player.getEquipment().getId(EquipmentSlot.HANDS) == -1) {
                                        setKey(10);
                                    } else {
                                        setKey(20);
                                    }
                                });

                                npc(10, "I'll keep an eye on you and make sure you don't have too much trouble with her!").executeAction(() -> {
                                    player.getEquipment().set(EquipmentSlot.WEAPON, new Item(ItemId.CORMORANTS_GLOVE_22817));
                                });

                                npc(20, "You're going to need to free up your hands first, though. Nothing in or on your hands.");

                                npc(30, "You do not have the minimum required levels participate in this minigame.");
                            }
                        });
                    }
                }
        );
    }

    @Override
    public int[] getNPCs() {
        return new int[] {NpcId.ALRY_THE_ANGLER};
    }
}
