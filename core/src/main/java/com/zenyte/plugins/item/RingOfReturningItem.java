package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Kris | 26. aug 2018 : 16:42:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 * profile</a>}
 */
public class RingOfReturningItem extends ItemPlugin {
    @Override
    public void handle() {
        bind("Rub", (player, item, container, slotId) -> new RingTeleport(item, container, slotId, player.getRespawnPoint().getLocation()).teleport(player));
    }

    @Override
    public int[] getItems() {
        return RingOfReturning.values.keySet().toIntArray();
    }


    private enum RingOfReturning {
        ONE(new Item(21138), null, 1), TWO(new Item(21136), new Item(21138), 2), THREE(new Item(21134), new Item(21136), 3), FOUR(new Item(21132), new Item(21134), 4), FIVE(new Item(21129), new Item(21132), 5);
        private static final Int2ObjectMap<RingOfReturning> values = new Int2ObjectOpenHashMap<>();

        static {
            for (final RingOfReturningItem.RingOfReturning value : values()) {
                values.put(value.item.getId(), value);
            }
        }

        private final int charges;
        private final Item item;
        private final Item result;

        RingOfReturning(final Item item, final Item result, final int charges) {
            this.item = item;
            this.result = result;
            this.charges = charges;
        }

        @NotNull
        private static final RingOfReturning get(@NotNull final Item item) {
            return Objects.requireNonNull(values.get(item.getId()));
        }

        public int getCharges() {
            return charges;
        }

        public Item getItem() {
            return item;
        }

        public Item getResult() {
            return result;
        }
    }


    private static final class RingTeleport implements Teleport {
        private final Item item;
        private final Container container;
        private final int slotId;
        private final Location destination;

        @Override
        public void teleport(final Player player) {
            final RingOfReturning ring = RingOfReturning.get(item);
            if (ring.getCharges() <= 0) {
                player.sendMessage("Your ring hasn't got any charges left.");
                return;
            }
            Teleport.super.teleport(player);
        }

        @Override
        public TeleportType getType() {
            return TeleportType.RING_OF_RETURNING_TELEPORT;
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
            final RingOfReturningItem.RingOfReturning ring = RingOfReturning.get(item);
            final int charges = ring.getCharges();
            final String message = charges == 1 ? "<col=7F00FF>Your ring of returning crumbles to dust.</col>" : charges == 2 ? "<col=7F00FF>Your ring of returning has one use left.</col>" : "<col=7F00FF>Your ring of returning has " + StringFormatUtil.format(charges - 1) + " uses left.</col>";
            player.sendMessage(message);
            final Item result = ring.getResult();
            container.set(slotId, result);
            container.refresh(player);
        }

        public RingTeleport(Item item, Container container, int slotId, Location destination) {
            this.item = item;
            this.container = container;
            this.slotId = slotId;
            this.destination = destination;
        }
    }
}
