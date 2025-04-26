package com.zenyte.plugins.item;

import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
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

/**
 * @author Tommeh | 1 okt. 2018 | 00:05:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>
 */
public class DigsitePendantItem extends ItemPlugin {
    private static final Location digsiteLocation = new Location(3340, 3445, 0);

    @Override
    public void handle() {
        bind("Rub", (player, item, container, slotId) -> {
            player.sendMessage("You rub the amulet...");
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Where would you like to teleport to?", new DialogueOption("Digsite", () -> new PendantTeleport(item, container, slotId, digsiteLocation).teleport(player)), new DialogueOption("Fossil Island", () -> new PendantTeleport(item, container, slotId, new Location(3763, 3870, 1)).teleport(player)), new DialogueOption("Lithkren Dungeon", () -> new PendantTeleport(item, container, slotId, new Location(3547, 10456, 0)).teleport(player)), new DialogueOption("Nowhere"));
                }
            });
        });
        bind("Digsite", (player, item, container, slotId) -> new PendantTeleport(item, container, slotId, new Location(3340, 3445, 0)).teleport(player));
        bind("Fossil Island", (player, item, container, slotId) -> new PendantTeleport(item, container, slotId, new Location(3763, 3870, 1)).teleport(player));
        bind("Lithkren Dungeon", (player, item, container, slotId) -> new PendantTeleport(item, container, slotId, new Location(3547, 10456, 0)).teleport(player));
    }

    @Override
    public int[] getItems() {
        return Pendant.pendants.keySet().toIntArray();
    }


    private enum Pendant {
        ONE(new Item(11190), null, 1), TWO(new Item(11191), new Item(11190), 2), THREE(new Item(11192), new Item(11191), 3), FOUR(new Item(11193), new Item(11192), 4), FIVE(new Item(11194), new Item(11193), 5);
        public static final Int2ObjectMap<Pendant> pendants = new Int2ObjectOpenHashMap<>();

        static {
            for (final DigsitePendantItem.Pendant amulet : values()) {
                pendants.put(amulet.getItem().getId(), amulet);
            }
        }

        private final Item item;
        private final Item result;
        private final int charges;

        private static final Pendant get(@NotNull final Item item) {
            return pendants.get(item.getId());
        }

        Pendant(Item item, Item result, int charges) {
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


    private static final class PendantTeleport implements Teleport {
        private final Item item;
        private final Container container;
        private final int slotId;
        private final Location destination;

        @Override
        public void teleport(final Player player) {
            final Pendant pendant = Pendant.get(item);
            if (pendant.getCharges() <= 0) {
                player.sendMessage("Your pendant hasn't got any charges left.");
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

        public PendantTeleport(Item item, Container container, int slotId, Location destination) {
            this.item = item;
            this.container = container;
            this.slotId = slotId;
            this.destination = destination;
        }

        @Override
        public void onArrival(final Player player) {
            final DigsitePendantItem.Pendant pendant = Pendant.get(item);
            final int charges = pendant.getCharges();
            final String message = charges == 1 ? "<col=7F00FF>You use your pendant's last charge.</col>" : charges == 2 ? "<col=7F00FF>Your pendant has one charge left.</col>" : "<col=7F00FF>Your pendant has " + StringFormatUtil.format(charges - 1) + " charges left.</col>";
            player.sendMessage(message);
            final Item result = pendant.getResult();
            container.set(slotId, result);
            container.refresh(player);
            if (destination.equals(digsiteLocation)) {
                player.getAchievementDiaries().update(VarrockDiary.TELEPORT_TO_DIGSITE);
            }
        }
    }
}
