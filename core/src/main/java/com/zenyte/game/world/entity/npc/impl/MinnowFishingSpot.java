package com.zenyte.game.world.entity.npc.impl;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.skills.fishing.Fishing;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.CharacterLoop;
import mgi.utilities.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

public class MinnowFishingSpot extends FishingSpot implements Spawnable {
    private static final Graphics sharkGraphics = new Graphics(1387);
    private static final Location[] westernPath = new Location[] {new Location(2612, 3443, 0), new Location(2611, 3443, 0), new Location(2610, 3443, 0), new Location(2609, 3443, 0), new Location(2609, 3444, 0), new Location(2610, 3444, 0), new Location(2611, 3444, 0), new Location(2612, 3444, 0)};
    private static final Location[] easternPath = new Location[] {new Location(2620, 3443, 0), new Location(2619, 3443, 0), new Location(2618, 3443, 0), new Location(2617, 3443, 0), new Location(2617, 3444, 0), new Location(2618, 3444, 0), new Location(2619, 3444, 0), new Location(2620, 3444, 0)};

    private static final Location[] UZONE = new Location[] {new Location(3391, 7793, 0), new Location(3391, 7792, 0), new Location(3391, 7791, 0), new Location(3391, 7790)};
    private static final Location[] UZONE2 = new Location[] {new Location(3395, 7793, 0), new Location(3395, 7792, 0), new Location(3395, 7791, 0), new Location(3395, 7790)};
    private int ticks;
    private final Location[] path;
    private int pathIndex;
    private boolean shark;

    public MinnowFishingSpot(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        if (tile == null) {
            path = null;
            return;
        }
        setRadius(0);
        ticks = 25;
        boolean dZone = tile.getY() > 7700;
        Location[] path = dZone ? UZONE : westernPath;
        Location matching = CollectionUtils.findMatching(path, tile::matches);
        if (matching == null) {
            matching = CollectionUtils.findMatching(path = dZone ? UZONE2 : easternPath, tile::matches);
        }
        Preconditions.checkArgument(matching != null);
        this.path = path;
        this.pathIndex = ArrayUtils.indexOf(path, tile);
    }

    @Override
    public void processSpot() {
        if (!shark) {
            shark = Utils.random(100) == 0;
            if (CharacterLoop.find(getLocation(), 1, Player.class, player -> !player.isDead() && player.getActionManager().getAction() instanceof Fishing).size() == 0) {
                shark = false;
            }
        }
        if (ticks > 3 && shark && ticks % 4 == 0) {
            final Location tile = new Location(getLocation());
            World.sendGraphics(sharkGraphics, tile);
            WorldTasksManager.schedule(() -> {
                CharacterLoop.find(tile, 1, Player.class, player -> !player.isDead() && player.getActionManager().getAction() instanceof Fishing).forEach(p -> {
                    if (p.getInventory().deleteItem(new Item(21356, 20)).getSucceededAmount() > 0) {
                        p.sendMessage("A flying fish gobbles up some of your minnows.");
                    }
                });
            }, 1);
        }
        if (--ticks == 0) {
            final Location tile = path[++pathIndex % path.length];
            addWalkSteps(tile.getX(), tile.getY(), 1, false);
            ticks = 25;
            shark = false;
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return id >= 7730 && id <= 7733;
    }
}
