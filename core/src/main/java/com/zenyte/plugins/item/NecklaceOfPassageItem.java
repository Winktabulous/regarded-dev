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
 * @author Kris | 26. aug 2018 : 16:41:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class NecklaceOfPassageItem extends ItemPlugin {
    @Override
    public void handle() {
        bind("Rub", (player, item, container, slotId) -> {
            player.sendMessage("You rub the amulet...");
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Where would you like to teleport to?", new DialogueOption("Wizards' Tower", () -> new NecklaceTeleport(item, container, slotId, new Location(3113, 3177, 0)).teleport(player)), new DialogueOption("The Outpost", () -> new NecklaceTeleport(item, container, slotId, new Location(2428, 3349, 0)).teleport(player)), new DialogueOption("Eagles' Eyrie", () -> new NecklaceTeleport(item, container, slotId, new Location(3406, 3157, 0)).teleport(player)), new DialogueOption("Nowhere"));
                }
            });
        });
        bind("Wizards' Tower", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(3113, 3177, 0)).teleport(player));
        bind("The Outpost", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(2428, 3349, 0)).teleport(player));
        bind("Eagles' Eyrie", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(3406, 3157, 0)).teleport(player));
    }

    @Override
    public int[] getItems() {
        return NecklaceOfPassage.necklaces.keySet().toIntArray();
    }


    private enum NecklaceOfPassage {
        ONE(new Item(21155), null, 1), TWO(new Item(21153), new Item(21155), 2), THREE(new Item(21151), new Item(21153), 3), FOUR(new Item(21149), new Item(21151), 4), FIVE(new Item(21146), new Item(21149), 5);
        public static final Int2ObjectMap<NecklaceOfPassage> necklaces = new Int2ObjectOpenHashMap<>();

        static {
            for (final NecklaceOfPassageItem.NecklaceOfPassage value : values()) {
                necklaces.put(value.item.getId(), value);
            }
        }

        private final Item item;
        private final Item result;
        private final int charges;

        @NotNull
        private static final NecklaceOfPassage get(@NotNull final Item item) {
            return Objects.requireNonNull(necklaces.get(item.getId()));
        }

        NecklaceOfPassage(Item item, Item result, int charges) {
            this.item = item;
            this.result = result;
            this.charges = charges;
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
            final NecklaceOfPassage necklace = NecklaceOfPassage.get(item);
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

        @Override
        public void onArrival(final Player player) {
            final NecklaceOfPassageItem.NecklaceOfPassage necklace = NecklaceOfPassage.get(item);
            final int charges = necklace.getCharges();
            final String message = charges == 1 ? "<col=7F00FF>Your necklace of passage crumbles to dust.</col>" : charges == 2 ? "<col=7F00FF>Your necklace of passage has one use left.</col>" : "<col=7F00FF>Your necklace of passage has " + StringFormatUtil.format(charges - 1) + " uses left.</col>";
            player.sendMessage(message);
            final Item result = necklace.getResult();
            container.set(slotId, result);
            container.refresh(player);
        }

        public NecklaceTeleport(Item item, Container container, int slotId, Location destination) {
            this.item = item;
            this.container = container;
            this.slotId = slotId;
            this.destination = destination;
        }
    }
}
