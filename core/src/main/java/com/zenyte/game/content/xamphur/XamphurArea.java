package com.zenyte.game.content.xamphur;


import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import kotlin.Pair;

/**
 * @author Tamatea
 */
public final class XamphurArea extends PolygonRegionArea implements CannonRestrictionPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(13422)};
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
        if(player.getHpHud() != null)
            player.getHpHud().close();

        final Xamphur xamphur = XamphurHandler.get().getXamphur();
        if (xamphur != null) {
            player.sendDeveloperMessage("Xamphur -> clearing your damage contributions to Xamphur.");
            xamphur.getReceivedDamage().remove(new Pair<>(player.getUsername().toLowerCase(), player.getGameMode()));
        }
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

    @Override
    public boolean safeHardcoreIronmanDeath() {
        return true;
    }

    @Override
    public String name() {
        return "Xamphur Event Boss";
    }

    @Override
    public void move(Player player) {
        for (Location darknessMark : Xamphur.marksOfDarkness) {
            if(player.getLocation().equals(darknessMark)) {
                Xamphur.markOfDarkness(player);
            }
        }
    }
}
