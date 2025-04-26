package com.zenyte.game.content.boss.bryophyta;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.LogoutPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 17/05/2019 | 15:07
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class BryophytaInstance extends DynamicArea implements LogoutPlugin, LootBroadcastPlugin {

    private final transient Player player;
    private transient Bryophyta bryophyta;

    private static final Location INSIDE_BOSROOM = new Location(3214, 9937, 0);
    public static final Location OUTSIDE_INSTANCE = new Location(3174, 9900, 0);

    public BryophytaInstance(final Player player, final AllocatedArea area) {
        super(area, 400, 1240);
        this.player = player;
    }

    @Override
    public void constructed() {
        player.unlock();
        player.setLocation(getLocation(INSIDE_BOSROOM));
        player.sendMessage("Your key fits the gate, causing it to swing open.");
        bryophyta = (Bryophyta) new Bryophyta(getLocation(3220, 9933, 0), this).spawn();
        player.addTemporaryAttribute("growthling_info_msg", 0);
    }

    @Override
    public void enter(Player player) {
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
        return player.getName() + "'s Bryophyta instance";
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

}
