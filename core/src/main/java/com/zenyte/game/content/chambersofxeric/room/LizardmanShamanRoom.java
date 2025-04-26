package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.CombatPointCapCalculator;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.ScalingMechanics;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.content.chambersofxeric.npc.LizardmanShaman;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kris | 16. nov 2017 : 2:32.42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class LizardmanShamanRoom extends RaidArea implements CycleProcessPlugin {
    /**
     * A 2D array containing coordinates to the lizardman spawns.
     */
    private static final Location[][] lizardmanSpawnLocations = new Location[][] {new Location[] {new Location(3273, 5270, 0), new Location(3281, 5269, 0), new Location(3278, 5262, 0), new Location(3273, 5255, 0)}, new Location[] {new Location(3306, 5260, 0), new Location(3306, 5267, 0), new Location(3315, 5267, 0), new Location(3316, 5259, 0)}, new Location[] {new Location(3340, 5258, 0), new Location(3346, 5259, 0), new Location(3341, 5268, 0), new Location(3347, 5270, 0)}};
    /**
     * A 2D array containing coordinates to the spirit tendrils blocking the entrance.
     */
    private static final Location[][] spiritTendrilSpawnLocations = new Location[][] {new Location[] {new Location(3279, 5249, 0), new Location(3280, 5249, 0)}, new Location[] {new Location(3311, 5251, 0), new Location(3310, 5251, 0)}, new Location[] {new Location(3342, 5251, 0), new Location(3343, 5251, 0)}};
    /**
     * Polygon coordinates which define the safe tiles on the static map.
     */
    private static final int[][][] safePolygons = new int[][][] {new int[][] {{3290, 5263}, {3290, 5261}, {3291, 5260}, {3291, 5259}, {3292, 5258}, {3292, 5250}, {3267, 5250}, {3267, 5244}, {3294, 5244}, {3294, 5263}}, new int[][] {{3305, 5251}, {3305, 5246}, {3318, 5246}, {3318, 5251}}, new int[][] {{3333, 5251}, {3333, 5246}, {3353, 5246}, {3353, 5251}}};
    /**
     * The extra spirit tendril spawns. One of the three rooms has a layout which requires duplicate spirit tendrils to avoid easy safespotting.
     */
    private static final Location[] extraSpiritTendrils = new Location[] {new Location(3289, 5262, 0), new Location(3289, 5261, 0)};
    /**
     * A 2D array containing coordinates to where the spawns blocking the exit are.
     */
    private static final Location[][] blockingExitLocations = new Location[][] {new Location[] {new Location(3268, 5263, 0), new Location(3269, 5263, 0), new Location(3268, 5264, 0), new Location(3269, 5264, 0)}, new Location[] {new Location(3311, 5275, 0), new Location(3311, 5274, 0), new Location(3312, 5275, 0), new Location(3312, 5274, 0)}, new Location[] {new Location(3353, 5265, 0), new Location(3352, 5265, 0), new Location(3353, 5264, 0), new Location(3352, 5264, 0)}};
    /**
     * A list of spawned lizardman shamans.
     */
    private final List<LizardmanShaman> shamans = new ObjectArrayList<>(4);
    /**
     * An array containing the two spawn NPCs that block the exit.
     */
    private final NPC[] blockingSpawns = new NPC[2];
    /**
     * A map of <username, number of uses> for the tendrils, mapped to prevent damaging playes who cross it for the first time, but damage those who repeatedly use it.
     */
    private final Object2IntMap<String> tendrils = new Object2IntOpenHashMap<>();
    /**
     * Whether or not the tendrils have been loaded; Loading occurs when any player walks within a specific close proximity of the object.
     */
    private boolean loaded;
    /**
     * The approximate location of the tendrils which is used to determine how far or close any player is to the object. If they walk within necessary proximity, the tendril objects
     * are spawned, blocking the path.
     */
    private Location tendril;
    /**
     * A polygon that defines the tiles that are safe from after-math of Lizardman attacks.
     */
    private final RSPolygon safePolygon;
    private final ArrayList<String> hitByShamansUsernames = new ArrayList<String>();

    public LizardmanShamanRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
        final int[][] polyPoints = safePolygons[index];
        final int[][] intArray = new int[polyPoints.length][2];
        for (int i = 0; i < polyPoints.length; i++) {
            final Location tile = getLocation(polyPoints[i][0], polyPoints[i][1], toPlane);
            intArray[i][0] = tile.getX();
            intArray[i][1] = tile.getY();
        }
        safePolygon = new RSPolygon(intArray, toPlane);
    }

    @Override
    public CombatPointCapCalculator buildPointsCap() {
        return new CombatPointCapCalculator().appendNPCs(shamans);
    }

    /*@Override
    public boolean canPass(final Player player, final WorldObject object) {
        for (val shaman : shamans) {
            if (!shaman.isDead() && !shaman.isFinished()) {
                player.sendMessage("You need to defeat the Lizardmen shaman before you may pass.");
                return false;
            }
        }
        return true;
    }*/
    /**
     * Checks whether all the lizardmen are dead, if so, kills the spawns blocking the exit.
     */
    public void finish() {
        for (final LizardmanShaman shaman : shamans) {
            if (!shaman.isDead() && !shaman.isFinished()) {
                return;
            }
        }
        getRaid().getPlayers().stream().filter(e -> !getHitByShamansUsernames().contains(e.getUsername())).forEach(p -> p.getCombatAchievements().complete(CAType.SHAYZIEN_SPECIALIST));
        WorldTasksManager.schedule(() -> {
            for (final NPC spawn : blockingSpawns) {
                if (spawn == null) {
                    continue;
                }
                spawn.sendDeath();
            }
        }, 6);
        WorldTasksManager.schedule(this::wipe, 2);
    }

    @Override
    public void loadRoom() {
        int amount = ScalingMechanics.getShamanCount(raid);
        final List<Location> tiles = new ArrayList<>(Arrays.asList(lizardmanSpawnLocations[index]));
        while (shamans.size() < amount) {
            shamans.add((LizardmanShaman) new LizardmanShaman(raid, this, 7573 + Utils.random(1), getLocation(tiles.get(Utils.random(tiles.size() - 1)))).spawn());
        }
        this.tendril = getLocation(spiritTendrilSpawnLocations[index][0]);
    }

    /**
     * Loads the extra bits in the room when any player in the raid walks close enough to the tendrils.
     */
    private final void loadExtras() {
        if (loaded) {
            return;
        }
        loaded = true;
        final Location[] locations = blockingExitLocations[index];
        for (int i = 0; i < 2; i++) {
            blockingSpawns[i] = new BlockingSpawn(this.getLocation(locations[i * 2])).spawn();
            blockingSpawns[i].setFaceLocation(this.getLocation(locations[(i * 2) + 1]));
        }
        for (final Location tendril : spiritTendrilSpawnLocations[index]) {
            World.spawnObject(new WorldObject(29768, 10, 0, this.getLocation(tendril)));
        }
        if (index == 0) {
            for (final Location tendril : extraSpiritTendrils) {
                World.spawnObject(new WorldObject(29768, 10, 0, this.getLocation(tendril)));
            }
        }
    }

    /**
     * Checks whether or not all the lizardmen in the room are dead.
     *
     * @return whether or not the lizardmen in the room are dead.
     */
    public boolean isDead() {
        for (final LizardmanShaman lizardman : this.shamans) {
            if (!lizardman.isDead()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Wipes the room clear of all the spirit tendrils, including the extras in the one specific layout.
     */
    private void wipe() {
        for (final Location tendril : spiritTendrilSpawnLocations[index]) {
            World.removeObject(World.getObjectWithType(getLocation(tendril), 10));
        }
        if (index == 0) {
            for (final Location tendril : extraSpiritTendrils) {
                World.removeObject(World.getObjectWithType(getLocation(tendril), 10));
            }
        }
    }

    @Override
    public void process() {
        if (loaded || tendril == null) {
            return;
        }
        for (final Player player : raid.getPlayers()) {
            if (player.getLocation().withinDistance(tendril, 6)) {
                loadExtras();
            }
        }
    }

    @Override
    public String name() {
        return "Chambers of Xeric: Lizardman Shaman room";
    }


    /**
     * A private class for the blocking spawn NPCs.
     */
    private static final class BlockingSpawn extends NPC {
        /**
         * The animation that's performed a few ticks before the spawn blows up.
         */
        private static final Animation aboutToExplodeAnimation = new Animation(7159);
        /**
         * The graphics that are sent when the spawn blows up.
         */
        private static final Graphics splashGraphics = new Graphics(1295);
        /**
         * The sound effect played when the spawn is about to explode.
         */
        private static final SoundEffect aboutToExplodeSound = new SoundEffect(665, 5, 0);
        /**
         * The sound effect played when the spawn explodes.
         */
        private static final SoundEffect explodeSound = new SoundEffect(1021, 5, 0);

        BlockingSpawn(final Location tile) {
            super(7575, tile, Direction.SOUTH, 0);
            World.getRegion(getLocation().getRegionId()).clip(getPlane() & 3, getX() & 63, getY() & 63);
        }

        @Override
        public void sendDeath() {
            setAnimation(aboutToExplodeAnimation);
            World.sendSoundEffect(getLocation(), aboutToExplodeSound);
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    World.getRegion(getLocation().getRegionId()).unclip(getPlane() & 3, getX() & 63, getY() & 63);
                    CharacterLoop.forEach(getLocation(), 2, Player.class, player -> player.applyHit(new Hit(1, HitType.REGULAR)));
                    reset();
                    finish();
                    stop();
                    World.sendSoundEffect(new Location(getLocation()), explodeSound);
                    World.sendGraphics(splashGraphics, new Location(getLocation()));
                }
            }, 2);
        }
    }

    public Object2IntMap<String> getTendrils() {
        return tendrils;
    }

    public RSPolygon getSafePolygon() {
        return safePolygon;
    }

    public final ArrayList<String> getHitByShamansUsernames() { return hitByShamansUsernames; }
}
