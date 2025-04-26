package com.zenyte.game.world.entity.npc.impl;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 24/01/2019 18:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ThrowerTroll extends NPC implements CombatScript, Spawnable {
    public ThrowerTroll(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    private static final Projectile PROJ = new Projectile(304, 30, 26, 30, 10, 3, 0, 5);
    private static final RSPolygon[] validLocations = new RSPolygon[] {new RSPolygon(new int[][] {{2876, 3696}, {2876, 3691}, {2887, 3700}, {2895, 3701}, {2903, 3698}, {2908, 3693}, {2911, 3698}, {2901, 3705}, {2886, 3707}}), new RSPolygon(new int[][] {{2834, 3592}, {2843, 3592}, {2853, 3602}, {2870, 3602}, {2872, 3600}, {2877, 3601}, {2871, 3607}, {2849, 3607}, {2837, 3598}})};
    private static final SoundEffect impactSound = new SoundEffect(860, 10, 0);

    @Override
    public int attack(Entity target) {
        animate();
        attackSound();
        delayHit(this, World.sendProjectile(this, target, PROJ), target, ranged(target, combatDefinitions.getMaxHit()).onLand(h -> World.sendSoundEffect(new Location(target.getLocation()), impactSound)));
        return combatDefinitions.getAttackSpeed();
    }

    @Override
    public boolean isAcceptableTarget(final Entity entity) {
        for (final RSPolygon polygon : validLocations) {
            if (polygon.contains(entity.getLocation())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isMovableEntity() {
        return false;
    }

    @Override
    public boolean validate(int id, String name) {
        return (id >= 931 && id <= 935) || (id >= 4135 && id <= 4139);
    }
}
