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
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 21/08/2019 00:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Aviansie extends SpawnableKillcountNPC implements Spawnable, CombatScript {
    private static final int[] javelinAviansie = new int[] {3163, 3164, 3182, 3179, 3171, 3167, 3181, 3178, 3180, 3175, 3170, 3173, 3172};

    public Aviansie(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("Aviansie");
    }

    private static final Projectile thrownAxe = new Projectile(1193, 85, 30, 30, 15, 8, 0, 5);
    private static final Projectile javelin = new Projectile(1192, 99, 30, 30, 10, 3, 0, 5);
    private static final Projectile magic = new Projectile(1192, 80, 30, 30, 0, 3, 0, 5);
    private static final SoundEffect attackSound = new SoundEffect(2699, 10, 0);

    @Override
    public int attack(final Entity target) {
        final Aviansie npc = this;
        World.sendSoundEffect(getMiddleLocation(), attackSound);
        if (ArrayUtils.contains(javelinAviansie, getId())) {
            npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
            delayHit(npc, World.sendProjectile(npc, target, npc.getId() == 3168 ? magic : javelin), target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), npc.getId() == 3168 ? MAGIC : RANGED, target), npc.getId() == 3168 ? HitType.MAGIC : HitType.RANGED));
        } else {
            npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
            delayHit(npc, World.sendProjectile(npc, target, thrownAxe), target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
