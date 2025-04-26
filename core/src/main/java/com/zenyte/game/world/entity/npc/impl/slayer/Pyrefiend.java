package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.godwars.npcs.SpawnableKillcountNPC;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;

/**
 * @author Christopher
 * @since 3/13/2020
 */
public class Pyrefiend extends SpawnableKillcountNPC implements CombatScript, Spawnable {
    public Pyrefiend(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(Entity target) {
        final NPCCombatDefinitions def = getCombatDefinitions();
        setAnimation(def.getAttackAnim());
        delayHit(this, 0, target, magicalMelee(target, def.getMaxHit()));
        return def.getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return name.equalsIgnoreCase("Pyrefiend");
    }
}
