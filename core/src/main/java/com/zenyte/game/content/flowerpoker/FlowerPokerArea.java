package com.zenyte.game.content.flowerpoker;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LayableObjectPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Jire
 */
public final class FlowerPokerArea extends PolygonRegionArea implements LayableObjectPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(13420)};
    }

    @Override
    public void enter(Player player) {
        player.setPlayerGambleable(true);
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.setPlayerGambleable(false);
    }

    @Override
    public String name() {
        return "Flower Poker Area";
    }

    @Override
    public boolean canLay(@NotNull Player player, @NotNull LayableObjectType type) {
        player.sendMessage("You can not do this here.");
        return false;
    }

}
