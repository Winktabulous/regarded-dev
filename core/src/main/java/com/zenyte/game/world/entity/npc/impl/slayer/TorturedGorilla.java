package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 05/08/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TorturedGorilla extends CrashSiteGorilla implements CombatScript, Spawnable {
    public TorturedGorilla(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    int failedHitsUntilSwitch() {
        return 4;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.TORTURED_GORILLA_7150;
    }
}
