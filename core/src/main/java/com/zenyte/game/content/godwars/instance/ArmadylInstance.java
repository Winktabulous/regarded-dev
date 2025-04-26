package com.zenyte.game.content.godwars.instance;

import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.content.godwars.GodwarsInstancePortal;
import com.zenyte.game.content.godwars.npcs.FlockleaderGeerin;
import com.zenyte.game.content.godwars.npcs.GodwarsBossMinion;
import com.zenyte.game.content.godwars.npcs.KreeArra;
import com.zenyte.game.content.godwars.npcs.WingmanSkree;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfBoundaryException;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kris | 15/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ArmadylInstance extends GodwarsInstance {
    private static final int ROTATION = 3;
    private final Location loginLocation = new ImmutableLocation(2830, 5288, 2);

    public ArmadylInstance(@NotNull final String clan, @NotNull final AllocatedArea allocatedArea) {
        super(clan, GodType.ARMADYL, allocatedArea, GodwarsInstancePortal.ARMADYL.getChunkX(), GodwarsInstancePortal.ARMADYL.getChunkY());
    }

    @Override
    protected IntList possibleKillcountMonsters() {
        return new IntArrayList(new int[] {
                NpcId.AVIANSIE_3171, NpcId.AVIANSIE_3174,
                NpcId.AVIANSIE_3175, NpcId.AVIANSIE_3176,
                NpcId.AVIANSIE_3182,
                NpcId.WEREWOLF_3136, NpcId.WEREWOLF_3136,
                NpcId.BLOODVELD_3138,
                NpcId.GORAK_3141,
                NpcId.SPIRITUAL_WARRIOR_3166,
                NpcId.SPIRITUAL_MAGE_3168}
        );
    }

    @Override
    protected List<Location> possibleSpawnTiles() {
        return List.of(
                new ImmutableLocation(1172, 4226, 0), new ImmutableLocation(1175, 4227, 0),
                new ImmutableLocation(1173, 4230, 0), new ImmutableLocation(1174, 4233, 0),
                new ImmutableLocation(1177, 4235, 0), new ImmutableLocation(1178, 4231, 0),
                new ImmutableLocation(1179, 4228, 0), new ImmutableLocation(1180, 4236, 0),
                new ImmutableLocation(1182, 4231, 0), new ImmutableLocation(1184, 4227, 0),
                new ImmutableLocation(1186, 4229, 0), new ImmutableLocation(1188, 4226, 0),
                new ImmutableLocation(1189, 4232, 0), new ImmutableLocation(1192, 4233, 0),
                new ImmutableLocation(1195, 4233, 0), new ImmutableLocation(1195, 4229, 0)
        );
    }

    @Override
    public Location onLoginLocation() {
        return RandomLocation.create(loginLocation,  1);
    }

    @Override
    public void constructed() {
        super.constructed();
        minions = new GodwarsBossMinion[] {new GodwarsBossMinion(NpcId.FLIGHT_KILISA, getLocation(new Location(1204, 4243, 0)), Direction.SOUTH, 5), new WingmanSkree(NpcId.WINGMAN_SKREE, getLocation(new Location(1201, 4238, 0)), Direction.SOUTH, 5), new FlockleaderGeerin(NpcId.FLOCKLEADER_GEERIN, getLocation(new Location(1203, 4245, 0)), Direction.SOUTH, 5)};
        final KreeArra kree = new KreeArra(minions, NpcId.KREEARRA, getLocation(new ImmutableLocation(1207, 4234, 0)), Direction.SOUTH, 2);
        killcountNPCS.add(kree.spawn());
        killcountNPCS.addAll(Arrays.asList(minions));
        final Location southWestCorner = getLocation(1199, 4229, 0);
        final Location northEastCorner = getLocation(1213, 4252, 0);
        polygon = new RSPolygon(new int[][] {{southWestCorner.getX(), northEastCorner.getY()}, {southWestCorner.getX(), southWestCorner.getY()}, {northEastCorner.getX(), southWestCorner.getY()}, {northEastCorner.getX(), northEastCorner.getY()}});
    }

    @Override
    protected void destroyed() {
    }

    @Override
    protected void build() {
        try {
            //copySquare(final AllocatedArea allocated, final int ratio, final int fromChunkX,
            // final int fromChunkY, final int fromPlane, final int toChunkX, final int toChunkY, final int toPlane, final int rotation)
            for (int i = 0; i < 4; i++) {
                MapBuilder.copySquare(area, 8, staticChunkX, staticChunkY, i, chunkX, chunkY, i, ROTATION);
            }
        } catch (
        //MapBuilder.copyAllPlanesMap(area, staticChunkX, staticChunkY, chunkX, chunkY, sizeX, sizeY);
        OutOfBoundaryException e) {
            destroyRegion();
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Location getLocation(final int x, final int y, final int height) {
        final int offsetX = x - (staticChunkX << 3);
        final int offsetY = y - (staticChunkY << 3);
        final double radians = Math.toRadians(90);
        final double regionHalf = 31.5;
        final int transformedX = (int) Math.round(regionHalf + ((offsetX - regionHalf) * Math.cos(radians)) - ((offsetY - regionHalf) * Math.sin(radians)));
        final int transformedY = (int) Math.round(regionHalf + ((offsetX - regionHalf) * Math.sin(radians)) + ((offsetY - regionHalf) * Math.cos(radians)));
        return new Location((chunkX << 3) + transformedX, (chunkY << 3) + transformedY, height);
    }

    @Override
    public Location getLocation(final Location tile) {
        return getLocation(tile.getX(), tile.getY(), tile.getPlane());
    }

    @Override
    public String name() {
        return "Armadyl Instance (CC: " + clan + ")";
    }
}
