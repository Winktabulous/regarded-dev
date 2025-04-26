package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.NotificationSettings;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Kris | 26. aug 2018 : 16:37:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class SlayerRingItem extends ItemPlugin {
    @Override
    public void handle() {
        bind("Rub", this::teleport);
        bind("Teleport", this::teleport);
        bind("Check", (player, item, slotId) -> player.getSlayer().sendTaskInformation());
        bind("Partner", (player, item, slotId) -> {
            player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 68);
            player.getSlayer().refreshPartnerInterface();
        });
        bind("Log", (player, item, slotId) -> player.getNotificationSettings().sendKillLog(NotificationSettings.SLAYER_NPC_NAMES, true));
    }

    private final void teleport(@NotNull final Player player, @NotNull final Item item, @NotNull final Container container, final int slotId) {
        player.sendMessage("You rub the amulet...");
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Where would you like to teleport to?", new DialogueOption("Slayer tower", () -> new RingTeleport(item, container, slotId, new Location(3429, 3538, 0)).teleport(player)), new DialogueOption("Fremennik Slayer Dungeon", () -> new RingTeleport(item, container, slotId, new Location(2807, 10002, 0)).teleport(player)), new DialogueOption("Tarn's Lair", () -> new RingTeleport(item, container, slotId, new Location(3185, 4601, 0)).teleport(player)), new DialogueOption("Stronghold Slayer Cave", () -> new RingTeleport(item, container, slotId, new Location(2431, 3424, 0)).teleport(player)), new DialogueOption("Dark Beasts", () -> new RingTeleport(item, container, slotId, new Location(2027, 4637, 0)).teleport(player)));
            }
        });
    }

    @Override
    public int[] getItems() {
        return SlayerRing.values.keySet().toIntArray();
    }


    private enum SlayerRing {
        ONE(new Item(11873), new Item(ItemId.ENCHANTED_GEM), 1), TWO(new Item(11872), new Item(11873), 2), THREE(new Item(11871), new Item(11872), 3), FOUR(new Item(11870), new Item(11871), 4), FIVE(new Item(11869), new Item(11870), 5), SIX(new Item(11868), new Item(11869), 6), SEVEN(new Item(11867), new Item(11868), 7), EIGHT(new Item(11866), new Item(11867), 8), ETERNAL(new Item(21268), null, -1);
        private static final Int2ObjectMap<SlayerRing> values = new Int2ObjectOpenHashMap<>();

        static {
            for (final SlayerRingItem.SlayerRing value : values()) {
                values.put(value.item.getId(), value);
            }
        }

        private final Item item;
        private final Item result;
        private final int charges;

        SlayerRing(Item item, Item result, int charges) {
            this.item = item;
            this.result = result;
            this.charges = charges;
        }

        @NotNull
        private static final SlayerRing get(@NotNull final Item item) {
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
        public TeleportType getType() {
            return TeleportType.REGULAR_TELEPORT;
        }

        @Override
        public void teleport(final Player player) {
            final SlayerRing ring = SlayerRing.get(item);
            if (ring != SlayerRing.ETERNAL && ring.getCharges() <= 0) {
                player.sendMessage("Your ring hasn't got any charges left.");
                return;
            }
            Teleport.super.teleport(player);
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
            return 30;
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
            final SlayerRingItem.SlayerRing ring = SlayerRing.get(item);
            final int charges = ring.getCharges();
            final String message = charges == 1 ? "<col=7F00FF>Your slayer ring crumbles to dust.</col>" : charges == 2 ? "<col=7F00FF>Your slayer ring has one charge left..</col>" : ring == SlayerRing.ETERNAL ? "<col=7F00FF>No charges were used since the ring holds unlimited charges." : "<col=7F00FF>Your slayer ring has " + StringFormatUtil.format(charges - 1) + " charges left.</col>";
            player.sendMessage(message);
            if (ring == SlayerRing.ETERNAL) {
                return;
            }
            final Item result = ring.getResult();
            container.set(slotId, result);
            container.refresh(player);
        }
    }
}
