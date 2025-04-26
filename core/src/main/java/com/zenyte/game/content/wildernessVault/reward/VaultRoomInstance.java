package com.zenyte.game.content.wildernessVault.reward;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.wildernessVault.WildernessVaultHandler;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity._Location;
import com.zenyte.game.world.entity.pathfinding.Flags;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.LoginPlugin;
import com.zenyte.game.world.region.area.plugins.LogoutPlugin;
import com.zenyte.game.world.region.area.plugins.LogoutRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.TeleportPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

public class VaultRoomInstance extends DynamicArea implements LogoutPlugin, LogoutRestrictionPlugin, LoginPlugin, TeleportPlugin {

    public static final ObjectArrayList<Player> playersInside = new ObjectArrayList<>();
    private static final Location[] COORDS = {
            new Location(2129, 7002), new Location(2129, 7003), new Location(2129, 7004), new Location(2130, 7004),
            new Location(2131, 7004), new Location(2132, 7004), new Location(2133, 7004), new Location(2133, 7003),
            new Location(2133, 7002), new Location(2133, 7001)
    };

    public VaultRoomInstance(AllocatedArea allocatedArea) {
        super(allocatedArea, 2113 / 8, 6977 / 8);
    }

    @Override
    public void constructed() {
        for (Location location : COORDS) {
            Location transformed = getLocation(location);
            int x = transformed.getX();
            int y = transformed.getY();
            World.getRegion(_Location.getRegionId(x, y), true).addFlag(0, x & 63, y & 63, Flags.FLOOR);
        }
    }

    @Override
    public Location onLoginLocation() {
        return WildernessVaultHandler.getInstance().getCurrentSpawn().locationPlayer();
    }

    @Override
    public void onLogout(final @NotNull Player player) {
        player.setLocation(WildernessVaultHandler.getInstance().getCurrentSpawn().locationPlayer());
    }

    @Override
    public void enter(Player player) {
        playersInside.add(player);
    }

    @Override
    public void leave(Player player, boolean logout) {
        playersInside.remove(player);
        WildernessVaultHandler.resetPlayer(player);
    }

    @Override
    public String name() {
        return "Vault reward room";
    }

    @Override
    public void login(Player player) {
        player.setLocation(WildernessVaultHandler.getInstance().getCurrentSpawn().locationPlayer());
        player.getVariables().schedule(250, TickVariable.TELEBLOCK);
        player.getVariables().schedule(350, TickVariable.TELEBLOCK_IMMUNITY);
        player.setAttackedTick(WorldThread.getCurrentCycle());
    }

    @Override
    public boolean manualLogout(Player player) {
        player.sendMessage("You cannot logout in this unsafe area!");
        return false;
    }

    @Override
    public boolean canTeleport(Player player, Teleport teleport) {
        player.sendMessage("You can't teleport out of here.");
        return false;
    }

}
