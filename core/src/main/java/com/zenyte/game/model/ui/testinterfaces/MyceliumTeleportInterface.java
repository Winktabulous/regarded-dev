package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tommeh | 19/07/2019 | 16:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class MyceliumTeleportInterface extends Interface {
    private static final Map<Integer, Location> TELEPORTS = new HashMap<Integer, Location>() {
        {
            put(0, new Location(3765, 3879, 1));
            put(1, new Location(3757, 3757, 0));
            put(2, new Location(3676, 3755, 0));
            put(3, new Location(3676, 3871, 0));
        }
    };

    @Override
    protected void attach() {
        put(5, "Teleport 1 Text");
        put(9, "Teleport 2 Text");
        put(13, "Teleport 3 Text");
        put(17, "Teleport 4 Text");
        put(4, "Teleport 1");
        put(8, "Teleport 2");
        put(12, "Teleport 3");
        put(16, "Teleport 4");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Teleport 1 Text"), "House on the Hill");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Teleport 2 Text"), "Verdant Valley");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Teleport 3 Text"), "Sticky Swamp");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Teleport 4 Text"), "Mushroom Meadow");
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        player.getPacketDispatcher().sendClientScript(2158);
    }

    @Override
    protected void build() {
        bind("Teleport 1", player -> teleport(player, 0));
        bind("Teleport 2", player -> teleport(player, 1));
        bind("Teleport 3", player -> teleport(player, 2));
        bind("Teleport 4", player -> teleport(player, 3));
    }

    private void teleport(final Player player, final int index) {
        final Location destination = TELEPORTS.get(index);
        if (player.getPosition().getTileDistance(destination) < 5) {
            player.sendMessage("You're already at that Magic Mushtree.");
            GameInterface.MYCELIUM_TELEPORTATION.open(player);
            return;
        }
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        new FadeScreen(player, () -> {
            if (destination != null) {
                player.setLocation(destination);
            }
        }).fade(2);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.MYCELIUM_TELEPORTATION;
    }
}
