package com.zenyte.game.world.entity.player.action.combat.magic.spelleffect;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.content.boss.phantommuspah.PhantomMuspah;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentUtils;

/**
 * @author Kris | 20/03/2019 22:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BindEffect implements SpellEffect {

    private final int tickDuration;
    private final boolean snareSpell;

    public BindEffect(final int tickDuration) {
        this(tickDuration, false);
    }

    public BindEffect(final int tickDuration, final boolean snareSpell) {
        this.tickDuration = tickDuration;
        this.snareSpell = snareSpell;
    }

    @Override
    public void spellEffect(final Entity player, final Entity target, final int damage) {
        int duration = tickDuration;
        if (player instanceof final Player p) {
            final int weaponId = p.getEquipment().getId(EquipmentSlot.WEAPON);
            if (snareSpell) {
                int swampBarkPieces = EquipmentUtils.swampBarkPces(p);
                if (swampBarkPieces > 0)
                    duration += swampBarkPieces;
            }
            else {
                if (weaponId == ItemId.ANCIENT_SCEPTRE || weaponId == ItemId.ICE_ANCIENT_SCEPTRE_28262)
                    duration = (int) (duration * 1.1);

                if (target instanceof PhantomMuspah)
                    duration = (int) (duration * 0.66);
            }
        }
        final int finalDuration = duration;
        if (target.freezeWithNotification(finalDuration, 5))
            if (target instanceof Player playerAsTarget)
                PlayerAttributesKt.setFreezeCaster(playerAsTarget, player);
    }
}
