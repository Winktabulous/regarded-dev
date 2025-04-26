package com.zenyte.game.content.custom;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.degradableitems.DegradeType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;
import com.zenyte.game.world.entity.player.action.combat.SpecialAttackScript;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;

public class LimeWhipCombat extends MeleeCombat {

    private boolean activated = false;
    public LimeWhipCombat(Entity target) {
        super(target);
    }

    @Override
    protected int getSpeed() {
        return 2;
    }

    @Override
    protected void animate() {
        if (activated) {
            player.setAnimation(new Animation(1658));
            activated = false;
        } else
            super.animate();
    }
}
