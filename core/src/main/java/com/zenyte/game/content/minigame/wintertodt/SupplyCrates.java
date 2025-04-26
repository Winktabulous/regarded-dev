package com.zenyte.game.content.minigame.wintertodt;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import mgi.types.config.items.ItemDefinitions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Corey
 * @since 22:00 - 21/07/2019
 */
public class SupplyCrates implements ObjectAction {
   @Override
   public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
      final int objectId = object.getId();
      final ItemDefinitions item = ItemDefinitions.getOrThrow(WintertodtCrate.VALUE_MAP.get(objectId));
      String itemName = item.getName().toLowerCase();
      if (item.getId() == WintertodtCrate.REJUVENATION_POTION.getItemId()) {
         itemName = "unfinished potion";
      } else if (item.getId() == WintertodtCrate.BRONZE_AXE.getItemId()) {
         itemName = "axe";
      }
      if (item.getId() != WintertodtCrate.REJUVENATION_POTION.itemId && player.getInventory().containsItem(item.getId(), 1)) {
         player.sendMessage("You already have " + Utils.getAOrAn(itemName) + " " + itemName + ".");
         return;
      }
      if (!player.getInventory().hasFreeSlots()) {
         player.sendMessage("You need space in your inventory to take " + Utils.getAOrAn(itemName) + " " + itemName + ".");
         return;
      }
      int quantity = 1;
      if (optionId == 2) {
         quantity = 5;
      } else if (optionId == 3) {
         quantity = 10;
      }
      if (quantity > 1) {
         player.sendMessage("You take the " + itemName + "s from the crate.");
      } else {
         player.sendMessage("You take " + Utils.getAOrAn(itemName) + " " + itemName + " from the crate.");
      }
      player.getInventory().addItem(item.getId(), quantity);
   }

   public Object[] getObjects() {
      return WintertodtCrate.VALUE_MAP.keySet().toArray();
   }


   private enum WintertodtCrate {
      HAMMER(2347, 29316), KNIFE(946, 29317), BRONZE_AXE(1351, 29318), TINDERBOX(590, 29319), REJUVENATION_POTION(20697, 29320);
      static final WintertodtCrate[] VALUES = values();
      static final Map<Integer, Integer> VALUE_MAP = new HashMap<>(VALUES.length);

      static {
         for (final SupplyCrates.WintertodtCrate crate : VALUES) {
            VALUE_MAP.put(crate.objectId, crate.itemId);
         }
      }

      private final int itemId;
      private final int objectId;

      WintertodtCrate(int itemId, int objectId) {
         this.itemId = itemId;
         this.objectId = objectId;
      }

      public int getItemId() {
         return itemId;
      }

      public int getObjectId() {
         return objectId;
      }
   }
}
