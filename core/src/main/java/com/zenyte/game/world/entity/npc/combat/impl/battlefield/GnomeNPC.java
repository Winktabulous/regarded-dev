package com.zenyte.game.world.entity.npc.combat.impl.battlefield;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 25/11/2018 13:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GnomeNPC extends NPC implements Spawnable {

    private static final IntArrayList list = new IntArrayList(new int[] {4977, 5971, 4975, 5968, 5967, 5970, 5969,
            5972, 4976, 5966, 5973, 6095, 6096, 4979, 6076});

    public GnomeNPC(final int id, final Location tile, final Direction facing, final int radius) {
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
        return target instanceof KhazardTrooperNPC;
    }

    @Override
    public boolean validate(int id, String name) {
        return list.contains(id);
    }
}
