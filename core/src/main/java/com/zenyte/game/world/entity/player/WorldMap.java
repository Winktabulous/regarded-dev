package com.zenyte.game.world.entity.player;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.Location;

public class WorldMap {

    private final Player player;

    private PaneType previousPane;

    private boolean visible, fullScreen;

    public WorldMap(final Player player) {
        this.player = player;
    }

    public void updateLocation() {
        updateLocation(player.getLocation());
    }

    public void updateLocation(Location location) {
        player.getPacketDispatcher().sendClientScript(1749,
                location.getPositionHash(),
                player.getGravestone().getGravestoneLocationBitpacked());
    }

    public void close() {
        visible = false;
        fullScreen = false;
        if (player.getInterfaceHandler().getPane().equals(PaneType.FULL_SCREEN)) {
            player.getInterfaceHandler().sendPane(PaneType.FULL_SCREEN, player.getWorldMap().getPreviousPane());
        }
        player.getInterfaceHandler().closeInterface(InterfacePosition.WORLD_MAP);
    }

    public void sendFullScreenWorldMap() {
        visible = true;
        previousPane = player.getInterfaceHandler().getPane();
        updateLocation();
        player.getInterfaceHandler().sendPane(previousPane, PaneType.FULL_SCREEN);
        player.getInterfaceHandler().sendInterface(InterfacePosition.WORLD_MAP, 595);
        player.getInterfaceHandler().sendInterface(594, 27, PaneType.FULL_SCREEN, false);
        player.getPacketDispatcher().sendComponentSettings(595, 17, 0, 4, AccessMask.CLICK_OP1);
    }

    public void sendFloatingWorldMap() {
        visible = true;
        updateLocation();
        player.getInterfaceHandler().sendInterface(InterfacePosition.WORLD_MAP, 595);
        player.getPacketDispatcher().sendComponentSettings(595, 17, 0, 4, AccessMask.CLICK_OP1);
    }

    public PaneType getPreviousPane() {
        return previousPane;
    }

    public void setPreviousPane(PaneType previousPane) {
        this.previousPane = previousPane;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

}
