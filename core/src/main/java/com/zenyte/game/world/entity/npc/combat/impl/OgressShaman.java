package com.zenyte.game.world.entity.npc.combat.impl;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.HitEntry;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combat.Default;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.action.combat.magic.spelleffect.SpellEffect;

/**
 * @author Kris | 18/08/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OgressShaman extends NPC implements CombatScript, Spawnable {
    public OgressShaman(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.attackDistance = 4;
    }

    @Override
    public int attack(Entity target) {
        animate();
        final CombatSpell spell = CombatSpell.EARTH_BOLT;
        final Projectile projectile = spell.getProjectile();
        int delay = 1;
        int clientDelay = 30;
        if (projectile != null) {
            clientDelay = projectile.getProjectileDuration(getLocation(), target.getLocation());
            if (projectile.getGraphicsId() != -1) {
                delay = World.sendProjectile(this, target, projectile);
            } else {
                delay = projectile.getTime(this, target);
            }
        }
        final SoundEffect sound = spell.getHitSound();
        final Graphics gfx = spell.getHitGfx();
        final SpellEffect effect = spell.getEffect();
        final HitEntry hitEntry = new HitEntry(this, delay, magic(target, combatDefinitions.getMaxHit()));
        target.appendHitEntry(hitEntry);
        if (hitEntry.getHit().getDamage() > 0) {
            if (gfx != null) {
                target.setGraphics(new Graphics(gfx.getId(), clientDelay, gfx.getHeight()));
            }
            if (sound != null) {
                World.sendSoundEffect(target.getLocation(), new SoundEffect(sound.getId(), sound.getRadius(), clientDelay));
            }
            delayHit(delay, target, hitEntry.getHit().onLand(hit -> {
                if (effect != null) {
                    effect.spellEffect(this, target, hit.getDamage());
                }
            }));
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
        return id == NpcId.OGRESS_SHAMAN || id == NpcId.OGRESS_SHAMAN_7992;
    }
}
