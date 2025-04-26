package com.zenyte.game.content.gravestones;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 13/06/2022
 */
public class GravestoneNPC extends NPC {
    private static final List<Integer> availableGravestones;
    static {
        List<Integer> gravestones = new ArrayList<>(256);
        for (int i = 0; i < 256; i++) {
            gravestones.add(i);
        }
        availableGravestones = gravestones;
    }

    @Override
    public void finish() {
        returnGravestone();
        super.finish();
    }

    private void returnGravestone() {
        final int slot = this.id - (this.id >= 10113 ? 10113 : 9858);
        if (availableGravestones.contains(slot)) return;
        availableGravestones.add(slot);
    }

    public GravestoneNPC(final int type, int slot, Location location) {
        super((type == 0 ? 9858 : 10113) + slot, location, true);
        this.radius = 0;
    }

    public static int allocateGravestone() {
        int id = availableGravestones.isEmpty() ? 0 : availableGravestones.get(0);
        availableGravestones.remove(Integer.valueOf(id));
        return id;
    }
}
