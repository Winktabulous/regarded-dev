package com.zenyte.game.content.treasuretrails.npcs;

import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SaradominWizard extends TreasureGuardian implements CombatScript {
    private static final Animation daggerAnimation = new Animation(376);
    private static final ForceTalk forceTalk = new ForceTalk("For Saradomin!");

    public SaradominWizard(@NotNull Player owner, @NotNull Location tile) {
        super(owner, tile, 2955);
        setForceTalk(forceTalk);
    }

    @Override
    public int attack(Entity target) {
        AttackType style = isWithinMeleeDistance(this, target) ? MELEE : AttackType.MAGIC;
        if (target instanceof Player) {
            final Player player = (Player) target;
            if (player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
                if (style != AttackType.MELEE) {
                    resetWalkSteps();
                    calcFollow(target, -1, true, false, false);
                    if (hasWalkSteps()) {
                        return 0;
                    }
                }
            } else if (player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
                style = AttackType.MAGIC;
            }
        }
        switch (style) {
        case MELEE: 
            delayHit(0, target, new Hit(this, getRandomMaxHit(this, 14, AttackType.STAB, target), HitType.MELEE));
            setAnimation(daggerAnimation);
            break;
        case MAGIC: 
            useSpell(CombatSpell.SARADOMIN_STRIKE, target, combatDefinitions.getMaxHit());
            break;
        }
        processPoison(this, target);
        return combatDefinitions.getAttackSpeed();
    }
}
