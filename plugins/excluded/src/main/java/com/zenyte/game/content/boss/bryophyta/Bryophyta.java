package com.zenyte.game.content.boss.bryophyta;

import com.near_reality.game.content.custom.SlayerHelmetEffects;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.ReceivedDamage;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kotlin.Pair;

/**
 * @author Tommeh | 17/05/2019 | 14:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class Bryophyta extends NPC implements CombatScript {
    private static final Animation AUTO_ATTACK_ANIM = new Animation(4658);
    private static final Animation MAGIC_ATTACK_ANIM = new Animation(7173);
    private static final Projectile MAGIC_ATTACK_PROJ = new Projectile(139, 50, 33, 46, 23, -5, 64, 10);
    private static final Graphics MAGIC_ATTACK_ONHIT_GFX = new Graphics(140, 0, 124);
    private static final Graphics SPLASH_GFX = new Graphics(85, 0, 124);
    private final BryophytaInstance instance;

    public Bryophyta(final Location tile, final BryophytaInstance instance) {
        super(8195, tile, Direction.SOUTH, 3);
        this.instance = instance;
        setSpawned(true);
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        super.handleIngoingHit(hit);
    }

    @Override
    public float getXpModifier(final Hit hit) {
        return 1;
    }

    @Override
    public int attack(Entity target) {
        if (isWithinMeleeDistance(this, target)) {
            if (Utils.random(1) == 0) {
                setAnimation(AUTO_ATTACK_ANIM);
                delayHit(this, 1, target, new Hit(this, getRandomMaxHit(this, 16, MELEE, target), HitType.MELEE));
            } else {
                return distanceAttack(target);
            }
        } else {
            return distanceAttack(target);
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    private int distanceAttack(final Entity target) {
        setAnimation(MAGIC_ATTACK_ANIM);
        World.sendProjectile(this, target, MAGIC_ATTACK_PROJ);
        delayHit(MAGIC_ATTACK_PROJ.getTime(this, target), target, new Hit(this, getRandomMaxHit(this, 16, MAGIC, target), HitType.MAGIC).onLand(hit -> {
            if (Utils.random(3) == 0) {
                target.getToxins().applyToxin(Toxins.ToxinType.POISON, 8, this);
            }
            if (hit.getDamage() == 0) {
                target.setGraphics(SPLASH_GFX);
            } else {
                target.setGraphics(MAGIC_ATTACK_ONHIT_GFX);
            }
        }));

        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
    }

    @Override
    protected void onFinish(Entity source) {
        if (source instanceof final Player player) {
            ObjectArrayList<ReceivedDamage> damage = getReceivedDamage().get(new Pair<>(player.getUsername(), player.getGameMode()));
            if (damage != null) {
                ReceivedDamage lastHit = damage.get(damage.size() - 1);
                if (lastHit.getType() == HitType.VENOM || lastHit.getType() == HitType.POISON) {
                    player.getCombatAchievements().complete(CAType.A_SLOW_DEATH);
                }
            }

            if (player.getVariables().getTime(TickVariable.POISON_IMMUNITY) > 0 || CombatUtilities.isWearingSerpentineHelmet(player) || SlayerHelmetEffects.INSTANCE.immuneToPoison(player, this)) {
                player.getCombatAchievements().complete(CAType.PREPARATION_IS_KEY);
            }

            if (player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
                player.getCombatAchievements().complete(CAType.PROTECTION_FROM_MOSS);
            }
        }

        super.onFinish(source);

        // KC tasks after finish, because KC is done there.
        if (source instanceof final Player player) {
            player.getCombatAchievements().checkKcTask("bryophyta", 1, CAType.BRYOPHYTA_NOVICE);
            player.getCombatAchievements().checkKcTask("bryophyta", 5, CAType.BRYOPHYTA_CHAMPION);
        }
    }


}
