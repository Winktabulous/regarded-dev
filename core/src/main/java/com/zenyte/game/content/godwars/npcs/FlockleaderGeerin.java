package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 21/08/2019 01:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FlockleaderGeerin extends GodwarsBossMinion implements Spawnable, CombatScript {
    public FlockleaderGeerin(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 3164;
    }

    private static final Projectile javelin = new Projectile(1192, 99, 30, 30, 10, 3, 0, 5);
    private static final SoundEffect attackSound = new SoundEffect(2699, 10, 0);

    @Override
    public int attack(final Entity target) {
        final FlockleaderGeerin npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        World.sendSoundEffect(getMiddleLocation(), attackSound);
        delayHit(npc, World.sendProjectile(npc, target, javelin), target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
