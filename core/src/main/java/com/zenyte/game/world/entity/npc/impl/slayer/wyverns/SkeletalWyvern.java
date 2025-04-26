package com.zenyte.game.world.entity.npc.impl.slayer.wyverns;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Tommeh | 17 apr. 2018 | 19:44:29
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SkeletalWyvern extends Wyvern {
    private static final Animation icyBreathAnim = new Animation(2985);
    private static final Graphics icyBreathGfx = new Graphics(501);
    private static final Animation rangedAnim = new Animation(2989);
    private static final Graphics rangedGfx = new Graphics(499);
    private static final Projectile rangedProjectile = new Projectile(500, 75, 35, 50, 0, 8, 0, 5);
    private static final AttackType[] ATTACK_STYLES = {AttackType.SLASH, AttackType.MAGIC, AttackType.RANGED};

    public SkeletalWyvern(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(final Entity target) {
        switch (getCombatDefinitions().getAttackStyle()) {
        case MAGIC: 
            magicAttack(target);
            break;
        case RANGED: 
            attackSound();
            setAnimation(rangedAnim);
            setGraphics(rangedGfx);
            delayHit(this, World.sendProjectile(this, target, rangedProjectile), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED).onLand(h -> World.sendSoundEffect(new Location(target.getLocation()), impactSound)));
            break;
        default: 
            setAnimation(getCombatDefinitions().getAttackAnim());
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
            break;
        }
        final AttackType currentStyle = combatDefinitions.getAttackStyle();
        final ObjectArrayList<AttackType> styles = new ObjectArrayList<AttackType>(Arrays.asList(ATTACK_STYLES));
        styles.remove(currentStyle);
        combatDefinitions.setAttackStyle(styles.get(Utils.random(styles.size() - 1)));
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(FaladorDiary.KILL_SKELETAL_WYVERN);
            player.getCombatAchievements().complete(CAType.A_FROZEN_FOE_FROM_THE_PAST);
        }
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equals("skeletal wyvern");
    }

    @Override
    protected String notificationName(@NotNull final Player player) {
        return "skeletal wyvern";
    }

    @Override
    protected Animation getIcyBreathAnim() {
        return icyBreathAnim;
    }

    @Override
    protected Graphics getIcyBreathGfx() {
        return icyBreathGfx;
    }
}
