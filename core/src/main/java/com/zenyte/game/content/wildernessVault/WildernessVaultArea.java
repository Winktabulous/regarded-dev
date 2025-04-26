package com.zenyte.game.content.wildernessVault;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.*;

public final class WildernessVaultArea extends PolygonRegionArea implements TeleportPlugin, DeathPlugin, LoginPlugin, LogoutRestrictionPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(7789)};
    }

    @Override
    public void login(Player player) {
        player.setLocation(getInactiveLocation());
        player.getVariables().schedule(250, TickVariable.TELEBLOCK);
        player.getVariables().schedule(350, TickVariable.TELEBLOCK_IMMUNITY);
        player.setAttackedTick(WorldThread.getCurrentCycle());
    }

    @Override
    public void enter(Player player) {
        GameInterface.WILDERNESS_VAULT_HUD.open(player);
        if(WildernessVaultHandler.getInstance().getVaultStatus() == WildernessVaultStatus.INACTIVE)
            player.setLocation(getInactiveLocation());
    }

    private static Location getInactiveLocation() {
        VaultSpawnDefinition spawn = WildernessVaultHandler.getInstance().getCurrentSpawn();
        if (spawn != null) {
            return spawn.locationPlayer();
        }

        return new Location(3087, 3491);
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

    @Override
    public void leave(Player player, boolean logout) {
        WildernessVaultHandler.resetPlayer(player);
    }

    @Override
    public String name() {
        return "Wilderness Vault Area";
    }

    @Override
    public boolean canTeleport(Player player, Teleport teleport) {
        player.sendMessage("You can't teleport out of here.");
        return false;
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
    public Location gravestoneLocation() {
        return WildernessVaultHandler.getInstance().getCurrentSpawn().locationPlayer();
    }

    @Override
    public boolean manualLogout(Player player) {
        player.sendMessage("You cannot logout in this unsafe area!");
        return false;
    }
}
