package com.zenyte.game.content.godwars.instance;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.content.godwars.GodwarsInstancePortal;
import com.zenyte.game.content.godwars.npcs.BalfrugKreeyath;
import com.zenyte.game.content.godwars.npcs.GodwarsBossMinion;
import com.zenyte.game.content.godwars.npcs.KrilTsutsaroth;
import com.zenyte.game.content.godwars.npcs.ZaklNGritch;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.plugins.events.LoginEvent;
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
public class ZamorakInstance extends GodwarsInstance {
    private final Location loginLocation = new ImmutableLocation(2934, 5350, 2);

    public ZamorakInstance(@NotNull final String clan, @NotNull final AllocatedArea allocatedArea) {
        super(clan, GodType.ZAMORAK, allocatedArea, GodwarsInstancePortal.ZAMORAK.getChunkX(), GodwarsInstancePortal.ZAMORAK.getChunkY());
    }

    @Override
    protected IntList possibleKillcountMonsters() {
        return new IntArrayList(new int[] {NpcId.BLOODVELD_3138, NpcId.BLOODVELD_3138, NpcId.BLOODVELD_3138, NpcId.SPIRITUAL_WARRIOR_3159, NpcId.SPIRITUAL_WARRIOR_3159, NpcId.WEREWOLF_3136, NpcId.WEREWOLF_3136, NpcId.SPIRITUAL_MAGE_3161, NpcId.SPIRITUAL_MAGE_3161, NpcId.IMP, NpcId.IMP, NpcId.IMP, NpcId.IMP});
    }

    @Override
    protected List<Location> possibleSpawnTiles() {
        return Collections.unmodifiableList(Arrays.asList(new ImmutableLocation(1191, 4400, 0), new ImmutableLocation(1193, 4397, 0), new ImmutableLocation(1196, 4397, 0), new ImmutableLocation(1196, 4393, 0), new ImmutableLocation(1198, 4393, 0), new ImmutableLocation(1191, 4392, 0), new ImmutableLocation(1193, 4390, 0), new ImmutableLocation(1193, 4387, 0), new ImmutableLocation(1183, 4387, 0), new ImmutableLocation(1186, 4385, 0), new ImmutableLocation(1184, 4380, 0), new ImmutableLocation(1187, 4379, 0), new ImmutableLocation(1190, 4377, 0), new ImmutableLocation(1193, 4379, 0), new ImmutableLocation(1191, 4382, 0), new ImmutableLocation(1196, 4383, 0), new ImmutableLocation(1199, 4384, 0), new ImmutableLocation(1196, 4379, 0), new ImmutableLocation(1199, 4379, 0), new ImmutableLocation(1202, 4380, 0), new ImmutableLocation(1198, 4389, 0), new ImmutableLocation(1193, 4384, 0), new ImmutableLocation(1203, 4383, 0)));
    }

    @Override
    public Location onLoginLocation() {
        return RandomLocation.create(loginLocation,  1);
    }

    @Override
    public void constructed() {
        super.constructed();
        minions = new GodwarsBossMinion[] {new GodwarsBossMinion(NpcId.TSTANON_KARLAK, getLocation(new Location(1202, 4367, 0)), Direction.SOUTH, 5), new ZaklNGritch(NpcId.ZAKLN_GRITCH, getLocation(new Location(1194, 4367, 0)), Direction.SOUTH, 5), new BalfrugKreeyath(NpcId.BALFRUG_KREEYATH, getLocation(new Location(1196, 4364, 0)), Direction.SOUTH, 5)};
        final KrilTsutsaroth kril = new KrilTsutsaroth(minions, NpcId.KRIL_TSUTSAROTH, getLocation(new ImmutableLocation(1195, 4363, 0)), Direction.SOUTH, 2);
        killcountNPCS.add(kril.spawn());
        killcountNPCS.addAll(Arrays.asList(minions));
        polygon = new RSPolygon(new int[][] {{getX(1190), getY(4372)}, {getX(1190), getY(4357)}, {getX(1214), getY(4357)}, {getX(1214), getY(4372)}});
        lightPolygon = new RSPolygon(new int[][] {{getX(1203), getY(4400)}, {getX(1203), getY(4386)}, {getX(1211), getY(4386)}, {getX(1211), getY(4400)}});
        darkPolygon = new RSPolygon(new int[][] {{getX(1189), getY(4376)}, {getX(1189), getY(4356)}, {getX(1216), getY(4356)}, {getX(1216), getY(4376)}});
    }

    private RSPolygon lightPolygon;
    private RSPolygon darkPolygon;

    @Override
    protected void destroyed() {
        for (final NPC npc : killcountNPCS) {
            npc.finish();
        }
    }

    @Override
    public String name() {
        return "Zamorak Instance (CC: " + clan + ")";
    }

    private int getPolygonLightness(final int x, final int y) {
        if (darkPolygon.contains(x, y)) {
            return 2;
        }
        if (lightPolygon.contains(x, y)) {
            return 4;
        }
        return 3;
    }

    protected void updateStatus(@NotNull final Player player) {
        if (player.getVarManager().getBitValue(Setting.SARADOMIN_LIGHT.getId()) == 1) {
            super.updateStatus(player);
            return;
        }
       this.processMovement(player, player.getX(), player.getY());
    }

    @Override
    public boolean processMovement(final Player player, final int x, final int y) {
        final VarManager varManager = player.getVarManager();
        if (varManager.getBitValue(Setting.SARADOMIN_LIGHT.getId()) == 0) {
            final int lightness = getPolygonLightness(x, y);
            if (varManager.getValue(STATUS_VAR) != lightness) {
                varManager.sendVar(STATUS_VAR, lightness);
            }
        }
        return super.processMovement(player, x, y);
    }

    @Subscribe
    public static final void onLogin(final LoginEvent event) {
        event.getPlayer().getVarManager().sendVar(STATUS_VAR, 0);
    }
}
