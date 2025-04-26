package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.CombatPointCapCalculator;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.content.chambersofxeric.npc.Crystal;
import com.zenyte.game.content.chambersofxeric.npc.VasaNistirio;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 16. nov 2017 : 2:33.10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class VasaNistirioRoom extends RaidArea {

    /**
     * The graphics played when the fire blocking the entrance vanishes.
     */
    private static final Graphics graphics = new Graphics(1296);

    /**
     * A 2D array containing locations of the crystals which vasa siphons from.
     */
    private static final Location[][] crystalLocations = new Location[][]{
            new Location[]{
                    new Location(3270, 5285, 0), new Location(3270, 5303, 0), new Location(3287, 5303, 0),
                    new Location(3287, 5285, 0)
            },
            new Location[]{
                    new Location(3301, 5285, 0), new Location(3301, 5302, 0), new Location(3319, 5302, 0),
                    new Location(3319, 5285, 0)
            },
            new Location[]{
                    new Location(3333, 5285, 0), new Location(3333, 5303, 0), new Location(3350, 5303, 0),
                    new Location(3350, 5285, 0)
            }
    };

    /**
     * The id of the crystal blocking the exit.
     */
    private static final int CRYSTAL = 30016;

    /**
     * An array containing coordinates to the crystal blocking the exit.
     */
    private static final Location[][] blockingCrystalLocations = new Location[][]{
            new Location[]{new Location(3268, 5295, 0), new Location(3268, 5296, 0)},
            new Location[]{new Location(3311, 5307, 0), new Location(3312, 5307, 0)},
            new Location[]{new Location(3355, 5296, 0), new Location(3355, 5295, 0)},
    };

    /**
     * An array containing coordinates to the fire blocking the entrance.
     */
    private static final Location[] fireLocations = new Location[]{
            new Location(3279, 5281, 0), new Location(3311, 5281, 0),
            new Location(3343, 5281, 0)
    };

    /**
     * An array of coordinates defining the center of the room, considering vasa's large size.
     */
    private static final Location[] npcCenterLocations = new Location[]{
            new Location(3278, 5293, 0), new Location(3309, 5293, 0),
            new Location(3341, 5294, 0)
    };

    /**
     * An array of coordinates defining the center of the room.
     */
    private static final Location[] absoluteCenterLocations = new Location[]{
            new Location(3280, 5296, 0), new Location(3312, 5296, 0),
            new Location(3344, 5296, 0)
    };
    /**
     * The crystals blocking the exit.
     */
    private final WorldObject[] blockingCrystals = new WorldObject[2];
    /**
     * An array of the crystal NPCs.
     */
    private final Crystal[] crystals = new Crystal[4];
    /**
     * An array of the crystal objects.
     */
    private final WorldObject[] crystalObjects = new WorldObject[4];
    /**
     * Vasa nistirio boss.
     */
    private VasaNistirio vasa;
    /**
     * Location of the fire blocking the entrance.
     */
    private Location fire;
    /**
     * Location of the center of the area.
     */
    private Location center;
    /**
     * Location of the center of the area, from vasa's viewport.
     */
    private Location npcCenter;

    public VasaNistirioRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY,
                            final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public CombatPointCapCalculator buildPointsCap() {
        return new CombatPointCapCalculator().appendNPC(vasa).appendNPC(new Crystal(raid, this, 7568, vasa.getLocation(), index), 4);
    }

    /**
     * Removes the magical fire blocking the entrance to the room.
     */
    public void removeMagicalFire() {
        clearCrystal();
        World.sendGraphics(graphics, fire);
        World.sendGraphics(graphics, new Location(fire.getX(), fire.getY() + 1, fire.getPlane()));
        WorldTasksManager.schedule(() -> World.removeObject(World.getObjectWithType(fire, 10)), 1);
    }

    /**
     * Spawns the fire to block the entrance of the room.
     */
    public void start() {
        fire = getObjectLocation(fireLocations[index], 2, 2, getRotation());
        World.spawnObject(new WorldObject(30019, 10, getRotation(), fire));
        calculateRemainingCombatPoints();
    }

    @Override
    public void loadRoom() {
        int idx = 0;
        for (final Location crystal : crystalLocations[index]) {
            final Location tile = getObjectLocation(crystal, 6, 6, getRotation());
            final WorldObject obj = new WorldObject(29775, 10, getRotation(), tile);
            crystalObjects[idx] = obj;
            idx++;
            World.spawnObject(obj);
        }
        npcCenter = getNPCLocation(npcCenterLocations[index], 6);
        vasa = new VasaNistirio(raid, this, new Location(npcCenter));
        vasa.spawn();
        vasa.setRadius(0);
        center = this.getLocation(absoluteCenterLocations[index]);
        for (int i = 0; i < 2; i++) {
            World.spawnObject(blockingCrystals[i] = new WorldObject(CRYSTAL, 10, getRotation(), getLocation(blockingCrystalLocations[index][i])));
        }
    }

    @Override
    public boolean hit(final Player source, final Entity target, final Hit hit, final float modifier) {
        super.hit(source, target, hit, modifier);
        if (target == vasa && vasa.getId() == 7567) {
            hit.setDamage(0);
        }
        return true;
    }

    /**
     * Resets the crystal defined by the index to default, alive state.
     */
    public void resetCrystal(final int index) {
        final Location crystal = crystalLocations[this.index][index];
        final Location tile = getObjectLocation(crystal, 6, 6, getRotation());
        final WorldObject obj = new WorldObject(29774, 10, getRotation(), tile);
        crystalObjects[index] = obj;
        crystals[index] = new Crystal(raid, this, 7568, tile, index);
        crystals[index].spawn();
        World.spawnObject(obj);
    }

    /**
     * Clears the crystal blocking the exit.
     */
    private void clearCrystal() {
        for (int i = 0; i < 2; i++) {
            Raid.shatterCrystal(blockingCrystals[i]);
        }
    }

   /* @Override
    public boolean canPass(final Player player, final WorldObject object) {
        if (vasa.isDead() || vasa.isFinished()) {
            return true;
        }
        player.sendMessage("You need to defeat Vasa Nistirio before you may pass.");
        return false;
    }
*/
    @Override
    public String name() {
        return "Chambers of Xeric: Vasa Nistirio room";
    }
    
    public Crystal[] getCrystals() {
        return crystals;
    }
    
    public WorldObject[] getCrystalObjects() {
        return crystalObjects;
    }
    
    public VasaNistirio getVasa() {
        return vasa;
    }
    
    public Location getCenter() {
        return center;
    }
    
    public Location getNpcCenter() {
        return npcCenter;
    }

}
