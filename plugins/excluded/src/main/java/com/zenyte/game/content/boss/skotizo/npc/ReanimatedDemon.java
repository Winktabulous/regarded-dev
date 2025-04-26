package com.zenyte.game.content.boss.skotizo.npc;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;

/**
 * @author Tommeh | 07/03/2020 | 11:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ReanimatedDemon extends NPC {

    private final Skotizo skotizo;

    public ReanimatedDemon(final Location location, final Skotizo skotizo) {
        super(NpcId.REANIMATED_DEMON_SPAWN, location, Direction.SOUTH, 128);
        this.skotizo = skotizo;
        this.maxDistance = this.aggressionDistance = 64;
        this.forceAggressive = true;
        this.randomWalkDelay = 3;
        this.spawned = true;
    }

    @Override
    protected void onDeath(Entity source) {
        super.onDeath(source);
        skotizo.getMinions().remove(this);
    }
}
