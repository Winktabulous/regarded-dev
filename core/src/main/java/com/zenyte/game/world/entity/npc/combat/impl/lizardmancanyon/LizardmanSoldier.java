package com.zenyte.game.world.entity.npc.combat.impl.lizardmancanyon;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 09/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LizardmanSoldier extends NPC implements Spawnable {

    private static final IntArrayList list = new IntArrayList(new int[] {6868, 6869, 6870, 6871});

    public LizardmanSoldier(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
        setTargetType(EntityType.NPC);
    }

    @Override
    public NPC spawn() {
        super.spawn();
        this.combatDefinitions.setAggressionType(AggressionType.ALWAYS_AGGRESSIVE);
        return this;
    }

    @Override
    public boolean isAcceptableTarget(final Entity target) {
        return target instanceof LizardmanBrute;
    }

    @Override
    public boolean validate(int id, String name) {
        return list.contains(id);
    }
}
