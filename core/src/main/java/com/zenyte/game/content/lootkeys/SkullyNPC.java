package com.zenyte.game.content.lootkeys;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

public class SkullyNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Eyup. They call me Skully. I run this Wilderness Loot Chest.");
                npc("Right now we're working on a coupl' things, come back in a little while.");
            }
        }));

        bind("Value", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Eyup. They call me Skully. I run this Wilderness Loot Chest.");
                npc("Right now we're working on a coupl' things, come back in a little while.");
            }
        }));
        bind("Settings", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Eyup. They call me Skully. I run this Wilderness Loot Chest.");
                npc("Right now we're working on a coupl' things, come back in a little while.");
            }
        }));
    }


    private Dialogue talkToOptions(Player player, NPC npc) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {

                if (player.getLootkeySettings() != null) {
                    options("Select an Option",
                            "How does the chest work?",
                            "Can I change how these loot keys work?",
                            "How much loot have I claimed?",
                            "Who are you, exactly?",
                            "Goodbye.")
                            .onOptionOne(() -> {
                                player.getDialogueManager().start(chestWorksDialogue(player, npc));
                            })
                            .onOptionTwo(() -> {
                                player.getDialogueManager().start(changeLootkeysOptions(player, npc));
                            })
                            .onOptionThree(() -> {
                                player.getDialogueManager().start(claimedLoot(player, npc, true));
                            })
                            .onOptionFour(() -> {
                                player.getDialogueManager().start(whoAreYou(player, npc));
                            })
                            .onOptionFive(() -> {
                                player.getDialogueManager().start(bye(player, npc));
                            });
                } else {
                    options("Select an Option",
                            "How does the chest work?",
                            "Can I have access to the chest?",
                            "How much loot have I claimed?",
                            "Who are you, exactly?",
                            "Goodbye.")
                            .onOptionOne(() -> {
                                player.getDialogueManager().start(chestWorksDialogue(player, npc));
                            })
                            .onOptionTwo(() -> {
                                player.getDialogueManager().start(accessToChest(player, npc));
                            })
                            .onOptionThree(() -> {
                                player.getDialogueManager().start(claimedLoot(player, npc, true));
                            })
                            .onOptionFour(() -> {
                                player.getDialogueManager().start(whoAreYou(player, npc));
                            })
                            .onOptionFive(() -> {
                                player.getDialogueManager().start(bye(player, npc));
                            });
                }

            }
        };
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 10382 };
    }

    private Dialogue changeLootkeysOptions(Player player, NPC npc) {
        return new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                LootkeySettings settings = player.getLootkeySettings();
                if (settings == null) {
                    finish();
                    return;
                }

                boolean enabled = settings.isEnabled();
                boolean dropToFloor = settings.isDropFood();
                boolean dropValuablesToFloor = settings.isDropValuables();

                options("Select an Option",
                        enabled ? "Turn loot keys off" : "Turn loot keys on",
                        dropToFloor ? "Send food to loot key" : "Drop food to floor",
                        dropValuablesToFloor ? "Send valuables to loot key" : "Drop valuables to floor",
                        "Change valuable item threshold"
                        )
                        .onOptionOne( () -> {
                            settings.setEnabled(!enabled);
                            if (enabled) {
                                player.getDialogueManager().start(turnOffLootkeys(player, npc));
                            } else {
                                player.getDialogueManager().start(turnOnLootkeys(player, npc));
                            }
                        })
                        .onOptionTwo(() -> {
                            settings.setDropFood(!dropToFloor);
                            if (!dropToFloor) {
                                player.getDialogueManager().start(new Dialogue(player, npc) {
                                    @Override
                                    public void buildDialogue() {
                                        npc("Ok, food and potions will now be dropped on the floor instead of stored in a loot key.");
                                    }
                                    @Override
                                    public void finish() {
                                        super.finish();
                                        player.getDialogueManager().start(changeLootkeysOptions(player, npc));
                                    }
                                });
                            } else {
                                player.getDialogueManager().start(new Dialogue(player, npc) {
                                    @Override
                                    public void buildDialogue() {
                                        npc("Ok, food and potions will now be sent to your loot keys.");
                                    }
                                    @Override
                                    public void finish() {
                                        super.finish();
                                        player.getDialogueManager().start(changeLootkeysOptions(player, npc));
                                    }
                                });
                            }
                        })
                        .onOptionThree(() -> {
                            settings.setDropValuables(!dropValuablesToFloor);
                            if (!dropValuablesToFloor) {
                                player.getDialogueManager().start(new Dialogue(player, npc) {
                                    @Override
                                    public void buildDialogue() {
                                        npc("Ok, valuable items will now be dropped on the floor instead of stored in a loot key.");
                                    }
                                    @Override
                                    public void finish() {
                                        super.finish();
                                        player.getDialogueManager().start(changeLootkeysOptions(player, npc));
                                    }
                                });
                            } else {
                                player.getDialogueManager().start(new Dialogue(player, npc) {
                                    @Override
                                    public void buildDialogue() {
                                        npc("Ok, valuable items will now be sent to your loot keys.");
                                    }
                                    @Override
                                    public void finish() {
                                        super.finish();
                                        player.getDialogueManager().start(changeLootkeysOptions(player, npc));
                                    }
                                });
                            }
                        })
                        .onOptionFour(() -> player.getDialogueManager().start(changeThreshold(player, npc)));


            }
        };
    }

    private Dialogue changeThreshold(Player player, NPC npc) {

        LootkeySettings settings = player.getLootkeySettings();
        if (settings == null) return null;

        int threshold = settings.getThreshold();

        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Currently, items worth " + Utils.formatNumberWithCommas(threshold) + "gp or more will" +
                        " be dropped to the floor. What do you want to change that value to?");
            }
            @Override
            public void finish() {
                super.finish();
                player.sendInputInt("Enter Amount:", amount -> {
                    settings.setThreshold(amount);
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            npc("Ok, now items worth at least " + Utils.formatNumberWithCommas(amount) + "gp or more " +
                                    "will drop to the floor.");
                        }
                        @Override
                        public void finish() {
                            super.finish();
                            player.getDialogueManager().start(changeLootkeysOptions(player, npc));
                        }
                    });
                });
            }
        };
    }

    private Dialogue turnOffLootkeys(Player player, NPC npc) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Ok, whenever you kill someone else now, their items will go to the floor like normal. " +
                        "Just remember, they could have loot keys on them, and they'll still go to your inventory!");
            }
            @Override
            public void finish() {
                super.finish();
                player.getDialogueManager().start(changeLootkeysOptions(player, npc));
            }
        };
    }

    private Dialogue turnOnLootkeys(Player player, NPC npc) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Ok, you'll now get the loot keys when you kill another person in the Wilderness.");
            }

            @Override
            public void finish() {
                super.finish();
                player.getDialogueManager().start(changeLootkeysOptions(player, npc));
            }
        };
    }


    private Dialogue whoAreYou(Player player, NPC npc) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                player("Who are you, exactly?");
                npc("Ah, used to be an adventurer like you, I guess. Used to roam the world, killing " +
                        "monsters for a living. 'Cept then, I discovered the Wilderness.");
                npc("Used to take on anyone and everyone who stood in me way, and made made myself a small " +
                        "fortune doing it too.");
                npc("It's how i got the name, used to go anywhere and everywhere with a skull over my head!");
                player("So how'd you end up like... this?");
                npc("Ah, you live a life feeding on others, eventually something higher up the food chain finds you.");
                npc("I used to have a hidden outpost near the Bone Yard, somewhere to keep my stuff in an emergency. " +
                        "One day all I come back to is a smouldering pile of rubble. ");
                npc("Next thing i know, someone's snuck up behind me and has put a spear through my back.");
                player("And you survived?!", Expression.HIGH_REV_SHOCKED);
                npc("Always carry a ring of lie, friend! Never know when you'll need it.", Expression.HIGH_REV_HAPPY);
                npc("Still, the damage was done. Everything i ever owned up in smoke, and even though the wound's fully " +
                        "healed, I can still feel that spear in my back to this day!");
                npc("Thankfully, Ferox was kind enough to take me in, and i managed to repurpose some of " +
                        "my old storage magic for adventurers like you.");

                player("But why come back to the Wilderness?");

                npc("What, go life a normal life behind a shop counter so i can pedal swamp paste for a living instead? I'll pass!");
                npc("Anyhow, enough about me. What can i do for you?");
            }

            @Override
            public void finish() {
                super.finish();
                player.getDialogueManager().start(talkToOptions(player, npc));
            }
        };
    }

    private Dialogue chestWorksDialogue(Player player, NPC npc) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Well, you know how when you kill someone all of their stuff just ends up in a pile on the " +
                        "floor and it's really hard to pick it all up quickly?");
                npc("I created an enchantment that teleports the loot to another dimension instead!");
                player("How does that help?");
                npc("Well, instead of getting a pile of rubbish, you get a single key. " +
                        "You take the key to this chest and BAM! There's your loot!");

                player("So the loot is safe in this other dimension?");

                npc("Eh, seems like it. As long as they've had the key, I've never had anyone unable " +
                        "to get the stuff back out. Though if you lose the key, you can't get the stuff out.");

                npc("Oh, and be careful running about with too many keys on you. Other people will know you've got them, " +
                        "and I'm not gonna stop them opening the chest!");

                player("So if i kill someone and take their keys, i can use them myself?");

                npc("...Eh, if you wanna think about it that way, sure.");

                npc("I got nothing better to do, so I'll keep track of the value of the stuff you unlock.");
                npc("And anything you don't want, you can have the chest destroy it.");
                npc("Except the food. I'll eat that instead.");
                npc("Now, if you want your own keys instead of relying on the misfortune of others, " +
                        "I can put the enchantment on you as well, but it's gonna cost ya.");

            }

            @Override
            public void finish() {
                super.finish();
                player.getDialogueManager().start(talkToOptions(player, npc));
            }
        };
    }

    private Dialogue claimedLoot(Player player, NPC npc, boolean bringBackToOptions) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if (player.getLootkeySettings() != null) {
                    long key = player.getLootkeySettings().getKeysClaimed();
                    if (key <= 0) {
                        npc("Well, seeing as you haven't claimed a key yet, I reckon you've claimed a total of 0gp worth of loot.");
                    } else {
                        long total = player.getLootkeySettings().getTotalValueClaimed();
                        npc("You have claimed "+key+" keys, I reckon you've claimed a total of "+ Utils.formatNumberWithCommas(total) +"gp worth of loot.");
                    }
                } else {
                    npc("Well, seeing as you haven't claimed a key yet, I reckon you've claimed a total of 0gp worth of loot.");
                }
            }

            @Override
            public void finish() {
                super.finish();
                if (bringBackToOptions) {
                    player.getDialogueManager().start(talkToOptions(player, npc));
                }
            }
        };
    }


    private Dialogue accessToChest(Player player, NPC npc) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if (player.getLootkeySettings() != null) {

                } else {
                    player("Can i have access to the chest?");
                    npc("So, if you have keys from someone else, you're more than welcome to put them in the chest and see what comes out.");
                    npc("If you want the enchantment that makes the keys, I'm afraid it's gonna cost you 5 million coins.");
                    options("Select an Option",
                            "Sure, I'll pay for that.",
                            "Not right now, thanks.",
                            "How much?!")
                            .onOptionOne(() -> {
                                player.getDialogueManager().start(firstPrompt(player, npc));
                            })
                            .onOptionTwo(() -> {
                                player.getDialogueManager().start(notRightNow(player, npc));
                            })
                            .onOptionThree(() -> {
                                player("How much?!");
                                npc("5 million coins.");
                            });
                }
            }

            @Override
            public void finish() {
                super.finish();
                player.getDialogueManager().start(talkToOptions(player, npc));
            }
        };
    }

    private Dialogue firstPrompt(Player player, NPC npc) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                player("Sure, I'll pay for that.");
                npc("Are you sure? 5 million is a lot of coins, and you won't get them back!");
                options("Pay 5 million coins to unlock Loot Keys?",
                        "Yes, pay 5 million coins!",
                        "No, I've changed my mind!")
                        .onOptionOne(() -> {
                            int coins  = player.getInventory().getAmountOf(995);
                            if (coins >= 5_000_000) {
                                player.getInventory().deleteItem(995, 5_000_000);
                                player.setLootkeySettings(new LootkeySettings(true, false,
                                        false, 5_000_000, 0, 0));
                                player.getDialogueManager().start(new Dialogue(player, npc) {
                                    @Override
                                    public void buildDialogue() {
                                        npc("Ok, let me just... and a bit of... and we're done! " +
                                                "You'll now get loot keys whenever you kill someone in the Wilderness. " +
                                                "Talk to me again if you have any questions!");
                                    }
                                });
                            } else {
                                player.getDialogueManager().start(new Dialogue(player, npc) {
                                    @Override
                                    public void buildDialogue() {
                                        npc("Hmm... I don't think you have enough money on you there, mate.");
                                    }
                                });
                            }
                        })
                        .onOptionTwo(() -> {
                            player.getDialogueManager().start(new Dialogue(player, npc) {
                                @Override
                                public void buildDialogue() {
                                    npc("Fair enough mate, talk to me again if you change your mind.");
                                }
                            });
                        });
            }
            @Override
            public void finish() {
                super.finish();
                player.getDialogueManager().start(talkToOptions(player, npc));
            }
        };
    }

    private Dialogue notRightNow(Player player, NPC npc) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                player("Not right now, thanks");
                npc("Fair enough mate, want to talk about something else?");
            }
            @Override
            public void finish() {
                super.finish();
                player.getDialogueManager().start(talkToOptions(player, npc));
            }
        };
    }


    private Dialogue bye(Player player, NPC npc) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                player("Goodbye.");
                npc("I'll be here if you need anything.");
                finish();
            }
        };
    }
}
