package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.DialogueManager;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Kris | 24/03/2019 12:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BurningAmulet extends ItemPlugin {
    private static final ImmutableLocation CHAOS_TEMPLE = new ImmutableLocation(3235, 3637, 0);
    private static final ImmutableLocation BANDIT_CAVE = new ImmutableLocation(3038, 3651, 0);
    private static final ImmutableLocation LAVA_MAZE = new ImmutableLocation(3027, 3839, 0);

    @Override
    public void handle() {
        bind("Rub", (player, item, container, slotId) -> {
            player.sendMessage("You rub the amulet...");
            final DialogueManager dialogueManager = player.getDialogueManager();
            dialogueManager.start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Where would you like to teleport to?", new DialogueOption("Chaos Temple", () -> dialogueManager.start(new BurningAmuletWarningDialogue(player, item, container, slotId, CHAOS_TEMPLE))), new DialogueOption("Bandit Camp", () -> dialogueManager.start(new BurningAmuletWarningDialogue(player, item, container, slotId, BANDIT_CAVE))), new DialogueOption("Lava Maze", () -> dialogueManager.start(new BurningAmuletWarningDialogue(player, item, container, slotId, LAVA_MAZE))), new DialogueOption("Nowhere"));
                }
            });
        });
        bind("Chaos Temple", (player, item, container, slotId) -> player.getDialogueManager().start(new BurningAmuletWarningDialogue(player, item, container, slotId, CHAOS_TEMPLE)));
        bind("Bandit Camp", (player, item, container, slotId) -> player.getDialogueManager().start(new BurningAmuletWarningDialogue(player, item, container, slotId, BANDIT_CAVE)));
        bind("Lava Maze", (player, item, container, slotId) -> player.getDialogueManager().start(new BurningAmuletWarningDialogue(player, item, container, slotId, LAVA_MAZE)));
    }

    @Override
    public int[] getItems() {
        return BurningAmuletItems.amulets.keySet().toIntArray();
    }


    private class BurningAmuletWarningDialogue extends Dialogue {
        private final Item item;
        private final Container container;
        private final int slotId;
        private final Location destination;
        private final int wildernessLevel;

        public BurningAmuletWarningDialogue(final Player player, final Item item, final Container container, final int slotId, final Location destination) {
            super(player);
            this.item = item;
            this.container = container;
            this.slotId = slotId;
            this.destination = destination;
            this.wildernessLevel = WildernessArea.getWildernessLevel(destination).getAsInt();
        }

        @Override
        public void buildDialogue() {
            final String wildyLevelString = "level " + wildernessLevel + " Wilderness.";
            options("That's in " + wildyLevelString, new DialogueOption("Okay, teleport to " + wildyLevelString, () -> new BurningAmuletTeleport(item, container, slotId, destination).teleport(player)), new DialogueOption("No."));
        }
    }


    private enum BurningAmuletItems {
        ONE(new Item(21175), null, 1), TWO(new Item(21173), new Item(21175), 2), THREE(new Item(21171), new Item(21173), 3), FOUR(new Item(21169), new Item(21171), 4), FIVE(new Item(21166), new Item(21169), 5);
        public static final Int2ObjectMap<BurningAmuletItems> amulets = new Int2ObjectOpenHashMap<>();

        static {
            for (final BurningAmulet.BurningAmuletItems value : values()) {
                amulets.put(value.item.getId(), value);
            }
        }

        private final Item item;
        private final Item result;
        private final int charges;

        private static final BurningAmuletItems get(@NotNull final Item item) {
            return Objects.requireNonNull(amulets.get(item.getId()));
        }

        BurningAmuletItems(Item item, Item result, int charges) {
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


    private static final class BurningAmuletTeleport implements Teleport {
        private final Item item;
        private final Container container;
        private final int slotId;
        private final Location destination;

        @Override
        public void teleport(final Player player) {
            final BurningAmuletItems glory = BurningAmuletItems.get(item);
            if (glory.getCharges() <= 0) {
                player.sendMessage("Your amulet hasn't got any charges left.");
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

        public BurningAmuletTeleport(Item item, Container container, int slotId, Location destination) {
            this.item = item;
            this.container = container;
            this.slotId = slotId;
            this.destination = destination;
        }

        @Override
        public void onArrival(final Player player) {
            final BurningAmulet.BurningAmuletItems amulet = BurningAmuletItems.get(item);
            final int charges = amulet.getCharges();
            final String message = charges == 1 ? "<col=7F00FF>You use your amulet's last charge.</col>" : charges == 2 ? "<col=7F00FF>Your amulet has one charge left.</col>" : "<col=7F00FF>Your amulet has " + StringFormatUtil.format(charges - 1) + " charges left.</col>";
            player.sendMessage(message);
            final Item result = amulet.getResult();
            container.set(slotId, result);
            container.refresh(player);
        }
    }
}
