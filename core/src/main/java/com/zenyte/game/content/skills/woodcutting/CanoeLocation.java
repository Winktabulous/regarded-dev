package com.zenyte.game.content.skills.woodcutting;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 7 feb. 2018 : 16:16:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public enum CanoeLocation {
    LUMBRIDGE(new Location(3240, 3242, 0), 32),
    CHAMPIONS_GUILD(new Location(3199, 3344, 0), 33),
    BARBARIAN_VILLAGE(new Location(3131, 3412, 0), 34),
    EDGEVILLE(new Location(3142, 3475, 0), 39),
    WILDERNESS(new Location(3141, 3796, 0), 35);
    private final Location tile;
    private final int componentId;
    private static final CanoeLocation[] VALUES = values();
    private static final Map<Integer, CanoeLocation> LOCATIONS = new HashMap<>();

    static {
        for (final CanoeLocation location : VALUES) {
            LOCATIONS.put(location.getComponentId(), location);
        }
    }

    CanoeLocation(Location tile, int componentId) {
        this.tile = tile;
        this.componentId = componentId;
    }

    public static CanoeLocation get(final int componentId) {
        return LOCATIONS.get(componentId);
    }

    public static void sendInterfaceConfiguration(final Player player, final int bit) {
        final CanoeDefinitions canoe = CanoeDefinitions.getDefinitionByBitValue(bit);
        if (canoe == null) {
            return;
        }
        int[][] componentVisibility = null;
        switch (canoe) {
            case LOG:
                if (player.getLocation().withinDistance(LUMBRIDGE.getTile(), 10))
                    componentVisibility = new int[][]{{11, 15, 31, 38}, {13, 50}};
                else if (player.getLocation().withinDistance(CHAMPIONS_GUILD.getTile(), 10))
                    componentVisibility = new int[][]{{13, 31, 38}, {11, 15, 47}};
                else if (player.getLocation().withinDistance(BARBARIAN_VILLAGE.getTile(), 10))
                    componentVisibility = new int[][]{{11, 15, 31}, {13, 38, 44}};
                else if (player.getLocation().withinDistance(EDGEVILLE.getTile(), 10))
                    componentVisibility = new int[][]{{11, 13, 31, 38}, {15, 36}};
                break;
            case DUGOUT:
                if (player.getLocation().withinDistance(LUMBRIDGE.getTile(), 10))
                    componentVisibility = new int[][]{{11, 31, 38}, {13, 15, 50}};
                else if (player.getLocation().withinDistance(CHAMPIONS_GUILD.getTile(), 10))
                    componentVisibility = new int[][]{{13, 31}, {11, 15, 38, 47}};
                else if (player.getLocation().withinDistance(BARBARIAN_VILLAGE.getTile(), 10))
                    componentVisibility = new int[][]{{15, 31}, {11, 13, 38, 44}};
                else if (player.getLocation().withinDistance(EDGEVILLE.getTile(), 10))
                    componentVisibility = new int[][]{{11, 31, 38}, {13, 15, 36}};
                break;
            case STABLE_DUGOUT:
                if (player.getLocation().withinDistance(LUMBRIDGE.getTile(), 10))
                    componentVisibility = new int[][]{{11, 31}, {13, 15, 38, 50}};
                else if (player.getLocation().withinDistance(CHAMPIONS_GUILD.getTile(), 10))
                    componentVisibility = new int[][]{{13, 31}, {11, 15, 38, 47}};
                else if (player.getLocation().withinDistance(BARBARIAN_VILLAGE.getTile(), 10))
                    componentVisibility = new int[][]{{15, 31}, {11, 13, 38, 44}};
                else if (player.getLocation().withinDistance(EDGEVILLE.getTile(), 10))
                    componentVisibility = new int[][]{{31, 38}, {11, 13, 15, 36}};
                break;
            case WAKA:
                if (player.getLocation().withinDistance(LUMBRIDGE.getTile(), 10))
                    componentVisibility = new int[][]{{11}, {13, 15, 31, 38, 50}};
                else if (player.getLocation().withinDistance(CHAMPIONS_GUILD.getTile(), 10))
                    componentVisibility = new int[][]{{13}, {11, 15, 31, 38, 47}};
                else if (player.getLocation().withinDistance(BARBARIAN_VILLAGE.getTile(), 10))
                    componentVisibility = new int[][]{{15}, {11, 13, 31, 38, 44}};
                else if (player.getLocation().withinDistance(EDGEVILLE.getTile(), 10))
                    componentVisibility = new int[][]{{38}, {11, 13, 15, 31, 36}};
                break;
            default:
                break;
        }
        if (componentVisibility == null) {
            return;
        }
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 57);
        for (int i = 0; i < componentVisibility[0].length; i++) {
            player.getPacketDispatcher().sendComponentVisibility(57, componentVisibility[0][i], true);
        }
        for (int i = 0; i < componentVisibility[1].length; i++) {
            player.getPacketDispatcher().sendComponentVisibility(57, componentVisibility[1][i], false);
        }
    }

    public Location getTile() {
        return tile;
    }

    public int getComponentId() {
        return componentId;
    }
}
