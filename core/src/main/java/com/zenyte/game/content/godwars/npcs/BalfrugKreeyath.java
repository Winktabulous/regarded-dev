package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 21/08/2019 00:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BalfrugKreeyath extends GodwarsBossMinion implements Spawnable, CombatScript {
    public BalfrugKreeyath(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    private static final Projectile projectile = new Projectile(1227, 50, 20, 30, 5, 18, 0, 5);
    private static final Graphics targetGraphics = new Graphics(157, 0, 90);
    private static final Graphics splashGraphics = new Graphics(85, 0, 124);
    private static final SoundEffect castSound = new SoundEffect(3868, 10, 0);
    private static final SoundEffect hitSound = new SoundEffect(3873, 10, -1);

    @Override
    public int attack(final Entity target) {
        final BalfrugKreeyath npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        World.sendSoundEffect(getMiddleLocation(), castSound);
        final int preciseDelay = projectile.getProjectileDuration(npc.getMiddleLocation(), target);
        World.sendSoundEffect(new Location(target.getLocation()), hitSound.withDelay(preciseDelay));
        delayHit(npc, World.sendProjectile(npc, target, projectile), target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC).onLand(hit -> target.setGraphics(hit.getDamage() > 0 ? targetGraphics : splashGraphics)));
        return npc.getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 3132;
    }
}
