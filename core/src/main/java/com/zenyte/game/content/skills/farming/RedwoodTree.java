package com.zenyte.game.content.skills.farming;

import com.zenyte.game.content.skills.woodcutting.TreeDefinitions;
import com.zenyte.utils.Ordinal;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import mgi.utilities.CollectionUtils;

import java.util.Objects;

/**
 * @author Kris | 19/02/2019 17:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RedwoodTree {
   /**
    * The map containing redwood branches and the time in milliseconds until it respawns(or when it respawned)
    */
   private final Object2LongOpenHashMap<RedwoodTreeBranch> map = new Object2LongOpenHashMap<>(4);

   /**
    * Sets the given branch as chopped-down.
    * @param id the id of the redwood branch.
    */
   void setChopped(final int id) {
      map.put(Objects.requireNonNull(RedwoodTreeBranch.branches.get(id)), System.currentTimeMillis() + (TreeDefinitions.REDWOOD_TREE.getRespawnDelay() * 600));
   }

   /**
    * Gets the next shortest delay for the next branch to respawn. If all branches are alive, returns 0.
    * @return the delay in milliseconds when the next branch respawns.
    */
   long nextDelay() {
      if (map.isEmpty()) {
         return 0;
      }
      long delay = 0;
      final long time = System.currentTimeMillis();
      for (final Object2LongMap.Entry<RedwoodTree.RedwoodTreeBranch> entry : map.object2LongEntrySet()) {
         if (time > entry.getLongValue() || delay != 0 && delay < entry.getLongValue()) {
            continue;
         }
         delay = entry.getLongValue();
      }
      return delay == 0 ? 0 : (delay - time);
   }

   /**
    * Gets the respective varbit value for the tree, considering its current state.
    * @return the varbit value of the tree, returns 18 - which is the default state - if all branches are alive.
    */
   public int value() {
      int value = 0;
      final long time = System.currentTimeMillis();
      for (final Object2LongMap.Entry<RedwoodTree.RedwoodTreeBranch> entry : map.object2LongEntrySet()) {
         if (time > entry.getLongValue()) {
            continue;
         }
         value |= 1 << entry.getKey().ordinal();
      }
      return value == 0 ? 18 : 40 + value;
   }


   @Ordinal
   @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
   public enum RedwoodTreeBranch {
      NORTHERN(34633), WESTERN(34635), EASTERN(34637), SOUTHERN(34639);
      private final int id;
      private static final Int2ObjectOpenHashMap<RedwoodTreeBranch> branches;

      static {
         CollectionUtils.populateMap(values(), branches = new Int2ObjectOpenHashMap<>(), branch -> branch.id);
      }

      RedwoodTreeBranch(int id) {
         this.id = id;
      }

      public int getId() {
         return id;
      }
   }
}
