package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.impl.slayer.Mogre;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OminousFishingSpot implements ObjectAction, ItemOnObjectAction {

    /**
     * Contains the tiles where the Mogres spawn, using object ids of the fishing spots as a way of keeping order of
     * which is which.
     */
    private static final Location[] spawnTiles = new Location[] { new Location(2982, 3110, 0), new Location(2993, 3107, 0), new Location(3005, 3119, 0) };

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.sendMessage("I don\'t think getting this close to the fishing spot is a good idea...");
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (!player.getInventory().containsItem(6664, 1)) {
            player.sendMessage("You need a fishing explosive to do this.");
            return;
        }
        player.getActionManager().setAction(new OminousFishingSpotObjectFollowAction(object));
    }

    @Override
    public void handle(final Player player, final Item item, int slot, final WorldObject object) {
        player.stopAll();
        player.faceObject(object);
        handleItemOnObjectAction(player, item, slot, object);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 6664 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.OMINOUS_FISHING_SPOT, ObjectId.OMINOUS_FISHING_SPOT_10088, ObjectId.OMINOUS_FISHING_SPOT_10089 };
    }

    private static final class OminousFishingSpotObjectFollowAction extends Action {

        private static final Animation animation = new Animation(2779);

        private static final Graphics throwingGraphics = new Graphics(50, 0, 96);

        private static final Projectile throwingProjectile = new Projectile(49, 34, 10, 20, 25, 0, 11, 3);

        public OminousFishingSpotObjectFollowAction(final WorldObject object) {
            this.object = object;
        }

        private final WorldObject object;

        @Override
        public boolean start() {
            return true;
        }

        @Override
        public boolean process() {
            return true;
        }

        private static final SoundEffect explosionSound = new SoundEffect(1487, 10, 0);

        @Override
        public int processWithDelay() {
            final double distance = player.getLocation().getDistance(object);
            if (distance >= 11 || player.isProjectileClipped(OminousFishingSpot.spawnTiles[object.getId() - 10087], false)) {
                if (!player.hasWalkSteps()) {
                    player.calcFollow(object, -1, true, true, false);
                    if (!player.hasWalkSteps()) {
                        player.sendMessage("I can\'t reach that.");
                        return -1;
                    }
                }
                return 0;
            }
            if (player.getTemporaryAttributes().containsKey("Is mogre spawned")) {
                player.sendMessage("You can only have one mogre spawned at once.");
                return -1;
            }
            player.getTemporaryAttributes().put("Is mogre spawned", true);
            player.stopAll();
            player.setAnimation(animation);
            player.setGraphics(throwingGraphics);
            player.sendSound(2708);
            player.getInventory().deleteItem(new Item(6664, 1));
            final int delay = World.sendProjectile(player.getLocation(), object, throwingProjectile);
            WorldTasksManager.schedule(() -> {
                World.sendSoundEffect(object, explosionSound);
                final Mogre mogre = new Mogre(2592, OminousFishingSpot.spawnTiles[object.getId() - 10087], Direction.NORTH, 5);
                mogre.spawn();
                mogre.getCombat().setTarget(player);
                mogre.setUsername(player.getUsername());
            }, delay + 1);
            return -1;
        }
    }
}
