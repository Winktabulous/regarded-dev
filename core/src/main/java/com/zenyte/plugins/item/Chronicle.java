package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.IntArray;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 28/04/2019 19:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Chronicle extends ItemPlugin implements PairedItemOnItemPlugin {
    @Override
    public void handle() {
        bind("Teleport", (player, item, container, slotId) -> new ChronicleTeleport(item, new Location(3202, 3357, 0)).teleport(player));
        bind("Check Charges", (player, item, container, slotId) -> {
            final int charges = player.getNumericAttribute("Chronicle charges").intValue();
            player.sendMessage("Your chronicle has " + charges + " charge" + (charges == 1 ? "" : "s") + " remaining");
        });
    }

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item card = from.getId() == 13658 ? from : to;
        player.getInventory().deleteItem(card == from ? fromSlot : toSlot, card);
        player.addAttribute("Chronicle charges", player.getNumericAttribute("Chronicle charges").intValue() + 1);
        player.sendMessage("You add a teleport card to the chronicle.");
    }

    @Override
    public int[] getItems() {
        return IntArray.of(13660);
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(13660, 13658)};
    }


    private static final class ChronicleTeleport implements Teleport {
        private final Item item;
        private final Location destination;

        @Override
        public void teleport(final Player player) {
            if (player.getNumericAttribute("Chronicle charges").intValue() <= 0) {
                player.sendMessage("Your chronicle has no charges remaining.");
                return;
            }
            Teleport.super.teleport(player);
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
            return 0;
        }

        @Override
        public Item[] getRunes() {
            return null;
        }

        @Override
        public int getWildernessLevel() {
            return 0;
        }

        @Override
        public boolean isCombatRestricted() {
            return UNRESTRICTED;
        }

        public ChronicleTeleport(Item item, Location destination) {
            this.item = item;
            this.destination = destination;
        }

        @Override
        public void onArrival(final Player player) {
            final int charges = player.getNumericAttribute("Chronicle charges").intValue();
            final String message = charges == 1 ? "<col=7F00FF>You use your chronicle's last charge.</col>" : charges == 2 ? "<col=7F00FF>Your chronicle has one charge left.</col>" : "<col=7F00FF>Your chronicle has " + StringFormatUtil.format(charges - 1) + " charges left.</col>";
            player.addAttribute("Chronicle charges", Math.max(0, player.getNumericAttribute("Chronicle charges").intValue() - 1));
            player.sendMessage(message);
        }
    }
}
