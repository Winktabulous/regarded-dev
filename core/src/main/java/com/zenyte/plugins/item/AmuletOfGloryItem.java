package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Kris | 26. aug 2018 : 16:09:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class AmuletOfGloryItem extends ItemPlugin {
    @Override
    public void handle() {
        bind("Rub", (player, item, container, slotId) -> {
            player.sendMessage("You rub the amulet...");
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Where would you like to teleport to?", new DialogueOption("Edgeville", () -> new GloryTeleport(item, container, slotId, new Location(3087, 3489, 0)).teleport(player)), new DialogueOption("Karamja", () -> new GloryTeleport(item, container, slotId, new Location(2918, 3176, 0)).teleport(player)), new DialogueOption("Draynor Village", () -> new GloryTeleport(item, container, slotId, new Location(3105, 3251, 0)).teleport(player)), new DialogueOption("Al Kharid", () -> new GloryTeleport(item, container, slotId, new Location(3293, 3163, 0)).teleport(player)), new DialogueOption("Nowhere"));
                }
            });
        });
        bind("Edgeville", (player, item, container, slotId) -> new GloryTeleport(item, container, slotId, new Location(3087, 3489, 0)).teleport(player));
        bind("Karamja", (player, item, container, slotId) -> new GloryTeleport(item, container, slotId, new Location(2918, 3176, 0)).teleport(player));
        bind("Draynor Village", (player, item, container, slotId) -> new GloryTeleport(item, container, slotId, new Location(3105, 3251, 0)).teleport(player));
        bind("Al Kharid", (player, item, container, slotId) -> new GloryTeleport(item, container, slotId, new Location(3293, 3163, 0)).teleport(player));
    }

    @Override
    public int[] getItems() {
        return AmuletOfGlory.amulets.keySet().toIntArray();
    }


    private enum AmuletOfGlory {
        ONE(new Item(1706), new Item(1704), 1), TWO(new Item(1708), new Item(1706), 2), THREE(new Item(1710), new Item(1708), 3), FOUR(new Item(1712), new Item(1710), 4), FIVE(new Item(11976), new Item(1712), 5), SIX(new Item(11978), new Item(11976), 6), ETERNAL(new Item(19707), null, -1), ONE_TRIM(new Item(10360), new Item(10362), 1), TWO_TRIM(new Item(10358), new Item(10360), 2), THREE_TRIM(new Item(10356), new Item(10358), 3), FOUR_TRIM(new Item(10354), new Item(10356), 4), FIVE_TRIM(new Item(11966), new Item(10354), 5), SIX_TRIM(new Item(11964), new Item(11966), 6);
        public static final Int2ObjectMap<AmuletOfGlory> amulets = new Int2ObjectOpenHashMap<>();

        static {
            for (final AmuletOfGloryItem.AmuletOfGlory value : values()) {
                amulets.put(value.item.getId(), value);
            }
        }

        private final Item item;
        private final Item result;
        private final int charges;

        private static final AmuletOfGlory get(@NotNull final Item item) {
            return Objects.requireNonNull(amulets.get(item.getId()));
        }

        AmuletOfGlory(Item item, Item result, int charges) {
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


    private static final class GloryTeleport implements Teleport {
        private final Item item;
        private final Container container;
        private final int slotId;
        private final Location destination;

        @Override
        public void teleport(final Player player) {
            final AmuletOfGlory glory = AmuletOfGlory.get(item);
            if (glory != AmuletOfGlory.ETERNAL && glory.getCharges() <= 0) {
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
            return 30;
        }

        @Override
        public boolean isCombatRestricted() {
            return UNRESTRICTED;
        }

        public GloryTeleport(Item item, Container container, int slotId, Location destination) {
            this.item = item;
            this.container = container;
            this.slotId = slotId;
            this.destination = destination;
        }

        @Override
        public void onArrival(final Player player) {
            if (player.getCombatAchievements().hasTierCompleted(CATierType.MASTER) && Utils.random(19) == 0) {
                player.sendFilteredMessage("As you've completed the master combat achievements, you have been prevented from losing a charge.");
                return;
            }
            final AmuletOfGloryItem.AmuletOfGlory glory = AmuletOfGlory.get(item);
            if (glory == AmuletOfGlory.ETERNAL) {
                return;
            }
            final int charges = glory.getCharges();
            final String message = charges == 1 ? "<col=7F00FF>You use your amulet's last charge.</col>" : charges == 2 ? "<col=7F00FF>Your amulet has one charge left.</col>" : item.getId() != 19707 ? "<col=7F00FF>Your amulet has " + StringFormatUtil.format(charges - 1) + " charges left.</col>" : "<col=7F00FF>No charges were used since the amulet holds unlimited charges.";
            player.sendMessage(message);
            final Item result = glory.getResult();
            container.set(slotId, new Item(result));
            container.refresh(player);
        }
    }
}
