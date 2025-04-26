package com.zenyte.plugins.item.capes;

import com.zenyte.game.content.RespawnPoint;
import com.zenyte.game.content.achievementdiary.plugins.item.ArdougneCloak;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.teleports.ItemTeleport;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.plugins.dialogue.OptionDialogue;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.StringFormatUtil;

import java.util.ArrayList;
import java.util.Map;

import static com.zenyte.game.content.skills.magic.spells.teleports.ItemTeleport.*;
import static com.zenyte.game.world.entity.player.dialogue.Dialogue.TITLE;

public class NewMaxCapes extends ItemPlugin {

    public static final int MAX_GRAPPLE_SEARCHES = 3;
    private static final ItemTeleport[] TELEPORTS = {MAX_CAPE_WARRIORS_GUILD, MAX_CAPE_FISHING_GUILD, MAX_CAPE_CRAFTING_GUILD, MAX_CAPE_FARMING_GUILD, MAX_CAPE_OTTOS_GROTTO};

    @Override
    public void handle() {
        bind("Features", (player, item, container, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                final ArrayList<DialogueOption> list = new ArrayList<DialogueOption>();
                list.add(new DialogueOption("Search", key(300)));
                list.add(new DialogueOption("Ring of Life settings", key(5)));
                list.add(new DialogueOption("Commune", key(200)));
                if (container.getType() == ContainerType.INVENTORY) {
                    list.add(new DialogueOption("Spellbook", () -> sendSpellbookDialogue(player)));
                }
                list.add(new DialogueOption("Stamina Boost", () -> {
                    final Number lastConsumptionDate = player.getNumericAttribute("Stamina Boost Use");
                    final long milliseconds = lastConsumptionDate.longValue();
                    if (TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - milliseconds) < 24) {
                        player.sendMessage("You have used your stamina boost for today. Try again tomorrow.");
                        return;
                    }
                    player.addAttribute("Stamina Boost Use", System.currentTimeMillis());
                    player.sendMessage("You feel reinvigorated.");
                    player.getVariables().setRunEnergy(100);
                    player.getVarManager().sendBit(25, 1);
                    player.getVariables().schedule(100, TickVariable.STAMINA_ENHANCEMENT);
                }));
                options(TITLE, list.toArray(new DialogueOption[0]));
                options(5, TITLE, new DialogueOption("Ring of life effect", key(50)), new DialogueOption("Ring of life spawn", key(100)), new DialogueOption("Back", () -> setKey(1)));
                final boolean enabled = player.getBooleanAttribute("Skillcape ring of life teleport");
                plain(50, "Your cape will" + (enabled ? " " : " not ") + "currently teleport you to safety should your health reach dangerous levels.");
                options("Would you like to " + (enabled ? "disable" : "enable") + " this feature?", new DialogueOption("Yes", () -> {
                    player.toggleBooleanAttribute("Skillcape ring of life teleport");
                    setKey(60);
                }), new DialogueOption("No"));
                plain(60, "Your cape will follow your instructions to" + (enabled ? " not " : " ") + "save you when your health is low.");
                options(100, "Choose a respawn destination.", new DialogueOption("Ardougne", () -> {
                    player.setRespawnPoint(RespawnPoint.ARDOUGNE);
                    player.sendMessage(Colour.RED.wrap("Your respawn location has now been changed to Ardougne."));
                }), new DialogueOption("Lumbridge - " + Colour.RS_RED.wrap("Default"), () -> {
                    player.setRespawnPoint(RespawnPoint.LUMBRIDGE);
                    player.sendMessage(Colour.RED.wrap("Your respawn location has now been changed to the default."));
                }));
                final boolean isGatheringJunk = player.getAttributes().containsKey("avasDeviceRetrieve");
                if (isGatheringJunk) {
                    plain(200, "The undead chicken can protect some of your ammunition while you're ranging, and will" +
                            " also gather random metal items for you.");
                    options("Ask it to stop gathering junk?", new DialogueOption("Yes", () -> {
                        player.getAttributes().remove("avasDeviceRetrieve");
                        setKey(205);
                    }), new DialogueOption("No"));
                    plain(205, "You somehow communicate your message to the undead chicken. Henceforth it will no " +
                            "longer gather up random metal items while you've got it equipped.");
                } else {
                    plain(200, "The undead chicken understands that you currently don't want it to accumulate random " +
                            "metal items while you've got it equipped.");
                    options("Ask it to start gathering junk?", new DialogueOption("Yes", () -> {
                        player.getAttributes().put("avasDeviceRetrieve", true);
                        setKey(205);
                    }), new DialogueOption("No"));
                    plain(205, "You somehow communicate your message to the undead chicken. Henceforth it will gather" +
                            " up random metal items while you've got it equipped.");
                }
                options(300, "What would you like to search for?", new DialogueOption("Pestle and Mortar", () -> {
                    if (player.getInventory().containsItem(233, 1)) {
                        player.sendMessage("You already have a pestle and mortar with you.");
                        return;
                    }
                    player.getInventory().addOrDrop(new Item(233));
                    player.sendMessage("You search the cape and find a " + Colour.RS_RED.wrap("Pestle and mortar."));
                    player.setAnimation(new Animation(1376));
                }), new DialogueOption("Mith Grapple & Crossbow", () -> {
                    final int searches = player.getVariables().getGrappleAndCrossbowSearches();
                    if (searches >= MAX_GRAPPLE_SEARCHES) {
                        player.sendMessage("You may only receive a grapple and crossbow three times per day. Try again tomorrow.");
                        return;
                    }
                    if (player.getInventory().containsItem(9174, 1) && player.getInventory().containsItem(9419, 1)) {
                        player.sendMessage("You already have a crossbow and a mith grapple with you.");
                        return;
                    }
                    player.getVariables().setGrappleAndCrossbowSearches(searches + 1);
                    player.getInventory().addOrDrop(new Item(9174));
                    player.getInventory().addOrDrop(new Item(9419));
                    player.sendMessage("You search the cape and find a " + Colour.RS_RED.wrap("Bronze crossbow") + " and " + Colour.RS_RED.wrap("Mithril grapple."));
                    final int remaining = MAX_GRAPPLE_SEARCHES - (searches + 1);
                    if (remaining >= 1) {
                        final String remainingString = remaining == 2 ? "twice more today" : "one more time today";
                        player.sendMessage("You may search the cape " + remainingString);
                    } else {
                        player.sendMessage("You search the cape for the final time today.");
                    }
                    player.setAnimation(new Animation(1376));
                }), new DialogueOption("Back", () -> setKey(1)));
            }
        }));
        bind("Spellbook", (player, item, slotId) -> sendSpellbookDialogue(player));
        bind("Other Teleports", (player, item, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options(TITLE, "Chinchompa Teleports", "Farming Guild").onOptionOne(() -> setKey(5)).onOptionTwo(() -> ItemTeleport.MAX_CAPE_FARMING_GUILD.teleport(player));
                options(5, "Which hunting location would you like to teleport to?", "Carnivorous chinchompas (Feldip Hills)", "Black chinchompas (Wilderness)", "Cancel").onOptionOne(() -> teleport(player, ItemTeleport.MAX_CAPE_CARNIVEROUS_CHINCHOMPAS)).onOptionTwo(() -> teleport(player, ItemTeleport.MAX_CAPE_BLACK_CHINCHOMPAS));
            }
        }));

        bind("Tele to POH", ((player, item, slotId) -> player.sendMessage("Construction is currently disabled.")));
        bind("Fishing Teleports", (player, item, slotId) -> {
            player.getDialogueManager().start(new OptionDialogue(player, TITLE, new String[] {"Fishing Guild", "Otto" +
                    "'s Grotto"}, new Runnable[] {() -> teleport(player, ItemTeleport.MAX_CAPE_FISHING_GUILD), () -> teleport(player, ItemTeleport.MAX_CAPE_OTTOS_GROTTO)}));
        });
        bind("POH Portals", (player, item, slotId) -> sendPOHPortalTeleports(player));
        bind("Warriors' Guild", (player, item, slotId) -> teleport(player, MAX_CAPE_WARRIORS_GUILD));
        bind("Crafting Guild", (player, item, slotId) -> teleport(player, ItemTeleport.MAX_CAPE_CRAFTING_GUILD));
        bind("Teleports", (player, item, slotId) -> {
            player.getDialogueManager().start(new OptionsMenuD(player, "Select a destination", "Warrior's Guild", "Fishing Guild", "Crafting Guild", "Farming Guild", "Otto's Grotto", "Chinchompas", "POH Portals") {
                @Override
                public void handleClick(int slotId) {
                    if (slotId <= 4) {
                        final ItemTeleport teleport = TELEPORTS[slotId];
                        teleport(player, teleport);
                    } else if (slotId == 5) {
                        player.getDialogueManager().start(new OptionDialogue(player, "Which hunting location would you like to teleport to?", new String[] {"Carnivorous chinchompas (Feldip Hills)", "Black chinchompas (Wilderness)", "Cancel"}, new Runnable[] {() -> teleport(player, MAX_CAPE_CARNIVEROUS_CHINCHOMPAS), () -> teleport(player, ItemTeleport.MAX_CAPE_BLACK_CHINCHOMPAS)}));
                    } else if (slotId == 6) {
                        player.getDialogueManager().finish();
                        sendPOHPortalTeleports(player);
                    }
                }
                @Override
                public boolean cancelOption() {
                    return false;
                }
            });
        });



        bind("Monastery Teleport", (player, item, slotId) -> {
            TeleportCollection.ARDOUGNE_CLOAK_MONASTERY_TELEPORT.teleport(player);
        });

        bind("Farm Teleport", (player, item, slotId) -> {
            ArdougneCloak.ardougneFarmTeleport(player, item);
        });

        bind("Commune", (player, item, slotId) -> {
            final Map<String, Object> map = player.getAttributes();
            if (map.containsKey("avasDeviceRetrieve")) {
                map.remove("avasDeviceRetrieve");
                player.sendMessage("You are no longer collecting random metal items.");
            } else {
                map.put("avasDeviceRetrieve", true);
                player.sendMessage("You are now collecting random metal items.");
            }
        });
    }

    public static final void sendSpellbookDialogue(final Player player) {
        if (player.getVariables().getSpellbookSwaps() >= 5) {
            player.getDialogueManager().start(new PlainChat(player, "You may only switch spellbooks five times per day. Try again tomorrow."));
            return;
        }
        final ObjectArrayList<Dialogue.DialogueOption> list = new ObjectArrayList<Dialogue.DialogueOption>();
        final Spellbook currentSpellbook = player.getCombatDefinitions().getSpellbook();
        for (final Spellbook spellbook : Spellbook.VALUES) {
            if (spellbook != currentSpellbook) {
                list.add(new Dialogue.DialogueOption(StringFormatUtil.formatString(spellbook.toString()), () -> {
                    player.getCombatDefinitions().setSpellbook(spellbook, true);
                    final int count = player.getVariables().getSpellbookSwaps() + 1;
                    player.getVariables().setSpellbookSwaps(count);
                    player.sendMessage(Colour.RED.wrap("You have changed your spellbook " + count + "/5 times today."));
                }));
            }
        }
        list.add(new Dialogue.DialogueOption("Cancel"));
        player.getInterfaceHandler().openGameTab(GameTab.SPELLBOOK_TAB);
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Choose spellbook:", list.toArray(new DialogueOption[0]));
            }
        });
    }


    private enum POHPortal implements Teleport {
        HOME(null),
        RIMMINGTON(new Location(2954, 3224, 0)),
        TAVERLEY(new Location(2894, 3465, 0)),
        POLLNIVNEACH(new Location(3340, 3004, 0)),
        HOSIDIUS(new Location(1743, 3517, 0)),
        RELLEKKA(new Location(2670, 3632, 0)),
        BRIMHAVEN(new Location(2758, 3178, 0)),
        YANILLE(new Location(2544, 3095, 0));

        POHPortal(final Location destination) {
            this.destination = destination;
            this.title = StringFormatUtil.formatString(name());
        }

        private final String title;
        private final Location destination;
        private static final POHPortal[] values = values();
        private static final Map<String, POHPortal> map = new Object2ObjectLinkedOpenHashMap<>();

        static {
            for (final POHPortal tele : values) {
                map.put(tele.title, tele);
            }
        }

        @Override
        public TeleportType getType() {
            return TeleportType.REGULAR_TELEPORT;
        }

        @Override
        public Location getDestination() {
            return destination;
        }

        @Override
        public int getLevel() {
            return 0;
        }

        @Override
        public double getExperience() {
            return 0;
        }

        @Override
        public int getRandomizationDistance() {
            return 2;
        }

        @Override
        public Item[] getRunes() {
            return null;
        }

        @Override
        public int getWildernessLevel() {
            return WILDERNESS_LEVEL;
        }

        @Override
        public boolean isCombatRestricted() {
            return false;
        }
    }

    public static final void sendPOHPortalTeleports(final Player player) {
        player.getDialogueManager().start(new OptionsMenuD(player, "House portal teleports", POHPortal.map.keySet().toArray(new String[0])) {
            @Override
            public void handleClick(int slotId) {
                final POHPortal type = POHPortal.values[slotId];
                if (type == POHPortal.HOME) {
                    player.sendMessage("Construction is currently disabled.");
                    return;
                }
                type.teleport(player);
            }
            @Override
            public boolean cancelOption() {
                return false;
            }
        });
    }

    private void teleport(final Player player, final Teleport teleport) {
        teleport.teleport(player);
    }

    @Override
    public int[] getItems() {
        return new int[] {
                13342, 13329, 13331, 13333, 13335, 21285,
                21776, 21780, 21784, 21898, 20760, 13337,
                ItemId.ASSEMBLER_MAX_CAPE_L,
                ItemId.INFERNAL_MAX_CAPE_L,
                ItemId.FIRE_MAX_CAPE_L,
                ItemId.IMBUED_SARADOMIN_MAX_CAPE_L,
                ItemId.IMBUED_ZAMORAK_MAX_CAPE_L,
                ItemId.IMBUED_GUTHIX_MAX_CAPE_L
        };
    }
}
