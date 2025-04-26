package com.zenyte.game.content.treasuretrails.npcs;

import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.AmmunitionDefinitions;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Kris | 12/04/2019 20:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AncientWizard extends TreasureGuardian implements CombatScript {
    private static final ForceTalk chat = new ForceTalk("Perish in agony!");

    public AncientWizard(@NotNull final Player owner, @NotNull final Location tile, final int id) {
        super(owner, tile, id);
        setForceTalk(chat);
    }

    @Override
    boolean isOutOfReach() {
        return false;
    }

    @Override
    public void processNPC() {
        block:
        if (++ticks > 50 || !owner.getLocation().withinDistance(this, 15)) {
            final Object list = owner.getTemporaryAttributes().get("TT Guardian NPC");
            if (list instanceof Collection) {
                for (final Object n : ((Collection) list)) {
                    if (n instanceof TreasureGuardian) {
                        if (((TreasureGuardian) n).ticks < 50 && owner.getLocation().withinDistance((TreasureGuardian) n, 15)) {
                            break block;
                        }
                    }
                }
                for (final Object n : ((Collection) list)) {
                    if (n instanceof TreasureGuardian) {
                        ((TreasureGuardian) n).finish();
                    }
                }
                owner.getTemporaryAttributes().remove("TT Guardian NPC");
            }
            return;
        }
        super.processNPC();
        if (isUnderCombat()) {
            ticks = 0;
        }
    }

    @Override
    public int attack(final Entity target) {
        switch (getId()) {
        case 7307: 
            {
                final CombatSpell spell = CombatSpell.BLOOD_BLITZ;
                setAnimation(spell.getAnimation());
                final Projectile proj = spell.getProjectile();
                final int time = proj.getProjectileDuration(getLocation(), target);
                target.setGraphics(new Graphics(spell.getHitGfx().getId(), time, 0));
                final int damage = getRandomMaxHit(this, 18, MAGIC, target);
                spell.getEffect().spellEffect(this, target, damage);
                World.sendSoundEffect(target.getLocation(), new SoundEffect(spell.getHitSound().getId(), 10, time));
                delayHit(this, World.sendProjectile(this, target, proj), target, new Hit(this, damage, HitType.MAGIC));
                break;
            }
        case 7308: 
            {
                final AmmunitionDefinitions defs = AmmunitionDefinitions.RUNITE_BOLT;
                setAnimation(new Animation(7552));
                final Projectile proj = defs.getProjectile();
                delayHit(this, World.sendProjectile(this, target, proj), target, new Hit(this, getRandomMaxHit(this, 23, RANGED, target), HitType.RANGED));
                break;
            }
        default: 
            {
                setAnimation(new Animation(376));
                delayHit(0, target, melee(target, 36));
                break;
            }
        }
        return combatDefinitions.getAttackSpeed();
    }
}
