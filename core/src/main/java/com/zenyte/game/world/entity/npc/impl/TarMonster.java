package com.zenyte.game.world.entity.npc.impl;

import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

/**
 * @author Christopher
 * @since 3/21/2020
 */
public class TarMonster extends NPC implements CombatScript {
    private static final Animation attackAnim = new Animation(7678);
    private static final Projectile attackProj = new Projectile(1404, 32, 64, 35, 15, 16, 0, 5);

    public TarMonster(int id, Location tile, boolean spawned) {
        super(id, tile, spawned);
    }

    @Override
    public int attack(Entity target) {
        setAnimation(attackAnim);
        final int delay = World.sendProjectile(this, target, attackProj);
        delayHit(delay, target, new Hit(this, getRandomMaxHit(this, 7, RANGED, target), HitType.DEFAULT));
        return 5;
    }
}
