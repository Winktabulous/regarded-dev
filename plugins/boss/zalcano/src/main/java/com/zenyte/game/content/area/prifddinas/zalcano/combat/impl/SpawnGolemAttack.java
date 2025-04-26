package com.zenyte.game.content.area.prifddinas.zalcano.combat.impl;

import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoGolemSpawn;
import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import com.zenyte.game.content.area.prifddinas.zalcano.combat.ZalcanoAttack;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

import java.util.List;

/**
 * Spawns Golem that heals Zalcano
 */
public class SpawnGolemAttack implements ZalcanoAttack {

    private static final Animation ANIM = new Animation(8432);
    private static final Projectile PROJECTILE =  new Projectile(1729, 80, 10, 0, 15, 35, 64, 7);

    private WorldTask spawnTask;

    @Override
    public void execute(ZalcanoInstance instance) {
        var locationToSpawn = PrisonWall.randomPrisonWall().getRandomLocation();

        instance.getZalcano().setLastGolemSpawn((int) (WorldThread.getCurrentCycle() + 50));
        instance.getZalcano().setFaceLocation(locationToSpawn);
        instance.getZalcano().setAnimation(ANIM);
        instance.getZalcano().freeze(3);
        WorldTasksManager.schedule(() -> World.sendProjectile(new Location(instance.getZalcano().getMiddleLocation()), locationToSpawn, PROJECTILE), 1);

        spawnTask = () -> {
            World.sendGraphics(new Graphics(1730), locationToSpawn);
            var direction = DirectionUtil.getFaceDirection(instance.getZalcano().getMiddleLocation(), locationToSpawn);
            instance.spawnGolem(new ZalcanoGolemSpawn(locationToSpawn, Direction.getMovementDirection(direction), instance));
        };

        WorldTasksManager.schedule(spawnTask, 5);
    }

    @Override
    public boolean canProcess(ZalcanoInstance instance) {
        return instance.getZalcano().getHitpoints() != 300 && instance.getZalcano().getLastGolemSpawn() < WorldThread.getCurrentCycle();
    }

    @Override
    public void interrupt() {
        if (spawnTask != null) {
            spawnTask.stop();
        }
    }

    private enum PrisonWall {
        NORTH(new Location(3029, 6061), true, 9),
        EAST(new Location(3045, 6044), false, 11),
        SOUTH(new Location(3029, 6037), true, 9),
        WEST(new Location(3022, 6044), false, 11)
        ;

        private final Location startTile;
        private final boolean incrementX;
        private final int length;

        private static final List<PrisonWall> VALUES = List.of(values());

        PrisonWall(Location startTile, boolean incrementX, int length) {
            this.startTile = startTile;
            this.incrementX = incrementX;
            this.length = length;
        }

        /**
         * Gets a random location for a prison wall
         * @return - true
         */
        public Location getRandomLocation() {
            if (incrementX) {
                var getX = startTile.getX();
                var newX = Utils.random(getX, getX + length);
                return new Location(newX, startTile.getY());
            }
            else {
                var getY = startTile.getY();
                var newY = Utils.random(getY, getY + length);
                return new Location(startTile.getX(), newY);
            }
        }

        /**
         * A method to get a random prison wall
         *
         * @return - a random prison wall
         */
        public static PrisonWall randomPrisonWall() {
            return Utils.getRandomCollectionElement(VALUES);
        }

    }

}
