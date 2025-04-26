package com.zenyte.game.world.entity.player.action.combat.melee;

import com.near_reality.game.world.entity.player.action.combat.ISpecialAttack;
import com.zenyte.game.model.item.degradableitems.DegradeType;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;
import com.zenyte.game.world.entity.player.action.combat.SpecialAttack;
import com.zenyte.game.world.entity.player.action.combat.SpecialAttackScript;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;

/**
 * @author Kris | 17/06/2019 16:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DragonSpearCombat extends MeleeCombat {
    public DragonSpearCombat(final Entity target) {
        super(target);
    }

    @Override
    public int processAfterMovement() {
        if (!isWithinAttackDistance()) {
            return 0;
        }
        if (!canAttack()) {
            return -1;
        }
        final RegionArea area = player.getArea();
        if (area instanceof PlayerCombatPlugin) {
            ((PlayerCombatPlugin) area).onAttack(player, target, "Melee", null, false);
        }
        if (player.getCombatDefinitions().isUsingSpecial()) {
            final ISpecialAttack spec = SpecialAttack.SPECIAL_ATTACKS.get(player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot()));
            if (spec == SpecialAttack.SHOVE) {
                if (target instanceof Player) {
                    final long lastShoveTick = ((Player) target).getNumericTemporaryAttribute("Last shove push").longValue();
                    if (WorldThread.WORLD_CYCLE < lastShoveTick) {
                        return 0;
                    }
                } else if (target instanceof NPC) {
                    if (target.getSize() > 1) {
                        player.sendMessage("That creature is too large to knock back!");
                        return -1;
                    }
                }
            }
        }
        addAttackedByDelay(player, target);
        final int delay = special();
        if (delay != -2) {
            return delay == SpecialAttackScript.WEAPON_SPEED ? getSpeed() : delay;
        }
        sendSoundEffect();
        final Hit hit = getHit(player, target, 1, 1, 1, false);
        extra(hit);
        addPoisonTask(hit.getDamage(), 0);
        delayHit(0, hit);
        animate();
        player.getChargesManager().removeCharges(DegradeType.OUTGOING_HIT);
        resetFlag();
        checkIfShouldTerminate(HitType.MELEE);
        return getSpeed();
    }
}
