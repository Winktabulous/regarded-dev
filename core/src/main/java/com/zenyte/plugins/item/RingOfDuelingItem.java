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
 * @author Kris | 26. aug 2018 : 16:34:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class RingOfDuelingItem extends ItemPlugin {
    @Override
    public void handle() {
        bind("Rub", (player, item, container, slotId) -> {
            player.sendFilteredMessage("You rub the ring...");
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Where would you like to teleport to?", new DialogueOption("Duel Arena", () -> new RingTeleport(item, container, slotId, new Location(3315, 3235, 0)).teleport(player)),
                            new DialogueOption("Castle Wars", () -> new RingTeleport(item, container, slotId, new Location(2440, 3090, 0)).teleport(player)),
                            new DialogueOption("Ferox Enclave", () -> new RingTeleport(item, container, slotId, new Location(3151, 3636, 0)).teleport(player)), new DialogueOption("Nowhere"));
                }
            });
        });
        bind("Duel Arena", (player, item, container, slotId) -> new RingTeleport(item, container, slotId, new Location(3315, 3235, 0)).teleport(player));
        bind("Castle Wars", (player, item, container, slotId) -> new RingTeleport(item, container, slotId, new Location(2440, 3090, 0)).teleport(player));
        bind("Ferox Enclave", (player, item, container, slotId) -> new RingTeleport(item, container, slotId, new Location(3151, 3636, 0)).teleport(player));
    }

    @Override
    public int[] getItems() {
        return RingOfDueling.values.keySet().toIntArray();
    }


    private enum RingOfDueling {
        ONE(new Item(2566), null, 1), TWO(new Item(2564), new Item(2566), 2), THREE(new Item(2562), new Item(2564), 3), FOUR(new Item(2560), new Item(2562), 4), FIVE(new Item(2558), new Item(2560), 5), SIX(new Item(2556), new Item(2558), 6), SEVEN(new Item(2554), new Item(2556), 7), EIGHT(new Item(2552), new Item(2554), 8);
        private static final Location[] LOCATIONS = new Location[] {new Location(3315, 3235, 0), new Location(2440, 3090, 0), new Location(3388, 3160, 0)};
        private static final Int2ObjectMap<RingOfDueling> values = new Int2ObjectOpenHashMap<>();

        static {
            for (final RingOfDuelingItem.RingOfDueling value : values()) {
                values.put(value.item.getId(), value);
            }
        }

        private final Item item;
        private final Item result;
        private final int charges;

        RingOfDueling(Item item, Item result, int charges) {
            this.item = item;
            this.result = result;
            this.charges = charges;
        }

        @NotNull
        private static final RingOfDueling get(@NotNull final Item item) {
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


    private static final class RingTeleport implements Teleport {
        private final Item item;
        private final Container container;
        private final int slotId;
        private final Location destination;

        @Override
        public void teleport(final Player player) {
            final RingOfDueling ring = RingOfDueling.get(item);
            if (ring.getCharges() <= 0) {
                player.sendMessage("Your ring hasn't got any charges left.");
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

        public RingTeleport(Item item, Container container, int slotId, Location destination) {
            this.item = item;
            this.container = container;
            this.slotId = slotId;
            this.destination = destination;
        }

        @Override
        public void onArrival(final Player player) {
            final RingOfDuelingItem.RingOfDueling ring = RingOfDueling.get(item);
            final int charges = ring.getCharges();
            final String message = charges == 1 ? "<col=7F00FF>Your ring of dueling crumbles to dust.</col>" : charges == 2 ? "<col=7F00FF>Your ring of dueling has one charge left..</col>" : "<col=7F00FF>Your ring of dueling has " + StringFormatUtil.format(charges - 1) + " charges left.</col>";
            player.sendMessage(message);
            final Item result = ring.getResult();
            container.set(slotId, result);
            container.refresh(player);
        }
    }
}
