package com.zenyte.game.content.boss.obor;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.LogoutPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 14/05/2019 | 10:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class OborInstance extends DynamicArea implements LogoutPlugin, LootBroadcastPlugin {

    private final transient Player player;
    private transient Obor obor;

    private static final Location OUTSIDE_BOSSROOM = new Location(3092, 9815, 0);
    private static final Location OUTSIDE_INSTANCE = new Location(3095, 9832, 0);

    public OborInstance(final Player player, final AllocatedArea area) {
        super(area, 384, 1224);
        this.player = player;
    }

    @Override
    public void constructed() {
        player.unlock();
        player.setLocation(getLocation(OUTSIDE_BOSSROOM));
        player.sendMessage("You use your key to unlock the gate.");
        obor = (Obor) new Obor(getLocation(3091, 9799, 0)).spawn();
    }

    @Override
    public void enter(Player player) {
        player.getPacketDispatcher().resetCamera();
    }

    @Override
    public void leave(Player player, boolean logout) {
        if (logout) {
            player.forceLocation(OUTSIDE_INSTANCE);
        }
    }

    @Override
    public void onLogout(final @NotNull Player player) {
        player.setLocation(getLocation(OUTSIDE_INSTANCE));
    }

    @Override
    public Location onLoginLocation() {
        return OUTSIDE_INSTANCE;
    }

    @Override
    public void cleared() {
        if (players.isEmpty()) {
            destroyRegion();
        }
    }

    @Override
    public String name() {
        return player.getName() + "'s Obor instance";
    }
}
