package com.zenyte.game.content.creaturecreation;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Chris
 * @since August 30 2020
 */
public class CreatureCreationArea extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{3008, 4416}, {3072, 4416}, {3072, 4352}, {3008, 4352}})};
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
        final CreatedCreatureNpc creature = SymbolOfLifeActivateDialogue.playerCreature.get(player.getUsername());
        if (creature != null) {
            creature.finish();
        }
    }

    @Override
    public String name() {
        return "Creature Creation";
    }
}
