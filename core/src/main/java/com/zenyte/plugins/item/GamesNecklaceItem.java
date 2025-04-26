package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Kris | 26. aug 2018 : 16:36:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class GamesNecklaceItem extends ItemPlugin {
    @Override
    public void handle() {
        bind("Rub", (player, item, container, slotId) -> {
            player.sendMessage("You rub the amulet...");
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Where would you like to teleport to?", new DialogueOption("Burthorpe", () -> new NecklaceTeleport(item, container, slotId, new Location(2898, 3552, 0)).teleport(player)), new DialogueOption("Barbarian Outpost", () -> new NecklaceTeleport(item, container, slotId, new Location(2519, 3572, 0)).teleport(player)), new DialogueOption("Corporeal Beast", () -> new NecklaceTeleport(item, container, slotId, new Location(2967, 4381, 2)).teleport(player)), new DialogueOption("Tears of Guthix", () -> new NecklaceTeleport(item, container, slotId, new Location(3245, 9500, 2)).teleport(player)), new DialogueOption("Wintertodt Camp", () -> new NecklaceTeleport(item, container, slotId, new Location(1627, 3941, 0)).teleport(player)));
                }
            });
        });
        bind("Burthorpe", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(2898, 3552, 0)).teleport(player));
        bind("Barbarian Outpost", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(2519, 3572, 0)).teleport(player));
        bind("Corporeal Beast", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(2967, 4381, 2)).teleport(player));
        bind("Tears of Guthix", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(3245, 9500, 2)).teleport(player));
        bind("Wintertodt Camp", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(1627, 3941, 0)).teleport(player));
    }

    @Override
    public int[] getItems() {
        return GamesNecklace.values.keySet().toIntArray();
    }


    private enum GamesNecklace {
        ONE(new Item(3867), null, 1), TWO(new Item(3865), new Item(3867), 2), THREE(new Item(3863), new Item(3865), 3), FOUR(new Item(3861), new Item(3863), 4), FIVE(new Item(3859), new Item(3861), 5), SIX(new Item(3857), new Item(3859), 6), SEVEN(new Item(3855), new Item(3857), 7), EIGHT(new Item(3853), new Item(3855), 8);
        private static final Int2ObjectMap<GamesNecklace> values = new Int2ObjectOpenHashMap<>();

        static {
            for (final GamesNecklaceItem.GamesNecklace value : values()) {
                values.put(value.item.getId(), value);
            }
        }

        private final Item item;
        private final Item result;
        private final int charges;

        GamesNecklace(Item item, Item result, int charges) {
            this.item = item;
            this.result = result;
            this.charges = charges;
        }

        private static final GamesNecklace get(@NotNull final Item item) {
            return Objects.requireNonNull(values.get(item.getId()));
        }

        public Item getItem() {
            return item;
        }

        public Item getResult() {
            return result;
        }

        public int getCharges() {
            return charges;
        }
    }


    private static final class NecklaceTeleport implements Teleport {
        private final Item item;
        private final Container container;
        private final int slotId;
        private final Location destination;

        @Override
        public void teleport(final Player player) {
            final GamesNecklace necklace = GamesNecklace.get(item);
            if (necklace.getCharges() <= 0) {
                player.sendMessage("Your necklace hasn't got any charges left.");
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
            return WILDERNESS_LEVEL;
        }

        @Override
        public boolean isCombatRestricted() {
            return UNRESTRICTED;
        }

        public NecklaceTeleport(Item item, Container container, int slotId, Location destination) {
            this.item = item;
            this.container = container;
            this.slotId = slotId;
            this.destination = destination;
        }

        @Override
        public void onArrival(final Player player) {
            final GamesNecklaceItem.GamesNecklace necklace = GamesNecklace.get(item);
            final int charges = necklace.getCharges();
            final String message = charges == 1 ? "<col=7F00FF>Your games necklace crumbles to dust.</col>" : charges == 2 ? "<col=7F00FF>Your games necklace has one charge left..</col>" : "<col=7F00FF>Your games necklace has " + StringFormatUtil.format(charges - 1) + " charges left.</col>";
            player.sendMessage(message);
            final Item result = necklace.getResult();
            container.set(slotId, result);
            container.refresh(player);
        }
    }
}
