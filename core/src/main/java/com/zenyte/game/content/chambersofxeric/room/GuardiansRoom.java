package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.CombatPointCapCalculator;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.content.chambersofxeric.npc.RaidGuardianNPC;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

/**
 * @author Kris | 16. nov 2017 : 2:47.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class GuardiansRoom extends RaidArea implements CycleProcessPlugin {
    /**
     * A 2D array containing the locations of the guardians, and the coordinate to which the player is pushed.
     */
    public static final Location[][] guardianSpawnTiles = new Location[][] {new Location[] {new Location(3276, 5265, 2), new Location(3276, 5269, 2), new Location(3276, 5267, 2), new Location(3276, 5268, 2)}, new Location[] {new Location(3312, 5271, 2), new Location(3308, 5271, 2), new Location(3310, 5271, 2), new Location(3311, 5271, 2)}, new Location[] {new Location(3344, 5268, 2), new Location(3344, 5272, 2), new Location(3344, 5271, 2), new Location(3344, 5270, 2)}};
    /**
     * A 2D array containing the coordinates to which each of the guardian faces.
     */
    public static final Location[][] faceTiles = new Location[][] {new Location[] {new Location(4000, 5265, 2), new Location(4000, 5269, 2)}, new Location[] {new Location(3312, 4000, 2), new Location(3308, 4000, 2)}, new Location[] {new Location(2000, 5268, 2), new Location(2000, 5272, 2)}};
    /**
     * A 2D array containing coordinates to the prevented tiles, standing upon which the player is pushed back.
     */
    private static final Location[][] preventedTiles = new Location[][] {new Location[] {new Location(3275, 5267, 2), new Location(3275, 5268, 2)}, new Location[] {new Location(3310, 5273, 2), new Location(3311, 5273, 2)}, new Location[] {new Location(3346, 5271, 2), new Location(3346, 5270, 2)}};
    /**
     * The sound effect that is played when the player is being pushed away from the exit.
     */
    private static final SoundEffect pushSound = new SoundEffect(851, 10, 0);
    /**
     * A list of the guardian NPCs.
     */
    private final List<RaidGuardianNPC> npcs = new ObjectArrayList<>(2);
    /**
     * An array of the prevented locations, standing upon which the player gets pushed back.
     */
    private final Location[] prevented = new Location[2];
    /**
     * Defines whether the puzzle has been finished or not; puzzle finishes when guardians die.
     */
    private boolean finished;
    /**
     * A temporary variable used for the purpose of slowing down pushback effect iterations. Instead of checking that every tic, we use the variable to slow it down to every 3 ticks.
     */
    private int ticks;

    public GuardiansRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public CombatPointCapCalculator buildPointsCap() {
        return new CombatPointCapCalculator().appendNPCs(npcs);
    }

    @Override
    public void loadRoom() {
        final Location[] loc = guardianSpawnTiles[index];
        final Location[] face = faceTiles[index];
        for (int i = 0; i < 2; i++) {
            final RaidGuardianNPC guardian = new RaidGuardianNPC(raid, this, 7569 + i, getNPCLocation(loc[i], 2));
            guardian.spawn();
            guardian.setFaceLocation(getLocation(face[i]));
            npcs.add(guardian);
            prevented[i] = this.getLocation(preventedTiles[index][i]);
            World.spawnObject(new WorldObject(26209, 10, 0, prevented[i]));
        }
    }

    /**
     * Finishes the room by removing the invisible blocking objects from in-front of the exit.
     */
    public void finish() {
        if (!finished()) {
            return;
        }
        for (final Location object : prevented) {
            World.removeObject(World.getObjectWithType(object, 10));
        }
    }

    /*@Override
    public final boolean canPass(final Player player, final WorldObject object) {
        return finished;
    }*/
    /**
     * Checks whether both the guardians are dead, and if so, sets the {@code GuardiansRoom#finished } to true.
     *
     * @return whether the guardians are both dead or not.
     */
    private boolean finished() {
        if (npcs.isEmpty()) {
            return false;
        }
        for (final RaidGuardianNPC guardian : npcs) {
            if (!guardian.isDead()) {
                return false;
            }
        }
        finished = true;
        return true;
    }

    @Override
    public void process() {
        if (finished || finished() || ++ticks % 3 != 0) {
            return;
        }
        for (final Player player : players) {
            final int prevented = isPrevented(player);
            if (prevented != -1) {
                pushBack(player, prevented);
            }
        }
    }

    /**
     * Checks whether the tile the player is standing on is defined as a prevented tile, standing upon which the player gets pushed
     * backwards.
     *
     * @param player the player whom to check.
     * @return index of the guardian who will push the player backwards.
     */
    private int isPrevented(final Player player) {
        if (player.isLocked()) {
            return -1;
        }
        final Location loc = player.getLocation();
        for (int i = 0; i < 2; i++) {
            if (prevented[i] == null) {
                continue;
            }
            final double distance = loc.getDistance(prevented[i]);
            if (distance <= 1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Pushes the player backwards as they've gone too close to the exit without killing the guardians.
     *
     * @param player the player whom to push backwards.
     * @param index  the index of the guardian who will push the player backwards.
     */
    private void pushBack(final Player player, final int index) {
        player.resetWalkSteps();
        final Location t = new Location(player.getLocation());
        WorldTasksManager.schedule(() -> {
            if (!t.matches(player)) {
                return;
            }
            final Location tile = getLocation(guardianSpawnTiles[this.index][2 + index]);
            player.setForceMovement(new ForceMovement(tile, 10, tile, 30, DirectionUtil.getFaceDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
            final RaidGuardianNPC n = npcs.stream().max((n2, n1) -> n1.getLocation().getDistance(player.getLocation()) > n2.getLocation().getDistance(player.getLocation()) ? 1 : -1).orElseThrow(RuntimeException::new);
            n.setFaceLocation(player.getLocation());
            n.setAnimation(RaidGuardianNPC.attack);
            final NPCCombat combat = n.getCombat();
            combat.setCombatDelay(n.getCombatDefinitions().getAttackSpeed());
            combat.setTarget(player);
            World.sendSoundEffect(n.getLocation(), pushSound);
            raid.addCombatPoints(player, 40);
            WorldTasksManager.schedule(() -> {
                player.setLocation(tile);
                player.setAnimation(Animation.STOP);
            });
            player.applyHit(new Hit(Utils.random(15, 30), HitType.REGULAR));
            final RaidGuardianNPC oppositeNPC = npcs.get(0) == n ? npcs.get(1) : npcs.get(0);
            if (oppositeNPC.isDead() || oppositeNPC.isFinished()) {
                return;
            }
            final Location oppositePreventedTile = getLocation(guardianSpawnTiles[this.index][2 + (index == 0 ? 1 : 0)]);
            oppositeNPC.stomp(oppositePreventedTile);
            oppositeNPC.setFaceLocation(player.getLocation());
            final NPCCombat oppositeCombat = oppositeNPC.getCombat();
            oppositeCombat.setCombatDelay(oppositeNPC.getCombatDefinitions().getAttackSpeed());
            oppositeCombat.setTarget(player);
        });
    }

    @Override
    public String name() {
        return "Chambers of Xeric: Guardians room";
    }

    public List<RaidGuardianNPC> getNpcs() {
        return npcs;
    }
}
