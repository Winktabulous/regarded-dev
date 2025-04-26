package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 28-10-2018 | 19:26
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class HouseOptionsTabInterface extends Interface {

    @Override
    protected void attach() {
        put(1, "Open house viewer");
        put(5, "Building mode ON");
        put(6, "Building mode OFF");
        put(8, "Teleport inside ON");
        put(9, "Teleport inside OFF");
        put(12, "Render doors closed");
        put(14, "Render doors open");
        put(16, "Render no doors");
        put(20, "Number of rooms");
    }

    @Override
    public void open(Player player) {
        if (player.isUnderCombat()) {
            player.sendMessage("You can't do this while in combat.");
            return;
        }
        if (!GameConstants.isOwner(player)) {
            player.sendMessage("House viewer is not yet ready.");
            return;
        }
        player.getInterfaceHandler().closeInterfaces();
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Number of rooms"), "Number of rooms: " + player.getConstruction().getAmountOfRooms());
        player.getConstruction().refreshHouseOptions();
    }

    @Override
    protected void build() {
        bind("Open house viewer", player -> player.getConstruction().getHouseViewer().openHouseViewer());
        bind("Building mode ON", player -> player.getConstruction().setBuildingMode(true));
        bind("Building mode OFF", player -> player.getConstruction().setBuildingMode(false));
        bind("Teleport inside ON", player -> player.getConstruction().setTeleportInside(true));
        bind("Teleport inside OFF", player -> player.getConstruction().setTeleportInside(false));
        bind("Render doors open", player -> player.getConstruction().setRenderDoorsOpen(true));
        bind("Render doors closed", player -> player.getConstruction().setRenderDoorsOpen(false));
        bind("Render no doors", player -> player.getConstruction().setRenderDoorsOpen(false));//TODO
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.HOUSE_OPTIONS_TAB;
    }
}
