package com.zenyte.plugins.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 14/06/2019 10:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SilverSickle extends ItemPlugin {
    private static final Animation bloomAnimation = new Animation(1100);
    private static final Graphics bloomGraphics = new Graphics(263);

    @Override
    public void handle() {
        bind("Bloom", (player, item, container, slotId) -> bloom(player));
        bind("Cast Bloom", (player, item, container, slotId) -> bloom(player));
    }

    private void bloom(final Player player) {
        if (player.getPrayerManager().getPrayerPoints() <= 0) {
            player.sendMessage("You don't have enough prayer points to do this.");
            return;
        }
        player.getPrayerManager().drainPrayerPoints(Utils.random(1, 6));
        player.lock(1);
        player.setAnimation(bloomAnimation);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) {
                    continue;
                }
                final Location tile = player.getLocation().transform(x, y, 0);
                final WorldObject object = World.getObjectWithId(tile, 3508);
                if (object == null) {
                    continue;
                }
                if (Utils.random(1) == 0) {
                    final WorldObject spawnedObj = new WorldObject(3509, object.getType(), object.getRotation(), tile);
                    World.spawnObject(spawnedObj);
                    WorldTasksManager.schedule(() -> {
                        if (World.getObjectWithType(tile, 10) == spawnedObj) {
                            World.spawnObject(object);
                        }
                    }, 100);
                }
                World.sendGraphics(bloomGraphics, tile);
            }
        }
    }

    @Override
    public int[] getItems() {
        return new int[] {2963, ItemId.IVANDIS_FLAIL, ItemId.BLISTERWOOD_FLAIL};
    }
}
