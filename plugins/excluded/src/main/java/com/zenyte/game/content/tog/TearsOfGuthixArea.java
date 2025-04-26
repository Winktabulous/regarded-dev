package com.zenyte.game.content.tog;

import com.zenyte.game.content.skills.magic.spells.MagicSpell;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.SpellPlugin;

public class TearsOfGuthixArea extends PolygonRegionArea implements SpellPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(
                        new int[][]{
                                { 3200, 9536 },
                                { 3200, 9472 },
                                { 3264, 9472 },
                                { 3264, 9536 }
                        })
        };
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public boolean canCast(final Player player, final MagicSpell spell) {
        return false;
    }

    @Override
    public String name() {
        return "Tears of Guthix";
    }
}
