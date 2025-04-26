package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;

public class CrystalOfMemories extends ItemPlugin {
    @Override
    public void handle() {
        bind("Teleport-back", (player, item, slotId) -> {
            if(!player.getAttributes().containsKey("CRYSTAL_OF_MEMORIES")) {
                player.sendMessage("You don't have a previous teleport.");
                return;
            }

            if (WildernessArea.isWithinWilderness(player)) {
                player.sendMessage("You can't use this item inside the Wilderness.");
                return;
            }

            Location location = new Location(((Number)player.getAttributes().get("CRYSTAL_OF_MEMORIES")).intValue());
            if (WildernessArea.isWithinWilderness(location)) {
                player.sendMessage("Crystal of memories refuses to teleport you to the Wilderness.");
                return;
            }

            player.sendMessage("You rub the crystal of memories and it brings you back to a place you remember.");
            teleport(location).teleport(player);
        });
    }

    public Teleport teleport(Location location) {
        return new Teleport() {
            @Override
            public TeleportType getType() {
                return TeleportType.CRYSTAL_OF_MEMORIES;
            }

            @Override
            public Location getDestination() {
                return location;
            }

            @Override
            public int getLevel() {
                return 0;
            }

            @Override
            public double getExperience() {
                return 0;
            }

            @Override
            public int getRandomizationDistance() {
                return 0;
            }

            @Override
            public Item[] getRunes() {
                return new Item[0];
            }

            @Override
            public int getWildernessLevel() {
                return 0;
            }

            @Override
            public boolean isCombatRestricted() {
                return false;
            }
        };
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.CRYSTAL_OF_MEMORIES};
    }
}
