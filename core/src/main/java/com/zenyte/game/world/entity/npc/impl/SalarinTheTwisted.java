package com.zenyte.game.world.entity.npc.impl;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SalarinTheTwisted extends NPC implements Spawnable {
    public SalarinTheTwisted(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.maxDistance = 8;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.SALARIN_THE_TWISTED;
    }

    @Override
    public float getXpModifier(final Hit hit) {
        final Object weapon = hit.getWeapon();
        if (weapon == CombatSpell.WIND_STRIKE || weapon == CombatSpell.EARTH_STRIKE || weapon == CombatSpell.WATER_STRIKE || weapon == CombatSpell.FIRE_STRIKE) {
            if (hit.getDamage() > 0) {
                hit.setDamage(Math.min(getHitpoints(), ((CombatSpell) weapon).getMaxHit()));
            }
            return 1;
        }
        hit.setDamage(0);
        if (Utils.random(4) == 0) {
            setForceTalk("Your pitiful attacks cannot hurt me!");
        }
        return 1;
    }
}
