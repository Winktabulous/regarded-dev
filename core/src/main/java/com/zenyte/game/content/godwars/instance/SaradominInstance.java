package com.zenyte.game.content.godwars.instance;

import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.content.godwars.GodwarsInstancePortal;
import com.zenyte.game.content.godwars.npcs.Bree;
import com.zenyte.game.content.godwars.npcs.CommanderZilyana;
import com.zenyte.game.content.godwars.npcs.GodwarsBossMinion;
import com.zenyte.game.content.godwars.npcs.Growler;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
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
 * @author Kris | 17/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SaradominInstance extends GodwarsInstance {
    private final Location loginLocation = new ImmutableLocation(2926, 5260, 0);

    public SaradominInstance(@NotNull final String clan, @NotNull final AllocatedArea allocatedArea) {
        super(clan, GodType.SARADOMIN, allocatedArea, GodwarsInstancePortal.SARADOMIN.getChunkX(), GodwarsInstancePortal.SARADOMIN.getChunkY());
    }

    @Override
    protected IntList possibleKillcountMonsters() {
        return new IntArrayList(new int[] {NpcId.WEREWOLF_3135, NpcId.SARADOMIN_PRIEST, NpcId.KNIGHT_OF_SARADOMIN, NpcId.SPIRITUAL_WARRIOR, NpcId.SPIRITUAL_MAGE, NpcId.GORAK_3141, NpcId.SPIRITUAL_RANGER, NpcId.ICEFIEND});
    }

    @Override
    protected List<Location> possibleSpawnTiles() {
        return Collections.unmodifiableList(Arrays.asList(new ImmutableLocation(1190, 4441, 0), new ImmutableLocation(1189, 4439, 0), new ImmutableLocation(1187, 4439, 0), new ImmutableLocation(1185, 4438, 0), new ImmutableLocation(1182, 4438, 0), new ImmutableLocation(1182, 4436, 0), new ImmutableLocation(1182, 4434, 0), new ImmutableLocation(1184, 4433, 0), new ImmutableLocation(1182, 4432, 0), new ImmutableLocation(1185, 4435, 0), new ImmutableLocation(1187, 4437, 0)));
    }

    @Override
    public Location onLoginLocation() {
        return RandomLocation.create(loginLocation,  1);
    }

    @Override
    public void constructed() {
        super.constructed();
        minions = new GodwarsBossMinion[] {new GodwarsBossMinion(NpcId.STARLIGHT, getLocation(new Location(1173, 4432, 0)), Direction.SOUTH, 5), new Growler(NpcId.GROWLER, getLocation(new Location(1169, 4431, 0)), Direction.SOUTH, 5), new Bree(NpcId.BREE, getLocation(new Location(1167, 4433, 0)), Direction.SOUTH, 5)};
        final CommanderZilyana zilyana = new CommanderZilyana(minions, NpcId.COMMANDER_ZILYANA, getLocation(new ImmutableLocation(1172, 4437, 0)), Direction.SOUTH, 2);
        killcountNPCS.add(zilyana.spawn());
        killcountNPCS.addAll(Arrays.asList(minions));
        polygon = new RSPolygon(new int[][] {{getX(1154), getY(4444)}, {getX(1154), getY(4425)}, {getX(1180), getY(4425)}, {getX(1180), getY(4444)}});
    }

    @Override
    protected void destroyed() {
    }

    @Override
    public RSPolygon chamberPolygon() {
        return polygon;
    }

    @Override
    public String name() {
        return "Saradomin Instance (CC: " + clan + ")";
    }
}
