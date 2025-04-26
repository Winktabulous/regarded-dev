package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.boons.impl.Pyromaniac;
import com.zenyte.game.content.skills.firemaking.BonfireAction;
import com.zenyte.game.content.skills.firemaking.Firemaking;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;

public class AnyLogsOnFlamesBoonAction implements ItemOnObjectAction {
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if(!player.getBoonManager().hasBoon(Pyromaniac.class)) {
            player.sendMessage(Pyromaniac.NO_PERK_MESSAGE);
            return;
        }
        Firemaking logs = null;
        for (Firemaking value : Firemaking.VALUES) {
            if(item.getId() == value.getLogs().getId())
                logs = value;
        }
        if(logs != null && logs.getLevel() > player.getSkills().getLevel(SkillConstants.FIREMAKING)) {
            player.sendMessage("You lack the required firemaking level to light these.");
            return;
        }
        if(logs != null)
            player.getActionManager().setAction(new BonfireAction(logs));
        else
            player.sendMessage("You don't have any logs to add to the fire!");
    }

    @Override
    public Object[] getItems() {
        return Firemaking.MAP.keySet().toArray(new Integer[0]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{"Fire", "Stove", "Bonfire", "Oven", "Furnace", "Brazier"};
    }
}
