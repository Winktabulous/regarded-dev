package com.zenyte.game.content.boss.sarachnis;

import com.google.common.collect.ImmutableSet;
import com.zenyte.game.content.boss.kraken.Kraken;
import com.zenyte.game.content.skills.slayer.Assignment;
import com.zenyte.game.content.skills.slayer.BossTask;
import com.zenyte.game.content.skills.slayer.BossTaskSumona;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.TimeUtils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.Region;
import com.zenyte.game.world.region.area.plugins.*;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project Near-Reality
 * @social Discord: Glabay
 * @since 2024-09-7
 */
public class SarachnisInstance extends DynamicArea implements CycleProcessPlugin, EntityAttackPlugin, DeathPlugin, LogoutPlugin, LootBroadcastPlugin {
    private final Location OUTSIDE_TILE = new Location(1842, 9912, 0);
    private final Location INSIDE_TILE = new Location(1842, 9911, 0);
    private int elapsedTime = 0;

    @Override
    public void process() {
        elapsedTime++;
        if (elapsedTime >= 6_000) { // 60 min
            elapsedTime = 0;
            player.setLocation(OUTSIDE_TILE);
            destroyRegion();
        }
    }

    public SarachnisInstance(final Player player, final AllocatedArea allocatedArea) {
        super(allocatedArea, 1821 / 8, 9882 / 8);
        this.player = player;
    }

    private final Player player;

    private ImmutableSet<Location> getLocations() {
        return ImmutableSet.of(
            getLocation(1837, 9906, 0),
            getLocation(1837, 9896, 0),
            getLocation(1846, 9896, 0),
            getLocation(1846, 9905, 0)
        );
    }

    @Override
    public void constructed() {
        var spidy = new Sarachnis(NpcId.SARACHNIS, getLocation(1839, 9898, 0), Direction.SOUTH, 5);
            spidy.setWalkDestinations(getLocations());
            spidy.spawn();

        player.sendMessage(Colour.RED.wrap("Should you die in the instance, your items will be permanently lost!"));
        player.setLocation(getLocation(INSIDE_TILE));
    }

    @Override
    public void enter(Player player) {
        player.mapInstance = this;
        player.setForceMultiArea(true);
    }
    @Override
    public void leave(Player player, boolean logout) {
        player.setForceMultiArea(false);
        if (logout) {
            player.forceLocation(OUTSIDE_TILE);
        }
        player.mapInstance = null;
    }

    @Override
    public void onLogout(final @NotNull Player player) {
        player.setLocation(OUTSIDE_TILE);
    }

    @Override
    public Location onLoginLocation() {
        return OUTSIDE_TILE;
    }

    @Override
    public String name() {
        return player.getName() + "'s Sarachnis instance";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        return true;
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public String getDeathInformation() {
        return null;
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }


}
