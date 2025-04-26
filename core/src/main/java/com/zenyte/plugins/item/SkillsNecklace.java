package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Kris | 25/03/2019 18:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SkillsNecklace extends ItemPlugin {
    @Override
    public void handle() {
        bind("Fishing Guild", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(2613, 3389, 0)).teleport(player));
        bind("Mining Guild", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(3048, 9763, 0)).teleport(player));
        bind("Crafting Guild", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(2935, 3295, 0)).teleport(player));
        bind("Cooking Guild", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(3143, 3439, 0)).teleport(player));
        bind("Woodcutting Guild", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, new Location(1661, 3505, 0)).teleport(player));
        bind("Farming Guild", (player, item, container, slotId) -> new NecklaceTeleport(item, container, slotId, player.getSkills().getLevelForXp(SkillConstants.FARMING) >= 45 ? new Location(1248, 3725, 0) : new Location(1248, 3719, 0)).teleport(player));
        bind("Rub", (player, item, container, slotId) -> player.getDialogueManager().start(new OptionsMenuD(player, "Where would you like to teleport to?", "Fishing Guild", "Mining Guild", "Crafting Guild", "Cooking Guild", "Woodcutting Guild", "Farming Guild") {
            @Override
            public void handleClick(int clickedSlot) {
                if (container.get(slotId) != item) {
                    return;
                }
                switch (clickedSlot) {
                case 0: 
                    new NecklaceTeleport(item, container, slotId, new Location(2613, 3389, 0)).teleport(player);
                    break;
                case 1: 
                    new NecklaceTeleport(item, container, slotId, new Location(3048, 9763, 0)).teleport(player);
                    break;
                case 2: 
                    new NecklaceTeleport(item, container, slotId, new Location(2935, 3295, 0)).teleport(player);
                    break;
                case 3: 
                    new NecklaceTeleport(item, container, slotId, new Location(3143, 3439, 0)).teleport(player);
                    break;
                case 4: 
                    new NecklaceTeleport(item, container, slotId, new Location(1661, 3505, 0)).teleport(player);
                    break;
                case 5: 
                    new NecklaceTeleport(item, container, slotId, player.getSkills().getLevelForXp(SkillConstants.FARMING) >= 45 ? new Location(1248, 3725, 0) : new Location(1248, 3719, 0)).teleport(player);
                    break;
                }
            }
            @Override
            public boolean cancelOption() {
                return false;
            }
        }));
    }

    @Override
    public int[] getItems() {
        return SkillsNecklaceItem.amulets.keySet().toIntArray();
    }


    private enum SkillsNecklaceItem {
        ONE(new Item(11111), new Item(11113), 1), TWO(new Item(11109), new Item(11111), 2), THREE(new Item(11107), new Item(11109), 3), FOUR(new Item(11105), new Item(11107), 4), FIVE(new Item(11970), new Item(11105), 5), SIX(new Item(11968), new Item(11970), 6);
        public static final Int2ObjectMap<SkillsNecklaceItem> amulets = new Int2ObjectOpenHashMap<>();

        static {
            for (final SkillsNecklace.SkillsNecklaceItem value : values()) {
                amulets.put(value.item.getId(), value);
            }
        }

        private final Item item;
        private final Item result;
        private final int charges;

        private static final SkillsNecklaceItem get(@NotNull final Item item) {
            return Objects.requireNonNull(amulets.get(item.getId()));
        }

        SkillsNecklaceItem(Item item, Item result, int charges) {
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
            final SkillsNecklaceItem glory = SkillsNecklaceItem.get(item);
            if (glory.getCharges() <= 0) {
                player.sendMessage("You will need to recharge your skills necklace before you can use it again.");
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

        public NecklaceTeleport(Item item, Container container, int slotId, Location destination) {
            this.item = item;
            this.container = container;
            this.slotId = slotId;
            this.destination = destination;
        }

        @Override
        public void onArrival(final Player player) {
            final SkillsNecklace.SkillsNecklaceItem necklace = SkillsNecklaceItem.get(item);
            final int charges = necklace.getCharges();
            final String message = charges == 1 ? "<col=7F00FF>You use your skills necklace's last charge.</col>" : charges == 2 ? "<col=7F00FF>Your skills necklace has one charge left.</col>" : "<col=7F00FF>Your skills necklace has " + StringFormatUtil.format(charges - 1) + " charges left.</col>";
            player.sendMessage(message);
            final Item result = necklace.getResult();
            container.set(slotId, result);
            container.refresh(player);
        }
    }
}
