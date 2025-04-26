package com.zenyte.game.content.kebos.alchemicalhydra.model;

import com.zenyte.game.content.kebos.alchemicalhydra.HydraPhase;
import com.zenyte.game.content.kebos.alchemicalhydra.instance.AlchemicalHydraInstance;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.AlchemicalHydra;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 04/11/2019 | 19:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class VentObject extends WorldObject {
    private final AlchemicalVent vent;

    public VentObject(final AlchemicalVent vent, final AlchemicalHydraInstance instance) {
        super(vent.getObjectId(), 10, 0, instance.getLocation(vent.getLocation()));
        this.vent = vent;
    }

    public boolean isOnVent(final AlchemicalHydra hydra) {
        return CollisionUtil.collides(hydra.getLocation(), hydra.getSize(), this, 1, 0);
    }

    public boolean isCorrectVent(final AlchemicalHydra hydra) {
        final HydraPhase phase = hydra.getPhase();
        return vent.getWeakeningPhase().equals(phase);
    }
}
