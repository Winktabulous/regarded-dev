package com.zenyte.game.content.skills.afk.impl;

import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.content.skills.construction.objects.superiorgarden.TopiaryBush;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.plugins.dialogue.PlainChat;

public class AfkFarmingAction extends BasicAfkAction {


    public AfkFarmingAction() {
    }

    @Override
    public Animation actionAnimation() {
        if (player.getInventory().containsItem(7409, 1) || player.getEquipment().getId(EquipmentSlot.WEAPON) == 7409)
            return TopiaryBush.TopiarySelectionD.MAGIC_SECATEURS_ANIM;
        else
            return TopiaryBush.TopiarySelectionD.SECATEURS_ANIM;
    }

    @Override
    public int getSkill() {
        return SkillConstants.FARMING;
    }

    @Override
    public String getMessage() {
        return "You prune the plant";
    }

    @Override
    public boolean hasRequiredItem() {
        if (!player.getInventory().containsItem(5329, 1) && !player.getInventory().containsItem(7409, 1) && player.getEquipment().getId(EquipmentSlot.WEAPON) != 7409) {
            player.getDialogueManager().start(new PlainChat(player, "You need some secateurs to clip the bush."));
            return false;
        }
        return true;
    }
}
