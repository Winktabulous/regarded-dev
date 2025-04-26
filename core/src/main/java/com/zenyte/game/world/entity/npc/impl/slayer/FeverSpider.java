package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

public class FeverSpider extends NPC implements CombatScript, Spawnable {
    public FeverSpider(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(final Entity target) {
        final FeverSpider npc = this;
        if (target instanceof Player) {
            final Player player = (Player) target;
            if (!player.getToxins().isDiseased() && player.getEquipment().getId(EquipmentSlot.HANDS) != ItemId.SLAYER_GLOVES_6720) {
                player.getToxins().applyToxin(Toxins.ToxinType.DISEASE, 10, false, this);
            }
        }
        attackSound();
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        return npc.getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 626;
    }
}
