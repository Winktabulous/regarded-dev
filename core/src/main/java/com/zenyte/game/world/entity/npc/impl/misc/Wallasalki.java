package com.zenyte.game.world.entity.npc.impl.misc;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.HitEntry;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combat.Default;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.action.combat.magic.spelleffect.SpellEffect;

/**
 * @author Kris | 17/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Wallasalki extends NPC implements CombatScript, Spawnable {
    private static final Projectile projectile = new Projectile(162, 22, 31, 30, 23, -5, 64, 10);

    public Wallasalki(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(Entity target) {
        animate();
        attackSound();
        final Wallasalki npc = this;
        int delay = 1;
        int clientDelay = 30;
        final CombatSpell spell = CombatSpell.WATER_WAVE;
        if (projectile != null) {
            clientDelay = projectile.getProjectileDuration(npc.getLocation(), target.getLocation());
            if (projectile.getGraphicsId() != -1) {
                delay = World.sendProjectile(npc, target, projectile);
            } else {
                delay = projectile.getTime(npc, target);
            }
        }
        final SoundEffect sound = spell.getHitSound();
        final Graphics gfx = spell.getHitGfx();
        final SpellEffect effect = spell.getEffect();
        final HitEntry hitEntry = new HitEntry(npc, delay, magic(target, combatDefinitions.getMaxHit()));
        target.appendHitEntry(hitEntry);//Processes prayer modifications, we need to know the max hit post-prayer to know whether it's a splash or not.
        delayHit(delay, target, hitEntry.getHit().onLand(hit -> {
            if (effect != null) {
                effect.spellEffect(npc, target, hit.getDamage());
            }
        }));
        if (hitEntry.getHit().getDamage() > 0) {
            if (gfx != null) {
                target.setGraphics(new Graphics(gfx.getId(), clientDelay, gfx.getHeight()));
            }
            if (sound != null) {
                World.sendSoundEffect(target.getLocation(), new SoundEffect(sound.getId(), sound.getRadius(), clientDelay));
            }
        } else {
            if (sound != null) {
                World.sendSoundEffect(target.getLocation(), new SoundEffect(227, 10, clientDelay));
            }
            if (gfx != null) {
                target.setGraphics(new Graphics(Default.SPLASH_GRAPHICS.getId(), clientDelay, Default.SPLASH_GRAPHICS.getHeight()));
            }
        }
        return combatDefinitions.getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 5938 || id == 5939;
    }
}
