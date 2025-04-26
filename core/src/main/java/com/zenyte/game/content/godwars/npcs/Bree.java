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
 * @author Kris | 21/08/2019 01:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Bree extends GodwarsBossMinion implements Spawnable, CombatScript {
    public Bree(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 2208;
    }

    private static final Projectile projectile = new Projectile(12, 52, 30, 40, 15, 10, 64, 5);
    private static final Graphics attackGraphics = new Graphics(1185);
    private static final SoundEffect attackSound = new SoundEffect(3841, 10, 0);

    @Override
    public int attack(final Entity target) {
        final Bree npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        npc.setGraphics(attackGraphics);
        delayHit(npc, World.sendProjectile(npc, target, projectile), target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
        World.sendSoundEffect(getMiddleLocation(), attackSound);
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
