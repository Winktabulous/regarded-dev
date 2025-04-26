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
 * @author Kris | 25/03/2019 18:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CombatBracelet extends ItemPlugin {
    @Override
    public void handle() {
        bind("Warriors' Guild", (player, item, container, slotId) -> new BraceletTeleport(item, container, slotId, new Location(2882, 3547, 0)).teleport(player));
        bind("Champions' Guild", (player, item, container, slotId) -> new BraceletTeleport(item, container, slotId, new Location(3192, 3367, 0)).teleport(player));
        bind("Monastery", (player, item, container, slotId) -> new BraceletTeleport(item, container, slotId, new Location(3052, 3487, 0)).teleport(player));
        bind("Ranging Guild", (player, item, container, slotId) -> new BraceletTeleport(item, container, slotId, new Location(2655, 3443, 0)).teleport(player));
        bind("Rub", (player, item, container, slotId) -> {
            player.sendMessage("You rub the amulet...");
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Where would you like to teleport to?", new DialogueOption("Warriors' Guild", () -> new BraceletTeleport(item, container, slotId, new Location(2882, 3547, 0)).teleport(player)), new DialogueOption("Champions' Guild", () -> new BraceletTeleport(item, container, slotId, new Location(3192, 3367, 0)).teleport(player)), new DialogueOption("Monastery", () -> new BraceletTeleport(item, container, slotId, new Location(3052, 3487, 0)).teleport(player)), new DialogueOption("Ranging Guild", () -> new BraceletTeleport(item, container, slotId, new Location(2655, 3443, 0)).teleport(player)), new DialogueOption("Nowhere"));
                }
            });
        });
    }

    @Override
    public int[] getItems() {
        return CombatBraceletItem.bracelets.keySet().toIntArray();
    }


    private enum CombatBraceletItem {
        ONE(new Item(11124), new Item(11126), 1), TWO(new Item(11122), new Item(11124), 2), THREE(new Item(11120), new Item(11122), 3), FOUR(new Item(11118), new Item(11120), 4), FIVE(new Item(11974), new Item(11118), 5), SIX(new Item(11972), new Item(11974), 6);
        public static final Int2ObjectMap<CombatBraceletItem> bracelets = new Int2ObjectOpenHashMap<>();

        static {
            for (final CombatBracelet.CombatBraceletItem value : values()) {
                bracelets.put(value.item.getId(), value);
            }
        }

        private final Item item;
        private final Item result;
        private final int charges;

        private static final CombatBraceletItem get(@NotNull final Item item) {
            return Objects.requireNonNull(bracelets.get(item.getId()));
        }

        CombatBraceletItem(Item item, Item result, int charges) {
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


    private static final class BraceletTeleport implements Teleport {
        private final Item item;
        private final Container container;
        private final int slotId;
        private final Location destination;

        @Override
        public void teleport(final Player player) {
            final CombatBraceletItem glory = CombatBraceletItem.get(item);
            if (glory.getCharges() <= 0) {
                player.sendMessage("You will need to recharge your combat bracelet before you can use it again.");
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

        public BraceletTeleport(Item item, Container container, int slotId, Location destination) {
            this.item = item;
            this.container = container;
            this.slotId = slotId;
            this.destination = destination;
        }

        @Override
        public void onArrival(final Player player) {
            final CombatBracelet.CombatBraceletItem bracelet = CombatBraceletItem.get(item);
            final int charges = bracelet.getCharges();
            final String message = charges == 1 ? "<col=7F00FF>You use your combat bracelet's last charge.</col>" : charges == 2 ? "<col=7F00FF>Your combat bracelet has one charge left.</col>" : "<col=7F00FF>Your combat bracelet has " + StringFormatUtil.format(charges - 1) + " charges left.</col>";
            player.sendMessage(message);
            final Item result = bracelet.getResult();
            container.set(slotId, result);
            container.refresh(player);
        }
    }
}
