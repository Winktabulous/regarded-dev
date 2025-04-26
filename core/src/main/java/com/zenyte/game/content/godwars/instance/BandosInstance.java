package com.zenyte.game.content.godwars.instance;

import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.content.godwars.GodwarsInstancePortal;
import com.zenyte.game.content.godwars.npcs.GeneralGraardor;
import com.zenyte.game.content.godwars.npcs.GodwarsBossMinion;
import com.zenyte.game.content.godwars.npcs.SergeantGrimspike;
import com.zenyte.game.content.godwars.npcs.SergeantSteelwill;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kris | 14/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BandosInstance extends GodwarsInstance {
    private final Location loginLocation = new ImmutableLocation(2857, 5363, 2);

    public BandosInstance(@NotNull final String clan, @NotNull final AllocatedArea allocatedArea) {
        super(clan, GodType.BANDOS, allocatedArea, GodwarsInstancePortal.BANDOS.getChunkX(), GodwarsInstancePortal.BANDOS.getChunkY());
    }

    @Override
    protected IntList possibleKillcountMonsters() {
        return new IntArrayList(new int[] {NpcId.GOBLIN_2245, NpcId.GOBLIN_2245, NpcId.GOBLIN_2245, NpcId.GOBLIN_2246, NpcId.GOBLIN_2246, NpcId.GOBLIN_2246, NpcId.SPIRITUAL_MAGE_2244, NpcId.SPIRITUAL_MAGE_2244, NpcId.JOGRE_2234, NpcId.JOGRE_2234, NpcId.ORK_2240, NpcId.ORK_2240, NpcId.BLOODVELD_3138, NpcId.CYCLOPS_2235});
    }

    @Override
    protected List<Location> possibleSpawnTiles() {
        return Collections.unmodifiableList(Arrays.asList(new ImmutableLocation(1175, 4295, 0), new ImmutableLocation(1176, 4298, 0), new ImmutableLocation(1179, 4295, 0), new ImmutableLocation(1182, 4297, 0), new ImmutableLocation(1179, 4301, 0), new ImmutableLocation(1183, 4300, 0), new ImmutableLocation(1184, 4295, 0), new ImmutableLocation(1184, 4303, 0), new ImmutableLocation(1186, 4305, 0), new ImmutableLocation(1188, 4302, 0), new ImmutableLocation(1190, 4304, 0), new ImmutableLocation(1193, 4302, 0), new ImmutableLocation(1190, 4300, 0), new ImmutableLocation(1191, 4297, 0), new ImmutableLocation(1189, 4295, 0), new ImmutableLocation(1185, 4300, 0), new ImmutableLocation(1192, 4299, 0), new ImmutableLocation(1193, 4304, 0), new ImmutableLocation(1184, 4306, 0), new ImmutableLocation(1179, 4303, 0)));
    }

    @Override
    public Location onLoginLocation() {
        return RandomLocation.create(loginLocation,  1);
    }

    @Override
    public void constructed() {
        super.constructed();
        minions = new GodwarsBossMinion[] {new GodwarsBossMinion(NpcId.SERGEANT_STRONGSTACK, getLocation(new Location(1203, 4311, 0)), Direction.SOUTH, 5), new SergeantSteelwill(NpcId.SERGEANT_STEELWILL, getLocation(new Location(1206, 4308, 0)), Direction.SOUTH, 5), new SergeantGrimspike(NpcId.SERGEANT_GRIMSPIKE, getLocation(new Location(1207, 4303, 0)), Direction.SOUTH, 5)};
        final GeneralGraardor graardor = new GeneralGraardor(minions, NpcId.GENERAL_GRAARDOR, getLocation(new ImmutableLocation(1205, 4308, 0)), Direction.SOUTH, 2);
        killcountNPCS.add(graardor.spawn());
        killcountNPCS.addAll(Arrays.asList(minions));
        polygon = new RSPolygon(new int[][] {{getX(1198), getY(4323)}, {getX(1198), getY(4299)}, {getX(1213), getY(4299)}, {getX(1213), getY(4323)}});
    }

    @Override
    protected void destroyed() {
        for (final NPC npc : killcountNPCS) {
            npc.finish();
        }
    }

    @Override
    public String name() {
        return "Bandos Instance (CC: " + clan + ")";
    }
}
