package com.zenyte.game.world.entity.player.action.misc;

import com.zenyte.game.content.flowerpoker.FlowerPokerManager;
import com.zenyte.game.content.flowerpoker.FlowerPokerSession;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.LayableObjectPlugin;
import com.zenyte.plugins.dialogue.OptionDialogue;
import com.zenyte.utils.TimeUnit;

import static com.zenyte.game.world.entity.player.dialogue.Dialogue.TITLE;

/**
 * @author Tommeh | 28-4-2019 | 14:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MithrilSeedPlanting extends Action {

    private static final Item MITHRIL_SEEDS = new Item(299);
    private static final Animation PICKUP_ANIM = new Animation(827);
    private final Flowers flowers;

    @Override
    public boolean start() {
        // Quick checks to see if this event can run
        if (player.getBooleanTemporaryAttribute("gambling")) return true;
        if (player.getNumericTemporaryAttribute("mithril seed delay").longValue() > System.currentTimeMillis()) return false;

        var location = new Location(player.getLocation());
        if (isValidMovementTile(location) || (player.getDuel() != null && player.getDuel().inDuel())) {
            player.sendMessage("You can't plant mithril seeds here.");
            return false;
        }
        final RegionArea area = player.getArea();
        if (area instanceof LayableObjectPlugin lop)
            if (!lop.canLay(player, LayableObjectPlugin.LayableObjectType.MITHRIL_SEED))
                return false;
        player.lock(4);
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        if (player.getBooleanTemporaryAttribute("gambling")) {
            FlowerPokerSession session = FlowerPokerManager.get(player).session;
            session.planting.plantAndWalk(player, session.arena.getDirection());
            return -1;
        }
        if (player.getNumericTemporaryAttribute("mithril seed delay").longValue() > System.currentTimeMillis()) return -1;
        var location = new Location(player.getLocation());
        if (isValidMovementTile(location) || (player.getDuel() != null && player.getDuel().inDuel())) {
            player.sendMessage("You can't plant mithril seeds here.");
            return -1;
        }
        final WorldObject object = new WorldObject(flowers.getObject(), 10, 0, location);
        if (!player.getInventory().deleteItem(MITHRIL_SEEDS).isFailure()) {
            player.setRouteEvent(new PlantTileEvent(player, object));
            player.getTemporaryAttributes().put("mithril seed delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(4));
            player.log(LogLevel.INFO, "Planting mithril seed: " + flowers + " at " + location + ".");
            player.sendFilteredMessage("You open the small mithril case.");
            player.sendFilteredMessage("You drop a seed by your feet.");
            World.spawnTemporaryObject(object, 100);
        }
        return -1;
    }

    private boolean isValidMovementTile(Location location) {
        return !World.isTileFree(location, 0) ||
            World.getObjectWithType(location, 10) != null ||
            World.getObjectWithType(location, 11) != null;
    }

    public enum Flowers {

        RED(2462, 2981, 148),
        BLUE(2464, 2982, 148),
        YELLOW(2466, 2983, 148),
        PURPLE(2468, 2984, 148),
        ORANGE(2470, 2985, 148),
        MIXED(2460, 2980, 148),
        ASSORTED(2472, 2986, 110),
        BLACK(2476, 2988, 2),
        WHITE(2474, 2987, 1);

        private final int item;
        private final int object;
        private final int chance;
        public static final Flowers[] all = values();

        public static Flowers random() {
            final int random = Utils.secureRandom(1001);
            int weight = 0;
            for (final MithrilSeedPlanting.Flowers value : all) {
                if ((weight += value.chance) >= random) {
                    return value;
                }
            }
            throw new IllegalStateException();
        }

        Flowers(int item, int object, int chance) {
            this.item = item;
            this.object = object;
            this.chance = chance;
        }

        public int getItem() {
            return item;
        }

        public int getObject() {
            return object;
        }

        public int getChance() {
            return chance;
        }
    }

    public MithrilSeedPlanting(Flowers flowers) {
        this.flowers = flowers;
    }

    class PlantTileEvent extends TileEvent {

        public PlantTileEvent(Player player, WorldObject object) {
            super(player, new TileStrategy(player.getLocation(), 1), () -> {
                player.faceObject(object);
                player.getDialogueManager().start(
                    new OptionDialogue(player, TITLE,
                        new String[] {
                        "Pick the flowers.", "Leave the flowers."
                    },
                        new Runnable[] {
                            () -> {
                                player.setAnimation(PICKUP_ANIM);
                                player.getInventory().addItem(flowers.getItem(), 1);
                                World.removeObject(object);
                            }
                    })
                );
            });
        }

        @Override
        protected boolean cannotMove(Entity entity) {
            return false;
        }
    }
}
