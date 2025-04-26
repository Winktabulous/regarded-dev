package com.zenyte.plugins.item;

import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.DoubleItemChat;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 25/03/2019 18:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class XericsTalisman extends ItemPlugin implements PairedItemOnItemPlugin {
    @Override
    public void handle() {
        bind("Xeric's Lookout", (player, item, container, slotId) -> new TalismanTeleport(item, container, slotId, new Location(1579, 3531, 0)).teleport(player));
        bind("Xeric's Glade", (player, item, container, slotId) -> new TalismanTeleport(item, container, slotId, new Location(1776, 3504, 0)).teleport(player));
        bind("Xeric's Inferno", (player, item, container, slotId) -> new TalismanTeleport(item, container, slotId, new Location(1503, 3815, 0)).teleport(player));
        bind("Xeric's Heart", (player, item, container, slotId) -> new TalismanTeleport(item, container, slotId, new Location(1643, 3671, 0)).teleport(player));
        bind("Xeric's Honour", (player, item, container, slotId) -> {
            if (!player.getAttributes().containsKey("xeric's honour")) {
                player.getDialogueManager().start(new PlainChat(player, "The talisman does not have the power to take you there."));
                return;
            }
            new TalismanTeleport(item, container, slotId, new Location(1254, 3559, 0)).teleport(player);
        });
        bind("Rub", (player, item, container, slotId) -> player.getDialogueManager().start(new OptionsMenuD(player, "The talisman has " + item.getCharges() + " " + (item.getCharges() == 1 ? "charge" : "charges") + ".", "Xeric's Look-out", "Xeric's Glade", "Xeric's Inferno", "Xeric's Heart", "Xeric's Honour", "Nowhere") {
            @Override
            public void handleClick(int clickedSlot) {
                if (container.get(slotId) != item) {
                    return;
                }
                switch (clickedSlot) {
                case 0: 
                    new TalismanTeleport(item, container, slotId, new Location(1579, 3531, 0)).teleport(player);
                    break;
                case 1: 
                    new TalismanTeleport(item, container, slotId, new Location(1776, 3504, 0)).teleport(player);
                    break;
                case 2: 
                    new TalismanTeleport(item, container, slotId, new Location(1503, 3815, 0)).teleport(player);
                    break;
                case 3: 
                    new TalismanTeleport(item, container, slotId, new Location(1643, 3671, 0)).teleport(player);
                    break;
                case 4: 
                    if (!player.getAttributes().containsKey("xeric's honour")) {
                        player.getDialogueManager().start(new PlainChat(player, "The talisman does not have the power to take you there."));
                        return;
                    }
                    new TalismanTeleport(item, container, slotId, new Location(1254, 3559, 0)).teleport(player);
                    break;
                }
            }
            @Override
            public boolean cancelOption() {
                return false;
            }
        }));
        bind("Dismantle", (player, item, container, slotId) -> {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Dismantle your Xeric Talisman?", new DialogueOption("Yes", () -> {
                        if (player.getInventory().getItem(slotId) == item) {
                            player.getInventory().ifDeleteItem(item, () -> player.getInventory().addOrDrop(new Item(13391, 100)));
                        }
                    }), new DialogueOption("No"));
                }
            });
        });
        bind("Check", (player, item, container, slotId) -> player.sendMessage("The talisman has " + item.getCharges() + " " + (item.getCharges() == 1 ? "charge" : "charges") + "."));
        bind("Inspect", (player, item, container, slotId) -> player.getDialogueManager().start(new ItemChat(player, item, "You inspect the tablet, and sense a magical power, running your fingers over the inscriptions, they tingle as you get a feel for Xeric's power. He must have stored one of his best spells in this tablet, perhaps he has some sort of device which could utilise it?")));
        bind("Uncharge", (player, item, container, slotId) -> {
            if (item.getCharges() == 0) {
                player.sendMessage("Your talisman has no charges remaining.");
                return;
            }
            player.getInventory().deleteItem(slotId, item);
            final Item talisman = new Item(13392, 1);
            final Item fangs = new Item(13391, item.getCharges());
            player.getInventory().addItem(talisman);
            player.getInventory().addOrDrop(fangs);
            player.getDialogueManager().start(new DoubleItemChat(player, talisman, fangs, "You remove " + fangs.getAmount() + " lizard " + (fangs.getAmount() == 1 ? "fang" : "fangs") + " from your talisman."));
        });
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (from.getId() == 21046 || to.getId() == 21046) {
            final Item tablet = from.getId() == 21046 ? from : to;
            final Item amulet = from == tablet ? to : from;
            if (player.getAttributes().containsKey("xeric's honour")) {
                player.sendMessage("You've already infused the amulet with the power of the tablet.");
                return;
            }
            player.getInventory().deleteItem(tablet);
            player.getAttributes().put("xeric's honour", true);
            player.getDialogueManager().start(new DoubleItemChat(player, tablet, amulet, "As the talisman and tablet are held together, a surge of power connects the two. After a brief moment, the tablet crumbles, but the eyes of the talisman glow stronger than ever."));
            return;
        }
        final Item amulet = (from.getId() == 13392 || from.getId() == 13393) ? from : to;
        final Item fangs = amulet == from ? to : from;
        if (amulet.getCharges() >= 1000) {
            player.sendMessage("Your Xeric's amulet is already fully charged.");
            return;
        }
        final int amount = Math.min(1000 - amulet.getCharges(), fangs.getAmount());
        player.getInventory().deleteItem(new Item(fangs.getId(), amount));
        amulet.setCharges(amulet.getCharges() + amount);
        amulet.setId(13393);
        player.getInventory().refreshAll();
        player.getDialogueManager().start(new ItemChat(player, amulet, "Your talisman now has " + (amulet.getCharges() <= 10 ? (Utils.numNames[amulet.getCharges()].substring(1)) : amulet.getCharges()) + " charge" + (amulet.getCharges() == 1 ? "" : "s") + "."));
    }

    @Override
    public int[] getItems() {
        return new int[] {13392, 13393, 21046};
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(13391, 13392), ItemPair.of(13391, 13393), ItemPair.of(21046, 13392), ItemPair.of(21046, 13393)};
    }


    private static final class TalismanTeleport implements Teleport {
        private final Item item;
        private final Container container;
        private final int slotId;
        private final Location destination;

        @Override
        public void teleport(final Player player) {
            if (item.getCharges() <= 0) {
                player.sendMessage("Your Xeric's talisman needs to be recharged before it can be used again.");
                return;
            }
            Teleport.super.teleport(player);
        }

        @Override
        public TeleportType getType() {
            return TeleportType.XERICS_TELEPORT;
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
            return 0;
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
            return UNRESTRICTED;
        }

        @Override
        public void onArrival(final Player player) {
            player.getChargesManager().removeCharges(item, 1, container, slotId);
            if (item.getCharges() <= 0) {
                player.sendMessage(Colour.RED.wrap("Your talisman has run out of charges."));
            }
            if (destination.getX() == 1643 && destination.getY() == 3671) {
                //Xeric's heart
                player.getAchievementDiaries().update(KourendDiary.TELEPORT_TO_XERICS_HEART);
            }
        }

        public TalismanTeleport(Item item, Container container, int slotId, Location destination) {
            this.item = item;
            this.container = container;
            this.slotId = slotId;
            this.destination = destination;
        }
    }
}
