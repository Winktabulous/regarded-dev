package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.CombatPointCapCalculator;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.content.chambersofxeric.npc.Tekton;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 16. nov 2017 : 2:40.33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class TektonRoom extends RaidArea implements CycleProcessPlugin, PlayerCombatPlugin {
   /**
    * An array containing coordinates to where Tekton initially lies.
    */
   private static final Location[] anvilTiles = new Location[] {new Location(3279, 5298, 1), new Location(3307, 5294, 1), new Location(3341, 5298, 1)};
   /**
    * A 2D array containing coordinates to all of the smoke pillars.
    */
   private static final Location[][] smokePillarTiles = new Location[][] {new Location[] {new Location(3286, 5289, 1), new Location(3275, 5288, 1), new Location(3275, 5301, 1), new Location(3286, 5301, 1), new Location(3287, 5296, 1)}, new Location[] {new Location(3318, 5288, 1), new Location(3307, 5288, 1), new Location(3308, 5301, 1), new Location(3319, 5300, 1)}, new Location[] {new Location(3348, 5289, 1), new Location(3340, 5287, 1), new Location(3336, 5291, 1), new Location(3337, 5301, 1), new Location(3349, 5300, 1)}};
   /**
    * An array containing coordinates to the crystal blocking the exit.
    */
   private static final Location[] blockingCrystalTiles = new Location[] {new Location(3270, 5294, 1), new Location(3310, 5304, 1), new Location(3352, 5296, 1)};
   /**
    * A 2D array containing coordinates to where the sparks fly from.
    */
   private static final Location[][] sparkTiles = new Location[][] {new Location[] {new Location(3273, 5288, 1), new Location(3273, 5302, 1), new Location(3280, 5305, 1), new Location(3289, 5303, 1), new Location(3288, 5289, 1)}, new Location[] {new Location(3320, 5289, 1), new Location(3321, 5303, 1), new Location(3317, 5302, 1), new Location(3303, 5295, 1), new Location(3305, 5288, 1), new Location(3305, 5301, 1)}, new Location[] {new Location(3350, 5289, 1), new Location(3334, 5288, 1), new Location(3335, 5302, 1), new Location(3343, 5304, 1), new Location(3350, 5303, 1)}};
   /**
    * A 2D array containing coordinates to where the fire spawns at.
    */
   private static final Location[] fireTiles = new Location[] {new Location(3279, 5281, 1), new Location(3311, 5281, 1), new Location(3343, 5281, 1)};
   /**
    * A 2D array containing coordinates to the tiles which are safe from sparks.
    */
   private static final Location[] safeTiles = new Location[] {new Location(3280, 5280, 1), new Location(3312, 5280, 1), new Location(3344, 5280, 1)};
   /**
    * The id of the crystal blocking the exit.
    */
   private static final int CRYSTAL = 30017;
   /**
    * An list of the smoke pillars locations.
    */
   private final List<Location> smokePillars = new ObjectArrayList<>();
   /**
    * A list of the locations from which the sparks fly.
    */
   private final List<Location> sparks = new ObjectArrayList<>();
   /**
    * The Tekton boss.
    */
   private Tekton tekton;
   /**
    * The crystal blocking the exit.
    */
   private WorldObject blockingCrystal;
   /**
    * The tile which is safe from sparks; one tile radius around it included.
    */
   private Location safeTile;
   /**
    * The number of ticks the room has been active for, used for damaging players standing on the smoke clouds, as the damage is applied only every three ticks.
    */
   private int ticks;
   /**
    * Whether or not the room has been finished.
    */
   private boolean finished;
   private final ArrayList<String> takenHitUsernames = new ArrayList<String>();

   public TektonRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
      super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
   }

   @Override
   public CombatPointCapCalculator buildPointsCap() {
      return new CombatPointCapCalculator().appendNPC(tekton);
   }

   /* @Override
    public boolean canPass(final Player player, final WorldObject object) {
        if (tekton == null || (!tekton.isDead() && !tekton.isFinished())) {
            player.sendMessage("You need to defeat Tekton before you may pass.");
            return false;
        }
        return true;
    }*/
   @Override
   public boolean processCombat(final Player player, final Entity entity, final String style) {
      if (entity == tekton) {
         if (!tekton.isDead() && !tekton.isFinished() && tekton.getId() == 7545) {
            player.sendMessage("Tekton's anvil protects him from your attacks.");
            return false;
         }
      }
      return true;
   }

   @Override
   public void process() {
      if (tekton == null || tekton.isDead()) {
         return;
      }
      if (ticks++ % 3 != 0) {
         return;
      }
      for (final Player p : players) {
         for (final Location t : smokePillars) {
            if (p.getLocation().withinDistance(t, 1)) {
               p.applyHit(new Hit(Utils.random(1, 3), HitType.REGULAR));
               addTakenHitEntry(p);
            }
         }
      }
   }

   @Override
   public void loadRoom() {
      safeTile = getLocation(safeTiles[index]);
      tekton = new Tekton(raid, Direction.cardinalDirections[((index == 1 ? 1 : 2) + getRotation()) & 3], this, getNPCLocation(anvilTiles[index], 6));
      tekton.spawn();
      for (final Location smoke : smokePillarTiles[index]) {
         smokePillars.add(getLocation(smoke));
      }
      for (final Location spark : sparkTiles[index]) {
         sparks.add(getLocation(spark));
      }
      World.spawnObject(blockingCrystal = new WorldObject(CRYSTAL, 10, getRotation(), getCrystalTile(blockingCrystalTiles[index])));
   }

   /**
    * Spawns the fire object at the entrance of the room.
    */
   public void spawnFire() {
      World.spawnObject(new WorldObject(30021, 10, getRotation(), getObjectLocation(fireTiles[index], 2, 2, getRotation())));
   }

   /**
    * Clears the crystal blocking the exit.
    */
   public void clearCrystal() {
      finished = true;
      Raid.shatterCrystal(blockingCrystal);
      WorldTasksManager.schedule(() -> World.removeObject(World.getObjectWithType(getObjectLocation(fireTiles[index], 2, 2, getRotation()), 10)), 2);
   }

   public void addTakenHitEntry(final Player player) {
      if (!takenHitUsernames.contains(player.getUsername())) {
         takenHitUsernames.add(player.getUsername());
      }
   }

   @Override
   public String name() {
      return "Chambers of Xeric: Tekton room";
   }

   public List<Location> getSparks() {
      return sparks;
   }

   public Tekton getTekton() {
      return tekton;
   }

   public Location getSafeTile() {
      return safeTile;
   }

   public boolean isFinished() {
      return finished;
   }

   public void setFinished(boolean finished) {
      this.finished = finished;
   }
}
