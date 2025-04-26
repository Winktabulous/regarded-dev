package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;

/**
 * @author Kris | 20/08/2019 23:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class InfernalMage extends NPC implements Spawnable, CombatScript {
    public InfernalMage(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("Infernal mage");
    }

    @Override
    public int attack(final Entity target) {
        attackSound();
        useSpell(CombatSpell.FIRE_BLAST, target, combatDefinitions.getMaxHit());
        return combatDefinitions.getAttackSpeed();
    }
}
