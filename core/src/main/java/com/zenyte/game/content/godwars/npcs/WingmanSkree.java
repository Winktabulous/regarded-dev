package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

/**
 * @author Christopher
 * @since 3/5/2020
 */
public class WingmanSkree extends GodwarsBossMinion implements Spawnable, CombatScript {
    private static final Animation attackAnim = new Animation(6958);
    private static final Projectile darts = new Projectile(1201, 99, 30, 30, 10, 3, 0, 5);
    private static final SoundEffect attackSound = new SoundEffect(3868, 10, 0);
    private static final SoundEffect hitSound = new SoundEffect(3873, 10, -1);

    public WingmanSkree(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(final Entity target) {
        setAnimation(attackAnim);
        World.sendSoundEffect(getMiddleLocation(), attackSound);
        World.sendSoundEffect(new Location(target.getLocation()), hitSound.withDelay(darts.getProjectileDuration(getMiddleLocation(), target)));
        delayHit(this, World.sendProjectile(this, target, darts), target, magic(target, 16));
        return 5;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 3163;
    }
}