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
 * @author Kris | 21/08/2019 01:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SergeantSteelwill extends GodwarsBossMinion implements Spawnable, CombatScript {
    public SergeantSteelwill(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 2217;
    }

    private static final Projectile projectile = new Projectile(1217, 31, 20, 30, 5, 18, 0, 5);
    private static final Graphics attackGraphics = new Graphics(1216);
    private static final Graphics targetGraphics = new Graphics(166, 0, 90);
    private static final Graphics splashGraphics = new Graphics(85, 0, 124);
    private static final SoundEffect attackSound = new SoundEffect(3863, 10, 0);
    private static final SoundEffect hitSound = new SoundEffect(3873, 10, -1);
    private static final SoundEffect splashSound = new SoundEffect(227, 10, -1);

    @Override
    public int attack(final Entity target) {
        final SergeantSteelwill npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        npc.setGraphics(attackGraphics);
        World.sendSoundEffect(getMiddleLocation(), attackSound);
        final Hit hit = new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC);
        delayHit(npc, World.sendProjectile(npc, target, projectile), target, hit);
        final int delay = projectile.getProjectileDuration(getMiddleLocation(), target);
        World.sendSoundEffect(new Location(target.getLocation()), (hit.getDamage() == 0 ? splashSound : hitSound).withDelay(delay));
        final Graphics gfxType = hit.getDamage() == 0 ? splashGraphics : targetGraphics;
        target.setGraphics(new Graphics(gfxType.getId(), delay, gfxType.getHeight()));
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
