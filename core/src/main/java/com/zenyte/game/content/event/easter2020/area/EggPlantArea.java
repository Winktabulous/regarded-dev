package com.zenyte.game.content.event.easter2020.area;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.event.easter2020.plugin.npc.EasterImpling;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.DropPlugin;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.plugins.SkipPluginScan;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @since 26/03/2020
 */
@SkipPluginScan
public class EggPlantArea extends PolygonRegionArea implements RandomEventRestrictionPlugin, CannonRestrictionPlugin, DropPlugin {
    
    private static final Location[] critterSpawns = new Location[]{
            new Location(2233, 4387),
            new Location(2229, 4384),
            new Location(2224, 4383),
            new Location(2222, 4389),
            new Location(2219, 4381),
            new Location(2220, 4357),
            new Location(2222, 4361),
            new Location(2229, 4362),
            new Location(2224, 4364),
            new Location(2221, 4368),
            new Location(2219, 4374),
            new Location(2220, 4379),
            new Location(2226, 4377),
            new Location(2231, 4377),
            new Location(2228, 4372),
            new Location(2231, 4373),
            new Location(2225, 4369),
            new Location(2227, 4371),
            new Location(2231, 4367),
            new Location(2234, 4366),
            new Location(2234, 4364),
            new Location(2228, 4362)
    };
    
    public EggPlantArea() {
    
        for (Location spawn : critterSpawns) {
            World.spawnNPC(Utils.random(EasterConstants.RAT_ID, EasterConstants.SNAIL_ID), spawn, Direction.SOUTH, 7);
        }
    
        for (EasterImpling.Npc impling : EasterImpling.Npc.values) {
            World.spawnNPC(impling.getNpcId(), impling.getLocation(), Direction.SOUTH, 3);
        }
    
        World.spawnNPC(EasterConstants.EASTER_BIRD_NPC, new Location(2204, 4371), Direction.SOUTH, 0);

        //Spawn both easter bunnies on the same tile, so depending on varbit, their stance animation switches.
        /*World.spawnNPC(EasterConstants.SAD_EASTER_BUNNY, new Location(3088, 3471), Direction.NORTH, 0);
        World.spawnNPC(EasterConstants.HAPPY_EASTER_BUNNY, new Location(3088, 3471), Direction.NORTH, 0);
        World.spawnNPC(EasterConstants.POST_QUEST_EASTER_BUNNY, new Location(3088, 3471), Direction.NORTH, 0);*/

        World.spawnNPC(EasterConstants.EASTER_BUNNY_JR_NEXT_TO_BIRD, new Location(2202, 4371), Direction.SOUTH, 0);
        World.spawnNPC(EasterConstants.EASTER_BUNNY_JR_NEXT_TO_EGGPAINTING_MACHINE, new Location(2203, 4364), Direction.NORTH, 0);
        World.spawnNPC(EasterConstants.EASTER_BUNNY_JR_NEXT_TO_COAL, new Location(2205, 4380), Direction.SOUTH, 0);
        World.spawnNPC(EasterConstants.EASTER_BUNNY_JR_NEXT_TO_NUT_MACHINE, new Location(2192, 4371), Direction.SOUTH, 0);
        World.spawnNPC(EasterConstants.EASTER_BUNNY_JR_ON_RED_CARPET, new Location(2208, 4395), Direction.SOUTH, 0);

        World.spawnNPC(EasterConstants.IMPLING_WORKER_EAST_NEAR_EGGPAINTING_MACHINE, new Location(2205, 4360), Direction.NORTH, 0);
        World.spawnNPC(EasterConstants.IMPLING_WORKER_MIDDLE_NEAR_EGGPAINTING_MACHINE, new Location(2202, 4360), Direction.NORTH, 0);
        World.spawnNPC(EasterConstants.IMPLING_WORKER_WEST_NEAR_EGGPAINTING_MACHINE, new Location(2198, 4360), Direction.NORTH, 0);
        World.spawnNPC(EasterConstants.IMPLING_MANAGER, new Location(2200, 4365), Direction.NORTH, 1);


        World.spawnNPC(EasterConstants.EASTER_BUNNY_JR_CHAIR, new Location(2191, 4396), Direction.SOUTH, 0);
        World.spawnNPC(EasterConstants.IMPLING_INCUBATOR_WORKER, new Location(2200, 4380), Direction.SOUTH, 1);
    
        {
            for (int i = 0; i < 2; i++)
                World.spawnNPC(15214 + i, new Location(2190, 4361, 0), Direction.WEST, 0);//Squirrel on wheel in s-w corner.
        
            for (int i = 0; i < 3; i++)
                World.spawnNPC(15218 + i, new Location(2204, 4371, 0), Direction.SOUTH, 0);//Bird
        
            for (int i = 0; i < 3; i++)
                World.spawnNPC(15248 + i, new Location(2193, 4370, 0), Direction.EAST, 0);//Barrels

            for (int i = 0; i < 3; i++)
            World.spawnNPC(15254 + i, new Location(2192, 4372, 0), Direction.EAST, 0);//Large piece above barrels

            for (int i = 0; i < 3; i++)
            World.spawnNPC(15242 + i, new Location(2193, 4376, 0), Direction.EAST, 0);//Large piece above ^

            for (int i = 0; i < 5; i++)
            World.spawnNPC(15224 + i, new Location(2201, 4380, 0), Direction.SOUTH, 0);//Incubator

            for (int i = 0; i < 2; i++)
            World.spawnNPC(15238 + i, new Location(2202, 4360, 0), Direction.WEST, 0);//Eastern part of the southern machine

            for (int i = 0; i < 2; i++)
            World.spawnNPC(15234 + i, new Location(2196, 4359, 0), Direction.WEST, 0);//Western part of the southern machine

            World.spawnNPC(15260, new Location(2193, 4369, 0), Direction.NORTH, 0);//Cream bucket
            World.spawnNPC(15261, new Location(2194, 4369, 0), Direction.WEST, 0);//Choco bucket
        }

        ItemDefinitions.get(EasterConstants.EasterItem.IMPLING_NET.getItemId()).setSlot(EquipmentSlot.WEAPON.getSlot());
    }
    
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {2176, 4352},
                        {2176, 4416},
                        {2240, 4416},
                        {2240, 4352}
                })
        };
    }
    
    @Override
    public void enter(Player player) {
        if (!player.getPrivilege().eligibleTo(PlayerPrivilege.DEVELOPER)) {
            player.lock(2);
            player.setLocation(new Location(3088, 3474, 0));
            return;
        }
        player.setViewDistance(Player.SCENE_DIAMETER);
    }
    
    @Override
    public void leave(Player player, boolean logout) {
        player.resetViewDistance();
        if (!logout) {
            for (int item : EasterConstants.EasterItem.items) {
                player.getInventory().deleteItem(new Item(item, 28));
                player.getEquipment().deleteItem(new Item(item));
            }
        }
    }
    
    @Override
    public String name() {
        return "Egg Plant";
    }
    
    @Override
    public boolean dropOnGround(Player player, Item item) {
        return !EasterConstants.EasterItem.items.contains(item.getId());
    }
}
