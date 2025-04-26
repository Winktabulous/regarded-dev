package com.zenyte.game.content.minigame.blastfurnace;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.LayableTrapRestrictionPlugin;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class BlastFurnaceArea extends PolygonRegionArea implements CannonRestrictionPlugin, LayableTrapRestrictionPlugin, CycleProcessPlugin {
    /* Object animations */
    public static final Animation CONVEYER_BELT = new Animation(2435);
    public static final Animation GEAR_ANIM = new Animation(2436);
    public static final Animation LAVA_FLOW = new Animation(2440);
    /* Entity animations */
    public static final Animation ORE_FALL = new Animation(2434);
    public static final Animation TAKE_COKE = new Animation(2441);
    public static final Animation PLACE_COKE = new Animation(2442);
    public static final Animation DUMPY_IDLE = new Animation(2443);
    /* Locations */
    public static final Location ORE_CONVEYER_START = new Location(1942, 4966, 0);
    public static final WorldObject BAR_DISPENSER = new WorldObject(9092, 10, 0, new Location(1940, 4963, 0));
    /* Objects */
    private static final WorldObject CONVEYER_MAIN = new WorldObject(9100, 10, 0, new Location(1943, 4967, 0));
    private static final WorldObject CONVEYER_TWO = new WorldObject(9101, 10, 0, new Location(1943, 4966, 0));
    private static final WorldObject CONVEYER_THREE = new WorldObject(9101, 10, 0, new Location(1943, 4965, 0));
    private static final WorldObject COGS_NORTH = new WorldObject(9104, 10, 0, new Location(1945, 4967, 0));
    private static final WorldObject COGS_SOUTH = new WorldObject(9108, 10, 0, new Location(1945, 4965, 0));
    private static final WorldObject DRIVE_BELT_NORTH = new WorldObject(9102, 10, 0, new Location(1944, 4967, 0));
    private static final WorldObject DRIVE_BELT_SOUTH = new WorldObject(9107, 10, 0, new Location(1944, 4965, 0));
    private static final WorldObject GEAR_BOX = new WorldObject(9106, 10, 0, new Location(1945, 4966, 0));
    /* Object spawns / removals */
    private static final WorldObject REMOVE_PEDALS = new WorldObject(9097, 10, 1, new Location(1947, 4966, 0));
    private static final WorldObject REMOVE_PUMP = new WorldObject(9090, 10, 1, new Location(1950, 4961, 0));
    private static final WorldObject FULL_STOVE = new WorldObject(9087, 10, 0, new Location(1948, 4963, 0));
    /* Minigame data */
    public static final boolean FURNACE_ACTIVE = true;
    public int iteration = 0;

    {
        World.loadRegion(7757);
        World.removeObject(REMOVE_PEDALS);
        World.removeObject(REMOVE_PUMP);
        World.spawnObject(FULL_STOVE);
    }

    @Override
    public void process() {
        playObjectAnimations();
        /* Blast furnace varbit-related code and coffer */
        for (final Player player : getPlayers()) {
            if (player == null) {
                continue;
            }
            if (player.getBlastFurnace().getCoffer() == 0) {
                if (player.getVarManager().getBitValue(5357) != 0) {
                    player.getVarManager().sendBit(5357, 0);
                }
                continue;
            }
            if (player.getBlastFurnace().isProcessBarsFromDialogue()) {
                if (!player.getInterfaceHandler().containsInterface(InterfacePosition.CHATBOX)) {
                    player.getBlastFurnace().processBars();
                    player.getBlastFurnace().setProcessBarsFromDialogue(false);
                }
            }
            // The false response of 15 is for players below 60 smithing, this is a modified rate to match OSRS' 15k/hr fee from paying the foreman when under 60 smithing.
            final int removalAmt = player.getSkills().getLevel(SkillConstants.SMITHING) >= 60 ? 12 : 15;
            final int coffer = player.getBlastFurnace().getCoffer();
            player.getBlastFurnace().setCoffer(coffer >= removalAmt ? (coffer - removalAmt) : 0);
            if (coffer < removalAmt) {
                player.getVarManager().sendBit(5356, 0);
            }
            player.getVarManager().sendBit(5357, player.getBlastFurnace().getCoffer());
        }
    }

    private void playObjectAnimations() {
        World.sendObjectAnimation(CONVEYER_MAIN, CONVEYER_BELT);
        World.sendObjectAnimation(CONVEYER_TWO, CONVEYER_BELT);
        World.sendObjectAnimation(CONVEYER_THREE, CONVEYER_BELT);
        World.sendObjectAnimation(COGS_NORTH, GEAR_ANIM);
        World.sendObjectAnimation(COGS_SOUTH, GEAR_ANIM);
        World.sendObjectAnimation(DRIVE_BELT_NORTH, GEAR_ANIM);
        World.sendObjectAnimation(DRIVE_BELT_SOUTH, GEAR_ANIM);
        World.sendObjectAnimation(GEAR_BOX, GEAR_ANIM);
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{1934, 4976}, {1959, 4976}, {1959, 4955}, {1934, 4955}}, 0)};
    }

    @Override
    public void enter(final Player player) {
        GameInterface.BLAST_FURNACE_COFFER.open(player);
        player.getBlastFurnace().processVarbits();
        player.getBlastFurnace().processBars();
        if (player.getBlastFurnace().getDispenserState() == 2) {
            player.getBlastFurnace().processCooling();
        }
    }

    @Override
    public void leave(final Player player, final boolean logout) {
        player.getInterfaceHandler().closeInterface(GameInterface.BLAST_FURNACE_COFFER);
    }

    @Override
    public String name() {
        return "Blast Furnace";
    }
}
