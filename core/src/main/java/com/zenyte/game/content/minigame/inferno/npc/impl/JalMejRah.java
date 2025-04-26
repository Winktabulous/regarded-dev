package com.zenyte.game.content.minigame.inferno.npc.impl;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combatdefs.StatType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 29/11/2019 | 19:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JalMejRah extends InfernoNPC {
    private static final Projectile attackProjectile = new Projectile(1382, 42, 38, 15, 17, 30, 0, 5);
    private static final Animation attackAnimation = new Animation(7578);
    private static final int[] drainableStats = {SkillConstants.ATTACK, SkillConstants.DEFENCE, SkillConstants.STRENGTH, SkillConstants.RANGED, SkillConstants.MAGIC, SkillConstants.HITPOINTS};
    /**
     * The stat types correspond 1:1 to {@link JalMejRah#drainableStats}.
     */
    private static final StatType[] statTypes = new StatType[] {StatType.ATTACK, StatType.DEFENCE, StatType.STRENGTH, StatType.RANGED, StatType.MAGIC, null};

    public JalMejRah(final Location location, final Inferno inferno) {
        super(7692, location, inferno);
        setAttackDistance(3);
    }

    private int sapCount;

    @Override
    public boolean isRevivable() {
        return true;
    }

    @Override
    public int attack(final Player player) {
        setAnimation(attackAnimation);
        World.sendProjectile(this, player, attackProjectile);
        WorldTasksManager.schedule(() -> {
            if (sapCount < 10 && Utils.random(9 + sapCount) == 0) {
                sapCount++;
                for (int i = 0; i < drainableStats.length; i++) {
                    final int skill = drainableStats[i];
                    if (skill == SkillConstants.HITPOINTS) {
                        final int drained = Math.min(sapCount, 4);
                        player.removeHitpoints(new Hit(this, 1, HitType.REGULAR));//Always drains the player for just one hitpoint.
                        player.applyHit(new Hit(this, 0, HitType.MISSED));
                        setHitpoints(getHitpoints() + drained);//Can heal above its maximum health.
                    } else {
                        final int currentLevel = player.getSkills().getLevel(skill);
                        player.getSkills().setLevel(skill, Math.max(0, currentLevel - 1));
                        final StatType statType = statTypes[i];
                        if (statType == null) {
                            continue;
                        }
                        combatDefinitions.getStatDefinitions().set(statType, combatDefinitions.getStatDefinitions().get(statType) + 1);
                    }
                }
            } else {
                delayHit(-1, player, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, player), HitType.RANGED));
            }
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() - 3);
        }, attackProjectile.getTime(this, player));
        return combatDefinitions.getAttackSpeed();
    }
}
